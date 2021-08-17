package ca.uhn.example.model;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Organization;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

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
	private CodeableConcept myBillingCode;

	
	/* *****************************
	 * Getters and setters
	 * *****************************/

	public CodeableConcept getBillingCode() {
		if (myBillingCode == null) {
			myBillingCode = new CodeableConcept();
		}
		return myBillingCode;
	}

	public void setBillingCode(CodeableConcept theBillingCode) {
		myBillingCode = theBillingCode;
	}
	
}
