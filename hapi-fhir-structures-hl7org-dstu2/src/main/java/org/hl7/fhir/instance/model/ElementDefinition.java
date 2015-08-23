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
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * Captures constraints on each element within the resource, profile, or extension.
 */
@DatatypeDef(name="ElementDefinition")
public class ElementDefinition extends Type implements ICompositeType {

    public enum PropertyRepresentation {
        /**
         * In XML, this property is represented as an attribute not an element
         */
        XMLATTR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PropertyRepresentation fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("xmlAttr".equals(codeString))
          return XMLATTR;
        throw new Exception("Unknown PropertyRepresentation code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case XMLATTR: return "xmlAttr";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case XMLATTR: return "http://hl7.org/fhir/property-representation";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case XMLATTR: return "In XML, this property is represented as an attribute not an element";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case XMLATTR: return "XML Attirbute";
            default: return "?";
          }
        }
    }

  public static class PropertyRepresentationEnumFactory implements EnumFactory<PropertyRepresentation> {
    public PropertyRepresentation fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("xmlAttr".equals(codeString))
          return PropertyRepresentation.XMLATTR;
        throw new IllegalArgumentException("Unknown PropertyRepresentation code '"+codeString+"'");
        }
    public String toCode(PropertyRepresentation code) {
      if (code == PropertyRepresentation.XMLATTR)
        return "xmlAttr";
      return "?";
      }
    }

    public enum SlicingRules {
        /**
         * No additional content is allowed other than that described by the slices in this profile
         */
        CLOSED, 
        /**
         * Additional content is allowed anywhere in the list
         */
        OPEN, 
        /**
         * Additional content is allowed, but only at the end of the list. Note that using this requires that the slices be ordered, which makes it hard to share uses. This should only be done where absolutely required
         */
        OPENATEND, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SlicingRules fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("closed".equals(codeString))
          return CLOSED;
        if ("open".equals(codeString))
          return OPEN;
        if ("openAtEnd".equals(codeString))
          return OPENATEND;
        throw new Exception("Unknown SlicingRules code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CLOSED: return "closed";
            case OPEN: return "open";
            case OPENATEND: return "openAtEnd";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CLOSED: return "http://hl7.org/fhir/resource-slicing-rules";
            case OPEN: return "http://hl7.org/fhir/resource-slicing-rules";
            case OPENATEND: return "http://hl7.org/fhir/resource-slicing-rules";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CLOSED: return "No additional content is allowed other than that described by the slices in this profile";
            case OPEN: return "Additional content is allowed anywhere in the list";
            case OPENATEND: return "Additional content is allowed, but only at the end of the list. Note that using this requires that the slices be ordered, which makes it hard to share uses. This should only be done where absolutely required";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CLOSED: return "Closed";
            case OPEN: return "Open";
            case OPENATEND: return "Open at End";
            default: return "?";
          }
        }
    }

  public static class SlicingRulesEnumFactory implements EnumFactory<SlicingRules> {
    public SlicingRules fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("closed".equals(codeString))
          return SlicingRules.CLOSED;
        if ("open".equals(codeString))
          return SlicingRules.OPEN;
        if ("openAtEnd".equals(codeString))
          return SlicingRules.OPENATEND;
        throw new IllegalArgumentException("Unknown SlicingRules code '"+codeString+"'");
        }
    public String toCode(SlicingRules code) {
      if (code == SlicingRules.CLOSED)
        return "closed";
      if (code == SlicingRules.OPEN)
        return "open";
      if (code == SlicingRules.OPENATEND)
        return "openAtEnd";
      return "?";
      }
    }

    public enum AggregationMode {
        /**
         * The reference is a local reference to a contained resource
         */
        CONTAINED, 
        /**
         * The reference to a resource that has to be resolved externally to the resource that includes the reference
         */
        REFERENCED, 
        /**
         * The resource the reference points to will be found in the same bundle as the resource that includes the reference
         */
        BUNDLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AggregationMode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("contained".equals(codeString))
          return CONTAINED;
        if ("referenced".equals(codeString))
          return REFERENCED;
        if ("bundled".equals(codeString))
          return BUNDLED;
        throw new Exception("Unknown AggregationMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONTAINED: return "contained";
            case REFERENCED: return "referenced";
            case BUNDLED: return "bundled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CONTAINED: return "http://hl7.org/fhir/resource-aggregation-mode";
            case REFERENCED: return "http://hl7.org/fhir/resource-aggregation-mode";
            case BUNDLED: return "http://hl7.org/fhir/resource-aggregation-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CONTAINED: return "The reference is a local reference to a contained resource";
            case REFERENCED: return "The reference to a resource that has to be resolved externally to the resource that includes the reference";
            case BUNDLED: return "The resource the reference points to will be found in the same bundle as the resource that includes the reference";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONTAINED: return "Contained";
            case REFERENCED: return "Referenced";
            case BUNDLED: return "Bundled";
            default: return "?";
          }
        }
    }

  public static class AggregationModeEnumFactory implements EnumFactory<AggregationMode> {
    public AggregationMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("contained".equals(codeString))
          return AggregationMode.CONTAINED;
        if ("referenced".equals(codeString))
          return AggregationMode.REFERENCED;
        if ("bundled".equals(codeString))
          return AggregationMode.BUNDLED;
        throw new IllegalArgumentException("Unknown AggregationMode code '"+codeString+"'");
        }
    public String toCode(AggregationMode code) {
      if (code == AggregationMode.CONTAINED)
        return "contained";
      if (code == AggregationMode.REFERENCED)
        return "referenced";
      if (code == AggregationMode.BUNDLED)
        return "bundled";
      return "?";
      }
    }

    public enum ConstraintSeverity {
        /**
         * If the constraint is violated, the resource is not conformant
         */
        ERROR, 
        /**
         * If the constraint is violated, the resource is conformant, but it is not necessarily following best practice.
         */
        WARNING, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConstraintSeverity fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("error".equals(codeString))
          return ERROR;
        if ("warning".equals(codeString))
          return WARNING;
        throw new Exception("Unknown ConstraintSeverity code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ERROR: return "error";
            case WARNING: return "warning";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ERROR: return "http://hl7.org/fhir/constraint-severity";
            case WARNING: return "http://hl7.org/fhir/constraint-severity";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ERROR: return "If the constraint is violated, the resource is not conformant";
            case WARNING: return "If the constraint is violated, the resource is conformant, but it is not necessarily following best practice.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ERROR: return "Error";
            case WARNING: return "Warning";
            default: return "?";
          }
        }
    }

  public static class ConstraintSeverityEnumFactory implements EnumFactory<ConstraintSeverity> {
    public ConstraintSeverity fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("error".equals(codeString))
          return ConstraintSeverity.ERROR;
        if ("warning".equals(codeString))
          return ConstraintSeverity.WARNING;
        throw new IllegalArgumentException("Unknown ConstraintSeverity code '"+codeString+"'");
        }
    public String toCode(ConstraintSeverity code) {
      if (code == ConstraintSeverity.ERROR)
        return "error";
      if (code == ConstraintSeverity.WARNING)
        return "warning";
      return "?";
      }
    }

    @Block()
    public static class ElementDefinitionSlicingComponent extends Element implements IBaseDatatypeElement {
        /**
         * Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
         */
        @Child(name = "discriminator", type = {StringType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Element values that used to distinguish the slices", formalDefinition="Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices." )
        protected List<StringType> discriminator;

        /**
         * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Text description of how slicing works (or not)", formalDefinition="A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated." )
        protected StringType description;

        /**
         * If the matching elements have to occur in the same order as defined in the profile.
         */
        @Child(name = "ordered", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="If elements must be in same order as slices", formalDefinition="If the matching elements have to occur in the same order as defined in the profile." )
        protected BooleanType ordered;

        /**
         * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.
         */
        @Child(name = "rules", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="closed | open | openAtEnd", formalDefinition="Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end." )
        protected Enumeration<SlicingRules> rules;

        private static final long serialVersionUID = 233544215L;

    /*
     * Constructor
     */
      public ElementDefinitionSlicingComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ElementDefinitionSlicingComponent(Enumeration<SlicingRules> rules) {
        super();
        this.rules = rules;
      }

        /**
         * @return {@link #discriminator} (Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices.)
         */
        public List<StringType> getDiscriminator() { 
          if (this.discriminator == null)
            this.discriminator = new ArrayList<StringType>();
          return this.discriminator;
        }

        public boolean hasDiscriminator() { 
          if (this.discriminator == null)
            return false;
          for (StringType item : this.discriminator)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #discriminator} (Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices.)
         */
    // syntactic sugar
        public StringType addDiscriminatorElement() {//2 
          StringType t = new StringType();
          if (this.discriminator == null)
            this.discriminator = new ArrayList<StringType>();
          this.discriminator.add(t);
          return t;
        }

        /**
         * @param value {@link #discriminator} (Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices.)
         */
        public ElementDefinitionSlicingComponent addDiscriminator(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.discriminator == null)
            this.discriminator = new ArrayList<StringType>();
          this.discriminator.add(t);
          return this;
        }

        /**
         * @param value {@link #discriminator} (Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices.)
         */
        public boolean hasDiscriminator(String value) { 
          if (this.discriminator == null)
            return false;
          for (StringType v : this.discriminator)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #description} (A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionSlicingComponent.description");
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
         * @param value {@link #description} (A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ElementDefinitionSlicingComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.
         */
        public ElementDefinitionSlicingComponent setDescription(String value) { 
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
         * @return {@link #ordered} (If the matching elements have to occur in the same order as defined in the profile.). This is the underlying object with id, value and extensions. The accessor "getOrdered" gives direct access to the value
         */
        public BooleanType getOrderedElement() { 
          if (this.ordered == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionSlicingComponent.ordered");
            else if (Configuration.doAutoCreate())
              this.ordered = new BooleanType(); // bb
          return this.ordered;
        }

        public boolean hasOrderedElement() { 
          return this.ordered != null && !this.ordered.isEmpty();
        }

        public boolean hasOrdered() { 
          return this.ordered != null && !this.ordered.isEmpty();
        }

        /**
         * @param value {@link #ordered} (If the matching elements have to occur in the same order as defined in the profile.). This is the underlying object with id, value and extensions. The accessor "getOrdered" gives direct access to the value
         */
        public ElementDefinitionSlicingComponent setOrderedElement(BooleanType value) { 
          this.ordered = value;
          return this;
        }

        /**
         * @return If the matching elements have to occur in the same order as defined in the profile.
         */
        public boolean getOrdered() { 
          return this.ordered == null || this.ordered.isEmpty() ? false : this.ordered.getValue();
        }

        /**
         * @param value If the matching elements have to occur in the same order as defined in the profile.
         */
        public ElementDefinitionSlicingComponent setOrdered(boolean value) { 
            if (this.ordered == null)
              this.ordered = new BooleanType();
            this.ordered.setValue(value);
          return this;
        }

        /**
         * @return {@link #rules} (Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.). This is the underlying object with id, value and extensions. The accessor "getRules" gives direct access to the value
         */
        public Enumeration<SlicingRules> getRulesElement() { 
          if (this.rules == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionSlicingComponent.rules");
            else if (Configuration.doAutoCreate())
              this.rules = new Enumeration<SlicingRules>(new SlicingRulesEnumFactory()); // bb
          return this.rules;
        }

        public boolean hasRulesElement() { 
          return this.rules != null && !this.rules.isEmpty();
        }

        public boolean hasRules() { 
          return this.rules != null && !this.rules.isEmpty();
        }

        /**
         * @param value {@link #rules} (Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.). This is the underlying object with id, value and extensions. The accessor "getRules" gives direct access to the value
         */
        public ElementDefinitionSlicingComponent setRulesElement(Enumeration<SlicingRules> value) { 
          this.rules = value;
          return this;
        }

        /**
         * @return Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.
         */
        public SlicingRules getRules() { 
          return this.rules == null ? null : this.rules.getValue();
        }

        /**
         * @param value Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.
         */
        public ElementDefinitionSlicingComponent setRules(SlicingRules value) { 
            if (this.rules == null)
              this.rules = new Enumeration<SlicingRules>(new SlicingRulesEnumFactory());
            this.rules.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("discriminator", "string", "Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices.", 0, java.lang.Integer.MAX_VALUE, discriminator));
          childrenList.add(new Property("description", "string", "A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("ordered", "boolean", "If the matching elements have to occur in the same order as defined in the profile.", 0, java.lang.Integer.MAX_VALUE, ordered));
          childrenList.add(new Property("rules", "code", "Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.", 0, java.lang.Integer.MAX_VALUE, rules));
        }

      public ElementDefinitionSlicingComponent copy() {
        ElementDefinitionSlicingComponent dst = new ElementDefinitionSlicingComponent();
        copyValues(dst);
        if (discriminator != null) {
          dst.discriminator = new ArrayList<StringType>();
          for (StringType i : discriminator)
            dst.discriminator.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        dst.ordered = ordered == null ? null : ordered.copy();
        dst.rules = rules == null ? null : rules.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionSlicingComponent))
          return false;
        ElementDefinitionSlicingComponent o = (ElementDefinitionSlicingComponent) other;
        return compareDeep(discriminator, o.discriminator, true) && compareDeep(description, o.description, true)
           && compareDeep(ordered, o.ordered, true) && compareDeep(rules, o.rules, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionSlicingComponent))
          return false;
        ElementDefinitionSlicingComponent o = (ElementDefinitionSlicingComponent) other;
        return compareValues(discriminator, o.discriminator, true) && compareValues(description, o.description, true)
           && compareValues(ordered, o.ordered, true) && compareValues(rules, o.rules, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (discriminator == null || discriminator.isEmpty()) && (description == null || description.isEmpty())
           && (ordered == null || ordered.isEmpty()) && (rules == null || rules.isEmpty());
      }

  }

    @Block()
    public static class ElementDefinitionBaseComponent extends Element implements IBaseDatatypeElement {
        /**
         * The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Path that identifies the base element", formalDefinition="The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base." )
        protected StringType path;

        /**
         * Minimum cardinality of the base element identified by the path.
         */
        @Child(name = "min", type = {IntegerType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Min cardinality of the base element", formalDefinition="Minimum cardinality of the base element identified by the path." )
        protected IntegerType min;

        /**
         * Maximum cardinality of the base element identified by the path.
         */
        @Child(name = "max", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Max cardinality of the base element", formalDefinition="Maximum cardinality of the base element identified by the path." )
        protected StringType max;

        private static final long serialVersionUID = 232204455L;

    /*
     * Constructor
     */
      public ElementDefinitionBaseComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ElementDefinitionBaseComponent(StringType path, IntegerType min, StringType max) {
        super();
        this.path = path;
        this.min = min;
        this.max = max;
      }

        /**
         * @return {@link #path} (The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBaseComponent.path");
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
         * @param value {@link #path} (The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public ElementDefinitionBaseComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.
         */
        public ElementDefinitionBaseComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        /**
         * @return {@link #min} (Minimum cardinality of the base element identified by the path.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public IntegerType getMinElement() { 
          if (this.min == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBaseComponent.min");
            else if (Configuration.doAutoCreate())
              this.min = new IntegerType(); // bb
          return this.min;
        }

        public boolean hasMinElement() { 
          return this.min != null && !this.min.isEmpty();
        }

        public boolean hasMin() { 
          return this.min != null && !this.min.isEmpty();
        }

        /**
         * @param value {@link #min} (Minimum cardinality of the base element identified by the path.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public ElementDefinitionBaseComponent setMinElement(IntegerType value) { 
          this.min = value;
          return this;
        }

        /**
         * @return Minimum cardinality of the base element identified by the path.
         */
        public int getMin() { 
          return this.min == null || this.min.isEmpty() ? 0 : this.min.getValue();
        }

        /**
         * @param value Minimum cardinality of the base element identified by the path.
         */
        public ElementDefinitionBaseComponent setMin(int value) { 
            if (this.min == null)
              this.min = new IntegerType();
            this.min.setValue(value);
          return this;
        }

        /**
         * @return {@link #max} (Maximum cardinality of the base element identified by the path.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public StringType getMaxElement() { 
          if (this.max == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBaseComponent.max");
            else if (Configuration.doAutoCreate())
              this.max = new StringType(); // bb
          return this.max;
        }

        public boolean hasMaxElement() { 
          return this.max != null && !this.max.isEmpty();
        }

        public boolean hasMax() { 
          return this.max != null && !this.max.isEmpty();
        }

        /**
         * @param value {@link #max} (Maximum cardinality of the base element identified by the path.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public ElementDefinitionBaseComponent setMaxElement(StringType value) { 
          this.max = value;
          return this;
        }

        /**
         * @return Maximum cardinality of the base element identified by the path.
         */
        public String getMax() { 
          return this.max == null ? null : this.max.getValue();
        }

        /**
         * @param value Maximum cardinality of the base element identified by the path.
         */
        public ElementDefinitionBaseComponent setMax(String value) { 
            if (this.max == null)
              this.max = new StringType();
            this.max.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("path", "string", "The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("min", "integer", "Minimum cardinality of the base element identified by the path.", 0, java.lang.Integer.MAX_VALUE, min));
          childrenList.add(new Property("max", "string", "Maximum cardinality of the base element identified by the path.", 0, java.lang.Integer.MAX_VALUE, max));
        }

      public ElementDefinitionBaseComponent copy() {
        ElementDefinitionBaseComponent dst = new ElementDefinitionBaseComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionBaseComponent))
          return false;
        ElementDefinitionBaseComponent o = (ElementDefinitionBaseComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(min, o.min, true) && compareDeep(max, o.max, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionBaseComponent))
          return false;
        ElementDefinitionBaseComponent o = (ElementDefinitionBaseComponent) other;
        return compareValues(path, o.path, true) && compareValues(min, o.min, true) && compareValues(max, o.max, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (path == null || path.isEmpty()) && (min == null || min.isEmpty())
           && (max == null || max.isEmpty());
      }

  }

    @Block()
    public static class TypeRefComponent extends Element implements IBaseDatatypeElement {
        /**
         * Name of Data type or Resource that is a(or the) type used for this element.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of Data type or Resource", formalDefinition="Name of Data type or Resource that is a(or the) type used for this element." )
        protected CodeType code;

        /**
         * Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.
         */
        @Child(name = "profile", type = {UriType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Profile (StructureDefinition) to apply (or IG)", formalDefinition="Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide." )
        protected List<UriType> profile;

        /**
         * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.
         */
        @Child(name = "aggregation", type = {CodeType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="contained | referenced | bundled - how aggregated", formalDefinition="If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle." )
        protected List<Enumeration<AggregationMode>> aggregation;

        private static final long serialVersionUID = -988693373L;

    /*
     * Constructor
     */
      public TypeRefComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TypeRefComponent(CodeType code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Name of Data type or Resource that is a(or the) type used for this element.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TypeRefComponent.code");
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
         * @param value {@link #code} (Name of Data type or Resource that is a(or the) type used for this element.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public TypeRefComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Name of Data type or Resource that is a(or the) type used for this element.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Name of Data type or Resource that is a(or the) type used for this element.
         */
        public TypeRefComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.)
         */
        public List<UriType> getProfile() { 
          if (this.profile == null)
            this.profile = new ArrayList<UriType>();
          return this.profile;
        }

        public boolean hasProfile() { 
          if (this.profile == null)
            return false;
          for (UriType item : this.profile)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #profile} (Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.)
         */
    // syntactic sugar
        public UriType addProfileElement() {//2 
          UriType t = new UriType();
          if (this.profile == null)
            this.profile = new ArrayList<UriType>();
          this.profile.add(t);
          return t;
        }

        /**
         * @param value {@link #profile} (Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.)
         */
        public TypeRefComponent addProfile(String value) { //1
          UriType t = new UriType();
          t.setValue(value);
          if (this.profile == null)
            this.profile = new ArrayList<UriType>();
          this.profile.add(t);
          return this;
        }

        /**
         * @param value {@link #profile} (Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.)
         */
        public boolean hasProfile(String value) { 
          if (this.profile == null)
            return false;
          for (UriType v : this.profile)
            if (v.equals(value)) // uri
              return true;
          return false;
        }

        /**
         * @return {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
        public List<Enumeration<AggregationMode>> getAggregation() { 
          if (this.aggregation == null)
            this.aggregation = new ArrayList<Enumeration<AggregationMode>>();
          return this.aggregation;
        }

        public boolean hasAggregation() { 
          if (this.aggregation == null)
            return false;
          for (Enumeration<AggregationMode> item : this.aggregation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
    // syntactic sugar
        public Enumeration<AggregationMode> addAggregationElement() {//2 
          Enumeration<AggregationMode> t = new Enumeration<AggregationMode>(new AggregationModeEnumFactory());
          if (this.aggregation == null)
            this.aggregation = new ArrayList<Enumeration<AggregationMode>>();
          this.aggregation.add(t);
          return t;
        }

        /**
         * @param value {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
        public TypeRefComponent addAggregation(AggregationMode value) { //1
          Enumeration<AggregationMode> t = new Enumeration<AggregationMode>(new AggregationModeEnumFactory());
          t.setValue(value);
          if (this.aggregation == null)
            this.aggregation = new ArrayList<Enumeration<AggregationMode>>();
          this.aggregation.add(t);
          return this;
        }

        /**
         * @param value {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
        public boolean hasAggregation(AggregationMode value) { 
          if (this.aggregation == null)
            return false;
          for (Enumeration<AggregationMode> v : this.aggregation)
            if (v.equals(value)) // code
              return true;
          return false;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "Name of Data type or Resource that is a(or the) type used for this element.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("profile", "uri", "Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("aggregation", "code", "If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.", 0, java.lang.Integer.MAX_VALUE, aggregation));
        }

      public TypeRefComponent copy() {
        TypeRefComponent dst = new TypeRefComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (profile != null) {
          dst.profile = new ArrayList<UriType>();
          for (UriType i : profile)
            dst.profile.add(i.copy());
        };
        if (aggregation != null) {
          dst.aggregation = new ArrayList<Enumeration<AggregationMode>>();
          for (Enumeration<AggregationMode> i : aggregation)
            dst.aggregation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TypeRefComponent))
          return false;
        TypeRefComponent o = (TypeRefComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(profile, o.profile, true) && compareDeep(aggregation, o.aggregation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TypeRefComponent))
          return false;
        TypeRefComponent o = (TypeRefComponent) other;
        return compareValues(code, o.code, true) && compareValues(profile, o.profile, true) && compareValues(aggregation, o.aggregation, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (profile == null || profile.isEmpty())
           && (aggregation == null || aggregation.isEmpty());
      }

  }

    @Block()
    public static class ElementDefinitionConstraintComponent extends Element implements IBaseDatatypeElement {
        /**
         * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.
         */
        @Child(name = "key", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Target of 'condition' reference above", formalDefinition="Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality." )
        protected IdType key;

        /**
         * Description of why this constraint is necessary or appropriate.
         */
        @Child(name = "requirements", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Why this constraint necessary or appropriate", formalDefinition="Description of why this constraint is necessary or appropriate." )
        protected StringType requirements;

        /**
         * Identifies the impact constraint violation has on the conformance of the instance.
         */
        @Child(name = "severity", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="error | warning", formalDefinition="Identifies the impact constraint violation has on the conformance of the instance." )
        protected Enumeration<ConstraintSeverity> severity;

        /**
         * Text that can be used to describe the constraint in messages identifying that the constraint has been violated.
         */
        @Child(name = "human", type = {StringType.class}, order=4, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human description of constraint", formalDefinition="Text that can be used to describe the constraint in messages identifying that the constraint has been violated." )
        protected StringType human;

        /**
         * An XPath expression of constraint that can be executed to see if this constraint is met.
         */
        @Child(name = "xpath", type = {StringType.class}, order=5, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="XPath expression of constraint", formalDefinition="An XPath expression of constraint that can be executed to see if this constraint is met." )
        protected StringType xpath;

        private static final long serialVersionUID = 854521265L;

    /*
     * Constructor
     */
      public ElementDefinitionConstraintComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ElementDefinitionConstraintComponent(IdType key, Enumeration<ConstraintSeverity> severity, StringType human, StringType xpath) {
        super();
        this.key = key;
        this.severity = severity;
        this.human = human;
        this.xpath = xpath;
      }

        /**
         * @return {@link #key} (Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.). This is the underlying object with id, value and extensions. The accessor "getKey" gives direct access to the value
         */
        public IdType getKeyElement() { 
          if (this.key == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.key");
            else if (Configuration.doAutoCreate())
              this.key = new IdType(); // bb
          return this.key;
        }

        public boolean hasKeyElement() { 
          return this.key != null && !this.key.isEmpty();
        }

        public boolean hasKey() { 
          return this.key != null && !this.key.isEmpty();
        }

        /**
         * @param value {@link #key} (Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.). This is the underlying object with id, value and extensions. The accessor "getKey" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setKeyElement(IdType value) { 
          this.key = value;
          return this;
        }

        /**
         * @return Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.
         */
        public String getKey() { 
          return this.key == null ? null : this.key.getValue();
        }

        /**
         * @param value Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.
         */
        public ElementDefinitionConstraintComponent setKey(String value) { 
            if (this.key == null)
              this.key = new IdType();
            this.key.setValue(value);
          return this;
        }

        /**
         * @return {@link #requirements} (Description of why this constraint is necessary or appropriate.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
         */
        public StringType getRequirementsElement() { 
          if (this.requirements == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.requirements");
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
         * @param value {@link #requirements} (Description of why this constraint is necessary or appropriate.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setRequirementsElement(StringType value) { 
          this.requirements = value;
          return this;
        }

        /**
         * @return Description of why this constraint is necessary or appropriate.
         */
        public String getRequirements() { 
          return this.requirements == null ? null : this.requirements.getValue();
        }

        /**
         * @param value Description of why this constraint is necessary or appropriate.
         */
        public ElementDefinitionConstraintComponent setRequirements(String value) { 
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
         * @return {@link #severity} (Identifies the impact constraint violation has on the conformance of the instance.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
         */
        public Enumeration<ConstraintSeverity> getSeverityElement() { 
          if (this.severity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.severity");
            else if (Configuration.doAutoCreate())
              this.severity = new Enumeration<ConstraintSeverity>(new ConstraintSeverityEnumFactory()); // bb
          return this.severity;
        }

        public boolean hasSeverityElement() { 
          return this.severity != null && !this.severity.isEmpty();
        }

        public boolean hasSeverity() { 
          return this.severity != null && !this.severity.isEmpty();
        }

        /**
         * @param value {@link #severity} (Identifies the impact constraint violation has on the conformance of the instance.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setSeverityElement(Enumeration<ConstraintSeverity> value) { 
          this.severity = value;
          return this;
        }

        /**
         * @return Identifies the impact constraint violation has on the conformance of the instance.
         */
        public ConstraintSeverity getSeverity() { 
          return this.severity == null ? null : this.severity.getValue();
        }

        /**
         * @param value Identifies the impact constraint violation has on the conformance of the instance.
         */
        public ElementDefinitionConstraintComponent setSeverity(ConstraintSeverity value) { 
            if (this.severity == null)
              this.severity = new Enumeration<ConstraintSeverity>(new ConstraintSeverityEnumFactory());
            this.severity.setValue(value);
          return this;
        }

        /**
         * @return {@link #human} (Text that can be used to describe the constraint in messages identifying that the constraint has been violated.). This is the underlying object with id, value and extensions. The accessor "getHuman" gives direct access to the value
         */
        public StringType getHumanElement() { 
          if (this.human == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.human");
            else if (Configuration.doAutoCreate())
              this.human = new StringType(); // bb
          return this.human;
        }

        public boolean hasHumanElement() { 
          return this.human != null && !this.human.isEmpty();
        }

        public boolean hasHuman() { 
          return this.human != null && !this.human.isEmpty();
        }

        /**
         * @param value {@link #human} (Text that can be used to describe the constraint in messages identifying that the constraint has been violated.). This is the underlying object with id, value and extensions. The accessor "getHuman" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setHumanElement(StringType value) { 
          this.human = value;
          return this;
        }

        /**
         * @return Text that can be used to describe the constraint in messages identifying that the constraint has been violated.
         */
        public String getHuman() { 
          return this.human == null ? null : this.human.getValue();
        }

        /**
         * @param value Text that can be used to describe the constraint in messages identifying that the constraint has been violated.
         */
        public ElementDefinitionConstraintComponent setHuman(String value) { 
            if (this.human == null)
              this.human = new StringType();
            this.human.setValue(value);
          return this;
        }

        /**
         * @return {@link #xpath} (An XPath expression of constraint that can be executed to see if this constraint is met.). This is the underlying object with id, value and extensions. The accessor "getXpath" gives direct access to the value
         */
        public StringType getXpathElement() { 
          if (this.xpath == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.xpath");
            else if (Configuration.doAutoCreate())
              this.xpath = new StringType(); // bb
          return this.xpath;
        }

        public boolean hasXpathElement() { 
          return this.xpath != null && !this.xpath.isEmpty();
        }

        public boolean hasXpath() { 
          return this.xpath != null && !this.xpath.isEmpty();
        }

        /**
         * @param value {@link #xpath} (An XPath expression of constraint that can be executed to see if this constraint is met.). This is the underlying object with id, value and extensions. The accessor "getXpath" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setXpathElement(StringType value) { 
          this.xpath = value;
          return this;
        }

        /**
         * @return An XPath expression of constraint that can be executed to see if this constraint is met.
         */
        public String getXpath() { 
          return this.xpath == null ? null : this.xpath.getValue();
        }

        /**
         * @param value An XPath expression of constraint that can be executed to see if this constraint is met.
         */
        public ElementDefinitionConstraintComponent setXpath(String value) { 
            if (this.xpath == null)
              this.xpath = new StringType();
            this.xpath.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("key", "id", "Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.", 0, java.lang.Integer.MAX_VALUE, key));
          childrenList.add(new Property("requirements", "string", "Description of why this constraint is necessary or appropriate.", 0, java.lang.Integer.MAX_VALUE, requirements));
          childrenList.add(new Property("severity", "code", "Identifies the impact constraint violation has on the conformance of the instance.", 0, java.lang.Integer.MAX_VALUE, severity));
          childrenList.add(new Property("human", "string", "Text that can be used to describe the constraint in messages identifying that the constraint has been violated.", 0, java.lang.Integer.MAX_VALUE, human));
          childrenList.add(new Property("xpath", "string", "An XPath expression of constraint that can be executed to see if this constraint is met.", 0, java.lang.Integer.MAX_VALUE, xpath));
        }

      public ElementDefinitionConstraintComponent copy() {
        ElementDefinitionConstraintComponent dst = new ElementDefinitionConstraintComponent();
        copyValues(dst);
        dst.key = key == null ? null : key.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        dst.severity = severity == null ? null : severity.copy();
        dst.human = human == null ? null : human.copy();
        dst.xpath = xpath == null ? null : xpath.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionConstraintComponent))
          return false;
        ElementDefinitionConstraintComponent o = (ElementDefinitionConstraintComponent) other;
        return compareDeep(key, o.key, true) && compareDeep(requirements, o.requirements, true) && compareDeep(severity, o.severity, true)
           && compareDeep(human, o.human, true) && compareDeep(xpath, o.xpath, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionConstraintComponent))
          return false;
        ElementDefinitionConstraintComponent o = (ElementDefinitionConstraintComponent) other;
        return compareValues(key, o.key, true) && compareValues(requirements, o.requirements, true) && compareValues(severity, o.severity, true)
           && compareValues(human, o.human, true) && compareValues(xpath, o.xpath, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (key == null || key.isEmpty()) && (requirements == null || requirements.isEmpty())
           && (severity == null || severity.isEmpty()) && (human == null || human.isEmpty()) && (xpath == null || xpath.isEmpty())
          ;
      }

  }

    @Block()
    public static class ElementDefinitionBindingComponent extends Element implements IBaseDatatypeElement {
        /**
         * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.
         */
        @Child(name = "strength", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="required | extensible | preferred | example", formalDefinition="Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances." )
        protected Enumeration<BindingStrength> strength;

        /**
         * Describes the intended use of this particular set of codes.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human explanation of the value set", formalDefinition="Describes the intended use of this particular set of codes." )
        protected StringType description;

        /**
         * Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.
         */
        @Child(name = "valueSet", type = {UriType.class, ValueSet.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Source of value set", formalDefinition="Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used." )
        protected Type valueSet;

        private static final long serialVersionUID = 1355538460L;

    /*
     * Constructor
     */
      public ElementDefinitionBindingComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ElementDefinitionBindingComponent(Enumeration<BindingStrength> strength) {
        super();
        this.strength = strength;
      }

        /**
         * @return {@link #strength} (Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.). This is the underlying object with id, value and extensions. The accessor "getStrength" gives direct access to the value
         */
        public Enumeration<BindingStrength> getStrengthElement() { 
          if (this.strength == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBindingComponent.strength");
            else if (Configuration.doAutoCreate())
              this.strength = new Enumeration<BindingStrength>(new BindingStrengthEnumFactory()); // bb
          return this.strength;
        }

        public boolean hasStrengthElement() { 
          return this.strength != null && !this.strength.isEmpty();
        }

        public boolean hasStrength() { 
          return this.strength != null && !this.strength.isEmpty();
        }

        /**
         * @param value {@link #strength} (Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.). This is the underlying object with id, value and extensions. The accessor "getStrength" gives direct access to the value
         */
        public ElementDefinitionBindingComponent setStrengthElement(Enumeration<BindingStrength> value) { 
          this.strength = value;
          return this;
        }

        /**
         * @return Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.
         */
        public BindingStrength getStrength() { 
          return this.strength == null ? null : this.strength.getValue();
        }

        /**
         * @param value Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.
         */
        public ElementDefinitionBindingComponent setStrength(BindingStrength value) { 
            if (this.strength == null)
              this.strength = new Enumeration<BindingStrength>(new BindingStrengthEnumFactory());
            this.strength.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Describes the intended use of this particular set of codes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBindingComponent.description");
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
         * @param value {@link #description} (Describes the intended use of this particular set of codes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ElementDefinitionBindingComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Describes the intended use of this particular set of codes.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Describes the intended use of this particular set of codes.
         */
        public ElementDefinitionBindingComponent setDescription(String value) { 
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
         * @return {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.)
         */
        public Type getValueSet() { 
          return this.valueSet;
        }

        /**
         * @return {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.)
         */
        public UriType getValueSetUriType() throws Exception { 
          if (!(this.valueSet instanceof UriType))
            throw new Exception("Type mismatch: the type UriType was expected, but "+this.valueSet.getClass().getName()+" was encountered");
          return (UriType) this.valueSet;
        }

        public boolean hasValueSetUriType() throws Exception { 
          return this.valueSet instanceof UriType;
        }

        /**
         * @return {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.)
         */
        public Reference getValueSetReference() throws Exception { 
          if (!(this.valueSet instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.valueSet.getClass().getName()+" was encountered");
          return (Reference) this.valueSet;
        }

        public boolean hasValueSetReference() throws Exception { 
          return this.valueSet instanceof Reference;
        }

        public boolean hasValueSet() { 
          return this.valueSet != null && !this.valueSet.isEmpty();
        }

        /**
         * @param value {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.)
         */
        public ElementDefinitionBindingComponent setValueSet(Type value) { 
          this.valueSet = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("strength", "code", "Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.", 0, java.lang.Integer.MAX_VALUE, strength));
          childrenList.add(new Property("description", "string", "Describes the intended use of this particular set of codes.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("valueSet[x]", "uri|Reference(ValueSet)", "Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.", 0, java.lang.Integer.MAX_VALUE, valueSet));
        }

      public ElementDefinitionBindingComponent copy() {
        ElementDefinitionBindingComponent dst = new ElementDefinitionBindingComponent();
        copyValues(dst);
        dst.strength = strength == null ? null : strength.copy();
        dst.description = description == null ? null : description.copy();
        dst.valueSet = valueSet == null ? null : valueSet.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionBindingComponent))
          return false;
        ElementDefinitionBindingComponent o = (ElementDefinitionBindingComponent) other;
        return compareDeep(strength, o.strength, true) && compareDeep(description, o.description, true)
           && compareDeep(valueSet, o.valueSet, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionBindingComponent))
          return false;
        ElementDefinitionBindingComponent o = (ElementDefinitionBindingComponent) other;
        return compareValues(strength, o.strength, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (strength == null || strength.isEmpty()) && (description == null || description.isEmpty())
           && (valueSet == null || valueSet.isEmpty());
      }

  }

    @Block()
    public static class ElementDefinitionMappingComponent extends Element implements IBaseDatatypeElement {
        /**
         * An internal reference to the definition of a mapping.
         */
        @Child(name = "identity", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference to mapping declaration", formalDefinition="An internal reference to the definition of a mapping." )
        protected IdType identity;

        /**
         * Identifies the computable language in which mapping.map is expressed.
         */
        @Child(name = "language", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Computable language of mapping", formalDefinition="Identifies the computable language in which mapping.map is expressed." )
        protected CodeType language;

        /**
         * Expresses what part of the target specification corresponds to this element.
         */
        @Child(name = "map", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Details of the mapping", formalDefinition="Expresses what part of the target specification corresponds to this element." )
        protected StringType map;

        private static final long serialVersionUID = -669205371L;

    /*
     * Constructor
     */
      public ElementDefinitionMappingComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ElementDefinitionMappingComponent(IdType identity, StringType map) {
        super();
        this.identity = identity;
        this.map = map;
      }

        /**
         * @return {@link #identity} (An internal reference to the definition of a mapping.). This is the underlying object with id, value and extensions. The accessor "getIdentity" gives direct access to the value
         */
        public IdType getIdentityElement() { 
          if (this.identity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionMappingComponent.identity");
            else if (Configuration.doAutoCreate())
              this.identity = new IdType(); // bb
          return this.identity;
        }

        public boolean hasIdentityElement() { 
          return this.identity != null && !this.identity.isEmpty();
        }

        public boolean hasIdentity() { 
          return this.identity != null && !this.identity.isEmpty();
        }

        /**
         * @param value {@link #identity} (An internal reference to the definition of a mapping.). This is the underlying object with id, value and extensions. The accessor "getIdentity" gives direct access to the value
         */
        public ElementDefinitionMappingComponent setIdentityElement(IdType value) { 
          this.identity = value;
          return this;
        }

        /**
         * @return An internal reference to the definition of a mapping.
         */
        public String getIdentity() { 
          return this.identity == null ? null : this.identity.getValue();
        }

        /**
         * @param value An internal reference to the definition of a mapping.
         */
        public ElementDefinitionMappingComponent setIdentity(String value) { 
            if (this.identity == null)
              this.identity = new IdType();
            this.identity.setValue(value);
          return this;
        }

        /**
         * @return {@link #language} (Identifies the computable language in which mapping.map is expressed.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public CodeType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionMappingComponent.language");
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
         * @param value {@link #language} (Identifies the computable language in which mapping.map is expressed.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public ElementDefinitionMappingComponent setLanguageElement(CodeType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return Identifies the computable language in which mapping.map is expressed.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value Identifies the computable language in which mapping.map is expressed.
         */
        public ElementDefinitionMappingComponent setLanguage(String value) { 
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
         * @return {@link #map} (Expresses what part of the target specification corresponds to this element.). This is the underlying object with id, value and extensions. The accessor "getMap" gives direct access to the value
         */
        public StringType getMapElement() { 
          if (this.map == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionMappingComponent.map");
            else if (Configuration.doAutoCreate())
              this.map = new StringType(); // bb
          return this.map;
        }

        public boolean hasMapElement() { 
          return this.map != null && !this.map.isEmpty();
        }

        public boolean hasMap() { 
          return this.map != null && !this.map.isEmpty();
        }

        /**
         * @param value {@link #map} (Expresses what part of the target specification corresponds to this element.). This is the underlying object with id, value and extensions. The accessor "getMap" gives direct access to the value
         */
        public ElementDefinitionMappingComponent setMapElement(StringType value) { 
          this.map = value;
          return this;
        }

        /**
         * @return Expresses what part of the target specification corresponds to this element.
         */
        public String getMap() { 
          return this.map == null ? null : this.map.getValue();
        }

        /**
         * @param value Expresses what part of the target specification corresponds to this element.
         */
        public ElementDefinitionMappingComponent setMap(String value) { 
            if (this.map == null)
              this.map = new StringType();
            this.map.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identity", "id", "An internal reference to the definition of a mapping.", 0, java.lang.Integer.MAX_VALUE, identity));
          childrenList.add(new Property("language", "code", "Identifies the computable language in which mapping.map is expressed.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("map", "string", "Expresses what part of the target specification corresponds to this element.", 0, java.lang.Integer.MAX_VALUE, map));
        }

      public ElementDefinitionMappingComponent copy() {
        ElementDefinitionMappingComponent dst = new ElementDefinitionMappingComponent();
        copyValues(dst);
        dst.identity = identity == null ? null : identity.copy();
        dst.language = language == null ? null : language.copy();
        dst.map = map == null ? null : map.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionMappingComponent))
          return false;
        ElementDefinitionMappingComponent o = (ElementDefinitionMappingComponent) other;
        return compareDeep(identity, o.identity, true) && compareDeep(language, o.language, true) && compareDeep(map, o.map, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionMappingComponent))
          return false;
        ElementDefinitionMappingComponent o = (ElementDefinitionMappingComponent) other;
        return compareValues(identity, o.identity, true) && compareValues(language, o.language, true) && compareValues(map, o.map, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identity == null || identity.isEmpty()) && (language == null || language.isEmpty())
           && (map == null || map.isEmpty());
      }

  }

    /**
     * The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.
     */
    @Child(name = "path", type = {StringType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The path of the element (see the Detailed Descriptions)", formalDefinition="The path identifies the element and is expressed as a \".\"-separated list of ancestor elements, beginning with the name of the resource or extension." )
    protected StringType path;

    /**
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case.
     */
    @Child(name = "representation", type = {CodeType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="How this element is represented in instances", formalDefinition="Codes that define how this element is represented in instances, when the deviation varies from the normal case." )
    protected List<Enumeration<PropertyRepresentation>> representation;

    /**
     * The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element.
     */
    @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name for this particular element definition (reference target)", formalDefinition="The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element." )
    protected StringType name;

    /**
     * The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     */
    @Child(name = "label", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name for element to display with or prompt for element", formalDefinition="The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form." )
    protected StringType label;

    /**
     * A code that provides the meaning for the element according to a particular terminology.
     */
    @Child(name = "code", type = {Coding.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Defining code", formalDefinition="A code that provides the meaning for the element according to a particular terminology." )
    protected List<Coding> code;

    /**
     * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).
     */
    @Child(name = "slicing", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="This element is sliced - slices follow", formalDefinition="Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)." )
    protected ElementDefinitionSlicingComponent slicing;

    /**
     * A concise description of what this element means (e.g. for use in auto-generated summaries).
     */
    @Child(name = "short", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Concise definition for xml presentation", formalDefinition="A concise description of what this element means (e.g. for use in auto-generated summaries)." )
    protected StringType short_;

    /**
     * Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.
     */
    @Child(name = "definition", type = {MarkdownType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Full formal definition as narrative text", formalDefinition="Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource." )
    protected MarkdownType definition;

    /**
     * Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.
     */
    @Child(name = "comments", type = {MarkdownType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Comments about the use of this element", formalDefinition="Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc." )
    protected MarkdownType comments;

    /**
     * This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.
     */
    @Child(name = "requirements", type = {MarkdownType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Why is this needed?", formalDefinition="This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element." )
    protected MarkdownType requirements;

    /**
     * Identifies additional names by which this element might also be known.
     */
    @Child(name = "alias", type = {StringType.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Other names", formalDefinition="Identifies additional names by which this element might also be known." )
    protected List<StringType> alias;

    /**
     * The minimum number of times this element SHALL appear in the instance.
     */
    @Child(name = "min", type = {IntegerType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Minimum Cardinality", formalDefinition="The minimum number of times this element SHALL appear in the instance." )
    protected IntegerType min;

    /**
     * The maximum number of times this element is permitted to appear in the instance.
     */
    @Child(name = "max", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Maximum Cardinality (a number or *)", formalDefinition="The maximum number of times this element is permitted to appear in the instance." )
    protected StringType max;

    /**
     * Information about the base definition of the element, provided to make it unncessary for tools to trace the derviation of the element through the derived and related profiles. This information is only provided where the element definition represents a constraint on another element definition, and must be present if there is a base element definition.
     */
    @Child(name = "base", type = {}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Base definition information for tools", formalDefinition="Information about the base definition of the element, provided to make it unncessary for tools to trace the derviation of the element through the derived and related profiles. This information is only provided where the element definition represents a constraint on another element definition, and must be present if there is a base element definition." )
    protected ElementDefinitionBaseComponent base;

    /**
     * The data type or resource that the value of this element is permitted to be.
     */
    @Child(name = "type", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Data type and Profile for this element", formalDefinition="The data type or resource that the value of this element is permitted to be." )
    protected List<TypeRefComponent> type;

    /**
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element.
     */
    @Child(name = "nameReference", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="To another element constraint (by element.name)", formalDefinition="Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element." )
    protected StringType nameReference;

    /**
     * The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false').
     */
    @Child(name = "defaultValue", type = {}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Specified value it missing from instance", formalDefinition="The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false')." )
    protected org.hl7.fhir.instance.model.Type defaultValue;

    /**
     * The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.
     */
    @Child(name = "meaningWhenMissing", type = {MarkdownType.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Implicit meaning when this element is missing", formalDefinition="The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'." )
    protected MarkdownType meaningWhenMissing;

    /**
     * Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing.
     */
    @Child(name = "fixed", type = {}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Value must be exactly this", formalDefinition="Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing." )
    protected org.hl7.fhir.instance.model.Type fixed;

    /**
     * Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.).
     */
    @Child(name = "pattern", type = {}, order=19, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Value must have at least these property values", formalDefinition="Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.)." )
    protected org.hl7.fhir.instance.model.Type pattern;

    /**
     * A sample value for this element demonstrating the type of information that would typically be captured.
     */
    @Child(name = "example", type = {}, order=20, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Example value: [as defined for type]", formalDefinition="A sample value for this element demonstrating the type of information that would typically be captured." )
    protected org.hl7.fhir.instance.model.Type example;

    /**
     * The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.
     */
    @Child(name = "minValue", type = {}, order=21, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Minimum Allowed Value (for some types)", formalDefinition="The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity." )
    protected org.hl7.fhir.instance.model.Type minValue;

    /**
     * The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.
     */
    @Child(name = "maxValue", type = {}, order=22, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Maximum Allowed Value (for some types)", formalDefinition="The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity." )
    protected org.hl7.fhir.instance.model.Type maxValue;

    /**
     * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.
     */
    @Child(name = "maxLength", type = {IntegerType.class}, order=23, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Max length for strings", formalDefinition="Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element." )
    protected IntegerType maxLength;

    /**
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance.
     */
    @Child(name = "condition", type = {IdType.class}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reference to invariant about presence", formalDefinition="A reference to an invariant that may make additional statements about the cardinality or value in the instance." )
    protected List<IdType> condition;

    /**
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance.
     */
    @Child(name = "constraint", type = {}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Condition that must evaluate to true", formalDefinition="Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance." )
    protected List<ElementDefinitionConstraintComponent> constraint;

    /**
     * If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful way.  If false, the element may be ignored and not supported.
     */
    @Child(name = "mustSupport", type = {BooleanType.class}, order=26, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If the element must supported", formalDefinition="If true, implementations that produce or consume resources SHALL provide \"support\" for the element in some meaningful way.  If false, the element may be ignored and not supported." )
    protected BooleanType mustSupport;

    /**
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     */
    @Child(name = "isModifier", type = {BooleanType.class}, order=27, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If this modifies the meaning of other elements", formalDefinition="If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system." )
    protected BooleanType isModifier;

    /**
     * Whether the element should be included if a client requests a search with the parameter _summary=true.
     */
    @Child(name = "isSummary", type = {BooleanType.class}, order=28, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include when _summary = true?", formalDefinition="Whether the element should be included if a client requests a search with the parameter _summary=true." )
    protected BooleanType isSummary;

    /**
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept).
     */
    @Child(name = "binding", type = {}, order=29, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="ValueSet details if this is coded", formalDefinition="Binds to a value set if this element is coded (code, Coding, CodeableConcept)." )
    protected ElementDefinitionBindingComponent binding;

    /**
     * Identifies a concept from an external specification that roughly corresponds to this element.
     */
    @Child(name = "mapping", type = {}, order=30, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Map element to another set of definitions", formalDefinition="Identifies a concept from an external specification that roughly corresponds to this element." )
    protected List<ElementDefinitionMappingComponent> mapping;

    private static final long serialVersionUID = -2084587620L;

  /*
   * Constructor
   */
    public ElementDefinition() {
      super();
    }

  /*
   * Constructor
   */
    public ElementDefinition(StringType path) {
      super();
      this.path = path;
    }

    /**
     * @return {@link #path} (The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
     */
    public StringType getPathElement() { 
      if (this.path == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.path");
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
     * @param value {@link #path} (The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
     */
    public ElementDefinition setPathElement(StringType value) { 
      this.path = value;
      return this;
    }

    /**
     * @return The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.
     */
    public String getPath() { 
      return this.path == null ? null : this.path.getValue();
    }

    /**
     * @param value The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.
     */
    public ElementDefinition setPath(String value) { 
        if (this.path == null)
          this.path = new StringType();
        this.path.setValue(value);
      return this;
    }

    /**
     * @return {@link #representation} (Codes that define how this element is represented in instances, when the deviation varies from the normal case.)
     */
    public List<Enumeration<PropertyRepresentation>> getRepresentation() { 
      if (this.representation == null)
        this.representation = new ArrayList<Enumeration<PropertyRepresentation>>();
      return this.representation;
    }

    public boolean hasRepresentation() { 
      if (this.representation == null)
        return false;
      for (Enumeration<PropertyRepresentation> item : this.representation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #representation} (Codes that define how this element is represented in instances, when the deviation varies from the normal case.)
     */
    // syntactic sugar
    public Enumeration<PropertyRepresentation> addRepresentationElement() {//2 
      Enumeration<PropertyRepresentation> t = new Enumeration<PropertyRepresentation>(new PropertyRepresentationEnumFactory());
      if (this.representation == null)
        this.representation = new ArrayList<Enumeration<PropertyRepresentation>>();
      this.representation.add(t);
      return t;
    }

    /**
     * @param value {@link #representation} (Codes that define how this element is represented in instances, when the deviation varies from the normal case.)
     */
    public ElementDefinition addRepresentation(PropertyRepresentation value) { //1
      Enumeration<PropertyRepresentation> t = new Enumeration<PropertyRepresentation>(new PropertyRepresentationEnumFactory());
      t.setValue(value);
      if (this.representation == null)
        this.representation = new ArrayList<Enumeration<PropertyRepresentation>>();
      this.representation.add(t);
      return this;
    }

    /**
     * @param value {@link #representation} (Codes that define how this element is represented in instances, when the deviation varies from the normal case.)
     */
    public boolean hasRepresentation(PropertyRepresentation value) { 
      if (this.representation == null)
        return false;
      for (Enumeration<PropertyRepresentation> v : this.representation)
        if (v.equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #name} (The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.name");
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
     * @param value {@link #name} (The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ElementDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element.
     */
    public ElementDefinition setName(String value) { 
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
     * @return {@link #label} (The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
     */
    public StringType getLabelElement() { 
      if (this.label == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.label");
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
     * @param value {@link #label} (The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
     */
    public ElementDefinition setLabelElement(StringType value) { 
      this.label = value;
      return this;
    }

    /**
     * @return The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     */
    public String getLabel() { 
      return this.label == null ? null : this.label.getValue();
    }

    /**
     * @param value The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     */
    public ElementDefinition setLabel(String value) { 
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
     * @return {@link #code} (A code that provides the meaning for the element according to a particular terminology.)
     */
    public List<Coding> getCode() { 
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      return this.code;
    }

    public boolean hasCode() { 
      if (this.code == null)
        return false;
      for (Coding item : this.code)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #code} (A code that provides the meaning for the element according to a particular terminology.)
     */
    // syntactic sugar
    public Coding addCode() { //3
      Coding t = new Coding();
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      this.code.add(t);
      return t;
    }

    // syntactic sugar
    public ElementDefinition addCode(Coding t) { //3
      if (t == null)
        return this;
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      this.code.add(t);
      return this;
    }

    /**
     * @return {@link #slicing} (Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).)
     */
    public ElementDefinitionSlicingComponent getSlicing() { 
      if (this.slicing == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.slicing");
        else if (Configuration.doAutoCreate())
          this.slicing = new ElementDefinitionSlicingComponent(); // cc
      return this.slicing;
    }

    public boolean hasSlicing() { 
      return this.slicing != null && !this.slicing.isEmpty();
    }

    /**
     * @param value {@link #slicing} (Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).)
     */
    public ElementDefinition setSlicing(ElementDefinitionSlicingComponent value) { 
      this.slicing = value;
      return this;
    }

    /**
     * @return {@link #short_} (A concise description of what this element means (e.g. for use in auto-generated summaries).). This is the underlying object with id, value and extensions. The accessor "getShort" gives direct access to the value
     */
    public StringType getShortElement() { 
      if (this.short_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.short_");
        else if (Configuration.doAutoCreate())
          this.short_ = new StringType(); // bb
      return this.short_;
    }

    public boolean hasShortElement() { 
      return this.short_ != null && !this.short_.isEmpty();
    }

    public boolean hasShort() { 
      return this.short_ != null && !this.short_.isEmpty();
    }

    /**
     * @param value {@link #short_} (A concise description of what this element means (e.g. for use in auto-generated summaries).). This is the underlying object with id, value and extensions. The accessor "getShort" gives direct access to the value
     */
    public ElementDefinition setShortElement(StringType value) { 
      this.short_ = value;
      return this;
    }

    /**
     * @return A concise description of what this element means (e.g. for use in auto-generated summaries).
     */
    public String getShort() { 
      return this.short_ == null ? null : this.short_.getValue();
    }

    /**
     * @param value A concise description of what this element means (e.g. for use in auto-generated summaries).
     */
    public ElementDefinition setShort(String value) { 
      if (Utilities.noString(value))
        this.short_ = null;
      else {
        if (this.short_ == null)
          this.short_ = new StringType();
        this.short_.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #definition} (Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public MarkdownType getDefinitionElement() { 
      if (this.definition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.definition");
        else if (Configuration.doAutoCreate())
          this.definition = new MarkdownType(); // bb
      return this.definition;
    }

    public boolean hasDefinitionElement() { 
      return this.definition != null && !this.definition.isEmpty();
    }

    public boolean hasDefinition() { 
      return this.definition != null && !this.definition.isEmpty();
    }

    /**
     * @param value {@link #definition} (Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public ElementDefinition setDefinitionElement(MarkdownType value) { 
      this.definition = value;
      return this;
    }

    /**
     * @return Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.
     */
    public String getDefinition() { 
      return this.definition == null ? null : this.definition.getValue();
    }

    /**
     * @param value Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.
     */
    public ElementDefinition setDefinition(String value) { 
      if (value == null)
        this.definition = null;
      else {
        if (this.definition == null)
          this.definition = new MarkdownType();
        this.definition.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #comments} (Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
     */
    public MarkdownType getCommentsElement() { 
      if (this.comments == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.comments");
        else if (Configuration.doAutoCreate())
          this.comments = new MarkdownType(); // bb
      return this.comments;
    }

    public boolean hasCommentsElement() { 
      return this.comments != null && !this.comments.isEmpty();
    }

    public boolean hasComments() { 
      return this.comments != null && !this.comments.isEmpty();
    }

    /**
     * @param value {@link #comments} (Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
     */
    public ElementDefinition setCommentsElement(MarkdownType value) { 
      this.comments = value;
      return this;
    }

    /**
     * @return Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.
     */
    public String getComments() { 
      return this.comments == null ? null : this.comments.getValue();
    }

    /**
     * @param value Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.
     */
    public ElementDefinition setComments(String value) { 
      if (value == null)
        this.comments = null;
      else {
        if (this.comments == null)
          this.comments = new MarkdownType();
        this.comments.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #requirements} (This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public MarkdownType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.requirements");
        else if (Configuration.doAutoCreate())
          this.requirements = new MarkdownType(); // bb
      return this.requirements;
    }

    public boolean hasRequirementsElement() { 
      return this.requirements != null && !this.requirements.isEmpty();
    }

    public boolean hasRequirements() { 
      return this.requirements != null && !this.requirements.isEmpty();
    }

    /**
     * @param value {@link #requirements} (This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public ElementDefinition setRequirementsElement(MarkdownType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.
     */
    public ElementDefinition setRequirements(String value) { 
      if (value == null)
        this.requirements = null;
      else {
        if (this.requirements == null)
          this.requirements = new MarkdownType();
        this.requirements.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #alias} (Identifies additional names by which this element might also be known.)
     */
    public List<StringType> getAlias() { 
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      return this.alias;
    }

    public boolean hasAlias() { 
      if (this.alias == null)
        return false;
      for (StringType item : this.alias)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #alias} (Identifies additional names by which this element might also be known.)
     */
    // syntactic sugar
    public StringType addAliasElement() {//2 
      StringType t = new StringType();
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      this.alias.add(t);
      return t;
    }

    /**
     * @param value {@link #alias} (Identifies additional names by which this element might also be known.)
     */
    public ElementDefinition addAlias(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      this.alias.add(t);
      return this;
    }

    /**
     * @param value {@link #alias} (Identifies additional names by which this element might also be known.)
     */
    public boolean hasAlias(String value) { 
      if (this.alias == null)
        return false;
      for (StringType v : this.alias)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #min} (The minimum number of times this element SHALL appear in the instance.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
     */
    public IntegerType getMinElement() { 
      if (this.min == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.min");
        else if (Configuration.doAutoCreate())
          this.min = new IntegerType(); // bb
      return this.min;
    }

    public boolean hasMinElement() { 
      return this.min != null && !this.min.isEmpty();
    }

    public boolean hasMin() { 
      return this.min != null && !this.min.isEmpty();
    }

    /**
     * @param value {@link #min} (The minimum number of times this element SHALL appear in the instance.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
     */
    public ElementDefinition setMinElement(IntegerType value) { 
      this.min = value;
      return this;
    }

    /**
     * @return The minimum number of times this element SHALL appear in the instance.
     */
    public int getMin() { 
      return this.min == null || this.min.isEmpty() ? 0 : this.min.getValue();
    }

    /**
     * @param value The minimum number of times this element SHALL appear in the instance.
     */
    public ElementDefinition setMin(int value) { 
        if (this.min == null)
          this.min = new IntegerType();
        this.min.setValue(value);
      return this;
    }

    /**
     * @return {@link #max} (The maximum number of times this element is permitted to appear in the instance.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
     */
    public StringType getMaxElement() { 
      if (this.max == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.max");
        else if (Configuration.doAutoCreate())
          this.max = new StringType(); // bb
      return this.max;
    }

    public boolean hasMaxElement() { 
      return this.max != null && !this.max.isEmpty();
    }

    public boolean hasMax() { 
      return this.max != null && !this.max.isEmpty();
    }

    /**
     * @param value {@link #max} (The maximum number of times this element is permitted to appear in the instance.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
     */
    public ElementDefinition setMaxElement(StringType value) { 
      this.max = value;
      return this;
    }

    /**
     * @return The maximum number of times this element is permitted to appear in the instance.
     */
    public String getMax() { 
      return this.max == null ? null : this.max.getValue();
    }

    /**
     * @param value The maximum number of times this element is permitted to appear in the instance.
     */
    public ElementDefinition setMax(String value) { 
      if (Utilities.noString(value))
        this.max = null;
      else {
        if (this.max == null)
          this.max = new StringType();
        this.max.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #base} (Information about the base definition of the element, provided to make it unncessary for tools to trace the derviation of the element through the derived and related profiles. This information is only provided where the element definition represents a constraint on another element definition, and must be present if there is a base element definition.)
     */
    public ElementDefinitionBaseComponent getBase() { 
      if (this.base == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.base");
        else if (Configuration.doAutoCreate())
          this.base = new ElementDefinitionBaseComponent(); // cc
      return this.base;
    }

    public boolean hasBase() { 
      return this.base != null && !this.base.isEmpty();
    }

    /**
     * @param value {@link #base} (Information about the base definition of the element, provided to make it unncessary for tools to trace the derviation of the element through the derived and related profiles. This information is only provided where the element definition represents a constraint on another element definition, and must be present if there is a base element definition.)
     */
    public ElementDefinition setBase(ElementDefinitionBaseComponent value) { 
      this.base = value;
      return this;
    }

    /**
     * @return {@link #type} (The data type or resource that the value of this element is permitted to be.)
     */
    public List<TypeRefComponent> getType() { 
      if (this.type == null)
        this.type = new ArrayList<TypeRefComponent>();
      return this.type;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (TypeRefComponent item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #type} (The data type or resource that the value of this element is permitted to be.)
     */
    // syntactic sugar
    public TypeRefComponent addType() { //3
      TypeRefComponent t = new TypeRefComponent();
      if (this.type == null)
        this.type = new ArrayList<TypeRefComponent>();
      this.type.add(t);
      return t;
    }

    // syntactic sugar
    public ElementDefinition addType(TypeRefComponent t) { //3
      if (t == null)
        return this;
      if (this.type == null)
        this.type = new ArrayList<TypeRefComponent>();
      this.type.add(t);
      return this;
    }

    /**
     * @return {@link #nameReference} (Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element.). This is the underlying object with id, value and extensions. The accessor "getNameReference" gives direct access to the value
     */
    public StringType getNameReferenceElement() { 
      if (this.nameReference == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.nameReference");
        else if (Configuration.doAutoCreate())
          this.nameReference = new StringType(); // bb
      return this.nameReference;
    }

    public boolean hasNameReferenceElement() { 
      return this.nameReference != null && !this.nameReference.isEmpty();
    }

    public boolean hasNameReference() { 
      return this.nameReference != null && !this.nameReference.isEmpty();
    }

    /**
     * @param value {@link #nameReference} (Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element.). This is the underlying object with id, value and extensions. The accessor "getNameReference" gives direct access to the value
     */
    public ElementDefinition setNameReferenceElement(StringType value) { 
      this.nameReference = value;
      return this;
    }

    /**
     * @return Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element.
     */
    public String getNameReference() { 
      return this.nameReference == null ? null : this.nameReference.getValue();
    }

    /**
     * @param value Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element.
     */
    public ElementDefinition setNameReference(String value) { 
      if (Utilities.noString(value))
        this.nameReference = null;
      else {
        if (this.nameReference == null)
          this.nameReference = new StringType();
        this.nameReference.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #defaultValue} (The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false').)
     */
    public org.hl7.fhir.instance.model.Type getDefaultValue() { 
      return this.defaultValue;
    }

    public boolean hasDefaultValue() { 
      return this.defaultValue != null && !this.defaultValue.isEmpty();
    }

    /**
     * @param value {@link #defaultValue} (The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false').)
     */
    public ElementDefinition setDefaultValue(org.hl7.fhir.instance.model.Type value) { 
      this.defaultValue = value;
      return this;
    }

    /**
     * @return {@link #meaningWhenMissing} (The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.). This is the underlying object with id, value and extensions. The accessor "getMeaningWhenMissing" gives direct access to the value
     */
    public MarkdownType getMeaningWhenMissingElement() { 
      if (this.meaningWhenMissing == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.meaningWhenMissing");
        else if (Configuration.doAutoCreate())
          this.meaningWhenMissing = new MarkdownType(); // bb
      return this.meaningWhenMissing;
    }

    public boolean hasMeaningWhenMissingElement() { 
      return this.meaningWhenMissing != null && !this.meaningWhenMissing.isEmpty();
    }

    public boolean hasMeaningWhenMissing() { 
      return this.meaningWhenMissing != null && !this.meaningWhenMissing.isEmpty();
    }

    /**
     * @param value {@link #meaningWhenMissing} (The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.). This is the underlying object with id, value and extensions. The accessor "getMeaningWhenMissing" gives direct access to the value
     */
    public ElementDefinition setMeaningWhenMissingElement(MarkdownType value) { 
      this.meaningWhenMissing = value;
      return this;
    }

    /**
     * @return The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.
     */
    public String getMeaningWhenMissing() { 
      return this.meaningWhenMissing == null ? null : this.meaningWhenMissing.getValue();
    }

    /**
     * @param value The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.
     */
    public ElementDefinition setMeaningWhenMissing(String value) { 
      if (value == null)
        this.meaningWhenMissing = null;
      else {
        if (this.meaningWhenMissing == null)
          this.meaningWhenMissing = new MarkdownType();
        this.meaningWhenMissing.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #fixed} (Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing.)
     */
    public org.hl7.fhir.instance.model.Type getFixed() { 
      return this.fixed;
    }

    public boolean hasFixed() { 
      return this.fixed != null && !this.fixed.isEmpty();
    }

    /**
     * @param value {@link #fixed} (Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing.)
     */
    public ElementDefinition setFixed(org.hl7.fhir.instance.model.Type value) { 
      this.fixed = value;
      return this;
    }

    /**
     * @return {@link #pattern} (Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.).)
     */
    public org.hl7.fhir.instance.model.Type getPattern() { 
      return this.pattern;
    }

    public boolean hasPattern() { 
      return this.pattern != null && !this.pattern.isEmpty();
    }

    /**
     * @param value {@link #pattern} (Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.).)
     */
    public ElementDefinition setPattern(org.hl7.fhir.instance.model.Type value) { 
      this.pattern = value;
      return this;
    }

    /**
     * @return {@link #example} (A sample value for this element demonstrating the type of information that would typically be captured.)
     */
    public org.hl7.fhir.instance.model.Type getExample() { 
      return this.example;
    }

    public boolean hasExample() { 
      return this.example != null && !this.example.isEmpty();
    }

    /**
     * @param value {@link #example} (A sample value for this element demonstrating the type of information that would typically be captured.)
     */
    public ElementDefinition setExample(org.hl7.fhir.instance.model.Type value) { 
      this.example = value;
      return this;
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public org.hl7.fhir.instance.model.Type getMinValue() { 
      return this.minValue;
    }

    public boolean hasMinValue() { 
      return this.minValue != null && !this.minValue.isEmpty();
    }

    /**
     * @param value {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public ElementDefinition setMinValue(org.hl7.fhir.instance.model.Type value) { 
      this.minValue = value;
      return this;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public org.hl7.fhir.instance.model.Type getMaxValue() { 
      return this.maxValue;
    }

    public boolean hasMaxValue() { 
      return this.maxValue != null && !this.maxValue.isEmpty();
    }

    /**
     * @param value {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public ElementDefinition setMaxValue(org.hl7.fhir.instance.model.Type value) { 
      this.maxValue = value;
      return this;
    }

    /**
     * @return {@link #maxLength} (Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.). This is the underlying object with id, value and extensions. The accessor "getMaxLength" gives direct access to the value
     */
    public IntegerType getMaxLengthElement() { 
      if (this.maxLength == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.maxLength");
        else if (Configuration.doAutoCreate())
          this.maxLength = new IntegerType(); // bb
      return this.maxLength;
    }

    public boolean hasMaxLengthElement() { 
      return this.maxLength != null && !this.maxLength.isEmpty();
    }

    public boolean hasMaxLength() { 
      return this.maxLength != null && !this.maxLength.isEmpty();
    }

    /**
     * @param value {@link #maxLength} (Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.). This is the underlying object with id, value and extensions. The accessor "getMaxLength" gives direct access to the value
     */
    public ElementDefinition setMaxLengthElement(IntegerType value) { 
      this.maxLength = value;
      return this;
    }

    /**
     * @return Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.
     */
    public int getMaxLength() { 
      return this.maxLength == null || this.maxLength.isEmpty() ? 0 : this.maxLength.getValue();
    }

    /**
     * @param value Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.
     */
    public ElementDefinition setMaxLength(int value) { 
        if (this.maxLength == null)
          this.maxLength = new IntegerType();
        this.maxLength.setValue(value);
      return this;
    }

    /**
     * @return {@link #condition} (A reference to an invariant that may make additional statements about the cardinality or value in the instance.)
     */
    public List<IdType> getCondition() { 
      if (this.condition == null)
        this.condition = new ArrayList<IdType>();
      return this.condition;
    }

    public boolean hasCondition() { 
      if (this.condition == null)
        return false;
      for (IdType item : this.condition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #condition} (A reference to an invariant that may make additional statements about the cardinality or value in the instance.)
     */
    // syntactic sugar
    public IdType addConditionElement() {//2 
      IdType t = new IdType();
      if (this.condition == null)
        this.condition = new ArrayList<IdType>();
      this.condition.add(t);
      return t;
    }

    /**
     * @param value {@link #condition} (A reference to an invariant that may make additional statements about the cardinality or value in the instance.)
     */
    public ElementDefinition addCondition(String value) { //1
      IdType t = new IdType();
      t.setValue(value);
      if (this.condition == null)
        this.condition = new ArrayList<IdType>();
      this.condition.add(t);
      return this;
    }

    /**
     * @param value {@link #condition} (A reference to an invariant that may make additional statements about the cardinality or value in the instance.)
     */
    public boolean hasCondition(String value) { 
      if (this.condition == null)
        return false;
      for (IdType v : this.condition)
        if (v.equals(value)) // id
          return true;
      return false;
    }

    /**
     * @return {@link #constraint} (Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance.)
     */
    public List<ElementDefinitionConstraintComponent> getConstraint() { 
      if (this.constraint == null)
        this.constraint = new ArrayList<ElementDefinitionConstraintComponent>();
      return this.constraint;
    }

    public boolean hasConstraint() { 
      if (this.constraint == null)
        return false;
      for (ElementDefinitionConstraintComponent item : this.constraint)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #constraint} (Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance.)
     */
    // syntactic sugar
    public ElementDefinitionConstraintComponent addConstraint() { //3
      ElementDefinitionConstraintComponent t = new ElementDefinitionConstraintComponent();
      if (this.constraint == null)
        this.constraint = new ArrayList<ElementDefinitionConstraintComponent>();
      this.constraint.add(t);
      return t;
    }

    // syntactic sugar
    public ElementDefinition addConstraint(ElementDefinitionConstraintComponent t) { //3
      if (t == null)
        return this;
      if (this.constraint == null)
        this.constraint = new ArrayList<ElementDefinitionConstraintComponent>();
      this.constraint.add(t);
      return this;
    }

    /**
     * @return {@link #mustSupport} (If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful way.  If false, the element may be ignored and not supported.). This is the underlying object with id, value and extensions. The accessor "getMustSupport" gives direct access to the value
     */
    public BooleanType getMustSupportElement() { 
      if (this.mustSupport == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.mustSupport");
        else if (Configuration.doAutoCreate())
          this.mustSupport = new BooleanType(); // bb
      return this.mustSupport;
    }

    public boolean hasMustSupportElement() { 
      return this.mustSupport != null && !this.mustSupport.isEmpty();
    }

    public boolean hasMustSupport() { 
      return this.mustSupport != null && !this.mustSupport.isEmpty();
    }

    /**
     * @param value {@link #mustSupport} (If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful way.  If false, the element may be ignored and not supported.). This is the underlying object with id, value and extensions. The accessor "getMustSupport" gives direct access to the value
     */
    public ElementDefinition setMustSupportElement(BooleanType value) { 
      this.mustSupport = value;
      return this;
    }

    /**
     * @return If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful way.  If false, the element may be ignored and not supported.
     */
    public boolean getMustSupport() { 
      return this.mustSupport == null || this.mustSupport.isEmpty() ? false : this.mustSupport.getValue();
    }

    /**
     * @param value If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful way.  If false, the element may be ignored and not supported.
     */
    public ElementDefinition setMustSupport(boolean value) { 
        if (this.mustSupport == null)
          this.mustSupport = new BooleanType();
        this.mustSupport.setValue(value);
      return this;
    }

    /**
     * @return {@link #isModifier} (If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.). This is the underlying object with id, value and extensions. The accessor "getIsModifier" gives direct access to the value
     */
    public BooleanType getIsModifierElement() { 
      if (this.isModifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.isModifier");
        else if (Configuration.doAutoCreate())
          this.isModifier = new BooleanType(); // bb
      return this.isModifier;
    }

    public boolean hasIsModifierElement() { 
      return this.isModifier != null && !this.isModifier.isEmpty();
    }

    public boolean hasIsModifier() { 
      return this.isModifier != null && !this.isModifier.isEmpty();
    }

    /**
     * @param value {@link #isModifier} (If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.). This is the underlying object with id, value and extensions. The accessor "getIsModifier" gives direct access to the value
     */
    public ElementDefinition setIsModifierElement(BooleanType value) { 
      this.isModifier = value;
      return this;
    }

    /**
     * @return If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     */
    public boolean getIsModifier() { 
      return this.isModifier == null || this.isModifier.isEmpty() ? false : this.isModifier.getValue();
    }

    /**
     * @param value If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     */
    public ElementDefinition setIsModifier(boolean value) { 
        if (this.isModifier == null)
          this.isModifier = new BooleanType();
        this.isModifier.setValue(value);
      return this;
    }

    /**
     * @return {@link #isSummary} (Whether the element should be included if a client requests a search with the parameter _summary=true.). This is the underlying object with id, value and extensions. The accessor "getIsSummary" gives direct access to the value
     */
    public BooleanType getIsSummaryElement() { 
      if (this.isSummary == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.isSummary");
        else if (Configuration.doAutoCreate())
          this.isSummary = new BooleanType(); // bb
      return this.isSummary;
    }

    public boolean hasIsSummaryElement() { 
      return this.isSummary != null && !this.isSummary.isEmpty();
    }

    public boolean hasIsSummary() { 
      return this.isSummary != null && !this.isSummary.isEmpty();
    }

    /**
     * @param value {@link #isSummary} (Whether the element should be included if a client requests a search with the parameter _summary=true.). This is the underlying object with id, value and extensions. The accessor "getIsSummary" gives direct access to the value
     */
    public ElementDefinition setIsSummaryElement(BooleanType value) { 
      this.isSummary = value;
      return this;
    }

    /**
     * @return Whether the element should be included if a client requests a search with the parameter _summary=true.
     */
    public boolean getIsSummary() { 
      return this.isSummary == null || this.isSummary.isEmpty() ? false : this.isSummary.getValue();
    }

    /**
     * @param value Whether the element should be included if a client requests a search with the parameter _summary=true.
     */
    public ElementDefinition setIsSummary(boolean value) { 
        if (this.isSummary == null)
          this.isSummary = new BooleanType();
        this.isSummary.setValue(value);
      return this;
    }

    /**
     * @return {@link #binding} (Binds to a value set if this element is coded (code, Coding, CodeableConcept).)
     */
    public ElementDefinitionBindingComponent getBinding() { 
      if (this.binding == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.binding");
        else if (Configuration.doAutoCreate())
          this.binding = new ElementDefinitionBindingComponent(); // cc
      return this.binding;
    }

    public boolean hasBinding() { 
      return this.binding != null && !this.binding.isEmpty();
    }

    /**
     * @param value {@link #binding} (Binds to a value set if this element is coded (code, Coding, CodeableConcept).)
     */
    public ElementDefinition setBinding(ElementDefinitionBindingComponent value) { 
      this.binding = value;
      return this;
    }

    /**
     * @return {@link #mapping} (Identifies a concept from an external specification that roughly corresponds to this element.)
     */
    public List<ElementDefinitionMappingComponent> getMapping() { 
      if (this.mapping == null)
        this.mapping = new ArrayList<ElementDefinitionMappingComponent>();
      return this.mapping;
    }

    public boolean hasMapping() { 
      if (this.mapping == null)
        return false;
      for (ElementDefinitionMappingComponent item : this.mapping)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #mapping} (Identifies a concept from an external specification that roughly corresponds to this element.)
     */
    // syntactic sugar
    public ElementDefinitionMappingComponent addMapping() { //3
      ElementDefinitionMappingComponent t = new ElementDefinitionMappingComponent();
      if (this.mapping == null)
        this.mapping = new ArrayList<ElementDefinitionMappingComponent>();
      this.mapping.add(t);
      return t;
    }

    // syntactic sugar
    public ElementDefinition addMapping(ElementDefinitionMappingComponent t) { //3
      if (t == null)
        return this;
      if (this.mapping == null)
        this.mapping = new ArrayList<ElementDefinitionMappingComponent>();
      this.mapping.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("path", "string", "The path identifies the element and is expressed as a \".\"-separated list of ancestor elements, beginning with the name of the resource or extension.", 0, java.lang.Integer.MAX_VALUE, path));
        childrenList.add(new Property("representation", "code", "Codes that define how this element is represented in instances, when the deviation varies from the normal case.", 0, java.lang.Integer.MAX_VALUE, representation));
        childrenList.add(new Property("name", "string", "The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("label", "string", "The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.", 0, java.lang.Integer.MAX_VALUE, label));
        childrenList.add(new Property("code", "Coding", "A code that provides the meaning for the element according to a particular terminology.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("slicing", "", "Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).", 0, java.lang.Integer.MAX_VALUE, slicing));
        childrenList.add(new Property("short", "string", "A concise description of what this element means (e.g. for use in auto-generated summaries).", 0, java.lang.Integer.MAX_VALUE, short_));
        childrenList.add(new Property("definition", "markdown", "Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("comments", "markdown", "Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.", 0, java.lang.Integer.MAX_VALUE, comments));
        childrenList.add(new Property("requirements", "markdown", "This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("alias", "string", "Identifies additional names by which this element might also be known.", 0, java.lang.Integer.MAX_VALUE, alias));
        childrenList.add(new Property("min", "integer", "The minimum number of times this element SHALL appear in the instance.", 0, java.lang.Integer.MAX_VALUE, min));
        childrenList.add(new Property("max", "string", "The maximum number of times this element is permitted to appear in the instance.", 0, java.lang.Integer.MAX_VALUE, max));
        childrenList.add(new Property("base", "", "Information about the base definition of the element, provided to make it unncessary for tools to trace the derviation of the element through the derived and related profiles. This information is only provided where the element definition represents a constraint on another element definition, and must be present if there is a base element definition.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("type", "", "The data type or resource that the value of this element is permitted to be.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("nameReference", "string", "Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element.", 0, java.lang.Integer.MAX_VALUE, nameReference));
        childrenList.add(new Property("defaultValue[x]", "*", "The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false').", 0, java.lang.Integer.MAX_VALUE, defaultValue));
        childrenList.add(new Property("meaningWhenMissing", "markdown", "The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.", 0, java.lang.Integer.MAX_VALUE, meaningWhenMissing));
        childrenList.add(new Property("fixed[x]", "*", "Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing.", 0, java.lang.Integer.MAX_VALUE, fixed));
        childrenList.add(new Property("pattern[x]", "*", "Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.).", 0, java.lang.Integer.MAX_VALUE, pattern));
        childrenList.add(new Property("example[x]", "*", "A sample value for this element demonstrating the type of information that would typically be captured.", 0, java.lang.Integer.MAX_VALUE, example));
        childrenList.add(new Property("minValue[x]", "*", "The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.", 0, java.lang.Integer.MAX_VALUE, minValue));
        childrenList.add(new Property("maxValue[x]", "*", "The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.", 0, java.lang.Integer.MAX_VALUE, maxValue));
        childrenList.add(new Property("maxLength", "integer", "Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.", 0, java.lang.Integer.MAX_VALUE, maxLength));
        childrenList.add(new Property("condition", "id", "A reference to an invariant that may make additional statements about the cardinality or value in the instance.", 0, java.lang.Integer.MAX_VALUE, condition));
        childrenList.add(new Property("constraint", "", "Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance.", 0, java.lang.Integer.MAX_VALUE, constraint));
        childrenList.add(new Property("mustSupport", "boolean", "If true, implementations that produce or consume resources SHALL provide \"support\" for the element in some meaningful way.  If false, the element may be ignored and not supported.", 0, java.lang.Integer.MAX_VALUE, mustSupport));
        childrenList.add(new Property("isModifier", "boolean", "If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.", 0, java.lang.Integer.MAX_VALUE, isModifier));
        childrenList.add(new Property("isSummary", "boolean", "Whether the element should be included if a client requests a search with the parameter _summary=true.", 0, java.lang.Integer.MAX_VALUE, isSummary));
        childrenList.add(new Property("binding", "", "Binds to a value set if this element is coded (code, Coding, CodeableConcept).", 0, java.lang.Integer.MAX_VALUE, binding));
        childrenList.add(new Property("mapping", "", "Identifies a concept from an external specification that roughly corresponds to this element.", 0, java.lang.Integer.MAX_VALUE, mapping));
      }

      public ElementDefinition copy() {
        ElementDefinition dst = new ElementDefinition();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        if (representation != null) {
          dst.representation = new ArrayList<Enumeration<PropertyRepresentation>>();
          for (Enumeration<PropertyRepresentation> i : representation)
            dst.representation.add(i.copy());
        };
        dst.name = name == null ? null : name.copy();
        dst.label = label == null ? null : label.copy();
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        dst.slicing = slicing == null ? null : slicing.copy();
        dst.short_ = short_ == null ? null : short_.copy();
        dst.definition = definition == null ? null : definition.copy();
        dst.comments = comments == null ? null : comments.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        if (alias != null) {
          dst.alias = new ArrayList<StringType>();
          for (StringType i : alias)
            dst.alias.add(i.copy());
        };
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
        dst.base = base == null ? null : base.copy();
        if (type != null) {
          dst.type = new ArrayList<TypeRefComponent>();
          for (TypeRefComponent i : type)
            dst.type.add(i.copy());
        };
        dst.nameReference = nameReference == null ? null : nameReference.copy();
        dst.defaultValue = defaultValue == null ? null : defaultValue.copy();
        dst.meaningWhenMissing = meaningWhenMissing == null ? null : meaningWhenMissing.copy();
        dst.fixed = fixed == null ? null : fixed.copy();
        dst.pattern = pattern == null ? null : pattern.copy();
        dst.example = example == null ? null : example.copy();
        dst.minValue = minValue == null ? null : minValue.copy();
        dst.maxValue = maxValue == null ? null : maxValue.copy();
        dst.maxLength = maxLength == null ? null : maxLength.copy();
        if (condition != null) {
          dst.condition = new ArrayList<IdType>();
          for (IdType i : condition)
            dst.condition.add(i.copy());
        };
        if (constraint != null) {
          dst.constraint = new ArrayList<ElementDefinitionConstraintComponent>();
          for (ElementDefinitionConstraintComponent i : constraint)
            dst.constraint.add(i.copy());
        };
        dst.mustSupport = mustSupport == null ? null : mustSupport.copy();
        dst.isModifier = isModifier == null ? null : isModifier.copy();
        dst.isSummary = isSummary == null ? null : isSummary.copy();
        dst.binding = binding == null ? null : binding.copy();
        if (mapping != null) {
          dst.mapping = new ArrayList<ElementDefinitionMappingComponent>();
          for (ElementDefinitionMappingComponent i : mapping)
            dst.mapping.add(i.copy());
        };
        return dst;
      }

      protected ElementDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinition))
          return false;
        ElementDefinition o = (ElementDefinition) other;
        return compareDeep(path, o.path, true) && compareDeep(representation, o.representation, true) && compareDeep(name, o.name, true)
           && compareDeep(label, o.label, true) && compareDeep(code, o.code, true) && compareDeep(slicing, o.slicing, true)
           && compareDeep(short_, o.short_, true) && compareDeep(definition, o.definition, true) && compareDeep(comments, o.comments, true)
           && compareDeep(requirements, o.requirements, true) && compareDeep(alias, o.alias, true) && compareDeep(min, o.min, true)
           && compareDeep(max, o.max, true) && compareDeep(base, o.base, true) && compareDeep(type, o.type, true)
           && compareDeep(nameReference, o.nameReference, true) && compareDeep(defaultValue, o.defaultValue, true)
           && compareDeep(meaningWhenMissing, o.meaningWhenMissing, true) && compareDeep(fixed, o.fixed, true)
           && compareDeep(pattern, o.pattern, true) && compareDeep(example, o.example, true) && compareDeep(minValue, o.minValue, true)
           && compareDeep(maxValue, o.maxValue, true) && compareDeep(maxLength, o.maxLength, true) && compareDeep(condition, o.condition, true)
           && compareDeep(constraint, o.constraint, true) && compareDeep(mustSupport, o.mustSupport, true)
           && compareDeep(isModifier, o.isModifier, true) && compareDeep(isSummary, o.isSummary, true) && compareDeep(binding, o.binding, true)
           && compareDeep(mapping, o.mapping, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinition))
          return false;
        ElementDefinition o = (ElementDefinition) other;
        return compareValues(path, o.path, true) && compareValues(representation, o.representation, true) && compareValues(name, o.name, true)
           && compareValues(label, o.label, true) && compareValues(short_, o.short_, true) && compareValues(definition, o.definition, true)
           && compareValues(comments, o.comments, true) && compareValues(requirements, o.requirements, true) && compareValues(alias, o.alias, true)
           && compareValues(min, o.min, true) && compareValues(max, o.max, true) && compareValues(nameReference, o.nameReference, true)
           && compareValues(meaningWhenMissing, o.meaningWhenMissing, true) && compareValues(maxLength, o.maxLength, true)
           && compareValues(condition, o.condition, true) && compareValues(mustSupport, o.mustSupport, true) && compareValues(isModifier, o.isModifier, true)
           && compareValues(isSummary, o.isSummary, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (path == null || path.isEmpty()) && (representation == null || representation.isEmpty())
           && (name == null || name.isEmpty()) && (label == null || label.isEmpty()) && (code == null || code.isEmpty())
           && (slicing == null || slicing.isEmpty()) && (short_ == null || short_.isEmpty()) && (definition == null || definition.isEmpty())
           && (comments == null || comments.isEmpty()) && (requirements == null || requirements.isEmpty())
           && (alias == null || alias.isEmpty()) && (min == null || min.isEmpty()) && (max == null || max.isEmpty())
           && (base == null || base.isEmpty()) && (type == null || type.isEmpty()) && (nameReference == null || nameReference.isEmpty())
           && (defaultValue == null || defaultValue.isEmpty()) && (meaningWhenMissing == null || meaningWhenMissing.isEmpty())
           && (fixed == null || fixed.isEmpty()) && (pattern == null || pattern.isEmpty()) && (example == null || example.isEmpty())
           && (minValue == null || minValue.isEmpty()) && (maxValue == null || maxValue.isEmpty()) && (maxLength == null || maxLength.isEmpty())
           && (condition == null || condition.isEmpty()) && (constraint == null || constraint.isEmpty())
           && (mustSupport == null || mustSupport.isEmpty()) && (isModifier == null || isModifier.isEmpty())
           && (isSummary == null || isSummary.isEmpty()) && (binding == null || binding.isEmpty()) && (mapping == null || mapping.isEmpty())
          ;
      }


}

