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
 * The DecisionSupportServiceModule resource describes decision support functionality that is available as a service.
 */
@ResourceDef(name="DecisionSupportServiceModule", profile="http://hl7.org/fhir/Profile/DecisionSupportServiceModule")
public class DecisionSupportServiceModule extends DomainResource {

    @Block()
    public static class DecisionSupportServiceModuleParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the parameter.
         */
        @Child(name = "name", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter name", formalDefinition="The name of the parameter." )
        protected CodeType name;

        /**
         * Whether the parameter is input or output for the module.
         */
        @Child(name = "use", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Whether the parameter is input or output for the module." )
        protected CodeType use;

        /**
         * A brief discussion of what the parameter is for and how it is used by the module.
         */
        @Child(name = "documentation", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A brief description of the parameter", formalDefinition="A brief discussion of what the parameter is for and how it is used by the module." )
        protected StringType documentation;

        /**
         * The type of the parameter.
         */
        @Child(name = "type", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The type of the parameter." )
        protected CodeType type;

        /**
         * If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.
         */
        @Child(name = "profile", type = {StructureDefinition.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The profile of the parameter, any", formalDefinition="If specified, this indicates a profile that the input data must conform to, or that the output data will conform to." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.)
         */
        protected StructureDefinition profileTarget;

        /**
         * Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.
         */
        @Child(name = "mustSupport", type = {StringType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Indicates that specific structure elements are referenced by the knowledge module", formalDefinition="Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available." )
        protected List<StringType> mustSupport;

        /**
         * Indicates that incoming data for this parameter is expected to match the given code filter. In other words, every instance should have a value for the path element that comes from the specified value set (or list of concepts).
         */
        @Child(name = "codeFilter", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Code filters for the required data, if any", formalDefinition="Indicates that incoming data for this parameter is expected to match the given code filter. In other words, every instance should have a value for the path element that comes from the specified value set (or list of concepts)." )
        protected List<DecisionSupportServiceModuleParameterCodeFilterComponent> codeFilter;

        /**
         * Indicates that incoming data for this parameter is expected to match the given date filter. In other words, every instance should have a value for the path element that is equal to the given datetime (or within the given Period).
         */
        @Child(name = "dateFilter", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Date filters for the required data, if any", formalDefinition="Indicates that incoming data for this parameter is expected to match the given date filter. In other words, every instance should have a value for the path element that is equal to the given datetime (or within the given Period)." )
        protected List<DecisionSupportServiceModuleParameterDateFilterComponent> dateFilter;

        private static final long serialVersionUID = 484857813L;

    /**
     * Constructor
     */
      public DecisionSupportServiceModuleParameterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DecisionSupportServiceModuleParameterComponent(CodeType use, CodeType type) {
        super();
        this.use = use;
        this.type = type;
      }

        /**
         * @return {@link #name} (The name of the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public CodeType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportServiceModuleParameterComponent.name");
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
         * @param value {@link #name} (The name of the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public DecisionSupportServiceModuleParameterComponent setNameElement(CodeType value) { 
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
        public DecisionSupportServiceModuleParameterComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new CodeType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #use} (Whether the parameter is input or output for the module.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
         */
        public CodeType getUseElement() { 
          if (this.use == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportServiceModuleParameterComponent.use");
            else if (Configuration.doAutoCreate())
              this.use = new CodeType(); // bb
          return this.use;
        }

        public boolean hasUseElement() { 
          return this.use != null && !this.use.isEmpty();
        }

        public boolean hasUse() { 
          return this.use != null && !this.use.isEmpty();
        }

        /**
         * @param value {@link #use} (Whether the parameter is input or output for the module.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
         */
        public DecisionSupportServiceModuleParameterComponent setUseElement(CodeType value) { 
          this.use = value;
          return this;
        }

        /**
         * @return Whether the parameter is input or output for the module.
         */
        public String getUse() { 
          return this.use == null ? null : this.use.getValue();
        }

        /**
         * @param value Whether the parameter is input or output for the module.
         */
        public DecisionSupportServiceModuleParameterComponent setUse(String value) { 
            if (this.use == null)
              this.use = new CodeType();
            this.use.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (A brief discussion of what the parameter is for and how it is used by the module.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportServiceModuleParameterComponent.documentation");
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
         * @param value {@link #documentation} (A brief discussion of what the parameter is for and how it is used by the module.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public DecisionSupportServiceModuleParameterComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return A brief discussion of what the parameter is for and how it is used by the module.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value A brief discussion of what the parameter is for and how it is used by the module.
         */
        public DecisionSupportServiceModuleParameterComponent setDocumentation(String value) { 
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
         * @return {@link #type} (The type of the parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportServiceModuleParameterComponent.type");
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
         * @param value {@link #type} (The type of the parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public DecisionSupportServiceModuleParameterComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of the parameter.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of the parameter.
         */
        public DecisionSupportServiceModuleParameterComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportServiceModuleParameterComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.)
         */
        public DecisionSupportServiceModuleParameterComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.)
         */
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportServiceModuleParameterComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.)
         */
        public DecisionSupportServiceModuleParameterComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        /**
         * @return {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.)
         */
        public List<StringType> getMustSupport() { 
          if (this.mustSupport == null)
            this.mustSupport = new ArrayList<StringType>();
          return this.mustSupport;
        }

        public boolean hasMustSupport() { 
          if (this.mustSupport == null)
            return false;
          for (StringType item : this.mustSupport)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.)
         */
    // syntactic sugar
        public StringType addMustSupportElement() {//2 
          StringType t = new StringType();
          if (this.mustSupport == null)
            this.mustSupport = new ArrayList<StringType>();
          this.mustSupport.add(t);
          return t;
        }

        /**
         * @param value {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.)
         */
        public DecisionSupportServiceModuleParameterComponent addMustSupport(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.mustSupport == null)
            this.mustSupport = new ArrayList<StringType>();
          this.mustSupport.add(t);
          return this;
        }

        /**
         * @param value {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.)
         */
        public boolean hasMustSupport(String value) { 
          if (this.mustSupport == null)
            return false;
          for (StringType v : this.mustSupport)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #codeFilter} (Indicates that incoming data for this parameter is expected to match the given code filter. In other words, every instance should have a value for the path element that comes from the specified value set (or list of concepts).)
         */
        public List<DecisionSupportServiceModuleParameterCodeFilterComponent> getCodeFilter() { 
          if (this.codeFilter == null)
            this.codeFilter = new ArrayList<DecisionSupportServiceModuleParameterCodeFilterComponent>();
          return this.codeFilter;
        }

        public boolean hasCodeFilter() { 
          if (this.codeFilter == null)
            return false;
          for (DecisionSupportServiceModuleParameterCodeFilterComponent item : this.codeFilter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #codeFilter} (Indicates that incoming data for this parameter is expected to match the given code filter. In other words, every instance should have a value for the path element that comes from the specified value set (or list of concepts).)
         */
    // syntactic sugar
        public DecisionSupportServiceModuleParameterCodeFilterComponent addCodeFilter() { //3
          DecisionSupportServiceModuleParameterCodeFilterComponent t = new DecisionSupportServiceModuleParameterCodeFilterComponent();
          if (this.codeFilter == null)
            this.codeFilter = new ArrayList<DecisionSupportServiceModuleParameterCodeFilterComponent>();
          this.codeFilter.add(t);
          return t;
        }

    // syntactic sugar
        public DecisionSupportServiceModuleParameterComponent addCodeFilter(DecisionSupportServiceModuleParameterCodeFilterComponent t) { //3
          if (t == null)
            return this;
          if (this.codeFilter == null)
            this.codeFilter = new ArrayList<DecisionSupportServiceModuleParameterCodeFilterComponent>();
          this.codeFilter.add(t);
          return this;
        }

        /**
         * @return {@link #dateFilter} (Indicates that incoming data for this parameter is expected to match the given date filter. In other words, every instance should have a value for the path element that is equal to the given datetime (or within the given Period).)
         */
        public List<DecisionSupportServiceModuleParameterDateFilterComponent> getDateFilter() { 
          if (this.dateFilter == null)
            this.dateFilter = new ArrayList<DecisionSupportServiceModuleParameterDateFilterComponent>();
          return this.dateFilter;
        }

        public boolean hasDateFilter() { 
          if (this.dateFilter == null)
            return false;
          for (DecisionSupportServiceModuleParameterDateFilterComponent item : this.dateFilter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dateFilter} (Indicates that incoming data for this parameter is expected to match the given date filter. In other words, every instance should have a value for the path element that is equal to the given datetime (or within the given Period).)
         */
    // syntactic sugar
        public DecisionSupportServiceModuleParameterDateFilterComponent addDateFilter() { //3
          DecisionSupportServiceModuleParameterDateFilterComponent t = new DecisionSupportServiceModuleParameterDateFilterComponent();
          if (this.dateFilter == null)
            this.dateFilter = new ArrayList<DecisionSupportServiceModuleParameterDateFilterComponent>();
          this.dateFilter.add(t);
          return t;
        }

    // syntactic sugar
        public DecisionSupportServiceModuleParameterComponent addDateFilter(DecisionSupportServiceModuleParameterDateFilterComponent t) { //3
          if (t == null)
            return this;
          if (this.dateFilter == null)
            this.dateFilter = new ArrayList<DecisionSupportServiceModuleParameterDateFilterComponent>();
          this.dateFilter.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "code", "The name of the parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("use", "code", "Whether the parameter is input or output for the module.", 0, java.lang.Integer.MAX_VALUE, use));
          childrenList.add(new Property("documentation", "string", "A brief discussion of what the parameter is for and how it is used by the module.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("type", "code", "The type of the parameter.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("profile", "Reference(StructureDefinition)", "If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("mustSupport", "string", "Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.", 0, java.lang.Integer.MAX_VALUE, mustSupport));
          childrenList.add(new Property("codeFilter", "", "Indicates that incoming data for this parameter is expected to match the given code filter. In other words, every instance should have a value for the path element that comes from the specified value set (or list of concepts).", 0, java.lang.Integer.MAX_VALUE, codeFilter));
          childrenList.add(new Property("dateFilter", "", "Indicates that incoming data for this parameter is expected to match the given date filter. In other words, every instance should have a value for the path element that is equal to the given datetime (or within the given Period).", 0, java.lang.Integer.MAX_VALUE, dateFilter));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToCode(value); // CodeType
        else if (name.equals("use"))
          this.use = castToCode(value); // CodeType
        else if (name.equals("documentation"))
          this.documentation = castToString(value); // StringType
        else if (name.equals("type"))
          this.type = castToCode(value); // CodeType
        else if (name.equals("profile"))
          this.profile = castToReference(value); // Reference
        else if (name.equals("mustSupport"))
          this.getMustSupport().add(castToString(value));
        else if (name.equals("codeFilter"))
          this.getCodeFilter().add((DecisionSupportServiceModuleParameterCodeFilterComponent) value);
        else if (name.equals("dateFilter"))
          this.getDateFilter().add((DecisionSupportServiceModuleParameterDateFilterComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportServiceModule.name");
        }
        else if (name.equals("use")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportServiceModule.use");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportServiceModule.documentation");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportServiceModule.type");
        }
        else if (name.equals("profile")) {
          this.profile = new Reference();
          return this.profile;
        }
        else if (name.equals("mustSupport")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportServiceModule.mustSupport");
        }
        else if (name.equals("codeFilter")) {
          return addCodeFilter();
        }
        else if (name.equals("dateFilter")) {
          return addDateFilter();
        }
        else
          return super.addChild(name);
      }

      public DecisionSupportServiceModuleParameterComponent copy() {
        DecisionSupportServiceModuleParameterComponent dst = new DecisionSupportServiceModuleParameterComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.use = use == null ? null : use.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        if (mustSupport != null) {
          dst.mustSupport = new ArrayList<StringType>();
          for (StringType i : mustSupport)
            dst.mustSupport.add(i.copy());
        };
        if (codeFilter != null) {
          dst.codeFilter = new ArrayList<DecisionSupportServiceModuleParameterCodeFilterComponent>();
          for (DecisionSupportServiceModuleParameterCodeFilterComponent i : codeFilter)
            dst.codeFilter.add(i.copy());
        };
        if (dateFilter != null) {
          dst.dateFilter = new ArrayList<DecisionSupportServiceModuleParameterDateFilterComponent>();
          for (DecisionSupportServiceModuleParameterDateFilterComponent i : dateFilter)
            dst.dateFilter.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DecisionSupportServiceModuleParameterComponent))
          return false;
        DecisionSupportServiceModuleParameterComponent o = (DecisionSupportServiceModuleParameterComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(use, o.use, true) && compareDeep(documentation, o.documentation, true)
           && compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true) && compareDeep(mustSupport, o.mustSupport, true)
           && compareDeep(codeFilter, o.codeFilter, true) && compareDeep(dateFilter, o.dateFilter, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DecisionSupportServiceModuleParameterComponent))
          return false;
        DecisionSupportServiceModuleParameterComponent o = (DecisionSupportServiceModuleParameterComponent) other;
        return compareValues(name, o.name, true) && compareValues(use, o.use, true) && compareValues(documentation, o.documentation, true)
           && compareValues(type, o.type, true) && compareValues(mustSupport, o.mustSupport, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (use == null || use.isEmpty())
           && (documentation == null || documentation.isEmpty()) && (type == null || type.isEmpty())
           && (profile == null || profile.isEmpty()) && (mustSupport == null || mustSupport.isEmpty())
           && (codeFilter == null || codeFilter.isEmpty()) && (dateFilter == null || dateFilter.isEmpty())
          ;
      }

  public String fhirType() {
    return "DecisionSupportServiceModule.parameter";

  }

  }

    @Block()
    public static class DecisionSupportServiceModuleParameterCodeFilterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The code-valued attribute of the filter", formalDefinition="The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept." )
        protected StringType path;

        /**
         * The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.
         */
        @Child(name = "valueSet", type = {UriType.class, ValueSet.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The valueset for the code filter", formalDefinition="The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset." )
        protected Type valueSet;

        /**
         * The codeable concept for the code filter. Only one of valueSet or codeableConcept may be specified. If codeableConcepts are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codeable concepts.
         */
        @Child(name = "codeableConcept", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The codeableConcepts for the filter", formalDefinition="The codeable concept for the code filter. Only one of valueSet or codeableConcept may be specified. If codeableConcepts are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codeable concepts." )
        protected List<CodeableConcept> codeableConcept;

        private static final long serialVersionUID = -666343535L;

    /**
     * Constructor
     */
      public DecisionSupportServiceModuleParameterCodeFilterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DecisionSupportServiceModuleParameterCodeFilterComponent(StringType path) {
        super();
        this.path = path;
      }

        /**
         * @return {@link #path} (The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportServiceModuleParameterCodeFilterComponent.path");
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
         * @param value {@link #path} (The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public DecisionSupportServiceModuleParameterCodeFilterComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
         */
        public DecisionSupportServiceModuleParameterCodeFilterComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        /**
         * @return {@link #valueSet} (The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.)
         */
        public Type getValueSet() { 
          return this.valueSet;
        }

        /**
         * @return {@link #valueSet} (The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.)
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
         * @return {@link #valueSet} (The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.)
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
         * @param value {@link #valueSet} (The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.)
         */
        public DecisionSupportServiceModuleParameterCodeFilterComponent setValueSet(Type value) { 
          this.valueSet = value;
          return this;
        }

        /**
         * @return {@link #codeableConcept} (The codeable concept for the code filter. Only one of valueSet or codeableConcept may be specified. If codeableConcepts are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codeable concepts.)
         */
        public List<CodeableConcept> getCodeableConcept() { 
          if (this.codeableConcept == null)
            this.codeableConcept = new ArrayList<CodeableConcept>();
          return this.codeableConcept;
        }

        public boolean hasCodeableConcept() { 
          if (this.codeableConcept == null)
            return false;
          for (CodeableConcept item : this.codeableConcept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #codeableConcept} (The codeable concept for the code filter. Only one of valueSet or codeableConcept may be specified. If codeableConcepts are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codeable concepts.)
         */
    // syntactic sugar
        public CodeableConcept addCodeableConcept() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.codeableConcept == null)
            this.codeableConcept = new ArrayList<CodeableConcept>();
          this.codeableConcept.add(t);
          return t;
        }

    // syntactic sugar
        public DecisionSupportServiceModuleParameterCodeFilterComponent addCodeableConcept(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.codeableConcept == null)
            this.codeableConcept = new ArrayList<CodeableConcept>();
          this.codeableConcept.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("path", "string", "The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("valueSet[x]", "uri|Reference(ValueSet)", "The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.", 0, java.lang.Integer.MAX_VALUE, valueSet));
          childrenList.add(new Property("codeableConcept", "CodeableConcept", "The codeable concept for the code filter. Only one of valueSet or codeableConcept may be specified. If codeableConcepts are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codeable concepts.", 0, java.lang.Integer.MAX_VALUE, codeableConcept));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path"))
          this.path = castToString(value); // StringType
        else if (name.equals("valueSet[x]"))
          this.valueSet = (Type) value; // Type
        else if (name.equals("codeableConcept"))
          this.getCodeableConcept().add(castToCodeableConcept(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportServiceModule.path");
        }
        else if (name.equals("valueSetUri")) {
          this.valueSet = new UriType();
          return this.valueSet;
        }
        else if (name.equals("valueSetReference")) {
          this.valueSet = new Reference();
          return this.valueSet;
        }
        else if (name.equals("codeableConcept")) {
          return addCodeableConcept();
        }
        else
          return super.addChild(name);
      }

      public DecisionSupportServiceModuleParameterCodeFilterComponent copy() {
        DecisionSupportServiceModuleParameterCodeFilterComponent dst = new DecisionSupportServiceModuleParameterCodeFilterComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.valueSet = valueSet == null ? null : valueSet.copy();
        if (codeableConcept != null) {
          dst.codeableConcept = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : codeableConcept)
            dst.codeableConcept.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DecisionSupportServiceModuleParameterCodeFilterComponent))
          return false;
        DecisionSupportServiceModuleParameterCodeFilterComponent o = (DecisionSupportServiceModuleParameterCodeFilterComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(valueSet, o.valueSet, true) && compareDeep(codeableConcept, o.codeableConcept, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DecisionSupportServiceModuleParameterCodeFilterComponent))
          return false;
        DecisionSupportServiceModuleParameterCodeFilterComponent o = (DecisionSupportServiceModuleParameterCodeFilterComponent) other;
        return compareValues(path, o.path, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (path == null || path.isEmpty()) && (valueSet == null || valueSet.isEmpty())
           && (codeableConcept == null || codeableConcept.isEmpty());
      }

  public String fhirType() {
    return "DecisionSupportServiceModule.parameter.codeFilter";

  }

  }

    @Block()
    public static class DecisionSupportServiceModuleParameterDateFilterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The date-valued attribute of the filter", formalDefinition="The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing." )
        protected StringType path;

        /**
         * The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.
         */
        @Child(name = "value", type = {DateTimeType.class, Period.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The value of the filter, as a Period or dateTime value", formalDefinition="The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime." )
        protected Type value;

        private static final long serialVersionUID = 1791957163L;

    /**
     * Constructor
     */
      public DecisionSupportServiceModuleParameterDateFilterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DecisionSupportServiceModuleParameterDateFilterComponent(StringType path) {
        super();
        this.path = path;
      }

        /**
         * @return {@link #path} (The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportServiceModuleParameterDateFilterComponent.path");
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
         * @param value {@link #path} (The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public DecisionSupportServiceModuleParameterDateFilterComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.
         */
        public DecisionSupportServiceModuleParameterDateFilterComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.)
         */
        public DateTimeType getValueDateTimeType() throws FHIRException { 
          if (!(this.value instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateTimeType) this.value;
        }

        public boolean hasValueDateTimeType() { 
          return this.value instanceof DateTimeType;
        }

        /**
         * @return {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.)
         */
        public Period getValuePeriod() throws FHIRException { 
          if (!(this.value instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Period) this.value;
        }

        public boolean hasValuePeriod() { 
          return this.value instanceof Period;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.)
         */
        public DecisionSupportServiceModuleParameterDateFilterComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("path", "string", "The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("value[x]", "dateTime|Period", "The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path"))
          this.path = castToString(value); // StringType
        else if (name.equals("value[x]"))
          this.value = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportServiceModule.path");
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public DecisionSupportServiceModuleParameterDateFilterComponent copy() {
        DecisionSupportServiceModuleParameterDateFilterComponent dst = new DecisionSupportServiceModuleParameterDateFilterComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DecisionSupportServiceModuleParameterDateFilterComponent))
          return false;
        DecisionSupportServiceModuleParameterDateFilterComponent o = (DecisionSupportServiceModuleParameterDateFilterComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DecisionSupportServiceModuleParameterDateFilterComponent))
          return false;
        DecisionSupportServiceModuleParameterDateFilterComponent o = (DecisionSupportServiceModuleParameterDateFilterComponent) other;
        return compareValues(path, o.path, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (path == null || path.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  public String fhirType() {
    return "DecisionSupportServiceModule.parameter.dateFilter";

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
     * A reference to a ModuleMetadata resource describing the metadata for the decision support service module.
     */
    @Child(name = "moduleMetadata", type = {ModuleMetadata.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Metadata for the service module", formalDefinition="A reference to a ModuleMetadata resource describing the metadata for the decision support service module." )
    protected Reference moduleMetadata;

    /**
     * The actual object that is the target of the reference (A reference to a ModuleMetadata resource describing the metadata for the decision support service module.)
     */
    protected ModuleMetadata moduleMetadataTarget;

    /**
     * The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
     */
    @Child(name = "parameter", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Parameters to the module", formalDefinition="The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse." )
    protected List<DecisionSupportServiceModuleParameterComponent> parameter;

    private static final long serialVersionUID = 1535823227L;

  /**
   * Constructor
   */
    public DecisionSupportServiceModule() {
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
    public DecisionSupportServiceModule addIdentifier(Identifier t) { //3
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
          throw new Error("Attempt to auto-create DecisionSupportServiceModule.version");
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
    public DecisionSupportServiceModule setVersionElement(StringType value) { 
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
    public DecisionSupportServiceModule setVersion(String value) { 
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
     * @return {@link #moduleMetadata} (A reference to a ModuleMetadata resource describing the metadata for the decision support service module.)
     */
    public Reference getModuleMetadata() { 
      if (this.moduleMetadata == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DecisionSupportServiceModule.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadata = new Reference(); // cc
      return this.moduleMetadata;
    }

    public boolean hasModuleMetadata() { 
      return this.moduleMetadata != null && !this.moduleMetadata.isEmpty();
    }

    /**
     * @param value {@link #moduleMetadata} (A reference to a ModuleMetadata resource describing the metadata for the decision support service module.)
     */
    public DecisionSupportServiceModule setModuleMetadata(Reference value) { 
      this.moduleMetadata = value;
      return this;
    }

    /**
     * @return {@link #moduleMetadata} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a ModuleMetadata resource describing the metadata for the decision support service module.)
     */
    public ModuleMetadata getModuleMetadataTarget() { 
      if (this.moduleMetadataTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DecisionSupportServiceModule.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadataTarget = new ModuleMetadata(); // aa
      return this.moduleMetadataTarget;
    }

    /**
     * @param value {@link #moduleMetadata} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a ModuleMetadata resource describing the metadata for the decision support service module.)
     */
    public DecisionSupportServiceModule setModuleMetadataTarget(ModuleMetadata value) { 
      this.moduleMetadataTarget = value;
      return this;
    }

    /**
     * @return {@link #parameter} (The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.)
     */
    public List<DecisionSupportServiceModuleParameterComponent> getParameter() { 
      if (this.parameter == null)
        this.parameter = new ArrayList<DecisionSupportServiceModuleParameterComponent>();
      return this.parameter;
    }

    public boolean hasParameter() { 
      if (this.parameter == null)
        return false;
      for (DecisionSupportServiceModuleParameterComponent item : this.parameter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #parameter} (The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.)
     */
    // syntactic sugar
    public DecisionSupportServiceModuleParameterComponent addParameter() { //3
      DecisionSupportServiceModuleParameterComponent t = new DecisionSupportServiceModuleParameterComponent();
      if (this.parameter == null)
        this.parameter = new ArrayList<DecisionSupportServiceModuleParameterComponent>();
      this.parameter.add(t);
      return t;
    }

    // syntactic sugar
    public DecisionSupportServiceModule addParameter(DecisionSupportServiceModuleParameterComponent t) { //3
      if (t == null)
        return this;
      if (this.parameter == null)
        this.parameter = new ArrayList<DecisionSupportServiceModuleParameterComponent>();
      this.parameter.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("moduleMetadata", "Reference(ModuleMetadata)", "A reference to a ModuleMetadata resource describing the metadata for the decision support service module.", 0, java.lang.Integer.MAX_VALUE, moduleMetadata));
        childrenList.add(new Property("parameter", "", "The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.", 0, java.lang.Integer.MAX_VALUE, parameter));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("moduleMetadata"))
          this.moduleMetadata = castToReference(value); // Reference
        else if (name.equals("parameter"))
          this.getParameter().add((DecisionSupportServiceModuleParameterComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportServiceModule.version");
        }
        else if (name.equals("moduleMetadata")) {
          this.moduleMetadata = new Reference();
          return this.moduleMetadata;
        }
        else if (name.equals("parameter")) {
          return addParameter();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DecisionSupportServiceModule";

  }

      public DecisionSupportServiceModule copy() {
        DecisionSupportServiceModule dst = new DecisionSupportServiceModule();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.moduleMetadata = moduleMetadata == null ? null : moduleMetadata.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<DecisionSupportServiceModuleParameterComponent>();
          for (DecisionSupportServiceModuleParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        return dst;
      }

      protected DecisionSupportServiceModule typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DecisionSupportServiceModule))
          return false;
        DecisionSupportServiceModule o = (DecisionSupportServiceModule) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true) && compareDeep(moduleMetadata, o.moduleMetadata, true)
           && compareDeep(parameter, o.parameter, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DecisionSupportServiceModule))
          return false;
        DecisionSupportServiceModule o = (DecisionSupportServiceModule) other;
        return compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (moduleMetadata == null || moduleMetadata.isEmpty()) && (parameter == null || parameter.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DecisionSupportServiceModule;
   }


}

