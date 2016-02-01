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
 * This resource allows for the definition of an order set as a sharable, consumable, and executable artifact in support of clinical decision support.
 */
@ResourceDef(name="OrderSet", profile="http://hl7.org/fhir/Profile/OrderSet")
public class OrderSet extends DomainResource {

    public enum OrderSetParticipantType {
        /**
         * The participant is the patient under evaluation
         */
        PATIENT, 
        /**
         * The participant is a person
         */
        PERSON, 
        /**
         * The participant is a practitioner involved in the patient's care
         */
        PRACTITIONER, 
        /**
         * The participant is a person related to the patient
         */
        RELATEDPERSON, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OrderSetParticipantType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return PATIENT;
        if ("person".equals(codeString))
          return PERSON;
        if ("practitioner".equals(codeString))
          return PRACTITIONER;
        if ("related-person".equals(codeString))
          return RELATEDPERSON;
        throw new FHIRException("Unknown OrderSetParticipantType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "patient";
            case PERSON: return "person";
            case PRACTITIONER: return "practitioner";
            case RELATEDPERSON: return "related-person";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PATIENT: return "http://hl7.org/fhir/order-set-participant";
            case PERSON: return "http://hl7.org/fhir/order-set-participant";
            case PRACTITIONER: return "http://hl7.org/fhir/order-set-participant";
            case RELATEDPERSON: return "http://hl7.org/fhir/order-set-participant";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "The participant is the patient under evaluation";
            case PERSON: return "The participant is a person";
            case PRACTITIONER: return "The participant is a practitioner involved in the patient's care";
            case RELATEDPERSON: return "The participant is a person related to the patient";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "Patient";
            case PERSON: return "Person";
            case PRACTITIONER: return "Practitioner";
            case RELATEDPERSON: return "Related Person";
            default: return "?";
          }
        }
    }

  public static class OrderSetParticipantTypeEnumFactory implements EnumFactory<OrderSetParticipantType> {
    public OrderSetParticipantType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return OrderSetParticipantType.PATIENT;
        if ("person".equals(codeString))
          return OrderSetParticipantType.PERSON;
        if ("practitioner".equals(codeString))
          return OrderSetParticipantType.PRACTITIONER;
        if ("related-person".equals(codeString))
          return OrderSetParticipantType.RELATEDPERSON;
        throw new IllegalArgumentException("Unknown OrderSetParticipantType code '"+codeString+"'");
        }
        public Enumeration<OrderSetParticipantType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("patient".equals(codeString))
          return new Enumeration<OrderSetParticipantType>(this, OrderSetParticipantType.PATIENT);
        if ("person".equals(codeString))
          return new Enumeration<OrderSetParticipantType>(this, OrderSetParticipantType.PERSON);
        if ("practitioner".equals(codeString))
          return new Enumeration<OrderSetParticipantType>(this, OrderSetParticipantType.PRACTITIONER);
        if ("related-person".equals(codeString))
          return new Enumeration<OrderSetParticipantType>(this, OrderSetParticipantType.RELATEDPERSON);
        throw new FHIRException("Unknown OrderSetParticipantType code '"+codeString+"'");
        }
    public String toCode(OrderSetParticipantType code) {
      if (code == OrderSetParticipantType.PATIENT)
        return "patient";
      if (code == OrderSetParticipantType.PERSON)
        return "person";
      if (code == OrderSetParticipantType.PRACTITIONER)
        return "practitioner";
      if (code == OrderSetParticipantType.RELATEDPERSON)
        return "related-person";
      return "?";
      }
    public String toSystem(OrderSetParticipantType code) {
      return code.getSystem();
      }
    }

    public enum OrderSetItemType {
        /**
         * The action is to create a new resource
         */
        CREATE, 
        /**
         * The action is to update an existing resource
         */
        UPDATE, 
        /**
         * The action is to remove an existing resource
         */
        REMOVE, 
        /**
         * The action is to fire a specific event
         */
        FIREEVENT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OrderSetItemType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return CREATE;
        if ("update".equals(codeString))
          return UPDATE;
        if ("remove".equals(codeString))
          return REMOVE;
        if ("fire-event".equals(codeString))
          return FIREEVENT;
        throw new FHIRException("Unknown OrderSetItemType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CREATE: return "create";
            case UPDATE: return "update";
            case REMOVE: return "remove";
            case FIREEVENT: return "fire-event";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CREATE: return "http://hl7.org/fhir/order-set-item-type";
            case UPDATE: return "http://hl7.org/fhir/order-set-item-type";
            case REMOVE: return "http://hl7.org/fhir/order-set-item-type";
            case FIREEVENT: return "http://hl7.org/fhir/order-set-item-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CREATE: return "The action is to create a new resource";
            case UPDATE: return "The action is to update an existing resource";
            case REMOVE: return "The action is to remove an existing resource";
            case FIREEVENT: return "The action is to fire a specific event";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CREATE: return "Create";
            case UPDATE: return "Update";
            case REMOVE: return "Remove";
            case FIREEVENT: return "Fire Event";
            default: return "?";
          }
        }
    }

  public static class OrderSetItemTypeEnumFactory implements EnumFactory<OrderSetItemType> {
    public OrderSetItemType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return OrderSetItemType.CREATE;
        if ("update".equals(codeString))
          return OrderSetItemType.UPDATE;
        if ("remove".equals(codeString))
          return OrderSetItemType.REMOVE;
        if ("fire-event".equals(codeString))
          return OrderSetItemType.FIREEVENT;
        throw new IllegalArgumentException("Unknown OrderSetItemType code '"+codeString+"'");
        }
        public Enumeration<OrderSetItemType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("create".equals(codeString))
          return new Enumeration<OrderSetItemType>(this, OrderSetItemType.CREATE);
        if ("update".equals(codeString))
          return new Enumeration<OrderSetItemType>(this, OrderSetItemType.UPDATE);
        if ("remove".equals(codeString))
          return new Enumeration<OrderSetItemType>(this, OrderSetItemType.REMOVE);
        if ("fire-event".equals(codeString))
          return new Enumeration<OrderSetItemType>(this, OrderSetItemType.FIREEVENT);
        throw new FHIRException("Unknown OrderSetItemType code '"+codeString+"'");
        }
    public String toCode(OrderSetItemType code) {
      if (code == OrderSetItemType.CREATE)
        return "create";
      if (code == OrderSetItemType.UPDATE)
        return "update";
      if (code == OrderSetItemType.REMOVE)
        return "remove";
      if (code == OrderSetItemType.FIREEVENT)
        return "fire-event";
      return "?";
      }
    public String toSystem(OrderSetItemType code) {
      return code.getSystem();
      }
    }

    public enum OrderSetItemGroupingBehavior {
        /**
         * Any group marked with this behavior should be displayed as a visual group to the end user
         */
        VISUALGROUP, 
        /**
         * A group with this behavior logically groups its sub-elements, and may be shown as a visual group to the end user, but it is not required to do so
         */
        LOGICALGROUP, 
        /**
         * A group of related alternative items is a sentence group if the target referenced by the item is the same in all the items, and each item simply constitutes a different variation on how to specify the details for the target. For example, two items that could be in a SentenceGroup are "aspirin, 500 mg, 2 times per day" and "aspirin, 300 mg, 3 times per day". In both cases, aspirin is the target referenced by the item, and the two items represent two different options for how aspirin might be ordered for the patient. Note that a SentenceGroup would almost always have an associated selection behavior of "AtMostOne", unless it's a required item, in which case, it would be "ExactlyOne"
         */
        SENTENCEGROUP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OrderSetItemGroupingBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("visual-group".equals(codeString))
          return VISUALGROUP;
        if ("logical-group".equals(codeString))
          return LOGICALGROUP;
        if ("sentence-group".equals(codeString))
          return SENTENCEGROUP;
        throw new FHIRException("Unknown OrderSetItemGroupingBehavior code '"+codeString+"'");
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
            case VISUALGROUP: return "http://hl7.org/fhir/grouping-behavior";
            case LOGICALGROUP: return "http://hl7.org/fhir/grouping-behavior";
            case SENTENCEGROUP: return "http://hl7.org/fhir/grouping-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case VISUALGROUP: return "Any group marked with this behavior should be displayed as a visual group to the end user";
            case LOGICALGROUP: return "A group with this behavior logically groups its sub-elements, and may be shown as a visual group to the end user, but it is not required to do so";
            case SENTENCEGROUP: return "A group of related alternative items is a sentence group if the target referenced by the item is the same in all the items, and each item simply constitutes a different variation on how to specify the details for the target. For example, two items that could be in a SentenceGroup are \"aspirin, 500 mg, 2 times per day\" and \"aspirin, 300 mg, 3 times per day\". In both cases, aspirin is the target referenced by the item, and the two items represent two different options for how aspirin might be ordered for the patient. Note that a SentenceGroup would almost always have an associated selection behavior of \"AtMostOne\", unless it's a required item, in which case, it would be \"ExactlyOne\"";
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

  public static class OrderSetItemGroupingBehaviorEnumFactory implements EnumFactory<OrderSetItemGroupingBehavior> {
    public OrderSetItemGroupingBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("visual-group".equals(codeString))
          return OrderSetItemGroupingBehavior.VISUALGROUP;
        if ("logical-group".equals(codeString))
          return OrderSetItemGroupingBehavior.LOGICALGROUP;
        if ("sentence-group".equals(codeString))
          return OrderSetItemGroupingBehavior.SENTENCEGROUP;
        throw new IllegalArgumentException("Unknown OrderSetItemGroupingBehavior code '"+codeString+"'");
        }
        public Enumeration<OrderSetItemGroupingBehavior> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("visual-group".equals(codeString))
          return new Enumeration<OrderSetItemGroupingBehavior>(this, OrderSetItemGroupingBehavior.VISUALGROUP);
        if ("logical-group".equals(codeString))
          return new Enumeration<OrderSetItemGroupingBehavior>(this, OrderSetItemGroupingBehavior.LOGICALGROUP);
        if ("sentence-group".equals(codeString))
          return new Enumeration<OrderSetItemGroupingBehavior>(this, OrderSetItemGroupingBehavior.SENTENCEGROUP);
        throw new FHIRException("Unknown OrderSetItemGroupingBehavior code '"+codeString+"'");
        }
    public String toCode(OrderSetItemGroupingBehavior code) {
      if (code == OrderSetItemGroupingBehavior.VISUALGROUP)
        return "visual-group";
      if (code == OrderSetItemGroupingBehavior.LOGICALGROUP)
        return "logical-group";
      if (code == OrderSetItemGroupingBehavior.SENTENCEGROUP)
        return "sentence-group";
      return "?";
      }
    public String toSystem(OrderSetItemGroupingBehavior code) {
      return code.getSystem();
      }
    }

    public enum OrderSetItemSelectionBehavior {
        /**
         * Any number of the items in the group may be chosen, from zero to all
         */
        ANY, 
        /**
         * All the items in the group must be selected as a single unit
         */
        ALL, 
        /**
         * All the items in the group are meant to be chosen as a single unit: either all must be selected by the end user, or none may be selected
         */
        ALLORNONE, 
        /**
         * The end user must choose one and only one of the selectable items in the group. The user may not choose none of the items in the group
         */
        EXACTLYONE, 
        /**
         * The end user may choose zero or at most one of the items in the group
         */
        ATMOSTONE, 
        /**
         * The end user must choose a minimum of one, and as many additional as desired
         */
        ONEORMORE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OrderSetItemSelectionBehavior fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown OrderSetItemSelectionBehavior code '"+codeString+"'");
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
            case ANY: return "http://hl7.org/fhir/selection-behavior";
            case ALL: return "http://hl7.org/fhir/selection-behavior";
            case ALLORNONE: return "http://hl7.org/fhir/selection-behavior";
            case EXACTLYONE: return "http://hl7.org/fhir/selection-behavior";
            case ATMOSTONE: return "http://hl7.org/fhir/selection-behavior";
            case ONEORMORE: return "http://hl7.org/fhir/selection-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ANY: return "Any number of the items in the group may be chosen, from zero to all";
            case ALL: return "All the items in the group must be selected as a single unit";
            case ALLORNONE: return "All the items in the group are meant to be chosen as a single unit: either all must be selected by the end user, or none may be selected";
            case EXACTLYONE: return "The end user must choose one and only one of the selectable items in the group. The user may not choose none of the items in the group";
            case ATMOSTONE: return "The end user may choose zero or at most one of the items in the group";
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

  public static class OrderSetItemSelectionBehaviorEnumFactory implements EnumFactory<OrderSetItemSelectionBehavior> {
    public OrderSetItemSelectionBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("any".equals(codeString))
          return OrderSetItemSelectionBehavior.ANY;
        if ("all".equals(codeString))
          return OrderSetItemSelectionBehavior.ALL;
        if ("all-or-none".equals(codeString))
          return OrderSetItemSelectionBehavior.ALLORNONE;
        if ("exactly-one".equals(codeString))
          return OrderSetItemSelectionBehavior.EXACTLYONE;
        if ("at-most-one".equals(codeString))
          return OrderSetItemSelectionBehavior.ATMOSTONE;
        if ("one-or-more".equals(codeString))
          return OrderSetItemSelectionBehavior.ONEORMORE;
        throw new IllegalArgumentException("Unknown OrderSetItemSelectionBehavior code '"+codeString+"'");
        }
        public Enumeration<OrderSetItemSelectionBehavior> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("any".equals(codeString))
          return new Enumeration<OrderSetItemSelectionBehavior>(this, OrderSetItemSelectionBehavior.ANY);
        if ("all".equals(codeString))
          return new Enumeration<OrderSetItemSelectionBehavior>(this, OrderSetItemSelectionBehavior.ALL);
        if ("all-or-none".equals(codeString))
          return new Enumeration<OrderSetItemSelectionBehavior>(this, OrderSetItemSelectionBehavior.ALLORNONE);
        if ("exactly-one".equals(codeString))
          return new Enumeration<OrderSetItemSelectionBehavior>(this, OrderSetItemSelectionBehavior.EXACTLYONE);
        if ("at-most-one".equals(codeString))
          return new Enumeration<OrderSetItemSelectionBehavior>(this, OrderSetItemSelectionBehavior.ATMOSTONE);
        if ("one-or-more".equals(codeString))
          return new Enumeration<OrderSetItemSelectionBehavior>(this, OrderSetItemSelectionBehavior.ONEORMORE);
        throw new FHIRException("Unknown OrderSetItemSelectionBehavior code '"+codeString+"'");
        }
    public String toCode(OrderSetItemSelectionBehavior code) {
      if (code == OrderSetItemSelectionBehavior.ANY)
        return "any";
      if (code == OrderSetItemSelectionBehavior.ALL)
        return "all";
      if (code == OrderSetItemSelectionBehavior.ALLORNONE)
        return "all-or-none";
      if (code == OrderSetItemSelectionBehavior.EXACTLYONE)
        return "exactly-one";
      if (code == OrderSetItemSelectionBehavior.ATMOSTONE)
        return "at-most-one";
      if (code == OrderSetItemSelectionBehavior.ONEORMORE)
        return "one-or-more";
      return "?";
      }
    public String toSystem(OrderSetItemSelectionBehavior code) {
      return code.getSystem();
      }
    }

    public enum OrderSetItemRequiredBehavior {
        /**
         * An item with this behavior must be included in the items processed by the end user; the end user may not choose not to include this item
         */
        MUST, 
        /**
         * An item with this behavior may be included in the set of items processed by the end user
         */
        COULD, 
        /**
         * An item with this behavior must be included in the set of items processed by the end user, unless the end user provides documentation as to why the item was not included
         */
        MUSTUNLESSDOCUMENTED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OrderSetItemRequiredBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("must".equals(codeString))
          return MUST;
        if ("could".equals(codeString))
          return COULD;
        if ("must-unless-documented".equals(codeString))
          return MUSTUNLESSDOCUMENTED;
        throw new FHIRException("Unknown OrderSetItemRequiredBehavior code '"+codeString+"'");
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
            case MUST: return "http://hl7.org/fhir/required-behavior";
            case COULD: return "http://hl7.org/fhir/required-behavior";
            case MUSTUNLESSDOCUMENTED: return "http://hl7.org/fhir/required-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MUST: return "An item with this behavior must be included in the items processed by the end user; the end user may not choose not to include this item";
            case COULD: return "An item with this behavior may be included in the set of items processed by the end user";
            case MUSTUNLESSDOCUMENTED: return "An item with this behavior must be included in the set of items processed by the end user, unless the end user provides documentation as to why the item was not included";
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

  public static class OrderSetItemRequiredBehaviorEnumFactory implements EnumFactory<OrderSetItemRequiredBehavior> {
    public OrderSetItemRequiredBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("must".equals(codeString))
          return OrderSetItemRequiredBehavior.MUST;
        if ("could".equals(codeString))
          return OrderSetItemRequiredBehavior.COULD;
        if ("must-unless-documented".equals(codeString))
          return OrderSetItemRequiredBehavior.MUSTUNLESSDOCUMENTED;
        throw new IllegalArgumentException("Unknown OrderSetItemRequiredBehavior code '"+codeString+"'");
        }
        public Enumeration<OrderSetItemRequiredBehavior> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("must".equals(codeString))
          return new Enumeration<OrderSetItemRequiredBehavior>(this, OrderSetItemRequiredBehavior.MUST);
        if ("could".equals(codeString))
          return new Enumeration<OrderSetItemRequiredBehavior>(this, OrderSetItemRequiredBehavior.COULD);
        if ("must-unless-documented".equals(codeString))
          return new Enumeration<OrderSetItemRequiredBehavior>(this, OrderSetItemRequiredBehavior.MUSTUNLESSDOCUMENTED);
        throw new FHIRException("Unknown OrderSetItemRequiredBehavior code '"+codeString+"'");
        }
    public String toCode(OrderSetItemRequiredBehavior code) {
      if (code == OrderSetItemRequiredBehavior.MUST)
        return "must";
      if (code == OrderSetItemRequiredBehavior.COULD)
        return "could";
      if (code == OrderSetItemRequiredBehavior.MUSTUNLESSDOCUMENTED)
        return "must-unless-documented";
      return "?";
      }
    public String toSystem(OrderSetItemRequiredBehavior code) {
      return code.getSystem();
      }
    }

    public enum OrderSetItemPrecheckBehavior {
        /**
         * An item with this behavior is one of the most frequent items that is, or should be, included by an end user, for the particular context in which the item occurs. The system displaying the item to the end user should consider "pre-checking" such an item as a convenience for the user
         */
        YES, 
        /**
         * An item with this behavior is one of the less frequent items included by the end user, for the particular context in which the item occurs. The system displaying the items to the end user would typically not "pre-check" such an item
         */
        NO, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OrderSetItemPrecheckBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("yes".equals(codeString))
          return YES;
        if ("no".equals(codeString))
          return NO;
        throw new FHIRException("Unknown OrderSetItemPrecheckBehavior code '"+codeString+"'");
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
            case YES: return "http://hl7.org/fhir/precheck-behavior";
            case NO: return "http://hl7.org/fhir/precheck-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case YES: return "An item with this behavior is one of the most frequent items that is, or should be, included by an end user, for the particular context in which the item occurs. The system displaying the item to the end user should consider \"pre-checking\" such an item as a convenience for the user";
            case NO: return "An item with this behavior is one of the less frequent items included by the end user, for the particular context in which the item occurs. The system displaying the items to the end user would typically not \"pre-check\" such an item";
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

  public static class OrderSetItemPrecheckBehaviorEnumFactory implements EnumFactory<OrderSetItemPrecheckBehavior> {
    public OrderSetItemPrecheckBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("yes".equals(codeString))
          return OrderSetItemPrecheckBehavior.YES;
        if ("no".equals(codeString))
          return OrderSetItemPrecheckBehavior.NO;
        throw new IllegalArgumentException("Unknown OrderSetItemPrecheckBehavior code '"+codeString+"'");
        }
        public Enumeration<OrderSetItemPrecheckBehavior> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("yes".equals(codeString))
          return new Enumeration<OrderSetItemPrecheckBehavior>(this, OrderSetItemPrecheckBehavior.YES);
        if ("no".equals(codeString))
          return new Enumeration<OrderSetItemPrecheckBehavior>(this, OrderSetItemPrecheckBehavior.NO);
        throw new FHIRException("Unknown OrderSetItemPrecheckBehavior code '"+codeString+"'");
        }
    public String toCode(OrderSetItemPrecheckBehavior code) {
      if (code == OrderSetItemPrecheckBehavior.YES)
        return "yes";
      if (code == OrderSetItemPrecheckBehavior.NO)
        return "no";
      return "?";
      }
    public String toSystem(OrderSetItemPrecheckBehavior code) {
      return code.getSystem();
      }
    }

    public enum OrderSetItemCardinalityBehavior {
        /**
         * The item may only be selected one time
         */
        SINGLE, 
        /**
         * The item may be selected multiple times
         */
        MULTIPLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OrderSetItemCardinalityBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("single".equals(codeString))
          return SINGLE;
        if ("multiple".equals(codeString))
          return MULTIPLE;
        throw new FHIRException("Unknown OrderSetItemCardinalityBehavior code '"+codeString+"'");
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
            case SINGLE: return "http://hl7.org/fhir/cardinality-behavior";
            case MULTIPLE: return "http://hl7.org/fhir/cardinality-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SINGLE: return "The item may only be selected one time";
            case MULTIPLE: return "The item may be selected multiple times";
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

  public static class OrderSetItemCardinalityBehaviorEnumFactory implements EnumFactory<OrderSetItemCardinalityBehavior> {
    public OrderSetItemCardinalityBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("single".equals(codeString))
          return OrderSetItemCardinalityBehavior.SINGLE;
        if ("multiple".equals(codeString))
          return OrderSetItemCardinalityBehavior.MULTIPLE;
        throw new IllegalArgumentException("Unknown OrderSetItemCardinalityBehavior code '"+codeString+"'");
        }
        public Enumeration<OrderSetItemCardinalityBehavior> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("single".equals(codeString))
          return new Enumeration<OrderSetItemCardinalityBehavior>(this, OrderSetItemCardinalityBehavior.SINGLE);
        if ("multiple".equals(codeString))
          return new Enumeration<OrderSetItemCardinalityBehavior>(this, OrderSetItemCardinalityBehavior.MULTIPLE);
        throw new FHIRException("Unknown OrderSetItemCardinalityBehavior code '"+codeString+"'");
        }
    public String toCode(OrderSetItemCardinalityBehavior code) {
      if (code == OrderSetItemCardinalityBehavior.SINGLE)
        return "single";
      if (code == OrderSetItemCardinalityBehavior.MULTIPLE)
        return "multiple";
      return "?";
      }
    public String toSystem(OrderSetItemCardinalityBehavior code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class OrderSetItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A unique identifier for the item.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A unique identifier for the item." )
        protected Identifier identifier;

        /**
         * A user-visible number for the item.
         */
        @Child(name = "number", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A user-visible number for the item." )
        protected StringType number;

        /**
         * The title of the item.
         */
        @Child(name = "title", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The title of the item." )
        protected StringType title;

        /**
         * A short description of the item.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A short description of the item." )
        protected StringType description;

        /**
         * A text equivalent of the item in the orderset.
         */
        @Child(name = "textEquivalent", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A text equivalent of the item in the orderset." )
        protected StringType textEquivalent;

        /**
         * Supporting evidence for the item.
         */
        @Child(name = "supportingEvidence", type = {Attachment.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Supporting evidence for the item." )
        protected List<Attachment> supportingEvidence;

        /**
         * Supporting documentation for the  item.
         */
        @Child(name = "documentation", type = {Attachment.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Supporting documentation for the  item." )
        protected List<Attachment> documentation;

        /**
         * The type of participant in the item.
         */
        @Child(name = "participantType", type = {CodeType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="patient | person | practitioner | related-person", formalDefinition="The type of participant in the item." )
        protected List<Enumeration<OrderSetParticipantType>> participantType;

        /**
         * Concepts associated with the item.
         */
        @Child(name = "concept", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Concepts associated with the item." )
        protected List<CodeableConcept> concept;

        /**
         * The type of item (create, update, remove).
         */
        @Child(name = "type", type = {CodeType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="create | update | remove | fire-event", formalDefinition="The type of item (create, update, remove)." )
        protected Enumeration<OrderSetItemType> type;

        /**
         * Defines organization behavior of a group: gives the reason why the items are grouped together.
         */
        @Child(name = "groupingBehavior", type = {CodeType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="visual-group | logical-group | sentence-group", formalDefinition="Defines organization behavior of a group: gives the reason why the items are grouped together." )
        protected Enumeration<OrderSetItemGroupingBehavior> groupingBehavior;

        /**
         * Defines selection behavior of a group: specifies the number of selectable items in the group that may be selected by the end user when the items of the group are displayed.
         */
        @Child(name = "selectionBehavior", type = {CodeType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="any | all | all-or-none | exactly-one | at-most-one | one-or-more", formalDefinition="Defines selection behavior of a group: specifies the number of selectable items in the group that may be selected by the end user when the items of the group are displayed." )
        protected Enumeration<OrderSetItemSelectionBehavior> selectionBehavior;

        /**
         * Defines requiredness behavior for selecting an action or an action group; i.e., whether the action or action group is required or optional.
         */
        @Child(name = "requiredBehavior", type = {CodeType.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="must | could | must-unless-documented", formalDefinition="Defines requiredness behavior for selecting an action or an action group; i.e., whether the action or action group is required or optional." )
        protected Enumeration<OrderSetItemRequiredBehavior> requiredBehavior;

        /**
         * Defines selection frequency behavior for an action or group; i.e., for most frequently selected items, the end-user system may provide convenience options in the UI (such as pre-selection) in order to (1) communicate to the end user what the most frequently selected item is, or should, be in a particular context, and (2) save the end user time.
         */
        @Child(name = "precheckBehavior", type = {CodeType.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="yes | no", formalDefinition="Defines selection frequency behavior for an action or group; i.e., for most frequently selected items, the end-user system may provide convenience options in the UI (such as pre-selection) in order to (1) communicate to the end user what the most frequently selected item is, or should, be in a particular context, and (2) save the end user time." )
        protected Enumeration<OrderSetItemPrecheckBehavior> precheckBehavior;

        /**
         * Defines behavior for an action or a group for how many times that item may be repeated, i.e., cardinality. For example, if a user is documenting lesions, the lesion element may be repeated several times, once for each occurrence of a lesion on the patient or tissue sample or image.
         */
        @Child(name = "cardinalityBehavior", type = {CodeType.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="single | multiple", formalDefinition="Defines behavior for an action or a group for how many times that item may be repeated, i.e., cardinality. For example, if a user is documenting lesions, the lesion element may be repeated several times, once for each occurrence of a lesion on the patient or tissue sample or image." )
        protected Enumeration<OrderSetItemCardinalityBehavior> cardinalityBehavior;

        /**
         * The resource that is the target of the item (e.g. CommunicationRequest).
         */
        @Child(name = "resource", type = {}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The resource that is the target of the item (e.g. CommunicationRequest)." )
        protected Reference resource;

        /**
         * The actual object that is the target of the reference (The resource that is the target of the item (e.g. CommunicationRequest).)
         */
        protected Resource resourceTarget;

        /**
         * Customizations that should be applied to the statically defined resource.
         */
        @Child(name = "customization", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Customizations that should be applied to the statically defined resource." )
        protected List<OrderSetItemCustomizationComponent> customization;

        /**
         * Sub items for the orderable.
         */
        @Child(name = "items", type = {OrderSetItemComponent.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Sub items for the orderable." )
        protected List<OrderSetItemComponent> items;

        private static final long serialVersionUID = -357042086L;

    /**
     * Constructor
     */
      public OrderSetItemComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (A unique identifier for the item.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (A unique identifier for the item.)
         */
        public OrderSetItemComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #number} (A user-visible number for the item.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public StringType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new StringType(); // bb
          return this.number;
        }

        public boolean hasNumberElement() { 
          return this.number != null && !this.number.isEmpty();
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (A user-visible number for the item.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public OrderSetItemComponent setNumberElement(StringType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return A user-visible number for the item.
         */
        public String getNumber() { 
          return this.number == null ? null : this.number.getValue();
        }

        /**
         * @param value A user-visible number for the item.
         */
        public OrderSetItemComponent setNumber(String value) { 
          if (Utilities.noString(value))
            this.number = null;
          else {
            if (this.number == null)
              this.number = new StringType();
            this.number.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #title} (The title of the item.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.title");
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
         * @param value {@link #title} (The title of the item.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public OrderSetItemComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return The title of the item.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value The title of the item.
         */
        public OrderSetItemComponent setTitle(String value) { 
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
         * @return {@link #description} (A short description of the item.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.description");
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
         * @param value {@link #description} (A short description of the item.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public OrderSetItemComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A short description of the item.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A short description of the item.
         */
        public OrderSetItemComponent setDescription(String value) { 
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
         * @return {@link #textEquivalent} (A text equivalent of the item in the orderset.). This is the underlying object with id, value and extensions. The accessor "getTextEquivalent" gives direct access to the value
         */
        public StringType getTextEquivalentElement() { 
          if (this.textEquivalent == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.textEquivalent");
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
         * @param value {@link #textEquivalent} (A text equivalent of the item in the orderset.). This is the underlying object with id, value and extensions. The accessor "getTextEquivalent" gives direct access to the value
         */
        public OrderSetItemComponent setTextEquivalentElement(StringType value) { 
          this.textEquivalent = value;
          return this;
        }

        /**
         * @return A text equivalent of the item in the orderset.
         */
        public String getTextEquivalent() { 
          return this.textEquivalent == null ? null : this.textEquivalent.getValue();
        }

        /**
         * @param value A text equivalent of the item in the orderset.
         */
        public OrderSetItemComponent setTextEquivalent(String value) { 
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
         * @return {@link #supportingEvidence} (Supporting evidence for the item.)
         */
        public List<Attachment> getSupportingEvidence() { 
          if (this.supportingEvidence == null)
            this.supportingEvidence = new ArrayList<Attachment>();
          return this.supportingEvidence;
        }

        public boolean hasSupportingEvidence() { 
          if (this.supportingEvidence == null)
            return false;
          for (Attachment item : this.supportingEvidence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #supportingEvidence} (Supporting evidence for the item.)
         */
    // syntactic sugar
        public Attachment addSupportingEvidence() { //3
          Attachment t = new Attachment();
          if (this.supportingEvidence == null)
            this.supportingEvidence = new ArrayList<Attachment>();
          this.supportingEvidence.add(t);
          return t;
        }

    // syntactic sugar
        public OrderSetItemComponent addSupportingEvidence(Attachment t) { //3
          if (t == null)
            return this;
          if (this.supportingEvidence == null)
            this.supportingEvidence = new ArrayList<Attachment>();
          this.supportingEvidence.add(t);
          return this;
        }

        /**
         * @return {@link #documentation} (Supporting documentation for the  item.)
         */
        public List<Attachment> getDocumentation() { 
          if (this.documentation == null)
            this.documentation = new ArrayList<Attachment>();
          return this.documentation;
        }

        public boolean hasDocumentation() { 
          if (this.documentation == null)
            return false;
          for (Attachment item : this.documentation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #documentation} (Supporting documentation for the  item.)
         */
    // syntactic sugar
        public Attachment addDocumentation() { //3
          Attachment t = new Attachment();
          if (this.documentation == null)
            this.documentation = new ArrayList<Attachment>();
          this.documentation.add(t);
          return t;
        }

    // syntactic sugar
        public OrderSetItemComponent addDocumentation(Attachment t) { //3
          if (t == null)
            return this;
          if (this.documentation == null)
            this.documentation = new ArrayList<Attachment>();
          this.documentation.add(t);
          return this;
        }

        /**
         * @return {@link #participantType} (The type of participant in the item.)
         */
        public List<Enumeration<OrderSetParticipantType>> getParticipantType() { 
          if (this.participantType == null)
            this.participantType = new ArrayList<Enumeration<OrderSetParticipantType>>();
          return this.participantType;
        }

        public boolean hasParticipantType() { 
          if (this.participantType == null)
            return false;
          for (Enumeration<OrderSetParticipantType> item : this.participantType)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #participantType} (The type of participant in the item.)
         */
    // syntactic sugar
        public Enumeration<OrderSetParticipantType> addParticipantTypeElement() {//2 
          Enumeration<OrderSetParticipantType> t = new Enumeration<OrderSetParticipantType>(new OrderSetParticipantTypeEnumFactory());
          if (this.participantType == null)
            this.participantType = new ArrayList<Enumeration<OrderSetParticipantType>>();
          this.participantType.add(t);
          return t;
        }

        /**
         * @param value {@link #participantType} (The type of participant in the item.)
         */
        public OrderSetItemComponent addParticipantType(OrderSetParticipantType value) { //1
          Enumeration<OrderSetParticipantType> t = new Enumeration<OrderSetParticipantType>(new OrderSetParticipantTypeEnumFactory());
          t.setValue(value);
          if (this.participantType == null)
            this.participantType = new ArrayList<Enumeration<OrderSetParticipantType>>();
          this.participantType.add(t);
          return this;
        }

        /**
         * @param value {@link #participantType} (The type of participant in the item.)
         */
        public boolean hasParticipantType(OrderSetParticipantType value) { 
          if (this.participantType == null)
            return false;
          for (Enumeration<OrderSetParticipantType> v : this.participantType)
            if (v.equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #concept} (Concepts associated with the item.)
         */
        public List<CodeableConcept> getConcept() { 
          if (this.concept == null)
            this.concept = new ArrayList<CodeableConcept>();
          return this.concept;
        }

        public boolean hasConcept() { 
          if (this.concept == null)
            return false;
          for (CodeableConcept item : this.concept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #concept} (Concepts associated with the item.)
         */
    // syntactic sugar
        public CodeableConcept addConcept() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.concept == null)
            this.concept = new ArrayList<CodeableConcept>();
          this.concept.add(t);
          return t;
        }

    // syntactic sugar
        public OrderSetItemComponent addConcept(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<CodeableConcept>();
          this.concept.add(t);
          return this;
        }

        /**
         * @return {@link #type} (The type of item (create, update, remove).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<OrderSetItemType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<OrderSetItemType>(new OrderSetItemTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of item (create, update, remove).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public OrderSetItemComponent setTypeElement(Enumeration<OrderSetItemType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of item (create, update, remove).
         */
        public OrderSetItemType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of item (create, update, remove).
         */
        public OrderSetItemComponent setType(OrderSetItemType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<OrderSetItemType>(new OrderSetItemTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #groupingBehavior} (Defines organization behavior of a group: gives the reason why the items are grouped together.). This is the underlying object with id, value and extensions. The accessor "getGroupingBehavior" gives direct access to the value
         */
        public Enumeration<OrderSetItemGroupingBehavior> getGroupingBehaviorElement() { 
          if (this.groupingBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.groupingBehavior");
            else if (Configuration.doAutoCreate())
              this.groupingBehavior = new Enumeration<OrderSetItemGroupingBehavior>(new OrderSetItemGroupingBehaviorEnumFactory()); // bb
          return this.groupingBehavior;
        }

        public boolean hasGroupingBehaviorElement() { 
          return this.groupingBehavior != null && !this.groupingBehavior.isEmpty();
        }

        public boolean hasGroupingBehavior() { 
          return this.groupingBehavior != null && !this.groupingBehavior.isEmpty();
        }

        /**
         * @param value {@link #groupingBehavior} (Defines organization behavior of a group: gives the reason why the items are grouped together.). This is the underlying object with id, value and extensions. The accessor "getGroupingBehavior" gives direct access to the value
         */
        public OrderSetItemComponent setGroupingBehaviorElement(Enumeration<OrderSetItemGroupingBehavior> value) { 
          this.groupingBehavior = value;
          return this;
        }

        /**
         * @return Defines organization behavior of a group: gives the reason why the items are grouped together.
         */
        public OrderSetItemGroupingBehavior getGroupingBehavior() { 
          return this.groupingBehavior == null ? null : this.groupingBehavior.getValue();
        }

        /**
         * @param value Defines organization behavior of a group: gives the reason why the items are grouped together.
         */
        public OrderSetItemComponent setGroupingBehavior(OrderSetItemGroupingBehavior value) { 
          if (value == null)
            this.groupingBehavior = null;
          else {
            if (this.groupingBehavior == null)
              this.groupingBehavior = new Enumeration<OrderSetItemGroupingBehavior>(new OrderSetItemGroupingBehaviorEnumFactory());
            this.groupingBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #selectionBehavior} (Defines selection behavior of a group: specifies the number of selectable items in the group that may be selected by the end user when the items of the group are displayed.). This is the underlying object with id, value and extensions. The accessor "getSelectionBehavior" gives direct access to the value
         */
        public Enumeration<OrderSetItemSelectionBehavior> getSelectionBehaviorElement() { 
          if (this.selectionBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.selectionBehavior");
            else if (Configuration.doAutoCreate())
              this.selectionBehavior = new Enumeration<OrderSetItemSelectionBehavior>(new OrderSetItemSelectionBehaviorEnumFactory()); // bb
          return this.selectionBehavior;
        }

        public boolean hasSelectionBehaviorElement() { 
          return this.selectionBehavior != null && !this.selectionBehavior.isEmpty();
        }

        public boolean hasSelectionBehavior() { 
          return this.selectionBehavior != null && !this.selectionBehavior.isEmpty();
        }

        /**
         * @param value {@link #selectionBehavior} (Defines selection behavior of a group: specifies the number of selectable items in the group that may be selected by the end user when the items of the group are displayed.). This is the underlying object with id, value and extensions. The accessor "getSelectionBehavior" gives direct access to the value
         */
        public OrderSetItemComponent setSelectionBehaviorElement(Enumeration<OrderSetItemSelectionBehavior> value) { 
          this.selectionBehavior = value;
          return this;
        }

        /**
         * @return Defines selection behavior of a group: specifies the number of selectable items in the group that may be selected by the end user when the items of the group are displayed.
         */
        public OrderSetItemSelectionBehavior getSelectionBehavior() { 
          return this.selectionBehavior == null ? null : this.selectionBehavior.getValue();
        }

        /**
         * @param value Defines selection behavior of a group: specifies the number of selectable items in the group that may be selected by the end user when the items of the group are displayed.
         */
        public OrderSetItemComponent setSelectionBehavior(OrderSetItemSelectionBehavior value) { 
          if (value == null)
            this.selectionBehavior = null;
          else {
            if (this.selectionBehavior == null)
              this.selectionBehavior = new Enumeration<OrderSetItemSelectionBehavior>(new OrderSetItemSelectionBehaviorEnumFactory());
            this.selectionBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #requiredBehavior} (Defines requiredness behavior for selecting an action or an action group; i.e., whether the action or action group is required or optional.). This is the underlying object with id, value and extensions. The accessor "getRequiredBehavior" gives direct access to the value
         */
        public Enumeration<OrderSetItemRequiredBehavior> getRequiredBehaviorElement() { 
          if (this.requiredBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.requiredBehavior");
            else if (Configuration.doAutoCreate())
              this.requiredBehavior = new Enumeration<OrderSetItemRequiredBehavior>(new OrderSetItemRequiredBehaviorEnumFactory()); // bb
          return this.requiredBehavior;
        }

        public boolean hasRequiredBehaviorElement() { 
          return this.requiredBehavior != null && !this.requiredBehavior.isEmpty();
        }

        public boolean hasRequiredBehavior() { 
          return this.requiredBehavior != null && !this.requiredBehavior.isEmpty();
        }

        /**
         * @param value {@link #requiredBehavior} (Defines requiredness behavior for selecting an action or an action group; i.e., whether the action or action group is required or optional.). This is the underlying object with id, value and extensions. The accessor "getRequiredBehavior" gives direct access to the value
         */
        public OrderSetItemComponent setRequiredBehaviorElement(Enumeration<OrderSetItemRequiredBehavior> value) { 
          this.requiredBehavior = value;
          return this;
        }

        /**
         * @return Defines requiredness behavior for selecting an action or an action group; i.e., whether the action or action group is required or optional.
         */
        public OrderSetItemRequiredBehavior getRequiredBehavior() { 
          return this.requiredBehavior == null ? null : this.requiredBehavior.getValue();
        }

        /**
         * @param value Defines requiredness behavior for selecting an action or an action group; i.e., whether the action or action group is required or optional.
         */
        public OrderSetItemComponent setRequiredBehavior(OrderSetItemRequiredBehavior value) { 
          if (value == null)
            this.requiredBehavior = null;
          else {
            if (this.requiredBehavior == null)
              this.requiredBehavior = new Enumeration<OrderSetItemRequiredBehavior>(new OrderSetItemRequiredBehaviorEnumFactory());
            this.requiredBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #precheckBehavior} (Defines selection frequency behavior for an action or group; i.e., for most frequently selected items, the end-user system may provide convenience options in the UI (such as pre-selection) in order to (1) communicate to the end user what the most frequently selected item is, or should, be in a particular context, and (2) save the end user time.). This is the underlying object with id, value and extensions. The accessor "getPrecheckBehavior" gives direct access to the value
         */
        public Enumeration<OrderSetItemPrecheckBehavior> getPrecheckBehaviorElement() { 
          if (this.precheckBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.precheckBehavior");
            else if (Configuration.doAutoCreate())
              this.precheckBehavior = new Enumeration<OrderSetItemPrecheckBehavior>(new OrderSetItemPrecheckBehaviorEnumFactory()); // bb
          return this.precheckBehavior;
        }

        public boolean hasPrecheckBehaviorElement() { 
          return this.precheckBehavior != null && !this.precheckBehavior.isEmpty();
        }

        public boolean hasPrecheckBehavior() { 
          return this.precheckBehavior != null && !this.precheckBehavior.isEmpty();
        }

        /**
         * @param value {@link #precheckBehavior} (Defines selection frequency behavior for an action or group; i.e., for most frequently selected items, the end-user system may provide convenience options in the UI (such as pre-selection) in order to (1) communicate to the end user what the most frequently selected item is, or should, be in a particular context, and (2) save the end user time.). This is the underlying object with id, value and extensions. The accessor "getPrecheckBehavior" gives direct access to the value
         */
        public OrderSetItemComponent setPrecheckBehaviorElement(Enumeration<OrderSetItemPrecheckBehavior> value) { 
          this.precheckBehavior = value;
          return this;
        }

        /**
         * @return Defines selection frequency behavior for an action or group; i.e., for most frequently selected items, the end-user system may provide convenience options in the UI (such as pre-selection) in order to (1) communicate to the end user what the most frequently selected item is, or should, be in a particular context, and (2) save the end user time.
         */
        public OrderSetItemPrecheckBehavior getPrecheckBehavior() { 
          return this.precheckBehavior == null ? null : this.precheckBehavior.getValue();
        }

        /**
         * @param value Defines selection frequency behavior for an action or group; i.e., for most frequently selected items, the end-user system may provide convenience options in the UI (such as pre-selection) in order to (1) communicate to the end user what the most frequently selected item is, or should, be in a particular context, and (2) save the end user time.
         */
        public OrderSetItemComponent setPrecheckBehavior(OrderSetItemPrecheckBehavior value) { 
          if (value == null)
            this.precheckBehavior = null;
          else {
            if (this.precheckBehavior == null)
              this.precheckBehavior = new Enumeration<OrderSetItemPrecheckBehavior>(new OrderSetItemPrecheckBehaviorEnumFactory());
            this.precheckBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #cardinalityBehavior} (Defines behavior for an action or a group for how many times that item may be repeated, i.e., cardinality. For example, if a user is documenting lesions, the lesion element may be repeated several times, once for each occurrence of a lesion on the patient or tissue sample or image.). This is the underlying object with id, value and extensions. The accessor "getCardinalityBehavior" gives direct access to the value
         */
        public Enumeration<OrderSetItemCardinalityBehavior> getCardinalityBehaviorElement() { 
          if (this.cardinalityBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.cardinalityBehavior");
            else if (Configuration.doAutoCreate())
              this.cardinalityBehavior = new Enumeration<OrderSetItemCardinalityBehavior>(new OrderSetItemCardinalityBehaviorEnumFactory()); // bb
          return this.cardinalityBehavior;
        }

        public boolean hasCardinalityBehaviorElement() { 
          return this.cardinalityBehavior != null && !this.cardinalityBehavior.isEmpty();
        }

        public boolean hasCardinalityBehavior() { 
          return this.cardinalityBehavior != null && !this.cardinalityBehavior.isEmpty();
        }

        /**
         * @param value {@link #cardinalityBehavior} (Defines behavior for an action or a group for how many times that item may be repeated, i.e., cardinality. For example, if a user is documenting lesions, the lesion element may be repeated several times, once for each occurrence of a lesion on the patient or tissue sample or image.). This is the underlying object with id, value and extensions. The accessor "getCardinalityBehavior" gives direct access to the value
         */
        public OrderSetItemComponent setCardinalityBehaviorElement(Enumeration<OrderSetItemCardinalityBehavior> value) { 
          this.cardinalityBehavior = value;
          return this;
        }

        /**
         * @return Defines behavior for an action or a group for how many times that item may be repeated, i.e., cardinality. For example, if a user is documenting lesions, the lesion element may be repeated several times, once for each occurrence of a lesion on the patient or tissue sample or image.
         */
        public OrderSetItemCardinalityBehavior getCardinalityBehavior() { 
          return this.cardinalityBehavior == null ? null : this.cardinalityBehavior.getValue();
        }

        /**
         * @param value Defines behavior for an action or a group for how many times that item may be repeated, i.e., cardinality. For example, if a user is documenting lesions, the lesion element may be repeated several times, once for each occurrence of a lesion on the patient or tissue sample or image.
         */
        public OrderSetItemComponent setCardinalityBehavior(OrderSetItemCardinalityBehavior value) { 
          if (value == null)
            this.cardinalityBehavior = null;
          else {
            if (this.cardinalityBehavior == null)
              this.cardinalityBehavior = new Enumeration<OrderSetItemCardinalityBehavior>(new OrderSetItemCardinalityBehaviorEnumFactory());
            this.cardinalityBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #resource} (The resource that is the target of the item (e.g. CommunicationRequest).)
         */
        public Reference getResource() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemComponent.resource");
            else if (Configuration.doAutoCreate())
              this.resource = new Reference(); // cc
          return this.resource;
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (The resource that is the target of the item (e.g. CommunicationRequest).)
         */
        public OrderSetItemComponent setResource(Reference value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return {@link #resource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource that is the target of the item (e.g. CommunicationRequest).)
         */
        public Resource getResourceTarget() { 
          return this.resourceTarget;
        }

        /**
         * @param value {@link #resource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource that is the target of the item (e.g. CommunicationRequest).)
         */
        public OrderSetItemComponent setResourceTarget(Resource value) { 
          this.resourceTarget = value;
          return this;
        }

        /**
         * @return {@link #customization} (Customizations that should be applied to the statically defined resource.)
         */
        public List<OrderSetItemCustomizationComponent> getCustomization() { 
          if (this.customization == null)
            this.customization = new ArrayList<OrderSetItemCustomizationComponent>();
          return this.customization;
        }

        public boolean hasCustomization() { 
          if (this.customization == null)
            return false;
          for (OrderSetItemCustomizationComponent item : this.customization)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #customization} (Customizations that should be applied to the statically defined resource.)
         */
    // syntactic sugar
        public OrderSetItemCustomizationComponent addCustomization() { //3
          OrderSetItemCustomizationComponent t = new OrderSetItemCustomizationComponent();
          if (this.customization == null)
            this.customization = new ArrayList<OrderSetItemCustomizationComponent>();
          this.customization.add(t);
          return t;
        }

    // syntactic sugar
        public OrderSetItemComponent addCustomization(OrderSetItemCustomizationComponent t) { //3
          if (t == null)
            return this;
          if (this.customization == null)
            this.customization = new ArrayList<OrderSetItemCustomizationComponent>();
          this.customization.add(t);
          return this;
        }

        /**
         * @return {@link #items} (Sub items for the orderable.)
         */
        public List<OrderSetItemComponent> getItems() { 
          if (this.items == null)
            this.items = new ArrayList<OrderSetItemComponent>();
          return this.items;
        }

        public boolean hasItems() { 
          if (this.items == null)
            return false;
          for (OrderSetItemComponent item : this.items)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #items} (Sub items for the orderable.)
         */
    // syntactic sugar
        public OrderSetItemComponent addItems() { //3
          OrderSetItemComponent t = new OrderSetItemComponent();
          if (this.items == null)
            this.items = new ArrayList<OrderSetItemComponent>();
          this.items.add(t);
          return t;
        }

    // syntactic sugar
        public OrderSetItemComponent addItems(OrderSetItemComponent t) { //3
          if (t == null)
            return this;
          if (this.items == null)
            this.items = new ArrayList<OrderSetItemComponent>();
          this.items.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "A unique identifier for the item.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("number", "string", "A user-visible number for the item.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("title", "string", "The title of the item.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("description", "string", "A short description of the item.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("textEquivalent", "string", "A text equivalent of the item in the orderset.", 0, java.lang.Integer.MAX_VALUE, textEquivalent));
          childrenList.add(new Property("supportingEvidence", "Attachment", "Supporting evidence for the item.", 0, java.lang.Integer.MAX_VALUE, supportingEvidence));
          childrenList.add(new Property("documentation", "Attachment", "Supporting documentation for the  item.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("participantType", "code", "The type of participant in the item.", 0, java.lang.Integer.MAX_VALUE, participantType));
          childrenList.add(new Property("concept", "CodeableConcept", "Concepts associated with the item.", 0, java.lang.Integer.MAX_VALUE, concept));
          childrenList.add(new Property("type", "code", "The type of item (create, update, remove).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("groupingBehavior", "code", "Defines organization behavior of a group: gives the reason why the items are grouped together.", 0, java.lang.Integer.MAX_VALUE, groupingBehavior));
          childrenList.add(new Property("selectionBehavior", "code", "Defines selection behavior of a group: specifies the number of selectable items in the group that may be selected by the end user when the items of the group are displayed.", 0, java.lang.Integer.MAX_VALUE, selectionBehavior));
          childrenList.add(new Property("requiredBehavior", "code", "Defines requiredness behavior for selecting an action or an action group; i.e., whether the action or action group is required or optional.", 0, java.lang.Integer.MAX_VALUE, requiredBehavior));
          childrenList.add(new Property("precheckBehavior", "code", "Defines selection frequency behavior for an action or group; i.e., for most frequently selected items, the end-user system may provide convenience options in the UI (such as pre-selection) in order to (1) communicate to the end user what the most frequently selected item is, or should, be in a particular context, and (2) save the end user time.", 0, java.lang.Integer.MAX_VALUE, precheckBehavior));
          childrenList.add(new Property("cardinalityBehavior", "code", "Defines behavior for an action or a group for how many times that item may be repeated, i.e., cardinality. For example, if a user is documenting lesions, the lesion element may be repeated several times, once for each occurrence of a lesion on the patient or tissue sample or image.", 0, java.lang.Integer.MAX_VALUE, cardinalityBehavior));
          childrenList.add(new Property("resource", "Reference(Any)", "The resource that is the target of the item (e.g. CommunicationRequest).", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("customization", "", "Customizations that should be applied to the statically defined resource.", 0, java.lang.Integer.MAX_VALUE, customization));
          childrenList.add(new Property("items", "@OrderSet.item", "Sub items for the orderable.", 0, java.lang.Integer.MAX_VALUE, items));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("number"))
          this.number = castToString(value); // StringType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("textEquivalent"))
          this.textEquivalent = castToString(value); // StringType
        else if (name.equals("supportingEvidence"))
          this.getSupportingEvidence().add(castToAttachment(value));
        else if (name.equals("documentation"))
          this.getDocumentation().add(castToAttachment(value));
        else if (name.equals("participantType"))
          this.getParticipantType().add(new OrderSetParticipantTypeEnumFactory().fromType(value));
        else if (name.equals("concept"))
          this.getConcept().add(castToCodeableConcept(value));
        else if (name.equals("type"))
          this.type = new OrderSetItemTypeEnumFactory().fromType(value); // Enumeration<OrderSetItemType>
        else if (name.equals("groupingBehavior"))
          this.groupingBehavior = new OrderSetItemGroupingBehaviorEnumFactory().fromType(value); // Enumeration<OrderSetItemGroupingBehavior>
        else if (name.equals("selectionBehavior"))
          this.selectionBehavior = new OrderSetItemSelectionBehaviorEnumFactory().fromType(value); // Enumeration<OrderSetItemSelectionBehavior>
        else if (name.equals("requiredBehavior"))
          this.requiredBehavior = new OrderSetItemRequiredBehaviorEnumFactory().fromType(value); // Enumeration<OrderSetItemRequiredBehavior>
        else if (name.equals("precheckBehavior"))
          this.precheckBehavior = new OrderSetItemPrecheckBehaviorEnumFactory().fromType(value); // Enumeration<OrderSetItemPrecheckBehavior>
        else if (name.equals("cardinalityBehavior"))
          this.cardinalityBehavior = new OrderSetItemCardinalityBehaviorEnumFactory().fromType(value); // Enumeration<OrderSetItemCardinalityBehavior>
        else if (name.equals("resource"))
          this.resource = castToReference(value); // Reference
        else if (name.equals("customization"))
          this.getCustomization().add((OrderSetItemCustomizationComponent) value);
        else if (name.equals("items"))
          this.getItems().add((OrderSetItemComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("number")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.number");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.title");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.description");
        }
        else if (name.equals("textEquivalent")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.textEquivalent");
        }
        else if (name.equals("supportingEvidence")) {
          return addSupportingEvidence();
        }
        else if (name.equals("documentation")) {
          return addDocumentation();
        }
        else if (name.equals("participantType")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.participantType");
        }
        else if (name.equals("concept")) {
          return addConcept();
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.type");
        }
        else if (name.equals("groupingBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.groupingBehavior");
        }
        else if (name.equals("selectionBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.selectionBehavior");
        }
        else if (name.equals("requiredBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.requiredBehavior");
        }
        else if (name.equals("precheckBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.precheckBehavior");
        }
        else if (name.equals("cardinalityBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.cardinalityBehavior");
        }
        else if (name.equals("resource")) {
          this.resource = new Reference();
          return this.resource;
        }
        else if (name.equals("customization")) {
          return addCustomization();
        }
        else if (name.equals("items")) {
          return addItems();
        }
        else
          return super.addChild(name);
      }

      public OrderSetItemComponent copy() {
        OrderSetItemComponent dst = new OrderSetItemComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.number = number == null ? null : number.copy();
        dst.title = title == null ? null : title.copy();
        dst.description = description == null ? null : description.copy();
        dst.textEquivalent = textEquivalent == null ? null : textEquivalent.copy();
        if (supportingEvidence != null) {
          dst.supportingEvidence = new ArrayList<Attachment>();
          for (Attachment i : supportingEvidence)
            dst.supportingEvidence.add(i.copy());
        };
        if (documentation != null) {
          dst.documentation = new ArrayList<Attachment>();
          for (Attachment i : documentation)
            dst.documentation.add(i.copy());
        };
        if (participantType != null) {
          dst.participantType = new ArrayList<Enumeration<OrderSetParticipantType>>();
          for (Enumeration<OrderSetParticipantType> i : participantType)
            dst.participantType.add(i.copy());
        };
        if (concept != null) {
          dst.concept = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : concept)
            dst.concept.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.groupingBehavior = groupingBehavior == null ? null : groupingBehavior.copy();
        dst.selectionBehavior = selectionBehavior == null ? null : selectionBehavior.copy();
        dst.requiredBehavior = requiredBehavior == null ? null : requiredBehavior.copy();
        dst.precheckBehavior = precheckBehavior == null ? null : precheckBehavior.copy();
        dst.cardinalityBehavior = cardinalityBehavior == null ? null : cardinalityBehavior.copy();
        dst.resource = resource == null ? null : resource.copy();
        if (customization != null) {
          dst.customization = new ArrayList<OrderSetItemCustomizationComponent>();
          for (OrderSetItemCustomizationComponent i : customization)
            dst.customization.add(i.copy());
        };
        if (items != null) {
          dst.items = new ArrayList<OrderSetItemComponent>();
          for (OrderSetItemComponent i : items)
            dst.items.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OrderSetItemComponent))
          return false;
        OrderSetItemComponent o = (OrderSetItemComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(number, o.number, true) && compareDeep(title, o.title, true)
           && compareDeep(description, o.description, true) && compareDeep(textEquivalent, o.textEquivalent, true)
           && compareDeep(supportingEvidence, o.supportingEvidence, true) && compareDeep(documentation, o.documentation, true)
           && compareDeep(participantType, o.participantType, true) && compareDeep(concept, o.concept, true)
           && compareDeep(type, o.type, true) && compareDeep(groupingBehavior, o.groupingBehavior, true) && compareDeep(selectionBehavior, o.selectionBehavior, true)
           && compareDeep(requiredBehavior, o.requiredBehavior, true) && compareDeep(precheckBehavior, o.precheckBehavior, true)
           && compareDeep(cardinalityBehavior, o.cardinalityBehavior, true) && compareDeep(resource, o.resource, true)
           && compareDeep(customization, o.customization, true) && compareDeep(items, o.items, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OrderSetItemComponent))
          return false;
        OrderSetItemComponent o = (OrderSetItemComponent) other;
        return compareValues(number, o.number, true) && compareValues(title, o.title, true) && compareValues(description, o.description, true)
           && compareValues(textEquivalent, o.textEquivalent, true) && compareValues(participantType, o.participantType, true)
           && compareValues(type, o.type, true) && compareValues(groupingBehavior, o.groupingBehavior, true) && compareValues(selectionBehavior, o.selectionBehavior, true)
           && compareValues(requiredBehavior, o.requiredBehavior, true) && compareValues(precheckBehavior, o.precheckBehavior, true)
           && compareValues(cardinalityBehavior, o.cardinalityBehavior, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (number == null || number.isEmpty())
           && (title == null || title.isEmpty()) && (description == null || description.isEmpty()) && (textEquivalent == null || textEquivalent.isEmpty())
           && (supportingEvidence == null || supportingEvidence.isEmpty()) && (documentation == null || documentation.isEmpty())
           && (participantType == null || participantType.isEmpty()) && (concept == null || concept.isEmpty())
           && (type == null || type.isEmpty()) && (groupingBehavior == null || groupingBehavior.isEmpty())
           && (selectionBehavior == null || selectionBehavior.isEmpty()) && (requiredBehavior == null || requiredBehavior.isEmpty())
           && (precheckBehavior == null || precheckBehavior.isEmpty()) && (cardinalityBehavior == null || cardinalityBehavior.isEmpty())
           && (resource == null || resource.isEmpty()) && (customization == null || customization.isEmpty())
           && (items == null || items.isEmpty());
      }

  public String fhirType() {
    return "OrderSet.item";

  }

  }

    @Block()
    public static class OrderSetItemCustomizationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The path to the element to be customized.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The path to the element to be customized." )
        protected StringType path;

        /**
         * An expression specifying the value of the customized element.
         */
        @Child(name = "expression", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="An expression specifying the value of the customized element." )
        protected StringType expression;

        private static final long serialVersionUID = -252690483L;

    /**
     * Constructor
     */
      public OrderSetItemCustomizationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public OrderSetItemCustomizationComponent(StringType path, StringType expression) {
        super();
        this.path = path;
        this.expression = expression;
      }

        /**
         * @return {@link #path} (The path to the element to be customized.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemCustomizationComponent.path");
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
         * @param value {@link #path} (The path to the element to be customized.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public OrderSetItemCustomizationComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The path to the element to be customized.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The path to the element to be customized.
         */
        public OrderSetItemCustomizationComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        /**
         * @return {@link #expression} (An expression specifying the value of the customized element.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderSetItemCustomizationComponent.expression");
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
        public OrderSetItemCustomizationComponent setExpressionElement(StringType value) { 
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
        public OrderSetItemCustomizationComponent setExpression(String value) { 
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("path", "string", "The path to the element to be customized.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("expression", "string", "An expression specifying the value of the customized element.", 0, java.lang.Integer.MAX_VALUE, expression));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path"))
          this.path = castToString(value); // StringType
        else if (name.equals("expression"))
          this.expression = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.path");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.expression");
        }
        else
          return super.addChild(name);
      }

      public OrderSetItemCustomizationComponent copy() {
        OrderSetItemCustomizationComponent dst = new OrderSetItemCustomizationComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OrderSetItemCustomizationComponent))
          return false;
        OrderSetItemCustomizationComponent o = (OrderSetItemCustomizationComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OrderSetItemCustomizationComponent))
          return false;
        OrderSetItemCustomizationComponent o = (OrderSetItemCustomizationComponent) other;
        return compareValues(path, o.path, true) && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (path == null || path.isEmpty()) && (expression == null || expression.isEmpty())
          ;
      }

  public String fhirType() {
    return "OrderSet.item.customization";

  }

  }

    /**
     * A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier", formalDefinition="A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact." )
    protected List<Identifier> identifier;

    /**
     * The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The version of the module, if any", formalDefinition="The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification." )
    protected StringType version;

    /**
     * A reference to a ModuleMetadata resource containing metadata for the orderset.
     */
    @Child(name = "moduleMetadata", type = {ModuleMetadata.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The metadata for the orderset", formalDefinition="A reference to a ModuleMetadata resource containing metadata for the orderset." )
    protected Reference moduleMetadata;

    /**
     * The actual object that is the target of the reference (A reference to a ModuleMetadata resource containing metadata for the orderset.)
     */
    protected ModuleMetadata moduleMetadataTarget;

    /**
     * A reference to a Library resource containing any formal logic used by the orderset.
     */
    @Child(name = "library", type = {Library.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Logic used by the orderset", formalDefinition="A reference to a Library resource containing any formal logic used by the orderset." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library resource containing any formal logic used by the orderset.)
     */
    protected List<Library> libraryTarget;


    /**
     * The definition of the items that make up the orderset.
     */
    @Child(name = "item", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The definition of the items that make up the orderset." )
    protected List<OrderSetItemComponent> item;

    private static final long serialVersionUID = -1392358630L;

  /**
   * Constructor
   */
    public OrderSet() {
      super();
    }

    /**
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
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
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
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
    public OrderSet addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OrderSet.version");
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
     * @param value {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public OrderSet setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    public OrderSet setVersion(String value) { 
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
     * @return {@link #moduleMetadata} (A reference to a ModuleMetadata resource containing metadata for the orderset.)
     */
    public Reference getModuleMetadata() { 
      if (this.moduleMetadata == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OrderSet.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadata = new Reference(); // cc
      return this.moduleMetadata;
    }

    public boolean hasModuleMetadata() { 
      return this.moduleMetadata != null && !this.moduleMetadata.isEmpty();
    }

    /**
     * @param value {@link #moduleMetadata} (A reference to a ModuleMetadata resource containing metadata for the orderset.)
     */
    public OrderSet setModuleMetadata(Reference value) { 
      this.moduleMetadata = value;
      return this;
    }

    /**
     * @return {@link #moduleMetadata} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a ModuleMetadata resource containing metadata for the orderset.)
     */
    public ModuleMetadata getModuleMetadataTarget() { 
      if (this.moduleMetadataTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OrderSet.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadataTarget = new ModuleMetadata(); // aa
      return this.moduleMetadataTarget;
    }

    /**
     * @param value {@link #moduleMetadata} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a ModuleMetadata resource containing metadata for the orderset.)
     */
    public OrderSet setModuleMetadataTarget(ModuleMetadata value) { 
      this.moduleMetadataTarget = value;
      return this;
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing any formal logic used by the orderset.)
     */
    public List<Reference> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      return this.library;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (Reference item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing any formal logic used by the orderset.)
     */
    // syntactic sugar
    public Reference addLibrary() { //3
      Reference t = new Reference();
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return t;
    }

    // syntactic sugar
    public OrderSet addLibrary(Reference t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return this;
    }

    /**
     * @return {@link #library} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A reference to a Library resource containing any formal logic used by the orderset.)
     */
    public List<Library> getLibraryTarget() { 
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      return this.libraryTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #library} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A reference to a Library resource containing any formal logic used by the orderset.)
     */
    public Library addLibraryTarget() { 
      Library r = new Library();
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      this.libraryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #item} (The definition of the items that make up the orderset.)
     */
    public List<OrderSetItemComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<OrderSetItemComponent>();
      return this.item;
    }

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (OrderSetItemComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #item} (The definition of the items that make up the orderset.)
     */
    // syntactic sugar
    public OrderSetItemComponent addItem() { //3
      OrderSetItemComponent t = new OrderSetItemComponent();
      if (this.item == null)
        this.item = new ArrayList<OrderSetItemComponent>();
      this.item.add(t);
      return t;
    }

    // syntactic sugar
    public OrderSet addItem(OrderSetItemComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<OrderSetItemComponent>();
      this.item.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("moduleMetadata", "Reference(ModuleMetadata)", "A reference to a ModuleMetadata resource containing metadata for the orderset.", 0, java.lang.Integer.MAX_VALUE, moduleMetadata));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library resource containing any formal logic used by the orderset.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("item", "", "The definition of the items that make up the orderset.", 0, java.lang.Integer.MAX_VALUE, item));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("moduleMetadata"))
          this.moduleMetadata = castToReference(value); // Reference
        else if (name.equals("library"))
          this.getLibrary().add(castToReference(value));
        else if (name.equals("item"))
          this.getItem().add((OrderSetItemComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrderSet.version");
        }
        else if (name.equals("moduleMetadata")) {
          this.moduleMetadata = new Reference();
          return this.moduleMetadata;
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "OrderSet";

  }

      public OrderSet copy() {
        OrderSet dst = new OrderSet();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.moduleMetadata = moduleMetadata == null ? null : moduleMetadata.copy();
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        if (item != null) {
          dst.item = new ArrayList<OrderSetItemComponent>();
          for (OrderSetItemComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      protected OrderSet typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OrderSet))
          return false;
        OrderSet o = (OrderSet) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true) && compareDeep(moduleMetadata, o.moduleMetadata, true)
           && compareDeep(library, o.library, true) && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OrderSet))
          return false;
        OrderSet o = (OrderSet) other;
        return compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (moduleMetadata == null || moduleMetadata.isEmpty()) && (library == null || library.isEmpty())
           && (item == null || item.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.OrderSet;
   }


}

