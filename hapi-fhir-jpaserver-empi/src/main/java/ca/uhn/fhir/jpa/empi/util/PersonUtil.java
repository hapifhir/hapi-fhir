package ca.uhn.fhir.jpa.empi.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Reference;

import java.util.List;
import java.util.stream.Stream;

public final class PersonUtil {
	private PersonUtil() {}

	public static Stream<IIdType> getLinks(FhirContext theFhirContext, IBaseResource thePerson) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = (Person)thePerson;
				return person.getLink().stream()
					.map(Person.PersonLinkComponent::getTarget)
					.map(IBaseReference::getReferenceElement)
					.map(IIdType::toUnqualifiedVersionless);
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + theFhirContext.getVersion().getVersion());
		}
	}

    public static boolean containsLinkTo(FhirContext theFhirContext, IBaseResource thePerson, IIdType theResourceId) {
		 Stream<IIdType> links = getLinks(theFhirContext, thePerson);
		 return links.anyMatch(link -> link.getValue().equals(theResourceId.getValue()));
    }

	public static void addLink(FhirContext theFhirContext, IBaseResource thePerson, IIdType theResourceId) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = (Person) thePerson;
				person.addLink().setTarget(new Reference(theResourceId));
				break;
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + theFhirContext.getVersion().getVersion());
		}
	}

	public static void removeLink(FhirContext theFhirContext, IBaseResource thePerson, IIdType theResourceId) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = (Person) thePerson;
				List<Person.PersonLinkComponent> links = person.getLink();
				links.removeIf(component -> component.hasTarget() && component.getTarget().getReference().equals(theResourceId.getValue()));
				break;
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + theFhirContext.getVersion().getVersion());
		}
	}

	public static IBaseResource createPersonFromPatient(FhirContext theFhirContext, IBaseResource thePatient) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = new Person();
				//FIXME EMPI populate from data from theResource
				return person;
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + theFhirContext.getVersion().getVersion());
		}

	}
}
