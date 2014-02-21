package ca.uhn.fhir.model.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.datatype.*;

/**
 * HAPI/FHIR <b>Patient</b> Resource
 * (Information about a person or animal receiving health care services)
 *
 * <p>
 * <b>Definition:</b>
 * Demographics and other administrative information about a person or animal receiving care or other health-related services
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Tracking patient is the center of the healthcare process
 * </p> 
 */
@ResourceDef(name="${resourceName}")
public class Patient extends BaseResource {

	@Child(name="identifier", order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="name", order=1, min=0, max=Child.MAX_UNLIMITED)	
	private List<HumanNameDt> myName;
	
	@Child(name="telecom", order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="gender", order=3, min=0, max=1)	
	private CodeableConceptDt myGender;
	
	@Child(name="birthDate", order=4, min=0, max=1)	
	private DateTimeDt myBirthDate;
	
	@Child(name="deceased[x]", order=5, min=0, max=1)	
	private IDatatype myDeceased;
	
	@Child(name="address", order=6, min=0, max=Child.MAX_UNLIMITED)	
	private List<AddressDt> myAddress;
	
	@Child(name="maritalStatus", order=7, min=0, max=1)	
	private CodeableConceptDt myMaritalStatus;
	
	@Child(name="multipleBirth[x]", order=8, min=0, max=1)	
	private IDatatype myMultipleBirth;
	
	@Child(name="photo", order=9, min=0, max=Child.MAX_UNLIMITED)	
	private List<AttachmentDt> myPhoto;
	
	@Child(name="contact", order=10, min=0, max=Child.MAX_UNLIMITED)	
	private List<Contact> myContact;
	
	@Child(name="animal", order=11, min=0, max=1)	
	private Animal myAnimal;
	
	@Child(name="communication", order=12, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodeableConceptDt> myCommunication;
	
	@Child(name="careProvider", order=13, min=0, max=Child.MAX_UNLIMITED)	
	private List<ResourceReference> myCareProvider;
	
	@Child(name="managingOrganization", order=14, min=0, max=1)	
	private ResourceReference myManagingOrganization;
	
	@Child(name="link", order=15, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myLink;
	
	@Child(name="active", order=16, min=0, max=1)	
	private BooleanDt myActive;
	

	/**
	 * Gets the value(s) for identifier (An identifier for the person as this patient)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person as a patient
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (An identifier for the person as this patient)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person as a patient
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for name (A name associated with the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the individual. 
     * </p> 
	 */
	public List<HumanNameDt> getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (A name associated with the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the individual. 
     * </p> 
	 */
	public void setName(List<HumanNameDt> theValue) {
		myName = theValue;
	}
	
	/**
	 * Gets the value(s) for telecom (A contact detail for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted. 
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (A contact detail for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted. 
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
	/**
	 * Gets the value(s) for gender (Gender for administrative purposes)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes. 
     * </p> 
	 */
	public CodeableConceptDt getGender() {
		return myGender;
	}

	/**
	 * Sets the value(s) for gender (Gender for administrative purposes)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes. 
     * </p> 
	 */
	public void setGender(CodeableConceptDt theValue) {
		myGender = theValue;
	}
	
	/**
	 * Gets the value(s) for birthDate (The date and time of birth for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the individual
     * </p> 
	 */
	public DateTimeDt getBirthDate() {
		return myBirthDate;
	}

	/**
	 * Sets the value(s) for birthDate (The date and time of birth for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the individual
     * </p> 
	 */
	public void setBirthDate(DateTimeDt theValue) {
		myBirthDate = theValue;
	}
	
	/**
	 * Gets the value(s) for deceased[x] (Indicates if the individual is deceased or not)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the individual is deceased or not
     * </p> 
	 */
	public IDatatype getDeceased() {
		return myDeceased;
	}

	/**
	 * Sets the value(s) for deceased[x] (Indicates if the individual is deceased or not)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the individual is deceased or not
     * </p> 
	 */
	public void setDeceased(IDatatype theValue) {
		myDeceased = theValue;
	}
	
	/**
	 * Gets the value(s) for address (Addresses for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * Addresses for the individual
     * </p> 
	 */
	public List<AddressDt> getAddress() {
		return myAddress;
	}

	/**
	 * Sets the value(s) for address (Addresses for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * Addresses for the individual
     * </p> 
	 */
	public void setAddress(List<AddressDt> theValue) {
		myAddress = theValue;
	}
	
	/**
	 * Gets the value(s) for maritalStatus (Marital (civil) status of a person)
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains a patient's most recent marital (civil) status.
     * </p> 
	 */
	public CodeableConceptDt getMaritalStatus() {
		return myMaritalStatus;
	}

	/**
	 * Sets the value(s) for maritalStatus (Marital (civil) status of a person)
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains a patient's most recent marital (civil) status.
     * </p> 
	 */
	public void setMaritalStatus(CodeableConceptDt theValue) {
		myMaritalStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for multipleBirth[x] (Whether patient is part of a multiple birth)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the patient is part of a multiple or indicates the actual birth order.
     * </p> 
	 */
	public IDatatype getMultipleBirth() {
		return myMultipleBirth;
	}

	/**
	 * Sets the value(s) for multipleBirth[x] (Whether patient is part of a multiple birth)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the patient is part of a multiple or indicates the actual birth order.
     * </p> 
	 */
	public void setMultipleBirth(IDatatype theValue) {
		myMultipleBirth = theValue;
	}
	
	/**
	 * Gets the value(s) for photo (Image of the person)
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public List<AttachmentDt> getPhoto() {
		return myPhoto;
	}

	/**
	 * Sets the value(s) for photo (Image of the person)
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public void setPhoto(List<AttachmentDt> theValue) {
		myPhoto = theValue;
	}
	
	/**
	 * Gets the value(s) for contact (A contact party (e.g. guardian, partner, friend) for the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	public List<Contact> getContact() {
		return myContact;
	}

	/**
	 * Sets the value(s) for contact (A contact party (e.g. guardian, partner, friend) for the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	public void setContact(List<Contact> theValue) {
		myContact = theValue;
	}
	
	/**
	 * Gets the value(s) for animal (If this patient is an animal (non-human))
	 *
     * <p>
     * <b>Definition:</b>
     * This element has a value if the patient is an animal
     * </p> 
	 */
	public Animal getAnimal() {
		return myAnimal;
	}

	/**
	 * Sets the value(s) for animal (If this patient is an animal (non-human))
	 *
     * <p>
     * <b>Definition:</b>
     * This element has a value if the patient is an animal
     * </p> 
	 */
	public void setAnimal(Animal theValue) {
		myAnimal = theValue;
	}
	
	/**
	 * Gets the value(s) for communication (Languages which may be used to communicate with the patient about his or her health)
	 *
     * <p>
     * <b>Definition:</b>
     * Languages which may be used to communicate with the patient about his or her health
     * </p> 
	 */
	public List<CodeableConceptDt> getCommunication() {
		return myCommunication;
	}

	/**
	 * Sets the value(s) for communication (Languages which may be used to communicate with the patient about his or her health)
	 *
     * <p>
     * <b>Definition:</b>
     * Languages which may be used to communicate with the patient about his or her health
     * </p> 
	 */
	public void setCommunication(List<CodeableConceptDt> theValue) {
		myCommunication = theValue;
	}
	
	/**
	 * Gets the value(s) for careProvider (Patient's nominated care provider)
	 *
     * <p>
     * <b>Definition:</b>
     * Patient's nominated care provider
     * </p> 
	 */
	public List<ResourceReference> getCareProvider() {
		return myCareProvider;
	}

	/**
	 * Sets the value(s) for careProvider (Patient's nominated care provider)
	 *
     * <p>
     * <b>Definition:</b>
     * Patient's nominated care provider
     * </p> 
	 */
	public void setCareProvider(List<ResourceReference> theValue) {
		myCareProvider = theValue;
	}
	
	/**
	 * Gets the value(s) for managingOrganization (Organization that is the custodian of the patient record)
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that is the custodian of the patient record
     * </p> 
	 */
	public ResourceReference getManagingOrganization() {
		return myManagingOrganization;
	}

	/**
	 * Sets the value(s) for managingOrganization (Organization that is the custodian of the patient record)
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that is the custodian of the patient record
     * </p> 
	 */
	public void setManagingOrganization(ResourceReference theValue) {
		myManagingOrganization = theValue;
	}
	
	/**
	 * Gets the value(s) for link (Link to another patient resource that concerns the same actual person)
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	public List<IDatatype> getLink() {
		return myLink;
	}

	/**
	 * Sets the value(s) for link (Link to another patient resource that concerns the same actual person)
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	public void setLink(List<IDatatype> theValue) {
		myLink = theValue;
	}
	
	/**
	 * Gets the value(s) for active (Whether this patient's record is in active use)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this patient record is in active use
     * </p> 
	 */
	public BooleanDt getActive() {
		return myActive;
	}

	/**
	 * Sets the value(s) for active (Whether this patient's record is in active use)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this patient record is in active use
     * </p> 
	 */
	public void setActive(BooleanDt theValue) {
		myActive = theValue;
	}
	

	/**
	 * Block class for child element: <b>Patient.contact</b> (A contact party (e.g. guardian, partner, friend) for the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	@Block(name="Patient.contact")	
	public static class Contact {
		@Child(name="relationship", order=0, min=0, max=Child.MAX_UNLIMITED)	
		private List<CodeableConceptDt> myRelationship;

		@Child(name="name", order=1, min=0, max=1)	
		private HumanNameDt myName;

		@Child(name="telecom", order=2, min=0, max=Child.MAX_UNLIMITED)	
		private List<ContactDt> myTelecom;

		@Child(name="address", order=3, min=0, max=1)	
		private AddressDt myAddress;

		@Child(name="gender", order=4, min=0, max=1)	
		private CodeableConceptDt myGender;

		@Child(name="organization", order=5, min=0, max=1)	
		private ResourceReference myOrganization;

		/**
		 * Gets the value(s) for relationship (The kind of relationship)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * The nature of the relationship between the patient and the contact person
	     * </p> 
		 */
		public List<CodeableConceptDt> getRelationship() {
			return myRelationship;
		}
	
		/**
		 * Sets the value(s) for relationship (The kind of relationship)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * The nature of the relationship between the patient and the contact person
	     * </p> 
		 */
		public void setRelationship(List<CodeableConceptDt> theValue) {
			myRelationship = theValue;
		}
	
		/**
		 * Gets the value(s) for name (A name associated with the person)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * A name associated with the person
	     * </p> 
		 */
		public HumanNameDt getName() {
			return myName;
		}
	
		/**
		 * Sets the value(s) for name (A name associated with the person)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * A name associated with the person
	     * </p> 
		 */
		public void setName(HumanNameDt theValue) {
			myName = theValue;
		}
	
		/**
		 * Gets the value(s) for telecom (A contact detail for the person)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * A contact detail for the person, e.g. a telephone number or an email address.
	     * </p> 
		 */
		public List<ContactDt> getTelecom() {
			return myTelecom;
		}
	
		/**
		 * Sets the value(s) for telecom (A contact detail for the person)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * A contact detail for the person, e.g. a telephone number or an email address.
	     * </p> 
		 */
		public void setTelecom(List<ContactDt> theValue) {
			myTelecom = theValue;
		}
	
		/**
		 * Gets the value(s) for address (Address for the contact person)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Address for the contact person
	     * </p> 
		 */
		public AddressDt getAddress() {
			return myAddress;
		}
	
		/**
		 * Sets the value(s) for address (Address for the contact person)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Address for the contact person
	     * </p> 
		 */
		public void setAddress(AddressDt theValue) {
			myAddress = theValue;
		}
	
		/**
		 * Gets the value(s) for gender (Gender for administrative purposes)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes. 
	     * </p> 
		 */
		public CodeableConceptDt getGender() {
			return myGender;
		}
	
		/**
		 * Sets the value(s) for gender (Gender for administrative purposes)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes. 
	     * </p> 
		 */
		public void setGender(CodeableConceptDt theValue) {
			myGender = theValue;
		}
	
		/**
		 * Gets the value(s) for organization (Organization that is associated with the contact)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Organization on behalf of which the contact is acting or for which the contact is working.
	     * </p> 
		 */
		public ResourceReference getOrganization() {
			return myOrganization;
		}
	
		/**
		 * Sets the value(s) for organization (Organization that is associated with the contact)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Organization on behalf of which the contact is acting or for which the contact is working.
	     * </p> 
		 */
		public void setOrganization(ResourceReference theValue) {
			myOrganization = theValue;
		}
	
	}

	/**
	 * Block class for child element: <b>Patient.animal</b> (If this patient is an animal (non-human))
	 *
     * <p>
     * <b>Definition:</b>
     * This element has a value if the patient is an animal
     * </p> 
	 */
	@Block(name="Patient.animal")	
	public static class Animal {
		@Child(name="species", order=0, min=1, max=1)	
		private CodeableConceptDt mySpecies;

		@Child(name="breed", order=1, min=0, max=1)	
		private CodeableConceptDt myBreed;

		@Child(name="genderStatus", order=2, min=0, max=1)	
		private CodeableConceptDt myGenderStatus;

		/**
		 * Gets the value(s) for species (E.g. Dog, Cow)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Identifies the high level categorization of the kind of animal
	     * </p> 
		 */
		public CodeableConceptDt getSpecies() {
			return mySpecies;
		}
	
		/**
		 * Sets the value(s) for species (E.g. Dog, Cow)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Identifies the high level categorization of the kind of animal
	     * </p> 
		 */
		public void setSpecies(CodeableConceptDt theValue) {
			mySpecies = theValue;
		}
	
		/**
		 * Gets the value(s) for breed (E.g. Poodle, Angus)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Identifies the detailed categorization of the kind of animal.
	     * </p> 
		 */
		public CodeableConceptDt getBreed() {
			return myBreed;
		}
	
		/**
		 * Sets the value(s) for breed (E.g. Poodle, Angus)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Identifies the detailed categorization of the kind of animal.
	     * </p> 
		 */
		public void setBreed(CodeableConceptDt theValue) {
			myBreed = theValue;
		}
	
		/**
		 * Gets the value(s) for genderStatus (E.g. Neutered, Intact)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Indicates the current state of the animal's reproductive organs
	     * </p> 
		 */
		public CodeableConceptDt getGenderStatus() {
			return myGenderStatus;
		}
	
		/**
		 * Sets the value(s) for genderStatus (E.g. Neutered, Intact)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * Indicates the current state of the animal's reproductive organs
	     * </p> 
		 */
		public void setGenderStatus(CodeableConceptDt theValue) {
			myGenderStatus = theValue;
		}
	
	}

	/**
	 * Block class for child element: <b>Patient.link</b> (Link to another patient resource that concerns the same actual person)
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	@Block(name="Patient.link")	
	public static class Link {
		@Child(name="other", order=0, min=1, max=1)	
		private ResourceReference myOther;

		@Child(name="type", order=1, min=1, max=1)	
		private CodeDt myType;

		/**
		 * Gets the value(s) for other (The other patient resource that the link refers to)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * The other patient resource that the link refers to
	     * </p> 
		 */
		public ResourceReference getOther() {
			return myOther;
		}
	
		/**
		 * Sets the value(s) for other (The other patient resource that the link refers to)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * The other patient resource that the link refers to
	     * </p> 
		 */
		public void setOther(ResourceReference theValue) {
			myOther = theValue;
		}
	
		/**
		 * Gets the value(s) for type (replace | refer | seealso - type of link)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * The type of link between this patient resource and another patient resource.
	     * </p> 
		 */
		public CodeDt getType() {
			return myType;
		}
	
		/**
		 * Sets the value(s) for type (replace | refer | seealso - type of link)
		 *
	     * <p>
	     * <b>Definition:</b>
	     * The type of link between this patient resource and another patient resource.
	     * </p> 
		 */
		public void setType(CodeDt theValue) {
			myType = theValue;
		}
	
	}


}