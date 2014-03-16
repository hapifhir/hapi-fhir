















package ca.uhn.fhir.model.dstu.resource;


import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.PatientRelationshipTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;


/**
 * HAPI/FHIR <b>RelatedPerson</b> Resource
 * (An person that is related to a patient, but who is not a direct target of care)
 *
 * <p>
 * <b>Definition:</b>
 * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to track persons related to the patient or the healthcare process.
 * </p> 
 */
@ResourceDef(name="RelatedPerson", profile="http://hl7.org/fhir/profiles/RelatedPerson", id="relatedperson")
public class RelatedPerson extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>A patient Identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>RelatedPerson.identifier</b><br/>
	 * </p>
	 */
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of name in any name part</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>RelatedPerson.name</b><br/>
	 * </p>
	 */
	public static final String SP_NAME = "name";

	/**
	 * Search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final String SP_PHONETIC = "phonetic";

	/**
	 * Search parameter constant for <b>telecom</b>
	 * <p>
	 * Description: <b>The value in any kind of contact</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>RelatedPerson.telecom</b><br/>
	 * </p>
	 */
	public static final String SP_TELECOM = "telecom";

	/**
	 * Search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>An address in any kind of address/part</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>RelatedPerson.address</b><br/>
	 * </p>
	 */
	public static final String SP_ADDRESS = "address";

	/**
	 * Search parameter constant for <b>gender</b>
	 * <p>
	 * Description: <b>Gender of the person</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>RelatedPerson.gender</b><br/>
	 * </p>
	 */
	public static final String SP_GENDER = "gender";

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The patient this person is related to</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>RelatedPerson.patient</b><br/>
	 * </p>
	 */
	public static final String SP_PATIENT = "patient";


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A Human identifier for this person",
		formalDefinition="Identifier for a person within a particular scope."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="patient", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class,
	})
	@Description(
		shortDefinition="The patient this person is related to",
		formalDefinition="The patient this person is related to"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="relationship", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="The nature of the relationship",
		formalDefinition="The nature of the relationship between a patient and the related person"
	)
	private BoundCodeableConceptDt<PatientRelationshipTypeEnum> myRelationship;
	
	@Child(name="name", type=HumanNameDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="A name associated with the person",
		formalDefinition="A name associated with the person"
	)
	private HumanNameDt myName;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A contact detail for the person",
		formalDefinition="A contact detail for the person, e.g. a telephone number or an email address."
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="gender", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Gender for administrative purposes",
		formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes."
	)
	private BoundCodeableConceptDt<AdministrativeGenderCodesEnum> myGender;
	
	@Child(name="address", type=AddressDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Address where the related person can be contacted or visited",
		formalDefinition="Address where the related person can be contacted or visited"
	)
	private AddressDt myAddress;
	
	@Child(name="photo", type=AttachmentDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Image of the person",
		formalDefinition="Image of the person"
	)
	private java.util.List<AttachmentDt> myPhoto;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myPatient,  myRelationship,  myName,  myTelecom,  myGender,  myAddress,  myPhoto);
	}
	
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(  myIdentifier,  myPatient,  myRelationship,  myName,  myTelecom,  myGender,  myAddress,  myPhoto);
	}
	

	/**
	 * Gets the value(s) for <b>identifier</b> (A Human identifier for this person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a person within a particular scope.
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (A Human identifier for this person)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a person within a particular scope.
     * </p> 
	 */
	public void setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (A Human identifier for this person)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a person within a particular scope.
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> (The patient this person is related to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient this person is related to
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (The patient this person is related to)
	 *
     * <p>
     * <b>Definition:</b>
     * The patient this person is related to
     * </p> 
	 */
	public void setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>relationship</b> (The nature of the relationship).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between a patient and the related person
     * </p> 
	 */
	public BoundCodeableConceptDt<PatientRelationshipTypeEnum> getRelationship() {  
		if (myRelationship == null) {
			myRelationship = new BoundCodeableConceptDt<PatientRelationshipTypeEnum>(PatientRelationshipTypeEnum.VALUESET_BINDER);
		}
		return myRelationship;
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (The nature of the relationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between a patient and the related person
     * </p> 
	 */
	public void setRelationship(BoundCodeableConceptDt<PatientRelationshipTypeEnum> theValue) {
		myRelationship = theValue;
	}


	/**
	 * Sets the value(s) for <b>relationship</b> (The nature of the relationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between a patient and the related person
     * </p> 
	 */
	public void setRelationship(PatientRelationshipTypeEnum theValue) {
		getRelationship().setValueAsEnum(theValue);
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
	public void setName(HumanNameDt theValue) {
		myName = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>telecom</b> (A contact detail for the person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the person, e.g. a telephone number or an email address.
     * </p> 
	 */
	public java.util.List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (A contact detail for the person)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the person, e.g. a telephone number or an email address.
     * </p> 
	 */
	public void setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (A contact detail for the person)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the person, e.g. a telephone number or an email address.
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
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
	public void setGender(BoundCodeableConceptDt<AdministrativeGenderCodesEnum> theValue) {
		myGender = theValue;
	}


	/**
	 * Sets the value(s) for <b>gender</b> (Gender for administrative purposes)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public void setGender(AdministrativeGenderCodesEnum theValue) {
		getGender().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>address</b> (Address where the related person can be contacted or visited).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Address where the related person can be contacted or visited
     * </p> 
	 */
	public AddressDt getAddress() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (Address where the related person can be contacted or visited)
	 *
     * <p>
     * <b>Definition:</b>
     * Address where the related person can be contacted or visited
     * </p> 
	 */
	public void setAddress(AddressDt theValue) {
		myAddress = theValue;
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
	public void setPhoto(java.util.List<AttachmentDt> theValue) {
		myPhoto = theValue;
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

  


}