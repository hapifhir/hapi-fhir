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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
/**
 * Captures constraints on each element within the resource, profile, or extension.
 */
@DatatypeDef(name="ElementDefinition")
public class ElementDefinition extends Type implements ICompositeType {

    public enum PropertyRepresentation {
        /**
         * In XML, this property is represented as an attribute not an element.
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
            case XMLATTR: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case XMLATTR: return "In XML, this property is represented as an attribute not an element.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case XMLATTR: return "xmlAttr";
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

    public enum ResourceSlicingRules {
        /**
         * No additional content is allowed other than that described by the slices in this profile.
         */
        CLOSED, 
        /**
         * Additional content is allowed anywhere in the list.
         */
        OPEN, 
        /**
         * Additional content is allowed, but only at the end of the list.
         */
        OPENATEND, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceSlicingRules fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("closed".equals(codeString))
          return CLOSED;
        if ("open".equals(codeString))
          return OPEN;
        if ("openAtEnd".equals(codeString))
          return OPENATEND;
        throw new Exception("Unknown ResourceSlicingRules code '"+codeString+"'");
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
            case CLOSED: return "";
            case OPEN: return "";
            case OPENATEND: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CLOSED: return "No additional content is allowed other than that described by the slices in this profile.";
            case OPEN: return "Additional content is allowed anywhere in the list.";
            case OPENATEND: return "Additional content is allowed, but only at the end of the list.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CLOSED: return "closed";
            case OPEN: return "open";
            case OPENATEND: return "openAtEnd";
            default: return "?";
          }
        }
    }

  public static class ResourceSlicingRulesEnumFactory implements EnumFactory<ResourceSlicingRules> {
    public ResourceSlicingRules fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("closed".equals(codeString))
          return ResourceSlicingRules.CLOSED;
        if ("open".equals(codeString))
          return ResourceSlicingRules.OPEN;
        if ("openAtEnd".equals(codeString))
          return ResourceSlicingRules.OPENATEND;
        throw new IllegalArgumentException("Unknown ResourceSlicingRules code '"+codeString+"'");
        }
    public String toCode(ResourceSlicingRules code) {
      if (code == ResourceSlicingRules.CLOSED)
        return "closed";
      if (code == ResourceSlicingRules.OPEN)
        return "open";
      if (code == ResourceSlicingRules.OPENATEND)
        return "openAtEnd";
      return "?";
      }
    }

    public enum ResourceAggregationMode {
        /**
         * The reference is a local reference to a contained resource.
         */
        CONTAINED, 
        /**
         * The reference to a resource that has to be resolved externally to the resource that includes the reference.
         */
        REFERENCED, 
        /**
         * The resource the reference points to will be found in the same bundle as the resource that includes the reference.
         */
        BUNDLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceAggregationMode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("contained".equals(codeString))
          return CONTAINED;
        if ("referenced".equals(codeString))
          return REFERENCED;
        if ("bundled".equals(codeString))
          return BUNDLED;
        throw new Exception("Unknown ResourceAggregationMode code '"+codeString+"'");
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
            case CONTAINED: return "";
            case REFERENCED: return "";
            case BUNDLED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CONTAINED: return "The reference is a local reference to a contained resource.";
            case REFERENCED: return "The reference to a resource that has to be resolved externally to the resource that includes the reference.";
            case BUNDLED: return "The resource the reference points to will be found in the same bundle as the resource that includes the reference.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONTAINED: return "contained";
            case REFERENCED: return "referenced";
            case BUNDLED: return "bundled";
            default: return "?";
          }
        }
    }

  public static class ResourceAggregationModeEnumFactory implements EnumFactory<ResourceAggregationMode> {
    public ResourceAggregationMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("contained".equals(codeString))
          return ResourceAggregationMode.CONTAINED;
        if ("referenced".equals(codeString))
          return ResourceAggregationMode.REFERENCED;
        if ("bundled".equals(codeString))
          return ResourceAggregationMode.BUNDLED;
        throw new IllegalArgumentException("Unknown ResourceAggregationMode code '"+codeString+"'");
        }
    public String toCode(ResourceAggregationMode code) {
      if (code == ResourceAggregationMode.CONTAINED)
        return "contained";
      if (code == ResourceAggregationMode.REFERENCED)
        return "referenced";
      if (code == ResourceAggregationMode.BUNDLED)
        return "bundled";
      return "?";
      }
    }

    public enum ConstraintSeverity {
        /**
         * If the constraint is violated, the resource is not conformant.
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
            case ERROR: return "";
            case WARNING: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ERROR: return "If the constraint is violated, the resource is not conformant.";
            case WARNING: return "If the constraint is violated, the resource is conformant, but it is not necessarily following best practice.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ERROR: return "error";
            case WARNING: return "warning";
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

    public enum BindingConformance {
        /**
         * Only codes in the specified set are allowed.  If the binding is extensible, other codes may be used for concepts not covered by the bound set of codes.
         */
        REQUIRED, 
        /**
         * For greater interoperability, implementers are strongly encouraged to use the bound set of codes, however alternate codes may be used in derived profiles and implementations if necessary without being considered non-conformant.
         */
        PREFERRED, 
        /**
         * The codes in the set are an example to illustrate the meaning of the field. There is no particular preference for its use nor any assertion that the provided values are sufficient to meet implementation needs.
         */
        EXAMPLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BindingConformance fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("required".equals(codeString))
          return REQUIRED;
        if ("preferred".equals(codeString))
          return PREFERRED;
        if ("example".equals(codeString))
          return EXAMPLE;
        throw new Exception("Unknown BindingConformance code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REQUIRED: return "required";
            case PREFERRED: return "preferred";
            case EXAMPLE: return "example";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REQUIRED: return "";
            case PREFERRED: return "";
            case EXAMPLE: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REQUIRED: return "Only codes in the specified set are allowed.  If the binding is extensible, other codes may be used for concepts not covered by the bound set of codes.";
            case PREFERRED: return "For greater interoperability, implementers are strongly encouraged to use the bound set of codes, however alternate codes may be used in derived profiles and implementations if necessary without being considered non-conformant.";
            case EXAMPLE: return "The codes in the set are an example to illustrate the meaning of the field. There is no particular preference for its use nor any assertion that the provided values are sufficient to meet implementation needs.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUIRED: return "required";
            case PREFERRED: return "preferred";
            case EXAMPLE: return "example";
            default: return "?";
          }
        }
    }

  public static class BindingConformanceEnumFactory implements EnumFactory<BindingConformance> {
    public BindingConformance fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("required".equals(codeString))
          return BindingConformance.REQUIRED;
        if ("preferred".equals(codeString))
          return BindingConformance.PREFERRED;
        if ("example".equals(codeString))
          return BindingConformance.EXAMPLE;
        throw new IllegalArgumentException("Unknown BindingConformance code '"+codeString+"'");
        }
    public String toCode(BindingConformance code) {
      if (code == BindingConformance.REQUIRED)
        return "required";
      if (code == BindingConformance.PREFERRED)
        return "preferred";
      if (code == BindingConformance.EXAMPLE)
        return "example";
      return "?";
      }
    }

    @Block
    public static class ElementDefinitionSlicingComponent extends BackboneElement {
        /**
         * Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
         */
        @Child(name="discriminator", type={StringType.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Element values that used to distinguish the slices", formalDefinition="Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices." )
        protected List<StringType> discriminator;

        /**
         * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.
         */
        @Child(name="description", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Text description of how slicing works (or not)", formalDefinition="A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated." )
        protected StringType description;

        /**
         * If the matching elements have to occur in the same order as defined in the profile.
         */
        @Child(name="ordered", type={BooleanType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="If elements must be in same order as slices", formalDefinition="If the matching elements have to occur in the same order as defined in the profile." )
        protected BooleanType ordered;

        /**
         * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.
         */
        @Child(name="rules", type={CodeType.class}, order=4, min=1, max=1)
        @Description(shortDefinition="closed | open | openAtEnd", formalDefinition="Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end." )
        protected Enumeration<ResourceSlicingRules> rules;

        private static final long serialVersionUID = -321298491L;

      public ElementDefinitionSlicingComponent() {
        super();
      }

      public ElementDefinitionSlicingComponent(Enumeration<ResourceSlicingRules> rules) {
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
          return this.ordered == null ? false : this.ordered.getValue();
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
        public Enumeration<ResourceSlicingRules> getRulesElement() { 
          if (this.rules == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionSlicingComponent.rules");
            else if (Configuration.doAutoCreate())
              this.rules = new Enumeration<ResourceSlicingRules>(new ResourceSlicingRulesEnumFactory()); // bb
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
        public ElementDefinitionSlicingComponent setRulesElement(Enumeration<ResourceSlicingRules> value) { 
          this.rules = value;
          return this;
        }

        /**
         * @return Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.
         */
        public ResourceSlicingRules getRules() { 
          return this.rules == null ? null : this.rules.getValue();
        }

        /**
         * @param value Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.
         */
        public ElementDefinitionSlicingComponent setRules(ResourceSlicingRules value) { 
            if (this.rules == null)
              this.rules = new Enumeration<ResourceSlicingRules>(new ResourceSlicingRulesEnumFactory());
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

    @Block
    public static class TypeRefComponent extends BackboneElement {
        /**
         * Name of Data type or Resource that is a(or the) type used for this element.
         */
        @Child(name="code", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Name of Data type or Resource", formalDefinition="Name of Data type or Resource that is a(or the) type used for this element." )
        protected CodeType code;

        /**
         * Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile.
         */
        @Child(name="profile", type={UriType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Profile.structure to apply", formalDefinition="Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile." )
        protected UriType profile;

        /**
         * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.
         */
        @Child(name="aggregation", type={CodeType.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="contained | referenced | bundled - how aggregated", formalDefinition="If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle." )
        protected List<Enumeration<ResourceAggregationMode>> aggregation;

        private static final long serialVersionUID = -1527133887L;

      public TypeRefComponent() {
        super();
      }

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
         * @return {@link #profile} (Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
         */
        public UriType getProfileElement() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TypeRefComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new UriType(); // bb
          return this.profile;
        }

        public boolean hasProfileElement() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
         */
        public TypeRefComponent setProfileElement(UriType value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile.
         */
        public String getProfile() { 
          return this.profile == null ? null : this.profile.getValue();
        }

        /**
         * @param value Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile.
         */
        public TypeRefComponent setProfile(String value) { 
          if (Utilities.noString(value))
            this.profile = null;
          else {
            if (this.profile == null)
              this.profile = new UriType();
            this.profile.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
        public List<Enumeration<ResourceAggregationMode>> getAggregation() { 
          if (this.aggregation == null)
            this.aggregation = new ArrayList<Enumeration<ResourceAggregationMode>>();
          return this.aggregation;
        }

        public boolean hasAggregation() { 
          if (this.aggregation == null)
            return false;
          for (Enumeration<ResourceAggregationMode> item : this.aggregation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
    // syntactic sugar
        public Enumeration<ResourceAggregationMode> addAggregationElement() {//2 
          Enumeration<ResourceAggregationMode> t = new Enumeration<ResourceAggregationMode>(new ResourceAggregationModeEnumFactory());
          if (this.aggregation == null)
            this.aggregation = new ArrayList<Enumeration<ResourceAggregationMode>>();
          this.aggregation.add(t);
          return t;
        }

        /**
         * @param value {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
        public TypeRefComponent addAggregation(ResourceAggregationMode value) { //1
          Enumeration<ResourceAggregationMode> t = new Enumeration<ResourceAggregationMode>(new ResourceAggregationModeEnumFactory());
          t.setValue(value);
          if (this.aggregation == null)
            this.aggregation = new ArrayList<Enumeration<ResourceAggregationMode>>();
          this.aggregation.add(t);
          return this;
        }

        /**
         * @param value {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
        public boolean hasAggregation(ResourceAggregationMode value) { 
          if (this.aggregation == null)
            return false;
          for (Enumeration<ResourceAggregationMode> v : this.aggregation)
            if (v.equals(value)) // code
              return true;
          return false;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "Name of Data type or Resource that is a(or the) type used for this element.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("profile", "uri", "Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile.", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("aggregation", "code", "If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.", 0, java.lang.Integer.MAX_VALUE, aggregation));
        }

      public TypeRefComponent copy() {
        TypeRefComponent dst = new TypeRefComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.profile = profile == null ? null : profile.copy();
        if (aggregation != null) {
          dst.aggregation = new ArrayList<Enumeration<ResourceAggregationMode>>();
          for (Enumeration<ResourceAggregationMode> i : aggregation)
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

    @Block
    public static class ElementDefinitionConstraintComponent extends BackboneElement {
        /**
         * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.
         */
        @Child(name="key", type={IdType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Target of 'condition' reference above", formalDefinition="Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality." )
        protected IdType key;

        /**
         * Used to label the constraint in OCL or in short displays incapable of displaying the full human description.
         */
        @Child(name="name", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Short human label", formalDefinition="Used to label the constraint in OCL or in short displays incapable of displaying the full human description." )
        protected StringType name;

        /**
         * Identifies the impact constraint violation has on the conformance of the instance.
         */
        @Child(name="severity", type={CodeType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="error | warning", formalDefinition="Identifies the impact constraint violation has on the conformance of the instance." )
        protected Enumeration<ConstraintSeverity> severity;

        /**
         * Text that can be used to describe the constraint in messages identifying that the constraint has been violated.
         */
        @Child(name="human", type={StringType.class}, order=4, min=1, max=1)
        @Description(shortDefinition="Human description of constraint", formalDefinition="Text that can be used to describe the constraint in messages identifying that the constraint has been violated." )
        protected StringType human;

        /**
         * An XPath expression of constraint that can be executed to see if this constraint is met.
         */
        @Child(name="xpath", type={StringType.class}, order=5, min=1, max=1)
        @Description(shortDefinition="XPath expression of constraint", formalDefinition="An XPath expression of constraint that can be executed to see if this constraint is met." )
        protected StringType xpath;

        private static final long serialVersionUID = -1195616532L;

      public ElementDefinitionConstraintComponent() {
        super();
      }

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
         * @return {@link #name} (Used to label the constraint in OCL or in short displays incapable of displaying the full human description.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.name");
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
         * @param value {@link #name} (Used to label the constraint in OCL or in short displays incapable of displaying the full human description.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Used to label the constraint in OCL or in short displays incapable of displaying the full human description.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Used to label the constraint in OCL or in short displays incapable of displaying the full human description.
         */
        public ElementDefinitionConstraintComponent setName(String value) { 
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
          childrenList.add(new Property("name", "string", "Used to label the constraint in OCL or in short displays incapable of displaying the full human description.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("severity", "code", "Identifies the impact constraint violation has on the conformance of the instance.", 0, java.lang.Integer.MAX_VALUE, severity));
          childrenList.add(new Property("human", "string", "Text that can be used to describe the constraint in messages identifying that the constraint has been violated.", 0, java.lang.Integer.MAX_VALUE, human));
          childrenList.add(new Property("xpath", "string", "An XPath expression of constraint that can be executed to see if this constraint is met.", 0, java.lang.Integer.MAX_VALUE, xpath));
        }

      public ElementDefinitionConstraintComponent copy() {
        ElementDefinitionConstraintComponent dst = new ElementDefinitionConstraintComponent();
        copyValues(dst);
        dst.key = key == null ? null : key.copy();
        dst.name = name == null ? null : name.copy();
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
        return compareDeep(key, o.key, true) && compareDeep(name, o.name, true) && compareDeep(severity, o.severity, true)
           && compareDeep(human, o.human, true) && compareDeep(xpath, o.xpath, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionConstraintComponent))
          return false;
        ElementDefinitionConstraintComponent o = (ElementDefinitionConstraintComponent) other;
        return compareValues(key, o.key, true) && compareValues(name, o.name, true) && compareValues(severity, o.severity, true)
           && compareValues(human, o.human, true) && compareValues(xpath, o.xpath, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (key == null || key.isEmpty()) && (name == null || name.isEmpty())
           && (severity == null || severity.isEmpty()) && (human == null || human.isEmpty()) && (xpath == null || xpath.isEmpty())
          ;
      }

  }

    @Block
    public static class ElementDefinitionBindingComponent extends BackboneElement {
        /**
         * A descriptive name for this - can be useful for generating implementation artifacts.
         */
        @Child(name="name", type={StringType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Descriptive Name", formalDefinition="A descriptive name for this - can be useful for generating implementation artifacts." )
        protected StringType name;

        /**
         * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone.
         */
        @Child(name="isExtensible", type={BooleanType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Can additional codes be used?", formalDefinition="If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone." )
        protected BooleanType isExtensible;

        /**
         * Indicates the degree of conformance expectations associated with this binding.
         */
        @Child(name="conformance", type={CodeType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="required | preferred | example", formalDefinition="Indicates the degree of conformance expectations associated with this binding." )
        protected Enumeration<BindingConformance> conformance;

        /**
         * Describes the intended use of this particular set of codes.
         */
        @Child(name="description", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Human explanation of the value set", formalDefinition="Describes the intended use of this particular set of codes." )
        protected StringType description;

        /**
         * Points to the value set or external definition that identifies the set of codes to be used.
         */
        @Child(name="reference", type={UriType.class, ValueSet.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Source of value set", formalDefinition="Points to the value set or external definition that identifies the set of codes to be used." )
        protected Type reference;

        private static final long serialVersionUID = 1041151319L;

      public ElementDefinitionBindingComponent() {
        super();
      }

      public ElementDefinitionBindingComponent(StringType name, BooleanType isExtensible) {
        super();
        this.name = name;
        this.isExtensible = isExtensible;
      }

        /**
         * @return {@link #name} (A descriptive name for this - can be useful for generating implementation artifacts.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBindingComponent.name");
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
         * @param value {@link #name} (A descriptive name for this - can be useful for generating implementation artifacts.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ElementDefinitionBindingComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A descriptive name for this - can be useful for generating implementation artifacts.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A descriptive name for this - can be useful for generating implementation artifacts.
         */
        public ElementDefinitionBindingComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #isExtensible} (If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone.). This is the underlying object with id, value and extensions. The accessor "getIsExtensible" gives direct access to the value
         */
        public BooleanType getIsExtensibleElement() { 
          if (this.isExtensible == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBindingComponent.isExtensible");
            else if (Configuration.doAutoCreate())
              this.isExtensible = new BooleanType(); // bb
          return this.isExtensible;
        }

        public boolean hasIsExtensibleElement() { 
          return this.isExtensible != null && !this.isExtensible.isEmpty();
        }

        public boolean hasIsExtensible() { 
          return this.isExtensible != null && !this.isExtensible.isEmpty();
        }

        /**
         * @param value {@link #isExtensible} (If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone.). This is the underlying object with id, value and extensions. The accessor "getIsExtensible" gives direct access to the value
         */
        public ElementDefinitionBindingComponent setIsExtensibleElement(BooleanType value) { 
          this.isExtensible = value;
          return this;
        }

        /**
         * @return If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone.
         */
        public boolean getIsExtensible() { 
          return this.isExtensible == null ? false : this.isExtensible.getValue();
        }

        /**
         * @param value If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone.
         */
        public ElementDefinitionBindingComponent setIsExtensible(boolean value) { 
            if (this.isExtensible == null)
              this.isExtensible = new BooleanType();
            this.isExtensible.setValue(value);
          return this;
        }

        /**
         * @return {@link #conformance} (Indicates the degree of conformance expectations associated with this binding.). This is the underlying object with id, value and extensions. The accessor "getConformance" gives direct access to the value
         */
        public Enumeration<BindingConformance> getConformanceElement() { 
          if (this.conformance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBindingComponent.conformance");
            else if (Configuration.doAutoCreate())
              this.conformance = new Enumeration<BindingConformance>(new BindingConformanceEnumFactory()); // bb
          return this.conformance;
        }

        public boolean hasConformanceElement() { 
          return this.conformance != null && !this.conformance.isEmpty();
        }

        public boolean hasConformance() { 
          return this.conformance != null && !this.conformance.isEmpty();
        }

        /**
         * @param value {@link #conformance} (Indicates the degree of conformance expectations associated with this binding.). This is the underlying object with id, value and extensions. The accessor "getConformance" gives direct access to the value
         */
        public ElementDefinitionBindingComponent setConformanceElement(Enumeration<BindingConformance> value) { 
          this.conformance = value;
          return this;
        }

        /**
         * @return Indicates the degree of conformance expectations associated with this binding.
         */
        public BindingConformance getConformance() { 
          return this.conformance == null ? null : this.conformance.getValue();
        }

        /**
         * @param value Indicates the degree of conformance expectations associated with this binding.
         */
        public ElementDefinitionBindingComponent setConformance(BindingConformance value) { 
          if (value == null)
            this.conformance = null;
          else {
            if (this.conformance == null)
              this.conformance = new Enumeration<BindingConformance>(new BindingConformanceEnumFactory());
            this.conformance.setValue(value);
          }
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
         * @return {@link #reference} (Points to the value set or external definition that identifies the set of codes to be used.)
         */
        public Type getReference() { 
          return this.reference;
        }

        /**
         * @return {@link #reference} (Points to the value set or external definition that identifies the set of codes to be used.)
         */
        public UriType getReferenceUriType() throws Exception { 
          if (!(this.reference instanceof UriType))
            throw new Exception("Type mismatch: the type UriType was expected, but "+this.reference.getClass().getName()+" was encountered");
          return (UriType) this.reference;
        }

        /**
         * @return {@link #reference} (Points to the value set or external definition that identifies the set of codes to be used.)
         */
        public Reference getReferenceReference() throws Exception { 
          if (!(this.reference instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.reference.getClass().getName()+" was encountered");
          return (Reference) this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Points to the value set or external definition that identifies the set of codes to be used.)
         */
        public ElementDefinitionBindingComponent setReference(Type value) { 
          this.reference = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "A descriptive name for this - can be useful for generating implementation artifacts.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("isExtensible", "boolean", "If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone.", 0, java.lang.Integer.MAX_VALUE, isExtensible));
          childrenList.add(new Property("conformance", "code", "Indicates the degree of conformance expectations associated with this binding.", 0, java.lang.Integer.MAX_VALUE, conformance));
          childrenList.add(new Property("description", "string", "Describes the intended use of this particular set of codes.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("reference[x]", "uri|Reference(ValueSet)", "Points to the value set or external definition that identifies the set of codes to be used.", 0, java.lang.Integer.MAX_VALUE, reference));
        }

      public ElementDefinitionBindingComponent copy() {
        ElementDefinitionBindingComponent dst = new ElementDefinitionBindingComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.isExtensible = isExtensible == null ? null : isExtensible.copy();
        dst.conformance = conformance == null ? null : conformance.copy();
        dst.description = description == null ? null : description.copy();
        dst.reference = reference == null ? null : reference.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionBindingComponent))
          return false;
        ElementDefinitionBindingComponent o = (ElementDefinitionBindingComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(isExtensible, o.isExtensible, true) && compareDeep(conformance, o.conformance, true)
           && compareDeep(description, o.description, true) && compareDeep(reference, o.reference, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionBindingComponent))
          return false;
        ElementDefinitionBindingComponent o = (ElementDefinitionBindingComponent) other;
        return compareValues(name, o.name, true) && compareValues(isExtensible, o.isExtensible, true) && compareValues(conformance, o.conformance, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (isExtensible == null || isExtensible.isEmpty())
           && (conformance == null || conformance.isEmpty()) && (description == null || description.isEmpty())
           && (reference == null || reference.isEmpty());
      }

  }

    @Block
    public static class ElementDefinitionMappingComponent extends BackboneElement {
        /**
         * An internal reference to the definition of a mapping.
         */
        @Child(name="identity", type={IdType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Reference to mapping declaration", formalDefinition="An internal reference to the definition of a mapping." )
        protected IdType identity;

        /**
         * Expresses what part of the target specification corresponds to this element.
         */
        @Child(name="map", type={StringType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Details of the mapping", formalDefinition="Expresses what part of the target specification corresponds to this element." )
        protected StringType map;

        private static final long serialVersionUID = -450627426L;

      public ElementDefinitionMappingComponent() {
        super();
      }

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
          childrenList.add(new Property("map", "string", "Expresses what part of the target specification corresponds to this element.", 0, java.lang.Integer.MAX_VALUE, map));
        }

      public ElementDefinitionMappingComponent copy() {
        ElementDefinitionMappingComponent dst = new ElementDefinitionMappingComponent();
        copyValues(dst);
        dst.identity = identity == null ? null : identity.copy();
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
        return compareDeep(identity, o.identity, true) && compareDeep(map, o.map, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionMappingComponent))
          return false;
        ElementDefinitionMappingComponent o = (ElementDefinitionMappingComponent) other;
        return compareValues(identity, o.identity, true) && compareValues(map, o.map, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identity == null || identity.isEmpty()) && (map == null || map.isEmpty())
          ;
      }

  }

    /**
     * The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.
     */
    @Child(name = "path", type = {StringType.class}, order = 0, min = 1, max = 1)
    @Description(shortDefinition="The path of the element (see the Detailed Descriptions)", formalDefinition="The path identifies the element and is expressed as a '.'-separated list of ancestor elements, beginning with the name of the resource or extension." )
    protected StringType path;

    /**
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case.
     */
    @Child(name = "representation", type = {CodeType.class}, order = 1, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="How this element is represented in instances", formalDefinition="Codes that define how this element is represented in instances, when the deviation varies from the normal case." )
    protected List<Enumeration<PropertyRepresentation>> representation;

    /**
     * The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element.
     */
    @Child(name = "name", type = {StringType.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Name for this particular element definition (reference target)", formalDefinition="The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element." )
    protected StringType name;

    /**
     * Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).
     */
    @Child(name = "slicing", type = {}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="This element is sliced - slices follow", formalDefinition="Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)." )
    protected ElementDefinitionSlicingComponent slicing;

    /**
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification).
     */
    @Child(name = "short_", type = {StringType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Concise definition for xml presentation", formalDefinition="A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)." )
    protected StringType short_;

    /**
     * The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.
     */
    @Child(name = "formal", type = {StringType.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Full formal definition in human language", formalDefinition="The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource." )
    protected StringType formal;

    /**
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     */
    @Child(name = "comments", type = {StringType.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="Comments about the use of this element", formalDefinition="Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc." )
    protected StringType comments;

    /**
     * Explains why this element is needed and why it's been constrained as it has.
     */
    @Child(name = "requirements", type = {StringType.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="Why is this needed?", formalDefinition="Explains why this element is needed and why it's been constrained as it has." )
    protected StringType requirements;

    /**
     * Identifies additional names by which this element might also be known.
     */
    @Child(name = "synonym", type = {StringType.class}, order = 8, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Other names", formalDefinition="Identifies additional names by which this element might also be known." )
    protected List<StringType> synonym;

    /**
     * The minimum number of times this element SHALL appear in the instance.
     */
    @Child(name = "min", type = {IntegerType.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Minimum Cardinality", formalDefinition="The minimum number of times this element SHALL appear in the instance." )
    protected IntegerType min;

    /**
     * The maximum number of times this element is permitted to appear in the instance.
     */
    @Child(name = "max", type = {StringType.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="Maximum Cardinality (a number or *)", formalDefinition="The maximum number of times this element is permitted to appear in the instance." )
    protected StringType max;

    /**
     * The data type or resource that the value of this element is permitted to be.
     */
    @Child(name = "type", type = {}, order = 11, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Data type and Profile for this element", formalDefinition="The data type or resource that the value of this element is permitted to be." )
    protected List<TypeRefComponent> type;

    /**
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element.
     */
    @Child(name = "nameReference", type = {StringType.class}, order = 12, min = 0, max = 1)
    @Description(shortDefinition="To another element constraint (by element.name)", formalDefinition="Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element." )
    protected StringType nameReference;

    /**
     * The value that should be used if there is no value stated in the instance.
     */
    @Child(name = "defaultValue", type = {}, order = 13, min = 0, max = 1)
    @Description(shortDefinition="Specified value it missing from instance", formalDefinition="The value that should be used if there is no value stated in the instance." )
    protected org.hl7.fhir.instance.model.Type defaultValue;

    /**
     * The Implicit meaning that is to be understood when this element is missing.
     */
    @Child(name = "meaningWhenMissing", type = {StringType.class}, order = 14, min = 0, max = 1)
    @Description(shortDefinition="Implicit meaning when this element is missing", formalDefinition="The Implicit meaning that is to be understood when this element is missing." )
    protected StringType meaningWhenMissing;

    /**
     * Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing.
     */
    @Child(name = "fixed", type = {}, order = 15, min = 0, max = 1)
    @Description(shortDefinition="Value must be exactly this", formalDefinition="Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing." )
    protected org.hl7.fhir.instance.model.Type fixed;

    /**
     * Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.).
     */
    @Child(name = "pattern", type = {}, order = 16, min = 0, max = 1)
    @Description(shortDefinition="Value must have at least these property values", formalDefinition="Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.)." )
    protected org.hl7.fhir.instance.model.Type pattern;

    /**
     * An example value for this element.
     */
    @Child(name = "example", type = {}, order = 17, min = 0, max = 1)
    @Description(shortDefinition="Example value: [as defined for type]", formalDefinition="An example value for this element." )
    protected org.hl7.fhir.instance.model.Type example;

    /**
     * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.
     */
    @Child(name = "maxLength", type = {IntegerType.class}, order = 18, min = 0, max = 1)
    @Description(shortDefinition="Max length for strings", formalDefinition="Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element." )
    protected IntegerType maxLength;

    /**
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance.
     */
    @Child(name = "condition", type = {IdType.class}, order = 19, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Reference to invariant about presence", formalDefinition="A reference to an invariant that may make additional statements about the cardinality or value in the instance." )
    protected List<IdType> condition;

    /**
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance.
     */
    @Child(name = "constraint", type = {}, order = 20, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Condition that must evaluate to true", formalDefinition="Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance." )
    protected List<ElementDefinitionConstraintComponent> constraint;

    /**
     * If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported.
     */
    @Child(name = "mustSupport", type = {BooleanType.class}, order = 21, min = 0, max = 1)
    @Description(shortDefinition="If the element must supported", formalDefinition="If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported." )
    protected BooleanType mustSupport;

    /**
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     */
    @Child(name = "isModifier", type = {BooleanType.class}, order = 22, min = 0, max = 1)
    @Description(shortDefinition="If this modifies the meaning of other elements", formalDefinition="If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system." )
    protected BooleanType isModifier;

    /**
     * Whether the element should be included if a client requests a search with the parameter _summary=true.
     */
    @Child(name = "isSummary", type = {BooleanType.class}, order = 23, min = 0, max = 1)
    @Description(shortDefinition="Include when _summary = true?", formalDefinition="Whether the element should be included if a client requests a search with the parameter _summary=true." )
    protected BooleanType isSummary;

    /**
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept).
     */
    @Child(name = "binding", type = {}, order = 24, min = 0, max = 1)
    @Description(shortDefinition="ValueSet details if this is coded", formalDefinition="Binds to a value set if this element is coded (code, Coding, CodeableConcept)." )
    protected ElementDefinitionBindingComponent binding;

    /**
     * Identifies a concept from an external specification that roughly corresponds to this element.
     */
    @Child(name = "mapping", type = {}, order = 25, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Map element to another set of definitions", formalDefinition="Identifies a concept from an external specification that roughly corresponds to this element." )
    protected List<ElementDefinitionMappingComponent> mapping;

    private static final long serialVersionUID = 2094512467L;

    public ElementDefinition() {
      super();
    }

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
     * @return {@link #slicing} (Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).)
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
     * @param value {@link #slicing} (Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).)
     */
    public ElementDefinition setSlicing(ElementDefinitionSlicingComponent value) { 
      this.slicing = value;
      return this;
    }

    /**
     * @return {@link #short_} (A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification).). This is the underlying object with id, value and extensions. The accessor "getShort" gives direct access to the value
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
     * @param value {@link #short_} (A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification).). This is the underlying object with id, value and extensions. The accessor "getShort" gives direct access to the value
     */
    public ElementDefinition setShortElement(StringType value) { 
      this.short_ = value;
      return this;
    }

    /**
     * @return A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification).
     */
    public String getShort() { 
      return this.short_ == null ? null : this.short_.getValue();
    }

    /**
     * @param value A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification).
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
     * @return {@link #formal} (The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.). This is the underlying object with id, value and extensions. The accessor "getFormal" gives direct access to the value
     */
    public StringType getFormalElement() { 
      if (this.formal == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.formal");
        else if (Configuration.doAutoCreate())
          this.formal = new StringType(); // bb
      return this.formal;
    }

    public boolean hasFormalElement() { 
      return this.formal != null && !this.formal.isEmpty();
    }

    public boolean hasFormal() { 
      return this.formal != null && !this.formal.isEmpty();
    }

    /**
     * @param value {@link #formal} (The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.). This is the underlying object with id, value and extensions. The accessor "getFormal" gives direct access to the value
     */
    public ElementDefinition setFormalElement(StringType value) { 
      this.formal = value;
      return this;
    }

    /**
     * @return The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.
     */
    public String getFormal() { 
      return this.formal == null ? null : this.formal.getValue();
    }

    /**
     * @param value The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.
     */
    public ElementDefinition setFormal(String value) { 
      if (Utilities.noString(value))
        this.formal = null;
      else {
        if (this.formal == null)
          this.formal = new StringType();
        this.formal.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #comments} (Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
     */
    public StringType getCommentsElement() { 
      if (this.comments == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.comments");
        else if (Configuration.doAutoCreate())
          this.comments = new StringType(); // bb
      return this.comments;
    }

    public boolean hasCommentsElement() { 
      return this.comments != null && !this.comments.isEmpty();
    }

    public boolean hasComments() { 
      return this.comments != null && !this.comments.isEmpty();
    }

    /**
     * @param value {@link #comments} (Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
     */
    public ElementDefinition setCommentsElement(StringType value) { 
      this.comments = value;
      return this;
    }

    /**
     * @return Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     */
    public String getComments() { 
      return this.comments == null ? null : this.comments.getValue();
    }

    /**
     * @param value Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     */
    public ElementDefinition setComments(String value) { 
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
     * @return {@link #requirements} (Explains why this element is needed and why it's been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.requirements");
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
     * @param value {@link #requirements} (Explains why this element is needed and why it's been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public ElementDefinition setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return Explains why this element is needed and why it's been constrained as it has.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value Explains why this element is needed and why it's been constrained as it has.
     */
    public ElementDefinition setRequirements(String value) { 
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
     * @return {@link #synonym} (Identifies additional names by which this element might also be known.)
     */
    public List<StringType> getSynonym() { 
      if (this.synonym == null)
        this.synonym = new ArrayList<StringType>();
      return this.synonym;
    }

    public boolean hasSynonym() { 
      if (this.synonym == null)
        return false;
      for (StringType item : this.synonym)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #synonym} (Identifies additional names by which this element might also be known.)
     */
    // syntactic sugar
    public StringType addSynonymElement() {//2 
      StringType t = new StringType();
      if (this.synonym == null)
        this.synonym = new ArrayList<StringType>();
      this.synonym.add(t);
      return t;
    }

    /**
     * @param value {@link #synonym} (Identifies additional names by which this element might also be known.)
     */
    public ElementDefinition addSynonym(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.synonym == null)
        this.synonym = new ArrayList<StringType>();
      this.synonym.add(t);
      return this;
    }

    /**
     * @param value {@link #synonym} (Identifies additional names by which this element might also be known.)
     */
    public boolean hasSynonym(String value) { 
      if (this.synonym == null)
        return false;
      for (StringType v : this.synonym)
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
      return this.min == null ? 0 : this.min.getValue();
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
     * @return {@link #defaultValue} (The value that should be used if there is no value stated in the instance.)
     */
    public org.hl7.fhir.instance.model.Type getDefaultValue() { 
      return this.defaultValue;
    }

    public boolean hasDefaultValue() { 
      return this.defaultValue != null && !this.defaultValue.isEmpty();
    }

    /**
     * @param value {@link #defaultValue} (The value that should be used if there is no value stated in the instance.)
     */
    public ElementDefinition setDefaultValue(org.hl7.fhir.instance.model.Type value) { 
      this.defaultValue = value;
      return this;
    }

    /**
     * @return {@link #meaningWhenMissing} (The Implicit meaning that is to be understood when this element is missing.). This is the underlying object with id, value and extensions. The accessor "getMeaningWhenMissing" gives direct access to the value
     */
    public StringType getMeaningWhenMissingElement() { 
      if (this.meaningWhenMissing == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.meaningWhenMissing");
        else if (Configuration.doAutoCreate())
          this.meaningWhenMissing = new StringType(); // bb
      return this.meaningWhenMissing;
    }

    public boolean hasMeaningWhenMissingElement() { 
      return this.meaningWhenMissing != null && !this.meaningWhenMissing.isEmpty();
    }

    public boolean hasMeaningWhenMissing() { 
      return this.meaningWhenMissing != null && !this.meaningWhenMissing.isEmpty();
    }

    /**
     * @param value {@link #meaningWhenMissing} (The Implicit meaning that is to be understood when this element is missing.). This is the underlying object with id, value and extensions. The accessor "getMeaningWhenMissing" gives direct access to the value
     */
    public ElementDefinition setMeaningWhenMissingElement(StringType value) { 
      this.meaningWhenMissing = value;
      return this;
    }

    /**
     * @return The Implicit meaning that is to be understood when this element is missing.
     */
    public String getMeaningWhenMissing() { 
      return this.meaningWhenMissing == null ? null : this.meaningWhenMissing.getValue();
    }

    /**
     * @param value The Implicit meaning that is to be understood when this element is missing.
     */
    public ElementDefinition setMeaningWhenMissing(String value) { 
      if (Utilities.noString(value))
        this.meaningWhenMissing = null;
      else {
        if (this.meaningWhenMissing == null)
          this.meaningWhenMissing = new StringType();
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
     * @return {@link #example} (An example value for this element.)
     */
    public org.hl7.fhir.instance.model.Type getExample() { 
      return this.example;
    }

    public boolean hasExample() { 
      return this.example != null && !this.example.isEmpty();
    }

    /**
     * @param value {@link #example} (An example value for this element.)
     */
    public ElementDefinition setExample(org.hl7.fhir.instance.model.Type value) { 
      this.example = value;
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
      return this.maxLength == null ? 0 : this.maxLength.getValue();
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

    /**
     * @return {@link #mustSupport} (If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported.). This is the underlying object with id, value and extensions. The accessor "getMustSupport" gives direct access to the value
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
     * @param value {@link #mustSupport} (If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported.). This is the underlying object with id, value and extensions. The accessor "getMustSupport" gives direct access to the value
     */
    public ElementDefinition setMustSupportElement(BooleanType value) { 
      this.mustSupport = value;
      return this;
    }

    /**
     * @return If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported.
     */
    public boolean getMustSupport() { 
      return this.mustSupport == null ? false : this.mustSupport.getValue();
    }

    /**
     * @param value If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported.
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
      return this.isModifier == null ? false : this.isModifier.getValue();
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
      return this.isSummary == null ? false : this.isSummary.getValue();
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("path", "string", "The path identifies the element and is expressed as a '.'-separated list of ancestor elements, beginning with the name of the resource or extension.", 0, java.lang.Integer.MAX_VALUE, path));
        childrenList.add(new Property("representation", "code", "Codes that define how this element is represented in instances, when the deviation varies from the normal case.", 0, java.lang.Integer.MAX_VALUE, representation));
        childrenList.add(new Property("name", "string", "The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("slicing", "", "Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).", 0, java.lang.Integer.MAX_VALUE, slicing));
        childrenList.add(new Property("short", "string", "A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification).", 0, java.lang.Integer.MAX_VALUE, short_));
        childrenList.add(new Property("formal", "string", "The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.", 0, java.lang.Integer.MAX_VALUE, formal));
        childrenList.add(new Property("comments", "string", "Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.", 0, java.lang.Integer.MAX_VALUE, comments));
        childrenList.add(new Property("requirements", "string", "Explains why this element is needed and why it's been constrained as it has.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("synonym", "string", "Identifies additional names by which this element might also be known.", 0, java.lang.Integer.MAX_VALUE, synonym));
        childrenList.add(new Property("min", "integer", "The minimum number of times this element SHALL appear in the instance.", 0, java.lang.Integer.MAX_VALUE, min));
        childrenList.add(new Property("max", "string", "The maximum number of times this element is permitted to appear in the instance.", 0, java.lang.Integer.MAX_VALUE, max));
        childrenList.add(new Property("type", "", "The data type or resource that the value of this element is permitted to be.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("nameReference", "string", "Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element.", 0, java.lang.Integer.MAX_VALUE, nameReference));
        childrenList.add(new Property("defaultValue[x]", "*", "The value that should be used if there is no value stated in the instance.", 0, java.lang.Integer.MAX_VALUE, defaultValue));
        childrenList.add(new Property("meaningWhenMissing", "string", "The Implicit meaning that is to be understood when this element is missing.", 0, java.lang.Integer.MAX_VALUE, meaningWhenMissing));
        childrenList.add(new Property("fixed[x]", "*", "Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing.", 0, java.lang.Integer.MAX_VALUE, fixed));
        childrenList.add(new Property("pattern[x]", "*", "Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.).", 0, java.lang.Integer.MAX_VALUE, pattern));
        childrenList.add(new Property("example[x]", "*", "An example value for this element.", 0, java.lang.Integer.MAX_VALUE, example));
        childrenList.add(new Property("maxLength", "integer", "Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.", 0, java.lang.Integer.MAX_VALUE, maxLength));
        childrenList.add(new Property("condition", "id", "A reference to an invariant that may make additional statements about the cardinality or value in the instance.", 0, java.lang.Integer.MAX_VALUE, condition));
        childrenList.add(new Property("constraint", "", "Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance.", 0, java.lang.Integer.MAX_VALUE, constraint));
        childrenList.add(new Property("mustSupport", "boolean", "If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported.", 0, java.lang.Integer.MAX_VALUE, mustSupport));
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
        dst.slicing = slicing == null ? null : slicing.copy();
        dst.short_ = short_ == null ? null : short_.copy();
        dst.formal = formal == null ? null : formal.copy();
        dst.comments = comments == null ? null : comments.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        if (synonym != null) {
          dst.synonym = new ArrayList<StringType>();
          for (StringType i : synonym)
            dst.synonym.add(i.copy());
        };
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
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
           && compareDeep(slicing, o.slicing, true) && compareDeep(short_, o.short_, true) && compareDeep(formal, o.formal, true)
           && compareDeep(comments, o.comments, true) && compareDeep(requirements, o.requirements, true) && compareDeep(synonym, o.synonym, true)
           && compareDeep(min, o.min, true) && compareDeep(max, o.max, true) && compareDeep(type, o.type, true)
           && compareDeep(nameReference, o.nameReference, true) && compareDeep(defaultValue, o.defaultValue, true)
           && compareDeep(meaningWhenMissing, o.meaningWhenMissing, true) && compareDeep(fixed, o.fixed, true)
           && compareDeep(pattern, o.pattern, true) && compareDeep(example, o.example, true) && compareDeep(maxLength, o.maxLength, true)
           && compareDeep(condition, o.condition, true) && compareDeep(constraint, o.constraint, true) && compareDeep(mustSupport, o.mustSupport, true)
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
           && compareValues(short_, o.short_, true) && compareValues(formal, o.formal, true) && compareValues(comments, o.comments, true)
           && compareValues(requirements, o.requirements, true) && compareValues(synonym, o.synonym, true) && compareValues(min, o.min, true)
           && compareValues(max, o.max, true) && compareValues(nameReference, o.nameReference, true) && compareValues(meaningWhenMissing, o.meaningWhenMissing, true)
           && compareValues(maxLength, o.maxLength, true) && compareValues(condition, o.condition, true) && compareValues(mustSupport, o.mustSupport, true)
           && compareValues(isModifier, o.isModifier, true) && compareValues(isSummary, o.isSummary, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (path == null || path.isEmpty()) && (representation == null || representation.isEmpty())
           && (name == null || name.isEmpty()) && (slicing == null || slicing.isEmpty()) && (short_ == null || short_.isEmpty())
           && (formal == null || formal.isEmpty()) && (comments == null || comments.isEmpty()) && (requirements == null || requirements.isEmpty())
           && (synonym == null || synonym.isEmpty()) && (min == null || min.isEmpty()) && (max == null || max.isEmpty())
           && (type == null || type.isEmpty()) && (nameReference == null || nameReference.isEmpty())
           && (defaultValue == null || defaultValue.isEmpty()) && (meaningWhenMissing == null || meaningWhenMissing.isEmpty())
           && (fixed == null || fixed.isEmpty()) && (pattern == null || pattern.isEmpty()) && (example == null || example.isEmpty())
           && (maxLength == null || maxLength.isEmpty()) && (condition == null || condition.isEmpty())
           && (constraint == null || constraint.isEmpty()) && (mustSupport == null || mustSupport.isEmpty())
           && (isModifier == null || isModifier.isEmpty()) && (isSummary == null || isSummary.isEmpty())
           && (binding == null || binding.isEmpty()) && (mapping == null || mapping.isEmpty());
      }


}

