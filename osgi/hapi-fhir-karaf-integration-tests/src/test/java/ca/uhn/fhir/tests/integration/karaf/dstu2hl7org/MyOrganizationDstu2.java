package ca.uhn.fhir.tests.integration.karaf.dstu2hl7org;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.dstu2.model.Organization;

@ResourceDef()
public class MyOrganizationDstu2 extends Organization {

	private static final long serialVersionUID = 1L;

}
