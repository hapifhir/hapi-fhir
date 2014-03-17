















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
 * HAPI/FHIR <b>ValueSet</b> Resource
 * (A set of codes drawn from one or more code systems)
 *
 * <p>
 * <b>Definition:</b>
 * A value set specifies a set of codes drawn from one or more code systems
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="ValueSet", profile="http://hl7.org/fhir/profiles/ValueSet", id="valueset")
public class ValueSet extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.identifier</b><br/>
	 * </p>
	 */
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.version</b><br/>
	 * </p>
	 */
	public static final String SP_VERSION = "version";

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the value set</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ValueSet.name</b><br/>
	 * </p>
	 */
	public static final String SP_NAME = "name";

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the value set</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ValueSet.publisher</b><br/>
	 * </p>
	 */
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the value set</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ValueSet.description</b><br/>
	 * </p>
	 */
	public static final String SP_DESCRIPTION = "description";

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.status</b><br/>
	 * </p>
	 */
	public static final String SP_STATUS = "status";

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The value set publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ValueSet.date</b><br/>
	 * </p>
	 */
	public static final String SP_DATE = "date";

	/**
	 * Search parameter constant for <b>system</b>
	 * <p>
	 * Description: <b>The system for any codes defined by this value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.define.system</b><br/>
	 * </p>
	 */
	public static final String SP_SYSTEM = "system";

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code defined in the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.define.concept.code</b><br/>
	 * </p>
	 */
	public static final String SP_CODE = "code";

	/**
	 * Search parameter constant for <b>reference</b>
	 * <p>
	 * Description: <b>A code system included or excluded in the value set or an imported value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.compose.include.system</b><br/>
	 * </p>
	 */
	public static final String SP_REFERENCE = "reference";

	/**
	 * Search parameter constant for <b>!restricts</b>
	 * <p>
	 * Description: <b>A value set listed in the restricts list</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.compose.restricts</b><br/>
	 * </p>
	 */
	public static final String SP_RESTRICTS = "!restricts";


	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Logical id to reference this value set",
		formalDefinition="The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)"
	)
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Logical id for this version of the value set",
		formalDefinition="The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Informal name for this value set",
		formalDefinition="A free text natural language name describing the value set"
	)
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Name of the publisher (Organization or individual)",
		formalDefinition="The name of the individual or organization that published the value set"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Contact information of the publisher",
		formalDefinition="Contacts of the publisher to assist a user in finding and communicating with the publisher"
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Human language description of the value set",
		formalDefinition="A free text natural language description of the use of the value set - reason for definition, conditions of use, etc."
	)
	private StringDt myDescription;
	
	@Child(name="copyright", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="About the value set or its content",
		formalDefinition="A copyright statement relating to the value set and/or its contents"
	)
	private StringDt myCopyright;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="draft | active | retired",
		formalDefinition="The status of the value set"
	)
	private BoundCodeDt<ValueSetStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="If for testing purposes, not real usage",
		formalDefinition="This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="extensible", type=BooleanDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Whether this is intended to be used with an extensible binding",
		formalDefinition="Whether this is intended to be used with an extensible binding or not"
	)
	private BooleanDt myExtensible;
	
	@Child(name="date", type=DateTimeDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Date for given status",
		formalDefinition="The date that the value set status was last changed"
	)
	private DateTimeDt myDate;
	
	@Child(name="define", order=11, min=0, max=1)	
	@Description(
		shortDefinition="When value set defines its own codes",
		formalDefinition=""
	)
	private Define myDefine;
	
	@Child(name="compose", order=12, min=0, max=1)	
	@Description(
		shortDefinition="When value set includes codes from elsewhere",
		formalDefinition=""
	)
	private Compose myCompose;
	
	@Child(name="expansion", order=13, min=0, max=1)	
	@Description(
		shortDefinition="When value set is an expansion",
		formalDefinition=""
	)
	private Expansion myExpansion;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myVersion,  myName,  myPublisher,  myTelecom,  myDescription,  myCopyright,  myStatus,  myExperimental,  myExtensible,  myDate,  myDefine,  myCompose,  myExpansion);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myVersion, myName, myPublisher, myTelecom, myDescription, myCopyright, myStatus, myExperimental, myExtensible, myDate, myDefine, myCompose, myExpansion);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Logical id to reference this value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
	}


 	/**
	 * Sets the value for <b>identifier</b> (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Logical id for this version of the value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Logical id for this version of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}


 	/**
	 * Sets the value for <b>version</b> (Logical id for this version of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion( String theString) {
		myVersion = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Informal name for this value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Informal name for this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Informal name for this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
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
     * The name of the individual or organization that published the value set
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
     * The name of the individual or organization that published the value set
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
     * The name of the individual or organization that published the value set
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
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
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
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
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
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (Human language description of the value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}


 	/**
	 * Sets the value for <b>description</b> (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>copyright</b> (About the value set or its content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public StringDt getCopyright() {  
		if (myCopyright == null) {
			myCopyright = new StringDt();
		}
		return myCopyright;
	}

	/**
	 * Sets the value(s) for <b>copyright</b> (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public void setCopyright(StringDt theValue) {
		myCopyright = theValue;
	}


 	/**
	 * Sets the value for <b>copyright</b> (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public void setCopyright( String theString) {
		myCopyright = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (draft | active | retired).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public BoundCodeDt<ValueSetStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ValueSetStatusEnum>(ValueSetStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public void setStatus(BoundCodeDt<ValueSetStatusEnum> theValue) {
		myStatus = theValue;
	}


	/**
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public void setStatus(ValueSetStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>experimental</b> (If for testing purposes, not real usage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
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
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
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
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public void setExperimental( Boolean theBoolean) {
		myExperimental = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>extensible</b> (Whether this is intended to be used with an extensible binding).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public BooleanDt getExtensible() {  
		if (myExtensible == null) {
			myExtensible = new BooleanDt();
		}
		return myExtensible;
	}

	/**
	 * Sets the value(s) for <b>extensible</b> (Whether this is intended to be used with an extensible binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public void setExtensible(BooleanDt theValue) {
		myExtensible = theValue;
	}


 	/**
	 * Sets the value for <b>extensible</b> (Whether this is intended to be used with an extensible binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public void setExtensible( Boolean theBoolean) {
		myExtensible = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (Date for given status).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public void setDate(DateTimeDt theValue) {
		myDate = theValue;
	}


 	/**
	 * Sets the value for <b>date</b> (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public void setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
	}

	/**
	 * Sets the value for <b>date</b> (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public void setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
	}

 
	/**
	 * Gets the value(s) for <b>define</b> (When value set defines its own codes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Define getDefine() {  
		if (myDefine == null) {
			myDefine = new Define();
		}
		return myDefine;
	}

	/**
	 * Sets the value(s) for <b>define</b> (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setDefine(Define theValue) {
		myDefine = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>compose</b> (When value set includes codes from elsewhere).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Compose getCompose() {  
		if (myCompose == null) {
			myCompose = new Compose();
		}
		return myCompose;
	}

	/**
	 * Sets the value(s) for <b>compose</b> (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setCompose(Compose theValue) {
		myCompose = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>expansion</b> (When value set is an expansion).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Expansion getExpansion() {  
		if (myExpansion == null) {
			myExpansion = new Expansion();
		}
		return myExpansion;
	}

	/**
	 * Sets the value(s) for <b>expansion</b> (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setExpansion(Expansion theValue) {
		myExpansion = theValue;
	}


  
	/**
	 * Block class for child element: <b>ValueSet.define</b> (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block(name="ValueSet.define")	
	public static class Define extends BaseElement implements IResourceBlock {
	
	@Child(name="system", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="URI to identify the code system",
		formalDefinition=""
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Version of this system",
		formalDefinition="The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked"
	)
	private StringDt myVersion;
	
	@Child(name="caseSensitive", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="If code comparison is case sensitive",
		formalDefinition="If code comparison is case sensitive when codes within this system are compared to each other"
	)
	private BooleanDt myCaseSensitive;
	
	@Child(name="concept", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Concepts in the code system",
		formalDefinition=""
	)
	private java.util.List<DefineConcept> myConcept;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myVersion,  myCaseSensitive,  myConcept);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myVersion, myCaseSensitive, myConcept);
	}

	/**
	 * Gets the value(s) for <b>system</b> (URI to identify the code system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getSystem() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (URI to identify the code system)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setSystem(UriDt theValue) {
		mySystem = theValue;
	}


 	/**
	 * Sets the value for <b>system</b> (URI to identify the code system)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Version of this system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Version of this system)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}


 	/**
	 * Sets the value for <b>version</b> (Version of this system)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked
     * </p> 
	 */
	public void setVersion( String theString) {
		myVersion = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>caseSensitive</b> (If code comparison is case sensitive).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If code comparison is case sensitive when codes within this system are compared to each other
     * </p> 
	 */
	public BooleanDt getCaseSensitive() {  
		if (myCaseSensitive == null) {
			myCaseSensitive = new BooleanDt();
		}
		return myCaseSensitive;
	}

	/**
	 * Sets the value(s) for <b>caseSensitive</b> (If code comparison is case sensitive)
	 *
     * <p>
     * <b>Definition:</b>
     * If code comparison is case sensitive when codes within this system are compared to each other
     * </p> 
	 */
	public void setCaseSensitive(BooleanDt theValue) {
		myCaseSensitive = theValue;
	}


 	/**
	 * Sets the value for <b>caseSensitive</b> (If code comparison is case sensitive)
	 *
     * <p>
     * <b>Definition:</b>
     * If code comparison is case sensitive when codes within this system are compared to each other
     * </p> 
	 */
	public void setCaseSensitive( Boolean theBoolean) {
		myCaseSensitive = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>concept</b> (Concepts in the code system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<DefineConcept> getConcept() {  
		if (myConcept == null) {
			myConcept = new java.util.ArrayList<DefineConcept>();
		}
		return myConcept;
	}

	/**
	 * Sets the value(s) for <b>concept</b> (Concepts in the code system)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setConcept(java.util.List<DefineConcept> theValue) {
		myConcept = theValue;
	}

	/**
	 * Adds and returns a new value for <b>concept</b> (Concepts in the code system)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DefineConcept addConcept() {
		DefineConcept newType = new DefineConcept();
		getConcept().add(newType);
		return newType; 
	}

  

	}

	/**
	 * Block class for child element: <b>ValueSet.define.concept</b> (Concepts in the code system)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block(name="ValueSet.define.concept")	
	public static class DefineConcept extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Code that identifies concept",
		formalDefinition=""
	)
	private CodeDt myCode;
	
	@Child(name="abstract", type=BooleanDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="If this code is not for use as a real concept",
		formalDefinition="If this code is not for use as a real concept"
	)
	private BooleanDt myAbstract;
	
	@Child(name="display", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Text to Display to the user",
		formalDefinition=""
	)
	private StringDt myDisplay;
	
	@Child(name="definition", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Formal Definition",
		formalDefinition="The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept"
	)
	private StringDt myDefinition;
	
	@Child(name="concept", type=DefineConcept.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Child Concepts (is-a / contains)",
		formalDefinition=""
	)
	private java.util.List<DefineConcept> myConcept;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myAbstract,  myDisplay,  myDefinition,  myConcept);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myAbstract, myDisplay, myDefinition, myConcept);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Code that identifies concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getCode() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Code that identifies concept)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setCode(CodeDt theValue) {
		myCode = theValue;
	}


 	/**
	 * Sets the value for <b>code</b> (Code that identifies concept)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setCode( String theCode) {
		myCode = new CodeDt(theCode); 
	}

 
	/**
	 * Gets the value(s) for <b>abstract</b> (If this code is not for use as a real concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this code is not for use as a real concept
     * </p> 
	 */
	public BooleanDt getAbstract() {  
		if (myAbstract == null) {
			myAbstract = new BooleanDt();
		}
		return myAbstract;
	}

	/**
	 * Sets the value(s) for <b>abstract</b> (If this code is not for use as a real concept)
	 *
     * <p>
     * <b>Definition:</b>
     * If this code is not for use as a real concept
     * </p> 
	 */
	public void setAbstract(BooleanDt theValue) {
		myAbstract = theValue;
	}


 	/**
	 * Sets the value for <b>abstract</b> (If this code is not for use as a real concept)
	 *
     * <p>
     * <b>Definition:</b>
     * If this code is not for use as a real concept
     * </p> 
	 */
	public void setAbstract( Boolean theBoolean) {
		myAbstract = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (Text to Display to the user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getDisplay() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (Text to Display to the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setDisplay(StringDt theValue) {
		myDisplay = theValue;
	}


 	/**
	 * Sets the value for <b>display</b> (Text to Display to the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> (Formal Definition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public StringDt getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new StringDt();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> (Formal Definition)
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public void setDefinition(StringDt theValue) {
		myDefinition = theValue;
	}


 	/**
	 * Sets the value for <b>definition</b> (Formal Definition)
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public void setDefinition( String theString) {
		myDefinition = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>concept</b> (Child Concepts (is-a / contains)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<DefineConcept> getConcept() {  
		if (myConcept == null) {
			myConcept = new java.util.ArrayList<DefineConcept>();
		}
		return myConcept;
	}

	/**
	 * Sets the value(s) for <b>concept</b> (Child Concepts (is-a / contains))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setConcept(java.util.List<DefineConcept> theValue) {
		myConcept = theValue;
	}

	/**
	 * Adds and returns a new value for <b>concept</b> (Child Concepts (is-a / contains))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DefineConcept addConcept() {
		DefineConcept newType = new DefineConcept();
		getConcept().add(newType);
		return newType; 
	}

  

	}



	/**
	 * Block class for child element: <b>ValueSet.compose</b> (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block(name="ValueSet.compose")	
	public static class Compose extends BaseElement implements IResourceBlock {
	
	@Child(name="import", type=UriDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Import the contents of another value set",
		formalDefinition="Includes the contents of the referenced value set as a part of the contents of this value set"
	)
	private java.util.List<UriDt> myImport;
	
	@Child(name="include", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Include one or more codes from a code system",
		formalDefinition="Include one or more codes from a code system"
	)
	private java.util.List<ComposeInclude> myInclude;
	
	@Child(name="exclude", type=ComposeInclude.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Explicitly exclude codes",
		formalDefinition="Exclude one or more codes from the value set"
	)
	private java.util.List<ComposeInclude> myExclude;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myImport,  myInclude,  myExclude);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myImport, myInclude, myExclude);
	}

	/**
	 * Gets the value(s) for <b>import</b> (Import the contents of another value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set
     * </p> 
	 */
	public java.util.List<UriDt> getImport() {  
		if (myImport == null) {
			myImport = new java.util.ArrayList<UriDt>();
		}
		return myImport;
	}

	/**
	 * Sets the value(s) for <b>import</b> (Import the contents of another value set)
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set
     * </p> 
	 */
	public void setImport(java.util.List<UriDt> theValue) {
		myImport = theValue;
	}

	/**
	 * Adds and returns a new value for <b>import</b> (Import the contents of another value set)
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set
     * </p> 
	 */
	public UriDt addImport() {
		UriDt newType = new UriDt();
		getImport().add(newType);
		return newType; 
	}

 	/**
	 * Adds a new value for <b>import</b> (Import the contents of another value set)
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set
     * </p> 
	 */
	public void addImport( String theUri) {
		if (myImport == null) {
			myImport = new java.util.ArrayList<UriDt>();
		}
		myImport.add(new UriDt(theUri)); 
	}

 
	/**
	 * Gets the value(s) for <b>include</b> (Include one or more codes from a code system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Include one or more codes from a code system
     * </p> 
	 */
	public java.util.List<ComposeInclude> getInclude() {  
		if (myInclude == null) {
			myInclude = new java.util.ArrayList<ComposeInclude>();
		}
		return myInclude;
	}

	/**
	 * Sets the value(s) for <b>include</b> (Include one or more codes from a code system)
	 *
     * <p>
     * <b>Definition:</b>
     * Include one or more codes from a code system
     * </p> 
	 */
	public void setInclude(java.util.List<ComposeInclude> theValue) {
		myInclude = theValue;
	}

	/**
	 * Adds and returns a new value for <b>include</b> (Include one or more codes from a code system)
	 *
     * <p>
     * <b>Definition:</b>
     * Include one or more codes from a code system
     * </p> 
	 */
	public ComposeInclude addInclude() {
		ComposeInclude newType = new ComposeInclude();
		getInclude().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>exclude</b> (Explicitly exclude codes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Exclude one or more codes from the value set
     * </p> 
	 */
	public java.util.List<ComposeInclude> getExclude() {  
		if (myExclude == null) {
			myExclude = new java.util.ArrayList<ComposeInclude>();
		}
		return myExclude;
	}

	/**
	 * Sets the value(s) for <b>exclude</b> (Explicitly exclude codes)
	 *
     * <p>
     * <b>Definition:</b>
     * Exclude one or more codes from the value set
     * </p> 
	 */
	public void setExclude(java.util.List<ComposeInclude> theValue) {
		myExclude = theValue;
	}

	/**
	 * Adds and returns a new value for <b>exclude</b> (Explicitly exclude codes)
	 *
     * <p>
     * <b>Definition:</b>
     * Exclude one or more codes from the value set
     * </p> 
	 */
	public ComposeInclude addExclude() {
		ComposeInclude newType = new ComposeInclude();
		getExclude().add(newType);
		return newType; 
	}

  

	}

	/**
	 * Block class for child element: <b>ValueSet.compose.include</b> (Include one or more codes from a code system)
	 *
     * <p>
     * <b>Definition:</b>
     * Include one or more codes from a code system
     * </p> 
	 */
	@Block(name="ValueSet.compose.include")	
	public static class ComposeInclude extends BaseElement implements IResourceBlock {
	
	@Child(name="system", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="The system the codes come from",
		formalDefinition="The code system from which the selected codes come from"
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Specific version of the code system referred to",
		formalDefinition="The version of the code system that the codes are selected from"
	)
	private StringDt myVersion;
	
	@Child(name="code", type=CodeDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Code or concept from system",
		formalDefinition="Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance"
	)
	private java.util.List<CodeDt> myCode;
	
	@Child(name="filter", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Select codes/concepts by their properties (including relationships)",
		formalDefinition="Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true."
	)
	private java.util.List<ComposeIncludeFilter> myFilter;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myVersion,  myCode,  myFilter);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myVersion, myCode, myFilter);
	}

	/**
	 * Gets the value(s) for <b>system</b> (The system the codes come from).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code system from which the selected codes come from
     * </p> 
	 */
	public UriDt getSystem() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (The system the codes come from)
	 *
     * <p>
     * <b>Definition:</b>
     * The code system from which the selected codes come from
     * </p> 
	 */
	public void setSystem(UriDt theValue) {
		mySystem = theValue;
	}


 	/**
	 * Sets the value for <b>system</b> (The system the codes come from)
	 *
     * <p>
     * <b>Definition:</b>
     * The code system from which the selected codes come from
     * </p> 
	 */
	public void setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Specific version of the code system referred to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system that the codes are selected from
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Specific version of the code system referred to)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system that the codes are selected from
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}


 	/**
	 * Sets the value for <b>version</b> (Specific version of the code system referred to)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system that the codes are selected from
     * </p> 
	 */
	public void setVersion( String theString) {
		myVersion = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Code or concept from system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance
     * </p> 
	 */
	public java.util.List<CodeDt> getCode() {  
		if (myCode == null) {
			myCode = new java.util.ArrayList<CodeDt>();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Code or concept from system)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance
     * </p> 
	 */
	public void setCode(java.util.List<CodeDt> theValue) {
		myCode = theValue;
	}

	/**
	 * Adds and returns a new value for <b>code</b> (Code or concept from system)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance
     * </p> 
	 */
	public CodeDt addCode() {
		CodeDt newType = new CodeDt();
		getCode().add(newType);
		return newType; 
	}

 	/**
	 * Adds a new value for <b>code</b> (Code or concept from system)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance
     * </p> 
	 */
	public void addCode( String theCode) {
		if (myCode == null) {
			myCode = new java.util.ArrayList<CodeDt>();
		}
		myCode.add(new CodeDt(theCode)); 
	}

 
	/**
	 * Gets the value(s) for <b>filter</b> (Select codes/concepts by their properties (including relationships)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
     * </p> 
	 */
	public java.util.List<ComposeIncludeFilter> getFilter() {  
		if (myFilter == null) {
			myFilter = new java.util.ArrayList<ComposeIncludeFilter>();
		}
		return myFilter;
	}

	/**
	 * Sets the value(s) for <b>filter</b> (Select codes/concepts by their properties (including relationships))
	 *
     * <p>
     * <b>Definition:</b>
     * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
     * </p> 
	 */
	public void setFilter(java.util.List<ComposeIncludeFilter> theValue) {
		myFilter = theValue;
	}

	/**
	 * Adds and returns a new value for <b>filter</b> (Select codes/concepts by their properties (including relationships))
	 *
     * <p>
     * <b>Definition:</b>
     * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
     * </p> 
	 */
	public ComposeIncludeFilter addFilter() {
		ComposeIncludeFilter newType = new ComposeIncludeFilter();
		getFilter().add(newType);
		return newType; 
	}

  

	}

	/**
	 * Block class for child element: <b>ValueSet.compose.include.filter</b> (Select codes/concepts by their properties (including relationships))
	 *
     * <p>
     * <b>Definition:</b>
     * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
     * </p> 
	 */
	@Block(name="ValueSet.compose.include.filter")	
	public static class ComposeIncludeFilter extends BaseElement implements IResourceBlock {
	
	@Child(name="property", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A code that identifies a property defined in the code system"
	)
	private CodeDt myProperty;
	
	@Child(name="op", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="= | is-a | is-not-a | regex | in | not in",
		formalDefinition="The kind of operation to perform as a part of the filter criteria"
	)
	private BoundCodeDt<FilterOperatorEnum> myOp;
	
	@Child(name="value", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Code from the system, or regex criteria",
		formalDefinition="The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value"
	)
	private CodeDt myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myProperty,  myOp,  myValue);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myProperty, myOp, myValue);
	}

	/**
	 * Gets the value(s) for <b>property</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a property defined in the code system
     * </p> 
	 */
	public CodeDt getProperty() {  
		if (myProperty == null) {
			myProperty = new CodeDt();
		}
		return myProperty;
	}

	/**
	 * Sets the value(s) for <b>property</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a property defined in the code system
     * </p> 
	 */
	public void setProperty(CodeDt theValue) {
		myProperty = theValue;
	}


 	/**
	 * Sets the value for <b>property</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a property defined in the code system
     * </p> 
	 */
	public void setProperty( String theCode) {
		myProperty = new CodeDt(theCode); 
	}

 
	/**
	 * Gets the value(s) for <b>op</b> (= | is-a | is-not-a | regex | in | not in).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public BoundCodeDt<FilterOperatorEnum> getOp() {  
		if (myOp == null) {
			myOp = new BoundCodeDt<FilterOperatorEnum>(FilterOperatorEnum.VALUESET_BINDER);
		}
		return myOp;
	}

	/**
	 * Sets the value(s) for <b>op</b> (= | is-a | is-not-a | regex | in | not in)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public void setOp(BoundCodeDt<FilterOperatorEnum> theValue) {
		myOp = theValue;
	}


	/**
	 * Sets the value(s) for <b>op</b> (= | is-a | is-not-a | regex | in | not in)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public void setOp(FilterOperatorEnum theValue) {
		getOp().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>value</b> (Code from the system, or regex criteria).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value
     * </p> 
	 */
	public CodeDt getValue() {  
		if (myValue == null) {
			myValue = new CodeDt();
		}
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value</b> (Code from the system, or regex criteria)
	 *
     * <p>
     * <b>Definition:</b>
     * The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value
     * </p> 
	 */
	public void setValue(CodeDt theValue) {
		myValue = theValue;
	}


 	/**
	 * Sets the value for <b>value</b> (Code from the system, or regex criteria)
	 *
     * <p>
     * <b>Definition:</b>
     * The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value
     * </p> 
	 */
	public void setValue( String theCode) {
		myValue = new CodeDt(theCode); 
	}

 

	}




	/**
	 * Block class for child element: <b>ValueSet.expansion</b> (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block(name="ValueSet.expansion")	
	public static class Expansion extends BaseElement implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Uniquely identifies this expansion",
		formalDefinition="An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="timestamp", type=InstantDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Time valueset expansion happened",
		formalDefinition=""
	)
	private InstantDt myTimestamp;
	
	@Child(name="contains", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Codes in the value set",
		formalDefinition=""
	)
	private java.util.List<ExpansionContains> myContains;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myTimestamp,  myContains);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myTimestamp, myContains);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Uniquely identifies this expansion).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Uniquely identifies this expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so
     * </p> 
	 */
	public void setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>timestamp</b> (Time valueset expansion happened).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getTimestamp() {  
		if (myTimestamp == null) {
			myTimestamp = new InstantDt();
		}
		return myTimestamp;
	}

	/**
	 * Sets the value(s) for <b>timestamp</b> (Time valueset expansion happened)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setTimestamp(InstantDt theValue) {
		myTimestamp = theValue;
	}


 	/**
	 * Sets the value for <b>timestamp</b> (Time valueset expansion happened)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setTimestamp( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myTimestamp = new InstantDt(theDate, thePrecision); 
	}

	/**
	 * Sets the value for <b>timestamp</b> (Time valueset expansion happened)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setTimestampWithMillisPrecision( Date theDate) {
		myTimestamp = new InstantDt(theDate); 
	}

 
	/**
	 * Gets the value(s) for <b>contains</b> (Codes in the value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ExpansionContains> getContains() {  
		if (myContains == null) {
			myContains = new java.util.ArrayList<ExpansionContains>();
		}
		return myContains;
	}

	/**
	 * Sets the value(s) for <b>contains</b> (Codes in the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setContains(java.util.List<ExpansionContains> theValue) {
		myContains = theValue;
	}

	/**
	 * Adds and returns a new value for <b>contains</b> (Codes in the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExpansionContains addContains() {
		ExpansionContains newType = new ExpansionContains();
		getContains().add(newType);
		return newType; 
	}

  

	}

	/**
	 * Block class for child element: <b>ValueSet.expansion.contains</b> (Codes in the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block(name="ValueSet.expansion.contains")	
	public static class ExpansionContains extends BaseElement implements IResourceBlock {
	
	@Child(name="system", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="System value for the code",
		formalDefinition=""
	)
	private UriDt mySystem;
	
	@Child(name="code", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Code - if blank, this is not a choosable code",
		formalDefinition=""
	)
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="User display for the concept",
		formalDefinition=""
	)
	private StringDt myDisplay;
	
	@Child(name="contains", type=ExpansionContains.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Codes contained in this concept",
		formalDefinition=""
	)
	private java.util.List<ExpansionContains> myContains;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myCode,  myDisplay,  myContains);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myCode, myDisplay, myContains);
	}

	/**
	 * Gets the value(s) for <b>system</b> (System value for the code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getSystem() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (System value for the code)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setSystem(UriDt theValue) {
		mySystem = theValue;
	}


 	/**
	 * Sets the value for <b>system</b> (System value for the code)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Code - if blank, this is not a choosable code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getCode() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Code - if blank, this is not a choosable code)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setCode(CodeDt theValue) {
		myCode = theValue;
	}


 	/**
	 * Sets the value for <b>code</b> (Code - if blank, this is not a choosable code)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setCode( String theCode) {
		myCode = new CodeDt(theCode); 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (User display for the concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getDisplay() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (User display for the concept)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setDisplay(StringDt theValue) {
		myDisplay = theValue;
	}


 	/**
	 * Sets the value for <b>display</b> (User display for the concept)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>contains</b> (Codes contained in this concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ExpansionContains> getContains() {  
		if (myContains == null) {
			myContains = new java.util.ArrayList<ExpansionContains>();
		}
		return myContains;
	}

	/**
	 * Sets the value(s) for <b>contains</b> (Codes contained in this concept)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setContains(java.util.List<ExpansionContains> theValue) {
		myContains = theValue;
	}

	/**
	 * Adds and returns a new value for <b>contains</b> (Codes contained in this concept)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExpansionContains addContains() {
		ExpansionContains newType = new ExpansionContains();
		getContains().add(newType);
		return newType; 
	}

  

	}





}