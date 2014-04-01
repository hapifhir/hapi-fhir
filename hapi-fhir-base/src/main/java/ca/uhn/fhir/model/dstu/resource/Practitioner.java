















package ca.uhn.fhir.model.dstu.resource;


import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerSpecialtyEnum;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;


/**
 * HAPI/FHIR <b>Practitioner</b> Resource
 * (A person with a  formal responsibility in the provisioning of healthcare or related services)
 *
 * <p>
 * <b>Definition:</b>
 * A person who is directly or indirectly involved in the provisioning of healthcare
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to track doctors, staff, locums etc. for both healthcare practitioners, funders, etc.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Practitioner">http://hl7.org/fhir/profiles/Practitioner</a> 
 * </p>
 *
 */
@ResourceDef(name="Practitioner", profile="http://hl7.org/fhir/profiles/Practitioner", id="practitioner")
public class Practitioner extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>A practitioner's Identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Practitioner.identifier</b><br/>
	 * </p>
	 */
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of either family or given name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	public static final String SP_NAME = "name";

	/**
	 * Search parameter constant for <b>family</b>
	 * <p>
	 * Description: <b>A portion of the family name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	public static final String SP_FAMILY = "family";

	/**
	 * Search parameter constant for <b>given</b>
	 * <p>
	 * Description: <b>A portion of the given name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	public static final String SP_GIVEN = "given";

	/**
	 * Search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	public static final String SP_PHONETIC = "phonetic";

	/**
	 * Search parameter constant for <b>telecom</b>
	 * <p>
	 * Description: <b>The value in any kind of contact</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.telecom</b><br/>
	 * </p>
	 */
	public static final String SP_TELECOM = "telecom";

	/**
	 * Search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>An address in any kind of address/part</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.address</b><br/>
	 * </p>
	 */
	public static final String SP_ADDRESS = "address";

	/**
	 * Search parameter constant for <b>gender</b>
	 * <p>
	 * Description: <b>Gender of the practitioner</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Practitioner.gender</b><br/>
	 * </p>
	 */
	public static final String SP_GENDER = "gender";

	/**
	 * Search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>The identity of the organization the practitioner represents / acts on behalf of</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Practitioner.organization</b><br/>
	 * </p>
	 */
	public static final String SP_ORGANIZATION = "organization";


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A identifier for the person as this agent",
		formalDefinition="An identifier that applies to this person in this role"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="A name associated with the person",
		formalDefinition="A name associated with the person"
	)
	private HumanNameDt myName;
	
	@Child(name="telecom", type=ContactDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A contact detail for the practitioner",
		formalDefinition="A contact detail for the practitioner, e.g. a telephone number or an email address."
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Where practitioner can be found/visited",
		formalDefinition="The postal address where the practitioner can be found or visited or to which mail can be delivered"
	)
	private AddressDt myAddress;
	
	@Child(name="gender", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Gender for administrative purposes",
		formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes."
	)
	private BoundCodeableConceptDt<AdministrativeGenderCodesEnum> myGender;
	
	@Child(name="birthDate", type=DateTimeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="The date and time of birth for the practitioner",
		formalDefinition="The date and time of birth for the practitioner"
	)
	private DateTimeDt myBirthDate;
	
	@Child(name="photo", type=AttachmentDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Image of the person",
		formalDefinition="Image of the person"
	)
	private java.util.List<AttachmentDt> myPhoto;
	
	@Child(name="organization", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class,
	})
	@Description(
		shortDefinition="The represented organization",
		formalDefinition="The organization that the practitioner represents"
	)
	private ResourceReferenceDt myOrganization;
	
	@Child(name="role", type=CodeableConceptDt.class, order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Roles which this practitioner may perform",
		formalDefinition="Roles which this practitioner is authorized to perform for the organization"
	)
	private java.util.List<BoundCodeableConceptDt<PractitionerRoleEnum>> myRole;
	
	@Child(name="specialty", type=CodeableConceptDt.class, order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Specific specialty of the practitioner",
		formalDefinition="Specific specialty of the practitioner"
	)
	private java.util.List<BoundCodeableConceptDt<PractitionerSpecialtyEnum>> mySpecialty;
	
	@Child(name="period", type=PeriodDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="The period during which the practitioner is authorized to perform in these role(s)",
		formalDefinition="The period during which the person is authorized to act as a practitioner in these role(s) for the organization"
	)
	private PeriodDt myPeriod;
	
	@Child(name="location", order=11, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Location.class,
	})
	@Description(
		shortDefinition="The location(s) at which this practitioner provides care",
		formalDefinition="The location(s) at which this practitioner provides care"
	)
	private java.util.List<ResourceReferenceDt> myLocation;
	
	@Child(name="qualification", order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Qualifications obtained by training and certification",
		formalDefinition=""
	)
	private java.util.List<Qualification> myQualification;
	
	@Child(name="communication", type=CodeableConceptDt.class, order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A language the practitioner is able to use in patient communication",
		formalDefinition="A language the practitioner is able to use in patient communication"
	)
	private java.util.List<CodeableConceptDt> myCommunication;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myTelecom,  myAddress,  myGender,  myBirthDate,  myPhoto,  myOrganization,  myRole,  mySpecialty,  myPeriod,  myLocation,  myQualification,  myCommunication);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myTelecom, myAddress, myGender, myBirthDate, myPhoto, myOrganization, myRole, mySpecialty, myPeriod, myLocation, myQualification, myCommunication);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (A identifier for the person as this agent).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (A identifier for the person as this agent)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
     * </p> 
	 */
	public Practitioner setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (A identifier for the person as this agent)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (A identifier for the person as this agent),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (A identifier for the person as this agent)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Practitioner addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (A identifier for the person as this agent)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Practitioner addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (A name associated with the person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the person
     * </p> 
	 */
	public HumanNameDt getName() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (A name associated with the person)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the person
     * </p> 
	 */
	public Practitioner setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>telecom</b> (A contact detail for the practitioner).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     * </p> 
	 */
	public java.util.List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (A contact detail for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     * </p> 
	 */
	public Practitioner setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (A contact detail for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (A contact detail for the practitioner),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     * </p> 
	 */
	public ContactDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>address</b> (Where practitioner can be found/visited).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The postal address where the practitioner can be found or visited or to which mail can be delivered
     * </p> 
	 */
	public AddressDt getAddress() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (Where practitioner can be found/visited)
	 *
     * <p>
     * <b>Definition:</b>
     * The postal address where the practitioner can be found or visited or to which mail can be delivered
     * </p> 
	 */
	public Practitioner setAddress(AddressDt theValue) {
		myAddress = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>gender</b> (Gender for administrative purposes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public BoundCodeableConceptDt<AdministrativeGenderCodesEnum> getGender() {  
		if (myGender == null) {
			myGender = new BoundCodeableConceptDt<AdministrativeGenderCodesEnum>(AdministrativeGenderCodesEnum.VALUESET_BINDER);
		}
		return myGender;
	}

	/**
	 * Sets the value(s) for <b>gender</b> (Gender for administrative purposes)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public Practitioner setGender(BoundCodeableConceptDt<AdministrativeGenderCodesEnum> theValue) {
		myGender = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>gender</b> (Gender for administrative purposes)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public Practitioner setGender(AdministrativeGenderCodesEnum theValue) {
		getGender().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>birthDate</b> (The date and time of birth for the practitioner).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public DateTimeDt getBirthDate() {  
		if (myBirthDate == null) {
			myBirthDate = new DateTimeDt();
		}
		return myBirthDate;
	}

	/**
	 * Sets the value(s) for <b>birthDate</b> (The date and time of birth for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public Practitioner setBirthDate(DateTimeDt theValue) {
		myBirthDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>birthDate</b> (The date and time of birth for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public Practitioner setBirthDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myBirthDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>birthDate</b> (The date and time of birth for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public Practitioner setBirthDateWithSecondsPrecision( Date theDate) {
		myBirthDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>photo</b> (Image of the person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public java.util.List<AttachmentDt> getPhoto() {  
		if (myPhoto == null) {
			myPhoto = new java.util.ArrayList<AttachmentDt>();
		}
		return myPhoto;
	}

	/**
	 * Sets the value(s) for <b>photo</b> (Image of the person)
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public Practitioner setPhoto(java.util.List<AttachmentDt> theValue) {
		myPhoto = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>photo</b> (Image of the person)
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public AttachmentDt addPhoto() {
		AttachmentDt newType = new AttachmentDt();
		getPhoto().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>photo</b> (Image of the person),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public AttachmentDt getPhotoFirstRep() {
		if (getPhoto().isEmpty()) {
			return addPhoto();
		}
		return getPhoto().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>organization</b> (The represented organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The organization that the practitioner represents
     * </p> 
	 */
	public ResourceReferenceDt getOrganization() {  
		if (myOrganization == null) {
			myOrganization = new ResourceReferenceDt();
		}
		return myOrganization;
	}

	/**
	 * Sets the value(s) for <b>organization</b> (The represented organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The organization that the practitioner represents
     * </p> 
	 */
	public Practitioner setOrganization(ResourceReferenceDt theValue) {
		myOrganization = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>role</b> (Roles which this practitioner may perform).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<PractitionerRoleEnum>> getRole() {  
		if (myRole == null) {
			myRole = new java.util.ArrayList<BoundCodeableConceptDt<PractitionerRoleEnum>>();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (Roles which this practitioner may perform)
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public Practitioner setRole(java.util.List<BoundCodeableConceptDt<PractitionerRoleEnum>> theValue) {
		myRole = theValue;
		return this;
	}

	/**
	 * Add a value for <b>role</b> (Roles which this practitioner may perform)
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public void addRole(PractitionerRoleEnum theValue) {
		getRole().add(new BoundCodeableConceptDt<PractitionerRoleEnum>(PractitionerRoleEnum.VALUESET_BINDER, theValue));
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>role</b> (Roles which this practitioner may perform)
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public Practitioner setRole(PractitionerRoleEnum theValue) {
		getRole().clear();
		addRole(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>specialty</b> (Specific specialty of the practitioner).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<PractitionerSpecialtyEnum>> getSpecialty() {  
		if (mySpecialty == null) {
			mySpecialty = new java.util.ArrayList<BoundCodeableConceptDt<PractitionerSpecialtyEnum>>();
		}
		return mySpecialty;
	}

	/**
	 * Sets the value(s) for <b>specialty</b> (Specific specialty of the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public Practitioner setSpecialty(java.util.List<BoundCodeableConceptDt<PractitionerSpecialtyEnum>> theValue) {
		mySpecialty = theValue;
		return this;
	}

	/**
	 * Add a value for <b>specialty</b> (Specific specialty of the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public void addSpecialty(PractitionerSpecialtyEnum theValue) {
		getSpecialty().add(new BoundCodeableConceptDt<PractitionerSpecialtyEnum>(PractitionerSpecialtyEnum.VALUESET_BINDER, theValue));
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>specialty</b> (Specific specialty of the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public Practitioner setSpecialty(PractitionerSpecialtyEnum theValue) {
		getSpecialty().clear();
		addSpecialty(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> (The period during which the practitioner is authorized to perform in these role(s)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which the person is authorized to act as a practitioner in these role(s) for the organization
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (The period during which the practitioner is authorized to perform in these role(s))
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which the person is authorized to act as a practitioner in these role(s) for the organization
     * </p> 
	 */
	public Practitioner setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>location</b> (The location(s) at which this practitioner provides care).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The location(s) at which this practitioner provides care
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getLocation() {  
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (The location(s) at which this practitioner provides care)
	 *
     * <p>
     * <b>Definition:</b>
     * The location(s) at which this practitioner provides care
     * </p> 
	 */
	public Practitioner setLocation(java.util.List<ResourceReferenceDt> theValue) {
		myLocation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>location</b> (The location(s) at which this practitioner provides care)
	 *
     * <p>
     * <b>Definition:</b>
     * The location(s) at which this practitioner provides care
     * </p> 
	 */
	public ResourceReferenceDt addLocation() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getLocation().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>qualification</b> (Qualifications obtained by training and certification).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<Qualification> getQualification() {  
		if (myQualification == null) {
			myQualification = new java.util.ArrayList<Qualification>();
		}
		return myQualification;
	}

	/**
	 * Sets the value(s) for <b>qualification</b> (Qualifications obtained by training and certification)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Practitioner setQualification(java.util.List<Qualification> theValue) {
		myQualification = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>qualification</b> (Qualifications obtained by training and certification)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Qualification addQualification() {
		Qualification newType = new Qualification();
		getQualification().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>qualification</b> (Qualifications obtained by training and certification),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Qualification getQualificationFirstRep() {
		if (getQualification().isEmpty()) {
			return addQualification();
		}
		return getQualification().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>communication</b> (A language the practitioner is able to use in patient communication).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getCommunication() {  
		if (myCommunication == null) {
			myCommunication = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myCommunication;
	}

	/**
	 * Sets the value(s) for <b>communication</b> (A language the practitioner is able to use in patient communication)
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public Practitioner setCommunication(java.util.List<CodeableConceptDt> theValue) {
		myCommunication = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>communication</b> (A language the practitioner is able to use in patient communication)
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public CodeableConceptDt addCommunication() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getCommunication().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>communication</b> (A language the practitioner is able to use in patient communication),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public CodeableConceptDt getCommunicationFirstRep() {
		if (getCommunication().isEmpty()) {
			return addCommunication();
		}
		return getCommunication().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Practitioner.qualification</b> (Qualifications obtained by training and certification)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block(name="Practitioner.qualification")	
	public static class Qualification extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Coded representation of the qualification",
		formalDefinition=""
	)
	private CodeableConceptDt myCode;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Period during which the qualification is valid",
		formalDefinition="Period during which the qualification is valid"
	)
	private PeriodDt myPeriod;
	
	@Child(name="issuer", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class,
	})
	@Description(
		shortDefinition="Organization that regulates and issues the qualification",
		formalDefinition="Organization that regulates and issues the qualification"
	)
	private ResourceReferenceDt myIssuer;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myPeriod,  myIssuer);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myPeriod, myIssuer);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Coded representation of the qualification).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Coded representation of the qualification)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Qualification setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> (Period during which the qualification is valid).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Period during which the qualification is valid
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (Period during which the qualification is valid)
	 *
     * <p>
     * <b>Definition:</b>
     * Period during which the qualification is valid
     * </p> 
	 */
	public Qualification setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>issuer</b> (Organization that regulates and issues the qualification).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that regulates and issues the qualification
     * </p> 
	 */
	public ResourceReferenceDt getIssuer() {  
		if (myIssuer == null) {
			myIssuer = new ResourceReferenceDt();
		}
		return myIssuer;
	}

	/**
	 * Sets the value(s) for <b>issuer</b> (Organization that regulates and issues the qualification)
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that regulates and issues the qualification
     * </p> 
	 */
	public Qualification setIssuer(ResourceReferenceDt theValue) {
		myIssuer = theValue;
		return this;
	}

  

	}




}