















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
 * HAPI/FHIR <b>Organization</b> Resource
 * (A grouping of people or organizations with a common purpose)
 *
 * <p>
 * <b>Definition:</b>
 * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="Organization", profile="http://hl7.org/fhir/profiles/Organization", id="organization")
public class Organization extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of the organization's name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Organization.name</b><br/>
	 * </p>
	 */
	public static final String SP_NAME = "name";

	/**
	 * Search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of the organization's name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final String SP_PHONETIC = "phonetic";

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>A code for the type of organization</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.type</b><br/>
	 * </p>
	 */
	public static final String SP_TYPE = "type";

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Any identifier for the organization (not the accreditation issuer's identifier)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.identifier</b><br/>
	 * </p>
	 */
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>!accreditation</b>
	 * <p>
	 * Description: <b>Any accreditation code</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.accreditation.code</b><br/>
	 * </p>
	 */
	public static final String SP_ACCREDITATION = "!accreditation";

	/**
	 * Search parameter constant for <b>partof</b>
	 * <p>
	 * Description: <b>Search all organizations that are part of the given organization</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Organization.partOf</b><br/>
	 * </p>
	 */
	public static final String SP_PARTOF = "partof";

	/**
	 * Search parameter constant for <b>active</b>
	 * <p>
	 * Description: <b>Whether the organization's record is active</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.active</b><br/>
	 * </p>
	 */
	public static final String SP_ACTIVE = "active";


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Identifies this organization  across multiple systems",
		formalDefinition="Identifier for the organization that is used to identify the organization across multiple disparate systems"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Name used for the organization",
		formalDefinition="A name associated with the organization"
	)
	private StringDt myName;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Kind of organization",
		formalDefinition="The kind of organization that this is"
	)
	private BoundCodeableConceptDt<OrganizationTypeEnum> myType;
	
	@Child(name="telecom", type=ContactDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A contact detail for the organization",
		formalDefinition="A contact detail for the organization"
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="An address for the organization",
		formalDefinition="An address for the organization"
	)
	private java.util.List<AddressDt> myAddress;
	
	@Child(name="partOf", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class,
	})
	@Description(
		shortDefinition="The organization of which this organization forms a part",
		formalDefinition="The organization of which this organization forms a part"
	)
	private ResourceReferenceDt myPartOf;
	
	@Child(name="contact", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Contact for the organization for a certain purpose",
		formalDefinition=""
	)
	private java.util.List<Contact> myContact;
	
	@Child(name="location", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Location.class,
	})
	@Description(
		shortDefinition="Location(s) the organization uses to provide services",
		formalDefinition="Location(s) the organization uses to provide services"
	)
	private java.util.List<ResourceReferenceDt> myLocation;
	
	@Child(name="active", type=BooleanDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Whether the organization's record is still in active use",
		formalDefinition="Whether the organization's record is still in active use"
	)
	private BooleanDt myActive;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myType,  myTelecom,  myAddress,  myPartOf,  myContact,  myLocation,  myActive);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myType, myTelecom, myAddress, myPartOf, myContact, myLocation, myActive);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Identifies this organization  across multiple systems).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Identifies this organization  across multiple systems)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
	 */
	public Organization setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Identifies this organization  across multiple systems)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Identifies this organization  across multiple systems),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>name</b> (Name used for the organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the organization
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name used for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the organization
     * </p> 
	 */
	public Organization setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Name used for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the organization
     * </p> 
	 */
	public Organization setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (Kind of organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of organization that this is
     * </p> 
	 */
	public BoundCodeableConceptDt<OrganizationTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<OrganizationTypeEnum>(OrganizationTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Kind of organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of organization that this is
     * </p> 
	 */
	public Organization setType(BoundCodeableConceptDt<OrganizationTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Kind of organization)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of organization that this is
     * </p> 
	 */
	public Organization setType(OrganizationTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>telecom</b> (A contact detail for the organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
     * </p> 
	 */
	public java.util.List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (A contact detail for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
     * </p> 
	 */
	public Organization setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (A contact detail for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (A contact detail for the organization),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
     * </p> 
	 */
	public ContactDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>address</b> (An address for the organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An address for the organization
     * </p> 
	 */
	public java.util.List<AddressDt> getAddress() {  
		if (myAddress == null) {
			myAddress = new java.util.ArrayList<AddressDt>();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (An address for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * An address for the organization
     * </p> 
	 */
	public Organization setAddress(java.util.List<AddressDt> theValue) {
		myAddress = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>address</b> (An address for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * An address for the organization
     * </p> 
	 */
	public AddressDt addAddress() {
		AddressDt newType = new AddressDt();
		getAddress().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>address</b> (An address for the organization),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An address for the organization
     * </p> 
	 */
	public AddressDt getAddressFirstRep() {
		if (getAddress().isEmpty()) {
			return addAddress();
		}
		return getAddress().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>partOf</b> (The organization of which this organization forms a part).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The organization of which this organization forms a part
     * </p> 
	 */
	public ResourceReferenceDt getPartOf() {  
		if (myPartOf == null) {
			myPartOf = new ResourceReferenceDt();
		}
		return myPartOf;
	}

	/**
	 * Sets the value(s) for <b>partOf</b> (The organization of which this organization forms a part)
	 *
     * <p>
     * <b>Definition:</b>
     * The organization of which this organization forms a part
     * </p> 
	 */
	public Organization setPartOf(ResourceReferenceDt theValue) {
		myPartOf = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>contact</b> (Contact for the organization for a certain purpose).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<Contact> getContact() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<Contact>();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> (Contact for the organization for a certain purpose)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Organization setContact(java.util.List<Contact> theValue) {
		myContact = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>contact</b> (Contact for the organization for a certain purpose)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Contact addContact() {
		Contact newType = new Contact();
		getContact().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>contact</b> (Contact for the organization for a certain purpose),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Contact getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>location</b> (Location(s) the organization uses to provide services).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Location(s) the organization uses to provide services
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getLocation() {  
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (Location(s) the organization uses to provide services)
	 *
     * <p>
     * <b>Definition:</b>
     * Location(s) the organization uses to provide services
     * </p> 
	 */
	public Organization setLocation(java.util.List<ResourceReferenceDt> theValue) {
		myLocation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>location</b> (Location(s) the organization uses to provide services)
	 *
     * <p>
     * <b>Definition:</b>
     * Location(s) the organization uses to provide services
     * </p> 
	 */
	public ResourceReferenceDt addLocation() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getLocation().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>active</b> (Whether the organization's record is still in active use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the organization's record is still in active use
     * </p> 
	 */
	public BooleanDt getActive() {  
		if (myActive == null) {
			myActive = new BooleanDt();
		}
		return myActive;
	}

	/**
	 * Sets the value(s) for <b>active</b> (Whether the organization's record is still in active use)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the organization's record is still in active use
     * </p> 
	 */
	public Organization setActive(BooleanDt theValue) {
		myActive = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>active</b> (Whether the organization's record is still in active use)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the organization's record is still in active use
     * </p> 
	 */
	public Organization setActive( Boolean theBoolean) {
		myActive = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Organization.contact</b> (Contact for the organization for a certain purpose)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block(name="Organization.contact")	
	public static class Contact extends BaseElement implements IResourceBlock {
	
	@Child(name="purpose", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="The type of contact",
		formalDefinition="Indicates a purpose for which the contact can be reached"
	)
	private CodeableConceptDt myPurpose;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="A name associated with the contact",
		formalDefinition="A name associated with the contact"
	)
	private HumanNameDt myName;
	
	@Child(name="telecom", type=ContactDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Contact details (telephone, email, etc)  for a contact",
		formalDefinition="A contact detail (e.g. a telephone number or an email address) by which the party may be contacted."
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Visiting or postal addresses for the contact",
		formalDefinition="Visiting or postal addresses for the contact"
	)
	private AddressDt myAddress;
	
	@Child(name="gender", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Gender for administrative purposes",
		formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes."
	)
	private BoundCodeableConceptDt<AdministrativeGenderCodesEnum> myGender;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPurpose,  myName,  myTelecom,  myAddress,  myGender);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPurpose, myName, myTelecom, myAddress, myGender);
	}

	/**
	 * Gets the value(s) for <b>purpose</b> (The type of contact).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates a purpose for which the contact can be reached
     * </p> 
	 */
	public CodeableConceptDt getPurpose() {  
		if (myPurpose == null) {
			myPurpose = new CodeableConceptDt();
		}
		return myPurpose;
	}

	/**
	 * Sets the value(s) for <b>purpose</b> (The type of contact)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates a purpose for which the contact can be reached
     * </p> 
	 */
	public Contact setPurpose(CodeableConceptDt theValue) {
		myPurpose = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>name</b> (A name associated with the contact).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the contact
     * </p> 
	 */
	public HumanNameDt getName() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (A name associated with the contact)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the contact
     * </p> 
	 */
	public Contact setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>telecom</b> (Contact details (telephone, email, etc)  for a contact).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
     * </p> 
	 */
	public java.util.List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (Contact details (telephone, email, etc)  for a contact)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
     * </p> 
	 */
	public Contact setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (Contact details (telephone, email, etc)  for a contact)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (Contact details (telephone, email, etc)  for a contact),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
     * </p> 
	 */
	public ContactDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>address</b> (Visiting or postal addresses for the contact).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Visiting or postal addresses for the contact
     * </p> 
	 */
	public AddressDt getAddress() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (Visiting or postal addresses for the contact)
	 *
     * <p>
     * <b>Definition:</b>
     * Visiting or postal addresses for the contact
     * </p> 
	 */
	public Contact setAddress(AddressDt theValue) {
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
	public Contact setGender(BoundCodeableConceptDt<AdministrativeGenderCodesEnum> theValue) {
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
	public Contact setGender(AdministrativeGenderCodesEnum theValue) {
		getGender().setValueAsEnum(theValue);
		return this;
	}

  

	}




}