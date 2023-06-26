package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.dstu3.model.Person;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.instance.model.api.ICompositeType;

/**
 * See #720
 */
@DatatypeDef(name = "ConsentTemplate")
public class Bug720Datatype extends Type implements ICompositeType {

	@Child(name = "contact", order = 8, min = 0, max = 1)
	@Description(shortDefinition = "responsible contact for this consent template")
	private Person contact = new Person();

	public Person getContact() {
		return contact;
	}


	@Override
	protected Type typedCopy() {
		throw new InternalErrorException("");
	}
}
