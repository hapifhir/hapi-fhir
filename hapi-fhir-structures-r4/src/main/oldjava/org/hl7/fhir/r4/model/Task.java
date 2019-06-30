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
 * A task to be performed.
 */
@ResourceDef(name="Task", profile="http://hl7.org/fhir/StructureDefinition/Task")
public class Task extends DomainResource {

    public enum TaskStatus {
        /**
         * The task is not yet ready to be acted upon.
         */
        DRAFT, 
        /**
         * The task is ready to be acted upon and action is sought.
         */
        REQUESTED, 
        /**
         * A potential performer has claimed ownership of the task and is evaluating whether to perform it.
         */
        RECEIVED, 
        /**
         * The potential performer has agreed to execute the task but has not yet started work.
         */
        ACCEPTED, 
        /**
         * The potential performer who claimed ownership of the task has decided not to execute it prior to performing any action.
         */
        REJECTED, 
        /**
         * The task is ready to be performed, but no action has yet been taken.  Used in place of requested/received/accepted/rejected when request assignment and acceptance is a given.
         */
        READY, 
        /**
         * The task was not completed.
         */
        CANCELLED, 
        /**
         * The task has been started but is not yet complete.
         */
        INPROGRESS, 
        /**
         * The task has been started but work has been paused.
         */
        ONHOLD, 
        /**
         * The task was attempted but could not be completed due to some error.
         */
        FAILED, 
        /**
         * The task has been completed.
         */
        COMPLETED, 
        /**
         * The task should never have existed and is retained only because of the possibility it may have used.
         */
        ENTEREDINERROR, 
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
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("failed".equals(codeString))
          return FAILED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
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
            case CANCELLED: return "cancelled";
            case INPROGRESS: return "in-progress";
            case ONHOLD: return "on-hold";
            case FAILED: return "failed";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
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
            case CANCELLED: return "http://hl7.org/fhir/task-status";
            case INPROGRESS: return "http://hl7.org/fhir/task-status";
            case ONHOLD: return "http://hl7.org/fhir/task-status";
            case FAILED: return "http://hl7.org/fhir/task-status";
            case COMPLETED: return "http://hl7.org/fhir/task-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/task-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The task is not yet ready to be acted upon.";
            case REQUESTED: return "The task is ready to be acted upon and action is sought.";
            case RECEIVED: return "A potential performer has claimed ownership of the task and is evaluating whether to perform it.";
            case ACCEPTED: return "The potential performer has agreed to execute the task but has not yet started work.";
            case REJECTED: return "The potential performer who claimed ownership of the task has decided not to execute it prior to performing any action.";
            case READY: return "The task is ready to be performed, but no action has yet been taken.  Used in place of requested/received/accepted/rejected when request assignment and acceptance is a given.";
            case CANCELLED: return "The task was not completed.";
            case INPROGRESS: return "The task has been started but is not yet complete.";
            case ONHOLD: return "The task has been started but work has been paused.";
            case FAILED: return "The task was attempted but could not be completed due to some error.";
            case COMPLETED: return "The task has been completed.";
            case ENTEREDINERROR: return "The task should never have existed and is retained only because of the possibility it may have used.";
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
            case CANCELLED: return "Cancelled";
            case INPROGRESS: return "In Progress";
            case ONHOLD: return "On Hold";
            case FAILED: return "Failed";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
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
        if ("cancelled".equals(codeString))
          return TaskStatus.CANCELLED;
        if ("in-progress".equals(codeString))
          return TaskStatus.INPROGRESS;
        if ("on-hold".equals(codeString))
          return TaskStatus.ONHOLD;
        if ("failed".equals(codeString))
          return TaskStatus.FAILED;
        if ("completed".equals(codeString))
          return TaskStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return TaskStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown TaskStatus code '"+codeString+"'");
        }
        public Enumeration<TaskStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<TaskStatus>(this);
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
        if ("cancelled".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.CANCELLED);
        if ("in-progress".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.INPROGRESS);
        if ("on-hold".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.ONHOLD);
        if ("failed".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.FAILED);
        if ("completed".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.ENTEREDINERROR);
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
      if (code == TaskStatus.CANCELLED)
        return "cancelled";
      if (code == TaskStatus.INPROGRESS)
        return "in-progress";
      if (code == TaskStatus.ONHOLD)
        return "on-hold";
      if (code == TaskStatus.FAILED)
        return "failed";
      if (code == TaskStatus.COMPLETED)
        return "completed";
      if (code == TaskStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(TaskStatus code) {
      return code.getSystem();
      }
    }

    public enum TaskIntent {
        /**
         * The intent is not known.  When dealing with Task, it's not always known (or relevant) how the task was initiated - i.e. whether it was proposed, planned, ordered or just done spontaneously.
         */
        UNKNOWN, 
        /**
         * null
         */
        PROPOSAL, 
        /**
         * null
         */
        PLAN, 
        /**
         * null
         */
        ORDER, 
        /**
         * null
         */
        ORIGINALORDER, 
        /**
         * null
         */
        REFLEXORDER, 
        /**
         * null
         */
        FILLERORDER, 
        /**
         * null
         */
        INSTANCEORDER, 
        /**
         * null
         */
        OPTION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TaskIntent fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if ("proposal".equals(codeString))
          return PROPOSAL;
        if ("plan".equals(codeString))
          return PLAN;
        if ("order".equals(codeString))
          return ORDER;
        if ("original-order".equals(codeString))
          return ORIGINALORDER;
        if ("reflex-order".equals(codeString))
          return REFLEXORDER;
        if ("filler-order".equals(codeString))
          return FILLERORDER;
        if ("instance-order".equals(codeString))
          return INSTANCEORDER;
        if ("option".equals(codeString))
          return OPTION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TaskIntent code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNKNOWN: return "unknown";
            case PROPOSAL: return "proposal";
            case PLAN: return "plan";
            case ORDER: return "order";
            case ORIGINALORDER: return "original-order";
            case REFLEXORDER: return "reflex-order";
            case FILLERORDER: return "filler-order";
            case INSTANCEORDER: return "instance-order";
            case OPTION: return "option";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case UNKNOWN: return "http://hl7.org/fhir/task-intent";
            case PROPOSAL: return "http://hl7.org/fhir/request-intent";
            case PLAN: return "http://hl7.org/fhir/request-intent";
            case ORDER: return "http://hl7.org/fhir/request-intent";
            case ORIGINALORDER: return "http://hl7.org/fhir/request-intent";
            case REFLEXORDER: return "http://hl7.org/fhir/request-intent";
            case FILLERORDER: return "http://hl7.org/fhir/request-intent";
            case INSTANCEORDER: return "http://hl7.org/fhir/request-intent";
            case OPTION: return "http://hl7.org/fhir/request-intent";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case UNKNOWN: return "The intent is not known.  When dealing with Task, it's not always known (or relevant) how the task was initiated - i.e. whether it was proposed, planned, ordered or just done spontaneously.";
            case PROPOSAL: return "";
            case PLAN: return "";
            case ORDER: return "";
            case ORIGINALORDER: return "";
            case REFLEXORDER: return "";
            case FILLERORDER: return "";
            case INSTANCEORDER: return "";
            case OPTION: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNKNOWN: return "Unknown";
            case PROPOSAL: return "proposal";
            case PLAN: return "plan";
            case ORDER: return "order";
            case ORIGINALORDER: return "original-order";
            case REFLEXORDER: return "reflex-order";
            case FILLERORDER: return "filler-order";
            case INSTANCEORDER: return "instance-order";
            case OPTION: return "option";
            default: return "?";
          }
        }
    }

  public static class TaskIntentEnumFactory implements EnumFactory<TaskIntent> {
    public TaskIntent fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return TaskIntent.UNKNOWN;
        if ("proposal".equals(codeString))
          return TaskIntent.PROPOSAL;
        if ("plan".equals(codeString))
          return TaskIntent.PLAN;
        if ("order".equals(codeString))
          return TaskIntent.ORDER;
        if ("original-order".equals(codeString))
          return TaskIntent.ORIGINALORDER;
        if ("reflex-order".equals(codeString))
          return TaskIntent.REFLEXORDER;
        if ("filler-order".equals(codeString))
          return TaskIntent.FILLERORDER;
        if ("instance-order".equals(codeString))
          return TaskIntent.INSTANCEORDER;
        if ("option".equals(codeString))
          return TaskIntent.OPTION;
        throw new IllegalArgumentException("Unknown TaskIntent code '"+codeString+"'");
        }
        public Enumeration<TaskIntent> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<TaskIntent>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("unknown".equals(codeString))
          return new Enumeration<TaskIntent>(this, TaskIntent.UNKNOWN);
        if ("proposal".equals(codeString))
          return new Enumeration<TaskIntent>(this, TaskIntent.PROPOSAL);
        if ("plan".equals(codeString))
          return new Enumeration<TaskIntent>(this, TaskIntent.PLAN);
        if ("order".equals(codeString))
          return new Enumeration<TaskIntent>(this, TaskIntent.ORDER);
        if ("original-order".equals(codeString))
          return new Enumeration<TaskIntent>(this, TaskIntent.ORIGINALORDER);
        if ("reflex-order".equals(codeString))
          return new Enumeration<TaskIntent>(this, TaskIntent.REFLEXORDER);
        if ("filler-order".equals(codeString))
          return new Enumeration<TaskIntent>(this, TaskIntent.FILLERORDER);
        if ("instance-order".equals(codeString))
          return new Enumeration<TaskIntent>(this, TaskIntent.INSTANCEORDER);
        if ("option".equals(codeString))
          return new Enumeration<TaskIntent>(this, TaskIntent.OPTION);
        throw new FHIRException("Unknown TaskIntent code '"+codeString+"'");
        }
    public String toCode(TaskIntent code) {
      if (code == TaskIntent.UNKNOWN)
        return "unknown";
      if (code == TaskIntent.PROPOSAL)
        return "proposal";
      if (code == TaskIntent.PLAN)
        return "plan";
      if (code == TaskIntent.ORDER)
        return "order";
      if (code == TaskIntent.ORIGINALORDER)
        return "original-order";
      if (code == TaskIntent.REFLEXORDER)
        return "reflex-order";
      if (code == TaskIntent.FILLERORDER)
        return "filler-order";
      if (code == TaskIntent.INSTANCEORDER)
        return "instance-order";
      if (code == TaskIntent.OPTION)
        return "option";
      return "?";
      }
    public String toSystem(TaskIntent code) {
      return code.getSystem();
      }
    }

    public enum TaskPriority {
        /**
         * The request has normal priority.
         */
        ROUTINE, 
        /**
         * The request should be actioned promptly - higher priority than routine.
         */
        URGENT, 
        /**
         * The request should be actioned as soon as possible - higher priority than urgent.
         */
        ASAP, 
        /**
         * The request should be actioned immediately - highest possible priority.  E.g. an emergency.
         */
        STAT, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TaskPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return ROUTINE;
        if ("urgent".equals(codeString))
          return URGENT;
        if ("asap".equals(codeString))
          return ASAP;
        if ("stat".equals(codeString))
          return STAT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TaskPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ROUTINE: return "routine";
            case URGENT: return "urgent";
            case ASAP: return "asap";
            case STAT: return "stat";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ROUTINE: return "http://hl7.org/fhir/request-priority";
            case URGENT: return "http://hl7.org/fhir/request-priority";
            case ASAP: return "http://hl7.org/fhir/request-priority";
            case STAT: return "http://hl7.org/fhir/request-priority";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ROUTINE: return "The request has normal priority.";
            case URGENT: return "The request should be actioned promptly - higher priority than routine.";
            case ASAP: return "The request should be actioned as soon as possible - higher priority than urgent.";
            case STAT: return "The request should be actioned immediately - highest possible priority.  E.g. an emergency.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ROUTINE: return "Routine";
            case URGENT: return "Urgent";
            case ASAP: return "ASAP";
            case STAT: return "STAT";
            default: return "?";
          }
        }
    }

  public static class TaskPriorityEnumFactory implements EnumFactory<TaskPriority> {
    public TaskPriority fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return TaskPriority.ROUTINE;
        if ("urgent".equals(codeString))
          return TaskPriority.URGENT;
        if ("asap".equals(codeString))
          return TaskPriority.ASAP;
        if ("stat".equals(codeString))
          return TaskPriority.STAT;
        throw new IllegalArgumentException("Unknown TaskPriority code '"+codeString+"'");
        }
        public Enumeration<TaskPriority> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<TaskPriority>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("routine".equals(codeString))
          return new Enumeration<TaskPriority>(this, TaskPriority.ROUTINE);
        if ("urgent".equals(codeString))
          return new Enumeration<TaskPriority>(this, TaskPriority.URGENT);
        if ("asap".equals(codeString))
          return new Enumeration<TaskPriority>(this, TaskPriority.ASAP);
        if ("stat".equals(codeString))
          return new Enumeration<TaskPriority>(this, TaskPriority.STAT);
        throw new FHIRException("Unknown TaskPriority code '"+codeString+"'");
        }
    public String toCode(TaskPriority code) {
      if (code == TaskPriority.ROUTINE)
        return "routine";
      if (code == TaskPriority.URGENT)
        return "urgent";
      if (code == TaskPriority.ASAP)
        return "asap";
      if (code == TaskPriority.STAT)
        return "stat";
      return "?";
      }
    public String toSystem(TaskPriority code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class TaskRestrictionComponent extends BackboneElement implements IBaseBackboneElement {
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
        @Description(shortDefinition="When fulfillment sought", formalDefinition="Over what time-period is fulfillment sought." )
        protected Period period;

        /**
         * For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?
         */
        @Child(name = "recipient", type = {Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class, Group.class, Organization.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="For whom is fulfillment sought?", formalDefinition="For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?" )
        protected List<Reference> recipient;
        /**
         * The actual objects that are the target of the reference (For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?)
         */
        protected List<Resource> recipientTarget;


        private static final long serialVersionUID = 1503908360L;

    /**
     * Constructor
     */
      public TaskRestrictionComponent() {
        super();
      }

        /**
         * @return {@link #repetitions} (Indicates the number of times the requested action should occur.). This is the underlying object with id, value and extensions. The accessor "getRepetitions" gives direct access to the value
         */
        public PositiveIntType getRepetitionsElement() { 
          if (this.repetitions == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TaskRestrictionComponent.repetitions");
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
        public TaskRestrictionComponent setRepetitionsElement(PositiveIntType value) { 
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
        public TaskRestrictionComponent setRepetitions(int value) { 
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
              throw new Error("Attempt to auto-create TaskRestrictionComponent.period");
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
        public TaskRestrictionComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #recipient} (For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?)
         */
        public List<Reference> getRecipient() { 
          if (this.recipient == null)
            this.recipient = new ArrayList<Reference>();
          return this.recipient;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TaskRestrictionComponent setRecipient(List<Reference> theRecipient) { 
          this.recipient = theRecipient;
          return this;
        }

        public boolean hasRecipient() { 
          if (this.recipient == null)
            return false;
          for (Reference item : this.recipient)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addRecipient() { //3
          Reference t = new Reference();
          if (this.recipient == null)
            this.recipient = new ArrayList<Reference>();
          this.recipient.add(t);
          return t;
        }

        public TaskRestrictionComponent addRecipient(Reference t) { //3
          if (t == null)
            return this;
          if (this.recipient == null)
            this.recipient = new ArrayList<Reference>();
          this.recipient.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #recipient}, creating it if it does not already exist
         */
        public Reference getRecipientFirstRep() { 
          if (getRecipient().isEmpty()) {
            addRecipient();
          }
          return getRecipient().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getRecipientTarget() { 
          if (this.recipientTarget == null)
            this.recipientTarget = new ArrayList<Resource>();
          return this.recipientTarget;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("repetitions", "positiveInt", "Indicates the number of times the requested action should occur.", 0, 1, repetitions));
          children.add(new Property("period", "Period", "Over what time-period is fulfillment sought.", 0, 1, period));
          children.add(new Property("recipient", "Reference(Patient|Practitioner|PractitionerRole|RelatedPerson|Group|Organization)", "For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?", 0, java.lang.Integer.MAX_VALUE, recipient));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 984367650: /*repetitions*/  return new Property("repetitions", "positiveInt", "Indicates the number of times the requested action should occur.", 0, 1, repetitions);
          case -991726143: /*period*/  return new Property("period", "Period", "Over what time-period is fulfillment sought.", 0, 1, period);
          case 820081177: /*recipient*/  return new Property("recipient", "Reference(Patient|Practitioner|PractitionerRole|RelatedPerson|Group|Organization)", "For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?", 0, java.lang.Integer.MAX_VALUE, recipient);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 984367650: /*repetitions*/ return this.repetitions == null ? new Base[0] : new Base[] {this.repetitions}; // PositiveIntType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 820081177: /*recipient*/ return this.recipient == null ? new Base[0] : this.recipient.toArray(new Base[this.recipient.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 984367650: // repetitions
          this.repetitions = castToPositiveInt(value); // PositiveIntType
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case 820081177: // recipient
          this.getRecipient().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("repetitions")) {
          this.repetitions = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("recipient")) {
          this.getRecipient().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 984367650:  return getRepetitionsElement();
        case -991726143:  return getPeriod(); 
        case 820081177:  return addRecipient(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 984367650: /*repetitions*/ return new String[] {"positiveInt"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case 820081177: /*recipient*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
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
        else if (name.equals("recipient")) {
          return addRecipient();
        }
        else
          return super.addChild(name);
      }

      public TaskRestrictionComponent copy() {
        TaskRestrictionComponent dst = new TaskRestrictionComponent();
        copyValues(dst);
        dst.repetitions = repetitions == null ? null : repetitions.copy();
        dst.period = period == null ? null : period.copy();
        if (recipient != null) {
          dst.recipient = new ArrayList<Reference>();
          for (Reference i : recipient)
            dst.recipient.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof TaskRestrictionComponent))
          return false;
        TaskRestrictionComponent o = (TaskRestrictionComponent) other_;
        return compareDeep(repetitions, o.repetitions, true) && compareDeep(period, o.period, true) && compareDeep(recipient, o.recipient, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof TaskRestrictionComponent))
          return false;
        TaskRestrictionComponent o = (TaskRestrictionComponent) other_;
        return compareValues(repetitions, o.repetitions, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(repetitions, period, recipient
          );
      }

  public String fhirType() {
    return "Task.restriction";

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
        @Description(shortDefinition="Content to use in performing the task", formalDefinition="The value of the input parameter as a basic type." )
        protected org.hl7.fhir.r4.model.Type value;

        private static final long serialVersionUID = -850267045L;

    /**
     * Constructor
     */
      public ParameterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ParameterComponent(CodeableConcept type, org.hl7.fhir.r4.model.Type value) {
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
        public org.hl7.fhir.r4.model.Type getValue() { 
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the input parameter as a basic type.)
         */
        public ParameterComponent setValue(org.hl7.fhir.r4.model.Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "A code or description indicating how the input is intended to be used as part of the task execution.", 0, 1, type));
          children.add(new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "A code or description indicating how the input is intended to be used as part of the task execution.", 0, 1, type);
          case -1410166417: /*value[x]*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1535024575: /*valueBase64Binary*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case 733421943: /*valueBoolean*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -786218365: /*valueCanonical*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -766209282: /*valueCode*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -766192449: /*valueDate*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case 1047929900: /*valueDateTime*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -2083993440: /*valueDecimal*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case 231604844: /*valueId*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1668687056: /*valueInstant*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1668204915: /*valueInteger*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -497880704: /*valueMarkdown*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1410178407: /*valueOid*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1249932027: /*valuePositiveInt*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1424603934: /*valueString*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -765708322: /*valueTime*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case 26529417: /*valueUnsignedInt*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1410172357: /*valueUri*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1410172354: /*valueUrl*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -765667124: /*valueUuid*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -478981821: /*valueAddress*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -67108992: /*valueAnnotation*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -475566732: /*valueAttachment*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case 924902896: /*valueCodeableConcept*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1887705029: /*valueCoding*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case 944904545: /*valueContactPoint*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -2026205465: /*valueHumanName*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -130498310: /*valueIdentifier*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1524344174: /*valuePeriod*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -2029823716: /*valueQuantity*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case 2030761548: /*valueRange*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case 2030767386: /*valueRatio*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case 1755241690: /*valueReference*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -962229101: /*valueSampledData*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -540985785: /*valueSignature*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1406282469: /*valueTiming*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          case -1858636920: /*valueDosage*/  return new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // org.hl7.fhir.r4.model.Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 111972721: // value
          this.value = castToType(value); // org.hl7.fhir.r4.model.Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // org.hl7.fhir.r4.model.Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 111972721: /*value*/ return new String[] {"*"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("valueBase64Binary")) {
          this.value = new Base64BinaryType();
          return this.value;
        }
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueCanonical")) {
          this.value = new CanonicalType();
          return this.value;
        }
        else if (name.equals("valueCode")) {
          this.value = new CodeType();
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
        else if (name.equals("valueDecimal")) {
          this.value = new DecimalType();
          return this.value;
        }
        else if (name.equals("valueId")) {
          this.value = new IdType();
          return this.value;
        }
        else if (name.equals("valueInstant")) {
          this.value = new InstantType();
          return this.value;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valueMarkdown")) {
          this.value = new MarkdownType();
          return this.value;
        }
        else if (name.equals("valueOid")) {
          this.value = new OidType();
          return this.value;
        }
        else if (name.equals("valuePositiveInt")) {
          this.value = new PositiveIntType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueUnsignedInt")) {
          this.value = new UnsignedIntType();
          return this.value;
        }
        else if (name.equals("valueUri")) {
          this.value = new UriType();
          return this.value;
        }
        else if (name.equals("valueUrl")) {
          this.value = new UrlType();
          return this.value;
        }
        else if (name.equals("valueUuid")) {
          this.value = new UuidType();
          return this.value;
        }
        else if (name.equals("valueAddress")) {
          this.value = new Address();
          return this.value;
        }
        else if (name.equals("valueAge")) {
          this.value = new Age();
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
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueContactPoint")) {
          this.value = new ContactPoint();
          return this.value;
        }
        else if (name.equals("valueCount")) {
          this.value = new Count();
          return this.value;
        }
        else if (name.equals("valueDistance")) {
          this.value = new Distance();
          return this.value;
        }
        else if (name.equals("valueDuration")) {
          this.value = new Duration();
          return this.value;
        }
        else if (name.equals("valueHumanName")) {
          this.value = new HumanName();
          return this.value;
        }
        else if (name.equals("valueIdentifier")) {
          this.value = new Identifier();
          return this.value;
        }
        else if (name.equals("valueMoney")) {
          this.value = new Money();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
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
        else if (name.equals("valueRatio")) {
          this.value = new Ratio();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
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
        else if (name.equals("valueTiming")) {
          this.value = new Timing();
          return this.value;
        }
        else if (name.equals("valueContactDetail")) {
          this.value = new ContactDetail();
          return this.value;
        }
        else if (name.equals("valueContributor")) {
          this.value = new Contributor();
          return this.value;
        }
        else if (name.equals("valueDataRequirement")) {
          this.value = new DataRequirement();
          return this.value;
        }
        else if (name.equals("valueExpression")) {
          this.value = new Expression();
          return this.value;
        }
        else if (name.equals("valueParameterDefinition")) {
          this.value = new ParameterDefinition();
          return this.value;
        }
        else if (name.equals("valueRelatedArtifact")) {
          this.value = new RelatedArtifact();
          return this.value;
        }
        else if (name.equals("valueTriggerDefinition")) {
          this.value = new TriggerDefinition();
          return this.value;
        }
        else if (name.equals("valueUsageContext")) {
          this.value = new UsageContext();
          return this.value;
        }
        else if (name.equals("valueDosage")) {
          this.value = new Dosage();
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ParameterComponent))
          return false;
        ParameterComponent o = (ParameterComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ParameterComponent))
          return false;
        ParameterComponent o = (ParameterComponent) other_;
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
        @Description(shortDefinition="Label for output", formalDefinition="The name of the Output parameter." )
        protected CodeableConcept type;

        /**
         * The value of the Output parameter as a basic type.
         */
        @Child(name = "value", type = {}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Result of output", formalDefinition="The value of the Output parameter as a basic type." )
        protected org.hl7.fhir.r4.model.Type value;

        private static final long serialVersionUID = -850267045L;

    /**
     * Constructor
     */
      public TaskOutputComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TaskOutputComponent(CodeableConcept type, org.hl7.fhir.r4.model.Type value) {
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
        public org.hl7.fhir.r4.model.Type getValue() { 
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the Output parameter as a basic type.)
         */
        public TaskOutputComponent setValue(org.hl7.fhir.r4.model.Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "The name of the Output parameter.", 0, 1, type));
          children.add(new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The name of the Output parameter.", 0, 1, type);
          case -1410166417: /*value[x]*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1535024575: /*valueBase64Binary*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case 733421943: /*valueBoolean*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -786218365: /*valueCanonical*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -766209282: /*valueCode*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -766192449: /*valueDate*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case 1047929900: /*valueDateTime*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -2083993440: /*valueDecimal*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case 231604844: /*valueId*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1668687056: /*valueInstant*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1668204915: /*valueInteger*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -497880704: /*valueMarkdown*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1410178407: /*valueOid*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1249932027: /*valuePositiveInt*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1424603934: /*valueString*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -765708322: /*valueTime*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case 26529417: /*valueUnsignedInt*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1410172357: /*valueUri*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1410172354: /*valueUrl*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -765667124: /*valueUuid*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -478981821: /*valueAddress*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -67108992: /*valueAnnotation*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -475566732: /*valueAttachment*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case 924902896: /*valueCodeableConcept*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1887705029: /*valueCoding*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case 944904545: /*valueContactPoint*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -2026205465: /*valueHumanName*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -130498310: /*valueIdentifier*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1524344174: /*valuePeriod*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -2029823716: /*valueQuantity*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case 2030761548: /*valueRange*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case 2030767386: /*valueRatio*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case 1755241690: /*valueReference*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -962229101: /*valueSampledData*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -540985785: /*valueSignature*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1406282469: /*valueTiming*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          case -1858636920: /*valueDosage*/  return new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // org.hl7.fhir.r4.model.Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 111972721: // value
          this.value = castToType(value); // org.hl7.fhir.r4.model.Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // org.hl7.fhir.r4.model.Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 111972721: /*value*/ return new String[] {"*"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("valueBase64Binary")) {
          this.value = new Base64BinaryType();
          return this.value;
        }
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueCanonical")) {
          this.value = new CanonicalType();
          return this.value;
        }
        else if (name.equals("valueCode")) {
          this.value = new CodeType();
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
        else if (name.equals("valueDecimal")) {
          this.value = new DecimalType();
          return this.value;
        }
        else if (name.equals("valueId")) {
          this.value = new IdType();
          return this.value;
        }
        else if (name.equals("valueInstant")) {
          this.value = new InstantType();
          return this.value;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valueMarkdown")) {
          this.value = new MarkdownType();
          return this.value;
        }
        else if (name.equals("valueOid")) {
          this.value = new OidType();
          return this.value;
        }
        else if (name.equals("valuePositiveInt")) {
          this.value = new PositiveIntType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueUnsignedInt")) {
          this.value = new UnsignedIntType();
          return this.value;
        }
        else if (name.equals("valueUri")) {
          this.value = new UriType();
          return this.value;
        }
        else if (name.equals("valueUrl")) {
          this.value = new UrlType();
          return this.value;
        }
        else if (name.equals("valueUuid")) {
          this.value = new UuidType();
          return this.value;
        }
        else if (name.equals("valueAddress")) {
          this.value = new Address();
          return this.value;
        }
        else if (name.equals("valueAge")) {
          this.value = new Age();
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
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueContactPoint")) {
          this.value = new ContactPoint();
          return this.value;
        }
        else if (name.equals("valueCount")) {
          this.value = new Count();
          return this.value;
        }
        else if (name.equals("valueDistance")) {
          this.value = new Distance();
          return this.value;
        }
        else if (name.equals("valueDuration")) {
          this.value = new Duration();
          return this.value;
        }
        else if (name.equals("valueHumanName")) {
          this.value = new HumanName();
          return this.value;
        }
        else if (name.equals("valueIdentifier")) {
          this.value = new Identifier();
          return this.value;
        }
        else if (name.equals("valueMoney")) {
          this.value = new Money();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
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
        else if (name.equals("valueRatio")) {
          this.value = new Ratio();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
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
        else if (name.equals("valueTiming")) {
          this.value = new Timing();
          return this.value;
        }
        else if (name.equals("valueContactDetail")) {
          this.value = new ContactDetail();
          return this.value;
        }
        else if (name.equals("valueContributor")) {
          this.value = new Contributor();
          return this.value;
        }
        else if (name.equals("valueDataRequirement")) {
          this.value = new DataRequirement();
          return this.value;
        }
        else if (name.equals("valueExpression")) {
          this.value = new Expression();
          return this.value;
        }
        else if (name.equals("valueParameterDefinition")) {
          this.value = new ParameterDefinition();
          return this.value;
        }
        else if (name.equals("valueRelatedArtifact")) {
          this.value = new RelatedArtifact();
          return this.value;
        }
        else if (name.equals("valueTriggerDefinition")) {
          this.value = new TriggerDefinition();
          return this.value;
        }
        else if (name.equals("valueUsageContext")) {
          this.value = new UsageContext();
          return this.value;
        }
        else if (name.equals("valueDosage")) {
          this.value = new Dosage();
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof TaskOutputComponent))
          return false;
        TaskOutputComponent o = (TaskOutputComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof TaskOutputComponent))
          return false;
        TaskOutputComponent o = (TaskOutputComponent) other_;
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
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Task Instance Identifier", formalDefinition="The business identifier for this task." )
    protected List<Identifier> identifier;

    /**
     * The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.
     */
    @Child(name = "instantiatesCanonical", type = {CanonicalType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Formal definition of task", formalDefinition="The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task." )
    protected CanonicalType instantiatesCanonical;

    /**
     * The URL pointing to an *externally* maintained  protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.
     */
    @Child(name = "instantiatesUri", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Formal definition of task", formalDefinition="The URL pointing to an *externally* maintained  protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task." )
    protected UriType instantiatesUri;

    /**
     * BasedOn refers to a higher-level authorization that triggered the creation of the task.  It references a "request" resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the "request" resource the task is seeking to fulfill.  This latter resource is referenced by FocusOn.  For example, based on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen from a patient.
     */
    @Child(name = "basedOn", type = {Reference.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Request fulfilled by this task", formalDefinition="BasedOn refers to a higher-level authorization that triggered the creation of the task.  It references a \"request\" resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the \"request\" resource the task is seeking to fulfill.  This latter resource is referenced by FocusOn.  For example, based on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen from a patient." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (BasedOn refers to a higher-level authorization that triggered the creation of the task.  It references a "request" resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the "request" resource the task is seeking to fulfill.  This latter resource is referenced by FocusOn.  For example, based on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen from a patient.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * An identifier that links together multiple tasks and other requests that were created in the same context.
     */
    @Child(name = "groupIdentifier", type = {Identifier.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Requisition or grouper id", formalDefinition="An identifier that links together multiple tasks and other requests that were created in the same context." )
    protected Identifier groupIdentifier;

    /**
     * Task that this particular task is part of.
     */
    @Child(name = "partOf", type = {Task.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Composite task", formalDefinition="Task that this particular task is part of." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (Task that this particular task is part of.)
     */
    protected List<Task> partOfTarget;


    /**
     * The current status of the task.
     */
    @Child(name = "status", type = {CodeType.class}, order=6, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | requested | received | accepted | +", formalDefinition="The current status of the task." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/task-status")
    protected Enumeration<TaskStatus> status;

    /**
     * An explanation as to why this task is held, failed, was refused, etc.
     */
    @Child(name = "statusReason", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for current status", formalDefinition="An explanation as to why this task is held, failed, was refused, etc." )
    protected CodeableConcept statusReason;

    /**
     * Contains business-specific nuances of the business state.
     */
    @Child(name = "businessStatus", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="E.g. \"Specimen collected\", \"IV prepped\"", formalDefinition="Contains business-specific nuances of the business state." )
    protected CodeableConcept businessStatus;

    /**
     * Indicates the "level" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, an actionable task, etc.
     */
    @Child(name = "intent", type = {CodeType.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="unknown | proposal | plan | order | original-order | reflex-order | filler-order | instance-order | option", formalDefinition="Indicates the \"level\" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, an actionable task, etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/task-intent")
    protected Enumeration<TaskIntent> intent;

    /**
     * Indicates how quickly the Task should be addressed with respect to other requests.
     */
    @Child(name = "priority", type = {CodeType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="routine | urgent | asap | stat", formalDefinition="Indicates how quickly the Task should be addressed with respect to other requests." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-priority")
    protected Enumeration<TaskPriority> priority;

    /**
     * A name or code (or both) briefly describing what the task involves.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Type", formalDefinition="A name or code (or both) briefly describing what the task involves." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/task-code")
    protected CodeableConcept code;

    /**
     * A free-text description of what is to be performed.
     */
    @Child(name = "description", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human-readable explanation of task", formalDefinition="A free-text description of what is to be performed." )
    protected StringType description;

    /**
     * The request being actioned or the resource being manipulated by this task.
     */
    @Child(name = "focus", type = {Reference.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What task is acting on", formalDefinition="The request being actioned or the resource being manipulated by this task." )
    protected Reference focus;

    /**
     * The actual object that is the target of the reference (The request being actioned or the resource being manipulated by this task.)
     */
    protected Resource focusTarget;

    /**
     * The entity who benefits from the performance of the service specified in the task (e.g., the patient).
     */
    @Child(name = "for", type = {Reference.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Beneficiary of the Task", formalDefinition="The entity who benefits from the performance of the service specified in the task (e.g., the patient)." )
    protected Reference for_;

    /**
     * The actual object that is the target of the reference (The entity who benefits from the performance of the service specified in the task (e.g., the patient).)
     */
    protected Resource for_Target;

    /**
     * The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Healthcare event during which this task originated", formalDefinition="The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.)
     */
    protected Encounter encounterTarget;

    /**
     * Identifies the time action was first taken against the task (start) and/or the time final action was taken against the task prior to marking it as completed (end).
     */
    @Child(name = "executionPeriod", type = {Period.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Start and end time of execution", formalDefinition="Identifies the time action was first taken against the task (start) and/or the time final action was taken against the task prior to marking it as completed (end)." )
    protected Period executionPeriod;

    /**
     * The date and time this task was created.
     */
    @Child(name = "authoredOn", type = {DateTimeType.class}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Task Creation Date", formalDefinition="The date and time this task was created." )
    protected DateTimeType authoredOn;

    /**
     * The date and time of last modification to this task.
     */
    @Child(name = "lastModified", type = {DateTimeType.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Last Modified Date", formalDefinition="The date and time of last modification to this task." )
    protected DateTimeType lastModified;

    /**
     * The creator of the task.
     */
    @Child(name = "requester", type = {Device.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class}, order=19, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who is asking for task to be done", formalDefinition="The creator of the task." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (The creator of the task.)
     */
    protected Resource requesterTarget;

    /**
     * The kind of participant that should perform the task.
     */
    @Child(name = "performerType", type = {CodeableConcept.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Requested performer", formalDefinition="The kind of participant that should perform the task." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/performer-role")
    protected List<CodeableConcept> performerType;

    /**
     * Individual organization or Device currently responsible for task execution.
     */
    @Child(name = "owner", type = {Practitioner.class, PractitionerRole.class, Organization.class, CareTeam.class, HealthcareService.class, Patient.class, Device.class, RelatedPerson.class}, order=21, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible individual", formalDefinition="Individual organization or Device currently responsible for task execution." )
    protected Reference owner;

    /**
     * The actual object that is the target of the reference (Individual organization or Device currently responsible for task execution.)
     */
    protected Resource ownerTarget;

    /**
     * Principal physical location where the this task is performed.
     */
    @Child(name = "location", type = {Location.class}, order=22, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where task occurs", formalDefinition="Principal physical location where the this task is performed." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (Principal physical location where the this task is performed.)
     */
    protected Location locationTarget;

    /**
     * A description or code indicating why this task needs to be performed.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=23, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why task is needed", formalDefinition="A description or code indicating why this task needs to be performed." )
    protected CodeableConcept reasonCode;

    /**
     * A resource reference indicating why this task needs to be performed.
     */
    @Child(name = "reasonReference", type = {Reference.class}, order=24, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why task is needed", formalDefinition="A resource reference indicating why this task needs to be performed." )
    protected Reference reasonReference;

    /**
     * The actual object that is the target of the reference (A resource reference indicating why this task needs to be performed.)
     */
    protected Resource reasonReferenceTarget;

    /**
     * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.
     */
    @Child(name = "insurance", type = {Coverage.class, ClaimResponse.class}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Associated insurance coverage", formalDefinition="Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task." )
    protected List<Reference> insurance;
    /**
     * The actual objects that are the target of the reference (Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.)
     */
    protected List<Resource> insuranceTarget;


    /**
     * Free-text information captured about the task as it progresses.
     */
    @Child(name = "note", type = {Annotation.class}, order=26, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about the task", formalDefinition="Free-text information captured about the task as it progresses." )
    protected List<Annotation> note;

    /**
     * Links to Provenance records for past versions of this Task that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the task.
     */
    @Child(name = "relevantHistory", type = {Provenance.class}, order=27, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Key events in history of the Task", formalDefinition="Links to Provenance records for past versions of this Task that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the task." )
    protected List<Reference> relevantHistory;
    /**
     * The actual objects that are the target of the reference (Links to Provenance records for past versions of this Task that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the task.)
     */
    protected List<Provenance> relevantHistoryTarget;


    /**
     * If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be actioned), this element identifies any limitations on what parts of the referenced request should be actioned.
     */
    @Child(name = "restriction", type = {}, order=28, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Constraints on fulfillment tasks", formalDefinition="If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be actioned), this element identifies any limitations on what parts of the referenced request should be actioned." )
    protected TaskRestrictionComponent restriction;

    /**
     * Additional information that may be needed in the execution of the task.
     */
    @Child(name = "input", type = {}, order=29, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Information used to perform task", formalDefinition="Additional information that may be needed in the execution of the task." )
    protected List<ParameterComponent> input;

    /**
     * Outputs produced by the Task.
     */
    @Child(name = "output", type = {}, order=30, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Information produced as part of task", formalDefinition="Outputs produced by the Task." )
    protected List<TaskOutputComponent> output;

    private static final long serialVersionUID = -765029272L;

  /**
   * Constructor
   */
    public Task() {
      super();
    }

  /**
   * Constructor
   */
    public Task(Enumeration<TaskStatus> status, Enumeration<TaskIntent> intent) {
      super();
      this.status = status;
      this.intent = intent;
    }

    /**
     * @return {@link #identifier} (The business identifier for this task.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Task setIdentifier(List<Identifier> theIdentifier) { 
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

    public Task addIdentifier(Identifier t) { //3
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
     * @return {@link #instantiatesCanonical} (The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.). This is the underlying object with id, value and extensions. The accessor "getInstantiatesCanonical" gives direct access to the value
     */
    public CanonicalType getInstantiatesCanonicalElement() { 
      if (this.instantiatesCanonical == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.instantiatesCanonical");
        else if (Configuration.doAutoCreate())
          this.instantiatesCanonical = new CanonicalType(); // bb
      return this.instantiatesCanonical;
    }

    public boolean hasInstantiatesCanonicalElement() { 
      return this.instantiatesCanonical != null && !this.instantiatesCanonical.isEmpty();
    }

    public boolean hasInstantiatesCanonical() { 
      return this.instantiatesCanonical != null && !this.instantiatesCanonical.isEmpty();
    }

    /**
     * @param value {@link #instantiatesCanonical} (The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.). This is the underlying object with id, value and extensions. The accessor "getInstantiatesCanonical" gives direct access to the value
     */
    public Task setInstantiatesCanonicalElement(CanonicalType value) { 
      this.instantiatesCanonical = value;
      return this;
    }

    /**
     * @return The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.
     */
    public String getInstantiatesCanonical() { 
      return this.instantiatesCanonical == null ? null : this.instantiatesCanonical.getValue();
    }

    /**
     * @param value The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.
     */
    public Task setInstantiatesCanonical(String value) { 
      if (Utilities.noString(value))
        this.instantiatesCanonical = null;
      else {
        if (this.instantiatesCanonical == null)
          this.instantiatesCanonical = new CanonicalType();
        this.instantiatesCanonical.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #instantiatesUri} (The URL pointing to an *externally* maintained  protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.). This is the underlying object with id, value and extensions. The accessor "getInstantiatesUri" gives direct access to the value
     */
    public UriType getInstantiatesUriElement() { 
      if (this.instantiatesUri == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.instantiatesUri");
        else if (Configuration.doAutoCreate())
          this.instantiatesUri = new UriType(); // bb
      return this.instantiatesUri;
    }

    public boolean hasInstantiatesUriElement() { 
      return this.instantiatesUri != null && !this.instantiatesUri.isEmpty();
    }

    public boolean hasInstantiatesUri() { 
      return this.instantiatesUri != null && !this.instantiatesUri.isEmpty();
    }

    /**
     * @param value {@link #instantiatesUri} (The URL pointing to an *externally* maintained  protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.). This is the underlying object with id, value and extensions. The accessor "getInstantiatesUri" gives direct access to the value
     */
    public Task setInstantiatesUriElement(UriType value) { 
      this.instantiatesUri = value;
      return this;
    }

    /**
     * @return The URL pointing to an *externally* maintained  protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.
     */
    public String getInstantiatesUri() { 
      return this.instantiatesUri == null ? null : this.instantiatesUri.getValue();
    }

    /**
     * @param value The URL pointing to an *externally* maintained  protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.
     */
    public Task setInstantiatesUri(String value) { 
      if (Utilities.noString(value))
        this.instantiatesUri = null;
      else {
        if (this.instantiatesUri == null)
          this.instantiatesUri = new UriType();
        this.instantiatesUri.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #basedOn} (BasedOn refers to a higher-level authorization that triggered the creation of the task.  It references a "request" resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the "request" resource the task is seeking to fulfill.  This latter resource is referenced by FocusOn.  For example, based on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen from a patient.)
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
     * @return {@link #groupIdentifier} (An identifier that links together multiple tasks and other requests that were created in the same context.)
     */
    public Identifier getGroupIdentifier() { 
      if (this.groupIdentifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.groupIdentifier");
        else if (Configuration.doAutoCreate())
          this.groupIdentifier = new Identifier(); // cc
      return this.groupIdentifier;
    }

    public boolean hasGroupIdentifier() { 
      return this.groupIdentifier != null && !this.groupIdentifier.isEmpty();
    }

    /**
     * @param value {@link #groupIdentifier} (An identifier that links together multiple tasks and other requests that were created in the same context.)
     */
    public Task setGroupIdentifier(Identifier value) { 
      this.groupIdentifier = value;
      return this;
    }

    /**
     * @return {@link #partOf} (Task that this particular task is part of.)
     */
    public List<Reference> getPartOf() { 
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      return this.partOf;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Task setPartOf(List<Reference> thePartOf) { 
      this.partOf = thePartOf;
      return this;
    }

    public boolean hasPartOf() { 
      if (this.partOf == null)
        return false;
      for (Reference item : this.partOf)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPartOf() { //3
      Reference t = new Reference();
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return t;
    }

    public Task addPartOf(Reference t) { //3
      if (t == null)
        return this;
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #partOf}, creating it if it does not already exist
     */
    public Reference getPartOfFirstRep() { 
      if (getPartOf().isEmpty()) {
        addPartOf();
      }
      return getPartOf().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Task> getPartOfTarget() { 
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<Task>();
      return this.partOfTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Task addPartOfTarget() { 
      Task r = new Task();
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<Task>();
      this.partOfTarget.add(r);
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
     * @return {@link #intent} (Indicates the "level" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, an actionable task, etc.). This is the underlying object with id, value and extensions. The accessor "getIntent" gives direct access to the value
     */
    public Enumeration<TaskIntent> getIntentElement() { 
      if (this.intent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.intent");
        else if (Configuration.doAutoCreate())
          this.intent = new Enumeration<TaskIntent>(new TaskIntentEnumFactory()); // bb
      return this.intent;
    }

    public boolean hasIntentElement() { 
      return this.intent != null && !this.intent.isEmpty();
    }

    public boolean hasIntent() { 
      return this.intent != null && !this.intent.isEmpty();
    }

    /**
     * @param value {@link #intent} (Indicates the "level" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, an actionable task, etc.). This is the underlying object with id, value and extensions. The accessor "getIntent" gives direct access to the value
     */
    public Task setIntentElement(Enumeration<TaskIntent> value) { 
      this.intent = value;
      return this;
    }

    /**
     * @return Indicates the "level" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, an actionable task, etc.
     */
    public TaskIntent getIntent() { 
      return this.intent == null ? null : this.intent.getValue();
    }

    /**
     * @param value Indicates the "level" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, an actionable task, etc.
     */
    public Task setIntent(TaskIntent value) { 
        if (this.intent == null)
          this.intent = new Enumeration<TaskIntent>(new TaskIntentEnumFactory());
        this.intent.setValue(value);
      return this;
    }

    /**
     * @return {@link #priority} (Indicates how quickly the Task should be addressed with respect to other requests.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
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
     * @param value {@link #priority} (Indicates how quickly the Task should be addressed with respect to other requests.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Task setPriorityElement(Enumeration<TaskPriority> value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return Indicates how quickly the Task should be addressed with respect to other requests.
     */
    public TaskPriority getPriority() { 
      return this.priority == null ? null : this.priority.getValue();
    }

    /**
     * @param value Indicates how quickly the Task should be addressed with respect to other requests.
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
     * @return {@link #encounter} (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.)
     */
    public Task setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.)
     */
    public Task setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #executionPeriod} (Identifies the time action was first taken against the task (start) and/or the time final action was taken against the task prior to marking it as completed (end).)
     */
    public Period getExecutionPeriod() { 
      if (this.executionPeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.executionPeriod");
        else if (Configuration.doAutoCreate())
          this.executionPeriod = new Period(); // cc
      return this.executionPeriod;
    }

    public boolean hasExecutionPeriod() { 
      return this.executionPeriod != null && !this.executionPeriod.isEmpty();
    }

    /**
     * @param value {@link #executionPeriod} (Identifies the time action was first taken against the task (start) and/or the time final action was taken against the task prior to marking it as completed (end).)
     */
    public Task setExecutionPeriod(Period value) { 
      this.executionPeriod = value;
      return this;
    }

    /**
     * @return {@link #authoredOn} (The date and time this task was created.). This is the underlying object with id, value and extensions. The accessor "getAuthoredOn" gives direct access to the value
     */
    public DateTimeType getAuthoredOnElement() { 
      if (this.authoredOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.authoredOn");
        else if (Configuration.doAutoCreate())
          this.authoredOn = new DateTimeType(); // bb
      return this.authoredOn;
    }

    public boolean hasAuthoredOnElement() { 
      return this.authoredOn != null && !this.authoredOn.isEmpty();
    }

    public boolean hasAuthoredOn() { 
      return this.authoredOn != null && !this.authoredOn.isEmpty();
    }

    /**
     * @param value {@link #authoredOn} (The date and time this task was created.). This is the underlying object with id, value and extensions. The accessor "getAuthoredOn" gives direct access to the value
     */
    public Task setAuthoredOnElement(DateTimeType value) { 
      this.authoredOn = value;
      return this;
    }

    /**
     * @return The date and time this task was created.
     */
    public Date getAuthoredOn() { 
      return this.authoredOn == null ? null : this.authoredOn.getValue();
    }

    /**
     * @param value The date and time this task was created.
     */
    public Task setAuthoredOn(Date value) { 
      if (value == null)
        this.authoredOn = null;
      else {
        if (this.authoredOn == null)
          this.authoredOn = new DateTimeType();
        this.authoredOn.setValue(value);
      }
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
      if (value == null)
        this.lastModified = null;
      else {
        if (this.lastModified == null)
          this.lastModified = new DateTimeType();
        this.lastModified.setValue(value);
      }
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
     * @return {@link #performerType} (The kind of participant that should perform the task.)
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
     * @return {@link #owner} (Individual organization or Device currently responsible for task execution.)
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
     * @param value {@link #owner} (Individual organization or Device currently responsible for task execution.)
     */
    public Task setOwner(Reference value) { 
      this.owner = value;
      return this;
    }

    /**
     * @return {@link #owner} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Individual organization or Device currently responsible for task execution.)
     */
    public Resource getOwnerTarget() { 
      return this.ownerTarget;
    }

    /**
     * @param value {@link #owner} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Individual organization or Device currently responsible for task execution.)
     */
    public Task setOwnerTarget(Resource value) { 
      this.ownerTarget = value;
      return this;
    }

    /**
     * @return {@link #location} (Principal physical location where the this task is performed.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (Principal physical location where the this task is performed.)
     */
    public Task setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Principal physical location where the this task is performed.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location(); // aa
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Principal physical location where the this task is performed.)
     */
    public Task setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #reasonCode} (A description or code indicating why this task needs to be performed.)
     */
    public CodeableConcept getReasonCode() { 
      if (this.reasonCode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.reasonCode");
        else if (Configuration.doAutoCreate())
          this.reasonCode = new CodeableConcept(); // cc
      return this.reasonCode;
    }

    public boolean hasReasonCode() { 
      return this.reasonCode != null && !this.reasonCode.isEmpty();
    }

    /**
     * @param value {@link #reasonCode} (A description or code indicating why this task needs to be performed.)
     */
    public Task setReasonCode(CodeableConcept value) { 
      this.reasonCode = value;
      return this;
    }

    /**
     * @return {@link #reasonReference} (A resource reference indicating why this task needs to be performed.)
     */
    public Reference getReasonReference() { 
      if (this.reasonReference == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.reasonReference");
        else if (Configuration.doAutoCreate())
          this.reasonReference = new Reference(); // cc
      return this.reasonReference;
    }

    public boolean hasReasonReference() { 
      return this.reasonReference != null && !this.reasonReference.isEmpty();
    }

    /**
     * @param value {@link #reasonReference} (A resource reference indicating why this task needs to be performed.)
     */
    public Task setReasonReference(Reference value) { 
      this.reasonReference = value;
      return this;
    }

    /**
     * @return {@link #reasonReference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A resource reference indicating why this task needs to be performed.)
     */
    public Resource getReasonReferenceTarget() { 
      return this.reasonReferenceTarget;
    }

    /**
     * @param value {@link #reasonReference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A resource reference indicating why this task needs to be performed.)
     */
    public Task setReasonReferenceTarget(Resource value) { 
      this.reasonReferenceTarget = value;
      return this;
    }

    /**
     * @return {@link #insurance} (Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.)
     */
    public List<Reference> getInsurance() { 
      if (this.insurance == null)
        this.insurance = new ArrayList<Reference>();
      return this.insurance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Task setInsurance(List<Reference> theInsurance) { 
      this.insurance = theInsurance;
      return this;
    }

    public boolean hasInsurance() { 
      if (this.insurance == null)
        return false;
      for (Reference item : this.insurance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addInsurance() { //3
      Reference t = new Reference();
      if (this.insurance == null)
        this.insurance = new ArrayList<Reference>();
      this.insurance.add(t);
      return t;
    }

    public Task addInsurance(Reference t) { //3
      if (t == null)
        return this;
      if (this.insurance == null)
        this.insurance = new ArrayList<Reference>();
      this.insurance.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #insurance}, creating it if it does not already exist
     */
    public Reference getInsuranceFirstRep() { 
      if (getInsurance().isEmpty()) {
        addInsurance();
      }
      return getInsurance().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getInsuranceTarget() { 
      if (this.insuranceTarget == null)
        this.insuranceTarget = new ArrayList<Resource>();
      return this.insuranceTarget;
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
     * @return {@link #relevantHistory} (Links to Provenance records for past versions of this Task that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the task.)
     */
    public List<Reference> getRelevantHistory() { 
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      return this.relevantHistory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Task setRelevantHistory(List<Reference> theRelevantHistory) { 
      this.relevantHistory = theRelevantHistory;
      return this;
    }

    public boolean hasRelevantHistory() { 
      if (this.relevantHistory == null)
        return false;
      for (Reference item : this.relevantHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addRelevantHistory() { //3
      Reference t = new Reference();
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      this.relevantHistory.add(t);
      return t;
    }

    public Task addRelevantHistory(Reference t) { //3
      if (t == null)
        return this;
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      this.relevantHistory.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relevantHistory}, creating it if it does not already exist
     */
    public Reference getRelevantHistoryFirstRep() { 
      if (getRelevantHistory().isEmpty()) {
        addRelevantHistory();
      }
      return getRelevantHistory().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Provenance> getRelevantHistoryTarget() { 
      if (this.relevantHistoryTarget == null)
        this.relevantHistoryTarget = new ArrayList<Provenance>();
      return this.relevantHistoryTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Provenance addRelevantHistoryTarget() { 
      Provenance r = new Provenance();
      if (this.relevantHistoryTarget == null)
        this.relevantHistoryTarget = new ArrayList<Provenance>();
      this.relevantHistoryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #restriction} (If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be actioned), this element identifies any limitations on what parts of the referenced request should be actioned.)
     */
    public TaskRestrictionComponent getRestriction() { 
      if (this.restriction == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.restriction");
        else if (Configuration.doAutoCreate())
          this.restriction = new TaskRestrictionComponent(); // cc
      return this.restriction;
    }

    public boolean hasRestriction() { 
      return this.restriction != null && !this.restriction.isEmpty();
    }

    /**
     * @param value {@link #restriction} (If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be actioned), this element identifies any limitations on what parts of the referenced request should be actioned.)
     */
    public Task setRestriction(TaskRestrictionComponent value) { 
      this.restriction = value;
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

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "The business identifier for this task.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("instantiatesCanonical", "canonical(ActivityDefinition)", "The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.", 0, 1, instantiatesCanonical));
        children.add(new Property("instantiatesUri", "uri", "The URL pointing to an *externally* maintained  protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.", 0, 1, instantiatesUri));
        children.add(new Property("basedOn", "Reference(Any)", "BasedOn refers to a higher-level authorization that triggered the creation of the task.  It references a \"request\" resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the \"request\" resource the task is seeking to fulfill.  This latter resource is referenced by FocusOn.  For example, based on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen from a patient.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        children.add(new Property("groupIdentifier", "Identifier", "An identifier that links together multiple tasks and other requests that were created in the same context.", 0, 1, groupIdentifier));
        children.add(new Property("partOf", "Reference(Task)", "Task that this particular task is part of.", 0, java.lang.Integer.MAX_VALUE, partOf));
        children.add(new Property("status", "code", "The current status of the task.", 0, 1, status));
        children.add(new Property("statusReason", "CodeableConcept", "An explanation as to why this task is held, failed, was refused, etc.", 0, 1, statusReason));
        children.add(new Property("businessStatus", "CodeableConcept", "Contains business-specific nuances of the business state.", 0, 1, businessStatus));
        children.add(new Property("intent", "code", "Indicates the \"level\" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, an actionable task, etc.", 0, 1, intent));
        children.add(new Property("priority", "code", "Indicates how quickly the Task should be addressed with respect to other requests.", 0, 1, priority));
        children.add(new Property("code", "CodeableConcept", "A name or code (or both) briefly describing what the task involves.", 0, 1, code));
        children.add(new Property("description", "string", "A free-text description of what is to be performed.", 0, 1, description));
        children.add(new Property("focus", "Reference(Any)", "The request being actioned or the resource being manipulated by this task.", 0, 1, focus));
        children.add(new Property("for", "Reference(Any)", "The entity who benefits from the performance of the service specified in the task (e.g., the patient).", 0, 1, for_));
        children.add(new Property("encounter", "Reference(Encounter)", "The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.", 0, 1, encounter));
        children.add(new Property("executionPeriod", "Period", "Identifies the time action was first taken against the task (start) and/or the time final action was taken against the task prior to marking it as completed (end).", 0, 1, executionPeriod));
        children.add(new Property("authoredOn", "dateTime", "The date and time this task was created.", 0, 1, authoredOn));
        children.add(new Property("lastModified", "dateTime", "The date and time of last modification to this task.", 0, 1, lastModified));
        children.add(new Property("requester", "Reference(Device|Organization|Patient|Practitioner|PractitionerRole|RelatedPerson)", "The creator of the task.", 0, 1, requester));
        children.add(new Property("performerType", "CodeableConcept", "The kind of participant that should perform the task.", 0, java.lang.Integer.MAX_VALUE, performerType));
        children.add(new Property("owner", "Reference(Practitioner|PractitionerRole|Organization|CareTeam|HealthcareService|Patient|Device|RelatedPerson)", "Individual organization or Device currently responsible for task execution.", 0, 1, owner));
        children.add(new Property("location", "Reference(Location)", "Principal physical location where the this task is performed.", 0, 1, location));
        children.add(new Property("reasonCode", "CodeableConcept", "A description or code indicating why this task needs to be performed.", 0, 1, reasonCode));
        children.add(new Property("reasonReference", "Reference(Any)", "A resource reference indicating why this task needs to be performed.", 0, 1, reasonReference));
        children.add(new Property("insurance", "Reference(Coverage|ClaimResponse)", "Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.", 0, java.lang.Integer.MAX_VALUE, insurance));
        children.add(new Property("note", "Annotation", "Free-text information captured about the task as it progresses.", 0, java.lang.Integer.MAX_VALUE, note));
        children.add(new Property("relevantHistory", "Reference(Provenance)", "Links to Provenance records for past versions of this Task that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the task.", 0, java.lang.Integer.MAX_VALUE, relevantHistory));
        children.add(new Property("restriction", "", "If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be actioned), this element identifies any limitations on what parts of the referenced request should be actioned.", 0, 1, restriction));
        children.add(new Property("input", "", "Additional information that may be needed in the execution of the task.", 0, java.lang.Integer.MAX_VALUE, input));
        children.add(new Property("output", "", "Outputs produced by the Task.", 0, java.lang.Integer.MAX_VALUE, output));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "The business identifier for this task.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 8911915: /*instantiatesCanonical*/  return new Property("instantiatesCanonical", "canonical(ActivityDefinition)", "The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.", 0, 1, instantiatesCanonical);
        case -1926393373: /*instantiatesUri*/  return new Property("instantiatesUri", "uri", "The URL pointing to an *externally* maintained  protocol, guideline, orderset or other definition that is adhered to in whole or in part by this Task.", 0, 1, instantiatesUri);
        case -332612366: /*basedOn*/  return new Property("basedOn", "Reference(Any)", "BasedOn refers to a higher-level authorization that triggered the creation of the task.  It references a \"request\" resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the \"request\" resource the task is seeking to fulfill.  This latter resource is referenced by FocusOn.  For example, based on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen from a patient.", 0, java.lang.Integer.MAX_VALUE, basedOn);
        case -445338488: /*groupIdentifier*/  return new Property("groupIdentifier", "Identifier", "An identifier that links together multiple tasks and other requests that were created in the same context.", 0, 1, groupIdentifier);
        case -995410646: /*partOf*/  return new Property("partOf", "Reference(Task)", "Task that this particular task is part of.", 0, java.lang.Integer.MAX_VALUE, partOf);
        case -892481550: /*status*/  return new Property("status", "code", "The current status of the task.", 0, 1, status);
        case 2051346646: /*statusReason*/  return new Property("statusReason", "CodeableConcept", "An explanation as to why this task is held, failed, was refused, etc.", 0, 1, statusReason);
        case 2008591314: /*businessStatus*/  return new Property("businessStatus", "CodeableConcept", "Contains business-specific nuances of the business state.", 0, 1, businessStatus);
        case -1183762788: /*intent*/  return new Property("intent", "code", "Indicates the \"level\" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, an actionable task, etc.", 0, 1, intent);
        case -1165461084: /*priority*/  return new Property("priority", "code", "Indicates how quickly the Task should be addressed with respect to other requests.", 0, 1, priority);
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A name or code (or both) briefly describing what the task involves.", 0, 1, code);
        case -1724546052: /*description*/  return new Property("description", "string", "A free-text description of what is to be performed.", 0, 1, description);
        case 97604824: /*focus*/  return new Property("focus", "Reference(Any)", "The request being actioned or the resource being manipulated by this task.", 0, 1, focus);
        case 101577: /*for*/  return new Property("for", "Reference(Any)", "The entity who benefits from the performance of the service specified in the task (e.g., the patient).", 0, 1, for_);
        case 1524132147: /*encounter*/  return new Property("encounter", "Reference(Encounter)", "The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.", 0, 1, encounter);
        case 1218624249: /*executionPeriod*/  return new Property("executionPeriod", "Period", "Identifies the time action was first taken against the task (start) and/or the time final action was taken against the task prior to marking it as completed (end).", 0, 1, executionPeriod);
        case -1500852503: /*authoredOn*/  return new Property("authoredOn", "dateTime", "The date and time this task was created.", 0, 1, authoredOn);
        case 1959003007: /*lastModified*/  return new Property("lastModified", "dateTime", "The date and time of last modification to this task.", 0, 1, lastModified);
        case 693933948: /*requester*/  return new Property("requester", "Reference(Device|Organization|Patient|Practitioner|PractitionerRole|RelatedPerson)", "The creator of the task.", 0, 1, requester);
        case -901444568: /*performerType*/  return new Property("performerType", "CodeableConcept", "The kind of participant that should perform the task.", 0, java.lang.Integer.MAX_VALUE, performerType);
        case 106164915: /*owner*/  return new Property("owner", "Reference(Practitioner|PractitionerRole|Organization|CareTeam|HealthcareService|Patient|Device|RelatedPerson)", "Individual organization or Device currently responsible for task execution.", 0, 1, owner);
        case 1901043637: /*location*/  return new Property("location", "Reference(Location)", "Principal physical location where the this task is performed.", 0, 1, location);
        case 722137681: /*reasonCode*/  return new Property("reasonCode", "CodeableConcept", "A description or code indicating why this task needs to be performed.", 0, 1, reasonCode);
        case -1146218137: /*reasonReference*/  return new Property("reasonReference", "Reference(Any)", "A resource reference indicating why this task needs to be performed.", 0, 1, reasonReference);
        case 73049818: /*insurance*/  return new Property("insurance", "Reference(Coverage|ClaimResponse)", "Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.", 0, java.lang.Integer.MAX_VALUE, insurance);
        case 3387378: /*note*/  return new Property("note", "Annotation", "Free-text information captured about the task as it progresses.", 0, java.lang.Integer.MAX_VALUE, note);
        case 1538891575: /*relevantHistory*/  return new Property("relevantHistory", "Reference(Provenance)", "Links to Provenance records for past versions of this Task that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the task.", 0, java.lang.Integer.MAX_VALUE, relevantHistory);
        case -1561062452: /*restriction*/  return new Property("restriction", "", "If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be actioned), this element identifies any limitations on what parts of the referenced request should be actioned.", 0, 1, restriction);
        case 100358090: /*input*/  return new Property("input", "", "Additional information that may be needed in the execution of the task.", 0, java.lang.Integer.MAX_VALUE, input);
        case -1005512447: /*output*/  return new Property("output", "", "Outputs produced by the Task.", 0, java.lang.Integer.MAX_VALUE, output);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 8911915: /*instantiatesCanonical*/ return this.instantiatesCanonical == null ? new Base[0] : new Base[] {this.instantiatesCanonical}; // CanonicalType
        case -1926393373: /*instantiatesUri*/ return this.instantiatesUri == null ? new Base[0] : new Base[] {this.instantiatesUri}; // UriType
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -445338488: /*groupIdentifier*/ return this.groupIdentifier == null ? new Base[0] : new Base[] {this.groupIdentifier}; // Identifier
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<TaskStatus>
        case 2051346646: /*statusReason*/ return this.statusReason == null ? new Base[0] : new Base[] {this.statusReason}; // CodeableConcept
        case 2008591314: /*businessStatus*/ return this.businessStatus == null ? new Base[0] : new Base[] {this.businessStatus}; // CodeableConcept
        case -1183762788: /*intent*/ return this.intent == null ? new Base[0] : new Base[] {this.intent}; // Enumeration<TaskIntent>
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // Enumeration<TaskPriority>
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 97604824: /*focus*/ return this.focus == null ? new Base[0] : new Base[] {this.focus}; // Reference
        case 101577: /*for*/ return this.for_ == null ? new Base[0] : new Base[] {this.for_}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case 1218624249: /*executionPeriod*/ return this.executionPeriod == null ? new Base[0] : new Base[] {this.executionPeriod}; // Period
        case -1500852503: /*authoredOn*/ return this.authoredOn == null ? new Base[0] : new Base[] {this.authoredOn}; // DateTimeType
        case 1959003007: /*lastModified*/ return this.lastModified == null ? new Base[0] : new Base[] {this.lastModified}; // DateTimeType
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // Reference
        case -901444568: /*performerType*/ return this.performerType == null ? new Base[0] : this.performerType.toArray(new Base[this.performerType.size()]); // CodeableConcept
        case 106164915: /*owner*/ return this.owner == null ? new Base[0] : new Base[] {this.owner}; // Reference
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : new Base[] {this.reasonCode}; // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : new Base[] {this.reasonReference}; // Reference
        case 73049818: /*insurance*/ return this.insurance == null ? new Base[0] : this.insurance.toArray(new Base[this.insurance.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 1538891575: /*relevantHistory*/ return this.relevantHistory == null ? new Base[0] : this.relevantHistory.toArray(new Base[this.relevantHistory.size()]); // Reference
        case -1561062452: /*restriction*/ return this.restriction == null ? new Base[0] : new Base[] {this.restriction}; // TaskRestrictionComponent
        case 100358090: /*input*/ return this.input == null ? new Base[0] : this.input.toArray(new Base[this.input.size()]); // ParameterComponent
        case -1005512447: /*output*/ return this.output == null ? new Base[0] : this.output.toArray(new Base[this.output.size()]); // TaskOutputComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 8911915: // instantiatesCanonical
          this.instantiatesCanonical = castToCanonical(value); // CanonicalType
          return value;
        case -1926393373: // instantiatesUri
          this.instantiatesUri = castToUri(value); // UriType
          return value;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          return value;
        case -445338488: // groupIdentifier
          this.groupIdentifier = castToIdentifier(value); // Identifier
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new TaskStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<TaskStatus>
          return value;
        case 2051346646: // statusReason
          this.statusReason = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 2008591314: // businessStatus
          this.businessStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1183762788: // intent
          value = new TaskIntentEnumFactory().fromType(castToCode(value));
          this.intent = (Enumeration) value; // Enumeration<TaskIntent>
          return value;
        case -1165461084: // priority
          value = new TaskPriorityEnumFactory().fromType(castToCode(value));
          this.priority = (Enumeration) value; // Enumeration<TaskPriority>
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 97604824: // focus
          this.focus = castToReference(value); // Reference
          return value;
        case 101577: // for
          this.for_ = castToReference(value); // Reference
          return value;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          return value;
        case 1218624249: // executionPeriod
          this.executionPeriod = castToPeriod(value); // Period
          return value;
        case -1500852503: // authoredOn
          this.authoredOn = castToDateTime(value); // DateTimeType
          return value;
        case 1959003007: // lastModified
          this.lastModified = castToDateTime(value); // DateTimeType
          return value;
        case 693933948: // requester
          this.requester = castToReference(value); // Reference
          return value;
        case -901444568: // performerType
          this.getPerformerType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 106164915: // owner
          this.owner = castToReference(value); // Reference
          return value;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          return value;
        case 722137681: // reasonCode
          this.reasonCode = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.reasonReference = castToReference(value); // Reference
          return value;
        case 73049818: // insurance
          this.getInsurance().add(castToReference(value)); // Reference
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case 1538891575: // relevantHistory
          this.getRelevantHistory().add(castToReference(value)); // Reference
          return value;
        case -1561062452: // restriction
          this.restriction = (TaskRestrictionComponent) value; // TaskRestrictionComponent
          return value;
        case 100358090: // input
          this.getInput().add((ParameterComponent) value); // ParameterComponent
          return value;
        case -1005512447: // output
          this.getOutput().add((TaskOutputComponent) value); // TaskOutputComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("instantiatesCanonical")) {
          this.instantiatesCanonical = castToCanonical(value); // CanonicalType
        } else if (name.equals("instantiatesUri")) {
          this.instantiatesUri = castToUri(value); // UriType
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("groupIdentifier")) {
          this.groupIdentifier = castToIdentifier(value); // Identifier
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new TaskStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<TaskStatus>
        } else if (name.equals("statusReason")) {
          this.statusReason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("businessStatus")) {
          this.businessStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("intent")) {
          value = new TaskIntentEnumFactory().fromType(castToCode(value));
          this.intent = (Enumeration) value; // Enumeration<TaskIntent>
        } else if (name.equals("priority")) {
          value = new TaskPriorityEnumFactory().fromType(castToCode(value));
          this.priority = (Enumeration) value; // Enumeration<TaskPriority>
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("focus")) {
          this.focus = castToReference(value); // Reference
        } else if (name.equals("for")) {
          this.for_ = castToReference(value); // Reference
        } else if (name.equals("encounter")) {
          this.encounter = castToReference(value); // Reference
        } else if (name.equals("executionPeriod")) {
          this.executionPeriod = castToPeriod(value); // Period
        } else if (name.equals("authoredOn")) {
          this.authoredOn = castToDateTime(value); // DateTimeType
        } else if (name.equals("lastModified")) {
          this.lastModified = castToDateTime(value); // DateTimeType
        } else if (name.equals("requester")) {
          this.requester = castToReference(value); // Reference
        } else if (name.equals("performerType")) {
          this.getPerformerType().add(castToCodeableConcept(value));
        } else if (name.equals("owner")) {
          this.owner = castToReference(value); // Reference
        } else if (name.equals("location")) {
          this.location = castToReference(value); // Reference
        } else if (name.equals("reasonCode")) {
          this.reasonCode = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("reasonReference")) {
          this.reasonReference = castToReference(value); // Reference
        } else if (name.equals("insurance")) {
          this.getInsurance().add(castToReference(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("relevantHistory")) {
          this.getRelevantHistory().add(castToReference(value));
        } else if (name.equals("restriction")) {
          this.restriction = (TaskRestrictionComponent) value; // TaskRestrictionComponent
        } else if (name.equals("input")) {
          this.getInput().add((ParameterComponent) value);
        } else if (name.equals("output")) {
          this.getOutput().add((TaskOutputComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 8911915:  return getInstantiatesCanonicalElement();
        case -1926393373:  return getInstantiatesUriElement();
        case -332612366:  return addBasedOn(); 
        case -445338488:  return getGroupIdentifier(); 
        case -995410646:  return addPartOf(); 
        case -892481550:  return getStatusElement();
        case 2051346646:  return getStatusReason(); 
        case 2008591314:  return getBusinessStatus(); 
        case -1183762788:  return getIntentElement();
        case -1165461084:  return getPriorityElement();
        case 3059181:  return getCode(); 
        case -1724546052:  return getDescriptionElement();
        case 97604824:  return getFocus(); 
        case 101577:  return getFor(); 
        case 1524132147:  return getEncounter(); 
        case 1218624249:  return getExecutionPeriod(); 
        case -1500852503:  return getAuthoredOnElement();
        case 1959003007:  return getLastModifiedElement();
        case 693933948:  return getRequester(); 
        case -901444568:  return addPerformerType(); 
        case 106164915:  return getOwner(); 
        case 1901043637:  return getLocation(); 
        case 722137681:  return getReasonCode(); 
        case -1146218137:  return getReasonReference(); 
        case 73049818:  return addInsurance(); 
        case 3387378:  return addNote(); 
        case 1538891575:  return addRelevantHistory(); 
        case -1561062452:  return getRestriction(); 
        case 100358090:  return addInput(); 
        case -1005512447:  return addOutput(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 8911915: /*instantiatesCanonical*/ return new String[] {"canonical"};
        case -1926393373: /*instantiatesUri*/ return new String[] {"uri"};
        case -332612366: /*basedOn*/ return new String[] {"Reference"};
        case -445338488: /*groupIdentifier*/ return new String[] {"Identifier"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 2051346646: /*statusReason*/ return new String[] {"CodeableConcept"};
        case 2008591314: /*businessStatus*/ return new String[] {"CodeableConcept"};
        case -1183762788: /*intent*/ return new String[] {"code"};
        case -1165461084: /*priority*/ return new String[] {"code"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 97604824: /*focus*/ return new String[] {"Reference"};
        case 101577: /*for*/ return new String[] {"Reference"};
        case 1524132147: /*encounter*/ return new String[] {"Reference"};
        case 1218624249: /*executionPeriod*/ return new String[] {"Period"};
        case -1500852503: /*authoredOn*/ return new String[] {"dateTime"};
        case 1959003007: /*lastModified*/ return new String[] {"dateTime"};
        case 693933948: /*requester*/ return new String[] {"Reference"};
        case -901444568: /*performerType*/ return new String[] {"CodeableConcept"};
        case 106164915: /*owner*/ return new String[] {"Reference"};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case 73049818: /*insurance*/ return new String[] {"Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case 1538891575: /*relevantHistory*/ return new String[] {"Reference"};
        case -1561062452: /*restriction*/ return new String[] {};
        case 100358090: /*input*/ return new String[] {};
        case -1005512447: /*output*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("instantiatesCanonical")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.instantiatesCanonical");
        }
        else if (name.equals("instantiatesUri")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.instantiatesUri");
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("groupIdentifier")) {
          this.groupIdentifier = new Identifier();
          return this.groupIdentifier;
        }
        else if (name.equals("partOf")) {
          return addPartOf();
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
        else if (name.equals("intent")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.intent");
        }
        else if (name.equals("priority")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.priority");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
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
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("executionPeriod")) {
          this.executionPeriod = new Period();
          return this.executionPeriod;
        }
        else if (name.equals("authoredOn")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.authoredOn");
        }
        else if (name.equals("lastModified")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.lastModified");
        }
        else if (name.equals("requester")) {
          this.requester = new Reference();
          return this.requester;
        }
        else if (name.equals("performerType")) {
          return addPerformerType();
        }
        else if (name.equals("owner")) {
          this.owner = new Reference();
          return this.owner;
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("reasonCode")) {
          this.reasonCode = new CodeableConcept();
          return this.reasonCode;
        }
        else if (name.equals("reasonReference")) {
          this.reasonReference = new Reference();
          return this.reasonReference;
        }
        else if (name.equals("insurance")) {
          return addInsurance();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("relevantHistory")) {
          return addRelevantHistory();
        }
        else if (name.equals("restriction")) {
          this.restriction = new TaskRestrictionComponent();
          return this.restriction;
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
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.instantiatesCanonical = instantiatesCanonical == null ? null : instantiatesCanonical.copy();
        dst.instantiatesUri = instantiatesUri == null ? null : instantiatesUri.copy();
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        dst.groupIdentifier = groupIdentifier == null ? null : groupIdentifier.copy();
        if (partOf != null) {
          dst.partOf = new ArrayList<Reference>();
          for (Reference i : partOf)
            dst.partOf.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.statusReason = statusReason == null ? null : statusReason.copy();
        dst.businessStatus = businessStatus == null ? null : businessStatus.copy();
        dst.intent = intent == null ? null : intent.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.code = code == null ? null : code.copy();
        dst.description = description == null ? null : description.copy();
        dst.focus = focus == null ? null : focus.copy();
        dst.for_ = for_ == null ? null : for_.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.executionPeriod = executionPeriod == null ? null : executionPeriod.copy();
        dst.authoredOn = authoredOn == null ? null : authoredOn.copy();
        dst.lastModified = lastModified == null ? null : lastModified.copy();
        dst.requester = requester == null ? null : requester.copy();
        if (performerType != null) {
          dst.performerType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : performerType)
            dst.performerType.add(i.copy());
        };
        dst.owner = owner == null ? null : owner.copy();
        dst.location = location == null ? null : location.copy();
        dst.reasonCode = reasonCode == null ? null : reasonCode.copy();
        dst.reasonReference = reasonReference == null ? null : reasonReference.copy();
        if (insurance != null) {
          dst.insurance = new ArrayList<Reference>();
          for (Reference i : insurance)
            dst.insurance.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (relevantHistory != null) {
          dst.relevantHistory = new ArrayList<Reference>();
          for (Reference i : relevantHistory)
            dst.relevantHistory.add(i.copy());
        };
        dst.restriction = restriction == null ? null : restriction.copy();
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Task))
          return false;
        Task o = (Task) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(instantiatesCanonical, o.instantiatesCanonical, true)
           && compareDeep(instantiatesUri, o.instantiatesUri, true) && compareDeep(basedOn, o.basedOn, true)
           && compareDeep(groupIdentifier, o.groupIdentifier, true) && compareDeep(partOf, o.partOf, true)
           && compareDeep(status, o.status, true) && compareDeep(statusReason, o.statusReason, true) && compareDeep(businessStatus, o.businessStatus, true)
           && compareDeep(intent, o.intent, true) && compareDeep(priority, o.priority, true) && compareDeep(code, o.code, true)
           && compareDeep(description, o.description, true) && compareDeep(focus, o.focus, true) && compareDeep(for_, o.for_, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(executionPeriod, o.executionPeriod, true)
           && compareDeep(authoredOn, o.authoredOn, true) && compareDeep(lastModified, o.lastModified, true)
           && compareDeep(requester, o.requester, true) && compareDeep(performerType, o.performerType, true)
           && compareDeep(owner, o.owner, true) && compareDeep(location, o.location, true) && compareDeep(reasonCode, o.reasonCode, true)
           && compareDeep(reasonReference, o.reasonReference, true) && compareDeep(insurance, o.insurance, true)
           && compareDeep(note, o.note, true) && compareDeep(relevantHistory, o.relevantHistory, true) && compareDeep(restriction, o.restriction, true)
           && compareDeep(input, o.input, true) && compareDeep(output, o.output, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Task))
          return false;
        Task o = (Task) other_;
        return compareValues(instantiatesUri, o.instantiatesUri, true) && compareValues(status, o.status, true)
           && compareValues(intent, o.intent, true) && compareValues(priority, o.priority, true) && compareValues(description, o.description, true)
           && compareValues(authoredOn, o.authoredOn, true) && compareValues(lastModified, o.lastModified, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, instantiatesCanonical
          , instantiatesUri, basedOn, groupIdentifier, partOf, status, statusReason, businessStatus
          , intent, priority, code, description, focus, for_, encounter, executionPeriod
          , authoredOn, lastModified, requester, performerType, owner, location, reasonCode
          , reasonReference, insurance, note, relevantHistory, restriction, input, output
          );
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
  @SearchParamDefinition(name="owner", path="Task.owner", description="Search by task owner", type="reference", target={CareTeam.class, Device.class, HealthcareService.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
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
  @SearchParamDefinition(name="requester", path="Task.requester", description="Search by task requester", type="reference", target={Device.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
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
   * Search parameter: <b>business-status</b>
   * <p>
   * Description: <b>Search by business status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.businessStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="business-status", path="Task.businessStatus", description="Search by business status", type="token" )
  public static final String SP_BUSINESS_STATUS = "business-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>business-status</b>
   * <p>
   * Description: <b>Search by business status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.businessStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam BUSINESS_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_BUSINESS_STATUS);

 /**
   * Search parameter: <b>period</b>
   * <p>
   * Description: <b>Search by period Task is/was underway</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Task.executionPeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="period", path="Task.executionPeriod", description="Search by period Task is/was underway", type="date" )
  public static final String SP_PERIOD = "period";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>period</b>
   * <p>
   * Description: <b>Search by period Task is/was underway</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Task.executionPeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam PERIOD = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_PERIOD);

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
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.for</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Task.for", description="Search by subject", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.for</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Task:subject").toLocked();

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
   * Search parameter: <b>part-of</b>
   * <p>
   * Description: <b>Search by task this task is part of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.partOf</b><br>
   * </p>
   */
  @SearchParamDefinition(name="part-of", path="Task.partOf", description="Search by task this task is part of", type="reference", target={Task.class } )
  public static final String SP_PART_OF = "part-of";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>part-of</b>
   * <p>
   * Description: <b>Search by task this task is part of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.partOf</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PART_OF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PART_OF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:part-of</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PART_OF = new ca.uhn.fhir.model.api.Include("Task:part-of").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Search by encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="Task.encounter", description="Search by encounter", type="reference", target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Search by encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("Task:encounter").toLocked();

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
   * Search parameter: <b>authored-on</b>
   * <p>
   * Description: <b>Search by creation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Task.authoredOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authored-on", path="Task.authoredOn", description="Search by creation date", type="date" )
  public static final String SP_AUTHORED_ON = "authored-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authored-on</b>
   * <p>
   * Description: <b>Search by creation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Task.authoredOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam AUTHORED_ON = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_AUTHORED_ON);

 /**
   * Search parameter: <b>intent</b>
   * <p>
   * Description: <b>Search by task intent</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.intent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="intent", path="Task.intent", description="Search by task intent", type="token" )
  public static final String SP_INTENT = "intent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>intent</b>
   * <p>
   * Description: <b>Search by task intent</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.intent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam INTENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_INTENT);

 /**
   * Search parameter: <b>group-identifier</b>
   * <p>
   * Description: <b>Search by group identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.groupIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="group-identifier", path="Task.groupIdentifier", description="Search by group identifier", type="token" )
  public static final String SP_GROUP_IDENTIFIER = "group-identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>group-identifier</b>
   * <p>
   * Description: <b>Search by group identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.groupIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam GROUP_IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_GROUP_IDENTIFIER);

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>Search by requests this task is based on</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="Task.basedOn", description="Search by requests this task is based on", type="reference" )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>Search by requests this task is based on</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("Task:based-on").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Search by patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.for</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Task.for.where(resolve() is Patient)", description="Search by patient", type="reference", target={Patient.class } )
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

