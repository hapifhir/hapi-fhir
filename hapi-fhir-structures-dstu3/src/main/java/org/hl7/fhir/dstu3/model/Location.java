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

import java.math.*;
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
 * Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated.
 */
@ResourceDef(name="Location", profile="http://hl7.org/fhir/Profile/Location")
public class Location extends DomainResource {

    public enum LocationStatus {
        /**
         * The location is operational.
         */
        ACTIVE, 
        /**
         * The location is temporarily closed.
         */
        SUSPENDED, 
        /**
         * The location is no longer used.
         */
        INACTIVE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static LocationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown LocationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case INACTIVE: return "inactive";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/location-status";
            case SUSPENDED: return "http://hl7.org/fhir/location-status";
            case INACTIVE: return "http://hl7.org/fhir/location-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The location is operational.";
            case SUSPENDED: return "The location is temporarily closed.";
            case INACTIVE: return "The location is no longer used.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case INACTIVE: return "Inactive";
            default: return "?";
          }
        }
    }

  public static class LocationStatusEnumFactory implements EnumFactory<LocationStatus> {
    public LocationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return LocationStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return LocationStatus.SUSPENDED;
        if ("inactive".equals(codeString))
          return LocationStatus.INACTIVE;
        throw new IllegalArgumentException("Unknown LocationStatus code '"+codeString+"'");
        }
        public Enumeration<LocationStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<LocationStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<LocationStatus>(this, LocationStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<LocationStatus>(this, LocationStatus.SUSPENDED);
        if ("inactive".equals(codeString))
          return new Enumeration<LocationStatus>(this, LocationStatus.INACTIVE);
        throw new FHIRException("Unknown LocationStatus code '"+codeString+"'");
        }
    public String toCode(LocationStatus code) {
      if (code == LocationStatus.ACTIVE)
        return "active";
      if (code == LocationStatus.SUSPENDED)
        return "suspended";
      if (code == LocationStatus.INACTIVE)
        return "inactive";
      return "?";
      }
    public String toSystem(LocationStatus code) {
      return code.getSystem();
      }
    }

    public enum LocationMode {
        /**
         * The Location resource represents a specific instance of a location (e.g. Operating Theatre 1A).
         */
        INSTANCE, 
        /**
         * The Location represents a class of locations (e.g. Any Operating Theatre) although this class of locations could be constrained within a specific boundary (such as organization, or parent location, address etc.).
         */
        KIND, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static LocationMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return INSTANCE;
        if ("kind".equals(codeString))
          return KIND;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown LocationMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTANCE: return "instance";
            case KIND: return "kind";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INSTANCE: return "http://hl7.org/fhir/location-mode";
            case KIND: return "http://hl7.org/fhir/location-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INSTANCE: return "The Location resource represents a specific instance of a location (e.g. Operating Theatre 1A).";
            case KIND: return "The Location represents a class of locations (e.g. Any Operating Theatre) although this class of locations could be constrained within a specific boundary (such as organization, or parent location, address etc.).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTANCE: return "Instance";
            case KIND: return "Kind";
            default: return "?";
          }
        }
    }

  public static class LocationModeEnumFactory implements EnumFactory<LocationMode> {
    public LocationMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return LocationMode.INSTANCE;
        if ("kind".equals(codeString))
          return LocationMode.KIND;
        throw new IllegalArgumentException("Unknown LocationMode code '"+codeString+"'");
        }
        public Enumeration<LocationMode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<LocationMode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("instance".equals(codeString))
          return new Enumeration<LocationMode>(this, LocationMode.INSTANCE);
        if ("kind".equals(codeString))
          return new Enumeration<LocationMode>(this, LocationMode.KIND);
        throw new FHIRException("Unknown LocationMode code '"+codeString+"'");
        }
    public String toCode(LocationMode code) {
      if (code == LocationMode.INSTANCE)
        return "instance";
      if (code == LocationMode.KIND)
        return "kind";
      return "?";
      }
    public String toSystem(LocationMode code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class LocationPositionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below).
         */
        @Child(name = "longitude", type = {DecimalType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Longitude with WGS84 datum", formalDefinition="Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)." )
        protected DecimalType longitude;

        /**
         * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below).
         */
        @Child(name = "latitude", type = {DecimalType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Latitude with WGS84 datum", formalDefinition="Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)." )
        protected DecimalType latitude;

        /**
         * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below).
         */
        @Child(name = "altitude", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Altitude with WGS84 datum", formalDefinition="Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)." )
        protected DecimalType altitude;

        private static final long serialVersionUID = -74276134L;

    /**
     * Constructor
     */
      public LocationPositionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public LocationPositionComponent(DecimalType longitude, DecimalType latitude) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
      }

        /**
         * @return {@link #longitude} (Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below).). This is the underlying object with id, value and extensions. The accessor "getLongitude" gives direct access to the value
         */
        public DecimalType getLongitudeElement() { 
          if (this.longitude == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LocationPositionComponent.longitude");
            else if (Configuration.doAutoCreate())
              this.longitude = new DecimalType(); // bb
          return this.longitude;
        }

        public boolean hasLongitudeElement() { 
          return this.longitude != null && !this.longitude.isEmpty();
        }

        public boolean hasLongitude() { 
          return this.longitude != null && !this.longitude.isEmpty();
        }

        /**
         * @param value {@link #longitude} (Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below).). This is the underlying object with id, value and extensions. The accessor "getLongitude" gives direct access to the value
         */
        public LocationPositionComponent setLongitudeElement(DecimalType value) { 
          this.longitude = value;
          return this;
        }

        /**
         * @return Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below).
         */
        public BigDecimal getLongitude() { 
          return this.longitude == null ? null : this.longitude.getValue();
        }

        /**
         * @param value Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below).
         */
        public LocationPositionComponent setLongitude(BigDecimal value) { 
            if (this.longitude == null)
              this.longitude = new DecimalType();
            this.longitude.setValue(value);
          return this;
        }

        /**
         * @param value Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below).
         */
        public LocationPositionComponent setLongitude(long value) { 
              this.longitude = new DecimalType();
            this.longitude.setValue(value);
          return this;
        }

        /**
         * @param value Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below).
         */
        public LocationPositionComponent setLongitude(double value) { 
              this.longitude = new DecimalType();
            this.longitude.setValue(value);
          return this;
        }

        /**
         * @return {@link #latitude} (Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below).). This is the underlying object with id, value and extensions. The accessor "getLatitude" gives direct access to the value
         */
        public DecimalType getLatitudeElement() { 
          if (this.latitude == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LocationPositionComponent.latitude");
            else if (Configuration.doAutoCreate())
              this.latitude = new DecimalType(); // bb
          return this.latitude;
        }

        public boolean hasLatitudeElement() { 
          return this.latitude != null && !this.latitude.isEmpty();
        }

        public boolean hasLatitude() { 
          return this.latitude != null && !this.latitude.isEmpty();
        }

        /**
         * @param value {@link #latitude} (Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below).). This is the underlying object with id, value and extensions. The accessor "getLatitude" gives direct access to the value
         */
        public LocationPositionComponent setLatitudeElement(DecimalType value) { 
          this.latitude = value;
          return this;
        }

        /**
         * @return Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below).
         */
        public BigDecimal getLatitude() { 
          return this.latitude == null ? null : this.latitude.getValue();
        }

        /**
         * @param value Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below).
         */
        public LocationPositionComponent setLatitude(BigDecimal value) { 
            if (this.latitude == null)
              this.latitude = new DecimalType();
            this.latitude.setValue(value);
          return this;
        }

        /**
         * @param value Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below).
         */
        public LocationPositionComponent setLatitude(long value) { 
              this.latitude = new DecimalType();
            this.latitude.setValue(value);
          return this;
        }

        /**
         * @param value Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below).
         */
        public LocationPositionComponent setLatitude(double value) { 
              this.latitude = new DecimalType();
            this.latitude.setValue(value);
          return this;
        }

        /**
         * @return {@link #altitude} (Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below).). This is the underlying object with id, value and extensions. The accessor "getAltitude" gives direct access to the value
         */
        public DecimalType getAltitudeElement() { 
          if (this.altitude == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LocationPositionComponent.altitude");
            else if (Configuration.doAutoCreate())
              this.altitude = new DecimalType(); // bb
          return this.altitude;
        }

        public boolean hasAltitudeElement() { 
          return this.altitude != null && !this.altitude.isEmpty();
        }

        public boolean hasAltitude() { 
          return this.altitude != null && !this.altitude.isEmpty();
        }

        /**
         * @param value {@link #altitude} (Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below).). This is the underlying object with id, value and extensions. The accessor "getAltitude" gives direct access to the value
         */
        public LocationPositionComponent setAltitudeElement(DecimalType value) { 
          this.altitude = value;
          return this;
        }

        /**
         * @return Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below).
         */
        public BigDecimal getAltitude() { 
          return this.altitude == null ? null : this.altitude.getValue();
        }

        /**
         * @param value Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below).
         */
        public LocationPositionComponent setAltitude(BigDecimal value) { 
          if (value == null)
            this.altitude = null;
          else {
            if (this.altitude == null)
              this.altitude = new DecimalType();
            this.altitude.setValue(value);
          }
          return this;
        }

        /**
         * @param value Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below).
         */
        public LocationPositionComponent setAltitude(long value) { 
              this.altitude = new DecimalType();
            this.altitude.setValue(value);
          return this;
        }

        /**
         * @param value Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below).
         */
        public LocationPositionComponent setAltitude(double value) { 
              this.altitude = new DecimalType();
            this.altitude.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("longitude", "decimal", "Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below).", 0, java.lang.Integer.MAX_VALUE, longitude));
          childrenList.add(new Property("latitude", "decimal", "Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below).", 0, java.lang.Integer.MAX_VALUE, latitude));
          childrenList.add(new Property("altitude", "decimal", "Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below).", 0, java.lang.Integer.MAX_VALUE, altitude));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 137365935: /*longitude*/ return this.longitude == null ? new Base[0] : new Base[] {this.longitude}; // DecimalType
        case -1439978388: /*latitude*/ return this.latitude == null ? new Base[0] : new Base[] {this.latitude}; // DecimalType
        case 2036550306: /*altitude*/ return this.altitude == null ? new Base[0] : new Base[] {this.altitude}; // DecimalType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 137365935: // longitude
          this.longitude = castToDecimal(value); // DecimalType
          return value;
        case -1439978388: // latitude
          this.latitude = castToDecimal(value); // DecimalType
          return value;
        case 2036550306: // altitude
          this.altitude = castToDecimal(value); // DecimalType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("longitude")) {
          this.longitude = castToDecimal(value); // DecimalType
        } else if (name.equals("latitude")) {
          this.latitude = castToDecimal(value); // DecimalType
        } else if (name.equals("altitude")) {
          this.altitude = castToDecimal(value); // DecimalType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 137365935:  return getLongitudeElement();
        case -1439978388:  return getLatitudeElement();
        case 2036550306:  return getAltitudeElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 137365935: /*longitude*/ return new String[] {"decimal"};
        case -1439978388: /*latitude*/ return new String[] {"decimal"};
        case 2036550306: /*altitude*/ return new String[] {"decimal"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("longitude")) {
          throw new FHIRException("Cannot call addChild on a primitive type Location.longitude");
        }
        else if (name.equals("latitude")) {
          throw new FHIRException("Cannot call addChild on a primitive type Location.latitude");
        }
        else if (name.equals("altitude")) {
          throw new FHIRException("Cannot call addChild on a primitive type Location.altitude");
        }
        else
          return super.addChild(name);
      }

      public LocationPositionComponent copy() {
        LocationPositionComponent dst = new LocationPositionComponent();
        copyValues(dst);
        dst.longitude = longitude == null ? null : longitude.copy();
        dst.latitude = latitude == null ? null : latitude.copy();
        dst.altitude = altitude == null ? null : altitude.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof LocationPositionComponent))
          return false;
        LocationPositionComponent o = (LocationPositionComponent) other;
        return compareDeep(longitude, o.longitude, true) && compareDeep(latitude, o.latitude, true) && compareDeep(altitude, o.altitude, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof LocationPositionComponent))
          return false;
        LocationPositionComponent o = (LocationPositionComponent) other;
        return compareValues(longitude, o.longitude, true) && compareValues(latitude, o.latitude, true) && compareValues(altitude, o.altitude, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(longitude, latitude, altitude
          );
      }

  public String fhirType() {
    return "Location.position";

  }

  }

    /**
     * Unique code or number identifying the location to its users.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique code or number identifying the location to its users", formalDefinition="Unique code or number identifying the location to its users." )
    protected List<Identifier> identifier;

    /**
     * The status property covers the general availability of the resource, not the current value which may be covered by the operationStatus, or by a schedule/slots if they are configured for the location.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | suspended | inactive", formalDefinition="The status property covers the general availability of the resource, not the current value which may be covered by the operationStatus, or by a schedule/slots if they are configured for the location." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/location-status")
    protected Enumeration<LocationStatus> status;

    /**
     * The Operational status covers operation values most relevant to beds (but can also apply to rooms/units/chair/etc such as an isolation unit/dialisys chair). This typically covers concepts such as contamination, housekeeping and other activities like maintenance.
     */
    @Child(name = "operationalStatus", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The Operational status of the location (typically only for a bed/room)", formalDefinition="The Operational status covers operation values most relevant to beds (but can also apply to rooms/units/chair/etc such as an isolation unit/dialisys chair). This typically covers concepts such as contamination, housekeeping and other activities like maintenance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v2-0116")
    protected Coding operationalStatus;

    /**
     * Name of the location as used by humans. Does not need to be unique.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the location as used by humans", formalDefinition="Name of the location as used by humans. Does not need to be unique." )
    protected StringType name;

    /**
     * A list of alternate names that the location is known as, or was known as in the past.
     */
    @Child(name = "alias", type = {StringType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A list of alternate names that the location is known as, or was known as in the past", formalDefinition="A list of alternate names that the location is known as, or was known as in the past." )
    protected List<StringType> alias;

    /**
     * Description of the Location, which helps in finding or referencing the place.
     */
    @Child(name = "description", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Additional details about the location that could be displayed as further information to identify the location beyond its name", formalDefinition="Description of the Location, which helps in finding or referencing the place." )
    protected StringType description;

    /**
     * Indicates whether a resource instance represents a specific location or a class of locations.
     */
    @Child(name = "mode", type = {CodeType.class}, order=6, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="instance | kind", formalDefinition="Indicates whether a resource instance represents a specific location or a class of locations." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/location-mode")
    protected Enumeration<LocationMode> mode;

    /**
     * Indicates the type of function performed at the location.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type of function performed", formalDefinition="Indicates the type of function performed at the location." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ServiceDeliveryLocationRoleType")
    protected CodeableConcept type;

    /**
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contact details of the location", formalDefinition="The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites." )
    protected List<ContactPoint> telecom;

    /**
     * Physical location.
     */
    @Child(name = "address", type = {Address.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Physical location", formalDefinition="Physical location." )
    protected Address address;

    /**
     * Physical form of the location, e.g. building, room, vehicle, road.
     */
    @Child(name = "physicalType", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Physical form of the location", formalDefinition="Physical form of the location, e.g. building, room, vehicle, road." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/location-physical-type")
    protected CodeableConcept physicalType;

    /**
     * The absolute geographic location of the Location, expressed using the WGS84 datum (This is the same co-ordinate system used in KML).
     */
    @Child(name = "position", type = {}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The absolute geographic location", formalDefinition="The absolute geographic location of the Location, expressed using the WGS84 datum (This is the same co-ordinate system used in KML)." )
    protected LocationPositionComponent position;

    /**
     * The organization responsible for the provisioning and upkeep of the location.
     */
    @Child(name = "managingOrganization", type = {Organization.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization responsible for provisioning and upkeep", formalDefinition="The organization responsible for the provisioning and upkeep of the location." )
    protected Reference managingOrganization;

    /**
     * The actual object that is the target of the reference (The organization responsible for the provisioning and upkeep of the location.)
     */
    protected Organization managingOrganizationTarget;

    /**
     * Another Location which this Location is physically part of.
     */
    @Child(name = "partOf", type = {Location.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Another Location this one is physically part of", formalDefinition="Another Location which this Location is physically part of." )
    protected Reference partOf;

    /**
     * The actual object that is the target of the reference (Another Location which this Location is physically part of.)
     */
    protected Location partOfTarget;

    /**
     * Technical endpoints providing access to services operated for the location.
     */
    @Child(name = "endpoint", type = {Endpoint.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Technical endpoints providing access to services operated for the location", formalDefinition="Technical endpoints providing access to services operated for the location." )
    protected List<Reference> endpoint;
    /**
     * The actual objects that are the target of the reference (Technical endpoints providing access to services operated for the location.)
     */
    protected List<Endpoint> endpointTarget;


    private static final long serialVersionUID = -1603579027L;

  /**
   * Constructor
   */
    public Location() {
      super();
    }

    /**
     * @return {@link #identifier} (Unique code or number identifying the location to its users.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Location setIdentifier(List<Identifier> theIdentifier) { 
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

    public Location addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (The status property covers the general availability of the resource, not the current value which may be covered by the operationStatus, or by a schedule/slots if they are configured for the location.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<LocationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<LocationStatus>(new LocationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status property covers the general availability of the resource, not the current value which may be covered by the operationStatus, or by a schedule/slots if they are configured for the location.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Location setStatusElement(Enumeration<LocationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status property covers the general availability of the resource, not the current value which may be covered by the operationStatus, or by a schedule/slots if they are configured for the location.
     */
    public LocationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status property covers the general availability of the resource, not the current value which may be covered by the operationStatus, or by a schedule/slots if they are configured for the location.
     */
    public Location setStatus(LocationStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<LocationStatus>(new LocationStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #operationalStatus} (The Operational status covers operation values most relevant to beds (but can also apply to rooms/units/chair/etc such as an isolation unit/dialisys chair). This typically covers concepts such as contamination, housekeeping and other activities like maintenance.)
     */
    public Coding getOperationalStatus() { 
      if (this.operationalStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.operationalStatus");
        else if (Configuration.doAutoCreate())
          this.operationalStatus = new Coding(); // cc
      return this.operationalStatus;
    }

    public boolean hasOperationalStatus() { 
      return this.operationalStatus != null && !this.operationalStatus.isEmpty();
    }

    /**
     * @param value {@link #operationalStatus} (The Operational status covers operation values most relevant to beds (but can also apply to rooms/units/chair/etc such as an isolation unit/dialisys chair). This typically covers concepts such as contamination, housekeeping and other activities like maintenance.)
     */
    public Location setOperationalStatus(Coding value) { 
      this.operationalStatus = value;
      return this;
    }

    /**
     * @return {@link #name} (Name of the location as used by humans. Does not need to be unique.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.name");
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
     * @param value {@link #name} (Name of the location as used by humans. Does not need to be unique.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Location setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return Name of the location as used by humans. Does not need to be unique.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value Name of the location as used by humans. Does not need to be unique.
     */
    public Location setName(String value) { 
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
     * @return {@link #alias} (A list of alternate names that the location is known as, or was known as in the past.)
     */
    public List<StringType> getAlias() { 
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      return this.alias;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Location setAlias(List<StringType> theAlias) { 
      this.alias = theAlias;
      return this;
    }

    public boolean hasAlias() { 
      if (this.alias == null)
        return false;
      for (StringType item : this.alias)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #alias} (A list of alternate names that the location is known as, or was known as in the past.)
     */
    public StringType addAliasElement() {//2 
      StringType t = new StringType();
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      this.alias.add(t);
      return t;
    }

    /**
     * @param value {@link #alias} (A list of alternate names that the location is known as, or was known as in the past.)
     */
    public Location addAlias(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      this.alias.add(t);
      return this;
    }

    /**
     * @param value {@link #alias} (A list of alternate names that the location is known as, or was known as in the past.)
     */
    public boolean hasAlias(String value) { 
      if (this.alias == null)
        return false;
      for (StringType v : this.alias)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #description} (Description of the Location, which helps in finding or referencing the place.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (Description of the Location, which helps in finding or referencing the place.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Location setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Description of the Location, which helps in finding or referencing the place.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Description of the Location, which helps in finding or referencing the place.
     */
    public Location setDescription(String value) { 
      if (Utilities.noString(value))
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #mode} (Indicates whether a resource instance represents a specific location or a class of locations.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
     */
    public Enumeration<LocationMode> getModeElement() { 
      if (this.mode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.mode");
        else if (Configuration.doAutoCreate())
          this.mode = new Enumeration<LocationMode>(new LocationModeEnumFactory()); // bb
      return this.mode;
    }

    public boolean hasModeElement() { 
      return this.mode != null && !this.mode.isEmpty();
    }

    public boolean hasMode() { 
      return this.mode != null && !this.mode.isEmpty();
    }

    /**
     * @param value {@link #mode} (Indicates whether a resource instance represents a specific location or a class of locations.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
     */
    public Location setModeElement(Enumeration<LocationMode> value) { 
      this.mode = value;
      return this;
    }

    /**
     * @return Indicates whether a resource instance represents a specific location or a class of locations.
     */
    public LocationMode getMode() { 
      return this.mode == null ? null : this.mode.getValue();
    }

    /**
     * @param value Indicates whether a resource instance represents a specific location or a class of locations.
     */
    public Location setMode(LocationMode value) { 
      if (value == null)
        this.mode = null;
      else {
        if (this.mode == null)
          this.mode = new Enumeration<LocationMode>(new LocationModeEnumFactory());
        this.mode.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (Indicates the type of function performed at the location.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Indicates the type of function performed at the location.)
     */
    public Location setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #telecom} (The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites.)
     */
    public List<ContactPoint> getTelecom() { 
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      return this.telecom;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Location setTelecom(List<ContactPoint> theTelecom) { 
      this.telecom = theTelecom;
      return this;
    }

    public boolean hasTelecom() { 
      if (this.telecom == null)
        return false;
      for (ContactPoint item : this.telecom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    public Location addTelecom(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #telecom}, creating it if it does not already exist
     */
    public ContactPoint getTelecomFirstRep() { 
      if (getTelecom().isEmpty()) {
        addTelecom();
      }
      return getTelecom().get(0);
    }

    /**
     * @return {@link #address} (Physical location.)
     */
    public Address getAddress() { 
      if (this.address == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.address");
        else if (Configuration.doAutoCreate())
          this.address = new Address(); // cc
      return this.address;
    }

    public boolean hasAddress() { 
      return this.address != null && !this.address.isEmpty();
    }

    /**
     * @param value {@link #address} (Physical location.)
     */
    public Location setAddress(Address value) { 
      this.address = value;
      return this;
    }

    /**
     * @return {@link #physicalType} (Physical form of the location, e.g. building, room, vehicle, road.)
     */
    public CodeableConcept getPhysicalType() { 
      if (this.physicalType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.physicalType");
        else if (Configuration.doAutoCreate())
          this.physicalType = new CodeableConcept(); // cc
      return this.physicalType;
    }

    public boolean hasPhysicalType() { 
      return this.physicalType != null && !this.physicalType.isEmpty();
    }

    /**
     * @param value {@link #physicalType} (Physical form of the location, e.g. building, room, vehicle, road.)
     */
    public Location setPhysicalType(CodeableConcept value) { 
      this.physicalType = value;
      return this;
    }

    /**
     * @return {@link #position} (The absolute geographic location of the Location, expressed using the WGS84 datum (This is the same co-ordinate system used in KML).)
     */
    public LocationPositionComponent getPosition() { 
      if (this.position == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.position");
        else if (Configuration.doAutoCreate())
          this.position = new LocationPositionComponent(); // cc
      return this.position;
    }

    public boolean hasPosition() { 
      return this.position != null && !this.position.isEmpty();
    }

    /**
     * @param value {@link #position} (The absolute geographic location of the Location, expressed using the WGS84 datum (This is the same co-ordinate system used in KML).)
     */
    public Location setPosition(LocationPositionComponent value) { 
      this.position = value;
      return this;
    }

    /**
     * @return {@link #managingOrganization} (The organization responsible for the provisioning and upkeep of the location.)
     */
    public Reference getManagingOrganization() { 
      if (this.managingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganization = new Reference(); // cc
      return this.managingOrganization;
    }

    public boolean hasManagingOrganization() { 
      return this.managingOrganization != null && !this.managingOrganization.isEmpty();
    }

    /**
     * @param value {@link #managingOrganization} (The organization responsible for the provisioning and upkeep of the location.)
     */
    public Location setManagingOrganization(Reference value) { 
      this.managingOrganization = value;
      return this;
    }

    /**
     * @return {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization responsible for the provisioning and upkeep of the location.)
     */
    public Organization getManagingOrganizationTarget() { 
      if (this.managingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganizationTarget = new Organization(); // aa
      return this.managingOrganizationTarget;
    }

    /**
     * @param value {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization responsible for the provisioning and upkeep of the location.)
     */
    public Location setManagingOrganizationTarget(Organization value) { 
      this.managingOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #partOf} (Another Location which this Location is physically part of.)
     */
    public Reference getPartOf() { 
      if (this.partOf == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.partOf");
        else if (Configuration.doAutoCreate())
          this.partOf = new Reference(); // cc
      return this.partOf;
    }

    public boolean hasPartOf() { 
      return this.partOf != null && !this.partOf.isEmpty();
    }

    /**
     * @param value {@link #partOf} (Another Location which this Location is physically part of.)
     */
    public Location setPartOf(Reference value) { 
      this.partOf = value;
      return this;
    }

    /**
     * @return {@link #partOf} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Another Location which this Location is physically part of.)
     */
    public Location getPartOfTarget() { 
      if (this.partOfTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Location.partOf");
        else if (Configuration.doAutoCreate())
          this.partOfTarget = new Location(); // aa
      return this.partOfTarget;
    }

    /**
     * @param value {@link #partOf} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Another Location which this Location is physically part of.)
     */
    public Location setPartOfTarget(Location value) { 
      this.partOfTarget = value;
      return this;
    }

    /**
     * @return {@link #endpoint} (Technical endpoints providing access to services operated for the location.)
     */
    public List<Reference> getEndpoint() { 
      if (this.endpoint == null)
        this.endpoint = new ArrayList<Reference>();
      return this.endpoint;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Location setEndpoint(List<Reference> theEndpoint) { 
      this.endpoint = theEndpoint;
      return this;
    }

    public boolean hasEndpoint() { 
      if (this.endpoint == null)
        return false;
      for (Reference item : this.endpoint)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addEndpoint() { //3
      Reference t = new Reference();
      if (this.endpoint == null)
        this.endpoint = new ArrayList<Reference>();
      this.endpoint.add(t);
      return t;
    }

    public Location addEndpoint(Reference t) { //3
      if (t == null)
        return this;
      if (this.endpoint == null)
        this.endpoint = new ArrayList<Reference>();
      this.endpoint.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #endpoint}, creating it if it does not already exist
     */
    public Reference getEndpointFirstRep() { 
      if (getEndpoint().isEmpty()) {
        addEndpoint();
      }
      return getEndpoint().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Endpoint> getEndpointTarget() { 
      if (this.endpointTarget == null)
        this.endpointTarget = new ArrayList<Endpoint>();
      return this.endpointTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Endpoint addEndpointTarget() { 
      Endpoint r = new Endpoint();
      if (this.endpointTarget == null)
        this.endpointTarget = new ArrayList<Endpoint>();
      this.endpointTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique code or number identifying the location to its users.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status property covers the general availability of the resource, not the current value which may be covered by the operationStatus, or by a schedule/slots if they are configured for the location.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("operationalStatus", "Coding", "The Operational status covers operation values most relevant to beds (but can also apply to rooms/units/chair/etc such as an isolation unit/dialisys chair). This typically covers concepts such as contamination, housekeeping and other activities like maintenance.", 0, java.lang.Integer.MAX_VALUE, operationalStatus));
        childrenList.add(new Property("name", "string", "Name of the location as used by humans. Does not need to be unique.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("alias", "string", "A list of alternate names that the location is known as, or was known as in the past.", 0, java.lang.Integer.MAX_VALUE, alias));
        childrenList.add(new Property("description", "string", "Description of the Location, which helps in finding or referencing the place.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("mode", "code", "Indicates whether a resource instance represents a specific location or a class of locations.", 0, java.lang.Integer.MAX_VALUE, mode));
        childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of function performed at the location.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("telecom", "ContactPoint", "The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("address", "Address", "Physical location.", 0, java.lang.Integer.MAX_VALUE, address));
        childrenList.add(new Property("physicalType", "CodeableConcept", "Physical form of the location, e.g. building, room, vehicle, road.", 0, java.lang.Integer.MAX_VALUE, physicalType));
        childrenList.add(new Property("position", "", "The absolute geographic location of the Location, expressed using the WGS84 datum (This is the same co-ordinate system used in KML).", 0, java.lang.Integer.MAX_VALUE, position));
        childrenList.add(new Property("managingOrganization", "Reference(Organization)", "The organization responsible for the provisioning and upkeep of the location.", 0, java.lang.Integer.MAX_VALUE, managingOrganization));
        childrenList.add(new Property("partOf", "Reference(Location)", "Another Location which this Location is physically part of.", 0, java.lang.Integer.MAX_VALUE, partOf));
        childrenList.add(new Property("endpoint", "Reference(Endpoint)", "Technical endpoints providing access to services operated for the location.", 0, java.lang.Integer.MAX_VALUE, endpoint));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<LocationStatus>
        case -2103166364: /*operationalStatus*/ return this.operationalStatus == null ? new Base[0] : new Base[] {this.operationalStatus}; // Coding
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 92902992: /*alias*/ return this.alias == null ? new Base[0] : this.alias.toArray(new Base[this.alias.size()]); // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : new Base[] {this.mode}; // Enumeration<LocationMode>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        case -1147692044: /*address*/ return this.address == null ? new Base[0] : new Base[] {this.address}; // Address
        case -1474715471: /*physicalType*/ return this.physicalType == null ? new Base[0] : new Base[] {this.physicalType}; // CodeableConcept
        case 747804969: /*position*/ return this.position == null ? new Base[0] : new Base[] {this.position}; // LocationPositionComponent
        case -2058947787: /*managingOrganization*/ return this.managingOrganization == null ? new Base[0] : new Base[] {this.managingOrganization}; // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : new Base[] {this.partOf}; // Reference
        case 1741102485: /*endpoint*/ return this.endpoint == null ? new Base[0] : this.endpoint.toArray(new Base[this.endpoint.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new LocationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<LocationStatus>
          return value;
        case -2103166364: // operationalStatus
          this.operationalStatus = castToCoding(value); // Coding
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 92902992: // alias
          this.getAlias().add(castToString(value)); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 3357091: // mode
          value = new LocationModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<LocationMode>
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          return value;
        case -1147692044: // address
          this.address = castToAddress(value); // Address
          return value;
        case -1474715471: // physicalType
          this.physicalType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 747804969: // position
          this.position = (LocationPositionComponent) value; // LocationPositionComponent
          return value;
        case -2058947787: // managingOrganization
          this.managingOrganization = castToReference(value); // Reference
          return value;
        case -995410646: // partOf
          this.partOf = castToReference(value); // Reference
          return value;
        case 1741102485: // endpoint
          this.getEndpoint().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new LocationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<LocationStatus>
        } else if (name.equals("operationalStatus")) {
          this.operationalStatus = castToCoding(value); // Coding
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("alias")) {
          this.getAlias().add(castToString(value));
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("mode")) {
          value = new LocationModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<LocationMode>
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("telecom")) {
          this.getTelecom().add(castToContactPoint(value));
        } else if (name.equals("address")) {
          this.address = castToAddress(value); // Address
        } else if (name.equals("physicalType")) {
          this.physicalType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("position")) {
          this.position = (LocationPositionComponent) value; // LocationPositionComponent
        } else if (name.equals("managingOrganization")) {
          this.managingOrganization = castToReference(value); // Reference
        } else if (name.equals("partOf")) {
          this.partOf = castToReference(value); // Reference
        } else if (name.equals("endpoint")) {
          this.getEndpoint().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -2103166364:  return getOperationalStatus(); 
        case 3373707:  return getNameElement();
        case 92902992:  return addAliasElement();
        case -1724546052:  return getDescriptionElement();
        case 3357091:  return getModeElement();
        case 3575610:  return getType(); 
        case -1429363305:  return addTelecom(); 
        case -1147692044:  return getAddress(); 
        case -1474715471:  return getPhysicalType(); 
        case 747804969:  return getPosition(); 
        case -2058947787:  return getManagingOrganization(); 
        case -995410646:  return getPartOf(); 
        case 1741102485:  return addEndpoint(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -2103166364: /*operationalStatus*/ return new String[] {"Coding"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 92902992: /*alias*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 3357091: /*mode*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1429363305: /*telecom*/ return new String[] {"ContactPoint"};
        case -1147692044: /*address*/ return new String[] {"Address"};
        case -1474715471: /*physicalType*/ return new String[] {"CodeableConcept"};
        case 747804969: /*position*/ return new String[] {};
        case -2058947787: /*managingOrganization*/ return new String[] {"Reference"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case 1741102485: /*endpoint*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Location.status");
        }
        else if (name.equals("operationalStatus")) {
          this.operationalStatus = new Coding();
          return this.operationalStatus;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Location.name");
        }
        else if (name.equals("alias")) {
          throw new FHIRException("Cannot call addChild on a primitive type Location.alias");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Location.description");
        }
        else if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type Location.mode");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else if (name.equals("address")) {
          this.address = new Address();
          return this.address;
        }
        else if (name.equals("physicalType")) {
          this.physicalType = new CodeableConcept();
          return this.physicalType;
        }
        else if (name.equals("position")) {
          this.position = new LocationPositionComponent();
          return this.position;
        }
        else if (name.equals("managingOrganization")) {
          this.managingOrganization = new Reference();
          return this.managingOrganization;
        }
        else if (name.equals("partOf")) {
          this.partOf = new Reference();
          return this.partOf;
        }
        else if (name.equals("endpoint")) {
          return addEndpoint();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Location";

  }

      public Location copy() {
        Location dst = new Location();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.operationalStatus = operationalStatus == null ? null : operationalStatus.copy();
        dst.name = name == null ? null : name.copy();
        if (alias != null) {
          dst.alias = new ArrayList<StringType>();
          for (StringType i : alias)
            dst.alias.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        dst.mode = mode == null ? null : mode.copy();
        dst.type = type == null ? null : type.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        dst.address = address == null ? null : address.copy();
        dst.physicalType = physicalType == null ? null : physicalType.copy();
        dst.position = position == null ? null : position.copy();
        dst.managingOrganization = managingOrganization == null ? null : managingOrganization.copy();
        dst.partOf = partOf == null ? null : partOf.copy();
        if (endpoint != null) {
          dst.endpoint = new ArrayList<Reference>();
          for (Reference i : endpoint)
            dst.endpoint.add(i.copy());
        };
        return dst;
      }

      protected Location typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Location))
          return false;
        Location o = (Location) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(operationalStatus, o.operationalStatus, true)
           && compareDeep(name, o.name, true) && compareDeep(alias, o.alias, true) && compareDeep(description, o.description, true)
           && compareDeep(mode, o.mode, true) && compareDeep(type, o.type, true) && compareDeep(telecom, o.telecom, true)
           && compareDeep(address, o.address, true) && compareDeep(physicalType, o.physicalType, true) && compareDeep(position, o.position, true)
           && compareDeep(managingOrganization, o.managingOrganization, true) && compareDeep(partOf, o.partOf, true)
           && compareDeep(endpoint, o.endpoint, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Location))
          return false;
        Location o = (Location) other;
        return compareValues(status, o.status, true) && compareValues(name, o.name, true) && compareValues(alias, o.alias, true)
           && compareValues(description, o.description, true) && compareValues(mode, o.mode, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, operationalStatus
          , name, alias, description, mode, type, telecom, address, physicalType, position
          , managingOrganization, partOf, endpoint);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Location;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>An identifier for the location</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Location.identifier", description="An identifier for the location", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>An identifier for the location</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>partof</b>
   * <p>
   * Description: <b>A location of which this location is a part</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Location.partOf</b><br>
   * </p>
   */
  @SearchParamDefinition(name="partof", path="Location.partOf", description="A location of which this location is a part", type="reference", target={Location.class } )
  public static final String SP_PARTOF = "partof";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>partof</b>
   * <p>
   * Description: <b>A location of which this location is a part</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Location.partOf</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTOF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTOF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Location:partof</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTOF = new ca.uhn.fhir.model.api.Include("Location:partof").toLocked();

 /**
   * Search parameter: <b>near-distance</b>
   * <p>
   * Description: <b>A distance quantity to limit the near search to locations within a specific distance

Requires the near parameter to also be included</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Location.position</b><br>
   * </p>
   */
  @SearchParamDefinition(name="near-distance", path="Location.position", description="A distance quantity to limit the near search to locations within a specific distance\n\nRequires the near parameter to also be included", type="quantity" )
  public static final String SP_NEAR_DISTANCE = "near-distance";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>near-distance</b>
   * <p>
   * Description: <b>A distance quantity to limit the near search to locations within a specific distance

Requires the near parameter to also be included</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Location.position</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam NEAR_DISTANCE = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_NEAR_DISTANCE);

 /**
   * Search parameter: <b>address</b>
   * <p>
   * Description: <b>A (part of the) address of the location</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.address</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address", path="Location.address", description="A (part of the) address of the location", type="string" )
  public static final String SP_ADDRESS = "address";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address</b>
   * <p>
   * Description: <b>A (part of the) address of the location</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.address</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS);

 /**
   * Search parameter: <b>address-state</b>
   * <p>
   * Description: <b>A state specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.address.state</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-state", path="Location.address.state", description="A state specified in an address", type="string" )
  public static final String SP_ADDRESS_STATE = "address-state";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-state</b>
   * <p>
   * Description: <b>A state specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.address.state</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_STATE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_STATE);

 /**
   * Search parameter: <b>operational-status</b>
   * <p>
   * Description: <b>Searches for locations (typically bed/room) that have an operational status (e.g. contaminated, housekeeping)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.operationalStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="operational-status", path="Location.operationalStatus", description="Searches for locations (typically bed/room) that have an operational status (e.g. contaminated, housekeeping)", type="token" )
  public static final String SP_OPERATIONAL_STATUS = "operational-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>operational-status</b>
   * <p>
   * Description: <b>Searches for locations (typically bed/room) that have an operational status (e.g. contaminated, housekeeping)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.operationalStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam OPERATIONAL_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_OPERATIONAL_STATUS);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>A code for the type of location</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Location.type", description="A code for the type of location", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>A code for the type of location</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>address-postalcode</b>
   * <p>
   * Description: <b>A postal code specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.address.postalCode</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-postalcode", path="Location.address.postalCode", description="A postal code specified in an address", type="string" )
  public static final String SP_ADDRESS_POSTALCODE = "address-postalcode";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-postalcode</b>
   * <p>
   * Description: <b>A postal code specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.address.postalCode</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_POSTALCODE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_POSTALCODE);

 /**
   * Search parameter: <b>address-country</b>
   * <p>
   * Description: <b>A country specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.address.country</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-country", path="Location.address.country", description="A country specified in an address", type="string" )
  public static final String SP_ADDRESS_COUNTRY = "address-country";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-country</b>
   * <p>
   * Description: <b>A country specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.address.country</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_COUNTRY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_COUNTRY);

 /**
   * Search parameter: <b>endpoint</b>
   * <p>
   * Description: <b>Technical endpoints providing access to services operated for the location</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Location.endpoint</b><br>
   * </p>
   */
  @SearchParamDefinition(name="endpoint", path="Location.endpoint", description="Technical endpoints providing access to services operated for the location", type="reference", target={Endpoint.class } )
  public static final String SP_ENDPOINT = "endpoint";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>endpoint</b>
   * <p>
   * Description: <b>Technical endpoints providing access to services operated for the location</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Location.endpoint</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENDPOINT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENDPOINT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Location:endpoint</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENDPOINT = new ca.uhn.fhir.model.api.Include("Location:endpoint").toLocked();

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>Searches for locations that are managed by the provided organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Location.managingOrganization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="Location.managingOrganization", description="Searches for locations that are managed by the provided organization", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>Searches for locations that are managed by the provided organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Location.managingOrganization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Location:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("Location:organization").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>A portion of the location's name or alias</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.name, Location.alias</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="Location.name | Location.alias", description="A portion of the location's name or alias", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>A portion of the location's name or alias</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.name, Location.alias</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>address-use</b>
   * <p>
   * Description: <b>A use code specified in an address</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.address.use</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-use", path="Location.address.use", description="A use code specified in an address", type="token" )
  public static final String SP_ADDRESS_USE = "address-use";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-use</b>
   * <p>
   * Description: <b>A use code specified in an address</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.address.use</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ADDRESS_USE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ADDRESS_USE);

 /**
   * Search parameter: <b>near</b>
   * <p>
   * Description: <b>The coordinates expressed as [latitude]:[longitude] (using the WGS84 datum, see notes) to find locations near to (servers may search using a square rather than a circle for efficiency)

Requires the near-distance parameter to be provided also</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.position</b><br>
   * </p>
   */
  @SearchParamDefinition(name="near", path="Location.position", description="The coordinates expressed as [latitude]:[longitude] (using the WGS84 datum, see notes) to find locations near to (servers may search using a square rather than a circle for efficiency)\n\nRequires the near-distance parameter to be provided also", type="token" )
  public static final String SP_NEAR = "near";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>near</b>
   * <p>
   * Description: <b>The coordinates expressed as [latitude]:[longitude] (using the WGS84 datum, see notes) to find locations near to (servers may search using a square rather than a circle for efficiency)

Requires the near-distance parameter to be provided also</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.position</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam NEAR = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_NEAR);

 /**
   * Search parameter: <b>address-city</b>
   * <p>
   * Description: <b>A city specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.address.city</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-city", path="Location.address.city", description="A city specified in an address", type="string" )
  public static final String SP_ADDRESS_CITY = "address-city";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-city</b>
   * <p>
   * Description: <b>A city specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Location.address.city</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_CITY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_CITY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Searches for locations with a specific kind of status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Location.status", description="Searches for locations with a specific kind of status", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Searches for locations with a specific kind of status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Location.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

