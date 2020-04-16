package ca.uhn.fhir.empi.util;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiProperties;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public final class PersonHelper {
	private static final Logger ourLog = getLogger(PersonHelper.class);

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IEmpiProperties myEmpiConfig;
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
	 * @param theSourceResource The Patient that will be used as the starting point for the person.
	 * @return the Person that is created.
	 */
	public IBaseResource createPersonFromEmpiTarget(IBaseResource theSourceResource) {
		String eidSystem = myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem();
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = new Person();
				CanonicalEID eidToApply;
				Optional<CanonicalEID> officialEID = CanonicalEID.extractFromResource(myFhirContext, eidSystem, theSourceResource);

				if (officialEID.isPresent()) {
					eidToApply = officialEID.get();
				} else {
					eidToApply =  myEIDHelper.createInternalEid();
				}
				person.addIdentifier(eidToApply.toR4());
				person.getMeta().addTag(buildEmpiManagedTag());
				copyPatientDataIntoPerson(theSourceResource, person);
				return person;
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	/**
	 * This will copy over all attributes that are copiable from Patient/Practitioner to Person.
	 *
	 * @param theBaseResource The incoming {@link Patient} or {@link Practitioner} who's data we want to copy into Person.
	 * @param thePerson The incoming {@link Person} who needs to have their data updated.
	 */
	private void copyPatientDataIntoPerson(IBaseResource theBaseResource, Person thePerson) {
		switch (myFhirContext.getResourceDefinition(theBaseResource).getName()) {
			case "Patient":
				Patient patient = (Patient)theBaseResource;
				thePerson.setName(patient.getName());
				thePerson.setAddress(patient.getAddress());
				thePerson.setTelecom(patient.getTelecom());
				thePerson.setBirthDate(patient.getBirthDate());
				thePerson.setGender(patient.getGender());
				thePerson.setPhoto(patient.getPhotoFirstRep());
				break;
			case "Practitioner":
				Practitioner practitioner = (Practitioner)theBaseResource;
				thePerson.setName(practitioner.getName());
				thePerson.setAddress(practitioner.getAddress());
				thePerson.setTelecom(practitioner.getTelecom());
				thePerson.setBirthDate(practitioner.getBirthDate());
				thePerson.setGender(practitioner.getGender());
				thePerson.setPhoto(practitioner.getPhotoFirstRep());
				break;
		}
	}

	private Coding buildEmpiManagedTag() {
		Coding empiManagedCoding = new Coding();
		empiManagedCoding.setSystem(Constants.SYSTEM_EMPI_MANAGED);
		empiManagedCoding.setCode(Constants.CODE_HAPI_EMPI_MANAGED);
		empiManagedCoding.setDisplay("This Person can only be modified by Smile CDR's EMPI system.");
		return empiManagedCoding;
	}

	public IBaseResource updatePersonFromEmpiTarget(IBaseResource thePerson, IBaseResource theEmpiTarget) {
		//This handles overwriting an automatically assigned EID if a patient that links is coming in with an official EID.
		Person person = ((Person)thePerson);
		Optional<CanonicalEID> incomingTargetEid = myEIDHelper.getExternalEid(theEmpiTarget);
		Optional<CanonicalEID> personOfficialEid = myEIDHelper.getExternalEid(thePerson);

		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				if (incomingTargetEid.isPresent()) {
					//The person has no EID. This should be impossible given that we auto-assign an EID at creation time.
					if (!personOfficialEid.isPresent()) {
						ourLog.debug("Incoming resource:{} with EID {} is applying this EID to its related Person, as this person does not yet have an external EID", theEmpiTarget.getIdElement().getValueAsString(), incomingTargetEid.get().getValue());
						person.addIdentifier(incomingTargetEid.get().toR4());
					} else if (personOfficialEid.isPresent() && eidsMatch(personOfficialEid, incomingTargetEid)){
						ourLog.debug("incoming resource:{} with EID {} does not need to overwrite person, as this EID is already present", theEmpiTarget.getIdElement().getValueAsString(), incomingTargetEid.get().getValue());
					} else {
						throw new IllegalArgumentException("This would create a duplicate person!");
					}
				}
			default:
				// FIXME EMPI moar versions
				break;
		}
		return thePerson;
	}

	private boolean eidsMatch(Optional<CanonicalEID> thePersonOfficialEid, Optional<CanonicalEID> theIncomingPatientEid) {
		return thePersonOfficialEid.isPresent()
			&& theIncomingPatientEid.isPresent()
			&& Objects.equals(thePersonOfficialEid.get().getValue(), theIncomingPatientEid.get().getValue());
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
