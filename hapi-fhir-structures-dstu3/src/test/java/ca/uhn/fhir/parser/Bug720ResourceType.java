package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * See #720
 */
@ResourceDef(name = "Bug720ResourceType", profile = "http://example.com/StructureDefinition/dontuse#Bug720ResourceType")
public class Bug720ResourceType extends DomainResource {


	@Child(name = "templates", order = 4, min = 1, max = Child.MAX_UNLIMITED, type = {Bug720Datatype.class})
	@Description(shortDefinition = "import information for 1-n consent templates")
	private List<Type> templates = new ArrayList<Type>();

	@Override
	public DomainResource copy() {
		throw new InternalErrorException("");
	}

	@Override
	public ResourceType getResourceType() {
		throw new InternalErrorException("");
	}

	public List<Type> getTemplates() {
		return templates;
	}
}
