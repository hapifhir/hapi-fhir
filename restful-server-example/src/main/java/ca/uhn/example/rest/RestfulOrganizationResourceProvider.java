package ca.uhn.example.rest;

import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * This is a very simple resource provider. See the RestfulPatientResourceProvider
 * for a more fancy example.
 */
public class RestfulOrganizationResourceProvider implements IResourceProvider {

	/**
	 * The getResourceType method comes from IResourceProvider, and must be overridden to indicate what type of resource this provider supplies.
	 */
	@Override
	public Class<Organization> getResourceType() {
		return Organization.class;
	}

	/**
	 * The "@Read" annotation indicates that this method supports the read operation. It takes one argument, the Resource type being returned.
	 * 
	 * @param theId
	 *            The read operation takes one parameter, which must be of type IdDt and must be annotated with the "@Read.IdParam" annotation.
	 * @return Returns a resource matching this identifier, or null if none exists.
	 */
	@Read()
	public Organization getResourceById(@IdParam IdDt theId) {
		
		/*
		 * We only support one organization, so the follwing
		 * exception causes an HTTP 404 response if the 
		 * ID of "1" isn't used.
		 */
		if (!"1".equals(theId.getValue())) {
			throw new ResourceNotFoundException(theId);
		}
		
		Organization retVal = new Organization();
		retVal.addIdentifier("urn:example:orgs", "FooOrganization");
		retVal.addAddress().addLine("123 Fake Street").setCity("Toronto");
		retVal.addTelecom().setUse(ContactUseEnum.WORK).setValue("1-888-123-4567");
		return retVal;
	}


}
