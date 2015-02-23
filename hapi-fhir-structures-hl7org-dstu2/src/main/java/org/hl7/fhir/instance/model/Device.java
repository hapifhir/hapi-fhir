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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * This resource identifies an instance of a manufactured thing that is used in the provision of healthcare without being substantially changed through that activity. The device may be a machine, an insert, a computer, an application, etc. This includes durable (reusable) medical equipment as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.
 */
@ResourceDef(name="Device", profile="http://hl7.org/fhir/Profile/Device")
public class Device extends DomainResource {

    /**
     * Unique instance identifiers assigned to a device by organizations like manufacturers, owners or regulatory agencies.   If the identifier identifies the type of device, Device.type should be used.   An example is the FDA Mandated Unique Device Identifier (UDI) which identifies an instance of a device uniquely if the serial number is present, .  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Instance id from manufacturer, owner, regulatory agencies and others", formalDefinition="Unique instance identifiers assigned to a device by organizations like manufacturers, owners or regulatory agencies.   If the identifier identifies the type of device, Device.type should be used.   An example is the FDA Mandated Unique Device Identifier (UDI) which identifies an instance of a device uniquely if the serial number is present, .  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm." )
    protected List<Identifier> identifier;

    /**
     * Code or identifier to identify a kind of device   An example is the FDA Mandated Unique Device Identifier (UDI) which identifies a type of a device when the serial number is absent, otherwise it uniquely identifies the device instance and Device.identifier should be used instead of Device.type.  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order = 1, min = 1, max = 1)
    @Description(shortDefinition="What kind of device this is", formalDefinition="Code or identifier to identify a kind of device   An example is the FDA Mandated Unique Device Identifier (UDI) which identifies a type of a device when the serial number is absent, otherwise it uniquely identifies the device instance and Device.identifier should be used instead of Device.type.  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm." )
    protected CodeableConcept type;

    /**
     * A name of the manufacturer.
     */
    @Child(name = "manufacturer", type = {StringType.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Name of device manufacturer", formalDefinition="A name of the manufacturer." )
    protected StringType manufacturer;

    /**
     * The "model" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.
     */
    @Child(name = "model", type = {StringType.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="Model id assigned by the manufacturer", formalDefinition="The 'model' - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type." )
    protected StringType model;

    /**
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.
     */
    @Child(name = "version", type = {StringType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Version number (i.e. software)", formalDefinition="The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware." )
    protected StringType version;

    /**
     * The Date and time when the device was manufactured.
     */
    @Child(name = "manufactureDate", type = {DateTimeType.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Manufacture date", formalDefinition="The Date and time when the device was manufactured." )
    protected DateTimeType manufactureDate;

    /**
     * The date and time beyond which this device is no longer valid or should not be used (if applicable).
     */
    @Child(name = "expiry", type = {DateTimeType.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="Date and time of expiry of this device (if applicable)", formalDefinition="The date and time beyond which this device is no longer valid or should not be used (if applicable)." )
    protected DateTimeType expiry;

    /**
     * Lot number assigned by the manufacturer.
     */
    @Child(name = "lotNumber", type = {StringType.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="Lot number of manufacture", formalDefinition="Lot number assigned by the manufacturer." )
    protected StringType lotNumber;

    /**
     * An organization that is responsible for the provision and ongoing maintenance of the device.
     */
    @Child(name = "owner", type = {Organization.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Organization responsible for device", formalDefinition="An organization that is responsible for the provision and ongoing maintenance of the device." )
    protected Reference owner;

    /**
     * The actual object that is the target of the reference (An organization that is responsible for the provision and ongoing maintenance of the device.)
     */
    protected Organization ownerTarget;

    /**
     * The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. "in/with the patient"), or a coded location.
     */
    @Child(name = "location", type = {Location.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Where the resource is found", formalDefinition="The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. 'in/with the patient'), or a coded location." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. "in/with the patient"), or a coded location.)
     */
    protected Location locationTarget;

    /**
     * Patient information, if the resource is affixed to a person.
     */
    @Child(name = "patient", type = {Patient.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="If the resource is affixed to a person", formalDefinition="Patient information, if the resource is affixed to a person." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Patient information, if the resource is affixed to a person.)
     */
    protected Patient patientTarget;

    /**
     * Contact details for an organization or a particular human that is responsible for the device.
     */
    @Child(name = "contact", type = {ContactPoint.class}, order = 11, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Details for human/organization for support", formalDefinition="Contact details for an organization or a particular human that is responsible for the device." )
    protected List<ContactPoint> contact;

    /**
     * A network address on which the device may be contacted directly.
     */
    @Child(name = "url", type = {UriType.class}, order = 12, min = 0, max = 1)
    @Description(shortDefinition="Network address to contact device", formalDefinition="A network address on which the device may be contacted directly." )
    protected UriType url;

    private static final long serialVersionUID = 398718675L;

    public Device() {
      super();
    }

    public Device(CodeableConcept type) {
      super();
      this.type = type;
    }

    /**
     * @return {@link #identifier} (Unique instance identifiers assigned to a device by organizations like manufacturers, owners or regulatory agencies.   If the identifier identifies the type of device, Device.type should be used.   An example is the FDA Mandated Unique Device Identifier (UDI) which identifies an instance of a device uniquely if the serial number is present, .  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm.)
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
     * @return {@link #identifier} (Unique instance identifiers assigned to a device by organizations like manufacturers, owners or regulatory agencies.   If the identifier identifies the type of device, Device.type should be used.   An example is the FDA Mandated Unique Device Identifier (UDI) which identifies an instance of a device uniquely if the serial number is present, .  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #type} (Code or identifier to identify a kind of device   An example is the FDA Mandated Unique Device Identifier (UDI) which identifies a type of a device when the serial number is absent, otherwise it uniquely identifies the device instance and Device.identifier should be used instead of Device.type.  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm.)
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
     * @param value {@link #type} (Code or identifier to identify a kind of device   An example is the FDA Mandated Unique Device Identifier (UDI) which identifies a type of a device when the serial number is absent, otherwise it uniquely identifies the device instance and Device.identifier should be used instead of Device.type.  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm.)
     */
    public Device setType(CodeableConcept value) { 
      this.type = value;
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
     * @return {@link #model} (The "model" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.). This is the underlying object with id, value and extensions. The accessor "getModel" gives direct access to the value
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
     * @param value {@link #model} (The "model" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.). This is the underlying object with id, value and extensions. The accessor "getModel" gives direct access to the value
     */
    public Device setModelElement(StringType value) { 
      this.model = value;
      return this;
    }

    /**
     * @return The "model" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.
     */
    public String getModel() { 
      return this.model == null ? null : this.model.getValue();
    }

    /**
     * @param value The "model" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.
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
     * @return {@link #manufactureDate} (The Date and time when the device was manufactured.). This is the underlying object with id, value and extensions. The accessor "getManufactureDate" gives direct access to the value
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
     * @param value {@link #manufactureDate} (The Date and time when the device was manufactured.). This is the underlying object with id, value and extensions. The accessor "getManufactureDate" gives direct access to the value
     */
    public Device setManufactureDateElement(DateTimeType value) { 
      this.manufactureDate = value;
      return this;
    }

    /**
     * @return The Date and time when the device was manufactured.
     */
    public Date getManufactureDate() { 
      return this.manufactureDate == null ? null : this.manufactureDate.getValue();
    }

    /**
     * @param value The Date and time when the device was manufactured.
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
     * @return {@link #expiry} (The date and time beyond which this device is no longer valid or should not be used (if applicable).). This is the underlying object with id, value and extensions. The accessor "getExpiry" gives direct access to the value
     */
    public DateTimeType getExpiryElement() { 
      if (this.expiry == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Device.expiry");
        else if (Configuration.doAutoCreate())
          this.expiry = new DateTimeType(); // bb
      return this.expiry;
    }

    public boolean hasExpiryElement() { 
      return this.expiry != null && !this.expiry.isEmpty();
    }

    public boolean hasExpiry() { 
      return this.expiry != null && !this.expiry.isEmpty();
    }

    /**
     * @param value {@link #expiry} (The date and time beyond which this device is no longer valid or should not be used (if applicable).). This is the underlying object with id, value and extensions. The accessor "getExpiry" gives direct access to the value
     */
    public Device setExpiryElement(DateTimeType value) { 
      this.expiry = value;
      return this;
    }

    /**
     * @return The date and time beyond which this device is no longer valid or should not be used (if applicable).
     */
    public Date getExpiry() { 
      return this.expiry == null ? null : this.expiry.getValue();
    }

    /**
     * @param value The date and time beyond which this device is no longer valid or should not be used (if applicable).
     */
    public Device setExpiry(Date value) { 
      if (value == null)
        this.expiry = null;
      else {
        if (this.expiry == null)
          this.expiry = new DateTimeType();
        this.expiry.setValue(value);
      }
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
     * @return {@link #location} (The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. "in/with the patient"), or a coded location.)
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
     * @param value {@link #location} (The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. "in/with the patient"), or a coded location.)
     */
    public Device setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. "in/with the patient"), or a coded location.)
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
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. "in/with the patient"), or a coded location.)
     */
    public Device setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #patient} (Patient information, if the resource is affixed to a person.)
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
     * @param value {@link #patient} (Patient information, if the resource is affixed to a person.)
     */
    public Device setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Patient information, if the resource is affixed to a person.)
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
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Patient information, if the resource is affixed to a person.)
     */
    public Device setPatientTarget(Patient value) { 
      this.patientTarget = value;
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique instance identifiers assigned to a device by organizations like manufacturers, owners or regulatory agencies.   If the identifier identifies the type of device, Device.type should be used.   An example is the FDA Mandated Unique Device Identifier (UDI) which identifies an instance of a device uniquely if the serial number is present, .  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "CodeableConcept", "Code or identifier to identify a kind of device   An example is the FDA Mandated Unique Device Identifier (UDI) which identifies a type of a device when the serial number is absent, otherwise it uniquely identifies the device instance and Device.identifier should be used instead of Device.type.  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("manufacturer", "string", "A name of the manufacturer.", 0, java.lang.Integer.MAX_VALUE, manufacturer));
        childrenList.add(new Property("model", "string", "The 'model' - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.", 0, java.lang.Integer.MAX_VALUE, model));
        childrenList.add(new Property("version", "string", "The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("manufactureDate", "dateTime", "The Date and time when the device was manufactured.", 0, java.lang.Integer.MAX_VALUE, manufactureDate));
        childrenList.add(new Property("expiry", "dateTime", "The date and time beyond which this device is no longer valid or should not be used (if applicable).", 0, java.lang.Integer.MAX_VALUE, expiry));
        childrenList.add(new Property("lotNumber", "string", "Lot number assigned by the manufacturer.", 0, java.lang.Integer.MAX_VALUE, lotNumber));
        childrenList.add(new Property("owner", "Reference(Organization)", "An organization that is responsible for the provision and ongoing maintenance of the device.", 0, java.lang.Integer.MAX_VALUE, owner));
        childrenList.add(new Property("location", "Reference(Location)", "The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. 'in/with the patient'), or a coded location.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("patient", "Reference(Patient)", "Patient information, if the resource is affixed to a person.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("contact", "ContactPoint", "Contact details for an organization or a particular human that is responsible for the device.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("url", "uri", "A network address on which the device may be contacted directly.", 0, java.lang.Integer.MAX_VALUE, url));
      }

      public Device copy() {
        Device dst = new Device();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.manufacturer = manufacturer == null ? null : manufacturer.copy();
        dst.model = model == null ? null : model.copy();
        dst.version = version == null ? null : version.copy();
        dst.manufactureDate = manufactureDate == null ? null : manufactureDate.copy();
        dst.expiry = expiry == null ? null : expiry.copy();
        dst.lotNumber = lotNumber == null ? null : lotNumber.copy();
        dst.owner = owner == null ? null : owner.copy();
        dst.location = location == null ? null : location.copy();
        dst.patient = patient == null ? null : patient.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactPoint>();
          for (ContactPoint i : contact)
            dst.contact.add(i.copy());
        };
        dst.url = url == null ? null : url.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(manufacturer, o.manufacturer, true)
           && compareDeep(model, o.model, true) && compareDeep(version, o.version, true) && compareDeep(manufactureDate, o.manufactureDate, true)
           && compareDeep(expiry, o.expiry, true) && compareDeep(lotNumber, o.lotNumber, true) && compareDeep(owner, o.owner, true)
           && compareDeep(location, o.location, true) && compareDeep(patient, o.patient, true) && compareDeep(contact, o.contact, true)
           && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Device))
          return false;
        Device o = (Device) other;
        return compareValues(manufacturer, o.manufacturer, true) && compareValues(model, o.model, true) && compareValues(version, o.version, true)
           && compareValues(manufactureDate, o.manufactureDate, true) && compareValues(expiry, o.expiry, true)
           && compareValues(lotNumber, o.lotNumber, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
           && (manufacturer == null || manufacturer.isEmpty()) && (model == null || model.isEmpty())
           && (version == null || version.isEmpty()) && (manufactureDate == null || manufactureDate.isEmpty())
           && (expiry == null || expiry.isEmpty()) && (lotNumber == null || lotNumber.isEmpty()) && (owner == null || owner.isEmpty())
           && (location == null || location.isEmpty()) && (patient == null || patient.isEmpty()) && (contact == null || contact.isEmpty())
           && (url == null || url.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Device;
   }

    @SearchParamDefinition(name = "identifier", path = "Device.identifier", description = "Instance id from manufacturer, owner, regulatory agencies and others", type = "token")
    public static final String SP_IDENTIFIER = "identifier";
    @SearchParamDefinition(name = "patient", path = "Device.patient", description = "Patient information, if the resource is affixed to a person", type = "reference")
    public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="organization", path="Device.owner", description="The organization responsible for the device", type="reference" )
  public static final String SP_ORGANIZATION = "organization";
  @SearchParamDefinition(name="model", path="Device.model", description="The model of the device", type="string" )
  public static final String SP_MODEL = "model";
  @SearchParamDefinition(name="location", path="Device.location", description="A location, where the resource is found", type="reference" )
  public static final String SP_LOCATION = "location";
  @SearchParamDefinition(name="type", path="Device.type", description="The type of the device", type="token" )
  public static final String SP_TYPE = "type";
    @SearchParamDefinition(name = "manufacturer", path = "Device.manufacturer", description = "The manufacturer of the device", type = "string")
    public static final String SP_MANUFACTURER = "manufacturer";

}

