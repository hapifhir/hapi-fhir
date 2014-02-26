















package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;

/**
 * HAPI/FHIR <b>Location</b> Resource
 * (Details and position information for a physical place)
 *
 * <p>
 * <b>Definition:</b>
 * Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated 
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="Location")
public class Location implements IResource {

	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	private IdentifierDt myIdentifier;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="description", type=StringDt.class, order=2, min=0, max=1)	
	private StringDt myDescription;
	
	@Child(name="type", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=5, min=0, max=1)	
	private AddressDt myAddress;
	
	@Child(name="physicalType", type=CodeableConceptDt.class, order=6, min=0, max=1)	
	private CodeableConceptDt myPhysicalType;
	
	@Child(name="position", order=7, min=0, max=1)	
	private Position myPosition;
	
	@Child(name="managingOrganization", order=8, min=0, max=1)
	@ChildResource(types= {
		Organization.class,
	})	
	private ResourceReference myManagingOrganization;
	
	@Child(name="status", type=CodeDt.class, order=9, min=0, max=1)	
	private CodeDt myStatus;
	
	@Child(name="partOf", order=10, min=0, max=1)
	@ChildResource(types= {
		Location.class,
	})	
	private ResourceReference myPartOf;
	
	@Child(name="mode", type=CodeDt.class, order=11, min=0, max=1)	
	private CodeDt myMode;
	
	/**
	 * Gets the value(s) for <b>identifier</b> (Unique code or number identifying the location to its users).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique code or number identifying the location to its users
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Unique code or number identifying the location to its users)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique code or number identifying the location to its users
     * </p> 
	 */
	public void setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>name</b> (Name of the location as used by humans).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the location as used by humans. Does not need to be unique.
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name of the location as used by humans)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the location as used by humans. Does not need to be unique.
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>name</b> (Name of the location as used by humans)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the location as used by humans. Does not need to be unique.
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>description</b> (Description of the Location, which helps in finding or referencing the place).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the Location, which helps in finding or referencing the place
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Description of the Location, which helps in finding or referencing the place)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the Location, which helps in finding or referencing the place
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>description</b> (Description of the Location, which helps in finding or referencing the place)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the Location, which helps in finding or referencing the place
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>type</b> (Indicates the type of function performed at the location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of function performed at the location
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Indicates the type of function performed at the location)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of function performed at the location
     * </p> 
	 */
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>telecom</b> (Contact details of the location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
	 */
	public List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (Contact details of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>address</b> (Physical location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AddressDt getAddress() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (Physical location)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setAddress(AddressDt theValue) {
		myAddress = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>physicalType</b> (Physical form of the location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Physical form of the location, e.g. building, room, vehicle, road
     * </p> 
	 */
	public CodeableConceptDt getPhysicalType() {  
		if (myPhysicalType == null) {
			myPhysicalType = new CodeableConceptDt();
		}
		return myPhysicalType;
	}

	/**
	 * Sets the value(s) for <b>physicalType</b> (Physical form of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * Physical form of the location, e.g. building, room, vehicle, road
     * </p> 
	 */
	public void setPhysicalType(CodeableConceptDt theValue) {
		myPhysicalType = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>position</b> (The absolute geographic location ).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	public Position getPosition() {  
		if (myPosition == null) {
			myPosition = new Position();
		}
		return myPosition;
	}

	/**
	 * Sets the value(s) for <b>position</b> (The absolute geographic location )
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	public void setPosition(Position theValue) {
		myPosition = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>managingOrganization</b> (The organization that is responsible for the provisioning and upkeep of the location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReference getManagingOrganization() {  
		if (myManagingOrganization == null) {
			myManagingOrganization = new ResourceReference();
		}
		return myManagingOrganization;
	}

	/**
	 * Sets the value(s) for <b>managingOrganization</b> (The organization that is responsible for the provisioning and upkeep of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setManagingOrganization(ResourceReference theValue) {
		myManagingOrganization = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>status</b> (active | suspended | inactive).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getStatus() {  
		if (myStatus == null) {
			myStatus = new CodeDt();
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (active | suspended | inactive)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>partOf</b> (Another Location which this Location is physically part of).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReference getPartOf() {  
		if (myPartOf == null) {
			myPartOf = new ResourceReference();
		}
		return myPartOf;
	}

	/**
	 * Sets the value(s) for <b>partOf</b> (Another Location which this Location is physically part of)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setPartOf(ResourceReference theValue) {
		myPartOf = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>mode</b> (instance | kind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public CodeDt getMode() {  
		if (myMode == null) {
			myMode = new CodeDt();
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (instance | kind)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public void setMode(CodeDt theValue) {
		myMode = theValue;
	}
	
 
	/**
	 * Block class for child element: <b>Location.position</b> (The absolute geographic location )
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	@Block(name="Location.position")	
	public static class Position implements IResourceBlock {
	
	@Child(name="longitude", type=DecimalDt.class, order=0, min=1, max=1)	
	private DecimalDt myLongitude;
	
	@Child(name="latitude", type=DecimalDt.class, order=1, min=1, max=1)	
	private DecimalDt myLatitude;
	
	@Child(name="altitude", type=DecimalDt.class, order=2, min=0, max=1)	
	private DecimalDt myAltitude;
	
	/**
	 * Gets the value(s) for <b>longitude</b> (Longitude as expressed in KML).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public DecimalDt getLongitude() {  
		if (myLongitude == null) {
			myLongitude = new DecimalDt();
		}
		return myLongitude;
	}

	/**
	 * Sets the value(s) for <b>longitude</b> (Longitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public void setLongitude(DecimalDt theValue) {
		myLongitude = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>latitude</b> (Latitude as expressed in KML).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public DecimalDt getLatitude() {  
		if (myLatitude == null) {
			myLatitude = new DecimalDt();
		}
		return myLatitude;
	}

	/**
	 * Sets the value(s) for <b>latitude</b> (Latitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public void setLatitude(DecimalDt theValue) {
		myLatitude = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>altitude</b> (Altitude as expressed in KML).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public DecimalDt getAltitude() {  
		if (myAltitude == null) {
			myAltitude = new DecimalDt();
		}
		return myAltitude;
	}

	/**
	 * Sets the value(s) for <b>altitude</b> (Altitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public void setAltitude(DecimalDt theValue) {
		myAltitude = theValue;
	}
	
 

	}




}