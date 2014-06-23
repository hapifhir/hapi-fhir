package example;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class PagingPatientProvider implements IResourceProvider {

	public IBundleProvider search(@RequiredParam(name=Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
		return null;
	}
	
	@Override
	public Class<? extends IResource> getResourceType() {
		return Patient.class;
	}

}
