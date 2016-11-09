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
@ChildOrder(names={"url", "identifier", "version", "name", "title", "type", "status", "experimental", "date", "description", "purpose", "usage", "approvalDate", "lastReviewDate", "effectivePeriod", "useContext", "jurisdiction", "topic", "contributor", "publisher", "contact", "copyright", "relatedArtifact", "library", "actionDefinition"})
public class PlanDefinition extends MetadataResource {

    public enum PlanActionRelationshipType {
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
        public static PlanActionRelationshipType fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown PlanActionRelationshipType code '"+codeString+"'");
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

  public static class PlanActionRelationshipTypeEnumFactory implements EnumFactory<PlanActionRelationshipType> {
    public PlanActionRelationshipType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("before-start".equals(codeString))
          return PlanActionRelationshipType.BEFORESTART;
        if ("before".equals(codeString))
          return PlanActionRelationshipType.BEFORE;
        if ("before-end".equals(codeString))
          return PlanActionRelationshipType.BEFOREEND;
        if ("concurrent-with-start".equals(codeString))
          return PlanActionRelationshipType.CONCURRENTWITHSTART;
        if ("concurrent".equals(codeString))
          return PlanActionRelationshipType.CONCURRENT;
        if ("concurrent-with-end".equals(codeString))
          return PlanActionRelationshipType.CONCURRENTWITHEND;
        if ("after-start".equals(codeString))
          return PlanActionRelationshipType.AFTERSTART;
        if ("after".equals(codeString))
          return PlanActionRelationshipType.AFTER;
        if ("after-end".equals(codeString))
          return PlanActionRelationshipType.AFTEREND;
        throw new IllegalArgumentException("Unknown PlanActionRelationshipType code '"+codeString+"'");
        }
        public Enumeration<PlanActionRelationshipType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("before-start".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.BEFORESTART);
        if ("before".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.BEFORE);
        if ("before-end".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.BEFOREEND);
        if ("concurrent-with-start".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.CONCURRENTWITHSTART);
        if ("concurrent".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.CONCURRENT);
        if ("concurrent-with-end".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.CONCURRENTWITHEND);
        if ("after-start".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.AFTERSTART);
        if ("after".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.AFTER);
        if ("after-end".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.AFTEREND);
        throw new FHIRException("Unknown PlanActionRelationshipType code '"+codeString+"'");
        }
    public String toCode(PlanActionRelationshipType code) {
      if (code == PlanActionRelationshipType.BEFORESTART)
        return "before-start";
      if (code == PlanActionRelationshipType.BEFORE)
        return "before";
      if (code == PlanActionRelationshipType.BEFOREEND)
        return "before-end";
      if (code == PlanActionRelationshipType.CONCURRENTWITHSTART)
        return "concurrent-with-start";
      if (code == PlanActionRelationshipType.CONCURRENT)
        return "concurrent";
      if (code == PlanActionRelationshipType.CONCURRENTWITHEND)
        return "concurrent-with-end";
      if (code == PlanActionRelationshipType.AFTERSTART)
        return "after-start";
      if (code == PlanActionRelationshipType.AFTER)
        return "after";
      if (code == PlanActionRelationshipType.AFTEREND)
        return "after-end";
      return "?";
      }
    public String toSystem(PlanActionRelationshipType code) {
      return code.getSystem();
      }
    }

    public enum PlanActionParticipantType {
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
        public static PlanActionParticipantType fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown PlanActionParticipantType code '"+codeString+"'");
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

  public static class PlanActionParticipantTypeEnumFactory implements EnumFactory<PlanActionParticipantType> {
    public PlanActionParticipantType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return PlanActionParticipantType.PATIENT;
        if ("practitioner".equals(codeString))
          return PlanActionParticipantType.PRACTITIONER;
        if ("related-person".equals(codeString))
          return PlanActionParticipantType.RELATEDPERSON;
        throw new IllegalArgumentException("Unknown PlanActionParticipantType code '"+codeString+"'");
        }
        public Enumeration<PlanActionParticipantType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("patient".equals(codeString))
          return new Enumeration<PlanActionParticipantType>(this, PlanActionParticipantType.PATIENT);
        if ("practitioner".equals(codeString))
          return new Enumeration<PlanActionParticipantType>(this, PlanActionParticipantType.PRACTITIONER);
        if ("related-person".equals(codeString))
          return new Enumeration<PlanActionParticipantType>(this, PlanActionParticipantType.RELATEDPERSON);
        throw new FHIRException("Unknown PlanActionParticipantType code '"+codeString+"'");
        }
    public String toCode(PlanActionParticipantType code) {
      if (code == PlanActionParticipantType.PATIENT)
        return "patient";
      if (code == PlanActionParticipantType.PRACTITIONER)
        return "practitioner";
      if (code == PlanActionParticipantType.RELATEDPERSON)
        return "related-person";
      return "?";
      }
    public String toSystem(PlanActionParticipantType code) {
      return code.getSystem();
      }
    }

    public enum PlanActionGroupingBehavior {
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
        public static PlanActionGroupingBehavior fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown PlanActionGroupingBehavior code '"+codeString+"'");
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

  public static class PlanActionGroupingBehaviorEnumFactory implements EnumFactory<PlanActionGroupingBehavior> {
    public PlanActionGroupingBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("visual-group".equals(codeString))
          return PlanActionGroupingBehavior.VISUALGROUP;
        if ("logical-group".equals(codeString))
          return PlanActionGroupingBehavior.LOGICALGROUP;
        if ("sentence-group".equals(codeString))
          return PlanActionGroupingBehavior.SENTENCEGROUP;
        throw new IllegalArgumentException("Unknown PlanActionGroupingBehavior code '"+codeString+"'");
        }
        public Enumeration<PlanActionGroupingBehavior> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("visual-group".equals(codeString))
          return new Enumeration<PlanActionGroupingBehavior>(this, PlanActionGroupingBehavior.VISUALGROUP);
        if ("logical-group".equals(codeString))
          return new Enumeration<PlanActionGroupingBehavior>(this, PlanActionGroupingBehavior.LOGICALGROUP);
        if ("sentence-group".equals(codeString))
          return new Enumeration<PlanActionGroupingBehavior>(this, PlanActionGroupingBehavior.SENTENCEGROUP);
        throw new FHIRException("Unknown PlanActionGroupingBehavior code '"+codeString+"'");
        }
    public String toCode(PlanActionGroupingBehavior code) {
      if (code == PlanActionGroupingBehavior.VISUALGROUP)
        return "visual-group";
      if (code == PlanActionGroupingBehavior.LOGICALGROUP)
        return "logical-group";
      if (code == PlanActionGroupingBehavior.SENTENCEGROUP)
        return "sentence-group";
      return "?";
      }
    public String toSystem(PlanActionGroupingBehavior code) {
      return code.getSystem();
      }
    }

    public enum PlanActionSelectionBehavior {
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
        public static PlanActionSelectionBehavior fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown PlanActionSelectionBehavior code '"+codeString+"'");
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

  public static class PlanActionSelectionBehaviorEnumFactory implements EnumFactory<PlanActionSelectionBehavior> {
    public PlanActionSelectionBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("any".equals(codeString))
          return PlanActionSelectionBehavior.ANY;
        if ("all".equals(codeString))
          return PlanActionSelectionBehavior.ALL;
        if ("all-or-none".equals(codeString))
          return PlanActionSelectionBehavior.ALLORNONE;
        if ("exactly-one".equals(codeString))
          return PlanActionSelectionBehavior.EXACTLYONE;
        if ("at-most-one".equals(codeString))
          return PlanActionSelectionBehavior.ATMOSTONE;
        if ("one-or-more".equals(codeString))
          return PlanActionSelectionBehavior.ONEORMORE;
        throw new IllegalArgumentException("Unknown PlanActionSelectionBehavior code '"+codeString+"'");
        }
        public Enumeration<PlanActionSelectionBehavior> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("any".equals(codeString))
          return new Enumeration<PlanActionSelectionBehavior>(this, PlanActionSelectionBehavior.ANY);
        if ("all".equals(codeString))
          return new Enumeration<PlanActionSelectionBehavior>(this, PlanActionSelectionBehavior.ALL);
        if ("all-or-none".equals(codeString))
          return new Enumeration<PlanActionSelectionBehavior>(this, PlanActionSelectionBehavior.ALLORNONE);
        if ("exactly-one".equals(codeString))
          return new Enumeration<PlanActionSelectionBehavior>(this, PlanActionSelectionBehavior.EXACTLYONE);
        if ("at-most-one".equals(codeString))
          return new Enumeration<PlanActionSelectionBehavior>(this, PlanActionSelectionBehavior.ATMOSTONE);
        if ("one-or-more".equals(codeString))
          return new Enumeration<PlanActionSelectionBehavior>(this, PlanActionSelectionBehavior.ONEORMORE);
        throw new FHIRException("Unknown PlanActionSelectionBehavior code '"+codeString+"'");
        }
    public String toCode(PlanActionSelectionBehavior code) {
      if (code == PlanActionSelectionBehavior.ANY)
        return "any";
      if (code == PlanActionSelectionBehavior.ALL)
        return "all";
      if (code == PlanActionSelectionBehavior.ALLORNONE)
        return "all-or-none";
      if (code == PlanActionSelectionBehavior.EXACTLYONE)
        return "exactly-one";
      if (code == PlanActionSelectionBehavior.ATMOSTONE)
        return "at-most-one";
      if (code == PlanActionSelectionBehavior.ONEORMORE)
        return "one-or-more";
      return "?";
      }
    public String toSystem(PlanActionSelectionBehavior code) {
      return code.getSystem();
      }
    }

    public enum PlanActionRequiredBehavior {
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
        public static PlanActionRequiredBehavior fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown PlanActionRequiredBehavior code '"+codeString+"'");
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

  public static class PlanActionRequiredBehaviorEnumFactory implements EnumFactory<PlanActionRequiredBehavior> {
    public PlanActionRequiredBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("must".equals(codeString))
          return PlanActionRequiredBehavior.MUST;
        if ("could".equals(codeString))
          return PlanActionRequiredBehavior.COULD;
        if ("must-unless-documented".equals(codeString))
          return PlanActionRequiredBehavior.MUSTUNLESSDOCUMENTED;
        throw new IllegalArgumentException("Unknown PlanActionRequiredBehavior code '"+codeString+"'");
        }
        public Enumeration<PlanActionRequiredBehavior> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("must".equals(codeString))
          return new Enumeration<PlanActionRequiredBehavior>(this, PlanActionRequiredBehavior.MUST);
        if ("could".equals(codeString))
          return new Enumeration<PlanActionRequiredBehavior>(this, PlanActionRequiredBehavior.COULD);
        if ("must-unless-documented".equals(codeString))
          return new Enumeration<PlanActionRequiredBehavior>(this, PlanActionRequiredBehavior.MUSTUNLESSDOCUMENTED);
        throw new FHIRException("Unknown PlanActionRequiredBehavior code '"+codeString+"'");
        }
    public String toCode(PlanActionRequiredBehavior code) {
      if (code == PlanActionRequiredBehavior.MUST)
        return "must";
      if (code == PlanActionRequiredBehavior.COULD)
        return "could";
      if (code == PlanActionRequiredBehavior.MUSTUNLESSDOCUMENTED)
        return "must-unless-documented";
      return "?";
      }
    public String toSystem(PlanActionRequiredBehavior code) {
      return code.getSystem();
      }
    }

    public enum PlanActionPrecheckBehavior {
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
        public static PlanActionPrecheckBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("yes".equals(codeString))
          return YES;
        if ("no".equals(codeString))
          return NO;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown PlanActionPrecheckBehavior code '"+codeString+"'");
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

  public static class PlanActionPrecheckBehaviorEnumFactory implements EnumFactory<PlanActionPrecheckBehavior> {
    public PlanActionPrecheckBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("yes".equals(codeString))
          return PlanActionPrecheckBehavior.YES;
        if ("no".equals(codeString))
          return PlanActionPrecheckBehavior.NO;
        throw new IllegalArgumentException("Unknown PlanActionPrecheckBehavior code '"+codeString+"'");
        }
        public Enumeration<PlanActionPrecheckBehavior> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("yes".equals(codeString))
          return new Enumeration<PlanActionPrecheckBehavior>(this, PlanActionPrecheckBehavior.YES);
        if ("no".equals(codeString))
          return new Enumeration<PlanActionPrecheckBehavior>(this, PlanActionPrecheckBehavior.NO);
        throw new FHIRException("Unknown PlanActionPrecheckBehavior code '"+codeString+"'");
        }
    public String toCode(PlanActionPrecheckBehavior code) {
      if (code == PlanActionPrecheckBehavior.YES)
        return "yes";
      if (code == PlanActionPrecheckBehavior.NO)
        return "no";
      return "?";
      }
    public String toSystem(PlanActionPrecheckBehavior code) {
      return code.getSystem();
      }
    }

    public enum PlanActionCardinalityBehavior {
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
        public static PlanActionCardinalityBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("single".equals(codeString))
          return SINGLE;
        if ("multiple".equals(codeString))
          return MULTIPLE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown PlanActionCardinalityBehavior code '"+codeString+"'");
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

  public static class PlanActionCardinalityBehaviorEnumFactory implements EnumFactory<PlanActionCardinalityBehavior> {
    public PlanActionCardinalityBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("single".equals(codeString))
          return PlanActionCardinalityBehavior.SINGLE;
        if ("multiple".equals(codeString))
          return PlanActionCardinalityBehavior.MULTIPLE;
        throw new IllegalArgumentException("Unknown PlanActionCardinalityBehavior code '"+codeString+"'");
        }
        public Enumeration<PlanActionCardinalityBehavior> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("single".equals(codeString))
          return new Enumeration<PlanActionCardinalityBehavior>(this, PlanActionCardinalityBehavior.SINGLE);
        if ("multiple".equals(codeString))
          return new Enumeration<PlanActionCardinalityBehavior>(this, PlanActionCardinalityBehavior.MULTIPLE);
        throw new FHIRException("Unknown PlanActionCardinalityBehavior code '"+codeString+"'");
        }
    public String toCode(PlanActionCardinalityBehavior code) {
      if (code == PlanActionCardinalityBehavior.SINGLE)
        return "single";
      if (code == PlanActionCardinalityBehavior.MULTIPLE)
        return "multiple";
      return "?";
      }
    public String toSystem(PlanActionCardinalityBehavior code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class PlanDefinitionActionDefinitionComponent extends BackboneElement implements IBaseBackboneElement {
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
         * A description of when the action should be triggered.
         */
        @Child(name = "triggerDefinition", type = {TriggerDefinition.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="When the action should be triggered", formalDefinition="A description of when the action should be triggered." )
        protected List<TriggerDefinition> triggerDefinition;

        /**
         * An expression specifying whether or not the action is applicable in a given context.
         */
        @Child(name = "condition", type = {}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether or not the action is applicable", formalDefinition="An expression specifying whether or not the action is applicable in a given context." )
        protected PlanDefinitionActionDefinitionConditionComponent condition;

        /**
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         */
        @Child(name = "relatedAction", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Relationship to another action", formalDefinition="A relationship to another action such as \"before\" or \"30-60 minutes after start of\"." )
        protected List<PlanDefinitionActionDefinitionRelatedActionComponent> relatedAction;

        /**
         * An optional value describing when the action should be performed.
         */
        @Child(name = "timing", type = {DateTimeType.class, Period.class, Duration.class, Range.class, Timing.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When the action should take place", formalDefinition="An optional value describing when the action should be performed." )
        protected Type timing;

        /**
         * The type of participant in the action.
         */
        @Child(name = "participantType", type = {CodeType.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="patient | practitioner | related-person", formalDefinition="The type of participant in the action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-participant-type")
        protected List<Enumeration<PlanActionParticipantType>> participantType;

        /**
         * The type of action to perform (create, update, remove).
         */
        @Child(name = "type", type = {Coding.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="create | update | remove | fire-event", formalDefinition="The type of action to perform (create, update, remove)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-type")
        protected Coding type;

        /**
         * Defines the grouping behavior for the action and its children.
         */
        @Child(name = "groupingBehavior", type = {CodeType.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="visual-group | logical-group | sentence-group", formalDefinition="Defines the grouping behavior for the action and its children." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-grouping-behavior")
        protected Enumeration<PlanActionGroupingBehavior> groupingBehavior;

        /**
         * Defines the selection behavior for the action and its children.
         */
        @Child(name = "selectionBehavior", type = {CodeType.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="any | all | all-or-none | exactly-one | at-most-one | one-or-more", formalDefinition="Defines the selection behavior for the action and its children." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-selection-behavior")
        protected Enumeration<PlanActionSelectionBehavior> selectionBehavior;

        /**
         * Defines the requiredness behavior for the action.
         */
        @Child(name = "requiredBehavior", type = {CodeType.class}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="must | could | must-unless-documented", formalDefinition="Defines the requiredness behavior for the action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-required-behavior")
        protected Enumeration<PlanActionRequiredBehavior> requiredBehavior;

        /**
         * Defines whether the action should usually be preselected.
         */
        @Child(name = "precheckBehavior", type = {CodeType.class}, order=17, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="yes | no", formalDefinition="Defines whether the action should usually be preselected." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-precheck-behavior")
        protected Enumeration<PlanActionPrecheckBehavior> precheckBehavior;

        /**
         * Defines whether the action can be selected multiple times.
         */
        @Child(name = "cardinalityBehavior", type = {CodeType.class}, order=18, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="single | multiple", formalDefinition="Defines whether the action can be selected multiple times." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-cardinality-behavior")
        protected Enumeration<PlanActionCardinalityBehavior> cardinalityBehavior;

        /**
         * A reference to an ActivityDefinition that describes the action to be taken in detail.
         */
        @Child(name = "activityDefinition", type = {ActivityDefinition.class}, order=19, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of the activity to be performed", formalDefinition="A reference to an ActivityDefinition that describes the action to be taken in detail." )
        protected Reference activityDefinition;

        /**
         * The actual object that is the target of the reference (A reference to an ActivityDefinition that describes the action to be taken in detail.)
         */
        protected ActivityDefinition activityDefinitionTarget;

        /**
         * A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.
         */
        @Child(name = "transform", type = {StructureMap.class}, order=20, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Transform to apply the template", formalDefinition="A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input." )
        protected Reference transform;

        /**
         * The actual object that is the target of the reference (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
         */
        protected StructureMap transformTarget;

        /**
         * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.
         */
        @Child(name = "dynamicValue", type = {}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Dynamic aspects of the definition", formalDefinition="Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result." )
        protected List<PlanDefinitionActionDefinitionDynamicValueComponent> dynamicValue;

        /**
         * Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.
         */
        @Child(name = "actionDefinition", type = {PlanDefinitionActionDefinitionComponent.class}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A sub-action", formalDefinition="Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition." )
        protected List<PlanDefinitionActionDefinitionComponent> actionDefinition;

        private static final long serialVersionUID = -1445789672L;

    /**
     * Constructor
     */
      public PlanDefinitionActionDefinitionComponent() {
        super();
      }

        /**
         * @return {@link #actionIdentifier} (A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.)
         */
        public Identifier getActionIdentifier() { 
          if (this.actionIdentifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.actionIdentifier");
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
        public PlanDefinitionActionDefinitionComponent setActionIdentifier(Identifier value) { 
          this.actionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #label} (A user-visible label for the action.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public StringType getLabelElement() { 
          if (this.label == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.label");
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
        public PlanDefinitionActionDefinitionComponent setLabelElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionComponent setLabel(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.title");
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
        public PlanDefinitionActionDefinitionComponent setTitleElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionComponent setTitle(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.description");
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
        public PlanDefinitionActionDefinitionComponent setDescriptionElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionComponent setDescription(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.textEquivalent");
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
        public PlanDefinitionActionDefinitionComponent setTextEquivalentElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionComponent setTextEquivalent(String value) { 
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
        public PlanDefinitionActionDefinitionComponent setCode(List<CodeableConcept> theCode) { 
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

        public PlanDefinitionActionDefinitionComponent addCode(CodeableConcept t) { //3
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
        public PlanDefinitionActionDefinitionComponent setDocumentation(List<RelatedArtifact> theDocumentation) { 
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

        public PlanDefinitionActionDefinitionComponent addDocumentation(RelatedArtifact t) { //3
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
        public PlanDefinitionActionDefinitionComponent setTriggerDefinition(List<TriggerDefinition> theTriggerDefinition) { 
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

        public PlanDefinitionActionDefinitionComponent addTriggerDefinition(TriggerDefinition t) { //3
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
         * @return {@link #condition} (An expression specifying whether or not the action is applicable in a given context.)
         */
        public PlanDefinitionActionDefinitionConditionComponent getCondition() { 
          if (this.condition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.condition");
            else if (Configuration.doAutoCreate())
              this.condition = new PlanDefinitionActionDefinitionConditionComponent(); // cc
          return this.condition;
        }

        public boolean hasCondition() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        /**
         * @param value {@link #condition} (An expression specifying whether or not the action is applicable in a given context.)
         */
        public PlanDefinitionActionDefinitionComponent setCondition(PlanDefinitionActionDefinitionConditionComponent value) { 
          this.condition = value;
          return this;
        }

        /**
         * @return {@link #relatedAction} (A relationship to another action such as "before" or "30-60 minutes after start of".)
         */
        public List<PlanDefinitionActionDefinitionRelatedActionComponent> getRelatedAction() { 
          if (this.relatedAction == null)
            this.relatedAction = new ArrayList<PlanDefinitionActionDefinitionRelatedActionComponent>();
          return this.relatedAction;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionDefinitionComponent setRelatedAction(List<PlanDefinitionActionDefinitionRelatedActionComponent> theRelatedAction) { 
          this.relatedAction = theRelatedAction;
          return this;
        }

        public boolean hasRelatedAction() { 
          if (this.relatedAction == null)
            return false;
          for (PlanDefinitionActionDefinitionRelatedActionComponent item : this.relatedAction)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionDefinitionRelatedActionComponent addRelatedAction() { //3
          PlanDefinitionActionDefinitionRelatedActionComponent t = new PlanDefinitionActionDefinitionRelatedActionComponent();
          if (this.relatedAction == null)
            this.relatedAction = new ArrayList<PlanDefinitionActionDefinitionRelatedActionComponent>();
          this.relatedAction.add(t);
          return t;
        }

        public PlanDefinitionActionDefinitionComponent addRelatedAction(PlanDefinitionActionDefinitionRelatedActionComponent t) { //3
          if (t == null)
            return this;
          if (this.relatedAction == null)
            this.relatedAction = new ArrayList<PlanDefinitionActionDefinitionRelatedActionComponent>();
          this.relatedAction.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #relatedAction}, creating it if it does not already exist
         */
        public PlanDefinitionActionDefinitionRelatedActionComponent getRelatedActionFirstRep() { 
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
        public PlanDefinitionActionDefinitionComponent setTiming(Type value) { 
          this.timing = value;
          return this;
        }

        /**
         * @return {@link #participantType} (The type of participant in the action.)
         */
        public List<Enumeration<PlanActionParticipantType>> getParticipantType() { 
          if (this.participantType == null)
            this.participantType = new ArrayList<Enumeration<PlanActionParticipantType>>();
          return this.participantType;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionDefinitionComponent setParticipantType(List<Enumeration<PlanActionParticipantType>> theParticipantType) { 
          this.participantType = theParticipantType;
          return this;
        }

        public boolean hasParticipantType() { 
          if (this.participantType == null)
            return false;
          for (Enumeration<PlanActionParticipantType> item : this.participantType)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #participantType} (The type of participant in the action.)
         */
        public Enumeration<PlanActionParticipantType> addParticipantTypeElement() {//2 
          Enumeration<PlanActionParticipantType> t = new Enumeration<PlanActionParticipantType>(new PlanActionParticipantTypeEnumFactory());
          if (this.participantType == null)
            this.participantType = new ArrayList<Enumeration<PlanActionParticipantType>>();
          this.participantType.add(t);
          return t;
        }

        /**
         * @param value {@link #participantType} (The type of participant in the action.)
         */
        public PlanDefinitionActionDefinitionComponent addParticipantType(PlanActionParticipantType value) { //1
          Enumeration<PlanActionParticipantType> t = new Enumeration<PlanActionParticipantType>(new PlanActionParticipantTypeEnumFactory());
          t.setValue(value);
          if (this.participantType == null)
            this.participantType = new ArrayList<Enumeration<PlanActionParticipantType>>();
          this.participantType.add(t);
          return this;
        }

        /**
         * @param value {@link #participantType} (The type of participant in the action.)
         */
        public boolean hasParticipantType(PlanActionParticipantType value) { 
          if (this.participantType == null)
            return false;
          for (Enumeration<PlanActionParticipantType> v : this.participantType)
            if (v.getValue().equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #type} (The type of action to perform (create, update, remove).)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.type");
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
        public PlanDefinitionActionDefinitionComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #groupingBehavior} (Defines the grouping behavior for the action and its children.). This is the underlying object with id, value and extensions. The accessor "getGroupingBehavior" gives direct access to the value
         */
        public Enumeration<PlanActionGroupingBehavior> getGroupingBehaviorElement() { 
          if (this.groupingBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.groupingBehavior");
            else if (Configuration.doAutoCreate())
              this.groupingBehavior = new Enumeration<PlanActionGroupingBehavior>(new PlanActionGroupingBehaviorEnumFactory()); // bb
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
        public PlanDefinitionActionDefinitionComponent setGroupingBehaviorElement(Enumeration<PlanActionGroupingBehavior> value) { 
          this.groupingBehavior = value;
          return this;
        }

        /**
         * @return Defines the grouping behavior for the action and its children.
         */
        public PlanActionGroupingBehavior getGroupingBehavior() { 
          return this.groupingBehavior == null ? null : this.groupingBehavior.getValue();
        }

        /**
         * @param value Defines the grouping behavior for the action and its children.
         */
        public PlanDefinitionActionDefinitionComponent setGroupingBehavior(PlanActionGroupingBehavior value) { 
          if (value == null)
            this.groupingBehavior = null;
          else {
            if (this.groupingBehavior == null)
              this.groupingBehavior = new Enumeration<PlanActionGroupingBehavior>(new PlanActionGroupingBehaviorEnumFactory());
            this.groupingBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #selectionBehavior} (Defines the selection behavior for the action and its children.). This is the underlying object with id, value and extensions. The accessor "getSelectionBehavior" gives direct access to the value
         */
        public Enumeration<PlanActionSelectionBehavior> getSelectionBehaviorElement() { 
          if (this.selectionBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.selectionBehavior");
            else if (Configuration.doAutoCreate())
              this.selectionBehavior = new Enumeration<PlanActionSelectionBehavior>(new PlanActionSelectionBehaviorEnumFactory()); // bb
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
        public PlanDefinitionActionDefinitionComponent setSelectionBehaviorElement(Enumeration<PlanActionSelectionBehavior> value) { 
          this.selectionBehavior = value;
          return this;
        }

        /**
         * @return Defines the selection behavior for the action and its children.
         */
        public PlanActionSelectionBehavior getSelectionBehavior() { 
          return this.selectionBehavior == null ? null : this.selectionBehavior.getValue();
        }

        /**
         * @param value Defines the selection behavior for the action and its children.
         */
        public PlanDefinitionActionDefinitionComponent setSelectionBehavior(PlanActionSelectionBehavior value) { 
          if (value == null)
            this.selectionBehavior = null;
          else {
            if (this.selectionBehavior == null)
              this.selectionBehavior = new Enumeration<PlanActionSelectionBehavior>(new PlanActionSelectionBehaviorEnumFactory());
            this.selectionBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #requiredBehavior} (Defines the requiredness behavior for the action.). This is the underlying object with id, value and extensions. The accessor "getRequiredBehavior" gives direct access to the value
         */
        public Enumeration<PlanActionRequiredBehavior> getRequiredBehaviorElement() { 
          if (this.requiredBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.requiredBehavior");
            else if (Configuration.doAutoCreate())
              this.requiredBehavior = new Enumeration<PlanActionRequiredBehavior>(new PlanActionRequiredBehaviorEnumFactory()); // bb
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
        public PlanDefinitionActionDefinitionComponent setRequiredBehaviorElement(Enumeration<PlanActionRequiredBehavior> value) { 
          this.requiredBehavior = value;
          return this;
        }

        /**
         * @return Defines the requiredness behavior for the action.
         */
        public PlanActionRequiredBehavior getRequiredBehavior() { 
          return this.requiredBehavior == null ? null : this.requiredBehavior.getValue();
        }

        /**
         * @param value Defines the requiredness behavior for the action.
         */
        public PlanDefinitionActionDefinitionComponent setRequiredBehavior(PlanActionRequiredBehavior value) { 
          if (value == null)
            this.requiredBehavior = null;
          else {
            if (this.requiredBehavior == null)
              this.requiredBehavior = new Enumeration<PlanActionRequiredBehavior>(new PlanActionRequiredBehaviorEnumFactory());
            this.requiredBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #precheckBehavior} (Defines whether the action should usually be preselected.). This is the underlying object with id, value and extensions. The accessor "getPrecheckBehavior" gives direct access to the value
         */
        public Enumeration<PlanActionPrecheckBehavior> getPrecheckBehaviorElement() { 
          if (this.precheckBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.precheckBehavior");
            else if (Configuration.doAutoCreate())
              this.precheckBehavior = new Enumeration<PlanActionPrecheckBehavior>(new PlanActionPrecheckBehaviorEnumFactory()); // bb
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
        public PlanDefinitionActionDefinitionComponent setPrecheckBehaviorElement(Enumeration<PlanActionPrecheckBehavior> value) { 
          this.precheckBehavior = value;
          return this;
        }

        /**
         * @return Defines whether the action should usually be preselected.
         */
        public PlanActionPrecheckBehavior getPrecheckBehavior() { 
          return this.precheckBehavior == null ? null : this.precheckBehavior.getValue();
        }

        /**
         * @param value Defines whether the action should usually be preselected.
         */
        public PlanDefinitionActionDefinitionComponent setPrecheckBehavior(PlanActionPrecheckBehavior value) { 
          if (value == null)
            this.precheckBehavior = null;
          else {
            if (this.precheckBehavior == null)
              this.precheckBehavior = new Enumeration<PlanActionPrecheckBehavior>(new PlanActionPrecheckBehaviorEnumFactory());
            this.precheckBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #cardinalityBehavior} (Defines whether the action can be selected multiple times.). This is the underlying object with id, value and extensions. The accessor "getCardinalityBehavior" gives direct access to the value
         */
        public Enumeration<PlanActionCardinalityBehavior> getCardinalityBehaviorElement() { 
          if (this.cardinalityBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.cardinalityBehavior");
            else if (Configuration.doAutoCreate())
              this.cardinalityBehavior = new Enumeration<PlanActionCardinalityBehavior>(new PlanActionCardinalityBehaviorEnumFactory()); // bb
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
        public PlanDefinitionActionDefinitionComponent setCardinalityBehaviorElement(Enumeration<PlanActionCardinalityBehavior> value) { 
          this.cardinalityBehavior = value;
          return this;
        }

        /**
         * @return Defines whether the action can be selected multiple times.
         */
        public PlanActionCardinalityBehavior getCardinalityBehavior() { 
          return this.cardinalityBehavior == null ? null : this.cardinalityBehavior.getValue();
        }

        /**
         * @param value Defines whether the action can be selected multiple times.
         */
        public PlanDefinitionActionDefinitionComponent setCardinalityBehavior(PlanActionCardinalityBehavior value) { 
          if (value == null)
            this.cardinalityBehavior = null;
          else {
            if (this.cardinalityBehavior == null)
              this.cardinalityBehavior = new Enumeration<PlanActionCardinalityBehavior>(new PlanActionCardinalityBehaviorEnumFactory());
            this.cardinalityBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #activityDefinition} (A reference to an ActivityDefinition that describes the action to be taken in detail.)
         */
        public Reference getActivityDefinition() { 
          if (this.activityDefinition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.activityDefinition");
            else if (Configuration.doAutoCreate())
              this.activityDefinition = new Reference(); // cc
          return this.activityDefinition;
        }

        public boolean hasActivityDefinition() { 
          return this.activityDefinition != null && !this.activityDefinition.isEmpty();
        }

        /**
         * @param value {@link #activityDefinition} (A reference to an ActivityDefinition that describes the action to be taken in detail.)
         */
        public PlanDefinitionActionDefinitionComponent setActivityDefinition(Reference value) { 
          this.activityDefinition = value;
          return this;
        }

        /**
         * @return {@link #activityDefinition} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to an ActivityDefinition that describes the action to be taken in detail.)
         */
        public ActivityDefinition getActivityDefinitionTarget() { 
          if (this.activityDefinitionTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.activityDefinition");
            else if (Configuration.doAutoCreate())
              this.activityDefinitionTarget = new ActivityDefinition(); // aa
          return this.activityDefinitionTarget;
        }

        /**
         * @param value {@link #activityDefinition} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to an ActivityDefinition that describes the action to be taken in detail.)
         */
        public PlanDefinitionActionDefinitionComponent setActivityDefinitionTarget(ActivityDefinition value) { 
          this.activityDefinitionTarget = value;
          return this;
        }

        /**
         * @return {@link #transform} (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
         */
        public Reference getTransform() { 
          if (this.transform == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.transform");
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
        public PlanDefinitionActionDefinitionComponent setTransform(Reference value) { 
          this.transform = value;
          return this;
        }

        /**
         * @return {@link #transform} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
         */
        public StructureMap getTransformTarget() { 
          if (this.transformTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.transform");
            else if (Configuration.doAutoCreate())
              this.transformTarget = new StructureMap(); // aa
          return this.transformTarget;
        }

        /**
         * @param value {@link #transform} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
         */
        public PlanDefinitionActionDefinitionComponent setTransformTarget(StructureMap value) { 
          this.transformTarget = value;
          return this;
        }

        /**
         * @return {@link #dynamicValue} (Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.)
         */
        public List<PlanDefinitionActionDefinitionDynamicValueComponent> getDynamicValue() { 
          if (this.dynamicValue == null)
            this.dynamicValue = new ArrayList<PlanDefinitionActionDefinitionDynamicValueComponent>();
          return this.dynamicValue;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionDefinitionComponent setDynamicValue(List<PlanDefinitionActionDefinitionDynamicValueComponent> theDynamicValue) { 
          this.dynamicValue = theDynamicValue;
          return this;
        }

        public boolean hasDynamicValue() { 
          if (this.dynamicValue == null)
            return false;
          for (PlanDefinitionActionDefinitionDynamicValueComponent item : this.dynamicValue)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionDefinitionDynamicValueComponent addDynamicValue() { //3
          PlanDefinitionActionDefinitionDynamicValueComponent t = new PlanDefinitionActionDefinitionDynamicValueComponent();
          if (this.dynamicValue == null)
            this.dynamicValue = new ArrayList<PlanDefinitionActionDefinitionDynamicValueComponent>();
          this.dynamicValue.add(t);
          return t;
        }

        public PlanDefinitionActionDefinitionComponent addDynamicValue(PlanDefinitionActionDefinitionDynamicValueComponent t) { //3
          if (t == null)
            return this;
          if (this.dynamicValue == null)
            this.dynamicValue = new ArrayList<PlanDefinitionActionDefinitionDynamicValueComponent>();
          this.dynamicValue.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #dynamicValue}, creating it if it does not already exist
         */
        public PlanDefinitionActionDefinitionDynamicValueComponent getDynamicValueFirstRep() { 
          if (getDynamicValue().isEmpty()) {
            addDynamicValue();
          }
          return getDynamicValue().get(0);
        }

        /**
         * @return {@link #actionDefinition} (Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.)
         */
        public List<PlanDefinitionActionDefinitionComponent> getActionDefinition() { 
          if (this.actionDefinition == null)
            this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
          return this.actionDefinition;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionDefinitionComponent setActionDefinition(List<PlanDefinitionActionDefinitionComponent> theActionDefinition) { 
          this.actionDefinition = theActionDefinition;
          return this;
        }

        public boolean hasActionDefinition() { 
          if (this.actionDefinition == null)
            return false;
          for (PlanDefinitionActionDefinitionComponent item : this.actionDefinition)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionDefinitionComponent addActionDefinition() { //3
          PlanDefinitionActionDefinitionComponent t = new PlanDefinitionActionDefinitionComponent();
          if (this.actionDefinition == null)
            this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
          this.actionDefinition.add(t);
          return t;
        }

        public PlanDefinitionActionDefinitionComponent addActionDefinition(PlanDefinitionActionDefinitionComponent t) { //3
          if (t == null)
            return this;
          if (this.actionDefinition == null)
            this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
          this.actionDefinition.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #actionDefinition}, creating it if it does not already exist
         */
        public PlanDefinitionActionDefinitionComponent getActionDefinitionFirstRep() { 
          if (getActionDefinition().isEmpty()) {
            addActionDefinition();
          }
          return getActionDefinition().get(0);
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
          childrenList.add(new Property("triggerDefinition", "TriggerDefinition", "A description of when the action should be triggered.", 0, java.lang.Integer.MAX_VALUE, triggerDefinition));
          childrenList.add(new Property("condition", "", "An expression specifying whether or not the action is applicable in a given context.", 0, java.lang.Integer.MAX_VALUE, condition));
          childrenList.add(new Property("relatedAction", "", "A relationship to another action such as \"before\" or \"30-60 minutes after start of\".", 0, java.lang.Integer.MAX_VALUE, relatedAction));
          childrenList.add(new Property("timing[x]", "dateTime|Period|Duration|Range|Timing", "An optional value describing when the action should be performed.", 0, java.lang.Integer.MAX_VALUE, timing));
          childrenList.add(new Property("participantType", "code", "The type of participant in the action.", 0, java.lang.Integer.MAX_VALUE, participantType));
          childrenList.add(new Property("type", "Coding", "The type of action to perform (create, update, remove).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("groupingBehavior", "code", "Defines the grouping behavior for the action and its children.", 0, java.lang.Integer.MAX_VALUE, groupingBehavior));
          childrenList.add(new Property("selectionBehavior", "code", "Defines the selection behavior for the action and its children.", 0, java.lang.Integer.MAX_VALUE, selectionBehavior));
          childrenList.add(new Property("requiredBehavior", "code", "Defines the requiredness behavior for the action.", 0, java.lang.Integer.MAX_VALUE, requiredBehavior));
          childrenList.add(new Property("precheckBehavior", "code", "Defines whether the action should usually be preselected.", 0, java.lang.Integer.MAX_VALUE, precheckBehavior));
          childrenList.add(new Property("cardinalityBehavior", "code", "Defines whether the action can be selected multiple times.", 0, java.lang.Integer.MAX_VALUE, cardinalityBehavior));
          childrenList.add(new Property("activityDefinition", "Reference(ActivityDefinition)", "A reference to an ActivityDefinition that describes the action to be taken in detail.", 0, java.lang.Integer.MAX_VALUE, activityDefinition));
          childrenList.add(new Property("transform", "Reference(StructureMap)", "A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.", 0, java.lang.Integer.MAX_VALUE, transform));
          childrenList.add(new Property("dynamicValue", "", "Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.", 0, java.lang.Integer.MAX_VALUE, dynamicValue));
          childrenList.add(new Property("actionDefinition", "@PlanDefinition.actionDefinition", "Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.", 0, java.lang.Integer.MAX_VALUE, actionDefinition));
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
        case 1126736171: /*triggerDefinition*/ return this.triggerDefinition == null ? new Base[0] : this.triggerDefinition.toArray(new Base[this.triggerDefinition.size()]); // TriggerDefinition
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : new Base[] {this.condition}; // PlanDefinitionActionDefinitionConditionComponent
        case -384107967: /*relatedAction*/ return this.relatedAction == null ? new Base[0] : this.relatedAction.toArray(new Base[this.relatedAction.size()]); // PlanDefinitionActionDefinitionRelatedActionComponent
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Type
        case 841294093: /*participantType*/ return this.participantType == null ? new Base[0] : this.participantType.toArray(new Base[this.participantType.size()]); // Enumeration<PlanActionParticipantType>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 586678389: /*groupingBehavior*/ return this.groupingBehavior == null ? new Base[0] : new Base[] {this.groupingBehavior}; // Enumeration<PlanActionGroupingBehavior>
        case 168639486: /*selectionBehavior*/ return this.selectionBehavior == null ? new Base[0] : new Base[] {this.selectionBehavior}; // Enumeration<PlanActionSelectionBehavior>
        case -1163906287: /*requiredBehavior*/ return this.requiredBehavior == null ? new Base[0] : new Base[] {this.requiredBehavior}; // Enumeration<PlanActionRequiredBehavior>
        case -1174249033: /*precheckBehavior*/ return this.precheckBehavior == null ? new Base[0] : new Base[] {this.precheckBehavior}; // Enumeration<PlanActionPrecheckBehavior>
        case -922577408: /*cardinalityBehavior*/ return this.cardinalityBehavior == null ? new Base[0] : new Base[] {this.cardinalityBehavior}; // Enumeration<PlanActionCardinalityBehavior>
        case -990265918: /*activityDefinition*/ return this.activityDefinition == null ? new Base[0] : new Base[] {this.activityDefinition}; // Reference
        case 1052666732: /*transform*/ return this.transform == null ? new Base[0] : new Base[] {this.transform}; // Reference
        case 572625010: /*dynamicValue*/ return this.dynamicValue == null ? new Base[0] : this.dynamicValue.toArray(new Base[this.dynamicValue.size()]); // PlanDefinitionActionDefinitionDynamicValueComponent
        case -285031383: /*actionDefinition*/ return this.actionDefinition == null ? new Base[0] : this.actionDefinition.toArray(new Base[this.actionDefinition.size()]); // PlanDefinitionActionDefinitionComponent
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
        case 1126736171: // triggerDefinition
          this.getTriggerDefinition().add(castToTriggerDefinition(value)); // TriggerDefinition
          break;
        case -861311717: // condition
          this.condition = (PlanDefinitionActionDefinitionConditionComponent) value; // PlanDefinitionActionDefinitionConditionComponent
          break;
        case -384107967: // relatedAction
          this.getRelatedAction().add((PlanDefinitionActionDefinitionRelatedActionComponent) value); // PlanDefinitionActionDefinitionRelatedActionComponent
          break;
        case -873664438: // timing
          this.timing = castToType(value); // Type
          break;
        case 841294093: // participantType
          this.getParticipantType().add(new PlanActionParticipantTypeEnumFactory().fromType(value)); // Enumeration<PlanActionParticipantType>
          break;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 586678389: // groupingBehavior
          this.groupingBehavior = new PlanActionGroupingBehaviorEnumFactory().fromType(value); // Enumeration<PlanActionGroupingBehavior>
          break;
        case 168639486: // selectionBehavior
          this.selectionBehavior = new PlanActionSelectionBehaviorEnumFactory().fromType(value); // Enumeration<PlanActionSelectionBehavior>
          break;
        case -1163906287: // requiredBehavior
          this.requiredBehavior = new PlanActionRequiredBehaviorEnumFactory().fromType(value); // Enumeration<PlanActionRequiredBehavior>
          break;
        case -1174249033: // precheckBehavior
          this.precheckBehavior = new PlanActionPrecheckBehaviorEnumFactory().fromType(value); // Enumeration<PlanActionPrecheckBehavior>
          break;
        case -922577408: // cardinalityBehavior
          this.cardinalityBehavior = new PlanActionCardinalityBehaviorEnumFactory().fromType(value); // Enumeration<PlanActionCardinalityBehavior>
          break;
        case -990265918: // activityDefinition
          this.activityDefinition = castToReference(value); // Reference
          break;
        case 1052666732: // transform
          this.transform = castToReference(value); // Reference
          break;
        case 572625010: // dynamicValue
          this.getDynamicValue().add((PlanDefinitionActionDefinitionDynamicValueComponent) value); // PlanDefinitionActionDefinitionDynamicValueComponent
          break;
        case -285031383: // actionDefinition
          this.getActionDefinition().add((PlanDefinitionActionDefinitionComponent) value); // PlanDefinitionActionDefinitionComponent
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
        else if (name.equals("triggerDefinition"))
          this.getTriggerDefinition().add(castToTriggerDefinition(value));
        else if (name.equals("condition"))
          this.condition = (PlanDefinitionActionDefinitionConditionComponent) value; // PlanDefinitionActionDefinitionConditionComponent
        else if (name.equals("relatedAction"))
          this.getRelatedAction().add((PlanDefinitionActionDefinitionRelatedActionComponent) value);
        else if (name.equals("timing[x]"))
          this.timing = castToType(value); // Type
        else if (name.equals("participantType"))
          this.getParticipantType().add(new PlanActionParticipantTypeEnumFactory().fromType(value));
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("groupingBehavior"))
          this.groupingBehavior = new PlanActionGroupingBehaviorEnumFactory().fromType(value); // Enumeration<PlanActionGroupingBehavior>
        else if (name.equals("selectionBehavior"))
          this.selectionBehavior = new PlanActionSelectionBehaviorEnumFactory().fromType(value); // Enumeration<PlanActionSelectionBehavior>
        else if (name.equals("requiredBehavior"))
          this.requiredBehavior = new PlanActionRequiredBehaviorEnumFactory().fromType(value); // Enumeration<PlanActionRequiredBehavior>
        else if (name.equals("precheckBehavior"))
          this.precheckBehavior = new PlanActionPrecheckBehaviorEnumFactory().fromType(value); // Enumeration<PlanActionPrecheckBehavior>
        else if (name.equals("cardinalityBehavior"))
          this.cardinalityBehavior = new PlanActionCardinalityBehaviorEnumFactory().fromType(value); // Enumeration<PlanActionCardinalityBehavior>
        else if (name.equals("activityDefinition"))
          this.activityDefinition = castToReference(value); // Reference
        else if (name.equals("transform"))
          this.transform = castToReference(value); // Reference
        else if (name.equals("dynamicValue"))
          this.getDynamicValue().add((PlanDefinitionActionDefinitionDynamicValueComponent) value);
        else if (name.equals("actionDefinition"))
          this.getActionDefinition().add((PlanDefinitionActionDefinitionComponent) value);
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
        case 1126736171:  return addTriggerDefinition(); // TriggerDefinition
        case -861311717:  return getCondition(); // PlanDefinitionActionDefinitionConditionComponent
        case -384107967:  return addRelatedAction(); // PlanDefinitionActionDefinitionRelatedActionComponent
        case 164632566:  return getTiming(); // Type
        case 841294093: throw new FHIRException("Cannot make property participantType as it is not a complex type"); // Enumeration<PlanActionParticipantType>
        case 3575610:  return getType(); // Coding
        case 586678389: throw new FHIRException("Cannot make property groupingBehavior as it is not a complex type"); // Enumeration<PlanActionGroupingBehavior>
        case 168639486: throw new FHIRException("Cannot make property selectionBehavior as it is not a complex type"); // Enumeration<PlanActionSelectionBehavior>
        case -1163906287: throw new FHIRException("Cannot make property requiredBehavior as it is not a complex type"); // Enumeration<PlanActionRequiredBehavior>
        case -1174249033: throw new FHIRException("Cannot make property precheckBehavior as it is not a complex type"); // Enumeration<PlanActionPrecheckBehavior>
        case -922577408: throw new FHIRException("Cannot make property cardinalityBehavior as it is not a complex type"); // Enumeration<PlanActionCardinalityBehavior>
        case -990265918:  return getActivityDefinition(); // Reference
        case 1052666732:  return getTransform(); // Reference
        case 572625010:  return addDynamicValue(); // PlanDefinitionActionDefinitionDynamicValueComponent
        case -285031383:  return addActionDefinition(); // PlanDefinitionActionDefinitionComponent
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
        else if (name.equals("documentation")) {
          return addDocumentation();
        }
        else if (name.equals("triggerDefinition")) {
          return addTriggerDefinition();
        }
        else if (name.equals("condition")) {
          this.condition = new PlanDefinitionActionDefinitionConditionComponent();
          return this.condition;
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
        else if (name.equals("participantType")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.participantType");
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
        else if (name.equals("activityDefinition")) {
          this.activityDefinition = new Reference();
          return this.activityDefinition;
        }
        else if (name.equals("transform")) {
          this.transform = new Reference();
          return this.transform;
        }
        else if (name.equals("dynamicValue")) {
          return addDynamicValue();
        }
        else if (name.equals("actionDefinition")) {
          return addActionDefinition();
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionActionDefinitionComponent copy() {
        PlanDefinitionActionDefinitionComponent dst = new PlanDefinitionActionDefinitionComponent();
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
        if (triggerDefinition != null) {
          dst.triggerDefinition = new ArrayList<TriggerDefinition>();
          for (TriggerDefinition i : triggerDefinition)
            dst.triggerDefinition.add(i.copy());
        };
        dst.condition = condition == null ? null : condition.copy();
        if (relatedAction != null) {
          dst.relatedAction = new ArrayList<PlanDefinitionActionDefinitionRelatedActionComponent>();
          for (PlanDefinitionActionDefinitionRelatedActionComponent i : relatedAction)
            dst.relatedAction.add(i.copy());
        };
        dst.timing = timing == null ? null : timing.copy();
        if (participantType != null) {
          dst.participantType = new ArrayList<Enumeration<PlanActionParticipantType>>();
          for (Enumeration<PlanActionParticipantType> i : participantType)
            dst.participantType.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.groupingBehavior = groupingBehavior == null ? null : groupingBehavior.copy();
        dst.selectionBehavior = selectionBehavior == null ? null : selectionBehavior.copy();
        dst.requiredBehavior = requiredBehavior == null ? null : requiredBehavior.copy();
        dst.precheckBehavior = precheckBehavior == null ? null : precheckBehavior.copy();
        dst.cardinalityBehavior = cardinalityBehavior == null ? null : cardinalityBehavior.copy();
        dst.activityDefinition = activityDefinition == null ? null : activityDefinition.copy();
        dst.transform = transform == null ? null : transform.copy();
        if (dynamicValue != null) {
          dst.dynamicValue = new ArrayList<PlanDefinitionActionDefinitionDynamicValueComponent>();
          for (PlanDefinitionActionDefinitionDynamicValueComponent i : dynamicValue)
            dst.dynamicValue.add(i.copy());
        };
        if (actionDefinition != null) {
          dst.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
          for (PlanDefinitionActionDefinitionComponent i : actionDefinition)
            dst.actionDefinition.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionComponent))
          return false;
        PlanDefinitionActionDefinitionComponent o = (PlanDefinitionActionDefinitionComponent) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(label, o.label, true)
           && compareDeep(title, o.title, true) && compareDeep(description, o.description, true) && compareDeep(textEquivalent, o.textEquivalent, true)
           && compareDeep(code, o.code, true) && compareDeep(documentation, o.documentation, true) && compareDeep(triggerDefinition, o.triggerDefinition, true)
           && compareDeep(condition, o.condition, true) && compareDeep(relatedAction, o.relatedAction, true)
           && compareDeep(timing, o.timing, true) && compareDeep(participantType, o.participantType, true)
           && compareDeep(type, o.type, true) && compareDeep(groupingBehavior, o.groupingBehavior, true) && compareDeep(selectionBehavior, o.selectionBehavior, true)
           && compareDeep(requiredBehavior, o.requiredBehavior, true) && compareDeep(precheckBehavior, o.precheckBehavior, true)
           && compareDeep(cardinalityBehavior, o.cardinalityBehavior, true) && compareDeep(activityDefinition, o.activityDefinition, true)
           && compareDeep(transform, o.transform, true) && compareDeep(dynamicValue, o.dynamicValue, true)
           && compareDeep(actionDefinition, o.actionDefinition, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionComponent))
          return false;
        PlanDefinitionActionDefinitionComponent o = (PlanDefinitionActionDefinitionComponent) other;
        return compareValues(label, o.label, true) && compareValues(title, o.title, true) && compareValues(description, o.description, true)
           && compareValues(textEquivalent, o.textEquivalent, true) && compareValues(participantType, o.participantType, true)
           && compareValues(groupingBehavior, o.groupingBehavior, true) && compareValues(selectionBehavior, o.selectionBehavior, true)
           && compareValues(requiredBehavior, o.requiredBehavior, true) && compareValues(precheckBehavior, o.precheckBehavior, true)
           && compareValues(cardinalityBehavior, o.cardinalityBehavior, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actionIdentifier, label, title
          , description, textEquivalent, code, documentation, triggerDefinition, condition
          , relatedAction, timing, participantType, type, groupingBehavior, selectionBehavior
          , requiredBehavior, precheckBehavior, cardinalityBehavior, activityDefinition, transform
          , dynamicValue, actionDefinition);
      }

  public String fhirType() {
    return "PlanDefinition.actionDefinition";

  }

  }

    @Block()
    public static class PlanDefinitionActionDefinitionConditionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A brief, natural language description of the condition that effectively communicates the intended semantics.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Natural language description of the condition", formalDefinition="A brief, natural language description of the condition that effectively communicates the intended semantics." )
        protected StringType description;

        /**
         * The media type of the language for the expression.
         */
        @Child(name = "language", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language of the expression", formalDefinition="The media type of the language for the expression." )
        protected StringType language;

        /**
         * An expression that returns true or false, indicating whether or not the condition is satisfied.
         */
        @Child(name = "expression", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Boolean-valued expression", formalDefinition="An expression that returns true or false, indicating whether or not the condition is satisfied." )
        protected StringType expression;

        private static final long serialVersionUID = 1354288281L;

    /**
     * Constructor
     */
      public PlanDefinitionActionDefinitionConditionComponent() {
        super();
      }

        /**
         * @return {@link #description} (A brief, natural language description of the condition that effectively communicates the intended semantics.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionConditionComponent.description");
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
        public PlanDefinitionActionDefinitionConditionComponent setDescriptionElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionConditionComponent setDescription(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionConditionComponent.language");
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
        public PlanDefinitionActionDefinitionConditionComponent setLanguageElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionConditionComponent setLanguage(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionConditionComponent.expression");
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
        public PlanDefinitionActionDefinitionConditionComponent setExpressionElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionConditionComponent setExpression(String value) { 
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
          childrenList.add(new Property("description", "string", "A brief, natural language description of the condition that effectively communicates the intended semantics.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("language", "string", "The media type of the language for the expression.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("expression", "string", "An expression that returns true or false, indicating whether or not the condition is satisfied.", 0, java.lang.Integer.MAX_VALUE, expression));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // StringType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -1613589672: // language
          this.language = castToString(value); // StringType
          break;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("language"))
          this.language = castToString(value); // StringType
        else if (name.equals("expression"))
          this.expression = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -1613589672: throw new FHIRException("Cannot make property language as it is not a complex type"); // StringType
        case -1795452264: throw new FHIRException("Cannot make property expression as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
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

      public PlanDefinitionActionDefinitionConditionComponent copy() {
        PlanDefinitionActionDefinitionConditionComponent dst = new PlanDefinitionActionDefinitionConditionComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.language = language == null ? null : language.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionConditionComponent))
          return false;
        PlanDefinitionActionDefinitionConditionComponent o = (PlanDefinitionActionDefinitionConditionComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(language, o.language, true)
           && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionConditionComponent))
          return false;
        PlanDefinitionActionDefinitionConditionComponent o = (PlanDefinitionActionDefinitionConditionComponent) other;
        return compareValues(description, o.description, true) && compareValues(language, o.language, true)
           && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, language, expression
          );
      }

  public String fhirType() {
    return "PlanDefinition.actionDefinition.condition";

  }

  }

    @Block()
    public static class PlanDefinitionActionDefinitionRelatedActionComponent extends BackboneElement implements IBaseBackboneElement {
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
        protected Enumeration<PlanActionRelationshipType> relationship;

        /**
         * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
         */
        @Child(name = "offset", type = {Duration.class, Range.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time offset for the relationship", formalDefinition="A duration or range of durations to apply to the relationship. For example, 30-60 minutes before." )
        protected Type offset;

        private static final long serialVersionUID = -2131606397L;

    /**
     * Constructor
     */
      public PlanDefinitionActionDefinitionRelatedActionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PlanDefinitionActionDefinitionRelatedActionComponent(Identifier actionIdentifier, Enumeration<PlanActionRelationshipType> relationship) {
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionRelatedActionComponent.actionIdentifier");
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
        public PlanDefinitionActionDefinitionRelatedActionComponent setActionIdentifier(Identifier value) { 
          this.actionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #relationship} (The relationship of this action to the related action.). This is the underlying object with id, value and extensions. The accessor "getRelationship" gives direct access to the value
         */
        public Enumeration<PlanActionRelationshipType> getRelationshipElement() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionRelatedActionComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new Enumeration<PlanActionRelationshipType>(new PlanActionRelationshipTypeEnumFactory()); // bb
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
        public PlanDefinitionActionDefinitionRelatedActionComponent setRelationshipElement(Enumeration<PlanActionRelationshipType> value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return The relationship of this action to the related action.
         */
        public PlanActionRelationshipType getRelationship() { 
          return this.relationship == null ? null : this.relationship.getValue();
        }

        /**
         * @param value The relationship of this action to the related action.
         */
        public PlanDefinitionActionDefinitionRelatedActionComponent setRelationship(PlanActionRelationshipType value) { 
            if (this.relationship == null)
              this.relationship = new Enumeration<PlanActionRelationshipType>(new PlanActionRelationshipTypeEnumFactory());
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
        public PlanDefinitionActionDefinitionRelatedActionComponent setOffset(Type value) { 
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
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // Enumeration<PlanActionRelationshipType>
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
          this.relationship = new PlanActionRelationshipTypeEnumFactory().fromType(value); // Enumeration<PlanActionRelationshipType>
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
          this.relationship = new PlanActionRelationshipTypeEnumFactory().fromType(value); // Enumeration<PlanActionRelationshipType>
        else if (name.equals("offset[x]"))
          this.offset = castToType(value); // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -889046145:  return getActionIdentifier(); // Identifier
        case -261851592: throw new FHIRException("Cannot make property relationship as it is not a complex type"); // Enumeration<PlanActionRelationshipType>
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

      public PlanDefinitionActionDefinitionRelatedActionComponent copy() {
        PlanDefinitionActionDefinitionRelatedActionComponent dst = new PlanDefinitionActionDefinitionRelatedActionComponent();
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
        if (!(other instanceof PlanDefinitionActionDefinitionRelatedActionComponent))
          return false;
        PlanDefinitionActionDefinitionRelatedActionComponent o = (PlanDefinitionActionDefinitionRelatedActionComponent) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(relationship, o.relationship, true)
           && compareDeep(offset, o.offset, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionRelatedActionComponent))
          return false;
        PlanDefinitionActionDefinitionRelatedActionComponent o = (PlanDefinitionActionDefinitionRelatedActionComponent) other;
        return compareValues(relationship, o.relationship, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actionIdentifier, relationship
          , offset);
      }

  public String fhirType() {
    return "PlanDefinition.actionDefinition.relatedAction";

  }

  }

    @Block()
    public static class PlanDefinitionActionDefinitionDynamicValueComponent extends BackboneElement implements IBaseBackboneElement {
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
      public PlanDefinitionActionDefinitionDynamicValueComponent() {
        super();
      }

        /**
         * @return {@link #description} (A brief, natural language description of the intended semantics of the dynamic value.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionDynamicValueComponent.description");
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
        public PlanDefinitionActionDefinitionDynamicValueComponent setDescriptionElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionDynamicValueComponent setDescription(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionDynamicValueComponent.path");
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
        public PlanDefinitionActionDefinitionDynamicValueComponent setPathElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionDynamicValueComponent setPath(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionDynamicValueComponent.language");
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
        public PlanDefinitionActionDefinitionDynamicValueComponent setLanguageElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionDynamicValueComponent setLanguage(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionDynamicValueComponent.expression");
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
        public PlanDefinitionActionDefinitionDynamicValueComponent setExpressionElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionDynamicValueComponent setExpression(String value) { 
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case 3433509: // path
          this.path = castToString(value); // StringType
          break;
        case -1613589672: // language
          this.language = castToString(value); // StringType
          break;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("path"))
          this.path = castToString(value); // StringType
        else if (name.equals("language"))
          this.language = castToString(value); // StringType
        else if (name.equals("expression"))
          this.expression = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case 3433509: throw new FHIRException("Cannot make property path as it is not a complex type"); // StringType
        case -1613589672: throw new FHIRException("Cannot make property language as it is not a complex type"); // StringType
        case -1795452264: throw new FHIRException("Cannot make property expression as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
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

      public PlanDefinitionActionDefinitionDynamicValueComponent copy() {
        PlanDefinitionActionDefinitionDynamicValueComponent dst = new PlanDefinitionActionDefinitionDynamicValueComponent();
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
        if (!(other instanceof PlanDefinitionActionDefinitionDynamicValueComponent))
          return false;
        PlanDefinitionActionDefinitionDynamicValueComponent o = (PlanDefinitionActionDefinitionDynamicValueComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(path, o.path, true) && compareDeep(language, o.language, true)
           && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionDynamicValueComponent))
          return false;
        PlanDefinitionActionDefinitionDynamicValueComponent o = (PlanDefinitionActionDefinitionDynamicValueComponent) other;
        return compareValues(description, o.description, true) && compareValues(path, o.path, true) && compareValues(language, o.language, true)
           && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, path, language
          , expression);
      }

  public String fhirType() {
    return "PlanDefinition.actionDefinition.dynamicValue";

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
     * Explains why this plan definition is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this plan definition is defined", formalDefinition="Explains why this plan definition is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * A detailed description of how the asset is used from a clinical perspective.
     */
    @Child(name = "usage", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the clinical usage of the asset", formalDefinition="A detailed description of how the asset is used from a clinical perspective." )
    protected StringType usage;

    /**
     * The date on which the asset content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    @Child(name = "approvalDate", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When plan definition approved by publisher", formalDefinition="The date on which the asset content was approved by the publisher. Approval happens once when the content is officially approved for usage." )
    protected DateType approvalDate;

    /**
     * The date on which the asset content was last reviewed. Review happens periodically after that, but doesn't change the original approval date.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Last review date for the plan definition", formalDefinition="The date on which the asset content was last reviewed. Review happens periodically after that, but doesn't change the original approval date." )
    protected DateType lastReviewDate;

    /**
     * The period during which the plan definition content was or is planned to be effective.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The effective date range for the plan definition", formalDefinition="The period during which the plan definition content was or is planned to be effective." )
    protected Period effectivePeriod;

    /**
     * Clinical topics related to the content of the asset.
     */
    @Child(name = "topic", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Descriptional topics for the asset", formalDefinition="Clinical topics related to the content of the asset." )
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
     * An action to be taken as part of the plan.
     */
    @Child(name = "actionDefinition", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Action defined by the plan", formalDefinition="An action to be taken as part of the plan." )
    protected List<PlanDefinitionActionDefinitionComponent> actionDefinition;

    private static final long serialVersionUID = 1345665574L;

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
     * @return {@link #url} (An absolute URL that is used to identify this plan definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this plan definition is (or will be) published. The URL SHOULD include the major version of the plan definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
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
     * @param value {@link #url} (An absolute URL that is used to identify this plan definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this plan definition is (or will be) published. The URL SHOULD include the major version of the plan definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public PlanDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this plan definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this plan definition is (or will be) published. The URL SHOULD include the major version of the plan definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this plan definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this plan definition is (or will be) published. The URL SHOULD include the major version of the plan definition. For more information see [Technical and Business Versions](resource.html#versions).
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
     * @return {@link #version} (The identifier that is used to identify this version of the plan definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions are orderable. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
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
     * @param value {@link #version} (The identifier that is used to identify this version of the plan definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions are orderable. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public PlanDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the plan definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions are orderable. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the plan definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions are orderable. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.
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
     * @return {@link #experimental} (A flag to indicate that this plan definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
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
     * @param value {@link #experimental} (A flag to indicate that this plan definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public PlanDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A flag to indicate that this plan definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A flag to indicate that this plan definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public PlanDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the plan definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the plan definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date  (and optionally time) when the plan definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the plan definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public PlanDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the plan definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the plan definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the plan definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the plan definition changes.
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
     * @return {@link #description} (A free text natural language description of the plan definition from the consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
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
     * @param value {@link #description} (A free text natural language description of the plan definition from the consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public PlanDefinition setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the plan definition from the consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the plan definition from the consumer's perspective.
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
     * @return {@link #purpose} (Explains why this plan definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
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
     * @param value {@link #purpose} (Explains why this plan definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public PlanDefinition setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explains why this plan definition is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explains why this plan definition is needed and why it has been designed as it has.
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
     * @return {@link #approvalDate} (The date on which the asset content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
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
     * @param value {@link #approvalDate} (The date on which the asset content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public PlanDefinition setApprovalDateElement(DateType value) { 
      this.approvalDate = value;
      return this;
    }

    /**
     * @return The date on which the asset content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public Date getApprovalDate() { 
      return this.approvalDate == null ? null : this.approvalDate.getValue();
    }

    /**
     * @param value The date on which the asset content was approved by the publisher. Approval happens once when the content is officially approved for usage.
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
     * @return {@link #lastReviewDate} (The date on which the asset content was last reviewed. Review happens periodically after that, but doesn't change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
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
     * @param value {@link #lastReviewDate} (The date on which the asset content was last reviewed. Review happens periodically after that, but doesn't change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public PlanDefinition setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the asset content was last reviewed. Review happens periodically after that, but doesn't change the original approval date.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the asset content was last reviewed. Review happens periodically after that, but doesn't change the original approval date.
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
     * @return {@link #effectivePeriod} (The period during which the plan definition content was or is planned to be effective.)
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
     * @param value {@link #effectivePeriod} (The period during which the plan definition content was or is planned to be effective.)
     */
    public PlanDefinition setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of code system definitions.)
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
     * @return {@link #jurisdiction} (A jurisdiction in which the plan definition is intended to be used.)
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
     * @return {@link #topic} (Clinical topics related to the content of the asset.)
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
     * @return {@link #actionDefinition} (An action to be taken as part of the plan.)
     */
    public List<PlanDefinitionActionDefinitionComponent> getActionDefinition() { 
      if (this.actionDefinition == null)
        this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
      return this.actionDefinition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setActionDefinition(List<PlanDefinitionActionDefinitionComponent> theActionDefinition) { 
      this.actionDefinition = theActionDefinition;
      return this;
    }

    public boolean hasActionDefinition() { 
      if (this.actionDefinition == null)
        return false;
      for (PlanDefinitionActionDefinitionComponent item : this.actionDefinition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public PlanDefinitionActionDefinitionComponent addActionDefinition() { //3
      PlanDefinitionActionDefinitionComponent t = new PlanDefinitionActionDefinitionComponent();
      if (this.actionDefinition == null)
        this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
      this.actionDefinition.add(t);
      return t;
    }

    public PlanDefinition addActionDefinition(PlanDefinitionActionDefinitionComponent t) { //3
      if (t == null)
        return this;
      if (this.actionDefinition == null)
        this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
      this.actionDefinition.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #actionDefinition}, creating it if it does not already exist
     */
    public PlanDefinitionActionDefinitionComponent getActionDefinitionFirstRep() { 
      if (getActionDefinition().isEmpty()) {
        addActionDefinition();
      }
      return getActionDefinition().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this plan definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this plan definition is (or will be) published. The URL SHOULD include the major version of the plan definition. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this plan definition when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the plan definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions are orderable. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active artifacts.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the plan definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the plan definition.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("type", "CodeableConcept", "The type of asset the plan definition represents, e.g. an order set, protocol, or event-condition-action rule.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("status", "code", "The status of this plan definition. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A flag to indicate that this plan definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the plan definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the plan definition changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the plan definition from the consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("purpose", "markdown", "Explains why this plan definition is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("usage", "string", "A detailed description of how the asset is used from a clinical perspective.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("approvalDate", "date", "The date on which the asset content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, java.lang.Integer.MAX_VALUE, approvalDate));
        childrenList.add(new Property("lastReviewDate", "date", "The date on which the asset content was last reviewed. Review happens periodically after that, but doesn't change the original approval date.", 0, java.lang.Integer.MAX_VALUE, lastReviewDate));
        childrenList.add(new Property("effectivePeriod", "Period", "The period during which the plan definition content was or is planned to be effective.", 0, java.lang.Integer.MAX_VALUE, effectivePeriod));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of code system definitions.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A jurisdiction in which the plan definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("topic", "CodeableConcept", "Clinical topics related to the content of the asset.", 0, java.lang.Integer.MAX_VALUE, topic));
        childrenList.add(new Property("contributor", "Contributor", "A contributor to the content of the asset, including authors, editors, reviewers, and endorsers.", 0, java.lang.Integer.MAX_VALUE, contributor));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the plan definition.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("copyright", "markdown", "A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the plan definition.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("relatedArtifact", "RelatedArtifact", "Related artifacts such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library resource containing any formal logic used by the plan definition.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("actionDefinition", "", "An action to be taken as part of the plan.", 0, java.lang.Integer.MAX_VALUE, actionDefinition));
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
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 666807069: /*relatedArtifact*/ return this.relatedArtifact == null ? new Base[0] : this.relatedArtifact.toArray(new Base[this.relatedArtifact.size()]); // RelatedArtifact
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // Reference
        case -285031383: /*actionDefinition*/ return this.actionDefinition == null ? new Base[0] : this.actionDefinition.toArray(new Base[this.actionDefinition.size()]); // PlanDefinitionActionDefinitionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 110371416: // title
          this.title = castToString(value); // StringType
          break;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -892481550: // status
          this.status = new PublicationStatusEnumFactory().fromType(value); // Enumeration<PublicationStatus>
          break;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          break;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          break;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          break;
        case 111574433: // usage
          this.usage = castToString(value); // StringType
          break;
        case 223539345: // approvalDate
          this.approvalDate = castToDate(value); // DateType
          break;
        case -1687512484: // lastReviewDate
          this.lastReviewDate = castToDate(value); // DateType
          break;
        case -403934648: // effectivePeriod
          this.effectivePeriod = castToPeriod(value); // Period
          break;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          break;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 110546223: // topic
          this.getTopic().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1895276325: // contributor
          this.getContributor().add(castToContributor(value)); // Contributor
          break;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          break;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          break;
        case 666807069: // relatedArtifact
          this.getRelatedArtifact().add(castToRelatedArtifact(value)); // RelatedArtifact
          break;
        case 166208699: // library
          this.getLibrary().add(castToReference(value)); // Reference
          break;
        case -285031383: // actionDefinition
          this.getActionDefinition().add((PlanDefinitionActionDefinitionComponent) value); // PlanDefinitionActionDefinitionComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("status"))
          this.status = new PublicationStatusEnumFactory().fromType(value); // Enumeration<PublicationStatus>
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("description"))
          this.description = castToMarkdown(value); // MarkdownType
        else if (name.equals("purpose"))
          this.purpose = castToMarkdown(value); // MarkdownType
        else if (name.equals("usage"))
          this.usage = castToString(value); // StringType
        else if (name.equals("approvalDate"))
          this.approvalDate = castToDate(value); // DateType
        else if (name.equals("lastReviewDate"))
          this.lastReviewDate = castToDate(value); // DateType
        else if (name.equals("effectivePeriod"))
          this.effectivePeriod = castToPeriod(value); // Period
        else if (name.equals("useContext"))
          this.getUseContext().add(castToUsageContext(value));
        else if (name.equals("jurisdiction"))
          this.getJurisdiction().add(castToCodeableConcept(value));
        else if (name.equals("topic"))
          this.getTopic().add(castToCodeableConcept(value));
        else if (name.equals("contributor"))
          this.getContributor().add(castToContributor(value));
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add(castToContactDetail(value));
        else if (name.equals("copyright"))
          this.copyright = castToMarkdown(value); // MarkdownType
        else if (name.equals("relatedArtifact"))
          this.getRelatedArtifact().add(castToRelatedArtifact(value));
        else if (name.equals("library"))
          this.getLibrary().add(castToReference(value));
        else if (name.equals("actionDefinition"))
          this.getActionDefinition().add((PlanDefinitionActionDefinitionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case -1618432855:  return addIdentifier(); // Identifier
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 110371416: throw new FHIRException("Cannot make property title as it is not a complex type"); // StringType
        case 3575610:  return getType(); // CodeableConcept
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<PublicationStatus>
        case -404562712: throw new FHIRException("Cannot make property experimental as it is not a complex type"); // BooleanType
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // MarkdownType
        case -220463842: throw new FHIRException("Cannot make property purpose as it is not a complex type"); // MarkdownType
        case 111574433: throw new FHIRException("Cannot make property usage as it is not a complex type"); // StringType
        case 223539345: throw new FHIRException("Cannot make property approvalDate as it is not a complex type"); // DateType
        case -1687512484: throw new FHIRException("Cannot make property lastReviewDate as it is not a complex type"); // DateType
        case -403934648:  return getEffectivePeriod(); // Period
        case -669707736:  return addUseContext(); // UsageContext
        case -507075711:  return addJurisdiction(); // CodeableConcept
        case 110546223:  return addTopic(); // CodeableConcept
        case -1895276325:  return addContributor(); // Contributor
        case 1447404028: throw new FHIRException("Cannot make property publisher as it is not a complex type"); // StringType
        case 951526432:  return addContact(); // ContactDetail
        case 1522889671: throw new FHIRException("Cannot make property copyright as it is not a complex type"); // MarkdownType
        case 666807069:  return addRelatedArtifact(); // RelatedArtifact
        case 166208699:  return addLibrary(); // Reference
        case -285031383:  return addActionDefinition(); // PlanDefinitionActionDefinitionComponent
        default: return super.makeProperty(hash, name);
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
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.publisher");
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
        else if (name.equals("actionDefinition")) {
          return addActionDefinition();
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
        dst.publisher = publisher == null ? null : publisher.copy();
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
        if (actionDefinition != null) {
          dst.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
          for (PlanDefinitionActionDefinitionComponent i : actionDefinition)
            dst.actionDefinition.add(i.copy());
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
           && compareDeep(library, o.library, true) && compareDeep(actionDefinition, o.actionDefinition, true)
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
          , relatedArtifact, library, actionDefinition);
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
   * Description: <b>External identifiers for the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="PlanDefinition.identifier", description="External identifiers for the plan definition", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifiers for the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>effective</b>
   * <p>
   * Description: <b>Effective time associated with the plan definition</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PlanDefinition.effectivePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective", path="PlanDefinition.effectivePeriod", description="Effective time associated with the plan definition", type="date" )
  public static final String SP_EFFECTIVE = "effective";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective</b>
   * <p>
   * Description: <b>Effective time associated with the plan definition</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PlanDefinition.effectivePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="PlanDefinition.jurisdiction", description="Intended jurisdiction for plan definition", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Name of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="PlanDefinition.name", description="Name of the plan definition", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Name of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search against the description of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="PlanDefinition.description", description="Text search against the description of the plan definition", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search against the description of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

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
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Text search against the title of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="PlanDefinition.title", description="Text search against the title of the plan definition", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Text search against the title of the plan definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The version identifier of the plan definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="PlanDefinition.version", description="The version identifier of the plan definition", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The version identifier of the plan definition</b><br>
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

