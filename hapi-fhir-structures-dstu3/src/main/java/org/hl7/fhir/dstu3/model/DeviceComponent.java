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
 * The characteristics, operational status and capabilities of a medical-related component of a medical device.
 */
@ResourceDef(name="DeviceComponent", profile="http://hl7.org/fhir/Profile/DeviceComponent")
public class DeviceComponent extends DomainResource {

    public enum MeasmntPrinciple {
        /**
         * Measurement principle isn't in the list.
         */
        OTHER, 
        /**
         * Measurement is done using the chemical principle.
         */
        CHEMICAL, 
        /**
         * Measurement is done using the electrical principle.
         */
        ELECTRICAL, 
        /**
         * Measurement is done using the impedance principle.
         */
        IMPEDANCE, 
        /**
         * Measurement is done using the nuclear principle.
         */
        NUCLEAR, 
        /**
         * Measurement is done using the optical principle.
         */
        OPTICAL, 
        /**
         * Measurement is done using the thermal principle.
         */
        THERMAL, 
        /**
         * Measurement is done using the biological principle.
         */
        BIOLOGICAL, 
        /**
         * Measurement is done using the mechanical principle.
         */
        MECHANICAL, 
        /**
         * Measurement is done using the acoustical principle.
         */
        ACOUSTICAL, 
        /**
         * Measurement is done using the manual principle.
         */
        MANUAL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MeasmntPrinciple fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("other".equals(codeString))
          return OTHER;
        if ("chemical".equals(codeString))
          return CHEMICAL;
        if ("electrical".equals(codeString))
          return ELECTRICAL;
        if ("impedance".equals(codeString))
          return IMPEDANCE;
        if ("nuclear".equals(codeString))
          return NUCLEAR;
        if ("optical".equals(codeString))
          return OPTICAL;
        if ("thermal".equals(codeString))
          return THERMAL;
        if ("biological".equals(codeString))
          return BIOLOGICAL;
        if ("mechanical".equals(codeString))
          return MECHANICAL;
        if ("acoustical".equals(codeString))
          return ACOUSTICAL;
        if ("manual".equals(codeString))
          return MANUAL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MeasmntPrinciple code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OTHER: return "other";
            case CHEMICAL: return "chemical";
            case ELECTRICAL: return "electrical";
            case IMPEDANCE: return "impedance";
            case NUCLEAR: return "nuclear";
            case OPTICAL: return "optical";
            case THERMAL: return "thermal";
            case BIOLOGICAL: return "biological";
            case MECHANICAL: return "mechanical";
            case ACOUSTICAL: return "acoustical";
            case MANUAL: return "manual";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case OTHER: return "http://hl7.org/fhir/measurement-principle";
            case CHEMICAL: return "http://hl7.org/fhir/measurement-principle";
            case ELECTRICAL: return "http://hl7.org/fhir/measurement-principle";
            case IMPEDANCE: return "http://hl7.org/fhir/measurement-principle";
            case NUCLEAR: return "http://hl7.org/fhir/measurement-principle";
            case OPTICAL: return "http://hl7.org/fhir/measurement-principle";
            case THERMAL: return "http://hl7.org/fhir/measurement-principle";
            case BIOLOGICAL: return "http://hl7.org/fhir/measurement-principle";
            case MECHANICAL: return "http://hl7.org/fhir/measurement-principle";
            case ACOUSTICAL: return "http://hl7.org/fhir/measurement-principle";
            case MANUAL: return "http://hl7.org/fhir/measurement-principle";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case OTHER: return "Measurement principle isn't in the list.";
            case CHEMICAL: return "Measurement is done using the chemical principle.";
            case ELECTRICAL: return "Measurement is done using the electrical principle.";
            case IMPEDANCE: return "Measurement is done using the impedance principle.";
            case NUCLEAR: return "Measurement is done using the nuclear principle.";
            case OPTICAL: return "Measurement is done using the optical principle.";
            case THERMAL: return "Measurement is done using the thermal principle.";
            case BIOLOGICAL: return "Measurement is done using the biological principle.";
            case MECHANICAL: return "Measurement is done using the mechanical principle.";
            case ACOUSTICAL: return "Measurement is done using the acoustical principle.";
            case MANUAL: return "Measurement is done using the manual principle.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OTHER: return "MSP Other";
            case CHEMICAL: return "MSP Chemical";
            case ELECTRICAL: return "MSP Electrical";
            case IMPEDANCE: return "MSP Impedance";
            case NUCLEAR: return "MSP Nuclear";
            case OPTICAL: return "MSP Optical";
            case THERMAL: return "MSP Thermal";
            case BIOLOGICAL: return "MSP Biological";
            case MECHANICAL: return "MSP Mechanical";
            case ACOUSTICAL: return "MSP Acoustical";
            case MANUAL: return "MSP Manual";
            default: return "?";
          }
        }
    }

  public static class MeasmntPrincipleEnumFactory implements EnumFactory<MeasmntPrinciple> {
    public MeasmntPrinciple fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("other".equals(codeString))
          return MeasmntPrinciple.OTHER;
        if ("chemical".equals(codeString))
          return MeasmntPrinciple.CHEMICAL;
        if ("electrical".equals(codeString))
          return MeasmntPrinciple.ELECTRICAL;
        if ("impedance".equals(codeString))
          return MeasmntPrinciple.IMPEDANCE;
        if ("nuclear".equals(codeString))
          return MeasmntPrinciple.NUCLEAR;
        if ("optical".equals(codeString))
          return MeasmntPrinciple.OPTICAL;
        if ("thermal".equals(codeString))
          return MeasmntPrinciple.THERMAL;
        if ("biological".equals(codeString))
          return MeasmntPrinciple.BIOLOGICAL;
        if ("mechanical".equals(codeString))
          return MeasmntPrinciple.MECHANICAL;
        if ("acoustical".equals(codeString))
          return MeasmntPrinciple.ACOUSTICAL;
        if ("manual".equals(codeString))
          return MeasmntPrinciple.MANUAL;
        throw new IllegalArgumentException("Unknown MeasmntPrinciple code '"+codeString+"'");
        }
        public Enumeration<MeasmntPrinciple> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MeasmntPrinciple>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("other".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.OTHER);
        if ("chemical".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.CHEMICAL);
        if ("electrical".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.ELECTRICAL);
        if ("impedance".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.IMPEDANCE);
        if ("nuclear".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.NUCLEAR);
        if ("optical".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.OPTICAL);
        if ("thermal".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.THERMAL);
        if ("biological".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.BIOLOGICAL);
        if ("mechanical".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.MECHANICAL);
        if ("acoustical".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.ACOUSTICAL);
        if ("manual".equals(codeString))
          return new Enumeration<MeasmntPrinciple>(this, MeasmntPrinciple.MANUAL);
        throw new FHIRException("Unknown MeasmntPrinciple code '"+codeString+"'");
        }
    public String toCode(MeasmntPrinciple code) {
      if (code == MeasmntPrinciple.OTHER)
        return "other";
      if (code == MeasmntPrinciple.CHEMICAL)
        return "chemical";
      if (code == MeasmntPrinciple.ELECTRICAL)
        return "electrical";
      if (code == MeasmntPrinciple.IMPEDANCE)
        return "impedance";
      if (code == MeasmntPrinciple.NUCLEAR)
        return "nuclear";
      if (code == MeasmntPrinciple.OPTICAL)
        return "optical";
      if (code == MeasmntPrinciple.THERMAL)
        return "thermal";
      if (code == MeasmntPrinciple.BIOLOGICAL)
        return "biological";
      if (code == MeasmntPrinciple.MECHANICAL)
        return "mechanical";
      if (code == MeasmntPrinciple.ACOUSTICAL)
        return "acoustical";
      if (code == MeasmntPrinciple.MANUAL)
        return "manual";
      return "?";
      }
    public String toSystem(MeasmntPrinciple code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class DeviceComponentProductionSpecificationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The specification type, such as, serial number, part number, hardware revision, software revision, etc.
         */
        @Child(name = "specType", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type or kind of production specification, for example serial number or software revision", formalDefinition="The specification type, such as, serial number, part number, hardware revision, software revision, etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/specification-type")
        protected CodeableConcept specType;

        /**
         * The internal component unique identification. This is a provision for manufacture specific standard components using a private OID. 11073-10101 has a partition for private OID semantic that the manufacturer can make use of.
         */
        @Child(name = "componentId", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Internal component unique identification", formalDefinition="The internal component unique identification. This is a provision for manufacture specific standard components using a private OID. 11073-10101 has a partition for private OID semantic that the manufacturer can make use of." )
        protected Identifier componentId;

        /**
         * The printable string defining the component.
         */
        @Child(name = "productionSpec", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A printable string defining the component", formalDefinition="The printable string defining the component." )
        protected StringType productionSpec;

        private static final long serialVersionUID = -1476597516L;

    /**
     * Constructor
     */
      public DeviceComponentProductionSpecificationComponent() {
        super();
      }

        /**
         * @return {@link #specType} (The specification type, such as, serial number, part number, hardware revision, software revision, etc.)
         */
        public CodeableConcept getSpecType() { 
          if (this.specType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceComponentProductionSpecificationComponent.specType");
            else if (Configuration.doAutoCreate())
              this.specType = new CodeableConcept(); // cc
          return this.specType;
        }

        public boolean hasSpecType() { 
          return this.specType != null && !this.specType.isEmpty();
        }

        /**
         * @param value {@link #specType} (The specification type, such as, serial number, part number, hardware revision, software revision, etc.)
         */
        public DeviceComponentProductionSpecificationComponent setSpecType(CodeableConcept value) { 
          this.specType = value;
          return this;
        }

        /**
         * @return {@link #componentId} (The internal component unique identification. This is a provision for manufacture specific standard components using a private OID. 11073-10101 has a partition for private OID semantic that the manufacturer can make use of.)
         */
        public Identifier getComponentId() { 
          if (this.componentId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceComponentProductionSpecificationComponent.componentId");
            else if (Configuration.doAutoCreate())
              this.componentId = new Identifier(); // cc
          return this.componentId;
        }

        public boolean hasComponentId() { 
          return this.componentId != null && !this.componentId.isEmpty();
        }

        /**
         * @param value {@link #componentId} (The internal component unique identification. This is a provision for manufacture specific standard components using a private OID. 11073-10101 has a partition for private OID semantic that the manufacturer can make use of.)
         */
        public DeviceComponentProductionSpecificationComponent setComponentId(Identifier value) { 
          this.componentId = value;
          return this;
        }

        /**
         * @return {@link #productionSpec} (The printable string defining the component.). This is the underlying object with id, value and extensions. The accessor "getProductionSpec" gives direct access to the value
         */
        public StringType getProductionSpecElement() { 
          if (this.productionSpec == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceComponentProductionSpecificationComponent.productionSpec");
            else if (Configuration.doAutoCreate())
              this.productionSpec = new StringType(); // bb
          return this.productionSpec;
        }

        public boolean hasProductionSpecElement() { 
          return this.productionSpec != null && !this.productionSpec.isEmpty();
        }

        public boolean hasProductionSpec() { 
          return this.productionSpec != null && !this.productionSpec.isEmpty();
        }

        /**
         * @param value {@link #productionSpec} (The printable string defining the component.). This is the underlying object with id, value and extensions. The accessor "getProductionSpec" gives direct access to the value
         */
        public DeviceComponentProductionSpecificationComponent setProductionSpecElement(StringType value) { 
          this.productionSpec = value;
          return this;
        }

        /**
         * @return The printable string defining the component.
         */
        public String getProductionSpec() { 
          return this.productionSpec == null ? null : this.productionSpec.getValue();
        }

        /**
         * @param value The printable string defining the component.
         */
        public DeviceComponentProductionSpecificationComponent setProductionSpec(String value) { 
          if (Utilities.noString(value))
            this.productionSpec = null;
          else {
            if (this.productionSpec == null)
              this.productionSpec = new StringType();
            this.productionSpec.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("specType", "CodeableConcept", "The specification type, such as, serial number, part number, hardware revision, software revision, etc.", 0, java.lang.Integer.MAX_VALUE, specType));
          childrenList.add(new Property("componentId", "Identifier", "The internal component unique identification. This is a provision for manufacture specific standard components using a private OID. 11073-10101 has a partition for private OID semantic that the manufacturer can make use of.", 0, java.lang.Integer.MAX_VALUE, componentId));
          childrenList.add(new Property("productionSpec", "string", "The printable string defining the component.", 0, java.lang.Integer.MAX_VALUE, productionSpec));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -2133482091: /*specType*/ return this.specType == null ? new Base[0] : new Base[] {this.specType}; // CodeableConcept
        case -985933064: /*componentId*/ return this.componentId == null ? new Base[0] : new Base[] {this.componentId}; // Identifier
        case 182147092: /*productionSpec*/ return this.productionSpec == null ? new Base[0] : new Base[] {this.productionSpec}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -2133482091: // specType
          this.specType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -985933064: // componentId
          this.componentId = castToIdentifier(value); // Identifier
          return value;
        case 182147092: // productionSpec
          this.productionSpec = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("specType")) {
          this.specType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("componentId")) {
          this.componentId = castToIdentifier(value); // Identifier
        } else if (name.equals("productionSpec")) {
          this.productionSpec = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -2133482091:  return getSpecType(); 
        case -985933064:  return getComponentId(); 
        case 182147092:  return getProductionSpecElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -2133482091: /*specType*/ return new String[] {"CodeableConcept"};
        case -985933064: /*componentId*/ return new String[] {"Identifier"};
        case 182147092: /*productionSpec*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("specType")) {
          this.specType = new CodeableConcept();
          return this.specType;
        }
        else if (name.equals("componentId")) {
          this.componentId = new Identifier();
          return this.componentId;
        }
        else if (name.equals("productionSpec")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceComponent.productionSpec");
        }
        else
          return super.addChild(name);
      }

      public DeviceComponentProductionSpecificationComponent copy() {
        DeviceComponentProductionSpecificationComponent dst = new DeviceComponentProductionSpecificationComponent();
        copyValues(dst);
        dst.specType = specType == null ? null : specType.copy();
        dst.componentId = componentId == null ? null : componentId.copy();
        dst.productionSpec = productionSpec == null ? null : productionSpec.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DeviceComponentProductionSpecificationComponent))
          return false;
        DeviceComponentProductionSpecificationComponent o = (DeviceComponentProductionSpecificationComponent) other;
        return compareDeep(specType, o.specType, true) && compareDeep(componentId, o.componentId, true)
           && compareDeep(productionSpec, o.productionSpec, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DeviceComponentProductionSpecificationComponent))
          return false;
        DeviceComponentProductionSpecificationComponent o = (DeviceComponentProductionSpecificationComponent) other;
        return compareValues(productionSpec, o.productionSpec, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(specType, componentId, productionSpec
          );
      }

  public String fhirType() {
    return "DeviceComponent.productionSpecification";

  }

  }

    /**
     * The locally assigned unique identification by the software. For example: handle ID.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Instance id assigned by the software stack", formalDefinition="The locally assigned unique identification by the software. For example: handle ID." )
    protected Identifier identifier;

    /**
     * The component type as defined in the object-oriented or metric nomenclature partition.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What kind of component it is", formalDefinition="The component type as defined in the object-oriented or metric nomenclature partition." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-kind")
    protected CodeableConcept type;

    /**
     * The timestamp for the most recent system change which includes device configuration or setting change.
     */
    @Child(name = "lastSystemChange", type = {InstantType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Recent system change timestamp", formalDefinition="The timestamp for the most recent system change which includes device configuration or setting change." )
    protected InstantType lastSystemChange;

    /**
     * The link to the source Device that contains administrative device information such as manufacture, serial number, etc.
     */
    @Child(name = "source", type = {Device.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Top-level device resource link", formalDefinition="The link to the source Device that contains administrative device information such as manufacture, serial number, etc." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (The link to the source Device that contains administrative device information such as manufacture, serial number, etc.)
     */
    protected Device sourceTarget;

    /**
     * The link to the parent resource. For example: Channel is linked to its VMD parent.
     */
    @Child(name = "parent", type = {DeviceComponent.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Parent resource link", formalDefinition="The link to the parent resource. For example: Channel is linked to its VMD parent." )
    protected Reference parent;

    /**
     * The actual object that is the target of the reference (The link to the parent resource. For example: Channel is linked to its VMD parent.)
     */
    protected DeviceComponent parentTarget;

    /**
     * The current operational status of the device. For example: On, Off, Standby, etc.
     */
    @Child(name = "operationalStatus", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Current operational status of the component, for example On, Off or Standby", formalDefinition="The current operational status of the device. For example: On, Off, Standby, etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/operational-status")
    protected List<CodeableConcept> operationalStatus;

    /**
     * The parameter group supported by the current device component that is based on some nomenclature, e.g. cardiovascular.
     */
    @Child(name = "parameterGroup", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Current supported parameter group", formalDefinition="The parameter group supported by the current device component that is based on some nomenclature, e.g. cardiovascular." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/parameter-group")
    protected CodeableConcept parameterGroup;

    /**
     * The physical principle of the measurement. For example: thermal, chemical, acoustical, etc.
     */
    @Child(name = "measurementPrinciple", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="other | chemical | electrical | impedance | nuclear | optical | thermal | biological | mechanical | acoustical | manual+", formalDefinition="The physical principle of the measurement. For example: thermal, chemical, acoustical, etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/measurement-principle")
    protected Enumeration<MeasmntPrinciple> measurementPrinciple;

    /**
     * The production specification such as component revision, serial number, etc.
     */
    @Child(name = "productionSpecification", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Specification details such as Component Revisions, or Serial Numbers", formalDefinition="The production specification such as component revision, serial number, etc." )
    protected List<DeviceComponentProductionSpecificationComponent> productionSpecification;

    /**
     * The language code for the human-readable text string produced by the device. This language code will follow the IETF language tag. Example: en-US.
     */
    @Child(name = "languageCode", type = {CodeableConcept.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Language code for the human-readable text strings produced by the device", formalDefinition="The language code for the human-readable text string produced by the device. This language code will follow the IETF language tag. Example: en-US." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/languages")
    protected CodeableConcept languageCode;

    private static final long serialVersionUID = 32987426L;

  /**
   * Constructor
   */
    public DeviceComponent() {
      super();
    }

  /**
   * Constructor
   */
    public DeviceComponent(Identifier identifier, CodeableConcept type) {
      super();
      this.identifier = identifier;
      this.type = type;
    }

    /**
     * @return {@link #identifier} (The locally assigned unique identification by the software. For example: handle ID.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceComponent.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (The locally assigned unique identification by the software. For example: handle ID.)
     */
    public DeviceComponent setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #type} (The component type as defined in the object-oriented or metric nomenclature partition.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceComponent.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The component type as defined in the object-oriented or metric nomenclature partition.)
     */
    public DeviceComponent setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #lastSystemChange} (The timestamp for the most recent system change which includes device configuration or setting change.). This is the underlying object with id, value and extensions. The accessor "getLastSystemChange" gives direct access to the value
     */
    public InstantType getLastSystemChangeElement() { 
      if (this.lastSystemChange == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceComponent.lastSystemChange");
        else if (Configuration.doAutoCreate())
          this.lastSystemChange = new InstantType(); // bb
      return this.lastSystemChange;
    }

    public boolean hasLastSystemChangeElement() { 
      return this.lastSystemChange != null && !this.lastSystemChange.isEmpty();
    }

    public boolean hasLastSystemChange() { 
      return this.lastSystemChange != null && !this.lastSystemChange.isEmpty();
    }

    /**
     * @param value {@link #lastSystemChange} (The timestamp for the most recent system change which includes device configuration or setting change.). This is the underlying object with id, value and extensions. The accessor "getLastSystemChange" gives direct access to the value
     */
    public DeviceComponent setLastSystemChangeElement(InstantType value) { 
      this.lastSystemChange = value;
      return this;
    }

    /**
     * @return The timestamp for the most recent system change which includes device configuration or setting change.
     */
    public Date getLastSystemChange() { 
      return this.lastSystemChange == null ? null : this.lastSystemChange.getValue();
    }

    /**
     * @param value The timestamp for the most recent system change which includes device configuration or setting change.
     */
    public DeviceComponent setLastSystemChange(Date value) { 
      if (value == null)
        this.lastSystemChange = null;
      else {
        if (this.lastSystemChange == null)
          this.lastSystemChange = new InstantType();
        this.lastSystemChange.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #source} (The link to the source Device that contains administrative device information such as manufacture, serial number, etc.)
     */
    public Reference getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceComponent.source");
        else if (Configuration.doAutoCreate())
          this.source = new Reference(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (The link to the source Device that contains administrative device information such as manufacture, serial number, etc.)
     */
    public DeviceComponent setSource(Reference value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The link to the source Device that contains administrative device information such as manufacture, serial number, etc.)
     */
    public Device getSourceTarget() { 
      if (this.sourceTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceComponent.source");
        else if (Configuration.doAutoCreate())
          this.sourceTarget = new Device(); // aa
      return this.sourceTarget;
    }

    /**
     * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The link to the source Device that contains administrative device information such as manufacture, serial number, etc.)
     */
    public DeviceComponent setSourceTarget(Device value) { 
      this.sourceTarget = value;
      return this;
    }

    /**
     * @return {@link #parent} (The link to the parent resource. For example: Channel is linked to its VMD parent.)
     */
    public Reference getParent() { 
      if (this.parent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceComponent.parent");
        else if (Configuration.doAutoCreate())
          this.parent = new Reference(); // cc
      return this.parent;
    }

    public boolean hasParent() { 
      return this.parent != null && !this.parent.isEmpty();
    }

    /**
     * @param value {@link #parent} (The link to the parent resource. For example: Channel is linked to its VMD parent.)
     */
    public DeviceComponent setParent(Reference value) { 
      this.parent = value;
      return this;
    }

    /**
     * @return {@link #parent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The link to the parent resource. For example: Channel is linked to its VMD parent.)
     */
    public DeviceComponent getParentTarget() { 
      if (this.parentTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceComponent.parent");
        else if (Configuration.doAutoCreate())
          this.parentTarget = new DeviceComponent(); // aa
      return this.parentTarget;
    }

    /**
     * @param value {@link #parent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The link to the parent resource. For example: Channel is linked to its VMD parent.)
     */
    public DeviceComponent setParentTarget(DeviceComponent value) { 
      this.parentTarget = value;
      return this;
    }

    /**
     * @return {@link #operationalStatus} (The current operational status of the device. For example: On, Off, Standby, etc.)
     */
    public List<CodeableConcept> getOperationalStatus() { 
      if (this.operationalStatus == null)
        this.operationalStatus = new ArrayList<CodeableConcept>();
      return this.operationalStatus;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceComponent setOperationalStatus(List<CodeableConcept> theOperationalStatus) { 
      this.operationalStatus = theOperationalStatus;
      return this;
    }

    public boolean hasOperationalStatus() { 
      if (this.operationalStatus == null)
        return false;
      for (CodeableConcept item : this.operationalStatus)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addOperationalStatus() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.operationalStatus == null)
        this.operationalStatus = new ArrayList<CodeableConcept>();
      this.operationalStatus.add(t);
      return t;
    }

    public DeviceComponent addOperationalStatus(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.operationalStatus == null)
        this.operationalStatus = new ArrayList<CodeableConcept>();
      this.operationalStatus.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #operationalStatus}, creating it if it does not already exist
     */
    public CodeableConcept getOperationalStatusFirstRep() { 
      if (getOperationalStatus().isEmpty()) {
        addOperationalStatus();
      }
      return getOperationalStatus().get(0);
    }

    /**
     * @return {@link #parameterGroup} (The parameter group supported by the current device component that is based on some nomenclature, e.g. cardiovascular.)
     */
    public CodeableConcept getParameterGroup() { 
      if (this.parameterGroup == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceComponent.parameterGroup");
        else if (Configuration.doAutoCreate())
          this.parameterGroup = new CodeableConcept(); // cc
      return this.parameterGroup;
    }

    public boolean hasParameterGroup() { 
      return this.parameterGroup != null && !this.parameterGroup.isEmpty();
    }

    /**
     * @param value {@link #parameterGroup} (The parameter group supported by the current device component that is based on some nomenclature, e.g. cardiovascular.)
     */
    public DeviceComponent setParameterGroup(CodeableConcept value) { 
      this.parameterGroup = value;
      return this;
    }

    /**
     * @return {@link #measurementPrinciple} (The physical principle of the measurement. For example: thermal, chemical, acoustical, etc.). This is the underlying object with id, value and extensions. The accessor "getMeasurementPrinciple" gives direct access to the value
     */
    public Enumeration<MeasmntPrinciple> getMeasurementPrincipleElement() { 
      if (this.measurementPrinciple == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceComponent.measurementPrinciple");
        else if (Configuration.doAutoCreate())
          this.measurementPrinciple = new Enumeration<MeasmntPrinciple>(new MeasmntPrincipleEnumFactory()); // bb
      return this.measurementPrinciple;
    }

    public boolean hasMeasurementPrincipleElement() { 
      return this.measurementPrinciple != null && !this.measurementPrinciple.isEmpty();
    }

    public boolean hasMeasurementPrinciple() { 
      return this.measurementPrinciple != null && !this.measurementPrinciple.isEmpty();
    }

    /**
     * @param value {@link #measurementPrinciple} (The physical principle of the measurement. For example: thermal, chemical, acoustical, etc.). This is the underlying object with id, value and extensions. The accessor "getMeasurementPrinciple" gives direct access to the value
     */
    public DeviceComponent setMeasurementPrincipleElement(Enumeration<MeasmntPrinciple> value) { 
      this.measurementPrinciple = value;
      return this;
    }

    /**
     * @return The physical principle of the measurement. For example: thermal, chemical, acoustical, etc.
     */
    public MeasmntPrinciple getMeasurementPrinciple() { 
      return this.measurementPrinciple == null ? null : this.measurementPrinciple.getValue();
    }

    /**
     * @param value The physical principle of the measurement. For example: thermal, chemical, acoustical, etc.
     */
    public DeviceComponent setMeasurementPrinciple(MeasmntPrinciple value) { 
      if (value == null)
        this.measurementPrinciple = null;
      else {
        if (this.measurementPrinciple == null)
          this.measurementPrinciple = new Enumeration<MeasmntPrinciple>(new MeasmntPrincipleEnumFactory());
        this.measurementPrinciple.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #productionSpecification} (The production specification such as component revision, serial number, etc.)
     */
    public List<DeviceComponentProductionSpecificationComponent> getProductionSpecification() { 
      if (this.productionSpecification == null)
        this.productionSpecification = new ArrayList<DeviceComponentProductionSpecificationComponent>();
      return this.productionSpecification;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceComponent setProductionSpecification(List<DeviceComponentProductionSpecificationComponent> theProductionSpecification) { 
      this.productionSpecification = theProductionSpecification;
      return this;
    }

    public boolean hasProductionSpecification() { 
      if (this.productionSpecification == null)
        return false;
      for (DeviceComponentProductionSpecificationComponent item : this.productionSpecification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DeviceComponentProductionSpecificationComponent addProductionSpecification() { //3
      DeviceComponentProductionSpecificationComponent t = new DeviceComponentProductionSpecificationComponent();
      if (this.productionSpecification == null)
        this.productionSpecification = new ArrayList<DeviceComponentProductionSpecificationComponent>();
      this.productionSpecification.add(t);
      return t;
    }

    public DeviceComponent addProductionSpecification(DeviceComponentProductionSpecificationComponent t) { //3
      if (t == null)
        return this;
      if (this.productionSpecification == null)
        this.productionSpecification = new ArrayList<DeviceComponentProductionSpecificationComponent>();
      this.productionSpecification.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #productionSpecification}, creating it if it does not already exist
     */
    public DeviceComponentProductionSpecificationComponent getProductionSpecificationFirstRep() { 
      if (getProductionSpecification().isEmpty()) {
        addProductionSpecification();
      }
      return getProductionSpecification().get(0);
    }

    /**
     * @return {@link #languageCode} (The language code for the human-readable text string produced by the device. This language code will follow the IETF language tag. Example: en-US.)
     */
    public CodeableConcept getLanguageCode() { 
      if (this.languageCode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceComponent.languageCode");
        else if (Configuration.doAutoCreate())
          this.languageCode = new CodeableConcept(); // cc
      return this.languageCode;
    }

    public boolean hasLanguageCode() { 
      return this.languageCode != null && !this.languageCode.isEmpty();
    }

    /**
     * @param value {@link #languageCode} (The language code for the human-readable text string produced by the device. This language code will follow the IETF language tag. Example: en-US.)
     */
    public DeviceComponent setLanguageCode(CodeableConcept value) { 
      this.languageCode = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The locally assigned unique identification by the software. For example: handle ID.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "CodeableConcept", "The component type as defined in the object-oriented or metric nomenclature partition.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("lastSystemChange", "instant", "The timestamp for the most recent system change which includes device configuration or setting change.", 0, java.lang.Integer.MAX_VALUE, lastSystemChange));
        childrenList.add(new Property("source", "Reference(Device)", "The link to the source Device that contains administrative device information such as manufacture, serial number, etc.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("parent", "Reference(DeviceComponent)", "The link to the parent resource. For example: Channel is linked to its VMD parent.", 0, java.lang.Integer.MAX_VALUE, parent));
        childrenList.add(new Property("operationalStatus", "CodeableConcept", "The current operational status of the device. For example: On, Off, Standby, etc.", 0, java.lang.Integer.MAX_VALUE, operationalStatus));
        childrenList.add(new Property("parameterGroup", "CodeableConcept", "The parameter group supported by the current device component that is based on some nomenclature, e.g. cardiovascular.", 0, java.lang.Integer.MAX_VALUE, parameterGroup));
        childrenList.add(new Property("measurementPrinciple", "code", "The physical principle of the measurement. For example: thermal, chemical, acoustical, etc.", 0, java.lang.Integer.MAX_VALUE, measurementPrinciple));
        childrenList.add(new Property("productionSpecification", "", "The production specification such as component revision, serial number, etc.", 0, java.lang.Integer.MAX_VALUE, productionSpecification));
        childrenList.add(new Property("languageCode", "CodeableConcept", "The language code for the human-readable text string produced by the device. This language code will follow the IETF language tag. Example: en-US.", 0, java.lang.Integer.MAX_VALUE, languageCode));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -2072475531: /*lastSystemChange*/ return this.lastSystemChange == null ? new Base[0] : new Base[] {this.lastSystemChange}; // InstantType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Reference
        case -995424086: /*parent*/ return this.parent == null ? new Base[0] : new Base[] {this.parent}; // Reference
        case -2103166364: /*operationalStatus*/ return this.operationalStatus == null ? new Base[0] : this.operationalStatus.toArray(new Base[this.operationalStatus.size()]); // CodeableConcept
        case 1111110742: /*parameterGroup*/ return this.parameterGroup == null ? new Base[0] : new Base[] {this.parameterGroup}; // CodeableConcept
        case 24324384: /*measurementPrinciple*/ return this.measurementPrinciple == null ? new Base[0] : new Base[] {this.measurementPrinciple}; // Enumeration<MeasmntPrinciple>
        case -455527222: /*productionSpecification*/ return this.productionSpecification == null ? new Base[0] : this.productionSpecification.toArray(new Base[this.productionSpecification.size()]); // DeviceComponentProductionSpecificationComponent
        case -2092349083: /*languageCode*/ return this.languageCode == null ? new Base[0] : new Base[] {this.languageCode}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -2072475531: // lastSystemChange
          this.lastSystemChange = castToInstant(value); // InstantType
          return value;
        case -896505829: // source
          this.source = castToReference(value); // Reference
          return value;
        case -995424086: // parent
          this.parent = castToReference(value); // Reference
          return value;
        case -2103166364: // operationalStatus
          this.getOperationalStatus().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1111110742: // parameterGroup
          this.parameterGroup = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 24324384: // measurementPrinciple
          value = new MeasmntPrincipleEnumFactory().fromType(castToCode(value));
          this.measurementPrinciple = (Enumeration) value; // Enumeration<MeasmntPrinciple>
          return value;
        case -455527222: // productionSpecification
          this.getProductionSpecification().add((DeviceComponentProductionSpecificationComponent) value); // DeviceComponentProductionSpecificationComponent
          return value;
        case -2092349083: // languageCode
          this.languageCode = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("lastSystemChange")) {
          this.lastSystemChange = castToInstant(value); // InstantType
        } else if (name.equals("source")) {
          this.source = castToReference(value); // Reference
        } else if (name.equals("parent")) {
          this.parent = castToReference(value); // Reference
        } else if (name.equals("operationalStatus")) {
          this.getOperationalStatus().add(castToCodeableConcept(value));
        } else if (name.equals("parameterGroup")) {
          this.parameterGroup = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("measurementPrinciple")) {
          value = new MeasmntPrincipleEnumFactory().fromType(castToCode(value));
          this.measurementPrinciple = (Enumeration) value; // Enumeration<MeasmntPrinciple>
        } else if (name.equals("productionSpecification")) {
          this.getProductionSpecification().add((DeviceComponentProductionSpecificationComponent) value);
        } else if (name.equals("languageCode")) {
          this.languageCode = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3575610:  return getType(); 
        case -2072475531:  return getLastSystemChangeElement();
        case -896505829:  return getSource(); 
        case -995424086:  return getParent(); 
        case -2103166364:  return addOperationalStatus(); 
        case 1111110742:  return getParameterGroup(); 
        case 24324384:  return getMeasurementPrincipleElement();
        case -455527222:  return addProductionSpecification(); 
        case -2092349083:  return getLanguageCode(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -2072475531: /*lastSystemChange*/ return new String[] {"instant"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        case -995424086: /*parent*/ return new String[] {"Reference"};
        case -2103166364: /*operationalStatus*/ return new String[] {"CodeableConcept"};
        case 1111110742: /*parameterGroup*/ return new String[] {"CodeableConcept"};
        case 24324384: /*measurementPrinciple*/ return new String[] {"code"};
        case -455527222: /*productionSpecification*/ return new String[] {};
        case -2092349083: /*languageCode*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("lastSystemChange")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceComponent.lastSystemChange");
        }
        else if (name.equals("source")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("parent")) {
          this.parent = new Reference();
          return this.parent;
        }
        else if (name.equals("operationalStatus")) {
          return addOperationalStatus();
        }
        else if (name.equals("parameterGroup")) {
          this.parameterGroup = new CodeableConcept();
          return this.parameterGroup;
        }
        else if (name.equals("measurementPrinciple")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceComponent.measurementPrinciple");
        }
        else if (name.equals("productionSpecification")) {
          return addProductionSpecification();
        }
        else if (name.equals("languageCode")) {
          this.languageCode = new CodeableConcept();
          return this.languageCode;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DeviceComponent";

  }

      public DeviceComponent copy() {
        DeviceComponent dst = new DeviceComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.type = type == null ? null : type.copy();
        dst.lastSystemChange = lastSystemChange == null ? null : lastSystemChange.copy();
        dst.source = source == null ? null : source.copy();
        dst.parent = parent == null ? null : parent.copy();
        if (operationalStatus != null) {
          dst.operationalStatus = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : operationalStatus)
            dst.operationalStatus.add(i.copy());
        };
        dst.parameterGroup = parameterGroup == null ? null : parameterGroup.copy();
        dst.measurementPrinciple = measurementPrinciple == null ? null : measurementPrinciple.copy();
        if (productionSpecification != null) {
          dst.productionSpecification = new ArrayList<DeviceComponentProductionSpecificationComponent>();
          for (DeviceComponentProductionSpecificationComponent i : productionSpecification)
            dst.productionSpecification.add(i.copy());
        };
        dst.languageCode = languageCode == null ? null : languageCode.copy();
        return dst;
      }

      protected DeviceComponent typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DeviceComponent))
          return false;
        DeviceComponent o = (DeviceComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(lastSystemChange, o.lastSystemChange, true)
           && compareDeep(source, o.source, true) && compareDeep(parent, o.parent, true) && compareDeep(operationalStatus, o.operationalStatus, true)
           && compareDeep(parameterGroup, o.parameterGroup, true) && compareDeep(measurementPrinciple, o.measurementPrinciple, true)
           && compareDeep(productionSpecification, o.productionSpecification, true) && compareDeep(languageCode, o.languageCode, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DeviceComponent))
          return false;
        DeviceComponent o = (DeviceComponent) other;
        return compareValues(lastSystemChange, o.lastSystemChange, true) && compareValues(measurementPrinciple, o.measurementPrinciple, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, type, lastSystemChange
          , source, parent, operationalStatus, parameterGroup, measurementPrinciple, productionSpecification
          , languageCode);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DeviceComponent;
   }

 /**
   * Search parameter: <b>parent</b>
   * <p>
   * Description: <b>The parent DeviceComponent resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceComponent.parent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="parent", path="DeviceComponent.parent", description="The parent DeviceComponent resource", type="reference", target={DeviceComponent.class } )
  public static final String SP_PARENT = "parent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>parent</b>
   * <p>
   * Description: <b>The parent DeviceComponent resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceComponent.parent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceComponent:parent</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARENT = new ca.uhn.fhir.model.api.Include("DeviceComponent:parent").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the component</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceComponent.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DeviceComponent.identifier", description="The identifier of the component", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the component</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceComponent.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>The device source</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceComponent.source</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="DeviceComponent.source", description="The device source", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device") }, target={Device.class } )
  public static final String SP_SOURCE = "source";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source</b>
   * <p>
   * Description: <b>The device source</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceComponent.source</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceComponent:source</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SOURCE = new ca.uhn.fhir.model.api.Include("DeviceComponent:source").toLocked();

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The device component type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceComponent.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="DeviceComponent.type", description="The device component type", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The device component type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceComponent.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);


}

