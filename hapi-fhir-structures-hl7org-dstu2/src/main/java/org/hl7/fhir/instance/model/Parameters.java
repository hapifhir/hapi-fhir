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
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * This special resource type is used to represent [operation](operations.html] request and response. It has no other use, and there is no RESTful end=point associated with it.
 */
@ResourceDef(name="Parameters", profile="http://hl7.org/fhir/Profile/Parameters")
public class Parameters extends Resource implements IBaseParameters {

    @Block()
    public static class ParametersParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the parameter (reference to the operation definition).
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name from the definition", formalDefinition="The name of the parameter (reference to the operation definition)." )
        protected StringType name;

        /**
         * If the parameter is a data type.
         */
        @Child(name = "value", type = {}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="If parameter is a data type", formalDefinition="If the parameter is a data type." )
        protected org.hl7.fhir.instance.model.Type value;

        /**
         * If the parameter is a whole resource.
         */
        @Child(name = "resource", type = {Resource.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="If parameter is a whole resource", formalDefinition="If the parameter is a whole resource." )
        protected Resource resource;

        /**
         * A named part of a parameter. In many implementation context, a set of named parts is known as a "Tuple".
         */
        @Child(name = "part", type = {ParametersParameterComponent.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Named part of a parameter (e.g. Tuple)", formalDefinition="A named part of a parameter. In many implementation context, a set of named parts is known as a \"Tuple\"." )
        protected List<ParametersParameterComponent> part;

        private static final long serialVersionUID = 1129239796L;

    /*
     * Constructor
     */
      public ParametersParameterComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ParametersParameterComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (The name of the parameter (reference to the operation definition).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ParametersParameterComponent.name");
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
         * @param value {@link #name} (The name of the parameter (reference to the operation definition).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ParametersParameterComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the parameter (reference to the operation definition).
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the parameter (reference to the operation definition).
         */
        public ParametersParameterComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (If the parameter is a data type.)
         */
        public org.hl7.fhir.instance.model.Type getValue() { 
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (If the parameter is a data type.)
         */
        public ParametersParameterComponent setValue(org.hl7.fhir.instance.model.Type value) { 
          this.value = value;
          return this;
        }

        /**
         * @return {@link #resource} (If the parameter is a whole resource.)
         */
        public Resource getResource() { 
          return this.resource;
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (If the parameter is a whole resource.)
         */
        public ParametersParameterComponent setResource(Resource value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return {@link #part} (A named part of a parameter. In many implementation context, a set of named parts is known as a "Tuple".)
         */
        public List<ParametersParameterComponent> getPart() { 
          if (this.part == null)
            this.part = new ArrayList<ParametersParameterComponent>();
          return this.part;
        }

        public boolean hasPart() { 
          if (this.part == null)
            return false;
          for (ParametersParameterComponent item : this.part)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #part} (A named part of a parameter. In many implementation context, a set of named parts is known as a "Tuple".)
         */
    // syntactic sugar
        public ParametersParameterComponent addPart() { //3
          ParametersParameterComponent t = new ParametersParameterComponent();
          if (this.part == null)
            this.part = new ArrayList<ParametersParameterComponent>();
          this.part.add(t);
          return t;
        }

    // syntactic sugar
        public ParametersParameterComponent addPart(ParametersParameterComponent t) { //3
          if (t == null)
            return this;
          if (this.part == null)
            this.part = new ArrayList<ParametersParameterComponent>();
          this.part.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of the parameter (reference to the operation definition).", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("value[x]", "*", "If the parameter is a data type.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("resource", "Resource", "If the parameter is a whole resource.", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("part", "@Parameters.parameter", "A named part of a parameter. In many implementation context, a set of named parts is known as a \"Tuple\".", 0, java.lang.Integer.MAX_VALUE, part));
        }

      public ParametersParameterComponent copy() {
        ParametersParameterComponent dst = new ParametersParameterComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        dst.resource = resource == null ? null : resource.copy();
        if (part != null) {
          dst.part = new ArrayList<ParametersParameterComponent>();
          for (ParametersParameterComponent i : part)
            dst.part.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ParametersParameterComponent))
          return false;
        ParametersParameterComponent o = (ParametersParameterComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true) && compareDeep(resource, o.resource, true)
           && compareDeep(part, o.part, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ParametersParameterComponent))
          return false;
        ParametersParameterComponent o = (ParametersParameterComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (value == null || value.isEmpty())
           && (resource == null || resource.isEmpty()) && (part == null || part.isEmpty());
      }

  }

    /**
     * A parameter passed to or received from the operation.
     */
    @Child(name = "parameter", type = {}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Operation Parameter", formalDefinition="A parameter passed to or received from the operation." )
    protected List<ParametersParameterComponent> parameter;

    private static final long serialVersionUID = -1495940293L;

  /*
   * Constructor
   */
    public Parameters() {
      super();
    }

    /**
     * @return {@link #parameter} (A parameter passed to or received from the operation.)
     */
    public List<ParametersParameterComponent> getParameter() { 
      if (this.parameter == null)
        this.parameter = new ArrayList<ParametersParameterComponent>();
      return this.parameter;
    }

    public boolean hasParameter() { 
      if (this.parameter == null)
        return false;
      for (ParametersParameterComponent item : this.parameter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #parameter} (A parameter passed to or received from the operation.)
     */
    // syntactic sugar
    public ParametersParameterComponent addParameter() { //3
      ParametersParameterComponent t = new ParametersParameterComponent();
      if (this.parameter == null)
        this.parameter = new ArrayList<ParametersParameterComponent>();
      this.parameter.add(t);
      return t;
    }

    // syntactic sugar
    public Parameters addParameter(ParametersParameterComponent t) { //3
      if (t == null)
        return this;
      if (this.parameter == null)
        this.parameter = new ArrayList<ParametersParameterComponent>();
      this.parameter.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("parameter", "", "A parameter passed to or received from the operation.", 0, java.lang.Integer.MAX_VALUE, parameter));
      }

      public Parameters copy() {
        Parameters dst = new Parameters();
        copyValues(dst);
        if (parameter != null) {
          dst.parameter = new ArrayList<ParametersParameterComponent>();
          for (ParametersParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        return dst;
      }

      protected Parameters typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Parameters))
          return false;
        Parameters o = (Parameters) other;
        return compareDeep(parameter, o.parameter, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Parameters))
          return false;
        Parameters o = (Parameters) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (parameter == null || parameter.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Parameters;
   }


}

