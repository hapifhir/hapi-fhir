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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * There is a variety of postal address formats defined around the world. This format defines a superset that is the basis for all addresses around the world.
 */
@DatatypeDef(name="Address")
public class Address extends Type implements ICompositeType {

    public enum AddressUse {
        /**
         * A communication address at a home.
         */
        HOME, 
        /**
         * An office address. First choice for business related contacts during business hours.
         */
        WORK, 
        /**
         * A temporary address. The period can provide more detailed information.
         */
        TEMP, 
        /**
         * This address is no longer in use (or was never correct, but retained for records).
         */
        OLD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AddressUse fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("home".equals(codeString))
          return HOME;
        if ("work".equals(codeString))
          return WORK;
        if ("temp".equals(codeString))
          return TEMP;
        if ("old".equals(codeString))
          return OLD;
        throw new Exception("Unknown AddressUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HOME: return "home";
            case WORK: return "work";
            case TEMP: return "temp";
            case OLD: return "old";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case HOME: return "http://hl7.org/fhir/address-use";
            case WORK: return "http://hl7.org/fhir/address-use";
            case TEMP: return "http://hl7.org/fhir/address-use";
            case OLD: return "http://hl7.org/fhir/address-use";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case HOME: return "A communication address at a home.";
            case WORK: return "An office address. First choice for business related contacts during business hours.";
            case TEMP: return "A temporary address. The period can provide more detailed information.";
            case OLD: return "This address is no longer in use (or was never correct, but retained for records).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HOME: return "Home";
            case WORK: return "Work";
            case TEMP: return "Temporary";
            case OLD: return "Old / Incorrect";
            default: return "?";
          }
        }
    }

  public static class AddressUseEnumFactory implements EnumFactory<AddressUse> {
    public AddressUse fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("home".equals(codeString))
          return AddressUse.HOME;
        if ("work".equals(codeString))
          return AddressUse.WORK;
        if ("temp".equals(codeString))
          return AddressUse.TEMP;
        if ("old".equals(codeString))
          return AddressUse.OLD;
        throw new IllegalArgumentException("Unknown AddressUse code '"+codeString+"'");
        }
    public String toCode(AddressUse code) {
      if (code == AddressUse.HOME)
        return "home";
      if (code == AddressUse.WORK)
        return "work";
      if (code == AddressUse.TEMP)
        return "temp";
      if (code == AddressUse.OLD)
        return "old";
      return "?";
      }
    }

    public enum AddressType {
        /**
         * Mailing addresses - PO Boxes and care-of addresses.
         */
        POSTAL, 
        /**
         * A physical address that can be visited.
         */
        PHYSICAL, 
        /**
         * An address that is both physical and postal.
         */
        BOTH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AddressType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("postal".equals(codeString))
          return POSTAL;
        if ("physical".equals(codeString))
          return PHYSICAL;
        if ("both".equals(codeString))
          return BOTH;
        throw new Exception("Unknown AddressType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case POSTAL: return "postal";
            case PHYSICAL: return "physical";
            case BOTH: return "both";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case POSTAL: return "http://hl7.org/fhir/address-type";
            case PHYSICAL: return "http://hl7.org/fhir/address-type";
            case BOTH: return "http://hl7.org/fhir/address-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case POSTAL: return "Mailing addresses - PO Boxes and care-of addresses.";
            case PHYSICAL: return "A physical address that can be visited.";
            case BOTH: return "An address that is both physical and postal.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case POSTAL: return "Postal";
            case PHYSICAL: return "Physical";
            case BOTH: return "Postal & Physical";
            default: return "?";
          }
        }
    }

  public static class AddressTypeEnumFactory implements EnumFactory<AddressType> {
    public AddressType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("postal".equals(codeString))
          return AddressType.POSTAL;
        if ("physical".equals(codeString))
          return AddressType.PHYSICAL;
        if ("both".equals(codeString))
          return AddressType.BOTH;
        throw new IllegalArgumentException("Unknown AddressType code '"+codeString+"'");
        }
    public String toCode(AddressType code) {
      if (code == AddressType.POSTAL)
        return "postal";
      if (code == AddressType.PHYSICAL)
        return "physical";
      if (code == AddressType.BOTH)
        return "both";
      return "?";
      }
    }

    /**
     * The purpose of this address.
     */
    @Child(name = "use", type = {CodeType.class}, order=0, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="home | work | temp | old - purpose of this address", formalDefinition="The purpose of this address." )
    protected Enumeration<AddressUse> use;

    /**
     * Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of addresses). Most addresses are both.
     */
    @Child(name = "type", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="postal | physical | both", formalDefinition="Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of addresses). Most addresses are both." )
    protected Enumeration<AddressType> type;

    /**
     * A full text representation of the address.
     */
    @Child(name = "text", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Text representation of the address", formalDefinition="A full text representation of the address." )
    protected StringType text;

    /**
     * This component contains the house number, apartment number, street name, street direction,  P.O. Box number, delivery hints, and similar address information.
     */
    @Child(name = "line", type = {StringType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Street name, number, direction & P.O. Box etc.", formalDefinition="This component contains the house number, apartment number, street name, street direction,  P.O. Box number, delivery hints, and similar address information." )
    protected List<StringType> line;

    /**
     * The name of the city, town, village or other community or delivery center.
     */
    @Child(name = "city", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of city, town etc.", formalDefinition="The name of the city, town, village or other community or delivery center." )
    protected StringType city;

    /**
     * The name of the administrative area (county).
     */
    @Child(name = "district", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="District name (aka county)", formalDefinition="The name of the administrative area (county)." )
    protected StringType district;

    /**
     * Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).
     */
    @Child(name = "state", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Sub-unit of country (abbreviations ok)", formalDefinition="Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes)." )
    protected StringType state;

    /**
     * A postal code designating a region defined by the postal service.
     */
    @Child(name = "postalCode", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Postal code for area", formalDefinition="A postal code designating a region defined by the postal service." )
    protected StringType postalCode;

    /**
     * Country - a nation as commonly understood or generally accepted.
     */
    @Child(name = "country", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Country (can be ISO 3166 3 letter code)", formalDefinition="Country - a nation as commonly understood or generally accepted." )
    protected StringType country;

    /**
     * Time period when address was/is in use.
     */
    @Child(name = "period", type = {Period.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time period when address was/is in use", formalDefinition="Time period when address was/is in use." )
    protected Period period;

    private static final long serialVersionUID = 561490318L;

  /*
   * Constructor
   */
    public Address() {
      super();
    }

    /**
     * @return {@link #use} (The purpose of this address.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Enumeration<AddressUse> getUseElement() { 
      if (this.use == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Address.use");
        else if (Configuration.doAutoCreate())
          this.use = new Enumeration<AddressUse>(new AddressUseEnumFactory()); // bb
      return this.use;
    }

    public boolean hasUseElement() { 
      return this.use != null && !this.use.isEmpty();
    }

    public boolean hasUse() { 
      return this.use != null && !this.use.isEmpty();
    }

    /**
     * @param value {@link #use} (The purpose of this address.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Address setUseElement(Enumeration<AddressUse> value) { 
      this.use = value;
      return this;
    }

    /**
     * @return The purpose of this address.
     */
    public AddressUse getUse() { 
      return this.use == null ? null : this.use.getValue();
    }

    /**
     * @param value The purpose of this address.
     */
    public Address setUse(AddressUse value) { 
      if (value == null)
        this.use = null;
      else {
        if (this.use == null)
          this.use = new Enumeration<AddressUse>(new AddressUseEnumFactory());
        this.use.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of addresses). Most addresses are both.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<AddressType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Address.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<AddressType>(new AddressTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of addresses). Most addresses are both.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Address setTypeElement(Enumeration<AddressType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of addresses). Most addresses are both.
     */
    public AddressType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of addresses). Most addresses are both.
     */
    public Address setType(AddressType value) { 
      if (value == null)
        this.type = null;
      else {
        if (this.type == null)
          this.type = new Enumeration<AddressType>(new AddressTypeEnumFactory());
        this.type.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #text} (A full text representation of the address.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
     */
    public StringType getTextElement() { 
      if (this.text == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Address.text");
        else if (Configuration.doAutoCreate())
          this.text = new StringType(); // bb
      return this.text;
    }

    public boolean hasTextElement() { 
      return this.text != null && !this.text.isEmpty();
    }

    public boolean hasText() { 
      return this.text != null && !this.text.isEmpty();
    }

    /**
     * @param value {@link #text} (A full text representation of the address.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
     */
    public Address setTextElement(StringType value) { 
      this.text = value;
      return this;
    }

    /**
     * @return A full text representation of the address.
     */
    public String getText() { 
      return this.text == null ? null : this.text.getValue();
    }

    /**
     * @param value A full text representation of the address.
     */
    public Address setText(String value) { 
      if (Utilities.noString(value))
        this.text = null;
      else {
        if (this.text == null)
          this.text = new StringType();
        this.text.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #line} (This component contains the house number, apartment number, street name, street direction,  P.O. Box number, delivery hints, and similar address information.)
     */
    public List<StringType> getLine() { 
      if (this.line == null)
        this.line = new ArrayList<StringType>();
      return this.line;
    }

    public boolean hasLine() { 
      if (this.line == null)
        return false;
      for (StringType item : this.line)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #line} (This component contains the house number, apartment number, street name, street direction,  P.O. Box number, delivery hints, and similar address information.)
     */
    // syntactic sugar
    public StringType addLineElement() {//2 
      StringType t = new StringType();
      if (this.line == null)
        this.line = new ArrayList<StringType>();
      this.line.add(t);
      return t;
    }

    /**
     * @param value {@link #line} (This component contains the house number, apartment number, street name, street direction,  P.O. Box number, delivery hints, and similar address information.)
     */
    public Address addLine(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.line == null)
        this.line = new ArrayList<StringType>();
      this.line.add(t);
      return this;
    }

    /**
     * @param value {@link #line} (This component contains the house number, apartment number, street name, street direction,  P.O. Box number, delivery hints, and similar address information.)
     */
    public boolean hasLine(String value) { 
      if (this.line == null)
        return false;
      for (StringType v : this.line)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #city} (The name of the city, town, village or other community or delivery center.). This is the underlying object with id, value and extensions. The accessor "getCity" gives direct access to the value
     */
    public StringType getCityElement() { 
      if (this.city == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Address.city");
        else if (Configuration.doAutoCreate())
          this.city = new StringType(); // bb
      return this.city;
    }

    public boolean hasCityElement() { 
      return this.city != null && !this.city.isEmpty();
    }

    public boolean hasCity() { 
      return this.city != null && !this.city.isEmpty();
    }

    /**
     * @param value {@link #city} (The name of the city, town, village or other community or delivery center.). This is the underlying object with id, value and extensions. The accessor "getCity" gives direct access to the value
     */
    public Address setCityElement(StringType value) { 
      this.city = value;
      return this;
    }

    /**
     * @return The name of the city, town, village or other community or delivery center.
     */
    public String getCity() { 
      return this.city == null ? null : this.city.getValue();
    }

    /**
     * @param value The name of the city, town, village or other community or delivery center.
     */
    public Address setCity(String value) { 
      if (Utilities.noString(value))
        this.city = null;
      else {
        if (this.city == null)
          this.city = new StringType();
        this.city.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #district} (The name of the administrative area (county).). This is the underlying object with id, value and extensions. The accessor "getDistrict" gives direct access to the value
     */
    public StringType getDistrictElement() { 
      if (this.district == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Address.district");
        else if (Configuration.doAutoCreate())
          this.district = new StringType(); // bb
      return this.district;
    }

    public boolean hasDistrictElement() { 
      return this.district != null && !this.district.isEmpty();
    }

    public boolean hasDistrict() { 
      return this.district != null && !this.district.isEmpty();
    }

    /**
     * @param value {@link #district} (The name of the administrative area (county).). This is the underlying object with id, value and extensions. The accessor "getDistrict" gives direct access to the value
     */
    public Address setDistrictElement(StringType value) { 
      this.district = value;
      return this;
    }

    /**
     * @return The name of the administrative area (county).
     */
    public String getDistrict() { 
      return this.district == null ? null : this.district.getValue();
    }

    /**
     * @param value The name of the administrative area (county).
     */
    public Address setDistrict(String value) { 
      if (Utilities.noString(value))
        this.district = null;
      else {
        if (this.district == null)
          this.district = new StringType();
        this.district.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #state} (Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).). This is the underlying object with id, value and extensions. The accessor "getState" gives direct access to the value
     */
    public StringType getStateElement() { 
      if (this.state == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Address.state");
        else if (Configuration.doAutoCreate())
          this.state = new StringType(); // bb
      return this.state;
    }

    public boolean hasStateElement() { 
      return this.state != null && !this.state.isEmpty();
    }

    public boolean hasState() { 
      return this.state != null && !this.state.isEmpty();
    }

    /**
     * @param value {@link #state} (Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).). This is the underlying object with id, value and extensions. The accessor "getState" gives direct access to the value
     */
    public Address setStateElement(StringType value) { 
      this.state = value;
      return this;
    }

    /**
     * @return Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).
     */
    public String getState() { 
      return this.state == null ? null : this.state.getValue();
    }

    /**
     * @param value Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).
     */
    public Address setState(String value) { 
      if (Utilities.noString(value))
        this.state = null;
      else {
        if (this.state == null)
          this.state = new StringType();
        this.state.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #postalCode} (A postal code designating a region defined by the postal service.). This is the underlying object with id, value and extensions. The accessor "getPostalCode" gives direct access to the value
     */
    public StringType getPostalCodeElement() { 
      if (this.postalCode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Address.postalCode");
        else if (Configuration.doAutoCreate())
          this.postalCode = new StringType(); // bb
      return this.postalCode;
    }

    public boolean hasPostalCodeElement() { 
      return this.postalCode != null && !this.postalCode.isEmpty();
    }

    public boolean hasPostalCode() { 
      return this.postalCode != null && !this.postalCode.isEmpty();
    }

    /**
     * @param value {@link #postalCode} (A postal code designating a region defined by the postal service.). This is the underlying object with id, value and extensions. The accessor "getPostalCode" gives direct access to the value
     */
    public Address setPostalCodeElement(StringType value) { 
      this.postalCode = value;
      return this;
    }

    /**
     * @return A postal code designating a region defined by the postal service.
     */
    public String getPostalCode() { 
      return this.postalCode == null ? null : this.postalCode.getValue();
    }

    /**
     * @param value A postal code designating a region defined by the postal service.
     */
    public Address setPostalCode(String value) { 
      if (Utilities.noString(value))
        this.postalCode = null;
      else {
        if (this.postalCode == null)
          this.postalCode = new StringType();
        this.postalCode.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #country} (Country - a nation as commonly understood or generally accepted.). This is the underlying object with id, value and extensions. The accessor "getCountry" gives direct access to the value
     */
    public StringType getCountryElement() { 
      if (this.country == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Address.country");
        else if (Configuration.doAutoCreate())
          this.country = new StringType(); // bb
      return this.country;
    }

    public boolean hasCountryElement() { 
      return this.country != null && !this.country.isEmpty();
    }

    public boolean hasCountry() { 
      return this.country != null && !this.country.isEmpty();
    }

    /**
     * @param value {@link #country} (Country - a nation as commonly understood or generally accepted.). This is the underlying object with id, value and extensions. The accessor "getCountry" gives direct access to the value
     */
    public Address setCountryElement(StringType value) { 
      this.country = value;
      return this;
    }

    /**
     * @return Country - a nation as commonly understood or generally accepted.
     */
    public String getCountry() { 
      return this.country == null ? null : this.country.getValue();
    }

    /**
     * @param value Country - a nation as commonly understood or generally accepted.
     */
    public Address setCountry(String value) { 
      if (Utilities.noString(value))
        this.country = null;
      else {
        if (this.country == null)
          this.country = new StringType();
        this.country.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #period} (Time period when address was/is in use.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Address.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Time period when address was/is in use.)
     */
    public Address setPeriod(Period value) { 
      this.period = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("use", "code", "The purpose of this address.", 0, java.lang.Integer.MAX_VALUE, use));
        childrenList.add(new Property("type", "code", "Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of addresses). Most addresses are both.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("text", "string", "A full text representation of the address.", 0, java.lang.Integer.MAX_VALUE, text));
        childrenList.add(new Property("line", "string", "This component contains the house number, apartment number, street name, street direction,  P.O. Box number, delivery hints, and similar address information.", 0, java.lang.Integer.MAX_VALUE, line));
        childrenList.add(new Property("city", "string", "The name of the city, town, village or other community or delivery center.", 0, java.lang.Integer.MAX_VALUE, city));
        childrenList.add(new Property("district", "string", "The name of the administrative area (county).", 0, java.lang.Integer.MAX_VALUE, district));
        childrenList.add(new Property("state", "string", "Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).", 0, java.lang.Integer.MAX_VALUE, state));
        childrenList.add(new Property("postalCode", "string", "A postal code designating a region defined by the postal service.", 0, java.lang.Integer.MAX_VALUE, postalCode));
        childrenList.add(new Property("country", "string", "Country - a nation as commonly understood or generally accepted.", 0, java.lang.Integer.MAX_VALUE, country));
        childrenList.add(new Property("period", "Period", "Time period when address was/is in use.", 0, java.lang.Integer.MAX_VALUE, period));
      }

      public Address copy() {
        Address dst = new Address();
        copyValues(dst);
        dst.use = use == null ? null : use.copy();
        dst.type = type == null ? null : type.copy();
        dst.text = text == null ? null : text.copy();
        if (line != null) {
          dst.line = new ArrayList<StringType>();
          for (StringType i : line)
            dst.line.add(i.copy());
        };
        dst.city = city == null ? null : city.copy();
        dst.district = district == null ? null : district.copy();
        dst.state = state == null ? null : state.copy();
        dst.postalCode = postalCode == null ? null : postalCode.copy();
        dst.country = country == null ? null : country.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      protected Address typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Address))
          return false;
        Address o = (Address) other;
        return compareDeep(use, o.use, true) && compareDeep(type, o.type, true) && compareDeep(text, o.text, true)
           && compareDeep(line, o.line, true) && compareDeep(city, o.city, true) && compareDeep(district, o.district, true)
           && compareDeep(state, o.state, true) && compareDeep(postalCode, o.postalCode, true) && compareDeep(country, o.country, true)
           && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Address))
          return false;
        Address o = (Address) other;
        return compareValues(use, o.use, true) && compareValues(type, o.type, true) && compareValues(text, o.text, true)
           && compareValues(line, o.line, true) && compareValues(city, o.city, true) && compareValues(district, o.district, true)
           && compareValues(state, o.state, true) && compareValues(postalCode, o.postalCode, true) && compareValues(country, o.country, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (use == null || use.isEmpty()) && (type == null || type.isEmpty())
           && (text == null || text.isEmpty()) && (line == null || line.isEmpty()) && (city == null || city.isEmpty())
           && (district == null || district.isEmpty()) && (state == null || state.isEmpty()) && (postalCode == null || postalCode.isEmpty())
           && (country == null || country.isEmpty()) && (period == null || period.isEmpty());
      }


}

