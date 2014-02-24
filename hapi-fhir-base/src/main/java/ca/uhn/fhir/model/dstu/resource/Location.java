











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
	private IDatatype myPosition;
	
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
	 * Gets the value(s) for identifier (Unique code or number identifying the location to its users)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique code or number identifying the location to its users
     * </p> 
	 */
	public IdentifierDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Unique code or number identifying the location to its users)
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
	 * Gets the value(s) for name (Name of the location as used by humans)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the location as used by humans. Does not need to be unique.
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Name of the location as used by humans)
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
	 * Gets the value(s) for description (Description of the Location, which helps in finding or referencing the place)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the Location, which helps in finding or referencing the place
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Description of the Location, which helps in finding or referencing the place)
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
	 * Gets the value(s) for type (Indicates the type of function performed at the location)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of function performed at the location
     * </p> 
	 */
	public CodeableConceptDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (Indicates the type of function performed at the location)
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
	 * Gets the value(s) for telecom (Contact details of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact details of the location)
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
	 * Gets the value(s) for address (Physical location)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public AddressDt getAddress() {
		return myAddress;
	}

	/**
	 * Sets the value(s) for address (Physical location)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setAddress(AddressDt theValue) {
		myAddress = theValue;
	}
	
	/**
	 * Gets the value(s) for physicalType (Physical form of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * Physical form of the location, e.g. building, room, vehicle, road
     * </p> 
	 */
	public CodeableConceptDt getPhysicalType() {
		return myPhysicalType;
	}

	/**
	 * Sets the value(s) for physicalType (Physical form of the location)
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
	 * Gets the value(s) for position (The absolute geographic location )
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	public IDatatype getPosition() {
		return myPosition;
	}

	/**
	 * Sets the value(s) for position (The absolute geographic location )
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	public void setPosition(IDatatype theValue) {
		myPosition = theValue;
	}
	
	/**
	 * Gets the value(s) for managingOrganization (The organization that is responsible for the provisioning and upkeep of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public ResourceReference getManagingOrganization() {
		return myManagingOrganization;
	}

	/**
	 * Sets the value(s) for managingOrganization (The organization that is responsible for the provisioning and upkeep of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setManagingOrganization(ResourceReference theValue) {
		myManagingOrganization = theValue;
	}
	
	/**
	 * Gets the value(s) for status (active | suspended | inactive)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (active | suspended | inactive)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for partOf (Another Location which this Location is physically part of)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public ResourceReference getPartOf() {
		return myPartOf;
	}

	/**
	 * Sets the value(s) for partOf (Another Location which this Location is physically part of)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setPartOf(ResourceReference theValue) {
		myPartOf = theValue;
	}
	
	/**
	 * Gets the value(s) for mode (instance | kind)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public CodeDt getMode() {
		return myMode;
	}

	/**
	 * Sets the value(s) for mode (instance | kind)
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
	private IDatatype myPosition;
	
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
	 * Gets the value(s) for identifier (Unique code or number identifying the location to its users)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique code or number identifying the location to its users
     * </p> 
	 */
	public IdentifierDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Unique code or number identifying the location to its users)
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
	 * Gets the value(s) for name (Name of the location as used by humans)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the location as used by humans. Does not need to be unique.
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Name of the location as used by humans)
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
	 * Gets the value(s) for description (Description of the Location, which helps in finding or referencing the place)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the Location, which helps in finding or referencing the place
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Description of the Location, which helps in finding or referencing the place)
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
	 * Gets the value(s) for type (Indicates the type of function performed at the location)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of function performed at the location
     * </p> 
	 */
	public CodeableConceptDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (Indicates the type of function performed at the location)
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
	 * Gets the value(s) for telecom (Contact details of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact details of the location)
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
	 * Gets the value(s) for address (Physical location)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public AddressDt getAddress() {
		return myAddress;
	}

	/**
	 * Sets the value(s) for address (Physical location)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setAddress(AddressDt theValue) {
		myAddress = theValue;
	}
	
	/**
	 * Gets the value(s) for physicalType (Physical form of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * Physical form of the location, e.g. building, room, vehicle, road
     * </p> 
	 */
	public CodeableConceptDt getPhysicalType() {
		return myPhysicalType;
	}

	/**
	 * Sets the value(s) for physicalType (Physical form of the location)
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
	 * Gets the value(s) for position (The absolute geographic location )
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	public IDatatype getPosition() {
		return myPosition;
	}

	/**
	 * Sets the value(s) for position (The absolute geographic location )
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	public void setPosition(IDatatype theValue) {
		myPosition = theValue;
	}
	
	/**
	 * Gets the value(s) for managingOrganization (The organization that is responsible for the provisioning and upkeep of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public ResourceReference getManagingOrganization() {
		return myManagingOrganization;
	}

	/**
	 * Sets the value(s) for managingOrganization (The organization that is responsible for the provisioning and upkeep of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setManagingOrganization(ResourceReference theValue) {
		myManagingOrganization = theValue;
	}
	
	/**
	 * Gets the value(s) for status (active | suspended | inactive)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (active | suspended | inactive)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for partOf (Another Location which this Location is physically part of)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public ResourceReference getPartOf() {
		return myPartOf;
	}

	/**
	 * Sets the value(s) for partOf (Another Location which this Location is physically part of)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setPartOf(ResourceReference theValue) {
		myPartOf = theValue;
	}
	
	/**
	 * Gets the value(s) for mode (instance | kind)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public CodeDt getMode() {
		return myMode;
	}

	/**
	 * Sets the value(s) for mode (instance | kind)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public void setMode(CodeDt theValue) {
		myMode = theValue;
	}
	
	}



}