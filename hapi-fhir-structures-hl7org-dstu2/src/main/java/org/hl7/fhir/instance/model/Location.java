package org.hl7.fhir.instance.model;

import java.math.BigDecimal;

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

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
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
         * added to help the parsers
         */
        NULL;
        public static LocationStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("inactive".equals(codeString))
          return INACTIVE;
        throw new Exception("Unknown LocationStatus code '"+codeString+"'");
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
    public String toCode(LocationStatus code) {
      if (code == LocationStatus.ACTIVE)
        return "active";
      if (code == LocationStatus.SUSPENDED)
        return "suspended";
      if (code == LocationStatus.INACTIVE)
        return "inactive";
      return "?";
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
         * added to help the parsers
         */
        NULL;
        public static LocationMode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return INSTANCE;
        if ("kind".equals(codeString))
          return KIND;
        throw new Exception("Unknown LocationMode code '"+codeString+"'");
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
    public String toCode(LocationMode code) {
      if (code == LocationMode.INSTANCE)
        return "instance";
      if (code == LocationMode.KIND)
        return "kind";
      return "?";
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

    /*
     * Constructor
     */
      public LocationPositionComponent() {
        super();
      }

    /*
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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("longitude", "decimal", "Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below).", 0, java.lang.Integer.MAX_VALUE, longitude));
          childrenList.add(new Property("latitude", "decimal", "Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below).", 0, java.lang.Integer.MAX_VALUE, latitude));
          childrenList.add(new Property("altitude", "decimal", "Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below).", 0, java.lang.Integer.MAX_VALUE, altitude));
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
        return super.isEmpty() && (longitude == null || longitude.isEmpty()) && (latitude == null || latitude.isEmpty())
           && (altitude == null || altitude.isEmpty());
      }

  }

    /**
     * Unique code or number identifying the location to its users.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique code or number identifying the location to its users", formalDefinition="Unique code or number identifying the location to its users." )
    protected List<Identifier> identifier;

    /**
     * active | suspended | inactive.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | suspended | inactive", formalDefinition="active | suspended | inactive." )
    protected Enumeration<LocationStatus> status;

    /**
     * Name of the location as used by humans. Does not need to be unique.
     */
    @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the location as used by humans", formalDefinition="Name of the location as used by humans. Does not need to be unique." )
    protected StringType name;

    /**
     * Description of the Location, which helps in finding or referencing the place.
     */
    @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Description of the location", formalDefinition="Description of the Location, which helps in finding or referencing the place." )
    protected StringType description;

    /**
     * Indicates whether a resource instance represents a specific location or a class of locations.
     */
    @Child(name = "mode", type = {CodeType.class}, order=4, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="instance | kind", formalDefinition="Indicates whether a resource instance represents a specific location or a class of locations." )
    protected Enumeration<LocationMode> mode;

    /**
     * Indicates the type of function performed at the location.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type of function performed", formalDefinition="Indicates the type of function performed at the location." )
    protected CodeableConcept type;

    /**
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contact details of the location", formalDefinition="The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites." )
    protected List<ContactPoint> telecom;

    /**
     * Physical location.
     */
    @Child(name = "address", type = {Address.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Physical location", formalDefinition="Physical location." )
    protected Address address;

    /**
     * Physical form of the location, e.g. building, room, vehicle, road.
     */
    @Child(name = "physicalType", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Physical form of the location", formalDefinition="Physical form of the location, e.g. building, room, vehicle, road." )
    protected CodeableConcept physicalType;

    /**
     * The absolute geographic location of the Location, expressed using the WGS84 datum (This is the same co-ordinate system used in KML).
     */
    @Child(name = "position", type = {}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The absolute geographic location", formalDefinition="The absolute geographic location of the Location, expressed using the WGS84 datum (This is the same co-ordinate system used in KML)." )
    protected LocationPositionComponent position;

    /**
     * The organization responsible for the provisioning and upkeep of the location.
     */
    @Child(name = "managingOrganization", type = {Organization.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization responsible for provisioning and upkeep", formalDefinition="The organization responsible for the provisioning and upkeep of the location." )
    protected Reference managingOrganization;

    /**
     * The actual object that is the target of the reference (The organization responsible for the provisioning and upkeep of the location.)
     */
    protected Organization managingOrganizationTarget;

    /**
     * Another Location which this Location is physically part of.
     */
    @Child(name = "partOf", type = {Location.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Another Location this one is physically part of", formalDefinition="Another Location which this Location is physically part of." )
    protected Reference partOf;

    /**
     * The actual object that is the target of the reference (Another Location which this Location is physically part of.)
     */
    protected Location partOfTarget;

    private static final long serialVersionUID = -2100435761L;

  /*
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

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Unique code or number identifying the location to its users.)
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
    public Location addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #status} (active | suspended | inactive.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
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
     * @param value {@link #status} (active | suspended | inactive.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Location setStatusElement(Enumeration<LocationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return active | suspended | inactive.
     */
    public LocationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value active | suspended | inactive.
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

    public boolean hasTelecom() { 
      if (this.telecom == null)
        return false;
      for (ContactPoint item : this.telecom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #telecom} (The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites.)
     */
    // syntactic sugar
    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    // syntactic sugar
    public Location addTelecom(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return this;
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique code or number identifying the location to its users.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "active | suspended | inactive.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("name", "string", "Name of the location as used by humans. Does not need to be unique.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("description", "string", "Description of the Location, which helps in finding or referencing the place.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("mode", "code", "Indicates whether a resource instance represents a specific location or a class of locations.", 0, java.lang.Integer.MAX_VALUE, mode));
        childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of function performed at the location.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("telecom", "ContactPoint", "The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("address", "Address", "Physical location.", 0, java.lang.Integer.MAX_VALUE, address));
        childrenList.add(new Property("physicalType", "CodeableConcept", "Physical form of the location, e.g. building, room, vehicle, road.", 0, java.lang.Integer.MAX_VALUE, physicalType));
        childrenList.add(new Property("position", "", "The absolute geographic location of the Location, expressed using the WGS84 datum (This is the same co-ordinate system used in KML).", 0, java.lang.Integer.MAX_VALUE, position));
        childrenList.add(new Property("managingOrganization", "Reference(Organization)", "The organization responsible for the provisioning and upkeep of the location.", 0, java.lang.Integer.MAX_VALUE, managingOrganization));
        childrenList.add(new Property("partOf", "Reference(Location)", "Another Location which this Location is physically part of.", 0, java.lang.Integer.MAX_VALUE, partOf));
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
        dst.name = name == null ? null : name.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(name, o.name, true)
           && compareDeep(description, o.description, true) && compareDeep(mode, o.mode, true) && compareDeep(type, o.type, true)
           && compareDeep(telecom, o.telecom, true) && compareDeep(address, o.address, true) && compareDeep(physicalType, o.physicalType, true)
           && compareDeep(position, o.position, true) && compareDeep(managingOrganization, o.managingOrganization, true)
           && compareDeep(partOf, o.partOf, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Location))
          return false;
        Location o = (Location) other;
        return compareValues(status, o.status, true) && compareValues(name, o.name, true) && compareValues(description, o.description, true)
           && compareValues(mode, o.mode, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (name == null || name.isEmpty()) && (description == null || description.isEmpty()) && (mode == null || mode.isEmpty())
           && (type == null || type.isEmpty()) && (telecom == null || telecom.isEmpty()) && (address == null || address.isEmpty())
           && (physicalType == null || physicalType.isEmpty()) && (position == null || position.isEmpty())
           && (managingOrganization == null || managingOrganization.isEmpty()) && (partOf == null || partOf.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Location;
   }

  @SearchParamDefinition(name="identifier", path="Location.identifier", description="Unique code or number identifying the location to its users", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="partof", path="Location.partOf", description="The location of which this location is a part", type="reference" )
  public static final String SP_PARTOF = "partof";
  @SearchParamDefinition(name="near-distance", path="Location.position", description="A distance quantity to limit the near search to locations within a specific distance", type="token" )
  public static final String SP_NEARDISTANCE = "near-distance";
  @SearchParamDefinition(name="address", path="Location.address", description="A (part of the) address of the location", type="string" )
  public static final String SP_ADDRESS = "address";
  @SearchParamDefinition(name="address-state", path="Location.address.state", description="A state specified in an address", type="string" )
  public static final String SP_ADDRESSSTATE = "address-state";
  @SearchParamDefinition(name="type", path="Location.type", description="A code for the type of location", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="address-postalcode", path="Location.address.postalCode", description="A postal code specified in an address", type="string" )
  public static final String SP_ADDRESSPOSTALCODE = "address-postalcode";
  @SearchParamDefinition(name="address-country", path="Location.address.country", description="A country specified in an address", type="string" )
  public static final String SP_ADDRESSCOUNTRY = "address-country";
  @SearchParamDefinition(name="organization", path="Location.managingOrganization", description="Searches for locations that are managed by the provided organization", type="reference" )
  public static final String SP_ORGANIZATION = "organization";
  @SearchParamDefinition(name="name", path="Location.name", description="A (portion of the) name of the location", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="address-use", path="Location.address.use", description="A use code specified in an address", type="token" )
  public static final String SP_ADDRESSUSE = "address-use";
  @SearchParamDefinition(name="near", path="Location.position", description="The coordinates expressed as [lat],[long] (using the WGS84 datum, see notes) to find locations near to (servers may search using a square rather than a circle for efficiency)", type="token" )
  public static final String SP_NEAR = "near";
  @SearchParamDefinition(name="address-city", path="Location.address.city", description="A city specified in an address", type="string" )
  public static final String SP_ADDRESSCITY = "address-city";
  @SearchParamDefinition(name="status", path="Location.status", description="Searches for locations with a specific kind of status", type="token" )
  public static final String SP_STATUS = "status";

}

