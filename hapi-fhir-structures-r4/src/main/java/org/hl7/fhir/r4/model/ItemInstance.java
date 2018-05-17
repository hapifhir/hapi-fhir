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
 * A physical, countable instance of an item, for example one box or one unit.
 */
@ResourceDef(name="ItemInstance", profile="http://hl7.org/fhir/Profile/ItemInstance")
public class ItemInstance extends DomainResource {

    /**
     * The quantity or amount of instances. For example if several units are being counted for inventory, this quantity can be more than one, provided they are not unique. Seriallized items are considered unique and as such would have a quantity max 1. This element is required and its presence asserts that the reource refers to a physical item.
     */
    @Child(name = "count", type = {IntegerType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The count of items", formalDefinition="The quantity or amount of instances. For example if several units are being counted for inventory, this quantity can be more than one, provided they are not unique. Seriallized items are considered unique and as such would have a quantity max 1. This element is required and its presence asserts that the reource refers to a physical item." )
    protected IntegerType count;

    /**
     * The location where the item is phisically located.
     */
    @Child(name = "location", type = {Location.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The physical location of the item", formalDefinition="The location where the item is phisically located." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The location where the item is phisically located.)
     */
    protected Location locationTarget;

    /**
     * The patient that the item is associated with (implanted in, given to).
     */
    @Child(name = "subject", type = {Patient.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The patient that the item is affixed to", formalDefinition="The patient that the item is associated with (implanted in, given to)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient that the item is associated with (implanted in, given to).)
     */
    protected Patient subjectTarget;

    /**
     * The manufacture or preparation date and time. Times are necessary for several examples - for example biologically derived products, prepared or coumpounded medication, rapidly decaying isotopes.
     */
    @Child(name = "manufactureDate", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The manufacture or preparation date and time", formalDefinition="The manufacture or preparation date and time. Times are necessary for several examples - for example biologically derived products, prepared or coumpounded medication, rapidly decaying isotopes." )
    protected DateTimeType manufactureDate;

    /**
     * The expiry or preparation date and time.
     */
    @Child(name = "expiryDate", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The expiry or preparation date and time", formalDefinition="The expiry or preparation date and time." )
    protected DateTimeType expiryDate;

    /**
     * The Software version associated with the device, typically only used for devices with embedded software or firmware.
     */
    @Child(name = "currentSWVersion", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The Software version associated with the device", formalDefinition="The Software version associated with the device, typically only used for devices with embedded software or firmware." )
    protected StringType currentSWVersion;

    /**
     * The lot or batch number.
     */
    @Child(name = "lotNumber", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The lot or batch number", formalDefinition="The lot or batch number." )
    protected StringType lotNumber;

    /**
     * The serial number if available.
     */
    @Child(name = "serialNumber", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The serial number if available", formalDefinition="The serial number if available." )
    protected StringType serialNumber;

    /**
     * The machine-readable AIDC string in base64 encoding. Can correspond to the UDI pattern in devices.
     */
    @Child(name = "carrierAIDC", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The machine-readable AIDC string in base64 encoding", formalDefinition="The machine-readable AIDC string in base64 encoding. Can correspond to the UDI pattern in devices." )
    protected StringType carrierAIDC;

    /**
     * The human-readable barcode string. Can correspond to the UDI pattern in devices.
     */
    @Child(name = "carrierHRF", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The human-readable barcode string", formalDefinition="The human-readable barcode string. Can correspond to the UDI pattern in devices." )
    protected StringType carrierHRF;

    private static final long serialVersionUID = 1664070945L;

  /**
   * Constructor
   */
    public ItemInstance() {
      super();
    }

  /**
   * Constructor
   */
    public ItemInstance(IntegerType count) {
      super();
      this.count = count;
    }

    /**
     * @return {@link #count} (The quantity or amount of instances. For example if several units are being counted for inventory, this quantity can be more than one, provided they are not unique. Seriallized items are considered unique and as such would have a quantity max 1. This element is required and its presence asserts that the reource refers to a physical item.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
     */
    public IntegerType getCountElement() { 
      if (this.count == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.count");
        else if (Configuration.doAutoCreate())
          this.count = new IntegerType(); // bb
      return this.count;
    }

    public boolean hasCountElement() { 
      return this.count != null && !this.count.isEmpty();
    }

    public boolean hasCount() { 
      return this.count != null && !this.count.isEmpty();
    }

    /**
     * @param value {@link #count} (The quantity or amount of instances. For example if several units are being counted for inventory, this quantity can be more than one, provided they are not unique. Seriallized items are considered unique and as such would have a quantity max 1. This element is required and its presence asserts that the reource refers to a physical item.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
     */
    public ItemInstance setCountElement(IntegerType value) { 
      this.count = value;
      return this;
    }

    /**
     * @return The quantity or amount of instances. For example if several units are being counted for inventory, this quantity can be more than one, provided they are not unique. Seriallized items are considered unique and as such would have a quantity max 1. This element is required and its presence asserts that the reource refers to a physical item.
     */
    public int getCount() { 
      return this.count == null || this.count.isEmpty() ? 0 : this.count.getValue();
    }

    /**
     * @param value The quantity or amount of instances. For example if several units are being counted for inventory, this quantity can be more than one, provided they are not unique. Seriallized items are considered unique and as such would have a quantity max 1. This element is required and its presence asserts that the reource refers to a physical item.
     */
    public ItemInstance setCount(int value) { 
        if (this.count == null)
          this.count = new IntegerType();
        this.count.setValue(value);
      return this;
    }

    /**
     * @return {@link #location} (The location where the item is phisically located.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (The location where the item is phisically located.)
     */
    public ItemInstance setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The location where the item is phisically located.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location(); // aa
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The location where the item is phisically located.)
     */
    public ItemInstance setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #subject} (The patient that the item is associated with (implanted in, given to).)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient that the item is associated with (implanted in, given to).)
     */
    public ItemInstance setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient that the item is associated with (implanted in, given to).)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient(); // aa
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient that the item is associated with (implanted in, given to).)
     */
    public ItemInstance setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #manufactureDate} (The manufacture or preparation date and time. Times are necessary for several examples - for example biologically derived products, prepared or coumpounded medication, rapidly decaying isotopes.). This is the underlying object with id, value and extensions. The accessor "getManufactureDate" gives direct access to the value
     */
    public DateTimeType getManufactureDateElement() { 
      if (this.manufactureDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.manufactureDate");
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
     * @param value {@link #manufactureDate} (The manufacture or preparation date and time. Times are necessary for several examples - for example biologically derived products, prepared or coumpounded medication, rapidly decaying isotopes.). This is the underlying object with id, value and extensions. The accessor "getManufactureDate" gives direct access to the value
     */
    public ItemInstance setManufactureDateElement(DateTimeType value) { 
      this.manufactureDate = value;
      return this;
    }

    /**
     * @return The manufacture or preparation date and time. Times are necessary for several examples - for example biologically derived products, prepared or coumpounded medication, rapidly decaying isotopes.
     */
    public Date getManufactureDate() { 
      return this.manufactureDate == null ? null : this.manufactureDate.getValue();
    }

    /**
     * @param value The manufacture or preparation date and time. Times are necessary for several examples - for example biologically derived products, prepared or coumpounded medication, rapidly decaying isotopes.
     */
    public ItemInstance setManufactureDate(Date value) { 
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
     * @return {@link #expiryDate} (The expiry or preparation date and time.). This is the underlying object with id, value and extensions. The accessor "getExpiryDate" gives direct access to the value
     */
    public DateTimeType getExpiryDateElement() { 
      if (this.expiryDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.expiryDate");
        else if (Configuration.doAutoCreate())
          this.expiryDate = new DateTimeType(); // bb
      return this.expiryDate;
    }

    public boolean hasExpiryDateElement() { 
      return this.expiryDate != null && !this.expiryDate.isEmpty();
    }

    public boolean hasExpiryDate() { 
      return this.expiryDate != null && !this.expiryDate.isEmpty();
    }

    /**
     * @param value {@link #expiryDate} (The expiry or preparation date and time.). This is the underlying object with id, value and extensions. The accessor "getExpiryDate" gives direct access to the value
     */
    public ItemInstance setExpiryDateElement(DateTimeType value) { 
      this.expiryDate = value;
      return this;
    }

    /**
     * @return The expiry or preparation date and time.
     */
    public Date getExpiryDate() { 
      return this.expiryDate == null ? null : this.expiryDate.getValue();
    }

    /**
     * @param value The expiry or preparation date and time.
     */
    public ItemInstance setExpiryDate(Date value) { 
      if (value == null)
        this.expiryDate = null;
      else {
        if (this.expiryDate == null)
          this.expiryDate = new DateTimeType();
        this.expiryDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #currentSWVersion} (The Software version associated with the device, typically only used for devices with embedded software or firmware.). This is the underlying object with id, value and extensions. The accessor "getCurrentSWVersion" gives direct access to the value
     */
    public StringType getCurrentSWVersionElement() { 
      if (this.currentSWVersion == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.currentSWVersion");
        else if (Configuration.doAutoCreate())
          this.currentSWVersion = new StringType(); // bb
      return this.currentSWVersion;
    }

    public boolean hasCurrentSWVersionElement() { 
      return this.currentSWVersion != null && !this.currentSWVersion.isEmpty();
    }

    public boolean hasCurrentSWVersion() { 
      return this.currentSWVersion != null && !this.currentSWVersion.isEmpty();
    }

    /**
     * @param value {@link #currentSWVersion} (The Software version associated with the device, typically only used for devices with embedded software or firmware.). This is the underlying object with id, value and extensions. The accessor "getCurrentSWVersion" gives direct access to the value
     */
    public ItemInstance setCurrentSWVersionElement(StringType value) { 
      this.currentSWVersion = value;
      return this;
    }

    /**
     * @return The Software version associated with the device, typically only used for devices with embedded software or firmware.
     */
    public String getCurrentSWVersion() { 
      return this.currentSWVersion == null ? null : this.currentSWVersion.getValue();
    }

    /**
     * @param value The Software version associated with the device, typically only used for devices with embedded software or firmware.
     */
    public ItemInstance setCurrentSWVersion(String value) { 
      if (Utilities.noString(value))
        this.currentSWVersion = null;
      else {
        if (this.currentSWVersion == null)
          this.currentSWVersion = new StringType();
        this.currentSWVersion.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #lotNumber} (The lot or batch number.). This is the underlying object with id, value and extensions. The accessor "getLotNumber" gives direct access to the value
     */
    public StringType getLotNumberElement() { 
      if (this.lotNumber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.lotNumber");
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
     * @param value {@link #lotNumber} (The lot or batch number.). This is the underlying object with id, value and extensions. The accessor "getLotNumber" gives direct access to the value
     */
    public ItemInstance setLotNumberElement(StringType value) { 
      this.lotNumber = value;
      return this;
    }

    /**
     * @return The lot or batch number.
     */
    public String getLotNumber() { 
      return this.lotNumber == null ? null : this.lotNumber.getValue();
    }

    /**
     * @param value The lot or batch number.
     */
    public ItemInstance setLotNumber(String value) { 
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
     * @return {@link #serialNumber} (The serial number if available.). This is the underlying object with id, value and extensions. The accessor "getSerialNumber" gives direct access to the value
     */
    public StringType getSerialNumberElement() { 
      if (this.serialNumber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.serialNumber");
        else if (Configuration.doAutoCreate())
          this.serialNumber = new StringType(); // bb
      return this.serialNumber;
    }

    public boolean hasSerialNumberElement() { 
      return this.serialNumber != null && !this.serialNumber.isEmpty();
    }

    public boolean hasSerialNumber() { 
      return this.serialNumber != null && !this.serialNumber.isEmpty();
    }

    /**
     * @param value {@link #serialNumber} (The serial number if available.). This is the underlying object with id, value and extensions. The accessor "getSerialNumber" gives direct access to the value
     */
    public ItemInstance setSerialNumberElement(StringType value) { 
      this.serialNumber = value;
      return this;
    }

    /**
     * @return The serial number if available.
     */
    public String getSerialNumber() { 
      return this.serialNumber == null ? null : this.serialNumber.getValue();
    }

    /**
     * @param value The serial number if available.
     */
    public ItemInstance setSerialNumber(String value) { 
      if (Utilities.noString(value))
        this.serialNumber = null;
      else {
        if (this.serialNumber == null)
          this.serialNumber = new StringType();
        this.serialNumber.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #carrierAIDC} (The machine-readable AIDC string in base64 encoding. Can correspond to the UDI pattern in devices.). This is the underlying object with id, value and extensions. The accessor "getCarrierAIDC" gives direct access to the value
     */
    public StringType getCarrierAIDCElement() { 
      if (this.carrierAIDC == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.carrierAIDC");
        else if (Configuration.doAutoCreate())
          this.carrierAIDC = new StringType(); // bb
      return this.carrierAIDC;
    }

    public boolean hasCarrierAIDCElement() { 
      return this.carrierAIDC != null && !this.carrierAIDC.isEmpty();
    }

    public boolean hasCarrierAIDC() { 
      return this.carrierAIDC != null && !this.carrierAIDC.isEmpty();
    }

    /**
     * @param value {@link #carrierAIDC} (The machine-readable AIDC string in base64 encoding. Can correspond to the UDI pattern in devices.). This is the underlying object with id, value and extensions. The accessor "getCarrierAIDC" gives direct access to the value
     */
    public ItemInstance setCarrierAIDCElement(StringType value) { 
      this.carrierAIDC = value;
      return this;
    }

    /**
     * @return The machine-readable AIDC string in base64 encoding. Can correspond to the UDI pattern in devices.
     */
    public String getCarrierAIDC() { 
      return this.carrierAIDC == null ? null : this.carrierAIDC.getValue();
    }

    /**
     * @param value The machine-readable AIDC string in base64 encoding. Can correspond to the UDI pattern in devices.
     */
    public ItemInstance setCarrierAIDC(String value) { 
      if (Utilities.noString(value))
        this.carrierAIDC = null;
      else {
        if (this.carrierAIDC == null)
          this.carrierAIDC = new StringType();
        this.carrierAIDC.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #carrierHRF} (The human-readable barcode string. Can correspond to the UDI pattern in devices.). This is the underlying object with id, value and extensions. The accessor "getCarrierHRF" gives direct access to the value
     */
    public StringType getCarrierHRFElement() { 
      if (this.carrierHRF == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ItemInstance.carrierHRF");
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
     * @param value {@link #carrierHRF} (The human-readable barcode string. Can correspond to the UDI pattern in devices.). This is the underlying object with id, value and extensions. The accessor "getCarrierHRF" gives direct access to the value
     */
    public ItemInstance setCarrierHRFElement(StringType value) { 
      this.carrierHRF = value;
      return this;
    }

    /**
     * @return The human-readable barcode string. Can correspond to the UDI pattern in devices.
     */
    public String getCarrierHRF() { 
      return this.carrierHRF == null ? null : this.carrierHRF.getValue();
    }

    /**
     * @param value The human-readable barcode string. Can correspond to the UDI pattern in devices.
     */
    public ItemInstance setCarrierHRF(String value) { 
      if (Utilities.noString(value))
        this.carrierHRF = null;
      else {
        if (this.carrierHRF == null)
          this.carrierHRF = new StringType();
        this.carrierHRF.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("count", "integer", "The quantity or amount of instances. For example if several units are being counted for inventory, this quantity can be more than one, provided they are not unique. Seriallized items are considered unique and as such would have a quantity max 1. This element is required and its presence asserts that the reource refers to a physical item.", 0, 1, count));
        children.add(new Property("location", "Reference(Location)", "The location where the item is phisically located.", 0, 1, location));
        children.add(new Property("subject", "Reference(Patient)", "The patient that the item is associated with (implanted in, given to).", 0, 1, subject));
        children.add(new Property("manufactureDate", "dateTime", "The manufacture or preparation date and time. Times are necessary for several examples - for example biologically derived products, prepared or coumpounded medication, rapidly decaying isotopes.", 0, 1, manufactureDate));
        children.add(new Property("expiryDate", "dateTime", "The expiry or preparation date and time.", 0, 1, expiryDate));
        children.add(new Property("currentSWVersion", "string", "The Software version associated with the device, typically only used for devices with embedded software or firmware.", 0, 1, currentSWVersion));
        children.add(new Property("lotNumber", "string", "The lot or batch number.", 0, 1, lotNumber));
        children.add(new Property("serialNumber", "string", "The serial number if available.", 0, 1, serialNumber));
        children.add(new Property("carrierAIDC", "string", "The machine-readable AIDC string in base64 encoding. Can correspond to the UDI pattern in devices.", 0, 1, carrierAIDC));
        children.add(new Property("carrierHRF", "string", "The human-readable barcode string. Can correspond to the UDI pattern in devices.", 0, 1, carrierHRF));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 94851343: /*count*/  return new Property("count", "integer", "The quantity or amount of instances. For example if several units are being counted for inventory, this quantity can be more than one, provided they are not unique. Seriallized items are considered unique and as such would have a quantity max 1. This element is required and its presence asserts that the reource refers to a physical item.", 0, 1, count);
        case 1901043637: /*location*/  return new Property("location", "Reference(Location)", "The location where the item is phisically located.", 0, 1, location);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient)", "The patient that the item is associated with (implanted in, given to).", 0, 1, subject);
        case 416714767: /*manufactureDate*/  return new Property("manufactureDate", "dateTime", "The manufacture or preparation date and time. Times are necessary for several examples - for example biologically derived products, prepared or coumpounded medication, rapidly decaying isotopes.", 0, 1, manufactureDate);
        case -816738431: /*expiryDate*/  return new Property("expiryDate", "dateTime", "The expiry or preparation date and time.", 0, 1, expiryDate);
        case -1538044805: /*currentSWVersion*/  return new Property("currentSWVersion", "string", "The Software version associated with the device, typically only used for devices with embedded software or firmware.", 0, 1, currentSWVersion);
        case 462547450: /*lotNumber*/  return new Property("lotNumber", "string", "The lot or batch number.", 0, 1, lotNumber);
        case 83787357: /*serialNumber*/  return new Property("serialNumber", "string", "The serial number if available.", 0, 1, serialNumber);
        case -768521825: /*carrierAIDC*/  return new Property("carrierAIDC", "string", "The machine-readable AIDC string in base64 encoding. Can correspond to the UDI pattern in devices.", 0, 1, carrierAIDC);
        case 806499972: /*carrierHRF*/  return new Property("carrierHRF", "string", "The human-readable barcode string. Can correspond to the UDI pattern in devices.", 0, 1, carrierHRF);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 94851343: /*count*/ return this.count == null ? new Base[0] : new Base[] {this.count}; // IntegerType
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 416714767: /*manufactureDate*/ return this.manufactureDate == null ? new Base[0] : new Base[] {this.manufactureDate}; // DateTimeType
        case -816738431: /*expiryDate*/ return this.expiryDate == null ? new Base[0] : new Base[] {this.expiryDate}; // DateTimeType
        case -1538044805: /*currentSWVersion*/ return this.currentSWVersion == null ? new Base[0] : new Base[] {this.currentSWVersion}; // StringType
        case 462547450: /*lotNumber*/ return this.lotNumber == null ? new Base[0] : new Base[] {this.lotNumber}; // StringType
        case 83787357: /*serialNumber*/ return this.serialNumber == null ? new Base[0] : new Base[] {this.serialNumber}; // StringType
        case -768521825: /*carrierAIDC*/ return this.carrierAIDC == null ? new Base[0] : new Base[] {this.carrierAIDC}; // StringType
        case 806499972: /*carrierHRF*/ return this.carrierHRF == null ? new Base[0] : new Base[] {this.carrierHRF}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 94851343: // count
          this.count = castToInteger(value); // IntegerType
          return value;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 416714767: // manufactureDate
          this.manufactureDate = castToDateTime(value); // DateTimeType
          return value;
        case -816738431: // expiryDate
          this.expiryDate = castToDateTime(value); // DateTimeType
          return value;
        case -1538044805: // currentSWVersion
          this.currentSWVersion = castToString(value); // StringType
          return value;
        case 462547450: // lotNumber
          this.lotNumber = castToString(value); // StringType
          return value;
        case 83787357: // serialNumber
          this.serialNumber = castToString(value); // StringType
          return value;
        case -768521825: // carrierAIDC
          this.carrierAIDC = castToString(value); // StringType
          return value;
        case 806499972: // carrierHRF
          this.carrierHRF = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("count")) {
          this.count = castToInteger(value); // IntegerType
        } else if (name.equals("location")) {
          this.location = castToReference(value); // Reference
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("manufactureDate")) {
          this.manufactureDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("expiryDate")) {
          this.expiryDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("currentSWVersion")) {
          this.currentSWVersion = castToString(value); // StringType
        } else if (name.equals("lotNumber")) {
          this.lotNumber = castToString(value); // StringType
        } else if (name.equals("serialNumber")) {
          this.serialNumber = castToString(value); // StringType
        } else if (name.equals("carrierAIDC")) {
          this.carrierAIDC = castToString(value); // StringType
        } else if (name.equals("carrierHRF")) {
          this.carrierHRF = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 94851343:  return getCountElement();
        case 1901043637:  return getLocation(); 
        case -1867885268:  return getSubject(); 
        case 416714767:  return getManufactureDateElement();
        case -816738431:  return getExpiryDateElement();
        case -1538044805:  return getCurrentSWVersionElement();
        case 462547450:  return getLotNumberElement();
        case 83787357:  return getSerialNumberElement();
        case -768521825:  return getCarrierAIDCElement();
        case 806499972:  return getCarrierHRFElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 94851343: /*count*/ return new String[] {"integer"};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 416714767: /*manufactureDate*/ return new String[] {"dateTime"};
        case -816738431: /*expiryDate*/ return new String[] {"dateTime"};
        case -1538044805: /*currentSWVersion*/ return new String[] {"string"};
        case 462547450: /*lotNumber*/ return new String[] {"string"};
        case 83787357: /*serialNumber*/ return new String[] {"string"};
        case -768521825: /*carrierAIDC*/ return new String[] {"string"};
        case 806499972: /*carrierHRF*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("count")) {
          throw new FHIRException("Cannot call addChild on a primitive type ItemInstance.count");
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("manufactureDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ItemInstance.manufactureDate");
        }
        else if (name.equals("expiryDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ItemInstance.expiryDate");
        }
        else if (name.equals("currentSWVersion")) {
          throw new FHIRException("Cannot call addChild on a primitive type ItemInstance.currentSWVersion");
        }
        else if (name.equals("lotNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ItemInstance.lotNumber");
        }
        else if (name.equals("serialNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ItemInstance.serialNumber");
        }
        else if (name.equals("carrierAIDC")) {
          throw new FHIRException("Cannot call addChild on a primitive type ItemInstance.carrierAIDC");
        }
        else if (name.equals("carrierHRF")) {
          throw new FHIRException("Cannot call addChild on a primitive type ItemInstance.carrierHRF");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ItemInstance";

  }

      public ItemInstance copy() {
        ItemInstance dst = new ItemInstance();
        copyValues(dst);
        dst.count = count == null ? null : count.copy();
        dst.location = location == null ? null : location.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.manufactureDate = manufactureDate == null ? null : manufactureDate.copy();
        dst.expiryDate = expiryDate == null ? null : expiryDate.copy();
        dst.currentSWVersion = currentSWVersion == null ? null : currentSWVersion.copy();
        dst.lotNumber = lotNumber == null ? null : lotNumber.copy();
        dst.serialNumber = serialNumber == null ? null : serialNumber.copy();
        dst.carrierAIDC = carrierAIDC == null ? null : carrierAIDC.copy();
        dst.carrierHRF = carrierHRF == null ? null : carrierHRF.copy();
        return dst;
      }

      protected ItemInstance typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ItemInstance))
          return false;
        ItemInstance o = (ItemInstance) other_;
        return compareDeep(count, o.count, true) && compareDeep(location, o.location, true) && compareDeep(subject, o.subject, true)
           && compareDeep(manufactureDate, o.manufactureDate, true) && compareDeep(expiryDate, o.expiryDate, true)
           && compareDeep(currentSWVersion, o.currentSWVersion, true) && compareDeep(lotNumber, o.lotNumber, true)
           && compareDeep(serialNumber, o.serialNumber, true) && compareDeep(carrierAIDC, o.carrierAIDC, true)
           && compareDeep(carrierHRF, o.carrierHRF, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ItemInstance))
          return false;
        ItemInstance o = (ItemInstance) other_;
        return compareValues(count, o.count, true) && compareValues(manufactureDate, o.manufactureDate, true)
           && compareValues(expiryDate, o.expiryDate, true) && compareValues(currentSWVersion, o.currentSWVersion, true)
           && compareValues(lotNumber, o.lotNumber, true) && compareValues(serialNumber, o.serialNumber, true)
           && compareValues(carrierAIDC, o.carrierAIDC, true) && compareValues(carrierHRF, o.carrierHRF, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(count, location, subject
          , manufactureDate, expiryDate, currentSWVersion, lotNumber, serialNumber, carrierAIDC
          , carrierHRF);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ItemInstance;
   }

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The identifier of the patient who has devices assigned to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ItemInstance.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="ItemInstance.subject", description="The identifier of the patient who has devices assigned to", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The identifier of the patient who has devices assigned to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ItemInstance.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ItemInstance:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("ItemInstance:subject").toLocked();


}

