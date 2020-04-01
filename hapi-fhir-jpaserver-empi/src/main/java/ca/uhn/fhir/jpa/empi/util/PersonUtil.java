package ca.uhn.fhir.jpa.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.config.IEmpiConfig;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Lazy
@Service
public final class PersonUtil {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IEmpiConfig myEmpiConfig;

	private PersonUtil(){}

	@VisibleForTesting
	PersonUtil(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public Stream<IIdType> getLinks(IBaseResource thePerson) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = (Person)thePerson;
				return person.getLink().stream()
					.map(Person.PersonLinkComponent::getTarget)
					.map(IBaseReference::getReferenceElement)
					.map(IIdType::toUnqualifiedVersionless);
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

    public boolean containsLinkTo(IBaseResource thePerson, IIdType theResourceId) {
		 Stream<IIdType> links = getLinks(thePerson);
		 return links.anyMatch(link -> link.getValue().equals(theResourceId.getValue()));
    }

	public void addLink(IBaseResource thePerson, IIdType theResourceId) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = (Person) thePerson;
				person.addLink().setTarget(new Reference(theResourceId));
				break;
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	public void removeLink(IBaseResource thePerson, IIdType theResourceId) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = (Person) thePerson;
				List<Person.PersonLinkComponent> links = person.getLink();
				links.removeIf(component -> component.hasTarget() && component.getTarget().getReference().equals(theResourceId.getValue()));
				break;
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	/**
	 * Create a Person from a given patient. This will carry over the Patient's EID if it exists. If it does not exist,
	 * a randomly generated UUID EID will be created.
	 *
	 * @param thePatient The Patient that will be used as the starting point for the person.
	 * @return the Person that is created.
	 */
	public IBaseResource createPersonFromPatient(IBaseResource thePatient) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = new Person();
				person.addIdentifier(createEIDIdentifierForPerson(thePatient));
				//FIXME EMPI populate from data from theResource
				return person;
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	public Identifier createEIDIdentifierForPerson(IBaseResource thePatient) {
		Optional<Identifier> first = ((Patient) thePatient).getIdentifier().stream().filter(id -> id.getSystem().equalsIgnoreCase(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())).findFirst();
		if (first.isPresent()) {
			Identifier identifier = first.get();
			identifier.setUse(Identifier.IdentifierUse.OFFICIAL);
			return identifier;
		} else {
			Identifier identifier = new Identifier();
			identifier.setSystem(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem());
			identifier.setValue(UUID.randomUUID().toString());
			identifier.setUse(Identifier.IdentifierUse.TEMP);
			return identifier;
		}
	}

	public String readEIDFromResource(IBaseResource theBaseResource) {
		//FIXME EMPI
		return "";
	}
}
