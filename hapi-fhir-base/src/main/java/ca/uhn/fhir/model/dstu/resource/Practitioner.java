











package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;

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
 */
@ResourceDef(name="Practitioner")
public class Practitioner implements IResource {

	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=1)	
	private HumanNameDt myName;
	
	@Child(name="telecom", type=ContactDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=3, min=0, max=1)	
	private AddressDt myAddress;
	
	@Child(name="gender", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	private CodeableConceptDt myGender;
	
	@Child(name="birthDate", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myBirthDate;
	
	@Child(name="photo", type=AttachmentDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	private List<AttachmentDt> myPhoto;
	
	@Child(name="organization", order=7, min=0, max=1)
	@ChildResource(types= {
		Organization.class,
	})	
	private ResourceReference myOrganization;
	
	@Child(name="role", type=CodeableConceptDt.class, order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodeableConceptDt> myRole;
	
	@Child(name="specialty", type=CodeableConceptDt.class, order=9, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodeableConceptDt> mySpecialty;
	
	@Child(name="period", type=PeriodDt.class, order=10, min=0, max=1)	
	private PeriodDt myPeriod;
	
	@Child(name="location", order=11, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Location.class,
	})	
	private List<ResourceReference> myLocation;
	
	@Child(name="qualification", order=12, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myQualification;
	
	@Child(name="communication", type=CodeableConceptDt.class, order=13, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodeableConceptDt> myCommunication;
	
	/**
	 * Gets the value(s) for identifier (A identifier for the person as this agent)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (A identifier for the person as this agent)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
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
	 * Gets the value(s) for telecom (A contact detail for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (A contact detail for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
	/**
	 * Gets the value(s) for address (Where practitioner can be found/visited)
	 *
     * <p>
     * <b>Definition:</b>
     * The postal address where the practitioner can be found or visited or to which mail can be delivered
     * </p> 
	 */
	public AddressDt getAddress() {
		return myAddress;
	}

	/**
	 * Sets the value(s) for address (Where practitioner can be found/visited)
	 *
     * <p>
     * <b>Definition:</b>
     * The postal address where the practitioner can be found or visited or to which mail can be delivered
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
	 * Gets the value(s) for birthDate (The date and time of birth for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public DateTimeDt getBirthDate() {
		return myBirthDate;
	}

	/**
	 * Sets the value(s) for birthDate (The date and time of birth for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public void setBirthDate(DateTimeDt theValue) {
		myBirthDate = theValue;
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
	 * Gets the value(s) for organization (The represented organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The organization that the practitioner represents
     * </p> 
	 */
	public ResourceReference getOrganization() {
		return myOrganization;
	}

	/**
	 * Sets the value(s) for organization (The represented organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The organization that the practitioner represents
     * </p> 
	 */
	public void setOrganization(ResourceReference theValue) {
		myOrganization = theValue;
	}
	
	/**
	 * Gets the value(s) for role (Roles which this practitioner may perform)
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public List<CodeableConceptDt> getRole() {
		return myRole;
	}

	/**
	 * Sets the value(s) for role (Roles which this practitioner may perform)
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public void setRole(List<CodeableConceptDt> theValue) {
		myRole = theValue;
	}
	
	/**
	 * Gets the value(s) for specialty (Specific specialty of the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public List<CodeableConceptDt> getSpecialty() {
		return mySpecialty;
	}

	/**
	 * Sets the value(s) for specialty (Specific specialty of the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public void setSpecialty(List<CodeableConceptDt> theValue) {
		mySpecialty = theValue;
	}
	
	/**
	 * Gets the value(s) for period (The period during which the practitioner is authorized to perform in these role(s))
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which the person is authorized to act as a practitioner in these role(s) for the organization
     * </p> 
	 */
	public PeriodDt getPeriod() {
		return myPeriod;
	}

	/**
	 * Sets the value(s) for period (The period during which the practitioner is authorized to perform in these role(s))
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which the person is authorized to act as a practitioner in these role(s) for the organization
     * </p> 
	 */
	public void setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
	}
	
	/**
	 * Gets the value(s) for location (The location(s) at which this practitioner provides care)
	 *
     * <p>
     * <b>Definition:</b>
     * The location(s) at which this practitioner provides care
     * </p> 
	 */
	public List<ResourceReference> getLocation() {
		return myLocation;
	}

	/**
	 * Sets the value(s) for location (The location(s) at which this practitioner provides care)
	 *
     * <p>
     * <b>Definition:</b>
     * The location(s) at which this practitioner provides care
     * </p> 
	 */
	public void setLocation(List<ResourceReference> theValue) {
		myLocation = theValue;
	}
	
	/**
	 * Gets the value(s) for qualification (Qualifications obtained by training and certification)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public List<IDatatype> getQualification() {
		return myQualification;
	}

	/**
	 * Sets the value(s) for qualification (Qualifications obtained by training and certification)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setQualification(List<IDatatype> theValue) {
		myQualification = theValue;
	}
	
	/**
	 * Gets the value(s) for communication (A language the practitioner is able to use in patient communication)
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public List<CodeableConceptDt> getCommunication() {
		return myCommunication;
	}

	/**
	 * Sets the value(s) for communication (A language the practitioner is able to use in patient communication)
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public void setCommunication(List<CodeableConceptDt> theValue) {
		myCommunication = theValue;
	}
	

	/**
	 * Block class for child element: <b>Practitioner.qualification</b> (Qualifications obtained by training and certification)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	@Block(name="Practitioner.qualification")	
	public static class Qualification implements IResourceBlock {
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=1)	
	private HumanNameDt myName;
	
	@Child(name="telecom", type=ContactDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=3, min=0, max=1)	
	private AddressDt myAddress;
	
	@Child(name="gender", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	private CodeableConceptDt myGender;
	
	@Child(name="birthDate", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myBirthDate;
	
	@Child(name="photo", type=AttachmentDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	private List<AttachmentDt> myPhoto;
	
	@Child(name="organization", order=7, min=0, max=1)
	@ChildResource(types= {
		Organization.class,
	})	
	private ResourceReference myOrganization;
	
	@Child(name="role", type=CodeableConceptDt.class, order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodeableConceptDt> myRole;
	
	@Child(name="specialty", type=CodeableConceptDt.class, order=9, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodeableConceptDt> mySpecialty;
	
	@Child(name="period", type=PeriodDt.class, order=10, min=0, max=1)	
	private PeriodDt myPeriod;
	
	@Child(name="location", order=11, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Location.class,
	})	
	private List<ResourceReference> myLocation;
	
	@Child(name="qualification", order=12, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myQualification;
	
	@Child(name="communication", type=CodeableConceptDt.class, order=13, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodeableConceptDt> myCommunication;
	
	/**
	 * Gets the value(s) for identifier (A identifier for the person as this agent)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (A identifier for the person as this agent)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
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
	 * Gets the value(s) for telecom (A contact detail for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (A contact detail for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
	/**
	 * Gets the value(s) for address (Where practitioner can be found/visited)
	 *
     * <p>
     * <b>Definition:</b>
     * The postal address where the practitioner can be found or visited or to which mail can be delivered
     * </p> 
	 */
	public AddressDt getAddress() {
		return myAddress;
	}

	/**
	 * Sets the value(s) for address (Where practitioner can be found/visited)
	 *
     * <p>
     * <b>Definition:</b>
     * The postal address where the practitioner can be found or visited or to which mail can be delivered
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
	 * Gets the value(s) for birthDate (The date and time of birth for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public DateTimeDt getBirthDate() {
		return myBirthDate;
	}

	/**
	 * Sets the value(s) for birthDate (The date and time of birth for the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public void setBirthDate(DateTimeDt theValue) {
		myBirthDate = theValue;
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
	 * Gets the value(s) for organization (The represented organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The organization that the practitioner represents
     * </p> 
	 */
	public ResourceReference getOrganization() {
		return myOrganization;
	}

	/**
	 * Sets the value(s) for organization (The represented organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The organization that the practitioner represents
     * </p> 
	 */
	public void setOrganization(ResourceReference theValue) {
		myOrganization = theValue;
	}
	
	/**
	 * Gets the value(s) for role (Roles which this practitioner may perform)
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public List<CodeableConceptDt> getRole() {
		return myRole;
	}

	/**
	 * Sets the value(s) for role (Roles which this practitioner may perform)
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public void setRole(List<CodeableConceptDt> theValue) {
		myRole = theValue;
	}
	
	/**
	 * Gets the value(s) for specialty (Specific specialty of the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public List<CodeableConceptDt> getSpecialty() {
		return mySpecialty;
	}

	/**
	 * Sets the value(s) for specialty (Specific specialty of the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public void setSpecialty(List<CodeableConceptDt> theValue) {
		mySpecialty = theValue;
	}
	
	/**
	 * Gets the value(s) for period (The period during which the practitioner is authorized to perform in these role(s))
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which the person is authorized to act as a practitioner in these role(s) for the organization
     * </p> 
	 */
	public PeriodDt getPeriod() {
		return myPeriod;
	}

	/**
	 * Sets the value(s) for period (The period during which the practitioner is authorized to perform in these role(s))
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which the person is authorized to act as a practitioner in these role(s) for the organization
     * </p> 
	 */
	public void setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
	}
	
	/**
	 * Gets the value(s) for location (The location(s) at which this practitioner provides care)
	 *
     * <p>
     * <b>Definition:</b>
     * The location(s) at which this practitioner provides care
     * </p> 
	 */
	public List<ResourceReference> getLocation() {
		return myLocation;
	}

	/**
	 * Sets the value(s) for location (The location(s) at which this practitioner provides care)
	 *
     * <p>
     * <b>Definition:</b>
     * The location(s) at which this practitioner provides care
     * </p> 
	 */
	public void setLocation(List<ResourceReference> theValue) {
		myLocation = theValue;
	}
	
	/**
	 * Gets the value(s) for qualification (Qualifications obtained by training and certification)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public List<IDatatype> getQualification() {
		return myQualification;
	}

	/**
	 * Sets the value(s) for qualification (Qualifications obtained by training and certification)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setQualification(List<IDatatype> theValue) {
		myQualification = theValue;
	}
	
	/**
	 * Gets the value(s) for communication (A language the practitioner is able to use in patient communication)
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public List<CodeableConceptDt> getCommunication() {
		return myCommunication;
	}

	/**
	 * Sets the value(s) for communication (A language the practitioner is able to use in patient communication)
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public void setCommunication(List<CodeableConceptDt> theValue) {
		myCommunication = theValue;
	}
	
	}



}