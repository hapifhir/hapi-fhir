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
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * This resource identifies an instance or a type of a manufactured item that is used in the provision of healthcare without being substantially changed through that activity. The device may be a medical or non-medical device.  Medical devices includes durable (reusable) medical equipment, implantable devices, as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.  Non-medical devices may include items such as a machine, cellphone, computer, application, etc.
 */
@ResourceDef(name="Device", profile="http://hl7.org/fhir/Profile/Device")
public class Device extends DomainResource {

    public enum DeviceStatus {
        /**
         * The Device is available for use.
         */
        AVAILABLE, 
        /**
         * The Device is no longer available for use (e.g. lost, expired, damaged).
         */
        NOTAVAILABLE, 
        /**
         * The Device was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("available".equals(codeString))
          return AVAILABLE;
        if ("not-available".equals(codeString))
          return NOTAVAILABLE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown DeviceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AVAILABLE: return "available";
            case NOTAVAILABLE: return "not-available";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AVAILABLE: return "http://hl7.org/fhir/devicestatus";
            case NOTAVAILABLE: return "http://hl7.org/fhir/devicestatus";
            case ENTEREDINERROR: return "http://hl7.org/fhir/devicestatus";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AVAILABLE: return "The Device is available for use.";
            case NOTAVAILABLE: return "The Device is no longer available for use (e.g. lost, expired, damaged).";
            case ENTEREDINERROR: return "The Device was entered in error and voided.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AVAILABLE: return "Available";
            case NOTAVAILABLE: return "Not Available";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class DeviceStatusEnumFactory implements EnumFactory<DeviceStatus> {
    public DeviceStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("available".equals(codeString))
          return DeviceStatus.AVAILABLE;
        if ("not-available".equals(codeString))
          return DeviceStatus.NOTAVAILABLE;
        if ("entered-in-error".equals(codeString))
          return DeviceStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown DeviceStatus code '"+codeString+"'");
        }
        public Enumeration<DeviceStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("available".equals(codeString))
          return new Enumeration<DeviceStatus>(this, DeviceStatus.AVAILABLE);
        if ("not-available".equals(codeString))
          return new Enumeration<DeviceStatus>(this, DeviceStatus.NOTAVAILABLE);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<DeviceStatus>(this, DeviceStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown DeviceStatus code '"+codeString+"'");
        }
    public String toCode(DeviceStatus code) {
      if (code == DeviceStatus.AVAILABLE)
        return "available";
      if (code == DeviceStatus.NOTAVAILABLE)
        return "not-available";
      if (code == DeviceStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(DeviceStatus code) {
      return code.getSystem();
      }
    }

    /**
     * Unique instance identifiers assigned to a device by manufacturers other organizations or owners.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Instance identifier", formalDefinition="Unique instance identifiers assigned to a device by manufacturers other organizations or owners." )
    protected List<Identifier> identifier;

    /**
     * [Unique device identifier (UDI)](device.html#5.11.3.2.2) barcode or rfid string assigned to device label or package.
     */
    @Child(name = "udiCarrier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Unique Device Identifier (UDI) Barcode string", formalDefinition="[Unique device identifier (UDI)](device.html#5.11.3.2.2) barcode or rfid string assigned to device label or package." )
    protected Identifier udiCarrier;

    /**
     * Status of the Device availability.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="available | not-available | entered-in-error", formalDefinition="Status of the Device availability." )
    protected Enumeration<DeviceStatus> status;

    /**
     * Code or identifier to identify a kind of device.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=3, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What kind of device this is", formalDefinition="Code or identifier to identify a kind of device." )
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

    private static final long serialVersionUID = -710362206L;

  /**
   * Constructor
   */
    public Device() {
      super();
    }

  /**
   * Constructor
   */
    public Device(CodeableConcept type) {
      super();
      this.type = type;
    }

    /**
     * @return {@link #identifier} (Unique instance identifiers assigned to a device by manufacturers other organizations or owners.)
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
     * @return {@link #identifier} (Unique instance identifiers assigned to a device by manufacturers other organizations or owners.)
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
    public Device addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #udiCarrier} ([Unique device identifier (UDI)](device.html#5.11.3.2.2) barcode or rfid string assigned to device label or package.)
     */
    public Identifier getUdiCarrier() { 
      if (this.udiCarrier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.udiCarrier");
        else if (Configuration.doAutoCreate())
          this.udiCarrier = new Identifier(); // cc
      return this.udiCarrier;
    }

    public boolean hasUdiCarrier() { 
      return this.udiCarrier != null && !this.udiCarrier.isEmpty();
    }

    /**
     * @param value {@link #udiCarrier} ([Unique device identifier (UDI)](device.html#5.11.3.2.2) barcode or rfid string assigned to device label or package.)
     */
    public Device setUdiCarrier(Identifier value) { 
      this.udiCarrier = value;
      return this;
    }

    /**
     * @return {@link #status} (Status of the Device availability.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<DeviceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<DeviceStatus>(new DeviceStatusEnumFactory()); // bb
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
    public Device setStatusElement(Enumeration<DeviceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Status of the Device availability.
     */
    public DeviceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Status of the Device availability.
     */
    public Device setStatus(DeviceStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<DeviceStatus>(new DeviceStatusEnumFactory());
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

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactPoint item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contact details for an organization or a particular human that is responsible for the device.)
     */
    // syntactic sugar
    public ContactPoint addContact() { //3
      ContactPoint t = new ContactPoint();
      if (this.contact == null)
        this.contact = new ArrayList<ContactPoint>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public Device addContact(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactPoint>();
      this.contact.add(t);
      return this;
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

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #note} (Descriptive information, usage information or implantation information that is not captured in an existing element.)
     */
    // syntactic sugar
    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    // syntactic sugar
    public Device addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique instance identifiers assigned to a device by manufacturers other organizations or owners.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("udiCarrier", "Identifier", "[Unique device identifier (UDI)](device.html#5.11.3.2.2) barcode or rfid string assigned to device label or package.", 0, java.lang.Integer.MAX_VALUE, udiCarrier));
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
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1343558178: /*udiCarrier*/ return this.udiCarrier == null ? new Base[0] : new Base[] {this.udiCarrier}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<DeviceStatus>
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
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -1343558178: // udiCarrier
          this.udiCarrier = castToIdentifier(value); // Identifier
          break;
        case -892481550: // status
          this.status = new DeviceStatusEnumFactory().fromType(value); // Enumeration<DeviceStatus>
          break;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case 462547450: // lotNumber
          this.lotNumber = castToString(value); // StringType
          break;
        case -1969347631: // manufacturer
          this.manufacturer = castToString(value); // StringType
          break;
        case 416714767: // manufactureDate
          this.manufactureDate = castToDateTime(value); // DateTimeType
          break;
        case -668811523: // expirationDate
          this.expirationDate = castToDateTime(value); // DateTimeType
          break;
        case 104069929: // model
          this.model = castToString(value); // StringType
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case 106164915: // owner
          this.owner = castToReference(value); // Reference
          break;
        case 951526432: // contact
          this.getContact().add(castToContactPoint(value)); // ContactPoint
          break;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          break;
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("udiCarrier"))
          this.udiCarrier = castToIdentifier(value); // Identifier
        else if (name.equals("status"))
          this.status = new DeviceStatusEnumFactory().fromType(value); // Enumeration<DeviceStatus>
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("lotNumber"))
          this.lotNumber = castToString(value); // StringType
        else if (name.equals("manufacturer"))
          this.manufacturer = castToString(value); // StringType
        else if (name.equals("manufactureDate"))
          this.manufactureDate = castToDateTime(value); // DateTimeType
        else if (name.equals("expirationDate"))
          this.expirationDate = castToDateTime(value); // DateTimeType
        else if (name.equals("model"))
          this.model = castToString(value); // StringType
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("owner"))
          this.owner = castToReference(value); // Reference
        else if (name.equals("contact"))
          this.getContact().add(castToContactPoint(value));
        else if (name.equals("location"))
          this.location = castToReference(value); // Reference
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -1343558178:  return getUdiCarrier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<DeviceStatus>
        case 3575610:  return getType(); // CodeableConcept
        case 462547450: throw new FHIRException("Cannot make property lotNumber as it is not a complex type"); // StringType
        case -1969347631: throw new FHIRException("Cannot make property manufacturer as it is not a complex type"); // StringType
        case 416714767: throw new FHIRException("Cannot make property manufactureDate as it is not a complex type"); // DateTimeType
        case -668811523: throw new FHIRException("Cannot make property expirationDate as it is not a complex type"); // DateTimeType
        case 104069929: throw new FHIRException("Cannot make property model as it is not a complex type"); // StringType
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case -791418107:  return getPatient(); // Reference
        case 106164915:  return getOwner(); // Reference
        case 951526432:  return addContact(); // ContactPoint
        case 1901043637:  return getLocation(); // Reference
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case 3387378:  return addNote(); // Annotation
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("udiCarrier")) {
          this.udiCarrier = new Identifier();
          return this.udiCarrier;
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
        dst.udiCarrier = udiCarrier == null ? null : udiCarrier.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(udiCarrier, o.udiCarrier, true)
           && compareDeep(status, o.status, true) && compareDeep(type, o.type, true) && compareDeep(lotNumber, o.lotNumber, true)
           && compareDeep(manufacturer, o.manufacturer, true) && compareDeep(manufactureDate, o.manufactureDate, true)
           && compareDeep(expirationDate, o.expirationDate, true) && compareDeep(model, o.model, true) && compareDeep(version, o.version, true)
           && compareDeep(patient, o.patient, true) && compareDeep(owner, o.owner, true) && compareDeep(contact, o.contact, true)
           && compareDeep(location, o.location, true) && compareDeep(url, o.url, true) && compareDeep(note, o.note, true)
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
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (udiCarrier == null || udiCarrier.isEmpty())
           && (status == null || status.isEmpty()) && (type == null || type.isEmpty()) && (lotNumber == null || lotNumber.isEmpty())
           && (manufacturer == null || manufacturer.isEmpty()) && (manufactureDate == null || manufactureDate.isEmpty())
           && (expirationDate == null || expirationDate.isEmpty()) && (model == null || model.isEmpty())
           && (version == null || version.isEmpty()) && (patient == null || patient.isEmpty()) && (owner == null || owner.isEmpty())
           && (contact == null || contact.isEmpty()) && (location == null || location.isEmpty()) && (url == null || url.isEmpty())
           && (note == null || note.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Device;
   }

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The organization responsible for the device</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Device.owner</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="Device.owner", description="The organization responsible for the device", type="reference" )
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
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Patient information, if the resource is affixed to a person</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Device.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Device.patient", description="Patient information, if the resource is affixed to a person", type="reference" )
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
   * Search parameter: <b>udicarrier</b>
   * <p>
   * Description: <b>Barcode string (udi)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Device.udiCarrier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="udicarrier", path="Device.udiCarrier", description="Barcode string (udi)", type="token" )
  public static final String SP_UDICARRIER = "udicarrier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>udicarrier</b>
   * <p>
   * Description: <b>Barcode string (udi)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Device.udiCarrier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam UDICARRIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_UDICARRIER);

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>A location, where the resource is found</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Device.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="Device.location", description="A location, where the resource is found", type="reference" )
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


}

