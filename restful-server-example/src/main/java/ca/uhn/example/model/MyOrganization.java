package ca.uhn.example.model;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IExtension;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.util.ElementUtil;

/**
 * This is an example of a customized model class. Essentially we have taken the
 * built-in Organization resource class, and extended with a custom extension.
 */
@ResourceDef(name = "Organization")
public class MyOrganization extends Organization {

	/* *****************************
	 * Fields
	 * *****************************/

	/**
	 * This is a basic extension, with a DataType value (in this case, String)
	 */
	@Description(shortDefinition = "Contains a simple code indicating the billing code for this organization")
	@Extension(url = "http://foo#billingCode", isModifier = false, definedLocally = true)
	@Child(name = "billingCode")
	private CodeDt myBillingCode;

	/**
	 * This is a composite extension, containing further extensions instead of
	 * a value. The class "EmergencyContact" is defined at the bottom
	 * of this file.
	 */
	@Description(shortDefinition="Contains emergency contact details")
	@Extension(url = "http://foo#emergencyContact", isModifier = false, definedLocally = true)
	@Child(name = "emergencyContact", min=0, max=Child.MAX_UNLIMITED)
	private List<EmergencyContact> myEmergencyContact;
	
	/* *****************************
	 * Getters and setters
	 * *****************************/

	public List<EmergencyContact> getEmergencyContact() {
		if (myEmergencyContact==null) {
			myEmergencyContact=new ArrayList<EmergencyContact>();
		}
		return myEmergencyContact;
	}

	public void setEmergencyContact(List<EmergencyContact> theEmergencyContact) {
		myEmergencyContact = theEmergencyContact;
	}

	public CodeDt getBillingCode() {
		if (myBillingCode == null) {
			myBillingCode = new CodeDt();
		}
		return myBillingCode;
	}

	public void setBillingCode(CodeDt theBillingCode) {
		myBillingCode = theBillingCode;
	}

	/* *****************************
	 * Boilerplate methods- Hopefully these will be removed or made optional
	 * in a future version of HAPI but for now they need to be added to all block
	 * types. These two methods follow a simple pattern where a utility method from
	 * ElementUtil is called and all fields are passed in.
	 * *****************************/
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ElementUtil.allPopulatedChildElements(theType, super.getAllPopulatedChildElementsOfType(theType), myBillingCode, myEmergencyContact);
	}

	@Override
	public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(myBillingCode, myEmergencyContact);
	}

	/**
	 * This "block definition" defines an extension type with multiple child extensions.
	 * It is referenced by the field myEmergencyContact above.
	 */
	@Block
	public static class EmergencyContact extends BaseIdentifiableElement implements IExtension
	{
		/* *****************************
		 * Fields
		 * *****************************/	
		
		/**
		 * This is a primitive datatype extension
		 */
		@Description(shortDefinition = "Should be set to true if the contact is active")
		@Extension(url = "http://foo#emergencyContactActive", isModifier = false, definedLocally = true)
		@Child(name = "active")
		private BooleanDt myActive;
				
		/**
		 * This is a composite datatype extension
		 */
		@Description(shortDefinition = "Contains the actual contact details")
		@Extension(url = "http://foo#emergencyContactContact", isModifier = false, definedLocally = true)
		@Child(name = "contact")
		private ContactPointDt myContact;

		/* *****************************
		 * Getters and setters
		 * *****************************/

		public BooleanDt getActive() {
			if (myActive == null) {
				myActive = new BooleanDt();
			}
			return myActive;
		}

		public void setActive(BooleanDt theActive) {
			myActive = theActive;
		}

		public ContactPointDt getContact() {
			if (myContact == null) {
				myContact = new ContactPointDt();
			}
			return myContact;
		}

		public void setContact(ContactPointDt theContact) {
			myContact = theContact;
		}

		/* *****************************
		 * Boilerplate methods- Hopefully these will be removed or made optional
		 * in a future version of HAPI but for now they need to be added to all block
		 * types. These two methods follow a simple pattern where a utility method from
		 * ElementUtil is called and all fields are passed in.
		 * *****************************/
		
		@Override
		public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
			return ElementUtil.allPopulatedChildElements(theType, myActive, myContact);
		}

		@Override
		public boolean isEmpty() {
			return ElementUtil.isEmpty(myActive, myContact);
		}

		
	}
	
}
