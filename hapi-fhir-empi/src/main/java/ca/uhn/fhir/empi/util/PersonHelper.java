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
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.model.CanonicalIdentityAssuranceLevel;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
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
	private IEmpiSettings myEmpiConfig;
	@Autowired
	private EIDHelper myEIDHelper;

	private final FhirContext myFhirContext;

	@Autowired
	public PersonHelper(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	/**
	 * Given a Person, extract all {@link IIdType}s for the linked targets.
	 * @param thePerson the Person to extract link IDs from.
	 *
	 * @return a Stream of {@link IIdType}.
	 */
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

	/**
	 * Determine whether or not the given {@link IBaseResource} person contains a link to a particular {@link IIdType}
	 *
	 * @param thePerson The person to check
	 * @param theResourceId The ID to check.
	 *
	 * @return A boolean indicating whether or not there was a contained link.
	 */
    public boolean containsLinkTo(IBaseResource thePerson, IIdType theResourceId) {
		 Stream<IIdType> links = getLinks(thePerson);
		 return links.anyMatch(link -> link.getValue().equals(theResourceId.getValue()));
    }

	/**
	 * Create or update a link from source {@link IBaseResource} to the target {@link IIdType}, with the given {@link CanonicalIdentityAssuranceLevel}.
	 *  @param thePerson The person who's link needs to be updated.
	 * @param theResourceId The target of the link
	 * @param canonicalAssuranceLevel The level of certainty of this link.
	 * @param theTransactionLogMessages
	 */
	public void addOrUpdateLink(IBaseResource thePerson, IIdType theResourceId, CanonicalIdentityAssuranceLevel canonicalAssuranceLevel, TransactionLogMessages theTransactionLogMessages) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				handleLinkUpdateR4(thePerson, theResourceId, canonicalAssuranceLevel, theTransactionLogMessages);
				break;
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	private void handleLinkUpdateR4(IBaseResource thePerson, IIdType theResourceId, CanonicalIdentityAssuranceLevel canonicalAssuranceLevel, TransactionLogMessages theTransactionLogMessages) {
		if (canonicalAssuranceLevel == null) {
			return;
		}

		Person person = (Person) thePerson;
		if (!containsLinkTo(thePerson, theResourceId)) {
			person.addLink().setTarget(new Reference(theResourceId)).setAssurance(canonicalAssuranceLevel.toR4());
			TransactionLogMessages.addMessage(theTransactionLogMessages, ("Creating new link from " + (StringUtils.isBlank(thePerson.getIdElement().getValue()) ? "new Person" : thePerson.getIdElement().toUnqualifiedVersionless()) + " -> " + theResourceId.toUnqualifiedVersionless() + " with IdentityAssuranceLevel: " + canonicalAssuranceLevel.name()));
		} else {
			person.getLink().stream()
				.filter(link -> link.getTarget().getReference().equalsIgnoreCase(theResourceId.getValue()))
				.findFirst()
				.ifPresent(link -> {
					TransactionLogMessages.addMessage(theTransactionLogMessages, ("Updating link from " + thePerson.getIdElement().toUnqualifiedVersionless() + " -> " + theResourceId.toUnqualifiedVersionless() + ". Changing IdentityAssuranceLevel: " + link.getAssurance().toCode() + " -> " + canonicalAssuranceLevel.name()));
					link.setAssurance(canonicalAssuranceLevel.toR4());
			});
		}
	}

	/**
	 * Removes a link from the given {@link IBaseResource} to the target {@link IIdType}.
	 *  @param thePerson The person to remove the link from.
	 * @param theResourceId The target ID to remove.
	 * @param theTransactionLogMessages
	 */
	public void removeLink(IBaseResource thePerson, IIdType theResourceId, TransactionLogMessages theTransactionLogMessages) {
		if (!containsLinkTo(thePerson, theResourceId)) {
			return;
		}
		TransactionLogMessages.addMessage(theTransactionLogMessages, "Removing PersonLinkComponent from " + thePerson.getIdElement().toUnqualifiedVersionless() + " -> " + theResourceId.toUnqualifiedVersionless());
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
				CanonicalEID eidToApply = myEIDHelper.getExternalEid(theSourceResource).orElse(myEIDHelper.createHapiEid());
				person.addIdentifier(eidToApply.toR4());
				person.getMeta().addTag(buildEmpiManagedTag());
				copyEmpiTargetDataIntoPerson(theSourceResource, person);
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
	private void copyEmpiTargetDataIntoPerson(IBaseResource theBaseResource, Person thePerson) {
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
		empiManagedCoding.setSystem(EmpiConstants.SYSTEM_EMPI_MANAGED);
		empiManagedCoding.setCode(EmpiConstants.CODE_HAPI_EMPI_MANAGED);
		empiManagedCoding.setDisplay("This Person can only be modified by Smile CDR's EMPI system.");
		return empiManagedCoding;
	}

	/**
	 * Update a Person's EID based on the incoming target resource. If the incoming resource has an external EID, it is applied
	 * to the Person, unless that person already has an external EID which does not match, in which case throw {@link IllegalArgumentException}
	 *
	 * @param thePerson The person to update the external EID on.
	 * @param theEmpiTarget The target we will retrieve the external EID from.
	 *
	 * @return the modified {@link IBaseResource} representing the person.
	 */
	public IBaseResource updatePersonExternalEidFromEmpiTarget(IBaseResource thePerson, IBaseResource theEmpiTarget) {
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
					} else if (personOfficialEid.isPresent() && myEIDHelper.eidsMatch(personOfficialEid.get(), incomingTargetEid.get())){
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

	/**
	 * An incoming resource is a potential duplicate if it matches a Patient that has a Person with an official EID, but
	 * the incoming resource also has an EID.
	 *
	 * @param theExistingPerson
	 * @param theComparingPerson
	 * @return
	 */
	public boolean isPotentialDuplicate(IBaseResource theExistingPerson, IBaseResource theComparingPerson) {
		Optional<CanonicalEID> firstEid = myEIDHelper.getExternalEid(theExistingPerson);
		Optional<CanonicalEID> secondEid = myEIDHelper.getExternalEid(theComparingPerson);
		return firstEid.isPresent() && secondEid.isPresent() && !Objects.equals(firstEid.get().getValue(), secondEid.get().getValue());
	}


}
