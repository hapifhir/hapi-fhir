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
 * A value set specifies a set of codes drawn from one or more code systems.
 */
@ResourceDef(name="ValueSet", profile="http://hl7.org/fhir/Profile/ValueSet")
public class ValueSet extends DomainResource {

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

    public enum FilterOperator implements FhirEnum {
        /**
         * The specified property of the code equals the provided value.
         */
        EQUAL, 
        /**
         * The specified property of the code has an is-a relationship with the provided value.
         */
        ISA, 
        /**
         * The specified property of the code does not have an is-a relationship with the provided value.
         */
        ISNOTA, 
        /**
         * The specified property of the code  matches the regex specified in the provided value.
         */
        REGEX, 
        /**
         * The specified property of the code is in the set of codes or concepts specified in the provided value (comma separated list).
         */
        IN, 
        /**
         * The specified property of the code is not in the set of codes or concepts specified in the provided value (comma separated list).
         */
        NOTIN, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final FilterOperatorEnumFactory ENUM_FACTORY = new FilterOperatorEnumFactory();

        public static FilterOperator fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("=".equals(codeString))
          return EQUAL;
        if ("is-a".equals(codeString))
          return ISA;
        if ("is-not-a".equals(codeString))
          return ISNOTA;
        if ("regex".equals(codeString))
          return REGEX;
        if ("in".equals(codeString))
          return IN;
        if ("not in".equals(codeString))
          return NOTIN;
        throw new IllegalArgumentException("Unknown FilterOperator code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case EQUAL: return "=";
            case ISA: return "is-a";
            case ISNOTA: return "is-not-a";
            case REGEX: return "regex";
            case IN: return "in";
            case NOTIN: return "not in";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case EQUAL: return "";
            case ISA: return "";
            case ISNOTA: return "";
            case REGEX: return "";
            case IN: return "";
            case NOTIN: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case EQUAL: return "The specified property of the code equals the provided value.";
            case ISA: return "The specified property of the code has an is-a relationship with the provided value.";
            case ISNOTA: return "The specified property of the code does not have an is-a relationship with the provided value.";
            case REGEX: return "The specified property of the code  matches the regex specified in the provided value.";
            case IN: return "The specified property of the code is in the set of codes or concepts specified in the provided value (comma separated list).";
            case NOTIN: return "The specified property of the code is not in the set of codes or concepts specified in the provided value (comma separated list).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EQUAL: return "=";
            case ISA: return "is-a";
            case ISNOTA: return "is-not-a";
            case REGEX: return "regex";
            case IN: return "in";
            case NOTIN: return "not in";
            default: return "?";
          }
        }
    }

  public static class FilterOperatorEnumFactory implements EnumFactory<FilterOperator> {
    public FilterOperator fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("=".equals(codeString))
          return FilterOperator.EQUAL;
        if ("is-a".equals(codeString))
          return FilterOperator.ISA;
        if ("is-not-a".equals(codeString))
          return FilterOperator.ISNOTA;
        if ("regex".equals(codeString))
          return FilterOperator.REGEX;
        if ("in".equals(codeString))
          return FilterOperator.IN;
        if ("not in".equals(codeString))
          return FilterOperator.NOTIN;
        throw new IllegalArgumentException("Unknown FilterOperator code '"+codeString+"'");
        }
    public String toCode(FilterOperator code) throws IllegalArgumentException {
      if (code == FilterOperator.EQUAL)
        return "=";
      if (code == FilterOperator.ISA)
        return "is-a";
      if (code == FilterOperator.ISNOTA)
        return "is-not-a";
      if (code == FilterOperator.REGEX)
        return "regex";
      if (code == FilterOperator.IN)
        return "in";
      if (code == FilterOperator.NOTIN)
        return "not in";
      return "?";
      }
    }

    @Block()
    public static class ValueSetDefineComponent extends BackboneElement {
        /**
         * URI to identify the code system.
         */
        @Child(name="system", type={UriType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="URI to identify the code system", formalDefinition="URI to identify the code system." )
        protected UriType system;

        /**
         * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked.
         */
        @Child(name="version", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Version of this system", formalDefinition="The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked." )
        protected StringType version;

        /**
         * If code comparison is case sensitive when codes within this system are compared to each other.
         */
        @Child(name="caseSensitive", type={BooleanType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="If code comparison is case sensitive", formalDefinition="If code comparison is case sensitive when codes within this system are compared to each other." )
        protected BooleanType caseSensitive;

        /**
         * Concepts in the code system.
         */
        @Child(name="concept", type={}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Concepts in the code system", formalDefinition="Concepts in the code system." )
        protected List<ConceptDefinitionComponent> concept;

        private static final long serialVersionUID = -1109401192L;

      public ValueSetDefineComponent() {
        super();
      }

      public ValueSetDefineComponent(UriType system) {
        super();
        this.system = system;
      }

        /**
         * @return {@link #system} (URI to identify the code system.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public UriType getSystemElement() { 
          if (this.system == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetDefineComponent.system");
            else if (Configuration.doAutoCreate())
              this.system = new UriType();
          return this.system;
        }

        public boolean hasSystemElement() { 
          return this.system != null && !this.system.isEmpty();
        }

        public boolean hasSystem() { 
          return this.system != null && !this.system.isEmpty();
        }

        /**
         * @param value {@link #system} (URI to identify the code system.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public ValueSetDefineComponent setSystemElement(UriType value) { 
          this.system = value;
          return this;
        }

        /**
         * @return URI to identify the code system.
         */
        public String getSystem() { 
          return this.system == null ? null : this.system.getValue();
        }

        /**
         * @param value URI to identify the code system.
         */
        public ValueSetDefineComponent setSystem(String value) { 
            if (this.system == null)
              this.system = new UriType();
            this.system.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetDefineComponent.version");
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
         * @param value {@link #version} (The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ValueSetDefineComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked.
         */
        public ValueSetDefineComponent setVersion(String value) { 
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
         * @return {@link #caseSensitive} (If code comparison is case sensitive when codes within this system are compared to each other.). This is the underlying object with id, value and extensions. The accessor "getCaseSensitive" gives direct access to the value
         */
        public BooleanType getCaseSensitiveElement() { 
          if (this.caseSensitive == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetDefineComponent.caseSensitive");
            else if (Configuration.doAutoCreate())
              this.caseSensitive = new BooleanType();
          return this.caseSensitive;
        }

        public boolean hasCaseSensitiveElement() { 
          return this.caseSensitive != null && !this.caseSensitive.isEmpty();
        }

        public boolean hasCaseSensitive() { 
          return this.caseSensitive != null && !this.caseSensitive.isEmpty();
        }

        /**
         * @param value {@link #caseSensitive} (If code comparison is case sensitive when codes within this system are compared to each other.). This is the underlying object with id, value and extensions. The accessor "getCaseSensitive" gives direct access to the value
         */
        public ValueSetDefineComponent setCaseSensitiveElement(BooleanType value) { 
          this.caseSensitive = value;
          return this;
        }

        /**
         * @return If code comparison is case sensitive when codes within this system are compared to each other.
         */
        public boolean getCaseSensitive() { 
          return this.caseSensitive == null ? false : this.caseSensitive.getValue();
        }

        /**
         * @param value If code comparison is case sensitive when codes within this system are compared to each other.
         */
        public ValueSetDefineComponent setCaseSensitive(boolean value) { 
          if (value == false)
            this.caseSensitive = null;
          else {
            if (this.caseSensitive == null)
              this.caseSensitive = new BooleanType();
            this.caseSensitive.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #concept} (Concepts in the code system.)
         */
        public List<ConceptDefinitionComponent> getConcept() { 
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          return this.concept;
        }

        public boolean hasConcept() { 
          if (this.concept == null)
            return false;
          for (ConceptDefinitionComponent item : this.concept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #concept} (Concepts in the code system.)
         */
    // syntactic sugar
        public ConceptDefinitionComponent addConcept() { //3
          ConceptDefinitionComponent t = new ConceptDefinitionComponent();
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          this.concept.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("system", "uri", "URI to identify the code system.", 0, java.lang.Integer.MAX_VALUE, system));
          childrenList.add(new Property("version", "string", "The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("caseSensitive", "boolean", "If code comparison is case sensitive when codes within this system are compared to each other.", 0, java.lang.Integer.MAX_VALUE, caseSensitive));
          childrenList.add(new Property("concept", "", "Concepts in the code system.", 0, java.lang.Integer.MAX_VALUE, concept));
        }

      public ValueSetDefineComponent copy() {
        ValueSetDefineComponent dst = new ValueSetDefineComponent();
        copyValues(dst);
        dst.system = system == null ? null : system.copy();
        dst.version = version == null ? null : version.copy();
        dst.caseSensitive = caseSensitive == null ? null : caseSensitive.copy();
        if (concept != null) {
          dst.concept = new ArrayList<ConceptDefinitionComponent>();
          for (ConceptDefinitionComponent i : concept)
            dst.concept.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (system == null || system.isEmpty()) && (version == null || version.isEmpty())
           && (caseSensitive == null || caseSensitive.isEmpty()) && (concept == null || concept.isEmpty())
          ;
      }

  }

    @Block()
    public static class ConceptDefinitionComponent extends BackboneElement {
        /**
         * Code that identifies concept.
         */
        @Child(name="code", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Code that identifies concept", formalDefinition="Code that identifies concept." )
        protected CodeType code;

        /**
         * If this code is not for use as a real concept.
         */
        @Child(name="abstract_", type={BooleanType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="If this code is not for use as a real concept", formalDefinition="If this code is not for use as a real concept." )
        protected BooleanType abstract_;

        /**
         * Text to Display to the user.
         */
        @Child(name="display", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Text to Display to the user", formalDefinition="Text to Display to the user." )
        protected StringType display;

        /**
         * The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept.
         */
        @Child(name="definition", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Formal Definition", formalDefinition="The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept." )
        protected StringType definition;

        /**
         * Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc.
         */
        @Child(name="designation", type={}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Additional representations for the concept", formalDefinition="Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc." )
        protected List<ConceptDefinitionDesignationComponent> designation;

        /**
         * Child Concepts (is-a / contains).
         */
        @Child(name="concept", type={ConceptDefinitionComponent.class}, order=6, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Child Concepts (is-a / contains)", formalDefinition="Child Concepts (is-a / contains)." )
        protected List<ConceptDefinitionComponent> concept;

        private static final long serialVersionUID = -318560292L;

      public ConceptDefinitionComponent() {
        super();
      }

      public ConceptDefinitionComponent(CodeType code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Code that identifies concept.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionComponent.code");
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
         * @param value {@link #code} (Code that identifies concept.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ConceptDefinitionComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Code that identifies concept.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Code that identifies concept.
         */
        public ConceptDefinitionComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #abstract_} (If this code is not for use as a real concept.). This is the underlying object with id, value and extensions. The accessor "getAbstract" gives direct access to the value
         */
        public BooleanType getAbstractElement() { 
          if (this.abstract_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionComponent.abstract_");
            else if (Configuration.doAutoCreate())
              this.abstract_ = new BooleanType();
          return this.abstract_;
        }

        public boolean hasAbstractElement() { 
          return this.abstract_ != null && !this.abstract_.isEmpty();
        }

        public boolean hasAbstract() { 
          return this.abstract_ != null && !this.abstract_.isEmpty();
        }

        /**
         * @param value {@link #abstract_} (If this code is not for use as a real concept.). This is the underlying object with id, value and extensions. The accessor "getAbstract" gives direct access to the value
         */
        public ConceptDefinitionComponent setAbstractElement(BooleanType value) { 
          this.abstract_ = value;
          return this;
        }

        /**
         * @return If this code is not for use as a real concept.
         */
        public boolean getAbstract() { 
          return this.abstract_ == null ? false : this.abstract_.getValue();
        }

        /**
         * @param value If this code is not for use as a real concept.
         */
        public ConceptDefinitionComponent setAbstract(boolean value) { 
          if (value == false)
            this.abstract_ = null;
          else {
            if (this.abstract_ == null)
              this.abstract_ = new BooleanType();
            this.abstract_.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #display} (Text to Display to the user.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public StringType getDisplayElement() { 
          if (this.display == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionComponent.display");
            else if (Configuration.doAutoCreate())
              this.display = new StringType();
          return this.display;
        }

        public boolean hasDisplayElement() { 
          return this.display != null && !this.display.isEmpty();
        }

        public boolean hasDisplay() { 
          return this.display != null && !this.display.isEmpty();
        }

        /**
         * @param value {@link #display} (Text to Display to the user.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public ConceptDefinitionComponent setDisplayElement(StringType value) { 
          this.display = value;
          return this;
        }

        /**
         * @return Text to Display to the user.
         */
        public String getDisplay() { 
          return this.display == null ? null : this.display.getValue();
        }

        /**
         * @param value Text to Display to the user.
         */
        public ConceptDefinitionComponent setDisplay(String value) { 
          if (Utilities.noString(value))
            this.display = null;
          else {
            if (this.display == null)
              this.display = new StringType();
            this.display.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #definition} (The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public StringType getDefinitionElement() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definition = new StringType();
          return this.definition;
        }

        public boolean hasDefinitionElement() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public ConceptDefinitionComponent setDefinitionElement(StringType value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept.
         */
        public String getDefinition() { 
          return this.definition == null ? null : this.definition.getValue();
        }

        /**
         * @param value The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept.
         */
        public ConceptDefinitionComponent setDefinition(String value) { 
          if (Utilities.noString(value))
            this.definition = null;
          else {
            if (this.definition == null)
              this.definition = new StringType();
            this.definition.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #designation} (Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc.)
         */
        public List<ConceptDefinitionDesignationComponent> getDesignation() { 
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          return this.designation;
        }

        public boolean hasDesignation() { 
          if (this.designation == null)
            return false;
          for (ConceptDefinitionDesignationComponent item : this.designation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #designation} (Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc.)
         */
    // syntactic sugar
        public ConceptDefinitionDesignationComponent addDesignation() { //3
          ConceptDefinitionDesignationComponent t = new ConceptDefinitionDesignationComponent();
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          this.designation.add(t);
          return t;
        }

        /**
         * @return {@link #concept} (Child Concepts (is-a / contains).)
         */
        public List<ConceptDefinitionComponent> getConcept() { 
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          return this.concept;
        }

        public boolean hasConcept() { 
          if (this.concept == null)
            return false;
          for (ConceptDefinitionComponent item : this.concept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #concept} (Child Concepts (is-a / contains).)
         */
    // syntactic sugar
        public ConceptDefinitionComponent addConcept() { //3
          ConceptDefinitionComponent t = new ConceptDefinitionComponent();
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          this.concept.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "Code that identifies concept.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("abstract", "boolean", "If this code is not for use as a real concept.", 0, java.lang.Integer.MAX_VALUE, abstract_));
          childrenList.add(new Property("display", "string", "Text to Display to the user.", 0, java.lang.Integer.MAX_VALUE, display));
          childrenList.add(new Property("definition", "string", "The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept.", 0, java.lang.Integer.MAX_VALUE, definition));
          childrenList.add(new Property("designation", "", "Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc.", 0, java.lang.Integer.MAX_VALUE, designation));
          childrenList.add(new Property("concept", "@ValueSet.define.concept", "Child Concepts (is-a / contains).", 0, java.lang.Integer.MAX_VALUE, concept));
        }

      public ConceptDefinitionComponent copy() {
        ConceptDefinitionComponent dst = new ConceptDefinitionComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.abstract_ = abstract_ == null ? null : abstract_.copy();
        dst.display = display == null ? null : display.copy();
        dst.definition = definition == null ? null : definition.copy();
        if (designation != null) {
          dst.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          for (ConceptDefinitionDesignationComponent i : designation)
            dst.designation.add(i.copy());
        };
        if (concept != null) {
          dst.concept = new ArrayList<ConceptDefinitionComponent>();
          for (ConceptDefinitionComponent i : concept)
            dst.concept.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (abstract_ == null || abstract_.isEmpty())
           && (display == null || display.isEmpty()) && (definition == null || definition.isEmpty())
           && (designation == null || designation.isEmpty()) && (concept == null || concept.isEmpty())
          ;
      }

  }

    @Block()
    public static class ConceptDefinitionDesignationComponent extends BackboneElement {
        /**
         * The language this designation is defined for.
         */
        @Child(name="language", type={CodeType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Language of the designation", formalDefinition="The language this designation is defined for." )
        protected CodeType language;

        /**
         * A code that details how this designation would be used.
         */
        @Child(name="use", type={Coding.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Details how this designation would be used", formalDefinition="A code that details how this designation would be used." )
        protected Coding use;

        /**
         * The text value for this designation.
         */
        @Child(name="value", type={StringType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="The text value for this designation", formalDefinition="The text value for this designation." )
        protected StringType value;

        private static final long serialVersionUID = 1515662414L;

      public ConceptDefinitionDesignationComponent() {
        super();
      }

      public ConceptDefinitionDesignationComponent(StringType value) {
        super();
        this.value = value;
      }

        /**
         * @return {@link #language} (The language this designation is defined for.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public CodeType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionDesignationComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new CodeType();
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The language this designation is defined for.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public ConceptDefinitionDesignationComponent setLanguageElement(CodeType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return The language this designation is defined for.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value The language this designation is defined for.
         */
        public ConceptDefinitionDesignationComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new CodeType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #use} (A code that details how this designation would be used.)
         */
        public Coding getUse() { 
          if (this.use == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionDesignationComponent.use");
            else if (Configuration.doAutoCreate())
              this.use = new Coding();
          return this.use;
        }

        public boolean hasUse() { 
          return this.use != null && !this.use.isEmpty();
        }

        /**
         * @param value {@link #use} (A code that details how this designation would be used.)
         */
        public ConceptDefinitionDesignationComponent setUse(Coding value) { 
          this.use = value;
          return this;
        }

        /**
         * @return {@link #value} (The text value for this designation.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionDesignationComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new StringType();
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The text value for this designation.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public ConceptDefinitionDesignationComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The text value for this designation.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The text value for this designation.
         */
        public ConceptDefinitionDesignationComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("language", "code", "The language this designation is defined for.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("use", "Coding", "A code that details how this designation would be used.", 0, java.lang.Integer.MAX_VALUE, use));
          childrenList.add(new Property("value", "string", "The text value for this designation.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      public ConceptDefinitionDesignationComponent copy() {
        ConceptDefinitionDesignationComponent dst = new ConceptDefinitionDesignationComponent();
        copyValues(dst);
        dst.language = language == null ? null : language.copy();
        dst.use = use == null ? null : use.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (language == null || language.isEmpty()) && (use == null || use.isEmpty())
           && (value == null || value.isEmpty());
      }

  }

    @Block()
    public static class ValueSetComposeComponent extends BackboneElement {
        /**
         * Includes the contents of the referenced value set as a part of the contents of this value set.
         */
        @Child(name="import_", type={UriType.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Import the contents of another value set", formalDefinition="Includes the contents of the referenced value set as a part of the contents of this value set." )
        protected List<UriType> import_;

        /**
         * Include one or more codes from a code system.
         */
        @Child(name="include", type={}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Include one or more codes from a code system", formalDefinition="Include one or more codes from a code system." )
        protected List<ConceptSetComponent> include;

        /**
         * Exclude one or more codes from the value set.
         */
        @Child(name="exclude", type={ConceptSetComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Explicitly exclude codes", formalDefinition="Exclude one or more codes from the value set." )
        protected List<ConceptSetComponent> exclude;

        private static final long serialVersionUID = -703166694L;

      public ValueSetComposeComponent() {
        super();
      }

        /**
         * @return {@link #import_} (Includes the contents of the referenced value set as a part of the contents of this value set.)
         */
        public List<UriType> getImport() { 
          if (this.import_ == null)
            this.import_ = new ArrayList<UriType>();
          return this.import_;
        }

        public boolean hasImport() { 
          if (this.import_ == null)
            return false;
          for (UriType item : this.import_)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #import_} (Includes the contents of the referenced value set as a part of the contents of this value set.)
         */
    // syntactic sugar
        public UriType addImportElement() {//2 
          UriType t = new UriType();
          if (this.import_ == null)
            this.import_ = new ArrayList<UriType>();
          this.import_.add(t);
          return t;
        }

        /**
         * @param value {@link #import_} (Includes the contents of the referenced value set as a part of the contents of this value set.)
         */
        public ValueSetComposeComponent addImport(String value) { //1
          UriType t = new UriType();
          t.setValue(value);
          if (this.import_ == null)
            this.import_ = new ArrayList<UriType>();
          this.import_.add(t);
          return this;
        }

        /**
         * @param value {@link #import_} (Includes the contents of the referenced value set as a part of the contents of this value set.)
         */
        public boolean hasImport(String value) { 
          if (this.import_ == null)
            return false;
          for (UriType v : this.import_)
            if (v.equals(value)) // uri
              return true;
          return false;
        }

        /**
         * @return {@link #include} (Include one or more codes from a code system.)
         */
        public List<ConceptSetComponent> getInclude() { 
          if (this.include == null)
            this.include = new ArrayList<ConceptSetComponent>();
          return this.include;
        }

        public boolean hasInclude() { 
          if (this.include == null)
            return false;
          for (ConceptSetComponent item : this.include)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #include} (Include one or more codes from a code system.)
         */
    // syntactic sugar
        public ConceptSetComponent addInclude() { //3
          ConceptSetComponent t = new ConceptSetComponent();
          if (this.include == null)
            this.include = new ArrayList<ConceptSetComponent>();
          this.include.add(t);
          return t;
        }

        /**
         * @return {@link #exclude} (Exclude one or more codes from the value set.)
         */
        public List<ConceptSetComponent> getExclude() { 
          if (this.exclude == null)
            this.exclude = new ArrayList<ConceptSetComponent>();
          return this.exclude;
        }

        public boolean hasExclude() { 
          if (this.exclude == null)
            return false;
          for (ConceptSetComponent item : this.exclude)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #exclude} (Exclude one or more codes from the value set.)
         */
    // syntactic sugar
        public ConceptSetComponent addExclude() { //3
          ConceptSetComponent t = new ConceptSetComponent();
          if (this.exclude == null)
            this.exclude = new ArrayList<ConceptSetComponent>();
          this.exclude.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("import", "uri", "Includes the contents of the referenced value set as a part of the contents of this value set.", 0, java.lang.Integer.MAX_VALUE, import_));
          childrenList.add(new Property("include", "", "Include one or more codes from a code system.", 0, java.lang.Integer.MAX_VALUE, include));
          childrenList.add(new Property("exclude", "@ValueSet.compose.include", "Exclude one or more codes from the value set.", 0, java.lang.Integer.MAX_VALUE, exclude));
        }

      public ValueSetComposeComponent copy() {
        ValueSetComposeComponent dst = new ValueSetComposeComponent();
        copyValues(dst);
        if (import_ != null) {
          dst.import_ = new ArrayList<UriType>();
          for (UriType i : import_)
            dst.import_.add(i.copy());
        };
        if (include != null) {
          dst.include = new ArrayList<ConceptSetComponent>();
          for (ConceptSetComponent i : include)
            dst.include.add(i.copy());
        };
        if (exclude != null) {
          dst.exclude = new ArrayList<ConceptSetComponent>();
          for (ConceptSetComponent i : exclude)
            dst.exclude.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (import_ == null || import_.isEmpty()) && (include == null || include.isEmpty())
           && (exclude == null || exclude.isEmpty());
      }

  }

    @Block()
    public static class ConceptSetComponent extends BackboneElement {
        /**
         * The code system from which the selected codes come from.
         */
        @Child(name="system", type={UriType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="The system the codes come from", formalDefinition="The code system from which the selected codes come from." )
        protected UriType system;

        /**
         * The version of the code system that the codes are selected from.
         */
        @Child(name="version", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Specific version of the code system referred to", formalDefinition="The version of the code system that the codes are selected from." )
        protected StringType version;

        /**
         * Specifies a concept to be included or excluded.
         */
        @Child(name="concept", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="A concept defined in the system", formalDefinition="Specifies a concept to be included or excluded." )
        protected List<ConceptReferenceComponent> concept;

        /**
         * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
         */
        @Child(name="filter", type={}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Select codes/concepts by their properties (including relationships)", formalDefinition="Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true." )
        protected List<ConceptSetFilterComponent> filter;

        private static final long serialVersionUID = -196054471L;

      public ConceptSetComponent() {
        super();
      }

      public ConceptSetComponent(UriType system) {
        super();
        this.system = system;
      }

        /**
         * @return {@link #system} (The code system from which the selected codes come from.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public UriType getSystemElement() { 
          if (this.system == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptSetComponent.system");
            else if (Configuration.doAutoCreate())
              this.system = new UriType();
          return this.system;
        }

        public boolean hasSystemElement() { 
          return this.system != null && !this.system.isEmpty();
        }

        public boolean hasSystem() { 
          return this.system != null && !this.system.isEmpty();
        }

        /**
         * @param value {@link #system} (The code system from which the selected codes come from.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public ConceptSetComponent setSystemElement(UriType value) { 
          this.system = value;
          return this;
        }

        /**
         * @return The code system from which the selected codes come from.
         */
        public String getSystem() { 
          return this.system == null ? null : this.system.getValue();
        }

        /**
         * @param value The code system from which the selected codes come from.
         */
        public ConceptSetComponent setSystem(String value) { 
            if (this.system == null)
              this.system = new UriType();
            this.system.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version of the code system that the codes are selected from.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptSetComponent.version");
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
         * @param value {@link #version} (The version of the code system that the codes are selected from.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ConceptSetComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version of the code system that the codes are selected from.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version of the code system that the codes are selected from.
         */
        public ConceptSetComponent setVersion(String value) { 
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
         * @return {@link #concept} (Specifies a concept to be included or excluded.)
         */
        public List<ConceptReferenceComponent> getConcept() { 
          if (this.concept == null)
            this.concept = new ArrayList<ConceptReferenceComponent>();
          return this.concept;
        }

        public boolean hasConcept() { 
          if (this.concept == null)
            return false;
          for (ConceptReferenceComponent item : this.concept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #concept} (Specifies a concept to be included or excluded.)
         */
    // syntactic sugar
        public ConceptReferenceComponent addConcept() { //3
          ConceptReferenceComponent t = new ConceptReferenceComponent();
          if (this.concept == null)
            this.concept = new ArrayList<ConceptReferenceComponent>();
          this.concept.add(t);
          return t;
        }

        /**
         * @return {@link #filter} (Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.)
         */
        public List<ConceptSetFilterComponent> getFilter() { 
          if (this.filter == null)
            this.filter = new ArrayList<ConceptSetFilterComponent>();
          return this.filter;
        }

        public boolean hasFilter() { 
          if (this.filter == null)
            return false;
          for (ConceptSetFilterComponent item : this.filter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #filter} (Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.)
         */
    // syntactic sugar
        public ConceptSetFilterComponent addFilter() { //3
          ConceptSetFilterComponent t = new ConceptSetFilterComponent();
          if (this.filter == null)
            this.filter = new ArrayList<ConceptSetFilterComponent>();
          this.filter.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("system", "uri", "The code system from which the selected codes come from.", 0, java.lang.Integer.MAX_VALUE, system));
          childrenList.add(new Property("version", "string", "The version of the code system that the codes are selected from.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("concept", "", "Specifies a concept to be included or excluded.", 0, java.lang.Integer.MAX_VALUE, concept));
          childrenList.add(new Property("filter", "", "Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.", 0, java.lang.Integer.MAX_VALUE, filter));
        }

      public ConceptSetComponent copy() {
        ConceptSetComponent dst = new ConceptSetComponent();
        copyValues(dst);
        dst.system = system == null ? null : system.copy();
        dst.version = version == null ? null : version.copy();
        if (concept != null) {
          dst.concept = new ArrayList<ConceptReferenceComponent>();
          for (ConceptReferenceComponent i : concept)
            dst.concept.add(i.copy());
        };
        if (filter != null) {
          dst.filter = new ArrayList<ConceptSetFilterComponent>();
          for (ConceptSetFilterComponent i : filter)
            dst.filter.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (system == null || system.isEmpty()) && (version == null || version.isEmpty())
           && (concept == null || concept.isEmpty()) && (filter == null || filter.isEmpty());
      }

  }

    @Block()
    public static class ConceptReferenceComponent extends BackboneElement {
        /**
         * Specifies a code for the concept to be included or excluded.
         */
        @Child(name="code", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Code or expression from system", formalDefinition="Specifies a code for the concept to be included or excluded." )
        protected CodeType code;

        /**
         * The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system.
         */
        @Child(name="display", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Test to display for this code for this value set", formalDefinition="The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system." )
        protected StringType display;

        /**
         * Additional representations for this concept when used in this value set - other languages, aliases, specialised purposes, used for particular purposes, etc.
         */
        @Child(name="designation", type={ConceptDefinitionDesignationComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Additional representations for this valueset", formalDefinition="Additional representations for this concept when used in this value set - other languages, aliases, specialised purposes, used for particular purposes, etc." )
        protected List<ConceptDefinitionDesignationComponent> designation;

        private static final long serialVersionUID = -1513912691L;

      public ConceptReferenceComponent() {
        super();
      }

      public ConceptReferenceComponent(CodeType code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Specifies a code for the concept to be included or excluded.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptReferenceComponent.code");
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
         * @param value {@link #code} (Specifies a code for the concept to be included or excluded.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ConceptReferenceComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Specifies a code for the concept to be included or excluded.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Specifies a code for the concept to be included or excluded.
         */
        public ConceptReferenceComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #display} (The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public StringType getDisplayElement() { 
          if (this.display == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptReferenceComponent.display");
            else if (Configuration.doAutoCreate())
              this.display = new StringType();
          return this.display;
        }

        public boolean hasDisplayElement() { 
          return this.display != null && !this.display.isEmpty();
        }

        public boolean hasDisplay() { 
          return this.display != null && !this.display.isEmpty();
        }

        /**
         * @param value {@link #display} (The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public ConceptReferenceComponent setDisplayElement(StringType value) { 
          this.display = value;
          return this;
        }

        /**
         * @return The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system.
         */
        public String getDisplay() { 
          return this.display == null ? null : this.display.getValue();
        }

        /**
         * @param value The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system.
         */
        public ConceptReferenceComponent setDisplay(String value) { 
          if (Utilities.noString(value))
            this.display = null;
          else {
            if (this.display == null)
              this.display = new StringType();
            this.display.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #designation} (Additional representations for this concept when used in this value set - other languages, aliases, specialised purposes, used for particular purposes, etc.)
         */
        public List<ConceptDefinitionDesignationComponent> getDesignation() { 
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          return this.designation;
        }

        public boolean hasDesignation() { 
          if (this.designation == null)
            return false;
          for (ConceptDefinitionDesignationComponent item : this.designation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #designation} (Additional representations for this concept when used in this value set - other languages, aliases, specialised purposes, used for particular purposes, etc.)
         */
    // syntactic sugar
        public ConceptDefinitionDesignationComponent addDesignation() { //3
          ConceptDefinitionDesignationComponent t = new ConceptDefinitionDesignationComponent();
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          this.designation.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "Specifies a code for the concept to be included or excluded.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("display", "string", "The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system.", 0, java.lang.Integer.MAX_VALUE, display));
          childrenList.add(new Property("designation", "@ValueSet.define.concept.designation", "Additional representations for this concept when used in this value set - other languages, aliases, specialised purposes, used for particular purposes, etc.", 0, java.lang.Integer.MAX_VALUE, designation));
        }

      public ConceptReferenceComponent copy() {
        ConceptReferenceComponent dst = new ConceptReferenceComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.display = display == null ? null : display.copy();
        if (designation != null) {
          dst.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          for (ConceptDefinitionDesignationComponent i : designation)
            dst.designation.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (display == null || display.isEmpty())
           && (designation == null || designation.isEmpty());
      }

  }

    @Block()
    public static class ConceptSetFilterComponent extends BackboneElement {
        /**
         * A code that identifies a property defined in the code system.
         */
        @Child(name="property", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="A property defined by the code system", formalDefinition="A code that identifies a property defined in the code system." )
        protected CodeType property;

        /**
         * The kind of operation to perform as a part of the filter criteria.
         */
        @Child(name="op", type={CodeType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="= | is-a | is-not-a | regex | in | not in", formalDefinition="The kind of operation to perform as a part of the filter criteria." )
        protected Enumeration<FilterOperator> op;

        /**
         * The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value.
         */
        @Child(name="value", type={CodeType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Code from the system, or regex criteria", formalDefinition="The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value." )
        protected CodeType value;

        private static final long serialVersionUID = 1985515000L;

      public ConceptSetFilterComponent() {
        super();
      }

      public ConceptSetFilterComponent(CodeType property, Enumeration<FilterOperator> op, CodeType value) {
        super();
        this.property = property;
        this.op = op;
        this.value = value;
      }

        /**
         * @return {@link #property} (A code that identifies a property defined in the code system.). This is the underlying object with id, value and extensions. The accessor "getProperty" gives direct access to the value
         */
        public CodeType getPropertyElement() { 
          if (this.property == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptSetFilterComponent.property");
            else if (Configuration.doAutoCreate())
              this.property = new CodeType();
          return this.property;
        }

        public boolean hasPropertyElement() { 
          return this.property != null && !this.property.isEmpty();
        }

        public boolean hasProperty() { 
          return this.property != null && !this.property.isEmpty();
        }

        /**
         * @param value {@link #property} (A code that identifies a property defined in the code system.). This is the underlying object with id, value and extensions. The accessor "getProperty" gives direct access to the value
         */
        public ConceptSetFilterComponent setPropertyElement(CodeType value) { 
          this.property = value;
          return this;
        }

        /**
         * @return A code that identifies a property defined in the code system.
         */
        public String getProperty() { 
          return this.property == null ? null : this.property.getValue();
        }

        /**
         * @param value A code that identifies a property defined in the code system.
         */
        public ConceptSetFilterComponent setProperty(String value) { 
            if (this.property == null)
              this.property = new CodeType();
            this.property.setValue(value);
          return this;
        }

        /**
         * @return {@link #op} (The kind of operation to perform as a part of the filter criteria.). This is the underlying object with id, value and extensions. The accessor "getOp" gives direct access to the value
         */
        public Enumeration<FilterOperator> getOpElement() { 
          if (this.op == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptSetFilterComponent.op");
            else if (Configuration.doAutoCreate())
              this.op = new Enumeration<FilterOperator>();
          return this.op;
        }

        public boolean hasOpElement() { 
          return this.op != null && !this.op.isEmpty();
        }

        public boolean hasOp() { 
          return this.op != null && !this.op.isEmpty();
        }

        /**
         * @param value {@link #op} (The kind of operation to perform as a part of the filter criteria.). This is the underlying object with id, value and extensions. The accessor "getOp" gives direct access to the value
         */
        public ConceptSetFilterComponent setOpElement(Enumeration<FilterOperator> value) { 
          this.op = value;
          return this;
        }

        /**
         * @return The kind of operation to perform as a part of the filter criteria.
         */
        public FilterOperator getOp() { 
          return this.op == null ? null : this.op.getValue();
        }

        /**
         * @param value The kind of operation to perform as a part of the filter criteria.
         */
        public ConceptSetFilterComponent setOp(FilterOperator value) { 
            if (this.op == null)
              this.op = new Enumeration<FilterOperator>(FilterOperator.ENUM_FACTORY);
            this.op.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public CodeType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptSetFilterComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new CodeType();
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public ConceptSetFilterComponent setValueElement(CodeType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value.
         */
        public ConceptSetFilterComponent setValue(String value) { 
            if (this.value == null)
              this.value = new CodeType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("property", "code", "A code that identifies a property defined in the code system.", 0, java.lang.Integer.MAX_VALUE, property));
          childrenList.add(new Property("op", "code", "The kind of operation to perform as a part of the filter criteria.", 0, java.lang.Integer.MAX_VALUE, op));
          childrenList.add(new Property("value", "code", "The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      public ConceptSetFilterComponent copy() {
        ConceptSetFilterComponent dst = new ConceptSetFilterComponent();
        copyValues(dst);
        dst.property = property == null ? null : property.copy();
        dst.op = op == null ? null : op.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (property == null || property.isEmpty()) && (op == null || op.isEmpty())
           && (value == null || value.isEmpty());
      }

  }

    @Block()
    public static class ValueSetExpansionComponent extends BackboneElement {
        /**
         * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so.
         */
        @Child(name="identifier", type={Identifier.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Uniquely identifies this expansion", formalDefinition="An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so." )
        protected Identifier identifier;

        /**
         * The time at which the expansion was produced by the expanding system.
         */
        @Child(name="timestamp", type={DateTimeType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Time valueset expansion happened", formalDefinition="The time at which the expansion was produced by the expanding system." )
        protected DateTimeType timestamp;

        /**
         * The codes that are contained in the value set expansion.
         */
        @Child(name="contains", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Codes in the value set", formalDefinition="The codes that are contained in the value set expansion." )
        protected List<ValueSetExpansionContainsComponent> contains;

        private static final long serialVersionUID = -1193480660L;

      public ValueSetExpansionComponent() {
        super();
      }

      public ValueSetExpansionComponent(DateTimeType timestamp) {
        super();
        this.timestamp = timestamp;
      }

        /**
         * @return {@link #identifier} (An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier();
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so.)
         */
        public ValueSetExpansionComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #timestamp} (The time at which the expansion was produced by the expanding system.). This is the underlying object with id, value and extensions. The accessor "getTimestamp" gives direct access to the value
         */
        public DateTimeType getTimestampElement() { 
          if (this.timestamp == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionComponent.timestamp");
            else if (Configuration.doAutoCreate())
              this.timestamp = new DateTimeType();
          return this.timestamp;
        }

        public boolean hasTimestampElement() { 
          return this.timestamp != null && !this.timestamp.isEmpty();
        }

        public boolean hasTimestamp() { 
          return this.timestamp != null && !this.timestamp.isEmpty();
        }

        /**
         * @param value {@link #timestamp} (The time at which the expansion was produced by the expanding system.). This is the underlying object with id, value and extensions. The accessor "getTimestamp" gives direct access to the value
         */
        public ValueSetExpansionComponent setTimestampElement(DateTimeType value) { 
          this.timestamp = value;
          return this;
        }

        /**
         * @return The time at which the expansion was produced by the expanding system.
         */
        public Date getTimestamp() { 
          return this.timestamp == null ? null : this.timestamp.getValue();
        }

        /**
         * @param value The time at which the expansion was produced by the expanding system.
         */
        public ValueSetExpansionComponent setTimestamp(Date value) { 
            if (this.timestamp == null)
              this.timestamp = new DateTimeType();
            this.timestamp.setValue(value);
          return this;
        }

        /**
         * @return {@link #contains} (The codes that are contained in the value set expansion.)
         */
        public List<ValueSetExpansionContainsComponent> getContains() { 
          if (this.contains == null)
            this.contains = new ArrayList<ValueSetExpansionContainsComponent>();
          return this.contains;
        }

        public boolean hasContains() { 
          if (this.contains == null)
            return false;
          for (ValueSetExpansionContainsComponent item : this.contains)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #contains} (The codes that are contained in the value set expansion.)
         */
    // syntactic sugar
        public ValueSetExpansionContainsComponent addContains() { //3
          ValueSetExpansionContainsComponent t = new ValueSetExpansionContainsComponent();
          if (this.contains == null)
            this.contains = new ArrayList<ValueSetExpansionContainsComponent>();
          this.contains.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("timestamp", "dateTime", "The time at which the expansion was produced by the expanding system.", 0, java.lang.Integer.MAX_VALUE, timestamp));
          childrenList.add(new Property("contains", "", "The codes that are contained in the value set expansion.", 0, java.lang.Integer.MAX_VALUE, contains));
        }

      public ValueSetExpansionComponent copy() {
        ValueSetExpansionComponent dst = new ValueSetExpansionComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.timestamp = timestamp == null ? null : timestamp.copy();
        if (contains != null) {
          dst.contains = new ArrayList<ValueSetExpansionContainsComponent>();
          for (ValueSetExpansionContainsComponent i : contains)
            dst.contains.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (timestamp == null || timestamp.isEmpty())
           && (contains == null || contains.isEmpty());
      }

  }

    @Block()
    public static class ValueSetExpansionContainsComponent extends BackboneElement {
        /**
         * The system in which the code for this item in the expansion is defined.
         */
        @Child(name="system", type={UriType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="System value for the code", formalDefinition="The system in which the code for this item in the expansion is defined." )
        protected UriType system;

        /**
         * If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value.
         */
        @Child(name="abstract_", type={BooleanType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="If user cannot select this entry", formalDefinition="If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value." )
        protected BooleanType abstract_;

        /**
         * The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence.
         */
        @Child(name="version", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Version in which this code / display is defined", formalDefinition="The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence." )
        protected StringType version;

        /**
         * Code - if blank, this is not a choosable code.
         */
        @Child(name="code", type={CodeType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Code - if blank, this is not a choosable code", formalDefinition="Code - if blank, this is not a choosable code." )
        protected CodeType code;

        /**
         * User display for the concept.
         */
        @Child(name="display", type={StringType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="User display for the concept", formalDefinition="User display for the concept." )
        protected StringType display;

        /**
         * Codes contained in this concept.
         */
        @Child(name="contains", type={ValueSetExpansionContainsComponent.class}, order=6, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Codes contained in this concept", formalDefinition="Codes contained in this concept." )
        protected List<ValueSetExpansionContainsComponent> contains;

        private static final long serialVersionUID = -2038349483L;

      public ValueSetExpansionContainsComponent() {
        super();
      }

        /**
         * @return {@link #system} (The system in which the code for this item in the expansion is defined.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public UriType getSystemElement() { 
          if (this.system == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionContainsComponent.system");
            else if (Configuration.doAutoCreate())
              this.system = new UriType();
          return this.system;
        }

        public boolean hasSystemElement() { 
          return this.system != null && !this.system.isEmpty();
        }

        public boolean hasSystem() { 
          return this.system != null && !this.system.isEmpty();
        }

        /**
         * @param value {@link #system} (The system in which the code for this item in the expansion is defined.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public ValueSetExpansionContainsComponent setSystemElement(UriType value) { 
          this.system = value;
          return this;
        }

        /**
         * @return The system in which the code for this item in the expansion is defined.
         */
        public String getSystem() { 
          return this.system == null ? null : this.system.getValue();
        }

        /**
         * @param value The system in which the code for this item in the expansion is defined.
         */
        public ValueSetExpansionContainsComponent setSystem(String value) { 
          if (Utilities.noString(value))
            this.system = null;
          else {
            if (this.system == null)
              this.system = new UriType();
            this.system.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #abstract_} (If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value.). This is the underlying object with id, value and extensions. The accessor "getAbstract" gives direct access to the value
         */
        public BooleanType getAbstractElement() { 
          if (this.abstract_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionContainsComponent.abstract_");
            else if (Configuration.doAutoCreate())
              this.abstract_ = new BooleanType();
          return this.abstract_;
        }

        public boolean hasAbstractElement() { 
          return this.abstract_ != null && !this.abstract_.isEmpty();
        }

        public boolean hasAbstract() { 
          return this.abstract_ != null && !this.abstract_.isEmpty();
        }

        /**
         * @param value {@link #abstract_} (If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value.). This is the underlying object with id, value and extensions. The accessor "getAbstract" gives direct access to the value
         */
        public ValueSetExpansionContainsComponent setAbstractElement(BooleanType value) { 
          this.abstract_ = value;
          return this;
        }

        /**
         * @return If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value.
         */
        public boolean getAbstract() { 
          return this.abstract_ == null ? false : this.abstract_.getValue();
        }

        /**
         * @param value If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value.
         */
        public ValueSetExpansionContainsComponent setAbstract(boolean value) { 
          if (value == false)
            this.abstract_ = null;
          else {
            if (this.abstract_ == null)
              this.abstract_ = new BooleanType();
            this.abstract_.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #version} (The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionContainsComponent.version");
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
         * @param value {@link #version} (The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ValueSetExpansionContainsComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence.
         */
        public ValueSetExpansionContainsComponent setVersion(String value) { 
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
         * @return {@link #code} (Code - if blank, this is not a choosable code.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionContainsComponent.code");
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
         * @param value {@link #code} (Code - if blank, this is not a choosable code.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ValueSetExpansionContainsComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Code - if blank, this is not a choosable code.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Code - if blank, this is not a choosable code.
         */
        public ValueSetExpansionContainsComponent setCode(String value) { 
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
         * @return {@link #display} (User display for the concept.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public StringType getDisplayElement() { 
          if (this.display == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionContainsComponent.display");
            else if (Configuration.doAutoCreate())
              this.display = new StringType();
          return this.display;
        }

        public boolean hasDisplayElement() { 
          return this.display != null && !this.display.isEmpty();
        }

        public boolean hasDisplay() { 
          return this.display != null && !this.display.isEmpty();
        }

        /**
         * @param value {@link #display} (User display for the concept.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public ValueSetExpansionContainsComponent setDisplayElement(StringType value) { 
          this.display = value;
          return this;
        }

        /**
         * @return User display for the concept.
         */
        public String getDisplay() { 
          return this.display == null ? null : this.display.getValue();
        }

        /**
         * @param value User display for the concept.
         */
        public ValueSetExpansionContainsComponent setDisplay(String value) { 
          if (Utilities.noString(value))
            this.display = null;
          else {
            if (this.display == null)
              this.display = new StringType();
            this.display.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #contains} (Codes contained in this concept.)
         */
        public List<ValueSetExpansionContainsComponent> getContains() { 
          if (this.contains == null)
            this.contains = new ArrayList<ValueSetExpansionContainsComponent>();
          return this.contains;
        }

        public boolean hasContains() { 
          if (this.contains == null)
            return false;
          for (ValueSetExpansionContainsComponent item : this.contains)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #contains} (Codes contained in this concept.)
         */
    // syntactic sugar
        public ValueSetExpansionContainsComponent addContains() { //3
          ValueSetExpansionContainsComponent t = new ValueSetExpansionContainsComponent();
          if (this.contains == null)
            this.contains = new ArrayList<ValueSetExpansionContainsComponent>();
          this.contains.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("system", "uri", "The system in which the code for this item in the expansion is defined.", 0, java.lang.Integer.MAX_VALUE, system));
          childrenList.add(new Property("abstract", "boolean", "If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value.", 0, java.lang.Integer.MAX_VALUE, abstract_));
          childrenList.add(new Property("version", "string", "The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("code", "code", "Code - if blank, this is not a choosable code.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("display", "string", "User display for the concept.", 0, java.lang.Integer.MAX_VALUE, display));
          childrenList.add(new Property("contains", "@ValueSet.expansion.contains", "Codes contained in this concept.", 0, java.lang.Integer.MAX_VALUE, contains));
        }

      public ValueSetExpansionContainsComponent copy() {
        ValueSetExpansionContainsComponent dst = new ValueSetExpansionContainsComponent();
        copyValues(dst);
        dst.system = system == null ? null : system.copy();
        dst.abstract_ = abstract_ == null ? null : abstract_.copy();
        dst.version = version == null ? null : version.copy();
        dst.code = code == null ? null : code.copy();
        dst.display = display == null ? null : display.copy();
        if (contains != null) {
          dst.contains = new ArrayList<ValueSetExpansionContainsComponent>();
          for (ValueSetExpansionContainsComponent i : contains)
            dst.contains.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (system == null || system.isEmpty()) && (abstract_ == null || abstract_.isEmpty())
           && (version == null || version.isEmpty()) && (code == null || code.isEmpty()) && (display == null || display.isEmpty())
           && (contains == null || contains.isEmpty());
      }

  }

    /**
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).
     */
    @Child(name="identifier", type={UriType.class}, order=-1, min=0, max=1)
    @Description(shortDefinition="Globally unique logical id for  value set", formalDefinition="The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)." )
    protected UriType identifier;

    /**
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    @Child(name="version", type={StringType.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Logical id for this version of the value set", formalDefinition="The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp." )
    protected StringType version;

    /**
     * A free text natural language name describing the value set.
     */
    @Child(name="name", type={StringType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Informal name for this value set", formalDefinition="A free text natural language name describing the value set." )
    protected StringType name;

    /**
     * This should describe "the semantic space" to be included in the value set. This can also describe the approach taken to build the value set.
     */
    @Child(name="purpose", type={StringType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Textual description of the intended scope and use", formalDefinition="This should describe 'the semantic space' to be included in the value set. This can also describe the approach taken to build the value set." )
    protected StringType purpose;

    /**
     * If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change.
     */
    @Child(name="immutable", type={BooleanType.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Indicates whether or not any change to the content logical definition may occur", formalDefinition="If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change." )
    protected BooleanType immutable;

    /**
     * The name of the individual or organization that published the value set.
     */
    @Child(name="publisher", type={StringType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the value set." )
    protected StringType publisher;

    /**
     * Contacts of the publisher to assist a user in finding and communicating with the publisher.
     */
    @Child(name="telecom", type={ContactPoint.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contact information of the publisher", formalDefinition="Contacts of the publisher to assist a user in finding and communicating with the publisher." )
    protected List<ContactPoint> telecom;

    /**
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set.
     */
    @Child(name="description", type={StringType.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Human language description of the value set", formalDefinition="A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set." )
    protected StringType description;

    /**
     * A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set.
     */
    @Child(name="copyright", type={StringType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="Publishing restrictions for the value set", formalDefinition="A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set." )
    protected StringType copyright;

    /**
     * The status of the value set.
     */
    @Child(name="status", type={CodeType.class}, order=8, min=1, max=1)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the value set." )
    protected Enumeration<ValuesetStatus> status;

    /**
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name="experimental", type={BooleanType.class}, order=9, min=0, max=1)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * Whether this is intended to be used with an extensible binding or not.
     */
    @Child(name="extensible", type={BooleanType.class}, order=10, min=0, max=1)
    @Description(shortDefinition="Whether this is intended to be used with an extensible binding", formalDefinition="Whether this is intended to be used with an extensible binding or not." )
    protected BooleanType extensible;

    /**
     * The date that the value set status was last changed.
     */
    @Child(name="date", type={DateTimeType.class}, order=11, min=0, max=1)
    @Description(shortDefinition="Date for given status", formalDefinition="The date that the value set status was last changed." )
    protected DateTimeType date;

    /**
     * If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.
     */
    @Child(name="stableDate", type={DateType.class}, order=12, min=0, max=1)
    @Description(shortDefinition="Fixed date for the version of all referenced code systems and value sets", formalDefinition="If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date." )
    protected DateType stableDate;

    /**
     * When value set defines its own codes.
     */
    @Child(name="define", type={}, order=13, min=0, max=1)
    @Description(shortDefinition="When value set defines its own codes", formalDefinition="When value set defines its own codes." )
    protected ValueSetDefineComponent define;

    /**
     * When value set includes codes from elsewhere.
     */
    @Child(name="compose", type={}, order=14, min=0, max=1)
    @Description(shortDefinition="When value set includes codes from elsewhere", formalDefinition="When value set includes codes from elsewhere." )
    protected ValueSetComposeComponent compose;

    /**
     * A value set can also be "expanded", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed.
     */
    @Child(name="expansion", type={}, order=15, min=0, max=1)
    @Description(shortDefinition="Used when the value set is 'expanded'", formalDefinition="A value set can also be 'expanded', where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed." )
    protected ValueSetExpansionComponent expansion;

    private static final long serialVersionUID = -1119903575L;

    public ValueSet() {
      super();
    }

    public ValueSet(Enumeration<ValuesetStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
     */
    public UriType getIdentifierElement() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new UriType();
      return this.identifier;
    }

    public boolean hasIdentifierElement() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
     */
    public ValueSet setIdentifierElement(UriType value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).
     */
    public String getIdentifier() { 
      return this.identifier == null ? null : this.identifier.getValue();
    }

    /**
     * @param value The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).
     */
    public ValueSet setIdentifier(String value) { 
      if (Utilities.noString(value))
        this.identifier = null;
      else {
        if (this.identifier == null)
          this.identifier = new UriType();
        this.identifier.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ValueSet setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public ValueSet setVersion(String value) { 
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
     * @return {@link #name} (A free text natural language name describing the value set.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.name");
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
     * @param value {@link #name} (A free text natural language name describing the value set.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ValueSet setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name describing the value set.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name describing the value set.
     */
    public ValueSet setName(String value) { 
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
     * @return {@link #purpose} (This should describe "the semantic space" to be included in the value set. This can also describe the approach taken to build the value set.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public StringType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new StringType();
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (This should describe "the semantic space" to be included in the value set. This can also describe the approach taken to build the value set.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public ValueSet setPurposeElement(StringType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return This should describe "the semantic space" to be included in the value set. This can also describe the approach taken to build the value set.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value This should describe "the semantic space" to be included in the value set. This can also describe the approach taken to build the value set.
     */
    public ValueSet setPurpose(String value) { 
      if (Utilities.noString(value))
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new StringType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #immutable} (If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change.). This is the underlying object with id, value and extensions. The accessor "getImmutable" gives direct access to the value
     */
    public BooleanType getImmutableElement() { 
      if (this.immutable == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.immutable");
        else if (Configuration.doAutoCreate())
          this.immutable = new BooleanType();
      return this.immutable;
    }

    public boolean hasImmutableElement() { 
      return this.immutable != null && !this.immutable.isEmpty();
    }

    public boolean hasImmutable() { 
      return this.immutable != null && !this.immutable.isEmpty();
    }

    /**
     * @param value {@link #immutable} (If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change.). This is the underlying object with id, value and extensions. The accessor "getImmutable" gives direct access to the value
     */
    public ValueSet setImmutableElement(BooleanType value) { 
      this.immutable = value;
      return this;
    }

    /**
     * @return If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change.
     */
    public boolean getImmutable() { 
      return this.immutable == null ? false : this.immutable.getValue();
    }

    /**
     * @param value If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change.
     */
    public ValueSet setImmutable(boolean value) { 
      if (value == false)
        this.immutable = null;
      else {
        if (this.immutable == null)
          this.immutable = new BooleanType();
        this.immutable.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the value set.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the value set.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ValueSet setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the value set.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the value set.
     */
    public ValueSet setPublisher(String value) { 
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
     * @return {@link #description} (A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.description");
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
     * @param value {@link #description} (A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ValueSet setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set.
     */
    public ValueSet setDescription(String value) { 
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
     * @return {@link #copyright} (A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ValueSet setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set.
     */
    public ValueSet setCopyright(String value) { 
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
     * @return {@link #status} (The status of the value set.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ValuesetStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.status");
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
     * @param value {@link #status} (The status of the value set.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ValueSet setStatusElement(Enumeration<ValuesetStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the value set.
     */
    public ValuesetStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the value set.
     */
    public ValueSet setStatus(ValuesetStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ValuesetStatus>(ValuesetStatus.ENUM_FACTORY);
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.experimental");
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
     * @param value {@link #experimental} (This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ValueSet setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null ? false : this.experimental.getValue();
    }

    /**
     * @param value This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public ValueSet setExperimental(boolean value) { 
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
     * @return {@link #extensible} (Whether this is intended to be used with an extensible binding or not.). This is the underlying object with id, value and extensions. The accessor "getExtensible" gives direct access to the value
     */
    public BooleanType getExtensibleElement() { 
      if (this.extensible == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.extensible");
        else if (Configuration.doAutoCreate())
          this.extensible = new BooleanType();
      return this.extensible;
    }

    public boolean hasExtensibleElement() { 
      return this.extensible != null && !this.extensible.isEmpty();
    }

    public boolean hasExtensible() { 
      return this.extensible != null && !this.extensible.isEmpty();
    }

    /**
     * @param value {@link #extensible} (Whether this is intended to be used with an extensible binding or not.). This is the underlying object with id, value and extensions. The accessor "getExtensible" gives direct access to the value
     */
    public ValueSet setExtensibleElement(BooleanType value) { 
      this.extensible = value;
      return this;
    }

    /**
     * @return Whether this is intended to be used with an extensible binding or not.
     */
    public boolean getExtensible() { 
      return this.extensible == null ? false : this.extensible.getValue();
    }

    /**
     * @param value Whether this is intended to be used with an extensible binding or not.
     */
    public ValueSet setExtensible(boolean value) { 
      if (value == false)
        this.extensible = null;
      else {
        if (this.extensible == null)
          this.extensible = new BooleanType();
        this.extensible.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #date} (The date that the value set status was last changed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.date");
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
     * @param value {@link #date} (The date that the value set status was last changed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ValueSet setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that the value set status was last changed.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that the value set status was last changed.
     */
    public ValueSet setDate(Date value) { 
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
     * @return {@link #stableDate} (If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.). This is the underlying object with id, value and extensions. The accessor "getStableDate" gives direct access to the value
     */
    public DateType getStableDateElement() { 
      if (this.stableDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.stableDate");
        else if (Configuration.doAutoCreate())
          this.stableDate = new DateType();
      return this.stableDate;
    }

    public boolean hasStableDateElement() { 
      return this.stableDate != null && !this.stableDate.isEmpty();
    }

    public boolean hasStableDate() { 
      return this.stableDate != null && !this.stableDate.isEmpty();
    }

    /**
     * @param value {@link #stableDate} (If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.). This is the underlying object with id, value and extensions. The accessor "getStableDate" gives direct access to the value
     */
    public ValueSet setStableDateElement(DateType value) { 
      this.stableDate = value;
      return this;
    }

    /**
     * @return If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.
     */
    public Date getStableDate() { 
      return this.stableDate == null ? null : this.stableDate.getValue();
    }

    /**
     * @param value If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.
     */
    public ValueSet setStableDate(Date value) { 
      if (value == null)
        this.stableDate = null;
      else {
        if (this.stableDate == null)
          this.stableDate = new DateType();
        this.stableDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #define} (When value set defines its own codes.)
     */
    public ValueSetDefineComponent getDefine() { 
      if (this.define == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.define");
        else if (Configuration.doAutoCreate())
          this.define = new ValueSetDefineComponent();
      return this.define;
    }

    public boolean hasDefine() { 
      return this.define != null && !this.define.isEmpty();
    }

    /**
     * @param value {@link #define} (When value set defines its own codes.)
     */
    public ValueSet setDefine(ValueSetDefineComponent value) { 
      this.define = value;
      return this;
    }

    /**
     * @return {@link #compose} (When value set includes codes from elsewhere.)
     */
    public ValueSetComposeComponent getCompose() { 
      if (this.compose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.compose");
        else if (Configuration.doAutoCreate())
          this.compose = new ValueSetComposeComponent();
      return this.compose;
    }

    public boolean hasCompose() { 
      return this.compose != null && !this.compose.isEmpty();
    }

    /**
     * @param value {@link #compose} (When value set includes codes from elsewhere.)
     */
    public ValueSet setCompose(ValueSetComposeComponent value) { 
      this.compose = value;
      return this;
    }

    /**
     * @return {@link #expansion} (A value set can also be "expanded", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed.)
     */
    public ValueSetExpansionComponent getExpansion() { 
      if (this.expansion == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.expansion");
        else if (Configuration.doAutoCreate())
          this.expansion = new ValueSetExpansionComponent();
      return this.expansion;
    }

    public boolean hasExpansion() { 
      return this.expansion != null && !this.expansion.isEmpty();
    }

    /**
     * @param value {@link #expansion} (A value set can also be "expanded", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed.)
     */
    public ValueSet setExpansion(ValueSetExpansionComponent value) { 
      this.expansion = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "uri", "The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name describing the value set.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("purpose", "string", "This should describe 'the semantic space' to be included in the value set. This can also describe the approach taken to build the value set.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("immutable", "boolean", "If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change.", 0, java.lang.Integer.MAX_VALUE, immutable));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the value set.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("telecom", "ContactPoint", "Contacts of the publisher to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("description", "string", "A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("status", "code", "The status of the value set.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("extensible", "boolean", "Whether this is intended to be used with an extensible binding or not.", 0, java.lang.Integer.MAX_VALUE, extensible));
        childrenList.add(new Property("date", "dateTime", "The date that the value set status was last changed.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("stableDate", "date", "If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.", 0, java.lang.Integer.MAX_VALUE, stableDate));
        childrenList.add(new Property("define", "", "When value set defines its own codes.", 0, java.lang.Integer.MAX_VALUE, define));
        childrenList.add(new Property("compose", "", "When value set includes codes from elsewhere.", 0, java.lang.Integer.MAX_VALUE, compose));
        childrenList.add(new Property("expansion", "", "A value set can also be 'expanded', where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed.", 0, java.lang.Integer.MAX_VALUE, expansion));
      }

      public ValueSet copy() {
        ValueSet dst = new ValueSet();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.immutable = immutable == null ? null : immutable.copy();
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
        dst.extensible = extensible == null ? null : extensible.copy();
        dst.date = date == null ? null : date.copy();
        dst.stableDate = stableDate == null ? null : stableDate.copy();
        dst.define = define == null ? null : define.copy();
        dst.compose = compose == null ? null : compose.copy();
        dst.expansion = expansion == null ? null : expansion.copy();
        return dst;
      }

      protected ValueSet typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (name == null || name.isEmpty()) && (purpose == null || purpose.isEmpty()) && (immutable == null || immutable.isEmpty())
           && (publisher == null || publisher.isEmpty()) && (telecom == null || telecom.isEmpty()) && (description == null || description.isEmpty())
           && (copyright == null || copyright.isEmpty()) && (status == null || status.isEmpty()) && (experimental == null || experimental.isEmpty())
           && (extensible == null || extensible.isEmpty()) && (date == null || date.isEmpty()) && (stableDate == null || stableDate.isEmpty())
           && (define == null || define.isEmpty()) && (compose == null || compose.isEmpty()) && (expansion == null || expansion.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ValueSet;
   }

  @SearchParamDefinition(name="system", path="ValueSet.define.system", description="The system for any codes defined by this value set", type="token" )
  public static final String SP_SYSTEM = "system";
  @SearchParamDefinition(name="status", path="ValueSet.status", description="The status of the value set", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="description", path="ValueSet.description", description="Text search in the description of the value set", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="name", path="ValueSet.name", description="The name of the value set", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="code", path="ValueSet.define.concept.code", description="A code defined in the value set", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="date", path="ValueSet.date", description="The value set publication date", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="ValueSet.identifier", description="The identifier of the value set", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="reference", path="ValueSet.compose.include.system", description="A code system included or excluded in the value set or an imported value set", type="token" )
  public static final String SP_REFERENCE = "reference";
  @SearchParamDefinition(name="publisher", path="ValueSet.publisher", description="Name of the publisher of the value set", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="version", path="ValueSet.version", description="The version identifier of the value set", type="token" )
  public static final String SP_VERSION = "version";

}

