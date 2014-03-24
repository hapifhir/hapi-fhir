















package ca.uhn.fhir.model.dstu.resource;


import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;

import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.AdmitSourceEnum;
import ca.uhn.fhir.model.dstu.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.AnimalSpeciesEnum;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.valueset.ConformanceEventModeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceStatementStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderPriorityEnum;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.DocumentModeEnum;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExtensionContextEnum;
import ca.uhn.fhir.model.dstu.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.FilterOperatorEnum;
import ca.uhn.fhir.model.dstu.resource.Group;
import ca.uhn.fhir.model.dstu.valueset.GroupTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.HierarchicalRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.ImagingModalityEnum;
import ca.uhn.fhir.model.dstu.resource.ImagingStudy;
import ca.uhn.fhir.model.dstu.valueset.InstanceAvailabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.LinkTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.valueset.LocationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu.resource.Media;
import ca.uhn.fhir.model.dstu.valueset.MediaTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.valueset.MedicationKindEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageEventEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageSignificanceCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageTransportEnum;
import ca.uhn.fhir.model.dstu.valueset.ModalityEnum;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.valueset.ObservationInterpretationCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.OrganizationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ParticipantTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.PatientRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerSpecialtyEnum;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.RangeDt;
import ca.uhn.fhir.model.dstu.composite.RatioDt;
import ca.uhn.fhir.model.dstu.resource.RelatedPerson;
import ca.uhn.fhir.model.dstu.valueset.ResourceProfileStatusEnum;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dstu.composite.SampledDataDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.valueset.SpecimenCollectionMethodEnum;
import ca.uhn.fhir.model.dstu.valueset.SpecimenTreatmentProcedureEnum;
import ca.uhn.fhir.model.dstu.resource.Substance;
import ca.uhn.fhir.model.dstu.valueset.SubstanceTypeEnum;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.valueset.ValueSetStatusEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.OidDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;


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
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myPatient, myRelationship, myName, myTelecom, myGender, myAddress, myPhoto);
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
	public RelatedPerson setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
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
	 * Gets the first repetition for <b>identifier</b> (A Human identifier for this person),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a person within a particular scope.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
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
	public RelatedPerson setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
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
	public RelatedPerson setRelationship(BoundCodeableConceptDt<PatientRelationshipTypeEnum> theValue) {
		myRelationship = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (The nature of the relationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between a patient and the related person
     * </p> 
	 */
	public RelatedPerson setRelationship(PatientRelationshipTypeEnum theValue) {
		getRelationship().setValueAsEnum(theValue);
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
	public RelatedPerson setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
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
	public RelatedPerson setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
		return this;
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
	 * Gets the first repetition for <b>telecom</b> (A contact detail for the person),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the person, e.g. a telephone number or an email address.
     * </p> 
	 */
	public ContactDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
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
	public RelatedPerson setGender(BoundCodeableConceptDt<AdministrativeGenderCodesEnum> theValue) {
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
	public RelatedPerson setGender(AdministrativeGenderCodesEnum theValue) {
		getGender().setValueAsEnum(theValue);
		return this;
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
	public RelatedPerson setAddress(AddressDt theValue) {
		myAddress = theValue;
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
	public RelatedPerson setPhoto(java.util.List<AttachmentDt> theValue) {
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
  


}