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
 * A group of related requests that can be used to capture intended activities that have inter-dependencies such as "give this medication after that one".
 */
@ResourceDef(name="RequestGroup", profile="http://hl7.org/fhir/Profile/RequestGroup")
public class RequestGroup extends DomainResource {

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
          if (code == null || code.isEmpty())
            return null;
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
          if (code == null || code.isEmpty())
            return null;
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
          if (code == null || code.isEmpty())
            return null;
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
          if (code == null || code.isEmpty())
            return null;
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
          if (code == null || code.isEmpty())
            return null;
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
          if (code == null || code.isEmpty())
            return null;
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
    public static class RequestGroupActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.
         */
        @Child(name = "actionIdentifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Unique identifier", formalDefinition="A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique." )
        protected Identifier actionIdentifier;

        /**
         * A user-visible label for the action.
         */
        @Child(name = "label", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="User-visible label for the action (e.g. 1. or A.)", formalDefinition="A user-visible label for the action." )
        protected StringType label;

        /**
         * The title of the action displayed to a user.
         */
        @Child(name = "title", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="User-visible title", formalDefinition="The title of the action displayed to a user." )
        protected StringType title;

        /**
         * A short description of the action used to provide a summary to display to the user.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short description of the action", formalDefinition="A short description of the action used to provide a summary to display to the user." )
        protected StringType description;

        /**
         * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.
         */
        @Child(name = "textEquivalent", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Static text equivalent of the action, used if the dynamic aspects cannot be interpreted by the receiving system", formalDefinition="A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically." )
        protected StringType textEquivalent;

        /**
         * The concept represented by this action or its sub-actions.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The meaning of the action or its sub-actions", formalDefinition="The concept represented by this action or its sub-actions." )
        protected List<CodeableConcept> code;

        /**
         * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.
         */
        @Child(name = "documentation", type = {RelatedArtifact.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Supporting documentation for the intended performer of the action", formalDefinition="Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources." )
        protected List<RelatedArtifact> documentation;

        /**
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         */
        @Child(name = "relatedAction", type = {}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Relationship to another action", formalDefinition="A relationship to another action such as \"before\" or \"30-60 minutes after start of\"." )
        protected RequestGroupActionRelatedActionComponent relatedAction;

        /**
         * An optional value describing when the action should be performed.
         */
        @Child(name = "timing", type = {DateTimeType.class, Period.class, Duration.class, Range.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When the action should take place", formalDefinition="An optional value describing when the action should be performed." )
        protected Type timing;

        /**
         * The participant in the action.
         */
        @Child(name = "participant", type = {Patient.class, Person.class, Practitioner.class, RelatedPerson.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Participant", formalDefinition="The participant in the action." )
        protected List<Reference> participant;
        /**
         * The actual objects that are the target of the reference (The participant in the action.)
         */
        protected List<Resource> participantTarget;


        /**
         * The type of action to perform (create, update, remove).
         */
        @Child(name = "type", type = {Coding.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="create | update | remove | fire-event", formalDefinition="The type of action to perform (create, update, remove)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-type")
        protected Coding type;

        /**
         * Defines the grouping behavior for the action and its children.
         */
        @Child(name = "groupingBehavior", type = {CodeType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="visual-group | logical-group | sentence-group", formalDefinition="Defines the grouping behavior for the action and its children." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-grouping-behavior")
        protected Enumeration<ActionGroupingBehavior> groupingBehavior;

        /**
         * Defines the selection behavior for the action and its children.
         */
        @Child(name = "selectionBehavior", type = {CodeType.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="any | all | all-or-none | exactly-one | at-most-one | one-or-more", formalDefinition="Defines the selection behavior for the action and its children." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-selection-behavior")
        protected Enumeration<ActionSelectionBehavior> selectionBehavior;

        /**
         * Defines the requiredness behavior for the action.
         */
        @Child(name = "requiredBehavior", type = {CodeType.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="must | could | must-unless-documented", formalDefinition="Defines the requiredness behavior for the action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-required-behavior")
        protected Enumeration<ActionRequiredBehavior> requiredBehavior;

        /**
         * Defines whether the action should usually be preselected.
         */
        @Child(name = "precheckBehavior", type = {CodeType.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="yes | no", formalDefinition="Defines whether the action should usually be preselected." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-precheck-behavior")
        protected Enumeration<ActionPrecheckBehavior> precheckBehavior;

        /**
         * Defines whether the action can be selected multiple times.
         */
        @Child(name = "cardinalityBehavior", type = {CodeType.class}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="single | multiple", formalDefinition="Defines whether the action can be selected multiple times." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-cardinality-behavior")
        protected Enumeration<ActionCardinalityBehavior> cardinalityBehavior;

        /**
         * The resource that is the target of the action (e.g. CommunicationRequest).
         */
        @Child(name = "resource", type = {Reference.class}, order=17, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The target of the action", formalDefinition="The resource that is the target of the action (e.g. CommunicationRequest)." )
        protected Reference resource;

        /**
         * The actual object that is the target of the reference (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        protected Resource resourceTarget;

        /**
         * Sub actions.
         */
        @Child(name = "action", type = {RequestGroupActionComponent.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Sub action", formalDefinition="Sub actions." )
        protected List<RequestGroupActionComponent> action;

        private static final long serialVersionUID = 1707056341L;

    /**
     * Constructor
     */
      public RequestGroupActionComponent() {
        super();
      }

        /**
         * @return {@link #actionIdentifier} (A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.)
         */
        public Identifier getActionIdentifier() { 
          if (this.actionIdentifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RequestGroupActionComponent.actionIdentifier");
            else if (Configuration.doAutoCreate())
              this.actionIdentifier = new Identifier(); // cc
          return this.actionIdentifier;
        }

        public boolean hasActionIdentifier() { 
          return this.actionIdentifier != null && !this.actionIdentifier.isEmpty();
        }

        /**
         * @param value {@link #actionIdentifier} (A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.)
         */
        public RequestGroupActionComponent setActionIdentifier(Identifier value) { 
          this.actionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #label} (A user-visible label for the action.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public StringType getLabelElement() { 
          if (this.label == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RequestGroupActionComponent.label");
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
        public RequestGroupActionComponent setLabelElement(StringType value) { 
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
        public RequestGroupActionComponent setLabel(String value) { 
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
              throw new Error("Attempt to auto-create RequestGroupActionComponent.title");
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
        public RequestGroupActionComponent setTitleElement(StringType value) { 
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
        public RequestGroupActionComponent setTitle(String value) { 
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
              throw new Error("Attempt to auto-create RequestGroupActionComponent.description");
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
        public RequestGroupActionComponent setDescriptionElement(StringType value) { 
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
        public RequestGroupActionComponent setDescription(String value) { 
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
              throw new Error("Attempt to auto-create RequestGroupActionComponent.textEquivalent");
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
        public RequestGroupActionComponent setTextEquivalentElement(StringType value) { 
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
        public RequestGroupActionComponent setTextEquivalent(String value) { 
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
         * @return {@link #code} (The concept represented by this action or its sub-actions.)
         */
        public List<CodeableConcept> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public RequestGroupActionComponent setCode(List<CodeableConcept> theCode) { 
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

        public RequestGroupActionComponent addCode(CodeableConcept t) { //3
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
        public RequestGroupActionComponent setDocumentation(List<RelatedArtifact> theDocumentation) { 
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

        public RequestGroupActionComponent addDocumentation(RelatedArtifact t) { //3
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
         * @return {@link #relatedAction} (A relationship to another action such as "before" or "30-60 minutes after start of".)
         */
        public RequestGroupActionRelatedActionComponent getRelatedAction() { 
          if (this.relatedAction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RequestGroupActionComponent.relatedAction");
            else if (Configuration.doAutoCreate())
              this.relatedAction = new RequestGroupActionRelatedActionComponent(); // cc
          return this.relatedAction;
        }

        public boolean hasRelatedAction() { 
          return this.relatedAction != null && !this.relatedAction.isEmpty();
        }

        /**
         * @param value {@link #relatedAction} (A relationship to another action such as "before" or "30-60 minutes after start of".)
         */
        public RequestGroupActionComponent setRelatedAction(RequestGroupActionRelatedActionComponent value) { 
          this.relatedAction = value;
          return this;
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

        public boolean hasTiming() { 
          return this.timing != null && !this.timing.isEmpty();
        }

        /**
         * @param value {@link #timing} (An optional value describing when the action should be performed.)
         */
        public RequestGroupActionComponent setTiming(Type value) { 
          this.timing = value;
          return this;
        }

        /**
         * @return {@link #participant} (The participant in the action.)
         */
        public List<Reference> getParticipant() { 
          if (this.participant == null)
            this.participant = new ArrayList<Reference>();
          return this.participant;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public RequestGroupActionComponent setParticipant(List<Reference> theParticipant) { 
          this.participant = theParticipant;
          return this;
        }

        public boolean hasParticipant() { 
          if (this.participant == null)
            return false;
          for (Reference item : this.participant)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addParticipant() { //3
          Reference t = new Reference();
          if (this.participant == null)
            this.participant = new ArrayList<Reference>();
          this.participant.add(t);
          return t;
        }

        public RequestGroupActionComponent addParticipant(Reference t) { //3
          if (t == null)
            return this;
          if (this.participant == null)
            this.participant = new ArrayList<Reference>();
          this.participant.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #participant}, creating it if it does not already exist
         */
        public Reference getParticipantFirstRep() { 
          if (getParticipant().isEmpty()) {
            addParticipant();
          }
          return getParticipant().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getParticipantTarget() { 
          if (this.participantTarget == null)
            this.participantTarget = new ArrayList<Resource>();
          return this.participantTarget;
        }

        /**
         * @return {@link #type} (The type of action to perform (create, update, remove).)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RequestGroupActionComponent.type");
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
        public RequestGroupActionComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #groupingBehavior} (Defines the grouping behavior for the action and its children.). This is the underlying object with id, value and extensions. The accessor "getGroupingBehavior" gives direct access to the value
         */
        public Enumeration<ActionGroupingBehavior> getGroupingBehaviorElement() { 
          if (this.groupingBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RequestGroupActionComponent.groupingBehavior");
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
        public RequestGroupActionComponent setGroupingBehaviorElement(Enumeration<ActionGroupingBehavior> value) { 
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
        public RequestGroupActionComponent setGroupingBehavior(ActionGroupingBehavior value) { 
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
              throw new Error("Attempt to auto-create RequestGroupActionComponent.selectionBehavior");
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
        public RequestGroupActionComponent setSelectionBehaviorElement(Enumeration<ActionSelectionBehavior> value) { 
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
        public RequestGroupActionComponent setSelectionBehavior(ActionSelectionBehavior value) { 
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
              throw new Error("Attempt to auto-create RequestGroupActionComponent.requiredBehavior");
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
        public RequestGroupActionComponent setRequiredBehaviorElement(Enumeration<ActionRequiredBehavior> value) { 
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
        public RequestGroupActionComponent setRequiredBehavior(ActionRequiredBehavior value) { 
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
              throw new Error("Attempt to auto-create RequestGroupActionComponent.precheckBehavior");
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
        public RequestGroupActionComponent setPrecheckBehaviorElement(Enumeration<ActionPrecheckBehavior> value) { 
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
        public RequestGroupActionComponent setPrecheckBehavior(ActionPrecheckBehavior value) { 
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
              throw new Error("Attempt to auto-create RequestGroupActionComponent.cardinalityBehavior");
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
        public RequestGroupActionComponent setCardinalityBehaviorElement(Enumeration<ActionCardinalityBehavior> value) { 
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
        public RequestGroupActionComponent setCardinalityBehavior(ActionCardinalityBehavior value) { 
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
         * @return {@link #resource} (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        public Reference getResource() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RequestGroupActionComponent.resource");
            else if (Configuration.doAutoCreate())
              this.resource = new Reference(); // cc
          return this.resource;
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        public RequestGroupActionComponent setResource(Reference value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return {@link #resource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        public Resource getResourceTarget() { 
          return this.resourceTarget;
        }

        /**
         * @param value {@link #resource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        public RequestGroupActionComponent setResourceTarget(Resource value) { 
          this.resourceTarget = value;
          return this;
        }

        /**
         * @return {@link #action} (Sub actions.)
         */
        public List<RequestGroupActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<RequestGroupActionComponent>();
          return this.action;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public RequestGroupActionComponent setAction(List<RequestGroupActionComponent> theAction) { 
          this.action = theAction;
          return this;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (RequestGroupActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public RequestGroupActionComponent addAction() { //3
          RequestGroupActionComponent t = new RequestGroupActionComponent();
          if (this.action == null)
            this.action = new ArrayList<RequestGroupActionComponent>();
          this.action.add(t);
          return t;
        }

        public RequestGroupActionComponent addAction(RequestGroupActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<RequestGroupActionComponent>();
          this.action.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
         */
        public RequestGroupActionComponent getActionFirstRep() { 
          if (getAction().isEmpty()) {
            addAction();
          }
          return getAction().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("actionIdentifier", "Identifier", "A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.", 0, java.lang.Integer.MAX_VALUE, actionIdentifier));
          childrenList.add(new Property("label", "string", "A user-visible label for the action.", 0, java.lang.Integer.MAX_VALUE, label));
          childrenList.add(new Property("title", "string", "The title of the action displayed to a user.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("description", "string", "A short description of the action used to provide a summary to display to the user.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("textEquivalent", "string", "A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.", 0, java.lang.Integer.MAX_VALUE, textEquivalent));
          childrenList.add(new Property("code", "CodeableConcept", "The concept represented by this action or its sub-actions.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("documentation", "RelatedArtifact", "Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("relatedAction", "", "A relationship to another action such as \"before\" or \"30-60 minutes after start of\".", 0, java.lang.Integer.MAX_VALUE, relatedAction));
          childrenList.add(new Property("timing[x]", "dateTime|Period|Duration|Range", "An optional value describing when the action should be performed.", 0, java.lang.Integer.MAX_VALUE, timing));
          childrenList.add(new Property("participant", "Reference(Patient|Person|Practitioner|RelatedPerson)", "The participant in the action.", 0, java.lang.Integer.MAX_VALUE, participant));
          childrenList.add(new Property("type", "Coding", "The type of action to perform (create, update, remove).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("groupingBehavior", "code", "Defines the grouping behavior for the action and its children.", 0, java.lang.Integer.MAX_VALUE, groupingBehavior));
          childrenList.add(new Property("selectionBehavior", "code", "Defines the selection behavior for the action and its children.", 0, java.lang.Integer.MAX_VALUE, selectionBehavior));
          childrenList.add(new Property("requiredBehavior", "code", "Defines the requiredness behavior for the action.", 0, java.lang.Integer.MAX_VALUE, requiredBehavior));
          childrenList.add(new Property("precheckBehavior", "code", "Defines whether the action should usually be preselected.", 0, java.lang.Integer.MAX_VALUE, precheckBehavior));
          childrenList.add(new Property("cardinalityBehavior", "code", "Defines whether the action can be selected multiple times.", 0, java.lang.Integer.MAX_VALUE, cardinalityBehavior));
          childrenList.add(new Property("resource", "Reference(Any)", "The resource that is the target of the action (e.g. CommunicationRequest).", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("action", "@RequestGroup.action", "Sub actions.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -889046145: /*actionIdentifier*/ return this.actionIdentifier == null ? new Base[0] : new Base[] {this.actionIdentifier}; // Identifier
        case 102727412: /*label*/ return this.label == null ? new Base[0] : new Base[] {this.label}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -900391049: /*textEquivalent*/ return this.textEquivalent == null ? new Base[0] : new Base[] {this.textEquivalent}; // StringType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // CodeableConcept
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : this.documentation.toArray(new Base[this.documentation.size()]); // RelatedArtifact
        case -384107967: /*relatedAction*/ return this.relatedAction == null ? new Base[0] : new Base[] {this.relatedAction}; // RequestGroupActionRelatedActionComponent
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Type
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 586678389: /*groupingBehavior*/ return this.groupingBehavior == null ? new Base[0] : new Base[] {this.groupingBehavior}; // Enumeration<ActionGroupingBehavior>
        case 168639486: /*selectionBehavior*/ return this.selectionBehavior == null ? new Base[0] : new Base[] {this.selectionBehavior}; // Enumeration<ActionSelectionBehavior>
        case -1163906287: /*requiredBehavior*/ return this.requiredBehavior == null ? new Base[0] : new Base[] {this.requiredBehavior}; // Enumeration<ActionRequiredBehavior>
        case -1174249033: /*precheckBehavior*/ return this.precheckBehavior == null ? new Base[0] : new Base[] {this.precheckBehavior}; // Enumeration<ActionPrecheckBehavior>
        case -922577408: /*cardinalityBehavior*/ return this.cardinalityBehavior == null ? new Base[0] : new Base[] {this.cardinalityBehavior}; // Enumeration<ActionCardinalityBehavior>
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // Reference
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // RequestGroupActionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -889046145: // actionIdentifier
          this.actionIdentifier = castToIdentifier(value); // Identifier
          break;
        case 102727412: // label
          this.label = castToString(value); // StringType
          break;
        case 110371416: // title
          this.title = castToString(value); // StringType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -900391049: // textEquivalent
          this.textEquivalent = castToString(value); // StringType
          break;
        case 3059181: // code
          this.getCode().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1587405498: // documentation
          this.getDocumentation().add(castToRelatedArtifact(value)); // RelatedArtifact
          break;
        case -384107967: // relatedAction
          this.relatedAction = (RequestGroupActionRelatedActionComponent) value; // RequestGroupActionRelatedActionComponent
          break;
        case -873664438: // timing
          this.timing = castToType(value); // Type
          break;
        case 767422259: // participant
          this.getParticipant().add(castToReference(value)); // Reference
          break;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 586678389: // groupingBehavior
          this.groupingBehavior = new ActionGroupingBehaviorEnumFactory().fromType(value); // Enumeration<ActionGroupingBehavior>
          break;
        case 168639486: // selectionBehavior
          this.selectionBehavior = new ActionSelectionBehaviorEnumFactory().fromType(value); // Enumeration<ActionSelectionBehavior>
          break;
        case -1163906287: // requiredBehavior
          this.requiredBehavior = new ActionRequiredBehaviorEnumFactory().fromType(value); // Enumeration<ActionRequiredBehavior>
          break;
        case -1174249033: // precheckBehavior
          this.precheckBehavior = new ActionPrecheckBehaviorEnumFactory().fromType(value); // Enumeration<ActionPrecheckBehavior>
          break;
        case -922577408: // cardinalityBehavior
          this.cardinalityBehavior = new ActionCardinalityBehaviorEnumFactory().fromType(value); // Enumeration<ActionCardinalityBehavior>
          break;
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          break;
        case -1422950858: // action
          this.getAction().add((RequestGroupActionComponent) value); // RequestGroupActionComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actionIdentifier"))
          this.actionIdentifier = castToIdentifier(value); // Identifier
        else if (name.equals("label"))
          this.label = castToString(value); // StringType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("textEquivalent"))
          this.textEquivalent = castToString(value); // StringType
        else if (name.equals("code"))
          this.getCode().add(castToCodeableConcept(value));
        else if (name.equals("documentation"))
          this.getDocumentation().add(castToRelatedArtifact(value));
        else if (name.equals("relatedAction"))
          this.relatedAction = (RequestGroupActionRelatedActionComponent) value; // RequestGroupActionRelatedActionComponent
        else if (name.equals("timing[x]"))
          this.timing = castToType(value); // Type
        else if (name.equals("participant"))
          this.getParticipant().add(castToReference(value));
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("groupingBehavior"))
          this.groupingBehavior = new ActionGroupingBehaviorEnumFactory().fromType(value); // Enumeration<ActionGroupingBehavior>
        else if (name.equals("selectionBehavior"))
          this.selectionBehavior = new ActionSelectionBehaviorEnumFactory().fromType(value); // Enumeration<ActionSelectionBehavior>
        else if (name.equals("requiredBehavior"))
          this.requiredBehavior = new ActionRequiredBehaviorEnumFactory().fromType(value); // Enumeration<ActionRequiredBehavior>
        else if (name.equals("precheckBehavior"))
          this.precheckBehavior = new ActionPrecheckBehaviorEnumFactory().fromType(value); // Enumeration<ActionPrecheckBehavior>
        else if (name.equals("cardinalityBehavior"))
          this.cardinalityBehavior = new ActionCardinalityBehaviorEnumFactory().fromType(value); // Enumeration<ActionCardinalityBehavior>
        else if (name.equals("resource"))
          this.resource = castToReference(value); // Reference
        else if (name.equals("action"))
          this.getAction().add((RequestGroupActionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -889046145:  return getActionIdentifier(); // Identifier
        case 102727412: throw new FHIRException("Cannot make property label as it is not a complex type"); // StringType
        case 110371416: throw new FHIRException("Cannot make property title as it is not a complex type"); // StringType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -900391049: throw new FHIRException("Cannot make property textEquivalent as it is not a complex type"); // StringType
        case 3059181:  return addCode(); // CodeableConcept
        case 1587405498:  return addDocumentation(); // RelatedArtifact
        case -384107967:  return getRelatedAction(); // RequestGroupActionRelatedActionComponent
        case 164632566:  return getTiming(); // Type
        case 767422259:  return addParticipant(); // Reference
        case 3575610:  return getType(); // Coding
        case 586678389: throw new FHIRException("Cannot make property groupingBehavior as it is not a complex type"); // Enumeration<ActionGroupingBehavior>
        case 168639486: throw new FHIRException("Cannot make property selectionBehavior as it is not a complex type"); // Enumeration<ActionSelectionBehavior>
        case -1163906287: throw new FHIRException("Cannot make property requiredBehavior as it is not a complex type"); // Enumeration<ActionRequiredBehavior>
        case -1174249033: throw new FHIRException("Cannot make property precheckBehavior as it is not a complex type"); // Enumeration<ActionPrecheckBehavior>
        case -922577408: throw new FHIRException("Cannot make property cardinalityBehavior as it is not a complex type"); // Enumeration<ActionCardinalityBehavior>
        case -341064690:  return getResource(); // Reference
        case -1422950858:  return addAction(); // RequestGroupActionComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actionIdentifier")) {
          this.actionIdentifier = new Identifier();
          return this.actionIdentifier;
        }
        else if (name.equals("label")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.label");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.title");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.description");
        }
        else if (name.equals("textEquivalent")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.textEquivalent");
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("documentation")) {
          return addDocumentation();
        }
        else if (name.equals("relatedAction")) {
          this.relatedAction = new RequestGroupActionRelatedActionComponent();
          return this.relatedAction;
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
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("groupingBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.groupingBehavior");
        }
        else if (name.equals("selectionBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.selectionBehavior");
        }
        else if (name.equals("requiredBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.requiredBehavior");
        }
        else if (name.equals("precheckBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.precheckBehavior");
        }
        else if (name.equals("cardinalityBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.cardinalityBehavior");
        }
        else if (name.equals("resource")) {
          this.resource = new Reference();
          return this.resource;
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

      public RequestGroupActionComponent copy() {
        RequestGroupActionComponent dst = new RequestGroupActionComponent();
        copyValues(dst);
        dst.actionIdentifier = actionIdentifier == null ? null : actionIdentifier.copy();
        dst.label = label == null ? null : label.copy();
        dst.title = title == null ? null : title.copy();
        dst.description = description == null ? null : description.copy();
        dst.textEquivalent = textEquivalent == null ? null : textEquivalent.copy();
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        if (documentation != null) {
          dst.documentation = new ArrayList<RelatedArtifact>();
          for (RelatedArtifact i : documentation)
            dst.documentation.add(i.copy());
        };
        dst.relatedAction = relatedAction == null ? null : relatedAction.copy();
        dst.timing = timing == null ? null : timing.copy();
        if (participant != null) {
          dst.participant = new ArrayList<Reference>();
          for (Reference i : participant)
            dst.participant.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.groupingBehavior = groupingBehavior == null ? null : groupingBehavior.copy();
        dst.selectionBehavior = selectionBehavior == null ? null : selectionBehavior.copy();
        dst.requiredBehavior = requiredBehavior == null ? null : requiredBehavior.copy();
        dst.precheckBehavior = precheckBehavior == null ? null : precheckBehavior.copy();
        dst.cardinalityBehavior = cardinalityBehavior == null ? null : cardinalityBehavior.copy();
        dst.resource = resource == null ? null : resource.copy();
        if (action != null) {
          dst.action = new ArrayList<RequestGroupActionComponent>();
          for (RequestGroupActionComponent i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RequestGroupActionComponent))
          return false;
        RequestGroupActionComponent o = (RequestGroupActionComponent) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(label, o.label, true)
           && compareDeep(title, o.title, true) && compareDeep(description, o.description, true) && compareDeep(textEquivalent, o.textEquivalent, true)
           && compareDeep(code, o.code, true) && compareDeep(documentation, o.documentation, true) && compareDeep(relatedAction, o.relatedAction, true)
           && compareDeep(timing, o.timing, true) && compareDeep(participant, o.participant, true) && compareDeep(type, o.type, true)
           && compareDeep(groupingBehavior, o.groupingBehavior, true) && compareDeep(selectionBehavior, o.selectionBehavior, true)
           && compareDeep(requiredBehavior, o.requiredBehavior, true) && compareDeep(precheckBehavior, o.precheckBehavior, true)
           && compareDeep(cardinalityBehavior, o.cardinalityBehavior, true) && compareDeep(resource, o.resource, true)
           && compareDeep(action, o.action, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RequestGroupActionComponent))
          return false;
        RequestGroupActionComponent o = (RequestGroupActionComponent) other;
        return compareValues(label, o.label, true) && compareValues(title, o.title, true) && compareValues(description, o.description, true)
           && compareValues(textEquivalent, o.textEquivalent, true) && compareValues(groupingBehavior, o.groupingBehavior, true)
           && compareValues(selectionBehavior, o.selectionBehavior, true) && compareValues(requiredBehavior, o.requiredBehavior, true)
           && compareValues(precheckBehavior, o.precheckBehavior, true) && compareValues(cardinalityBehavior, o.cardinalityBehavior, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actionIdentifier, label, title
          , description, textEquivalent, code, documentation, relatedAction, timing, participant
          , type, groupingBehavior, selectionBehavior, requiredBehavior, precheckBehavior, cardinalityBehavior
          , resource, action);
      }

  public String fhirType() {
    return "RequestGroup.action";

  }

  }

    @Block()
    public static class RequestGroupActionRelatedActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The unique identifier of the related action.
         */
        @Child(name = "actionIdentifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifier of the related action", formalDefinition="The unique identifier of the related action." )
        protected Identifier actionIdentifier;

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

        private static final long serialVersionUID = 1011239532L;

    /**
     * Constructor
     */
      public RequestGroupActionRelatedActionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public RequestGroupActionRelatedActionComponent(Identifier actionIdentifier, Enumeration<ActionRelationshipType> relationship) {
        super();
        this.actionIdentifier = actionIdentifier;
        this.relationship = relationship;
      }

        /**
         * @return {@link #actionIdentifier} (The unique identifier of the related action.)
         */
        public Identifier getActionIdentifier() { 
          if (this.actionIdentifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RequestGroupActionRelatedActionComponent.actionIdentifier");
            else if (Configuration.doAutoCreate())
              this.actionIdentifier = new Identifier(); // cc
          return this.actionIdentifier;
        }

        public boolean hasActionIdentifier() { 
          return this.actionIdentifier != null && !this.actionIdentifier.isEmpty();
        }

        /**
         * @param value {@link #actionIdentifier} (The unique identifier of the related action.)
         */
        public RequestGroupActionRelatedActionComponent setActionIdentifier(Identifier value) { 
          this.actionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #relationship} (The relationship of this action to the related action.). This is the underlying object with id, value and extensions. The accessor "getRelationship" gives direct access to the value
         */
        public Enumeration<ActionRelationshipType> getRelationshipElement() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RequestGroupActionRelatedActionComponent.relationship");
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
        public RequestGroupActionRelatedActionComponent setRelationshipElement(Enumeration<ActionRelationshipType> value) { 
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
        public RequestGroupActionRelatedActionComponent setRelationship(ActionRelationshipType value) { 
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
        public RequestGroupActionRelatedActionComponent setOffset(Type value) { 
          this.offset = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("actionIdentifier", "Identifier", "The unique identifier of the related action.", 0, java.lang.Integer.MAX_VALUE, actionIdentifier));
          childrenList.add(new Property("relationship", "code", "The relationship of this action to the related action.", 0, java.lang.Integer.MAX_VALUE, relationship));
          childrenList.add(new Property("offset[x]", "Duration|Range", "A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.", 0, java.lang.Integer.MAX_VALUE, offset));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -889046145: /*actionIdentifier*/ return this.actionIdentifier == null ? new Base[0] : new Base[] {this.actionIdentifier}; // Identifier
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // Enumeration<ActionRelationshipType>
        case -1019779949: /*offset*/ return this.offset == null ? new Base[0] : new Base[] {this.offset}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -889046145: // actionIdentifier
          this.actionIdentifier = castToIdentifier(value); // Identifier
          break;
        case -261851592: // relationship
          this.relationship = new ActionRelationshipTypeEnumFactory().fromType(value); // Enumeration<ActionRelationshipType>
          break;
        case -1019779949: // offset
          this.offset = castToType(value); // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actionIdentifier"))
          this.actionIdentifier = castToIdentifier(value); // Identifier
        else if (name.equals("relationship"))
          this.relationship = new ActionRelationshipTypeEnumFactory().fromType(value); // Enumeration<ActionRelationshipType>
        else if (name.equals("offset[x]"))
          this.offset = castToType(value); // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -889046145:  return getActionIdentifier(); // Identifier
        case -261851592: throw new FHIRException("Cannot make property relationship as it is not a complex type"); // Enumeration<ActionRelationshipType>
        case -1960684787:  return getOffset(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actionIdentifier")) {
          this.actionIdentifier = new Identifier();
          return this.actionIdentifier;
        }
        else if (name.equals("relationship")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.relationship");
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

      public RequestGroupActionRelatedActionComponent copy() {
        RequestGroupActionRelatedActionComponent dst = new RequestGroupActionRelatedActionComponent();
        copyValues(dst);
        dst.actionIdentifier = actionIdentifier == null ? null : actionIdentifier.copy();
        dst.relationship = relationship == null ? null : relationship.copy();
        dst.offset = offset == null ? null : offset.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RequestGroupActionRelatedActionComponent))
          return false;
        RequestGroupActionRelatedActionComponent o = (RequestGroupActionRelatedActionComponent) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(relationship, o.relationship, true)
           && compareDeep(offset, o.offset, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RequestGroupActionRelatedActionComponent))
          return false;
        RequestGroupActionRelatedActionComponent o = (RequestGroupActionRelatedActionComponent) other;
        return compareValues(relationship, o.relationship, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actionIdentifier, relationship
          , offset);
      }

  public String fhirType() {
    return "RequestGroup.action.relatedAction";

  }

  }

    /**
     * Allows a service to provide a unique, business identifier for the response.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="Allows a service to provide a unique, business identifier for the response." )
    protected Identifier identifier;

    /**
     * The subject for which the request group was created.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Subject of the request group", formalDefinition="The subject for which the request group was created." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The subject for which the request group was created.)
     */
    protected Resource subjectTarget;

    /**
     * Describes the context of the request group, if any.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Encounter or Episode for the request group", formalDefinition="Describes the context of the request group, if any." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (Describes the context of the request group, if any.)
     */
    protected Resource contextTarget;

    /**
     * Indicates when the request group was created.
     */
    @Child(name = "occurrenceDateTime", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the request group was authored", formalDefinition="Indicates when the request group was created." )
    protected DateTimeType occurrenceDateTime;

    /**
     * Provides a reference to the author of the request group.
     */
    @Child(name = "author", type = {Device.class, Practitioner.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Device or practitioner that authored the request group", formalDefinition="Provides a reference to the author of the request group." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Provides a reference to the author of the request group.)
     */
    protected Resource authorTarget;

    /**
     * Indicates the reason the request group was created. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.
     */
    @Child(name = "reason", type = {CodeableConcept.class, Reference.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reason for the request group", formalDefinition="Indicates the reason the request group was created. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response." )
    protected Type reason;

    /**
     * Provides a mechanism to communicate additional information about the response.
     */
    @Child(name = "note", type = {Annotation.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional notes about the response", formalDefinition="Provides a mechanism to communicate additional information about the response." )
    protected List<Annotation> note;

    /**
     * The actions, if any, produced by the evaluation of the artifact.
     */
    @Child(name = "action", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Proposed actions, if any", formalDefinition="The actions, if any, produced by the evaluation of the artifact." )
    protected List<RequestGroupActionComponent> action;

    private static final long serialVersionUID = 334451815L;

  /**
   * Constructor
   */
    public RequestGroup() {
      super();
    }

    /**
     * @return {@link #identifier} (Allows a service to provide a unique, business identifier for the response.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RequestGroup.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Allows a service to provide a unique, business identifier for the response.)
     */
    public RequestGroup setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #subject} (The subject for which the request group was created.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RequestGroup.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The subject for which the request group was created.)
     */
    public RequestGroup setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The subject for which the request group was created.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The subject for which the request group was created.)
     */
    public RequestGroup setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (Describes the context of the request group, if any.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RequestGroup.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (Describes the context of the request group, if any.)
     */
    public RequestGroup setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the context of the request group, if any.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the context of the request group, if any.)
     */
    public RequestGroup setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #occurrenceDateTime} (Indicates when the request group was created.). This is the underlying object with id, value and extensions. The accessor "getOccurrenceDateTime" gives direct access to the value
     */
    public DateTimeType getOccurrenceDateTimeElement() { 
      if (this.occurrenceDateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RequestGroup.occurrenceDateTime");
        else if (Configuration.doAutoCreate())
          this.occurrenceDateTime = new DateTimeType(); // bb
      return this.occurrenceDateTime;
    }

    public boolean hasOccurrenceDateTimeElement() { 
      return this.occurrenceDateTime != null && !this.occurrenceDateTime.isEmpty();
    }

    public boolean hasOccurrenceDateTime() { 
      return this.occurrenceDateTime != null && !this.occurrenceDateTime.isEmpty();
    }

    /**
     * @param value {@link #occurrenceDateTime} (Indicates when the request group was created.). This is the underlying object with id, value and extensions. The accessor "getOccurrenceDateTime" gives direct access to the value
     */
    public RequestGroup setOccurrenceDateTimeElement(DateTimeType value) { 
      this.occurrenceDateTime = value;
      return this;
    }

    /**
     * @return Indicates when the request group was created.
     */
    public Date getOccurrenceDateTime() { 
      return this.occurrenceDateTime == null ? null : this.occurrenceDateTime.getValue();
    }

    /**
     * @param value Indicates when the request group was created.
     */
    public RequestGroup setOccurrenceDateTime(Date value) { 
      if (value == null)
        this.occurrenceDateTime = null;
      else {
        if (this.occurrenceDateTime == null)
          this.occurrenceDateTime = new DateTimeType();
        this.occurrenceDateTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #author} (Provides a reference to the author of the request group.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RequestGroup.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Provides a reference to the author of the request group.)
     */
    public RequestGroup setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Provides a reference to the author of the request group.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Provides a reference to the author of the request group.)
     */
    public RequestGroup setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #reason} (Indicates the reason the request group was created. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.)
     */
    public Type getReason() { 
      return this.reason;
    }

    /**
     * @return {@link #reason} (Indicates the reason the request group was created. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.)
     */
    public CodeableConcept getReasonCodeableConcept() throws FHIRException { 
      if (!(this.reason instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (CodeableConcept) this.reason;
    }

    public boolean hasReasonCodeableConcept() { 
      return this.reason instanceof CodeableConcept;
    }

    /**
     * @return {@link #reason} (Indicates the reason the request group was created. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.)
     */
    public Reference getReasonReference() throws FHIRException { 
      if (!(this.reason instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (Reference) this.reason;
    }

    public boolean hasReasonReference() { 
      return this.reason instanceof Reference;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (Indicates the reason the request group was created. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.)
     */
    public RequestGroup setReason(Type value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #note} (Provides a mechanism to communicate additional information about the response.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public RequestGroup setNote(List<Annotation> theNote) { 
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

    public RequestGroup addNote(Annotation t) { //3
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
     * @return {@link #action} (The actions, if any, produced by the evaluation of the artifact.)
     */
    public List<RequestGroupActionComponent> getAction() { 
      if (this.action == null)
        this.action = new ArrayList<RequestGroupActionComponent>();
      return this.action;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public RequestGroup setAction(List<RequestGroupActionComponent> theAction) { 
      this.action = theAction;
      return this;
    }

    public boolean hasAction() { 
      if (this.action == null)
        return false;
      for (RequestGroupActionComponent item : this.action)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public RequestGroupActionComponent addAction() { //3
      RequestGroupActionComponent t = new RequestGroupActionComponent();
      if (this.action == null)
        this.action = new ArrayList<RequestGroupActionComponent>();
      this.action.add(t);
      return t;
    }

    public RequestGroup addAction(RequestGroupActionComponent t) { //3
      if (t == null)
        return this;
      if (this.action == null)
        this.action = new ArrayList<RequestGroupActionComponent>();
      this.action.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
     */
    public RequestGroupActionComponent getActionFirstRep() { 
      if (getAction().isEmpty()) {
        addAction();
      }
      return getAction().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Allows a service to provide a unique, business identifier for the response.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The subject for which the request group was created.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "Describes the context of the request group, if any.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("occurrenceDateTime", "dateTime", "Indicates when the request group was created.", 0, java.lang.Integer.MAX_VALUE, occurrenceDateTime));
        childrenList.add(new Property("author", "Reference(Device|Practitioner)", "Provides a reference to the author of the request group.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("reason[x]", "CodeableConcept|Reference(Any)", "Indicates the reason the request group was created. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("note", "Annotation", "Provides a mechanism to communicate additional information about the response.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("action", "", "The actions, if any, produced by the evaluation of the artifact.", 0, java.lang.Integer.MAX_VALUE, action));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -298443636: /*occurrenceDateTime*/ return this.occurrenceDateTime == null ? new Base[0] : new Base[] {this.occurrenceDateTime}; // DateTimeType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // Type
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // RequestGroupActionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          break;
        case -298443636: // occurrenceDateTime
          this.occurrenceDateTime = castToDateTime(value); // DateTimeType
          break;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          break;
        case -934964668: // reason
          this.reason = castToType(value); // Type
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case -1422950858: // action
          this.getAction().add((RequestGroupActionComponent) value); // RequestGroupActionComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("context"))
          this.context = castToReference(value); // Reference
        else if (name.equals("occurrenceDateTime"))
          this.occurrenceDateTime = castToDateTime(value); // DateTimeType
        else if (name.equals("author"))
          this.author = castToReference(value); // Reference
        else if (name.equals("reason[x]"))
          this.reason = castToType(value); // Type
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("action"))
          this.getAction().add((RequestGroupActionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case -1867885268:  return getSubject(); // Reference
        case 951530927:  return getContext(); // Reference
        case -298443636: throw new FHIRException("Cannot make property occurrenceDateTime as it is not a complex type"); // DateTimeType
        case -1406328437:  return getAuthor(); // Reference
        case -669418564:  return getReason(); // Type
        case 3387378:  return addNote(); // Annotation
        case -1422950858:  return addAction(); // RequestGroupActionComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("occurrenceDateTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type RequestGroup.occurrenceDateTime");
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("reasonCodeableConcept")) {
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else if (name.equals("reasonReference")) {
          this.reason = new Reference();
          return this.reason;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "RequestGroup";

  }

      public RequestGroup copy() {
        RequestGroup dst = new RequestGroup();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.occurrenceDateTime = occurrenceDateTime == null ? null : occurrenceDateTime.copy();
        dst.author = author == null ? null : author.copy();
        dst.reason = reason == null ? null : reason.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<RequestGroupActionComponent>();
          for (RequestGroupActionComponent i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      protected RequestGroup typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RequestGroup))
          return false;
        RequestGroup o = (RequestGroup) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true)
           && compareDeep(occurrenceDateTime, o.occurrenceDateTime, true) && compareDeep(author, o.author, true)
           && compareDeep(reason, o.reason, true) && compareDeep(note, o.note, true) && compareDeep(action, o.action, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RequestGroup))
          return false;
        RequestGroup o = (RequestGroup) other;
        return compareValues(occurrenceDateTime, o.occurrenceDateTime, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, subject, context
          , occurrenceDateTime, author, reason, note, action);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.RequestGroup;
   }

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for request groups</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="RequestGroup.subject", description="The identity of a patient to search for request groups", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for request groups</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RequestGroup:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("RequestGroup:patient").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The subject that the request group is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="RequestGroup.subject", description="The subject that the request group is about", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The subject that the request group is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RequestGroup:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("RequestGroup:subject").toLocked();

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>The author of the request group</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="RequestGroup.author", description="The author of the request group", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Practitioner.class } )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>The author of the request group</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RequestGroup:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("RequestGroup:author").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>The context the request group applies to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="RequestGroup.context", description="The context the request group applies to", type="reference", target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>The context the request group applies to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RequestGroup:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("RequestGroup:context").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>The encounter the request group applies to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="RequestGroup.context", description="The encounter the request group applies to", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>The encounter the request group applies to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RequestGroup:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("RequestGroup:encounter").toLocked();

 /**
   * Search parameter: <b>participant</b>
   * <p>
   * Description: <b>The participant in the requests in the group</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.action.participant</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant", path="RequestGroup.action.participant", description="The participant in the requests in the group", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Patient.class, Person.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PARTICIPANT = "participant";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participant</b>
   * <p>
   * Description: <b>The participant in the requests in the group</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>RequestGroup.action.participant</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTICIPANT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTICIPANT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>RequestGroup:participant</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTICIPANT = new ca.uhn.fhir.model.api.Include("RequestGroup:participant").toLocked();


}

