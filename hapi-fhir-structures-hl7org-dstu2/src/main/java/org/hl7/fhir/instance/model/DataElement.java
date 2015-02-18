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
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * The formal description of a single piece of information that can be gathered and reported.
 */
@ResourceDef(name="DataElement", profile="http://hl7.org/fhir/Profile/DataElement")
public class DataElement extends DomainResource {

    public enum ResourceObservationDefStatus {
        /**
         * This data element is still under development.
         */
        DRAFT, 
        /**
         * This data element is ready for normal use.
         */
        ACTIVE, 
        /**
         * This data element has been deprecated, withdrawn or superseded and should no longer be used.
         */
        RETIRED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceObservationDefStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("retired".equals(codeString))
          return RETIRED;
        throw new Exception("Unknown ResourceObservationDefStatus code '"+codeString+"'");
        }
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
            case DRAFT: return "This data element is still under development.";
            case ACTIVE: return "This data element is ready for normal use.";
            case RETIRED: return "This data element has been deprecated, withdrawn or superseded and should no longer be used.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case RETIRED: return "retired";
            default: return "?";
          }
        }
    }

  public static class ResourceObservationDefStatusEnumFactory implements EnumFactory<ResourceObservationDefStatus> {
    public ResourceObservationDefStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ResourceObservationDefStatus.DRAFT;
        if ("active".equals(codeString))
          return ResourceObservationDefStatus.ACTIVE;
        if ("retired".equals(codeString))
          return ResourceObservationDefStatus.RETIRED;
        throw new IllegalArgumentException("Unknown ResourceObservationDefStatus code '"+codeString+"'");
        }
    public String toCode(ResourceObservationDefStatus code) {
      if (code == ResourceObservationDefStatus.DRAFT)
        return "draft";
      if (code == ResourceObservationDefStatus.ACTIVE)
        return "active";
      if (code == ResourceObservationDefStatus.RETIRED)
        return "retired";
      return "?";
      }
    }

    public enum DataelementGranularity {
        /**
         * The data element is sufficiently well-constrained that multiple pieces of data captured according to the constraints of the data element will be comparable (though in some cases, a degree of automated conversion/normalization may be required).
         */
        COMPARABLE, 
        /**
         * The data element is fully specified down to a single value set, single unit of measure, single data type, etc.  Multiple pieces of data associated with this data element are fully compareable.
         */
        FULLYSPECIFIED, 
        /**
         * The data element allows multiple units of measure having equivalent meaning.  E.g. "cc" (cubic centimeter) and "mL".
         */
        EQUIVALENT, 
        /**
         * The data element allows multiple units of measure that are convertable between each other (e.g. Inches and centimeters) and/or allows data to be captured in multiple value sets for which a known mapping exists allowing conversion of meaning.
         */
        CONVERTABLE, 
        /**
         * A convertable data element where unit conversions are different only by a power of 10.  E.g. g, mg, kg.
         */
        SCALEABLE, 
        /**
         * The data element is unconstrained in units, choice of data types and/or choice of vocabulary such that automated comparison of data captured using the data element is not possible.
         */
        FLEXIBLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DataelementGranularity fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("comparable".equals(codeString))
          return COMPARABLE;
        if ("fully-specified".equals(codeString))
          return FULLYSPECIFIED;
        if ("equivalent".equals(codeString))
          return EQUIVALENT;
        if ("convertable".equals(codeString))
          return CONVERTABLE;
        if ("scaleable".equals(codeString))
          return SCALEABLE;
        if ("flexible".equals(codeString))
          return FLEXIBLE;
        throw new Exception("Unknown DataelementGranularity code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPARABLE: return "comparable";
            case FULLYSPECIFIED: return "fully-specified";
            case EQUIVALENT: return "equivalent";
            case CONVERTABLE: return "convertable";
            case SCALEABLE: return "scaleable";
            case FLEXIBLE: return "flexible";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMPARABLE: return "";
            case FULLYSPECIFIED: return "";
            case EQUIVALENT: return "";
            case CONVERTABLE: return "";
            case SCALEABLE: return "";
            case FLEXIBLE: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMPARABLE: return "The data element is sufficiently well-constrained that multiple pieces of data captured according to the constraints of the data element will be comparable (though in some cases, a degree of automated conversion/normalization may be required).";
            case FULLYSPECIFIED: return "The data element is fully specified down to a single value set, single unit of measure, single data type, etc.  Multiple pieces of data associated with this data element are fully compareable.";
            case EQUIVALENT: return "The data element allows multiple units of measure having equivalent meaning.  E.g. 'cc' (cubic centimeter) and 'mL'.";
            case CONVERTABLE: return "The data element allows multiple units of measure that are convertable between each other (e.g. Inches and centimeters) and/or allows data to be captured in multiple value sets for which a known mapping exists allowing conversion of meaning.";
            case SCALEABLE: return "A convertable data element where unit conversions are different only by a power of 10.  E.g. g, mg, kg.";
            case FLEXIBLE: return "The data element is unconstrained in units, choice of data types and/or choice of vocabulary such that automated comparison of data captured using the data element is not possible.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPARABLE: return "comparable";
            case FULLYSPECIFIED: return "fully-specified";
            case EQUIVALENT: return "equivalent";
            case CONVERTABLE: return "convertable";
            case SCALEABLE: return "scaleable";
            case FLEXIBLE: return "flexible";
            default: return "?";
          }
        }
    }

  public static class DataelementGranularityEnumFactory implements EnumFactory<DataelementGranularity> {
    public DataelementGranularity fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("comparable".equals(codeString))
          return DataelementGranularity.COMPARABLE;
        if ("fully-specified".equals(codeString))
          return DataelementGranularity.FULLYSPECIFIED;
        if ("equivalent".equals(codeString))
          return DataelementGranularity.EQUIVALENT;
        if ("convertable".equals(codeString))
          return DataelementGranularity.CONVERTABLE;
        if ("scaleable".equals(codeString))
          return DataelementGranularity.SCALEABLE;
        if ("flexible".equals(codeString))
          return DataelementGranularity.FLEXIBLE;
        throw new IllegalArgumentException("Unknown DataelementGranularity code '"+codeString+"'");
        }
    public String toCode(DataelementGranularity code) {
      if (code == DataelementGranularity.COMPARABLE)
        return "comparable";
      if (code == DataelementGranularity.FULLYSPECIFIED)
        return "fully-specified";
      if (code == DataelementGranularity.EQUIVALENT)
        return "equivalent";
      if (code == DataelementGranularity.CONVERTABLE)
        return "convertable";
      if (code == DataelementGranularity.SCALEABLE)
        return "scaleable";
      if (code == DataelementGranularity.FLEXIBLE)
        return "flexible";
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

    @Block()
    public static class DataElementBindingComponent extends BackboneElement {
        /**
         * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone.
         */
        @Child(name="isExtensible", type={BooleanType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Can additional codes be used?", formalDefinition="If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone." )
        protected BooleanType isExtensible;

        /**
         * Indicates the degree of conformance expectations associated with this binding.
         */
        @Child(name="conformance", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="required | preferred | example", formalDefinition="Indicates the degree of conformance expectations associated with this binding." )
        protected Enumeration<BindingConformance> conformance;

        /**
         * Describes the intended use of this particular set of codes.
         */
        @Child(name="description", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Human explanation of the value set", formalDefinition="Describes the intended use of this particular set of codes." )
        protected StringType description;

        /**
         * Points to the value set that identifies the set of codes to be used.
         */
        @Child(name="valueSet", type={ValueSet.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Source of value set", formalDefinition="Points to the value set that identifies the set of codes to be used." )
        protected Reference valueSet;

        /**
         * The actual object that is the target of the reference (Points to the value set that identifies the set of codes to be used.)
         */
        protected ValueSet valueSetTarget;

        private static final long serialVersionUID = -1297440999L;

      public DataElementBindingComponent() {
        super();
      }

      public DataElementBindingComponent(BooleanType isExtensible) {
        super();
        this.isExtensible = isExtensible;
      }

        /**
         * @return {@link #isExtensible} (If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone.). This is the underlying object with id, value and extensions. The accessor "getIsExtensible" gives direct access to the value
         */
        public BooleanType getIsExtensibleElement() { 
          if (this.isExtensible == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementBindingComponent.isExtensible");
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
        public DataElementBindingComponent setIsExtensibleElement(BooleanType value) { 
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
        public DataElementBindingComponent setIsExtensible(boolean value) { 
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
              throw new Error("Attempt to auto-create DataElementBindingComponent.conformance");
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
        public DataElementBindingComponent setConformanceElement(Enumeration<BindingConformance> value) { 
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
        public DataElementBindingComponent setConformance(BindingConformance value) { 
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
              throw new Error("Attempt to auto-create DataElementBindingComponent.description");
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
        public DataElementBindingComponent setDescriptionElement(StringType value) { 
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
        public DataElementBindingComponent setDescription(String value) { 
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
         * @return {@link #valueSet} (Points to the value set that identifies the set of codes to be used.)
         */
        public Reference getValueSet() { 
          if (this.valueSet == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementBindingComponent.valueSet");
            else if (Configuration.doAutoCreate())
              this.valueSet = new Reference(); // cc
          return this.valueSet;
        }

        public boolean hasValueSet() { 
          return this.valueSet != null && !this.valueSet.isEmpty();
        }

        /**
         * @param value {@link #valueSet} (Points to the value set that identifies the set of codes to be used.)
         */
        public DataElementBindingComponent setValueSet(Reference value) { 
          this.valueSet = value;
          return this;
        }

        /**
         * @return {@link #valueSet} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Points to the value set that identifies the set of codes to be used.)
         */
        public ValueSet getValueSetTarget() { 
          if (this.valueSetTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementBindingComponent.valueSet");
            else if (Configuration.doAutoCreate())
              this.valueSetTarget = new ValueSet(); // aa
          return this.valueSetTarget;
        }

        /**
         * @param value {@link #valueSet} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Points to the value set that identifies the set of codes to be used.)
         */
        public DataElementBindingComponent setValueSetTarget(ValueSet value) { 
          this.valueSetTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("isExtensible", "boolean", "If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone.", 0, java.lang.Integer.MAX_VALUE, isExtensible));
          childrenList.add(new Property("conformance", "code", "Indicates the degree of conformance expectations associated with this binding.", 0, java.lang.Integer.MAX_VALUE, conformance));
          childrenList.add(new Property("description", "string", "Describes the intended use of this particular set of codes.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("valueSet", "Reference(ValueSet)", "Points to the value set that identifies the set of codes to be used.", 0, java.lang.Integer.MAX_VALUE, valueSet));
        }

      public DataElementBindingComponent copy() {
        DataElementBindingComponent dst = new DataElementBindingComponent();
        copyValues(dst);
        dst.isExtensible = isExtensible == null ? null : isExtensible.copy();
        dst.conformance = conformance == null ? null : conformance.copy();
        dst.description = description == null ? null : description.copy();
        dst.valueSet = valueSet == null ? null : valueSet.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DataElementBindingComponent))
          return false;
        DataElementBindingComponent o = (DataElementBindingComponent) other;
        return compareDeep(isExtensible, o.isExtensible, true) && compareDeep(conformance, o.conformance, true)
           && compareDeep(description, o.description, true) && compareDeep(valueSet, o.valueSet, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DataElementBindingComponent))
          return false;
        DataElementBindingComponent o = (DataElementBindingComponent) other;
        return compareValues(isExtensible, o.isExtensible, true) && compareValues(conformance, o.conformance, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (isExtensible == null || isExtensible.isEmpty()) && (conformance == null || conformance.isEmpty())
           && (description == null || description.isEmpty()) && (valueSet == null || valueSet.isEmpty())
          ;
      }

  }

    @Block()
    public static class DataElementMappingComponent extends BackboneElement {
        /**
         * A URI that identifies the specification that this mapping is expressed to.
         */
        @Child(name="uri", type={UriType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Identifies what this mapping refers to", formalDefinition="A URI that identifies the specification that this mapping is expressed to." )
        protected UriType uri;

        /**
         * If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element.
         */
        @Child(name="definitional", type={BooleanType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="True if mapping defines element", formalDefinition="If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element." )
        protected BooleanType definitional;

        /**
         * A name for the specification that is being mapped to.
         */
        @Child(name="name", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Names what this mapping refers to", formalDefinition="A name for the specification that is being mapped to." )
        protected StringType name;

        /**
         * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         */
        @Child(name="comments", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Versions, Issues, Scope limitations etc", formalDefinition="Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage." )
        protected StringType comments;

        /**
         * Expresses what part of the target specification corresponds to this element.
         */
        @Child(name="map", type={StringType.class}, order=5, min=1, max=1)
        @Description(shortDefinition="Details of the mapping", formalDefinition="Expresses what part of the target specification corresponds to this element." )
        protected StringType map;

        private static final long serialVersionUID = 797049346L;

      public DataElementMappingComponent() {
        super();
      }

      public DataElementMappingComponent(StringType map) {
        super();
        this.map = map;
      }

        /**
         * @return {@link #uri} (A URI that identifies the specification that this mapping is expressed to.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public UriType getUriElement() { 
          if (this.uri == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementMappingComponent.uri");
            else if (Configuration.doAutoCreate())
              this.uri = new UriType(); // bb
          return this.uri;
        }

        public boolean hasUriElement() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        public boolean hasUri() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        /**
         * @param value {@link #uri} (A URI that identifies the specification that this mapping is expressed to.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public DataElementMappingComponent setUriElement(UriType value) { 
          this.uri = value;
          return this;
        }

        /**
         * @return A URI that identifies the specification that this mapping is expressed to.
         */
        public String getUri() { 
          return this.uri == null ? null : this.uri.getValue();
        }

        /**
         * @param value A URI that identifies the specification that this mapping is expressed to.
         */
        public DataElementMappingComponent setUri(String value) { 
          if (Utilities.noString(value))
            this.uri = null;
          else {
            if (this.uri == null)
              this.uri = new UriType();
            this.uri.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #definitional} (If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element.). This is the underlying object with id, value and extensions. The accessor "getDefinitional" gives direct access to the value
         */
        public BooleanType getDefinitionalElement() { 
          if (this.definitional == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementMappingComponent.definitional");
            else if (Configuration.doAutoCreate())
              this.definitional = new BooleanType(); // bb
          return this.definitional;
        }

        public boolean hasDefinitionalElement() { 
          return this.definitional != null && !this.definitional.isEmpty();
        }

        public boolean hasDefinitional() { 
          return this.definitional != null && !this.definitional.isEmpty();
        }

        /**
         * @param value {@link #definitional} (If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element.). This is the underlying object with id, value and extensions. The accessor "getDefinitional" gives direct access to the value
         */
        public DataElementMappingComponent setDefinitionalElement(BooleanType value) { 
          this.definitional = value;
          return this;
        }

        /**
         * @return If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element.
         */
        public boolean getDefinitional() { 
          return this.definitional == null ? false : this.definitional.getValue();
        }

        /**
         * @param value If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element.
         */
        public DataElementMappingComponent setDefinitional(boolean value) { 
            if (this.definitional == null)
              this.definitional = new BooleanType();
            this.definitional.setValue(value);
          return this;
        }

        /**
         * @return {@link #name} (A name for the specification that is being mapped to.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementMappingComponent.name");
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
         * @param value {@link #name} (A name for the specification that is being mapped to.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public DataElementMappingComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A name for the specification that is being mapped to.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A name for the specification that is being mapped to.
         */
        public DataElementMappingComponent setName(String value) { 
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
         * @return {@link #comments} (Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
         */
        public StringType getCommentsElement() { 
          if (this.comments == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementMappingComponent.comments");
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
         * @param value {@link #comments} (Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
         */
        public DataElementMappingComponent setCommentsElement(StringType value) { 
          this.comments = value;
          return this;
        }

        /**
         * @return Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         */
        public String getComments() { 
          return this.comments == null ? null : this.comments.getValue();
        }

        /**
         * @param value Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         */
        public DataElementMappingComponent setComments(String value) { 
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
         * @return {@link #map} (Expresses what part of the target specification corresponds to this element.). This is the underlying object with id, value and extensions. The accessor "getMap" gives direct access to the value
         */
        public StringType getMapElement() { 
          if (this.map == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementMappingComponent.map");
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
        public DataElementMappingComponent setMapElement(StringType value) { 
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
        public DataElementMappingComponent setMap(String value) { 
            if (this.map == null)
              this.map = new StringType();
            this.map.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("uri", "uri", "A URI that identifies the specification that this mapping is expressed to.", 0, java.lang.Integer.MAX_VALUE, uri));
          childrenList.add(new Property("definitional", "boolean", "If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element.", 0, java.lang.Integer.MAX_VALUE, definitional));
          childrenList.add(new Property("name", "string", "A name for the specification that is being mapped to.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("comments", "string", "Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.", 0, java.lang.Integer.MAX_VALUE, comments));
          childrenList.add(new Property("map", "string", "Expresses what part of the target specification corresponds to this element.", 0, java.lang.Integer.MAX_VALUE, map));
        }

      public DataElementMappingComponent copy() {
        DataElementMappingComponent dst = new DataElementMappingComponent();
        copyValues(dst);
        dst.uri = uri == null ? null : uri.copy();
        dst.definitional = definitional == null ? null : definitional.copy();
        dst.name = name == null ? null : name.copy();
        dst.comments = comments == null ? null : comments.copy();
        dst.map = map == null ? null : map.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DataElementMappingComponent))
          return false;
        DataElementMappingComponent o = (DataElementMappingComponent) other;
        return compareDeep(uri, o.uri, true) && compareDeep(definitional, o.definitional, true) && compareDeep(name, o.name, true)
           && compareDeep(comments, o.comments, true) && compareDeep(map, o.map, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DataElementMappingComponent))
          return false;
        DataElementMappingComponent o = (DataElementMappingComponent) other;
        return compareValues(uri, o.uri, true) && compareValues(definitional, o.definitional, true) && compareValues(name, o.name, true)
           && compareValues(comments, o.comments, true) && compareValues(map, o.map, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (uri == null || uri.isEmpty()) && (definitional == null || definitional.isEmpty())
           && (name == null || name.isEmpty()) && (comments == null || comments.isEmpty()) && (map == null || map.isEmpty())
          ;
      }

  }

    /**
     * The identifier that is used to identify this data element when it is referenced in a Profile, Questionnaire or an instance.
     */
    @Child(name="identifier", type={Identifier.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Logical id to reference this data element", formalDefinition="The identifier that is used to identify this data element when it is referenced in a Profile, Questionnaire or an instance." )
    protected Identifier identifier;

    /**
     * The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.
     */
    @Child(name="version", type={StringType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Logical id for this version of the data element", formalDefinition="The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually." )
    protected StringType version;

    /**
     * Details of the individual or organization who accepts responsibility for publishing the data element.
     */
    @Child(name="publisher", type={StringType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="Details of the individual or organization who accepts responsibility for publishing the data element." )
    protected StringType publisher;

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     */
    @Child(name="telecom", type={ContactPoint.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contact information of the publisher", formalDefinition="Contact details to assist a user in finding and communicating with the publisher." )
    protected List<ContactPoint> telecom;

    /**
     * The status of the data element.
     */
    @Child(name="status", type={CodeType.class}, order=4, min=1, max=1)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the data element." )
    protected Enumeration<ResourceObservationDefStatus> status;

    /**
     * The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired).
     */
    @Child(name="date", type={DateTimeType.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Date for this version of the data element", formalDefinition="The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired)." )
    protected DateTimeType date;

    /**
     * The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.
     */
    @Child(name="name", type={StringType.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Descriptive label for this element definition", formalDefinition="The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used." )
    protected StringType name;

    /**
     * A set of terms from external terminologies that may be used to assist with indexing and searching of data element definitions.
     */
    @Child(name="category", type={CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Assist with indexing and finding", formalDefinition="A set of terms from external terminologies that may be used to assist with indexing and searching of data element definitions." )
    protected List<CodeableConcept> category;

    /**
     * Identifies how precise the data element is in its definition.
     */
    @Child(name="granularity", type={CodeType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="comparable | fully-specified | equivalent | convertable | scaleable | flexible", formalDefinition="Identifies how precise the data element is in its definition." )
    protected Enumeration<DataelementGranularity> granularity;

    /**
     * A code that provides the meaning for a data element according to a particular terminology.
     */
    @Child(name="code", type={Coding.class}, order=9, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Identifying concept", formalDefinition="A code that provides the meaning for a data element according to a particular terminology." )
    protected List<Coding> code;

    /**
     * The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey).
     */
    @Child(name="question", type={StringType.class}, order=10, min=0, max=1)
    @Description(shortDefinition="Prompt for element phrased as question", formalDefinition="The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey)." )
    protected StringType question;

    /**
     * The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     */
    @Child(name="label", type={StringType.class}, order=11, min=0, max=1)
    @Description(shortDefinition="Name for element to display with or prompt for element", formalDefinition="The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form." )
    protected StringType label;

    /**
     * Provides a complete explanation of the meaning of the data element for human readability.
     */
    @Child(name="definition", type={StringType.class}, order=12, min=0, max=1)
    @Description(shortDefinition="Definition/description as narrative text", formalDefinition="Provides a complete explanation of the meaning of the data element for human readability." )
    protected StringType definition;

    /**
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     */
    @Child(name="comments", type={StringType.class}, order=13, min=0, max=1)
    @Description(shortDefinition="Comments about the use of this element", formalDefinition="Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc." )
    protected StringType comments;

    /**
     * Explains why this element is needed and why it's been constrained as it has.
     */
    @Child(name="requirements", type={StringType.class}, order=14, min=0, max=1)
    @Description(shortDefinition="Why is this needed?", formalDefinition="Explains why this element is needed and why it's been constrained as it has." )
    protected StringType requirements;

    /**
     * Identifies additional names by which this element might also be known.
     */
    @Child(name="synonym", type={StringType.class}, order=15, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Other names", formalDefinition="Identifies additional names by which this element might also be known." )
    protected List<StringType> synonym;

    /**
     * The FHIR data type that is the type for data that corresponds to this data element.
     */
    @Child(name="type", type={CodeType.class}, order=16, min=0, max=1)
    @Description(shortDefinition="Name of Data type", formalDefinition="The FHIR data type that is the type for data that corresponds to this data element." )
    protected CodeType type;

    /**
     * A sample value for this element demonstrating the type of information that would typically be captured.
     */
    @Child(name="example", type={}, order=17, min=0, max=1)
    @Description(shortDefinition="Example value: [as defined for type]", formalDefinition="A sample value for this element demonstrating the type of information that would typically be captured." )
    protected org.hl7.fhir.instance.model.Type example;

    /**
     * Indicates the shortest length that SHALL be supported by conformant instances without truncation.
     */
    @Child(name="maxLength", type={IntegerType.class}, order=18, min=0, max=1)
    @Description(shortDefinition="Length for strings", formalDefinition="Indicates the shortest length that SHALL be supported by conformant instances without truncation." )
    protected IntegerType maxLength;

    /**
     * Identifies the units of measure in which the data element should be captured or expressed.
     */
    @Child(name="units", type={CodeableConcept.class, ValueSet.class}, order=19, min=0, max=1)
    @Description(shortDefinition="Units to use for measured value", formalDefinition="Identifies the units of measure in which the data element should be captured or expressed." )
    protected Type units;

    /**
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept).
     */
    @Child(name="binding", type={}, order=20, min=0, max=1)
    @Description(shortDefinition="ValueSet details if this is coded", formalDefinition="Binds to a value set if this element is coded (code, Coding, CodeableConcept)." )
    protected DataElementBindingComponent binding;

    /**
     * Identifies a concept from an external specification that roughly corresponds to this element.
     */
    @Child(name="mapping", type={}, order=21, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Map element to another set of definitions", formalDefinition="Identifies a concept from an external specification that roughly corresponds to this element." )
    protected List<DataElementMappingComponent> mapping;

    private static final long serialVersionUID = 1810868955L;

    public DataElement() {
      super();
    }

    public DataElement(Enumeration<ResourceObservationDefStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (The identifier that is used to identify this data element when it is referenced in a Profile, Questionnaire or an instance.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (The identifier that is used to identify this data element when it is referenced in a Profile, Questionnaire or an instance.)
     */
    public DataElement setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public DataElement setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.
     */
    public DataElement setVersion(String value) { 
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
     * @return {@link #publisher} (Details of the individual or organization who accepts responsibility for publishing the data element.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.publisher");
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
     * @param value {@link #publisher} (Details of the individual or organization who accepts responsibility for publishing the data element.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public DataElement setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return Details of the individual or organization who accepts responsibility for publishing the data element.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value Details of the individual or organization who accepts responsibility for publishing the data element.
     */
    public DataElement setPublisher(String value) { 
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
     * @return {@link #telecom} (Contact details to assist a user in finding and communicating with the publisher.)
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
     * @return {@link #telecom} (Contact details to assist a user in finding and communicating with the publisher.)
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
     * @return {@link #status} (The status of the data element.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ResourceObservationDefStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ResourceObservationDefStatus>(new ResourceObservationDefStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the data element.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public DataElement setStatusElement(Enumeration<ResourceObservationDefStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the data element.
     */
    public ResourceObservationDefStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the data element.
     */
    public DataElement setStatus(ResourceObservationDefStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ResourceObservationDefStatus>(new ResourceObservationDefStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired).). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.date");
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
     * @param value {@link #date} (The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired).). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DataElement setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired).
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired).
     */
    public DataElement setDate(Date value) { 
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
     * @return {@link #name} (The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.name");
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
     * @param value {@link #name} (The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public DataElement setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.
     */
    public DataElement setName(String value) { 
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
     * @return {@link #category} (A set of terms from external terminologies that may be used to assist with indexing and searching of data element definitions.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #category} (A set of terms from external terminologies that may be used to assist with indexing and searching of data element definitions.)
     */
    // syntactic sugar
    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    /**
     * @return {@link #granularity} (Identifies how precise the data element is in its definition.). This is the underlying object with id, value and extensions. The accessor "getGranularity" gives direct access to the value
     */
    public Enumeration<DataelementGranularity> getGranularityElement() { 
      if (this.granularity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.granularity");
        else if (Configuration.doAutoCreate())
          this.granularity = new Enumeration<DataelementGranularity>(new DataelementGranularityEnumFactory()); // bb
      return this.granularity;
    }

    public boolean hasGranularityElement() { 
      return this.granularity != null && !this.granularity.isEmpty();
    }

    public boolean hasGranularity() { 
      return this.granularity != null && !this.granularity.isEmpty();
    }

    /**
     * @param value {@link #granularity} (Identifies how precise the data element is in its definition.). This is the underlying object with id, value and extensions. The accessor "getGranularity" gives direct access to the value
     */
    public DataElement setGranularityElement(Enumeration<DataelementGranularity> value) { 
      this.granularity = value;
      return this;
    }

    /**
     * @return Identifies how precise the data element is in its definition.
     */
    public DataelementGranularity getGranularity() { 
      return this.granularity == null ? null : this.granularity.getValue();
    }

    /**
     * @param value Identifies how precise the data element is in its definition.
     */
    public DataElement setGranularity(DataelementGranularity value) { 
      if (value == null)
        this.granularity = null;
      else {
        if (this.granularity == null)
          this.granularity = new Enumeration<DataelementGranularity>(new DataelementGranularityEnumFactory());
        this.granularity.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #code} (A code that provides the meaning for a data element according to a particular terminology.)
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
     * @return {@link #code} (A code that provides the meaning for a data element according to a particular terminology.)
     */
    // syntactic sugar
    public Coding addCode() { //3
      Coding t = new Coding();
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      this.code.add(t);
      return t;
    }

    /**
     * @return {@link #question} (The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey).). This is the underlying object with id, value and extensions. The accessor "getQuestion" gives direct access to the value
     */
    public StringType getQuestionElement() { 
      if (this.question == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.question");
        else if (Configuration.doAutoCreate())
          this.question = new StringType(); // bb
      return this.question;
    }

    public boolean hasQuestionElement() { 
      return this.question != null && !this.question.isEmpty();
    }

    public boolean hasQuestion() { 
      return this.question != null && !this.question.isEmpty();
    }

    /**
     * @param value {@link #question} (The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey).). This is the underlying object with id, value and extensions. The accessor "getQuestion" gives direct access to the value
     */
    public DataElement setQuestionElement(StringType value) { 
      this.question = value;
      return this;
    }

    /**
     * @return The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey).
     */
    public String getQuestion() { 
      return this.question == null ? null : this.question.getValue();
    }

    /**
     * @param value The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey).
     */
    public DataElement setQuestion(String value) { 
      if (Utilities.noString(value))
        this.question = null;
      else {
        if (this.question == null)
          this.question = new StringType();
        this.question.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #label} (The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
     */
    public StringType getLabelElement() { 
      if (this.label == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.label");
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
    public DataElement setLabelElement(StringType value) { 
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
    public DataElement setLabel(String value) { 
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
     * @return {@link #definition} (Provides a complete explanation of the meaning of the data element for human readability.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public StringType getDefinitionElement() { 
      if (this.definition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.definition");
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
     * @param value {@link #definition} (Provides a complete explanation of the meaning of the data element for human readability.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public DataElement setDefinitionElement(StringType value) { 
      this.definition = value;
      return this;
    }

    /**
     * @return Provides a complete explanation of the meaning of the data element for human readability.
     */
    public String getDefinition() { 
      return this.definition == null ? null : this.definition.getValue();
    }

    /**
     * @param value Provides a complete explanation of the meaning of the data element for human readability.
     */
    public DataElement setDefinition(String value) { 
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
     * @return {@link #comments} (Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
     */
    public StringType getCommentsElement() { 
      if (this.comments == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.comments");
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
    public DataElement setCommentsElement(StringType value) { 
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
    public DataElement setComments(String value) { 
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
          throw new Error("Attempt to auto-create DataElement.requirements");
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
    public DataElement setRequirementsElement(StringType value) { 
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
    public DataElement setRequirements(String value) { 
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
    public DataElement addSynonym(String value) { //1
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
     * @return {@link #type} (The FHIR data type that is the type for data that corresponds to this data element.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public CodeType getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeType(); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The FHIR data type that is the type for data that corresponds to this data element.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public DataElement setTypeElement(CodeType value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The FHIR data type that is the type for data that corresponds to this data element.
     */
    public String getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The FHIR data type that is the type for data that corresponds to this data element.
     */
    public DataElement setType(String value) { 
      if (Utilities.noString(value))
        this.type = null;
      else {
        if (this.type == null)
          this.type = new CodeType();
        this.type.setValue(value);
      }
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
    public DataElement setExample(org.hl7.fhir.instance.model.Type value) { 
      this.example = value;
      return this;
    }

    /**
     * @return {@link #maxLength} (Indicates the shortest length that SHALL be supported by conformant instances without truncation.). This is the underlying object with id, value and extensions. The accessor "getMaxLength" gives direct access to the value
     */
    public IntegerType getMaxLengthElement() { 
      if (this.maxLength == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.maxLength");
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
     * @param value {@link #maxLength} (Indicates the shortest length that SHALL be supported by conformant instances without truncation.). This is the underlying object with id, value and extensions. The accessor "getMaxLength" gives direct access to the value
     */
    public DataElement setMaxLengthElement(IntegerType value) { 
      this.maxLength = value;
      return this;
    }

    /**
     * @return Indicates the shortest length that SHALL be supported by conformant instances without truncation.
     */
    public int getMaxLength() { 
      return this.maxLength == null ? 0 : this.maxLength.getValue();
    }

    /**
     * @param value Indicates the shortest length that SHALL be supported by conformant instances without truncation.
     */
    public DataElement setMaxLength(int value) { 
        if (this.maxLength == null)
          this.maxLength = new IntegerType();
        this.maxLength.setValue(value);
      return this;
    }

    /**
     * @return {@link #units} (Identifies the units of measure in which the data element should be captured or expressed.)
     */
    public Type getUnits() { 
      return this.units;
    }

    /**
     * @return {@link #units} (Identifies the units of measure in which the data element should be captured or expressed.)
     */
    public CodeableConcept getUnitsCodeableConcept() throws Exception { 
      if (!(this.units instanceof CodeableConcept))
        throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.units.getClass().getName()+" was encountered");
      return (CodeableConcept) this.units;
    }

    /**
     * @return {@link #units} (Identifies the units of measure in which the data element should be captured or expressed.)
     */
    public Reference getUnitsReference() throws Exception { 
      if (!(this.units instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.units.getClass().getName()+" was encountered");
      return (Reference) this.units;
    }

    public boolean hasUnits() { 
      return this.units != null && !this.units.isEmpty();
    }

    /**
     * @param value {@link #units} (Identifies the units of measure in which the data element should be captured or expressed.)
     */
    public DataElement setUnits(Type value) { 
      this.units = value;
      return this;
    }

    /**
     * @return {@link #binding} (Binds to a value set if this element is coded (code, Coding, CodeableConcept).)
     */
    public DataElementBindingComponent getBinding() { 
      if (this.binding == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.binding");
        else if (Configuration.doAutoCreate())
          this.binding = new DataElementBindingComponent(); // cc
      return this.binding;
    }

    public boolean hasBinding() { 
      return this.binding != null && !this.binding.isEmpty();
    }

    /**
     * @param value {@link #binding} (Binds to a value set if this element is coded (code, Coding, CodeableConcept).)
     */
    public DataElement setBinding(DataElementBindingComponent value) { 
      this.binding = value;
      return this;
    }

    /**
     * @return {@link #mapping} (Identifies a concept from an external specification that roughly corresponds to this element.)
     */
    public List<DataElementMappingComponent> getMapping() { 
      if (this.mapping == null)
        this.mapping = new ArrayList<DataElementMappingComponent>();
      return this.mapping;
    }

    public boolean hasMapping() { 
      if (this.mapping == null)
        return false;
      for (DataElementMappingComponent item : this.mapping)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #mapping} (Identifies a concept from an external specification that roughly corresponds to this element.)
     */
    // syntactic sugar
    public DataElementMappingComponent addMapping() { //3
      DataElementMappingComponent t = new DataElementMappingComponent();
      if (this.mapping == null)
        this.mapping = new ArrayList<DataElementMappingComponent>();
      this.mapping.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The identifier that is used to identify this data element when it is referenced in a Profile, Questionnaire or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("publisher", "string", "Details of the individual or organization who accepts responsibility for publishing the data element.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("telecom", "ContactPoint", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("status", "code", "The status of the data element.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("date", "dateTime", "The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired).", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("name", "string", "The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("category", "CodeableConcept", "A set of terms from external terminologies that may be used to assist with indexing and searching of data element definitions.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("granularity", "code", "Identifies how precise the data element is in its definition.", 0, java.lang.Integer.MAX_VALUE, granularity));
        childrenList.add(new Property("code", "Coding", "A code that provides the meaning for a data element according to a particular terminology.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("question", "string", "The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey).", 0, java.lang.Integer.MAX_VALUE, question));
        childrenList.add(new Property("label", "string", "The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.", 0, java.lang.Integer.MAX_VALUE, label));
        childrenList.add(new Property("definition", "string", "Provides a complete explanation of the meaning of the data element for human readability.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("comments", "string", "Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.", 0, java.lang.Integer.MAX_VALUE, comments));
        childrenList.add(new Property("requirements", "string", "Explains why this element is needed and why it's been constrained as it has.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("synonym", "string", "Identifies additional names by which this element might also be known.", 0, java.lang.Integer.MAX_VALUE, synonym));
        childrenList.add(new Property("type", "code", "The FHIR data type that is the type for data that corresponds to this data element.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("example[x]", "*", "A sample value for this element demonstrating the type of information that would typically be captured.", 0, java.lang.Integer.MAX_VALUE, example));
        childrenList.add(new Property("maxLength", "integer", "Indicates the shortest length that SHALL be supported by conformant instances without truncation.", 0, java.lang.Integer.MAX_VALUE, maxLength));
        childrenList.add(new Property("units[x]", "CodeableConcept|Reference(ValueSet)", "Identifies the units of measure in which the data element should be captured or expressed.", 0, java.lang.Integer.MAX_VALUE, units));
        childrenList.add(new Property("binding", "", "Binds to a value set if this element is coded (code, Coding, CodeableConcept).", 0, java.lang.Integer.MAX_VALUE, binding));
        childrenList.add(new Property("mapping", "", "Identifies a concept from an external specification that roughly corresponds to this element.", 0, java.lang.Integer.MAX_VALUE, mapping));
      }

      public DataElement copy() {
        DataElement dst = new DataElement();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.date = date == null ? null : date.copy();
        dst.name = name == null ? null : name.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.granularity = granularity == null ? null : granularity.copy();
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        dst.question = question == null ? null : question.copy();
        dst.label = label == null ? null : label.copy();
        dst.definition = definition == null ? null : definition.copy();
        dst.comments = comments == null ? null : comments.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        if (synonym != null) {
          dst.synonym = new ArrayList<StringType>();
          for (StringType i : synonym)
            dst.synonym.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.example = example == null ? null : example.copy();
        dst.maxLength = maxLength == null ? null : maxLength.copy();
        dst.units = units == null ? null : units.copy();
        dst.binding = binding == null ? null : binding.copy();
        if (mapping != null) {
          dst.mapping = new ArrayList<DataElementMappingComponent>();
          for (DataElementMappingComponent i : mapping)
            dst.mapping.add(i.copy());
        };
        return dst;
      }

      protected DataElement typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DataElement))
          return false;
        DataElement o = (DataElement) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true) && compareDeep(publisher, o.publisher, true)
           && compareDeep(telecom, o.telecom, true) && compareDeep(status, o.status, true) && compareDeep(date, o.date, true)
           && compareDeep(name, o.name, true) && compareDeep(category, o.category, true) && compareDeep(granularity, o.granularity, true)
           && compareDeep(code, o.code, true) && compareDeep(question, o.question, true) && compareDeep(label, o.label, true)
           && compareDeep(definition, o.definition, true) && compareDeep(comments, o.comments, true) && compareDeep(requirements, o.requirements, true)
           && compareDeep(synonym, o.synonym, true) && compareDeep(type, o.type, true) && compareDeep(example, o.example, true)
           && compareDeep(maxLength, o.maxLength, true) && compareDeep(units, o.units, true) && compareDeep(binding, o.binding, true)
           && compareDeep(mapping, o.mapping, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DataElement))
          return false;
        DataElement o = (DataElement) other;
        return compareValues(version, o.version, true) && compareValues(publisher, o.publisher, true) && compareValues(status, o.status, true)
           && compareValues(date, o.date, true) && compareValues(name, o.name, true) && compareValues(granularity, o.granularity, true)
           && compareValues(question, o.question, true) && compareValues(label, o.label, true) && compareValues(definition, o.definition, true)
           && compareValues(comments, o.comments, true) && compareValues(requirements, o.requirements, true) && compareValues(synonym, o.synonym, true)
           && compareValues(type, o.type, true) && compareValues(maxLength, o.maxLength, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (publisher == null || publisher.isEmpty()) && (telecom == null || telecom.isEmpty()) && (status == null || status.isEmpty())
           && (date == null || date.isEmpty()) && (name == null || name.isEmpty()) && (category == null || category.isEmpty())
           && (granularity == null || granularity.isEmpty()) && (code == null || code.isEmpty()) && (question == null || question.isEmpty())
           && (label == null || label.isEmpty()) && (definition == null || definition.isEmpty()) && (comments == null || comments.isEmpty())
           && (requirements == null || requirements.isEmpty()) && (synonym == null || synonym.isEmpty())
           && (type == null || type.isEmpty()) && (example == null || example.isEmpty()) && (maxLength == null || maxLength.isEmpty())
           && (units == null || units.isEmpty()) && (binding == null || binding.isEmpty()) && (mapping == null || mapping.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DataElement;
   }

  @SearchParamDefinition(name="date", path="DataElement.date", description="The data element publication date", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="DataElement.identifier", description="The identifier of the data element", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="code", path="DataElement.code", description="A code for the data element (server may choose to do subsumption)", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="name", path="DataElement.name", description="Name of the data element", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="publisher", path="DataElement.publisher", description="Name of the publisher of the data element", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="description", path="DataElement.definition", description="Text search in the description of the data element", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="category", path="DataElement.category", description="A category assigned to the data element (server may choose to do subsumption)", type="token" )
  public static final String SP_CATEGORY = "category";
  @SearchParamDefinition(name="version", path="DataElement.version", description="The version identifier of the data element", type="string" )
  public static final String SP_VERSION = "version";
  @SearchParamDefinition(name="status", path="DataElement.status", description="The current status of the data element", type="token" )
  public static final String SP_STATUS = "status";

}

