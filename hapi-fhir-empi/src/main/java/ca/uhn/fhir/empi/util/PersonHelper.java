package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.fhirpath.IFhirPath;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static ca.uhn.fhir.rest.api.Constants.CODE_HAPI_EMPI_MANAGED;
import static ca.uhn.fhir.rest.api.Constants.SYSTEM_EMPI_MANAGED;

@Lazy
@Service
public final class PersonHelper {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IEmpiConfig myEmpiConfig;

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
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				Person person = new Person();

				SystemAgnosticIdentifier systemAgnosticIdentifier = readEIDFromResource(thePatient);
				if (systemAgnosticIdentifier == null) {
					systemAgnosticIdentifier = createRandomEid();
				} else {
					//Any incoming EID is automatically official regardless of what they say.
					systemAgnosticIdentifier.setUse("official");
				}
				person.addIdentifier(systemAgnosticIdentifier.toR4());
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
	public void copyPatientDataIntoPerson(Patient thePatient, Person thePerson) {
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
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				//This handles overwriting an automatically assigned EID if a patient that links is coming in with an official EID.
				Person person = ((Person)thePerson);
				Identifier identifier = person.getIdentifier().stream().filter(theIdentifier -> theIdentifier.getSystem().equalsIgnoreCase(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())).findFirst().orElse(null);
				SystemAgnosticIdentifier incomingEid = readEIDFromResource(thePatient);
				if (StringUtils.isBlank(incomingEid.getUse())) {
					incomingEid.setUse("official");
				}
				if (identifier == null){
					person.addIdentifier(incomingEid.toR4());
				} else if (identifier.getUse().equals(Identifier.IdentifierUse.SECONDARY)) {
					person.getIdentifier().remove(identifier);
					person.addIdentifier(incomingEid.toR4());
				} else if (identifier.getUse().equals(Identifier.IdentifierUse.OFFICIAL)) {
					// FIXME EMPI create potential duplicate user link.
				}
			default:
				// FIXME EMPI moar versions
				break;
		}
		return thePerson;
	}


	public SystemAgnosticIdentifier createRandomEid() {
		return new SystemAgnosticIdentifier(
			myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem(),
			UUID.randomUUID().toString(),
			"secondary"
		);
	}

	public static class SystemAgnosticIdentifier {
		private String mySystem;
		private String myUse;
		private String myValue;

		public SystemAgnosticIdentifier(String theSystem, String theValue, String theUse){
			mySystem = theSystem;
			myUse = theUse;
			myValue = theValue;
		}

		/** Constructor which takes in an IBase representing an EID Identifier
		 *  and builds a SystemAgnosticIdentifier out of it.
		 * @param theFhirPath
		 * @param theIBase
		 */
		public SystemAgnosticIdentifier(IFhirPath theFhirPath, IBase theIBase) {
			List<IBase> use = theFhirPath.evaluate(theIBase, "use", IBase.class);
			List<IBase> value = theFhirPath.evaluate(theIBase, "value", IBase.class);
			List<IBase> system = theFhirPath.evaluate(theIBase, "system", IBase.class);

			myUse = use.size() > 0 ? use.get(0).toString(): null;
			myValue= value.size() > 0 ? value.get(0).toString(): null;
			mySystem= system.size() > 0 ? system.get(0).toString(): null;
		}

		public Identifier toR4() {
			return new Identifier()
				.setUse(Identifier.IdentifierUse.fromCode(myUse))
				.setSystem(mySystem)
				.setValue(myValue);
		}

		public org.hl7.fhir.dstu3.model.Identifier toDSTU3(){
			return new org.hl7.fhir.dstu3.model.Identifier()
				.setUse(org.hl7.fhir.dstu3.model.Identifier.IdentifierUse.fromCode(myUse))
				.setSystem(mySystem)
				.setValue(myValue);
		}

		public  String getSystem() {
			return mySystem;
		}

		public  String getUse() {
			return myUse;
		}

		public  String getValue() {
			return myValue;
		}

		private void setSystem(String theSystem) {
			mySystem = theSystem;
		}

		private void setUse(String theUse) {
			myUse = theUse;
		}

		private void setValue(String theValue) {
			myValue = theValue;
		}
	}


	public SystemAgnosticIdentifier readEIDFromResource(IBaseResource theBaseResource) {
		IFhirPath fhirPath = myFhirContext.newFhirPath();
		List<IBase> evaluate = fhirPath.evaluate(
			theBaseResource,
			buildEidFhirPath(theBaseResource),
			IBase.class);

		if (evaluate.size() > 1) {
			throw new RuntimeException("Resources cannot have two EIDs!");
		} else if (evaluate.size() == 1) {
			return new SystemAgnosticIdentifier(fhirPath, evaluate.get(0));
		} else {
			return null;
		}
	}

	/**
	 * Get the appropriate FHIRPath expression to extract the EID identifier value, regardless of resource type.
	 * e.g. if theBaseResource is a patient, and the EMPI EID system is test-system, this will return
	 *
	 * Patient.identifier.where(system='test-system').value
	 *
	 */
	private String buildEidFhirPath(IBaseResource theBaseResource) {
		return myFhirContext.getResourceDefinition(theBaseResource).getName()
			+ ".identifier.where(system='"
			+ myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem()
			+ "')";
	}
}
