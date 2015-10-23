package embedded;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public class SomeResourceProvider implements IResourceProvider {

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Practitioner.class;
	}

	@Search()
	public List<Practitioner> findPractitionersByName(
			@RequiredParam(name = Practitioner.SP_NAME) final StringDt theName) {
		throw new UnprocessableEntityException(
				"Please provide more than 4 characters for the name");
	}

}
