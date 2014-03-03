















package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;
import ca.uhn.fhir.model.dstu.valueset.*;

/**
 * HAPI/FHIR <b>Device</b> Resource
 * (An instance of a manufactured thing that is used in the provision of healthcare)
 *
 * <p>
 * <b>Definition:</b>
 * This resource identifies an instance of a manufactured thing that is used in the provision of healthcare without being substantially changed through that activity. The device may be a machine, an insert, a computer, an application, etc. This includes durable (reusable) medical equipment as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Allows institutions to track their devices. 
 * </p> 
 */
@ResourceDef(name="Device")
public class Device extends BaseResource implements IResource {

	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="manufacturer", type=StringDt.class, order=2, min=0, max=1)	
	private StringDt myManufacturer;
	
	@Child(name="model", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myModel;
	
	@Child(name="version", type=StringDt.class, order=4, min=0, max=1)	
	private StringDt myVersion;
	
	@Child(name="expiry", type=DateDt.class, order=5, min=0, max=1)	
	private DateDt myExpiry;
	
	@Child(name="udi", type=StringDt.class, order=6, min=0, max=1)	
	private StringDt myUdi;
	
	@Child(name="lotNumber", type=StringDt.class, order=7, min=0, max=1)	
	private StringDt myLotNumber;
	
	@Child(name="owner", order=8, min=0, max=1)
	@ChildResource(types= {
		Organization.class,
	})	
	private ResourceReference myOwner;
	
	@Child(name="location", order=9, min=0, max=1)
	@ChildResource(types= {
		Location.class,
	})	
	private ResourceReference myLocation;
	
	@Child(name="patient", order=10, min=0, max=1)
	@ChildResource(types= {
		Patient.class,
	})	
	private ResourceReference myPatient;
	
	@Child(name="contact", type=ContactDt.class, order=11, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myContact;
	
	@Child(name="url", type=UriDt.class, order=12, min=0, max=1)	
	private UriDt myUrl;
	
	/**
	 * Gets the value(s) for <b>identifier</b> (Instance id from manufacturer, owner and others).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Instance id from manufacturer, owner and others)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (What kind of device this is).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A kind of this device
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (What kind of device this is)
	 *
     * <p>
     * <b>Definition:</b>
     * A kind of this device
     * </p> 
	 */
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>manufacturer</b> (Name of device manufacturer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name of the manufacturer
     * </p> 
	 */
	public StringDt getManufacturer() {  
		if (myManufacturer == null) {
			myManufacturer = new StringDt();
		}
		return myManufacturer;
	}

	/**
	 * Sets the value(s) for <b>manufacturer</b> (Name of device manufacturer)
	 *
     * <p>
     * <b>Definition:</b>
     * A name of the manufacturer
     * </p> 
	 */
	public void setManufacturer(StringDt theValue) {
		myManufacturer = theValue;
	}

 	/**
	 * Sets the value(s) for <b>manufacturer</b> (Name of device manufacturer)
	 *
     * <p>
     * <b>Definition:</b>
     * A name of the manufacturer
     * </p> 
	 */
	public void setManufacturer( String theString) {
		myManufacturer = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>model</b> (Model id assigned by the manufacturer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The "model" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type
     * </p> 
	 */
	public StringDt getModel() {  
		if (myModel == null) {
			myModel = new StringDt();
		}
		return myModel;
	}

	/**
	 * Sets the value(s) for <b>model</b> (Model id assigned by the manufacturer)
	 *
     * <p>
     * <b>Definition:</b>
     * The "model" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type
     * </p> 
	 */
	public void setModel(StringDt theValue) {
		myModel = theValue;
	}

 	/**
	 * Sets the value(s) for <b>model</b> (Model id assigned by the manufacturer)
	 *
     * <p>
     * <b>Definition:</b>
     * The "model" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type
     * </p> 
	 */
	public void setModel( String theString) {
		myModel = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>version</b> (Version number (i.e. software)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Version number (i.e. software))
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}

 	/**
	 * Sets the value(s) for <b>version</b> (Version number (i.e. software))
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware
     * </p> 
	 */
	public void setVersion( String theString) {
		myVersion = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>expiry</b> (Date of expiry of this device (if applicable)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date of expiry of this device (if applicable)
     * </p> 
	 */
	public DateDt getExpiry() {  
		if (myExpiry == null) {
			myExpiry = new DateDt();
		}
		return myExpiry;
	}

	/**
	 * Sets the value(s) for <b>expiry</b> (Date of expiry of this device (if applicable))
	 *
     * <p>
     * <b>Definition:</b>
     * Date of expiry of this device (if applicable)
     * </p> 
	 */
	public void setExpiry(DateDt theValue) {
		myExpiry = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>udi</b> (FDA Mandated Unique Device Identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * FDA Mandated Unique Device Identifier. Use the human readable information (the content that the user sees, which is sometimes different to the exact syntax represented in the barcode)  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm
     * </p> 
	 */
	public StringDt getUdi() {  
		if (myUdi == null) {
			myUdi = new StringDt();
		}
		return myUdi;
	}

	/**
	 * Sets the value(s) for <b>udi</b> (FDA Mandated Unique Device Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * FDA Mandated Unique Device Identifier. Use the human readable information (the content that the user sees, which is sometimes different to the exact syntax represented in the barcode)  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm
     * </p> 
	 */
	public void setUdi(StringDt theValue) {
		myUdi = theValue;
	}

 	/**
	 * Sets the value(s) for <b>udi</b> (FDA Mandated Unique Device Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * FDA Mandated Unique Device Identifier. Use the human readable information (the content that the user sees, which is sometimes different to the exact syntax represented in the barcode)  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm
     * </p> 
	 */
	public void setUdi( String theString) {
		myUdi = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>lotNumber</b> (Lot number of manufacture).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number assigned by the manufacturer
     * </p> 
	 */
	public StringDt getLotNumber() {  
		if (myLotNumber == null) {
			myLotNumber = new StringDt();
		}
		return myLotNumber;
	}

	/**
	 * Sets the value(s) for <b>lotNumber</b> (Lot number of manufacture)
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number assigned by the manufacturer
     * </p> 
	 */
	public void setLotNumber(StringDt theValue) {
		myLotNumber = theValue;
	}

 	/**
	 * Sets the value(s) for <b>lotNumber</b> (Lot number of manufacture)
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number assigned by the manufacturer
     * </p> 
	 */
	public void setLotNumber( String theString) {
		myLotNumber = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>owner</b> (Organization responsible for device).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An organization that is responsible for the provision and ongoing maintenance of the device
     * </p> 
	 */
	public ResourceReference getOwner() {  
		if (myOwner == null) {
			myOwner = new ResourceReference();
		}
		return myOwner;
	}

	/**
	 * Sets the value(s) for <b>owner</b> (Organization responsible for device)
	 *
     * <p>
     * <b>Definition:</b>
     * An organization that is responsible for the provision and ongoing maintenance of the device
     * </p> 
	 */
	public void setOwner(ResourceReference theValue) {
		myOwner = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>location</b> (Where the resource is found).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. "in/with the patient"), or a coded location
     * </p> 
	 */
	public ResourceReference getLocation() {  
		if (myLocation == null) {
			myLocation = new ResourceReference();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (Where the resource is found)
	 *
     * <p>
     * <b>Definition:</b>
     * The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. "in/with the patient"), or a coded location
     * </p> 
	 */
	public void setLocation(ResourceReference theValue) {
		myLocation = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> (If the resource is affixed to a person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Patient information, if the resource is affixed to a person
     * </p> 
	 */
	public ResourceReference getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReference();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (If the resource is affixed to a person)
	 *
     * <p>
     * <b>Definition:</b>
     * Patient information, if the resource is affixed to a person
     * </p> 
	 */
	public void setPatient(ResourceReference theValue) {
		myPatient = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>contact</b> (Details for human/organization for support).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public List<ContactDt> getContact() {  
		if (myContact == null) {
			myContact = new ArrayList<ContactDt>();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> (Details for human/organization for support)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public void setContact(List<ContactDt> theValue) {
		myContact = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>url</b> (Network address to contact device).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A network address on which the device may be contacted directly
     * </p> 
	 */
	public UriDt getUrl() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	/**
	 * Sets the value(s) for <b>url</b> (Network address to contact device)
	 *
     * <p>
     * <b>Definition:</b>
     * A network address on which the device may be contacted directly
     * </p> 
	 */
	public void setUrl(UriDt theValue) {
		myUrl = theValue;
	}

  


}