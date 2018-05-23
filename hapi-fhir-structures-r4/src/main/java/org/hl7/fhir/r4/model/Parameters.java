package org.hl7.fhir.r4.model;

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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * This special resource type is used to represent an operation request and response (operations.html). It has no other use, and there is no RESTful endpoint associated with it.
 */
@ResourceDef(name="Parameters", profile="http://hl7.org/fhir/Profile/Parameters")
public class Parameters extends Resource implements IBaseParameters {

    @Block()
    public static class ParametersParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the parameter (reference to the operation definition).
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name from the definition", formalDefinition="The name of the parameter (reference to the operation definition)." )
        protected StringType name;

        /**
         * If the parameter is a data type.
         */
        @Child(name = "value", type = {}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="If parameter is a data type", formalDefinition="If the parameter is a data type." )
        protected org.hl7.fhir.r4.model.Type value;

        /**
         * If the parameter is a whole resource.
         */
        @Child(name = "resource", type = {Resource.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="If parameter is a whole resource", formalDefinition="If the parameter is a whole resource." )
        protected Resource resource;

        /**
         * A named part of a multi-part parameter.
         */
        @Child(name = "part", type = {ParametersParameterComponent.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Named part of a multi-part parameter", formalDefinition="A named part of a multi-part parameter." )
        protected List<ParametersParameterComponent> part;

        private static final long serialVersionUID = -1265707999L;

    /**
     * Constructor
     */
      public ParametersParameterComponent() {
        super();
      }

    /**
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
        public org.hl7.fhir.r4.model.Type getValue() { 
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (If the parameter is a data type.)
         */
        public ParametersParameterComponent setValue(org.hl7.fhir.r4.model.Type value) { 
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
         * @return {@link #part} (A named part of a multi-part parameter.)
         */
        public List<ParametersParameterComponent> getPart() { 
          if (this.part == null)
            this.part = new ArrayList<ParametersParameterComponent>();
          return this.part;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ParametersParameterComponent setPart(List<ParametersParameterComponent> thePart) { 
          this.part = thePart;
          return this;
        }

        public boolean hasPart() { 
          if (this.part == null)
            return false;
          for (ParametersParameterComponent item : this.part)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ParametersParameterComponent addPart() { //3
          ParametersParameterComponent t = new ParametersParameterComponent();
          if (this.part == null)
            this.part = new ArrayList<ParametersParameterComponent>();
          this.part.add(t);
          return t;
        }

        public ParametersParameterComponent addPart(ParametersParameterComponent t) { //3
          if (t == null)
            return this;
          if (this.part == null)
            this.part = new ArrayList<ParametersParameterComponent>();
          this.part.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #part}, creating it if it does not already exist
         */
        public ParametersParameterComponent getPartFirstRep() { 
          if (getPart().isEmpty()) {
            addPart();
          }
          return getPart().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "The name of the parameter (reference to the operation definition).", 0, 1, name));
          children.add(new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value));
          children.add(new Property("resource", "Resource", "If the parameter is a whole resource.", 0, 1, resource));
          children.add(new Property("part", "@Parameters.parameter", "A named part of a multi-part parameter.", 0, java.lang.Integer.MAX_VALUE, part));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "The name of the parameter (reference to the operation definition).", 0, 1, name);
          case -1410166417: /*value[x]*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1535024575: /*valueBase64Binary*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case 733421943: /*valueBoolean*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -786218365: /*valueCanonical*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -766209282: /*valueCode*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -766192449: /*valueDate*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case 1047929900: /*valueDateTime*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -2083993440: /*valueDecimal*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case 231604844: /*valueId*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1668687056: /*valueInstant*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1668204915: /*valueInteger*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -497880704: /*valueMarkdown*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1410178407: /*valueOid*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1249932027: /*valuePositiveInt*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1424603934: /*valueString*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -765708322: /*valueTime*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case 26529417: /*valueUnsignedInt*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1410172357: /*valueUri*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1410172354: /*valueUrl*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -765667124: /*valueUuid*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -478981821: /*valueAddress*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -67108992: /*valueAnnotation*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -475566732: /*valueAttachment*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case 924902896: /*valueCodeableConcept*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1887705029: /*valueCoding*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case 944904545: /*valueContactPoint*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -2026205465: /*valueHumanName*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -130498310: /*valueIdentifier*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1524344174: /*valuePeriod*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -2029823716: /*valueQuantity*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case 2030761548: /*valueRange*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case 2030767386: /*valueRatio*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case 1755241690: /*valueReference*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -962229101: /*valueSampledData*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -540985785: /*valueSignature*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1406282469: /*valueTiming*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -1858636920: /*valueDosage*/  return new Property("value[x]", "*", "If the parameter is a data type.", 0, 1, value);
          case -341064690: /*resource*/  return new Property("resource", "Resource", "If the parameter is a whole resource.", 0, 1, resource);
          case 3433459: /*part*/  return new Property("part", "@Parameters.parameter", "A named part of a multi-part parameter.", 0, java.lang.Integer.MAX_VALUE, part);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // org.hl7.fhir.r4.model.Type
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // Resource
        case 3433459: /*part*/ return this.part == null ? new Base[0] : this.part.toArray(new Base[this.part.size()]); // ParametersParameterComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 111972721: // value
          this.value = castToType(value); // org.hl7.fhir.r4.model.Type
          return value;
        case -341064690: // resource
          this.resource = castToResource(value); // Resource
          return value;
        case 3433459: // part
          this.getPart().add((ParametersParameterComponent) value); // ParametersParameterComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // org.hl7.fhir.r4.model.Type
        } else if (name.equals("resource")) {
          this.resource = castToResource(value); // Resource
        } else if (name.equals("part")) {
          this.getPart().add((ParametersParameterComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        case -341064690: throw new FHIRException("Cannot make property resource as it is not a complex type"); // Resource
        case 3433459:  return addPart(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 111972721: /*value*/ return new String[] {"*"};
        case -341064690: /*resource*/ return new String[] {"Resource"};
        case 3433459: /*part*/ return new String[] {"@Parameters.parameter"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Parameters.name");
        }
        else if (name.equals("valueBase64Binary")) {
          this.value = new Base64BinaryType();
          return this.value;
        }
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueCanonical")) {
          this.value = new CanonicalType();
          return this.value;
        }
        else if (name.equals("valueCode")) {
          this.value = new CodeType();
          return this.value;
        }
        else if (name.equals("valueDate")) {
          this.value = new DateType();
          return this.value;
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valueDecimal")) {
          this.value = new DecimalType();
          return this.value;
        }
        else if (name.equals("valueId")) {
          this.value = new IdType();
          return this.value;
        }
        else if (name.equals("valueInstant")) {
          this.value = new InstantType();
          return this.value;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valueMarkdown")) {
          this.value = new MarkdownType();
          return this.value;
        }
        else if (name.equals("valueOid")) {
          this.value = new OidType();
          return this.value;
        }
        else if (name.equals("valuePositiveInt")) {
          this.value = new PositiveIntType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueUnsignedInt")) {
          this.value = new UnsignedIntType();
          return this.value;
        }
        else if (name.equals("valueUri")) {
          this.value = new UriType();
          return this.value;
        }
        else if (name.equals("valueUrl")) {
          this.value = new UrlType();
          return this.value;
        }
        else if (name.equals("valueUuid")) {
          this.value = new UuidType();
          return this.value;
        }
        else if (name.equals("valueAddress")) {
          this.value = new Address();
          return this.value;
        }
        else if (name.equals("valueAge")) {
          this.value = new Age();
          return this.value;
        }
        else if (name.equals("valueAnnotation")) {
          this.value = new Annotation();
          return this.value;
        }
        else if (name.equals("valueAttachment")) {
          this.value = new Attachment();
          return this.value;
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueContactPoint")) {
          this.value = new ContactPoint();
          return this.value;
        }
        else if (name.equals("valueCount")) {
          this.value = new Count();
          return this.value;
        }
        else if (name.equals("valueDistance")) {
          this.value = new Distance();
          return this.value;
        }
        else if (name.equals("valueDuration")) {
          this.value = new Duration();
          return this.value;
        }
        else if (name.equals("valueHumanName")) {
          this.value = new HumanName();
          return this.value;
        }
        else if (name.equals("valueIdentifier")) {
          this.value = new Identifier();
          return this.value;
        }
        else if (name.equals("valueMoney")) {
          this.value = new Money();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueRange")) {
          this.value = new Range();
          return this.value;
        }
        else if (name.equals("valueRatio")) {
          this.value = new Ratio();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
          return this.value;
        }
        else if (name.equals("valueSampledData")) {
          this.value = new SampledData();
          return this.value;
        }
        else if (name.equals("valueSignature")) {
          this.value = new Signature();
          return this.value;
        }
        else if (name.equals("valueTiming")) {
          this.value = new Timing();
          return this.value;
        }
        else if (name.equals("valueParameterDefinition")) {
          this.value = new ParameterDefinition();
          return this.value;
        }
        else if (name.equals("valueDataRequirement")) {
          this.value = new DataRequirement();
          return this.value;
        }
        else if (name.equals("valueRelatedArtifact")) {
          this.value = new RelatedArtifact();
          return this.value;
        }
        else if (name.equals("valueContactDetail")) {
          this.value = new ContactDetail();
          return this.value;
        }
        else if (name.equals("valueContributor")) {
          this.value = new Contributor();
          return this.value;
        }
        else if (name.equals("valueTriggerDefinition")) {
          this.value = new TriggerDefinition();
          return this.value;
        }
        else if (name.equals("valueUsageContext")) {
          this.value = new UsageContext();
          return this.value;
        }
        else if (name.equals("valueDosage")) {
          this.value = new Dosage();
          return this.value;
        }
        else if (name.equals("resource")) {
          throw new FHIRException("Cannot call addChild on an abstract type Parameters.resource");
        }
        else if (name.equals("part")) {
          return addPart();
        }
        else
          return super.addChild(name);
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ParametersParameterComponent))
          return false;
        ParametersParameterComponent o = (ParametersParameterComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true) && compareDeep(resource, o.resource, true)
           && compareDeep(part, o.part, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ParametersParameterComponent))
          return false;
        ParametersParameterComponent o = (ParametersParameterComponent) other_;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, value, resource, part
          );
      }

  public String fhirType() {
    return "Parameters.parameter";

  }

  }

    /**
     * A parameter passed to or received from the operation.
     */
    @Child(name = "parameter", type = {}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Operation Parameter", formalDefinition="A parameter passed to or received from the operation." )
    protected List<ParametersParameterComponent> parameter;

    private static final long serialVersionUID = -1495940293L;

  /**
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Parameters setParameter(List<ParametersParameterComponent> theParameter) { 
      this.parameter = theParameter;
      return this;
    }

    public boolean hasParameter() { 
      if (this.parameter == null)
        return false;
      for (ParametersParameterComponent item : this.parameter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ParametersParameterComponent addParameter() { //3
      ParametersParameterComponent t = new ParametersParameterComponent();
      if (this.parameter == null)
        this.parameter = new ArrayList<ParametersParameterComponent>();
      this.parameter.add(t);
      return t;
    }

    public Parameters addParameter(ParametersParameterComponent t) { //3
      if (t == null)
        return this;
      if (this.parameter == null)
        this.parameter = new ArrayList<ParametersParameterComponent>();
      this.parameter.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #parameter}, creating it if it does not already exist
     */
    public ParametersParameterComponent getParameterFirstRep() { 
      if (getParameter().isEmpty()) {
        addParameter();
      }
      return getParameter().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("parameter", "", "A parameter passed to or received from the operation.", 0, java.lang.Integer.MAX_VALUE, parameter));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 1954460585: /*parameter*/  return new Property("parameter", "", "A parameter passed to or received from the operation.", 0, java.lang.Integer.MAX_VALUE, parameter);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1954460585: /*parameter*/ return this.parameter == null ? new Base[0] : this.parameter.toArray(new Base[this.parameter.size()]); // ParametersParameterComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1954460585: // parameter
          this.getParameter().add((ParametersParameterComponent) value); // ParametersParameterComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("parameter")) {
          this.getParameter().add((ParametersParameterComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1954460585:  return addParameter(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1954460585: /*parameter*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("parameter")) {
          return addParameter();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Parameters";

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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Parameters))
          return false;
        Parameters o = (Parameters) other_;
        return compareDeep(parameter, o.parameter, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Parameters))
          return false;
        Parameters o = (Parameters) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(parameter);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Parameters;
   }


}

