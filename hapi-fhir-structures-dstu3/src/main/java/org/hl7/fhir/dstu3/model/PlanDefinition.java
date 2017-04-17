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
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. The resource is general enough to support the description of a broad range of clinical artifacts such as clinical decision support rules, order sets and protocols.
 */
@ResourceDef(name="PlanDefinition", profile="http://hl7.org/fhir/Profile/PlanDefinition")
@ChildOrder(names={"url", "identifier", "version", "name", "title", "type", "status", "experimental", "date", "publisher", "description", "purpose", "usage", "approvalDate", "lastReviewDate", "effectivePeriod", "useContext", "jurisdiction", "topic", "contributor", "contact", "copyright", "relatedArtifact", "library", "goal", "action"})
public class PlanDefinition extends MetadataResource {

    public enum ActionConditionKind {
        /**
         * The condition describes whether or not a given action is applicable
         */
        APPLICABILITY, 
        /**
         * The condition is a starting condition for the action
         */
        START, 
        /**
         * The condition is a stop, or exit condition for the action
         */
        STOP, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActionConditionKind fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("applicability".equals(codeString))
          return APPLICABILITY;
        if ("start".equals(codeString))
          return START;
        if ("stop".equals(codeString))
          return STOP;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActionConditionKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case APPLICABILITY: return "applicability";
            case START: return "start";
            case STOP: return "stop";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case APPLICABILITY: return "http://hl7.org/fhir/action-condition-kind";
            case START: return "http://hl7.org/fhir/action-condition-kind";
            case STOP: return "http://hl7.org/fhir/action-condition-kind";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case APPLICABILITY: return "The condition describes whether or not a given action is applicable";
            case START: return "The condition is a starting condition for the action";
            case STOP: return "The condition is a stop, or exit condition for the action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case APPLICABILITY: return "Applicability";
            case START: return "Start";
            case STOP: return "Stop";
            default: return "?";
          }
        }
    }

  public static class ActionConditionKindEnumFactory implements EnumFactory<ActionConditionKind> {
    public ActionConditionKind fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("applicability".equals(codeString))
          return ActionConditionKind.APPLICABILITY;
        if ("start".equals(codeString))
          return ActionConditionKind.START;
        if ("stop".equals(codeString))
          return ActionConditionKind.STOP;
        throw new IllegalArgumentException("Unknown ActionConditionKind code '"+codeString+"'");
        }
        public Enumeration<ActionConditionKind> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActionConditionKind>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("applicability".equals(codeString))
          return new Enumeration<ActionConditionKind>(this, ActionConditionKind.APPLICABILITY);
        if ("start".equals(codeString))
          return new Enumeration<ActionConditionKind>(this, ActionConditionKind.START);
        if ("stop".equals(codeString))
          return new Enumeration<ActionConditionKind>(this, ActionConditionKind.STOP);
        throw new FHIRException("Unknown ActionConditionKind code '"+codeString+"'");
        }
    public String toCode(ActionConditionKind code) {
      if (code == ActionConditionKind.APPLICABILITY)
        return "applicability";
      if (code == ActionConditionKind.START)
        return "start";
      if (code == ActionConditionKind.STOP)
        return "stop";
      return "?";
      }
    public String toSystem(ActionConditionKind code) {
      return code.getSystem();
      }
    }

    public enum ActionRelationshipType {
        /**
         * The action must be performed before the start of the related action
         */
        BEFORESTART, 
        /**
         * The action must be performed before the related action
         */
        BEFORE, 
        /**
         * The action must be performed before the end of the related action
         */
        BEFOREEND, 
        /**
         * The action must be performed concurrent with the start of the related action
         */
        CONCURRENTWITHSTART, 
        /**
         * The action must be performed concurrent with the related action
         */
        CONCURRENT, 
        /**
         * The action must be performed concurrent with the end of the related action
         */
        CONCURRENTWITHEND, 
        /**
         * The action must be performed after the start of the related action
         */
        AFTERSTART, 
        /**
         * The action must be performed after the related action
         */
        AFTER, 
        /**
         * The action must be performed after the end of the related action
         */
        AFTEREND, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActionRelationshipType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("before-start".equals(codeString))
          return BEFORESTART;
        if ("before".equals(codeString))
          return BEFORE;
        if ("before-end".equals(codeString))
          return BEFOREEND;
        if ("concurrent-with-start".equals(codeString))
          return CONCURRENTWITHSTART;
        if ("concurrent".equals(codeString))
          return CONCURRENT;
        if ("concurrent-with-end".equals(codeString))
          return CONCURRENTWITHEND;
        if ("after-start".equals(codeString))
          return AFTERSTART;
        if ("after".equals(codeString))
          return AFTER;
        if ("after-end".equals(codeString))
          return AFTEREND;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActionRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BEFORESTART: return "before-start";
            case BEFORE: return "before";
            case BEFOREEND: return "before-end";
            case CONCURRENTWITHSTART: return "concurrent-with-start";
            case CONCURRENT: return "concurrent";
            case CONCURRENTWITHEND: return "concurrent-with-end";
            case AFTERSTART: return "after-start";
            case AFTER: return "after";
            case AFTEREND: return "after-end";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case BEFORESTART: return "http://hl7.org/fhir/action-relationship-type";
            case BEFORE: return "http://hl7.org/fhir/action-relationship-type";
            case BEFOREEND: return "http://hl7.org/fhir/action-relationship-type";
            case CONCURRENTWITHSTART: return "http://hl7.org/fhir/action-relationship-type";
            case CONCURRENT: return "http://hl7.org/fhir/action-relationship-type";
            case CONCURRENTWITHEND: return "http://hl7.org/fhir/action-relationship-type";
            case AFTERSTART: return "http://hl7.org/fhir/action-relationship-type";
            case AFTER: return "http://hl7.org/fhir/action-relationship-type";
            case AFTEREND: return "http://hl7.org/fhir/action-relationship-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case BEFORESTART: return "The action must be performed before the start of the related action";
            case BEFORE: return "The action must be performed before the related action";
            case BEFOREEND: return "The action must be performed before the end of the related action";
            case CONCURRENTWITHSTART: return "The action must be performed concurrent with the start of the related action";
            case CONCURRENT: return "The action must be performed concurrent with the related action";
            case CONCURRENTWITHEND: return "The action must be performed concurrent with the end of the related action";
            case AFTERSTART: return "The action must be performed after the start of the related action";
            case AFTER: return "The action must be performed after the related action";
            case AFTEREND: return "The action must be performed after the end of the related action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BEFORESTART: return "Before Start";
            case BEFORE: return "Before";
            case BEFOREEND: return "Before End";
            case CONCURRENTWITHSTART: return "Concurrent With Start";
            case CONCURRENT: return "Concurrent";
            case CONCURRENTWITHEND: return "Concurrent With End";
            case AFTERSTART: return "After Start";
            case AFTER: return "After";
            case AFTEREND: return "After End";
            default: return "?";
          }
        }
    }

  public static class ActionRelationshipTypeEnumFactory implements EnumFactory<ActionRelationshipType> {
    public ActionRelationshipType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("before-start".equals(codeString))
          return ActionRelationshipType.BEFORESTART;
        if ("before".equals(codeString))
          return ActionRelationshipType.BEFORE;
        if ("before-end".equals(codeString))
          return ActionRelationshipType.BEFOREEND;
        if ("concurrent-with-start".equals(codeString))
          return ActionRelationshipType.CONCURRENTWITHSTART;
        if ("concurrent".equals(codeString))
          return ActionRelationshipType.CONCURRENT;
        if ("concurrent-with-end".equals(codeString))
          return ActionRelationshipType.CONCURRENTWITHEND;
        if ("after-start".equals(codeString))
          return ActionRelationshipType.AFTERSTART;
        if ("after".equals(codeString))
          return ActionRelationshipType.AFTER;
        if ("after-end".equals(codeString))
          return ActionRelationshipType.AFTEREND;
        throw new IllegalArgumentException("Unknown ActionRelationshipType code '"+codeString+"'");
        }
        public Enumeration<ActionRelationshipType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActionRelationshipType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("before-start".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.BEFORESTART);
        if ("before".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.BEFORE);
        if ("before-end".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.BEFOREEND);
        if ("concurrent-with-start".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.CONCURRENTWITHSTART);
        if ("concurrent".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.CONCURRENT);
        if ("concurrent-with-end".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.CONCURRENTWITHEND);
        if ("after-start".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.AFTERSTART);
        if ("after".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.AFTER);
        if ("after-end".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.AFTEREND);
        throw new FHIRException("Unknown ActionRelationshipType code '"+codeString+"'");
        }
    public String toCode(ActionRelationshipType code) {
      if (code == ActionRelationshipType.BEFORESTART)
        return "before-start";
      if (code == ActionRelationshipType.BEFORE)
        return "before";
      if (code == ActionRelationshipType.BEFOREEND)
        return "before-end";
      if (code == ActionRelationshipType.CONCURRENTWITHSTART)
        return "concurrent-with-start";
      if (code == ActionRelationshipType.CONCURRENT)
        return "concurrent";
      if (code == ActionRelationshipType.CONCURRENTWITHEND)
        return "concurrent-with-end";
      if (code == ActionRelationshipType.AFTERSTART)
        return "after-start";
      if (code == ActionRelationshipType.AFTER)
        return "after";
      if (code == ActionRelationshipType.AFTEREND)
        return "after-end";
      return "?";
      }
    public String toSystem(ActionRelationshipType code) {
      return code.getSystem();
      }
    }

    public enum ActionParticipantType {
        /**
         * The participant is the patient under evaluation
         */
        PATIENT, 
        /**
         * The participant is a practitioner involved in the patient's care
         */
        PRACTITIONER, 
        /**
         * The participant is a person related to the patient
         */
        RELATEDPERSON, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActionParticipantType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return PATIENT;
        if ("practitioner".equals(codeString))
          return PRACTITIONER;
        if ("related-person".equals(codeString))
          return RELATEDPERSON;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActionParticipantType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "patient";
            case PRACTITIONER: return "practitioner";
            case RELATEDPERSON: return "related-person";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PATIENT: return "http://hl7.org/fhir/action-participant-type";
            case PRACTITIONER: return "http://hl7.org/fhir/action-participant-type";
            case RELATEDPERSON: return "http://hl7.org/fhir/action-participant-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "The participant is the patient under evaluation";
            case PRACTITIONER: return "The participant is a practitioner involved in the patient's care";
            case RELATEDPERSON: return "The participant is a person related to the patient";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "Patient";
            case PRACTITIONER: return "Practitioner";
            case RELATEDPERSON: return "Related Person";
            default: return "?";
          }
        }
    }

  public static class ActionParticipantTypeEnumFactory implements EnumFactory<ActionParticipantType> {
    public ActionParticipantType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return ActionParticipantType.PATIENT;
        if ("practitioner".equals(codeString))
          return ActionParticipantType.PRACTITIONER;
        if ("related-person".equals(codeString))
          return ActionParticipantType.RELATEDPERSON;
        throw new IllegalArgumentException("Unknown ActionParticipantType code '"+codeString+"'");
        }
        public Enumeration<ActionParticipantType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActionParticipantType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("patient".equals(codeString))
          return new Enumeration<ActionParticipantType>(this, ActionParticipantType.PATIENT);
        if ("practitioner".equals(codeString))
          return new Enumeration<ActionParticipantType>(this, ActionParticipantType.PRACTITIONER);
        if ("related-person".equals(codeString))
          return new Enumeration<ActionParticipantType>(this, ActionParticipantType.RELATEDPERSON);
        throw new FHIRException("Unknown ActionParticipantType code '"+codeString+"'");
        }
    public String toCode(ActionParticipantType code) {
      if (code == ActionParticipantType.PATIENT)
        return "patient";
      if (code == ActionParticipantType.PRACTITIONER)
        return "practitioner";
      if (code == ActionParticipantType.RELATEDPERSON)
        return "related-person";
      return "?";
      }
    public String toSystem(ActionParticipantType code) {
      return code.getSystem();
      }
    }

    public enum ActionGroupingBehavior {
        /**
         * Any group marked with this behavior should be displayed as a visual group to the end user
         */
        VISUALGROUP, 
        /**
         * A group with this behavior logically groups its sub-elements, and may be shown as a visual group to the end user, but it is not required to do so
         */
        LOGICALGROUP, 
        /**
         * A group of related alternative actions is a sentence group if the target referenced by the action is the same in all the actions and each action simply constitutes a different variation on how to specify the details for the target. For example, two actions that could be in a SentenceGroup are "aspirin, 500 mg, 2 times per day" and "aspirin, 300 mg, 3 times per day". In both cases, aspirin is the target referenced by the action, and the two actions represent different options for how aspirin might be ordered for the patient. Note that a SentenceGroup would almost always have an associated selection behavior of "AtMostOne", unless it's a required action, in which case, it would be "ExactlyOne"
         */
        SENTENCEGROUP, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActionGroupingBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("visual-group".equals(codeString))
          return VISUALGROUP;
        if ("logical-group".equals(codeString))
          return LOGICALGROUP;
        if ("sentence-group".equals(codeString))
          return SENTENCEGROUP;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActionGroupingBehavior code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case VISUALGROUP: return "visual-group";
            case LOGICALGROUP: return "logical-group";
            case SENTENCEGROUP: return "sentence-group";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case VISUALGROUP: return "http://hl7.org/fhir/action-grouping-behavior";
            case LOGICALGROUP: return "http://hl7.org/fhir/action-grouping-behavior";
            case SENTENCEGROUP: return "http://hl7.org/fhir/action-grouping-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case VISUALGROUP: return "Any group marked with this behavior should be displayed as a visual group to the end user";
            case LOGICALGROUP: return "A group with this behavior logically groups its sub-elements, and may be shown as a visual group to the end user, but it is not required to do so";
            case SENTENCEGROUP: return "A group of related alternative actions is a sentence group if the target referenced by the action is the same in all the actions and each action simply constitutes a different variation on how to specify the details for the target. For example, two actions that could be in a SentenceGroup are \"aspirin, 500 mg, 2 times per day\" and \"aspirin, 300 mg, 3 times per day\". In both cases, aspirin is the target referenced by the action, and the two actions represent different options for how aspirin might be ordered for the patient. Note that a SentenceGroup would almost always have an associated selection behavior of \"AtMostOne\", unless it's a required action, in which case, it would be \"ExactlyOne\"";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case VISUALGROUP: return "Visual Group";
            case LOGICALGROUP: return "Logical Group";
            case SENTENCEGROUP: return "Sentence Group";
            default: return "?";
          }
        }
    }

  public static class ActionGroupingBehaviorEnumFactory implements EnumFactory<ActionGroupingBehavior> {
    public ActionGroupingBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("visual-group".equals(codeString))
          return ActionGroupingBehavior.VISUALGROUP;
        if ("logical-group".equals(codeString))
          return ActionGroupingBehavior.LOGICALGROUP;
        if ("sentence-group".equals(codeString))
          return ActionGroupingBehavior.SENTENCEGROUP;
        throw new IllegalArgumentException("Unknown ActionGroupingBehavior code '"+codeString+"'");
        }
        public Enumeration<ActionGroupingBehavior> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActionGroupingBehavior>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("visual-group".equals(codeString))
          return new Enumeration<ActionGroupingBehavior>(this, ActionGroupingBehavior.VISUALGROUP);
        if ("logical-group".equals(codeString))
          return new Enumeration<ActionGroupingBehavior>(this, ActionGroupingBehavior.LOGICALGROUP);
        if ("sentence-group".equals(codeString))
          return new Enumeration<ActionGroupingBehavior>(this, ActionGroupingBehavior.SENTENCEGROUP);
        throw new FHIRException("Unknown ActionGroupingBehavior code '"+codeString+"'");
        }
    public String toCode(ActionGroupingBehavior code) {
      if (code == ActionGroupingBehavior.VISUALGROUP)
        return "visual-group";
      if (code == ActionGroupingBehavior.LOGICALGROUP)
        return "logical-group";
      if (code == ActionGroupingBehavior.SENTENCEGROUP)
        return "sentence-group";
      return "?";
      }
    public String toSystem(ActionGroupingBehavior code) {
      return code.getSystem();
      }
    }

    public enum ActionSelectionBehavior {
        /**
         * Any number of the actions in the group may be chosen, from zero to all
         */
        ANY, 
        /**
         * All the actions in the group must be selected as a single unit
         */
        ALL, 
        /**
         * All the actions in the group are meant to be chosen as a single unit: either all must be selected by the end user, or none may be selected
         */
        ALLORNONE, 
        /**
         * The end user must choose one and only one of the selectable actions in the group. The user may not choose none of the actions in the group
         */
        EXACTLYONE, 
        /**
         * The end user may choose zero or at most one of the actions in the group
         */
        ATMOSTONE, 
        /**
         * The end user must choose a minimum of one, and as many additional as desired
         */
        ONEORMORE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActionSelectionBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("any".equals(codeString))
          return ANY;
        if ("all".equals(codeString))
          return ALL;
        if ("all-or-none".equals(codeString))
          return ALLORNONE;
        if ("exactly-one".equals(codeString))
          return EXACTLYONE;
        if ("at-most-one".equals(codeString))
          return ATMOSTONE;
        if ("one-or-more".equals(codeString))
          return ONEORMORE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActionSelectionBehavior code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ANY: return "any";
            case ALL: return "all";
            case ALLORNONE: return "all-or-none";
            case EXACTLYONE: return "exactly-one";
            case ATMOSTONE: return "at-most-one";
            case ONEORMORE: return "one-or-more";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ANY: return "http://hl7.org/fhir/action-selection-behavior";
            case ALL: return "http://hl7.org/fhir/action-selection-behavior";
            case ALLORNONE: return "http://hl7.org/fhir/action-selection-behavior";
            case EXACTLYONE: return "http://hl7.org/fhir/action-selection-behavior";
            case ATMOSTONE: return "http://hl7.org/fhir/action-selection-behavior";
            case ONEORMORE: return "http://hl7.org/fhir/action-selection-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ANY: return "Any number of the actions in the group may be chosen, from zero to all";
            case ALL: return "All the actions in the group must be selected as a single unit";
            case ALLORNONE: return "All the actions in the group are meant to be chosen as a single unit: either all must be selected by the end user, or none may be selected";
            case EXACTLYONE: return "The end user must choose one and only one of the selectable actions in the group. The user may not choose none of the actions in the group";
            case ATMOSTONE: return "The end user may choose zero or at most one of the actions in the group";
            case ONEORMORE: return "The end user must choose a minimum of one, and as many additional as desired";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ANY: return "Any";
            case ALL: return "All";
            case ALLORNONE: return "All Or None";
            case EXACTLYONE: return "Exactly One";
            case ATMOSTONE: return "At Most One";
            case ONEORMORE: return "One Or More";
            default: return "?";
          }
        }
    }

  public static class ActionSelectionBehaviorEnumFactory implements EnumFactory<ActionSelectionBehavior> {
    public ActionSelectionBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("any".equals(codeString))
          return ActionSelectionBehavior.ANY;
        if ("all".equals(codeString))
          return ActionSelectionBehavior.ALL;
        if ("all-or-none".equals(codeString))
          return ActionSelectionBehavior.ALLORNONE;
        if ("exactly-one".equals(codeString))
          return ActionSelectionBehavior.EXACTLYONE;
        if ("at-most-one".equals(codeString))
          return ActionSelectionBehavior.ATMOSTONE;
        if ("one-or-more".equals(codeString))
          return ActionSelectionBehavior.ONEORMORE;
        throw new IllegalArgumentException("Unknown ActionSelectionBehavior code '"+codeString+"'");
        }
        public Enumeration<ActionSelectionBehavior> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActionSelectionBehavior>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("any".equals(codeString))
          return new Enumeration<ActionSelectionBehavior>(this, ActionSelectionBehavior.ANY);
        if ("all".equals(codeString))
          return new Enumeration<ActionSelectionBehavior>(this, ActionSelectionBehavior.ALL);
        if ("all-or-none".equals(codeString))
          return new Enumeration<ActionSelectionBehavior>(this, ActionSelectionBehavior.ALLORNONE);
        if ("exactly-one".equals(codeString))
          return new Enumeration<ActionSelectionBehavior>(this, ActionSelectionBehavior.EXACTLYONE);
        if ("at-most-one".equals(codeString))
          return new Enumeration<ActionSelectionBehavior>(this, ActionSelectionBehavior.ATMOSTONE);
        if ("one-or-more".equals(codeString))
          return new Enumeration<ActionSelectionBehavior>(this, ActionSelectionBehavior.ONEORMORE);
        throw new FHIRException("Unknown ActionSelectionBehavior code '"+codeString+"'");
        }
    public String toCode(ActionSelectionBehavior code) {
      if (code == ActionSelectionBehavior.ANY)
        return "any";
      if (code == ActionSelectionBehavior.ALL)
        return "all";
      if (code == ActionSelectionBehavior.ALLORNONE)
        return "all-or-none";
      if (code == ActionSelectionBehavior.EXACTLYONE)
        return "exactly-one";
      if (code == ActionSelectionBehavior.ATMOSTONE)
        return "at-most-one";
      if (code == ActionSelectionBehavior.ONEORMORE)
        return "one-or-more";
      return "?";
      }
    public String toSystem(ActionSelectionBehavior code) {
      return code.getSystem();
      }
    }

    public enum ActionRequiredBehavior {
        /**
         * An action with this behavior must be included in the actions processed by the end user; the end user may not choose not to include this action
         */
        MUST, 
        /**
         * An action with this behavior may be included in the set of actions processed by the end user
         */
        COULD, 
        /**
         * An action with this behavior must be included in the set of actions processed by the end user, unless the end user provides documentation as to why the action was not included
         */
        MUSTUNLESSDOCUMENTED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActionRequiredBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("must".equals(codeString))
          return MUST;
        if ("could".equals(codeString))
          return COULD;
        if ("must-unless-documented".equals(codeString))
          return MUSTUNLESSDOCUMENTED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActionRequiredBehavior code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MUST: return "must";
            case COULD: return "could";
            case MUSTUNLESSDOCUMENTED: return "must-unless-documented";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MUST: return "http://hl7.org/fhir/action-required-behavior";
            case COULD: return "http://hl7.org/fhir/action-required-behavior";
            case MUSTUNLESSDOCUMENTED: return "http://hl7.org/fhir/action-required-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MUST: return "An action with this behavior must be included in the actions processed by the end user; the end user may not choose not to include this action";
            case COULD: return "An action with this behavior may be included in the set of actions processed by the end user";
            case MUSTUNLESSDOCUMENTED: return "An action with this behavior must be included in the set of actions processed by the end user, unless the end user provides documentation as to why the action was not included";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MUST: return "Must";
            case COULD: return "Could";
            case MUSTUNLESSDOCUMENTED: return "Must Unless Documented";
            default: return "?";
          }
        }
    }

  public static class ActionRequiredBehaviorEnumFactory implements EnumFactory<ActionRequiredBehavior> {
    public ActionRequiredBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("must".equals(codeString))
          return ActionRequiredBehavior.MUST;
        if ("could".equals(codeString))
          return ActionRequiredBehavior.COULD;
        if ("must-unless-documented".equals(codeString))
          return ActionRequiredBehavior.MUSTUNLESSDOCUMENTED;
        throw new IllegalArgumentException("Unknown ActionRequiredBehavior code '"+codeString+"'");
        }
        public Enumeration<ActionRequiredBehavior> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActionRequiredBehavior>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("must".equals(codeString))
          return new Enumeration<ActionRequiredBehavior>(this, ActionRequiredBehavior.MUST);
        if ("could".equals(codeString))
          return new Enumeration<ActionRequiredBehavior>(this, ActionRequiredBehavior.COULD);
        if ("must-unless-documented".equals(codeString))
          return new Enumeration<ActionRequiredBehavior>(this, ActionRequiredBehavior.MUSTUNLESSDOCUMENTED);
        throw new FHIRException("Unknown ActionRequiredBehavior code '"+codeString+"'");
        }
    public String toCode(ActionRequiredBehavior code) {
      if (code == ActionRequiredBehavior.MUST)
        return "must";
      if (code == ActionRequiredBehavior.COULD)
        return "could";
      if (code == ActionRequiredBehavior.MUSTUNLESSDOCUMENTED)
        return "must-unless-documented";
      return "?";
      }
    public String toSystem(ActionRequiredBehavior code) {
      return code.getSystem();
      }
    }

    public enum ActionPrecheckBehavior {
        /**
         * An action with this behavior is one of the most frequent action that is, or should be, included by an end user, for the particular context in which the action occurs. The system displaying the action to the end user should consider "pre-checking" such an action as a convenience for the user
         */
        YES, 
        /**
         * An action with this behavior is one of the less frequent actions included by the end user, for the particular context in which the action occurs. The system displaying the actions to the end user would typically not "pre-check" such an action
         */
        NO, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActionPrecheckBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("yes".equals(codeString))
          return YES;
        if ("no".equals(codeString))
          return NO;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActionPrecheckBehavior code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case YES: return "yes";
            case NO: return "no";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case YES: return "http://hl7.org/fhir/action-precheck-behavior";
            case NO: return "http://hl7.org/fhir/action-precheck-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case YES: return "An action with this behavior is one of the most frequent action that is, or should be, included by an end user, for the particular context in which the action occurs. The system displaying the action to the end user should consider \"pre-checking\" such an action as a convenience for the user";
            case NO: return "An action with this behavior is one of the less frequent actions included by the end user, for the particular context in which the action occurs. The system displaying the actions to the end user would typically not \"pre-check\" such an action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case YES: return "Yes";
            case NO: return "No";
            default: return "?";
          }
        }
    }

  public static class ActionPrecheckBehaviorEnumFactory implements EnumFactory<ActionPrecheckBehavior> {
    public ActionPrecheckBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("yes".equals(codeString))
          return ActionPrecheckBehavior.YES;
        if ("no".equals(codeString))
          return ActionPrecheckBehavior.NO;
        throw new IllegalArgumentException("Unknown ActionPrecheckBehavior code '"+codeString+"'");
        }
        public Enumeration<ActionPrecheckBehavior> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActionPrecheckBehavior>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("yes".equals(codeString))
          return new Enumeration<ActionPrecheckBehavior>(this, ActionPrecheckBehavior.YES);
        if ("no".equals(codeString))
          return new Enumeration<ActionPrecheckBehavior>(this, ActionPrecheckBehavior.NO);
        throw new FHIRException("Unknown ActionPrecheckBehavior code '"+codeString+"'");
        }
    public String toCode(ActionPrecheckBehavior code) {
      if (code == ActionPrecheckBehavior.YES)
        return "yes";
      if (code == ActionPrecheckBehavior.NO)
        return "no";
      return "?";
      }
    public String toSystem(ActionPrecheckBehavior code) {
      return code.getSystem();
      }
    }

    public enum ActionCardinalityBehavior {
        /**
         * The action may only be selected one time
         */
        SINGLE, 
        /**
         * The action may be selected multiple times
         */
        MULTIPLE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActionCardinalityBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("single".equals(codeString))
          return SINGLE;
        if ("multiple".equals(codeString))
          return MULTIPLE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActionCardinalityBehavior code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SINGLE: return "single";
            case MULTIPLE: return "multiple";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case SINGLE: return "http://hl7.org/fhir/action-cardinality-behavior";
            case MULTIPLE: return "http://hl7.org/fhir/action-cardinality-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SINGLE: return "The action may only be selected one time";
            case MULTIPLE: return "The action may be selected multiple times";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SINGLE: return "Single";
            case MULTIPLE: return "Multiple";
            default: return "?";
          }
        }
    }

  public static class ActionCardinalityBehaviorEnumFactory implements EnumFactory<ActionCardinalityBehavior> {
    public ActionCardinalityBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("single".equals(codeString))
          return ActionCardinalityBehavior.SINGLE;
        if ("multiple".equals(codeString))
          return ActionCardinalityBehavior.MULTIPLE;
        throw new IllegalArgumentException("Unknown ActionCardinalityBehavior code '"+codeString+"'");
        }
        public Enumeration<ActionCardinalityBehavior> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ActionCardinalityBehavior>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("single".equals(codeString))
          return new Enumeration<ActionCardinalityBehavior>(this, ActionCardinalityBehavior.SINGLE);
        if ("multiple".equals(codeString))
          return new Enumeration<ActionCardinalityBehavior>(this, ActionCardinalityBehavior.MULTIPLE);
        throw new FHIRException("Unknown ActionCardinalityBehavior code '"+codeString+"'");
        }
    public String toCode(ActionCardinalityBehavior code) {
      if (code == ActionCardinalityBehavior.SINGLE)
        return "single";
      if (code == ActionCardinalityBehavior.MULTIPLE)
        return "multiple";
      return "?";
      }
    public String toSystem(ActionCardinalityBehavior code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class PlanDefinitionGoalComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates a category the goal falls within.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="E.g. Treatment, dietary, behavioral, etc", formalDefinition="Indicates a category the goal falls within." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-category")
        protected CodeableConcept category;

        /**
         * Human-readable and/or coded description of a specific desired objective of care, such as "control blood pressure" or "negotiate an obstacle course" or "dance with child at wedding".
         */
        @Child(name = "description", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code or text describing the goal", formalDefinition="Human-readable and/or coded description of a specific desired objective of care, such as \"control blood pressure\" or \"negotiate an obstacle course\" or \"dance with child at wedding\"." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinical-findings")
        protected CodeableConcept description;

        /**
         * Identifies the expected level of importance associated with reaching/sustaining the defined goal.
         */
        @Child(name = "priority", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="high-priority | medium-priority | low-priority", formalDefinition="Identifies the expected level of importance associated with reaching/sustaining the defined goal." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-priority")
        protected CodeableConcept priority;

        /**
         * The event after which the goal should begin being pursued.
         */
        @Child(name = "start", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When goal pursuit begins", formalDefinition="The event after which the goal should begin being pursued." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-start-event")
        protected CodeableConcept start;

        /**
         * Identifies problems, conditions, issues, or concerns the goal is intended to address.
         */
        @Child(name = "addresses", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What does the goal address", formalDefinition="Identifies problems, conditions, issues, or concerns the goal is intended to address." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
        protected List<CodeableConcept> addresses;

        /**
         * Didactic or other informational resources associated with the goal that provide further supporting information about the goal. Information resources can include inline text commentary and links to web resources.
         */
        @Child(name = "documentation", type = {RelatedArtifact.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Supporting documentation for the goal", formalDefinition="Didactic or other informational resources associated with the goal that provide further supporting information about the goal. Information resources can include inline text commentary and links to web resources." )
        protected List<RelatedArtifact> documentation;

        /**
         * Indicates what should be done and within what timeframe.
         */
        @Child(name = "target", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Target outcome for the goal", formalDefinition="Indicates what should be done and within what timeframe." )
        protected List<PlanDefinitionGoalTargetComponent> target;

        private static final long serialVersionUID = -795308926L;

    /**
     * Constructor
     */
      public PlanDefinitionGoalComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PlanDefinitionGoalComponent(CodeableConcept description) {
        super();
        this.description = description;
      }

        /**
         * @return {@link #category} (Indicates a category the goal falls within.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionGoalComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Indicates a category the goal falls within.)
         */
        public PlanDefinitionGoalComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #description} (Human-readable and/or coded description of a specific desired objective of care, such as "control blood pressure" or "negotiate an obstacle course" or "dance with child at wedding".)
         */
        public CodeableConcept getDescription() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionGoalComponent.description");
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
        public PlanDefinitionGoalComponent setDescription(CodeableConcept value) { 
          this.description = value;
          return this;
        }

        /**
         * @return {@link #priority} (Identifies the expected level of importance associated with reaching/sustaining the defined goal.)
         */
        public CodeableConcept getPriority() { 
          if (this.priority == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionGoalComponent.priority");
            else if (Configuration.doAutoCreate())
              this.priority = new CodeableConcept(); // cc
          return this.priority;
        }

        public boolean hasPriority() { 
          return this.priority != null && !this.priority.isEmpty();
        }

        /**
         * @param value {@link #priority} (Identifies the expected level of importance associated with reaching/sustaining the defined goal.)
         */
        public PlanDefinitionGoalComponent setPriority(CodeableConcept value) { 
          this.priority = value;
          return this;
        }

        /**
         * @return {@link #start} (The event after which the goal should begin being pursued.)
         */
        public CodeableConcept getStart() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionGoalComponent.start");
            else if (Configuration.doAutoCreate())
              this.start = new CodeableConcept(); // cc
          return this.start;
        }

        public boolean hasStart() { 
          return this.start != null && !this.start.isEmpty();
        }

        /**
         * @param value {@link #start} (The event after which the goal should begin being pursued.)
         */
        public PlanDefinitionGoalComponent setStart(CodeableConcept value) { 
          this.start = value;
          return this;
        }

        /**
         * @return {@link #addresses} (Identifies problems, conditions, issues, or concerns the goal is intended to address.)
         */
        public List<CodeableConcept> getAddresses() { 
          if (this.addresses == null)
            this.addresses = new ArrayList<CodeableConcept>();
          return this.addresses;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionGoalComponent setAddresses(List<CodeableConcept> theAddresses) { 
          this.addresses = theAddresses;
          return this;
        }

        public boolean hasAddresses() { 
          if (this.addresses == null)
            return false;
          for (CodeableConcept item : this.addresses)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addAddresses() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.addresses == null)
            this.addresses = new ArrayList<CodeableConcept>();
          this.addresses.add(t);
          return t;
        }

        public PlanDefinitionGoalComponent addAddresses(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.addresses == null)
            this.addresses = new ArrayList<CodeableConcept>();
          this.addresses.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #addresses}, creating it if it does not already exist
         */
        public CodeableConcept getAddressesFirstRep() { 
          if (getAddresses().isEmpty()) {
            addAddresses();
          }
          return getAddresses().get(0);
        }

        /**
         * @return {@link #documentation} (Didactic or other informational resources associated with the goal that provide further supporting information about the goal. Information resources can include inline text commentary and links to web resources.)
         */
        public List<RelatedArtifact> getDocumentation() { 
          if (this.documentation == null)
            this.documentation = new ArrayList<RelatedArtifact>();
          return this.documentation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionGoalComponent setDocumentation(List<RelatedArtifact> theDocumentation) { 
          this.documentation = theDocumentation;
          return this;
        }

        public boolean hasDocumentation() { 
          if (this.documentation == null)
            return false;
          for (RelatedArtifact item : this.documentation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public RelatedArtifact addDocumentation() { //3
          RelatedArtifact t = new RelatedArtifact();
          if (this.documentation == null)
            this.documentation = new ArrayList<RelatedArtifact>();
          this.documentation.add(t);
          return t;
        }

        public PlanDefinitionGoalComponent addDocumentation(RelatedArtifact t) { //3
          if (t == null)
            return this;
          if (this.documentation == null)
            this.documentation = new ArrayList<RelatedArtifact>();
          this.documentation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #documentation}, creating it if it does not already exist
         */
        public RelatedArtifact getDocumentationFirstRep() { 
          if (getDocumentation().isEmpty()) {
            addDocumentation();
          }
          return getDocumentation().get(0);
        }

        /**
         * @return {@link #target} (Indicates what should be done and within what timeframe.)
         */
        public List<PlanDefinitionGoalTargetComponent> getTarget() { 
          if (this.target == null)
            this.target = new ArrayList<PlanDefinitionGoalTargetComponent>();
          return this.target;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionGoalComponent setTarget(List<PlanDefinitionGoalTargetComponent> theTarget) { 
          this.target = theTarget;
          return this;
        }

        public boolean hasTarget() { 
          if (this.target == null)
            return false;
          for (PlanDefinitionGoalTargetComponent item : this.target)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionGoalTargetComponent addTarget() { //3
          PlanDefinitionGoalTargetComponent t = new PlanDefinitionGoalTargetComponent();
          if (this.target == null)
            this.target = new ArrayList<PlanDefinitionGoalTargetComponent>();
          this.target.add(t);
          return t;
        }

        public PlanDefinitionGoalComponent addTarget(PlanDefinitionGoalTargetComponent t) { //3
          if (t == null)
            return this;
          if (this.target == null)
            this.target = new ArrayList<PlanDefinitionGoalTargetComponent>();
          this.target.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #target}, creating it if it does not already exist
         */
        public PlanDefinitionGoalTargetComponent getTargetFirstRep() { 
          if (getTarget().isEmpty()) {
            addTarget();
          }
          return getTarget().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "CodeableConcept", "Indicates a category the goal falls within.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("description", "CodeableConcept", "Human-readable and/or coded description of a specific desired objective of care, such as \"control blood pressure\" or \"negotiate an obstacle course\" or \"dance with child at wedding\".", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("priority", "CodeableConcept", "Identifies the expected level of importance associated with reaching/sustaining the defined goal.", 0, java.lang.Integer.MAX_VALUE, priority));
          childrenList.add(new Property("start", "CodeableConcept", "The event after which the goal should begin being pursued.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("addresses", "CodeableConcept", "Identifies problems, conditions, issues, or concerns the goal is intended to address.", 0, java.lang.Integer.MAX_VALUE, addresses));
          childrenList.add(new Property("documentation", "RelatedArtifact", "Didactic or other informational resources associated with the goal that provide further supporting information about the goal. Information resources can include inline text commentary and links to web resources.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("target", "", "Indicates what should be done and within what timeframe.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // CodeableConcept
        case 874544034: /*addresses*/ return this.addresses == null ? new Base[0] : this.addresses.toArray(new Base[this.addresses.size()]); // CodeableConcept
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : this.documentation.toArray(new Base[this.documentation.size()]); // RelatedArtifact
        case -880905839: /*target*/ return this.target == null ? new Base[0] : this.target.toArray(new Base[this.target.size()]); // PlanDefinitionGoalTargetComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1165461084: // priority
          this.priority = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 109757538: // start
          this.start = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 874544034: // addresses
          this.getAddresses().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1587405498: // documentation
          this.getDocumentation().add(castToRelatedArtifact(value)); // RelatedArtifact
          return value;
        case -880905839: // target
          this.getTarget().add((PlanDefinitionGoalTargetComponent) value); // PlanDefinitionGoalTargetComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.description = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("priority")) {
          this.priority = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("start")) {
          this.start = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("addresses")) {
          this.getAddresses().add(castToCodeableConcept(value));
        } else if (name.equals("documentation")) {
          this.getDocumentation().add(castToRelatedArtifact(value));
        } else if (name.equals("target")) {
          this.getTarget().add((PlanDefinitionGoalTargetComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); 
        case -1724546052:  return getDescription(); 
        case -1165461084:  return getPriority(); 
        case 109757538:  return getStart(); 
        case 874544034:  return addAddresses(); 
        case 1587405498:  return addDocumentation(); 
        case -880905839:  return addTarget(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"CodeableConcept"};
        case -1165461084: /*priority*/ return new String[] {"CodeableConcept"};
        case 109757538: /*start*/ return new String[] {"CodeableConcept"};
        case 874544034: /*addresses*/ return new String[] {"CodeableConcept"};
        case 1587405498: /*documentation*/ return new String[] {"RelatedArtifact"};
        case -880905839: /*target*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("description")) {
          this.description = new CodeableConcept();
          return this.description;
        }
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else if (name.equals("start")) {
          this.start = new CodeableConcept();
          return this.start;
        }
        else if (name.equals("addresses")) {
          return addAddresses();
        }
        else if (name.equals("documentation")) {
          return addDocumentation();
        }
        else if (name.equals("target")) {
          return addTarget();
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionGoalComponent copy() {
        PlanDefinitionGoalComponent dst = new PlanDefinitionGoalComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.description = description == null ? null : description.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.start = start == null ? null : start.copy();
        if (addresses != null) {
          dst.addresses = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : addresses)
            dst.addresses.add(i.copy());
        };
        if (documentation != null) {
          dst.documentation = new ArrayList<RelatedArtifact>();
          for (RelatedArtifact i : documentation)
            dst.documentation.add(i.copy());
        };
        if (target != null) {
          dst.target = new ArrayList<PlanDefinitionGoalTargetComponent>();
          for (PlanDefinitionGoalTargetComponent i : target)
            dst.target.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionGoalComponent))
          return false;
        PlanDefinitionGoalComponent o = (PlanDefinitionGoalComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(description, o.description, true)
           && compareDeep(priority, o.priority, true) && compareDeep(start, o.start, true) && compareDeep(addresses, o.addresses, true)
           && compareDeep(documentation, o.documentation, true) && compareDeep(target, o.target, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionGoalComponent))
          return false;
        PlanDefinitionGoalComponent o = (PlanDefinitionGoalComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, description, priority
          , start, addresses, documentation, target);
      }

  public String fhirType() {
    return "PlanDefinition.goal";

  }

  }

    @Block()
    public static class PlanDefinitionGoalTargetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The parameter whose value is to be tracked, e.g. body weigth, blood pressure, or hemoglobin A1c level.
         */
        @Child(name = "measure", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The parameter whose value is to be tracked", formalDefinition="The parameter whose value is to be tracked, e.g. body weigth, blood pressure, or hemoglobin A1c level." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-codes")
        protected CodeableConcept measure;

        /**
         * The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%. Either the high or low or both values of the range can be specified. Whan a low value is missing, it indicates that the goal is achieved at any value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any value at or above the low value.
         */
        @Child(name = "detail", type = {Quantity.class, Range.class, CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The target value to be achieved", formalDefinition="The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%. Either the high or low or both values of the range can be specified. Whan a low value is missing, it indicates that the goal is achieved at any value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any value at or above the low value." )
        protected Type detail;

        /**
         * Indicates the timeframe after the start of the goal in which the goal should be met.
         */
        @Child(name = "due", type = {Duration.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reach goal within", formalDefinition="Indicates the timeframe after the start of the goal in which the goal should be met." )
        protected Duration due;

        private static final long serialVersionUID = -131874144L;

    /**
     * Constructor
     */
      public PlanDefinitionGoalTargetComponent() {
        super();
      }

        /**
         * @return {@link #measure} (The parameter whose value is to be tracked, e.g. body weigth, blood pressure, or hemoglobin A1c level.)
         */
        public CodeableConcept getMeasure() { 
          if (this.measure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionGoalTargetComponent.measure");
            else if (Configuration.doAutoCreate())
              this.measure = new CodeableConcept(); // cc
          return this.measure;
        }

        public boolean hasMeasure() { 
          return this.measure != null && !this.measure.isEmpty();
        }

        /**
         * @param value {@link #measure} (The parameter whose value is to be tracked, e.g. body weigth, blood pressure, or hemoglobin A1c level.)
         */
        public PlanDefinitionGoalTargetComponent setMeasure(CodeableConcept value) { 
          this.measure = value;
          return this;
        }

        /**
         * @return {@link #detail} (The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%. Either the high or low or both values of the range can be specified. Whan a low value is missing, it indicates that the goal is achieved at any value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any value at or above the low value.)
         */
        public Type getDetail() { 
          return this.detail;
        }

        /**
         * @return {@link #detail} (The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%. Either the high or low or both values of the range can be specified. Whan a low value is missing, it indicates that the goal is achieved at any value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any value at or above the low value.)
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
         * @return {@link #detail} (The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%. Either the high or low or both values of the range can be specified. Whan a low value is missing, it indicates that the goal is achieved at any value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any value at or above the low value.)
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
         * @return {@link #detail} (The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%. Either the high or low or both values of the range can be specified. Whan a low value is missing, it indicates that the goal is achieved at any value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any value at or above the low value.)
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
         * @param value {@link #detail} (The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%. Either the high or low or both values of the range can be specified. Whan a low value is missing, it indicates that the goal is achieved at any value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any value at or above the low value.)
         */
        public PlanDefinitionGoalTargetComponent setDetail(Type value) { 
          this.detail = value;
          return this;
        }

        /**
         * @return {@link #due} (Indicates the timeframe after the start of the goal in which the goal should be met.)
         */
        public Duration getDue() { 
          if (this.due == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionGoalTargetComponent.due");
            else if (Configuration.doAutoCreate())
              this.due = new Duration(); // cc
          return this.due;
        }

        public boolean hasDue() { 
          return this.due != null && !this.due.isEmpty();
        }

        /**
         * @param value {@link #due} (Indicates the timeframe after the start of the goal in which the goal should be met.)
         */
        public PlanDefinitionGoalTargetComponent setDue(Duration value) { 
          this.due = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("measure", "CodeableConcept", "The parameter whose value is to be tracked, e.g. body weigth, blood pressure, or hemoglobin A1c level.", 0, java.lang.Integer.MAX_VALUE, measure));
          childrenList.add(new Property("detail[x]", "Quantity|Range|CodeableConcept", "The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%. Either the high or low or both values of the range can be specified. Whan a low value is missing, it indicates that the goal is achieved at any value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any value at or above the low value.", 0, java.lang.Integer.MAX_VALUE, detail));
          childrenList.add(new Property("due", "Duration", "Indicates the timeframe after the start of the goal in which the goal should be met.", 0, java.lang.Integer.MAX_VALUE, due));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 938321246: /*measure*/ return this.measure == null ? new Base[0] : new Base[] {this.measure}; // CodeableConcept
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : new Base[] {this.detail}; // Type
        case 99828: /*due*/ return this.due == null ? new Base[0] : new Base[] {this.due}; // Duration
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
          this.due = castToDuration(value); // Duration
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
        } else if (name.equals("due")) {
          this.due = castToDuration(value); // Duration
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
        case 99828:  return getDue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 938321246: /*measure*/ return new String[] {"CodeableConcept"};
        case -1335224239: /*detail*/ return new String[] {"Quantity", "Range", "CodeableConcept"};
        case 99828: /*due*/ return new String[] {"Duration"};
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
        else if (name.equals("due")) {
          this.due = new Duration();
          return this.due;
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionGoalTargetComponent copy() {
        PlanDefinitionGoalTargetComponent dst = new PlanDefinitionGoalTargetComponent();
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
        if (!(other instanceof PlanDefinitionGoalTargetComponent))
          return false;
        PlanDefinitionGoalTargetComponent o = (PlanDefinitionGoalTargetComponent) other;
        return compareDeep(measure, o.measure, true) && compareDeep(detail, o.detail, true) && compareDeep(due, o.due, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionGoalTargetComponent))
          return false;
        PlanDefinitionGoalTargetComponent o = (PlanDefinitionGoalTargetComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(measure, detail, due);
      }

  public String fhirType() {
    return "PlanDefinition.goal.target";

  }

  }

    @Block()
    public static class PlanDefinitionActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A user-visible label for the action.
         */
        @Child(name = "label", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="User-visible label for the action (e.g. 1. or A.)", formalDefinition="A user-visible label for the action." )
        protected StringType label;

        /**
         * The title of the action displayed to a user.
         */
        @Child(name = "title", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="User-visible title", formalDefinition="The title of the action displayed to a user." )
        protected StringType title;

        /**
         * A short description of the action used to provide a summary to display to the user.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short description of the action", formalDefinition="A short description of the action used to provide a summary to display to the user." )
        protected StringType description;

        /**
         * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.
         */
        @Child(name = "textEquivalent", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Static text equivalent of the action, used if the dynamic aspects cannot be interpreted by the receiving system", formalDefinition="A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically." )
        protected StringType textEquivalent;

        /**
         * A code that provides meaning for the action or action group. For example, a section may have a LOINC code for a the section of a documentation template.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Code representing the meaning of the action or sub-actions", formalDefinition="A code that provides meaning for the action or action group. For example, a section may have a LOINC code for a the section of a documentation template." )
        protected List<CodeableConcept> code;

        /**
         * A description of why this action is necessary or appropriate.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Why the action should be performed", formalDefinition="A description of why this action is necessary or appropriate." )
        protected List<CodeableConcept> reason;

        /**
         * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.
         */
        @Child(name = "documentation", type = {RelatedArtifact.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Supporting documentation for the intended performer of the action", formalDefinition="Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources." )
        protected List<RelatedArtifact> documentation;

        /**
         * Identifies goals that this action supports. The reference must be to a goal element defined within this plan definition.
         */
        @Child(name = "goalId", type = {IdType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What goals this action supports", formalDefinition="Identifies goals that this action supports. The reference must be to a goal element defined within this plan definition." )
        protected List<IdType> goalId;

        /**
         * A description of when the action should be triggered.
         */
        @Child(name = "triggerDefinition", type = {TriggerDefinition.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="When the action should be triggered", formalDefinition="A description of when the action should be triggered." )
        protected List<TriggerDefinition> triggerDefinition;

        /**
         * An expression that describes applicability criteria, or start/stop conditions for the action.
         */
        @Child(name = "condition", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Whether or not the action is applicable", formalDefinition="An expression that describes applicability criteria, or start/stop conditions for the action." )
        protected List<PlanDefinitionActionConditionComponent> condition;

        /**
         * Defines input data requirements for the action.
         */
        @Child(name = "input", type = {DataRequirement.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Input data requirements", formalDefinition="Defines input data requirements for the action." )
        protected List<DataRequirement> input;

        /**
         * Defines the outputs of the action, if any.
         */
        @Child(name = "output", type = {DataRequirement.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Output data definition", formalDefinition="Defines the outputs of the action, if any." )
        protected List<DataRequirement> output;

        /**
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         */
        @Child(name = "relatedAction", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Relationship to another action", formalDefinition="A relationship to another action such as \"before\" or \"30-60 minutes after start of\"." )
        protected List<PlanDefinitionActionRelatedActionComponent> relatedAction;

        /**
         * An optional value describing when the action should be performed.
         */
        @Child(name = "timing", type = {DateTimeType.class, Period.class, Duration.class, Range.class, Timing.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When the action should take place", formalDefinition="An optional value describing when the action should be performed." )
        protected Type timing;

        /**
         * Indicates who should participate in performing the action described.
         */
        @Child(name = "participant", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Who should participate in the action", formalDefinition="Indicates who should participate in performing the action described." )
        protected List<PlanDefinitionActionParticipantComponent> participant;

        /**
         * The type of action to perform (create, update, remove).
         */
        @Child(name = "type", type = {Coding.class}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="create | update | remove | fire-event", formalDefinition="The type of action to perform (create, update, remove)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-type")
        protected Coding type;

        /**
         * Defines the grouping behavior for the action and its children.
         */
        @Child(name = "groupingBehavior", type = {CodeType.class}, order=17, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="visual-group | logical-group | sentence-group", formalDefinition="Defines the grouping behavior for the action and its children." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-grouping-behavior")
        protected Enumeration<ActionGroupingBehavior> groupingBehavior;

        /**
         * Defines the selection behavior for the action and its children.
         */
        @Child(name = "selectionBehavior", type = {CodeType.class}, order=18, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="any | all | all-or-none | exactly-one | at-most-one | one-or-more", formalDefinition="Defines the selection behavior for the action and its children." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-selection-behavior")
        protected Enumeration<ActionSelectionBehavior> selectionBehavior;

        /**
         * Defines the requiredness behavior for the action.
         */
        @Child(name = "requiredBehavior", type = {CodeType.class}, order=19, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="must | could | must-unless-documented", formalDefinition="Defines the requiredness behavior for the action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-required-behavior")
        protected Enumeration<ActionRequiredBehavior> requiredBehavior;

        /**
         * Defines whether the action should usually be preselected.
         */
        @Child(name = "precheckBehavior", type = {CodeType.class}, order=20, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="yes | no", formalDefinition="Defines whether the action should usually be preselected." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-precheck-behavior")
        protected Enumeration<ActionPrecheckBehavior> precheckBehavior;

        /**
         * Defines whether the action can be selected multiple times.
         */
        @Child(name = "cardinalityBehavior", type = {CodeType.class}, order=21, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="single | multiple", formalDefinition="Defines whether the action can be selected multiple times." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-cardinality-behavior")
        protected Enumeration<ActionCardinalityBehavior> cardinalityBehavior;

        /**
         * A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that describes a series of actions to be taken.
         */
        @Child(name = "definition", type = {ActivityDefinition.class, PlanDefinition.class}, order=22, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of the activity to be performed", formalDefinition="A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that describes a series of actions to be taken." )
        protected Reference definition;

        /**
         * The actual object that is the target of the reference (A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that describes a series of actions to be taken.)
         */
        protected Resource definitionTarget;

        /**
         * A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.
         */
        @Child(name = "transform", type = {StructureMap.class}, order=23, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Transform to apply the template", formalDefinition="A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input." )
        protected Reference transform;

        /**
         * The actual object that is the target of the reference (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
         */
        protected StructureMap transformTarget;

        /**
         * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.
         */
        @Child(name = "dynamicValue", type = {}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Dynamic aspects of the definition", formalDefinition="Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result." )
        protected List<PlanDefinitionActionDynamicValueComponent> dynamicValue;

        /**
         * Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.
         */
        @Child(name = "action", type = {PlanDefinitionActionComponent.class}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A sub-action", formalDefinition="Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition." )
        protected List<PlanDefinitionActionComponent> action;

        private static final long serialVersionUID = -1404963512L;

    /**
     * Constructor
     */
      public PlanDefinitionActionComponent() {
        super();
      }

        /**
         * @return {@link #label} (A user-visible label for the action.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public StringType getLabelElement() { 
          if (this.label == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.label");
            else if (Configuration.doAutoCreate())
              this.label = new StringType(); // bb
          return this.label;
        }

        public boolean hasLabelElement() { 
          return this.label != null && !this.label.isEmpty();
        }

        public boolean hasLabel() { 
          return this.label != null && !this.label.isEmpty();
        }

        /**
         * @param value {@link #label} (A user-visible label for the action.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public PlanDefinitionActionComponent setLabelElement(StringType value) { 
          this.label = value;
          return this;
        }

        /**
         * @return A user-visible label for the action.
         */
        public String getLabel() { 
          return this.label == null ? null : this.label.getValue();
        }

        /**
         * @param value A user-visible label for the action.
         */
        public PlanDefinitionActionComponent setLabel(String value) { 
          if (Utilities.noString(value))
            this.label = null;
          else {
            if (this.label == null)
              this.label = new StringType();
            this.label.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #title} (The title of the action displayed to a user.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.title");
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
         * @param value {@link #title} (The title of the action displayed to a user.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public PlanDefinitionActionComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return The title of the action displayed to a user.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value The title of the action displayed to a user.
         */
        public PlanDefinitionActionComponent setTitle(String value) { 
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
         * @return {@link #description} (A short description of the action used to provide a summary to display to the user.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.description");
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
         * @param value {@link #description} (A short description of the action used to provide a summary to display to the user.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public PlanDefinitionActionComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A short description of the action used to provide a summary to display to the user.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A short description of the action used to provide a summary to display to the user.
         */
        public PlanDefinitionActionComponent setDescription(String value) { 
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
         * @return {@link #textEquivalent} (A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.). This is the underlying object with id, value and extensions. The accessor "getTextEquivalent" gives direct access to the value
         */
        public StringType getTextEquivalentElement() { 
          if (this.textEquivalent == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.textEquivalent");
            else if (Configuration.doAutoCreate())
              this.textEquivalent = new StringType(); // bb
          return this.textEquivalent;
        }

        public boolean hasTextEquivalentElement() { 
          return this.textEquivalent != null && !this.textEquivalent.isEmpty();
        }

        public boolean hasTextEquivalent() { 
          return this.textEquivalent != null && !this.textEquivalent.isEmpty();
        }

        /**
         * @param value {@link #textEquivalent} (A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.). This is the underlying object with id, value and extensions. The accessor "getTextEquivalent" gives direct access to the value
         */
        public PlanDefinitionActionComponent setTextEquivalentElement(StringType value) { 
          this.textEquivalent = value;
          return this;
        }

        /**
         * @return A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.
         */
        public String getTextEquivalent() { 
          return this.textEquivalent == null ? null : this.textEquivalent.getValue();
        }

        /**
         * @param value A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.
         */
        public PlanDefinitionActionComponent setTextEquivalent(String value) { 
          if (Utilities.noString(value))
            this.textEquivalent = null;
          else {
            if (this.textEquivalent == null)
              this.textEquivalent = new StringType();
            this.textEquivalent.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #code} (A code that provides meaning for the action or action group. For example, a section may have a LOINC code for a the section of a documentation template.)
         */
        public List<CodeableConcept> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setCode(List<CodeableConcept> theCode) { 
          this.code = theCode;
          return this;
        }

        public boolean hasCode() { 
          if (this.code == null)
            return false;
          for (CodeableConcept item : this.code)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
         */
        public CodeableConcept getCodeFirstRep() { 
          if (getCode().isEmpty()) {
            addCode();
          }
          return getCode().get(0);
        }

        /**
         * @return {@link #reason} (A description of why this action is necessary or appropriate.)
         */
        public List<CodeableConcept> getReason() { 
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          return this.reason;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setReason(List<CodeableConcept> theReason) { 
          this.reason = theReason;
          return this;
        }

        public boolean hasReason() { 
          if (this.reason == null)
            return false;
          for (CodeableConcept item : this.reason)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addReason() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          this.reason.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addReason(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          this.reason.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #reason}, creating it if it does not already exist
         */
        public CodeableConcept getReasonFirstRep() { 
          if (getReason().isEmpty()) {
            addReason();
          }
          return getReason().get(0);
        }

        /**
         * @return {@link #documentation} (Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.)
         */
        public List<RelatedArtifact> getDocumentation() { 
          if (this.documentation == null)
            this.documentation = new ArrayList<RelatedArtifact>();
          return this.documentation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setDocumentation(List<RelatedArtifact> theDocumentation) { 
          this.documentation = theDocumentation;
          return this;
        }

        public boolean hasDocumentation() { 
          if (this.documentation == null)
            return false;
          for (RelatedArtifact item : this.documentation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public RelatedArtifact addDocumentation() { //3
          RelatedArtifact t = new RelatedArtifact();
          if (this.documentation == null)
            this.documentation = new ArrayList<RelatedArtifact>();
          this.documentation.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addDocumentation(RelatedArtifact t) { //3
          if (t == null)
            return this;
          if (this.documentation == null)
            this.documentation = new ArrayList<RelatedArtifact>();
          this.documentation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #documentation}, creating it if it does not already exist
         */
        public RelatedArtifact getDocumentationFirstRep() { 
          if (getDocumentation().isEmpty()) {
            addDocumentation();
          }
          return getDocumentation().get(0);
        }

        /**
         * @return {@link #goalId} (Identifies goals that this action supports. The reference must be to a goal element defined within this plan definition.)
         */
        public List<IdType> getGoalId() { 
          if (this.goalId == null)
            this.goalId = new ArrayList<IdType>();
          return this.goalId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setGoalId(List<IdType> theGoalId) { 
          this.goalId = theGoalId;
          return this;
        }

        public boolean hasGoalId() { 
          if (this.goalId == null)
            return false;
          for (IdType item : this.goalId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #goalId} (Identifies goals that this action supports. The reference must be to a goal element defined within this plan definition.)
         */
        public IdType addGoalIdElement() {//2 
          IdType t = new IdType();
          if (this.goalId == null)
            this.goalId = new ArrayList<IdType>();
          this.goalId.add(t);
          return t;
        }

        /**
         * @param value {@link #goalId} (Identifies goals that this action supports. The reference must be to a goal element defined within this plan definition.)
         */
        public PlanDefinitionActionComponent addGoalId(String value) { //1
          IdType t = new IdType();
          t.setValue(value);
          if (this.goalId == null)
            this.goalId = new ArrayList<IdType>();
          this.goalId.add(t);
          return this;
        }

        /**
         * @param value {@link #goalId} (Identifies goals that this action supports. The reference must be to a goal element defined within this plan definition.)
         */
        public boolean hasGoalId(String value) { 
          if (this.goalId == null)
            return false;
          for (IdType v : this.goalId)
            if (v.equals(value)) // id
              return true;
          return false;
        }

        /**
         * @return {@link #triggerDefinition} (A description of when the action should be triggered.)
         */
        public List<TriggerDefinition> getTriggerDefinition() { 
          if (this.triggerDefinition == null)
            this.triggerDefinition = new ArrayList<TriggerDefinition>();
          return this.triggerDefinition;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setTriggerDefinition(List<TriggerDefinition> theTriggerDefinition) { 
          this.triggerDefinition = theTriggerDefinition;
          return this;
        }

        public boolean hasTriggerDefinition() { 
          if (this.triggerDefinition == null)
            return false;
          for (TriggerDefinition item : this.triggerDefinition)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public TriggerDefinition addTriggerDefinition() { //3
          TriggerDefinition t = new TriggerDefinition();
          if (this.triggerDefinition == null)
            this.triggerDefinition = new ArrayList<TriggerDefinition>();
          this.triggerDefinition.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addTriggerDefinition(TriggerDefinition t) { //3
          if (t == null)
            return this;
          if (this.triggerDefinition == null)
            this.triggerDefinition = new ArrayList<TriggerDefinition>();
          this.triggerDefinition.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #triggerDefinition}, creating it if it does not already exist
         */
        public TriggerDefinition getTriggerDefinitionFirstRep() { 
          if (getTriggerDefinition().isEmpty()) {
            addTriggerDefinition();
          }
          return getTriggerDefinition().get(0);
        }

        /**
         * @return {@link #condition} (An expression that describes applicability criteria, or start/stop conditions for the action.)
         */
        public List<PlanDefinitionActionConditionComponent> getCondition() { 
          if (this.condition == null)
            this.condition = new ArrayList<PlanDefinitionActionConditionComponent>();
          return this.condition;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setCondition(List<PlanDefinitionActionConditionComponent> theCondition) { 
          this.condition = theCondition;
          return this;
        }

        public boolean hasCondition() { 
          if (this.condition == null)
            return false;
          for (PlanDefinitionActionConditionComponent item : this.condition)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionConditionComponent addCondition() { //3
          PlanDefinitionActionConditionComponent t = new PlanDefinitionActionConditionComponent();
          if (this.condition == null)
            this.condition = new ArrayList<PlanDefinitionActionConditionComponent>();
          this.condition.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addCondition(PlanDefinitionActionConditionComponent t) { //3
          if (t == null)
            return this;
          if (this.condition == null)
            this.condition = new ArrayList<PlanDefinitionActionConditionComponent>();
          this.condition.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #condition}, creating it if it does not already exist
         */
        public PlanDefinitionActionConditionComponent getConditionFirstRep() { 
          if (getCondition().isEmpty()) {
            addCondition();
          }
          return getCondition().get(0);
        }

        /**
         * @return {@link #input} (Defines input data requirements for the action.)
         */
        public List<DataRequirement> getInput() { 
          if (this.input == null)
            this.input = new ArrayList<DataRequirement>();
          return this.input;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setInput(List<DataRequirement> theInput) { 
          this.input = theInput;
          return this;
        }

        public boolean hasInput() { 
          if (this.input == null)
            return false;
          for (DataRequirement item : this.input)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public DataRequirement addInput() { //3
          DataRequirement t = new DataRequirement();
          if (this.input == null)
            this.input = new ArrayList<DataRequirement>();
          this.input.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addInput(DataRequirement t) { //3
          if (t == null)
            return this;
          if (this.input == null)
            this.input = new ArrayList<DataRequirement>();
          this.input.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #input}, creating it if it does not already exist
         */
        public DataRequirement getInputFirstRep() { 
          if (getInput().isEmpty()) {
            addInput();
          }
          return getInput().get(0);
        }

        /**
         * @return {@link #output} (Defines the outputs of the action, if any.)
         */
        public List<DataRequirement> getOutput() { 
          if (this.output == null)
            this.output = new ArrayList<DataRequirement>();
          return this.output;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setOutput(List<DataRequirement> theOutput) { 
          this.output = theOutput;
          return this;
        }

        public boolean hasOutput() { 
          if (this.output == null)
            return false;
          for (DataRequirement item : this.output)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public DataRequirement addOutput() { //3
          DataRequirement t = new DataRequirement();
          if (this.output == null)
            this.output = new ArrayList<DataRequirement>();
          this.output.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addOutput(DataRequirement t) { //3
          if (t == null)
            return this;
          if (this.output == null)
            this.output = new ArrayList<DataRequirement>();
          this.output.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #output}, creating it if it does not already exist
         */
        public DataRequirement getOutputFirstRep() { 
          if (getOutput().isEmpty()) {
            addOutput();
          }
          return getOutput().get(0);
        }

        /**
         * @return {@link #relatedAction} (A relationship to another action such as "before" or "30-60 minutes after start of".)
         */
        public List<PlanDefinitionActionRelatedActionComponent> getRelatedAction() { 
          if (this.relatedAction == null)
            this.relatedAction = new ArrayList<PlanDefinitionActionRelatedActionComponent>();
          return this.relatedAction;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setRelatedAction(List<PlanDefinitionActionRelatedActionComponent> theRelatedAction) { 
          this.relatedAction = theRelatedAction;
          return this;
        }

        public boolean hasRelatedAction() { 
          if (this.relatedAction == null)
            return false;
          for (PlanDefinitionActionRelatedActionComponent item : this.relatedAction)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionRelatedActionComponent addRelatedAction() { //3
          PlanDefinitionActionRelatedActionComponent t = new PlanDefinitionActionRelatedActionComponent();
          if (this.relatedAction == null)
            this.relatedAction = new ArrayList<PlanDefinitionActionRelatedActionComponent>();
          this.relatedAction.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addRelatedAction(PlanDefinitionActionRelatedActionComponent t) { //3
          if (t == null)
            return this;
          if (this.relatedAction == null)
            this.relatedAction = new ArrayList<PlanDefinitionActionRelatedActionComponent>();
          this.relatedAction.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #relatedAction}, creating it if it does not already exist
         */
        public PlanDefinitionActionRelatedActionComponent getRelatedActionFirstRep() { 
          if (getRelatedAction().isEmpty()) {
            addRelatedAction();
          }
          return getRelatedAction().get(0);
        }

        /**
         * @return {@link #timing} (An optional value describing when the action should be performed.)
         */
        public Type getTiming() { 
          return this.timing;
        }

        /**
         * @return {@link #timing} (An optional value describing when the action should be performed.)
         */
        public DateTimeType getTimingDateTimeType() throws FHIRException { 
          if (!(this.timing instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.timing.getClass().getName()+" was encountered");
          return (DateTimeType) this.timing;
        }

        public boolean hasTimingDateTimeType() { 
          return this.timing instanceof DateTimeType;
        }

        /**
         * @return {@link #timing} (An optional value describing when the action should be performed.)
         */
        public Period getTimingPeriod() throws FHIRException { 
          if (!(this.timing instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.timing.getClass().getName()+" was encountered");
          return (Period) this.timing;
        }

        public boolean hasTimingPeriod() { 
          return this.timing instanceof Period;
        }

        /**
         * @return {@link #timing} (An optional value describing when the action should be performed.)
         */
        public Duration getTimingDuration() throws FHIRException { 
          if (!(this.timing instanceof Duration))
            throw new FHIRException("Type mismatch: the type Duration was expected, but "+this.timing.getClass().getName()+" was encountered");
          return (Duration) this.timing;
        }

        public boolean hasTimingDuration() { 
          return this.timing instanceof Duration;
        }

        /**
         * @return {@link #timing} (An optional value describing when the action should be performed.)
         */
        public Range getTimingRange() throws FHIRException { 
          if (!(this.timing instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.timing.getClass().getName()+" was encountered");
          return (Range) this.timing;
        }

        public boolean hasTimingRange() { 
          return this.timing instanceof Range;
        }

        /**
         * @return {@link #timing} (An optional value describing when the action should be performed.)
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
         * @param value {@link #timing} (An optional value describing when the action should be performed.)
         */
        public PlanDefinitionActionComponent setTiming(Type value) { 
          this.timing = value;
          return this;
        }

        /**
         * @return {@link #participant} (Indicates who should participate in performing the action described.)
         */
        public List<PlanDefinitionActionParticipantComponent> getParticipant() { 
          if (this.participant == null)
            this.participant = new ArrayList<PlanDefinitionActionParticipantComponent>();
          return this.participant;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setParticipant(List<PlanDefinitionActionParticipantComponent> theParticipant) { 
          this.participant = theParticipant;
          return this;
        }

        public boolean hasParticipant() { 
          if (this.participant == null)
            return false;
          for (PlanDefinitionActionParticipantComponent item : this.participant)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionParticipantComponent addParticipant() { //3
          PlanDefinitionActionParticipantComponent t = new PlanDefinitionActionParticipantComponent();
          if (this.participant == null)
            this.participant = new ArrayList<PlanDefinitionActionParticipantComponent>();
          this.participant.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addParticipant(PlanDefinitionActionParticipantComponent t) { //3
          if (t == null)
            return this;
          if (this.participant == null)
            this.participant = new ArrayList<PlanDefinitionActionParticipantComponent>();
          this.participant.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #participant}, creating it if it does not already exist
         */
        public PlanDefinitionActionParticipantComponent getParticipantFirstRep() { 
          if (getParticipant().isEmpty()) {
            addParticipant();
          }
          return getParticipant().get(0);
        }

        /**
         * @return {@link #type} (The type of action to perform (create, update, remove).)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of action to perform (create, update, remove).)
         */
        public PlanDefinitionActionComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #groupingBehavior} (Defines the grouping behavior for the action and its children.). This is the underlying object with id, value and extensions. The accessor "getGroupingBehavior" gives direct access to the value
         */
        public Enumeration<ActionGroupingBehavior> getGroupingBehaviorElement() { 
          if (this.groupingBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.groupingBehavior");
            else if (Configuration.doAutoCreate())
              this.groupingBehavior = new Enumeration<ActionGroupingBehavior>(new ActionGroupingBehaviorEnumFactory()); // bb
          return this.groupingBehavior;
        }

        public boolean hasGroupingBehaviorElement() { 
          return this.groupingBehavior != null && !this.groupingBehavior.isEmpty();
        }

        public boolean hasGroupingBehavior() { 
          return this.groupingBehavior != null && !this.groupingBehavior.isEmpty();
        }

        /**
         * @param value {@link #groupingBehavior} (Defines the grouping behavior for the action and its children.). This is the underlying object with id, value and extensions. The accessor "getGroupingBehavior" gives direct access to the value
         */
        public PlanDefinitionActionComponent setGroupingBehaviorElement(Enumeration<ActionGroupingBehavior> value) { 
          this.groupingBehavior = value;
          return this;
        }

        /**
         * @return Defines the grouping behavior for the action and its children.
         */
        public ActionGroupingBehavior getGroupingBehavior() { 
          return this.groupingBehavior == null ? null : this.groupingBehavior.getValue();
        }

        /**
         * @param value Defines the grouping behavior for the action and its children.
         */
        public PlanDefinitionActionComponent setGroupingBehavior(ActionGroupingBehavior value) { 
          if (value == null)
            this.groupingBehavior = null;
          else {
            if (this.groupingBehavior == null)
              this.groupingBehavior = new Enumeration<ActionGroupingBehavior>(new ActionGroupingBehaviorEnumFactory());
            this.groupingBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #selectionBehavior} (Defines the selection behavior for the action and its children.). This is the underlying object with id, value and extensions. The accessor "getSelectionBehavior" gives direct access to the value
         */
        public Enumeration<ActionSelectionBehavior> getSelectionBehaviorElement() { 
          if (this.selectionBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.selectionBehavior");
            else if (Configuration.doAutoCreate())
              this.selectionBehavior = new Enumeration<ActionSelectionBehavior>(new ActionSelectionBehaviorEnumFactory()); // bb
          return this.selectionBehavior;
        }

        public boolean hasSelectionBehaviorElement() { 
          return this.selectionBehavior != null && !this.selectionBehavior.isEmpty();
        }

        public boolean hasSelectionBehavior() { 
          return this.selectionBehavior != null && !this.selectionBehavior.isEmpty();
        }

        /**
         * @param value {@link #selectionBehavior} (Defines the selection behavior for the action and its children.). This is the underlying object with id, value and extensions. The accessor "getSelectionBehavior" gives direct access to the value
         */
        public PlanDefinitionActionComponent setSelectionBehaviorElement(Enumeration<ActionSelectionBehavior> value) { 
          this.selectionBehavior = value;
          return this;
        }

        /**
         * @return Defines the selection behavior for the action and its children.
         */
        public ActionSelectionBehavior getSelectionBehavior() { 
          return this.selectionBehavior == null ? null : this.selectionBehavior.getValue();
        }

        /**
         * @param value Defines the selection behavior for the action and its children.
         */
        public PlanDefinitionActionComponent setSelectionBehavior(ActionSelectionBehavior value) { 
          if (value == null)
            this.selectionBehavior = null;
          else {
            if (this.selectionBehavior == null)
              this.selectionBehavior = new Enumeration<ActionSelectionBehavior>(new ActionSelectionBehaviorEnumFactory());
            this.selectionBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #requiredBehavior} (Defines the requiredness behavior for the action.). This is the underlying object with id, value and extensions. The accessor "getRequiredBehavior" gives direct access to the value
         */
        public Enumeration<ActionRequiredBehavior> getRequiredBehaviorElement() { 
          if (this.requiredBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.requiredBehavior");
            else if (Configuration.doAutoCreate())
              this.requiredBehavior = new Enumeration<ActionRequiredBehavior>(new ActionRequiredBehaviorEnumFactory()); // bb
          return this.requiredBehavior;
        }

        public boolean hasRequiredBehaviorElement() { 
          return this.requiredBehavior != null && !this.requiredBehavior.isEmpty();
        }

        public boolean hasRequiredBehavior() { 
          return this.requiredBehavior != null && !this.requiredBehavior.isEmpty();
        }

        /**
         * @param value {@link #requiredBehavior} (Defines the requiredness behavior for the action.). This is the underlying object with id, value and extensions. The accessor "getRequiredBehavior" gives direct access to the value
         */
        public PlanDefinitionActionComponent setRequiredBehaviorElement(Enumeration<ActionRequiredBehavior> value) { 
          this.requiredBehavior = value;
          return this;
        }

        /**
         * @return Defines the requiredness behavior for the action.
         */
        public ActionRequiredBehavior getRequiredBehavior() { 
          return this.requiredBehavior == null ? null : this.requiredBehavior.getValue();
        }

        /**
         * @param value Defines the requiredness behavior for the action.
         */
        public PlanDefinitionActionComponent setRequiredBehavior(ActionRequiredBehavior value) { 
          if (value == null)
            this.requiredBehavior = null;
          else {
            if (this.requiredBehavior == null)
              this.requiredBehavior = new Enumeration<ActionRequiredBehavior>(new ActionRequiredBehaviorEnumFactory());
            this.requiredBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #precheckBehavior} (Defines whether the action should usually be preselected.). This is the underlying object with id, value and extensions. The accessor "getPrecheckBehavior" gives direct access to the value
         */
        public Enumeration<ActionPrecheckBehavior> getPrecheckBehaviorElement() { 
          if (this.precheckBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.precheckBehavior");
            else if (Configuration.doAutoCreate())
              this.precheckBehavior = new Enumeration<ActionPrecheckBehavior>(new ActionPrecheckBehaviorEnumFactory()); // bb
          return this.precheckBehavior;
        }

        public boolean hasPrecheckBehaviorElement() { 
          return this.precheckBehavior != null && !this.precheckBehavior.isEmpty();
        }

        public boolean hasPrecheckBehavior() { 
          return this.precheckBehavior != null && !this.precheckBehavior.isEmpty();
        }

        /**
         * @param value {@link #precheckBehavior} (Defines whether the action should usually be preselected.). This is the underlying object with id, value and extensions. The accessor "getPrecheckBehavior" gives direct access to the value
         */
        public PlanDefinitionActionComponent setPrecheckBehaviorElement(Enumeration<ActionPrecheckBehavior> value) { 
          this.precheckBehavior = value;
          return this;
        }

        /**
         * @return Defines whether the action should usually be preselected.
         */
        public ActionPrecheckBehavior getPrecheckBehavior() { 
          return this.precheckBehavior == null ? null : this.precheckBehavior.getValue();
        }

        /**
         * @param value Defines whether the action should usually be preselected.
         */
        public PlanDefinitionActionComponent setPrecheckBehavior(ActionPrecheckBehavior value) { 
          if (value == null)
            this.precheckBehavior = null;
          else {
            if (this.precheckBehavior == null)
              this.precheckBehavior = new Enumeration<ActionPrecheckBehavior>(new ActionPrecheckBehaviorEnumFactory());
            this.precheckBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #cardinalityBehavior} (Defines whether the action can be selected multiple times.). This is the underlying object with id, value and extensions. The accessor "getCardinalityBehavior" gives direct access to the value
         */
        public Enumeration<ActionCardinalityBehavior> getCardinalityBehaviorElement() { 
          if (this.cardinalityBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.cardinalityBehavior");
            else if (Configuration.doAutoCreate())
              this.cardinalityBehavior = new Enumeration<ActionCardinalityBehavior>(new ActionCardinalityBehaviorEnumFactory()); // bb
          return this.cardinalityBehavior;
        }

        public boolean hasCardinalityBehaviorElement() { 
          return this.cardinalityBehavior != null && !this.cardinalityBehavior.isEmpty();
        }

        public boolean hasCardinalityBehavior() { 
          return this.cardinalityBehavior != null && !this.cardinalityBehavior.isEmpty();
        }

        /**
         * @param value {@link #cardinalityBehavior} (Defines whether the action can be selected multiple times.). This is the underlying object with id, value and extensions. The accessor "getCardinalityBehavior" gives direct access to the value
         */
        public PlanDefinitionActionComponent setCardinalityBehaviorElement(Enumeration<ActionCardinalityBehavior> value) { 
          this.cardinalityBehavior = value;
          return this;
        }

        /**
         * @return Defines whether the action can be selected multiple times.
         */
        public ActionCardinalityBehavior getCardinalityBehavior() { 
          return this.cardinalityBehavior == null ? null : this.cardinalityBehavior.getValue();
        }

        /**
         * @param value Defines whether the action can be selected multiple times.
         */
        public PlanDefinitionActionComponent setCardinalityBehavior(ActionCardinalityBehavior value) { 
          if (value == null)
            this.cardinalityBehavior = null;
          else {
            if (this.cardinalityBehavior == null)
              this.cardinalityBehavior = new Enumeration<ActionCardinalityBehavior>(new ActionCardinalityBehaviorEnumFactory());
            this.cardinalityBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #definition} (A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that describes a series of actions to be taken.)
         */
        public Reference getDefinition() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definition = new Reference(); // cc
          return this.definition;
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that describes a series of actions to be taken.)
         */
        public PlanDefinitionActionComponent setDefinition(Reference value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return {@link #definition} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that describes a series of actions to be taken.)
         */
        public Resource getDefinitionTarget() { 
          return this.definitionTarget;
        }

        /**
         * @param value {@link #definition} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that describes a series of actions to be taken.)
         */
        public PlanDefinitionActionComponent setDefinitionTarget(Resource value) { 
          this.definitionTarget = value;
          return this;
        }

        /**
         * @return {@link #transform} (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
         */
        public Reference getTransform() { 
          if (this.transform == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.transform");
            else if (Configuration.doAutoCreate())
              this.transform = new Reference(); // cc
          return this.transform;
        }

        public boolean hasTransform() { 
          return this.transform != null && !this.transform.isEmpty();
        }

        /**
         * @param value {@link #transform} (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
         */
        public PlanDefinitionActionComponent setTransform(Reference value) { 
          this.transform = value;
          return this;
        }

        /**
         * @return {@link #transform} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
         */
        public StructureMap getTransformTarget() { 
          if (this.transformTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionComponent.transform");
            else if (Configuration.doAutoCreate())
              this.transformTarget = new StructureMap(); // aa
          return this.transformTarget;
        }

        /**
         * @param value {@link #transform} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
         */
        public PlanDefinitionActionComponent setTransformTarget(StructureMap value) { 
          this.transformTarget = value;
          return this;
        }

        /**
         * @return {@link #dynamicValue} (Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.)
         */
        public List<PlanDefinitionActionDynamicValueComponent> getDynamicValue() { 
          if (this.dynamicValue == null)
            this.dynamicValue = new ArrayList<PlanDefinitionActionDynamicValueComponent>();
          return this.dynamicValue;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setDynamicValue(List<PlanDefinitionActionDynamicValueComponent> theDynamicValue) { 
          this.dynamicValue = theDynamicValue;
          return this;
        }

        public boolean hasDynamicValue() { 
          if (this.dynamicValue == null)
            return false;
          for (PlanDefinitionActionDynamicValueComponent item : this.dynamicValue)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionDynamicValueComponent addDynamicValue() { //3
          PlanDefinitionActionDynamicValueComponent t = new PlanDefinitionActionDynamicValueComponent();
          if (this.dynamicValue == null)
            this.dynamicValue = new ArrayList<PlanDefinitionActionDynamicValueComponent>();
          this.dynamicValue.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addDynamicValue(PlanDefinitionActionDynamicValueComponent t) { //3
          if (t == null)
            return this;
          if (this.dynamicValue == null)
            this.dynamicValue = new ArrayList<PlanDefinitionActionDynamicValueComponent>();
          this.dynamicValue.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #dynamicValue}, creating it if it does not already exist
         */
        public PlanDefinitionActionDynamicValueComponent getDynamicValueFirstRep() { 
          if (getDynamicValue().isEmpty()) {
            addDynamicValue();
          }
          return getDynamicValue().get(0);
        }

        /**
         * @return {@link #action} (Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.)
         */
        public List<PlanDefinitionActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<PlanDefinitionActionComponent>();
          return this.action;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionComponent setAction(List<PlanDefinitionActionComponent> theAction) { 
          this.action = theAction;
          return this;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (PlanDefinitionActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionComponent addAction() { //3
          PlanDefinitionActionComponent t = new PlanDefinitionActionComponent();
          if (this.action == null)
            this.action = new ArrayList<PlanDefinitionActionComponent>();
          this.action.add(t);
          return t;
        }

        public PlanDefinitionActionComponent addAction(PlanDefinitionActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<PlanDefinitionActionComponent>();
          this.action.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
         */
        public PlanDefinitionActionComponent getActionFirstRep() { 
          if (getAction().isEmpty()) {
            addAction();
          }
          return getAction().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("label", "string", "A user-visible label for the action.", 0, java.lang.Integer.MAX_VALUE, label));
          childrenList.add(new Property("title", "string", "The title of the action displayed to a user.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("description", "string", "A short description of the action used to provide a summary to display to the user.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("textEquivalent", "string", "A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.", 0, java.lang.Integer.MAX_VALUE, textEquivalent));
          childrenList.add(new Property("code", "CodeableConcept", "A code that provides meaning for the action or action group. For example, a section may have a LOINC code for a the section of a documentation template.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("reason", "CodeableConcept", "A description of why this action is necessary or appropriate.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("documentation", "RelatedArtifact", "Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("goalId", "id", "Identifies goals that this action supports. The reference must be to a goal element defined within this plan definition.", 0, java.lang.Integer.MAX_VALUE, goalId));
          childrenList.add(new Property("triggerDefinition", "TriggerDefinition", "A description of when the action should be triggered.", 0, java.lang.Integer.MAX_VALUE, triggerDefinition));
          childrenList.add(new Property("condition", "", "An expression that describes applicability criteria, or start/stop conditions for the action.", 0, java.lang.Integer.MAX_VALUE, condition));
          childrenList.add(new Property("input", "DataRequirement", "Defines input data requirements for the action.", 0, java.lang.Integer.MAX_VALUE, input));
          childrenList.add(new Property("output", "DataRequirement", "Defines the outputs of the action, if any.", 0, java.lang.Integer.MAX_VALUE, output));
          childrenList.add(new Property("relatedAction", "", "A relationship to another action such as \"before\" or \"30-60 minutes after start of\".", 0, java.lang.Integer.MAX_VALUE, relatedAction));
          childrenList.add(new Property("timing[x]", "dateTime|Period|Duration|Range|Timing", "An optional value describing when the action should be performed.", 0, java.lang.Integer.MAX_VALUE, timing));
          childrenList.add(new Property("participant", "", "Indicates who should participate in performing the action described.", 0, java.lang.Integer.MAX_VALUE, participant));
          childrenList.add(new Property("type", "Coding", "The type of action to perform (create, update, remove).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("groupingBehavior", "code", "Defines the grouping behavior for the action and its children.", 0, java.lang.Integer.MAX_VALUE, groupingBehavior));
          childrenList.add(new Property("selectionBehavior", "code", "Defines the selection behavior for the action and its children.", 0, java.lang.Integer.MAX_VALUE, selectionBehavior));
          childrenList.add(new Property("requiredBehavior", "code", "Defines the requiredness behavior for the action.", 0, java.lang.Integer.MAX_VALUE, requiredBehavior));
          childrenList.add(new Property("precheckBehavior", "code", "Defines whether the action should usually be preselected.", 0, java.lang.Integer.MAX_VALUE, precheckBehavior));
          childrenList.add(new Property("cardinalityBehavior", "code", "Defines whether the action can be selected multiple times.", 0, java.lang.Integer.MAX_VALUE, cardinalityBehavior));
          childrenList.add(new Property("definition", "Reference(ActivityDefinition|PlanDefinition)", "A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that describes a series of actions to be taken.", 0, java.lang.Integer.MAX_VALUE, definition));
          childrenList.add(new Property("transform", "Reference(StructureMap)", "A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.", 0, java.lang.Integer.MAX_VALUE, transform));
          childrenList.add(new Property("dynamicValue", "", "Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.", 0, java.lang.Integer.MAX_VALUE, dynamicValue));
          childrenList.add(new Property("action", "@PlanDefinition.action", "Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 102727412: /*label*/ return this.label == null ? new Base[0] : new Base[] {this.label}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -900391049: /*textEquivalent*/ return this.textEquivalent == null ? new Base[0] : new Base[] {this.textEquivalent}; // StringType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // CodeableConcept
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // CodeableConcept
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : this.documentation.toArray(new Base[this.documentation.size()]); // RelatedArtifact
        case -1240658034: /*goalId*/ return this.goalId == null ? new Base[0] : this.goalId.toArray(new Base[this.goalId.size()]); // IdType
        case 1126736171: /*triggerDefinition*/ return this.triggerDefinition == null ? new Base[0] : this.triggerDefinition.toArray(new Base[this.triggerDefinition.size()]); // TriggerDefinition
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : this.condition.toArray(new Base[this.condition.size()]); // PlanDefinitionActionConditionComponent
        case 100358090: /*input*/ return this.input == null ? new Base[0] : this.input.toArray(new Base[this.input.size()]); // DataRequirement
        case -1005512447: /*output*/ return this.output == null ? new Base[0] : this.output.toArray(new Base[this.output.size()]); // DataRequirement
        case -384107967: /*relatedAction*/ return this.relatedAction == null ? new Base[0] : this.relatedAction.toArray(new Base[this.relatedAction.size()]); // PlanDefinitionActionRelatedActionComponent
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Type
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // PlanDefinitionActionParticipantComponent
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 586678389: /*groupingBehavior*/ return this.groupingBehavior == null ? new Base[0] : new Base[] {this.groupingBehavior}; // Enumeration<ActionGroupingBehavior>
        case 168639486: /*selectionBehavior*/ return this.selectionBehavior == null ? new Base[0] : new Base[] {this.selectionBehavior}; // Enumeration<ActionSelectionBehavior>
        case -1163906287: /*requiredBehavior*/ return this.requiredBehavior == null ? new Base[0] : new Base[] {this.requiredBehavior}; // Enumeration<ActionRequiredBehavior>
        case -1174249033: /*precheckBehavior*/ return this.precheckBehavior == null ? new Base[0] : new Base[] {this.precheckBehavior}; // Enumeration<ActionPrecheckBehavior>
        case -922577408: /*cardinalityBehavior*/ return this.cardinalityBehavior == null ? new Base[0] : new Base[] {this.cardinalityBehavior}; // Enumeration<ActionCardinalityBehavior>
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // Reference
        case 1052666732: /*transform*/ return this.transform == null ? new Base[0] : new Base[] {this.transform}; // Reference
        case 572625010: /*dynamicValue*/ return this.dynamicValue == null ? new Base[0] : this.dynamicValue.toArray(new Base[this.dynamicValue.size()]); // PlanDefinitionActionDynamicValueComponent
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // PlanDefinitionActionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 102727412: // label
          this.label = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -900391049: // textEquivalent
          this.textEquivalent = castToString(value); // StringType
          return value;
        case 3059181: // code
          this.getCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -934964668: // reason
          this.getReason().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1587405498: // documentation
          this.getDocumentation().add(castToRelatedArtifact(value)); // RelatedArtifact
          return value;
        case -1240658034: // goalId
          this.getGoalId().add(castToId(value)); // IdType
          return value;
        case 1126736171: // triggerDefinition
          this.getTriggerDefinition().add(castToTriggerDefinition(value)); // TriggerDefinition
          return value;
        case -861311717: // condition
          this.getCondition().add((PlanDefinitionActionConditionComponent) value); // PlanDefinitionActionConditionComponent
          return value;
        case 100358090: // input
          this.getInput().add(castToDataRequirement(value)); // DataRequirement
          return value;
        case -1005512447: // output
          this.getOutput().add(castToDataRequirement(value)); // DataRequirement
          return value;
        case -384107967: // relatedAction
          this.getRelatedAction().add((PlanDefinitionActionRelatedActionComponent) value); // PlanDefinitionActionRelatedActionComponent
          return value;
        case -873664438: // timing
          this.timing = castToType(value); // Type
          return value;
        case 767422259: // participant
          this.getParticipant().add((PlanDefinitionActionParticipantComponent) value); // PlanDefinitionActionParticipantComponent
          return value;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          return value;
        case 586678389: // groupingBehavior
          value = new ActionGroupingBehaviorEnumFactory().fromType(castToCode(value));
          this.groupingBehavior = (Enumeration) value; // Enumeration<ActionGroupingBehavior>
          return value;
        case 168639486: // selectionBehavior
          value = new ActionSelectionBehaviorEnumFactory().fromType(castToCode(value));
          this.selectionBehavior = (Enumeration) value; // Enumeration<ActionSelectionBehavior>
          return value;
        case -1163906287: // requiredBehavior
          value = new ActionRequiredBehaviorEnumFactory().fromType(castToCode(value));
          this.requiredBehavior = (Enumeration) value; // Enumeration<ActionRequiredBehavior>
          return value;
        case -1174249033: // precheckBehavior
          value = new ActionPrecheckBehaviorEnumFactory().fromType(castToCode(value));
          this.precheckBehavior = (Enumeration) value; // Enumeration<ActionPrecheckBehavior>
          return value;
        case -922577408: // cardinalityBehavior
          value = new ActionCardinalityBehaviorEnumFactory().fromType(castToCode(value));
          this.cardinalityBehavior = (Enumeration) value; // Enumeration<ActionCardinalityBehavior>
          return value;
        case -1014418093: // definition
          this.definition = castToReference(value); // Reference
          return value;
        case 1052666732: // transform
          this.transform = castToReference(value); // Reference
          return value;
        case 572625010: // dynamicValue
          this.getDynamicValue().add((PlanDefinitionActionDynamicValueComponent) value); // PlanDefinitionActionDynamicValueComponent
          return value;
        case -1422950858: // action
          this.getAction().add((PlanDefinitionActionComponent) value); // PlanDefinitionActionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("label")) {
          this.label = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("textEquivalent")) {
          this.textEquivalent = castToString(value); // StringType
        } else if (name.equals("code")) {
          this.getCode().add(castToCodeableConcept(value));
        } else if (name.equals("reason")) {
          this.getReason().add(castToCodeableConcept(value));
        } else if (name.equals("documentation")) {
          this.getDocumentation().add(castToRelatedArtifact(value));
        } else if (name.equals("goalId")) {
          this.getGoalId().add(castToId(value));
        } else if (name.equals("triggerDefinition")) {
          this.getTriggerDefinition().add(castToTriggerDefinition(value));
        } else if (name.equals("condition")) {
          this.getCondition().add((PlanDefinitionActionConditionComponent) value);
        } else if (name.equals("input")) {
          this.getInput().add(castToDataRequirement(value));
        } else if (name.equals("output")) {
          this.getOutput().add(castToDataRequirement(value));
        } else if (name.equals("relatedAction")) {
          this.getRelatedAction().add((PlanDefinitionActionRelatedActionComponent) value);
        } else if (name.equals("timing[x]")) {
          this.timing = castToType(value); // Type
        } else if (name.equals("participant")) {
          this.getParticipant().add((PlanDefinitionActionParticipantComponent) value);
        } else if (name.equals("type")) {
          this.type = castToCoding(value); // Coding
        } else if (name.equals("groupingBehavior")) {
          value = new ActionGroupingBehaviorEnumFactory().fromType(castToCode(value));
          this.groupingBehavior = (Enumeration) value; // Enumeration<ActionGroupingBehavior>
        } else if (name.equals("selectionBehavior")) {
          value = new ActionSelectionBehaviorEnumFactory().fromType(castToCode(value));
          this.selectionBehavior = (Enumeration) value; // Enumeration<ActionSelectionBehavior>
        } else if (name.equals("requiredBehavior")) {
          value = new ActionRequiredBehaviorEnumFactory().fromType(castToCode(value));
          this.requiredBehavior = (Enumeration) value; // Enumeration<ActionRequiredBehavior>
        } else if (name.equals("precheckBehavior")) {
          value = new ActionPrecheckBehaviorEnumFactory().fromType(castToCode(value));
          this.precheckBehavior = (Enumeration) value; // Enumeration<ActionPrecheckBehavior>
        } else if (name.equals("cardinalityBehavior")) {
          value = new ActionCardinalityBehaviorEnumFactory().fromType(castToCode(value));
          this.cardinalityBehavior = (Enumeration) value; // Enumeration<ActionCardinalityBehavior>
        } else if (name.equals("definition")) {
          this.definition = castToReference(value); // Reference
        } else if (name.equals("transform")) {
          this.transform = castToReference(value); // Reference
        } else if (name.equals("dynamicValue")) {
          this.getDynamicValue().add((PlanDefinitionActionDynamicValueComponent) value);
        } else if (name.equals("action")) {
          this.getAction().add((PlanDefinitionActionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 102727412:  return getLabelElement();
        case 110371416:  return getTitleElement();
        case -1724546052:  return getDescriptionElement();
        case -900391049:  return getTextEquivalentElement();
        case 3059181:  return addCode(); 
        case -934964668:  return addReason(); 
        case 1587405498:  return addDocumentation(); 
        case -1240658034:  return addGoalIdElement();
        case 1126736171:  return addTriggerDefinition(); 
        case -861311717:  return addCondition(); 
        case 100358090:  return addInput(); 
        case -1005512447:  return addOutput(); 
        case -384107967:  return addRelatedAction(); 
        case 164632566:  return getTiming(); 
        case -873664438:  return getTiming(); 
        case 767422259:  return addParticipant(); 
        case 3575610:  return getType(); 
        case 586678389:  return getGroupingBehaviorElement();
        case 168639486:  return getSelectionBehaviorElement();
        case -1163906287:  return getRequiredBehaviorElement();
        case -1174249033:  return getPrecheckBehaviorElement();
        case -922577408:  return getCardinalityBehaviorElement();
        case -1014418093:  return getDefinition(); 
        case 1052666732:  return getTransform(); 
        case 572625010:  return addDynamicValue(); 
        case -1422950858:  return addAction(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 102727412: /*label*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -900391049: /*textEquivalent*/ return new String[] {"string"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -934964668: /*reason*/ return new String[] {"CodeableConcept"};
        case 1587405498: /*documentation*/ return new String[] {"RelatedArtifact"};
        case -1240658034: /*goalId*/ return new String[] {"id"};
        case 1126736171: /*triggerDefinition*/ return new String[] {"TriggerDefinition"};
        case -861311717: /*condition*/ return new String[] {};
        case 100358090: /*input*/ return new String[] {"DataRequirement"};
        case -1005512447: /*output*/ return new String[] {"DataRequirement"};
        case -384107967: /*relatedAction*/ return new String[] {};
        case -873664438: /*timing*/ return new String[] {"dateTime", "Period", "Duration", "Range", "Timing"};
        case 767422259: /*participant*/ return new String[] {};
        case 3575610: /*type*/ return new String[] {"Coding"};
        case 586678389: /*groupingBehavior*/ return new String[] {"code"};
        case 168639486: /*selectionBehavior*/ return new String[] {"code"};
        case -1163906287: /*requiredBehavior*/ return new String[] {"code"};
        case -1174249033: /*precheckBehavior*/ return new String[] {"code"};
        case -922577408: /*cardinalityBehavior*/ return new String[] {"code"};
        case -1014418093: /*definition*/ return new String[] {"Reference"};
        case 1052666732: /*transform*/ return new String[] {"Reference"};
        case 572625010: /*dynamicValue*/ return new String[] {};
        case -1422950858: /*action*/ return new String[] {"@PlanDefinition.action"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("label")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.label");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.title");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.description");
        }
        else if (name.equals("textEquivalent")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.textEquivalent");
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("reason")) {
          return addReason();
        }
        else if (name.equals("documentation")) {
          return addDocumentation();
        }
        else if (name.equals("goalId")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.goalId");
        }
        else if (name.equals("triggerDefinition")) {
          return addTriggerDefinition();
        }
        else if (name.equals("condition")) {
          return addCondition();
        }
        else if (name.equals("input")) {
          return addInput();
        }
        else if (name.equals("output")) {
          return addOutput();
        }
        else if (name.equals("relatedAction")) {
          return addRelatedAction();
        }
        else if (name.equals("timingDateTime")) {
          this.timing = new DateTimeType();
          return this.timing;
        }
        else if (name.equals("timingPeriod")) {
          this.timing = new Period();
          return this.timing;
        }
        else if (name.equals("timingDuration")) {
          this.timing = new Duration();
          return this.timing;
        }
        else if (name.equals("timingRange")) {
          this.timing = new Range();
          return this.timing;
        }
        else if (name.equals("timingTiming")) {
          this.timing = new Timing();
          return this.timing;
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("groupingBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.groupingBehavior");
        }
        else if (name.equals("selectionBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.selectionBehavior");
        }
        else if (name.equals("requiredBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.requiredBehavior");
        }
        else if (name.equals("precheckBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.precheckBehavior");
        }
        else if (name.equals("cardinalityBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.cardinalityBehavior");
        }
        else if (name.equals("definition")) {
          this.definition = new Reference();
          return this.definition;
        }
        else if (name.equals("transform")) {
          this.transform = new Reference();
          return this.transform;
        }
        else if (name.equals("dynamicValue")) {
          return addDynamicValue();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionActionComponent copy() {
        PlanDefinitionActionComponent dst = new PlanDefinitionActionComponent();
        copyValues(dst);
        dst.label = label == null ? null : label.copy();
        dst.title = title == null ? null : title.copy();
        dst.description = description == null ? null : description.copy();
        dst.textEquivalent = textEquivalent == null ? null : textEquivalent.copy();
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        if (reason != null) {
          dst.reason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reason)
            dst.reason.add(i.copy());
        };
        if (documentation != null) {
          dst.documentation = new ArrayList<RelatedArtifact>();
          for (RelatedArtifact i : documentation)
            dst.documentation.add(i.copy());
        };
        if (goalId != null) {
          dst.goalId = new ArrayList<IdType>();
          for (IdType i : goalId)
            dst.goalId.add(i.copy());
        };
        if (triggerDefinition != null) {
          dst.triggerDefinition = new ArrayList<TriggerDefinition>();
          for (TriggerDefinition i : triggerDefinition)
            dst.triggerDefinition.add(i.copy());
        };
        if (condition != null) {
          dst.condition = new ArrayList<PlanDefinitionActionConditionComponent>();
          for (PlanDefinitionActionConditionComponent i : condition)
            dst.condition.add(i.copy());
        };
        if (input != null) {
          dst.input = new ArrayList<DataRequirement>();
          for (DataRequirement i : input)
            dst.input.add(i.copy());
        };
        if (output != null) {
          dst.output = new ArrayList<DataRequirement>();
          for (DataRequirement i : output)
            dst.output.add(i.copy());
        };
        if (relatedAction != null) {
          dst.relatedAction = new ArrayList<PlanDefinitionActionRelatedActionComponent>();
          for (PlanDefinitionActionRelatedActionComponent i : relatedAction)
            dst.relatedAction.add(i.copy());
        };
        dst.timing = timing == null ? null : timing.copy();
        if (participant != null) {
          dst.participant = new ArrayList<PlanDefinitionActionParticipantComponent>();
          for (PlanDefinitionActionParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.groupingBehavior = groupingBehavior == null ? null : groupingBehavior.copy();
        dst.selectionBehavior = selectionBehavior == null ? null : selectionBehavior.copy();
        dst.requiredBehavior = requiredBehavior == null ? null : requiredBehavior.copy();
        dst.precheckBehavior = precheckBehavior == null ? null : precheckBehavior.copy();
        dst.cardinalityBehavior = cardinalityBehavior == null ? null : cardinalityBehavior.copy();
        dst.definition = definition == null ? null : definition.copy();
        dst.transform = transform == null ? null : transform.copy();
        if (dynamicValue != null) {
          dst.dynamicValue = new ArrayList<PlanDefinitionActionDynamicValueComponent>();
          for (PlanDefinitionActionDynamicValueComponent i : dynamicValue)
            dst.dynamicValue.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<PlanDefinitionActionComponent>();
          for (PlanDefinitionActionComponent i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionActionComponent))
          return false;
        PlanDefinitionActionComponent o = (PlanDefinitionActionComponent) other;
        return compareDeep(label, o.label, true) && compareDeep(title, o.title, true) && compareDeep(description, o.description, true)
           && compareDeep(textEquivalent, o.textEquivalent, true) && compareDeep(code, o.code, true) && compareDeep(reason, o.reason, true)
           && compareDeep(documentation, o.documentation, true) && compareDeep(goalId, o.goalId, true) && compareDeep(triggerDefinition, o.triggerDefinition, true)
           && compareDeep(condition, o.condition, true) && compareDeep(input, o.input, true) && compareDeep(output, o.output, true)
           && compareDeep(relatedAction, o.relatedAction, true) && compareDeep(timing, o.timing, true) && compareDeep(participant, o.participant, true)
           && compareDeep(type, o.type, true) && compareDeep(groupingBehavior, o.groupingBehavior, true) && compareDeep(selectionBehavior, o.selectionBehavior, true)
           && compareDeep(requiredBehavior, o.requiredBehavior, true) && compareDeep(precheckBehavior, o.precheckBehavior, true)
           && compareDeep(cardinalityBehavior, o.cardinalityBehavior, true) && compareDeep(definition, o.definition, true)
           && compareDeep(transform, o.transform, true) && compareDeep(dynamicValue, o.dynamicValue, true)
           && compareDeep(action, o.action, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionComponent))
          return false;
        PlanDefinitionActionComponent o = (PlanDefinitionActionComponent) other;
        return compareValues(label, o.label, true) && compareValues(title, o.title, true) && compareValues(description, o.description, true)
           && compareValues(textEquivalent, o.textEquivalent, true) && compareValues(goalId, o.goalId, true) && compareValues(groupingBehavior, o.groupingBehavior, true)
           && compareValues(selectionBehavior, o.selectionBehavior, true) && compareValues(requiredBehavior, o.requiredBehavior, true)
           && compareValues(precheckBehavior, o.precheckBehavior, true) && compareValues(cardinalityBehavior, o.cardinalityBehavior, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(label, title, description
          , textEquivalent, code, reason, documentation, goalId, triggerDefinition, condition
          , input, output, relatedAction, timing, participant, type, groupingBehavior, selectionBehavior
          , requiredBehavior, precheckBehavior, cardinalityBehavior, definition, transform, dynamicValue
          , action);
      }

  public String fhirType() {
    return "PlanDefinition.action";

  }

  }

    @Block()
    public static class PlanDefinitionActionConditionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The kind of condition.
         */
        @Child(name = "kind", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="applicability | start | stop", formalDefinition="The kind of condition." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-condition-kind")
        protected Enumeration<ActionConditionKind> kind;

        /**
         * A brief, natural language description of the condition that effectively communicates the intended semantics.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Natural language description of the condition", formalDefinition="A brief, natural language description of the condition that effectively communicates the intended semantics." )
        protected StringType description;

        /**
         * The media type of the language for the expression.
         */
        @Child(name = "language", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language of the expression", formalDefinition="The media type of the language for the expression." )
        protected StringType language;

        /**
         * An expression that returns true or false, indicating whether or not the condition is satisfied.
         */
        @Child(name = "expression", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Boolean-valued expression", formalDefinition="An expression that returns true or false, indicating whether or not the condition is satisfied." )
        protected StringType expression;

        private static final long serialVersionUID = 944300105L;

    /**
     * Constructor
     */
      public PlanDefinitionActionConditionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PlanDefinitionActionConditionComponent(Enumeration<ActionConditionKind> kind) {
        super();
        this.kind = kind;
      }

        /**
         * @return {@link #kind} (The kind of condition.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
         */
        public Enumeration<ActionConditionKind> getKindElement() { 
          if (this.kind == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionConditionComponent.kind");
            else if (Configuration.doAutoCreate())
              this.kind = new Enumeration<ActionConditionKind>(new ActionConditionKindEnumFactory()); // bb
          return this.kind;
        }

        public boolean hasKindElement() { 
          return this.kind != null && !this.kind.isEmpty();
        }

        public boolean hasKind() { 
          return this.kind != null && !this.kind.isEmpty();
        }

        /**
         * @param value {@link #kind} (The kind of condition.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
         */
        public PlanDefinitionActionConditionComponent setKindElement(Enumeration<ActionConditionKind> value) { 
          this.kind = value;
          return this;
        }

        /**
         * @return The kind of condition.
         */
        public ActionConditionKind getKind() { 
          return this.kind == null ? null : this.kind.getValue();
        }

        /**
         * @param value The kind of condition.
         */
        public PlanDefinitionActionConditionComponent setKind(ActionConditionKind value) { 
            if (this.kind == null)
              this.kind = new Enumeration<ActionConditionKind>(new ActionConditionKindEnumFactory());
            this.kind.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (A brief, natural language description of the condition that effectively communicates the intended semantics.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionConditionComponent.description");
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
         * @param value {@link #description} (A brief, natural language description of the condition that effectively communicates the intended semantics.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public PlanDefinitionActionConditionComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A brief, natural language description of the condition that effectively communicates the intended semantics.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A brief, natural language description of the condition that effectively communicates the intended semantics.
         */
        public PlanDefinitionActionConditionComponent setDescription(String value) { 
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
         * @return {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public StringType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionConditionComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new StringType(); // bb
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public PlanDefinitionActionConditionComponent setLanguageElement(StringType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return The media type of the language for the expression.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value The media type of the language for the expression.
         */
        public PlanDefinitionActionConditionComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new StringType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #expression} (An expression that returns true or false, indicating whether or not the condition is satisfied.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionConditionComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (An expression that returns true or false, indicating whether or not the condition is satisfied.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public PlanDefinitionActionConditionComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return An expression that returns true or false, indicating whether or not the condition is satisfied.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value An expression that returns true or false, indicating whether or not the condition is satisfied.
         */
        public PlanDefinitionActionConditionComponent setExpression(String value) { 
          if (Utilities.noString(value))
            this.expression = null;
          else {
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("kind", "code", "The kind of condition.", 0, java.lang.Integer.MAX_VALUE, kind));
          childrenList.add(new Property("description", "string", "A brief, natural language description of the condition that effectively communicates the intended semantics.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("language", "string", "The media type of the language for the expression.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("expression", "string", "An expression that returns true or false, indicating whether or not the condition is satisfied.", 0, java.lang.Integer.MAX_VALUE, expression));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3292052: /*kind*/ return this.kind == null ? new Base[0] : new Base[] {this.kind}; // Enumeration<ActionConditionKind>
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // StringType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3292052: // kind
          value = new ActionConditionKindEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<ActionConditionKind>
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1613589672: // language
          this.language = castToString(value); // StringType
          return value;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("kind")) {
          value = new ActionConditionKindEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<ActionConditionKind>
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("language")) {
          this.language = castToString(value); // StringType
        } else if (name.equals("expression")) {
          this.expression = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3292052:  return getKindElement();
        case -1724546052:  return getDescriptionElement();
        case -1613589672:  return getLanguageElement();
        case -1795452264:  return getExpressionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3292052: /*kind*/ return new String[] {"code"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1613589672: /*language*/ return new String[] {"string"};
        case -1795452264: /*expression*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("kind")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.kind");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.description");
        }
        else if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.language");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.expression");
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionActionConditionComponent copy() {
        PlanDefinitionActionConditionComponent dst = new PlanDefinitionActionConditionComponent();
        copyValues(dst);
        dst.kind = kind == null ? null : kind.copy();
        dst.description = description == null ? null : description.copy();
        dst.language = language == null ? null : language.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionActionConditionComponent))
          return false;
        PlanDefinitionActionConditionComponent o = (PlanDefinitionActionConditionComponent) other;
        return compareDeep(kind, o.kind, true) && compareDeep(description, o.description, true) && compareDeep(language, o.language, true)
           && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionConditionComponent))
          return false;
        PlanDefinitionActionConditionComponent o = (PlanDefinitionActionConditionComponent) other;
        return compareValues(kind, o.kind, true) && compareValues(description, o.description, true) && compareValues(language, o.language, true)
           && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(kind, description, language
          , expression);
      }

  public String fhirType() {
    return "PlanDefinition.action.condition";

  }

  }

    @Block()
    public static class PlanDefinitionActionRelatedActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The element id of the related action.
         */
        @Child(name = "actionId", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What action is this related to", formalDefinition="The element id of the related action." )
        protected IdType actionId;

        /**
         * The relationship of this action to the related action.
         */
        @Child(name = "relationship", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="before-start | before | before-end | concurrent-with-start | concurrent | concurrent-with-end | after-start | after | after-end", formalDefinition="The relationship of this action to the related action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-relationship-type")
        protected Enumeration<ActionRelationshipType> relationship;

        /**
         * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
         */
        @Child(name = "offset", type = {Duration.class, Range.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time offset for the relationship", formalDefinition="A duration or range of durations to apply to the relationship. For example, 30-60 minutes before." )
        protected Type offset;

        private static final long serialVersionUID = 1063306770L;

    /**
     * Constructor
     */
      public PlanDefinitionActionRelatedActionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PlanDefinitionActionRelatedActionComponent(IdType actionId, Enumeration<ActionRelationshipType> relationship) {
        super();
        this.actionId = actionId;
        this.relationship = relationship;
      }

        /**
         * @return {@link #actionId} (The element id of the related action.). This is the underlying object with id, value and extensions. The accessor "getActionId" gives direct access to the value
         */
        public IdType getActionIdElement() { 
          if (this.actionId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionRelatedActionComponent.actionId");
            else if (Configuration.doAutoCreate())
              this.actionId = new IdType(); // bb
          return this.actionId;
        }

        public boolean hasActionIdElement() { 
          return this.actionId != null && !this.actionId.isEmpty();
        }

        public boolean hasActionId() { 
          return this.actionId != null && !this.actionId.isEmpty();
        }

        /**
         * @param value {@link #actionId} (The element id of the related action.). This is the underlying object with id, value and extensions. The accessor "getActionId" gives direct access to the value
         */
        public PlanDefinitionActionRelatedActionComponent setActionIdElement(IdType value) { 
          this.actionId = value;
          return this;
        }

        /**
         * @return The element id of the related action.
         */
        public String getActionId() { 
          return this.actionId == null ? null : this.actionId.getValue();
        }

        /**
         * @param value The element id of the related action.
         */
        public PlanDefinitionActionRelatedActionComponent setActionId(String value) { 
            if (this.actionId == null)
              this.actionId = new IdType();
            this.actionId.setValue(value);
          return this;
        }

        /**
         * @return {@link #relationship} (The relationship of this action to the related action.). This is the underlying object with id, value and extensions. The accessor "getRelationship" gives direct access to the value
         */
        public Enumeration<ActionRelationshipType> getRelationshipElement() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionRelatedActionComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new Enumeration<ActionRelationshipType>(new ActionRelationshipTypeEnumFactory()); // bb
          return this.relationship;
        }

        public boolean hasRelationshipElement() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        public boolean hasRelationship() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        /**
         * @param value {@link #relationship} (The relationship of this action to the related action.). This is the underlying object with id, value and extensions. The accessor "getRelationship" gives direct access to the value
         */
        public PlanDefinitionActionRelatedActionComponent setRelationshipElement(Enumeration<ActionRelationshipType> value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return The relationship of this action to the related action.
         */
        public ActionRelationshipType getRelationship() { 
          return this.relationship == null ? null : this.relationship.getValue();
        }

        /**
         * @param value The relationship of this action to the related action.
         */
        public PlanDefinitionActionRelatedActionComponent setRelationship(ActionRelationshipType value) { 
            if (this.relationship == null)
              this.relationship = new Enumeration<ActionRelationshipType>(new ActionRelationshipTypeEnumFactory());
            this.relationship.setValue(value);
          return this;
        }

        /**
         * @return {@link #offset} (A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.)
         */
        public Type getOffset() { 
          return this.offset;
        }

        /**
         * @return {@link #offset} (A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.)
         */
        public Duration getOffsetDuration() throws FHIRException { 
          if (!(this.offset instanceof Duration))
            throw new FHIRException("Type mismatch: the type Duration was expected, but "+this.offset.getClass().getName()+" was encountered");
          return (Duration) this.offset;
        }

        public boolean hasOffsetDuration() { 
          return this.offset instanceof Duration;
        }

        /**
         * @return {@link #offset} (A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.)
         */
        public Range getOffsetRange() throws FHIRException { 
          if (!(this.offset instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.offset.getClass().getName()+" was encountered");
          return (Range) this.offset;
        }

        public boolean hasOffsetRange() { 
          return this.offset instanceof Range;
        }

        public boolean hasOffset() { 
          return this.offset != null && !this.offset.isEmpty();
        }

        /**
         * @param value {@link #offset} (A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.)
         */
        public PlanDefinitionActionRelatedActionComponent setOffset(Type value) { 
          this.offset = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("actionId", "id", "The element id of the related action.", 0, java.lang.Integer.MAX_VALUE, actionId));
          childrenList.add(new Property("relationship", "code", "The relationship of this action to the related action.", 0, java.lang.Integer.MAX_VALUE, relationship));
          childrenList.add(new Property("offset[x]", "Duration|Range", "A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.", 0, java.lang.Integer.MAX_VALUE, offset));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1656172047: /*actionId*/ return this.actionId == null ? new Base[0] : new Base[] {this.actionId}; // IdType
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // Enumeration<ActionRelationshipType>
        case -1019779949: /*offset*/ return this.offset == null ? new Base[0] : new Base[] {this.offset}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1656172047: // actionId
          this.actionId = castToId(value); // IdType
          return value;
        case -261851592: // relationship
          value = new ActionRelationshipTypeEnumFactory().fromType(castToCode(value));
          this.relationship = (Enumeration) value; // Enumeration<ActionRelationshipType>
          return value;
        case -1019779949: // offset
          this.offset = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actionId")) {
          this.actionId = castToId(value); // IdType
        } else if (name.equals("relationship")) {
          value = new ActionRelationshipTypeEnumFactory().fromType(castToCode(value));
          this.relationship = (Enumeration) value; // Enumeration<ActionRelationshipType>
        } else if (name.equals("offset[x]")) {
          this.offset = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1656172047:  return getActionIdElement();
        case -261851592:  return getRelationshipElement();
        case -1960684787:  return getOffset(); 
        case -1019779949:  return getOffset(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1656172047: /*actionId*/ return new String[] {"id"};
        case -261851592: /*relationship*/ return new String[] {"code"};
        case -1019779949: /*offset*/ return new String[] {"Duration", "Range"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actionId")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.actionId");
        }
        else if (name.equals("relationship")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.relationship");
        }
        else if (name.equals("offsetDuration")) {
          this.offset = new Duration();
          return this.offset;
        }
        else if (name.equals("offsetRange")) {
          this.offset = new Range();
          return this.offset;
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionActionRelatedActionComponent copy() {
        PlanDefinitionActionRelatedActionComponent dst = new PlanDefinitionActionRelatedActionComponent();
        copyValues(dst);
        dst.actionId = actionId == null ? null : actionId.copy();
        dst.relationship = relationship == null ? null : relationship.copy();
        dst.offset = offset == null ? null : offset.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionActionRelatedActionComponent))
          return false;
        PlanDefinitionActionRelatedActionComponent o = (PlanDefinitionActionRelatedActionComponent) other;
        return compareDeep(actionId, o.actionId, true) && compareDeep(relationship, o.relationship, true)
           && compareDeep(offset, o.offset, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionRelatedActionComponent))
          return false;
        PlanDefinitionActionRelatedActionComponent o = (PlanDefinitionActionRelatedActionComponent) other;
        return compareValues(actionId, o.actionId, true) && compareValues(relationship, o.relationship, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actionId, relationship, offset
          );
      }

  public String fhirType() {
    return "PlanDefinition.action.relatedAction";

  }

  }

    @Block()
    public static class PlanDefinitionActionParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of participant in the action.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="patient | practitioner | related-person", formalDefinition="The type of participant in the action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-participant-type")
        protected Enumeration<ActionParticipantType> type;

        /**
         * The role the participant should play in performing the described action.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="E.g. Nurse, Surgeon, Parent, etc", formalDefinition="The role the participant should play in performing the described action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-participant-role")
        protected CodeableConcept role;

        private static final long serialVersionUID = -1152013659L;

    /**
     * Constructor
     */
      public PlanDefinitionActionParticipantComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PlanDefinitionActionParticipantComponent(Enumeration<ActionParticipantType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The type of participant in the action.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ActionParticipantType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionParticipantComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ActionParticipantType>(new ActionParticipantTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of participant in the action.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public PlanDefinitionActionParticipantComponent setTypeElement(Enumeration<ActionParticipantType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of participant in the action.
         */
        public ActionParticipantType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of participant in the action.
         */
        public PlanDefinitionActionParticipantComponent setType(ActionParticipantType value) { 
            if (this.type == null)
              this.type = new Enumeration<ActionParticipantType>(new ActionParticipantTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #role} (The role the participant should play in performing the described action.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionParticipantComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (The role the participant should play in performing the described action.)
         */
        public PlanDefinitionActionParticipantComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of participant in the action.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("role", "CodeableConcept", "The role the participant should play in performing the described action.", 0, java.lang.Integer.MAX_VALUE, role));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ActionParticipantType>
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new ActionParticipantTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ActionParticipantType>
          return value;
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new ActionParticipantTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ActionParticipantType>
        } else if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 3506294:  return getRole(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.type");
        }
        else if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionActionParticipantComponent copy() {
        PlanDefinitionActionParticipantComponent dst = new PlanDefinitionActionParticipantComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.role = role == null ? null : role.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionActionParticipantComponent))
          return false;
        PlanDefinitionActionParticipantComponent o = (PlanDefinitionActionParticipantComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(role, o.role, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionParticipantComponent))
          return false;
        PlanDefinitionActionParticipantComponent o = (PlanDefinitionActionParticipantComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, role);
      }

  public String fhirType() {
    return "PlanDefinition.action.participant";

  }

  }

    @Block()
    public static class PlanDefinitionActionDynamicValueComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A brief, natural language description of the intended semantics of the dynamic value.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Natural language description of the dynamic value", formalDefinition="A brief, natural language description of the intended semantics of the dynamic value." )
        protected StringType description;

        /**
         * The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        @Child(name = "path", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The path to the element to be set dynamically", formalDefinition="The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression." )
        protected StringType path;

        /**
         * The media type of the language for the expression.
         */
        @Child(name = "language", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language of the expression", formalDefinition="The media type of the language for the expression." )
        protected StringType language;

        /**
         * An expression specifying the value of the customized element.
         */
        @Child(name = "expression", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="An expression that provides the dynamic value for the customization", formalDefinition="An expression specifying the value of the customized element." )
        protected StringType expression;

        private static final long serialVersionUID = 448404361L;

    /**
     * Constructor
     */
      public PlanDefinitionActionDynamicValueComponent() {
        super();
      }

        /**
         * @return {@link #description} (A brief, natural language description of the intended semantics of the dynamic value.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDynamicValueComponent.description");
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
         * @param value {@link #description} (A brief, natural language description of the intended semantics of the dynamic value.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public PlanDefinitionActionDynamicValueComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A brief, natural language description of the intended semantics of the dynamic value.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A brief, natural language description of the intended semantics of the dynamic value.
         */
        public PlanDefinitionActionDynamicValueComponent setDescription(String value) { 
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
         * @return {@link #path} (The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDynamicValueComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public PlanDefinitionActionDynamicValueComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        public PlanDefinitionActionDynamicValueComponent setPath(String value) { 
          if (Utilities.noString(value))
            this.path = null;
          else {
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public StringType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDynamicValueComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new StringType(); // bb
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public PlanDefinitionActionDynamicValueComponent setLanguageElement(StringType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return The media type of the language for the expression.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value The media type of the language for the expression.
         */
        public PlanDefinitionActionDynamicValueComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new StringType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #expression} (An expression specifying the value of the customized element.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDynamicValueComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (An expression specifying the value of the customized element.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public PlanDefinitionActionDynamicValueComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return An expression specifying the value of the customized element.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value An expression specifying the value of the customized element.
         */
        public PlanDefinitionActionDynamicValueComponent setExpression(String value) { 
          if (Utilities.noString(value))
            this.expression = null;
          else {
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "A brief, natural language description of the intended semantics of the dynamic value.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("path", "string", "The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("language", "string", "The media type of the language for the expression.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("expression", "string", "An expression specifying the value of the customized element.", 0, java.lang.Integer.MAX_VALUE, expression));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // StringType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        case -1613589672: // language
          this.language = castToString(value); // StringType
          return value;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else if (name.equals("language")) {
          this.language = castToString(value); // StringType
        } else if (name.equals("expression")) {
          this.expression = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case 3433509:  return getPathElement();
        case -1613589672:  return getLanguageElement();
        case -1795452264:  return getExpressionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case 3433509: /*path*/ return new String[] {"string"};
        case -1613589672: /*language*/ return new String[] {"string"};
        case -1795452264: /*expression*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.description");
        }
        else if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.path");
        }
        else if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.language");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.expression");
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionActionDynamicValueComponent copy() {
        PlanDefinitionActionDynamicValueComponent dst = new PlanDefinitionActionDynamicValueComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.path = path == null ? null : path.copy();
        dst.language = language == null ? null : language.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDynamicValueComponent))
          return false;
        PlanDefinitionActionDynamicValueComponent o = (PlanDefinitionActionDynamicValueComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(path, o.path, true) && compareDeep(language, o.language, true)
           && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDynamicValueComponent))
          return false;
        PlanDefinitionActionDynamicValueComponent o = (PlanDefinitionActionDynamicValueComponent) other;
        return compareValues(description, o.description, true) && compareValues(path, o.path, true) && compareValues(language, o.language, true)
           && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, path, language
          , expression);
      }

  public String fhirType() {
    return "PlanDefinition.action.dynamicValue";

  }

  }

    /**
     * A formal identifier that is used to identify this plan definition when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the plan definition", formalDefinition="A formal identifier that is used to identify this plan definition when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * The type of asset the plan definition represents, e.g. an order set, protocol, or event-condition-action rule.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="order-set | protocol | eca-rule", formalDefinition="The type of asset the plan definition represents, e.g. an order set, protocol, or event-condition-action rule." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/plan-definition-type")
    protected CodeableConcept type;

    /**
     * Explaination of why this plan definition is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this plan definition is defined", formalDefinition="Explaination of why this plan definition is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * A detailed description of how the asset is used from a clinical perspective.
     */
    @Child(name = "usage", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the clinical usage of the asset", formalDefinition="A detailed description of how the asset is used from a clinical perspective." )
    protected StringType usage;

    /**
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    @Child(name = "approvalDate", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the plan definition was approved by publisher", formalDefinition="The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage." )
    protected DateType approvalDate;

    /**
     * The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the plan definition was last reviewed", formalDefinition="The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date." )
    protected DateType lastReviewDate;

    /**
     * The period during which the plan definition content was or is planned to be in active use.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the plan definition is expected to be used", formalDefinition="The period during which the plan definition content was or is planned to be in active use." )
    protected Period effectivePeriod;

    /**
     * Descriptive topics related to the content of the plan definition. Topics provide a high-level categorization of the definition that can be useful for filtering and searching.
     */
    @Child(name = "topic", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="E.g. Education, Treatment, Assessment, etc", formalDefinition="Descriptive topics related to the content of the plan definition. Topics provide a high-level categorization of the definition that can be useful for filtering and searching." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/definition-topic")
    protected List<CodeableConcept> topic;

    /**
     * A contributor to the content of the asset, including authors, editors, reviewers, and endorsers.
     */
    @Child(name = "contributor", type = {Contributor.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A content contributor", formalDefinition="A contributor to the content of the asset, including authors, editors, reviewers, and endorsers." )
    protected List<Contributor> contributor;

    /**
     * A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the plan definition.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the plan definition." )
    protected MarkdownType copyright;

    /**
     * Related artifacts such as additional documentation, justification, or bibliographic references.
     */
    @Child(name = "relatedArtifact", type = {RelatedArtifact.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Related artifacts for the asset", formalDefinition="Related artifacts such as additional documentation, justification, or bibliographic references." )
    protected List<RelatedArtifact> relatedArtifact;

    /**
     * A reference to a Library resource containing any formal logic used by the plan definition.
     */
    @Child(name = "library", type = {Library.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Logic used by the plan definition", formalDefinition="A reference to a Library resource containing any formal logic used by the plan definition." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library resource containing any formal logic used by the plan definition.)
     */
    protected List<Library> libraryTarget;


    /**
     * Goals that describe what the activities within the plan are intended to achieve. For example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
     */
    @Child(name = "goal", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What the plan is trying to accomplish", formalDefinition="Goals that describe what the activities within the plan are intended to achieve. For example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc." )
    protected List<PlanDefinitionGoalComponent> goal;

    /**
     * An action to be taken as part of the plan.
     */
    @Child(name = "action", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Action defined by the plan", formalDefinition="An action to be taken as part of the plan." )
    protected List<PlanDefinitionActionComponent> action;

    private static final long serialVersionUID = -1191108677L;

  /**
   * Constructor
   */
    public PlanDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public PlanDefinition(Enumeration<PublicationStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this plan definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this plan definition is (or will be) published. The URL SHOULD include the major version of the plan definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (An absolute URI that is used to identify this plan definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this plan definition is (or will be) published. The URL SHOULD include the major version of the plan definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public PlanDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this plan definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this plan definition is (or will be) published. The URL SHOULD include the major version of the plan definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this plan definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this plan definition is (or will be) published. The URL SHOULD include the major version of the plan definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public PlanDefinition setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #identifier} (A formal identifier that is used to identify this plan definition when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setIdentifier(List<Identifier> theIdentifier) { 
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

    public PlanDefinition addIdentifier(Identifier t) { //3
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
     * @return {@link #version} (The identifier that is used to identify this version of the plan definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The identifier that is used to identify this version of the plan definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public PlanDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the plan definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the plan definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    public PlanDefinition setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #name} (A natural language name identifying the plan definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.name");
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
     * @param value {@link #name} (A natural language name identifying the plan definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public PlanDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the plan definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the plan definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public PlanDefinition setName(String value) { 
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
     * @return {@link #title} (A short, descriptive, user-friendly title for the plan definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.title");
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
     * @param value {@link #title} (A short, descriptive, user-friendly title for the plan definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public PlanDefinition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the plan definition.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the plan definition.
     */
    public PlanDefinition setTitle(String value) { 
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
     * @return {@link #type} (The type of asset the plan definition represents, e.g. an order set, protocol, or event-condition-action rule.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of asset the plan definition represents, e.g. an order set, protocol, or event-condition-action rule.)
     */
    public PlanDefinition setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of this plan definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this plan definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public PlanDefinition setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this plan definition. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this plan definition. Enables tracking the life-cycle of the content.
     */
    public PlanDefinition setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this plan definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (A boolean value to indicate that this plan definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public PlanDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this plan definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this plan definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public PlanDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the plan definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the plan definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date  (and optionally time) when the plan definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the plan definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public PlanDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the plan definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the plan definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the plan definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the plan definition changes.
     */
    public PlanDefinition setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the plan definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the individual or organization that published the plan definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public PlanDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the plan definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the plan definition.
     */
    public PlanDefinition setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (A free text natural language description of the plan definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the plan definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public PlanDefinition setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the plan definition from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the plan definition from a consumer's perspective.
     */
    public PlanDefinition setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #purpose} (Explaination of why this plan definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new MarkdownType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (Explaination of why this plan definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public PlanDefinition setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explaination of why this plan definition is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explaination of why this plan definition is needed and why it has been designed as it has.
     */
    public PlanDefinition setPurpose(String value) { 
      if (value == null)
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new MarkdownType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #usage} (A detailed description of how the asset is used from a clinical perspective.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public StringType getUsageElement() { 
      if (this.usage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.usage");
        else if (Configuration.doAutoCreate())
          this.usage = new StringType(); // bb
      return this.usage;
    }

    public boolean hasUsageElement() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    public boolean hasUsage() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    /**
     * @param value {@link #usage} (A detailed description of how the asset is used from a clinical perspective.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public PlanDefinition setUsageElement(StringType value) { 
      this.usage = value;
      return this;
    }

    /**
     * @return A detailed description of how the asset is used from a clinical perspective.
     */
    public String getUsage() { 
      return this.usage == null ? null : this.usage.getValue();
    }

    /**
     * @param value A detailed description of how the asset is used from a clinical perspective.
     */
    public PlanDefinition setUsage(String value) { 
      if (Utilities.noString(value))
        this.usage = null;
      else {
        if (this.usage == null)
          this.usage = new StringType();
        this.usage.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public DateType getApprovalDateElement() { 
      if (this.approvalDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.approvalDate");
        else if (Configuration.doAutoCreate())
          this.approvalDate = new DateType(); // bb
      return this.approvalDate;
    }

    public boolean hasApprovalDateElement() { 
      return this.approvalDate != null && !this.approvalDate.isEmpty();
    }

    public boolean hasApprovalDate() { 
      return this.approvalDate != null && !this.approvalDate.isEmpty();
    }

    /**
     * @param value {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public PlanDefinition setApprovalDateElement(DateType value) { 
      this.approvalDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public Date getApprovalDate() { 
      return this.approvalDate == null ? null : this.approvalDate.getValue();
    }

    /**
     * @param value The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public PlanDefinition setApprovalDate(Date value) { 
      if (value == null)
        this.approvalDate = null;
      else {
        if (this.approvalDate == null)
          this.approvalDate = new DateType();
        this.approvalDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public DateType getLastReviewDateElement() { 
      if (this.lastReviewDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.lastReviewDate");
        else if (Configuration.doAutoCreate())
          this.lastReviewDate = new DateType(); // bb
      return this.lastReviewDate;
    }

    public boolean hasLastReviewDateElement() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    public boolean hasLastReviewDate() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    /**
     * @param value {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public PlanDefinition setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
     */
    public PlanDefinition setLastReviewDate(Date value) { 
      if (value == null)
        this.lastReviewDate = null;
      else {
        if (this.lastReviewDate == null)
          this.lastReviewDate = new DateType();
        this.lastReviewDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #effectivePeriod} (The period during which the plan definition content was or is planned to be in active use.)
     */
    public Period getEffectivePeriod() { 
      if (this.effectivePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.effectivePeriod");
        else if (Configuration.doAutoCreate())
          this.effectivePeriod = new Period(); // cc
      return this.effectivePeriod;
    }

    public boolean hasEffectivePeriod() { 
      return this.effectivePeriod != null && !this.effectivePeriod.isEmpty();
    }

    /**
     * @param value {@link #effectivePeriod} (The period during which the plan definition content was or is planned to be in active use.)
     */
    public PlanDefinition setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate plan definition instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setUseContext(List<UsageContext> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (UsageContext item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addUseContext() { //3
      UsageContext t = new UsageContext();
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return t;
    }

    public PlanDefinition addUseContext(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public UsageContext getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #jurisdiction} (A legal or geographic region in which the plan definition is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public PlanDefinition addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #topic} (Descriptive topics related to the content of the plan definition. Topics provide a high-level categorization of the definition that can be useful for filtering and searching.)
     */
    public List<CodeableConcept> getTopic() { 
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      return this.topic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setTopic(List<CodeableConcept> theTopic) { 
      this.topic = theTopic;
      return this;
    }

    public boolean hasTopic() { 
      if (this.topic == null)
        return false;
      for (CodeableConcept item : this.topic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addTopic() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return t;
    }

    public PlanDefinition addTopic(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #topic}, creating it if it does not already exist
     */
    public CodeableConcept getTopicFirstRep() { 
      if (getTopic().isEmpty()) {
        addTopic();
      }
      return getTopic().get(0);
    }

    /**
     * @return {@link #contributor} (A contributor to the content of the asset, including authors, editors, reviewers, and endorsers.)
     */
    public List<Contributor> getContributor() { 
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      return this.contributor;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setContributor(List<Contributor> theContributor) { 
      this.contributor = theContributor;
      return this;
    }

    public boolean hasContributor() { 
      if (this.contributor == null)
        return false;
      for (Contributor item : this.contributor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Contributor addContributor() { //3
      Contributor t = new Contributor();
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      this.contributor.add(t);
      return t;
    }

    public PlanDefinition addContributor(Contributor t) { //3
      if (t == null)
        return this;
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      this.contributor.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contributor}, creating it if it does not already exist
     */
    public Contributor getContributorFirstRep() { 
      if (getContributor().isEmpty()) {
        addContributor();
      }
      return getContributor().get(0);
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public PlanDefinition addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the plan definition.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new MarkdownType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the plan definition.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public PlanDefinition setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the plan definition.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the plan definition.
     */
    public PlanDefinition setCopyright(String value) { 
      if (value == null)
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new MarkdownType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #relatedArtifact} (Related artifacts such as additional documentation, justification, or bibliographic references.)
     */
    public List<RelatedArtifact> getRelatedArtifact() { 
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      return this.relatedArtifact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setRelatedArtifact(List<RelatedArtifact> theRelatedArtifact) { 
      this.relatedArtifact = theRelatedArtifact;
      return this;
    }

    public boolean hasRelatedArtifact() { 
      if (this.relatedArtifact == null)
        return false;
      for (RelatedArtifact item : this.relatedArtifact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public RelatedArtifact addRelatedArtifact() { //3
      RelatedArtifact t = new RelatedArtifact();
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      this.relatedArtifact.add(t);
      return t;
    }

    public PlanDefinition addRelatedArtifact(RelatedArtifact t) { //3
      if (t == null)
        return this;
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      this.relatedArtifact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedArtifact}, creating it if it does not already exist
     */
    public RelatedArtifact getRelatedArtifactFirstRep() { 
      if (getRelatedArtifact().isEmpty()) {
        addRelatedArtifact();
      }
      return getRelatedArtifact().get(0);
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing any formal logic used by the plan definition.)
     */
    public List<Reference> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      return this.library;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setLibrary(List<Reference> theLibrary) { 
      this.library = theLibrary;
      return this;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (Reference item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addLibrary() { //3
      Reference t = new Reference();
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return t;
    }

    public PlanDefinition addLibrary(Reference t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #library}, creating it if it does not already exist
     */
    public Reference getLibraryFirstRep() { 
      if (getLibrary().isEmpty()) {
        addLibrary();
      }
      return getLibrary().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Library> getLibraryTarget() { 
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      return this.libraryTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Library addLibraryTarget() { 
      Library r = new Library();
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      this.libraryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #goal} (Goals that describe what the activities within the plan are intended to achieve. For example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.)
     */
    public List<PlanDefinitionGoalComponent> getGoal() { 
      if (this.goal == null)
        this.goal = new ArrayList<PlanDefinitionGoalComponent>();
      return this.goal;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setGoal(List<PlanDefinitionGoalComponent> theGoal) { 
      this.goal = theGoal;
      return this;
    }

    public boolean hasGoal() { 
      if (this.goal == null)
        return false;
      for (PlanDefinitionGoalComponent item : this.goal)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public PlanDefinitionGoalComponent addGoal() { //3
      PlanDefinitionGoalComponent t = new PlanDefinitionGoalComponent();
      if (this.goal == null)
        this.goal = new ArrayList<PlanDefinitionGoalComponent>();
      this.goal.add(t);
      return t;
    }

    public PlanDefinition addGoal(PlanDefinitionGoalComponent t) { //3
      if (t == null)
        return this;
      if (this.goal == null)
        this.goal = new ArrayList<PlanDefinitionGoalComponent>();
      this.goal.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #goal}, creating it if it does not already exist
     */
    public PlanDefinitionGoalComponent getGoalFirstRep() { 
      if (getGoal().isEmpty()) {
        addGoal();
      }
      return getGoal().get(0);
    }

    /**
     * @return {@link #action} (An action to be taken as part of the plan.)
     */
    public List<PlanDefinitionActionComponent> getAction() { 
      if (this.action == null)
        this.action = new ArrayList<PlanDefinitionActionComponent>();
      return this.action;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setAction(List<PlanDefinitionActionComponent> theAction) { 
      this.action = theAction;
      return this;
    }

    public boolean hasAction() { 
      if (this.action == null)
        return false;
      for (PlanDefinitionActionComponent item : this.action)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public PlanDefinitionActionComponent addAction() { //3
      PlanDefinitionActionComponent t = new PlanDefinitionActionComponent();
      if (this.action == null)
        this.action = new ArrayList<PlanDefinitionActionComponent>();
      this.action.add(t);
      return t;
    }

    public PlanDefinition addAction(PlanDefinitionActionComponent t) { //3
      if (t == null)
        return this;
      if (this.action == null)
        this.action = new ArrayList<PlanDefinitionActionComponent>();
      this.action.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
     */
    public PlanDefinitionActionComponent getActionFirstRep() { 
      if (getAction().isEmpty()) {
        addAction();
      }
      return getAction().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this plan definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this plan definition is (or will be) published. The URL SHOULD include the major version of the plan definition. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this plan definition when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the plan definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the plan definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the plan definition.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("type", "CodeableConcept", "The type of asset the plan definition represents, e.g. an order set, protocol, or event-condition-action rule.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("status", "code", "The status of this plan definition. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this plan definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the plan definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the plan definition changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the plan definition.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the plan definition from a consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("purpose", "markdown", "Explaination of why this plan definition is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("usage", "string", "A detailed description of how the asset is used from a clinical perspective.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, java.lang.Integer.MAX_VALUE, approvalDate));
        childrenList.add(new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.", 0, java.lang.Integer.MAX_VALUE, lastReviewDate));
        childrenList.add(new Property("effectivePeriod", "Period", "The period during which the plan definition content was or is planned to be in active use.", 0, java.lang.Integer.MAX_VALUE, effectivePeriod));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate plan definition instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the plan definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("topic", "CodeableConcept", "Descriptive topics related to the content of the plan definition. Topics provide a high-level categorization of the definition that can be useful for filtering and searching.", 0, java.lang.Integer.MAX_VALUE, topic));
        childrenList.add(new Property("contributor", "Contributor", "A contributor to the content of the asset, including authors, editors, reviewers, and endorsers.", 0, java.lang.Integer.MAX_VALUE, contributor));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("copyright", "markdown", "A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the plan definition.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("relatedArtifact", "RelatedArtifact", "Related artifacts such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library resource containing any formal logic used by the plan definition.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("goal", "", "Goals that describe what the activities within the plan are intended to achieve. For example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.", 0, java.lang.Integer.MAX_VALUE, goal));
        childrenList.add(new Property("action", "", "An action to be taken as part of the plan.", 0, java.lang.Integer.MAX_VALUE, action));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : new Base[] {this.usage}; // StringType
        case 223539345: /*approvalDate*/ return this.approvalDate == null ? new Base[0] : new Base[] {this.approvalDate}; // DateType
        case -1687512484: /*lastReviewDate*/ return this.lastReviewDate == null ? new Base[0] : new Base[] {this.lastReviewDate}; // DateType
        case -403934648: /*effectivePeriod*/ return this.effectivePeriod == null ? new Base[0] : new Base[] {this.effectivePeriod}; // Period
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : this.topic.toArray(new Base[this.topic.size()]); // CodeableConcept
        case -1895276325: /*contributor*/ return this.contributor == null ? new Base[0] : this.contributor.toArray(new Base[this.contributor.size()]); // Contributor
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 666807069: /*relatedArtifact*/ return this.relatedArtifact == null ? new Base[0] : this.relatedArtifact.toArray(new Base[this.relatedArtifact.size()]); // RelatedArtifact
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // Reference
        case 3178259: /*goal*/ return this.goal == null ? new Base[0] : this.goal.toArray(new Base[this.goal.size()]); // PlanDefinitionGoalComponent
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // PlanDefinitionActionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 111574433: // usage
          this.usage = castToString(value); // StringType
          return value;
        case 223539345: // approvalDate
          this.approvalDate = castToDate(value); // DateType
          return value;
        case -1687512484: // lastReviewDate
          this.lastReviewDate = castToDate(value); // DateType
          return value;
        case -403934648: // effectivePeriod
          this.effectivePeriod = castToPeriod(value); // Period
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 110546223: // topic
          this.getTopic().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1895276325: // contributor
          this.getContributor().add(castToContributor(value)); // Contributor
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 666807069: // relatedArtifact
          this.getRelatedArtifact().add(castToRelatedArtifact(value)); // RelatedArtifact
          return value;
        case 166208699: // library
          this.getLibrary().add(castToReference(value)); // Reference
          return value;
        case 3178259: // goal
          this.getGoal().add((PlanDefinitionGoalComponent) value); // PlanDefinitionGoalComponent
          return value;
        case -1422950858: // action
          this.getAction().add((PlanDefinitionActionComponent) value); // PlanDefinitionActionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("usage")) {
          this.usage = castToString(value); // StringType
        } else if (name.equals("approvalDate")) {
          this.approvalDate = castToDate(value); // DateType
        } else if (name.equals("lastReviewDate")) {
          this.lastReviewDate = castToDate(value); // DateType
        } else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = castToPeriod(value); // Period
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("topic")) {
          this.getTopic().add(castToCodeableConcept(value));
        } else if (name.equals("contributor")) {
          this.getContributor().add(castToContributor(value));
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("relatedArtifact")) {
          this.getRelatedArtifact().add(castToRelatedArtifact(value));
        } else if (name.equals("library")) {
          this.getLibrary().add(castToReference(value));
        } else if (name.equals("goal")) {
          this.getGoal().add((PlanDefinitionGoalComponent) value);
        } else if (name.equals("action")) {
          this.getAction().add((PlanDefinitionActionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case -1618432855:  return addIdentifier(); 
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case 3575610:  return getType(); 
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case -1724546052:  return getDescriptionElement();
        case -220463842:  return getPurposeElement();
        case 111574433:  return getUsageElement();
        case 223539345:  return getApprovalDateElement();
        case -1687512484:  return getLastReviewDateElement();
        case -403934648:  return getEffectivePeriod(); 
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 110546223:  return addTopic(); 
        case -1895276325:  return addContributor(); 
        case 951526432:  return addContact(); 
        case 1522889671:  return getCopyrightElement();
        case 666807069:  return addRelatedArtifact(); 
        case 166208699:  return addLibrary(); 
        case 3178259:  return addGoal(); 
        case -1422950858:  return addAction(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 111574433: /*usage*/ return new String[] {"string"};
        case 223539345: /*approvalDate*/ return new String[] {"date"};
        case -1687512484: /*lastReviewDate*/ return new String[] {"date"};
        case -403934648: /*effectivePeriod*/ return new String[] {"Period"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 110546223: /*topic*/ return new String[] {"CodeableConcept"};
        case -1895276325: /*contributor*/ return new String[] {"Contributor"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 666807069: /*relatedArtifact*/ return new String[] {"RelatedArtifact"};
        case 166208699: /*library*/ return new String[] {"Reference"};
        case 3178259: /*goal*/ return new String[] {};
        case -1422950858: /*action*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.title");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.publisher");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.description");
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.purpose");
        }
        else if (name.equals("usage")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.usage");
        }
        else if (name.equals("approvalDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.approvalDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.lastReviewDate");
        }
        else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = new Period();
          return this.effectivePeriod;
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("topic")) {
          return addTopic();
        }
        else if (name.equals("contributor")) {
          return addContributor();
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.copyright");
        }
        else if (name.equals("relatedArtifact")) {
          return addRelatedArtifact();
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("goal")) {
          return addGoal();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "PlanDefinition";

  }

      public PlanDefinition copy() {
        PlanDefinition dst = new PlanDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        dst.type = type == null ? null : type.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        dst.description = description == null ? null : description.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.usage = usage == null ? null : usage.copy();
        dst.approvalDate = approvalDate == null ? null : approvalDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<UsageContext>();
          for (UsageContext i : useContext)
            dst.useContext.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        if (topic != null) {
          dst.topic = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : topic)
            dst.topic.add(i.copy());
        };
        if (contributor != null) {
          dst.contributor = new ArrayList<Contributor>();
          for (Contributor i : contributor)
            dst.contributor.add(i.copy());
        };
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        if (relatedArtifact != null) {
          dst.relatedArtifact = new ArrayList<RelatedArtifact>();
          for (RelatedArtifact i : relatedArtifact)
            dst.relatedArtifact.add(i.copy());
        };
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        if (goal != null) {
          dst.goal = new ArrayList<PlanDefinitionGoalComponent>();
          for (PlanDefinitionGoalComponent i : goal)
            dst.goal.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<PlanDefinitionActionComponent>();
          for (PlanDefinitionActionComponent i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      protected PlanDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinition))
          return false;
        PlanDefinition o = (PlanDefinition) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(purpose, o.purpose, true)
           && compareDeep(usage, o.usage, true) && compareDeep(approvalDate, o.approvalDate, true) && compareDeep(lastReviewDate, o.lastReviewDate, true)
           && compareDeep(effectivePeriod, o.effectivePeriod, true) && compareDeep(topic, o.topic, true) && compareDeep(contributor, o.contributor, true)
           && compareDeep(copyright, o.copyright, true) && compareDeep(relatedArtifact, o.relatedArtifact, true)
           && compareDeep(library, o.library, true) && compareDeep(goal, o.goal, true) && compareDeep(action, o.action, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinition))
          return false;
        PlanDefinition o = (PlanDefinition) other;
        return compareValues(purpose, o.purpose, true) && compareValues(usage, o.usage, true) && compareValues(approvalDate, o.approvalDate, true)
           && compareValues(lastReviewDate, o.lastReviewDate, true) && compareValues(copyright, o.copyright, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, type, purpose
          , usage, approvalDate, lastReviewDate, effectivePeriod, topic, contributor, copyright
          , relatedArtifact, library, goal, action);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.PlanDefinition;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The plan definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PlanDefinition.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="PlanDefinition.date", description="The plan definition publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The plan definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PlanDefinition.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="PlanDefinition.identifier", description="External identifier for the plan definition", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>successor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PlanDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="successor", path="PlanDefinition.relatedArtifact.where(type='successor').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_SUCCESSOR = "successor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>successor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PlanDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUCCESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUCCESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PlanDefinition:successor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUCCESSOR = new ca.uhn.fhir.model.api.Include("PlanDefinition:successor").toLocked();

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="PlanDefinition.jurisdiction", description="Intended jurisdiction for the plan definition", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="PlanDefinition.description", description="The description of the plan definition", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>derived-from</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PlanDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="derived-from", path="PlanDefinition.relatedArtifact.where(type='derived-from').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_DERIVED_FROM = "derived-from";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>derived-from</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PlanDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DERIVED_FROM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DERIVED_FROM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PlanDefinition:derived-from</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DERIVED_FROM = new ca.uhn.fhir.model.api.Include("PlanDefinition:derived-from").toLocked();

 /**
   * Search parameter: <b>predecessor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PlanDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="predecessor", path="PlanDefinition.relatedArtifact.where(type='predecessor').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_PREDECESSOR = "predecessor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>predecessor</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PlanDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PREDECESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PREDECESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PlanDefinition:predecessor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PREDECESSOR = new ca.uhn.fhir.model.api.Include("PlanDefinition:predecessor").toLocked();

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="PlanDefinition.title", description="The human-friendly name of the plan definition", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>composed-of</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PlanDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="composed-of", path="PlanDefinition.relatedArtifact.where(type='composed-of').resource", description="What resource is being referenced", type="reference" )
  public static final String SP_COMPOSED_OF = "composed-of";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>composed-of</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PlanDefinition.relatedArtifact.resource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam COMPOSED_OF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_COMPOSED_OF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PlanDefinition:composed-of</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_COMPOSED_OF = new ca.uhn.fhir.model.api.Include("PlanDefinition:composed-of").toLocked();

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="PlanDefinition.version", description="The business version of the plan definition", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the plan definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>PlanDefinition.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="PlanDefinition.url", description="The uri that identifies the plan definition", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the plan definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>PlanDefinition.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>effective</b>
   * <p>
   * Description: <b>The time during which the plan definition is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PlanDefinition.effectivePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective", path="PlanDefinition.effectivePeriod", description="The time during which the plan definition is intended to be in use", type="date" )
  public static final String SP_EFFECTIVE = "effective";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective</b>
   * <p>
   * Description: <b>The time during which the plan definition is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PlanDefinition.effectivePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE);

 /**
   * Search parameter: <b>depends-on</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PlanDefinition.relatedArtifact.resource, PlanDefinition.library</b><br>
   * </p>
   */
  @SearchParamDefinition(name="depends-on", path="PlanDefinition.relatedArtifact.where(type='depends-on').resource | PlanDefinition.library", description="What resource is being referenced", type="reference" )
  public static final String SP_DEPENDS_ON = "depends-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>depends-on</b>
   * <p>
   * Description: <b>What resource is being referenced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PlanDefinition.relatedArtifact.resource, PlanDefinition.library</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEPENDS_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEPENDS_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PlanDefinition:depends-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEPENDS_ON = new ca.uhn.fhir.model.api.Include("PlanDefinition:depends-on").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="PlanDefinition.name", description="Computationally friendly name of the plan definition", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="PlanDefinition.publisher", description="Name of the publisher of the plan definition", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="PlanDefinition.topic", description="Topics associated with the module", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="PlanDefinition.status", description="The current status of the plan definition", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

