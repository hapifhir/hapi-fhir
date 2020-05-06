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
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.model.CanonicalIdentityAssuranceLevel;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PersonHelper {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

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
				Person personR4 = (Person)thePerson;
				return personR4.getLink().stream()
					.map(Person.PersonLinkComponent::getTarget)
					.map(IBaseReference::getReferenceElement)
					.map(IIdType::toUnqualifiedVersionless);
			case DSTU3:
				org.hl7.fhir.dstu3.model.Person personStu3 = (org.hl7.fhir.dstu3.model.Person)thePerson;
				return personStu3.getLink().stream()
					.map(org.hl7.fhir.dstu3.model.Person.PersonLinkComponent::getTarget)
					.map(IBaseReference::getReferenceElement)
					.map(IIdType::toUnqualifiedVersionless);
			default:
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
	public void addOrUpdateLink(IBaseResource thePerson, IIdType theResourceId, @Nonnull CanonicalIdentityAssuranceLevel canonicalAssuranceLevel, TransactionLogMessages theTransactionLogMessages) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				handleLinkUpdateR4(thePerson, theResourceId, canonicalAssuranceLevel, theTransactionLogMessages);
				break;
			case DSTU3:
				handleLinkUpdateDSTU3(thePerson, theResourceId, canonicalAssuranceLevel, theTransactionLogMessages);
				break;
			default:
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	private void handleLinkUpdateDSTU3(IBaseResource thePerson, IIdType theResourceId, CanonicalIdentityAssuranceLevel theCanonicalAssuranceLevel, TransactionLogMessages theTransactionLogMessages) {
		if (theCanonicalAssuranceLevel == null) {
			ourLog.warn("Refusing to update or add a link without an Assurance Level.");
			return;
		}

		org.hl7.fhir.dstu3.model.Person person = (org.hl7.fhir.dstu3.model.Person) thePerson;
		if (!containsLinkTo(thePerson, theResourceId)) {
			person.addLink().setTarget(new org.hl7.fhir.dstu3.model.Reference(theResourceId)).setAssurance(theCanonicalAssuranceLevel.toDstu3());
			logLinkAddMessage(thePerson, theResourceId, theCanonicalAssuranceLevel, theTransactionLogMessages);
		} else {
			person.getLink().stream()
				.filter(link -> link.getTarget().getReference().equalsIgnoreCase(theResourceId.getValue()))
				.findFirst()
				.ifPresent(link -> {
					logLinkUpdateMessage(thePerson, theResourceId, theCanonicalAssuranceLevel, theTransactionLogMessages, link.getAssurance().toCode());
					link.setAssurance(theCanonicalAssuranceLevel.toDstu3());
				});
		}
	}

	private void logLinkAddMessage(IBaseResource thePerson, IIdType theResourceId, CanonicalIdentityAssuranceLevel theCanonicalAssuranceLevel, TransactionLogMessages theTransactionLogMessages) {
		TransactionLogMessages.addMessage(theTransactionLogMessages, ("Creating new link from " + (StringUtils.isBlank(thePerson.getIdElement().toUnqualifiedVersionless().getValue()) ? "new Person" : thePerson.getIdElement().toUnqualifiedVersionless()) + " -> " + theResourceId.toUnqualifiedVersionless() + " with IdentityAssuranceLevel: " + theCanonicalAssuranceLevel.name()));
	}

	private void logLinkUpdateMessage(IBaseResource thePerson, IIdType theResourceId, CanonicalIdentityAssuranceLevel canonicalAssuranceLevel, TransactionLogMessages theTransactionLogMessages, String theOriginalAssuranceLevel) {
		TransactionLogMessages.addMessage(theTransactionLogMessages, ("Updating link from " + thePerson.getIdElement().toUnqualifiedVersionless() + " -> " + theResourceId.toUnqualifiedVersionless() + ". Changing IdentityAssuranceLevel: " + theOriginalAssuranceLevel + " -> " + canonicalAssuranceLevel.name()));
	}

	private void handleLinkUpdateR4(IBaseResource thePerson, IIdType theResourceId, CanonicalIdentityAssuranceLevel canonicalAssuranceLevel, TransactionLogMessages theTransactionLogMessages) {
		if (canonicalAssuranceLevel == null) {
			ourLog.warn("Refusing to update or add a link without an Assurance Level.");
			return;
		}

		Person person = (Person) thePerson;
		if (!containsLinkTo(thePerson, theResourceId)) {
			person.addLink().setTarget(new Reference(theResourceId)).setAssurance(canonicalAssuranceLevel.toR4());
			logLinkAddMessage(thePerson, theResourceId, canonicalAssuranceLevel, theTransactionLogMessages);
		} else {
			person.getLink().stream()
				.filter(link -> link.getTarget().getReference().equalsIgnoreCase(theResourceId.getValue()))
				.findFirst()
				.ifPresent(link -> {
					logLinkUpdateMessage(thePerson, theResourceId, canonicalAssuranceLevel, theTransactionLogMessages, link.getAssurance().toCode());
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
			case DSTU3:
				org.hl7.fhir.dstu3.model.Person personDstu3 = (org.hl7.fhir.dstu3.model.Person)thePerson;
				personDstu3.getLink().removeIf(component -> component.hasTarget() && component.getTarget().getReference().equalsIgnoreCase(theResourceId.getValue()));
				break;
			default:
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
		List<CanonicalEID> eidsToApply = myEIDHelper.getExternalEid(theSourceResource);

		if (eidsToApply.isEmpty()) {
			eidsToApply.add(myEIDHelper.createHapiEid());
		}
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				Person personR4 = new Person();
				eidsToApply.forEach(eid -> personR4.addIdentifier(eid.toR4()));
				personR4.getMeta().addTag((Coding)buildEmpiManagedTag());
				copyEmpiTargetDataIntoPerson(theSourceResource, personR4);
				return personR4;
			case DSTU3:
				org.hl7.fhir.dstu3.model.Person personDSTU3 = new org.hl7.fhir.dstu3.model.Person();
				eidsToApply.forEach(eid -> personDSTU3.addIdentifier(eid.toDSTU3()));
				personDSTU3.getMeta().addTag((org.hl7.fhir.dstu3.model.Coding)buildEmpiManagedTag());
				copyEmpiTargetDataIntoPerson(theSourceResource, personDSTU3);
				return personDSTU3;
			default:
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	/**
	 * This will copy over all attributes that are copiable from Patient/Practitioner to Person.
	 *
	 * @param theBaseResource The incoming {@link Patient} or {@link Practitioner} who's data we want to copy into Person.
	 * @param thePerson The incoming {@link Person} who needs to have their data updated.
	 */
	private void copyEmpiTargetDataIntoPerson(IBaseResource theBaseResource,  IBaseResource thePerson) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				copyR4TargetInformation(theBaseResource, thePerson);
				break;
			case DSTU3:
				copyDSTU3TargetInformation(theBaseResource, thePerson);
				break;
			default:
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	private void copyR4TargetInformation(IBaseResource theBaseResource, IBaseResource thePerson) {
		Person person = (Person) thePerson;
		switch (myFhirContext.getResourceType(theBaseResource)) {
			case "Patient":
				Patient patient = (Patient)theBaseResource;
				person.setName(patient.getName());
				person.setAddress(patient.getAddress());
				person.setTelecom(patient.getTelecom());
				person.setBirthDate(patient.getBirthDate());
				person.setGender(patient.getGender());
				person.setPhoto(patient.getPhotoFirstRep());
				break;
			case "Practitioner":
				Practitioner practitioner = (Practitioner)theBaseResource;
				person.setName(practitioner.getName());
				person.setAddress(practitioner.getAddress());
				person.setTelecom(practitioner.getTelecom());
				person.setBirthDate(practitioner.getBirthDate());
				person.setGender(practitioner.getGender());
				person.setPhoto(practitioner.getPhotoFirstRep());
				break;
			default:
				throw new UnsupportedOperationException("EMPI targets are limited to Practitioner/Patient. This is a : " + myFhirContext.getResourceType(theBaseResource));
		}
	}
	private void copyDSTU3TargetInformation(IBaseResource theBaseResource, IBaseResource thePerson) {
		org.hl7.fhir.dstu3.model.Person person = (org.hl7.fhir.dstu3.model.Person)thePerson;
		switch (myFhirContext.getResourceType(theBaseResource)) {
			case "Patient":
				org.hl7.fhir.dstu3.model.Patient patient = (org.hl7.fhir.dstu3.model.Patient)theBaseResource;
				person.setName(patient.getName());
				person.setAddress(patient.getAddress());
				person.setTelecom(patient.getTelecom());
				person.setBirthDate(patient.getBirthDate());
				person.setGender(patient.getGender());
				person.setPhoto(patient.getPhotoFirstRep());
				break;
			case "Practitioner":
				org.hl7.fhir.dstu3.model.Practitioner practitioner = (org.hl7.fhir.dstu3.model.Practitioner)theBaseResource;
				person.setName(practitioner.getName());
				person.setAddress(practitioner.getAddress());
				person.setTelecom(practitioner.getTelecom());
				person.setBirthDate(practitioner.getBirthDate());
				person.setGender(practitioner.getGender());
				person.setPhoto(practitioner.getPhotoFirstRep());
				break;
			default:
				throw new UnsupportedOperationException("EMPI targets are limited to Practitioner/Patient. This is a : " + myFhirContext.getResourceType(theBaseResource));
		}
	}

	private IBaseCoding buildEmpiManagedTag() {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				Coding empiManagedCoding = new Coding();
				empiManagedCoding.setSystem(EmpiConstants.SYSTEM_EMPI_MANAGED);
				empiManagedCoding.setCode(EmpiConstants.CODE_HAPI_EMPI_MANAGED);
				empiManagedCoding.setDisplay(EmpiConstants.DISPLAY_HAPI_EMPI_MANAGED);
				return empiManagedCoding;
			case DSTU3:
				org.hl7.fhir.dstu3.model.Coding empiManagedCodingDstu3 = new org.hl7.fhir.dstu3.model.Coding();
				empiManagedCodingDstu3.setSystem(EmpiConstants.SYSTEM_EMPI_MANAGED);
				empiManagedCodingDstu3.setCode(EmpiConstants.CODE_HAPI_EMPI_MANAGED);
				empiManagedCodingDstu3.setDisplay(EmpiConstants.DISPLAY_HAPI_EMPI_MANAGED);
				return empiManagedCodingDstu3;
			default:
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());

		}
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
		List<CanonicalEID> incomingTargetEid = myEIDHelper.getExternalEid(theEmpiTarget);
		List<CanonicalEID> personOfficialEid = myEIDHelper.getExternalEid(thePerson);

		if (!incomingTargetEid.isEmpty()) {
			//The person has no EID. This should be impossible given that we auto-assign an EID at creation time.
			if (personOfficialEid.isEmpty()) {
				ourLog.debug("Incoming resource:{} with EID {} is applying this EID to its related Person, as this person does not yet have an external EID", theEmpiTarget.getIdElement().toUnqualifiedVersionless(), incomingTargetEid.stream().map(eid -> eid.toString()).collect(Collectors.joining(",")));
				addCanonicalEidsToPersonIfAbsent(thePerson, incomingTargetEid);
			} else if (!personOfficialEid.isEmpty() && myEIDHelper.eidMatchExists(personOfficialEid, incomingTargetEid)){
				//FIXME GGG handle multiple new EIDs. What are the rules here?
				ourLog.debug("incoming resource:{} with EIDs {} does not need to overwrite person, as this EID is already present",
					theEmpiTarget.getIdElement().toUnqualifiedVersionless(),
					incomingTargetEid.stream().map(CanonicalEID::toString).collect(Collectors.joining(",")));
			} else {
				throw new IllegalArgumentException("This would create a duplicate person!");
			}
		}
		return thePerson;
	}

	public IBaseResource overwriteExternalEids(IBaseResource thePerson, List<CanonicalEID> theNewEid) {
		clearExternalEids(thePerson);
		addCanonicalEidsToPersonIfAbsent(thePerson, theNewEid);
		return thePerson;
	}

	private void clearExternalEids(IBaseResource thePerson) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				Person personR4 = (Person) thePerson;
				personR4.getIdentifier().removeIf(theIdentifier ->  theIdentifier.getSystem().equalsIgnoreCase(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem()));
				break;
			case DSTU3:
				org.hl7.fhir.dstu3.model.Person personDstu3 = (org.hl7.fhir.dstu3.model.Person) thePerson;
				personDstu3.getIdentifier().removeIf(theIdentifier ->  theIdentifier.getSystem().equalsIgnoreCase(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem()));
				break;
			default:
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	private void addCanonicalEidsToPersonIfAbsent(IBaseResource thePerson, List<CanonicalEID> theIncomingTargetEid) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				theIncomingTargetEid.forEach(eid -> addIdentifierIfAbsent((Person)thePerson, eid.toR4()));
				break;
			case DSTU3:
				theIncomingTargetEid.forEach(eid -> addIdentifierIfAbsent((org.hl7.fhir.dstu3.model.Person)thePerson, eid.toDSTU3()));
				break;
			default:
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	/**
	 * To avoid adding duplicate
	 * @param thePerson
	 * @param theIdentifier
	 */
	private void addIdentifierIfAbsent(org.hl7.fhir.dstu3.model.Person thePerson, org.hl7.fhir.dstu3.model.Identifier theIdentifier) {
		Optional<org.hl7.fhir.dstu3.model.Identifier> first = thePerson.getIdentifier().stream().filter(identifier -> identifier.getSystem().equals(theIdentifier.getSystem())).filter(identifier -> identifier.getValue().equals(theIdentifier.getValue())).findFirst();
		if (first.isPresent()) {
			return;
		} else {
			thePerson.addIdentifier(theIdentifier);
		}
	}

	private void addIdentifierIfAbsent(Person thePerson, Identifier theIdentifier) {
		Optional<Identifier> first = thePerson.getIdentifier().stream().filter(identifier -> identifier.getSystem().equals(theIdentifier.getSystem())).filter(identifier -> identifier.getValue().equals(theIdentifier.getValue())).findFirst();
		if (first.isPresent()) {
			return;
		} else {
			thePerson.addIdentifier(theIdentifier);
		}
	}


	public void mergePersonFields(IBaseResource thePersonToDelete, IBaseResource thePersonToKeep) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				mergeR4PersonFields(thePersonToDelete, thePersonToKeep);
				break;
			case DSTU3:
				mergeDstu3PersonFields(thePersonToDelete, thePersonToKeep);
				break;
			default:
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	private void mergeR4PersonFields(IBaseResource thePersonToDelete, IBaseResource thePersonToKeep) {
		Person fromPerson = (Person)thePersonToDelete;
		Person toPerson = (Person)thePersonToKeep;
		if (!toPerson.hasName()) {
			toPerson.setName(fromPerson.getName());
		}
		if (!toPerson.hasAddress()) {
			toPerson.setAddress(fromPerson.getAddress());
		}
		if (!toPerson.hasTelecom()) {
			toPerson.setTelecom(fromPerson.getTelecom());
		}
		if (!toPerson.hasBirthDate()) {
			toPerson.setBirthDate(fromPerson.getBirthDate());
		}
		if (!toPerson.hasGender()) {
			toPerson.setGender(fromPerson.getGender());
		}
		if (!toPerson.hasPhoto()) {
			toPerson.setPhoto(fromPerson.getPhoto());
		}
	}


	private void mergeDstu3PersonFields(IBaseResource thePersonToDelete, IBaseResource thePersonToKeep) {
		org.hl7.fhir.dstu3.model.Person fromPerson = (org.hl7.fhir.dstu3.model.Person)thePersonToDelete;
		org.hl7.fhir.dstu3.model.Person toPerson = (org.hl7.fhir.dstu3.model.Person)thePersonToKeep;
		if (!toPerson.hasName()) {
			toPerson.setName(fromPerson.getName());
		}
		if (!toPerson.hasAddress()) {
			toPerson.setAddress(fromPerson.getAddress());
		}
		if (!toPerson.hasTelecom()) {
			toPerson.setTelecom(fromPerson.getTelecom());
		}
		if (!toPerson.hasBirthDate()) {
			toPerson.setBirthDate(fromPerson.getBirthDate());
		}
		if (!toPerson.hasGender()) {
			toPerson.setGender(fromPerson.getGender());
		}
		if (!toPerson.hasPhoto()) {
			toPerson.setPhoto(fromPerson.getPhoto());
		}
	}

	/**
	 * An incoming resource is a potential duplicate if it matches a Patient that has a Person with an official EID, but
	 * the incoming resource also has an EID that does not match.
	 *
	 * @param theExistingPerson
	 * @param theComparingPerson
	 * @return
	 */
	public boolean isPotentialDuplicate(IBaseResource theExistingPerson, IBaseResource theComparingPerson) {
		List<CanonicalEID> externalEidsPerson = myEIDHelper.getExternalEid(theExistingPerson);
		List<CanonicalEID> externalEidsResource = myEIDHelper.getExternalEid(theComparingPerson);
		return !externalEidsPerson.isEmpty() && !externalEidsResource.isEmpty() && !myEIDHelper.eidMatchExists(externalEidsResource, externalEidsPerson);
	}
}
