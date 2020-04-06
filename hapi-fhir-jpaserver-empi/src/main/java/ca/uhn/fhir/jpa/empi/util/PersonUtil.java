package ca.uhn.fhir.jpa.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.config.IEmpiConfig;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Reference;
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
				SystemAgnosticIdentifier systemAgnosticIdentifier = getOrCreateEidFromResource(thePatient);
				person.addIdentifier(systemAgnosticIdentifier.toR4());
				person.getMeta().addTag(buildEmpiManagedTag());
				// FIXME EMPI populate from data from theResource
				return person;
			default:
				// FIXME EMPI moar versions
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
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
				SystemAgnosticIdentifier incomingEid = getOrCreateEidFromResource(thePatient);

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


	public SystemAgnosticIdentifier getEidFromResource(IBaseResource theBaseResource) {
		String eid = readEIDFromResource(theBaseResource);
		if (StringUtils.isBlank(eid)) {
			return null;
		} else {
			String system = myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem();
			String use = "official";
			return new SystemAgnosticIdentifier(system, eid, use);
		}
	}

	public SystemAgnosticIdentifier getOrCreateEidFromResource(IBaseResource thePatient) {
		SystemAgnosticIdentifier eid;
		eid = getEidFromResource(thePatient);

		if (eid == null) {
			eid = new SystemAgnosticIdentifier(
				myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem(),
				UUID.randomUUID().toString(),
				"secondary"
			);
		}
		return eid;
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

		private String getSystem() {
			return mySystem;
		}

		private String getUse() {
			return myUse;
		}

		private String getValue() {
			return myValue;
		}
	}

	public String readEIDFromResource(IBaseResource theBaseResource) {
		List<IBase> evaluate = myFhirContext.newFhirPath().evaluate(
			theBaseResource,
			buildEidIdentifierFhirPath(theBaseResource),
			IBase.class);

		if (evaluate.size() > 1) {
			// FIXME EMPI determine correct error to throw here.
			throw new RuntimeException("Resources cannot have two EIDs!");
		} else if (evaluate.size() == 1) {
			return evaluate.get(0).toString();
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
	private String buildEidIdentifierFhirPath(IBaseResource theBaseResource) {
		return myFhirContext.getResourceDefinition(theBaseResource).getName()
			+ ".identifier.where(system='"
			+ myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem()
			+ "').value";
	}
}
