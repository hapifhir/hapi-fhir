package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, Dec 7, 2014 21:45-0500 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models.
 */
@ResourceDef(name="ConceptMap", profile="http://hl7.org/fhir/Profile/ConceptMap")
public class ConceptMap extends DomainResource {

    public enum ValuesetStatus implements FhirEnum {
        /**
         * This valueset is still under development.
         */
        DRAFT, 
        /**
         * This valueset is ready for normal use.
         */
        ACTIVE, 
        /**
         * This valueset has been withdrawn or superceded and should no longer be used.
         */
        RETIRED, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final ValuesetStatusEnumFactory ENUM_FACTORY = new ValuesetStatusEnumFactory();

        public static ValuesetStatus fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("retired".equals(codeString))
          return RETIRED;
        throw new IllegalArgumentException("Unknown ValuesetStatus code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case RETIRED: return "retired";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "";
            case ACTIVE: return "";
            case RETIRED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "This valueset is still under development.";
            case ACTIVE: return "This valueset is ready for normal use.";
            case RETIRED: return "This valueset has been withdrawn or superceded and should no longer be used.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case ACTIVE: return "Active";
            case RETIRED: return "Retired";
            default: return "?";
          }
        }
    }

  public static class ValuesetStatusEnumFactory implements EnumFactory<ValuesetStatus> {
    public ValuesetStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ValuesetStatus.DRAFT;
        if ("active".equals(codeString))
          return ValuesetStatus.ACTIVE;
        if ("retired".equals(codeString))
          return ValuesetStatus.RETIRED;
        throw new IllegalArgumentException("Unknown ValuesetStatus code '"+codeString+"'");
        }
    public String toCode(ValuesetStatus code) throws IllegalArgumentException {
      if (code == ValuesetStatus.DRAFT)
        return "draft";
      if (code == ValuesetStatus.ACTIVE)
        return "active";
      if (code == ValuesetStatus.RETIRED)
        return "retired";
      return "?";
      }
    }

    public enum ConceptEquivalence implements FhirEnum {
        /**
         * The definitions of the concepts mean the same thing (including when structural implications of meaning are considered) (i.e. extensionally identical).
         */
        EQUIVALENT, 
        /**
         * The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural implications of meaning are identifical or irrelevant (i.e. intensionally identical).
         */
        EQUAL, 
        /**
         * The target mapping is wider in meaning than the source concept.
         */
        WIDER, 
        /**
         * The target mapping subsumes the meaning of the source concept (e.g. the source is-a target).
         */
        SUBSUMES, 
        /**
         * The target mapping is narrower in meaning that the source concept. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when atempting to use these mappings operationally.
         */
        NARROWER, 
        /**
         * The target mapping specialises the meaning of the source concept (e.g. the target is-a source).
         */
        SPECIALISES, 
        /**
         * The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when atempting to use these mappings operationally.
         */
        INEXACT, 
        /**
         * There is no match for this concept in the destination concept system.
         */
        UNMATCHED, 
        /**
         * This is an explicit assertion that there is no mapping between the source and target concept.
         */
        DISJOINT, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final ConceptEquivalenceEnumFactory ENUM_FACTORY = new ConceptEquivalenceEnumFactory();

        public static ConceptEquivalence fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("equivalent".equals(codeString))
          return EQUIVALENT;
        if ("equal".equals(codeString))
          return EQUAL;
        if ("wider".equals(codeString))
          return WIDER;
        if ("subsumes".equals(codeString))
          return SUBSUMES;
        if ("narrower".equals(codeString))
          return NARROWER;
        if ("specialises".equals(codeString))
          return SPECIALISES;
        if ("inexact".equals(codeString))
          return INEXACT;
        if ("unmatched".equals(codeString))
          return UNMATCHED;
        if ("disjoint".equals(codeString))
          return DISJOINT;
        throw new IllegalArgumentException("Unknown ConceptEquivalence code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case EQUIVALENT: return "equivalent";
            case EQUAL: return "equal";
            case WIDER: return "wider";
            case SUBSUMES: return "subsumes";
            case NARROWER: return "narrower";
            case SPECIALISES: return "specialises";
            case INEXACT: return "inexact";
            case UNMATCHED: return "unmatched";
            case DISJOINT: return "disjoint";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case EQUIVALENT: return "";
            case EQUAL: return "";
            case WIDER: return "";
            case SUBSUMES: return "";
            case NARROWER: return "";
            case SPECIALISES: return "";
            case INEXACT: return "";
            case UNMATCHED: return "";
            case DISJOINT: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case EQUIVALENT: return "The definitions of the concepts mean the same thing (including when structural implications of meaning are considered) (i.e. extensionally identical).";
            case EQUAL: return "The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural implications of meaning are identifical or irrelevant (i.e. intensionally identical).";
            case WIDER: return "The target mapping is wider in meaning than the source concept.";
            case SUBSUMES: return "The target mapping subsumes the meaning of the source concept (e.g. the source is-a target).";
            case NARROWER: return "The target mapping is narrower in meaning that the source concept. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when atempting to use these mappings operationally.";
            case SPECIALISES: return "The target mapping specialises the meaning of the source concept (e.g. the target is-a source).";
            case INEXACT: return "The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when atempting to use these mappings operationally.";
            case UNMATCHED: return "There is no match for this concept in the destination concept system.";
            case DISJOINT: return "This is an explicit assertion that there is no mapping between the source and target concept.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EQUIVALENT: return "equivalent";
            case EQUAL: return "equal";
            case WIDER: return "wider";
            case SUBSUMES: return "subsumes";
            case NARROWER: return "narrower";
            case SPECIALISES: return "specialises";
            case INEXACT: return "inexact";
            case UNMATCHED: return "unmatched";
            case DISJOINT: return "disjoint";
            default: return "?";
          }
        }
    }

  public static class ConceptEquivalenceEnumFactory implements EnumFactory<ConceptEquivalence> {
    public ConceptEquivalence fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("equivalent".equals(codeString))
          return ConceptEquivalence.EQUIVALENT;
        if ("equal".equals(codeString))
          return ConceptEquivalence.EQUAL;
        if ("wider".equals(codeString))
          return ConceptEquivalence.WIDER;
        if ("subsumes".equals(codeString))
          return ConceptEquivalence.SUBSUMES;
        if ("narrower".equals(codeString))
          return ConceptEquivalence.NARROWER;
        if ("specialises".equals(codeString))
          return ConceptEquivalence.SPECIALISES;
        if ("inexact".equals(codeString))
          return ConceptEquivalence.INEXACT;
        if ("unmatched".equals(codeString))
          return ConceptEquivalence.UNMATCHED;
        if ("disjoint".equals(codeString))
          return ConceptEquivalence.DISJOINT;
        throw new IllegalArgumentException("Unknown ConceptEquivalence code '"+codeString+"'");
        }
    public String toCode(ConceptEquivalence code) throws IllegalArgumentException {
      if (code == ConceptEquivalence.EQUIVALENT)
        return "equivalent";
      if (code == ConceptEquivalence.EQUAL)
        return "equal";
      if (code == ConceptEquivalence.WIDER)
        return "wider";
      if (code == ConceptEquivalence.SUBSUMES)
        return "subsumes";
      if (code == ConceptEquivalence.NARROWER)
        return "narrower";
      if (code == ConceptEquivalence.SPECIALISES)
        return "specialises";
      if (code == ConceptEquivalence.INEXACT)
        return "inexact";
      if (code == ConceptEquivalence.UNMATCHED)
        return "unmatched";
      if (code == ConceptEquivalence.DISJOINT)
        return "disjoint";
      return "?";
      }
    }

    @Block()
    public static class ConceptMapElementComponent extends BackboneElement {
        /**
         * Code System (if the source is a value value set that crosses more than one code system).
         */
        @Child(name="codeSystem", type={UriType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Code System (if value set crosses code systems)", formalDefinition="Code System (if the source is a value value set that crosses more than one code system)." )
        protected UriType codeSystem;

        /**
         * Identity (code or path) or the element/item being mapped.
         */
        @Child(name="code", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Identifies element being mapped", formalDefinition="Identity (code or path) or the element/item being mapped." )
        protected CodeType code;

        /**
         * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value.
         */
        @Child(name="dependsOn", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Other elements required for this mapping (from context)", formalDefinition="A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value." )
        protected List<OtherElementComponent> dependsOn;

        /**
         * A concept from the target value set that this concept maps to.
         */
        @Child(name="map", type={}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Target of this map", formalDefinition="A concept from the target value set that this concept maps to." )
        protected List<ConceptMapElementMapComponent> map;

        private static final long serialVersionUID = 2079040744L;

      public ConceptMapElementComponent() {
        super();
      }

        /**
         * @return {@link #codeSystem} (Code System (if the source is a value value set that crosses more than one code system).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public UriType getCodeSystemElement() { 
          if (this.codeSystem == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptMapElementComponent.codeSystem");
            else if (Configuration.doAutoCreate())
              this.codeSystem = new UriType();
          return this.codeSystem;
        }

        public boolean hasCodeSystemElement() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        public boolean hasCodeSystem() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        /**
         * @param value {@link #codeSystem} (Code System (if the source is a value value set that crosses more than one code system).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public ConceptMapElementComponent setCodeSystemElement(UriType value) { 
          this.codeSystem = value;
          return this;
        }

        /**
         * @return Code System (if the source is a value value set that crosses more than one code system).
         */
        public String getCodeSystem() { 
          return this.codeSystem == null ? null : this.codeSystem.getValue();
        }

        /**
         * @param value Code System (if the source is a value value set that crosses more than one code system).
         */
        public ConceptMapElementComponent setCodeSystem(String value) { 
          if (Utilities.noString(value))
            this.codeSystem = null;
          else {
            if (this.codeSystem == null)
              this.codeSystem = new UriType();
            this.codeSystem.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #code} (Identity (code or path) or the element/item being mapped.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptMapElementComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType();
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Identity (code or path) or the element/item being mapped.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ConceptMapElementComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Identity (code or path) or the element/item being mapped.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Identity (code or path) or the element/item being mapped.
         */
        public ConceptMapElementComponent setCode(String value) { 
          if (Utilities.noString(value))
            this.code = null;
          else {
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #dependsOn} (A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value.)
         */
        public List<OtherElementComponent> getDependsOn() { 
          if (this.dependsOn == null)
            this.dependsOn = new ArrayList<OtherElementComponent>();
          return this.dependsOn;
        }

        public boolean hasDependsOn() { 
          if (this.dependsOn == null)
            return false;
          for (OtherElementComponent item : this.dependsOn)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dependsOn} (A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value.)
         */
    // syntactic sugar
        public OtherElementComponent addDependsOn() { //3
          OtherElementComponent t = new OtherElementComponent();
          if (this.dependsOn == null)
            this.dependsOn = new ArrayList<OtherElementComponent>();
          this.dependsOn.add(t);
          return t;
        }

        /**
         * @return {@link #map} (A concept from the target value set that this concept maps to.)
         */
        public List<ConceptMapElementMapComponent> getMap() { 
          if (this.map == null)
            this.map = new ArrayList<ConceptMapElementMapComponent>();
          return this.map;
        }

        public boolean hasMap() { 
          if (this.map == null)
            return false;
          for (ConceptMapElementMapComponent item : this.map)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #map} (A concept from the target value set that this concept maps to.)
         */
    // syntactic sugar
        public ConceptMapElementMapComponent addMap() { //3
          ConceptMapElementMapComponent t = new ConceptMapElementMapComponent();
          if (this.map == null)
            this.map = new ArrayList<ConceptMapElementMapComponent>();
          this.map.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("codeSystem", "uri", "Code System (if the source is a value value set that crosses more than one code system).", 0, java.lang.Integer.MAX_VALUE, codeSystem));
          childrenList.add(new Property("code", "code", "Identity (code or path) or the element/item being mapped.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("dependsOn", "", "A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value.", 0, java.lang.Integer.MAX_VALUE, dependsOn));
          childrenList.add(new Property("map", "", "A concept from the target value set that this concept maps to.", 0, java.lang.Integer.MAX_VALUE, map));
        }

      public ConceptMapElementComponent copy() {
        ConceptMapElementComponent dst = new ConceptMapElementComponent();
        copyValues(dst);
        dst.codeSystem = codeSystem == null ? null : codeSystem.copy();
        dst.code = code == null ? null : code.copy();
        if (dependsOn != null) {
          dst.dependsOn = new ArrayList<OtherElementComponent>();
          for (OtherElementComponent i : dependsOn)
            dst.dependsOn.add(i.copy());
        };
        if (map != null) {
          dst.map = new ArrayList<ConceptMapElementMapComponent>();
          for (ConceptMapElementMapComponent i : map)
            dst.map.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (codeSystem == null || codeSystem.isEmpty()) && (code == null || code.isEmpty())
           && (dependsOn == null || dependsOn.isEmpty()) && (map == null || map.isEmpty());
      }

  }

    @Block()
    public static class OtherElementComponent extends BackboneElement {
        /**
         * A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.
         */
        @Child(name="element", type={UriType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Reference to element/field/valueset mapping depends on", formalDefinition="A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition." )
        protected UriType element;

        /**
         * The code system of the dependency code (if the source/dependency is a value set that cross code systems).
         */
        @Child(name="codeSystem", type={UriType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Code System (if necessary)", formalDefinition="The code system of the dependency code (if the source/dependency is a value set that cross code systems)." )
        protected UriType codeSystem;

        /**
         * Identity (code or path) or the element/item that the map depends on / refers to.
         */
        @Child(name="code", type={StringType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Value of the referenced element", formalDefinition="Identity (code or path) or the element/item that the map depends on / refers to." )
        protected StringType code;

        private static final long serialVersionUID = 1488522448L;

      public OtherElementComponent() {
        super();
      }

      public OtherElementComponent(UriType element, UriType codeSystem, StringType code) {
        super();
        this.element = element;
        this.codeSystem = codeSystem;
        this.code = code;
      }

        /**
         * @return {@link #element} (A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.). This is the underlying object with id, value and extensions. The accessor "getElement" gives direct access to the value
         */
        public UriType getElementElement() { 
          if (this.element == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OtherElementComponent.element");
            else if (Configuration.doAutoCreate())
              this.element = new UriType();
          return this.element;
        }

        public boolean hasElementElement() { 
          return this.element != null && !this.element.isEmpty();
        }

        public boolean hasElement() { 
          return this.element != null && !this.element.isEmpty();
        }

        /**
         * @param value {@link #element} (A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.). This is the underlying object with id, value and extensions. The accessor "getElement" gives direct access to the value
         */
        public OtherElementComponent setElementElement(UriType value) { 
          this.element = value;
          return this;
        }

        /**
         * @return A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.
         */
        public String getElement() { 
          return this.element == null ? null : this.element.getValue();
        }

        /**
         * @param value A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.
         */
        public OtherElementComponent setElement(String value) { 
            if (this.element == null)
              this.element = new UriType();
            this.element.setValue(value);
          return this;
        }

        /**
         * @return {@link #codeSystem} (The code system of the dependency code (if the source/dependency is a value set that cross code systems).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public UriType getCodeSystemElement() { 
          if (this.codeSystem == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OtherElementComponent.codeSystem");
            else if (Configuration.doAutoCreate())
              this.codeSystem = new UriType();
          return this.codeSystem;
        }

        public boolean hasCodeSystemElement() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        public boolean hasCodeSystem() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        /**
         * @param value {@link #codeSystem} (The code system of the dependency code (if the source/dependency is a value set that cross code systems).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public OtherElementComponent setCodeSystemElement(UriType value) { 
          this.codeSystem = value;
          return this;
        }

        /**
         * @return The code system of the dependency code (if the source/dependency is a value set that cross code systems).
         */
        public String getCodeSystem() { 
          return this.codeSystem == null ? null : this.codeSystem.getValue();
        }

        /**
         * @param value The code system of the dependency code (if the source/dependency is a value set that cross code systems).
         */
        public OtherElementComponent setCodeSystem(String value) { 
            if (this.codeSystem == null)
              this.codeSystem = new UriType();
            this.codeSystem.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (Identity (code or path) or the element/item that the map depends on / refers to.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public StringType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OtherElementComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new StringType();
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Identity (code or path) or the element/item that the map depends on / refers to.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public OtherElementComponent setCodeElement(StringType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Identity (code or path) or the element/item that the map depends on / refers to.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Identity (code or path) or the element/item that the map depends on / refers to.
         */
        public OtherElementComponent setCode(String value) { 
            if (this.code == null)
              this.code = new StringType();
            this.code.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("element", "uri", "A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.", 0, java.lang.Integer.MAX_VALUE, element));
          childrenList.add(new Property("codeSystem", "uri", "The code system of the dependency code (if the source/dependency is a value set that cross code systems).", 0, java.lang.Integer.MAX_VALUE, codeSystem));
          childrenList.add(new Property("code", "string", "Identity (code or path) or the element/item that the map depends on / refers to.", 0, java.lang.Integer.MAX_VALUE, code));
        }

      public OtherElementComponent copy() {
        OtherElementComponent dst = new OtherElementComponent();
        copyValues(dst);
        dst.element = element == null ? null : element.copy();
        dst.codeSystem = codeSystem == null ? null : codeSystem.copy();
        dst.code = code == null ? null : code.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (element == null || element.isEmpty()) && (codeSystem == null || codeSystem.isEmpty())
           && (code == null || code.isEmpty());
      }

  }

    @Block()
    public static class ConceptMapElementMapComponent extends BackboneElement {
        /**
         * The code system of the target code (if the target is a value set that cross code systems).
         */
        @Child(name="codeSystem", type={UriType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="System of the target (if necessary)", formalDefinition="The code system of the target code (if the target is a value set that cross code systems)." )
        protected UriType codeSystem;

        /**
         * Identity (code or path) or the element/item that the map refers to.
         */
        @Child(name="code", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Code that identifies the target element", formalDefinition="Identity (code or path) or the element/item that the map refers to." )
        protected CodeType code;

        /**
         * The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target.
         */
        @Child(name="equivalence", type={CodeType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="equivalent | equal | wider | subsumes | narrower | specialises | inexact | unmatched | disjoint", formalDefinition="The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target." )
        protected Enumeration<ConceptEquivalence> equivalence;

        /**
         * A description of status/issues in mapping that conveys additional information not represented in  the structured data.
         */
        @Child(name="comments", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Description of status/issues in mapping", formalDefinition="A description of status/issues in mapping that conveys additional information not represented in  the structured data." )
        protected StringType comments;

        /**
         * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on.
         */
        @Child(name="product", type={OtherElementComponent.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Other concepts that this mapping also produces", formalDefinition="A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on." )
        protected List<OtherElementComponent> product;

        private static final long serialVersionUID = 606421694L;

      public ConceptMapElementMapComponent() {
        super();
      }

      public ConceptMapElementMapComponent(Enumeration<ConceptEquivalence> equivalence) {
        super();
        this.equivalence = equivalence;
      }

        /**
         * @return {@link #codeSystem} (The code system of the target code (if the target is a value set that cross code systems).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public UriType getCodeSystemElement() { 
          if (this.codeSystem == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptMapElementMapComponent.codeSystem");
            else if (Configuration.doAutoCreate())
              this.codeSystem = new UriType();
          return this.codeSystem;
        }

        public boolean hasCodeSystemElement() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        public boolean hasCodeSystem() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        /**
         * @param value {@link #codeSystem} (The code system of the target code (if the target is a value set that cross code systems).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public ConceptMapElementMapComponent setCodeSystemElement(UriType value) { 
          this.codeSystem = value;
          return this;
        }

        /**
         * @return The code system of the target code (if the target is a value set that cross code systems).
         */
        public String getCodeSystem() { 
          return this.codeSystem == null ? null : this.codeSystem.getValue();
        }

        /**
         * @param value The code system of the target code (if the target is a value set that cross code systems).
         */
        public ConceptMapElementMapComponent setCodeSystem(String value) { 
          if (Utilities.noString(value))
            this.codeSystem = null;
          else {
            if (this.codeSystem == null)
              this.codeSystem = new UriType();
            this.codeSystem.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #code} (Identity (code or path) or the element/item that the map refers to.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptMapElementMapComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType();
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Identity (code or path) or the element/item that the map refers to.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ConceptMapElementMapComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Identity (code or path) or the element/item that the map refers to.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Identity (code or path) or the element/item that the map refers to.
         */
        public ConceptMapElementMapComponent setCode(String value) { 
          if (Utilities.noString(value))
            this.code = null;
          else {
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #equivalence} (The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target.). This is the underlying object with id, value and extensions. The accessor "getEquivalence" gives direct access to the value
         */
        public Enumeration<ConceptEquivalence> getEquivalenceElement() { 
          if (this.equivalence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptMapElementMapComponent.equivalence");
            else if (Configuration.doAutoCreate())
              this.equivalence = new Enumeration<ConceptEquivalence>();
          return this.equivalence;
        }

        public boolean hasEquivalenceElement() { 
          return this.equivalence != null && !this.equivalence.isEmpty();
        }

        public boolean hasEquivalence() { 
          return this.equivalence != null && !this.equivalence.isEmpty();
        }

        /**
         * @param value {@link #equivalence} (The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target.). This is the underlying object with id, value and extensions. The accessor "getEquivalence" gives direct access to the value
         */
        public ConceptMapElementMapComponent setEquivalenceElement(Enumeration<ConceptEquivalence> value) { 
          this.equivalence = value;
          return this;
        }

        /**
         * @return The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target.
         */
        public ConceptEquivalence getEquivalence() { 
          return this.equivalence == null ? null : this.equivalence.getValue();
        }

        /**
         * @param value The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target.
         */
        public ConceptMapElementMapComponent setEquivalence(ConceptEquivalence value) { 
            if (this.equivalence == null)
              this.equivalence = new Enumeration<ConceptEquivalence>(ConceptEquivalence.ENUM_FACTORY);
            this.equivalence.setValue(value);
          return this;
        }

        /**
         * @return {@link #comments} (A description of status/issues in mapping that conveys additional information not represented in  the structured data.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
         */
        public StringType getCommentsElement() { 
          if (this.comments == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptMapElementMapComponent.comments");
            else if (Configuration.doAutoCreate())
              this.comments = new StringType();
          return this.comments;
        }

        public boolean hasCommentsElement() { 
          return this.comments != null && !this.comments.isEmpty();
        }

        public boolean hasComments() { 
          return this.comments != null && !this.comments.isEmpty();
        }

        /**
         * @param value {@link #comments} (A description of status/issues in mapping that conveys additional information not represented in  the structured data.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
         */
        public ConceptMapElementMapComponent setCommentsElement(StringType value) { 
          this.comments = value;
          return this;
        }

        /**
         * @return A description of status/issues in mapping that conveys additional information not represented in  the structured data.
         */
        public String getComments() { 
          return this.comments == null ? null : this.comments.getValue();
        }

        /**
         * @param value A description of status/issues in mapping that conveys additional information not represented in  the structured data.
         */
        public ConceptMapElementMapComponent setComments(String value) { 
          if (Utilities.noString(value))
            this.comments = null;
          else {
            if (this.comments == null)
              this.comments = new StringType();
            this.comments.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #product} (A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on.)
         */
        public List<OtherElementComponent> getProduct() { 
          if (this.product == null)
            this.product = new ArrayList<OtherElementComponent>();
          return this.product;
        }

        public boolean hasProduct() { 
          if (this.product == null)
            return false;
          for (OtherElementComponent item : this.product)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #product} (A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on.)
         */
    // syntactic sugar
        public OtherElementComponent addProduct() { //3
          OtherElementComponent t = new OtherElementComponent();
          if (this.product == null)
            this.product = new ArrayList<OtherElementComponent>();
          this.product.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("codeSystem", "uri", "The code system of the target code (if the target is a value set that cross code systems).", 0, java.lang.Integer.MAX_VALUE, codeSystem));
          childrenList.add(new Property("code", "code", "Identity (code or path) or the element/item that the map refers to.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("equivalence", "code", "The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target.", 0, java.lang.Integer.MAX_VALUE, equivalence));
          childrenList.add(new Property("comments", "string", "A description of status/issues in mapping that conveys additional information not represented in  the structured data.", 0, java.lang.Integer.MAX_VALUE, comments));
          childrenList.add(new Property("product", "@ConceptMap.element.dependsOn", "A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on.", 0, java.lang.Integer.MAX_VALUE, product));
        }

      public ConceptMapElementMapComponent copy() {
        ConceptMapElementMapComponent dst = new ConceptMapElementMapComponent();
        copyValues(dst);
        dst.codeSystem = codeSystem == null ? null : codeSystem.copy();
        dst.code = code == null ? null : code.copy();
        dst.equivalence = equivalence == null ? null : equivalence.copy();
        dst.comments = comments == null ? null : comments.copy();
        if (product != null) {
          dst.product = new ArrayList<OtherElementComponent>();
          for (OtherElementComponent i : product)
            dst.product.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (codeSystem == null || codeSystem.isEmpty()) && (code == null || code.isEmpty())
           && (equivalence == null || equivalence.isEmpty()) && (comments == null || comments.isEmpty())
           && (product == null || product.isEmpty());
      }

  }

    /**
     * The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).
     */
    @Child(name="identifier", type={StringType.class}, order=-1, min=0, max=1)
    @Description(shortDefinition="Logical id to reference this concept map", formalDefinition="The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)." )
    protected StringType identifier;

    /**
     * The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    @Child(name="version", type={StringType.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Logical id for this version of the concept map", formalDefinition="The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp." )
    protected StringType version;

    /**
     * A free text natural language name describing the concept map.
     */
    @Child(name="name", type={StringType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Informal name for this concept map", formalDefinition="A free text natural language name describing the concept map." )
    protected StringType name;

    /**
     * The name of the individual or organization that published the concept map.
     */
    @Child(name="publisher", type={StringType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the concept map." )
    protected StringType publisher;

    /**
     * Contacts of the publisher to assist a user in finding and communicating with the publisher.
     */
    @Child(name="telecom", type={ContactPoint.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contact information of the publisher", formalDefinition="Contacts of the publisher to assist a user in finding and communicating with the publisher." )
    protected List<ContactPoint> telecom;

    /**
     * A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.
     */
    @Child(name="description", type={StringType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Human language description of the concept map", formalDefinition="A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc." )
    protected StringType description;

    /**
     * A copyright statement relating to the concept map and/or its contents.
     */
    @Child(name="copyright", type={StringType.class}, order=5, min=0, max=1)
    @Description(shortDefinition="About the concept map or its content", formalDefinition="A copyright statement relating to the concept map and/or its contents." )
    protected StringType copyright;

    /**
     * The status of the concept map.
     */
    @Child(name="status", type={CodeType.class}, order=6, min=1, max=1)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the concept map." )
    protected Enumeration<ValuesetStatus> status;

    /**
     * This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name="experimental", type={BooleanType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The date that the concept map status was last changed.
     */
    @Child(name="date", type={DateTimeType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Date for given status", formalDefinition="The date that the concept map status was last changed." )
    protected DateTimeType date;

    /**
     * The source value set that specifies the concepts that are being mapped.
     */
    @Child(name="source", type={UriType.class, ValueSet.class, Profile.class}, order=9, min=1, max=1)
    @Description(shortDefinition="Identifies the source of the concepts which are being mapped", formalDefinition="The source value set that specifies the concepts that are being mapped." )
    protected Type source;

    /**
     * The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.
     */
    @Child(name="target", type={UriType.class, ValueSet.class, Profile.class}, order=10, min=1, max=1)
    @Description(shortDefinition="Provides context to the mappings", formalDefinition="The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made." )
    protected Type target;

    /**
     * Mappings for an individual concept in the source to one or more concepts in the target.
     */
    @Child(name="element", type={}, order=11, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Mappings for a concept from the source set", formalDefinition="Mappings for an individual concept in the source to one or more concepts in the target." )
    protected List<ConceptMapElementComponent> element;

    private static final long serialVersionUID = 1130142902L;

    public ConceptMap() {
      super();
    }

    public ConceptMap(Enumeration<ValuesetStatus> status, Type source, Type target) {
      super();
      this.status = status;
      this.source = source;
      this.target = target;
    }

    /**
     * @return {@link #identifier} (The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
     */
    public StringType getIdentifierElement() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new StringType();
      return this.identifier;
    }

    public boolean hasIdentifierElement() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
     */
    public ConceptMap setIdentifierElement(StringType value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).
     */
    public String getIdentifier() { 
      return this.identifier == null ? null : this.identifier.getValue();
    }

    /**
     * @param value The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).
     */
    public ConceptMap setIdentifier(String value) { 
      if (Utilities.noString(value))
        this.identifier = null;
      else {
        if (this.identifier == null)
          this.identifier = new StringType();
        this.identifier.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType();
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ConceptMap setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public ConceptMap setVersion(String value) { 
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
     * @return {@link #name} (A free text natural language name describing the concept map.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.name");
        else if (Configuration.doAutoCreate())
          this.name = new StringType();
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A free text natural language name describing the concept map.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ConceptMap setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name describing the concept map.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name describing the concept map.
     */
    public ConceptMap setName(String value) { 
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
     * @return {@link #publisher} (The name of the individual or organization that published the concept map.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType();
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the individual or organization that published the concept map.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ConceptMap setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the concept map.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the concept map.
     */
    public ConceptMap setPublisher(String value) { 
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
     * @return {@link #telecom} (Contacts of the publisher to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactPoint> getTelecom() { 
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      return this.telecom;
    }

    public boolean hasTelecom() { 
      if (this.telecom == null)
        return false;
      for (ContactPoint item : this.telecom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #telecom} (Contacts of the publisher to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    /**
     * @return {@link #description} (A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType();
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ConceptMap setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.
     */
    public ConceptMap setDescription(String value) { 
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
     * @return {@link #copyright} (A copyright statement relating to the concept map and/or its contents.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new StringType();
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the concept map and/or its contents.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ConceptMap setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the concept map and/or its contents.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the concept map and/or its contents.
     */
    public ConceptMap setCopyright(String value) { 
      if (Utilities.noString(value))
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new StringType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (The status of the concept map.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ValuesetStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ValuesetStatus>();
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the concept map.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ConceptMap setStatusElement(Enumeration<ValuesetStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the concept map.
     */
    public ValuesetStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the concept map.
     */
    public ConceptMap setStatus(ValuesetStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ValuesetStatus>(ValuesetStatus.ENUM_FACTORY);
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType();
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ConceptMap setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null ? false : this.experimental.getValue();
    }

    /**
     * @param value This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public ConceptMap setExperimental(boolean value) { 
      if (value == false)
        this.experimental = null;
      else {
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #date} (The date that the concept map status was last changed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType();
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date that the concept map status was last changed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ConceptMap setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that the concept map status was last changed.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that the concept map status was last changed.
     */
    public ConceptMap setDate(Date value) { 
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
     * @return {@link #source} (The source value set that specifies the concepts that are being mapped.)
     */
    public Type getSource() { 
      return this.source;
    }

    /**
     * @return {@link #source} (The source value set that specifies the concepts that are being mapped.)
     */
    public UriType getSourceUriType() throws Exception { 
      if (!(this.source instanceof UriType))
        throw new Exception("Type mismatch: the type UriType was expected, but "+this.source.getClass().getName()+" was encountered");
      return (UriType) this.source;
    }

    /**
     * @return {@link #source} (The source value set that specifies the concepts that are being mapped.)
     */
    public Reference getSourceReference() throws Exception { 
      if (!(this.source instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.source.getClass().getName()+" was encountered");
      return (Reference) this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (The source value set that specifies the concepts that are being mapped.)
     */
    public ConceptMap setSource(Type value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #target} (The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.)
     */
    public Type getTarget() { 
      return this.target;
    }

    /**
     * @return {@link #target} (The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.)
     */
    public UriType getTargetUriType() throws Exception { 
      if (!(this.target instanceof UriType))
        throw new Exception("Type mismatch: the type UriType was expected, but "+this.target.getClass().getName()+" was encountered");
      return (UriType) this.target;
    }

    /**
     * @return {@link #target} (The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.)
     */
    public Reference getTargetReference() throws Exception { 
      if (!(this.target instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.target.getClass().getName()+" was encountered");
      return (Reference) this.target;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.)
     */
    public ConceptMap setTarget(Type value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #element} (Mappings for an individual concept in the source to one or more concepts in the target.)
     */
    public List<ConceptMapElementComponent> getElement() { 
      if (this.element == null)
        this.element = new ArrayList<ConceptMapElementComponent>();
      return this.element;
    }

    public boolean hasElement() { 
      if (this.element == null)
        return false;
      for (ConceptMapElementComponent item : this.element)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #element} (Mappings for an individual concept in the source to one or more concepts in the target.)
     */
    // syntactic sugar
    public ConceptMapElementComponent addElement() { //3
      ConceptMapElementComponent t = new ConceptMapElementComponent();
      if (this.element == null)
        this.element = new ArrayList<ConceptMapElementComponent>();
      this.element.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "string", "The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name describing the concept map.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the concept map.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("telecom", "ContactPoint", "Contacts of the publisher to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("description", "string", "A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the concept map and/or its contents.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("status", "code", "The status of the concept map.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date that the concept map status was last changed.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("source[x]", "uri|Reference(ValueSet|Profile)", "The source value set that specifies the concepts that are being mapped.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("target[x]", "uri|Reference(ValueSet|Profile)", "The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("element", "", "Mappings for an individual concept in the source to one or more concepts in the target.", 0, java.lang.Integer.MAX_VALUE, element));
      }

      public ConceptMap copy() {
        ConceptMap dst = new ConceptMap();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.source = source == null ? null : source.copy();
        dst.target = target == null ? null : target.copy();
        if (element != null) {
          dst.element = new ArrayList<ConceptMapElementComponent>();
          for (ConceptMapElementComponent i : element)
            dst.element.add(i.copy());
        };
        return dst;
      }

      protected ConceptMap typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (name == null || name.isEmpty()) && (publisher == null || publisher.isEmpty()) && (telecom == null || telecom.isEmpty())
           && (description == null || description.isEmpty()) && (copyright == null || copyright.isEmpty())
           && (status == null || status.isEmpty()) && (experimental == null || experimental.isEmpty())
           && (date == null || date.isEmpty()) && (source == null || source.isEmpty()) && (target == null || target.isEmpty())
           && (element == null || element.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ConceptMap;
   }

  @SearchParamDefinition(name="product", path="ConceptMap.element.map.product.element", description="Reference to element/field/valueset mapping depends on", type="token" )
  public static final String SP_PRODUCT = "product";
  @SearchParamDefinition(name="dependson", path="ConceptMap.element.dependsOn.element", description="Reference to element/field/valueset mapping depends on", type="token" )
  public static final String SP_DEPENDSON = "dependson";
  @SearchParamDefinition(name="system", path="ConceptMap.element.map.codeSystem", description="The system for any destination concepts mapped by this map", type="token" )
  public static final String SP_SYSTEM = "system";
  @SearchParamDefinition(name="source", path="ConceptMap.source[x]", description="The system for any concepts mapped by this concept map", type="reference" )
  public static final String SP_SOURCE = "source";
  @SearchParamDefinition(name="status", path="ConceptMap.status", description="Status of the concept map", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="description", path="ConceptMap.description", description="Text search in the description of the concept map", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="name", path="ConceptMap.name", description="Name of the concept map", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="target", path="ConceptMap.target[x]", description="Provides context to the mappings", type="reference" )
  public static final String SP_TARGET = "target";
  @SearchParamDefinition(name="date", path="ConceptMap.date", description="The concept map publication date", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="ConceptMap.identifier", description="The identifier of the concept map", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="publisher", path="ConceptMap.publisher", description="Name of the publisher of the concept map", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="version", path="ConceptMap.version", description="The version identifier of the concept map", type="token" )
  public static final String SP_VERSION = "version";

}

