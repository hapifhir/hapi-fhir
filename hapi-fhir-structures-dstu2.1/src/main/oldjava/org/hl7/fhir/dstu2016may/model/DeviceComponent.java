package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * Describes the characteristics, operational status and capabilities of a medical-related component of a medical device.
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
         * added to help the parsers
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
          if (code == null || code.isEmpty())
            return null;
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
         * Describes the specification type, such as, serial number, part number, hardware revision, software revision, etc.
         */
        @Child(name = "specType", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specification type", formalDefinition="Describes the specification type, such as, serial number, part number, hardware revision, software revision, etc." )
        protected CodeableConcept specType;

        /**
         * Describes the internal component unique identification. This is a provision for manufacture specific standard components using a private OID. 11073-10101 has a partition for private OID semantic that the manufacture can make use of.
         */
        @Child(name = "componentId", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Internal component unique identification", formalDefinition="Describes the internal component unique identification. This is a provision for manufacture specific standard components using a private OID. 11073-10101 has a partition for private OID semantic that the manufacture can make use of." )
        protected Identifier componentId;

        /**
         * Describes the printable string defining the component.
         */
        @Child(name = "productionSpec", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A printable string defining the component", formalDefinition="Describes the printable string defining the component." )
        protected StringType productionSpec;

        private static final long serialVersionUID = -1476597516L;

    /**
     * Constructor
     */
      public DeviceComponentProductionSpecificationComponent() {
        super();
      }

        /**
         * @return {@link #specType} (Describes the specification type, such as, serial number, part number, hardware revision, software revision, etc.)
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
         * @param value {@link #specType} (Describes the specification type, such as, serial number, part number, hardware revision, software revision, etc.)
         */
        public DeviceComponentProductionSpecificationComponent setSpecType(CodeableConcept value) { 
          this.specType = value;
          return this;
        }

        /**
         * @return {@link #componentId} (Describes the internal component unique identification. This is a provision for manufacture specific standard components using a private OID. 11073-10101 has a partition for private OID semantic that the manufacture can make use of.)
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
         * @param value {@link #componentId} (Describes the internal component unique identification. This is a provision for manufacture specific standard components using a private OID. 11073-10101 has a partition for private OID semantic that the manufacture can make use of.)
         */
        public DeviceComponentProductionSpecificationComponent setComponentId(Identifier value) { 
          this.componentId = value;
          return this;
        }

        /**
         * @return {@link #productionSpec} (Describes the printable string defining the component.). This is the underlying object with id, value and extensions. The accessor "getProductionSpec" gives direct access to the value
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
         * @param value {@link #productionSpec} (Describes the printable string defining the component.). This is the underlying object with id, value and extensions. The accessor "getProductionSpec" gives direct access to the value
         */
        public DeviceComponentProductionSpecificationComponent setProductionSpecElement(StringType value) { 
          this.productionSpec = value;
          return this;
        }

        /**
         * @return Describes the printable string defining the component.
         */
        public String getProductionSpec() { 
          return this.productionSpec == null ? null : this.productionSpec.getValue();
        }

        /**
         * @param value Describes the printable string defining the component.
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
          childrenList.add(new Property("specType", "CodeableConcept", "Describes the specification type, such as, serial number, part number, hardware revision, software revision, etc.", 0, java.lang.Integer.MAX_VALUE, specType));
          childrenList.add(new Property("componentId", "Identifier", "Describes the internal component unique identification. This is a provision for manufacture specific standard components using a private OID. 11073-10101 has a partition for private OID semantic that the manufacture can make use of.", 0, java.lang.Integer.MAX_VALUE, componentId));
          childrenList.add(new Property("productionSpec", "string", "Describes the printable string defining the component.", 0, java.lang.Integer.MAX_VALUE, productionSpec));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -2133482091: // specType
          this.specType = castToCodeableConcept(value); // CodeableConcept
          break;
        case -985933064: // componentId
          this.componentId = castToIdentifier(value); // Identifier
          break;
        case 182147092: // productionSpec
          this.productionSpec = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("specType"))
          this.specType = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("componentId"))
          this.componentId = castToIdentifier(value); // Identifier
        else if (name.equals("productionSpec"))
          this.productionSpec = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -2133482091:  return getSpecType(); // CodeableConcept
        case -985933064:  return getComponentId(); // Identifier
        case 182147092: throw new FHIRException("Cannot make property productionSpec as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (specType == null || specType.isEmpty()) && (componentId == null || componentId.isEmpty())
           && (productionSpec == null || productionSpec.isEmpty());
      }

  public String fhirType() {
    return "DeviceComponent.productionSpecification";

  }

  }

    /**
     * Describes the specific component type as defined in the object-oriented or metric nomenclature partition.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What kind of component it is", formalDefinition="Describes the specific component type as defined in the object-oriented or metric nomenclature partition." )
    protected CodeableConcept type;

    /**
     * Describes the local assigned unique identification by the software. For example: handle ID.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Instance id assigned by the software stack", formalDefinition="Describes the local assigned unique identification by the software. For example: handle ID." )
    protected Identifier identifier;

    /**
     * Describes the timestamp for the most recent system change which includes device configuration or setting change.
     */
    @Child(name = "lastSystemChange", type = {InstantType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Recent system change timestamp", formalDefinition="Describes the timestamp for the most recent system change which includes device configuration or setting change." )
    protected InstantType lastSystemChange;

    /**
     * Describes the link to the source Device that contains administrative device information such as manufacture, serial number, etc.
     */
    @Child(name = "source", type = {Device.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A source device of this component", formalDefinition="Describes the link to the source Device that contains administrative device information such as manufacture, serial number, etc." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (Describes the link to the source Device that contains administrative device information such as manufacture, serial number, etc.)
     */
    protected Device sourceTarget;

    /**
     * Describes the link to the parent resource. For example: Channel is linked to its VMD parent.
     */
    @Child(name = "parent", type = {DeviceComponent.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Parent resource link", formalDefinition="Describes the link to the parent resource. For example: Channel is linked to its VMD parent." )
    protected Reference parent;

    /**
     * The actual object that is the target of the reference (Describes the link to the parent resource. For example: Channel is linked to its VMD parent.)
     */
    protected DeviceComponent parentTarget;

    /**
     * Indicates current operational status of the device. For example: On, Off, Standby, etc.
     */
    @Child(name = "operationalStatus", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Component operational status", formalDefinition="Indicates current operational status of the device. For example: On, Off, Standby, etc." )
    protected List<CodeableConcept> operationalStatus;

    /**
     * Describes the parameter group supported by the current device component that is based on some nomenclature, e.g. cardiovascular.
     */
    @Child(name = "parameterGroup", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Current supported parameter group", formalDefinition="Describes the parameter group supported by the current device component that is based on some nomenclature, e.g. cardiovascular." )
    protected CodeableConcept parameterGroup;

    /**
     * Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc.
     */
    @Child(name = "measurementPrinciple", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="other | chemical | electrical | impedance | nuclear | optical | thermal | biological | mechanical | acoustical | manual+", formalDefinition="Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc." )
    protected Enumeration<MeasmntPrinciple> measurementPrinciple;

    /**
     * Describes the production specification such as component revision, serial number, etc.
     */
    @Child(name = "productionSpecification", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Production specification of the component", formalDefinition="Describes the production specification such as component revision, serial number, etc." )
    protected List<DeviceComponentProductionSpecificationComponent> productionSpecification;

    /**
     * Describes the language code for the human-readable text string produced by the device. This language code will follow the IETF language tag. Example: en-US.
     */
    @Child(name = "languageCode", type = {CodeableConcept.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Language code for the human-readable text strings produced by the device", formalDefinition="Describes the language code for the human-readable text string produced by the device. This language code will follow the IETF language tag. Example: en-US." )
    protected CodeableConcept languageCode;

    private static final long serialVersionUID = -1742890034L;

  /**
   * Constructor
   */
    public DeviceComponent() {
      super();
    }

  /**
   * Constructor
   */
    public DeviceComponent(CodeableConcept type, Identifier identifier, InstantType lastSystemChange) {
      super();
      this.type = type;
      this.identifier = identifier;
      this.lastSystemChange = lastSystemChange;
    }

    /**
     * @return {@link #type} (Describes the specific component type as defined in the object-oriented or metric nomenclature partition.)
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
     * @param value {@link #type} (Describes the specific component type as defined in the object-oriented or metric nomenclature partition.)
     */
    public DeviceComponent setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Describes the local assigned unique identification by the software. For example: handle ID.)
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
     * @param value {@link #identifier} (Describes the local assigned unique identification by the software. For example: handle ID.)
     */
    public DeviceComponent setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #lastSystemChange} (Describes the timestamp for the most recent system change which includes device configuration or setting change.). This is the underlying object with id, value and extensions. The accessor "getLastSystemChange" gives direct access to the value
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
     * @param value {@link #lastSystemChange} (Describes the timestamp for the most recent system change which includes device configuration or setting change.). This is the underlying object with id, value and extensions. The accessor "getLastSystemChange" gives direct access to the value
     */
    public DeviceComponent setLastSystemChangeElement(InstantType value) { 
      this.lastSystemChange = value;
      return this;
    }

    /**
     * @return Describes the timestamp for the most recent system change which includes device configuration or setting change.
     */
    public Date getLastSystemChange() { 
      return this.lastSystemChange == null ? null : this.lastSystemChange.getValue();
    }

    /**
     * @param value Describes the timestamp for the most recent system change which includes device configuration or setting change.
     */
    public DeviceComponent setLastSystemChange(Date value) { 
        if (this.lastSystemChange == null)
          this.lastSystemChange = new InstantType();
        this.lastSystemChange.setValue(value);
      return this;
    }

    /**
     * @return {@link #source} (Describes the link to the source Device that contains administrative device information such as manufacture, serial number, etc.)
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
     * @param value {@link #source} (Describes the link to the source Device that contains administrative device information such as manufacture, serial number, etc.)
     */
    public DeviceComponent setSource(Reference value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the link to the source Device that contains administrative device information such as manufacture, serial number, etc.)
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
     * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the link to the source Device that contains administrative device information such as manufacture, serial number, etc.)
     */
    public DeviceComponent setSourceTarget(Device value) { 
      this.sourceTarget = value;
      return this;
    }

    /**
     * @return {@link #parent} (Describes the link to the parent resource. For example: Channel is linked to its VMD parent.)
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
     * @param value {@link #parent} (Describes the link to the parent resource. For example: Channel is linked to its VMD parent.)
     */
    public DeviceComponent setParent(Reference value) { 
      this.parent = value;
      return this;
    }

    /**
     * @return {@link #parent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the link to the parent resource. For example: Channel is linked to its VMD parent.)
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
     * @param value {@link #parent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the link to the parent resource. For example: Channel is linked to its VMD parent.)
     */
    public DeviceComponent setParentTarget(DeviceComponent value) { 
      this.parentTarget = value;
      return this;
    }

    /**
     * @return {@link #operationalStatus} (Indicates current operational status of the device. For example: On, Off, Standby, etc.)
     */
    public List<CodeableConcept> getOperationalStatus() { 
      if (this.operationalStatus == null)
        this.operationalStatus = new ArrayList<CodeableConcept>();
      return this.operationalStatus;
    }

    public boolean hasOperationalStatus() { 
      if (this.operationalStatus == null)
        return false;
      for (CodeableConcept item : this.operationalStatus)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #operationalStatus} (Indicates current operational status of the device. For example: On, Off, Standby, etc.)
     */
    // syntactic sugar
    public CodeableConcept addOperationalStatus() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.operationalStatus == null)
        this.operationalStatus = new ArrayList<CodeableConcept>();
      this.operationalStatus.add(t);
      return t;
    }

    // syntactic sugar
    public DeviceComponent addOperationalStatus(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.operationalStatus == null)
        this.operationalStatus = new ArrayList<CodeableConcept>();
      this.operationalStatus.add(t);
      return this;
    }

    /**
     * @return {@link #parameterGroup} (Describes the parameter group supported by the current device component that is based on some nomenclature, e.g. cardiovascular.)
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
     * @param value {@link #parameterGroup} (Describes the parameter group supported by the current device component that is based on some nomenclature, e.g. cardiovascular.)
     */
    public DeviceComponent setParameterGroup(CodeableConcept value) { 
      this.parameterGroup = value;
      return this;
    }

    /**
     * @return {@link #measurementPrinciple} (Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc.). This is the underlying object with id, value and extensions. The accessor "getMeasurementPrinciple" gives direct access to the value
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
     * @param value {@link #measurementPrinciple} (Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc.). This is the underlying object with id, value and extensions. The accessor "getMeasurementPrinciple" gives direct access to the value
     */
    public DeviceComponent setMeasurementPrincipleElement(Enumeration<MeasmntPrinciple> value) { 
      this.measurementPrinciple = value;
      return this;
    }

    /**
     * @return Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc.
     */
    public MeasmntPrinciple getMeasurementPrinciple() { 
      return this.measurementPrinciple == null ? null : this.measurementPrinciple.getValue();
    }

    /**
     * @param value Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc.
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
     * @return {@link #productionSpecification} (Describes the production specification such as component revision, serial number, etc.)
     */
    public List<DeviceComponentProductionSpecificationComponent> getProductionSpecification() { 
      if (this.productionSpecification == null)
        this.productionSpecification = new ArrayList<DeviceComponentProductionSpecificationComponent>();
      return this.productionSpecification;
    }

    public boolean hasProductionSpecification() { 
      if (this.productionSpecification == null)
        return false;
      for (DeviceComponentProductionSpecificationComponent item : this.productionSpecification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #productionSpecification} (Describes the production specification such as component revision, serial number, etc.)
     */
    // syntactic sugar
    public DeviceComponentProductionSpecificationComponent addProductionSpecification() { //3
      DeviceComponentProductionSpecificationComponent t = new DeviceComponentProductionSpecificationComponent();
      if (this.productionSpecification == null)
        this.productionSpecification = new ArrayList<DeviceComponentProductionSpecificationComponent>();
      this.productionSpecification.add(t);
      return t;
    }

    // syntactic sugar
    public DeviceComponent addProductionSpecification(DeviceComponentProductionSpecificationComponent t) { //3
      if (t == null)
        return this;
      if (this.productionSpecification == null)
        this.productionSpecification = new ArrayList<DeviceComponentProductionSpecificationComponent>();
      this.productionSpecification.add(t);
      return this;
    }

    /**
     * @return {@link #languageCode} (Describes the language code for the human-readable text string produced by the device. This language code will follow the IETF language tag. Example: en-US.)
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
     * @param value {@link #languageCode} (Describes the language code for the human-readable text string produced by the device. This language code will follow the IETF language tag. Example: en-US.)
     */
    public DeviceComponent setLanguageCode(CodeableConcept value) { 
      this.languageCode = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "CodeableConcept", "Describes the specific component type as defined in the object-oriented or metric nomenclature partition.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("identifier", "Identifier", "Describes the local assigned unique identification by the software. For example: handle ID.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("lastSystemChange", "instant", "Describes the timestamp for the most recent system change which includes device configuration or setting change.", 0, java.lang.Integer.MAX_VALUE, lastSystemChange));
        childrenList.add(new Property("source", "Reference(Device)", "Describes the link to the source Device that contains administrative device information such as manufacture, serial number, etc.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("parent", "Reference(DeviceComponent)", "Describes the link to the parent resource. For example: Channel is linked to its VMD parent.", 0, java.lang.Integer.MAX_VALUE, parent));
        childrenList.add(new Property("operationalStatus", "CodeableConcept", "Indicates current operational status of the device. For example: On, Off, Standby, etc.", 0, java.lang.Integer.MAX_VALUE, operationalStatus));
        childrenList.add(new Property("parameterGroup", "CodeableConcept", "Describes the parameter group supported by the current device component that is based on some nomenclature, e.g. cardiovascular.", 0, java.lang.Integer.MAX_VALUE, parameterGroup));
        childrenList.add(new Property("measurementPrinciple", "code", "Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc.", 0, java.lang.Integer.MAX_VALUE, measurementPrinciple));
        childrenList.add(new Property("productionSpecification", "", "Describes the production specification such as component revision, serial number, etc.", 0, java.lang.Integer.MAX_VALUE, productionSpecification));
        childrenList.add(new Property("languageCode", "CodeableConcept", "Describes the language code for the human-readable text string produced by the device. This language code will follow the IETF language tag. Example: en-US.", 0, java.lang.Integer.MAX_VALUE, languageCode));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case -2072475531: // lastSystemChange
          this.lastSystemChange = castToInstant(value); // InstantType
          break;
        case -896505829: // source
          this.source = castToReference(value); // Reference
          break;
        case -995424086: // parent
          this.parent = castToReference(value); // Reference
          break;
        case -2103166364: // operationalStatus
          this.getOperationalStatus().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1111110742: // parameterGroup
          this.parameterGroup = castToCodeableConcept(value); // CodeableConcept
          break;
        case 24324384: // measurementPrinciple
          this.measurementPrinciple = new MeasmntPrincipleEnumFactory().fromType(value); // Enumeration<MeasmntPrinciple>
          break;
        case -455527222: // productionSpecification
          this.getProductionSpecification().add((DeviceComponentProductionSpecificationComponent) value); // DeviceComponentProductionSpecificationComponent
          break;
        case -2092349083: // languageCode
          this.languageCode = castToCodeableConcept(value); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("lastSystemChange"))
          this.lastSystemChange = castToInstant(value); // InstantType
        else if (name.equals("source"))
          this.source = castToReference(value); // Reference
        else if (name.equals("parent"))
          this.parent = castToReference(value); // Reference
        else if (name.equals("operationalStatus"))
          this.getOperationalStatus().add(castToCodeableConcept(value));
        else if (name.equals("parameterGroup"))
          this.parameterGroup = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("measurementPrinciple"))
          this.measurementPrinciple = new MeasmntPrincipleEnumFactory().fromType(value); // Enumeration<MeasmntPrinciple>
        else if (name.equals("productionSpecification"))
          this.getProductionSpecification().add((DeviceComponentProductionSpecificationComponent) value);
        else if (name.equals("languageCode"))
          this.languageCode = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // CodeableConcept
        case -1618432855:  return getIdentifier(); // Identifier
        case -2072475531: throw new FHIRException("Cannot make property lastSystemChange as it is not a complex type"); // InstantType
        case -896505829:  return getSource(); // Reference
        case -995424086:  return getParent(); // Reference
        case -2103166364:  return addOperationalStatus(); // CodeableConcept
        case 1111110742:  return getParameterGroup(); // CodeableConcept
        case 24324384: throw new FHIRException("Cannot make property measurementPrinciple as it is not a complex type"); // Enumeration<MeasmntPrinciple>
        case -455527222:  return addProductionSpecification(); // DeviceComponentProductionSpecificationComponent
        case -2092349083:  return getLanguageCode(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
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
        dst.type = type == null ? null : type.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
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
        return compareDeep(type, o.type, true) && compareDeep(identifier, o.identifier, true) && compareDeep(lastSystemChange, o.lastSystemChange, true)
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
        return super.isEmpty() && (type == null || type.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (lastSystemChange == null || lastSystemChange.isEmpty()) && (source == null || source.isEmpty())
           && (parent == null || parent.isEmpty()) && (operationalStatus == null || operationalStatus.isEmpty())
           && (parameterGroup == null || parameterGroup.isEmpty()) && (measurementPrinciple == null || measurementPrinciple.isEmpty())
           && (productionSpecification == null || productionSpecification.isEmpty()) && (languageCode == null || languageCode.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DeviceComponent;
   }

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>The device source</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceComponent.source</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="DeviceComponent.source", description="The device source", type="reference" )
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
   * Search parameter: <b>parent</b>
   * <p>
   * Description: <b>The parent DeviceComponent resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceComponent.parent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="parent", path="DeviceComponent.parent", description="The parent DeviceComponent resource", type="reference" )
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

