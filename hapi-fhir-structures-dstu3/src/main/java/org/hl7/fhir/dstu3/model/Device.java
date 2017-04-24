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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * This resource identifies an instance or a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices include durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc.
 */
@ResourceDef(name="Device", profile="http://hl7.org/fhir/Profile/Device")
public class Device extends DomainResource {

    public enum UDIEntryType {
        /**
         * A Barcode scanner captured the data from the device label
         */
        BARCODE, 
        /**
         * An RFID chip reader captured the data from the device label
         */
        RFID, 
        /**
         * The data was read from the label by a person and manually entered. (e.g.  via a keyboard)
         */
        MANUAL, 
        /**
         * The data originated from a patient's implant card and read by an operator.
         */
        CARD, 
        /**
         * The data originated from a patient source and not directly scanned or read from a label or card.
         */
        SELFREPORTED, 
        /**
         * The method of data capture has not been determined
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static UDIEntryType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("barcode".equals(codeString))
          return BARCODE;
        if ("rfid".equals(codeString))
          return RFID;
        if ("manual".equals(codeString))
          return MANUAL;
        if ("card".equals(codeString))
          return CARD;
        if ("self-reported".equals(codeString))
          return SELFREPORTED;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown UDIEntryType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BARCODE: return "barcode";
            case RFID: return "rfid";
            case MANUAL: return "manual";
            case CARD: return "card";
            case SELFREPORTED: return "self-reported";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case BARCODE: return "http://hl7.org/fhir/udi-entry-type";
            case RFID: return "http://hl7.org/fhir/udi-entry-type";
            case MANUAL: return "http://hl7.org/fhir/udi-entry-type";
            case CARD: return "http://hl7.org/fhir/udi-entry-type";
            case SELFREPORTED: return "http://hl7.org/fhir/udi-entry-type";
            case UNKNOWN: return "http://hl7.org/fhir/udi-entry-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case BARCODE: return "A Barcode scanner captured the data from the device label";
            case RFID: return "An RFID chip reader captured the data from the device label";
            case MANUAL: return "The data was read from the label by a person and manually entered. (e.g.  via a keyboard)";
            case CARD: return "The data originated from a patient's implant card and read by an operator.";
            case SELFREPORTED: return "The data originated from a patient source and not directly scanned or read from a label or card.";
            case UNKNOWN: return "The method of data capture has not been determined";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BARCODE: return "BarCode";
            case RFID: return "RFID";
            case MANUAL: return "Manual";
            case CARD: return "Card";
            case SELFREPORTED: return "Self Reported";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class UDIEntryTypeEnumFactory implements EnumFactory<UDIEntryType> {
    public UDIEntryType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("barcode".equals(codeString))
          return UDIEntryType.BARCODE;
        if ("rfid".equals(codeString))
          return UDIEntryType.RFID;
        if ("manual".equals(codeString))
          return UDIEntryType.MANUAL;
        if ("card".equals(codeString))
          return UDIEntryType.CARD;
        if ("self-reported".equals(codeString))
          return UDIEntryType.SELFREPORTED;
        if ("unknown".equals(codeString))
          return UDIEntryType.UNKNOWN;
        throw new IllegalArgumentException("Unknown UDIEntryType code '"+codeString+"'");
        }
        public Enumeration<UDIEntryType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<UDIEntryType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("barcode".equals(codeString))
          return new Enumeration<UDIEntryType>(this, UDIEntryType.BARCODE);
        if ("rfid".equals(codeString))
          return new Enumeration<UDIEntryType>(this, UDIEntryType.RFID);
        if ("manual".equals(codeString))
          return new Enumeration<UDIEntryType>(this, UDIEntryType.MANUAL);
        if ("card".equals(codeString))
          return new Enumeration<UDIEntryType>(this, UDIEntryType.CARD);
        if ("self-reported".equals(codeString))
          return new Enumeration<UDIEntryType>(this, UDIEntryType.SELFREPORTED);
        if ("unknown".equals(codeString))
          return new Enumeration<UDIEntryType>(this, UDIEntryType.UNKNOWN);
        throw new FHIRException("Unknown UDIEntryType code '"+codeString+"'");
        }
    public String toCode(UDIEntryType code) {
      if (code == UDIEntryType.BARCODE)
        return "barcode";
      if (code == UDIEntryType.RFID)
        return "rfid";
      if (code == UDIEntryType.MANUAL)
        return "manual";
      if (code == UDIEntryType.CARD)
        return "card";
      if (code == UDIEntryType.SELFREPORTED)
        return "self-reported";
      if (code == UDIEntryType.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(UDIEntryType code) {
      return code.getSystem();
      }
    }

    public enum FHIRDeviceStatus {
        /**
         * The Device is available for use.  Note: This means for *implanted devices*  the device is implanted in the patient.
         */
        ACTIVE, 
        /**
         * The Device is no longer available for use (e.g. lost, expired, damaged).  Note: This means for *implanted devices*  the device has been removed from the patient.
         */
        INACTIVE, 
        /**
         * The Device was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * The status of the device has not been determined.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static FHIRDeviceStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown FHIRDeviceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/device-status";
            case INACTIVE: return "http://hl7.org/fhir/device-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/device-status";
            case UNKNOWN: return "http://hl7.org/fhir/device-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The Device is available for use.  Note: This means for *implanted devices*  the device is implanted in the patient.";
            case INACTIVE: return "The Device is no longer available for use (e.g. lost, expired, damaged).  Note: This means for *implanted devices*  the device has been removed from the patient.";
            case ENTEREDINERROR: return "The Device was entered in error and voided.";
            case UNKNOWN: return "The status of the device has not been determined.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class FHIRDeviceStatusEnumFactory implements EnumFactory<FHIRDeviceStatus> {
    public FHIRDeviceStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return FHIRDeviceStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return FHIRDeviceStatus.INACTIVE;
        if ("entered-in-error".equals(codeString))
          return FHIRDeviceStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return FHIRDeviceStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown FHIRDeviceStatus code '"+codeString+"'");
        }
        public Enumeration<FHIRDeviceStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<FHIRDeviceStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<FHIRDeviceStatus>(this, FHIRDeviceStatus.ACTIVE);
        if ("inactive".equals(codeString))
          return new Enumeration<FHIRDeviceStatus>(this, FHIRDeviceStatus.INACTIVE);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<FHIRDeviceStatus>(this, FHIRDeviceStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<FHIRDeviceStatus>(this, FHIRDeviceStatus.UNKNOWN);
        throw new FHIRException("Unknown FHIRDeviceStatus code '"+codeString+"'");
        }
    public String toCode(FHIRDeviceStatus code) {
      if (code == FHIRDeviceStatus.ACTIVE)
        return "active";
      if (code == FHIRDeviceStatus.INACTIVE)
        return "inactive";
      if (code == FHIRDeviceStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == FHIRDeviceStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(FHIRDeviceStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class DeviceUdiComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version or model of a device.
         */
        @Child(name = "deviceIdentifier", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Mandatory fixed portion of UDI", formalDefinition="The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version or model of a device." )
        protected StringType deviceIdentifier;

        /**
         * Name of device as used in labeling or catalog.
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Device Name as appears on UDI label", formalDefinition="Name of device as used in labeling or catalog." )
        protected StringType name;

        /**
         * The identity of the authoritative source for UDI generation within a  jurisdiction.  All UDIs are globally unique within a single namespace. with the appropriate repository uri as the system.  For example,  UDIs of devices managed in the U.S. by the FDA, the value is  http://hl7.org/fhir/NamingSystem/fda-udi.
         */
        @Child(name = "jurisdiction", type = {UriType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Regional UDI authority", formalDefinition="The identity of the authoritative source for UDI generation within a  jurisdiction.  All UDIs are globally unique within a single namespace. with the appropriate repository uri as the system.  For example,  UDIs of devices managed in the U.S. by the FDA, the value is  http://hl7.org/fhir/NamingSystem/fda-udi." )
        protected UriType jurisdiction;

        /**
         * The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging of the device.
         */
        @Child(name = "carrierHRF", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="UDI Human Readable Barcode String", formalDefinition="The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging of the device." )
        protected StringType carrierHRF;

        /**
         * The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode string as printed on the packaging of the device - E.g a barcode or RFID.   Because of limitations on character sets in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded.
         */
        @Child(name = "carrierAIDC", type = {Base64BinaryType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="UDI Machine Readable Barcode String", formalDefinition="The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode string as printed on the packaging of the device - E.g a barcode or RFID.   Because of limitations on character sets in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded." )
        protected Base64BinaryType carrierAIDC;

        /**
         * Organization that is charged with issuing UDIs for devices.  For example, the US FDA issuers include :
1) GS1: 
http://hl7.org/fhir/NamingSystem/gs1-di, 
2) HIBCC:
http://hl7.org/fhir/NamingSystem/hibcc-dI, 
3) ICCBBA for blood containers:
http://hl7.org/fhir/NamingSystem/iccbba-blood-di, 
4) ICCBA for other devices:
http://hl7.org/fhir/NamingSystem/iccbba-other-di.
         */
        @Child(name = "issuer", type = {UriType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="UDI Issuing Organization", formalDefinition="Organization that is charged with issuing UDIs for devices.  For example, the US FDA issuers include :\n1) GS1: \nhttp://hl7.org/fhir/NamingSystem/gs1-di, \n2) HIBCC:\nhttp://hl7.org/fhir/NamingSystem/hibcc-dI, \n3) ICCBBA for blood containers:\nhttp://hl7.org/fhir/NamingSystem/iccbba-blood-di, \n4) ICCBA for other devices:\nhttp://hl7.org/fhir/NamingSystem/iccbba-other-di." )
        protected UriType issuer;

        /**
         * A coded entry to indicate how the data was entered.
         */
        @Child(name = "entryType", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="barcode | rfid | manual +", formalDefinition="A coded entry to indicate how the data was entered." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/udi-entry-type")
        protected Enumeration<UDIEntryType> entryType;

        private static final long serialVersionUID = -1105798343L;

    /**
     * Constructor
     */
      public DeviceUdiComponent() {
        super();
      }

        /**
         * @return {@link #deviceIdentifier} (The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version or model of a device.). This is the underlying object with id, value and extensions. The accessor "getDeviceIdentifier" gives direct access to the value
         */
        public StringType getDeviceIdentifierElement() { 
          if (this.deviceIdentifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceUdiComponent.deviceIdentifier");
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
         * @param value {@link #deviceIdentifier} (The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version or model of a device.). This is the underlying object with id, value and extensions. The accessor "getDeviceIdentifier" gives direct access to the value
         */
        public DeviceUdiComponent setDeviceIdentifierElement(StringType value) { 
          this.deviceIdentifier = value;
          return this;
        }

        /**
         * @return The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version or model of a device.
         */
        public String getDeviceIdentifier() { 
          return this.deviceIdentifier == null ? null : this.deviceIdentifier.getValue();
        }

        /**
         * @param value The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version or model of a device.
         */
        public DeviceUdiComponent setDeviceIdentifier(String value) { 
          if (Utilities.noString(value))
            this.deviceIdentifier = null;
          else {
            if (this.deviceIdentifier == null)
              this.deviceIdentifier = new StringType();
            this.deviceIdentifier.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #name} (Name of device as used in labeling or catalog.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceUdiComponent.name");
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
         * @param value {@link #name} (Name of device as used in labeling or catalog.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public DeviceUdiComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Name of device as used in labeling or catalog.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Name of device as used in labeling or catalog.
         */
        public DeviceUdiComponent setName(String value) { 
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
         * @return {@link #jurisdiction} (The identity of the authoritative source for UDI generation within a  jurisdiction.  All UDIs are globally unique within a single namespace. with the appropriate repository uri as the system.  For example,  UDIs of devices managed in the U.S. by the FDA, the value is  http://hl7.org/fhir/NamingSystem/fda-udi.). This is the underlying object with id, value and extensions. The accessor "getJurisdiction" gives direct access to the value
         */
        public UriType getJurisdictionElement() { 
          if (this.jurisdiction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceUdiComponent.jurisdiction");
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
         * @param value {@link #jurisdiction} (The identity of the authoritative source for UDI generation within a  jurisdiction.  All UDIs are globally unique within a single namespace. with the appropriate repository uri as the system.  For example,  UDIs of devices managed in the U.S. by the FDA, the value is  http://hl7.org/fhir/NamingSystem/fda-udi.). This is the underlying object with id, value and extensions. The accessor "getJurisdiction" gives direct access to the value
         */
        public DeviceUdiComponent setJurisdictionElement(UriType value) { 
          this.jurisdiction = value;
          return this;
        }

        /**
         * @return The identity of the authoritative source for UDI generation within a  jurisdiction.  All UDIs are globally unique within a single namespace. with the appropriate repository uri as the system.  For example,  UDIs of devices managed in the U.S. by the FDA, the value is  http://hl7.org/fhir/NamingSystem/fda-udi.
         */
        public String getJurisdiction() { 
          return this.jurisdiction == null ? null : this.jurisdiction.getValue();
        }

        /**
         * @param value The identity of the authoritative source for UDI generation within a  jurisdiction.  All UDIs are globally unique within a single namespace. with the appropriate repository uri as the system.  For example,  UDIs of devices managed in the U.S. by the FDA, the value is  http://hl7.org/fhir/NamingSystem/fda-udi.
         */
        public DeviceUdiComponent setJurisdiction(String value) { 
          if (Utilities.noString(value))
            this.jurisdiction = null;
          else {
            if (this.jurisdiction == null)
              this.jurisdiction = new UriType();
            this.jurisdiction.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #carrierHRF} (The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging of the device.). This is the underlying object with id, value and extensions. The accessor "getCarrierHRF" gives direct access to the value
         */
        public StringType getCarrierHRFElement() { 
          if (this.carrierHRF == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceUdiComponent.carrierHRF");
            else if (Configuration.doAutoCreate())
              this.carrierHRF = new StringType(); // bb
          return this.carrierHRF;
        }

        public boolean hasCarrierHRFElement() { 
          return this.carrierHRF != null && !this.carrierHRF.isEmpty();
        }

        public boolean hasCarrierHRF() { 
          return this.carrierHRF != null && !this.carrierHRF.isEmpty();
        }

        /**
         * @param value {@link #carrierHRF} (The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging of the device.). This is the underlying object with id, value and extensions. The accessor "getCarrierHRF" gives direct access to the value
         */
        public DeviceUdiComponent setCarrierHRFElement(StringType value) { 
          this.carrierHRF = value;
          return this;
        }

        /**
         * @return The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging of the device.
         */
        public String getCarrierHRF() { 
          return this.carrierHRF == null ? null : this.carrierHRF.getValue();
        }

        /**
         * @param value The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging of the device.
         */
        public DeviceUdiComponent setCarrierHRF(String value) { 
          if (Utilities.noString(value))
            this.carrierHRF = null;
          else {
            if (this.carrierHRF == null)
              this.carrierHRF = new StringType();
            this.carrierHRF.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #carrierAIDC} (The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode string as printed on the packaging of the device - E.g a barcode or RFID.   Because of limitations on character sets in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded.). This is the underlying object with id, value and extensions. The accessor "getCarrierAIDC" gives direct access to the value
         */
        public Base64BinaryType getCarrierAIDCElement() { 
          if (this.carrierAIDC == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceUdiComponent.carrierAIDC");
            else if (Configuration.doAutoCreate())
              this.carrierAIDC = new Base64BinaryType(); // bb
          return this.carrierAIDC;
        }

        public boolean hasCarrierAIDCElement() { 
          return this.carrierAIDC != null && !this.carrierAIDC.isEmpty();
        }

        public boolean hasCarrierAIDC() { 
          return this.carrierAIDC != null && !this.carrierAIDC.isEmpty();
        }

        /**
         * @param value {@link #carrierAIDC} (The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode string as printed on the packaging of the device - E.g a barcode or RFID.   Because of limitations on character sets in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded.). This is the underlying object with id, value and extensions. The accessor "getCarrierAIDC" gives direct access to the value
         */
        public DeviceUdiComponent setCarrierAIDCElement(Base64BinaryType value) { 
          this.carrierAIDC = value;
          return this;
        }

        /**
         * @return The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode string as printed on the packaging of the device - E.g a barcode or RFID.   Because of limitations on character sets in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded.
         */
        public byte[] getCarrierAIDC() { 
          return this.carrierAIDC == null ? null : this.carrierAIDC.getValue();
        }

        /**
         * @param value The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode string as printed on the packaging of the device - E.g a barcode or RFID.   Because of limitations on character sets in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded.
         */
        public DeviceUdiComponent setCarrierAIDC(byte[] value) { 
          if (value == null)
            this.carrierAIDC = null;
          else {
            if (this.carrierAIDC == null)
              this.carrierAIDC = new Base64BinaryType();
            this.carrierAIDC.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #issuer} (Organization that is charged with issuing UDIs for devices.  For example, the US FDA issuers include :
1) GS1: 
http://hl7.org/fhir/NamingSystem/gs1-di, 
2) HIBCC:
http://hl7.org/fhir/NamingSystem/hibcc-dI, 
3) ICCBBA for blood containers:
http://hl7.org/fhir/NamingSystem/iccbba-blood-di, 
4) ICCBA for other devices:
http://hl7.org/fhir/NamingSystem/iccbba-other-di.). This is the underlying object with id, value and extensions. The accessor "getIssuer" gives direct access to the value
         */
        public UriType getIssuerElement() { 
          if (this.issuer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceUdiComponent.issuer");
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
         * @param value {@link #issuer} (Organization that is charged with issuing UDIs for devices.  For example, the US FDA issuers include :
1) GS1: 
http://hl7.org/fhir/NamingSystem/gs1-di, 
2) HIBCC:
http://hl7.org/fhir/NamingSystem/hibcc-dI, 
3) ICCBBA for blood containers:
http://hl7.org/fhir/NamingSystem/iccbba-blood-di, 
4) ICCBA for other devices:
http://hl7.org/fhir/NamingSystem/iccbba-other-di.). This is the underlying object with id, value and extensions. The accessor "getIssuer" gives direct access to the value
         */
        public DeviceUdiComponent setIssuerElement(UriType value) { 
          this.issuer = value;
          return this;
        }

        /**
         * @return Organization that is charged with issuing UDIs for devices.  For example, the US FDA issuers include :
1) GS1: 
http://hl7.org/fhir/NamingSystem/gs1-di, 
2) HIBCC:
http://hl7.org/fhir/NamingSystem/hibcc-dI, 
3) ICCBBA for blood containers:
http://hl7.org/fhir/NamingSystem/iccbba-blood-di, 
4) ICCBA for other devices:
http://hl7.org/fhir/NamingSystem/iccbba-other-di.
         */
        public String getIssuer() { 
          return this.issuer == null ? null : this.issuer.getValue();
        }

        /**
         * @param value Organization that is charged with issuing UDIs for devices.  For example, the US FDA issuers include :
1) GS1: 
http://hl7.org/fhir/NamingSystem/gs1-di, 
2) HIBCC:
http://hl7.org/fhir/NamingSystem/hibcc-dI, 
3) ICCBBA for blood containers:
http://hl7.org/fhir/NamingSystem/iccbba-blood-di, 
4) ICCBA for other devices:
http://hl7.org/fhir/NamingSystem/iccbba-other-di.
         */
        public DeviceUdiComponent setIssuer(String value) { 
          if (Utilities.noString(value))
            this.issuer = null;
          else {
            if (this.issuer == null)
              this.issuer = new UriType();
            this.issuer.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #entryType} (A coded entry to indicate how the data was entered.). This is the underlying object with id, value and extensions. The accessor "getEntryType" gives direct access to the value
         */
        public Enumeration<UDIEntryType> getEntryTypeElement() { 
          if (this.entryType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceUdiComponent.entryType");
            else if (Configuration.doAutoCreate())
              this.entryType = new Enumeration<UDIEntryType>(new UDIEntryTypeEnumFactory()); // bb
          return this.entryType;
        }

        public boolean hasEntryTypeElement() { 
          return this.entryType != null && !this.entryType.isEmpty();
        }

        public boolean hasEntryType() { 
          return this.entryType != null && !this.entryType.isEmpty();
        }

        /**
         * @param value {@link #entryType} (A coded entry to indicate how the data was entered.). This is the underlying object with id, value and extensions. The accessor "getEntryType" gives direct access to the value
         */
        public DeviceUdiComponent setEntryTypeElement(Enumeration<UDIEntryType> value) { 
          this.entryType = value;
          return this;
        }

        /**
         * @return A coded entry to indicate how the data was entered.
         */
        public UDIEntryType getEntryType() { 
          return this.entryType == null ? null : this.entryType.getValue();
        }

        /**
         * @param value A coded entry to indicate how the data was entered.
         */
        public DeviceUdiComponent setEntryType(UDIEntryType value) { 
          if (value == null)
            this.entryType = null;
          else {
            if (this.entryType == null)
              this.entryType = new Enumeration<UDIEntryType>(new UDIEntryTypeEnumFactory());
            this.entryType.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("deviceIdentifier", "string", "The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version or model of a device.", 0, java.lang.Integer.MAX_VALUE, deviceIdentifier));
          childrenList.add(new Property("name", "string", "Name of device as used in labeling or catalog.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("jurisdiction", "uri", "The identity of the authoritative source for UDI generation within a  jurisdiction.  All UDIs are globally unique within a single namespace. with the appropriate repository uri as the system.  For example,  UDIs of devices managed in the U.S. by the FDA, the value is  http://hl7.org/fhir/NamingSystem/fda-udi.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
          childrenList.add(new Property("carrierHRF", "string", "The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging of the device.", 0, java.lang.Integer.MAX_VALUE, carrierHRF));
          childrenList.add(new Property("carrierAIDC", "base64Binary", "The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode string as printed on the packaging of the device - E.g a barcode or RFID.   Because of limitations on character sets in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded.", 0, java.lang.Integer.MAX_VALUE, carrierAIDC));
          childrenList.add(new Property("issuer", "uri", "Organization that is charged with issuing UDIs for devices.  For example, the US FDA issuers include :\n1) GS1: \nhttp://hl7.org/fhir/NamingSystem/gs1-di, \n2) HIBCC:\nhttp://hl7.org/fhir/NamingSystem/hibcc-dI, \n3) ICCBBA for blood containers:\nhttp://hl7.org/fhir/NamingSystem/iccbba-blood-di, \n4) ICCBA for other devices:\nhttp://hl7.org/fhir/NamingSystem/iccbba-other-di.", 0, java.lang.Integer.MAX_VALUE, issuer));
          childrenList.add(new Property("entryType", "code", "A coded entry to indicate how the data was entered.", 0, java.lang.Integer.MAX_VALUE, entryType));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1322005407: /*deviceIdentifier*/ return this.deviceIdentifier == null ? new Base[0] : new Base[] {this.deviceIdentifier}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : new Base[] {this.jurisdiction}; // UriType
        case 806499972: /*carrierHRF*/ return this.carrierHRF == null ? new Base[0] : new Base[] {this.carrierHRF}; // StringType
        case -768521825: /*carrierAIDC*/ return this.carrierAIDC == null ? new Base[0] : new Base[] {this.carrierAIDC}; // Base64BinaryType
        case -1179159879: /*issuer*/ return this.issuer == null ? new Base[0] : new Base[] {this.issuer}; // UriType
        case -479362356: /*entryType*/ return this.entryType == null ? new Base[0] : new Base[] {this.entryType}; // Enumeration<UDIEntryType>
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1322005407: // deviceIdentifier
          this.deviceIdentifier = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -507075711: // jurisdiction
          this.jurisdiction = castToUri(value); // UriType
          return value;
        case 806499972: // carrierHRF
          this.carrierHRF = castToString(value); // StringType
          return value;
        case -768521825: // carrierAIDC
          this.carrierAIDC = castToBase64Binary(value); // Base64BinaryType
          return value;
        case -1179159879: // issuer
          this.issuer = castToUri(value); // UriType
          return value;
        case -479362356: // entryType
          value = new UDIEntryTypeEnumFactory().fromType(castToCode(value));
          this.entryType = (Enumeration) value; // Enumeration<UDIEntryType>
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("deviceIdentifier")) {
          this.deviceIdentifier = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("jurisdiction")) {
          this.jurisdiction = castToUri(value); // UriType
        } else if (name.equals("carrierHRF")) {
          this.carrierHRF = castToString(value); // StringType
        } else if (name.equals("carrierAIDC")) {
          this.carrierAIDC = castToBase64Binary(value); // Base64BinaryType
        } else if (name.equals("issuer")) {
          this.issuer = castToUri(value); // UriType
        } else if (name.equals("entryType")) {
          value = new UDIEntryTypeEnumFactory().fromType(castToCode(value));
          this.entryType = (Enumeration) value; // Enumeration<UDIEntryType>
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1322005407:  return getDeviceIdentifierElement();
        case 3373707:  return getNameElement();
        case -507075711:  return getJurisdictionElement();
        case 806499972:  return getCarrierHRFElement();
        case -768521825:  return getCarrierAIDCElement();
        case -1179159879:  return getIssuerElement();
        case -479362356:  return getEntryTypeElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1322005407: /*deviceIdentifier*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -507075711: /*jurisdiction*/ return new String[] {"uri"};
        case 806499972: /*carrierHRF*/ return new String[] {"string"};
        case -768521825: /*carrierAIDC*/ return new String[] {"base64Binary"};
        case -1179159879: /*issuer*/ return new String[] {"uri"};
        case -479362356: /*entryType*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("deviceIdentifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.deviceIdentifier");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.name");
        }
        else if (name.equals("jurisdiction")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.jurisdiction");
        }
        else if (name.equals("carrierHRF")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.carrierHRF");
        }
        else if (name.equals("carrierAIDC")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.carrierAIDC");
        }
        else if (name.equals("issuer")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.issuer");
        }
        else if (name.equals("entryType")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.entryType");
        }
        else
          return super.addChild(name);
      }

      public DeviceUdiComponent copy() {
        DeviceUdiComponent dst = new DeviceUdiComponent();
        copyValues(dst);
        dst.deviceIdentifier = deviceIdentifier == null ? null : deviceIdentifier.copy();
        dst.name = name == null ? null : name.copy();
        dst.jurisdiction = jurisdiction == null ? null : jurisdiction.copy();
        dst.carrierHRF = carrierHRF == null ? null : carrierHRF.copy();
        dst.carrierAIDC = carrierAIDC == null ? null : carrierAIDC.copy();
        dst.issuer = issuer == null ? null : issuer.copy();
        dst.entryType = entryType == null ? null : entryType.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DeviceUdiComponent))
          return false;
        DeviceUdiComponent o = (DeviceUdiComponent) other;
        return compareDeep(deviceIdentifier, o.deviceIdentifier, true) && compareDeep(name, o.name, true)
           && compareDeep(jurisdiction, o.jurisdiction, true) && compareDeep(carrierHRF, o.carrierHRF, true)
           && compareDeep(carrierAIDC, o.carrierAIDC, true) && compareDeep(issuer, o.issuer, true) && compareDeep(entryType, o.entryType, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DeviceUdiComponent))
          return false;
        DeviceUdiComponent o = (DeviceUdiComponent) other;
        return compareValues(deviceIdentifier, o.deviceIdentifier, true) && compareValues(name, o.name, true)
           && compareValues(jurisdiction, o.jurisdiction, true) && compareValues(carrierHRF, o.carrierHRF, true)
           && compareValues(carrierAIDC, o.carrierAIDC, true) && compareValues(issuer, o.issuer, true) && compareValues(entryType, o.entryType, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(deviceIdentifier, name, jurisdiction
          , carrierHRF, carrierAIDC, issuer, entryType);
      }

  public String fhirType() {
    return "Device.udi";

  }

  }

    /**
     * Unique instance identifiers assigned to a device by manufacturers other organizations or owners.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Instance identifier", formalDefinition="Unique instance identifiers assigned to a device by manufacturers other organizations or owners." )
    protected List<Identifier> identifier;

    /**
     * [Unique device identifier (UDI)](device.html#5.11.3.2.2) assigned to device label or package.
     */
    @Child(name = "udi", type = {}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique Device Identifier (UDI) Barcode string", formalDefinition="[Unique device identifier (UDI)](device.html#5.11.3.2.2) assigned to device label or package." )
    protected DeviceUdiComponent udi;

    /**
     * Status of the Device availability.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | inactive | entered-in-error | unknown", formalDefinition="Status of the Device availability." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-status")
    protected Enumeration<FHIRDeviceStatus> status;

    /**
     * Code or identifier to identify a kind of device.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What kind of device this is", formalDefinition="Code or identifier to identify a kind of device." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-kind")
    protected CodeableConcept type;

    /**
     * Lot number assigned by the manufacturer.
     */
    @Child(name = "lotNumber", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Lot number of manufacture", formalDefinition="Lot number assigned by the manufacturer." )
    protected StringType lotNumber;

    /**
     * A name of the manufacturer.
     */
    @Child(name = "manufacturer", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Name of device manufacturer", formalDefinition="A name of the manufacturer." )
    protected StringType manufacturer;

    /**
     * The date and time when the device was manufactured.
     */
    @Child(name = "manufactureDate", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Date when the device was made", formalDefinition="The date and time when the device was manufactured." )
    protected DateTimeType manufactureDate;

    /**
     * The date and time beyond which this device is no longer valid or should not be used (if applicable).
     */
    @Child(name = "expirationDate", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Date and time of expiry of this device (if applicable)", formalDefinition="The date and time beyond which this device is no longer valid or should not be used (if applicable)." )
    protected DateTimeType expirationDate;

    /**
     * The "model" is an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.
     */
    @Child(name = "model", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Model id assigned by the manufacturer", formalDefinition="The \"model\" is an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type." )
    protected StringType model;

    /**
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.
     */
    @Child(name = "version", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Version number (i.e. software)", formalDefinition="The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware." )
    protected StringType version;

    /**
     * Patient information, If the device is affixed to a person.
     */
    @Child(name = "patient", type = {Patient.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Patient to whom Device is affixed", formalDefinition="Patient information, If the device is affixed to a person." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Patient information, If the device is affixed to a person.)
     */
    protected Patient patientTarget;

    /**
     * An organization that is responsible for the provision and ongoing maintenance of the device.
     */
    @Child(name = "owner", type = {Organization.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Organization responsible for device", formalDefinition="An organization that is responsible for the provision and ongoing maintenance of the device." )
    protected Reference owner;

    /**
     * The actual object that is the target of the reference (An organization that is responsible for the provision and ongoing maintenance of the device.)
     */
    protected Organization ownerTarget;

    /**
     * Contact details for an organization or a particular human that is responsible for the device.
     */
    @Child(name = "contact", type = {ContactPoint.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Details for human/organization for support", formalDefinition="Contact details for an organization or a particular human that is responsible for the device." )
    protected List<ContactPoint> contact;

    /**
     * The place where the device can be found.
     */
    @Child(name = "location", type = {Location.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Where the resource is found", formalDefinition="The place where the device can be found." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The place where the device can be found.)
     */
    protected Location locationTarget;

    /**
     * A network address on which the device may be contacted directly.
     */
    @Child(name = "url", type = {UriType.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Network address to contact device", formalDefinition="A network address on which the device may be contacted directly." )
    protected UriType url;

    /**
     * Descriptive information, usage information or implantation information that is not captured in an existing element.
     */
    @Child(name = "note", type = {Annotation.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Device notes and comments", formalDefinition="Descriptive information, usage information or implantation information that is not captured in an existing element." )
    protected List<Annotation> note;

    /**
     * Provides additional safety characteristics about a medical device.  For example devices containing latex.
     */
    @Child(name = "safety", type = {CodeableConcept.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Safety Characteristics of Device", formalDefinition="Provides additional safety characteristics about a medical device.  For example devices containing latex." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-safety")
    protected List<CodeableConcept> safety;

    private static final long serialVersionUID = -1056263930L;

  /**
   * Constructor
   */
    public Device() {
      super();
    }

    /**
     * @return {@link #identifier} (Unique instance identifiers assigned to a device by manufacturers other organizations or owners.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Device setIdentifier(List<Identifier> theIdentifier) { 
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

    public Device addIdentifier(Identifier t) { //3
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
     * @return {@link #udi} ([Unique device identifier (UDI)](device.html#5.11.3.2.2) assigned to device label or package.)
     */
    public DeviceUdiComponent getUdi() { 
      if (this.udi == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.udi");
        else if (Configuration.doAutoCreate())
          this.udi = new DeviceUdiComponent(); // cc
      return this.udi;
    }

    public boolean hasUdi() { 
      return this.udi != null && !this.udi.isEmpty();
    }

    /**
     * @param value {@link #udi} ([Unique device identifier (UDI)](device.html#5.11.3.2.2) assigned to device label or package.)
     */
    public Device setUdi(DeviceUdiComponent value) { 
      this.udi = value;
      return this;
    }

    /**
     * @return {@link #status} (Status of the Device availability.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<FHIRDeviceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<FHIRDeviceStatus>(new FHIRDeviceStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Status of the Device availability.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Device setStatusElement(Enumeration<FHIRDeviceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Status of the Device availability.
     */
    public FHIRDeviceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Status of the Device availability.
     */
    public Device setStatus(FHIRDeviceStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<FHIRDeviceStatus>(new FHIRDeviceStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (Code or identifier to identify a kind of device.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Code or identifier to identify a kind of device.)
     */
    public Device setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #lotNumber} (Lot number assigned by the manufacturer.). This is the underlying object with id, value and extensions. The accessor "getLotNumber" gives direct access to the value
     */
    public StringType getLotNumberElement() { 
      if (this.lotNumber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.lotNumber");
        else if (Configuration.doAutoCreate())
          this.lotNumber = new StringType(); // bb
      return this.lotNumber;
    }

    public boolean hasLotNumberElement() { 
      return this.lotNumber != null && !this.lotNumber.isEmpty();
    }

    public boolean hasLotNumber() { 
      return this.lotNumber != null && !this.lotNumber.isEmpty();
    }

    /**
     * @param value {@link #lotNumber} (Lot number assigned by the manufacturer.). This is the underlying object with id, value and extensions. The accessor "getLotNumber" gives direct access to the value
     */
    public Device setLotNumberElement(StringType value) { 
      this.lotNumber = value;
      return this;
    }

    /**
     * @return Lot number assigned by the manufacturer.
     */
    public String getLotNumber() { 
      return this.lotNumber == null ? null : this.lotNumber.getValue();
    }

    /**
     * @param value Lot number assigned by the manufacturer.
     */
    public Device setLotNumber(String value) { 
      if (Utilities.noString(value))
        this.lotNumber = null;
      else {
        if (this.lotNumber == null)
          this.lotNumber = new StringType();
        this.lotNumber.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #manufacturer} (A name of the manufacturer.). This is the underlying object with id, value and extensions. The accessor "getManufacturer" gives direct access to the value
     */
    public StringType getManufacturerElement() { 
      if (this.manufacturer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.manufacturer");
        else if (Configuration.doAutoCreate())
          this.manufacturer = new StringType(); // bb
      return this.manufacturer;
    }

    public boolean hasManufacturerElement() { 
      return this.manufacturer != null && !this.manufacturer.isEmpty();
    }

    public boolean hasManufacturer() { 
      return this.manufacturer != null && !this.manufacturer.isEmpty();
    }

    /**
     * @param value {@link #manufacturer} (A name of the manufacturer.). This is the underlying object with id, value and extensions. The accessor "getManufacturer" gives direct access to the value
     */
    public Device setManufacturerElement(StringType value) { 
      this.manufacturer = value;
      return this;
    }

    /**
     * @return A name of the manufacturer.
     */
    public String getManufacturer() { 
      return this.manufacturer == null ? null : this.manufacturer.getValue();
    }

    /**
     * @param value A name of the manufacturer.
     */
    public Device setManufacturer(String value) { 
      if (Utilities.noString(value))
        this.manufacturer = null;
      else {
        if (this.manufacturer == null)
          this.manufacturer = new StringType();
        this.manufacturer.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #manufactureDate} (The date and time when the device was manufactured.). This is the underlying object with id, value and extensions. The accessor "getManufactureDate" gives direct access to the value
     */
    public DateTimeType getManufactureDateElement() { 
      if (this.manufactureDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.manufactureDate");
        else if (Configuration.doAutoCreate())
          this.manufactureDate = new DateTimeType(); // bb
      return this.manufactureDate;
    }

    public boolean hasManufactureDateElement() { 
      return this.manufactureDate != null && !this.manufactureDate.isEmpty();
    }

    public boolean hasManufactureDate() { 
      return this.manufactureDate != null && !this.manufactureDate.isEmpty();
    }

    /**
     * @param value {@link #manufactureDate} (The date and time when the device was manufactured.). This is the underlying object with id, value and extensions. The accessor "getManufactureDate" gives direct access to the value
     */
    public Device setManufactureDateElement(DateTimeType value) { 
      this.manufactureDate = value;
      return this;
    }

    /**
     * @return The date and time when the device was manufactured.
     */
    public Date getManufactureDate() { 
      return this.manufactureDate == null ? null : this.manufactureDate.getValue();
    }

    /**
     * @param value The date and time when the device was manufactured.
     */
    public Device setManufactureDate(Date value) { 
      if (value == null)
        this.manufactureDate = null;
      else {
        if (this.manufactureDate == null)
          this.manufactureDate = new DateTimeType();
        this.manufactureDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #expirationDate} (The date and time beyond which this device is no longer valid or should not be used (if applicable).). This is the underlying object with id, value and extensions. The accessor "getExpirationDate" gives direct access to the value
     */
    public DateTimeType getExpirationDateElement() { 
      if (this.expirationDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.expirationDate");
        else if (Configuration.doAutoCreate())
          this.expirationDate = new DateTimeType(); // bb
      return this.expirationDate;
    }

    public boolean hasExpirationDateElement() { 
      return this.expirationDate != null && !this.expirationDate.isEmpty();
    }

    public boolean hasExpirationDate() { 
      return this.expirationDate != null && !this.expirationDate.isEmpty();
    }

    /**
     * @param value {@link #expirationDate} (The date and time beyond which this device is no longer valid or should not be used (if applicable).). This is the underlying object with id, value and extensions. The accessor "getExpirationDate" gives direct access to the value
     */
    public Device setExpirationDateElement(DateTimeType value) { 
      this.expirationDate = value;
      return this;
    }

    /**
     * @return The date and time beyond which this device is no longer valid or should not be used (if applicable).
     */
    public Date getExpirationDate() { 
      return this.expirationDate == null ? null : this.expirationDate.getValue();
    }

    /**
     * @param value The date and time beyond which this device is no longer valid or should not be used (if applicable).
     */
    public Device setExpirationDate(Date value) { 
      if (value == null)
        this.expirationDate = null;
      else {
        if (this.expirationDate == null)
          this.expirationDate = new DateTimeType();
        this.expirationDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #model} (The "model" is an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.). This is the underlying object with id, value and extensions. The accessor "getModel" gives direct access to the value
     */
    public StringType getModelElement() { 
      if (this.model == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.model");
        else if (Configuration.doAutoCreate())
          this.model = new StringType(); // bb
      return this.model;
    }

    public boolean hasModelElement() { 
      return this.model != null && !this.model.isEmpty();
    }

    public boolean hasModel() { 
      return this.model != null && !this.model.isEmpty();
    }

    /**
     * @param value {@link #model} (The "model" is an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.). This is the underlying object with id, value and extensions. The accessor "getModel" gives direct access to the value
     */
    public Device setModelElement(StringType value) { 
      this.model = value;
      return this;
    }

    /**
     * @return The "model" is an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.
     */
    public String getModel() { 
      return this.model == null ? null : this.model.getValue();
    }

    /**
     * @param value The "model" is an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.
     */
    public Device setModel(String value) { 
      if (Utilities.noString(value))
        this.model = null;
      else {
        if (this.model == null)
          this.model = new StringType();
        this.model.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #version} (The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.version");
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
     * @param value {@link #version} (The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public Device setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.
     */
    public Device setVersion(String value) { 
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
     * @return {@link #patient} (Patient information, If the device is affixed to a person.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Patient information, If the device is affixed to a person.)
     */
    public Device setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Patient information, If the device is affixed to a person.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Patient information, If the device is affixed to a person.)
     */
    public Device setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #owner} (An organization that is responsible for the provision and ongoing maintenance of the device.)
     */
    public Reference getOwner() { 
      if (this.owner == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.owner");
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
    public Device setOwner(Reference value) { 
      this.owner = value;
      return this;
    }

    /**
     * @return {@link #owner} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An organization that is responsible for the provision and ongoing maintenance of the device.)
     */
    public Organization getOwnerTarget() { 
      if (this.ownerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.owner");
        else if (Configuration.doAutoCreate())
          this.ownerTarget = new Organization(); // aa
      return this.ownerTarget;
    }

    /**
     * @param value {@link #owner} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An organization that is responsible for the provision and ongoing maintenance of the device.)
     */
    public Device setOwnerTarget(Organization value) { 
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
    public Device setContact(List<ContactPoint> theContact) { 
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

    public Device addContact(ContactPoint t) { //3
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
     * @return {@link #location} (The place where the device can be found.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (The place where the device can be found.)
     */
    public Device setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The place where the device can be found.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location(); // aa
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The place where the device can be found.)
     */
    public Device setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #url} (A network address on which the device may be contacted directly.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.url");
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
    public Device setUrlElement(UriType value) { 
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
    public Device setUrl(String value) { 
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
    public Device setNote(List<Annotation> theNote) { 
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

    public Device addNote(Annotation t) { //3
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
     * @return {@link #safety} (Provides additional safety characteristics about a medical device.  For example devices containing latex.)
     */
    public List<CodeableConcept> getSafety() { 
      if (this.safety == null)
        this.safety = new ArrayList<CodeableConcept>();
      return this.safety;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Device setSafety(List<CodeableConcept> theSafety) { 
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

    public Device addSafety(CodeableConcept t) { //3
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique instance identifiers assigned to a device by manufacturers other organizations or owners.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("udi", "", "[Unique device identifier (UDI)](device.html#5.11.3.2.2) assigned to device label or package.", 0, java.lang.Integer.MAX_VALUE, udi));
        childrenList.add(new Property("status", "code", "Status of the Device availability.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("type", "CodeableConcept", "Code or identifier to identify a kind of device.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("lotNumber", "string", "Lot number assigned by the manufacturer.", 0, java.lang.Integer.MAX_VALUE, lotNumber));
        childrenList.add(new Property("manufacturer", "string", "A name of the manufacturer.", 0, java.lang.Integer.MAX_VALUE, manufacturer));
        childrenList.add(new Property("manufactureDate", "dateTime", "The date and time when the device was manufactured.", 0, java.lang.Integer.MAX_VALUE, manufactureDate));
        childrenList.add(new Property("expirationDate", "dateTime", "The date and time beyond which this device is no longer valid or should not be used (if applicable).", 0, java.lang.Integer.MAX_VALUE, expirationDate));
        childrenList.add(new Property("model", "string", "The \"model\" is an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.", 0, java.lang.Integer.MAX_VALUE, model));
        childrenList.add(new Property("version", "string", "The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("patient", "Reference(Patient)", "Patient information, If the device is affixed to a person.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("owner", "Reference(Organization)", "An organization that is responsible for the provision and ongoing maintenance of the device.", 0, java.lang.Integer.MAX_VALUE, owner));
        childrenList.add(new Property("contact", "ContactPoint", "Contact details for an organization or a particular human that is responsible for the device.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("location", "Reference(Location)", "The place where the device can be found.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("url", "uri", "A network address on which the device may be contacted directly.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("note", "Annotation", "Descriptive information, usage information or implantation information that is not captured in an existing element.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("safety", "CodeableConcept", "Provides additional safety characteristics about a medical device.  For example devices containing latex.", 0, java.lang.Integer.MAX_VALUE, safety));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : new Base[] {this.udi}; // DeviceUdiComponent
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<FHIRDeviceStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 462547450: /*lotNumber*/ return this.lotNumber == null ? new Base[0] : new Base[] {this.lotNumber}; // StringType
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : new Base[] {this.manufacturer}; // StringType
        case 416714767: /*manufactureDate*/ return this.manufactureDate == null ? new Base[0] : new Base[] {this.manufactureDate}; // DateTimeType
        case -668811523: /*expirationDate*/ return this.expirationDate == null ? new Base[0] : new Base[] {this.expirationDate}; // DateTimeType
        case 104069929: /*model*/ return this.model == null ? new Base[0] : new Base[] {this.model}; // StringType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 106164915: /*owner*/ return this.owner == null ? new Base[0] : new Base[] {this.owner}; // Reference
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactPoint
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -909893934: /*safety*/ return this.safety == null ? new Base[0] : this.safety.toArray(new Base[this.safety.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 115642: // udi
          this.udi = (DeviceUdiComponent) value; // DeviceUdiComponent
          return value;
        case -892481550: // status
          value = new FHIRDeviceStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<FHIRDeviceStatus>
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 462547450: // lotNumber
          this.lotNumber = castToString(value); // StringType
          return value;
        case -1969347631: // manufacturer
          this.manufacturer = castToString(value); // StringType
          return value;
        case 416714767: // manufactureDate
          this.manufactureDate = castToDateTime(value); // DateTimeType
          return value;
        case -668811523: // expirationDate
          this.expirationDate = castToDateTime(value); // DateTimeType
          return value;
        case 104069929: // model
          this.model = castToString(value); // StringType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case 106164915: // owner
          this.owner = castToReference(value); // Reference
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactPoint(value)); // ContactPoint
          return value;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          return value;
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case -909893934: // safety
          this.getSafety().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("udi")) {
          this.udi = (DeviceUdiComponent) value; // DeviceUdiComponent
        } else if (name.equals("status")) {
          value = new FHIRDeviceStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<FHIRDeviceStatus>
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("lotNumber")) {
          this.lotNumber = castToString(value); // StringType
        } else if (name.equals("manufacturer")) {
          this.manufacturer = castToString(value); // StringType
        } else if (name.equals("manufactureDate")) {
          this.manufactureDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("expirationDate")) {
          this.expirationDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("model")) {
          this.model = castToString(value); // StringType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("owner")) {
          this.owner = castToReference(value); // Reference
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactPoint(value));
        } else if (name.equals("location")) {
          this.location = castToReference(value); // Reference
        } else if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("safety")) {
          this.getSafety().add(castToCodeableConcept(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 115642:  return getUdi(); 
        case -892481550:  return getStatusElement();
        case 3575610:  return getType(); 
        case 462547450:  return getLotNumberElement();
        case -1969347631:  return getManufacturerElement();
        case 416714767:  return getManufactureDateElement();
        case -668811523:  return getExpirationDateElement();
        case 104069929:  return getModelElement();
        case 351608024:  return getVersionElement();
        case -791418107:  return getPatient(); 
        case 106164915:  return getOwner(); 
        case 951526432:  return addContact(); 
        case 1901043637:  return getLocation(); 
        case 116079:  return getUrlElement();
        case 3387378:  return addNote(); 
        case -909893934:  return addSafety(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 115642: /*udi*/ return new String[] {};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 462547450: /*lotNumber*/ return new String[] {"string"};
        case -1969347631: /*manufacturer*/ return new String[] {"string"};
        case 416714767: /*manufactureDate*/ return new String[] {"dateTime"};
        case -668811523: /*expirationDate*/ return new String[] {"dateTime"};
        case 104069929: /*model*/ return new String[] {"string"};
        case 351608024: /*version*/ return new String[] {"string"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 106164915: /*owner*/ return new String[] {"Reference"};
        case 951526432: /*contact*/ return new String[] {"ContactPoint"};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case 116079: /*url*/ return new String[] {"uri"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case -909893934: /*safety*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("udi")) {
          this.udi = new DeviceUdiComponent();
          return this.udi;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.status");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("lotNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.lotNumber");
        }
        else if (name.equals("manufacturer")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.manufacturer");
        }
        else if (name.equals("manufactureDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.manufactureDate");
        }
        else if (name.equals("expirationDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.expirationDate");
        }
        else if (name.equals("model")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.model");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.version");
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("owner")) {
          this.owner = new Reference();
          return this.owner;
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Device.url");
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("safety")) {
          return addSafety();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Device";

  }

      public Device copy() {
        Device dst = new Device();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.udi = udi == null ? null : udi.copy();
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.lotNumber = lotNumber == null ? null : lotNumber.copy();
        dst.manufacturer = manufacturer == null ? null : manufacturer.copy();
        dst.manufactureDate = manufactureDate == null ? null : manufactureDate.copy();
        dst.expirationDate = expirationDate == null ? null : expirationDate.copy();
        dst.model = model == null ? null : model.copy();
        dst.version = version == null ? null : version.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.owner = owner == null ? null : owner.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactPoint>();
          for (ContactPoint i : contact)
            dst.contact.add(i.copy());
        };
        dst.location = location == null ? null : location.copy();
        dst.url = url == null ? null : url.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (safety != null) {
          dst.safety = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : safety)
            dst.safety.add(i.copy());
        };
        return dst;
      }

      protected Device typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Device))
          return false;
        Device o = (Device) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(udi, o.udi, true) && compareDeep(status, o.status, true)
           && compareDeep(type, o.type, true) && compareDeep(lotNumber, o.lotNumber, true) && compareDeep(manufacturer, o.manufacturer, true)
           && compareDeep(manufactureDate, o.manufactureDate, true) && compareDeep(expirationDate, o.expirationDate, true)
           && compareDeep(model, o.model, true) && compareDeep(version, o.version, true) && compareDeep(patient, o.patient, true)
           && compareDeep(owner, o.owner, true) && compareDeep(contact, o.contact, true) && compareDeep(location, o.location, true)
           && compareDeep(url, o.url, true) && compareDeep(note, o.note, true) && compareDeep(safety, o.safety, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Device))
          return false;
        Device o = (Device) other;
        return compareValues(status, o.status, true) && compareValues(lotNumber, o.lotNumber, true) && compareValues(manufacturer, o.manufacturer, true)
           && compareValues(manufactureDate, o.manufactureDate, true) && compareValues(expirationDate, o.expirationDate, true)
           && compareValues(model, o.model, true) && compareValues(version, o.version, true) && compareValues(url, o.url, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, udi, status
          , type, lotNumber, manufacturer, manufactureDate, expirationDate, model, version
          , patient, owner, contact, location, url, note, safety);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Device;
   }

 /**
   * Search parameter: <b>udi-di</b>
   * <p>
   * Description: <b>The udi Device Identifier (DI)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Device.udi.deviceIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="udi-di", path="Device.udi.deviceIdentifier", description="The udi Device Identifier (DI)", type="string" )
  public static final String SP_UDI_DI = "udi-di";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>udi-di</b>
   * <p>
   * Description: <b>The udi Device Identifier (DI)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Device.udi.deviceIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam UDI_DI = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_UDI_DI);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Instance id from manufacturer, owner, and others</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Device.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Device.identifier", description="Instance id from manufacturer, owner, and others", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Instance id from manufacturer, owner, and others</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Device.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>udi-carrier</b>
   * <p>
   * Description: <b>UDI Barcode (RFID or other technology) string either in HRF format or AIDC format converted to base64 string.</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Device.udi.carrierHRF, Device.udi.carrierAIDC</b><br>
   * </p>
   */
  @SearchParamDefinition(name="udi-carrier", path="Device.udi.carrierHRF | Device.udi.carrierAIDC", description="UDI Barcode (RFID or other technology) string either in HRF format or AIDC format converted to base64 string.", type="string" )
  public static final String SP_UDI_CARRIER = "udi-carrier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>udi-carrier</b>
   * <p>
   * Description: <b>UDI Barcode (RFID or other technology) string either in HRF format or AIDC format converted to base64 string.</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Device.udi.carrierHRF, Device.udi.carrierAIDC</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam UDI_CARRIER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_UDI_CARRIER);

 /**
   * Search parameter: <b>device-name</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the Device.udi.name  or Device.type.coding.display or  Device.type.text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Device.udi.name, Device.type.text, Device.type.coding.display</b><br>
   * </p>
   */
  @SearchParamDefinition(name="device-name", path="Device.udi.name | Device.type.text | Device.type.coding.display", description="A server defined search that may match any of the string fields in the Device.udi.name  or Device.type.coding.display or  Device.type.text", type="string" )
  public static final String SP_DEVICE_NAME = "device-name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>device-name</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the Device.udi.name  or Device.type.coding.display or  Device.type.text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Device.udi.name, Device.type.text, Device.type.coding.display</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DEVICE_NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DEVICE_NAME);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Patient information, if the resource is affixed to a person</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Device.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Device.patient", description="Patient information, if the resource is affixed to a person", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Patient information, if the resource is affixed to a person</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Device.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Device:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Device:patient").toLocked();

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The organization responsible for the device</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Device.owner</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="Device.owner", description="The organization responsible for the device", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The organization responsible for the device</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Device.owner</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Device:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("Device:organization").toLocked();

 /**
   * Search parameter: <b>model</b>
   * <p>
   * Description: <b>The model of the device</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Device.model</b><br>
   * </p>
   */
  @SearchParamDefinition(name="model", path="Device.model", description="The model of the device", type="string" )
  public static final String SP_MODEL = "model";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>model</b>
   * <p>
   * Description: <b>The model of the device</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Device.model</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam MODEL = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_MODEL);

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>A location, where the resource is found</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Device.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="Device.location", description="A location, where the resource is found", type="reference", target={Location.class } )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>A location, where the resource is found</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Device.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Device:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("Device:location").toLocked();

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The type of the device</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Device.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Device.type", description="The type of the device", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The type of the device</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Device.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>Network address to contact device</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Device.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="Device.url", description="Network address to contact device", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>Network address to contact device</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Device.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>manufacturer</b>
   * <p>
   * Description: <b>The manufacturer of the device</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Device.manufacturer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="manufacturer", path="Device.manufacturer", description="The manufacturer of the device", type="string" )
  public static final String SP_MANUFACTURER = "manufacturer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
   * <p>
   * Description: <b>The manufacturer of the device</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Device.manufacturer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam MANUFACTURER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_MANUFACTURER);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>active | inactive | entered-in-error | unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Device.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Device.status", description="active | inactive | entered-in-error | unknown", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>active | inactive | entered-in-error | unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Device.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

