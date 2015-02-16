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

// Generated on Sat, Feb 14, 2015 16:12-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
 */
@ResourceDef(name="OperationDefinition", profile="http://hl7.org/fhir/Profile/OperationDefinition")
public class OperationDefinition extends DomainResource {

    public enum ResourceProfileStatus {
        /**
         * This profile is still under development.
         */
        DRAFT, 
        /**
         * This profile is ready for normal use.
         */
        ACTIVE, 
        /**
         * This profile has been deprecated, withdrawn or superseded and should no longer be used.
         */
        RETIRED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceProfileStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("retired".equals(codeString))
          return RETIRED;
        throw new Exception("Unknown ResourceProfileStatus code '"+codeString+"'");
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
            case DRAFT: return "This profile is still under development.";
            case ACTIVE: return "This profile is ready for normal use.";
            case RETIRED: return "This profile has been deprecated, withdrawn or superseded and should no longer be used.";
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

  public static class ResourceProfileStatusEnumFactory implements EnumFactory<ResourceProfileStatus> {
    public ResourceProfileStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ResourceProfileStatus.DRAFT;
        if ("active".equals(codeString))
          return ResourceProfileStatus.ACTIVE;
        if ("retired".equals(codeString))
          return ResourceProfileStatus.RETIRED;
        throw new IllegalArgumentException("Unknown ResourceProfileStatus code '"+codeString+"'");
        }
    public String toCode(ResourceProfileStatus code) {
      if (code == ResourceProfileStatus.DRAFT)
        return "draft";
      if (code == ResourceProfileStatus.ACTIVE)
        return "active";
      if (code == ResourceProfileStatus.RETIRED)
        return "retired";
      return "?";
      }
    }

    public enum OperationKind {
        /**
         * This operation is invoked as an operation.
         */
        OPERATION, 
        /**
         * This operation is a named query, invoked using the search mechanism.
         */
        QUERY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OperationKind fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("operation".equals(codeString))
          return OPERATION;
        if ("query".equals(codeString))
          return QUERY;
        throw new Exception("Unknown OperationKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OPERATION: return "operation";
            case QUERY: return "query";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case OPERATION: return "";
            case QUERY: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case OPERATION: return "This operation is invoked as an operation.";
            case QUERY: return "This operation is a named query, invoked using the search mechanism.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OPERATION: return "operation";
            case QUERY: return "query";
            default: return "?";
          }
        }
    }

  public static class OperationKindEnumFactory implements EnumFactory<OperationKind> {
    public OperationKind fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("operation".equals(codeString))
          return OperationKind.OPERATION;
        if ("query".equals(codeString))
          return OperationKind.QUERY;
        throw new IllegalArgumentException("Unknown OperationKind code '"+codeString+"'");
        }
    public String toCode(OperationKind code) {
      if (code == OperationKind.OPERATION)
        return "operation";
      if (code == OperationKind.QUERY)
        return "query";
      return "?";
      }
    }

    public enum OperationParameterUse {
        /**
         * This is an input parameter.
         */
        IN, 
        /**
         * This is an output parameter.
         */
        OUT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OperationParameterUse fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in".equals(codeString))
          return IN;
        if ("out".equals(codeString))
          return OUT;
        throw new Exception("Unknown OperationParameterUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case IN: return "in";
            case OUT: return "out";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case IN: return "";
            case OUT: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case IN: return "This is an input parameter.";
            case OUT: return "This is an output parameter.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case IN: return "in";
            case OUT: return "out";
            default: return "?";
          }
        }
    }

  public static class OperationParameterUseEnumFactory implements EnumFactory<OperationParameterUse> {
    public OperationParameterUse fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in".equals(codeString))
          return OperationParameterUse.IN;
        if ("out".equals(codeString))
          return OperationParameterUse.OUT;
        throw new IllegalArgumentException("Unknown OperationParameterUse code '"+codeString+"'");
        }
    public String toCode(OperationParameterUse code) {
      if (code == OperationParameterUse.IN)
        return "in";
      if (code == OperationParameterUse.OUT)
        return "out";
      return "?";
      }
    }

    @Block()
    public static class OperationDefinitionParameterComponent extends BackboneElement {
        /**
         * The name of used to identify the parameter.
         */
        @Child(name="name", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Name of the parameter", formalDefinition="The name of used to identify the parameter." )
        protected CodeType name;

        /**
         * Whether this is an input or an output parameter.
         */
        @Child(name="use", type={CodeType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="in | out", formalDefinition="Whether this is an input or an output parameter." )
        protected Enumeration<OperationParameterUse> use;

        /**
         * The minimum number of times this parameter SHALL appear in the request or response.
         */
        @Child(name="min", type={IntegerType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Minimum Cardinality", formalDefinition="The minimum number of times this parameter SHALL appear in the request or response." )
        protected IntegerType min;

        /**
         * The maximum number of times this element is permitted to appear in the request or response.
         */
        @Child(name="max", type={StringType.class}, order=4, min=1, max=1)
        @Description(shortDefinition="Maximum Cardinality (a number or *)", formalDefinition="The maximum number of times this element is permitted to appear in the request or response." )
        protected StringType max;

        /**
         * Describes the meaning or use of this parameter.
         */
        @Child(name="documentation", type={StringType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Description of meaning/use", formalDefinition="Describes the meaning or use of this parameter." )
        protected StringType documentation;

        /**
         * The type for this parameter.
         */
        @Child(name="type", type={CodeType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="What type this parameter hs", formalDefinition="The type for this parameter." )
        protected CodeType type;

        /**
         * A profile the specifies the rules that this parameter must conform to.
         */
        @Child(name="profile", type={Profile.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Profile on the type", formalDefinition="A profile the specifies the rules that this parameter must conform to." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (A profile the specifies the rules that this parameter must conform to.)
         */
        protected Profile profileTarget;

        /**
         * The parts of a Tuple Parameter.
         */
        @Child(name="part", type={}, order=8, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Parts of a Tuple Parameter", formalDefinition="The parts of a Tuple Parameter." )
        protected List<OperationDefinitionParameterPartComponent> part;

        private static final long serialVersionUID = 176609099L;

      public OperationDefinitionParameterComponent() {
        super();
      }

      public OperationDefinitionParameterComponent(CodeType name, Enumeration<OperationParameterUse> use, IntegerType min, StringType max) {
        super();
        this.name = name;
        this.use = use;
        this.min = min;
        this.max = max;
      }

        /**
         * @return {@link #name} (The name of used to identify the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public CodeType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new CodeType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name of used to identify the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setNameElement(CodeType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of used to identify the parameter.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of used to identify the parameter.
         */
        public OperationDefinitionParameterComponent setName(String value) { 
            if (this.name == null)
              this.name = new CodeType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #use} (Whether this is an input or an output parameter.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
         */
        public Enumeration<OperationParameterUse> getUseElement() { 
          if (this.use == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.use");
            else if (Configuration.doAutoCreate())
              this.use = new Enumeration<OperationParameterUse>(new OperationParameterUseEnumFactory()); // bb
          return this.use;
        }

        public boolean hasUseElement() { 
          return this.use != null && !this.use.isEmpty();
        }

        public boolean hasUse() { 
          return this.use != null && !this.use.isEmpty();
        }

        /**
         * @param value {@link #use} (Whether this is an input or an output parameter.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setUseElement(Enumeration<OperationParameterUse> value) { 
          this.use = value;
          return this;
        }

        /**
         * @return Whether this is an input or an output parameter.
         */
        public OperationParameterUse getUse() { 
          return this.use == null ? null : this.use.getValue();
        }

        /**
         * @param value Whether this is an input or an output parameter.
         */
        public OperationDefinitionParameterComponent setUse(OperationParameterUse value) { 
            if (this.use == null)
              this.use = new Enumeration<OperationParameterUse>(new OperationParameterUseEnumFactory());
            this.use.setValue(value);
          return this;
        }

        /**
         * @return {@link #min} (The minimum number of times this parameter SHALL appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public IntegerType getMinElement() { 
          if (this.min == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.min");
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
         * @param value {@link #min} (The minimum number of times this parameter SHALL appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setMinElement(IntegerType value) { 
          this.min = value;
          return this;
        }

        /**
         * @return The minimum number of times this parameter SHALL appear in the request or response.
         */
        public int getMin() { 
          return this.min == null ? 0 : this.min.getValue();
        }

        /**
         * @param value The minimum number of times this parameter SHALL appear in the request or response.
         */
        public OperationDefinitionParameterComponent setMin(int value) { 
            if (this.min == null)
              this.min = new IntegerType();
            this.min.setValue(value);
          return this;
        }

        /**
         * @return {@link #max} (The maximum number of times this element is permitted to appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public StringType getMaxElement() { 
          if (this.max == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.max");
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
         * @param value {@link #max} (The maximum number of times this element is permitted to appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setMaxElement(StringType value) { 
          this.max = value;
          return this;
        }

        /**
         * @return The maximum number of times this element is permitted to appear in the request or response.
         */
        public String getMax() { 
          return this.max == null ? null : this.max.getValue();
        }

        /**
         * @param value The maximum number of times this element is permitted to appear in the request or response.
         */
        public OperationDefinitionParameterComponent setMax(String value) { 
            if (this.max == null)
              this.max = new StringType();
            this.max.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (Describes the meaning or use of this parameter.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Describes the meaning or use of this parameter.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Describes the meaning or use of this parameter.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Describes the meaning or use of this parameter.
         */
        public OperationDefinitionParameterComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (The type for this parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.type");
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
         * @param value {@link #type} (The type for this parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type for this parameter.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type for this parameter.
         */
        public OperationDefinitionParameterComponent setType(String value) { 
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
         * @return {@link #profile} (A profile the specifies the rules that this parameter must conform to.)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (A profile the specifies the rules that this parameter must conform to.)
         */
        public OperationDefinitionParameterComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A profile the specifies the rules that this parameter must conform to.)
         */
        public Profile getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new Profile(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A profile the specifies the rules that this parameter must conform to.)
         */
        public OperationDefinitionParameterComponent setProfileTarget(Profile value) { 
          this.profileTarget = value;
          return this;
        }

        /**
         * @return {@link #part} (The parts of a Tuple Parameter.)
         */
        public List<OperationDefinitionParameterPartComponent> getPart() { 
          if (this.part == null)
            this.part = new ArrayList<OperationDefinitionParameterPartComponent>();
          return this.part;
        }

        public boolean hasPart() { 
          if (this.part == null)
            return false;
          for (OperationDefinitionParameterPartComponent item : this.part)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #part} (The parts of a Tuple Parameter.)
         */
    // syntactic sugar
        public OperationDefinitionParameterPartComponent addPart() { //3
          OperationDefinitionParameterPartComponent t = new OperationDefinitionParameterPartComponent();
          if (this.part == null)
            this.part = new ArrayList<OperationDefinitionParameterPartComponent>();
          this.part.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "code", "The name of used to identify the parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("use", "code", "Whether this is an input or an output parameter.", 0, java.lang.Integer.MAX_VALUE, use));
          childrenList.add(new Property("min", "integer", "The minimum number of times this parameter SHALL appear in the request or response.", 0, java.lang.Integer.MAX_VALUE, min));
          childrenList.add(new Property("max", "string", "The maximum number of times this element is permitted to appear in the request or response.", 0, java.lang.Integer.MAX_VALUE, max));
          childrenList.add(new Property("documentation", "string", "Describes the meaning or use of this parameter.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("type", "code", "The type for this parameter.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("profile", "Reference(Profile)", "A profile the specifies the rules that this parameter must conform to.", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("part", "", "The parts of a Tuple Parameter.", 0, java.lang.Integer.MAX_VALUE, part));
        }

      public OperationDefinitionParameterComponent copy() {
        OperationDefinitionParameterComponent dst = new OperationDefinitionParameterComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.use = use == null ? null : use.copy();
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        if (part != null) {
          dst.part = new ArrayList<OperationDefinitionParameterPartComponent>();
          for (OperationDefinitionParameterPartComponent i : part)
            dst.part.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationDefinitionParameterComponent))
          return false;
        OperationDefinitionParameterComponent o = (OperationDefinitionParameterComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(use, o.use, true) && compareDeep(min, o.min, true)
           && compareDeep(max, o.max, true) && compareDeep(documentation, o.documentation, true) && compareDeep(type, o.type, true)
           && compareDeep(profile, o.profile, true) && compareDeep(part, o.part, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationDefinitionParameterComponent))
          return false;
        OperationDefinitionParameterComponent o = (OperationDefinitionParameterComponent) other;
        return compareValues(name, o.name, true) && compareValues(use, o.use, true) && compareValues(min, o.min, true)
           && compareValues(max, o.max, true) && compareValues(documentation, o.documentation, true) && compareValues(type, o.type, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (use == null || use.isEmpty())
           && (min == null || min.isEmpty()) && (max == null || max.isEmpty()) && (documentation == null || documentation.isEmpty())
           && (type == null || type.isEmpty()) && (profile == null || profile.isEmpty()) && (part == null || part.isEmpty())
          ;
      }

  }

    @Block()
    public static class OperationDefinitionParameterPartComponent extends BackboneElement {
        /**
         * The name of used to identify the parameter.
         */
        @Child(name="name", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Name of the parameter", formalDefinition="The name of used to identify the parameter." )
        protected CodeType name;

        /**
         * The minimum number of times this parameter SHALL appear in the request or response.
         */
        @Child(name="min", type={IntegerType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Minimum Cardinality", formalDefinition="The minimum number of times this parameter SHALL appear in the request or response." )
        protected IntegerType min;

        /**
         * The maximum number of times this element is permitted to appear in the request or response.
         */
        @Child(name="max", type={StringType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Maximum Cardinality (a number or *)", formalDefinition="The maximum number of times this element is permitted to appear in the request or response." )
        protected StringType max;

        /**
         * Describes the meaning or use of this parameter.
         */
        @Child(name="documentation", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Description of meaning/use", formalDefinition="Describes the meaning or use of this parameter." )
        protected StringType documentation;

        /**
         * The type for this parameter.
         */
        @Child(name="type", type={CodeType.class}, order=5, min=1, max=1)
        @Description(shortDefinition="What type this parameter hs", formalDefinition="The type for this parameter." )
        protected CodeType type;

        /**
         * A profile the specifies the rules that this parameter must conform to.
         */
        @Child(name="profile", type={Profile.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Profile on the type", formalDefinition="A profile the specifies the rules that this parameter must conform to." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (A profile the specifies the rules that this parameter must conform to.)
         */
        protected Profile profileTarget;

        private static final long serialVersionUID = -2143629468L;

      public OperationDefinitionParameterPartComponent() {
        super();
      }

      public OperationDefinitionParameterPartComponent(CodeType name, IntegerType min, StringType max, CodeType type) {
        super();
        this.name = name;
        this.min = min;
        this.max = max;
        this.type = type;
      }

        /**
         * @return {@link #name} (The name of used to identify the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public CodeType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterPartComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new CodeType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name of used to identify the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public OperationDefinitionParameterPartComponent setNameElement(CodeType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of used to identify the parameter.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of used to identify the parameter.
         */
        public OperationDefinitionParameterPartComponent setName(String value) { 
            if (this.name == null)
              this.name = new CodeType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #min} (The minimum number of times this parameter SHALL appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public IntegerType getMinElement() { 
          if (this.min == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterPartComponent.min");
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
         * @param value {@link #min} (The minimum number of times this parameter SHALL appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public OperationDefinitionParameterPartComponent setMinElement(IntegerType value) { 
          this.min = value;
          return this;
        }

        /**
         * @return The minimum number of times this parameter SHALL appear in the request or response.
         */
        public int getMin() { 
          return this.min == null ? 0 : this.min.getValue();
        }

        /**
         * @param value The minimum number of times this parameter SHALL appear in the request or response.
         */
        public OperationDefinitionParameterPartComponent setMin(int value) { 
            if (this.min == null)
              this.min = new IntegerType();
            this.min.setValue(value);
          return this;
        }

        /**
         * @return {@link #max} (The maximum number of times this element is permitted to appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public StringType getMaxElement() { 
          if (this.max == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterPartComponent.max");
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
         * @param value {@link #max} (The maximum number of times this element is permitted to appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public OperationDefinitionParameterPartComponent setMaxElement(StringType value) { 
          this.max = value;
          return this;
        }

        /**
         * @return The maximum number of times this element is permitted to appear in the request or response.
         */
        public String getMax() { 
          return this.max == null ? null : this.max.getValue();
        }

        /**
         * @param value The maximum number of times this element is permitted to appear in the request or response.
         */
        public OperationDefinitionParameterPartComponent setMax(String value) { 
            if (this.max == null)
              this.max = new StringType();
            this.max.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (Describes the meaning or use of this parameter.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterPartComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Describes the meaning or use of this parameter.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public OperationDefinitionParameterPartComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Describes the meaning or use of this parameter.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Describes the meaning or use of this parameter.
         */
        public OperationDefinitionParameterPartComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (The type for this parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterPartComponent.type");
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
         * @param value {@link #type} (The type for this parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public OperationDefinitionParameterPartComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type for this parameter.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type for this parameter.
         */
        public OperationDefinitionParameterPartComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (A profile the specifies the rules that this parameter must conform to.)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterPartComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (A profile the specifies the rules that this parameter must conform to.)
         */
        public OperationDefinitionParameterPartComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A profile the specifies the rules that this parameter must conform to.)
         */
        public Profile getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterPartComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new Profile(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A profile the specifies the rules that this parameter must conform to.)
         */
        public OperationDefinitionParameterPartComponent setProfileTarget(Profile value) { 
          this.profileTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "code", "The name of used to identify the parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("min", "integer", "The minimum number of times this parameter SHALL appear in the request or response.", 0, java.lang.Integer.MAX_VALUE, min));
          childrenList.add(new Property("max", "string", "The maximum number of times this element is permitted to appear in the request or response.", 0, java.lang.Integer.MAX_VALUE, max));
          childrenList.add(new Property("documentation", "string", "Describes the meaning or use of this parameter.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("type", "code", "The type for this parameter.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("profile", "Reference(Profile)", "A profile the specifies the rules that this parameter must conform to.", 0, java.lang.Integer.MAX_VALUE, profile));
        }

      public OperationDefinitionParameterPartComponent copy() {
        OperationDefinitionParameterPartComponent dst = new OperationDefinitionParameterPartComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationDefinitionParameterPartComponent))
          return false;
        OperationDefinitionParameterPartComponent o = (OperationDefinitionParameterPartComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(min, o.min, true) && compareDeep(max, o.max, true)
           && compareDeep(documentation, o.documentation, true) && compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationDefinitionParameterPartComponent))
          return false;
        OperationDefinitionParameterPartComponent o = (OperationDefinitionParameterPartComponent) other;
        return compareValues(name, o.name, true) && compareValues(min, o.min, true) && compareValues(max, o.max, true)
           && compareValues(documentation, o.documentation, true) && compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (min == null || min.isEmpty())
           && (max == null || max.isEmpty()) && (documentation == null || documentation.isEmpty()) && (type == null || type.isEmpty())
           && (profile == null || profile.isEmpty());
      }

  }

    /**
     * The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).
     */
    @Child(name="identifier", type={UriType.class}, order=-1, min=0, max=1)
    @Description(shortDefinition="Logical id to reference this operation definition", formalDefinition="The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)." )
    protected UriType identifier;

    /**
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    @Child(name="version", type={StringType.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Logical id for this version of the operation definition", formalDefinition="The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp." )
    protected StringType version;

    /**
     * A free text natural language name identifying the Profile.
     */
    @Child(name="title", type={StringType.class}, order=1, min=1, max=1)
    @Description(shortDefinition="Informal name for this profile", formalDefinition="A free text natural language name identifying the Profile." )
    protected StringType title;

    /**
     * Details of the individual or organization who accepts responsibility for publishing the profile.
     */
    @Child(name="publisher", type={StringType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="Details of the individual or organization who accepts responsibility for publishing the profile." )
    protected StringType publisher;

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     */
    @Child(name="telecom", type={ContactPoint.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contact information of the publisher", formalDefinition="Contact details to assist a user in finding and communicating with the publisher." )
    protected List<ContactPoint> telecom;

    /**
     * A free text natural language description of the profile and its use.
     */
    @Child(name="description", type={StringType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Natural language description of the operation", formalDefinition="A free text natural language description of the profile and its use." )
    protected StringType description;

    /**
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     */
    @Child(name="code", type={Coding.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Assist with indexing and finding", formalDefinition="A set of terms from external terminologies that may be used to assist with indexing and searching of templates." )
    protected List<Coding> code;

    /**
     * The status of the profile.
     */
    @Child(name="status", type={CodeType.class}, order=6, min=1, max=1)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the profile." )
    protected Enumeration<ResourceProfileStatus> status;

    /**
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name="experimental", type={BooleanType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The date that this version of the profile was published.
     */
    @Child(name="date", type={DateTimeType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Date for this version of the operation definition", formalDefinition="The date that this version of the profile was published." )
    protected DateTimeType date;

    /**
     * Whether this is operation or named query.
     */
    @Child(name="kind", type={CodeType.class}, order=9, min=1, max=1)
    @Description(shortDefinition="operation | query", formalDefinition="Whether this is operation or named query." )
    protected Enumeration<OperationKind> kind;

    /**
     * The name used to invoke the operation.
     */
    @Child(name="name", type={CodeType.class}, order=10, min=1, max=1)
    @Description(shortDefinition="Name used to invoke the operation", formalDefinition="The name used to invoke the operation." )
    protected CodeType name;

    /**
     * Additional information about how to use this operation or named query.
     */
    @Child(name="notes", type={StringType.class}, order=11, min=0, max=1)
    @Description(shortDefinition="Additional information about use", formalDefinition="Additional information about how to use this operation or named query." )
    protected StringType notes;

    /**
     * Indicates that this operation definition is a constraining profile on the base.
     */
    @Child(name="base", type={OperationDefinition.class}, order=12, min=0, max=1)
    @Description(shortDefinition="Marks this as a profile of the base", formalDefinition="Indicates that this operation definition is a constraining profile on the base." )
    protected Reference base;

    /**
     * The actual object that is the target of the reference (Indicates that this operation definition is a constraining profile on the base.)
     */
    protected OperationDefinition baseTarget;

    /**
     * Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).
     */
    @Child(name="system", type={BooleanType.class}, order=13, min=1, max=1)
    @Description(shortDefinition="Invoke at the system level?", formalDefinition="Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context)." )
    protected BooleanType system;

    /**
     * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).
     */
    @Child(name="type", type={CodeType.class}, order=14, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Invoke at resource level for these type", formalDefinition="Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context)." )
    protected List<CodeType> type;

    /**
     * Indicates whether this operation can be invoked on a particular instance of one of the given types.
     */
    @Child(name="instance", type={BooleanType.class}, order=15, min=1, max=1)
    @Description(shortDefinition="Invoke on an instance?", formalDefinition="Indicates whether this operation can be invoked on a particular instance of one of the given types." )
    protected BooleanType instance;

    /**
     * The parameters for the operation/query.
     */
    @Child(name="parameter", type={}, order=16, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Parameters for the operation/query", formalDefinition="The parameters for the operation/query." )
    protected List<OperationDefinitionParameterComponent> parameter;

    private static final long serialVersionUID = 300577956L;

    public OperationDefinition() {
      super();
    }

    public OperationDefinition(StringType title, Enumeration<ResourceProfileStatus> status, Enumeration<OperationKind> kind, CodeType name, BooleanType system, BooleanType instance) {
      super();
      this.title = title;
      this.status = status;
      this.kind = kind;
      this.name = name;
      this.system = system;
      this.instance = instance;
    }

    /**
     * @return {@link #identifier} (The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
     */
    public UriType getIdentifierElement() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.identifier");
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
     * @param value {@link #identifier} (The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
     */
    public OperationDefinition setIdentifierElement(UriType value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).
     */
    public String getIdentifier() { 
      return this.identifier == null ? null : this.identifier.getValue();
    }

    /**
     * @param value The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).
     */
    public OperationDefinition setIdentifier(String value) { 
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
     * @return {@link #version} (The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public OperationDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public OperationDefinition setVersion(String value) { 
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
     * @return {@link #title} (A free text natural language name identifying the Profile.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.title");
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
     * @param value {@link #title} (A free text natural language name identifying the Profile.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public OperationDefinition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A free text natural language name identifying the Profile.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A free text natural language name identifying the Profile.
     */
    public OperationDefinition setTitle(String value) { 
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (Details of the individual or organization who accepts responsibility for publishing the profile.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.publisher");
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
     * @param value {@link #publisher} (Details of the individual or organization who accepts responsibility for publishing the profile.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public OperationDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return Details of the individual or organization who accepts responsibility for publishing the profile.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value Details of the individual or organization who accepts responsibility for publishing the profile.
     */
    public OperationDefinition setPublisher(String value) { 
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
     * @return {@link #description} (A free text natural language description of the profile and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.description");
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
     * @param value {@link #description} (A free text natural language description of the profile and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public OperationDefinition setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the profile and its use.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the profile and its use.
     */
    public OperationDefinition setDescription(String value) { 
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
     * @return {@link #code} (A set of terms from external terminologies that may be used to assist with indexing and searching of templates.)
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
     * @return {@link #code} (A set of terms from external terminologies that may be used to assist with indexing and searching of templates.)
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
     * @return {@link #status} (The status of the profile.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ResourceProfileStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ResourceProfileStatus>(new ResourceProfileStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the profile.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public OperationDefinition setStatusElement(Enumeration<ResourceProfileStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the profile.
     */
    public ResourceProfileStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the profile.
     */
    public OperationDefinition setStatus(ResourceProfileStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ResourceProfileStatus>(new ResourceProfileStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.experimental");
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
     * @param value {@link #experimental} (This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public OperationDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null ? false : this.experimental.getValue();
    }

    /**
     * @param value This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public OperationDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date that this version of the profile was published.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.date");
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
     * @param value {@link #date} (The date that this version of the profile was published.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public OperationDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that this version of the profile was published.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that this version of the profile was published.
     */
    public OperationDefinition setDate(Date value) { 
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
     * @return {@link #kind} (Whether this is operation or named query.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public Enumeration<OperationKind> getKindElement() { 
      if (this.kind == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.kind");
        else if (Configuration.doAutoCreate())
          this.kind = new Enumeration<OperationKind>(new OperationKindEnumFactory()); // bb
      return this.kind;
    }

    public boolean hasKindElement() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    public boolean hasKind() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    /**
     * @param value {@link #kind} (Whether this is operation or named query.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public OperationDefinition setKindElement(Enumeration<OperationKind> value) { 
      this.kind = value;
      return this;
    }

    /**
     * @return Whether this is operation or named query.
     */
    public OperationKind getKind() { 
      return this.kind == null ? null : this.kind.getValue();
    }

    /**
     * @param value Whether this is operation or named query.
     */
    public OperationDefinition setKind(OperationKind value) { 
        if (this.kind == null)
          this.kind = new Enumeration<OperationKind>(new OperationKindEnumFactory());
        this.kind.setValue(value);
      return this;
    }

    /**
     * @return {@link #name} (The name used to invoke the operation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public CodeType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.name");
        else if (Configuration.doAutoCreate())
          this.name = new CodeType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (The name used to invoke the operation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public OperationDefinition setNameElement(CodeType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return The name used to invoke the operation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value The name used to invoke the operation.
     */
    public OperationDefinition setName(String value) { 
        if (this.name == null)
          this.name = new CodeType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #notes} (Additional information about how to use this operation or named query.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public StringType getNotesElement() { 
      if (this.notes == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.notes");
        else if (Configuration.doAutoCreate())
          this.notes = new StringType(); // bb
      return this.notes;
    }

    public boolean hasNotesElement() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    public boolean hasNotes() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    /**
     * @param value {@link #notes} (Additional information about how to use this operation or named query.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public OperationDefinition setNotesElement(StringType value) { 
      this.notes = value;
      return this;
    }

    /**
     * @return Additional information about how to use this operation or named query.
     */
    public String getNotes() { 
      return this.notes == null ? null : this.notes.getValue();
    }

    /**
     * @param value Additional information about how to use this operation or named query.
     */
    public OperationDefinition setNotes(String value) { 
      if (Utilities.noString(value))
        this.notes = null;
      else {
        if (this.notes == null)
          this.notes = new StringType();
        this.notes.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #base} (Indicates that this operation definition is a constraining profile on the base.)
     */
    public Reference getBase() { 
      if (this.base == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.base");
        else if (Configuration.doAutoCreate())
          this.base = new Reference(); // cc
      return this.base;
    }

    public boolean hasBase() { 
      return this.base != null && !this.base.isEmpty();
    }

    /**
     * @param value {@link #base} (Indicates that this operation definition is a constraining profile on the base.)
     */
    public OperationDefinition setBase(Reference value) { 
      this.base = value;
      return this;
    }

    /**
     * @return {@link #base} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates that this operation definition is a constraining profile on the base.)
     */
    public OperationDefinition getBaseTarget() { 
      if (this.baseTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.base");
        else if (Configuration.doAutoCreate())
          this.baseTarget = new OperationDefinition(); // aa
      return this.baseTarget;
    }

    /**
     * @param value {@link #base} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates that this operation definition is a constraining profile on the base.)
     */
    public OperationDefinition setBaseTarget(OperationDefinition value) { 
      this.baseTarget = value;
      return this;
    }

    /**
     * @return {@link #system} (Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public BooleanType getSystemElement() { 
      if (this.system == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.system");
        else if (Configuration.doAutoCreate())
          this.system = new BooleanType(); // bb
      return this.system;
    }

    public boolean hasSystemElement() { 
      return this.system != null && !this.system.isEmpty();
    }

    public boolean hasSystem() { 
      return this.system != null && !this.system.isEmpty();
    }

    /**
     * @param value {@link #system} (Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public OperationDefinition setSystemElement(BooleanType value) { 
      this.system = value;
      return this;
    }

    /**
     * @return Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).
     */
    public boolean getSystem() { 
      return this.system == null ? false : this.system.getValue();
    }

    /**
     * @param value Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).
     */
    public OperationDefinition setSystem(boolean value) { 
        if (this.system == null)
          this.system = new BooleanType();
        this.system.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).)
     */
    public List<CodeType> getType() { 
      if (this.type == null)
        this.type = new ArrayList<CodeType>();
      return this.type;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (CodeType item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #type} (Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).)
     */
    // syntactic sugar
    public CodeType addTypeElement() {//2 
      CodeType t = new CodeType();
      if (this.type == null)
        this.type = new ArrayList<CodeType>();
      this.type.add(t);
      return t;
    }

    /**
     * @param value {@link #type} (Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).)
     */
    public OperationDefinition addType(String value) { //1
      CodeType t = new CodeType();
      t.setValue(value);
      if (this.type == null)
        this.type = new ArrayList<CodeType>();
      this.type.add(t);
      return this;
    }

    /**
     * @param value {@link #type} (Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).)
     */
    public boolean hasType(String value) { 
      if (this.type == null)
        return false;
      for (CodeType v : this.type)
        if (v.equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #instance} (Indicates whether this operation can be invoked on a particular instance of one of the given types.). This is the underlying object with id, value and extensions. The accessor "getInstance" gives direct access to the value
     */
    public BooleanType getInstanceElement() { 
      if (this.instance == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.instance");
        else if (Configuration.doAutoCreate())
          this.instance = new BooleanType(); // bb
      return this.instance;
    }

    public boolean hasInstanceElement() { 
      return this.instance != null && !this.instance.isEmpty();
    }

    public boolean hasInstance() { 
      return this.instance != null && !this.instance.isEmpty();
    }

    /**
     * @param value {@link #instance} (Indicates whether this operation can be invoked on a particular instance of one of the given types.). This is the underlying object with id, value and extensions. The accessor "getInstance" gives direct access to the value
     */
    public OperationDefinition setInstanceElement(BooleanType value) { 
      this.instance = value;
      return this;
    }

    /**
     * @return Indicates whether this operation can be invoked on a particular instance of one of the given types.
     */
    public boolean getInstance() { 
      return this.instance == null ? false : this.instance.getValue();
    }

    /**
     * @param value Indicates whether this operation can be invoked on a particular instance of one of the given types.
     */
    public OperationDefinition setInstance(boolean value) { 
        if (this.instance == null)
          this.instance = new BooleanType();
        this.instance.setValue(value);
      return this;
    }

    /**
     * @return {@link #parameter} (The parameters for the operation/query.)
     */
    public List<OperationDefinitionParameterComponent> getParameter() { 
      if (this.parameter == null)
        this.parameter = new ArrayList<OperationDefinitionParameterComponent>();
      return this.parameter;
    }

    public boolean hasParameter() { 
      if (this.parameter == null)
        return false;
      for (OperationDefinitionParameterComponent item : this.parameter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #parameter} (The parameters for the operation/query.)
     */
    // syntactic sugar
    public OperationDefinitionParameterComponent addParameter() { //3
      OperationDefinitionParameterComponent t = new OperationDefinitionParameterComponent();
      if (this.parameter == null)
        this.parameter = new ArrayList<OperationDefinitionParameterComponent>();
      this.parameter.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "uri", "The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("title", "string", "A free text natural language name identifying the Profile.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("publisher", "string", "Details of the individual or organization who accepts responsibility for publishing the profile.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("telecom", "ContactPoint", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("description", "string", "A free text natural language description of the profile and its use.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("code", "Coding", "A set of terms from external terminologies that may be used to assist with indexing and searching of templates.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("status", "code", "The status of the profile.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date that this version of the profile was published.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("kind", "code", "Whether this is operation or named query.", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("name", "code", "The name used to invoke the operation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("notes", "string", "Additional information about how to use this operation or named query.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("base", "Reference(OperationDefinition)", "Indicates that this operation definition is a constraining profile on the base.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("system", "boolean", "Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).", 0, java.lang.Integer.MAX_VALUE, system));
        childrenList.add(new Property("type", "code", "Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("instance", "boolean", "Indicates whether this operation can be invoked on a particular instance of one of the given types.", 0, java.lang.Integer.MAX_VALUE, instance));
        childrenList.add(new Property("parameter", "", "The parameters for the operation/query.", 0, java.lang.Integer.MAX_VALUE, parameter));
      }

      public OperationDefinition copy() {
        OperationDefinition dst = new OperationDefinition();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        dst.title = title == null ? null : title.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.kind = kind == null ? null : kind.copy();
        dst.name = name == null ? null : name.copy();
        dst.notes = notes == null ? null : notes.copy();
        dst.base = base == null ? null : base.copy();
        dst.system = system == null ? null : system.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeType>();
          for (CodeType i : type)
            dst.type.add(i.copy());
        };
        dst.instance = instance == null ? null : instance.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<OperationDefinitionParameterComponent>();
          for (OperationDefinitionParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        return dst;
      }

      protected OperationDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationDefinition))
          return false;
        OperationDefinition o = (OperationDefinition) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true) && compareDeep(title, o.title, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(telecom, o.telecom, true) && compareDeep(description, o.description, true)
           && compareDeep(code, o.code, true) && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true)
           && compareDeep(date, o.date, true) && compareDeep(kind, o.kind, true) && compareDeep(name, o.name, true)
           && compareDeep(notes, o.notes, true) && compareDeep(base, o.base, true) && compareDeep(system, o.system, true)
           && compareDeep(type, o.type, true) && compareDeep(instance, o.instance, true) && compareDeep(parameter, o.parameter, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationDefinition))
          return false;
        OperationDefinition o = (OperationDefinition) other;
        return compareValues(identifier, o.identifier, true) && compareValues(version, o.version, true) && compareValues(title, o.title, true)
           && compareValues(publisher, o.publisher, true) && compareValues(description, o.description, true) && compareValues(status, o.status, true)
           && compareValues(experimental, o.experimental, true) && compareValues(date, o.date, true) && compareValues(kind, o.kind, true)
           && compareValues(name, o.name, true) && compareValues(notes, o.notes, true) && compareValues(system, o.system, true)
           && compareValues(type, o.type, true) && compareValues(instance, o.instance, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (title == null || title.isEmpty()) && (publisher == null || publisher.isEmpty()) && (telecom == null || telecom.isEmpty())
           && (description == null || description.isEmpty()) && (code == null || code.isEmpty()) && (status == null || status.isEmpty())
           && (experimental == null || experimental.isEmpty()) && (date == null || date.isEmpty()) && (kind == null || kind.isEmpty())
           && (name == null || name.isEmpty()) && (notes == null || notes.isEmpty()) && (base == null || base.isEmpty())
           && (system == null || system.isEmpty()) && (type == null || type.isEmpty()) && (instance == null || instance.isEmpty())
           && (parameter == null || parameter.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.OperationDefinition;
   }

  @SearchParamDefinition(name="status", path="OperationDefinition.status", description="draft | active | retired", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="code", path="OperationDefinition.code", description="Assist with indexing and finding", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="date", path="OperationDefinition.date", description="Date for this version of the operation definition", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="type", path="OperationDefinition.type", description="Invoke at resource level for these type", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="kind", path="OperationDefinition.kind", description="operation | query", type="token" )
  public static final String SP_KIND = "kind";
  @SearchParamDefinition(name="version", path="OperationDefinition.version", description="Logical id for this version of the operation definition", type="token" )
  public static final String SP_VERSION = "version";
  @SearchParamDefinition(name="publisher", path="OperationDefinition.publisher", description="Name of the publisher (Organization or individual)", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="title", path="OperationDefinition.title", description="Informal name for this profile", type="string" )
  public static final String SP_TITLE = "title";
  @SearchParamDefinition(name="system", path="OperationDefinition.system", description="Invoke at the system level?", type="token" )
  public static final String SP_SYSTEM = "system";
  @SearchParamDefinition(name="name", path="OperationDefinition.name", description="Name used to invoke the operation", type="token" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="base", path="OperationDefinition.base", description="Marks this as a profile of the base", type="reference" )
  public static final String SP_BASE = "base";
  @SearchParamDefinition(name="instance", path="OperationDefinition.instance", description="Invoke on an instance?", type="token" )
  public static final String SP_INSTANCE = "instance";
  @SearchParamDefinition(name="identifier", path="OperationDefinition.identifier", description="Logical id to reference this operation definition", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="profile", path="OperationDefinition.parameter.profile", description="Profile on the type", type="reference" )
  public static final String SP_PROFILE = "profile";

}

