package ca.uhn.fhirtest.rp;

import ca.uhn.fhir.jpa.provider.JpaResourceProviderDstu2;
import ca.uhn.fhir.model.api.IResource;

public class FhirtestBaseResourceProviderDstu2<T extends IResource> extends JpaResourceProviderDstu2<T> {

}
