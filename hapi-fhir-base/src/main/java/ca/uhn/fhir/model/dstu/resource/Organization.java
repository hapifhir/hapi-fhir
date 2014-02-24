











package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;

/**
 * HAPI/FHIR <b>Organization</b> Resource
 * (A grouping of people or organizations with a common purpose)
 *
 * <p>
 * <b>Definition:</b>
 * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="Organization")
public class Organization implements IResource {

	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="telecom", type=ContactDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<AddressDt> myAddress;
	
	@Child(name="partOf", order=5, min=0, max=1)
	@ChildResource(types= {
		Organization.class,
	})	
	private ResourceReference myPartOf;
	
	@Child(name="contact", order=6, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myContact;
	
	@Child(name="location", order=7, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Location.class,
	})	
	private List<ResourceReference> myLocation;
	
	@Child(name="active", type=BooleanDt.class, order=8, min=0, max=1)	
	private BooleanDt myActive;
	
	/**
	 * Gets the value(s) for identifier (Identifies this organization  across multiple systems)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Identifies this organization  across multiple systems)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for name (Name used for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the organization
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Name used for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the organization
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Gets the value(s) for type (Kind of organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of organization that this is
     * </p> 
	 */
	public CodeableConceptDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (Kind of organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of organization that this is
     * </p> 
	 */
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
	}
	
	/**
	 * Gets the value(s) for telecom (A contact detail for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (A contact detail for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
	/**
	 * Gets the value(s) for address (An address for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * An address for the organization
     * </p> 
	 */
	public List<AddressDt> getAddress() {
		return myAddress;
	}

	/**
	 * Sets the value(s) for address (An address for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * An address for the organization
     * </p> 
	 */
	public void setAddress(List<AddressDt> theValue) {
		myAddress = theValue;
	}
	
	/**
	 * Gets the value(s) for partOf (The organization of which this organization forms a part)
	 *
     * <p>
     * <b>Definition:</b>
     * The organization of which this organization forms a part
     * </p> 
	 */
	public ResourceReference getPartOf() {
		return myPartOf;
	}

	/**
	 * Sets the value(s) for partOf (The organization of which this organization forms a part)
	 *
     * <p>
     * <b>Definition:</b>
     * The organization of which this organization forms a part
     * </p> 
	 */
	public void setPartOf(ResourceReference theValue) {
		myPartOf = theValue;
	}
	
	/**
	 * Gets the value(s) for contact (Contact for the organization for a certain purpose)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public List<IDatatype> getContact() {
		return myContact;
	}

	/**
	 * Sets the value(s) for contact (Contact for the organization for a certain purpose)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setContact(List<IDatatype> theValue) {
		myContact = theValue;
	}
	
	/**
	 * Gets the value(s) for location (Location(s) the organization uses to provide services)
	 *
     * <p>
     * <b>Definition:</b>
     * Location(s) the organization uses to provide services
     * </p> 
	 */
	public List<ResourceReference> getLocation() {
		return myLocation;
	}

	/**
	 * Sets the value(s) for location (Location(s) the organization uses to provide services)
	 *
     * <p>
     * <b>Definition:</b>
     * Location(s) the organization uses to provide services
     * </p> 
	 */
	public void setLocation(List<ResourceReference> theValue) {
		myLocation = theValue;
	}
	
	/**
	 * Gets the value(s) for active (Whether the organization's record is still in active use)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the organization's record is still in active use
     * </p> 
	 */
	public BooleanDt getActive() {
		return myActive;
	}

	/**
	 * Sets the value(s) for active (Whether the organization's record is still in active use)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the organization's record is still in active use
     * </p> 
	 */
	public void setActive(BooleanDt theValue) {
		myActive = theValue;
	}
	

	/**
	 * Block class for child element: <b>Organization.contact</b> (Contact for the organization for a certain purpose)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	@Block(name="Organization.contact")	
	public static class Contact implements IResourceBlock {
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="telecom", type=ContactDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<AddressDt> myAddress;
	
	@Child(name="partOf", order=5, min=0, max=1)
	@ChildResource(types= {
		Organization.class,
	})	
	private ResourceReference myPartOf;
	
	@Child(name="contact", order=6, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myContact;
	
	@Child(name="location", order=7, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Location.class,
	})	
	private List<ResourceReference> myLocation;
	
	@Child(name="active", type=BooleanDt.class, order=8, min=0, max=1)	
	private BooleanDt myActive;
	
	/**
	 * Gets the value(s) for identifier (Identifies this organization  across multiple systems)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Identifies this organization  across multiple systems)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for name (Name used for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the organization
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Name used for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the organization
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Gets the value(s) for type (Kind of organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of organization that this is
     * </p> 
	 */
	public CodeableConceptDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (Kind of organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of organization that this is
     * </p> 
	 */
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
	}
	
	/**
	 * Gets the value(s) for telecom (A contact detail for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (A contact detail for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
	/**
	 * Gets the value(s) for address (An address for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * An address for the organization
     * </p> 
	 */
	public List<AddressDt> getAddress() {
		return myAddress;
	}

	/**
	 * Sets the value(s) for address (An address for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * An address for the organization
     * </p> 
	 */
	public void setAddress(List<AddressDt> theValue) {
		myAddress = theValue;
	}
	
	/**
	 * Gets the value(s) for partOf (The organization of which this organization forms a part)
	 *
     * <p>
     * <b>Definition:</b>
     * The organization of which this organization forms a part
     * </p> 
	 */
	public ResourceReference getPartOf() {
		return myPartOf;
	}

	/**
	 * Sets the value(s) for partOf (The organization of which this organization forms a part)
	 *
     * <p>
     * <b>Definition:</b>
     * The organization of which this organization forms a part
     * </p> 
	 */
	public void setPartOf(ResourceReference theValue) {
		myPartOf = theValue;
	}
	
	/**
	 * Gets the value(s) for contact (Contact for the organization for a certain purpose)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public List<IDatatype> getContact() {
		return myContact;
	}

	/**
	 * Sets the value(s) for contact (Contact for the organization for a certain purpose)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setContact(List<IDatatype> theValue) {
		myContact = theValue;
	}
	
	/**
	 * Gets the value(s) for location (Location(s) the organization uses to provide services)
	 *
     * <p>
     * <b>Definition:</b>
     * Location(s) the organization uses to provide services
     * </p> 
	 */
	public List<ResourceReference> getLocation() {
		return myLocation;
	}

	/**
	 * Sets the value(s) for location (Location(s) the organization uses to provide services)
	 *
     * <p>
     * <b>Definition:</b>
     * Location(s) the organization uses to provide services
     * </p> 
	 */
	public void setLocation(List<ResourceReference> theValue) {
		myLocation = theValue;
	}
	
	/**
	 * Gets the value(s) for active (Whether the organization's record is still in active use)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the organization's record is still in active use
     * </p> 
	 */
	public BooleanDt getActive() {
		return myActive;
	}

	/**
	 * Sets the value(s) for active (Whether the organization's record is still in active use)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the organization's record is still in active use
     * </p> 
	 */
	public void setActive(BooleanDt theValue) {
		myActive = theValue;
	}
	
	}



}