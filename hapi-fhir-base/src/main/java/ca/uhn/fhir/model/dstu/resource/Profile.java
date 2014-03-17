















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
 * HAPI/FHIR <b>Profile</b> Resource
 * (Resource Profile)
 *
 * <p>
 * <b>Definition:</b>
 * A Resource Profile - a statement of use of one or more FHIR Resources.  It may include constraints on Resources and Data Types, Terminology Binding Statements and Extension Definitions
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="Profile", profile="http://hl7.org/fhir/profiles/Profile", id="profile")
public class Profile extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the profile</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Profile.identifier</b><br/>
	 * </p>
	 */
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the profile</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Profile.version</b><br/>
	 * </p>
	 */
	public static final String SP_VERSION = "version";

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the profile</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Profile.name</b><br/>
	 * </p>
	 */
	public static final String SP_NAME = "name";

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the profile</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Profile.publisher</b><br/>
	 * </p>
	 */
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the profile</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Profile.description</b><br/>
	 * </p>
	 */
	public static final String SP_DESCRIPTION = "description";

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The current status of the profile</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Profile.status</b><br/>
	 * </p>
	 */
	public static final String SP_STATUS = "status";

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The profile publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Profile.date</b><br/>
	 * </p>
	 */
	public static final String SP_DATE = "date";

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code for the profile in the format uri::code (server may choose to do subsumption)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Profile.code</b><br/>
	 * </p>
	 */
	public static final String SP_CODE = "code";

	/**
	 * Search parameter constant for <b>extension</b>
	 * <p>
	 * Description: <b>An extension code (use or definition)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Profile.extensionDefn.code</b><br/>
	 * </p>
	 */
	public static final String SP_EXTENSION = "extension";

	/**
	 * Search parameter constant for <b>valueset</b>
	 * <p>
	 * Description: <b>A vocabulary binding code</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Profile.structure.element.definition.binding.reference[x]</b><br/>
	 * </p>
	 */
	public static final String SP_VALUESET = "valueset";

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>Type of resource that is constrained in the profile</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Profile.structure.type</b><br/>
	 * </p>
	 */
	public static final String SP_TYPE = "type";


	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Logical id to reference this profile",
		formalDefinition="The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)"
	)
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Logical id for this version of the profile",
		formalDefinition="The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Informal name for this profile",
		formalDefinition="A free text natural language name identifying the Profile"
	)
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Name of the publisher (Organization or individual)",
		formalDefinition="Details of the individual or organization who accepts responsibility for publishing the profile"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Contact information of the publisher",
		formalDefinition="Contact details to assist a user in finding and communicating with the publisher"
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Natural language description of the profile",
		formalDefinition="A free text natural language description of the profile and its use"
	)
	private StringDt myDescription;
	
	@Child(name="code", type=CodingDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Assist with indexing and finding",
		formalDefinition="A set of terms from external terminologies that may be used to assist with indexing and searching of templates."
	)
	private java.util.List<CodingDt> myCode;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="draft | active | retired",
		formalDefinition="The status of the profile"
	)
	private BoundCodeDt<ResourceProfileStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="If for testing purposes, not real usage",
		formalDefinition="This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="date", type=DateTimeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Date for this version of the profile",
		formalDefinition="The date that this version of the profile was published"
	)
	private DateTimeDt myDate;
	
	@Child(name="requirements", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Scope and Usage this profile is for",
		formalDefinition="The Scope and Usage that this profile was created to meet"
	)
	private StringDt myRequirements;
	
	@Child(name="fhirVersion", type=IdDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="FHIR Version this profile targets",
		formalDefinition="The version of the FHIR specification on which this profile is based"
	)
	private IdDt myFhirVersion;
	
	@Child(name="mapping", order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External specification that the content is mapped to",
		formalDefinition="An external specification that the content is mapped to"
	)
	private java.util.List<Mapping> myMapping;
	
	@Child(name="structure", order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A constraint on a resource or a data type",
		formalDefinition="A constraint statement about what contents a resource or data type may have"
	)
	private java.util.List<Structure> myStructure;
	
	@Child(name="extensionDefn", order=14, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Definition of an extension",
		formalDefinition="An extension defined as part of the profile"
	)
	private java.util.List<ExtensionDefn> myExtensionDefn;
	
	@Child(name="query", order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Definition of a named query",
		formalDefinition="Definition of a named query and its parameters and their meaning"
	)
	private java.util.List<Query> myQuery;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myVersion,  myName,  myPublisher,  myTelecom,  myDescription,  myCode,  myStatus,  myExperimental,  myDate,  myRequirements,  myFhirVersion,  myMapping,  myStructure,  myExtensionDefn,  myQuery);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myVersion, myName, myPublisher, myTelecom, myDescription, myCode, myStatus, myExperimental, myDate, myRequirements, myFhirVersion, myMapping, myStructure, myExtensionDefn, myQuery);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Logical id to reference this profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Logical id to reference this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
	}


 	/**
	 * Sets the value for <b>identifier</b> (Logical id to reference this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Logical id for this version of the profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Logical id for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}


 	/**
	 * Sets the value for <b>version</b> (Logical id for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion( String theString) {
		myVersion = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Informal name for this profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Informal name for this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Informal name for this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>publisher</b> (Name of the publisher (Organization or individual)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public StringDt getPublisher() {  
		if (myPublisher == null) {
			myPublisher = new StringDt();
		}
		return myPublisher;
	}

	/**
	 * Sets the value(s) for <b>publisher</b> (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public void setPublisher(StringDt theValue) {
		myPublisher = theValue;
	}


 	/**
	 * Sets the value for <b>publisher</b> (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public void setPublisher( String theString) {
		myPublisher = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>telecom</b> (Contact information of the publisher).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public java.util.List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public void setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (Natural language description of the profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Natural language description of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}


 	/**
	 * Sets the value for <b>description</b> (Natural language description of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Assist with indexing and finding).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public java.util.List<CodingDt> getCode() {  
		if (myCode == null) {
			myCode = new java.util.ArrayList<CodingDt>();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Assist with indexing and finding)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public void setCode(java.util.List<CodingDt> theValue) {
		myCode = theValue;
	}

	/**
	 * Adds and returns a new value for <b>code</b> (Assist with indexing and finding)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public CodingDt addCode() {
		CodingDt newType = new CodingDt();
		getCode().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>status</b> (draft | active | retired).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public BoundCodeDt<ResourceProfileStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ResourceProfileStatusEnum>(ResourceProfileStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public void setStatus(BoundCodeDt<ResourceProfileStatusEnum> theValue) {
		myStatus = theValue;
	}


	/**
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public void setStatus(ResourceProfileStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>experimental</b> (If for testing purposes, not real usage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {  
		if (myExperimental == null) {
			myExperimental = new BooleanDt();
		}
		return myExperimental;
	}

	/**
	 * Sets the value(s) for <b>experimental</b> (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public void setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
	}


 	/**
	 * Sets the value for <b>experimental</b> (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public void setExperimental( Boolean theBoolean) {
		myExperimental = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (Date for this version of the profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Date for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public void setDate(DateTimeDt theValue) {
		myDate = theValue;
	}


 	/**
	 * Sets the value for <b>date</b> (Date for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public void setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
	}

	/**
	 * Sets the value for <b>date</b> (Date for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public void setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
	}

 
	/**
	 * Gets the value(s) for <b>requirements</b> (Scope and Usage this profile is for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public StringDt getRequirements() {  
		if (myRequirements == null) {
			myRequirements = new StringDt();
		}
		return myRequirements;
	}

	/**
	 * Sets the value(s) for <b>requirements</b> (Scope and Usage this profile is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public void setRequirements(StringDt theValue) {
		myRequirements = theValue;
	}


 	/**
	 * Sets the value for <b>requirements</b> (Scope and Usage this profile is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public void setRequirements( String theString) {
		myRequirements = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>fhirVersion</b> (FHIR Version this profile targets).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public IdDt getFhirVersion() {  
		if (myFhirVersion == null) {
			myFhirVersion = new IdDt();
		}
		return myFhirVersion;
	}

	/**
	 * Sets the value(s) for <b>fhirVersion</b> (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public void setFhirVersion(IdDt theValue) {
		myFhirVersion = theValue;
	}


 	/**
	 * Sets the value for <b>fhirVersion</b> (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public void setFhirVersion( String theId) {
		myFhirVersion = new IdDt(theId); 
	}

 
	/**
	 * Gets the value(s) for <b>mapping</b> (External specification that the content is mapped to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public java.util.List<Mapping> getMapping() {  
		if (myMapping == null) {
			myMapping = new java.util.ArrayList<Mapping>();
		}
		return myMapping;
	}

	/**
	 * Sets the value(s) for <b>mapping</b> (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public void setMapping(java.util.List<Mapping> theValue) {
		myMapping = theValue;
	}

	/**
	 * Adds and returns a new value for <b>mapping</b> (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public Mapping addMapping() {
		Mapping newType = new Mapping();
		getMapping().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>structure</b> (A constraint on a resource or a data type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public java.util.List<Structure> getStructure() {  
		if (myStructure == null) {
			myStructure = new java.util.ArrayList<Structure>();
		}
		return myStructure;
	}

	/**
	 * Sets the value(s) for <b>structure</b> (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public void setStructure(java.util.List<Structure> theValue) {
		myStructure = theValue;
	}

	/**
	 * Adds and returns a new value for <b>structure</b> (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public Structure addStructure() {
		Structure newType = new Structure();
		getStructure().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>extensionDefn</b> (Definition of an extension).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public java.util.List<ExtensionDefn> getExtensionDefn() {  
		if (myExtensionDefn == null) {
			myExtensionDefn = new java.util.ArrayList<ExtensionDefn>();
		}
		return myExtensionDefn;
	}

	/**
	 * Sets the value(s) for <b>extensionDefn</b> (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public void setExtensionDefn(java.util.List<ExtensionDefn> theValue) {
		myExtensionDefn = theValue;
	}

	/**
	 * Adds and returns a new value for <b>extensionDefn</b> (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public ExtensionDefn addExtensionDefn() {
		ExtensionDefn newType = new ExtensionDefn();
		getExtensionDefn().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>query</b> (Definition of a named query).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public java.util.List<Query> getQuery() {  
		if (myQuery == null) {
			myQuery = new java.util.ArrayList<Query>();
		}
		return myQuery;
	}

	/**
	 * Sets the value(s) for <b>query</b> (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public void setQuery(java.util.List<Query> theValue) {
		myQuery = theValue;
	}

	/**
	 * Adds and returns a new value for <b>query</b> (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public Query addQuery() {
		Query newType = new Query();
		getQuery().add(newType);
		return newType; 
	}

  
	/**
	 * Block class for child element: <b>Profile.mapping</b> (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	@Block(name="Profile.mapping")	
	public static class Mapping extends BaseElement implements IResourceBlock {
	
	@Child(name="identity", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Internal id when this mapping is used",
		formalDefinition="An Internal id that is used to identify this mapping set when specific mappings are made"
	)
	private IdDt myIdentity;
	
	@Child(name="uri", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Identifies what this mapping refers to",
		formalDefinition="A URI that identifies the specification that this mapping is expressed to"
	)
	private UriDt myUri;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Names what this mapping refers to",
		formalDefinition="A name for the specification that is being mapped to"
	)
	private StringDt myName;
	
	@Child(name="comments", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Versions, Issues, Scope limitations etc",
		formalDefinition="Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage"
	)
	private StringDt myComments;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentity,  myUri,  myName,  myComments);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentity, myUri, myName, myComments);
	}

	/**
	 * Gets the value(s) for <b>identity</b> (Internal id when this mapping is used).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An Internal id that is used to identify this mapping set when specific mappings are made
     * </p> 
	 */
	public IdDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new IdDt();
		}
		return myIdentity;
	}

	/**
	 * Sets the value(s) for <b>identity</b> (Internal id when this mapping is used)
	 *
     * <p>
     * <b>Definition:</b>
     * An Internal id that is used to identify this mapping set when specific mappings are made
     * </p> 
	 */
	public void setIdentity(IdDt theValue) {
		myIdentity = theValue;
	}


 	/**
	 * Sets the value for <b>identity</b> (Internal id when this mapping is used)
	 *
     * <p>
     * <b>Definition:</b>
     * An Internal id that is used to identify this mapping set when specific mappings are made
     * </p> 
	 */
	public void setIdentity( String theId) {
		myIdentity = new IdDt(theId); 
	}

 
	/**
	 * Gets the value(s) for <b>uri</b> (Identifies what this mapping refers to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A URI that identifies the specification that this mapping is expressed to
     * </p> 
	 */
	public UriDt getUri() {  
		if (myUri == null) {
			myUri = new UriDt();
		}
		return myUri;
	}

	/**
	 * Sets the value(s) for <b>uri</b> (Identifies what this mapping refers to)
	 *
     * <p>
     * <b>Definition:</b>
     * A URI that identifies the specification that this mapping is expressed to
     * </p> 
	 */
	public void setUri(UriDt theValue) {
		myUri = theValue;
	}


 	/**
	 * Sets the value for <b>uri</b> (Identifies what this mapping refers to)
	 *
     * <p>
     * <b>Definition:</b>
     * A URI that identifies the specification that this mapping is expressed to
     * </p> 
	 */
	public void setUri( String theUri) {
		myUri = new UriDt(theUri); 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Names what this mapping refers to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name for the specification that is being mapped to
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Names what this mapping refers to)
	 *
     * <p>
     * <b>Definition:</b>
     * A name for the specification that is being mapped to
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Names what this mapping refers to)
	 *
     * <p>
     * <b>Definition:</b>
     * A name for the specification that is being mapped to
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>comments</b> (Versions, Issues, Scope limitations etc).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage
     * </p> 
	 */
	public StringDt getComments() {  
		if (myComments == null) {
			myComments = new StringDt();
		}
		return myComments;
	}

	/**
	 * Sets the value(s) for <b>comments</b> (Versions, Issues, Scope limitations etc)
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage
     * </p> 
	 */
	public void setComments(StringDt theValue) {
		myComments = theValue;
	}


 	/**
	 * Sets the value for <b>comments</b> (Versions, Issues, Scope limitations etc)
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage
     * </p> 
	 */
	public void setComments( String theString) {
		myComments = new StringDt(theString); 
	}

 

	}


	/**
	 * Block class for child element: <b>Profile.structure</b> (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	@Block(name="Profile.structure")	
	public static class Structure extends BaseElement implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="The Resource or Data Type being described",
		formalDefinition="The Resource or Data type being described"
	)
	private BoundCodeDt<FHIRDefinedTypeEnum> myType;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Name for this particular structure (reference target)",
		formalDefinition="The name of this resource constraint statement (to refer to it from other resource constraints - from Profile.structure.element.definition.type.profile)"
	)
	private StringDt myName;
	
	@Child(name="publish", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="This definition is published (i.e. for validation)",
		formalDefinition="This definition of a profile on a structure is published as a formal statement. Some structural definitions might be defined purely for internal use within the profile, and not intended to be used outside that context"
	)
	private BooleanDt myPublish;
	
	@Child(name="purpose", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Human summary: why describe this resource?",
		formalDefinition="Human summary: why describe this resource?"
	)
	private StringDt myPurpose;
	
	@Child(name="element", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Definition of elements in the resource (if no profile)",
		formalDefinition="Captures constraints on each element within the resource"
	)
	private java.util.List<StructureElement> myElement;
	
	@Child(name="searchParam", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Search params defined",
		formalDefinition="Additional search parameters for implementations to support and/or make use of"
	)
	private java.util.List<StructureSearchParam> mySearchParam;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myName,  myPublish,  myPurpose,  myElement,  mySearchParam);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myName, myPublish, myPurpose, myElement, mySearchParam);
	}

	/**
	 * Gets the value(s) for <b>type</b> (The Resource or Data Type being described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Resource or Data type being described
     * </p> 
	 */
	public BoundCodeDt<FHIRDefinedTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<FHIRDefinedTypeEnum>(FHIRDefinedTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (The Resource or Data Type being described)
	 *
     * <p>
     * <b>Definition:</b>
     * The Resource or Data type being described
     * </p> 
	 */
	public void setType(BoundCodeDt<FHIRDefinedTypeEnum> theValue) {
		myType = theValue;
	}


	/**
	 * Sets the value(s) for <b>type</b> (The Resource or Data Type being described)
	 *
     * <p>
     * <b>Definition:</b>
     * The Resource or Data type being described
     * </p> 
	 */
	public void setType(FHIRDefinedTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>name</b> (Name for this particular structure (reference target)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this resource constraint statement (to refer to it from other resource constraints - from Profile.structure.element.definition.type.profile)
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name for this particular structure (reference target))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this resource constraint statement (to refer to it from other resource constraints - from Profile.structure.element.definition.type.profile)
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Name for this particular structure (reference target))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this resource constraint statement (to refer to it from other resource constraints - from Profile.structure.element.definition.type.profile)
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>publish</b> (This definition is published (i.e. for validation)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This definition of a profile on a structure is published as a formal statement. Some structural definitions might be defined purely for internal use within the profile, and not intended to be used outside that context
     * </p> 
	 */
	public BooleanDt getPublish() {  
		if (myPublish == null) {
			myPublish = new BooleanDt();
		}
		return myPublish;
	}

	/**
	 * Sets the value(s) for <b>publish</b> (This definition is published (i.e. for validation))
	 *
     * <p>
     * <b>Definition:</b>
     * This definition of a profile on a structure is published as a formal statement. Some structural definitions might be defined purely for internal use within the profile, and not intended to be used outside that context
     * </p> 
	 */
	public void setPublish(BooleanDt theValue) {
		myPublish = theValue;
	}


 	/**
	 * Sets the value for <b>publish</b> (This definition is published (i.e. for validation))
	 *
     * <p>
     * <b>Definition:</b>
     * This definition of a profile on a structure is published as a formal statement. Some structural definitions might be defined purely for internal use within the profile, and not intended to be used outside that context
     * </p> 
	 */
	public void setPublish( Boolean theBoolean) {
		myPublish = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>purpose</b> (Human summary: why describe this resource?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human summary: why describe this resource?
     * </p> 
	 */
	public StringDt getPurpose() {  
		if (myPurpose == null) {
			myPurpose = new StringDt();
		}
		return myPurpose;
	}

	/**
	 * Sets the value(s) for <b>purpose</b> (Human summary: why describe this resource?)
	 *
     * <p>
     * <b>Definition:</b>
     * Human summary: why describe this resource?
     * </p> 
	 */
	public void setPurpose(StringDt theValue) {
		myPurpose = theValue;
	}


 	/**
	 * Sets the value for <b>purpose</b> (Human summary: why describe this resource?)
	 *
     * <p>
     * <b>Definition:</b>
     * Human summary: why describe this resource?
     * </p> 
	 */
	public void setPurpose( String theString) {
		myPurpose = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>element</b> (Definition of elements in the resource (if no profile)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	public java.util.List<StructureElement> getElement() {  
		if (myElement == null) {
			myElement = new java.util.ArrayList<StructureElement>();
		}
		return myElement;
	}

	/**
	 * Sets the value(s) for <b>element</b> (Definition of elements in the resource (if no profile))
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	public void setElement(java.util.List<StructureElement> theValue) {
		myElement = theValue;
	}

	/**
	 * Adds and returns a new value for <b>element</b> (Definition of elements in the resource (if no profile))
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	public StructureElement addElement() {
		StructureElement newType = new StructureElement();
		getElement().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>searchParam</b> (Search params defined).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public java.util.List<StructureSearchParam> getSearchParam() {  
		if (mySearchParam == null) {
			mySearchParam = new java.util.ArrayList<StructureSearchParam>();
		}
		return mySearchParam;
	}

	/**
	 * Sets the value(s) for <b>searchParam</b> (Search params defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public void setSearchParam(java.util.List<StructureSearchParam> theValue) {
		mySearchParam = theValue;
	}

	/**
	 * Adds and returns a new value for <b>searchParam</b> (Search params defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public StructureSearchParam addSearchParam() {
		StructureSearchParam newType = new StructureSearchParam();
		getSearchParam().add(newType);
		return newType; 
	}

  

	}

	/**
	 * Block class for child element: <b>Profile.structure.element</b> (Definition of elements in the resource (if no profile))
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	@Block(name="Profile.structure.element")	
	public static class StructureElement extends BaseElement implements IResourceBlock {
	
	@Child(name="path", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="The path of the element (see the formal definitions)",
		formalDefinition="The path identifies the element and is expressed as a \".\"-separated list of ancestor elements, beginning with the name of the resource"
	)
	private StringDt myPath;
	
	@Child(name="representation", type=CodeDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="How this element is represented in instances",
		formalDefinition="Codes that define how this element is represented in instances, when the deviation varies from the normal case"
	)
	private java.util.List<BoundCodeDt<PropertyRepresentationEnum>> myRepresentation;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Name for this particular element definition (reference target)",
		formalDefinition="The name of this element definition (to refer to it from other element definitions using Profile.structure.element.definition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element"
	)
	private StringDt myName;
	
	@Child(name="slicing", order=3, min=0, max=1)	
	@Description(
		shortDefinition="This element is sliced - slices follow",
		formalDefinition="Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)"
	)
	private StructureElementSlicing mySlicing;
	
	@Child(name="definition", order=4, min=0, max=1)	
	@Description(
		shortDefinition="More specific definition of the element",
		formalDefinition="Definition of the content of the element to provide a more specific definition than that contained for the element in the base resource"
	)
	private StructureElementDefinition myDefinition;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPath,  myRepresentation,  myName,  mySlicing,  myDefinition);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPath, myRepresentation, myName, mySlicing, myDefinition);
	}

	/**
	 * Gets the value(s) for <b>path</b> (The path of the element (see the formal definitions)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The path identifies the element and is expressed as a \".\"-separated list of ancestor elements, beginning with the name of the resource
     * </p> 
	 */
	public StringDt getPath() {  
		if (myPath == null) {
			myPath = new StringDt();
		}
		return myPath;
	}

	/**
	 * Sets the value(s) for <b>path</b> (The path of the element (see the formal definitions))
	 *
     * <p>
     * <b>Definition:</b>
     * The path identifies the element and is expressed as a \".\"-separated list of ancestor elements, beginning with the name of the resource
     * </p> 
	 */
	public void setPath(StringDt theValue) {
		myPath = theValue;
	}


 	/**
	 * Sets the value for <b>path</b> (The path of the element (see the formal definitions))
	 *
     * <p>
     * <b>Definition:</b>
     * The path identifies the element and is expressed as a \".\"-separated list of ancestor elements, beginning with the name of the resource
     * </p> 
	 */
	public void setPath( String theString) {
		myPath = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>representation</b> (How this element is represented in instances).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public java.util.List<BoundCodeDt<PropertyRepresentationEnum>> getRepresentation() {  
		if (myRepresentation == null) {
			myRepresentation = new java.util.ArrayList<BoundCodeDt<PropertyRepresentationEnum>>();
		}
		return myRepresentation;
	}

	/**
	 * Sets the value(s) for <b>representation</b> (How this element is represented in instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public void setRepresentation(java.util.List<BoundCodeDt<PropertyRepresentationEnum>> theValue) {
		myRepresentation = theValue;
	}


	/**
	 * Add a value for <b>representation</b> (How this element is represented in instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public void addRepresentation(PropertyRepresentationEnum theValue) {
		getRepresentation().add(new BoundCodeDt<PropertyRepresentationEnum>(PropertyRepresentationEnum.VALUESET_BINDER, theValue));
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>representation</b> (How this element is represented in instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public void setRepresentation(PropertyRepresentationEnum theValue) {
		getRepresentation().clear();
		addRepresentation(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>name</b> (Name for this particular element definition (reference target)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this element definition (to refer to it from other element definitions using Profile.structure.element.definition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name for this particular element definition (reference target))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this element definition (to refer to it from other element definitions using Profile.structure.element.definition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Name for this particular element definition (reference target))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this element definition (to refer to it from other element definitions using Profile.structure.element.definition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>slicing</b> (This element is sliced - slices follow).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	public StructureElementSlicing getSlicing() {  
		if (mySlicing == null) {
			mySlicing = new StructureElementSlicing();
		}
		return mySlicing;
	}

	/**
	 * Sets the value(s) for <b>slicing</b> (This element is sliced - slices follow)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	public void setSlicing(StructureElementSlicing theValue) {
		mySlicing = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>definition</b> (More specific definition of the element).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of the content of the element to provide a more specific definition than that contained for the element in the base resource
     * </p> 
	 */
	public StructureElementDefinition getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new StructureElementDefinition();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> (More specific definition of the element)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of the content of the element to provide a more specific definition than that contained for the element in the base resource
     * </p> 
	 */
	public void setDefinition(StructureElementDefinition theValue) {
		myDefinition = theValue;
	}


  

	}

	/**
	 * Block class for child element: <b>Profile.structure.element.slicing</b> (This element is sliced - slices follow)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	@Block(name="Profile.structure.element.slicing")	
	public static class StructureElementSlicing extends BaseElement implements IResourceBlock {
	
	@Child(name="discriminator", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Element that used to distinguish the slices",
		formalDefinition="Designates which child element is used to discriminate between the slices when processing an instance. The value of the child element in the instance SHALL completely distinguish which slice the element in the resource matches based on the allowed values for that element in each of the slices"
	)
	private IdDt myDiscriminator;
	
	@Child(name="ordered", type=BooleanDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="If elements must be in same order as slices",
		formalDefinition="If the matching elements have to occur in the same order as defined in the profile"
	)
	private BooleanDt myOrdered;
	
	@Child(name="rules", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="closed | open | openAtEnd",
		formalDefinition="Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end"
	)
	private BoundCodeDt<SlicingRulesEnum> myRules;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDiscriminator,  myOrdered,  myRules);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDiscriminator, myOrdered, myRules);
	}

	/**
	 * Gets the value(s) for <b>discriminator</b> (Element that used to distinguish the slices).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child element is used to discriminate between the slices when processing an instance. The value of the child element in the instance SHALL completely distinguish which slice the element in the resource matches based on the allowed values for that element in each of the slices
     * </p> 
	 */
	public IdDt getDiscriminator() {  
		if (myDiscriminator == null) {
			myDiscriminator = new IdDt();
		}
		return myDiscriminator;
	}

	/**
	 * Sets the value(s) for <b>discriminator</b> (Element that used to distinguish the slices)
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child element is used to discriminate between the slices when processing an instance. The value of the child element in the instance SHALL completely distinguish which slice the element in the resource matches based on the allowed values for that element in each of the slices
     * </p> 
	 */
	public void setDiscriminator(IdDt theValue) {
		myDiscriminator = theValue;
	}


 	/**
	 * Sets the value for <b>discriminator</b> (Element that used to distinguish the slices)
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child element is used to discriminate between the slices when processing an instance. The value of the child element in the instance SHALL completely distinguish which slice the element in the resource matches based on the allowed values for that element in each of the slices
     * </p> 
	 */
	public void setDiscriminator( String theId) {
		myDiscriminator = new IdDt(theId); 
	}

 
	/**
	 * Gets the value(s) for <b>ordered</b> (If elements must be in same order as slices).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the matching elements have to occur in the same order as defined in the profile
     * </p> 
	 */
	public BooleanDt getOrdered() {  
		if (myOrdered == null) {
			myOrdered = new BooleanDt();
		}
		return myOrdered;
	}

	/**
	 * Sets the value(s) for <b>ordered</b> (If elements must be in same order as slices)
	 *
     * <p>
     * <b>Definition:</b>
     * If the matching elements have to occur in the same order as defined in the profile
     * </p> 
	 */
	public void setOrdered(BooleanDt theValue) {
		myOrdered = theValue;
	}


 	/**
	 * Sets the value for <b>ordered</b> (If elements must be in same order as slices)
	 *
     * <p>
     * <b>Definition:</b>
     * If the matching elements have to occur in the same order as defined in the profile
     * </p> 
	 */
	public void setOrdered( Boolean theBoolean) {
		myOrdered = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>rules</b> (closed | open | openAtEnd).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end
     * </p> 
	 */
	public BoundCodeDt<SlicingRulesEnum> getRules() {  
		if (myRules == null) {
			myRules = new BoundCodeDt<SlicingRulesEnum>(SlicingRulesEnum.VALUESET_BINDER);
		}
		return myRules;
	}

	/**
	 * Sets the value(s) for <b>rules</b> (closed | open | openAtEnd)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end
     * </p> 
	 */
	public void setRules(BoundCodeDt<SlicingRulesEnum> theValue) {
		myRules = theValue;
	}


	/**
	 * Sets the value(s) for <b>rules</b> (closed | open | openAtEnd)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end
     * </p> 
	 */
	public void setRules(SlicingRulesEnum theValue) {
		getRules().setValueAsEnum(theValue);
	}

  

	}


	/**
	 * Block class for child element: <b>Profile.structure.element.definition</b> (More specific definition of the element)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of the content of the element to provide a more specific definition than that contained for the element in the base resource
     * </p> 
	 */
	@Block(name="Profile.structure.element.definition")	
	public static class StructureElementDefinition extends BaseElement implements IResourceBlock {
	
	@Child(name="short", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Concise definition for xml presentation",
		formalDefinition="A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)"
	)
	private StringDt myShort;
	
	@Child(name="formal", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Full formal definition in human language",
		formalDefinition="The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource"
	)
	private StringDt myFormal;
	
	@Child(name="comments", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Comments about the use of this element",
		formalDefinition="Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc."
	)
	private StringDt myComments;
	
	@Child(name="requirements", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Why is this needed?",
		formalDefinition="Explains why this element is needed and why it's been constrained as it has"
	)
	private StringDt myRequirements;
	
	@Child(name="synonym", type=StringDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Other names",
		formalDefinition="Identifies additional names by which this element might also be known"
	)
	private java.util.List<StringDt> mySynonym;
	
	@Child(name="min", type=IntegerDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Minimum Cardinality",
		formalDefinition="The minimum number of times this element SHALL appear in the instance"
	)
	private IntegerDt myMin;
	
	@Child(name="max", type=StringDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="Maximum Cardinality (a number or *)",
		formalDefinition="The maximum number of times this element is permitted to appear in the instance"
	)
	private StringDt myMax;
	
	@Child(name="type", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Data type and Profile for this element",
		formalDefinition="The data type or resource that the value of this element is permitted to be"
	)
	private java.util.List<StructureElementDefinitionType> myType;
	
	@Child(name="nameReference", type=StringDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="To another element constraint (by element.name)",
		formalDefinition="Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element"
	)
	private StringDt myNameReference;
	
	@Child(name="value", type=IDatatype.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Fixed value: [as defined for a primitive type]",
		formalDefinition="Specifies a primitive value that SHALL hold for this element in the instance"
	)
	private IDatatype myValue;
	
	@Child(name="example", type=IDatatype.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Example value: [as defined for type]",
		formalDefinition="An example value for this element"
	)
	private IDatatype myExample;
	
	@Child(name="maxLength", type=IntegerDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="Length for strings",
		formalDefinition="Indicates the shortest length that SHALL be supported by conformant instances without truncation"
	)
	private IntegerDt myMaxLength;
	
	@Child(name="condition", type=IdDt.class, order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Reference to invariant about presence",
		formalDefinition="A reference to an invariant that may make additional statements about the cardinality or value in the instance"
	)
	private java.util.List<IdDt> myCondition;
	
	@Child(name="constraint", order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Condition that must evaluate to true",
		formalDefinition="Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance"
	)
	private java.util.List<StructureElementDefinitionConstraint> myConstraint;
	
	@Child(name="mustSupport", type=BooleanDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="If the element must supported",
		formalDefinition="If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported"
	)
	private BooleanDt myMustSupport;
	
	@Child(name="isModifier", type=BooleanDt.class, order=15, min=1, max=1)	
	@Description(
		shortDefinition="If this modifies the meaning of other elements",
		formalDefinition="If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system."
	)
	private BooleanDt myIsModifier;
	
	@Child(name="binding", order=16, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet details if this is coded",
		formalDefinition="Binds to a value set if this element is coded (code, Coding, CodeableConcept)"
	)
	private StructureElementDefinitionBinding myBinding;
	
	@Child(name="mapping", order=17, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Map element to another set of definitions",
		formalDefinition="Identifies a concept from an external specification that roughly corresponds to this element"
	)
	private java.util.List<StructureElementDefinitionMapping> myMapping;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myShort,  myFormal,  myComments,  myRequirements,  mySynonym,  myMin,  myMax,  myType,  myNameReference,  myValue,  myExample,  myMaxLength,  myCondition,  myConstraint,  myMustSupport,  myIsModifier,  myBinding,  myMapping);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myShort, myFormal, myComments, myRequirements, mySynonym, myMin, myMax, myType, myNameReference, myValue, myExample, myMaxLength, myCondition, myConstraint, myMustSupport, myIsModifier, myBinding, myMapping);
	}

	/**
	 * Gets the value(s) for <b>short</b> (Concise definition for xml presentation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)
     * </p> 
	 */
	public StringDt getShort() {  
		if (myShort == null) {
			myShort = new StringDt();
		}
		return myShort;
	}

	/**
	 * Sets the value(s) for <b>short</b> (Concise definition for xml presentation)
	 *
     * <p>
     * <b>Definition:</b>
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)
     * </p> 
	 */
	public void setShort(StringDt theValue) {
		myShort = theValue;
	}


 	/**
	 * Sets the value for <b>short</b> (Concise definition for xml presentation)
	 *
     * <p>
     * <b>Definition:</b>
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)
     * </p> 
	 */
	public void setShort( String theString) {
		myShort = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>formal</b> (Full formal definition in human language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public StringDt getFormal() {  
		if (myFormal == null) {
			myFormal = new StringDt();
		}
		return myFormal;
	}

	/**
	 * Sets the value(s) for <b>formal</b> (Full formal definition in human language)
	 *
     * <p>
     * <b>Definition:</b>
     * The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public void setFormal(StringDt theValue) {
		myFormal = theValue;
	}


 	/**
	 * Sets the value for <b>formal</b> (Full formal definition in human language)
	 *
     * <p>
     * <b>Definition:</b>
     * The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public void setFormal( String theString) {
		myFormal = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>comments</b> (Comments about the use of this element).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public StringDt getComments() {  
		if (myComments == null) {
			myComments = new StringDt();
		}
		return myComments;
	}

	/**
	 * Sets the value(s) for <b>comments</b> (Comments about the use of this element)
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public void setComments(StringDt theValue) {
		myComments = theValue;
	}


 	/**
	 * Sets the value for <b>comments</b> (Comments about the use of this element)
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public void setComments( String theString) {
		myComments = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>requirements</b> (Why is this needed?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public StringDt getRequirements() {  
		if (myRequirements == null) {
			myRequirements = new StringDt();
		}
		return myRequirements;
	}

	/**
	 * Sets the value(s) for <b>requirements</b> (Why is this needed?)
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public void setRequirements(StringDt theValue) {
		myRequirements = theValue;
	}


 	/**
	 * Sets the value for <b>requirements</b> (Why is this needed?)
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public void setRequirements( String theString) {
		myRequirements = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>synonym</b> (Other names).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public java.util.List<StringDt> getSynonym() {  
		if (mySynonym == null) {
			mySynonym = new java.util.ArrayList<StringDt>();
		}
		return mySynonym;
	}

	/**
	 * Sets the value(s) for <b>synonym</b> (Other names)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public void setSynonym(java.util.List<StringDt> theValue) {
		mySynonym = theValue;
	}

	/**
	 * Adds and returns a new value for <b>synonym</b> (Other names)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public StringDt addSynonym() {
		StringDt newType = new StringDt();
		getSynonym().add(newType);
		return newType; 
	}

 	/**
	 * Adds a new value for <b>synonym</b> (Other names)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public void addSynonym( String theString) {
		if (mySynonym == null) {
			mySynonym = new java.util.ArrayList<StringDt>();
		}
		mySynonym.add(new StringDt(theString)); 
	}

 
	/**
	 * Gets the value(s) for <b>min</b> (Minimum Cardinality).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this element SHALL appear in the instance
     * </p> 
	 */
	public IntegerDt getMin() {  
		if (myMin == null) {
			myMin = new IntegerDt();
		}
		return myMin;
	}

	/**
	 * Sets the value(s) for <b>min</b> (Minimum Cardinality)
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this element SHALL appear in the instance
     * </p> 
	 */
	public void setMin(IntegerDt theValue) {
		myMin = theValue;
	}


 	/**
	 * Sets the value for <b>min</b> (Minimum Cardinality)
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this element SHALL appear in the instance
     * </p> 
	 */
	public void setMin( Integer theInteger) {
		myMin = new IntegerDt(theInteger); 
	}

 
	/**
	 * Gets the value(s) for <b>max</b> (Maximum Cardinality (a number or *)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the instance
     * </p> 
	 */
	public StringDt getMax() {  
		if (myMax == null) {
			myMax = new StringDt();
		}
		return myMax;
	}

	/**
	 * Sets the value(s) for <b>max</b> (Maximum Cardinality (a number or *))
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the instance
     * </p> 
	 */
	public void setMax(StringDt theValue) {
		myMax = theValue;
	}


 	/**
	 * Sets the value for <b>max</b> (Maximum Cardinality (a number or *))
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the instance
     * </p> 
	 */
	public void setMax( String theString) {
		myMax = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (Data type and Profile for this element).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	public java.util.List<StructureElementDefinitionType> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<StructureElementDefinitionType>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Data type and Profile for this element)
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	public void setType(java.util.List<StructureElementDefinitionType> theValue) {
		myType = theValue;
	}

	/**
	 * Adds and returns a new value for <b>type</b> (Data type and Profile for this element)
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	public StructureElementDefinitionType addType() {
		StructureElementDefinitionType newType = new StructureElementDefinitionType();
		getType().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>nameReference</b> (To another element constraint (by element.name)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element
     * </p> 
	 */
	public StringDt getNameReference() {  
		if (myNameReference == null) {
			myNameReference = new StringDt();
		}
		return myNameReference;
	}

	/**
	 * Sets the value(s) for <b>nameReference</b> (To another element constraint (by element.name))
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element
     * </p> 
	 */
	public void setNameReference(StringDt theValue) {
		myNameReference = theValue;
	}


 	/**
	 * Sets the value for <b>nameReference</b> (To another element constraint (by element.name))
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element
     * </p> 
	 */
	public void setNameReference( String theString) {
		myNameReference = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>value[x]</b> (Fixed value: [as defined for a primitive type]).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a primitive value that SHALL hold for this element in the instance
     * </p> 
	 */
	public IDatatype getValue() {  
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value[x]</b> (Fixed value: [as defined for a primitive type])
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a primitive value that SHALL hold for this element in the instance
     * </p> 
	 */
	public void setValue(IDatatype theValue) {
		myValue = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>example[x]</b> (Example value: [as defined for type]).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An example value for this element
     * </p> 
	 */
	public IDatatype getExample() {  
		return myExample;
	}

	/**
	 * Sets the value(s) for <b>example[x]</b> (Example value: [as defined for type])
	 *
     * <p>
     * <b>Definition:</b>
     * An example value for this element
     * </p> 
	 */
	public void setExample(IDatatype theValue) {
		myExample = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>maxLength</b> (Length for strings).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the shortest length that SHALL be supported by conformant instances without truncation
     * </p> 
	 */
	public IntegerDt getMaxLength() {  
		if (myMaxLength == null) {
			myMaxLength = new IntegerDt();
		}
		return myMaxLength;
	}

	/**
	 * Sets the value(s) for <b>maxLength</b> (Length for strings)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the shortest length that SHALL be supported by conformant instances without truncation
     * </p> 
	 */
	public void setMaxLength(IntegerDt theValue) {
		myMaxLength = theValue;
	}


 	/**
	 * Sets the value for <b>maxLength</b> (Length for strings)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the shortest length that SHALL be supported by conformant instances without truncation
     * </p> 
	 */
	public void setMaxLength( Integer theInteger) {
		myMaxLength = new IntegerDt(theInteger); 
	}

 
	/**
	 * Gets the value(s) for <b>condition</b> (Reference to invariant about presence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public java.util.List<IdDt> getCondition() {  
		if (myCondition == null) {
			myCondition = new java.util.ArrayList<IdDt>();
		}
		return myCondition;
	}

	/**
	 * Sets the value(s) for <b>condition</b> (Reference to invariant about presence)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public void setCondition(java.util.List<IdDt> theValue) {
		myCondition = theValue;
	}

	/**
	 * Adds and returns a new value for <b>condition</b> (Reference to invariant about presence)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public IdDt addCondition() {
		IdDt newType = new IdDt();
		getCondition().add(newType);
		return newType; 
	}

 	/**
	 * Adds a new value for <b>condition</b> (Reference to invariant about presence)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public void addCondition( String theId) {
		if (myCondition == null) {
			myCondition = new java.util.ArrayList<IdDt>();
		}
		myCondition.add(new IdDt(theId)); 
	}

 
	/**
	 * Gets the value(s) for <b>constraint</b> (Condition that must evaluate to true).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	public java.util.List<StructureElementDefinitionConstraint> getConstraint() {  
		if (myConstraint == null) {
			myConstraint = new java.util.ArrayList<StructureElementDefinitionConstraint>();
		}
		return myConstraint;
	}

	/**
	 * Sets the value(s) for <b>constraint</b> (Condition that must evaluate to true)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	public void setConstraint(java.util.List<StructureElementDefinitionConstraint> theValue) {
		myConstraint = theValue;
	}

	/**
	 * Adds and returns a new value for <b>constraint</b> (Condition that must evaluate to true)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	public StructureElementDefinitionConstraint addConstraint() {
		StructureElementDefinitionConstraint newType = new StructureElementDefinitionConstraint();
		getConstraint().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>mustSupport</b> (If the element must supported).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public BooleanDt getMustSupport() {  
		if (myMustSupport == null) {
			myMustSupport = new BooleanDt();
		}
		return myMustSupport;
	}

	/**
	 * Sets the value(s) for <b>mustSupport</b> (If the element must supported)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public void setMustSupport(BooleanDt theValue) {
		myMustSupport = theValue;
	}


 	/**
	 * Sets the value for <b>mustSupport</b> (If the element must supported)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public void setMustSupport( Boolean theBoolean) {
		myMustSupport = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>isModifier</b> (If this modifies the meaning of other elements).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * </p> 
	 */
	public BooleanDt getIsModifier() {  
		if (myIsModifier == null) {
			myIsModifier = new BooleanDt();
		}
		return myIsModifier;
	}

	/**
	 * Sets the value(s) for <b>isModifier</b> (If this modifies the meaning of other elements)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * </p> 
	 */
	public void setIsModifier(BooleanDt theValue) {
		myIsModifier = theValue;
	}


 	/**
	 * Sets the value for <b>isModifier</b> (If this modifies the meaning of other elements)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * </p> 
	 */
	public void setIsModifier( Boolean theBoolean) {
		myIsModifier = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>binding</b> (ValueSet details if this is coded).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	public StructureElementDefinitionBinding getBinding() {  
		if (myBinding == null) {
			myBinding = new StructureElementDefinitionBinding();
		}
		return myBinding;
	}

	/**
	 * Sets the value(s) for <b>binding</b> (ValueSet details if this is coded)
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	public void setBinding(StructureElementDefinitionBinding theValue) {
		myBinding = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>mapping</b> (Map element to another set of definitions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public java.util.List<StructureElementDefinitionMapping> getMapping() {  
		if (myMapping == null) {
			myMapping = new java.util.ArrayList<StructureElementDefinitionMapping>();
		}
		return myMapping;
	}

	/**
	 * Sets the value(s) for <b>mapping</b> (Map element to another set of definitions)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public void setMapping(java.util.List<StructureElementDefinitionMapping> theValue) {
		myMapping = theValue;
	}

	/**
	 * Adds and returns a new value for <b>mapping</b> (Map element to another set of definitions)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public StructureElementDefinitionMapping addMapping() {
		StructureElementDefinitionMapping newType = new StructureElementDefinitionMapping();
		getMapping().add(newType);
		return newType; 
	}

  

	}

	/**
	 * Block class for child element: <b>Profile.structure.element.definition.type</b> (Data type and Profile for this element)
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	@Block(name="Profile.structure.element.definition.type")	
	public static class StructureElementDefinitionType extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Name of Data type or Resource",
		formalDefinition=""
	)
	private BoundCodeDt<DataTypeEnum> myCode;
	
	@Child(name="profile", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Profile.structure to apply",
		formalDefinition="Identifies a profile that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile"
	)
	private UriDt myProfile;
	
	@Child(name="aggregation", type=CodeDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="contained | referenced | bundled - how aggregated",
		formalDefinition="If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle"
	)
	private java.util.List<BoundCodeDt<AggregationModeEnum>> myAggregation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myProfile,  myAggregation);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myProfile, myAggregation);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Name of Data type or Resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<DataTypeEnum> getCode() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<DataTypeEnum>(DataTypeEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Name of Data type or Resource)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setCode(BoundCodeDt<DataTypeEnum> theValue) {
		myCode = theValue;
	}


	/**
	 * Sets the value(s) for <b>code</b> (Name of Data type or Resource)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setCode(DataTypeEnum theValue) {
		getCode().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>profile</b> (Profile.structure to apply).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile
     * </p> 
	 */
	public UriDt getProfile() {  
		if (myProfile == null) {
			myProfile = new UriDt();
		}
		return myProfile;
	}

	/**
	 * Sets the value(s) for <b>profile</b> (Profile.structure to apply)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile
     * </p> 
	 */
	public void setProfile(UriDt theValue) {
		myProfile = theValue;
	}


 	/**
	 * Sets the value for <b>profile</b> (Profile.structure to apply)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile
     * </p> 
	 */
	public void setProfile( String theUri) {
		myProfile = new UriDt(theUri); 
	}

 
	/**
	 * Gets the value(s) for <b>aggregation</b> (contained | referenced | bundled - how aggregated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public java.util.List<BoundCodeDt<AggregationModeEnum>> getAggregation() {  
		if (myAggregation == null) {
			myAggregation = new java.util.ArrayList<BoundCodeDt<AggregationModeEnum>>();
		}
		return myAggregation;
	}

	/**
	 * Sets the value(s) for <b>aggregation</b> (contained | referenced | bundled - how aggregated)
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public void setAggregation(java.util.List<BoundCodeDt<AggregationModeEnum>> theValue) {
		myAggregation = theValue;
	}


	/**
	 * Add a value for <b>aggregation</b> (contained | referenced | bundled - how aggregated)
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public void addAggregation(AggregationModeEnum theValue) {
		getAggregation().add(new BoundCodeDt<AggregationModeEnum>(AggregationModeEnum.VALUESET_BINDER, theValue));
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>aggregation</b> (contained | referenced | bundled - how aggregated)
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public void setAggregation(AggregationModeEnum theValue) {
		getAggregation().clear();
		addAggregation(theValue);
	}

  

	}


	/**
	 * Block class for child element: <b>Profile.structure.element.definition.constraint</b> (Condition that must evaluate to true)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	@Block(name="Profile.structure.element.definition.constraint")	
	public static class StructureElementDefinitionConstraint extends BaseElement implements IResourceBlock {
	
	@Child(name="key", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Target of 'condition' reference above",
		formalDefinition="Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality"
	)
	private IdDt myKey;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Short human label",
		formalDefinition="Used to label the constraint in OCL or in short displays incapable of displaying the full human description"
	)
	private StringDt myName;
	
	@Child(name="severity", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="error | warning",
		formalDefinition="Identifies the impact constraint violation has on the conformance of the instance"
	)
	private BoundCodeDt<ConstraintSeverityEnum> mySeverity;
	
	@Child(name="human", type=StringDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Human description of constraint",
		formalDefinition="Text that can be used to describe the constraint in messages identifying that the constraint has been violated"
	)
	private StringDt myHuman;
	
	@Child(name="xpath", type=StringDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="XPath expression of constraint",
		formalDefinition="XPath expression of constraint"
	)
	private StringDt myXpath;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myKey,  myName,  mySeverity,  myHuman,  myXpath);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myKey, myName, mySeverity, myHuman, myXpath);
	}

	/**
	 * Gets the value(s) for <b>key</b> (Target of 'condition' reference above).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality
     * </p> 
	 */
	public IdDt getKey() {  
		if (myKey == null) {
			myKey = new IdDt();
		}
		return myKey;
	}

	/**
	 * Sets the value(s) for <b>key</b> (Target of 'condition' reference above)
	 *
     * <p>
     * <b>Definition:</b>
     * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality
     * </p> 
	 */
	public void setKey(IdDt theValue) {
		myKey = theValue;
	}


 	/**
	 * Sets the value for <b>key</b> (Target of 'condition' reference above)
	 *
     * <p>
     * <b>Definition:</b>
     * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality
     * </p> 
	 */
	public void setKey( String theId) {
		myKey = new IdDt(theId); 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Short human label).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Used to label the constraint in OCL or in short displays incapable of displaying the full human description
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Short human label)
	 *
     * <p>
     * <b>Definition:</b>
     * Used to label the constraint in OCL or in short displays incapable of displaying the full human description
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Short human label)
	 *
     * <p>
     * <b>Definition:</b>
     * Used to label the constraint in OCL or in short displays incapable of displaying the full human description
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>severity</b> (error | warning).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the impact constraint violation has on the conformance of the instance
     * </p> 
	 */
	public BoundCodeDt<ConstraintSeverityEnum> getSeverity() {  
		if (mySeverity == null) {
			mySeverity = new BoundCodeDt<ConstraintSeverityEnum>(ConstraintSeverityEnum.VALUESET_BINDER);
		}
		return mySeverity;
	}

	/**
	 * Sets the value(s) for <b>severity</b> (error | warning)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the impact constraint violation has on the conformance of the instance
     * </p> 
	 */
	public void setSeverity(BoundCodeDt<ConstraintSeverityEnum> theValue) {
		mySeverity = theValue;
	}


	/**
	 * Sets the value(s) for <b>severity</b> (error | warning)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the impact constraint violation has on the conformance of the instance
     * </p> 
	 */
	public void setSeverity(ConstraintSeverityEnum theValue) {
		getSeverity().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>human</b> (Human description of constraint).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Text that can be used to describe the constraint in messages identifying that the constraint has been violated
     * </p> 
	 */
	public StringDt getHuman() {  
		if (myHuman == null) {
			myHuman = new StringDt();
		}
		return myHuman;
	}

	/**
	 * Sets the value(s) for <b>human</b> (Human description of constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * Text that can be used to describe the constraint in messages identifying that the constraint has been violated
     * </p> 
	 */
	public void setHuman(StringDt theValue) {
		myHuman = theValue;
	}


 	/**
	 * Sets the value for <b>human</b> (Human description of constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * Text that can be used to describe the constraint in messages identifying that the constraint has been violated
     * </p> 
	 */
	public void setHuman( String theString) {
		myHuman = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>xpath</b> (XPath expression of constraint).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * XPath expression of constraint
     * </p> 
	 */
	public StringDt getXpath() {  
		if (myXpath == null) {
			myXpath = new StringDt();
		}
		return myXpath;
	}

	/**
	 * Sets the value(s) for <b>xpath</b> (XPath expression of constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * XPath expression of constraint
     * </p> 
	 */
	public void setXpath(StringDt theValue) {
		myXpath = theValue;
	}


 	/**
	 * Sets the value for <b>xpath</b> (XPath expression of constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * XPath expression of constraint
     * </p> 
	 */
	public void setXpath( String theString) {
		myXpath = new StringDt(theString); 
	}

 

	}


	/**
	 * Block class for child element: <b>Profile.structure.element.definition.binding</b> (ValueSet details if this is coded)
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	@Block(name="Profile.structure.element.definition.binding")	
	public static class StructureElementDefinitionBinding extends BaseElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Descriptive Name",
		formalDefinition="A descriptive name for this - can be useful for generating implementation artifacts"
	)
	private StringDt myName;
	
	@Child(name="isExtensible", type=BooleanDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Can additional codes be used?",
		formalDefinition="If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone"
	)
	private BooleanDt myIsExtensible;
	
	@Child(name="conformance", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="required | preferred | example",
		formalDefinition="Indicates the degree of conformance expectations associated with this binding"
	)
	private BoundCodeDt<BindingConformanceEnum> myConformance;
	
	@Child(name="description", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Human explanation of the value set",
		formalDefinition="Describes the intended use of this particular set of codes"
	)
	private StringDt myDescription;
	
	@Child(name="reference", order=4, min=0, max=1, type={
		UriDt.class,
		ValueSet.class,
	})
	@Description(
		shortDefinition="Source of value set",
		formalDefinition="Points to the value set or external definition that identifies the set of codes to be used"
	)
	private IDatatype myReference;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myIsExtensible,  myConformance,  myDescription,  myReference);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myIsExtensible, myConformance, myDescription, myReference);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Descriptive Name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A descriptive name for this - can be useful for generating implementation artifacts
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Descriptive Name)
	 *
     * <p>
     * <b>Definition:</b>
     * A descriptive name for this - can be useful for generating implementation artifacts
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Descriptive Name)
	 *
     * <p>
     * <b>Definition:</b>
     * A descriptive name for this - can be useful for generating implementation artifacts
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>isExtensible</b> (Can additional codes be used?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public BooleanDt getIsExtensible() {  
		if (myIsExtensible == null) {
			myIsExtensible = new BooleanDt();
		}
		return myIsExtensible;
	}

	/**
	 * Sets the value(s) for <b>isExtensible</b> (Can additional codes be used?)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public void setIsExtensible(BooleanDt theValue) {
		myIsExtensible = theValue;
	}


 	/**
	 * Sets the value for <b>isExtensible</b> (Can additional codes be used?)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public void setIsExtensible( Boolean theBoolean) {
		myIsExtensible = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>conformance</b> (required | preferred | example).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public BoundCodeDt<BindingConformanceEnum> getConformance() {  
		if (myConformance == null) {
			myConformance = new BoundCodeDt<BindingConformanceEnum>(BindingConformanceEnum.VALUESET_BINDER);
		}
		return myConformance;
	}

	/**
	 * Sets the value(s) for <b>conformance</b> (required | preferred | example)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public void setConformance(BoundCodeDt<BindingConformanceEnum> theValue) {
		myConformance = theValue;
	}


	/**
	 * Sets the value(s) for <b>conformance</b> (required | preferred | example)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public void setConformance(BindingConformanceEnum theValue) {
		getConformance().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (Human explanation of the value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Human explanation of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}


 	/**
	 * Sets the value for <b>description</b> (Human explanation of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>reference[x]</b> (Source of value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Points to the value set or external definition that identifies the set of codes to be used
     * </p> 
	 */
	public IDatatype getReference() {  
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference[x]</b> (Source of value set)
	 *
     * <p>
     * <b>Definition:</b>
     * Points to the value set or external definition that identifies the set of codes to be used
     * </p> 
	 */
	public void setReference(IDatatype theValue) {
		myReference = theValue;
	}


  

	}


	/**
	 * Block class for child element: <b>Profile.structure.element.definition.mapping</b> (Map element to another set of definitions)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	@Block(name="Profile.structure.element.definition.mapping")	
	public static class StructureElementDefinitionMapping extends BaseElement implements IResourceBlock {
	
	@Child(name="identity", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Reference to mapping declaration",
		formalDefinition="An internal reference to the definition of a mapping"
	)
	private IdDt myIdentity;
	
	@Child(name="map", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Details of the mapping",
		formalDefinition="Expresses what part of the target specification corresponds to this element"
	)
	private StringDt myMap;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentity,  myMap);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentity, myMap);
	}

	/**
	 * Gets the value(s) for <b>identity</b> (Reference to mapping declaration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An internal reference to the definition of a mapping
     * </p> 
	 */
	public IdDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new IdDt();
		}
		return myIdentity;
	}

	/**
	 * Sets the value(s) for <b>identity</b> (Reference to mapping declaration)
	 *
     * <p>
     * <b>Definition:</b>
     * An internal reference to the definition of a mapping
     * </p> 
	 */
	public void setIdentity(IdDt theValue) {
		myIdentity = theValue;
	}


 	/**
	 * Sets the value for <b>identity</b> (Reference to mapping declaration)
	 *
     * <p>
     * <b>Definition:</b>
     * An internal reference to the definition of a mapping
     * </p> 
	 */
	public void setIdentity( String theId) {
		myIdentity = new IdDt(theId); 
	}

 
	/**
	 * Gets the value(s) for <b>map</b> (Details of the mapping).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public StringDt getMap() {  
		if (myMap == null) {
			myMap = new StringDt();
		}
		return myMap;
	}

	/**
	 * Sets the value(s) for <b>map</b> (Details of the mapping)
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public void setMap(StringDt theValue) {
		myMap = theValue;
	}


 	/**
	 * Sets the value for <b>map</b> (Details of the mapping)
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public void setMap( String theString) {
		myMap = new StringDt(theString); 
	}

 

	}




	/**
	 * Block class for child element: <b>Profile.structure.searchParam</b> (Search params defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	@Block(name="Profile.structure.searchParam")	
	public static class StructureSearchParam extends BaseElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Name of search parameter",
		formalDefinition="The name of the standard or custom search parameter"
	)
	private StringDt myName;
	
	@Child(name="type", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="number | date | string | token | reference | composite | quantity",
		formalDefinition="The type of value a search parameter refers to, and how the content is interpreted"
	)
	private BoundCodeDt<SearchParamTypeEnum> myType;
	
	@Child(name="documentation", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Contents and meaning of search parameter",
		formalDefinition="A specification for search parameters. For standard parameters, provides additional information on how the parameter is used in this solution.  For custom parameters, provides a description of what the parameter does"
	)
	private StringDt myDocumentation;
	
	@Child(name="xpath", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="XPath that extracts the parameter set",
		formalDefinition="An XPath expression that returns a set of elements for the search parameter"
	)
	private StringDt myXpath;
	
	@Child(name="target", type=CodeDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Types of resource (if a resource reference)",
		formalDefinition="Types of resource (if a resource is referenced)"
	)
	private java.util.List<BoundCodeDt<ResourceTypeEnum>> myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myType,  myDocumentation,  myXpath,  myTarget);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myType, myDocumentation, myXpath, myTarget);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Name of search parameter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the standard or custom search parameter
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name of search parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the standard or custom search parameter
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Name of search parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the standard or custom search parameter
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (number | date | string | token | reference | composite | quantity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public BoundCodeDt<SearchParamTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<SearchParamTypeEnum>(SearchParamTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (number | date | string | token | reference | composite | quantity)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public void setType(BoundCodeDt<SearchParamTypeEnum> theValue) {
		myType = theValue;
	}


	/**
	 * Sets the value(s) for <b>type</b> (number | date | string | token | reference | composite | quantity)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public void setType(SearchParamTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (Contents and meaning of search parameter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification for search parameters. For standard parameters, provides additional information on how the parameter is used in this solution.  For custom parameters, provides a description of what the parameter does
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Contents and meaning of search parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification for search parameters. For standard parameters, provides additional information on how the parameter is used in this solution.  For custom parameters, provides a description of what the parameter does
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}


 	/**
	 * Sets the value for <b>documentation</b> (Contents and meaning of search parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification for search parameters. For standard parameters, provides additional information on how the parameter is used in this solution.  For custom parameters, provides a description of what the parameter does
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>xpath</b> (XPath that extracts the parameter set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An XPath expression that returns a set of elements for the search parameter
     * </p> 
	 */
	public StringDt getXpath() {  
		if (myXpath == null) {
			myXpath = new StringDt();
		}
		return myXpath;
	}

	/**
	 * Sets the value(s) for <b>xpath</b> (XPath that extracts the parameter set)
	 *
     * <p>
     * <b>Definition:</b>
     * An XPath expression that returns a set of elements for the search parameter
     * </p> 
	 */
	public void setXpath(StringDt theValue) {
		myXpath = theValue;
	}


 	/**
	 * Sets the value for <b>xpath</b> (XPath that extracts the parameter set)
	 *
     * <p>
     * <b>Definition:</b>
     * An XPath expression that returns a set of elements for the search parameter
     * </p> 
	 */
	public void setXpath( String theString) {
		myXpath = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>target</b> (Types of resource (if a resource reference)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public java.util.List<BoundCodeDt<ResourceTypeEnum>> getTarget() {  
		if (myTarget == null) {
			myTarget = new java.util.ArrayList<BoundCodeDt<ResourceTypeEnum>>();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (Types of resource (if a resource reference))
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public void setTarget(java.util.List<BoundCodeDt<ResourceTypeEnum>> theValue) {
		myTarget = theValue;
	}


	/**
	 * Add a value for <b>target</b> (Types of resource (if a resource reference))
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public void addTarget(ResourceTypeEnum theValue) {
		getTarget().add(new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER, theValue));
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>target</b> (Types of resource (if a resource reference))
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public void setTarget(ResourceTypeEnum theValue) {
		getTarget().clear();
		addTarget(theValue);
	}

  

	}



	/**
	 * Block class for child element: <b>Profile.extensionDefn</b> (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	@Block(name="Profile.extensionDefn")	
	public static class ExtensionDefn extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Identifies the extension in this profile",
		formalDefinition="A unique code (within the profile) used to identify the extension"
	)
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Use this name when displaying the value",
		formalDefinition="Defined so that applications can use this name when displaying the value of the extension to the user"
	)
	private StringDt myDisplay;
	
	@Child(name="contextType", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="resource | datatype | mapping | extension",
		formalDefinition="Identifies the type of context to which the extension applies"
	)
	private BoundCodeDt<ExtensionContextEnum> myContextType;
	
	@Child(name="context", type=StringDt.class, order=3, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Where the extension can be used in instances",
		formalDefinition="Identifies the types of resource or data type elements to which the extension can be applied"
	)
	private java.util.List<StringDt> myContext;
	
	@Child(name="definition", type=StructureElementDefinition.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Definition of the extension and its content",
		formalDefinition="Definition of the extension and its content"
	)
	private StructureElementDefinition myDefinition;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDisplay,  myContextType,  myContext,  myDefinition);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myDisplay, myContextType, myContext, myDefinition);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Identifies the extension in this profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique code (within the profile) used to identify the extension
     * </p> 
	 */
	public CodeDt getCode() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Identifies the extension in this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique code (within the profile) used to identify the extension
     * </p> 
	 */
	public void setCode(CodeDt theValue) {
		myCode = theValue;
	}


 	/**
	 * Sets the value for <b>code</b> (Identifies the extension in this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique code (within the profile) used to identify the extension
     * </p> 
	 */
	public void setCode( String theCode) {
		myCode = new CodeDt(theCode); 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (Use this name when displaying the value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Defined so that applications can use this name when displaying the value of the extension to the user
     * </p> 
	 */
	public StringDt getDisplay() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (Use this name when displaying the value)
	 *
     * <p>
     * <b>Definition:</b>
     * Defined so that applications can use this name when displaying the value of the extension to the user
     * </p> 
	 */
	public void setDisplay(StringDt theValue) {
		myDisplay = theValue;
	}


 	/**
	 * Sets the value for <b>display</b> (Use this name when displaying the value)
	 *
     * <p>
     * <b>Definition:</b>
     * Defined so that applications can use this name when displaying the value of the extension to the user
     * </p> 
	 */
	public void setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>contextType</b> (resource | datatype | mapping | extension).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of context to which the extension applies
     * </p> 
	 */
	public BoundCodeDt<ExtensionContextEnum> getContextType() {  
		if (myContextType == null) {
			myContextType = new BoundCodeDt<ExtensionContextEnum>(ExtensionContextEnum.VALUESET_BINDER);
		}
		return myContextType;
	}

	/**
	 * Sets the value(s) for <b>contextType</b> (resource | datatype | mapping | extension)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of context to which the extension applies
     * </p> 
	 */
	public void setContextType(BoundCodeDt<ExtensionContextEnum> theValue) {
		myContextType = theValue;
	}


	/**
	 * Sets the value(s) for <b>contextType</b> (resource | datatype | mapping | extension)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of context to which the extension applies
     * </p> 
	 */
	public void setContextType(ExtensionContextEnum theValue) {
		getContextType().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>context</b> (Where the extension can be used in instances).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public java.util.List<StringDt> getContext() {  
		if (myContext == null) {
			myContext = new java.util.ArrayList<StringDt>();
		}
		return myContext;
	}

	/**
	 * Sets the value(s) for <b>context</b> (Where the extension can be used in instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public void setContext(java.util.List<StringDt> theValue) {
		myContext = theValue;
	}

	/**
	 * Adds and returns a new value for <b>context</b> (Where the extension can be used in instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public StringDt addContext() {
		StringDt newType = new StringDt();
		getContext().add(newType);
		return newType; 
	}

 	/**
	 * Adds a new value for <b>context</b> (Where the extension can be used in instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public void addContext( String theString) {
		if (myContext == null) {
			myContext = new java.util.ArrayList<StringDt>();
		}
		myContext.add(new StringDt(theString)); 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> (Definition of the extension and its content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of the extension and its content
     * </p> 
	 */
	public StructureElementDefinition getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new StructureElementDefinition();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> (Definition of the extension and its content)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of the extension and its content
     * </p> 
	 */
	public void setDefinition(StructureElementDefinition theValue) {
		myDefinition = theValue;
	}


  

	}


	/**
	 * Block class for child element: <b>Profile.query</b> (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	@Block(name="Profile.query")	
	public static class Query extends BaseElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Special named queries (_query=)",
		formalDefinition="The name of a query, which is used in the URI from Conformance statements declaring use of the query.  Typically this will also be the name for the _query parameter when the query is called, though in some cases it may be aliased by a server to avoid collisions"
	)
	private StringDt myName;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Describes the named query",
		formalDefinition="Description of the query - the functionality it offers, and considerations about how it functions and to use it"
	)
	private StringDt myDocumentation;
	
	@Child(name="parameter", type=StructureSearchParam.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Parameter for the named query",
		formalDefinition="A parameter of a named query"
	)
	private java.util.List<StructureSearchParam> myParameter;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myDocumentation,  myParameter);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myDocumentation, myParameter);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Special named queries (_query=)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the URI from Conformance statements declaring use of the query.  Typically this will also be the name for the _query parameter when the query is called, though in some cases it may be aliased by a server to avoid collisions
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Special named queries (_query=))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the URI from Conformance statements declaring use of the query.  Typically this will also be the name for the _query parameter when the query is called, though in some cases it may be aliased by a server to avoid collisions
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Special named queries (_query=))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the URI from Conformance statements declaring use of the query.  Typically this will also be the name for the _query parameter when the query is called, though in some cases it may be aliased by a server to avoid collisions
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>documentation</b> (Describes the named query).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the query - the functionality it offers, and considerations about how it functions and to use it
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Describes the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the query - the functionality it offers, and considerations about how it functions and to use it
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}


 	/**
	 * Sets the value for <b>documentation</b> (Describes the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the query - the functionality it offers, and considerations about how it functions and to use it
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>parameter</b> (Parameter for the named query).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A parameter of a named query
     * </p> 
	 */
	public java.util.List<StructureSearchParam> getParameter() {  
		if (myParameter == null) {
			myParameter = new java.util.ArrayList<StructureSearchParam>();
		}
		return myParameter;
	}

	/**
	 * Sets the value(s) for <b>parameter</b> (Parameter for the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * A parameter of a named query
     * </p> 
	 */
	public void setParameter(java.util.List<StructureSearchParam> theValue) {
		myParameter = theValue;
	}

	/**
	 * Adds and returns a new value for <b>parameter</b> (Parameter for the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * A parameter of a named query
     * </p> 
	 */
	public StructureSearchParam addParameter() {
		StructureSearchParam newType = new StructureSearchParam();
		getParameter().add(newType);
		return newType; 
	}

  

	}




}