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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

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
 * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
 */
@ResourceDef(name="OperationDefinition", profile="http://hl7.org/fhir/Profile/OperationDefinition")
@ChildOrder(names={"url", "version", "name", "status", "kind", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "purpose", "idempotent", "code", "comment", "base", "resource", "system", "type", "instance", "parameter", "overload"})
public class OperationDefinition extends MetadataResource {

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
         * added to help the parsers with the generic types
         */
        NULL;
        public static OperationKind fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("operation".equals(codeString))
          return OPERATION;
        if ("query".equals(codeString))
          return QUERY;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown OperationKind code '"+codeString+"'");
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
            case OPERATION: return "http://hl7.org/fhir/operation-kind";
            case QUERY: return "http://hl7.org/fhir/operation-kind";
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
            case OPERATION: return "Operation";
            case QUERY: return "Query";
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
        public Enumeration<OperationKind> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<OperationKind>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("operation".equals(codeString))
          return new Enumeration<OperationKind>(this, OperationKind.OPERATION);
        if ("query".equals(codeString))
          return new Enumeration<OperationKind>(this, OperationKind.QUERY);
        throw new FHIRException("Unknown OperationKind code '"+codeString+"'");
        }
    public String toCode(OperationKind code) {
      if (code == OperationKind.OPERATION)
        return "operation";
      if (code == OperationKind.QUERY)
        return "query";
      return "?";
      }
    public String toSystem(OperationKind code) {
      return code.getSystem();
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
         * added to help the parsers with the generic types
         */
        NULL;
        public static OperationParameterUse fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in".equals(codeString))
          return IN;
        if ("out".equals(codeString))
          return OUT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown OperationParameterUse code '"+codeString+"'");
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
            case IN: return "http://hl7.org/fhir/operation-parameter-use";
            case OUT: return "http://hl7.org/fhir/operation-parameter-use";
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
            case IN: return "In";
            case OUT: return "Out";
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
        public Enumeration<OperationParameterUse> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<OperationParameterUse>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("in".equals(codeString))
          return new Enumeration<OperationParameterUse>(this, OperationParameterUse.IN);
        if ("out".equals(codeString))
          return new Enumeration<OperationParameterUse>(this, OperationParameterUse.OUT);
        throw new FHIRException("Unknown OperationParameterUse code '"+codeString+"'");
        }
    public String toCode(OperationParameterUse code) {
      if (code == OperationParameterUse.IN)
        return "in";
      if (code == OperationParameterUse.OUT)
        return "out";
      return "?";
      }
    public String toSystem(OperationParameterUse code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class OperationDefinitionParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of used to identify the parameter.
         */
        @Child(name = "name", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name in Parameters.parameter.name or in URL", formalDefinition="The name of used to identify the parameter." )
        protected CodeType name;

        /**
         * Whether this is an input or an output parameter.
         */
        @Child(name = "use", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="in | out", formalDefinition="Whether this is an input or an output parameter." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/operation-parameter-use")
        protected Enumeration<OperationParameterUse> use;

        /**
         * The minimum number of times this parameter SHALL appear in the request or response.
         */
        @Child(name = "min", type = {IntegerType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Minimum Cardinality", formalDefinition="The minimum number of times this parameter SHALL appear in the request or response." )
        protected IntegerType min;

        /**
         * The maximum number of times this element is permitted to appear in the request or response.
         */
        @Child(name = "max", type = {StringType.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Maximum Cardinality (a number or *)", formalDefinition="The maximum number of times this element is permitted to appear in the request or response." )
        protected StringType max;

        /**
         * Describes the meaning or use of this parameter.
         */
        @Child(name = "documentation", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of meaning/use", formalDefinition="Describes the meaning or use of this parameter." )
        protected StringType documentation;

        /**
         * The type for this parameter.
         */
        @Child(name = "type", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What type this parameter has", formalDefinition="The type for this parameter." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/all-types")
        protected CodeType type;

        /**
         * How the parameter is understood as a search parameter. This is only used if the parameter type is 'string'.
         */
        @Child(name = "searchType", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="number | date | string | token | reference | composite | quantity | uri", formalDefinition="How the parameter is understood as a search parameter. This is only used if the parameter type is 'string'." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/search-param-type")
        protected Enumeration<SearchParamType> searchType;

        /**
         * A profile the specifies the rules that this parameter must conform to.
         */
        @Child(name = "profile", type = {StructureDefinition.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Profile on the type", formalDefinition="A profile the specifies the rules that this parameter must conform to." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (A profile the specifies the rules that this parameter must conform to.)
         */
        protected StructureDefinition profileTarget;

        /**
         * Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).
         */
        @Child(name = "binding", type = {}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="ValueSet details if this is coded", formalDefinition="Binds to a value set if this parameter is coded (code, Coding, CodeableConcept)." )
        protected OperationDefinitionParameterBindingComponent binding;

        /**
         * The parts of a nested Parameter.
         */
        @Child(name = "part", type = {OperationDefinitionParameterComponent.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Parts of a nested Parameter", formalDefinition="The parts of a nested Parameter." )
        protected List<OperationDefinitionParameterComponent> part;

        private static final long serialVersionUID = -885506257L;

    /**
     * Constructor
     */
      public OperationDefinitionParameterComponent() {
        super();
      }

    /**
     * Constructor
     */
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
          return this.min == null || this.min.isEmpty() ? 0 : this.min.getValue();
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
         * @return {@link #searchType} (How the parameter is understood as a search parameter. This is only used if the parameter type is 'string'.). This is the underlying object with id, value and extensions. The accessor "getSearchType" gives direct access to the value
         */
        public Enumeration<SearchParamType> getSearchTypeElement() { 
          if (this.searchType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.searchType");
            else if (Configuration.doAutoCreate())
              this.searchType = new Enumeration<SearchParamType>(new SearchParamTypeEnumFactory()); // bb
          return this.searchType;
        }

        public boolean hasSearchTypeElement() { 
          return this.searchType != null && !this.searchType.isEmpty();
        }

        public boolean hasSearchType() { 
          return this.searchType != null && !this.searchType.isEmpty();
        }

        /**
         * @param value {@link #searchType} (How the parameter is understood as a search parameter. This is only used if the parameter type is 'string'.). This is the underlying object with id, value and extensions. The accessor "getSearchType" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setSearchTypeElement(Enumeration<SearchParamType> value) { 
          this.searchType = value;
          return this;
        }

        /**
         * @return How the parameter is understood as a search parameter. This is only used if the parameter type is 'string'.
         */
        public SearchParamType getSearchType() { 
          return this.searchType == null ? null : this.searchType.getValue();
        }

        /**
         * @param value How the parameter is understood as a search parameter. This is only used if the parameter type is 'string'.
         */
        public OperationDefinitionParameterComponent setSearchType(SearchParamType value) { 
          if (value == null)
            this.searchType = null;
          else {
            if (this.searchType == null)
              this.searchType = new Enumeration<SearchParamType>(new SearchParamTypeEnumFactory());
            this.searchType.setValue(value);
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
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A profile the specifies the rules that this parameter must conform to.)
         */
        public OperationDefinitionParameterComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        /**
         * @return {@link #binding} (Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).)
         */
        public OperationDefinitionParameterBindingComponent getBinding() { 
          if (this.binding == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.binding");
            else if (Configuration.doAutoCreate())
              this.binding = new OperationDefinitionParameterBindingComponent(); // cc
          return this.binding;
        }

        public boolean hasBinding() { 
          return this.binding != null && !this.binding.isEmpty();
        }

        /**
         * @param value {@link #binding} (Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).)
         */
        public OperationDefinitionParameterComponent setBinding(OperationDefinitionParameterBindingComponent value) { 
          this.binding = value;
          return this;
        }

        /**
         * @return {@link #part} (The parts of a nested Parameter.)
         */
        public List<OperationDefinitionParameterComponent> getPart() { 
          if (this.part == null)
            this.part = new ArrayList<OperationDefinitionParameterComponent>();
          return this.part;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public OperationDefinitionParameterComponent setPart(List<OperationDefinitionParameterComponent> thePart) { 
          this.part = thePart;
          return this;
        }

        public boolean hasPart() { 
          if (this.part == null)
            return false;
          for (OperationDefinitionParameterComponent item : this.part)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public OperationDefinitionParameterComponent addPart() { //3
          OperationDefinitionParameterComponent t = new OperationDefinitionParameterComponent();
          if (this.part == null)
            this.part = new ArrayList<OperationDefinitionParameterComponent>();
          this.part.add(t);
          return t;
        }

        public OperationDefinitionParameterComponent addPart(OperationDefinitionParameterComponent t) { //3
          if (t == null)
            return this;
          if (this.part == null)
            this.part = new ArrayList<OperationDefinitionParameterComponent>();
          this.part.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #part}, creating it if it does not already exist
         */
        public OperationDefinitionParameterComponent getPartFirstRep() { 
          if (getPart().isEmpty()) {
            addPart();
          }
          return getPart().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "code", "The name of used to identify the parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("use", "code", "Whether this is an input or an output parameter.", 0, java.lang.Integer.MAX_VALUE, use));
          childrenList.add(new Property("min", "integer", "The minimum number of times this parameter SHALL appear in the request or response.", 0, java.lang.Integer.MAX_VALUE, min));
          childrenList.add(new Property("max", "string", "The maximum number of times this element is permitted to appear in the request or response.", 0, java.lang.Integer.MAX_VALUE, max));
          childrenList.add(new Property("documentation", "string", "Describes the meaning or use of this parameter.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("type", "code", "The type for this parameter.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("searchType", "code", "How the parameter is understood as a search parameter. This is only used if the parameter type is 'string'.", 0, java.lang.Integer.MAX_VALUE, searchType));
          childrenList.add(new Property("profile", "Reference(StructureDefinition)", "A profile the specifies the rules that this parameter must conform to.", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("binding", "", "Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).", 0, java.lang.Integer.MAX_VALUE, binding));
          childrenList.add(new Property("part", "@OperationDefinition.parameter", "The parts of a nested Parameter.", 0, java.lang.Integer.MAX_VALUE, part));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // CodeType
        case 116103: /*use*/ return this.use == null ? new Base[0] : new Base[] {this.use}; // Enumeration<OperationParameterUse>
        case 108114: /*min*/ return this.min == null ? new Base[0] : new Base[] {this.min}; // IntegerType
        case 107876: /*max*/ return this.max == null ? new Base[0] : new Base[] {this.max}; // StringType
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case -710454014: /*searchType*/ return this.searchType == null ? new Base[0] : new Base[] {this.searchType}; // Enumeration<SearchParamType>
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // Reference
        case -108220795: /*binding*/ return this.binding == null ? new Base[0] : new Base[] {this.binding}; // OperationDefinitionParameterBindingComponent
        case 3433459: /*part*/ return this.part == null ? new Base[0] : this.part.toArray(new Base[this.part.size()]); // OperationDefinitionParameterComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToCode(value); // CodeType
          return value;
        case 116103: // use
          value = new OperationParameterUseEnumFactory().fromType(castToCode(value));
          this.use = (Enumeration) value; // Enumeration<OperationParameterUse>
          return value;
        case 108114: // min
          this.min = castToInteger(value); // IntegerType
          return value;
        case 107876: // max
          this.max = castToString(value); // StringType
          return value;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          return value;
        case -710454014: // searchType
          value = new SearchParamTypeEnumFactory().fromType(castToCode(value));
          this.searchType = (Enumeration) value; // Enumeration<SearchParamType>
          return value;
        case -309425751: // profile
          this.profile = castToReference(value); // Reference
          return value;
        case -108220795: // binding
          this.binding = (OperationDefinitionParameterBindingComponent) value; // OperationDefinitionParameterBindingComponent
          return value;
        case 3433459: // part
          this.getPart().add((OperationDefinitionParameterComponent) value); // OperationDefinitionParameterComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToCode(value); // CodeType
        } else if (name.equals("use")) {
          value = new OperationParameterUseEnumFactory().fromType(castToCode(value));
          this.use = (Enumeration) value; // Enumeration<OperationParameterUse>
        } else if (name.equals("min")) {
          this.min = castToInteger(value); // IntegerType
        } else if (name.equals("max")) {
          this.max = castToString(value); // StringType
        } else if (name.equals("documentation")) {
          this.documentation = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToCode(value); // CodeType
        } else if (name.equals("searchType")) {
          value = new SearchParamTypeEnumFactory().fromType(castToCode(value));
          this.searchType = (Enumeration) value; // Enumeration<SearchParamType>
        } else if (name.equals("profile")) {
          this.profile = castToReference(value); // Reference
        } else if (name.equals("binding")) {
          this.binding = (OperationDefinitionParameterBindingComponent) value; // OperationDefinitionParameterBindingComponent
        } else if (name.equals("part")) {
          this.getPart().add((OperationDefinitionParameterComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 116103:  return getUseElement();
        case 108114:  return getMinElement();
        case 107876:  return getMaxElement();
        case 1587405498:  return getDocumentationElement();
        case 3575610:  return getTypeElement();
        case -710454014:  return getSearchTypeElement();
        case -309425751:  return getProfile(); 
        case -108220795:  return getBinding(); 
        case 3433459:  return addPart(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"code"};
        case 116103: /*use*/ return new String[] {"code"};
        case 108114: /*min*/ return new String[] {"integer"};
        case 107876: /*max*/ return new String[] {"string"};
        case 1587405498: /*documentation*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"code"};
        case -710454014: /*searchType*/ return new String[] {"code"};
        case -309425751: /*profile*/ return new String[] {"Reference"};
        case -108220795: /*binding*/ return new String[] {};
        case 3433459: /*part*/ return new String[] {"@OperationDefinition.parameter"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.name");
        }
        else if (name.equals("use")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.use");
        }
        else if (name.equals("min")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.min");
        }
        else if (name.equals("max")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.max");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.documentation");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.type");
        }
        else if (name.equals("searchType")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.searchType");
        }
        else if (name.equals("profile")) {
          this.profile = new Reference();
          return this.profile;
        }
        else if (name.equals("binding")) {
          this.binding = new OperationDefinitionParameterBindingComponent();
          return this.binding;
        }
        else if (name.equals("part")) {
          return addPart();
        }
        else
          return super.addChild(name);
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
        dst.searchType = searchType == null ? null : searchType.copy();
        dst.profile = profile == null ? null : profile.copy();
        dst.binding = binding == null ? null : binding.copy();
        if (part != null) {
          dst.part = new ArrayList<OperationDefinitionParameterComponent>();
          for (OperationDefinitionParameterComponent i : part)
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
           && compareDeep(searchType, o.searchType, true) && compareDeep(profile, o.profile, true) && compareDeep(binding, o.binding, true)
           && compareDeep(part, o.part, true);
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
           && compareValues(searchType, o.searchType, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, use, min, max, documentation
          , type, searchType, profile, binding, part);
      }

  public String fhirType() {
    return "OperationDefinition.parameter";

  }

  }

    @Block()
    public static class OperationDefinitionParameterBindingComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.
         */
        @Child(name = "strength", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="required | extensible | preferred | example", formalDefinition="Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/binding-strength")
        protected Enumeration<BindingStrength> strength;

        /**
         * Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.
         */
        @Child(name = "valueSet", type = {UriType.class, ValueSet.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Source of value set", formalDefinition="Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used." )
        protected Type valueSet;

        private static final long serialVersionUID = 857140521L;

    /**
     * Constructor
     */
      public OperationDefinitionParameterBindingComponent() {
        super();
      }

    /**
     * Constructor
     */
      public OperationDefinitionParameterBindingComponent(Enumeration<BindingStrength> strength, Type valueSet) {
        super();
        this.strength = strength;
        this.valueSet = valueSet;
      }

        /**
         * @return {@link #strength} (Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.). This is the underlying object with id, value and extensions. The accessor "getStrength" gives direct access to the value
         */
        public Enumeration<BindingStrength> getStrengthElement() { 
          if (this.strength == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterBindingComponent.strength");
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
        public OperationDefinitionParameterBindingComponent setStrengthElement(Enumeration<BindingStrength> value) { 
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
        public OperationDefinitionParameterBindingComponent setStrength(BindingStrength value) { 
            if (this.strength == null)
              this.strength = new Enumeration<BindingStrength>(new BindingStrengthEnumFactory());
            this.strength.setValue(value);
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
        public UriType getValueSetUriType() throws FHIRException { 
          if (!(this.valueSet instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.valueSet.getClass().getName()+" was encountered");
          return (UriType) this.valueSet;
        }

        public boolean hasValueSetUriType() { 
          return this.valueSet instanceof UriType;
        }

        /**
         * @return {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.)
         */
        public Reference getValueSetReference() throws FHIRException { 
          if (!(this.valueSet instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.valueSet.getClass().getName()+" was encountered");
          return (Reference) this.valueSet;
        }

        public boolean hasValueSetReference() { 
          return this.valueSet instanceof Reference;
        }

        public boolean hasValueSet() { 
          return this.valueSet != null && !this.valueSet.isEmpty();
        }

        /**
         * @param value {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.)
         */
        public OperationDefinitionParameterBindingComponent setValueSet(Type value) { 
          this.valueSet = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("strength", "code", "Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.", 0, java.lang.Integer.MAX_VALUE, strength));
          childrenList.add(new Property("valueSet[x]", "uri|Reference(ValueSet)", "Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.", 0, java.lang.Integer.MAX_VALUE, valueSet));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1791316033: /*strength*/ return this.strength == null ? new Base[0] : new Base[] {this.strength}; // Enumeration<BindingStrength>
        case -1410174671: /*valueSet*/ return this.valueSet == null ? new Base[0] : new Base[] {this.valueSet}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1791316033: // strength
          value = new BindingStrengthEnumFactory().fromType(castToCode(value));
          this.strength = (Enumeration) value; // Enumeration<BindingStrength>
          return value;
        case -1410174671: // valueSet
          this.valueSet = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("strength")) {
          value = new BindingStrengthEnumFactory().fromType(castToCode(value));
          this.strength = (Enumeration) value; // Enumeration<BindingStrength>
        } else if (name.equals("valueSet[x]")) {
          this.valueSet = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1791316033:  return getStrengthElement();
        case -1438410321:  return getValueSet(); 
        case -1410174671:  return getValueSet(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1791316033: /*strength*/ return new String[] {"code"};
        case -1410174671: /*valueSet*/ return new String[] {"uri", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("strength")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.strength");
        }
        else if (name.equals("valueSetUri")) {
          this.valueSet = new UriType();
          return this.valueSet;
        }
        else if (name.equals("valueSetReference")) {
          this.valueSet = new Reference();
          return this.valueSet;
        }
        else
          return super.addChild(name);
      }

      public OperationDefinitionParameterBindingComponent copy() {
        OperationDefinitionParameterBindingComponent dst = new OperationDefinitionParameterBindingComponent();
        copyValues(dst);
        dst.strength = strength == null ? null : strength.copy();
        dst.valueSet = valueSet == null ? null : valueSet.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationDefinitionParameterBindingComponent))
          return false;
        OperationDefinitionParameterBindingComponent o = (OperationDefinitionParameterBindingComponent) other;
        return compareDeep(strength, o.strength, true) && compareDeep(valueSet, o.valueSet, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationDefinitionParameterBindingComponent))
          return false;
        OperationDefinitionParameterBindingComponent o = (OperationDefinitionParameterBindingComponent) other;
        return compareValues(strength, o.strength, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(strength, valueSet);
      }

  public String fhirType() {
    return "OperationDefinition.parameter.binding";

  }

  }

    @Block()
    public static class OperationDefinitionOverloadComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Name of parameter to include in overload.
         */
        @Child(name = "parameterName", type = {StringType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Name of parameter to include in overload", formalDefinition="Name of parameter to include in overload." )
        protected List<StringType> parameterName;

        /**
         * Comments to go on overload.
         */
        @Child(name = "comment", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Comments to go on overload", formalDefinition="Comments to go on overload." )
        protected StringType comment;

        private static final long serialVersionUID = -907948545L;

    /**
     * Constructor
     */
      public OperationDefinitionOverloadComponent() {
        super();
      }

        /**
         * @return {@link #parameterName} (Name of parameter to include in overload.)
         */
        public List<StringType> getParameterName() { 
          if (this.parameterName == null)
            this.parameterName = new ArrayList<StringType>();
          return this.parameterName;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public OperationDefinitionOverloadComponent setParameterName(List<StringType> theParameterName) { 
          this.parameterName = theParameterName;
          return this;
        }

        public boolean hasParameterName() { 
          if (this.parameterName == null)
            return false;
          for (StringType item : this.parameterName)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #parameterName} (Name of parameter to include in overload.)
         */
        public StringType addParameterNameElement() {//2 
          StringType t = new StringType();
          if (this.parameterName == null)
            this.parameterName = new ArrayList<StringType>();
          this.parameterName.add(t);
          return t;
        }

        /**
         * @param value {@link #parameterName} (Name of parameter to include in overload.)
         */
        public OperationDefinitionOverloadComponent addParameterName(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.parameterName == null)
            this.parameterName = new ArrayList<StringType>();
          this.parameterName.add(t);
          return this;
        }

        /**
         * @param value {@link #parameterName} (Name of parameter to include in overload.)
         */
        public boolean hasParameterName(String value) { 
          if (this.parameterName == null)
            return false;
          for (StringType v : this.parameterName)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #comment} (Comments to go on overload.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public StringType getCommentElement() { 
          if (this.comment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionOverloadComponent.comment");
            else if (Configuration.doAutoCreate())
              this.comment = new StringType(); // bb
          return this.comment;
        }

        public boolean hasCommentElement() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        public boolean hasComment() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        /**
         * @param value {@link #comment} (Comments to go on overload.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public OperationDefinitionOverloadComponent setCommentElement(StringType value) { 
          this.comment = value;
          return this;
        }

        /**
         * @return Comments to go on overload.
         */
        public String getComment() { 
          return this.comment == null ? null : this.comment.getValue();
        }

        /**
         * @param value Comments to go on overload.
         */
        public OperationDefinitionOverloadComponent setComment(String value) { 
          if (Utilities.noString(value))
            this.comment = null;
          else {
            if (this.comment == null)
              this.comment = new StringType();
            this.comment.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("parameterName", "string", "Name of parameter to include in overload.", 0, java.lang.Integer.MAX_VALUE, parameterName));
          childrenList.add(new Property("comment", "string", "Comments to go on overload.", 0, java.lang.Integer.MAX_VALUE, comment));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -379607596: /*parameterName*/ return this.parameterName == null ? new Base[0] : this.parameterName.toArray(new Base[this.parameterName.size()]); // StringType
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -379607596: // parameterName
          this.getParameterName().add(castToString(value)); // StringType
          return value;
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("parameterName")) {
          this.getParameterName().add(castToString(value));
        } else if (name.equals("comment")) {
          this.comment = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -379607596:  return addParameterNameElement();
        case 950398559:  return getCommentElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -379607596: /*parameterName*/ return new String[] {"string"};
        case 950398559: /*comment*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("parameterName")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.parameterName");
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.comment");
        }
        else
          return super.addChild(name);
      }

      public OperationDefinitionOverloadComponent copy() {
        OperationDefinitionOverloadComponent dst = new OperationDefinitionOverloadComponent();
        copyValues(dst);
        if (parameterName != null) {
          dst.parameterName = new ArrayList<StringType>();
          for (StringType i : parameterName)
            dst.parameterName.add(i.copy());
        };
        dst.comment = comment == null ? null : comment.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationDefinitionOverloadComponent))
          return false;
        OperationDefinitionOverloadComponent o = (OperationDefinitionOverloadComponent) other;
        return compareDeep(parameterName, o.parameterName, true) && compareDeep(comment, o.comment, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationDefinitionOverloadComponent))
          return false;
        OperationDefinitionOverloadComponent o = (OperationDefinitionOverloadComponent) other;
        return compareValues(parameterName, o.parameterName, true) && compareValues(comment, o.comment, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(parameterName, comment);
      }

  public String fhirType() {
    return "OperationDefinition.overload";

  }

  }

    /**
     * Whether this is an operation or a named query.
     */
    @Child(name = "kind", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="operation | query", formalDefinition="Whether this is an operation or a named query." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/operation-kind")
    protected Enumeration<OperationKind> kind;

    /**
     * Explaination of why this operation definition is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this operation definition is defined", formalDefinition="Explaination of why this operation definition is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.
     */
    @Child(name = "idempotent", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether content is unchanged by the operation", formalDefinition="Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST." )
    protected BooleanType idempotent;

    /**
     * The name used to invoke the operation.
     */
    @Child(name = "code", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name used to invoke the operation", formalDefinition="The name used to invoke the operation." )
    protected CodeType code;

    /**
     * Additional information about how to use this operation or named query.
     */
    @Child(name = "comment", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Additional information about use", formalDefinition="Additional information about how to use this operation or named query." )
    protected StringType comment;

    /**
     * Indicates that this operation definition is a constraining profile on the base.
     */
    @Child(name = "base", type = {OperationDefinition.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Marks this as a profile of the base", formalDefinition="Indicates that this operation definition is a constraining profile on the base." )
    protected Reference base;

    /**
     * The actual object that is the target of the reference (Indicates that this operation definition is a constraining profile on the base.)
     */
    protected OperationDefinition baseTarget;

    /**
     * The types on which this operation can be executed.
     */
    @Child(name = "resource", type = {CodeType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Types this operation applies to", formalDefinition="The types on which this operation can be executed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
    protected List<CodeType> resource;

    /**
     * Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).
     */
    @Child(name = "system", type = {BooleanType.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Invoke at the system level?", formalDefinition="Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context)." )
    protected BooleanType system;

    /**
     * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a specific resource id for the context).
     */
    @Child(name = "type", type = {BooleanType.class}, order=8, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Invole at the type level?", formalDefinition="Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a specific resource id for the context)." )
    protected BooleanType type;

    /**
     * Indicates whether this operation can be invoked on a particular instance of one of the given types.
     */
    @Child(name = "instance", type = {BooleanType.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Invoke on an instance?", formalDefinition="Indicates whether this operation can be invoked on a particular instance of one of the given types." )
    protected BooleanType instance;

    /**
     * The parameters for the operation/query.
     */
    @Child(name = "parameter", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Parameters for the operation/query", formalDefinition="The parameters for the operation/query." )
    protected List<OperationDefinitionParameterComponent> parameter;

    /**
     * Defines an appropriate combination of parameters to use when invoking this operation, to help code generators when generating overloaded parameter sets for this operation.
     */
    @Child(name = "overload", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Define overloaded variants for when  generating code", formalDefinition="Defines an appropriate combination of parameters to use when invoking this operation, to help code generators when generating overloaded parameter sets for this operation." )
    protected List<OperationDefinitionOverloadComponent> overload;

    private static final long serialVersionUID = 1292377899L;

  /**
   * Constructor
   */
    public OperationDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public OperationDefinition(StringType name, Enumeration<PublicationStatus> status, Enumeration<OperationKind> kind, CodeType code, BooleanType system, BooleanType type, BooleanType instance) {
      super();
      this.name = name;
      this.status = status;
      this.kind = kind;
      this.code = code;
      this.system = system;
      this.type = type;
      this.instance = instance;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published. The URL SHOULD include the major version of the operation definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.url");
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
     * @param value {@link #url} (An absolute URI that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published. The URL SHOULD include the major version of the operation definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public OperationDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published. The URL SHOULD include the major version of the operation definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published. The URL SHOULD include the major version of the operation definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public OperationDefinition setUrl(String value) { 
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
     * @return {@link #version} (The identifier that is used to identify this version of the operation definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the operation definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
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
     * @param value {@link #version} (The identifier that is used to identify this version of the operation definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the operation definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public OperationDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the operation definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the operation definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the operation definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the operation definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
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
     * @return {@link #name} (A natural language name identifying the operation definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.name");
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
     * @param value {@link #name} (A natural language name identifying the operation definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public OperationDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the operation definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the operation definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public OperationDefinition setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of this operation definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.status");
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
     * @param value {@link #status} (The status of this operation definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public OperationDefinition setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this operation definition. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this operation definition. Enables tracking the life-cycle of the content.
     */
    public OperationDefinition setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #kind} (Whether this is an operation or a named query.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
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
     * @param value {@link #kind} (Whether this is an operation or a named query.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public OperationDefinition setKindElement(Enumeration<OperationKind> value) { 
      this.kind = value;
      return this;
    }

    /**
     * @return Whether this is an operation or a named query.
     */
    public OperationKind getKind() { 
      return this.kind == null ? null : this.kind.getValue();
    }

    /**
     * @param value Whether this is an operation or a named query.
     */
    public OperationDefinition setKind(OperationKind value) { 
        if (this.kind == null)
          this.kind = new Enumeration<OperationKind>(new OperationKindEnumFactory());
        this.kind.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this operation definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
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
     * @param value {@link #experimental} (A boolean value to indicate that this operation definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public OperationDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this operation definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this operation definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public OperationDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the operation definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the operation definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date  (and optionally time) when the operation definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the operation definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public OperationDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the operation definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the operation definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the operation definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the operation definition changes.
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
     * @return {@link #publisher} (The name of the individual or organization that published the operation definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
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
     * @param value {@link #publisher} (The name of the individual or organization that published the operation definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public OperationDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the operation definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the operation definition.
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
    public OperationDefinition setContact(List<ContactDetail> theContact) { 
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

    public OperationDefinition addContact(ContactDetail t) { //3
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
     * @return {@link #description} (A free text natural language description of the operation definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.description");
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
     * @param value {@link #description} (A free text natural language description of the operation definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public OperationDefinition setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the operation definition from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the operation definition from a consumer's perspective.
     */
    public OperationDefinition setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate operation definition instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OperationDefinition setUseContext(List<UsageContext> theUseContext) { 
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

    public OperationDefinition addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A legal or geographic region in which the operation definition is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OperationDefinition setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public OperationDefinition addJurisdiction(CodeableConcept t) { //3
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
     * @return {@link #purpose} (Explaination of why this operation definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.purpose");
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
     * @param value {@link #purpose} (Explaination of why this operation definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public OperationDefinition setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explaination of why this operation definition is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explaination of why this operation definition is needed and why it has been designed as it has.
     */
    public OperationDefinition setPurpose(String value) { 
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
     * @return {@link #idempotent} (Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.). This is the underlying object with id, value and extensions. The accessor "getIdempotent" gives direct access to the value
     */
    public BooleanType getIdempotentElement() { 
      if (this.idempotent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.idempotent");
        else if (Configuration.doAutoCreate())
          this.idempotent = new BooleanType(); // bb
      return this.idempotent;
    }

    public boolean hasIdempotentElement() { 
      return this.idempotent != null && !this.idempotent.isEmpty();
    }

    public boolean hasIdempotent() { 
      return this.idempotent != null && !this.idempotent.isEmpty();
    }

    /**
     * @param value {@link #idempotent} (Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.). This is the underlying object with id, value and extensions. The accessor "getIdempotent" gives direct access to the value
     */
    public OperationDefinition setIdempotentElement(BooleanType value) { 
      this.idempotent = value;
      return this;
    }

    /**
     * @return Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.
     */
    public boolean getIdempotent() { 
      return this.idempotent == null || this.idempotent.isEmpty() ? false : this.idempotent.getValue();
    }

    /**
     * @param value Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.
     */
    public OperationDefinition setIdempotent(boolean value) { 
        if (this.idempotent == null)
          this.idempotent = new BooleanType();
        this.idempotent.setValue(value);
      return this;
    }

    /**
     * @return {@link #code} (The name used to invoke the operation.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public CodeType getCodeElement() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.code");
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
     * @param value {@link #code} (The name used to invoke the operation.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public OperationDefinition setCodeElement(CodeType value) { 
      this.code = value;
      return this;
    }

    /**
     * @return The name used to invoke the operation.
     */
    public String getCode() { 
      return this.code == null ? null : this.code.getValue();
    }

    /**
     * @param value The name used to invoke the operation.
     */
    public OperationDefinition setCode(String value) { 
        if (this.code == null)
          this.code = new CodeType();
        this.code.setValue(value);
      return this;
    }

    /**
     * @return {@link #comment} (Additional information about how to use this operation or named query.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.comment");
        else if (Configuration.doAutoCreate())
          this.comment = new StringType(); // bb
      return this.comment;
    }

    public boolean hasCommentElement() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    public boolean hasComment() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    /**
     * @param value {@link #comment} (Additional information about how to use this operation or named query.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public OperationDefinition setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return Additional information about how to use this operation or named query.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value Additional information about how to use this operation or named query.
     */
    public OperationDefinition setComment(String value) { 
      if (Utilities.noString(value))
        this.comment = null;
      else {
        if (this.comment == null)
          this.comment = new StringType();
        this.comment.setValue(value);
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
     * @return {@link #resource} (The types on which this operation can be executed.)
     */
    public List<CodeType> getResource() { 
      if (this.resource == null)
        this.resource = new ArrayList<CodeType>();
      return this.resource;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OperationDefinition setResource(List<CodeType> theResource) { 
      this.resource = theResource;
      return this;
    }

    public boolean hasResource() { 
      if (this.resource == null)
        return false;
      for (CodeType item : this.resource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #resource} (The types on which this operation can be executed.)
     */
    public CodeType addResourceElement() {//2 
      CodeType t = new CodeType();
      if (this.resource == null)
        this.resource = new ArrayList<CodeType>();
      this.resource.add(t);
      return t;
    }

    /**
     * @param value {@link #resource} (The types on which this operation can be executed.)
     */
    public OperationDefinition addResource(String value) { //1
      CodeType t = new CodeType();
      t.setValue(value);
      if (this.resource == null)
        this.resource = new ArrayList<CodeType>();
      this.resource.add(t);
      return this;
    }

    /**
     * @param value {@link #resource} (The types on which this operation can be executed.)
     */
    public boolean hasResource(String value) { 
      if (this.resource == null)
        return false;
      for (CodeType v : this.resource)
        if (v.equals(value)) // code
          return true;
      return false;
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
      return this.system == null || this.system.isEmpty() ? false : this.system.getValue();
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
     * @return {@link #type} (Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a specific resource id for the context).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public BooleanType getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.type");
        else if (Configuration.doAutoCreate())
          this.type = new BooleanType(); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a specific resource id for the context).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public OperationDefinition setTypeElement(BooleanType value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a specific resource id for the context).
     */
    public boolean getType() { 
      return this.type == null || this.type.isEmpty() ? false : this.type.getValue();
    }

    /**
     * @param value Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a specific resource id for the context).
     */
    public OperationDefinition setType(boolean value) { 
        if (this.type == null)
          this.type = new BooleanType();
        this.type.setValue(value);
      return this;
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
      return this.instance == null || this.instance.isEmpty() ? false : this.instance.getValue();
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OperationDefinition setParameter(List<OperationDefinitionParameterComponent> theParameter) { 
      this.parameter = theParameter;
      return this;
    }

    public boolean hasParameter() { 
      if (this.parameter == null)
        return false;
      for (OperationDefinitionParameterComponent item : this.parameter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public OperationDefinitionParameterComponent addParameter() { //3
      OperationDefinitionParameterComponent t = new OperationDefinitionParameterComponent();
      if (this.parameter == null)
        this.parameter = new ArrayList<OperationDefinitionParameterComponent>();
      this.parameter.add(t);
      return t;
    }

    public OperationDefinition addParameter(OperationDefinitionParameterComponent t) { //3
      if (t == null)
        return this;
      if (this.parameter == null)
        this.parameter = new ArrayList<OperationDefinitionParameterComponent>();
      this.parameter.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #parameter}, creating it if it does not already exist
     */
    public OperationDefinitionParameterComponent getParameterFirstRep() { 
      if (getParameter().isEmpty()) {
        addParameter();
      }
      return getParameter().get(0);
    }

    /**
     * @return {@link #overload} (Defines an appropriate combination of parameters to use when invoking this operation, to help code generators when generating overloaded parameter sets for this operation.)
     */
    public List<OperationDefinitionOverloadComponent> getOverload() { 
      if (this.overload == null)
        this.overload = new ArrayList<OperationDefinitionOverloadComponent>();
      return this.overload;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OperationDefinition setOverload(List<OperationDefinitionOverloadComponent> theOverload) { 
      this.overload = theOverload;
      return this;
    }

    public boolean hasOverload() { 
      if (this.overload == null)
        return false;
      for (OperationDefinitionOverloadComponent item : this.overload)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public OperationDefinitionOverloadComponent addOverload() { //3
      OperationDefinitionOverloadComponent t = new OperationDefinitionOverloadComponent();
      if (this.overload == null)
        this.overload = new ArrayList<OperationDefinitionOverloadComponent>();
      this.overload.add(t);
      return t;
    }

    public OperationDefinition addOverload(OperationDefinitionOverloadComponent t) { //3
      if (t == null)
        return this;
      if (this.overload == null)
        this.overload = new ArrayList<OperationDefinitionOverloadComponent>();
      this.overload.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #overload}, creating it if it does not already exist
     */
    public OperationDefinitionOverloadComponent getOverloadFirstRep() { 
      if (getOverload().isEmpty()) {
        addOverload();
      }
      return getOverload().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published. The URL SHOULD include the major version of the operation definition. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the operation definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the operation definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the operation definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of this operation definition. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("kind", "code", "Whether this is an operation or a named query.", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this operation definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the operation definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the operation definition changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the operation definition.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the operation definition from a consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate operation definition instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the operation definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("purpose", "markdown", "Explaination of why this operation definition is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("idempotent", "boolean", "Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.", 0, java.lang.Integer.MAX_VALUE, idempotent));
        childrenList.add(new Property("code", "code", "The name used to invoke the operation.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("comment", "string", "Additional information about how to use this operation or named query.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("base", "Reference(OperationDefinition)", "Indicates that this operation definition is a constraining profile on the base.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("resource", "code", "The types on which this operation can be executed.", 0, java.lang.Integer.MAX_VALUE, resource));
        childrenList.add(new Property("system", "boolean", "Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).", 0, java.lang.Integer.MAX_VALUE, system));
        childrenList.add(new Property("type", "boolean", "Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a specific resource id for the context).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("instance", "boolean", "Indicates whether this operation can be invoked on a particular instance of one of the given types.", 0, java.lang.Integer.MAX_VALUE, instance));
        childrenList.add(new Property("parameter", "", "The parameters for the operation/query.", 0, java.lang.Integer.MAX_VALUE, parameter));
        childrenList.add(new Property("overload", "", "Defines an appropriate combination of parameters to use when invoking this operation, to help code generators when generating overloaded parameter sets for this operation.", 0, java.lang.Integer.MAX_VALUE, overload));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case 3292052: /*kind*/ return this.kind == null ? new Base[0] : new Base[] {this.kind}; // Enumeration<OperationKind>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 1680468793: /*idempotent*/ return this.idempotent == null ? new Base[0] : new Base[] {this.idempotent}; // BooleanType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeType
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        case 3016401: /*base*/ return this.base == null ? new Base[0] : new Base[] {this.base}; // Reference
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : this.resource.toArray(new Base[this.resource.size()]); // CodeType
        case -887328209: /*system*/ return this.system == null ? new Base[0] : new Base[] {this.system}; // BooleanType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // BooleanType
        case 555127957: /*instance*/ return this.instance == null ? new Base[0] : new Base[] {this.instance}; // BooleanType
        case 1954460585: /*parameter*/ return this.parameter == null ? new Base[0] : this.parameter.toArray(new Base[this.parameter.size()]); // OperationDefinitionParameterComponent
        case 529823674: /*overload*/ return this.overload == null ? new Base[0] : this.overload.toArray(new Base[this.overload.size()]); // OperationDefinitionOverloadComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case 3292052: // kind
          value = new OperationKindEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<OperationKind>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 1680468793: // idempotent
          this.idempotent = castToBoolean(value); // BooleanType
          return value;
        case 3059181: // code
          this.code = castToCode(value); // CodeType
          return value;
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          return value;
        case 3016401: // base
          this.base = castToReference(value); // Reference
          return value;
        case -341064690: // resource
          this.getResource().add(castToCode(value)); // CodeType
          return value;
        case -887328209: // system
          this.system = castToBoolean(value); // BooleanType
          return value;
        case 3575610: // type
          this.type = castToBoolean(value); // BooleanType
          return value;
        case 555127957: // instance
          this.instance = castToBoolean(value); // BooleanType
          return value;
        case 1954460585: // parameter
          this.getParameter().add((OperationDefinitionParameterComponent) value); // OperationDefinitionParameterComponent
          return value;
        case 529823674: // overload
          this.getOverload().add((OperationDefinitionOverloadComponent) value); // OperationDefinitionOverloadComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("kind")) {
          value = new OperationKindEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<OperationKind>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("idempotent")) {
          this.idempotent = castToBoolean(value); // BooleanType
        } else if (name.equals("code")) {
          this.code = castToCode(value); // CodeType
        } else if (name.equals("comment")) {
          this.comment = castToString(value); // StringType
        } else if (name.equals("base")) {
          this.base = castToReference(value); // Reference
        } else if (name.equals("resource")) {
          this.getResource().add(castToCode(value));
        } else if (name.equals("system")) {
          this.system = castToBoolean(value); // BooleanType
        } else if (name.equals("type")) {
          this.type = castToBoolean(value); // BooleanType
        } else if (name.equals("instance")) {
          this.instance = castToBoolean(value); // BooleanType
        } else if (name.equals("parameter")) {
          this.getParameter().add((OperationDefinitionParameterComponent) value);
        } else if (name.equals("overload")) {
          this.getOverload().add((OperationDefinitionOverloadComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case -892481550:  return getStatusElement();
        case 3292052:  return getKindElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case -220463842:  return getPurposeElement();
        case 1680468793:  return getIdempotentElement();
        case 3059181:  return getCodeElement();
        case 950398559:  return getCommentElement();
        case 3016401:  return getBase(); 
        case -341064690:  return addResourceElement();
        case -887328209:  return getSystemElement();
        case 3575610:  return getTypeElement();
        case 555127957:  return getInstanceElement();
        case 1954460585:  return addParameter(); 
        case 529823674:  return addOverload(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3292052: /*kind*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 1680468793: /*idempotent*/ return new String[] {"boolean"};
        case 3059181: /*code*/ return new String[] {"code"};
        case 950398559: /*comment*/ return new String[] {"string"};
        case 3016401: /*base*/ return new String[] {"Reference"};
        case -341064690: /*resource*/ return new String[] {"code"};
        case -887328209: /*system*/ return new String[] {"boolean"};
        case 3575610: /*type*/ return new String[] {"boolean"};
        case 555127957: /*instance*/ return new String[] {"boolean"};
        case 1954460585: /*parameter*/ return new String[] {};
        case 529823674: /*overload*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.status");
        }
        else if (name.equals("kind")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.kind");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.purpose");
        }
        else if (name.equals("idempotent")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.idempotent");
        }
        else if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.code");
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.comment");
        }
        else if (name.equals("base")) {
          this.base = new Reference();
          return this.base;
        }
        else if (name.equals("resource")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.resource");
        }
        else if (name.equals("system")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.system");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.type");
        }
        else if (name.equals("instance")) {
          throw new FHIRException("Cannot call addChild on a primitive type OperationDefinition.instance");
        }
        else if (name.equals("parameter")) {
          return addParameter();
        }
        else if (name.equals("overload")) {
          return addOverload();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "OperationDefinition";

  }

      public OperationDefinition copy() {
        OperationDefinition dst = new OperationDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.kind = kind == null ? null : kind.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
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
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.idempotent = idempotent == null ? null : idempotent.copy();
        dst.code = code == null ? null : code.copy();
        dst.comment = comment == null ? null : comment.copy();
        dst.base = base == null ? null : base.copy();
        if (resource != null) {
          dst.resource = new ArrayList<CodeType>();
          for (CodeType i : resource)
            dst.resource.add(i.copy());
        };
        dst.system = system == null ? null : system.copy();
        dst.type = type == null ? null : type.copy();
        dst.instance = instance == null ? null : instance.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<OperationDefinitionParameterComponent>();
          for (OperationDefinitionParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        if (overload != null) {
          dst.overload = new ArrayList<OperationDefinitionOverloadComponent>();
          for (OperationDefinitionOverloadComponent i : overload)
            dst.overload.add(i.copy());
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
        return compareDeep(kind, o.kind, true) && compareDeep(purpose, o.purpose, true) && compareDeep(idempotent, o.idempotent, true)
           && compareDeep(code, o.code, true) && compareDeep(comment, o.comment, true) && compareDeep(base, o.base, true)
           && compareDeep(resource, o.resource, true) && compareDeep(system, o.system, true) && compareDeep(type, o.type, true)
           && compareDeep(instance, o.instance, true) && compareDeep(parameter, o.parameter, true) && compareDeep(overload, o.overload, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationDefinition))
          return false;
        OperationDefinition o = (OperationDefinition) other;
        return compareValues(kind, o.kind, true) && compareValues(purpose, o.purpose, true) && compareValues(idempotent, o.idempotent, true)
           && compareValues(code, o.code, true) && compareValues(comment, o.comment, true) && compareValues(resource, o.resource, true)
           && compareValues(system, o.system, true) && compareValues(type, o.type, true) && compareValues(instance, o.instance, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(kind, purpose, idempotent
          , code, comment, base, resource, system, type, instance, parameter, overload
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.OperationDefinition;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The operation definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>OperationDefinition.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="OperationDefinition.date", description="The operation definition publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The operation definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>OperationDefinition.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Name used to invoke the operation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="OperationDefinition.code", description="Name used to invoke the operation", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Name used to invoke the operation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>instance</b>
   * <p>
   * Description: <b>Invoke on an instance?</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.instance</b><br>
   * </p>
   */
  @SearchParamDefinition(name="instance", path="OperationDefinition.instance", description="Invoke on an instance?", type="token" )
  public static final String SP_INSTANCE = "instance";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>instance</b>
   * <p>
   * Description: <b>Invoke on an instance?</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.instance</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam INSTANCE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_INSTANCE);

 /**
   * Search parameter: <b>kind</b>
   * <p>
   * Description: <b>operation | query</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.kind</b><br>
   * </p>
   */
  @SearchParamDefinition(name="kind", path="OperationDefinition.kind", description="operation | query", type="token" )
  public static final String SP_KIND = "kind";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>kind</b>
   * <p>
   * Description: <b>operation | query</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.kind</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam KIND = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_KIND);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the operation definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="OperationDefinition.jurisdiction", description="Intended jurisdiction for the operation definition", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the operation definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the operation definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OperationDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="OperationDefinition.description", description="The description of the operation definition", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the operation definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OperationDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Invole at the type level?</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="OperationDefinition.type", description="Invole at the type level?", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Invole at the type level?</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the operation definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="OperationDefinition.version", description="The business version of the operation definition", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the operation definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the operation definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>OperationDefinition.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="OperationDefinition.url", description="The uri that identifies the operation definition", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the operation definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>OperationDefinition.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>system</b>
   * <p>
   * Description: <b>Invoke at the system level?</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.system</b><br>
   * </p>
   */
  @SearchParamDefinition(name="system", path="OperationDefinition.system", description="Invoke at the system level?", type="token" )
  public static final String SP_SYSTEM = "system";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>system</b>
   * <p>
   * Description: <b>Invoke at the system level?</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.system</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SYSTEM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SYSTEM);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the operation definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OperationDefinition.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="OperationDefinition.name", description="Computationally friendly name of the operation definition", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the operation definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OperationDefinition.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the operation definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OperationDefinition.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="OperationDefinition.publisher", description="Name of the publisher of the operation definition", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the operation definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OperationDefinition.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>param-profile</b>
   * <p>
   * Description: <b>Profile on the type</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OperationDefinition.parameter.profile</b><br>
   * </p>
   */
  @SearchParamDefinition(name="param-profile", path="OperationDefinition.parameter.profile", description="Profile on the type", type="reference", target={StructureDefinition.class } )
  public static final String SP_PARAM_PROFILE = "param-profile";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>param-profile</b>
   * <p>
   * Description: <b>Profile on the type</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OperationDefinition.parameter.profile</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARAM_PROFILE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARAM_PROFILE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>OperationDefinition:param-profile</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARAM_PROFILE = new ca.uhn.fhir.model.api.Include("OperationDefinition:param-profile").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the operation definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="OperationDefinition.status", description="The current status of the operation definition", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the operation definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OperationDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>base</b>
   * <p>
   * Description: <b>Marks this as a profile of the base</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OperationDefinition.base</b><br>
   * </p>
   */
  @SearchParamDefinition(name="base", path="OperationDefinition.base", description="Marks this as a profile of the base", type="reference", target={OperationDefinition.class } )
  public static final String SP_BASE = "base";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>base</b>
   * <p>
   * Description: <b>Marks this as a profile of the base</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OperationDefinition.base</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>OperationDefinition:base</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASE = new ca.uhn.fhir.model.api.Include("OperationDefinition:base").toLocked();


}

