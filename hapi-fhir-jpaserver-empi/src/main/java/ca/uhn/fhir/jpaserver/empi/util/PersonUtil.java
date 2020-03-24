package ca.uhn.fhir.jpaserver.empi.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Person;

import java.util.List;
import java.util.stream.Collectors;

public final class PersonUtil {
	private PersonUtil() {}

	public static List<IIdType> getLinks(FhirContext theFhirContext, IBaseResource thePerson) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = (Person)thePerson;
				return person.getLink().stream()
					.map(Person.PersonLinkComponent::getTarget)
					.map(IBaseReference::getReferenceElement)
					.collect(Collectors.toList());
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + theFhirContext.getVersion().getVersion());
		}
	}
}
