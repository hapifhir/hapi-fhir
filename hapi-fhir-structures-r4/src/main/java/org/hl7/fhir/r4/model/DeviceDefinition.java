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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

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
 * The characteristics, operational status and capabilities of a medical-related component of a medical device.
 */
@ResourceDef(name="DeviceDefinition", profile="http://hl7.org/fhir/Profile/DeviceDefinition")
public class DeviceDefinition extends DomainResource {

    public enum DeviceNameType {
        /**
         * UDI Label name.
         */
        UDILABELNAME, 
        /**
         * User Friendly name.
         */
        USERFRIENDLYNAME, 
        /**
         * Patient Reported name.
         */
        PATIENTREPORTEDNAME, 
        /**
         * Manufacturer name.
         */
        MANUFACTURERNAME, 
        /**
         * Model name.
         */
        MODELNAME, 
        /**
         * other.
         */
        OTHER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DeviceNameType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("udi-label-name".equals(codeString))
          return UDILABELNAME;
        if ("user-friendly-name".equals(codeString))
          return USERFRIENDLYNAME;
        if ("patient-reported-name".equals(codeString))
          return PATIENTREPORTEDNAME;
        if ("manufacturer-name".equals(codeString))
          return MANUFACTURERNAME;
        if ("model-name".equals(codeString))
          return MODELNAME;
        if ("other".equals(codeString))
          return OTHER;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DeviceNameType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UDILABELNAME: return "udi-label-name";
            case USERFRIENDLYNAME: return "user-friendly-name";
            case PATIENTREPORTEDNAME: return "patient-reported-name";
            case MANUFACTURERNAME: return "manufacturer-name";
            case MODELNAME: return "model-name";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case UDILABELNAME: return "http://hl7.org/fhir/device-nametype";
            case USERFRIENDLYNAME: return "http://hl7.org/fhir/device-nametype";
            case PATIENTREPORTEDNAME: return "http://hl7.org/fhir/device-nametype";
            case MANUFACTURERNAME: return "http://hl7.org/fhir/device-nametype";
            case MODELNAME: return "http://hl7.org/fhir/device-nametype";
            case OTHER: return "http://hl7.org/fhir/device-nametype";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case UDILABELNAME: return "UDI Label name.";
            case USERFRIENDLYNAME: return "User Friendly name.";
            case PATIENTREPORTEDNAME: return "Patient Reported name.";
            case MANUFACTURERNAME: return "Manufacturer name.";
            case MODELNAME: return "Model name.";
            case OTHER: return "other.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UDILABELNAME: return "UDI Label name";
            case USERFRIENDLYNAME: return "User Friendly name";
            case PATIENTREPORTEDNAME: return "Patient Reported name";
            case MANUFACTURERNAME: return "Manufacturer name";
            case MODELNAME: return "Model name";
            case OTHER: return "other";
            default: return "?";
          }
        }
    }

  public static class DeviceNameTypeEnumFactory implements EnumFactory<DeviceNameType> {
    public DeviceNameType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("udi-label-name".equals(codeString))
          return DeviceNameType.UDILABELNAME;
        if ("user-friendly-name".equals(codeString))
          return DeviceNameType.USERFRIENDLYNAME;
        if ("patient-reported-name".equals(codeString))
          return DeviceNameType.PATIENTREPORTEDNAME;
        if ("manufacturer-name".equals(codeString))
          return DeviceNameType.MANUFACTURERNAME;
        if ("model-name".equals(codeString))
          return DeviceNameType.MODELNAME;
        if ("other".equals(codeString))
          return DeviceNameType.OTHER;
        throw new IllegalArgumentException("Unknown DeviceNameType code '"+codeString+"'");
        }
        public Enumeration<DeviceNameType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DeviceNameType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("udi-label-name".equals(codeString))
          return new Enumeration<DeviceNameType>(this, DeviceNameType.UDILABELNAME);
        if ("user-friendly-name".equals(codeString))
          return new Enumeration<DeviceNameType>(this, DeviceNameType.USERFRIENDLYNAME);
        if ("patient-reported-name".equals(codeString))
          return new Enumeration<DeviceNameType>(this, DeviceNameType.PATIENTREPORTEDNAME);
        if ("manufacturer-name".equals(codeString))
          return new Enumeration<DeviceNameType>(this, DeviceNameType.MANUFACTURERNAME);
        if ("model-name".equals(codeString))
          return new Enumeration<DeviceNameType>(this, DeviceNameType.MODELNAME);
        if ("other".equals(codeString))
          return new Enumeration<DeviceNameType>(this, DeviceNameType.OTHER);
        throw new FHIRException("Unknown DeviceNameType code '"+codeString+"'");
        }
    public String toCode(DeviceNameType code) {
      if (code == DeviceNameType.UDILABELNAME)
        return "udi-label-name";
      if (code == DeviceNameType.USERFRIENDLYNAME)
        return "user-friendly-name";
      if (code == DeviceNameType.PATIENTREPORTEDNAME)
        return "patient-reported-name";
      if (code == DeviceNameType.MANUFACTURERNAME)
        return "manufacturer-name";
      if (code == DeviceNameType.MODELNAME)
        return "model-name";
      if (code == DeviceNameType.OTHER)
        return "other";
      return "?";
      }
    public String toSystem(DeviceNameType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class DeviceDefinitionUdiDeviceIdentifierComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier.
         */
        @Child(name = "deviceIdentifier", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier", formalDefinition="The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier." )
        protected StringType deviceIdentifier;

        /**
         * The organization that assigns the identifier algorithm.
         */
        @Child(name = "issuer", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The organization that assigns the identifier algorithm", formalDefinition="The organization that assigns the identifier algorithm." )
        protected UriType issuer;

        /**
         * The jurisdiction to which the deviceIdentifier applies.
         */
        @Child(name = "jurisdiction", type = {UriType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The jurisdiction to which the deviceIdentifier applies", formalDefinition="The jurisdiction to which the deviceIdentifier applies." )
        protected UriType jurisdiction;

        private static final long serialVersionUID = -1577319218L;

    /**
     * Constructor
     */
      public DeviceDefinitionUdiDeviceIdentifierComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DeviceDefinitionUdiDeviceIdentifierComponent(StringType deviceIdentifier, UriType issuer, UriType jurisdiction) {
        super();
        this.deviceIdentifier = deviceIdentifier;
        this.issuer = issuer;
        this.jurisdiction = jurisdiction;
      }

        /**
         * @return {@link #deviceIdentifier} (The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier.). This is the underlying object with id, value and extensions. The accessor "getDeviceIdentifier" gives direct access to the value
         */
        public StringType getDeviceIdentifierElement() { 
          if (this.deviceIdentifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionUdiDeviceIdentifierComponent.deviceIdentifier");
            else if (Configuration.doAutoCreate())
              this.deviceIdentifier = new StringType(); // bb
          return this.deviceIdentifier;
        }

        public boolean hasDeviceIdentifierElement() { 
          return this.deviceIdentifier != null && !this.deviceIdentifier.isEmpty();
        }

        public boolean hasDeviceIdentifier() { 
          return this.deviceIdentifier != null && !this.deviceIdentifier.isEmpty();
        }

        /**
         * @param value {@link #deviceIdentifier} (The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier.). This is the underlying object with id, value and extensions. The accessor "getDeviceIdentifier" gives direct access to the value
         */
        public DeviceDefinitionUdiDeviceIdentifierComponent setDeviceIdentifierElement(StringType value) { 
          this.deviceIdentifier = value;
          return this;
        }

        /**
         * @return The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier.
         */
        public String getDeviceIdentifier() { 
          return this.deviceIdentifier == null ? null : this.deviceIdentifier.getValue();
        }

        /**
         * @param value The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier.
         */
        public DeviceDefinitionUdiDeviceIdentifierComponent setDeviceIdentifier(String value) { 
            if (this.deviceIdentifier == null)
              this.deviceIdentifier = new StringType();
            this.deviceIdentifier.setValue(value);
          return this;
        }

        /**
         * @return {@link #issuer} (The organization that assigns the identifier algorithm.). This is the underlying object with id, value and extensions. The accessor "getIssuer" gives direct access to the value
         */
        public UriType getIssuerElement() { 
          if (this.issuer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionUdiDeviceIdentifierComponent.issuer");
            else if (Configuration.doAutoCreate())
              this.issuer = new UriType(); // bb
          return this.issuer;
        }

        public boolean hasIssuerElement() { 
          return this.issuer != null && !this.issuer.isEmpty();
        }

        public boolean hasIssuer() { 
          return this.issuer != null && !this.issuer.isEmpty();
        }

        /**
         * @param value {@link #issuer} (The organization that assigns the identifier algorithm.). This is the underlying object with id, value and extensions. The accessor "getIssuer" gives direct access to the value
         */
        public DeviceDefinitionUdiDeviceIdentifierComponent setIssuerElement(UriType value) { 
          this.issuer = value;
          return this;
        }

        /**
         * @return The organization that assigns the identifier algorithm.
         */
        public String getIssuer() { 
          return this.issuer == null ? null : this.issuer.getValue();
        }

        /**
         * @param value The organization that assigns the identifier algorithm.
         */
        public DeviceDefinitionUdiDeviceIdentifierComponent setIssuer(String value) { 
            if (this.issuer == null)
              this.issuer = new UriType();
            this.issuer.setValue(value);
          return this;
        }

        /**
         * @return {@link #jurisdiction} (The jurisdiction to which the deviceIdentifier applies.). This is the underlying object with id, value and extensions. The accessor "getJurisdiction" gives direct access to the value
         */
        public UriType getJurisdictionElement() { 
          if (this.jurisdiction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionUdiDeviceIdentifierComponent.jurisdiction");
            else if (Configuration.doAutoCreate())
              this.jurisdiction = new UriType(); // bb
          return this.jurisdiction;
        }

        public boolean hasJurisdictionElement() { 
          return this.jurisdiction != null && !this.jurisdiction.isEmpty();
        }

        public boolean hasJurisdiction() { 
          return this.jurisdiction != null && !this.jurisdiction.isEmpty();
        }

        /**
         * @param value {@link #jurisdiction} (The jurisdiction to which the deviceIdentifier applies.). This is the underlying object with id, value and extensions. The accessor "getJurisdiction" gives direct access to the value
         */
        public DeviceDefinitionUdiDeviceIdentifierComponent setJurisdictionElement(UriType value) { 
          this.jurisdiction = value;
          return this;
        }

        /**
         * @return The jurisdiction to which the deviceIdentifier applies.
         */
        public String getJurisdiction() { 
          return this.jurisdiction == null ? null : this.jurisdiction.getValue();
        }

        /**
         * @param value The jurisdiction to which the deviceIdentifier applies.
         */
        public DeviceDefinitionUdiDeviceIdentifierComponent setJurisdiction(String value) { 
            if (this.jurisdiction == null)
              this.jurisdiction = new UriType();
            this.jurisdiction.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("deviceIdentifier", "string", "The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier.", 0, 1, deviceIdentifier));
          children.add(new Property("issuer", "uri", "The organization that assigns the identifier algorithm.", 0, 1, issuer));
          children.add(new Property("jurisdiction", "uri", "The jurisdiction to which the deviceIdentifier applies.", 0, 1, jurisdiction));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1322005407: /*deviceIdentifier*/  return new Property("deviceIdentifier", "string", "The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier.", 0, 1, deviceIdentifier);
          case -1179159879: /*issuer*/  return new Property("issuer", "uri", "The organization that assigns the identifier algorithm.", 0, 1, issuer);
          case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "uri", "The jurisdiction to which the deviceIdentifier applies.", 0, 1, jurisdiction);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1322005407: /*deviceIdentifier*/ return this.deviceIdentifier == null ? new Base[0] : new Base[] {this.deviceIdentifier}; // StringType
        case -1179159879: /*issuer*/ return this.issuer == null ? new Base[0] : new Base[] {this.issuer}; // UriType
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : new Base[] {this.jurisdiction}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1322005407: // deviceIdentifier
          this.deviceIdentifier = castToString(value); // StringType
          return value;
        case -1179159879: // issuer
          this.issuer = castToUri(value); // UriType
          return value;
        case -507075711: // jurisdiction
          this.jurisdiction = castToUri(value); // UriType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("deviceIdentifier")) {
          this.deviceIdentifier = castToString(value); // StringType
        } else if (name.equals("issuer")) {
          this.issuer = castToUri(value); // UriType
        } else if (name.equals("jurisdiction")) {
          this.jurisdiction = castToUri(value); // UriType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1322005407:  return getDeviceIdentifierElement();
        case -1179159879:  return getIssuerElement();
        case -507075711:  return getJurisdictionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1322005407: /*deviceIdentifier*/ return new String[] {"string"};
        case -1179159879: /*issuer*/ return new String[] {"uri"};
        case -507075711: /*jurisdiction*/ return new String[] {"uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("deviceIdentifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.deviceIdentifier");
        }
        else if (name.equals("issuer")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.issuer");
        }
        else if (name.equals("jurisdiction")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.jurisdiction");
        }
        else
          return super.addChild(name);
      }

      public DeviceDefinitionUdiDeviceIdentifierComponent copy() {
        DeviceDefinitionUdiDeviceIdentifierComponent dst = new DeviceDefinitionUdiDeviceIdentifierComponent();
        copyValues(dst);
        dst.deviceIdentifier = deviceIdentifier == null ? null : deviceIdentifier.copy();
        dst.issuer = issuer == null ? null : issuer.copy();
        dst.jurisdiction = jurisdiction == null ? null : jurisdiction.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionUdiDeviceIdentifierComponent))
          return false;
        DeviceDefinitionUdiDeviceIdentifierComponent o = (DeviceDefinitionUdiDeviceIdentifierComponent) other_;
        return compareDeep(deviceIdentifier, o.deviceIdentifier, true) && compareDeep(issuer, o.issuer, true)
           && compareDeep(jurisdiction, o.jurisdiction, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionUdiDeviceIdentifierComponent))
          return false;
        DeviceDefinitionUdiDeviceIdentifierComponent o = (DeviceDefinitionUdiDeviceIdentifierComponent) other_;
        return compareValues(deviceIdentifier, o.deviceIdentifier, true) && compareValues(issuer, o.issuer, true)
           && compareValues(jurisdiction, o.jurisdiction, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(deviceIdentifier, issuer, jurisdiction
          );
      }

  public String fhirType() {
    return "DeviceDefinition.udiDeviceIdentifier";

  }

  }

    @Block()
    public static class DeviceDefinitionDeviceNameComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the device.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The name of the device", formalDefinition="The name of the device." )
        protected StringType name;

        /**
         * The type of deviceName.
UDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName.
         */
        @Child(name = "type", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="udi-label-name | user-friendly-name | patient-reported-name | manufacturer-name | model-name | other", formalDefinition="The type of deviceName.\nUDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-nametype")
        protected Enumeration<DeviceNameType> type;

        private static final long serialVersionUID = 918983440L;

    /**
     * Constructor
     */
      public DeviceDefinitionDeviceNameComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DeviceDefinitionDeviceNameComponent(StringType name, Enumeration<DeviceNameType> type) {
        super();
        this.name = name;
        this.type = type;
      }

        /**
         * @return {@link #name} (The name of the device.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionDeviceNameComponent.name");
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
         * @param value {@link #name} (The name of the device.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public DeviceDefinitionDeviceNameComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the device.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the device.
         */
        public DeviceDefinitionDeviceNameComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of deviceName.
UDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<DeviceNameType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionDeviceNameComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<DeviceNameType>(new DeviceNameTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of deviceName.
UDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public DeviceDefinitionDeviceNameComponent setTypeElement(Enumeration<DeviceNameType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of deviceName.
UDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName.
         */
        public DeviceNameType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of deviceName.
UDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName.
         */
        public DeviceDefinitionDeviceNameComponent setType(DeviceNameType value) { 
            if (this.type == null)
              this.type = new Enumeration<DeviceNameType>(new DeviceNameTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "The name of the device.", 0, 1, name));
          children.add(new Property("type", "code", "The type of deviceName.\nUDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName.", 0, 1, type));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "The name of the device.", 0, 1, name);
          case 3575610: /*type*/  return new Property("type", "code", "The type of deviceName.\nUDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName.", 0, 1, type);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<DeviceNameType>
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 3575610: // type
          value = new DeviceNameTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<DeviceNameType>
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("type")) {
          value = new DeviceNameTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<DeviceNameType>
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 3575610:  return getTypeElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.name");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.type");
        }
        else
          return super.addChild(name);
      }

      public DeviceDefinitionDeviceNameComponent copy() {
        DeviceDefinitionDeviceNameComponent dst = new DeviceDefinitionDeviceNameComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.type = type == null ? null : type.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionDeviceNameComponent))
          return false;
        DeviceDefinitionDeviceNameComponent o = (DeviceDefinitionDeviceNameComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(type, o.type, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionDeviceNameComponent))
          return false;
        DeviceDefinitionDeviceNameComponent o = (DeviceDefinitionDeviceNameComponent) other_;
        return compareValues(name, o.name, true) && compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, type);
      }

  public String fhirType() {
    return "DeviceDefinition.deviceName";

  }

  }

    @Block()
    public static class DeviceDefinitionSpecializationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The standard that is used to operate and communicate.
         */
        @Child(name = "systemType", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The standard that is used to operate and communicate", formalDefinition="The standard that is used to operate and communicate." )
        protected StringType systemType;

        /**
         * The version of the standard that is used to operate and communicate.
         */
        @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The version of the standard that is used to operate and communicate", formalDefinition="The version of the standard that is used to operate and communicate." )
        protected StringType version;

        private static final long serialVersionUID = -249304393L;

    /**
     * Constructor
     */
      public DeviceDefinitionSpecializationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DeviceDefinitionSpecializationComponent(StringType systemType) {
        super();
        this.systemType = systemType;
      }

        /**
         * @return {@link #systemType} (The standard that is used to operate and communicate.). This is the underlying object with id, value and extensions. The accessor "getSystemType" gives direct access to the value
         */
        public StringType getSystemTypeElement() { 
          if (this.systemType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionSpecializationComponent.systemType");
            else if (Configuration.doAutoCreate())
              this.systemType = new StringType(); // bb
          return this.systemType;
        }

        public boolean hasSystemTypeElement() { 
          return this.systemType != null && !this.systemType.isEmpty();
        }

        public boolean hasSystemType() { 
          return this.systemType != null && !this.systemType.isEmpty();
        }

        /**
         * @param value {@link #systemType} (The standard that is used to operate and communicate.). This is the underlying object with id, value and extensions. The accessor "getSystemType" gives direct access to the value
         */
        public DeviceDefinitionSpecializationComponent setSystemTypeElement(StringType value) { 
          this.systemType = value;
          return this;
        }

        /**
         * @return The standard that is used to operate and communicate.
         */
        public String getSystemType() { 
          return this.systemType == null ? null : this.systemType.getValue();
        }

        /**
         * @param value The standard that is used to operate and communicate.
         */
        public DeviceDefinitionSpecializationComponent setSystemType(String value) { 
            if (this.systemType == null)
              this.systemType = new StringType();
            this.systemType.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version of the standard that is used to operate and communicate.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionSpecializationComponent.version");
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
         * @param value {@link #version} (The version of the standard that is used to operate and communicate.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public DeviceDefinitionSpecializationComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version of the standard that is used to operate and communicate.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version of the standard that is used to operate and communicate.
         */
        public DeviceDefinitionSpecializationComponent setVersion(String value) { 
          if (Utilities.noString(value))
            this.version = null;
          else {
            if (this.version == null)
              this.version = new StringType();
            this.version.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("systemType", "string", "The standard that is used to operate and communicate.", 0, 1, systemType));
          children.add(new Property("version", "string", "The version of the standard that is used to operate and communicate.", 0, 1, version));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 642893321: /*systemType*/  return new Property("systemType", "string", "The standard that is used to operate and communicate.", 0, 1, systemType);
          case 351608024: /*version*/  return new Property("version", "string", "The version of the standard that is used to operate and communicate.", 0, 1, version);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 642893321: /*systemType*/ return this.systemType == null ? new Base[0] : new Base[] {this.systemType}; // StringType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 642893321: // systemType
          this.systemType = castToString(value); // StringType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("systemType")) {
          this.systemType = castToString(value); // StringType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 642893321:  return getSystemTypeElement();
        case 351608024:  return getVersionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 642893321: /*systemType*/ return new String[] {"string"};
        case 351608024: /*version*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("systemType")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.systemType");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.version");
        }
        else
          return super.addChild(name);
      }

      public DeviceDefinitionSpecializationComponent copy() {
        DeviceDefinitionSpecializationComponent dst = new DeviceDefinitionSpecializationComponent();
        copyValues(dst);
        dst.systemType = systemType == null ? null : systemType.copy();
        dst.version = version == null ? null : version.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionSpecializationComponent))
          return false;
        DeviceDefinitionSpecializationComponent o = (DeviceDefinitionSpecializationComponent) other_;
        return compareDeep(systemType, o.systemType, true) && compareDeep(version, o.version, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionSpecializationComponent))
          return false;
        DeviceDefinitionSpecializationComponent o = (DeviceDefinitionSpecializationComponent) other_;
        return compareValues(systemType, o.systemType, true) && compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(systemType, version);
      }

  public String fhirType() {
    return "DeviceDefinition.specialization";

  }

  }

    @Block()
    public static class DeviceDefinitionCapabilityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of capability.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of capability", formalDefinition="Type of capability." )
        protected CodeableConcept type;

        /**
         * Description of capability.
         */
        @Child(name = "description", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Description of capability", formalDefinition="Description of capability." )
        protected List<CodeableConcept> description;

        private static final long serialVersionUID = -192945344L;

    /**
     * Constructor
     */
      public DeviceDefinitionCapabilityComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DeviceDefinitionCapabilityComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Type of capability.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionCapabilityComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of capability.)
         */
        public DeviceDefinitionCapabilityComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #description} (Description of capability.)
         */
        public List<CodeableConcept> getDescription() { 
          if (this.description == null)
            this.description = new ArrayList<CodeableConcept>();
          return this.description;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DeviceDefinitionCapabilityComponent setDescription(List<CodeableConcept> theDescription) { 
          this.description = theDescription;
          return this;
        }

        public boolean hasDescription() { 
          if (this.description == null)
            return false;
          for (CodeableConcept item : this.description)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addDescription() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.description == null)
            this.description = new ArrayList<CodeableConcept>();
          this.description.add(t);
          return t;
        }

        public DeviceDefinitionCapabilityComponent addDescription(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.description == null)
            this.description = new ArrayList<CodeableConcept>();
          this.description.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #description}, creating it if it does not already exist
         */
        public CodeableConcept getDescriptionFirstRep() { 
          if (getDescription().isEmpty()) {
            addDescription();
          }
          return getDescription().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Type of capability.", 0, 1, type));
          children.add(new Property("description", "CodeableConcept", "Description of capability.", 0, java.lang.Integer.MAX_VALUE, description));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of capability.", 0, 1, type);
          case -1724546052: /*description*/  return new Property("description", "CodeableConcept", "Description of capability.", 0, java.lang.Integer.MAX_VALUE, description);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : this.description.toArray(new Base[this.description.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1724546052: // description
          this.getDescription().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.getDescription().add(castToCodeableConcept(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1724546052:  return addDescription(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("description")) {
          return addDescription();
        }
        else
          return super.addChild(name);
      }

      public DeviceDefinitionCapabilityComponent copy() {
        DeviceDefinitionCapabilityComponent dst = new DeviceDefinitionCapabilityComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (description != null) {
          dst.description = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : description)
            dst.description.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionCapabilityComponent))
          return false;
        DeviceDefinitionCapabilityComponent o = (DeviceDefinitionCapabilityComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(description, o.description, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionCapabilityComponent))
          return false;
        DeviceDefinitionCapabilityComponent o = (DeviceDefinitionCapabilityComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, description);
      }

  public String fhirType() {
    return "DeviceDefinition.capability";

  }

  }

    @Block()
    public static class DeviceDefinitionPropertyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code that specifies the property DeviceDefinitionPropetyCode (Extensible).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code that specifies the property DeviceDefinitionPropetyCode (Extensible)", formalDefinition="Code that specifies the property DeviceDefinitionPropetyCode (Extensible)." )
        protected CodeableConcept type;

        /**
         * Property value as a quantity.
         */
        @Child(name = "valueQuanity", type = {Quantity.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Property value as a quantity", formalDefinition="Property value as a quantity." )
        protected List<Quantity> valueQuanity;

        /**
         * Property value as a code, e.g., NTP4 (synced to NTP).
         */
        @Child(name = "valueCode", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Property value as a code, e.g., NTP4 (synced to NTP)", formalDefinition="Property value as a code, e.g., NTP4 (synced to NTP)." )
        protected List<CodeableConcept> valueCode;

        private static final long serialVersionUID = 1035620625L;

    /**
     * Constructor
     */
      public DeviceDefinitionPropertyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DeviceDefinitionPropertyComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Code that specifies the property DeviceDefinitionPropetyCode (Extensible).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionPropertyComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Code that specifies the property DeviceDefinitionPropetyCode (Extensible).)
         */
        public DeviceDefinitionPropertyComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #valueQuanity} (Property value as a quantity.)
         */
        public List<Quantity> getValueQuanity() { 
          if (this.valueQuanity == null)
            this.valueQuanity = new ArrayList<Quantity>();
          return this.valueQuanity;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DeviceDefinitionPropertyComponent setValueQuanity(List<Quantity> theValueQuanity) { 
          this.valueQuanity = theValueQuanity;
          return this;
        }

        public boolean hasValueQuanity() { 
          if (this.valueQuanity == null)
            return false;
          for (Quantity item : this.valueQuanity)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Quantity addValueQuanity() { //3
          Quantity t = new Quantity();
          if (this.valueQuanity == null)
            this.valueQuanity = new ArrayList<Quantity>();
          this.valueQuanity.add(t);
          return t;
        }

        public DeviceDefinitionPropertyComponent addValueQuanity(Quantity t) { //3
          if (t == null)
            return this;
          if (this.valueQuanity == null)
            this.valueQuanity = new ArrayList<Quantity>();
          this.valueQuanity.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #valueQuanity}, creating it if it does not already exist
         */
        public Quantity getValueQuanityFirstRep() { 
          if (getValueQuanity().isEmpty()) {
            addValueQuanity();
          }
          return getValueQuanity().get(0);
        }

        /**
         * @return {@link #valueCode} (Property value as a code, e.g., NTP4 (synced to NTP).)
         */
        public List<CodeableConcept> getValueCode() { 
          if (this.valueCode == null)
            this.valueCode = new ArrayList<CodeableConcept>();
          return this.valueCode;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DeviceDefinitionPropertyComponent setValueCode(List<CodeableConcept> theValueCode) { 
          this.valueCode = theValueCode;
          return this;
        }

        public boolean hasValueCode() { 
          if (this.valueCode == null)
            return false;
          for (CodeableConcept item : this.valueCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addValueCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.valueCode == null)
            this.valueCode = new ArrayList<CodeableConcept>();
          this.valueCode.add(t);
          return t;
        }

        public DeviceDefinitionPropertyComponent addValueCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.valueCode == null)
            this.valueCode = new ArrayList<CodeableConcept>();
          this.valueCode.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #valueCode}, creating it if it does not already exist
         */
        public CodeableConcept getValueCodeFirstRep() { 
          if (getValueCode().isEmpty()) {
            addValueCode();
          }
          return getValueCode().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Code that specifies the property DeviceDefinitionPropetyCode (Extensible).", 0, 1, type));
          children.add(new Property("valueQuanity", "Quantity", "Property value as a quantity.", 0, java.lang.Integer.MAX_VALUE, valueQuanity));
          children.add(new Property("valueCode", "CodeableConcept", "Property value as a code, e.g., NTP4 (synced to NTP).", 0, java.lang.Integer.MAX_VALUE, valueCode));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Code that specifies the property DeviceDefinitionPropetyCode (Extensible).", 0, 1, type);
          case 1319984908: /*valueQuanity*/  return new Property("valueQuanity", "Quantity", "Property value as a quantity.", 0, java.lang.Integer.MAX_VALUE, valueQuanity);
          case -766209282: /*valueCode*/  return new Property("valueCode", "CodeableConcept", "Property value as a code, e.g., NTP4 (synced to NTP).", 0, java.lang.Integer.MAX_VALUE, valueCode);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 1319984908: /*valueQuanity*/ return this.valueQuanity == null ? new Base[0] : this.valueQuanity.toArray(new Base[this.valueQuanity.size()]); // Quantity
        case -766209282: /*valueCode*/ return this.valueCode == null ? new Base[0] : this.valueCode.toArray(new Base[this.valueCode.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1319984908: // valueQuanity
          this.getValueQuanity().add(castToQuantity(value)); // Quantity
          return value;
        case -766209282: // valueCode
          this.getValueCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("valueQuanity")) {
          this.getValueQuanity().add(castToQuantity(value));
        } else if (name.equals("valueCode")) {
          this.getValueCode().add(castToCodeableConcept(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 1319984908:  return addValueQuanity(); 
        case -766209282:  return addValueCode(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 1319984908: /*valueQuanity*/ return new String[] {"Quantity"};
        case -766209282: /*valueCode*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("valueQuanity")) {
          return addValueQuanity();
        }
        else if (name.equals("valueCode")) {
          return addValueCode();
        }
        else
          return super.addChild(name);
      }

      public DeviceDefinitionPropertyComponent copy() {
        DeviceDefinitionPropertyComponent dst = new DeviceDefinitionPropertyComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (valueQuanity != null) {
          dst.valueQuanity = new ArrayList<Quantity>();
          for (Quantity i : valueQuanity)
            dst.valueQuanity.add(i.copy());
        };
        if (valueCode != null) {
          dst.valueCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : valueCode)
            dst.valueCode.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionPropertyComponent))
          return false;
        DeviceDefinitionPropertyComponent o = (DeviceDefinitionPropertyComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(valueQuanity, o.valueQuanity, true) && compareDeep(valueCode, o.valueCode, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionPropertyComponent))
          return false;
        DeviceDefinitionPropertyComponent o = (DeviceDefinitionPropertyComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, valueQuanity, valueCode
          );
      }

  public String fhirType() {
    return "DeviceDefinition.property";

  }

  }

    @Block()
    public static class DeviceDefinitionMaterialComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The substance.
         */
        @Child(name = "substance", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The substance", formalDefinition="The substance." )
        protected CodeableConcept substance;

        /**
         * Indicates an alternative material of the device.
         */
        @Child(name = "alternate", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Indicates an alternative material of the device", formalDefinition="Indicates an alternative material of the device." )
        protected BooleanType alternate;

        /**
         * Whether the substance is a known or suspected allergen.
         */
        @Child(name = "allergenicIndicator", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the substance is a known or suspected allergen", formalDefinition="Whether the substance is a known or suspected allergen." )
        protected BooleanType allergenicIndicator;

        private static final long serialVersionUID = 1232736508L;

    /**
     * Constructor
     */
      public DeviceDefinitionMaterialComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DeviceDefinitionMaterialComponent(CodeableConcept substance) {
        super();
        this.substance = substance;
      }

        /**
         * @return {@link #substance} (The substance.)
         */
        public CodeableConcept getSubstance() { 
          if (this.substance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionMaterialComponent.substance");
            else if (Configuration.doAutoCreate())
              this.substance = new CodeableConcept(); // cc
          return this.substance;
        }

        public boolean hasSubstance() { 
          return this.substance != null && !this.substance.isEmpty();
        }

        /**
         * @param value {@link #substance} (The substance.)
         */
        public DeviceDefinitionMaterialComponent setSubstance(CodeableConcept value) { 
          this.substance = value;
          return this;
        }

        /**
         * @return {@link #alternate} (Indicates an alternative material of the device.). This is the underlying object with id, value and extensions. The accessor "getAlternate" gives direct access to the value
         */
        public BooleanType getAlternateElement() { 
          if (this.alternate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionMaterialComponent.alternate");
            else if (Configuration.doAutoCreate())
              this.alternate = new BooleanType(); // bb
          return this.alternate;
        }

        public boolean hasAlternateElement() { 
          return this.alternate != null && !this.alternate.isEmpty();
        }

        public boolean hasAlternate() { 
          return this.alternate != null && !this.alternate.isEmpty();
        }

        /**
         * @param value {@link #alternate} (Indicates an alternative material of the device.). This is the underlying object with id, value and extensions. The accessor "getAlternate" gives direct access to the value
         */
        public DeviceDefinitionMaterialComponent setAlternateElement(BooleanType value) { 
          this.alternate = value;
          return this;
        }

        /**
         * @return Indicates an alternative material of the device.
         */
        public boolean getAlternate() { 
          return this.alternate == null || this.alternate.isEmpty() ? false : this.alternate.getValue();
        }

        /**
         * @param value Indicates an alternative material of the device.
         */
        public DeviceDefinitionMaterialComponent setAlternate(boolean value) { 
            if (this.alternate == null)
              this.alternate = new BooleanType();
            this.alternate.setValue(value);
          return this;
        }

        /**
         * @return {@link #allergenicIndicator} (Whether the substance is a known or suspected allergen.). This is the underlying object with id, value and extensions. The accessor "getAllergenicIndicator" gives direct access to the value
         */
        public BooleanType getAllergenicIndicatorElement() { 
          if (this.allergenicIndicator == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceDefinitionMaterialComponent.allergenicIndicator");
            else if (Configuration.doAutoCreate())
              this.allergenicIndicator = new BooleanType(); // bb
          return this.allergenicIndicator;
        }

        public boolean hasAllergenicIndicatorElement() { 
          return this.allergenicIndicator != null && !this.allergenicIndicator.isEmpty();
        }

        public boolean hasAllergenicIndicator() { 
          return this.allergenicIndicator != null && !this.allergenicIndicator.isEmpty();
        }

        /**
         * @param value {@link #allergenicIndicator} (Whether the substance is a known or suspected allergen.). This is the underlying object with id, value and extensions. The accessor "getAllergenicIndicator" gives direct access to the value
         */
        public DeviceDefinitionMaterialComponent setAllergenicIndicatorElement(BooleanType value) { 
          this.allergenicIndicator = value;
          return this;
        }

        /**
         * @return Whether the substance is a known or suspected allergen.
         */
        public boolean getAllergenicIndicator() { 
          return this.allergenicIndicator == null || this.allergenicIndicator.isEmpty() ? false : this.allergenicIndicator.getValue();
        }

        /**
         * @param value Whether the substance is a known or suspected allergen.
         */
        public DeviceDefinitionMaterialComponent setAllergenicIndicator(boolean value) { 
            if (this.allergenicIndicator == null)
              this.allergenicIndicator = new BooleanType();
            this.allergenicIndicator.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("substance", "CodeableConcept", "The substance.", 0, 1, substance));
          children.add(new Property("alternate", "boolean", "Indicates an alternative material of the device.", 0, 1, alternate));
          children.add(new Property("allergenicIndicator", "boolean", "Whether the substance is a known or suspected allergen.", 0, 1, allergenicIndicator));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 530040176: /*substance*/  return new Property("substance", "CodeableConcept", "The substance.", 0, 1, substance);
          case -1408024454: /*alternate*/  return new Property("alternate", "boolean", "Indicates an alternative material of the device.", 0, 1, alternate);
          case 75406931: /*allergenicIndicator*/  return new Property("allergenicIndicator", "boolean", "Whether the substance is a known or suspected allergen.", 0, 1, allergenicIndicator);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 530040176: /*substance*/ return this.substance == null ? new Base[0] : new Base[] {this.substance}; // CodeableConcept
        case -1408024454: /*alternate*/ return this.alternate == null ? new Base[0] : new Base[] {this.alternate}; // BooleanType
        case 75406931: /*allergenicIndicator*/ return this.allergenicIndicator == null ? new Base[0] : new Base[] {this.allergenicIndicator}; // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 530040176: // substance
          this.substance = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1408024454: // alternate
          this.alternate = castToBoolean(value); // BooleanType
          return value;
        case 75406931: // allergenicIndicator
          this.allergenicIndicator = castToBoolean(value); // BooleanType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("substance")) {
          this.substance = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("alternate")) {
          this.alternate = castToBoolean(value); // BooleanType
        } else if (name.equals("allergenicIndicator")) {
          this.allergenicIndicator = castToBoolean(value); // BooleanType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 530040176:  return getSubstance(); 
        case -1408024454:  return getAlternateElement();
        case 75406931:  return getAllergenicIndicatorElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 530040176: /*substance*/ return new String[] {"CodeableConcept"};
        case -1408024454: /*alternate*/ return new String[] {"boolean"};
        case 75406931: /*allergenicIndicator*/ return new String[] {"boolean"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("substance")) {
          this.substance = new CodeableConcept();
          return this.substance;
        }
        else if (name.equals("alternate")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.alternate");
        }
        else if (name.equals("allergenicIndicator")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.allergenicIndicator");
        }
        else
          return super.addChild(name);
      }

      public DeviceDefinitionMaterialComponent copy() {
        DeviceDefinitionMaterialComponent dst = new DeviceDefinitionMaterialComponent();
        copyValues(dst);
        dst.substance = substance == null ? null : substance.copy();
        dst.alternate = alternate == null ? null : alternate.copy();
        dst.allergenicIndicator = allergenicIndicator == null ? null : allergenicIndicator.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionMaterialComponent))
          return false;
        DeviceDefinitionMaterialComponent o = (DeviceDefinitionMaterialComponent) other_;
        return compareDeep(substance, o.substance, true) && compareDeep(alternate, o.alternate, true) && compareDeep(allergenicIndicator, o.allergenicIndicator, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DeviceDefinitionMaterialComponent))
          return false;
        DeviceDefinitionMaterialComponent o = (DeviceDefinitionMaterialComponent) other_;
        return compareValues(alternate, o.alternate, true) && compareValues(allergenicIndicator, o.allergenicIndicator, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(substance, alternate, allergenicIndicator
          );
      }

  public String fhirType() {
    return "DeviceDefinition.material";

  }

  }

    /**
     * Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For example: handle ID.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Instance identifier", formalDefinition="Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For example: handle ID." )
    protected List<Identifier> identifier;

    /**
     * Unique device identifier (UDI) assigned to device label or package.  Note that the Device may include multiple udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple jurisdictions it could have been sold.
     */
    @Child(name = "udiDeviceIdentifier", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Unique Device Identifier (UDI) Barcode string", formalDefinition="Unique device identifier (UDI) assigned to device label or package.  Note that the Device may include multiple udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple jurisdictions it could have been sold." )
    protected List<DeviceDefinitionUdiDeviceIdentifierComponent> udiDeviceIdentifier;

    /**
     * A name of the manufacturer.
     */
    @Child(name = "manufacturer", type = {StringType.class, Organization.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Name of device manufacturer", formalDefinition="A name of the manufacturer." )
    protected Type manufacturer;

    /**
     * A name given to the device to identify it.
     */
    @Child(name = "deviceName", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A name given to the device to identify it", formalDefinition="A name given to the device to identify it." )
    protected List<DeviceDefinitionDeviceNameComponent> deviceName;

    /**
     * The model number for the device.
     */
    @Child(name = "modelNumber", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The model number for the device", formalDefinition="The model number for the device." )
    protected StringType modelNumber;

    /**
     * What kind of device or device system this is.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What kind of device or device system this is", formalDefinition="What kind of device or device system this is." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-kind")
    protected CodeableConcept type;

    /**
     * The capabilities supported on a  device, the standards to which the device conforms for a particular purpose, and used for the communication.
     */
    @Child(name = "specialization", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The capabilities supported on a  device, the standards to which the device conforms for a particular purpose, and used for the communication", formalDefinition="The capabilities supported on a  device, the standards to which the device conforms for a particular purpose, and used for the communication." )
    protected List<DeviceDefinitionSpecializationComponent> specialization;

    /**
     * The actual design of the device or software version running on the device.
     */
    @Child(name = "version", type = {StringType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The actual design of the device or software version running on the device", formalDefinition="The actual design of the device or software version running on the device." )
    protected List<StringType> version;

    /**
     * Safety characteristics of the device.
     */
    @Child(name = "safety", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Safety characteristics of the device", formalDefinition="Safety characteristics of the device." )
    protected List<CodeableConcept> safety;

    /**
     * Shelf Life and storage information.
     */
    @Child(name = "shelfLifeStorage", type = {ProductShelfLife.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Shelf Life and storage information", formalDefinition="Shelf Life and storage information." )
    protected List<ProductShelfLife> shelfLifeStorage;

    /**
     * Dimensions, color etc.
     */
    @Child(name = "physicalCharacteristics", type = {ProdCharacteristic.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Dimensions, color etc.", formalDefinition="Dimensions, color etc." )
    protected ProdCharacteristic physicalCharacteristics;

    /**
     * Language code for the human-readable text strings produced by the device (all supported).
     */
    @Child(name = "languageCode", type = {CodeableConcept.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Language code for the human-readable text strings produced by the device (all supported)", formalDefinition="Language code for the human-readable text strings produced by the device (all supported)." )
    protected List<CodeableConcept> languageCode;

    /**
     * Device capabilities.
     */
    @Child(name = "capability", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Device capabilities", formalDefinition="Device capabilities." )
    protected List<DeviceDefinitionCapabilityComponent> capability;

    /**
     * The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.
     */
    @Child(name = "property", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties", formalDefinition="The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties." )
    protected List<DeviceDefinitionPropertyComponent> property;

    /**
     * An organization that is responsible for the provision and ongoing maintenance of the device.
     */
    @Child(name = "owner", type = {Organization.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Organization responsible for device", formalDefinition="An organization that is responsible for the provision and ongoing maintenance of the device." )
    protected Reference owner;

    /**
     * The actual object that is the target of the reference (An organization that is responsible for the provision and ongoing maintenance of the device.)
     */
    protected Organization ownerTarget;

    /**
     * Contact details for an organization or a particular human that is responsible for the device.
     */
    @Child(name = "contact", type = {ContactPoint.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Details for human/organization for support", formalDefinition="Contact details for an organization or a particular human that is responsible for the device." )
    protected List<ContactPoint> contact;

    /**
     * A network address on which the device may be contacted directly.
     */
    @Child(name = "url", type = {UriType.class}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Network address to contact device", formalDefinition="A network address on which the device may be contacted directly." )
    protected UriType url;

    /**
     * Access to on-line information about the device.
     */
    @Child(name = "onlineInformation", type = {UriType.class}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Access to on-line information", formalDefinition="Access to on-line information about the device." )
    protected UriType onlineInformation;

    /**
     * Descriptive information, usage information or implantation information that is not captured in an existing element.
     */
    @Child(name = "note", type = {Annotation.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Device notes and comments", formalDefinition="Descriptive information, usage information or implantation information that is not captured in an existing element." )
    protected List<Annotation> note;

    /**
     * The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of devices in the same package of the medicinal product).
     */
    @Child(name = "quantity", type = {Quantity.class}, order=19, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of devices in the same package of the medicinal product)", formalDefinition="The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of devices in the same package of the medicinal product)." )
    protected Quantity quantity;

    /**
     * The parent device it can be part of.
     */
    @Child(name = "parentDevice", type = {DeviceDefinition.class}, order=20, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The parent device it can be part of", formalDefinition="The parent device it can be part of." )
    protected Reference parentDevice;

    /**
     * The actual object that is the target of the reference (The parent device it can be part of.)
     */
    protected DeviceDefinition parentDeviceTarget;

    /**
     * A substance used to create the material(s) of which the device is made.
     */
    @Child(name = "material", type = {}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A substance used to create the material(s) of which the device is made", formalDefinition="A substance used to create the material(s) of which the device is made." )
    protected List<DeviceDefinitionMaterialComponent> material;

    private static final long serialVersionUID = -2041532433L;

  /**
   * Constructor
   */
    public DeviceDefinition() {
      super();
    }

    /**
     * @return {@link #identifier} (Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For example: handle ID.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public DeviceDefinition addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #udiDeviceIdentifier} (Unique device identifier (UDI) assigned to device label or package.  Note that the Device may include multiple udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple jurisdictions it could have been sold.)
     */
    public List<DeviceDefinitionUdiDeviceIdentifierComponent> getUdiDeviceIdentifier() { 
      if (this.udiDeviceIdentifier == null)
        this.udiDeviceIdentifier = new ArrayList<DeviceDefinitionUdiDeviceIdentifierComponent>();
      return this.udiDeviceIdentifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setUdiDeviceIdentifier(List<DeviceDefinitionUdiDeviceIdentifierComponent> theUdiDeviceIdentifier) { 
      this.udiDeviceIdentifier = theUdiDeviceIdentifier;
      return this;
    }

    public boolean hasUdiDeviceIdentifier() { 
      if (this.udiDeviceIdentifier == null)
        return false;
      for (DeviceDefinitionUdiDeviceIdentifierComponent item : this.udiDeviceIdentifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DeviceDefinitionUdiDeviceIdentifierComponent addUdiDeviceIdentifier() { //3
      DeviceDefinitionUdiDeviceIdentifierComponent t = new DeviceDefinitionUdiDeviceIdentifierComponent();
      if (this.udiDeviceIdentifier == null)
        this.udiDeviceIdentifier = new ArrayList<DeviceDefinitionUdiDeviceIdentifierComponent>();
      this.udiDeviceIdentifier.add(t);
      return t;
    }

    public DeviceDefinition addUdiDeviceIdentifier(DeviceDefinitionUdiDeviceIdentifierComponent t) { //3
      if (t == null)
        return this;
      if (this.udiDeviceIdentifier == null)
        this.udiDeviceIdentifier = new ArrayList<DeviceDefinitionUdiDeviceIdentifierComponent>();
      this.udiDeviceIdentifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #udiDeviceIdentifier}, creating it if it does not already exist
     */
    public DeviceDefinitionUdiDeviceIdentifierComponent getUdiDeviceIdentifierFirstRep() { 
      if (getUdiDeviceIdentifier().isEmpty()) {
        addUdiDeviceIdentifier();
      }
      return getUdiDeviceIdentifier().get(0);
    }

    /**
     * @return {@link #manufacturer} (A name of the manufacturer.)
     */
    public Type getManufacturer() { 
      return this.manufacturer;
    }

    /**
     * @return {@link #manufacturer} (A name of the manufacturer.)
     */
    public StringType getManufacturerStringType() throws FHIRException { 
      if (this.manufacturer == null)
        return null;
      if (!(this.manufacturer instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.manufacturer.getClass().getName()+" was encountered");
      return (StringType) this.manufacturer;
    }

    public boolean hasManufacturerStringType() { 
      return this != null && this.manufacturer instanceof StringType;
    }

    /**
     * @return {@link #manufacturer} (A name of the manufacturer.)
     */
    public Reference getManufacturerReference() throws FHIRException { 
      if (this.manufacturer == null)
        return null;
      if (!(this.manufacturer instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.manufacturer.getClass().getName()+" was encountered");
      return (Reference) this.manufacturer;
    }

    public boolean hasManufacturerReference() { 
      return this != null && this.manufacturer instanceof Reference;
    }

    public boolean hasManufacturer() { 
      return this.manufacturer != null && !this.manufacturer.isEmpty();
    }

    /**
     * @param value {@link #manufacturer} (A name of the manufacturer.)
     */
    public DeviceDefinition setManufacturer(Type value) { 
      if (value != null && !(value instanceof StringType || value instanceof Reference))
        throw new Error("Not the right type for DeviceDefinition.manufacturer[x]: "+value.fhirType());
      this.manufacturer = value;
      return this;
    }

    /**
     * @return {@link #deviceName} (A name given to the device to identify it.)
     */
    public List<DeviceDefinitionDeviceNameComponent> getDeviceName() { 
      if (this.deviceName == null)
        this.deviceName = new ArrayList<DeviceDefinitionDeviceNameComponent>();
      return this.deviceName;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setDeviceName(List<DeviceDefinitionDeviceNameComponent> theDeviceName) { 
      this.deviceName = theDeviceName;
      return this;
    }

    public boolean hasDeviceName() { 
      if (this.deviceName == null)
        return false;
      for (DeviceDefinitionDeviceNameComponent item : this.deviceName)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DeviceDefinitionDeviceNameComponent addDeviceName() { //3
      DeviceDefinitionDeviceNameComponent t = new DeviceDefinitionDeviceNameComponent();
      if (this.deviceName == null)
        this.deviceName = new ArrayList<DeviceDefinitionDeviceNameComponent>();
      this.deviceName.add(t);
      return t;
    }

    public DeviceDefinition addDeviceName(DeviceDefinitionDeviceNameComponent t) { //3
      if (t == null)
        return this;
      if (this.deviceName == null)
        this.deviceName = new ArrayList<DeviceDefinitionDeviceNameComponent>();
      this.deviceName.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #deviceName}, creating it if it does not already exist
     */
    public DeviceDefinitionDeviceNameComponent getDeviceNameFirstRep() { 
      if (getDeviceName().isEmpty()) {
        addDeviceName();
      }
      return getDeviceName().get(0);
    }

    /**
     * @return {@link #modelNumber} (The model number for the device.). This is the underlying object with id, value and extensions. The accessor "getModelNumber" gives direct access to the value
     */
    public StringType getModelNumberElement() { 
      if (this.modelNumber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceDefinition.modelNumber");
        else if (Configuration.doAutoCreate())
          this.modelNumber = new StringType(); // bb
      return this.modelNumber;
    }

    public boolean hasModelNumberElement() { 
      return this.modelNumber != null && !this.modelNumber.isEmpty();
    }

    public boolean hasModelNumber() { 
      return this.modelNumber != null && !this.modelNumber.isEmpty();
    }

    /**
     * @param value {@link #modelNumber} (The model number for the device.). This is the underlying object with id, value and extensions. The accessor "getModelNumber" gives direct access to the value
     */
    public DeviceDefinition setModelNumberElement(StringType value) { 
      this.modelNumber = value;
      return this;
    }

    /**
     * @return The model number for the device.
     */
    public String getModelNumber() { 
      return this.modelNumber == null ? null : this.modelNumber.getValue();
    }

    /**
     * @param value The model number for the device.
     */
    public DeviceDefinition setModelNumber(String value) { 
      if (Utilities.noString(value))
        this.modelNumber = null;
      else {
        if (this.modelNumber == null)
          this.modelNumber = new StringType();
        this.modelNumber.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (What kind of device or device system this is.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceDefinition.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (What kind of device or device system this is.)
     */
    public DeviceDefinition setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #specialization} (The capabilities supported on a  device, the standards to which the device conforms for a particular purpose, and used for the communication.)
     */
    public List<DeviceDefinitionSpecializationComponent> getSpecialization() { 
      if (this.specialization == null)
        this.specialization = new ArrayList<DeviceDefinitionSpecializationComponent>();
      return this.specialization;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setSpecialization(List<DeviceDefinitionSpecializationComponent> theSpecialization) { 
      this.specialization = theSpecialization;
      return this;
    }

    public boolean hasSpecialization() { 
      if (this.specialization == null)
        return false;
      for (DeviceDefinitionSpecializationComponent item : this.specialization)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DeviceDefinitionSpecializationComponent addSpecialization() { //3
      DeviceDefinitionSpecializationComponent t = new DeviceDefinitionSpecializationComponent();
      if (this.specialization == null)
        this.specialization = new ArrayList<DeviceDefinitionSpecializationComponent>();
      this.specialization.add(t);
      return t;
    }

    public DeviceDefinition addSpecialization(DeviceDefinitionSpecializationComponent t) { //3
      if (t == null)
        return this;
      if (this.specialization == null)
        this.specialization = new ArrayList<DeviceDefinitionSpecializationComponent>();
      this.specialization.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #specialization}, creating it if it does not already exist
     */
    public DeviceDefinitionSpecializationComponent getSpecializationFirstRep() { 
      if (getSpecialization().isEmpty()) {
        addSpecialization();
      }
      return getSpecialization().get(0);
    }

    /**
     * @return {@link #version} (The actual design of the device or software version running on the device.)
     */
    public List<StringType> getVersion() { 
      if (this.version == null)
        this.version = new ArrayList<StringType>();
      return this.version;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setVersion(List<StringType> theVersion) { 
      this.version = theVersion;
      return this;
    }

    public boolean hasVersion() { 
      if (this.version == null)
        return false;
      for (StringType item : this.version)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #version} (The actual design of the device or software version running on the device.)
     */
    public StringType addVersionElement() {//2 
      StringType t = new StringType();
      if (this.version == null)
        this.version = new ArrayList<StringType>();
      this.version.add(t);
      return t;
    }

    /**
     * @param value {@link #version} (The actual design of the device or software version running on the device.)
     */
    public DeviceDefinition addVersion(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.version == null)
        this.version = new ArrayList<StringType>();
      this.version.add(t);
      return this;
    }

    /**
     * @param value {@link #version} (The actual design of the device or software version running on the device.)
     */
    public boolean hasVersion(String value) { 
      if (this.version == null)
        return false;
      for (StringType v : this.version)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #safety} (Safety characteristics of the device.)
     */
    public List<CodeableConcept> getSafety() { 
      if (this.safety == null)
        this.safety = new ArrayList<CodeableConcept>();
      return this.safety;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setSafety(List<CodeableConcept> theSafety) { 
      this.safety = theSafety;
      return this;
    }

    public boolean hasSafety() { 
      if (this.safety == null)
        return false;
      for (CodeableConcept item : this.safety)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addSafety() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.safety == null)
        this.safety = new ArrayList<CodeableConcept>();
      this.safety.add(t);
      return t;
    }

    public DeviceDefinition addSafety(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.safety == null)
        this.safety = new ArrayList<CodeableConcept>();
      this.safety.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #safety}, creating it if it does not already exist
     */
    public CodeableConcept getSafetyFirstRep() { 
      if (getSafety().isEmpty()) {
        addSafety();
      }
      return getSafety().get(0);
    }

    /**
     * @return {@link #shelfLifeStorage} (Shelf Life and storage information.)
     */
    public List<ProductShelfLife> getShelfLifeStorage() { 
      if (this.shelfLifeStorage == null)
        this.shelfLifeStorage = new ArrayList<ProductShelfLife>();
      return this.shelfLifeStorage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setShelfLifeStorage(List<ProductShelfLife> theShelfLifeStorage) { 
      this.shelfLifeStorage = theShelfLifeStorage;
      return this;
    }

    public boolean hasShelfLifeStorage() { 
      if (this.shelfLifeStorage == null)
        return false;
      for (ProductShelfLife item : this.shelfLifeStorage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProductShelfLife addShelfLifeStorage() { //3
      ProductShelfLife t = new ProductShelfLife();
      if (this.shelfLifeStorage == null)
        this.shelfLifeStorage = new ArrayList<ProductShelfLife>();
      this.shelfLifeStorage.add(t);
      return t;
    }

    public DeviceDefinition addShelfLifeStorage(ProductShelfLife t) { //3
      if (t == null)
        return this;
      if (this.shelfLifeStorage == null)
        this.shelfLifeStorage = new ArrayList<ProductShelfLife>();
      this.shelfLifeStorage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #shelfLifeStorage}, creating it if it does not already exist
     */
    public ProductShelfLife getShelfLifeStorageFirstRep() { 
      if (getShelfLifeStorage().isEmpty()) {
        addShelfLifeStorage();
      }
      return getShelfLifeStorage().get(0);
    }

    /**
     * @return {@link #physicalCharacteristics} (Dimensions, color etc.)
     */
    public ProdCharacteristic getPhysicalCharacteristics() { 
      if (this.physicalCharacteristics == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceDefinition.physicalCharacteristics");
        else if (Configuration.doAutoCreate())
          this.physicalCharacteristics = new ProdCharacteristic(); // cc
      return this.physicalCharacteristics;
    }

    public boolean hasPhysicalCharacteristics() { 
      return this.physicalCharacteristics != null && !this.physicalCharacteristics.isEmpty();
    }

    /**
     * @param value {@link #physicalCharacteristics} (Dimensions, color etc.)
     */
    public DeviceDefinition setPhysicalCharacteristics(ProdCharacteristic value) { 
      this.physicalCharacteristics = value;
      return this;
    }

    /**
     * @return {@link #languageCode} (Language code for the human-readable text strings produced by the device (all supported).)
     */
    public List<CodeableConcept> getLanguageCode() { 
      if (this.languageCode == null)
        this.languageCode = new ArrayList<CodeableConcept>();
      return this.languageCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setLanguageCode(List<CodeableConcept> theLanguageCode) { 
      this.languageCode = theLanguageCode;
      return this;
    }

    public boolean hasLanguageCode() { 
      if (this.languageCode == null)
        return false;
      for (CodeableConcept item : this.languageCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addLanguageCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.languageCode == null)
        this.languageCode = new ArrayList<CodeableConcept>();
      this.languageCode.add(t);
      return t;
    }

    public DeviceDefinition addLanguageCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.languageCode == null)
        this.languageCode = new ArrayList<CodeableConcept>();
      this.languageCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #languageCode}, creating it if it does not already exist
     */
    public CodeableConcept getLanguageCodeFirstRep() { 
      if (getLanguageCode().isEmpty()) {
        addLanguageCode();
      }
      return getLanguageCode().get(0);
    }

    /**
     * @return {@link #capability} (Device capabilities.)
     */
    public List<DeviceDefinitionCapabilityComponent> getCapability() { 
      if (this.capability == null)
        this.capability = new ArrayList<DeviceDefinitionCapabilityComponent>();
      return this.capability;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setCapability(List<DeviceDefinitionCapabilityComponent> theCapability) { 
      this.capability = theCapability;
      return this;
    }

    public boolean hasCapability() { 
      if (this.capability == null)
        return false;
      for (DeviceDefinitionCapabilityComponent item : this.capability)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DeviceDefinitionCapabilityComponent addCapability() { //3
      DeviceDefinitionCapabilityComponent t = new DeviceDefinitionCapabilityComponent();
      if (this.capability == null)
        this.capability = new ArrayList<DeviceDefinitionCapabilityComponent>();
      this.capability.add(t);
      return t;
    }

    public DeviceDefinition addCapability(DeviceDefinitionCapabilityComponent t) { //3
      if (t == null)
        return this;
      if (this.capability == null)
        this.capability = new ArrayList<DeviceDefinitionCapabilityComponent>();
      this.capability.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #capability}, creating it if it does not already exist
     */
    public DeviceDefinitionCapabilityComponent getCapabilityFirstRep() { 
      if (getCapability().isEmpty()) {
        addCapability();
      }
      return getCapability().get(0);
    }

    /**
     * @return {@link #property} (The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.)
     */
    public List<DeviceDefinitionPropertyComponent> getProperty() { 
      if (this.property == null)
        this.property = new ArrayList<DeviceDefinitionPropertyComponent>();
      return this.property;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setProperty(List<DeviceDefinitionPropertyComponent> theProperty) { 
      this.property = theProperty;
      return this;
    }

    public boolean hasProperty() { 
      if (this.property == null)
        return false;
      for (DeviceDefinitionPropertyComponent item : this.property)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DeviceDefinitionPropertyComponent addProperty() { //3
      DeviceDefinitionPropertyComponent t = new DeviceDefinitionPropertyComponent();
      if (this.property == null)
        this.property = new ArrayList<DeviceDefinitionPropertyComponent>();
      this.property.add(t);
      return t;
    }

    public DeviceDefinition addProperty(DeviceDefinitionPropertyComponent t) { //3
      if (t == null)
        return this;
      if (this.property == null)
        this.property = new ArrayList<DeviceDefinitionPropertyComponent>();
      this.property.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #property}, creating it if it does not already exist
     */
    public DeviceDefinitionPropertyComponent getPropertyFirstRep() { 
      if (getProperty().isEmpty()) {
        addProperty();
      }
      return getProperty().get(0);
    }

    /**
     * @return {@link #owner} (An organization that is responsible for the provision and ongoing maintenance of the device.)
     */
    public Reference getOwner() { 
      if (this.owner == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceDefinition.owner");
        else if (Configuration.doAutoCreate())
          this.owner = new Reference(); // cc
      return this.owner;
    }

    public boolean hasOwner() { 
      return this.owner != null && !this.owner.isEmpty();
    }

    /**
     * @param value {@link #owner} (An organization that is responsible for the provision and ongoing maintenance of the device.)
     */
    public DeviceDefinition setOwner(Reference value) { 
      this.owner = value;
      return this;
    }

    /**
     * @return {@link #owner} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An organization that is responsible for the provision and ongoing maintenance of the device.)
     */
    public Organization getOwnerTarget() { 
      if (this.ownerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceDefinition.owner");
        else if (Configuration.doAutoCreate())
          this.ownerTarget = new Organization(); // aa
      return this.ownerTarget;
    }

    /**
     * @param value {@link #owner} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An organization that is responsible for the provision and ongoing maintenance of the device.)
     */
    public DeviceDefinition setOwnerTarget(Organization value) { 
      this.ownerTarget = value;
      return this;
    }

    /**
     * @return {@link #contact} (Contact details for an organization or a particular human that is responsible for the device.)
     */
    public List<ContactPoint> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactPoint>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setContact(List<ContactPoint> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactPoint item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactPoint addContact() { //3
      ContactPoint t = new ContactPoint();
      if (this.contact == null)
        this.contact = new ArrayList<ContactPoint>();
      this.contact.add(t);
      return t;
    }

    public DeviceDefinition addContact(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactPoint>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactPoint getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #url} (A network address on which the device may be contacted directly.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceDefinition.url");
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
     * @param value {@link #url} (A network address on which the device may be contacted directly.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public DeviceDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return A network address on which the device may be contacted directly.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value A network address on which the device may be contacted directly.
     */
    public DeviceDefinition setUrl(String value) { 
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
     * @return {@link #onlineInformation} (Access to on-line information about the device.). This is the underlying object with id, value and extensions. The accessor "getOnlineInformation" gives direct access to the value
     */
    public UriType getOnlineInformationElement() { 
      if (this.onlineInformation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceDefinition.onlineInformation");
        else if (Configuration.doAutoCreate())
          this.onlineInformation = new UriType(); // bb
      return this.onlineInformation;
    }

    public boolean hasOnlineInformationElement() { 
      return this.onlineInformation != null && !this.onlineInformation.isEmpty();
    }

    public boolean hasOnlineInformation() { 
      return this.onlineInformation != null && !this.onlineInformation.isEmpty();
    }

    /**
     * @param value {@link #onlineInformation} (Access to on-line information about the device.). This is the underlying object with id, value and extensions. The accessor "getOnlineInformation" gives direct access to the value
     */
    public DeviceDefinition setOnlineInformationElement(UriType value) { 
      this.onlineInformation = value;
      return this;
    }

    /**
     * @return Access to on-line information about the device.
     */
    public String getOnlineInformation() { 
      return this.onlineInformation == null ? null : this.onlineInformation.getValue();
    }

    /**
     * @param value Access to on-line information about the device.
     */
    public DeviceDefinition setOnlineInformation(String value) { 
      if (Utilities.noString(value))
        this.onlineInformation = null;
      else {
        if (this.onlineInformation == null)
          this.onlineInformation = new UriType();
        this.onlineInformation.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #note} (Descriptive information, usage information or implantation information that is not captured in an existing element.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public DeviceDefinition addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
    }

    /**
     * @return {@link #quantity} (The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of devices in the same package of the medicinal product).)
     */
    public Quantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceDefinition.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new Quantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of devices in the same package of the medicinal product).)
     */
    public DeviceDefinition setQuantity(Quantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #parentDevice} (The parent device it can be part of.)
     */
    public Reference getParentDevice() { 
      if (this.parentDevice == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceDefinition.parentDevice");
        else if (Configuration.doAutoCreate())
          this.parentDevice = new Reference(); // cc
      return this.parentDevice;
    }

    public boolean hasParentDevice() { 
      return this.parentDevice != null && !this.parentDevice.isEmpty();
    }

    /**
     * @param value {@link #parentDevice} (The parent device it can be part of.)
     */
    public DeviceDefinition setParentDevice(Reference value) { 
      this.parentDevice = value;
      return this;
    }

    /**
     * @return {@link #parentDevice} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The parent device it can be part of.)
     */
    public DeviceDefinition getParentDeviceTarget() { 
      if (this.parentDeviceTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceDefinition.parentDevice");
        else if (Configuration.doAutoCreate())
          this.parentDeviceTarget = new DeviceDefinition(); // aa
      return this.parentDeviceTarget;
    }

    /**
     * @param value {@link #parentDevice} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The parent device it can be part of.)
     */
    public DeviceDefinition setParentDeviceTarget(DeviceDefinition value) { 
      this.parentDeviceTarget = value;
      return this;
    }

    /**
     * @return {@link #material} (A substance used to create the material(s) of which the device is made.)
     */
    public List<DeviceDefinitionMaterialComponent> getMaterial() { 
      if (this.material == null)
        this.material = new ArrayList<DeviceDefinitionMaterialComponent>();
      return this.material;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceDefinition setMaterial(List<DeviceDefinitionMaterialComponent> theMaterial) { 
      this.material = theMaterial;
      return this;
    }

    public boolean hasMaterial() { 
      if (this.material == null)
        return false;
      for (DeviceDefinitionMaterialComponent item : this.material)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DeviceDefinitionMaterialComponent addMaterial() { //3
      DeviceDefinitionMaterialComponent t = new DeviceDefinitionMaterialComponent();
      if (this.material == null)
        this.material = new ArrayList<DeviceDefinitionMaterialComponent>();
      this.material.add(t);
      return t;
    }

    public DeviceDefinition addMaterial(DeviceDefinitionMaterialComponent t) { //3
      if (t == null)
        return this;
      if (this.material == null)
        this.material = new ArrayList<DeviceDefinitionMaterialComponent>();
      this.material.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #material}, creating it if it does not already exist
     */
    public DeviceDefinitionMaterialComponent getMaterialFirstRep() { 
      if (getMaterial().isEmpty()) {
        addMaterial();
      }
      return getMaterial().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For example: handle ID.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("udiDeviceIdentifier", "", "Unique device identifier (UDI) assigned to device label or package.  Note that the Device may include multiple udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple jurisdictions it could have been sold.", 0, java.lang.Integer.MAX_VALUE, udiDeviceIdentifier));
        children.add(new Property("manufacturer[x]", "string|Reference(Organization)", "A name of the manufacturer.", 0, 1, manufacturer));
        children.add(new Property("deviceName", "", "A name given to the device to identify it.", 0, java.lang.Integer.MAX_VALUE, deviceName));
        children.add(new Property("modelNumber", "string", "The model number for the device.", 0, 1, modelNumber));
        children.add(new Property("type", "CodeableConcept", "What kind of device or device system this is.", 0, 1, type));
        children.add(new Property("specialization", "", "The capabilities supported on a  device, the standards to which the device conforms for a particular purpose, and used for the communication.", 0, java.lang.Integer.MAX_VALUE, specialization));
        children.add(new Property("version", "string", "The actual design of the device or software version running on the device.", 0, java.lang.Integer.MAX_VALUE, version));
        children.add(new Property("safety", "CodeableConcept", "Safety characteristics of the device.", 0, java.lang.Integer.MAX_VALUE, safety));
        children.add(new Property("shelfLifeStorage", "ProductShelfLife", "Shelf Life and storage information.", 0, java.lang.Integer.MAX_VALUE, shelfLifeStorage));
        children.add(new Property("physicalCharacteristics", "ProdCharacteristic", "Dimensions, color etc.", 0, 1, physicalCharacteristics));
        children.add(new Property("languageCode", "CodeableConcept", "Language code for the human-readable text strings produced by the device (all supported).", 0, java.lang.Integer.MAX_VALUE, languageCode));
        children.add(new Property("capability", "", "Device capabilities.", 0, java.lang.Integer.MAX_VALUE, capability));
        children.add(new Property("property", "", "The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.", 0, java.lang.Integer.MAX_VALUE, property));
        children.add(new Property("owner", "Reference(Organization)", "An organization that is responsible for the provision and ongoing maintenance of the device.", 0, 1, owner));
        children.add(new Property("contact", "ContactPoint", "Contact details for an organization or a particular human that is responsible for the device.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("url", "uri", "A network address on which the device may be contacted directly.", 0, 1, url));
        children.add(new Property("onlineInformation", "uri", "Access to on-line information about the device.", 0, 1, onlineInformation));
        children.add(new Property("note", "Annotation", "Descriptive information, usage information or implantation information that is not captured in an existing element.", 0, java.lang.Integer.MAX_VALUE, note));
        children.add(new Property("quantity", "Quantity", "The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of devices in the same package of the medicinal product).", 0, 1, quantity));
        children.add(new Property("parentDevice", "Reference(DeviceDefinition)", "The parent device it can be part of.", 0, 1, parentDevice));
        children.add(new Property("material", "", "A substance used to create the material(s) of which the device is made.", 0, java.lang.Integer.MAX_VALUE, material));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For example: handle ID.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -99121287: /*udiDeviceIdentifier*/  return new Property("udiDeviceIdentifier", "", "Unique device identifier (UDI) assigned to device label or package.  Note that the Device may include multiple udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple jurisdictions it could have been sold.", 0, java.lang.Integer.MAX_VALUE, udiDeviceIdentifier);
        case 418079503: /*manufacturer[x]*/  return new Property("manufacturer[x]", "string|Reference(Organization)", "A name of the manufacturer.", 0, 1, manufacturer);
        case -1969347631: /*manufacturer*/  return new Property("manufacturer[x]", "string|Reference(Organization)", "A name of the manufacturer.", 0, 1, manufacturer);
        case -630681790: /*manufacturerString*/  return new Property("manufacturer[x]", "string|Reference(Organization)", "A name of the manufacturer.", 0, 1, manufacturer);
        case 1104934522: /*manufacturerReference*/  return new Property("manufacturer[x]", "string|Reference(Organization)", "A name of the manufacturer.", 0, 1, manufacturer);
        case 780988929: /*deviceName*/  return new Property("deviceName", "", "A name given to the device to identify it.", 0, java.lang.Integer.MAX_VALUE, deviceName);
        case 346619858: /*modelNumber*/  return new Property("modelNumber", "string", "The model number for the device.", 0, 1, modelNumber);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "What kind of device or device system this is.", 0, 1, type);
        case 682815883: /*specialization*/  return new Property("specialization", "", "The capabilities supported on a  device, the standards to which the device conforms for a particular purpose, and used for the communication.", 0, java.lang.Integer.MAX_VALUE, specialization);
        case 351608024: /*version*/  return new Property("version", "string", "The actual design of the device or software version running on the device.", 0, java.lang.Integer.MAX_VALUE, version);
        case -909893934: /*safety*/  return new Property("safety", "CodeableConcept", "Safety characteristics of the device.", 0, java.lang.Integer.MAX_VALUE, safety);
        case 172049237: /*shelfLifeStorage*/  return new Property("shelfLifeStorage", "ProductShelfLife", "Shelf Life and storage information.", 0, java.lang.Integer.MAX_VALUE, shelfLifeStorage);
        case -1599676319: /*physicalCharacteristics*/  return new Property("physicalCharacteristics", "ProdCharacteristic", "Dimensions, color etc.", 0, 1, physicalCharacteristics);
        case -2092349083: /*languageCode*/  return new Property("languageCode", "CodeableConcept", "Language code for the human-readable text strings produced by the device (all supported).", 0, java.lang.Integer.MAX_VALUE, languageCode);
        case -783669992: /*capability*/  return new Property("capability", "", "Device capabilities.", 0, java.lang.Integer.MAX_VALUE, capability);
        case -993141291: /*property*/  return new Property("property", "", "The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.", 0, java.lang.Integer.MAX_VALUE, property);
        case 106164915: /*owner*/  return new Property("owner", "Reference(Organization)", "An organization that is responsible for the provision and ongoing maintenance of the device.", 0, 1, owner);
        case 951526432: /*contact*/  return new Property("contact", "ContactPoint", "Contact details for an organization or a particular human that is responsible for the device.", 0, java.lang.Integer.MAX_VALUE, contact);
        case 116079: /*url*/  return new Property("url", "uri", "A network address on which the device may be contacted directly.", 0, 1, url);
        case -788511527: /*onlineInformation*/  return new Property("onlineInformation", "uri", "Access to on-line information about the device.", 0, 1, onlineInformation);
        case 3387378: /*note*/  return new Property("note", "Annotation", "Descriptive information, usage information or implantation information that is not captured in an existing element.", 0, java.lang.Integer.MAX_VALUE, note);
        case -1285004149: /*quantity*/  return new Property("quantity", "Quantity", "The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of devices in the same package of the medicinal product).", 0, 1, quantity);
        case 620260256: /*parentDevice*/  return new Property("parentDevice", "Reference(DeviceDefinition)", "The parent device it can be part of.", 0, 1, parentDevice);
        case 299066663: /*material*/  return new Property("material", "", "A substance used to create the material(s) of which the device is made.", 0, java.lang.Integer.MAX_VALUE, material);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -99121287: /*udiDeviceIdentifier*/ return this.udiDeviceIdentifier == null ? new Base[0] : this.udiDeviceIdentifier.toArray(new Base[this.udiDeviceIdentifier.size()]); // DeviceDefinitionUdiDeviceIdentifierComponent
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : new Base[] {this.manufacturer}; // Type
        case 780988929: /*deviceName*/ return this.deviceName == null ? new Base[0] : this.deviceName.toArray(new Base[this.deviceName.size()]); // DeviceDefinitionDeviceNameComponent
        case 346619858: /*modelNumber*/ return this.modelNumber == null ? new Base[0] : new Base[] {this.modelNumber}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 682815883: /*specialization*/ return this.specialization == null ? new Base[0] : this.specialization.toArray(new Base[this.specialization.size()]); // DeviceDefinitionSpecializationComponent
        case 351608024: /*version*/ return this.version == null ? new Base[0] : this.version.toArray(new Base[this.version.size()]); // StringType
        case -909893934: /*safety*/ return this.safety == null ? new Base[0] : this.safety.toArray(new Base[this.safety.size()]); // CodeableConcept
        case 172049237: /*shelfLifeStorage*/ return this.shelfLifeStorage == null ? new Base[0] : this.shelfLifeStorage.toArray(new Base[this.shelfLifeStorage.size()]); // ProductShelfLife
        case -1599676319: /*physicalCharacteristics*/ return this.physicalCharacteristics == null ? new Base[0] : new Base[] {this.physicalCharacteristics}; // ProdCharacteristic
        case -2092349083: /*languageCode*/ return this.languageCode == null ? new Base[0] : this.languageCode.toArray(new Base[this.languageCode.size()]); // CodeableConcept
        case -783669992: /*capability*/ return this.capability == null ? new Base[0] : this.capability.toArray(new Base[this.capability.size()]); // DeviceDefinitionCapabilityComponent
        case -993141291: /*property*/ return this.property == null ? new Base[0] : this.property.toArray(new Base[this.property.size()]); // DeviceDefinitionPropertyComponent
        case 106164915: /*owner*/ return this.owner == null ? new Base[0] : new Base[] {this.owner}; // Reference
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactPoint
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -788511527: /*onlineInformation*/ return this.onlineInformation == null ? new Base[0] : new Base[] {this.onlineInformation}; // UriType
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case 620260256: /*parentDevice*/ return this.parentDevice == null ? new Base[0] : new Base[] {this.parentDevice}; // Reference
        case 299066663: /*material*/ return this.material == null ? new Base[0] : this.material.toArray(new Base[this.material.size()]); // DeviceDefinitionMaterialComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -99121287: // udiDeviceIdentifier
          this.getUdiDeviceIdentifier().add((DeviceDefinitionUdiDeviceIdentifierComponent) value); // DeviceDefinitionUdiDeviceIdentifierComponent
          return value;
        case -1969347631: // manufacturer
          this.manufacturer = castToType(value); // Type
          return value;
        case 780988929: // deviceName
          this.getDeviceName().add((DeviceDefinitionDeviceNameComponent) value); // DeviceDefinitionDeviceNameComponent
          return value;
        case 346619858: // modelNumber
          this.modelNumber = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 682815883: // specialization
          this.getSpecialization().add((DeviceDefinitionSpecializationComponent) value); // DeviceDefinitionSpecializationComponent
          return value;
        case 351608024: // version
          this.getVersion().add(castToString(value)); // StringType
          return value;
        case -909893934: // safety
          this.getSafety().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 172049237: // shelfLifeStorage
          this.getShelfLifeStorage().add(castToProductShelfLife(value)); // ProductShelfLife
          return value;
        case -1599676319: // physicalCharacteristics
          this.physicalCharacteristics = castToProdCharacteristic(value); // ProdCharacteristic
          return value;
        case -2092349083: // languageCode
          this.getLanguageCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -783669992: // capability
          this.getCapability().add((DeviceDefinitionCapabilityComponent) value); // DeviceDefinitionCapabilityComponent
          return value;
        case -993141291: // property
          this.getProperty().add((DeviceDefinitionPropertyComponent) value); // DeviceDefinitionPropertyComponent
          return value;
        case 106164915: // owner
          this.owner = castToReference(value); // Reference
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactPoint(value)); // ContactPoint
          return value;
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case -788511527: // onlineInformation
          this.onlineInformation = castToUri(value); // UriType
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case 620260256: // parentDevice
          this.parentDevice = castToReference(value); // Reference
          return value;
        case 299066663: // material
          this.getMaterial().add((DeviceDefinitionMaterialComponent) value); // DeviceDefinitionMaterialComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("udiDeviceIdentifier")) {
          this.getUdiDeviceIdentifier().add((DeviceDefinitionUdiDeviceIdentifierComponent) value);
        } else if (name.equals("manufacturer[x]")) {
          this.manufacturer = castToType(value); // Type
        } else if (name.equals("deviceName")) {
          this.getDeviceName().add((DeviceDefinitionDeviceNameComponent) value);
        } else if (name.equals("modelNumber")) {
          this.modelNumber = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("specialization")) {
          this.getSpecialization().add((DeviceDefinitionSpecializationComponent) value);
        } else if (name.equals("version")) {
          this.getVersion().add(castToString(value));
        } else if (name.equals("safety")) {
          this.getSafety().add(castToCodeableConcept(value));
        } else if (name.equals("shelfLifeStorage")) {
          this.getShelfLifeStorage().add(castToProductShelfLife(value));
        } else if (name.equals("physicalCharacteristics")) {
          this.physicalCharacteristics = castToProdCharacteristic(value); // ProdCharacteristic
        } else if (name.equals("languageCode")) {
          this.getLanguageCode().add(castToCodeableConcept(value));
        } else if (name.equals("capability")) {
          this.getCapability().add((DeviceDefinitionCapabilityComponent) value);
        } else if (name.equals("property")) {
          this.getProperty().add((DeviceDefinitionPropertyComponent) value);
        } else if (name.equals("owner")) {
          this.owner = castToReference(value); // Reference
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactPoint(value));
        } else if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("onlineInformation")) {
          this.onlineInformation = castToUri(value); // UriType
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("parentDevice")) {
          this.parentDevice = castToReference(value); // Reference
        } else if (name.equals("material")) {
          this.getMaterial().add((DeviceDefinitionMaterialComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -99121287:  return addUdiDeviceIdentifier(); 
        case 418079503:  return getManufacturer(); 
        case -1969347631:  return getManufacturer(); 
        case 780988929:  return addDeviceName(); 
        case 346619858:  return getModelNumberElement();
        case 3575610:  return getType(); 
        case 682815883:  return addSpecialization(); 
        case 351608024:  return addVersionElement();
        case -909893934:  return addSafety(); 
        case 172049237:  return addShelfLifeStorage(); 
        case -1599676319:  return getPhysicalCharacteristics(); 
        case -2092349083:  return addLanguageCode(); 
        case -783669992:  return addCapability(); 
        case -993141291:  return addProperty(); 
        case 106164915:  return getOwner(); 
        case 951526432:  return addContact(); 
        case 116079:  return getUrlElement();
        case -788511527:  return getOnlineInformationElement();
        case 3387378:  return addNote(); 
        case -1285004149:  return getQuantity(); 
        case 620260256:  return getParentDevice(); 
        case 299066663:  return addMaterial(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -99121287: /*udiDeviceIdentifier*/ return new String[] {};
        case -1969347631: /*manufacturer*/ return new String[] {"string", "Reference"};
        case 780988929: /*deviceName*/ return new String[] {};
        case 346619858: /*modelNumber*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 682815883: /*specialization*/ return new String[] {};
        case 351608024: /*version*/ return new String[] {"string"};
        case -909893934: /*safety*/ return new String[] {"CodeableConcept"};
        case 172049237: /*shelfLifeStorage*/ return new String[] {"ProductShelfLife"};
        case -1599676319: /*physicalCharacteristics*/ return new String[] {"ProdCharacteristic"};
        case -2092349083: /*languageCode*/ return new String[] {"CodeableConcept"};
        case -783669992: /*capability*/ return new String[] {};
        case -993141291: /*property*/ return new String[] {};
        case 106164915: /*owner*/ return new String[] {"Reference"};
        case 951526432: /*contact*/ return new String[] {"ContactPoint"};
        case 116079: /*url*/ return new String[] {"uri"};
        case -788511527: /*onlineInformation*/ return new String[] {"uri"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case -1285004149: /*quantity*/ return new String[] {"Quantity"};
        case 620260256: /*parentDevice*/ return new String[] {"Reference"};
        case 299066663: /*material*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("udiDeviceIdentifier")) {
          return addUdiDeviceIdentifier();
        }
        else if (name.equals("manufacturerString")) {
          this.manufacturer = new StringType();
          return this.manufacturer;
        }
        else if (name.equals("manufacturerReference")) {
          this.manufacturer = new Reference();
          return this.manufacturer;
        }
        else if (name.equals("deviceName")) {
          return addDeviceName();
        }
        else if (name.equals("modelNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.modelNumber");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("specialization")) {
          return addSpecialization();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.version");
        }
        else if (name.equals("safety")) {
          return addSafety();
        }
        else if (name.equals("shelfLifeStorage")) {
          return addShelfLifeStorage();
        }
        else if (name.equals("physicalCharacteristics")) {
          this.physicalCharacteristics = new ProdCharacteristic();
          return this.physicalCharacteristics;
        }
        else if (name.equals("languageCode")) {
          return addLanguageCode();
        }
        else if (name.equals("capability")) {
          return addCapability();
        }
        else if (name.equals("property")) {
          return addProperty();
        }
        else if (name.equals("owner")) {
          this.owner = new Reference();
          return this.owner;
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.url");
        }
        else if (name.equals("onlineInformation")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceDefinition.onlineInformation");
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("parentDevice")) {
          this.parentDevice = new Reference();
          return this.parentDevice;
        }
        else if (name.equals("material")) {
          return addMaterial();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DeviceDefinition";

  }

      public DeviceDefinition copy() {
        DeviceDefinition dst = new DeviceDefinition();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (udiDeviceIdentifier != null) {
          dst.udiDeviceIdentifier = new ArrayList<DeviceDefinitionUdiDeviceIdentifierComponent>();
          for (DeviceDefinitionUdiDeviceIdentifierComponent i : udiDeviceIdentifier)
            dst.udiDeviceIdentifier.add(i.copy());
        };
        dst.manufacturer = manufacturer == null ? null : manufacturer.copy();
        if (deviceName != null) {
          dst.deviceName = new ArrayList<DeviceDefinitionDeviceNameComponent>();
          for (DeviceDefinitionDeviceNameComponent i : deviceName)
            dst.deviceName.add(i.copy());
        };
        dst.modelNumber = modelNumber == null ? null : modelNumber.copy();
        dst.type = type == null ? null : type.copy();
        if (specialization != null) {
          dst.specialization = new ArrayList<DeviceDefinitionSpecializationComponent>();
          for (DeviceDefinitionSpecializationComponent i : specialization)
            dst.specialization.add(i.copy());
        };
        if (version != null) {
          dst.version = new ArrayList<StringType>();
          for (StringType i : version)
            dst.version.add(i.copy());
        };
        if (safety != null) {
          dst.safety = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : safety)
            dst.safety.add(i.copy());
        };
        if (shelfLifeStorage != null) {
          dst.shelfLifeStorage = new ArrayList<ProductShelfLife>();
          for (ProductShelfLife i : shelfLifeStorage)
            dst.shelfLifeStorage.add(i.copy());
        };
        dst.physicalCharacteristics = physicalCharacteristics == null ? null : physicalCharacteristics.copy();
        if (languageCode != null) {
          dst.languageCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : languageCode)
            dst.languageCode.add(i.copy());
        };
        if (capability != null) {
          dst.capability = new ArrayList<DeviceDefinitionCapabilityComponent>();
          for (DeviceDefinitionCapabilityComponent i : capability)
            dst.capability.add(i.copy());
        };
        if (property != null) {
          dst.property = new ArrayList<DeviceDefinitionPropertyComponent>();
          for (DeviceDefinitionPropertyComponent i : property)
            dst.property.add(i.copy());
        };
        dst.owner = owner == null ? null : owner.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactPoint>();
          for (ContactPoint i : contact)
            dst.contact.add(i.copy());
        };
        dst.url = url == null ? null : url.copy();
        dst.onlineInformation = onlineInformation == null ? null : onlineInformation.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.parentDevice = parentDevice == null ? null : parentDevice.copy();
        if (material != null) {
          dst.material = new ArrayList<DeviceDefinitionMaterialComponent>();
          for (DeviceDefinitionMaterialComponent i : material)
            dst.material.add(i.copy());
        };
        return dst;
      }

      protected DeviceDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DeviceDefinition))
          return false;
        DeviceDefinition o = (DeviceDefinition) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(udiDeviceIdentifier, o.udiDeviceIdentifier, true)
           && compareDeep(manufacturer, o.manufacturer, true) && compareDeep(deviceName, o.deviceName, true)
           && compareDeep(modelNumber, o.modelNumber, true) && compareDeep(type, o.type, true) && compareDeep(specialization, o.specialization, true)
           && compareDeep(version, o.version, true) && compareDeep(safety, o.safety, true) && compareDeep(shelfLifeStorage, o.shelfLifeStorage, true)
           && compareDeep(physicalCharacteristics, o.physicalCharacteristics, true) && compareDeep(languageCode, o.languageCode, true)
           && compareDeep(capability, o.capability, true) && compareDeep(property, o.property, true) && compareDeep(owner, o.owner, true)
           && compareDeep(contact, o.contact, true) && compareDeep(url, o.url, true) && compareDeep(onlineInformation, o.onlineInformation, true)
           && compareDeep(note, o.note, true) && compareDeep(quantity, o.quantity, true) && compareDeep(parentDevice, o.parentDevice, true)
           && compareDeep(material, o.material, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DeviceDefinition))
          return false;
        DeviceDefinition o = (DeviceDefinition) other_;
        return compareValues(modelNumber, o.modelNumber, true) && compareValues(version, o.version, true) && compareValues(url, o.url, true)
           && compareValues(onlineInformation, o.onlineInformation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, udiDeviceIdentifier
          , manufacturer, deviceName, modelNumber, type, specialization, version, safety
          , shelfLifeStorage, physicalCharacteristics, languageCode, capability, property, owner
          , contact, url, onlineInformation, note, quantity, parentDevice, material);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DeviceDefinition;
   }

 /**
   * Search parameter: <b>parent</b>
   * <p>
   * Description: <b>The parent DeviceDefinition resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceDefinition.parentDevice</b><br>
   * </p>
   */
  @SearchParamDefinition(name="parent", path="DeviceDefinition.parentDevice", description="The parent DeviceDefinition resource", type="reference", target={DeviceDefinition.class } )
  public static final String SP_PARENT = "parent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>parent</b>
   * <p>
   * Description: <b>The parent DeviceDefinition resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceDefinition.parentDevice</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceDefinition:parent</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARENT = new ca.uhn.fhir.model.api.Include("DeviceDefinition:parent").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the component</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceDefinition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DeviceDefinition.identifier", description="The identifier of the component", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the component</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceDefinition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The device component type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceDefinition.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="DeviceDefinition.type", description="The device component type", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The device component type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceDefinition.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);


}

