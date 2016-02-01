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
 * A definition of behaviors to be taken in particular circumstances, often including conditions, options and other decision points.
 */
@ResourceDef(name="Protocol", profile="http://hl7.org/fhir/Profile/Protocol")
public class Protocol extends DomainResource {

    public enum ProtocolStatus {
        /**
         * This protocol is still under development
         */
        DRAFT, 
        /**
         * This protocol was authored for testing purposes (or education/evaluation/marketing)
         */
        TESTING, 
        /**
         * This protocol is undergoing review to check that it is ready for production use
         */
        REVIEW, 
        /**
         * This protocol is ready for use in production systems
         */
        ACTIVE, 
        /**
         * This protocol has been withdrawn and should no longer be used
         */
        WITHDRAWN, 
        /**
         * This protocol has been replaced and a different protocol should be used in its place
         */
        SUPERSEDED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProtocolStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("testing".equals(codeString))
          return TESTING;
        if ("review".equals(codeString))
          return REVIEW;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("withdrawn".equals(codeString))
          return WITHDRAWN;
        if ("superseded".equals(codeString))
          return SUPERSEDED;
        throw new FHIRException("Unknown ProtocolStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case TESTING: return "testing";
            case REVIEW: return "review";
            case ACTIVE: return "active";
            case WITHDRAWN: return "withdrawn";
            case SUPERSEDED: return "superseded";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/protocol-status";
            case TESTING: return "http://hl7.org/fhir/protocol-status";
            case REVIEW: return "http://hl7.org/fhir/protocol-status";
            case ACTIVE: return "http://hl7.org/fhir/protocol-status";
            case WITHDRAWN: return "http://hl7.org/fhir/protocol-status";
            case SUPERSEDED: return "http://hl7.org/fhir/protocol-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "This protocol is still under development";
            case TESTING: return "This protocol was authored for testing purposes (or education/evaluation/marketing)";
            case REVIEW: return "This protocol is undergoing review to check that it is ready for production use";
            case ACTIVE: return "This protocol is ready for use in production systems";
            case WITHDRAWN: return "This protocol has been withdrawn and should no longer be used";
            case SUPERSEDED: return "This protocol has been replaced and a different protocol should be used in its place";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case TESTING: return "Testing";
            case REVIEW: return "Review";
            case ACTIVE: return "Active";
            case WITHDRAWN: return "Withdrawn";
            case SUPERSEDED: return "Superseded";
            default: return "?";
          }
        }
    }

  public static class ProtocolStatusEnumFactory implements EnumFactory<ProtocolStatus> {
    public ProtocolStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ProtocolStatus.DRAFT;
        if ("testing".equals(codeString))
          return ProtocolStatus.TESTING;
        if ("review".equals(codeString))
          return ProtocolStatus.REVIEW;
        if ("active".equals(codeString))
          return ProtocolStatus.ACTIVE;
        if ("withdrawn".equals(codeString))
          return ProtocolStatus.WITHDRAWN;
        if ("superseded".equals(codeString))
          return ProtocolStatus.SUPERSEDED;
        throw new IllegalArgumentException("Unknown ProtocolStatus code '"+codeString+"'");
        }
        public Enumeration<ProtocolStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<ProtocolStatus>(this, ProtocolStatus.DRAFT);
        if ("testing".equals(codeString))
          return new Enumeration<ProtocolStatus>(this, ProtocolStatus.TESTING);
        if ("review".equals(codeString))
          return new Enumeration<ProtocolStatus>(this, ProtocolStatus.REVIEW);
        if ("active".equals(codeString))
          return new Enumeration<ProtocolStatus>(this, ProtocolStatus.ACTIVE);
        if ("withdrawn".equals(codeString))
          return new Enumeration<ProtocolStatus>(this, ProtocolStatus.WITHDRAWN);
        if ("superseded".equals(codeString))
          return new Enumeration<ProtocolStatus>(this, ProtocolStatus.SUPERSEDED);
        throw new FHIRException("Unknown ProtocolStatus code '"+codeString+"'");
        }
    public String toCode(ProtocolStatus code) {
      if (code == ProtocolStatus.DRAFT)
        return "draft";
      if (code == ProtocolStatus.TESTING)
        return "testing";
      if (code == ProtocolStatus.REVIEW)
        return "review";
      if (code == ProtocolStatus.ACTIVE)
        return "active";
      if (code == ProtocolStatus.WITHDRAWN)
        return "withdrawn";
      if (code == ProtocolStatus.SUPERSEDED)
        return "superseded";
      return "?";
      }
    public String toSystem(ProtocolStatus code) {
      return code.getSystem();
      }
    }

    public enum ProtocolType {
        /**
         * The protocol describes the steps to manage a particular health condition including monitoring, treatment, mitigation and/or follow-up
         */
        CONDITION, 
        /**
         * The protocol describes the appropriate use of a particular device (medical device, software, etc.)
         */
        DEVICE, 
        /**
         * The protocol describes the appropriate use of a particular medication including indications for use, dosages, treatment cycles, etc.
         */
        DRUG, 
        /**
         * The protocol describes the set of steps to occur for study subjects enrolled in an interventional study
         */
        STUDY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProtocolType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("condition".equals(codeString))
          return CONDITION;
        if ("device".equals(codeString))
          return DEVICE;
        if ("drug".equals(codeString))
          return DRUG;
        if ("study".equals(codeString))
          return STUDY;
        throw new FHIRException("Unknown ProtocolType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONDITION: return "condition";
            case DEVICE: return "device";
            case DRUG: return "drug";
            case STUDY: return "study";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CONDITION: return "http://hl7.org/fhir/protocol-type";
            case DEVICE: return "http://hl7.org/fhir/protocol-type";
            case DRUG: return "http://hl7.org/fhir/protocol-type";
            case STUDY: return "http://hl7.org/fhir/protocol-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CONDITION: return "The protocol describes the steps to manage a particular health condition including monitoring, treatment, mitigation and/or follow-up";
            case DEVICE: return "The protocol describes the appropriate use of a particular device (medical device, software, etc.)";
            case DRUG: return "The protocol describes the appropriate use of a particular medication including indications for use, dosages, treatment cycles, etc.";
            case STUDY: return "The protocol describes the set of steps to occur for study subjects enrolled in an interventional study";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONDITION: return "Condition";
            case DEVICE: return "Device";
            case DRUG: return "Drug";
            case STUDY: return "Study";
            default: return "?";
          }
        }
    }

  public static class ProtocolTypeEnumFactory implements EnumFactory<ProtocolType> {
    public ProtocolType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("condition".equals(codeString))
          return ProtocolType.CONDITION;
        if ("device".equals(codeString))
          return ProtocolType.DEVICE;
        if ("drug".equals(codeString))
          return ProtocolType.DRUG;
        if ("study".equals(codeString))
          return ProtocolType.STUDY;
        throw new IllegalArgumentException("Unknown ProtocolType code '"+codeString+"'");
        }
        public Enumeration<ProtocolType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("condition".equals(codeString))
          return new Enumeration<ProtocolType>(this, ProtocolType.CONDITION);
        if ("device".equals(codeString))
          return new Enumeration<ProtocolType>(this, ProtocolType.DEVICE);
        if ("drug".equals(codeString))
          return new Enumeration<ProtocolType>(this, ProtocolType.DRUG);
        if ("study".equals(codeString))
          return new Enumeration<ProtocolType>(this, ProtocolType.STUDY);
        throw new FHIRException("Unknown ProtocolType code '"+codeString+"'");
        }
    public String toCode(ProtocolType code) {
      if (code == ProtocolType.CONDITION)
        return "condition";
      if (code == ProtocolType.DEVICE)
        return "device";
      if (code == ProtocolType.DRUG)
        return "drug";
      if (code == ProtocolType.STUDY)
        return "study";
      return "?";
      }
    public String toSystem(ProtocolType code) {
      return code.getSystem();
      }
    }

    public enum ActivityDefinitionCategory {
        /**
         * To consume food of a specified nature
         */
        DIET, 
        /**
         * To consume/receive a drug, vaccine or other product
         */
        DRUG, 
        /**
         * To meet or communicate with the patient (in-patient, out-patient, phone call, etc.)
         */
        ENCOUNTER, 
        /**
         * To capture information about a patient (vitals, labs, diagnostic images, etc.)
         */
        OBSERVATION, 
        /**
         * To modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)
         */
        PROCEDURE, 
        /**
         * To provide something to the patient (medication, medical supply, etc.)
         */
        SUPPLY, 
        /**
         * Some other form of action
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ActivityDefinitionCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("diet".equals(codeString))
          return DIET;
        if ("drug".equals(codeString))
          return DRUG;
        if ("encounter".equals(codeString))
          return ENCOUNTER;
        if ("observation".equals(codeString))
          return OBSERVATION;
        if ("procedure".equals(codeString))
          return PROCEDURE;
        if ("supply".equals(codeString))
          return SUPPLY;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown ActivityDefinitionCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DIET: return "diet";
            case DRUG: return "drug";
            case ENCOUNTER: return "encounter";
            case OBSERVATION: return "observation";
            case PROCEDURE: return "procedure";
            case SUPPLY: return "supply";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DIET: return "http://hl7.org/fhir/activity-definition-category";
            case DRUG: return "http://hl7.org/fhir/activity-definition-category";
            case ENCOUNTER: return "http://hl7.org/fhir/activity-definition-category";
            case OBSERVATION: return "http://hl7.org/fhir/activity-definition-category";
            case PROCEDURE: return "http://hl7.org/fhir/activity-definition-category";
            case SUPPLY: return "http://hl7.org/fhir/activity-definition-category";
            case OTHER: return "http://hl7.org/fhir/activity-definition-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DIET: return "To consume food of a specified nature";
            case DRUG: return "To consume/receive a drug, vaccine or other product";
            case ENCOUNTER: return "To meet or communicate with the patient (in-patient, out-patient, phone call, etc.)";
            case OBSERVATION: return "To capture information about a patient (vitals, labs, diagnostic images, etc.)";
            case PROCEDURE: return "To modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)";
            case SUPPLY: return "To provide something to the patient (medication, medical supply, etc.)";
            case OTHER: return "Some other form of action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIET: return "Diet";
            case DRUG: return "Drug";
            case ENCOUNTER: return "Encounter";
            case OBSERVATION: return "Observation";
            case PROCEDURE: return "Procedure";
            case SUPPLY: return "Supply";
            case OTHER: return "Other";
            default: return "?";
          }
        }
    }

  public static class ActivityDefinitionCategoryEnumFactory implements EnumFactory<ActivityDefinitionCategory> {
    public ActivityDefinitionCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("diet".equals(codeString))
          return ActivityDefinitionCategory.DIET;
        if ("drug".equals(codeString))
          return ActivityDefinitionCategory.DRUG;
        if ("encounter".equals(codeString))
          return ActivityDefinitionCategory.ENCOUNTER;
        if ("observation".equals(codeString))
          return ActivityDefinitionCategory.OBSERVATION;
        if ("procedure".equals(codeString))
          return ActivityDefinitionCategory.PROCEDURE;
        if ("supply".equals(codeString))
          return ActivityDefinitionCategory.SUPPLY;
        if ("other".equals(codeString))
          return ActivityDefinitionCategory.OTHER;
        throw new IllegalArgumentException("Unknown ActivityDefinitionCategory code '"+codeString+"'");
        }
        public Enumeration<ActivityDefinitionCategory> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("diet".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.DIET);
        if ("drug".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.DRUG);
        if ("encounter".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.ENCOUNTER);
        if ("observation".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.OBSERVATION);
        if ("procedure".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.PROCEDURE);
        if ("supply".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.SUPPLY);
        if ("other".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.OTHER);
        throw new FHIRException("Unknown ActivityDefinitionCategory code '"+codeString+"'");
        }
    public String toCode(ActivityDefinitionCategory code) {
      if (code == ActivityDefinitionCategory.DIET)
        return "diet";
      if (code == ActivityDefinitionCategory.DRUG)
        return "drug";
      if (code == ActivityDefinitionCategory.ENCOUNTER)
        return "encounter";
      if (code == ActivityDefinitionCategory.OBSERVATION)
        return "observation";
      if (code == ActivityDefinitionCategory.PROCEDURE)
        return "procedure";
      if (code == ActivityDefinitionCategory.SUPPLY)
        return "supply";
      if (code == ActivityDefinitionCategory.OTHER)
        return "other";
      return "?";
      }
    public String toSystem(ActivityDefinitionCategory code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ProtocolStepComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Label for step.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Label for step", formalDefinition="Label for step." )
        protected StringType name;

        /**
         * Human description of activity.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human description of activity", formalDefinition="Human description of activity." )
        protected StringType description;

        /**
         * How long does step last?
         */
        @Child(name = "duration", type = {Duration.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How long does step last?", formalDefinition="How long does step last?" )
        protected Duration duration;

        /**
         * Rules prior to execution.
         */
        @Child(name = "precondition", type = {}, order=4, min=0, max=1, modifier=true, summary=true)
        @Description(shortDefinition="Rules prior to execution", formalDefinition="Rules prior to execution." )
        protected ProtocolStepPreconditionComponent precondition;

        /**
         * Indicates the conditions that must be met for activities that are part of this time point to terminate.
         */
        @Child(name = "exit", type = {ProtocolStepPreconditionComponent.class}, order=5, min=0, max=1, modifier=true, summary=true)
        @Description(shortDefinition="Rules prior to completion", formalDefinition="Indicates the conditions that must be met for activities that are part of this time point to terminate." )
        protected ProtocolStepPreconditionComponent exit;

        /**
         * First activity within timepoint.
         */
        @Child(name = "firstActivity", type = {UriType.class}, order=6, min=0, max=1, modifier=true, summary=true)
        @Description(shortDefinition="First activity within timepoint", formalDefinition="First activity within timepoint." )
        protected UriType firstActivity;

        /**
         * Activities that occur within timepoint.
         */
        @Child(name = "activity", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=true, summary=true)
        @Description(shortDefinition="Activities that occur within timepoint", formalDefinition="Activities that occur within timepoint." )
        protected List<ProtocolStepActivityComponent> activity;

        /**
         * What happens next?
         */
        @Child(name = "next", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="What happens next?", formalDefinition="What happens next?" )
        protected List<ProtocolStepNextComponent> next;

        private static final long serialVersionUID = 626452062L;

    /**
     * Constructor
     */
      public ProtocolStepComponent() {
        super();
      }

        /**
         * @return {@link #name} (Label for step.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepComponent.name");
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
         * @param value {@link #name} (Label for step.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ProtocolStepComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Label for step.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Label for step.
         */
        public ProtocolStepComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (Human description of activity.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepComponent.description");
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
         * @param value {@link #description} (Human description of activity.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ProtocolStepComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Human description of activity.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Human description of activity.
         */
        public ProtocolStepComponent setDescription(String value) { 
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
         * @return {@link #duration} (How long does step last?)
         */
        public Duration getDuration() { 
          if (this.duration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepComponent.duration");
            else if (Configuration.doAutoCreate())
              this.duration = new Duration(); // cc
          return this.duration;
        }

        public boolean hasDuration() { 
          return this.duration != null && !this.duration.isEmpty();
        }

        /**
         * @param value {@link #duration} (How long does step last?)
         */
        public ProtocolStepComponent setDuration(Duration value) { 
          this.duration = value;
          return this;
        }

        /**
         * @return {@link #precondition} (Rules prior to execution.)
         */
        public ProtocolStepPreconditionComponent getPrecondition() { 
          if (this.precondition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepComponent.precondition");
            else if (Configuration.doAutoCreate())
              this.precondition = new ProtocolStepPreconditionComponent(); // cc
          return this.precondition;
        }

        public boolean hasPrecondition() { 
          return this.precondition != null && !this.precondition.isEmpty();
        }

        /**
         * @param value {@link #precondition} (Rules prior to execution.)
         */
        public ProtocolStepComponent setPrecondition(ProtocolStepPreconditionComponent value) { 
          this.precondition = value;
          return this;
        }

        /**
         * @return {@link #exit} (Indicates the conditions that must be met for activities that are part of this time point to terminate.)
         */
        public ProtocolStepPreconditionComponent getExit() { 
          if (this.exit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepComponent.exit");
            else if (Configuration.doAutoCreate())
              this.exit = new ProtocolStepPreconditionComponent(); // cc
          return this.exit;
        }

        public boolean hasExit() { 
          return this.exit != null && !this.exit.isEmpty();
        }

        /**
         * @param value {@link #exit} (Indicates the conditions that must be met for activities that are part of this time point to terminate.)
         */
        public ProtocolStepComponent setExit(ProtocolStepPreconditionComponent value) { 
          this.exit = value;
          return this;
        }

        /**
         * @return {@link #firstActivity} (First activity within timepoint.). This is the underlying object with id, value and extensions. The accessor "getFirstActivity" gives direct access to the value
         */
        public UriType getFirstActivityElement() { 
          if (this.firstActivity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepComponent.firstActivity");
            else if (Configuration.doAutoCreate())
              this.firstActivity = new UriType(); // bb
          return this.firstActivity;
        }

        public boolean hasFirstActivityElement() { 
          return this.firstActivity != null && !this.firstActivity.isEmpty();
        }

        public boolean hasFirstActivity() { 
          return this.firstActivity != null && !this.firstActivity.isEmpty();
        }

        /**
         * @param value {@link #firstActivity} (First activity within timepoint.). This is the underlying object with id, value and extensions. The accessor "getFirstActivity" gives direct access to the value
         */
        public ProtocolStepComponent setFirstActivityElement(UriType value) { 
          this.firstActivity = value;
          return this;
        }

        /**
         * @return First activity within timepoint.
         */
        public String getFirstActivity() { 
          return this.firstActivity == null ? null : this.firstActivity.getValue();
        }

        /**
         * @param value First activity within timepoint.
         */
        public ProtocolStepComponent setFirstActivity(String value) { 
          if (Utilities.noString(value))
            this.firstActivity = null;
          else {
            if (this.firstActivity == null)
              this.firstActivity = new UriType();
            this.firstActivity.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #activity} (Activities that occur within timepoint.)
         */
        public List<ProtocolStepActivityComponent> getActivity() { 
          if (this.activity == null)
            this.activity = new ArrayList<ProtocolStepActivityComponent>();
          return this.activity;
        }

        public boolean hasActivity() { 
          if (this.activity == null)
            return false;
          for (ProtocolStepActivityComponent item : this.activity)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #activity} (Activities that occur within timepoint.)
         */
    // syntactic sugar
        public ProtocolStepActivityComponent addActivity() { //3
          ProtocolStepActivityComponent t = new ProtocolStepActivityComponent();
          if (this.activity == null)
            this.activity = new ArrayList<ProtocolStepActivityComponent>();
          this.activity.add(t);
          return t;
        }

    // syntactic sugar
        public ProtocolStepComponent addActivity(ProtocolStepActivityComponent t) { //3
          if (t == null)
            return this;
          if (this.activity == null)
            this.activity = new ArrayList<ProtocolStepActivityComponent>();
          this.activity.add(t);
          return this;
        }

        /**
         * @return {@link #next} (What happens next?)
         */
        public List<ProtocolStepNextComponent> getNext() { 
          if (this.next == null)
            this.next = new ArrayList<ProtocolStepNextComponent>();
          return this.next;
        }

        public boolean hasNext() { 
          if (this.next == null)
            return false;
          for (ProtocolStepNextComponent item : this.next)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #next} (What happens next?)
         */
    // syntactic sugar
        public ProtocolStepNextComponent addNext() { //3
          ProtocolStepNextComponent t = new ProtocolStepNextComponent();
          if (this.next == null)
            this.next = new ArrayList<ProtocolStepNextComponent>();
          this.next.add(t);
          return t;
        }

    // syntactic sugar
        public ProtocolStepComponent addNext(ProtocolStepNextComponent t) { //3
          if (t == null)
            return this;
          if (this.next == null)
            this.next = new ArrayList<ProtocolStepNextComponent>();
          this.next.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Label for step.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "Human description of activity.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("duration", "Duration", "How long does step last?", 0, java.lang.Integer.MAX_VALUE, duration));
          childrenList.add(new Property("precondition", "", "Rules prior to execution.", 0, java.lang.Integer.MAX_VALUE, precondition));
          childrenList.add(new Property("exit", "@Protocol.step.precondition", "Indicates the conditions that must be met for activities that are part of this time point to terminate.", 0, java.lang.Integer.MAX_VALUE, exit));
          childrenList.add(new Property("firstActivity", "uri", "First activity within timepoint.", 0, java.lang.Integer.MAX_VALUE, firstActivity));
          childrenList.add(new Property("activity", "", "Activities that occur within timepoint.", 0, java.lang.Integer.MAX_VALUE, activity));
          childrenList.add(new Property("next", "", "What happens next?", 0, java.lang.Integer.MAX_VALUE, next));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("duration"))
          this.duration = castToDuration(value); // Duration
        else if (name.equals("precondition"))
          this.precondition = (ProtocolStepPreconditionComponent) value; // ProtocolStepPreconditionComponent
        else if (name.equals("exit"))
          this.exit = (ProtocolStepPreconditionComponent) value; // ProtocolStepPreconditionComponent
        else if (name.equals("firstActivity"))
          this.firstActivity = castToUri(value); // UriType
        else if (name.equals("activity"))
          this.getActivity().add((ProtocolStepActivityComponent) value);
        else if (name.equals("next"))
          this.getNext().add((ProtocolStepNextComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.description");
        }
        else if (name.equals("duration")) {
          this.duration = new Duration();
          return this.duration;
        }
        else if (name.equals("precondition")) {
          this.precondition = new ProtocolStepPreconditionComponent();
          return this.precondition;
        }
        else if (name.equals("exit")) {
          this.exit = new ProtocolStepPreconditionComponent();
          return this.exit;
        }
        else if (name.equals("firstActivity")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.firstActivity");
        }
        else if (name.equals("activity")) {
          return addActivity();
        }
        else if (name.equals("next")) {
          return addNext();
        }
        else
          return super.addChild(name);
      }

      public ProtocolStepComponent copy() {
        ProtocolStepComponent dst = new ProtocolStepComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.duration = duration == null ? null : duration.copy();
        dst.precondition = precondition == null ? null : precondition.copy();
        dst.exit = exit == null ? null : exit.copy();
        dst.firstActivity = firstActivity == null ? null : firstActivity.copy();
        if (activity != null) {
          dst.activity = new ArrayList<ProtocolStepActivityComponent>();
          for (ProtocolStepActivityComponent i : activity)
            dst.activity.add(i.copy());
        };
        if (next != null) {
          dst.next = new ArrayList<ProtocolStepNextComponent>();
          for (ProtocolStepNextComponent i : next)
            dst.next.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProtocolStepComponent))
          return false;
        ProtocolStepComponent o = (ProtocolStepComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(description, o.description, true) && compareDeep(duration, o.duration, true)
           && compareDeep(precondition, o.precondition, true) && compareDeep(exit, o.exit, true) && compareDeep(firstActivity, o.firstActivity, true)
           && compareDeep(activity, o.activity, true) && compareDeep(next, o.next, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProtocolStepComponent))
          return false;
        ProtocolStepComponent o = (ProtocolStepComponent) other;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true) && compareValues(firstActivity, o.firstActivity, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (description == null || description.isEmpty())
           && (duration == null || duration.isEmpty()) && (precondition == null || precondition.isEmpty())
           && (exit == null || exit.isEmpty()) && (firstActivity == null || firstActivity.isEmpty())
           && (activity == null || activity.isEmpty()) && (next == null || next.isEmpty());
      }

  public String fhirType() {
    return "Protocol.step";

  }

  }

    @Block()
    public static class ProtocolStepPreconditionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Human-readable description of the condition.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Description of condition", formalDefinition="Human-readable description of the condition." )
        protected StringType description;

        /**
         * Defines the name/value pair that must hold for the condition to be met.
         */
        @Child(name = "condition", type = {}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Condition evaluated", formalDefinition="Defines the name/value pair that must hold for the condition to be met." )
        protected ProtocolStepPreconditionConditionComponent condition;

        /**
         * Lists a set of conditions that must all be met.
         */
        @Child(name = "intersection", type = {ProtocolStepPreconditionComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="And conditions", formalDefinition="Lists a set of conditions that must all be met." )
        protected List<ProtocolStepPreconditionComponent> intersection;

        /**
         * Lists alternative conditions, at least one of must be met.
         */
        @Child(name = "union", type = {ProtocolStepPreconditionComponent.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Or conditions", formalDefinition="Lists alternative conditions, at least one of must be met." )
        protected List<ProtocolStepPreconditionComponent> union;

        /**
         * Lists conditions of which none must be met.
         */
        @Child(name = "exclude", type = {ProtocolStepPreconditionComponent.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Not conditions", formalDefinition="Lists conditions of which none must be met." )
        protected List<ProtocolStepPreconditionComponent> exclude;

        private static final long serialVersionUID = -1469954145L;

    /**
     * Constructor
     */
      public ProtocolStepPreconditionComponent() {
        super();
      }

        /**
         * @return {@link #description} (Human-readable description of the condition.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepPreconditionComponent.description");
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
         * @param value {@link #description} (Human-readable description of the condition.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ProtocolStepPreconditionComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Human-readable description of the condition.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Human-readable description of the condition.
         */
        public ProtocolStepPreconditionComponent setDescription(String value) { 
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
         * @return {@link #condition} (Defines the name/value pair that must hold for the condition to be met.)
         */
        public ProtocolStepPreconditionConditionComponent getCondition() { 
          if (this.condition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepPreconditionComponent.condition");
            else if (Configuration.doAutoCreate())
              this.condition = new ProtocolStepPreconditionConditionComponent(); // cc
          return this.condition;
        }

        public boolean hasCondition() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        /**
         * @param value {@link #condition} (Defines the name/value pair that must hold for the condition to be met.)
         */
        public ProtocolStepPreconditionComponent setCondition(ProtocolStepPreconditionConditionComponent value) { 
          this.condition = value;
          return this;
        }

        /**
         * @return {@link #intersection} (Lists a set of conditions that must all be met.)
         */
        public List<ProtocolStepPreconditionComponent> getIntersection() { 
          if (this.intersection == null)
            this.intersection = new ArrayList<ProtocolStepPreconditionComponent>();
          return this.intersection;
        }

        public boolean hasIntersection() { 
          if (this.intersection == null)
            return false;
          for (ProtocolStepPreconditionComponent item : this.intersection)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #intersection} (Lists a set of conditions that must all be met.)
         */
    // syntactic sugar
        public ProtocolStepPreconditionComponent addIntersection() { //3
          ProtocolStepPreconditionComponent t = new ProtocolStepPreconditionComponent();
          if (this.intersection == null)
            this.intersection = new ArrayList<ProtocolStepPreconditionComponent>();
          this.intersection.add(t);
          return t;
        }

    // syntactic sugar
        public ProtocolStepPreconditionComponent addIntersection(ProtocolStepPreconditionComponent t) { //3
          if (t == null)
            return this;
          if (this.intersection == null)
            this.intersection = new ArrayList<ProtocolStepPreconditionComponent>();
          this.intersection.add(t);
          return this;
        }

        /**
         * @return {@link #union} (Lists alternative conditions, at least one of must be met.)
         */
        public List<ProtocolStepPreconditionComponent> getUnion() { 
          if (this.union == null)
            this.union = new ArrayList<ProtocolStepPreconditionComponent>();
          return this.union;
        }

        public boolean hasUnion() { 
          if (this.union == null)
            return false;
          for (ProtocolStepPreconditionComponent item : this.union)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #union} (Lists alternative conditions, at least one of must be met.)
         */
    // syntactic sugar
        public ProtocolStepPreconditionComponent addUnion() { //3
          ProtocolStepPreconditionComponent t = new ProtocolStepPreconditionComponent();
          if (this.union == null)
            this.union = new ArrayList<ProtocolStepPreconditionComponent>();
          this.union.add(t);
          return t;
        }

    // syntactic sugar
        public ProtocolStepPreconditionComponent addUnion(ProtocolStepPreconditionComponent t) { //3
          if (t == null)
            return this;
          if (this.union == null)
            this.union = new ArrayList<ProtocolStepPreconditionComponent>();
          this.union.add(t);
          return this;
        }

        /**
         * @return {@link #exclude} (Lists conditions of which none must be met.)
         */
        public List<ProtocolStepPreconditionComponent> getExclude() { 
          if (this.exclude == null)
            this.exclude = new ArrayList<ProtocolStepPreconditionComponent>();
          return this.exclude;
        }

        public boolean hasExclude() { 
          if (this.exclude == null)
            return false;
          for (ProtocolStepPreconditionComponent item : this.exclude)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #exclude} (Lists conditions of which none must be met.)
         */
    // syntactic sugar
        public ProtocolStepPreconditionComponent addExclude() { //3
          ProtocolStepPreconditionComponent t = new ProtocolStepPreconditionComponent();
          if (this.exclude == null)
            this.exclude = new ArrayList<ProtocolStepPreconditionComponent>();
          this.exclude.add(t);
          return t;
        }

    // syntactic sugar
        public ProtocolStepPreconditionComponent addExclude(ProtocolStepPreconditionComponent t) { //3
          if (t == null)
            return this;
          if (this.exclude == null)
            this.exclude = new ArrayList<ProtocolStepPreconditionComponent>();
          this.exclude.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "Human-readable description of the condition.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("condition", "", "Defines the name/value pair that must hold for the condition to be met.", 0, java.lang.Integer.MAX_VALUE, condition));
          childrenList.add(new Property("intersection", "@Protocol.step.precondition", "Lists a set of conditions that must all be met.", 0, java.lang.Integer.MAX_VALUE, intersection));
          childrenList.add(new Property("union", "@Protocol.step.precondition", "Lists alternative conditions, at least one of must be met.", 0, java.lang.Integer.MAX_VALUE, union));
          childrenList.add(new Property("exclude", "@Protocol.step.precondition", "Lists conditions of which none must be met.", 0, java.lang.Integer.MAX_VALUE, exclude));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("condition"))
          this.condition = (ProtocolStepPreconditionConditionComponent) value; // ProtocolStepPreconditionConditionComponent
        else if (name.equals("intersection"))
          this.getIntersection().add((ProtocolStepPreconditionComponent) value);
        else if (name.equals("union"))
          this.getUnion().add((ProtocolStepPreconditionComponent) value);
        else if (name.equals("exclude"))
          this.getExclude().add((ProtocolStepPreconditionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.description");
        }
        else if (name.equals("condition")) {
          this.condition = new ProtocolStepPreconditionConditionComponent();
          return this.condition;
        }
        else if (name.equals("intersection")) {
          return addIntersection();
        }
        else if (name.equals("union")) {
          return addUnion();
        }
        else if (name.equals("exclude")) {
          return addExclude();
        }
        else
          return super.addChild(name);
      }

      public ProtocolStepPreconditionComponent copy() {
        ProtocolStepPreconditionComponent dst = new ProtocolStepPreconditionComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.condition = condition == null ? null : condition.copy();
        if (intersection != null) {
          dst.intersection = new ArrayList<ProtocolStepPreconditionComponent>();
          for (ProtocolStepPreconditionComponent i : intersection)
            dst.intersection.add(i.copy());
        };
        if (union != null) {
          dst.union = new ArrayList<ProtocolStepPreconditionComponent>();
          for (ProtocolStepPreconditionComponent i : union)
            dst.union.add(i.copy());
        };
        if (exclude != null) {
          dst.exclude = new ArrayList<ProtocolStepPreconditionComponent>();
          for (ProtocolStepPreconditionComponent i : exclude)
            dst.exclude.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProtocolStepPreconditionComponent))
          return false;
        ProtocolStepPreconditionComponent o = (ProtocolStepPreconditionComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(condition, o.condition, true)
           && compareDeep(intersection, o.intersection, true) && compareDeep(union, o.union, true) && compareDeep(exclude, o.exclude, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProtocolStepPreconditionComponent))
          return false;
        ProtocolStepPreconditionComponent o = (ProtocolStepPreconditionComponent) other;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (description == null || description.isEmpty()) && (condition == null || condition.isEmpty())
           && (intersection == null || intersection.isEmpty()) && (union == null || union.isEmpty())
           && (exclude == null || exclude.isEmpty());
      }

  public String fhirType() {
    return "Protocol.step.precondition";

  }

  }

    @Block()
    public static class ProtocolStepPreconditionConditionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of observation, test or other assertion being evaluated by the condition.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Observation / test / assertion", formalDefinition="The type of observation, test or other assertion being evaluated by the condition." )
        protected CodeableConcept type;

        /**
         * Indicates what value the observation/test/assertion must have in order for the condition to be considered to be satisfied.
         */
        @Child(name = "value", type = {CodeableConcept.class, BooleanType.class, SimpleQuantity.class, Range.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Value needed to satisfy condition", formalDefinition="Indicates what value the observation/test/assertion must have in order for the condition to be considered to be satisfied." )
        protected Type value;

        private static final long serialVersionUID = -491121170L;

    /**
     * Constructor
     */
      public ProtocolStepPreconditionConditionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProtocolStepPreconditionConditionComponent(CodeableConcept type, Type value) {
        super();
        this.type = type;
        this.value = value;
      }

        /**
         * @return {@link #type} (The type of observation, test or other assertion being evaluated by the condition.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepPreconditionConditionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of observation, test or other assertion being evaluated by the condition.)
         */
        public ProtocolStepPreconditionConditionComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #value} (Indicates what value the observation/test/assertion must have in order for the condition to be considered to be satisfied.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (Indicates what value the observation/test/assertion must have in order for the condition to be considered to be satisfied.)
         */
        public CodeableConcept getValueCodeableConcept() throws FHIRException { 
          if (!(this.value instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.value.getClass().getName()+" was encountered");
          return (CodeableConcept) this.value;
        }

        public boolean hasValueCodeableConcept() { 
          return this.value instanceof CodeableConcept;
        }

        /**
         * @return {@link #value} (Indicates what value the observation/test/assertion must have in order for the condition to be considered to be satisfied.)
         */
        public BooleanType getValueBooleanType() throws FHIRException { 
          if (!(this.value instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (BooleanType) this.value;
        }

        public boolean hasValueBooleanType() { 
          return this.value instanceof BooleanType;
        }

        /**
         * @return {@link #value} (Indicates what value the observation/test/assertion must have in order for the condition to be considered to be satisfied.)
         */
        public SimpleQuantity getValueSimpleQuantity() throws FHIRException { 
          if (!(this.value instanceof SimpleQuantity))
            throw new FHIRException("Type mismatch: the type SimpleQuantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (SimpleQuantity) this.value;
        }

        public boolean hasValueSimpleQuantity() { 
          return this.value instanceof SimpleQuantity;
        }

        /**
         * @return {@link #value} (Indicates what value the observation/test/assertion must have in order for the condition to be considered to be satisfied.)
         */
        public Range getValueRange() throws FHIRException { 
          if (!(this.value instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Range) this.value;
        }

        public boolean hasValueRange() { 
          return this.value instanceof Range;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Indicates what value the observation/test/assertion must have in order for the condition to be considered to be satisfied.)
         */
        public ProtocolStepPreconditionConditionComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "The type of observation, test or other assertion being evaluated by the condition.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("value[x]", "CodeableConcept|boolean|SimpleQuantity|Range", "Indicates what value the observation/test/assertion must have in order for the condition to be considered to be satisfied.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("value[x]"))
          this.value = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueSimpleQuantity")) {
          this.value = new SimpleQuantity();
          return this.value;
        }
        else if (name.equals("valueRange")) {
          this.value = new Range();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public ProtocolStepPreconditionConditionComponent copy() {
        ProtocolStepPreconditionConditionComponent dst = new ProtocolStepPreconditionConditionComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProtocolStepPreconditionConditionComponent))
          return false;
        ProtocolStepPreconditionConditionComponent o = (ProtocolStepPreconditionConditionComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProtocolStepPreconditionConditionComponent))
          return false;
        ProtocolStepPreconditionConditionComponent o = (ProtocolStepPreconditionConditionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  public String fhirType() {
    return "Protocol.step.precondition.condition";

  }

  }

    @Block()
    public static class ProtocolStepActivityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * What can be done instead?
         */
        @Child(name = "alternative", type = {UriType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=true, summary=true)
        @Description(shortDefinition="What can be done instead?", formalDefinition="What can be done instead?" )
        protected List<UriType> alternative;

        /**
         * Activities that are part of this activity.
         */
        @Child(name = "component", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Activities that are part of this activity", formalDefinition="Activities that are part of this activity." )
        protected List<ProtocolStepActivityComponentComponent> component;

        /**
         * What happens next.
         */
        @Child(name = "following", type = {UriType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=true, summary=true)
        @Description(shortDefinition="What happens next", formalDefinition="What happens next." )
        protected List<UriType> following;

        /**
         * Indicates the length of time to wait between the conditions being satisfied for the activity to start and the actual start of the activity.
         */
        @Child(name = "wait", type = {Duration.class}, order=4, min=0, max=1, modifier=true, summary=true)
        @Description(shortDefinition="Pause before start", formalDefinition="Indicates the length of time to wait between the conditions being satisfied for the activity to start and the actual start of the activity." )
        protected Duration wait;

        /**
         * Information about the nature of the activity, including type, timing and other qualifiers.
         */
        @Child(name = "detail", type = {}, order=5, min=1, max=1, modifier=true, summary=true)
        @Description(shortDefinition="Details of activity", formalDefinition="Information about the nature of the activity, including type, timing and other qualifiers." )
        protected ProtocolStepActivityDetailComponent detail;

        private static final long serialVersionUID = 1430131373L;

    /**
     * Constructor
     */
      public ProtocolStepActivityComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProtocolStepActivityComponent(ProtocolStepActivityDetailComponent detail) {
        super();
        this.detail = detail;
      }

        /**
         * @return {@link #alternative} (What can be done instead?)
         */
        public List<UriType> getAlternative() { 
          if (this.alternative == null)
            this.alternative = new ArrayList<UriType>();
          return this.alternative;
        }

        public boolean hasAlternative() { 
          if (this.alternative == null)
            return false;
          for (UriType item : this.alternative)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #alternative} (What can be done instead?)
         */
    // syntactic sugar
        public UriType addAlternativeElement() {//2 
          UriType t = new UriType();
          if (this.alternative == null)
            this.alternative = new ArrayList<UriType>();
          this.alternative.add(t);
          return t;
        }

        /**
         * @param value {@link #alternative} (What can be done instead?)
         */
        public ProtocolStepActivityComponent addAlternative(String value) { //1
          UriType t = new UriType();
          t.setValue(value);
          if (this.alternative == null)
            this.alternative = new ArrayList<UriType>();
          this.alternative.add(t);
          return this;
        }

        /**
         * @param value {@link #alternative} (What can be done instead?)
         */
        public boolean hasAlternative(String value) { 
          if (this.alternative == null)
            return false;
          for (UriType v : this.alternative)
            if (v.equals(value)) // uri
              return true;
          return false;
        }

        /**
         * @return {@link #component} (Activities that are part of this activity.)
         */
        public List<ProtocolStepActivityComponentComponent> getComponent() { 
          if (this.component == null)
            this.component = new ArrayList<ProtocolStepActivityComponentComponent>();
          return this.component;
        }

        public boolean hasComponent() { 
          if (this.component == null)
            return false;
          for (ProtocolStepActivityComponentComponent item : this.component)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #component} (Activities that are part of this activity.)
         */
    // syntactic sugar
        public ProtocolStepActivityComponentComponent addComponent() { //3
          ProtocolStepActivityComponentComponent t = new ProtocolStepActivityComponentComponent();
          if (this.component == null)
            this.component = new ArrayList<ProtocolStepActivityComponentComponent>();
          this.component.add(t);
          return t;
        }

    // syntactic sugar
        public ProtocolStepActivityComponent addComponent(ProtocolStepActivityComponentComponent t) { //3
          if (t == null)
            return this;
          if (this.component == null)
            this.component = new ArrayList<ProtocolStepActivityComponentComponent>();
          this.component.add(t);
          return this;
        }

        /**
         * @return {@link #following} (What happens next.)
         */
        public List<UriType> getFollowing() { 
          if (this.following == null)
            this.following = new ArrayList<UriType>();
          return this.following;
        }

        public boolean hasFollowing() { 
          if (this.following == null)
            return false;
          for (UriType item : this.following)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #following} (What happens next.)
         */
    // syntactic sugar
        public UriType addFollowingElement() {//2 
          UriType t = new UriType();
          if (this.following == null)
            this.following = new ArrayList<UriType>();
          this.following.add(t);
          return t;
        }

        /**
         * @param value {@link #following} (What happens next.)
         */
        public ProtocolStepActivityComponent addFollowing(String value) { //1
          UriType t = new UriType();
          t.setValue(value);
          if (this.following == null)
            this.following = new ArrayList<UriType>();
          this.following.add(t);
          return this;
        }

        /**
         * @param value {@link #following} (What happens next.)
         */
        public boolean hasFollowing(String value) { 
          if (this.following == null)
            return false;
          for (UriType v : this.following)
            if (v.equals(value)) // uri
              return true;
          return false;
        }

        /**
         * @return {@link #wait} (Indicates the length of time to wait between the conditions being satisfied for the activity to start and the actual start of the activity.)
         */
        public Duration getWait() { 
          if (this.wait == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityComponent.wait");
            else if (Configuration.doAutoCreate())
              this.wait = new Duration(); // cc
          return this.wait;
        }

        public boolean hasWait() { 
          return this.wait != null && !this.wait.isEmpty();
        }

        /**
         * @param value {@link #wait} (Indicates the length of time to wait between the conditions being satisfied for the activity to start and the actual start of the activity.)
         */
        public ProtocolStepActivityComponent setWait(Duration value) { 
          this.wait = value;
          return this;
        }

        /**
         * @return {@link #detail} (Information about the nature of the activity, including type, timing and other qualifiers.)
         */
        public ProtocolStepActivityDetailComponent getDetail() { 
          if (this.detail == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityComponent.detail");
            else if (Configuration.doAutoCreate())
              this.detail = new ProtocolStepActivityDetailComponent(); // cc
          return this.detail;
        }

        public boolean hasDetail() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        /**
         * @param value {@link #detail} (Information about the nature of the activity, including type, timing and other qualifiers.)
         */
        public ProtocolStepActivityComponent setDetail(ProtocolStepActivityDetailComponent value) { 
          this.detail = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("alternative", "uri", "What can be done instead?", 0, java.lang.Integer.MAX_VALUE, alternative));
          childrenList.add(new Property("component", "", "Activities that are part of this activity.", 0, java.lang.Integer.MAX_VALUE, component));
          childrenList.add(new Property("following", "uri", "What happens next.", 0, java.lang.Integer.MAX_VALUE, following));
          childrenList.add(new Property("wait", "Duration", "Indicates the length of time to wait between the conditions being satisfied for the activity to start and the actual start of the activity.", 0, java.lang.Integer.MAX_VALUE, wait));
          childrenList.add(new Property("detail", "", "Information about the nature of the activity, including type, timing and other qualifiers.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("alternative"))
          this.getAlternative().add(castToUri(value));
        else if (name.equals("component"))
          this.getComponent().add((ProtocolStepActivityComponentComponent) value);
        else if (name.equals("following"))
          this.getFollowing().add(castToUri(value));
        else if (name.equals("wait"))
          this.wait = castToDuration(value); // Duration
        else if (name.equals("detail"))
          this.detail = (ProtocolStepActivityDetailComponent) value; // ProtocolStepActivityDetailComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("alternative")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.alternative");
        }
        else if (name.equals("component")) {
          return addComponent();
        }
        else if (name.equals("following")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.following");
        }
        else if (name.equals("wait")) {
          this.wait = new Duration();
          return this.wait;
        }
        else if (name.equals("detail")) {
          this.detail = new ProtocolStepActivityDetailComponent();
          return this.detail;
        }
        else
          return super.addChild(name);
      }

      public ProtocolStepActivityComponent copy() {
        ProtocolStepActivityComponent dst = new ProtocolStepActivityComponent();
        copyValues(dst);
        if (alternative != null) {
          dst.alternative = new ArrayList<UriType>();
          for (UriType i : alternative)
            dst.alternative.add(i.copy());
        };
        if (component != null) {
          dst.component = new ArrayList<ProtocolStepActivityComponentComponent>();
          for (ProtocolStepActivityComponentComponent i : component)
            dst.component.add(i.copy());
        };
        if (following != null) {
          dst.following = new ArrayList<UriType>();
          for (UriType i : following)
            dst.following.add(i.copy());
        };
        dst.wait = wait == null ? null : wait.copy();
        dst.detail = detail == null ? null : detail.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProtocolStepActivityComponent))
          return false;
        ProtocolStepActivityComponent o = (ProtocolStepActivityComponent) other;
        return compareDeep(alternative, o.alternative, true) && compareDeep(component, o.component, true)
           && compareDeep(following, o.following, true) && compareDeep(wait, o.wait, true) && compareDeep(detail, o.detail, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProtocolStepActivityComponent))
          return false;
        ProtocolStepActivityComponent o = (ProtocolStepActivityComponent) other;
        return compareValues(alternative, o.alternative, true) && compareValues(following, o.following, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (alternative == null || alternative.isEmpty()) && (component == null || component.isEmpty())
           && (following == null || following.isEmpty()) && (wait == null || wait.isEmpty()) && (detail == null || detail.isEmpty())
          ;
      }

  public String fhirType() {
    return "Protocol.step.activity";

  }

  }

    @Block()
    public static class ProtocolStepActivityComponentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Order of occurrence.
         */
        @Child(name = "sequence", type = {IntegerType.class}, order=1, min=0, max=1, modifier=true, summary=true)
        @Description(shortDefinition="Order of occurrence", formalDefinition="Order of occurrence." )
        protected IntegerType sequence;

        /**
         * Component activity.
         */
        @Child(name = "activity", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Component activity", formalDefinition="Component activity." )
        protected UriType activity;

        private static final long serialVersionUID = -856295616L;

    /**
     * Constructor
     */
      public ProtocolStepActivityComponentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProtocolStepActivityComponentComponent(UriType activity) {
        super();
        this.activity = activity;
      }

        /**
         * @return {@link #sequence} (Order of occurrence.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public IntegerType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityComponentComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new IntegerType(); // bb
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (Order of occurrence.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public ProtocolStepActivityComponentComponent setSequenceElement(IntegerType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return Order of occurrence.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value Order of occurrence.
         */
        public ProtocolStepActivityComponentComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new IntegerType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #activity} (Component activity.). This is the underlying object with id, value and extensions. The accessor "getActivity" gives direct access to the value
         */
        public UriType getActivityElement() { 
          if (this.activity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityComponentComponent.activity");
            else if (Configuration.doAutoCreate())
              this.activity = new UriType(); // bb
          return this.activity;
        }

        public boolean hasActivityElement() { 
          return this.activity != null && !this.activity.isEmpty();
        }

        public boolean hasActivity() { 
          return this.activity != null && !this.activity.isEmpty();
        }

        /**
         * @param value {@link #activity} (Component activity.). This is the underlying object with id, value and extensions. The accessor "getActivity" gives direct access to the value
         */
        public ProtocolStepActivityComponentComponent setActivityElement(UriType value) { 
          this.activity = value;
          return this;
        }

        /**
         * @return Component activity.
         */
        public String getActivity() { 
          return this.activity == null ? null : this.activity.getValue();
        }

        /**
         * @param value Component activity.
         */
        public ProtocolStepActivityComponentComponent setActivity(String value) { 
            if (this.activity == null)
              this.activity = new UriType();
            this.activity.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "integer", "Order of occurrence.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("activity", "uri", "Component activity.", 0, java.lang.Integer.MAX_VALUE, activity));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToInteger(value); // IntegerType
        else if (name.equals("activity"))
          this.activity = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.sequence");
        }
        else if (name.equals("activity")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.activity");
        }
        else
          return super.addChild(name);
      }

      public ProtocolStepActivityComponentComponent copy() {
        ProtocolStepActivityComponentComponent dst = new ProtocolStepActivityComponentComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.activity = activity == null ? null : activity.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProtocolStepActivityComponentComponent))
          return false;
        ProtocolStepActivityComponentComponent o = (ProtocolStepActivityComponentComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(activity, o.activity, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProtocolStepActivityComponentComponent))
          return false;
        ProtocolStepActivityComponentComponent o = (ProtocolStepActivityComponentComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(activity, o.activity, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (activity == null || activity.isEmpty())
          ;
      }

  public String fhirType() {
    return "Protocol.step.activity.component";

  }

  }

    @Block()
    public static class ProtocolStepActivityDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * High-level categorization of the type of activity.
         */
        @Child(name = "category", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="diet | drug | encounter | observation +", formalDefinition="High-level categorization of the type of activity." )
        protected Enumeration<ActivityDefinitionCategory> category;

        /**
         * Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Detail type of activity", formalDefinition="Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter." )
        protected CodeableConcept code;

        /**
         * The period, timing or frequency upon which the described activity is to occur.
         */
        @Child(name = "timing", type = {CodeableConcept.class, Timing.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="When activity is to occur", formalDefinition="The period, timing or frequency upon which the described activity is to occur." )
        protected Type timing;

        /**
         * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
         */
        @Child(name = "location", type = {Location.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Where it should happen", formalDefinition="Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc." )
        protected Reference location;

        /**
         * The actual object that is the target of the reference (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        protected Location locationTarget;

        /**
         * Identifies who's expected to be involved in the activity.
         */
        @Child(name = "performer", type = {Practitioner.class, Organization.class, RelatedPerson.class, Patient.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Who's responsible?", formalDefinition="Identifies who's expected to be involved in the activity." )
        protected List<Reference> performer;
        /**
         * The actual objects that are the target of the reference (Identifies who's expected to be involved in the activity.)
         */
        protected List<Resource> performerTarget;


        /**
         * Identifies the food, drug or other product being consumed or supplied in the activity.
         */
        @Child(name = "product", type = {Medication.class, Substance.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="What's administered/supplied", formalDefinition="Identifies the food, drug or other product being consumed or supplied in the activity." )
        protected Reference product;

        /**
         * The actual object that is the target of the reference (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        protected Resource productTarget;

        /**
         * Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How much is administered/consumed/supplied", formalDefinition="Identifies the quantity expected to be consumed at once (per dose, per meal, etc.)." )
        protected SimpleQuantity quantity;

        /**
         * This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        @Child(name = "description", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Extra info on activity occurrence", formalDefinition="This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc." )
        protected StringType description;

        private static final long serialVersionUID = 8207475L;

    /**
     * Constructor
     */
      public ProtocolStepActivityDetailComponent() {
        super();
      }

        /**
         * @return {@link #category} (High-level categorization of the type of activity.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
         */
        public Enumeration<ActivityDefinitionCategory> getCategoryElement() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityDetailComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Enumeration<ActivityDefinitionCategory>(new ActivityDefinitionCategoryEnumFactory()); // bb
          return this.category;
        }

        public boolean hasCategoryElement() { 
          return this.category != null && !this.category.isEmpty();
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (High-level categorization of the type of activity.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
         */
        public ProtocolStepActivityDetailComponent setCategoryElement(Enumeration<ActivityDefinitionCategory> value) { 
          this.category = value;
          return this;
        }

        /**
         * @return High-level categorization of the type of activity.
         */
        public ActivityDefinitionCategory getCategory() { 
          return this.category == null ? null : this.category.getValue();
        }

        /**
         * @param value High-level categorization of the type of activity.
         */
        public ProtocolStepActivityDetailComponent setCategory(ActivityDefinitionCategory value) { 
          if (value == null)
            this.category = null;
          else {
            if (this.category == null)
              this.category = new Enumeration<ActivityDefinitionCategory>(new ActivityDefinitionCategoryEnumFactory());
            this.category.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #code} (Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityDetailComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.)
         */
        public ProtocolStepActivityDetailComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public Type getTiming() { 
          return this.timing;
        }

        /**
         * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public CodeableConcept getTimingCodeableConcept() throws FHIRException { 
          if (!(this.timing instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.timing.getClass().getName()+" was encountered");
          return (CodeableConcept) this.timing;
        }

        public boolean hasTimingCodeableConcept() { 
          return this.timing instanceof CodeableConcept;
        }

        /**
         * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public Timing getTimingTiming() throws FHIRException { 
          if (!(this.timing instanceof Timing))
            throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.timing.getClass().getName()+" was encountered");
          return (Timing) this.timing;
        }

        public boolean hasTimingTiming() { 
          return this.timing instanceof Timing;
        }

        public boolean hasTiming() { 
          return this.timing != null && !this.timing.isEmpty();
        }

        /**
         * @param value {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public ProtocolStepActivityDetailComponent setTiming(Type value) { 
          this.timing = value;
          return this;
        }

        /**
         * @return {@link #location} (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        public Reference getLocation() { 
          if (this.location == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityDetailComponent.location");
            else if (Configuration.doAutoCreate())
              this.location = new Reference(); // cc
          return this.location;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        public ProtocolStepActivityDetailComponent setLocation(Reference value) { 
          this.location = value;
          return this;
        }

        /**
         * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        public Location getLocationTarget() { 
          if (this.locationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityDetailComponent.location");
            else if (Configuration.doAutoCreate())
              this.locationTarget = new Location(); // aa
          return this.locationTarget;
        }

        /**
         * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        public ProtocolStepActivityDetailComponent setLocationTarget(Location value) { 
          this.locationTarget = value;
          return this;
        }

        /**
         * @return {@link #performer} (Identifies who's expected to be involved in the activity.)
         */
        public List<Reference> getPerformer() { 
          if (this.performer == null)
            this.performer = new ArrayList<Reference>();
          return this.performer;
        }

        public boolean hasPerformer() { 
          if (this.performer == null)
            return false;
          for (Reference item : this.performer)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #performer} (Identifies who's expected to be involved in the activity.)
         */
    // syntactic sugar
        public Reference addPerformer() { //3
          Reference t = new Reference();
          if (this.performer == null)
            this.performer = new ArrayList<Reference>();
          this.performer.add(t);
          return t;
        }

    // syntactic sugar
        public ProtocolStepActivityDetailComponent addPerformer(Reference t) { //3
          if (t == null)
            return this;
          if (this.performer == null)
            this.performer = new ArrayList<Reference>();
          this.performer.add(t);
          return this;
        }

        /**
         * @return {@link #performer} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies who's expected to be involved in the activity.)
         */
        public List<Resource> getPerformerTarget() { 
          if (this.performerTarget == null)
            this.performerTarget = new ArrayList<Resource>();
          return this.performerTarget;
        }

        /**
         * @return {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        public Reference getProduct() { 
          if (this.product == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityDetailComponent.product");
            else if (Configuration.doAutoCreate())
              this.product = new Reference(); // cc
          return this.product;
        }

        public boolean hasProduct() { 
          return this.product != null && !this.product.isEmpty();
        }

        /**
         * @param value {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        public ProtocolStepActivityDetailComponent setProduct(Reference value) { 
          this.product = value;
          return this;
        }

        /**
         * @return {@link #product} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        public Resource getProductTarget() { 
          return this.productTarget;
        }

        /**
         * @param value {@link #product} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        public ProtocolStepActivityDetailComponent setProductTarget(Resource value) { 
          this.productTarget = value;
          return this;
        }

        /**
         * @return {@link #quantity} (Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityDetailComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).)
         */
        public ProtocolStepActivityDetailComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #description} (This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepActivityDetailComponent.description");
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
         * @param value {@link #description} (This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ProtocolStepActivityDetailComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        public ProtocolStepActivityDetailComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "code", "High-level categorization of the type of activity.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("code", "CodeableConcept", "Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("timing[x]", "CodeableConcept|Timing", "The period, timing or frequency upon which the described activity is to occur.", 0, java.lang.Integer.MAX_VALUE, timing));
          childrenList.add(new Property("location", "Reference(Location)", "Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("performer", "Reference(Practitioner|Organization|RelatedPerson|Patient)", "Identifies who's expected to be involved in the activity.", 0, java.lang.Integer.MAX_VALUE, performer));
          childrenList.add(new Property("product", "Reference(Medication|Substance)", "Identifies the food, drug or other product being consumed or supplied in the activity.", 0, java.lang.Integer.MAX_VALUE, product));
          childrenList.add(new Property("quantity", "SimpleQuantity", "Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("description", "string", "This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.", 0, java.lang.Integer.MAX_VALUE, description));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category"))
          this.category = new ActivityDefinitionCategoryEnumFactory().fromType(value); // Enumeration<ActivityDefinitionCategory>
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("timing[x]"))
          this.timing = (Type) value; // Type
        else if (name.equals("location"))
          this.location = castToReference(value); // Reference
        else if (name.equals("performer"))
          this.getPerformer().add(castToReference(value));
        else if (name.equals("product"))
          this.product = castToReference(value); // Reference
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.category");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("timingCodeableConcept")) {
          this.timing = new CodeableConcept();
          return this.timing;
        }
        else if (name.equals("timingTiming")) {
          this.timing = new Timing();
          return this.timing;
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("performer")) {
          return addPerformer();
        }
        else if (name.equals("product")) {
          this.product = new Reference();
          return this.product;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.description");
        }
        else
          return super.addChild(name);
      }

      public ProtocolStepActivityDetailComponent copy() {
        ProtocolStepActivityDetailComponent dst = new ProtocolStepActivityDetailComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.timing = timing == null ? null : timing.copy();
        dst.location = location == null ? null : location.copy();
        if (performer != null) {
          dst.performer = new ArrayList<Reference>();
          for (Reference i : performer)
            dst.performer.add(i.copy());
        };
        dst.product = product == null ? null : product.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProtocolStepActivityDetailComponent))
          return false;
        ProtocolStepActivityDetailComponent o = (ProtocolStepActivityDetailComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(timing, o.timing, true)
           && compareDeep(location, o.location, true) && compareDeep(performer, o.performer, true) && compareDeep(product, o.product, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(description, o.description, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProtocolStepActivityDetailComponent))
          return false;
        ProtocolStepActivityDetailComponent o = (ProtocolStepActivityDetailComponent) other;
        return compareValues(category, o.category, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (code == null || code.isEmpty())
           && (timing == null || timing.isEmpty()) && (location == null || location.isEmpty()) && (performer == null || performer.isEmpty())
           && (product == null || product.isEmpty()) && (quantity == null || quantity.isEmpty()) && (description == null || description.isEmpty())
          ;
      }

  public String fhirType() {
    return "Protocol.step.activity.detail";

  }

  }

    @Block()
    public static class ProtocolStepNextComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Description of what happens next.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Description of what happens next", formalDefinition="Description of what happens next." )
        protected StringType description;

        /**
         * Id of following step.
         */
        @Child(name = "reference", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Id of following step", formalDefinition="Id of following step." )
        protected UriType reference;

        /**
         * Condition in which next step is executed.
         */
        @Child(name = "condition", type = {ProtocolStepPreconditionComponent.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Condition in which next step is executed", formalDefinition="Condition in which next step is executed." )
        protected ProtocolStepPreconditionComponent condition;

        private static final long serialVersionUID = -1343883194L;

    /**
     * Constructor
     */
      public ProtocolStepNextComponent() {
        super();
      }

        /**
         * @return {@link #description} (Description of what happens next.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepNextComponent.description");
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
         * @param value {@link #description} (Description of what happens next.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ProtocolStepNextComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Description of what happens next.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Description of what happens next.
         */
        public ProtocolStepNextComponent setDescription(String value) { 
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
         * @return {@link #reference} (Id of following step.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
         */
        public UriType getReferenceElement() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepNextComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new UriType(); // bb
          return this.reference;
        }

        public boolean hasReferenceElement() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Id of following step.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
         */
        public ProtocolStepNextComponent setReferenceElement(UriType value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return Id of following step.
         */
        public String getReference() { 
          return this.reference == null ? null : this.reference.getValue();
        }

        /**
         * @param value Id of following step.
         */
        public ProtocolStepNextComponent setReference(String value) { 
          if (Utilities.noString(value))
            this.reference = null;
          else {
            if (this.reference == null)
              this.reference = new UriType();
            this.reference.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #condition} (Condition in which next step is executed.)
         */
        public ProtocolStepPreconditionComponent getCondition() { 
          if (this.condition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProtocolStepNextComponent.condition");
            else if (Configuration.doAutoCreate())
              this.condition = new ProtocolStepPreconditionComponent(); // cc
          return this.condition;
        }

        public boolean hasCondition() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        /**
         * @param value {@link #condition} (Condition in which next step is executed.)
         */
        public ProtocolStepNextComponent setCondition(ProtocolStepPreconditionComponent value) { 
          this.condition = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "Description of what happens next.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("reference", "uri", "Id of following step.", 0, java.lang.Integer.MAX_VALUE, reference));
          childrenList.add(new Property("condition", "@Protocol.step.precondition", "Condition in which next step is executed.", 0, java.lang.Integer.MAX_VALUE, condition));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("reference"))
          this.reference = castToUri(value); // UriType
        else if (name.equals("condition"))
          this.condition = (ProtocolStepPreconditionComponent) value; // ProtocolStepPreconditionComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.description");
        }
        else if (name.equals("reference")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.reference");
        }
        else if (name.equals("condition")) {
          this.condition = new ProtocolStepPreconditionComponent();
          return this.condition;
        }
        else
          return super.addChild(name);
      }

      public ProtocolStepNextComponent copy() {
        ProtocolStepNextComponent dst = new ProtocolStepNextComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.reference = reference == null ? null : reference.copy();
        dst.condition = condition == null ? null : condition.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProtocolStepNextComponent))
          return false;
        ProtocolStepNextComponent o = (ProtocolStepNextComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(reference, o.reference, true)
           && compareDeep(condition, o.condition, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProtocolStepNextComponent))
          return false;
        ProtocolStepNextComponent o = (ProtocolStepNextComponent) other;
        return compareValues(description, o.description, true) && compareValues(reference, o.reference, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (description == null || description.isEmpty()) && (reference == null || reference.isEmpty())
           && (condition == null || condition.isEmpty());
      }

  public String fhirType() {
    return "Protocol.step.next";

  }

  }

    /**
     * A unique identifier for the protocol instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique Id for this particular protocol", formalDefinition="A unique identifier for the protocol instance." )
    protected List<Identifier> identifier;

    /**
     * Name of protocol.
     */
    @Child(name = "title", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of protocol", formalDefinition="Name of protocol." )
    protected StringType title;

    /**
     * The status of the protocol.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | testing | review | active | withdrawn | superseded", formalDefinition="The status of the protocol." )
    protected Enumeration<ProtocolStatus> status;

    /**
     * A code that classifies the general type of context to which these behavior definitions apply.  This is used for searching, sorting and display purposes.
     */
    @Child(name = "type", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="condition | device | drug | study", formalDefinition="A code that classifies the general type of context to which these behavior definitions apply.  This is used for searching, sorting and display purposes." )
    protected Enumeration<ProtocolType> type;

    /**
     * What does protocol deal with?
     */
    @Child(name = "subject", type = {Condition.class, Device.class, Medication.class}, order=4, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="What does protocol deal with?", formalDefinition="What does protocol deal with?" )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (What does protocol deal with?)
     */
    protected Resource subjectTarget;

    /**
     * To whom does Protocol apply?
     */
    @Child(name = "group", type = {Group.class}, order=5, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="To whom does Protocol apply?", formalDefinition="To whom does Protocol apply?" )
    protected Reference group;

    /**
     * The actual object that is the target of the reference (To whom does Protocol apply?)
     */
    protected Group groupTarget;

    /**
     * When is protocol to be used?
     */
    @Child(name = "purpose", type = {StringType.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When is protocol to be used?", formalDefinition="When is protocol to be used?" )
    protected StringType purpose;

    /**
     * Who wrote protocol?
     */
    @Child(name = "author", type = {Organization.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who wrote protocol?", formalDefinition="Who wrote protocol?" )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Who wrote protocol?)
     */
    protected Organization authorTarget;

    /**
     * What's done as part of protocol.
     */
    @Child(name = "step", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What's done as part of protocol", formalDefinition="What's done as part of protocol." )
    protected List<ProtocolStepComponent> step;

    private static final long serialVersionUID = -1458830869L;

  /**
   * Constructor
   */
    public Protocol() {
      super();
    }

  /**
   * Constructor
   */
    public Protocol(Enumeration<ProtocolStatus> status, Enumeration<ProtocolType> type, StringType purpose) {
      super();
      this.status = status;
      this.type = type;
      this.purpose = purpose;
    }

    /**
     * @return {@link #identifier} (A unique identifier for the protocol instance.)
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
     * @return {@link #identifier} (A unique identifier for the protocol instance.)
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
    public Protocol addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #title} (Name of protocol.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Protocol.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (Name of protocol.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public Protocol setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return Name of protocol.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value Name of protocol.
     */
    public Protocol setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (The status of the protocol.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ProtocolStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Protocol.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ProtocolStatus>(new ProtocolStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the protocol.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Protocol setStatusElement(Enumeration<ProtocolStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the protocol.
     */
    public ProtocolStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the protocol.
     */
    public Protocol setStatus(ProtocolStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ProtocolStatus>(new ProtocolStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (A code that classifies the general type of context to which these behavior definitions apply.  This is used for searching, sorting and display purposes.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<ProtocolType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Protocol.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<ProtocolType>(new ProtocolTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (A code that classifies the general type of context to which these behavior definitions apply.  This is used for searching, sorting and display purposes.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Protocol setTypeElement(Enumeration<ProtocolType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return A code that classifies the general type of context to which these behavior definitions apply.  This is used for searching, sorting and display purposes.
     */
    public ProtocolType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value A code that classifies the general type of context to which these behavior definitions apply.  This is used for searching, sorting and display purposes.
     */
    public Protocol setType(ProtocolType value) { 
        if (this.type == null)
          this.type = new Enumeration<ProtocolType>(new ProtocolTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #subject} (What does protocol deal with?)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Protocol.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (What does protocol deal with?)
     */
    public Protocol setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (What does protocol deal with?)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (What does protocol deal with?)
     */
    public Protocol setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #group} (To whom does Protocol apply?)
     */
    public Reference getGroup() { 
      if (this.group == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Protocol.group");
        else if (Configuration.doAutoCreate())
          this.group = new Reference(); // cc
      return this.group;
    }

    public boolean hasGroup() { 
      return this.group != null && !this.group.isEmpty();
    }

    /**
     * @param value {@link #group} (To whom does Protocol apply?)
     */
    public Protocol setGroup(Reference value) { 
      this.group = value;
      return this;
    }

    /**
     * @return {@link #group} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (To whom does Protocol apply?)
     */
    public Group getGroupTarget() { 
      if (this.groupTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Protocol.group");
        else if (Configuration.doAutoCreate())
          this.groupTarget = new Group(); // aa
      return this.groupTarget;
    }

    /**
     * @param value {@link #group} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (To whom does Protocol apply?)
     */
    public Protocol setGroupTarget(Group value) { 
      this.groupTarget = value;
      return this;
    }

    /**
     * @return {@link #purpose} (When is protocol to be used?). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public StringType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Protocol.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new StringType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (When is protocol to be used?). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public Protocol setPurposeElement(StringType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return When is protocol to be used?
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value When is protocol to be used?
     */
    public Protocol setPurpose(String value) { 
        if (this.purpose == null)
          this.purpose = new StringType();
        this.purpose.setValue(value);
      return this;
    }

    /**
     * @return {@link #author} (Who wrote protocol?)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Protocol.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Who wrote protocol?)
     */
    public Protocol setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who wrote protocol?)
     */
    public Organization getAuthorTarget() { 
      if (this.authorTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Protocol.author");
        else if (Configuration.doAutoCreate())
          this.authorTarget = new Organization(); // aa
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who wrote protocol?)
     */
    public Protocol setAuthorTarget(Organization value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #step} (What's done as part of protocol.)
     */
    public List<ProtocolStepComponent> getStep() { 
      if (this.step == null)
        this.step = new ArrayList<ProtocolStepComponent>();
      return this.step;
    }

    public boolean hasStep() { 
      if (this.step == null)
        return false;
      for (ProtocolStepComponent item : this.step)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #step} (What's done as part of protocol.)
     */
    // syntactic sugar
    public ProtocolStepComponent addStep() { //3
      ProtocolStepComponent t = new ProtocolStepComponent();
      if (this.step == null)
        this.step = new ArrayList<ProtocolStepComponent>();
      this.step.add(t);
      return t;
    }

    // syntactic sugar
    public Protocol addStep(ProtocolStepComponent t) { //3
      if (t == null)
        return this;
      if (this.step == null)
        this.step = new ArrayList<ProtocolStepComponent>();
      this.step.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A unique identifier for the protocol instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("title", "string", "Name of protocol.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of the protocol.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("type", "code", "A code that classifies the general type of context to which these behavior definitions apply.  This is used for searching, sorting and display purposes.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subject", "Reference(Condition|Device|Medication)", "What does protocol deal with?", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("group", "Reference(Group)", "To whom does Protocol apply?", 0, java.lang.Integer.MAX_VALUE, group));
        childrenList.add(new Property("purpose", "string", "When is protocol to be used?", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("author", "Reference(Organization)", "Who wrote protocol?", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("step", "", "What's done as part of protocol.", 0, java.lang.Integer.MAX_VALUE, step));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new ProtocolStatusEnumFactory().fromType(value); // Enumeration<ProtocolStatus>
        else if (name.equals("type"))
          this.type = new ProtocolTypeEnumFactory().fromType(value); // Enumeration<ProtocolType>
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("group"))
          this.group = castToReference(value); // Reference
        else if (name.equals("purpose"))
          this.purpose = castToString(value); // StringType
        else if (name.equals("author"))
          this.author = castToReference(value); // Reference
        else if (name.equals("step"))
          this.getStep().add((ProtocolStepComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.status");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.type");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("group")) {
          this.group = new Reference();
          return this.group;
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type Protocol.purpose");
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("step")) {
          return addStep();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Protocol";

  }

      public Protocol copy() {
        Protocol dst = new Protocol();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.title = title == null ? null : title.copy();
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.group = group == null ? null : group.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.author = author == null ? null : author.copy();
        if (step != null) {
          dst.step = new ArrayList<ProtocolStepComponent>();
          for (ProtocolStepComponent i : step)
            dst.step.add(i.copy());
        };
        return dst;
      }

      protected Protocol typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Protocol))
          return false;
        Protocol o = (Protocol) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(title, o.title, true) && compareDeep(status, o.status, true)
           && compareDeep(type, o.type, true) && compareDeep(subject, o.subject, true) && compareDeep(group, o.group, true)
           && compareDeep(purpose, o.purpose, true) && compareDeep(author, o.author, true) && compareDeep(step, o.step, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Protocol))
          return false;
        Protocol o = (Protocol) other;
        return compareValues(title, o.title, true) && compareValues(status, o.status, true) && compareValues(type, o.type, true)
           && compareValues(purpose, o.purpose, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (title == null || title.isEmpty())
           && (status == null || status.isEmpty()) && (type == null || type.isEmpty()) && (subject == null || subject.isEmpty())
           && (group == null || group.isEmpty()) && (purpose == null || purpose.isEmpty()) && (author == null || author.isEmpty())
           && (step == null || step.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Protocol;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The unique id for a particular protocol</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Protocol.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Protocol.identifier", description="The unique id for a particular protocol", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The unique id for a particular protocol</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Protocol.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Protocols with specified subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Protocol.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Protocol.subject", description="Protocols with specified subject", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Protocols with specified subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Protocol.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Protocol:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Protocol:subject").toLocked();


}

