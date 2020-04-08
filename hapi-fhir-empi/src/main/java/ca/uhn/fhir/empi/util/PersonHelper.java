package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiConfig;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static ca.uhn.fhir.rest.api.Constants.*;

@Lazy
@Service
public final class PersonHelper {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IEmpiConfig myEmpiConfig;
	@Autowired
	private EIDHelper myEIDHelper;

	private PersonHelper(){}

	@VisibleForTesting
	PersonHelper(FhirContext theFhirContext) {
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
		String eidSystem = myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem();
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = new Person();
				CanonicalEID eidToApply;
				Optional<CanonicalEID> officialEID = CanonicalEID.extractFromResource(myFhirContext, eidSystem, thePatient);

				if (officialEID.isPresent()) {
					eidToApply = officialEID.get();
				} else {
					eidToApply =  myEIDHelper.createInternalEid();
				}
				person.addIdentifier(eidToApply.toR4());
				person.getMeta().addTag(buildEmpiManagedTag());
				copyPatientDataIntoPerson((Patient)thePatient, person);
				return person;
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	/**
	 * This will copy over all attributes that are copiable from Patient/Practitioner to Person.
	 * TODO Is Shallow copying OK? Seemsgood.
	 *
	 * @param thePatient The incoming {@link Patient} who's data we want to copy into Person.
	 * @param thePerson The incoming {@link Person} who needs to have their data updated.
	 */
	private void copyPatientDataIntoPerson(Patient thePatient, Person thePerson) {
		thePerson.setName(thePatient.getName());
		thePerson.setAddress(thePatient.getAddress());
		thePerson.setTelecom(thePatient.getTelecom());
		thePerson.setBirthDate(thePatient.getBirthDate());
		thePerson.setGender(thePatient.getGender());
		thePerson.setPhoto(thePatient.getPhotoFirstRep());
	}

	private Coding buildEmpiManagedTag() {
		Coding empiManagedCoding = new Coding();
		empiManagedCoding.setSystem(SYSTEM_EMPI_MANAGED);
		empiManagedCoding.setCode(CODE_HAPI_EMPI_MANAGED);
		empiManagedCoding.setDisplay("This Person can only be modified by Smile CDR's EMPI system.");
		return empiManagedCoding;
	}

	public IBaseResource updatePersonFromPatient(IBaseResource thePerson, IBaseResource thePatient) {
		//This handles overwriting an automatically assigned EID if a patient that links is coming in with an official EID.
		Person person = ((Person)thePerson);
		Optional<CanonicalEID> incomingPatientEid = myEIDHelper.getExternalEid(thePatient);
		Optional<CanonicalEID> personOfficialEid = myEIDHelper.getExternalEid(thePerson);

		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				if (incomingPatientEid.isPresent()) {
					//The person has no EID. This should be impossible given that we auto-assign an EID at creation time.
					if (!personOfficialEid.isPresent()) {
						person.addIdentifier(incomingPatientEid.get().toR4());
					} else {
						throw new IllegalArgumentException("This should create  duplicate person");
					}
				}
			default:
				// FIXME EMPI moar versions
				break;
		}
		return thePerson;
	}

	/**
	 * An incoming resource is a potential duplicate if it matches a Patient that has a Person with an official EID, but
	 * the incoming resource also has an EID.
	 *
	 * @param theExistingPerson
	 * @param theComparingPerson
	 * @return
	 */
	public boolean isPotentialDuplicate(IBaseResource theExistingPerson, IBaseResource theComparingPerson) {
		String enterpriseEIDSystem = myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem();
		Optional<CanonicalEID> firstEid = CanonicalEID.extractFromResource(myFhirContext, enterpriseEIDSystem, theExistingPerson);
		Optional<CanonicalEID> secondEid = CanonicalEID.extractFromResource(myFhirContext, enterpriseEIDSystem, theComparingPerson);
		return firstEid.isPresent() && secondEid.isPresent() && !Objects.equals(firstEid.get().getValue(), secondEid.get().getValue());
	}


}
