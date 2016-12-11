package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A task to be performed.
 */
@ResourceDef(name="Task", profile="http://hl7.org/fhir/Profile/Task")
public class Task extends DomainResource {

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
         * added to help the parsers
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
         * added to help the parsers
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

    @Block()
    public static class ParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the input parameter.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Input Name", formalDefinition="The name of the input parameter." )
        protected StringType name;

        /**
         * The value of the input parameter as a basic type.
         */
        @Child(name = "value", type = {}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Input Value", formalDefinition="The value of the input parameter as a basic type." )
        protected org.hl7.fhir.dstu2016may.model.Type value;

        private static final long serialVersionUID = 342865819L;

    /**
     * Constructor
     */
      public ParameterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ParameterComponent(StringType name, org.hl7.fhir.dstu2016may.model.Type value) {
        super();
        this.name = name;
        this.value = value;
      }

        /**
         * @return {@link #name} (The name of the input parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ParameterComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name of the input parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ParameterComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the input parameter.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the input parameter.
         */
        public ParameterComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value of the input parameter as a basic type.)
         */
        public org.hl7.fhir.dstu2016may.model.Type getValue() { 
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the input parameter as a basic type.)
         */
        public ParameterComponent setValue(org.hl7.fhir.dstu2016may.model.Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of the input parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // org.hl7.fhir.dstu2016may.model.Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 111972721: // value
          this.value = (org.hl7.fhir.dstu2016may.model.Type) value; // org.hl7.fhir.dstu2016may.model.Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("value[x]"))
          this.value = (org.hl7.fhir.dstu2016may.model.Type) value; // org.hl7.fhir.dstu2016may.model.Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1410166417:  return getValue(); // org.hl7.fhir.dstu2016may.model.Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.name");
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
        dst.name = name == null ? null : name.copy();
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
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ParameterComponent))
          return false;
        ParameterComponent o = (ParameterComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (value == null || value.isEmpty())
          ;
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
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Output Name", formalDefinition="The name of the Output parameter." )
        protected StringType name;

        /**
         * The value of the Output parameter as a basic type.
         */
        @Child(name = "value", type = {}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Output Value", formalDefinition="The value of the Output parameter as a basic type." )
        protected org.hl7.fhir.dstu2016may.model.Type value;

        private static final long serialVersionUID = 342865819L;

    /**
     * Constructor
     */
      public TaskOutputComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TaskOutputComponent(StringType name, org.hl7.fhir.dstu2016may.model.Type value) {
        super();
        this.name = name;
        this.value = value;
      }

        /**
         * @return {@link #name} (The name of the Output parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TaskOutputComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name of the Output parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public TaskOutputComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the Output parameter.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the Output parameter.
         */
        public TaskOutputComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value of the Output parameter as a basic type.)
         */
        public org.hl7.fhir.dstu2016may.model.Type getValue() { 
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the Output parameter as a basic type.)
         */
        public TaskOutputComponent setValue(org.hl7.fhir.dstu2016may.model.Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of the Output parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // org.hl7.fhir.dstu2016may.model.Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 111972721: // value
          this.value = (org.hl7.fhir.dstu2016may.model.Type) value; // org.hl7.fhir.dstu2016may.model.Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("value[x]"))
          this.value = (org.hl7.fhir.dstu2016may.model.Type) value; // org.hl7.fhir.dstu2016may.model.Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1410166417:  return getValue(); // org.hl7.fhir.dstu2016may.model.Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.name");
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
        dst.name = name == null ? null : name.copy();
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
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TaskOutputComponent))
          return false;
        TaskOutputComponent o = (TaskOutputComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (value == null || value.isEmpty())
          ;
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
     * A name or code (or both) briefly describing what the task involves.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Type", formalDefinition="A name or code (or both) briefly describing what the task involves." )
    protected CodeableConcept type;

    /**
     * A description of this task.
     */
    @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Description", formalDefinition="A description of this task." )
    protected StringType description;

    /**
     * The type of participant that can execute the task.
     */
    @Child(name = "performerType", type = {Coding.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="requester | dispatcher | scheduler | performer | monitor | manager | acquirer | reviewer", formalDefinition="The type of participant that can execute the task." )
    protected List<Coding> performerType;

    /**
     * The priority of the task among other tasks of the same type.
     */
    @Child(name = "priority", type = {CodeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="low | normal | high", formalDefinition="The priority of the task among other tasks of the same type." )
    protected Enumeration<TaskPriority> priority;

    /**
     * The current status of the task.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="draft | requested | received | accepted | +", formalDefinition="The current status of the task." )
    protected Enumeration<TaskStatus> status;

    /**
     * An explaination as to why this task failed.
     */
    @Child(name = "failureReason", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Failure Reason", formalDefinition="An explaination as to why this task failed." )
    protected CodeableConcept failureReason;

    /**
     * The subject of the task.
     */
    @Child(name = "subject", type = {}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Task Subject", formalDefinition="The subject of the task." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The subject of the task.)
     */
    protected Resource subjectTarget;

    /**
     * The entity who benefits from the performance of the service specified in the task (e.g., the patient).
     */
    @Child(name = "for", type = {}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Beneficiary of the Task", formalDefinition="The entity who benefits from the performance of the service specified in the task (e.g., the patient)." )
    protected Reference for_;

    /**
     * The actual object that is the target of the reference (The entity who benefits from the performance of the service specified in the task (e.g., the patient).)
     */
    protected Resource for_Target;

    /**
     * A reference to a formal or informal definition of the task.
     */
    @Child(name = "definition", type = {UriType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Definition", formalDefinition="A reference to a formal or informal definition of the task." )
    protected UriType definition;

    /**
     * The date and time this task was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=10, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Task Creation Date", formalDefinition="The date and time this task was created." )
    protected DateTimeType created;

    /**
     * The date and time of last modification to this task.
     */
    @Child(name = "lastModified", type = {DateTimeType.class}, order=11, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Last Modified Date", formalDefinition="The date and time of last modification to this task." )
    protected DateTimeType lastModified;

    /**
     * The creator of the task.
     */
    @Child(name = "creator", type = {Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=12, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Task Creator", formalDefinition="The creator of the task." )
    protected Reference creator;

    /**
     * The actual object that is the target of the reference (The creator of the task.)
     */
    protected Resource creatorTarget;

    /**
     * The owner of this task.  The participant who can execute this task.
     */
    @Child(name = "owner", type = {Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Task Owner", formalDefinition="The owner of this task.  The participant who can execute this task." )
    protected Reference owner;

    /**
     * The actual object that is the target of the reference (The owner of this task.  The participant who can execute this task.)
     */
    protected Resource ownerTarget;

    /**
     * Task that this particular task is part of.
     */
    @Child(name = "parent", type = {Task.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Composite task", formalDefinition="Task that this particular task is part of." )
    protected Reference parent;

    /**
     * The actual object that is the target of the reference (Task that this particular task is part of.)
     */
    protected Task parentTarget;

    /**
     * Inputs to the task.
     */
    @Child(name = "input", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Task Input", formalDefinition="Inputs to the task." )
    protected List<ParameterComponent> input;

    /**
     * Outputs produced by the Task.
     */
    @Child(name = "output", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Task Output", formalDefinition="Outputs produced by the Task." )
    protected List<TaskOutputComponent> output;

    private static final long serialVersionUID = 969281174L;

  /**
   * Constructor
   */
    public Task() {
      super();
    }

  /**
   * Constructor
   */
    public Task(Enumeration<TaskStatus> status, DateTimeType created, DateTimeType lastModified, Reference creator) {
      super();
      this.status = status;
      this.created = created;
      this.lastModified = lastModified;
      this.creator = creator;
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
     * @return {@link #type} (A name or code (or both) briefly describing what the task involves.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (A name or code (or both) briefly describing what the task involves.)
     */
    public Task setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #description} (A description of this task.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
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
     * @param value {@link #description} (A description of this task.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Task setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A description of this task.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A description of this task.
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
     * @return {@link #performerType} (The type of participant that can execute the task.)
     */
    public List<Coding> getPerformerType() { 
      if (this.performerType == null)
        this.performerType = new ArrayList<Coding>();
      return this.performerType;
    }

    public boolean hasPerformerType() { 
      if (this.performerType == null)
        return false;
      for (Coding item : this.performerType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #performerType} (The type of participant that can execute the task.)
     */
    // syntactic sugar
    public Coding addPerformerType() { //3
      Coding t = new Coding();
      if (this.performerType == null)
        this.performerType = new ArrayList<Coding>();
      this.performerType.add(t);
      return t;
    }

    // syntactic sugar
    public Task addPerformerType(Coding t) { //3
      if (t == null)
        return this;
      if (this.performerType == null)
        this.performerType = new ArrayList<Coding>();
      this.performerType.add(t);
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
     * @return {@link #failureReason} (An explaination as to why this task failed.)
     */
    public CodeableConcept getFailureReason() { 
      if (this.failureReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.failureReason");
        else if (Configuration.doAutoCreate())
          this.failureReason = new CodeableConcept(); // cc
      return this.failureReason;
    }

    public boolean hasFailureReason() { 
      return this.failureReason != null && !this.failureReason.isEmpty();
    }

    /**
     * @param value {@link #failureReason} (An explaination as to why this task failed.)
     */
    public Task setFailureReason(CodeableConcept value) { 
      this.failureReason = value;
      return this;
    }

    /**
     * @return {@link #subject} (The subject of the task.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The subject of the task.)
     */
    public Task setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The subject of the task.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The subject of the task.)
     */
    public Task setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
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
     * @return {@link #definition} (A reference to a formal or informal definition of the task.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
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
     * @param value {@link #definition} (A reference to a formal or informal definition of the task.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public Task setDefinitionElement(UriType value) { 
      this.definition = value;
      return this;
    }

    /**
     * @return A reference to a formal or informal definition of the task.
     */
    public String getDefinition() { 
      return this.definition == null ? null : this.definition.getValue();
    }

    /**
     * @param value A reference to a formal or informal definition of the task.
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
     * @return {@link #creator} (The creator of the task.)
     */
    public Reference getCreator() { 
      if (this.creator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.creator");
        else if (Configuration.doAutoCreate())
          this.creator = new Reference(); // cc
      return this.creator;
    }

    public boolean hasCreator() { 
      return this.creator != null && !this.creator.isEmpty();
    }

    /**
     * @param value {@link #creator} (The creator of the task.)
     */
    public Task setCreator(Reference value) { 
      this.creator = value;
      return this;
    }

    /**
     * @return {@link #creator} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The creator of the task.)
     */
    public Resource getCreatorTarget() { 
      return this.creatorTarget;
    }

    /**
     * @param value {@link #creator} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The creator of the task.)
     */
    public Task setCreatorTarget(Resource value) { 
      this.creatorTarget = value;
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
     * @return {@link #parent} (Task that this particular task is part of.)
     */
    public Reference getParent() { 
      if (this.parent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.parent");
        else if (Configuration.doAutoCreate())
          this.parent = new Reference(); // cc
      return this.parent;
    }

    public boolean hasParent() { 
      return this.parent != null && !this.parent.isEmpty();
    }

    /**
     * @param value {@link #parent} (Task that this particular task is part of.)
     */
    public Task setParent(Reference value) { 
      this.parent = value;
      return this;
    }

    /**
     * @return {@link #parent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Task that this particular task is part of.)
     */
    public Task getParentTarget() { 
      if (this.parentTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.parent");
        else if (Configuration.doAutoCreate())
          this.parentTarget = new Task(); // aa
      return this.parentTarget;
    }

    /**
     * @param value {@link #parent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Task that this particular task is part of.)
     */
    public Task setParentTarget(Task value) { 
      this.parentTarget = value;
      return this;
    }

    /**
     * @return {@link #input} (Inputs to the task.)
     */
    public List<ParameterComponent> getInput() { 
      if (this.input == null)
        this.input = new ArrayList<ParameterComponent>();
      return this.input;
    }

    public boolean hasInput() { 
      if (this.input == null)
        return false;
      for (ParameterComponent item : this.input)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #input} (Inputs to the task.)
     */
    // syntactic sugar
    public ParameterComponent addInput() { //3
      ParameterComponent t = new ParameterComponent();
      if (this.input == null)
        this.input = new ArrayList<ParameterComponent>();
      this.input.add(t);
      return t;
    }

    // syntactic sugar
    public Task addInput(ParameterComponent t) { //3
      if (t == null)
        return this;
      if (this.input == null)
        this.input = new ArrayList<ParameterComponent>();
      this.input.add(t);
      return this;
    }

    /**
     * @return {@link #output} (Outputs produced by the Task.)
     */
    public List<TaskOutputComponent> getOutput() { 
      if (this.output == null)
        this.output = new ArrayList<TaskOutputComponent>();
      return this.output;
    }

    public boolean hasOutput() { 
      if (this.output == null)
        return false;
      for (TaskOutputComponent item : this.output)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #output} (Outputs produced by the Task.)
     */
    // syntactic sugar
    public TaskOutputComponent addOutput() { //3
      TaskOutputComponent t = new TaskOutputComponent();
      if (this.output == null)
        this.output = new ArrayList<TaskOutputComponent>();
      this.output.add(t);
      return t;
    }

    // syntactic sugar
    public Task addOutput(TaskOutputComponent t) { //3
      if (t == null)
        return this;
      if (this.output == null)
        this.output = new ArrayList<TaskOutputComponent>();
      this.output.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The business identifier for this task.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "CodeableConcept", "A name or code (or both) briefly describing what the task involves.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("description", "string", "A description of this task.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("performerType", "Coding", "The type of participant that can execute the task.", 0, java.lang.Integer.MAX_VALUE, performerType));
        childrenList.add(new Property("priority", "code", "The priority of the task among other tasks of the same type.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("status", "code", "The current status of the task.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("failureReason", "CodeableConcept", "An explaination as to why this task failed.", 0, java.lang.Integer.MAX_VALUE, failureReason));
        childrenList.add(new Property("subject", "Reference(Any)", "The subject of the task.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("for", "Reference(Any)", "The entity who benefits from the performance of the service specified in the task (e.g., the patient).", 0, java.lang.Integer.MAX_VALUE, for_));
        childrenList.add(new Property("definition", "uri", "A reference to a formal or informal definition of the task.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("created", "dateTime", "The date and time this task was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("lastModified", "dateTime", "The date and time of last modification to this task.", 0, java.lang.Integer.MAX_VALUE, lastModified));
        childrenList.add(new Property("creator", "Reference(Device|Organization|Patient|Practitioner|RelatedPerson)", "The creator of the task.", 0, java.lang.Integer.MAX_VALUE, creator));
        childrenList.add(new Property("owner", "Reference(Device|Organization|Patient|Practitioner|RelatedPerson)", "The owner of this task.  The participant who can execute this task.", 0, java.lang.Integer.MAX_VALUE, owner));
        childrenList.add(new Property("parent", "Reference(Task)", "Task that this particular task is part of.", 0, java.lang.Integer.MAX_VALUE, parent));
        childrenList.add(new Property("input", "", "Inputs to the task.", 0, java.lang.Integer.MAX_VALUE, input));
        childrenList.add(new Property("output", "", "Outputs produced by the Task.", 0, java.lang.Integer.MAX_VALUE, output));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -901444568: /*performerType*/ return this.performerType == null ? new Base[0] : this.performerType.toArray(new Base[this.performerType.size()]); // Coding
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // Enumeration<TaskPriority>
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<TaskStatus>
        case -1990598546: /*failureReason*/ return this.failureReason == null ? new Base[0] : new Base[] {this.failureReason}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 101577: /*for*/ return this.for_ == null ? new Base[0] : new Base[] {this.for_}; // Reference
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // UriType
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1959003007: /*lastModified*/ return this.lastModified == null ? new Base[0] : new Base[] {this.lastModified}; // DateTimeType
        case 1028554796: /*creator*/ return this.creator == null ? new Base[0] : new Base[] {this.creator}; // Reference
        case 106164915: /*owner*/ return this.owner == null ? new Base[0] : new Base[] {this.owner}; // Reference
        case -995424086: /*parent*/ return this.parent == null ? new Base[0] : new Base[] {this.parent}; // Reference
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
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -901444568: // performerType
          this.getPerformerType().add(castToCoding(value)); // Coding
          break;
        case -1165461084: // priority
          this.priority = new TaskPriorityEnumFactory().fromType(value); // Enumeration<TaskPriority>
          break;
        case -892481550: // status
          this.status = new TaskStatusEnumFactory().fromType(value); // Enumeration<TaskStatus>
          break;
        case -1990598546: // failureReason
          this.failureReason = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case 101577: // for
          this.for_ = castToReference(value); // Reference
          break;
        case -1014418093: // definition
          this.definition = castToUri(value); // UriType
          break;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          break;
        case 1959003007: // lastModified
          this.lastModified = castToDateTime(value); // DateTimeType
          break;
        case 1028554796: // creator
          this.creator = castToReference(value); // Reference
          break;
        case 106164915: // owner
          this.owner = castToReference(value); // Reference
          break;
        case -995424086: // parent
          this.parent = castToReference(value); // Reference
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
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("performerType"))
          this.getPerformerType().add(castToCoding(value));
        else if (name.equals("priority"))
          this.priority = new TaskPriorityEnumFactory().fromType(value); // Enumeration<TaskPriority>
        else if (name.equals("status"))
          this.status = new TaskStatusEnumFactory().fromType(value); // Enumeration<TaskStatus>
        else if (name.equals("failureReason"))
          this.failureReason = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("for"))
          this.for_ = castToReference(value); // Reference
        else if (name.equals("definition"))
          this.definition = castToUri(value); // UriType
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("lastModified"))
          this.lastModified = castToDateTime(value); // DateTimeType
        else if (name.equals("creator"))
          this.creator = castToReference(value); // Reference
        else if (name.equals("owner"))
          this.owner = castToReference(value); // Reference
        else if (name.equals("parent"))
          this.parent = castToReference(value); // Reference
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
        case 3575610:  return getType(); // CodeableConcept
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -901444568:  return addPerformerType(); // Coding
        case -1165461084: throw new FHIRException("Cannot make property priority as it is not a complex type"); // Enumeration<TaskPriority>
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<TaskStatus>
        case -1990598546:  return getFailureReason(); // CodeableConcept
        case -1867885268:  return getSubject(); // Reference
        case 101577:  return getFor(); // Reference
        case -1014418093: throw new FHIRException("Cannot make property definition as it is not a complex type"); // UriType
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case 1959003007: throw new FHIRException("Cannot make property lastModified as it is not a complex type"); // DateTimeType
        case 1028554796:  return getCreator(); // Reference
        case 106164915:  return getOwner(); // Reference
        case -995424086:  return getParent(); // Reference
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
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.description");
        }
        else if (name.equals("performerType")) {
          return addPerformerType();
        }
        else if (name.equals("priority")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.priority");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.status");
        }
        else if (name.equals("failureReason")) {
          this.failureReason = new CodeableConcept();
          return this.failureReason;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("for")) {
          this.for_ = new Reference();
          return this.for_;
        }
        else if (name.equals("definition")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.definition");
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.created");
        }
        else if (name.equals("lastModified")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.lastModified");
        }
        else if (name.equals("creator")) {
          this.creator = new Reference();
          return this.creator;
        }
        else if (name.equals("owner")) {
          this.owner = new Reference();
          return this.owner;
        }
        else if (name.equals("parent")) {
          this.parent = new Reference();
          return this.parent;
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
        dst.type = type == null ? null : type.copy();
        dst.description = description == null ? null : description.copy();
        if (performerType != null) {
          dst.performerType = new ArrayList<Coding>();
          for (Coding i : performerType)
            dst.performerType.add(i.copy());
        };
        dst.priority = priority == null ? null : priority.copy();
        dst.status = status == null ? null : status.copy();
        dst.failureReason = failureReason == null ? null : failureReason.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.for_ = for_ == null ? null : for_.copy();
        dst.definition = definition == null ? null : definition.copy();
        dst.created = created == null ? null : created.copy();
        dst.lastModified = lastModified == null ? null : lastModified.copy();
        dst.creator = creator == null ? null : creator.copy();
        dst.owner = owner == null ? null : owner.copy();
        dst.parent = parent == null ? null : parent.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(description, o.description, true)
           && compareDeep(performerType, o.performerType, true) && compareDeep(priority, o.priority, true)
           && compareDeep(status, o.status, true) && compareDeep(failureReason, o.failureReason, true) && compareDeep(subject, o.subject, true)
           && compareDeep(for_, o.for_, true) && compareDeep(definition, o.definition, true) && compareDeep(created, o.created, true)
           && compareDeep(lastModified, o.lastModified, true) && compareDeep(creator, o.creator, true) && compareDeep(owner, o.owner, true)
           && compareDeep(parent, o.parent, true) && compareDeep(input, o.input, true) && compareDeep(output, o.output, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Task))
          return false;
        Task o = (Task) other;
        return compareValues(description, o.description, true) && compareValues(priority, o.priority, true)
           && compareValues(status, o.status, true) && compareValues(definition, o.definition, true) && compareValues(created, o.created, true)
           && compareValues(lastModified, o.lastModified, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
           && (description == null || description.isEmpty()) && (performerType == null || performerType.isEmpty())
           && (priority == null || priority.isEmpty()) && (status == null || status.isEmpty()) && (failureReason == null || failureReason.isEmpty())
           && (subject == null || subject.isEmpty()) && (for_ == null || for_.isEmpty()) && (definition == null || definition.isEmpty())
           && (created == null || created.isEmpty()) && (lastModified == null || lastModified.isEmpty())
           && (creator == null || creator.isEmpty()) && (owner == null || owner.isEmpty()) && (parent == null || parent.isEmpty())
           && (input == null || input.isEmpty()) && (output == null || output.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Task;
   }

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

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Search by task subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Task.subject", description="Search by task subject", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Search by task subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Task:subject").toLocked();

 /**
   * Search parameter: <b>parent</b>
   * <p>
   * Description: <b>Search by parent task</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.parent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="parent", path="Task.parent", description="Search by parent task", type="reference" )
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
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Search by task type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Task.type", description="Search by task type", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Search by task type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

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
   * Search parameter: <b>creator</b>
   * <p>
   * Description: <b>Search by task creator</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.creator</b><br>
   * </p>
   */
  @SearchParamDefinition(name="creator", path="Task.creator", description="Search by task creator", type="reference" )
  public static final String SP_CREATOR = "creator";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>creator</b>
   * <p>
   * Description: <b>Search by task creator</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.creator</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CREATOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CREATOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:creator</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CREATOR = new ca.uhn.fhir.model.api.Include("Task:creator").toLocked();

 /**
   * Search parameter: <b>failure</b>
   * <p>
   * Description: <b>Search by failure reason</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.failureReason</b><br>
   * </p>
   */
  @SearchParamDefinition(name="failure", path="Task.failureReason", description="Search by failure reason", type="token" )
  public static final String SP_FAILURE = "failure";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>failure</b>
   * <p>
   * Description: <b>Search by failure reason</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.failureReason</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FAILURE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FAILURE);

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
   * Search parameter: <b>owner</b>
   * <p>
   * Description: <b>Search by task owner</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.owner</b><br>
   * </p>
   */
  @SearchParamDefinition(name="owner", path="Task.owner", description="Search by task owner", type="reference" )
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


}

