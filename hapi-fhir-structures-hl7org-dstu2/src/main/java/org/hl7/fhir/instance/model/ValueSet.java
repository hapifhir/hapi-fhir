package org.hl7.fhir.instance.model;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.Enumerations.*;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * A value set specifies a set of codes drawn from one or more code systems.
 */
@ResourceDef(name="ValueSet", profile="http://hl7.org/fhir/Profile/ValueSet")
public class ValueSet extends DomainResource {

    public enum FilterOperator {
        /**
         * The specified property of the code equals the provided value
         */
        EQUAL, 
        /**
         * Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, including the provided concept itself
         */
        ISA, 
        /**
         * The specified property of the code does not have an is-a relationship with the provided value
         */
        ISNOTA, 
        /**
         * The specified property of the code  matches the regex specified in the provided value
         */
        REGEX, 
        /**
         * The specified property of the code is in the set of codes or concepts specified in the provided value (comma separated list)
         */
        IN, 
        /**
         * The specified property of the code is not in the set of codes or concepts specified in the provided value (comma separated list)
         */
        NOTIN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FilterOperator fromCode(String codeString) throws Exception {
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
        if ("not-in".equals(codeString))
          return NOTIN;
        throw new Exception("Unknown FilterOperator code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EQUAL: return "=";
            case ISA: return "is-a";
            case ISNOTA: return "is-not-a";
            case REGEX: return "regex";
            case IN: return "in";
            case NOTIN: return "not-in";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case EQUAL: return "http://hl7.org/fhir/filter-operator";
            case ISA: return "http://hl7.org/fhir/filter-operator";
            case ISNOTA: return "http://hl7.org/fhir/filter-operator";
            case REGEX: return "http://hl7.org/fhir/filter-operator";
            case IN: return "http://hl7.org/fhir/filter-operator";
            case NOTIN: return "http://hl7.org/fhir/filter-operator";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case EQUAL: return "The specified property of the code equals the provided value";
            case ISA: return "Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, including the provided concept itself";
            case ISNOTA: return "The specified property of the code does not have an is-a relationship with the provided value";
            case REGEX: return "The specified property of the code  matches the regex specified in the provided value";
            case IN: return "The specified property of the code is in the set of codes or concepts specified in the provided value (comma separated list)";
            case NOTIN: return "The specified property of the code is not in the set of codes or concepts specified in the provided value (comma separated list)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EQUAL: return "Equals";
            case ISA: return "Is A (by subsumption)";
            case ISNOTA: return "Not (Is A) (by subsumption)";
            case REGEX: return "Regular Expression";
            case IN: return "In Set";
            case NOTIN: return "Not in Set";
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
        if ("not-in".equals(codeString))
          return FilterOperator.NOTIN;
        throw new IllegalArgumentException("Unknown FilterOperator code '"+codeString+"'");
        }
    public String toCode(FilterOperator code) {
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
        return "not-in";
      return "?";
      }
    }

    @Block()
    public static class ValueSetContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the value set.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of a individual to contact", formalDefinition="The name of an individual to contact regarding the value set." )
        protected StringType name;

        /**
         * Contact details for individual (if a name was provided) or the publisher.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Contact details for individual or publisher", formalDefinition="Contact details for individual (if a name was provided) or the publisher." )
        protected List<ContactPoint> telecom;

        private static final long serialVersionUID = -1179697803L;

    /*
     * Constructor
     */
      public ValueSetContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the value set.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the value set.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ValueSetContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the value set.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the value set.
         */
        public ValueSetContactComponent setName(String value) { 
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
         * @return {@link #telecom} (Contact details for individual (if a name was provided) or the publisher.)
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
         * @return {@link #telecom} (Contact details for individual (if a name was provided) or the publisher.)
         */
    // syntactic sugar
        public ContactPoint addTelecom() { //3
          ContactPoint t = new ContactPoint();
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return t;
        }

    // syntactic sugar
        public ValueSetContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the value set.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      public ValueSetContactComponent copy() {
        ValueSetContactComponent dst = new ValueSetContactComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ValueSetContactComponent))
          return false;
        ValueSetContactComponent o = (ValueSetContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ValueSetContactComponent))
          return false;
        ValueSetContactComponent o = (ValueSetContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  }

    @Block()
    public static class ValueSetCodeSystemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system.
         */
        @Child(name = "system", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="URI to identify the code system (e.g. in Coding.system)", formalDefinition="An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system." )
        protected UriType system;

        /**
         * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked.
         */
        @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Version (for use in Coding.version)", formalDefinition="The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked." )
        protected StringType version;

        /**
         * If code comparison is case sensitive when codes within this system are compared to each other.
         */
        @Child(name = "caseSensitive", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="If code comparison is case sensitive", formalDefinition="If code comparison is case sensitive when codes within this system are compared to each other." )
        protected BooleanType caseSensitive;

        /**
         * Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are.
         */
        @Child(name = "concept", type = {}, order=4, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Concepts in the code system", formalDefinition="Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are." )
        protected List<ConceptDefinitionComponent> concept;

        private static final long serialVersionUID = -1109401192L;

    /*
     * Constructor
     */
      public ValueSetCodeSystemComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ValueSetCodeSystemComponent(UriType system) {
        super();
        this.system = system;
      }

        /**
         * @return {@link #system} (An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public UriType getSystemElement() { 
          if (this.system == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetCodeSystemComponent.system");
            else if (Configuration.doAutoCreate())
              this.system = new UriType(); // bb
          return this.system;
        }

        public boolean hasSystemElement() { 
          return this.system != null && !this.system.isEmpty();
        }

        public boolean hasSystem() { 
          return this.system != null && !this.system.isEmpty();
        }

        /**
         * @param value {@link #system} (An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public ValueSetCodeSystemComponent setSystemElement(UriType value) { 
          this.system = value;
          return this;
        }

        /**
         * @return An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system.
         */
        public String getSystem() { 
          return this.system == null ? null : this.system.getValue();
        }

        /**
         * @param value An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system.
         */
        public ValueSetCodeSystemComponent setSystem(String value) { 
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
              throw new Error("Attempt to auto-create ValueSetCodeSystemComponent.version");
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
         * @param value {@link #version} (The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ValueSetCodeSystemComponent setVersionElement(StringType value) { 
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
        public ValueSetCodeSystemComponent setVersion(String value) { 
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
              throw new Error("Attempt to auto-create ValueSetCodeSystemComponent.caseSensitive");
            else if (Configuration.doAutoCreate())
              this.caseSensitive = new BooleanType(); // bb
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
        public ValueSetCodeSystemComponent setCaseSensitiveElement(BooleanType value) { 
          this.caseSensitive = value;
          return this;
        }

        /**
         * @return If code comparison is case sensitive when codes within this system are compared to each other.
         */
        public boolean getCaseSensitive() { 
          return this.caseSensitive == null || this.caseSensitive.isEmpty() ? false : this.caseSensitive.getValue();
        }

        /**
         * @param value If code comparison is case sensitive when codes within this system are compared to each other.
         */
        public ValueSetCodeSystemComponent setCaseSensitive(boolean value) { 
            if (this.caseSensitive == null)
              this.caseSensitive = new BooleanType();
            this.caseSensitive.setValue(value);
          return this;
        }

        /**
         * @return {@link #concept} (Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are.)
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
         * @return {@link #concept} (Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are.)
         */
    // syntactic sugar
        public ConceptDefinitionComponent addConcept() { //3
          ConceptDefinitionComponent t = new ConceptDefinitionComponent();
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          this.concept.add(t);
          return t;
        }

    // syntactic sugar
        public ValueSetCodeSystemComponent addConcept(ConceptDefinitionComponent t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          this.concept.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("system", "uri", "An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system.", 0, java.lang.Integer.MAX_VALUE, system));
          childrenList.add(new Property("version", "string", "The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("caseSensitive", "boolean", "If code comparison is case sensitive when codes within this system are compared to each other.", 0, java.lang.Integer.MAX_VALUE, caseSensitive));
          childrenList.add(new Property("concept", "", "Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are.", 0, java.lang.Integer.MAX_VALUE, concept));
        }

      public ValueSetCodeSystemComponent copy() {
        ValueSetCodeSystemComponent dst = new ValueSetCodeSystemComponent();
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ValueSetCodeSystemComponent))
          return false;
        ValueSetCodeSystemComponent o = (ValueSetCodeSystemComponent) other;
        return compareDeep(system, o.system, true) && compareDeep(version, o.version, true) && compareDeep(caseSensitive, o.caseSensitive, true)
           && compareDeep(concept, o.concept, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ValueSetCodeSystemComponent))
          return false;
        ValueSetCodeSystemComponent o = (ValueSetCodeSystemComponent) other;
        return compareValues(system, o.system, true) && compareValues(version, o.version, true) && compareValues(caseSensitive, o.caseSensitive, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (system == null || system.isEmpty()) && (version == null || version.isEmpty())
           && (caseSensitive == null || caseSensitive.isEmpty()) && (concept == null || concept.isEmpty())
          ;
      }

  }

    @Block()
    public static class ConceptDefinitionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code - a text symbol - that uniquely identifies the concept within the code system.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code that identifies concept", formalDefinition="A code - a text symbol - that uniquely identifies the concept within the code system." )
        protected CodeType code;

        /**
         * If this code is not for use as a real concept.
         */
        @Child(name = "abstract", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="If this code is not for use as a real concept", formalDefinition="If this code is not for use as a real concept." )
        protected BooleanType abstract_;

        /**
         * A human readable string that is the recommended default way to present this concept to a user.
         */
        @Child(name = "display", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Text to Display to the user", formalDefinition="A human readable string that is the recommended default way to present this concept to a user." )
        protected StringType display;

        /**
         * The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept.
         */
        @Child(name = "definition", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Formal Definition", formalDefinition="The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept." )
        protected StringType definition;

        /**
         * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc.
         */
        @Child(name = "designation", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additional representations for the concept", formalDefinition="Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc." )
        protected List<ConceptDefinitionDesignationComponent> designation;

        /**
         * Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts.
         */
        @Child(name = "concept", type = {ConceptDefinitionComponent.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Child Concepts (is-a / contains / categorises)", formalDefinition="Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts." )
        protected List<ConceptDefinitionComponent> concept;

        private static final long serialVersionUID = -318560292L;

    /*
     * Constructor
     */
      public ConceptDefinitionComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ConceptDefinitionComponent(CodeType code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (A code - a text symbol - that uniquely identifies the concept within the code system.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code - a text symbol - that uniquely identifies the concept within the code system.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ConceptDefinitionComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return A code - a text symbol - that uniquely identifies the concept within the code system.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value A code - a text symbol - that uniquely identifies the concept within the code system.
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
              this.abstract_ = new BooleanType(); // bb
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
          return this.abstract_ == null || this.abstract_.isEmpty() ? false : this.abstract_.getValue();
        }

        /**
         * @param value If this code is not for use as a real concept.
         */
        public ConceptDefinitionComponent setAbstract(boolean value) { 
            if (this.abstract_ == null)
              this.abstract_ = new BooleanType();
            this.abstract_.setValue(value);
          return this;
        }

        /**
         * @return {@link #display} (A human readable string that is the recommended default way to present this concept to a user.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public StringType getDisplayElement() { 
          if (this.display == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionComponent.display");
            else if (Configuration.doAutoCreate())
              this.display = new StringType(); // bb
          return this.display;
        }

        public boolean hasDisplayElement() { 
          return this.display != null && !this.display.isEmpty();
        }

        public boolean hasDisplay() { 
          return this.display != null && !this.display.isEmpty();
        }

        /**
         * @param value {@link #display} (A human readable string that is the recommended default way to present this concept to a user.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public ConceptDefinitionComponent setDisplayElement(StringType value) { 
          this.display = value;
          return this;
        }

        /**
         * @return A human readable string that is the recommended default way to present this concept to a user.
         */
        public String getDisplay() { 
          return this.display == null ? null : this.display.getValue();
        }

        /**
         * @param value A human readable string that is the recommended default way to present this concept to a user.
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
         * @return {@link #definition} (The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public StringType getDefinitionElement() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definition = new StringType(); // bb
          return this.definition;
        }

        public boolean hasDefinitionElement() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public ConceptDefinitionComponent setDefinitionElement(StringType value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept.
         */
        public String getDefinition() { 
          return this.definition == null ? null : this.definition.getValue();
        }

        /**
         * @param value The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept.
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
         * @return {@link #designation} (Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc.)
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
         * @return {@link #designation} (Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc.)
         */
    // syntactic sugar
        public ConceptDefinitionDesignationComponent addDesignation() { //3
          ConceptDefinitionDesignationComponent t = new ConceptDefinitionDesignationComponent();
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          this.designation.add(t);
          return t;
        }

    // syntactic sugar
        public ConceptDefinitionComponent addDesignation(ConceptDefinitionDesignationComponent t) { //3
          if (t == null)
            return this;
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          this.designation.add(t);
          return this;
        }

        /**
         * @return {@link #concept} (Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts.)
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
         * @return {@link #concept} (Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts.)
         */
    // syntactic sugar
        public ConceptDefinitionComponent addConcept() { //3
          ConceptDefinitionComponent t = new ConceptDefinitionComponent();
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          this.concept.add(t);
          return t;
        }

    // syntactic sugar
        public ConceptDefinitionComponent addConcept(ConceptDefinitionComponent t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          this.concept.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "A code - a text symbol - that uniquely identifies the concept within the code system.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("abstract", "boolean", "If this code is not for use as a real concept.", 0, java.lang.Integer.MAX_VALUE, abstract_));
          childrenList.add(new Property("display", "string", "A human readable string that is the recommended default way to present this concept to a user.", 0, java.lang.Integer.MAX_VALUE, display));
          childrenList.add(new Property("definition", "string", "The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept.", 0, java.lang.Integer.MAX_VALUE, definition));
          childrenList.add(new Property("designation", "", "Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc.", 0, java.lang.Integer.MAX_VALUE, designation));
          childrenList.add(new Property("concept", "@ValueSet.codeSystem.concept", "Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts.", 0, java.lang.Integer.MAX_VALUE, concept));
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConceptDefinitionComponent))
          return false;
        ConceptDefinitionComponent o = (ConceptDefinitionComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(abstract_, o.abstract_, true) && compareDeep(display, o.display, true)
           && compareDeep(definition, o.definition, true) && compareDeep(designation, o.designation, true)
           && compareDeep(concept, o.concept, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConceptDefinitionComponent))
          return false;
        ConceptDefinitionComponent o = (ConceptDefinitionComponent) other;
        return compareValues(code, o.code, true) && compareValues(abstract_, o.abstract_, true) && compareValues(display, o.display, true)
           && compareValues(definition, o.definition, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (abstract_ == null || abstract_.isEmpty())
           && (display == null || display.isEmpty()) && (definition == null || definition.isEmpty())
           && (designation == null || designation.isEmpty()) && (concept == null || concept.isEmpty())
          ;
      }

  }

    @Block()
    public static class ConceptDefinitionDesignationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The language this designation is defined for.
         */
        @Child(name = "language", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human language of the designation", formalDefinition="The language this designation is defined for." )
        protected CodeType language;

        /**
         * A code that details how this designation would be used.
         */
        @Child(name = "use", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Details how this designation would be used", formalDefinition="A code that details how this designation would be used." )
        protected Coding use;

        /**
         * The text value for this designation.
         */
        @Child(name = "value", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The text value for this designation", formalDefinition="The text value for this designation." )
        protected StringType value;

        private static final long serialVersionUID = 1515662414L;

    /*
     * Constructor
     */
      public ConceptDefinitionDesignationComponent() {
        super();
      }

    /*
     * Constructor
     */
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
              this.language = new CodeType(); // bb
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
              this.use = new Coding(); // cc
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
              this.value = new StringType(); // bb
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConceptDefinitionDesignationComponent))
          return false;
        ConceptDefinitionDesignationComponent o = (ConceptDefinitionDesignationComponent) other;
        return compareDeep(language, o.language, true) && compareDeep(use, o.use, true) && compareDeep(value, o.value, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConceptDefinitionDesignationComponent))
          return false;
        ConceptDefinitionDesignationComponent o = (ConceptDefinitionDesignationComponent) other;
        return compareValues(language, o.language, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (language == null || language.isEmpty()) && (use == null || use.isEmpty())
           && (value == null || value.isEmpty());
      }

  }

    @Block()
    public static class ValueSetComposeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri.
         */
        @Child(name = "import", type = {UriType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Import the contents of another value set", formalDefinition="Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri." )
        protected List<UriType> import_;

        /**
         * Include one or more codes from a code system.
         */
        @Child(name = "include", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Include one or more codes from a code system", formalDefinition="Include one or more codes from a code system." )
        protected List<ConceptSetComponent> include;

        /**
         * Exclude one or more codes from the value set.
         */
        @Child(name = "exclude", type = {ConceptSetComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Explicitly exclude codes", formalDefinition="Exclude one or more codes from the value set." )
        protected List<ConceptSetComponent> exclude;

        private static final long serialVersionUID = -703166694L;

    /*
     * Constructor
     */
      public ValueSetComposeComponent() {
        super();
      }

        /**
         * @return {@link #import_} (Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri.)
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
         * @return {@link #import_} (Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri.)
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
         * @param value {@link #import_} (Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri.)
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
         * @param value {@link #import_} (Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri.)
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

    // syntactic sugar
        public ValueSetComposeComponent addInclude(ConceptSetComponent t) { //3
          if (t == null)
            return this;
          if (this.include == null)
            this.include = new ArrayList<ConceptSetComponent>();
          this.include.add(t);
          return this;
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

    // syntactic sugar
        public ValueSetComposeComponent addExclude(ConceptSetComponent t) { //3
          if (t == null)
            return this;
          if (this.exclude == null)
            this.exclude = new ArrayList<ConceptSetComponent>();
          this.exclude.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("import", "uri", "Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri.", 0, java.lang.Integer.MAX_VALUE, import_));
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ValueSetComposeComponent))
          return false;
        ValueSetComposeComponent o = (ValueSetComposeComponent) other;
        return compareDeep(import_, o.import_, true) && compareDeep(include, o.include, true) && compareDeep(exclude, o.exclude, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ValueSetComposeComponent))
          return false;
        ValueSetComposeComponent o = (ValueSetComposeComponent) other;
        return compareValues(import_, o.import_, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (import_ == null || import_.isEmpty()) && (include == null || include.isEmpty())
           && (exclude == null || exclude.isEmpty());
      }

  }

    @Block()
    public static class ConceptSetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An absolute URI which is the code system from which the selected codes come from.
         */
        @Child(name = "system", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The system the codes come from", formalDefinition="An absolute URI which is the code system from which the selected codes come from." )
        protected UriType system;

        /**
         * The version of the code system that the codes are selected from.
         */
        @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specific version of the code system referred to", formalDefinition="The version of the code system that the codes are selected from." )
        protected StringType version;

        /**
         * Specifies a concept to be included or excluded.
         */
        @Child(name = "concept", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A concept defined in the system", formalDefinition="Specifies a concept to be included or excluded." )
        protected List<ConceptReferenceComponent> concept;

        /**
         * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
         */
        @Child(name = "filter", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Select codes/concepts by their properties (including relationships)", formalDefinition="Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true." )
        protected List<ConceptSetFilterComponent> filter;

        private static final long serialVersionUID = -196054471L;

    /*
     * Constructor
     */
      public ConceptSetComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ConceptSetComponent(UriType system) {
        super();
        this.system = system;
      }

        /**
         * @return {@link #system} (An absolute URI which is the code system from which the selected codes come from.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public UriType getSystemElement() { 
          if (this.system == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptSetComponent.system");
            else if (Configuration.doAutoCreate())
              this.system = new UriType(); // bb
          return this.system;
        }

        public boolean hasSystemElement() { 
          return this.system != null && !this.system.isEmpty();
        }

        public boolean hasSystem() { 
          return this.system != null && !this.system.isEmpty();
        }

        /**
         * @param value {@link #system} (An absolute URI which is the code system from which the selected codes come from.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public ConceptSetComponent setSystemElement(UriType value) { 
          this.system = value;
          return this;
        }

        /**
         * @return An absolute URI which is the code system from which the selected codes come from.
         */
        public String getSystem() { 
          return this.system == null ? null : this.system.getValue();
        }

        /**
         * @param value An absolute URI which is the code system from which the selected codes come from.
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

    // syntactic sugar
        public ConceptSetComponent addConcept(ConceptReferenceComponent t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<ConceptReferenceComponent>();
          this.concept.add(t);
          return this;
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

    // syntactic sugar
        public ConceptSetComponent addFilter(ConceptSetFilterComponent t) { //3
          if (t == null)
            return this;
          if (this.filter == null)
            this.filter = new ArrayList<ConceptSetFilterComponent>();
          this.filter.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("system", "uri", "An absolute URI which is the code system from which the selected codes come from.", 0, java.lang.Integer.MAX_VALUE, system));
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConceptSetComponent))
          return false;
        ConceptSetComponent o = (ConceptSetComponent) other;
        return compareDeep(system, o.system, true) && compareDeep(version, o.version, true) && compareDeep(concept, o.concept, true)
           && compareDeep(filter, o.filter, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConceptSetComponent))
          return false;
        ConceptSetComponent o = (ConceptSetComponent) other;
        return compareValues(system, o.system, true) && compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (system == null || system.isEmpty()) && (version == null || version.isEmpty())
           && (concept == null || concept.isEmpty()) && (filter == null || filter.isEmpty());
      }

  }

    @Block()
    public static class ConceptReferenceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specifies a code for the concept to be included or excluded.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code or expression from system", formalDefinition="Specifies a code for the concept to be included or excluded." )
        protected CodeType code;

        /**
         * The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system.
         */
        @Child(name = "display", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Test to display for this code for this value set", formalDefinition="The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system." )
        protected StringType display;

        /**
         * Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc.
         */
        @Child(name = "designation", type = {ConceptDefinitionDesignationComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additional representations for this valueset", formalDefinition="Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc." )
        protected List<ConceptDefinitionDesignationComponent> designation;

        private static final long serialVersionUID = -1513912691L;

    /*
     * Constructor
     */
      public ConceptReferenceComponent() {
        super();
      }

    /*
     * Constructor
     */
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
              this.code = new CodeType(); // bb
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
              this.display = new StringType(); // bb
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
         * @return {@link #designation} (Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc.)
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
         * @return {@link #designation} (Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc.)
         */
    // syntactic sugar
        public ConceptDefinitionDesignationComponent addDesignation() { //3
          ConceptDefinitionDesignationComponent t = new ConceptDefinitionDesignationComponent();
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          this.designation.add(t);
          return t;
        }

    // syntactic sugar
        public ConceptReferenceComponent addDesignation(ConceptDefinitionDesignationComponent t) { //3
          if (t == null)
            return this;
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          this.designation.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "Specifies a code for the concept to be included or excluded.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("display", "string", "The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system.", 0, java.lang.Integer.MAX_VALUE, display));
          childrenList.add(new Property("designation", "@ValueSet.codeSystem.concept.designation", "Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc.", 0, java.lang.Integer.MAX_VALUE, designation));
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConceptReferenceComponent))
          return false;
        ConceptReferenceComponent o = (ConceptReferenceComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(display, o.display, true) && compareDeep(designation, o.designation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConceptReferenceComponent))
          return false;
        ConceptReferenceComponent o = (ConceptReferenceComponent) other;
        return compareValues(code, o.code, true) && compareValues(display, o.display, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (display == null || display.isEmpty())
           && (designation == null || designation.isEmpty());
      }

  }

    @Block()
    public static class ConceptSetFilterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code that identifies a property defined in the code system.
         */
        @Child(name = "property", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A property defined by the code system", formalDefinition="A code that identifies a property defined in the code system." )
        protected CodeType property;

        /**
         * The kind of operation to perform as a part of the filter criteria.
         */
        @Child(name = "op", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="= | is-a | is-not-a | regex | in | not-in", formalDefinition="The kind of operation to perform as a part of the filter criteria." )
        protected Enumeration<FilterOperator> op;

        /**
         * The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value.
         */
        @Child(name = "value", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code from the system, or regex criteria", formalDefinition="The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value." )
        protected CodeType value;

        private static final long serialVersionUID = 1985515000L;

    /*
     * Constructor
     */
      public ConceptSetFilterComponent() {
        super();
      }

    /*
     * Constructor
     */
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
              this.property = new CodeType(); // bb
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
              this.op = new Enumeration<FilterOperator>(new FilterOperatorEnumFactory()); // bb
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
              this.op = new Enumeration<FilterOperator>(new FilterOperatorEnumFactory());
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
              this.value = new CodeType(); // bb
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConceptSetFilterComponent))
          return false;
        ConceptSetFilterComponent o = (ConceptSetFilterComponent) other;
        return compareDeep(property, o.property, true) && compareDeep(op, o.op, true) && compareDeep(value, o.value, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConceptSetFilterComponent))
          return false;
        ConceptSetFilterComponent o = (ConceptSetFilterComponent) other;
        return compareValues(property, o.property, true) && compareValues(op, o.op, true) && compareValues(value, o.value, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (property == null || property.isEmpty()) && (op == null || op.isEmpty())
           && (value == null || value.isEmpty());
      }

  }

    @Block()
    public static class ValueSetExpansionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so.
         */
        @Child(name = "identifier", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Uniquely identifies this expansion", formalDefinition="An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so." )
        protected UriType identifier;

        /**
         * The time at which the expansion was produced by the expanding system.
         */
        @Child(name = "timestamp", type = {DateTimeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time valueset expansion happened", formalDefinition="The time at which the expansion was produced by the expanding system." )
        protected DateTimeType timestamp;

        /**
         * The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter.
         */
        @Child(name = "total", type = {IntegerType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total number of codes in the expansion", formalDefinition="The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter." )
        protected IntegerType total;

        /**
         * If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present.
         */
        @Child(name = "offset", type = {IntegerType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Offset at which this resource starts", formalDefinition="If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present." )
        protected IntegerType offset;

        /**
         * A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.
         */
        @Child(name = "parameter", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Parameter that controlled the expansion process", formalDefinition="A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion." )
        protected List<ValueSetExpansionParameterComponent> parameter;

        /**
         * The codes that are contained in the value set expansion.
         */
        @Child(name = "contains", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Codes in the value set", formalDefinition="The codes that are contained in the value set expansion." )
        protected List<ValueSetExpansionContainsComponent> contains;

        private static final long serialVersionUID = -43471993L;

    /*
     * Constructor
     */
      public ValueSetExpansionComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ValueSetExpansionComponent(UriType identifier, DateTimeType timestamp) {
        super();
        this.identifier = identifier;
        this.timestamp = timestamp;
      }

        /**
         * @return {@link #identifier} (An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public UriType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new UriType(); // bb
          return this.identifier;
        }

        public boolean hasIdentifierElement() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public ValueSetExpansionComponent setIdentifierElement(UriType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so.
         */
        public ValueSetExpansionComponent setIdentifier(String value) { 
            if (this.identifier == null)
              this.identifier = new UriType();
            this.identifier.setValue(value);
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
              this.timestamp = new DateTimeType(); // bb
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
         * @return {@link #total} (The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter.). This is the underlying object with id, value and extensions. The accessor "getTotal" gives direct access to the value
         */
        public IntegerType getTotalElement() { 
          if (this.total == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionComponent.total");
            else if (Configuration.doAutoCreate())
              this.total = new IntegerType(); // bb
          return this.total;
        }

        public boolean hasTotalElement() { 
          return this.total != null && !this.total.isEmpty();
        }

        public boolean hasTotal() { 
          return this.total != null && !this.total.isEmpty();
        }

        /**
         * @param value {@link #total} (The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter.). This is the underlying object with id, value and extensions. The accessor "getTotal" gives direct access to the value
         */
        public ValueSetExpansionComponent setTotalElement(IntegerType value) { 
          this.total = value;
          return this;
        }

        /**
         * @return The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter.
         */
        public int getTotal() { 
          return this.total == null || this.total.isEmpty() ? 0 : this.total.getValue();
        }

        /**
         * @param value The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter.
         */
        public ValueSetExpansionComponent setTotal(int value) { 
            if (this.total == null)
              this.total = new IntegerType();
            this.total.setValue(value);
          return this;
        }

        /**
         * @return {@link #offset} (If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present.). This is the underlying object with id, value and extensions. The accessor "getOffset" gives direct access to the value
         */
        public IntegerType getOffsetElement() { 
          if (this.offset == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionComponent.offset");
            else if (Configuration.doAutoCreate())
              this.offset = new IntegerType(); // bb
          return this.offset;
        }

        public boolean hasOffsetElement() { 
          return this.offset != null && !this.offset.isEmpty();
        }

        public boolean hasOffset() { 
          return this.offset != null && !this.offset.isEmpty();
        }

        /**
         * @param value {@link #offset} (If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present.). This is the underlying object with id, value and extensions. The accessor "getOffset" gives direct access to the value
         */
        public ValueSetExpansionComponent setOffsetElement(IntegerType value) { 
          this.offset = value;
          return this;
        }

        /**
         * @return If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present.
         */
        public int getOffset() { 
          return this.offset == null || this.offset.isEmpty() ? 0 : this.offset.getValue();
        }

        /**
         * @param value If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present.
         */
        public ValueSetExpansionComponent setOffset(int value) { 
            if (this.offset == null)
              this.offset = new IntegerType();
            this.offset.setValue(value);
          return this;
        }

        /**
         * @return {@link #parameter} (A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.)
         */
        public List<ValueSetExpansionParameterComponent> getParameter() { 
          if (this.parameter == null)
            this.parameter = new ArrayList<ValueSetExpansionParameterComponent>();
          return this.parameter;
        }

        public boolean hasParameter() { 
          if (this.parameter == null)
            return false;
          for (ValueSetExpansionParameterComponent item : this.parameter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #parameter} (A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.)
         */
    // syntactic sugar
        public ValueSetExpansionParameterComponent addParameter() { //3
          ValueSetExpansionParameterComponent t = new ValueSetExpansionParameterComponent();
          if (this.parameter == null)
            this.parameter = new ArrayList<ValueSetExpansionParameterComponent>();
          this.parameter.add(t);
          return t;
        }

    // syntactic sugar
        public ValueSetExpansionComponent addParameter(ValueSetExpansionParameterComponent t) { //3
          if (t == null)
            return this;
          if (this.parameter == null)
            this.parameter = new ArrayList<ValueSetExpansionParameterComponent>();
          this.parameter.add(t);
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

    // syntactic sugar
        public ValueSetExpansionComponent addContains(ValueSetExpansionContainsComponent t) { //3
          if (t == null)
            return this;
          if (this.contains == null)
            this.contains = new ArrayList<ValueSetExpansionContainsComponent>();
          this.contains.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "uri", "An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("timestamp", "dateTime", "The time at which the expansion was produced by the expanding system.", 0, java.lang.Integer.MAX_VALUE, timestamp));
          childrenList.add(new Property("total", "integer", "The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter.", 0, java.lang.Integer.MAX_VALUE, total));
          childrenList.add(new Property("offset", "integer", "If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present.", 0, java.lang.Integer.MAX_VALUE, offset));
          childrenList.add(new Property("parameter", "", "A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.", 0, java.lang.Integer.MAX_VALUE, parameter));
          childrenList.add(new Property("contains", "", "The codes that are contained in the value set expansion.", 0, java.lang.Integer.MAX_VALUE, contains));
        }

      public ValueSetExpansionComponent copy() {
        ValueSetExpansionComponent dst = new ValueSetExpansionComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.timestamp = timestamp == null ? null : timestamp.copy();
        dst.total = total == null ? null : total.copy();
        dst.offset = offset == null ? null : offset.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<ValueSetExpansionParameterComponent>();
          for (ValueSetExpansionParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        if (contains != null) {
          dst.contains = new ArrayList<ValueSetExpansionContainsComponent>();
          for (ValueSetExpansionContainsComponent i : contains)
            dst.contains.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ValueSetExpansionComponent))
          return false;
        ValueSetExpansionComponent o = (ValueSetExpansionComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(timestamp, o.timestamp, true)
           && compareDeep(total, o.total, true) && compareDeep(offset, o.offset, true) && compareDeep(parameter, o.parameter, true)
           && compareDeep(contains, o.contains, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ValueSetExpansionComponent))
          return false;
        ValueSetExpansionComponent o = (ValueSetExpansionComponent) other;
        return compareValues(identifier, o.identifier, true) && compareValues(timestamp, o.timestamp, true)
           && compareValues(total, o.total, true) && compareValues(offset, o.offset, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (timestamp == null || timestamp.isEmpty())
           && (total == null || total.isEmpty()) && (offset == null || offset.isEmpty()) && (parameter == null || parameter.isEmpty())
           && (contains == null || contains.isEmpty());
      }

  }

    @Block()
    public static class ValueSetExpansionParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the parameter.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name as assigned by server", formalDefinition="The name of the parameter." )
        protected StringType name;

        /**
         * The value of the parameter.
         */
        @Child(name = "value", type = {StringType.class, BooleanType.class, IntegerType.class, DecimalType.class, UriType.class, CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value of the named parameter", formalDefinition="The value of the parameter." )
        protected Type value;

        private static final long serialVersionUID = 1172641169L;

    /*
     * Constructor
     */
      public ValueSetExpansionParameterComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ValueSetExpansionParameterComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (The name of the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionParameterComponent.name");
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
         * @param value {@link #name} (The name of the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ValueSetExpansionParameterComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the parameter.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the parameter.
         */
        public ValueSetExpansionParameterComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value of the parameter.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The value of the parameter.)
         */
        public StringType getValueStringType() throws Exception { 
          if (!(this.value instanceof StringType))
            throw new Exception("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() throws Exception { 
          return this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (The value of the parameter.)
         */
        public BooleanType getValueBooleanType() throws Exception { 
          if (!(this.value instanceof BooleanType))
            throw new Exception("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (BooleanType) this.value;
        }

        public boolean hasValueBooleanType() throws Exception { 
          return this.value instanceof BooleanType;
        }

        /**
         * @return {@link #value} (The value of the parameter.)
         */
        public IntegerType getValueIntegerType() throws Exception { 
          if (!(this.value instanceof IntegerType))
            throw new Exception("Type mismatch: the type IntegerType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (IntegerType) this.value;
        }

        public boolean hasValueIntegerType() throws Exception { 
          return this.value instanceof IntegerType;
        }

        /**
         * @return {@link #value} (The value of the parameter.)
         */
        public DecimalType getValueDecimalType() throws Exception { 
          if (!(this.value instanceof DecimalType))
            throw new Exception("Type mismatch: the type DecimalType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DecimalType) this.value;
        }

        public boolean hasValueDecimalType() throws Exception { 
          return this.value instanceof DecimalType;
        }

        /**
         * @return {@link #value} (The value of the parameter.)
         */
        public UriType getValueUriType() throws Exception { 
          if (!(this.value instanceof UriType))
            throw new Exception("Type mismatch: the type UriType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (UriType) this.value;
        }

        public boolean hasValueUriType() throws Exception { 
          return this.value instanceof UriType;
        }

        /**
         * @return {@link #value} (The value of the parameter.)
         */
        public CodeType getValueCodeType() throws Exception { 
          if (!(this.value instanceof CodeType))
            throw new Exception("Type mismatch: the type CodeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (CodeType) this.value;
        }

        public boolean hasValueCodeType() throws Exception { 
          return this.value instanceof CodeType;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the parameter.)
         */
        public ValueSetExpansionParameterComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of the parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("value[x]", "string|boolean|integer|decimal|uri|code", "The value of the parameter.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      public ValueSetExpansionParameterComponent copy() {
        ValueSetExpansionParameterComponent dst = new ValueSetExpansionParameterComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ValueSetExpansionParameterComponent))
          return false;
        ValueSetExpansionParameterComponent o = (ValueSetExpansionParameterComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ValueSetExpansionParameterComponent))
          return false;
        ValueSetExpansionParameterComponent o = (ValueSetExpansionParameterComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  }

    @Block()
    public static class ValueSetExpansionContainsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An absolute URI which is the code system in which the code for this item in the expansion is defined.
         */
        @Child(name = "system", type = {UriType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="System value for the code", formalDefinition="An absolute URI which is the code system in which the code for this item in the expansion is defined." )
        protected UriType system;

        /**
         * If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value.
         */
        @Child(name = "abstract", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="If user cannot select this entry", formalDefinition="If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value." )
        protected BooleanType abstract_;

        /**
         * The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Version in which this code / display is defined", formalDefinition="The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence." )
        protected StringType version;

        /**
         * The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set.
         */
        @Child(name = "code", type = {CodeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code - if blank, this is not a choosable code", formalDefinition="The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set." )
        protected CodeType code;

        /**
         * The recommended display for this item in the expansion.
         */
        @Child(name = "display", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="User display for the concept", formalDefinition="The recommended display for this item in the expansion." )
        protected StringType display;

        /**
         * Other codes and entries contained under this entry in the heirarchy.
         */
        @Child(name = "contains", type = {ValueSetExpansionContainsComponent.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Codes contained under this entry", formalDefinition="Other codes and entries contained under this entry in the heirarchy." )
        protected List<ValueSetExpansionContainsComponent> contains;

        private static final long serialVersionUID = -2038349483L;

    /*
     * Constructor
     */
      public ValueSetExpansionContainsComponent() {
        super();
      }

        /**
         * @return {@link #system} (An absolute URI which is the code system in which the code for this item in the expansion is defined.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public UriType getSystemElement() { 
          if (this.system == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionContainsComponent.system");
            else if (Configuration.doAutoCreate())
              this.system = new UriType(); // bb
          return this.system;
        }

        public boolean hasSystemElement() { 
          return this.system != null && !this.system.isEmpty();
        }

        public boolean hasSystem() { 
          return this.system != null && !this.system.isEmpty();
        }

        /**
         * @param value {@link #system} (An absolute URI which is the code system in which the code for this item in the expansion is defined.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public ValueSetExpansionContainsComponent setSystemElement(UriType value) { 
          this.system = value;
          return this;
        }

        /**
         * @return An absolute URI which is the code system in which the code for this item in the expansion is defined.
         */
        public String getSystem() { 
          return this.system == null ? null : this.system.getValue();
        }

        /**
         * @param value An absolute URI which is the code system in which the code for this item in the expansion is defined.
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
              this.abstract_ = new BooleanType(); // bb
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
          return this.abstract_ == null || this.abstract_.isEmpty() ? false : this.abstract_.getValue();
        }

        /**
         * @param value If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value.
         */
        public ValueSetExpansionContainsComponent setAbstract(boolean value) { 
            if (this.abstract_ == null)
              this.abstract_ = new BooleanType();
            this.abstract_.setValue(value);
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
         * @return {@link #code} (The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionContainsComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ValueSetExpansionContainsComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set.
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
         * @return {@link #display} (The recommended display for this item in the expansion.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public StringType getDisplayElement() { 
          if (this.display == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValueSetExpansionContainsComponent.display");
            else if (Configuration.doAutoCreate())
              this.display = new StringType(); // bb
          return this.display;
        }

        public boolean hasDisplayElement() { 
          return this.display != null && !this.display.isEmpty();
        }

        public boolean hasDisplay() { 
          return this.display != null && !this.display.isEmpty();
        }

        /**
         * @param value {@link #display} (The recommended display for this item in the expansion.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public ValueSetExpansionContainsComponent setDisplayElement(StringType value) { 
          this.display = value;
          return this;
        }

        /**
         * @return The recommended display for this item in the expansion.
         */
        public String getDisplay() { 
          return this.display == null ? null : this.display.getValue();
        }

        /**
         * @param value The recommended display for this item in the expansion.
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
         * @return {@link #contains} (Other codes and entries contained under this entry in the heirarchy.)
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
         * @return {@link #contains} (Other codes and entries contained under this entry in the heirarchy.)
         */
    // syntactic sugar
        public ValueSetExpansionContainsComponent addContains() { //3
          ValueSetExpansionContainsComponent t = new ValueSetExpansionContainsComponent();
          if (this.contains == null)
            this.contains = new ArrayList<ValueSetExpansionContainsComponent>();
          this.contains.add(t);
          return t;
        }

    // syntactic sugar
        public ValueSetExpansionContainsComponent addContains(ValueSetExpansionContainsComponent t) { //3
          if (t == null)
            return this;
          if (this.contains == null)
            this.contains = new ArrayList<ValueSetExpansionContainsComponent>();
          this.contains.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("system", "uri", "An absolute URI which is the code system in which the code for this item in the expansion is defined.", 0, java.lang.Integer.MAX_VALUE, system));
          childrenList.add(new Property("abstract", "boolean", "If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value.", 0, java.lang.Integer.MAX_VALUE, abstract_));
          childrenList.add(new Property("version", "string", "The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("code", "code", "The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("display", "string", "The recommended display for this item in the expansion.", 0, java.lang.Integer.MAX_VALUE, display));
          childrenList.add(new Property("contains", "@ValueSet.expansion.contains", "Other codes and entries contained under this entry in the heirarchy.", 0, java.lang.Integer.MAX_VALUE, contains));
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ValueSetExpansionContainsComponent))
          return false;
        ValueSetExpansionContainsComponent o = (ValueSetExpansionContainsComponent) other;
        return compareDeep(system, o.system, true) && compareDeep(abstract_, o.abstract_, true) && compareDeep(version, o.version, true)
           && compareDeep(code, o.code, true) && compareDeep(display, o.display, true) && compareDeep(contains, o.contains, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ValueSetExpansionContainsComponent))
          return false;
        ValueSetExpansionContainsComponent o = (ValueSetExpansionContainsComponent) other;
        return compareValues(system, o.system, true) && compareValues(abstract_, o.abstract_, true) && compareValues(version, o.version, true)
           && compareValues(code, o.code, true) && compareValues(display, o.display, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (system == null || system.isEmpty()) && (abstract_ == null || abstract_.isEmpty())
           && (version == null || version.isEmpty()) && (code == null || code.isEmpty()) && (display == null || display.isEmpty())
           && (contains == null || contains.isEmpty());
      }

  }

    /**
     * An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Globally unique logical id for  value set", formalDefinition="An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published." )
    protected UriType url;

    /**
     * Formal identifier that is used to identify this value set when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the value set (v2 / CDA)", formalDefinition="Formal identifier that is used to identify this value set when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected Identifier identifier;

    /**
     * Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical id for this version of the value set", formalDefinition="Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp." )
    protected StringType version;

    /**
     * A free text natural language name describing the value set.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name for this value set", formalDefinition="A free text natural language name describing the value set." )
    protected StringType name;

    /**
     * The status of the value set.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the value set." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the value set.
     */
    @Child(name = "publisher", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the value set." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<ValueSetContactComponent> contact;

    /**
     * The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition').
     */
    @Child(name = "date", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date for given status", formalDefinition="The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition')." )
    protected DateTimeType date;

    /**
     * If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date.
     */
    @Child(name = "lockedDate", type = {DateType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Fixed date for all referenced code systems and value sets", formalDefinition="If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date." )
    protected DateType lockedDate;

    /**
     * A free text natural language description of the use of the value set - reason for definition, "the semantic space" to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set.
     */
    @Child(name = "description", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human language description of the value set", formalDefinition="A free text natural language description of the use of the value set - reason for definition, \"the semantic space\" to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set." )
    protected StringType description;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Content intends to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions." )
    protected List<CodeableConcept> useContext;

    /**
     * If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change.
     */
    @Child(name = "immutable", type = {BooleanType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Indicates whether or not any change to the content logical definition may occur", formalDefinition="If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change." )
    protected BooleanType immutable;

    /**
     * Explains why this value set is needed and why it's been constrained as it has.
     */
    @Child(name = "requirements", type = {StringType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why is this needed?", formalDefinition="Explains why this value set is needed and why it's been constrained as it has." )
    protected StringType requirements;

    /**
     * A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set.
     */
    @Child(name = "copyright", type = {StringType.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or Publishing restrictions", formalDefinition="A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set." )
    protected StringType copyright;

    /**
     * Whether this is intended to be used with an extensible binding or not.
     */
    @Child(name = "extensible", type = {BooleanType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether this is intended to be used with an extensible binding", formalDefinition="Whether this is intended to be used with an extensible binding or not." )
    protected BooleanType extensible;

    /**
     * A definition of an code system, inlined into the value set (as a packaging convenience). Note that the inline code system may be used from other value sets by referring to it's (codeSystem.system) directly.
     */
    @Child(name = "codeSystem", type = {}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="An inline code system - part of this value set", formalDefinition="A definition of an code system, inlined into the value set (as a packaging convenience). Note that the inline code system may be used from other value sets by referring to it's (codeSystem.system) directly." )
    protected ValueSetCodeSystemComponent codeSystem;

    /**
     * A set of criteria that provide the content logical definition of the value set by including or excluding codes from outside this value set.
     */
    @Child(name = "compose", type = {}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When value set includes codes from elsewhere", formalDefinition="A set of criteria that provide the content logical definition of the value set by including or excluding codes from outside this value set." )
    protected ValueSetComposeComponent compose;

    /**
     * A value set can also be "expanded", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed.
     */
    @Child(name = "expansion", type = {}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Used when the value set is \"expanded\"", formalDefinition="A value set can also be \"expanded\", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed." )
    protected ValueSetExpansionComponent expansion;

    private static final long serialVersionUID = -467533312L;

  /*
   * Constructor
   */
    public ValueSet() {
      super();
    }

  /*
   * Constructor
   */
    public ValueSet(Enumeration<ConformanceResourceStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.url");
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
     * @param value {@link #url} (An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ValueSet setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published.
     */
    public ValueSet setUrl(String value) { 
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
     * @return {@link #identifier} (Formal identifier that is used to identify this value set when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Formal identifier that is used to identify this value set when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public ValueSet setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #version} (Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.version");
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
     * @param value {@link #version} (Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ValueSet setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
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
     * @return {@link #status} (The status of the value set.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory()); // bb
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
    public ValueSet setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the value set.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the value set.
     */
    public ValueSet setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
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
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public ValueSet setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
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
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    public List<ValueSetContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ValueSetContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ValueSetContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public ValueSetContactComponent addContact() { //3
      ValueSetContactComponent t = new ValueSetContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<ValueSetContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public ValueSet addContact(ValueSetContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ValueSetContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition').). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.date");
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
     * @param value {@link #date} (The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition').). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ValueSet setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition').
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition').
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
     * @return {@link #lockedDate} (If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date.). This is the underlying object with id, value and extensions. The accessor "getLockedDate" gives direct access to the value
     */
    public DateType getLockedDateElement() { 
      if (this.lockedDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.lockedDate");
        else if (Configuration.doAutoCreate())
          this.lockedDate = new DateType(); // bb
      return this.lockedDate;
    }

    public boolean hasLockedDateElement() { 
      return this.lockedDate != null && !this.lockedDate.isEmpty();
    }

    public boolean hasLockedDate() { 
      return this.lockedDate != null && !this.lockedDate.isEmpty();
    }

    /**
     * @param value {@link #lockedDate} (If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date.). This is the underlying object with id, value and extensions. The accessor "getLockedDate" gives direct access to the value
     */
    public ValueSet setLockedDateElement(DateType value) { 
      this.lockedDate = value;
      return this;
    }

    /**
     * @return If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date.
     */
    public Date getLockedDate() { 
      return this.lockedDate == null ? null : this.lockedDate.getValue();
    }

    /**
     * @param value If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date.
     */
    public ValueSet setLockedDate(Date value) { 
      if (value == null)
        this.lockedDate = null;
      else {
        if (this.lockedDate == null)
          this.lockedDate = new DateType();
        this.lockedDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (A free text natural language description of the use of the value set - reason for definition, "the semantic space" to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.description");
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
     * @param value {@link #description} (A free text natural language description of the use of the value set - reason for definition, "the semantic space" to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ValueSet setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the use of the value set - reason for definition, "the semantic space" to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the use of the value set - reason for definition, "the semantic space" to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set.
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions.)
     */
    public List<CodeableConcept> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      return this.useContext;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (CodeableConcept item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions.)
     */
    // syntactic sugar
    public CodeableConcept addUseContext() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return t;
    }

    // syntactic sugar
    public ValueSet addUseContext(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
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
          this.immutable = new BooleanType(); // bb
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
      return this.immutable == null || this.immutable.isEmpty() ? false : this.immutable.getValue();
    }

    /**
     * @param value If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change.
     */
    public ValueSet setImmutable(boolean value) { 
        if (this.immutable == null)
          this.immutable = new BooleanType();
        this.immutable.setValue(value);
      return this;
    }

    /**
     * @return {@link #requirements} (Explains why this value set is needed and why it's been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.requirements");
        else if (Configuration.doAutoCreate())
          this.requirements = new StringType(); // bb
      return this.requirements;
    }

    public boolean hasRequirementsElement() { 
      return this.requirements != null && !this.requirements.isEmpty();
    }

    public boolean hasRequirements() { 
      return this.requirements != null && !this.requirements.isEmpty();
    }

    /**
     * @param value {@link #requirements} (Explains why this value set is needed and why it's been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public ValueSet setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return Explains why this value set is needed and why it's been constrained as it has.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value Explains why this value set is needed and why it's been constrained as it has.
     */
    public ValueSet setRequirements(String value) { 
      if (Utilities.noString(value))
        this.requirements = null;
      else {
        if (this.requirements == null)
          this.requirements = new StringType();
        this.requirements.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new StringType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ValueSet setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set.
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
     * @return {@link #extensible} (Whether this is intended to be used with an extensible binding or not.). This is the underlying object with id, value and extensions. The accessor "getExtensible" gives direct access to the value
     */
    public BooleanType getExtensibleElement() { 
      if (this.extensible == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.extensible");
        else if (Configuration.doAutoCreate())
          this.extensible = new BooleanType(); // bb
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
      return this.extensible == null || this.extensible.isEmpty() ? false : this.extensible.getValue();
    }

    /**
     * @param value Whether this is intended to be used with an extensible binding or not.
     */
    public ValueSet setExtensible(boolean value) { 
        if (this.extensible == null)
          this.extensible = new BooleanType();
        this.extensible.setValue(value);
      return this;
    }

    /**
     * @return {@link #codeSystem} (A definition of an code system, inlined into the value set (as a packaging convenience). Note that the inline code system may be used from other value sets by referring to it's (codeSystem.system) directly.)
     */
    public ValueSetCodeSystemComponent getCodeSystem() { 
      if (this.codeSystem == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.codeSystem");
        else if (Configuration.doAutoCreate())
          this.codeSystem = new ValueSetCodeSystemComponent(); // cc
      return this.codeSystem;
    }

    public boolean hasCodeSystem() { 
      return this.codeSystem != null && !this.codeSystem.isEmpty();
    }

    /**
     * @param value {@link #codeSystem} (A definition of an code system, inlined into the value set (as a packaging convenience). Note that the inline code system may be used from other value sets by referring to it's (codeSystem.system) directly.)
     */
    public ValueSet setCodeSystem(ValueSetCodeSystemComponent value) { 
      this.codeSystem = value;
      return this;
    }

    /**
     * @return {@link #compose} (A set of criteria that provide the content logical definition of the value set by including or excluding codes from outside this value set.)
     */
    public ValueSetComposeComponent getCompose() { 
      if (this.compose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ValueSet.compose");
        else if (Configuration.doAutoCreate())
          this.compose = new ValueSetComposeComponent(); // cc
      return this.compose;
    }

    public boolean hasCompose() { 
      return this.compose != null && !this.compose.isEmpty();
    }

    /**
     * @param value {@link #compose} (A set of criteria that provide the content logical definition of the value set by including or excluding codes from outside this value set.)
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
          this.expansion = new ValueSetExpansionComponent(); // cc
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
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "Formal identifier that is used to identify this value set when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name describing the value set.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of the value set.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the value set.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition').", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("lockedDate", "date", "If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date.", 0, java.lang.Integer.MAX_VALUE, lockedDate));
        childrenList.add(new Property("description", "string", "A free text natural language description of the use of the value set - reason for definition, \"the semantic space\" to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("immutable", "boolean", "If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change.", 0, java.lang.Integer.MAX_VALUE, immutable));
        childrenList.add(new Property("requirements", "string", "Explains why this value set is needed and why it's been constrained as it has.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("extensible", "boolean", "Whether this is intended to be used with an extensible binding or not.", 0, java.lang.Integer.MAX_VALUE, extensible));
        childrenList.add(new Property("codeSystem", "", "A definition of an code system, inlined into the value set (as a packaging convenience). Note that the inline code system may be used from other value sets by referring to it's (codeSystem.system) directly.", 0, java.lang.Integer.MAX_VALUE, codeSystem));
        childrenList.add(new Property("compose", "", "A set of criteria that provide the content logical definition of the value set by including or excluding codes from outside this value set.", 0, java.lang.Integer.MAX_VALUE, compose));
        childrenList.add(new Property("expansion", "", "A value set can also be \"expanded\", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed.", 0, java.lang.Integer.MAX_VALUE, expansion));
      }

      public ValueSet copy() {
        ValueSet dst = new ValueSet();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ValueSetContactComponent>();
          for (ValueSetContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.lockedDate = lockedDate == null ? null : lockedDate.copy();
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : useContext)
            dst.useContext.add(i.copy());
        };
        dst.immutable = immutable == null ? null : immutable.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.extensible = extensible == null ? null : extensible.copy();
        dst.codeSystem = codeSystem == null ? null : codeSystem.copy();
        dst.compose = compose == null ? null : compose.copy();
        dst.expansion = expansion == null ? null : expansion.copy();
        return dst;
      }

      protected ValueSet typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ValueSet))
          return false;
        ValueSet o = (ValueSet) other;
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(name, o.name, true) && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true)
           && compareDeep(lockedDate, o.lockedDate, true) && compareDeep(description, o.description, true)
           && compareDeep(useContext, o.useContext, true) && compareDeep(immutable, o.immutable, true) && compareDeep(requirements, o.requirements, true)
           && compareDeep(copyright, o.copyright, true) && compareDeep(extensible, o.extensible, true) && compareDeep(codeSystem, o.codeSystem, true)
           && compareDeep(compose, o.compose, true) && compareDeep(expansion, o.expansion, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ValueSet))
          return false;
        ValueSet o = (ValueSet) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(lockedDate, o.lockedDate, true) && compareValues(description, o.description, true)
           && compareValues(immutable, o.immutable, true) && compareValues(requirements, o.requirements, true)
           && compareValues(copyright, o.copyright, true) && compareValues(extensible, o.extensible, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (name == null || name.isEmpty()) && (status == null || status.isEmpty())
           && (experimental == null || experimental.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty()) && (lockedDate == null || lockedDate.isEmpty())
           && (description == null || description.isEmpty()) && (useContext == null || useContext.isEmpty())
           && (immutable == null || immutable.isEmpty()) && (requirements == null || requirements.isEmpty())
           && (copyright == null || copyright.isEmpty()) && (extensible == null || extensible.isEmpty())
           && (codeSystem == null || codeSystem.isEmpty()) && (compose == null || compose.isEmpty())
           && (expansion == null || expansion.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ValueSet;
   }

  @SearchParamDefinition(name="date", path="ValueSet.date", description="The value set publication date", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="ValueSet.identifier", description="The identifier for the value set", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="code", path="ValueSet.codeSystem.concept.code", description="A code defined in the value set", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="description", path="ValueSet.description", description="Text search in the description of the value set", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="version", path="ValueSet.version", description="The version identifier of the value set", type="token" )
  public static final String SP_VERSION = "version";
  @SearchParamDefinition(name="url", path="ValueSet.url", description="The logical url for the value set", type="uri" )
  public static final String SP_URL = "url";
  @SearchParamDefinition(name="expansion", path="ValueSet.expansion.identifier", description="Uniquely identifies this expansion", type="uri" )
  public static final String SP_EXPANSION = "expansion";
  @SearchParamDefinition(name="reference", path="ValueSet.compose.include.system", description="A code system included or excluded in the value set or an imported value set", type="uri" )
  public static final String SP_REFERENCE = "reference";
  @SearchParamDefinition(name="system", path="ValueSet.codeSystem.system", description="The system for any codes defined by this value set", type="uri" )
  public static final String SP_SYSTEM = "system";
  @SearchParamDefinition(name="name", path="ValueSet.name", description="The name of the value set", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="context", path="ValueSet.useContext", description="A use context assigned to the value set", type="token" )
  public static final String SP_CONTEXT = "context";
  @SearchParamDefinition(name="publisher", path="ValueSet.publisher", description="Name of the publisher of the value set", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="status", path="ValueSet.status", description="The status of the value set", type="token" )
  public static final String SP_STATUS = "status";

}

