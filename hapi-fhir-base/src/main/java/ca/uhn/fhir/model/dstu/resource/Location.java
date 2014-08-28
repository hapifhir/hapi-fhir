















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.rest.gclient.*;

import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.AdmitSourceEnum;
import ca.uhn.fhir.model.dstu.resource.AdverseReaction;
import ca.uhn.fhir.model.dstu.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.AlertStatusEnum;
import ca.uhn.fhir.model.dstu.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu.valueset.AnimalSpeciesEnum;
import ca.uhn.fhir.model.dstu.resource.Appointment;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.resource.Availability;
import ca.uhn.fhir.model.dstu.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dstu.resource.CarePlan;
import ca.uhn.fhir.model.dstu.valueset.CarePlanActivityCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanActivityStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanGoalStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CausalityExpectationEnum;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.valueset.CompositionAttestationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConceptMapEquivalenceEnum;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.valueset.ConditionRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConditionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceEventModeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceStatementStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.dstu.valueset.CriticalityEnum;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderPriorityEnum;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dstu.resource.DocumentManifest;
import ca.uhn.fhir.model.dstu.valueset.DocumentModeEnum;
import ca.uhn.fhir.model.dstu.resource.DocumentReference;
import ca.uhn.fhir.model.dstu.valueset.DocumentReferenceStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.DocumentRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExposureTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExtensionContextEnum;
import ca.uhn.fhir.model.dstu.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dstu.resource.FamilyHistory;
import ca.uhn.fhir.model.dstu.valueset.FilterOperatorEnum;
import ca.uhn.fhir.model.dstu.resource.GVFMeta;
import ca.uhn.fhir.model.dstu.resource.Group;
import ca.uhn.fhir.model.dstu.valueset.GroupTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.HierarchicalRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ImagingModalityEnum;
import ca.uhn.fhir.model.dstu.resource.ImagingStudy;
import ca.uhn.fhir.model.dstu.resource.Immunization;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationReasonCodesEnum;
import ca.uhn.fhir.model.dstu.resource.ImmunizationRecommendation;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRecommendationDateCriterionCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRecommendationStatusCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRouteCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.InstanceAvailabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.LinkTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ListModeEnum;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.valueset.LocationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu.resource.Media;
import ca.uhn.fhir.model.dstu.valueset.MediaTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu.valueset.MedicationAdministrationStatusEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu.valueset.MedicationDispenseStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.MedicationKindEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.dstu.valueset.MedicationPrescriptionStatusEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu.valueset.MessageEventEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageSignificanceCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageTransportEnum;
import ca.uhn.fhir.model.dstu.resource.Microarray;
import ca.uhn.fhir.model.dstu.valueset.ModalityEnum;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.valueset.ObservationInterpretationCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Order;
import ca.uhn.fhir.model.dstu.valueset.OrderOutcomeStatusEnum;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.OrganizationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ParticipantTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.PatientRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerSpecialtyEnum;
import ca.uhn.fhir.model.dstu.resource.Procedure;
import ca.uhn.fhir.model.dstu.valueset.ProcedureRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dstu.valueset.ProvenanceEntityRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.valueset.QueryOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireGroupNameEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireNameEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireStatusEnum;
import ca.uhn.fhir.model.dstu.composite.RangeDt;
import ca.uhn.fhir.model.dstu.composite.RatioDt;
import ca.uhn.fhir.model.dstu.valueset.ReactionSeverityEnum;
import ca.uhn.fhir.model.dstu.resource.RelatedPerson;
import ca.uhn.fhir.model.dstu.valueset.ResourceProfileStatusEnum;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ResponseTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dstu.composite.SampledDataDt;
import ca.uhn.fhir.model.dstu.composite.ScheduleDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventActionEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventParticipantNetworkTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventSourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SensitivityStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SensitivityTypeEnum;
import ca.uhn.fhir.model.dstu.resource.SequencingAnalysis;
import ca.uhn.fhir.model.dstu.resource.SequencingLab;
import ca.uhn.fhir.model.dstu.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.dstu.resource.Slot;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.valueset.SpecimenCollectionMethodEnum;
import ca.uhn.fhir.model.dstu.valueset.SpecimenTreatmentProcedureEnum;
import ca.uhn.fhir.model.dstu.resource.Substance;
import ca.uhn.fhir.model.dstu.valueset.SubstanceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyDispenseStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyItemTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyTypeEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.valueset.ValueSetStatusEnum;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.AgeDt;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IdrefDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.OidDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;


/**
 * HAPI/FHIR <b>Location</b> Resource
 * (Details and position information for a physical place)
 *
 * <p>
 * <b>Definition:</b>
 * Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Location">http://hl7.org/fhir/profiles/Location</a> 
 * </p>
 *
 */
@ResourceDef(name="Location", profile="http://hl7.org/fhir/profiles/Location", id="location")
public class Location extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Location.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A (portion of the) name of the location</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Location.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Location.name", description="A (portion of the) name of the location", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A (portion of the) name of the location</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Location.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>A code for the type of location</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Location.type", description="A code for the type of location", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>A code for the type of location</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>A (part of the) address of the location</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Location.address</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="address", path="Location.address", description="A (part of the) address of the location", type="string"  )
	public static final String SP_ADDRESS = "address";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>A (part of the) address of the location</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Location.address</b><br/>
	 * </p>
	 */
	public static final StringClientParam ADDRESS = new StringClientParam(SP_ADDRESS);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Searches for locations with a specific kind of status</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Location.status", description="Searches for locations with a specific kind of status", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Searches for locations with a specific kind of status</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>partof</b>
	 * <p>
	 * Description: <b>The location of which this location is a part</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Location.partOf</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="partof", path="Location.partOf", description="The location of which this location is a part", type="reference"  )
	public static final String SP_PARTOF = "partof";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>partof</b>
	 * <p>
	 * Description: <b>The location of which this location is a part</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Location.partOf</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PARTOF = new ReferenceClientParam(SP_PARTOF);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Location.partOf</b>".
	 */
	public static final Include INCLUDE_PARTOF = new Include("Location.partOf");

	/**
	 * Search parameter constant for <b>near</b>
	 * <p>
	 * Description: <b>The coordinates expressed as [lat],[long] (using KML, see notes) to find locations near to (servers may search using a square rather than a circle for efficiency)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="near", path="", description="The coordinates expressed as [lat],[long] (using KML, see notes) to find locations near to (servers may search using a square rather than a circle for efficiency)", type="token"  )
	public static final String SP_NEAR = "near";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>near</b>
	 * <p>
	 * Description: <b>The coordinates expressed as [lat],[long] (using KML, see notes) to find locations near to (servers may search using a square rather than a circle for efficiency)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final TokenClientParam NEAR = new TokenClientParam(SP_NEAR);

	/**
	 * Search parameter constant for <b>near-distance</b>
	 * <p>
	 * Description: <b>A distance quantity to limit the near search to locations within a specific distance</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="near-distance", path="", description="A distance quantity to limit the near search to locations within a specific distance", type="token"  )
	public static final String SP_NEAR_DISTANCE = "near-distance";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>near-distance</b>
	 * <p>
	 * Description: <b>A distance quantity to limit the near search to locations within a specific distance</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final TokenClientParam NEAR_DISTANCE = new TokenClientParam(SP_NEAR_DISTANCE);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Unique code or number identifying the location to its users",
		formalDefinition="Unique code or number identifying the location to its users"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Name of the location as used by humans",
		formalDefinition="Name of the location as used by humans. Does not need to be unique."
	)
	private StringDt myName;
	
	@Child(name="description", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Description of the Location, which helps in finding or referencing the place",
		formalDefinition="Description of the Location, which helps in finding or referencing the place"
	)
	private StringDt myDescription;
	
	@Child(name="type", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Indicates the type of function performed at the location",
		formalDefinition="Indicates the type of function performed at the location"
	)
	private BoundCodeableConceptDt<LocationTypeEnum> myType;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Contact details of the location",
		formalDefinition="The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites"
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Physical location",
		formalDefinition=""
	)
	private AddressDt myAddress;
	
	@Child(name="physicalType", type=CodeableConceptDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Physical form of the location",
		formalDefinition="Physical form of the location, e.g. building, room, vehicle, road"
	)
	private CodeableConceptDt myPhysicalType;
	
	@Child(name="position", order=7, min=0, max=1)	
	@Description(
		shortDefinition="The absolute geographic location",
		formalDefinition="The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)"
	)
	private Position myPosition;
	
	@Child(name="managingOrganization", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="The organization that is responsible for the provisioning and upkeep of the location",
		formalDefinition=""
	)
	private ResourceReferenceDt myManagingOrganization;
	
	@Child(name="status", type=CodeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="active | suspended | inactive",
		formalDefinition=""
	)
	private BoundCodeDt<LocationStatusEnum> myStatus;
	
	@Child(name="partOf", order=10, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Another Location which this Location is physically part of",
		formalDefinition=""
	)
	private ResourceReferenceDt myPartOf;
	
	@Child(name="mode", type=CodeDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="instance | kind",
		formalDefinition="Indicates whether a resource instance represents a specific location or a class of locations"
	)
	private BoundCodeDt<LocationModeEnum> myMode;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myDescription,  myType,  myTelecom,  myAddress,  myPhysicalType,  myPosition,  myManagingOrganization,  myStatus,  myPartOf,  myMode);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myDescription, myType, myTelecom, myAddress, myPhysicalType, myPosition, myManagingOrganization, myStatus, myPartOf, myMode);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Unique code or number identifying the location to its users).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique code or number identifying the location to its users
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Unique code or number identifying the location to its users)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique code or number identifying the location to its users
     * </p> 
	 */
	public Location setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Unique code or number identifying the location to its users)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique code or number identifying the location to its users
     * </p> 
	 */
	public Location setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Unique code or number identifying the location to its users)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique code or number identifying the location to its users
     * </p> 
	 */
	public Location setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Name of the location as used by humans).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the location as used by humans. Does not need to be unique.
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name of the location as used by humans)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the location as used by humans. Does not need to be unique.
     * </p> 
	 */
	public Location setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Name of the location as used by humans)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the location as used by humans. Does not need to be unique.
     * </p> 
	 */
	public Location setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (Description of the Location, which helps in finding or referencing the place).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the Location, which helps in finding or referencing the place
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Description of the Location, which helps in finding or referencing the place)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the Location, which helps in finding or referencing the place
     * </p> 
	 */
	public Location setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Description of the Location, which helps in finding or referencing the place)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the Location, which helps in finding or referencing the place
     * </p> 
	 */
	public Location setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (Indicates the type of function performed at the location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of function performed at the location
     * </p> 
	 */
	public BoundCodeableConceptDt<LocationTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<LocationTypeEnum>(LocationTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Indicates the type of function performed at the location)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of function performed at the location
     * </p> 
	 */
	public Location setType(BoundCodeableConceptDt<LocationTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Indicates the type of function performed at the location)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of function performed at the location
     * </p> 
	 */
	public Location setType(LocationTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>telecom</b> (Contact details of the location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
	 */
	public java.util.List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (Contact details of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
	 */
	public Location setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (Contact details of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (Contact details of the location),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
	 */
	public ContactDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
 	/**
	 * Adds a new value for <b>telecom</b> (Contact details of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Location addTelecom( ContactUseEnum theContactUse,  String theValue) {
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		myTelecom.add(new ContactDt(theContactUse, theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>telecom</b> (Contact details of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Location addTelecom( String theValue) {
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		myTelecom.add(new ContactDt(theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>address</b> (Physical location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AddressDt getAddress() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (Physical location)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setAddress(AddressDt theValue) {
		myAddress = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>physicalType</b> (Physical form of the location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Physical form of the location, e.g. building, room, vehicle, road
     * </p> 
	 */
	public CodeableConceptDt getPhysicalType() {  
		if (myPhysicalType == null) {
			myPhysicalType = new CodeableConceptDt();
		}
		return myPhysicalType;
	}

	/**
	 * Sets the value(s) for <b>physicalType</b> (Physical form of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * Physical form of the location, e.g. building, room, vehicle, road
     * </p> 
	 */
	public Location setPhysicalType(CodeableConceptDt theValue) {
		myPhysicalType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>position</b> (The absolute geographic location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	public Position getPosition() {  
		if (myPosition == null) {
			myPosition = new Position();
		}
		return myPosition;
	}

	/**
	 * Sets the value(s) for <b>position</b> (The absolute geographic location)
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	public Location setPosition(Position theValue) {
		myPosition = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>managingOrganization</b> (The organization that is responsible for the provisioning and upkeep of the location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getManagingOrganization() {  
		if (myManagingOrganization == null) {
			myManagingOrganization = new ResourceReferenceDt();
		}
		return myManagingOrganization;
	}

	/**
	 * Sets the value(s) for <b>managingOrganization</b> (The organization that is responsible for the provisioning and upkeep of the location)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setManagingOrganization(ResourceReferenceDt theValue) {
		myManagingOrganization = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>status</b> (active | suspended | inactive).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<LocationStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<LocationStatusEnum>(LocationStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (active | suspended | inactive)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setStatus(BoundCodeDt<LocationStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (active | suspended | inactive)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setStatus(LocationStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>partOf</b> (Another Location which this Location is physically part of).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getPartOf() {  
		if (myPartOf == null) {
			myPartOf = new ResourceReferenceDt();
		}
		return myPartOf;
	}

	/**
	 * Sets the value(s) for <b>partOf</b> (Another Location which this Location is physically part of)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setPartOf(ResourceReferenceDt theValue) {
		myPartOf = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>mode</b> (instance | kind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public BoundCodeDt<LocationModeEnum> getMode() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<LocationModeEnum>(LocationModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (instance | kind)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public Location setMode(BoundCodeDt<LocationModeEnum> theValue) {
		myMode = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (instance | kind)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public Location setMode(LocationModeEnum theValue) {
		getMode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Block class for child element: <b>Location.position</b> (The absolute geographic location)
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	@Block()	
	public static class Position extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="longitude", type=DecimalDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Longitude as expressed in KML",
		formalDefinition="Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)"
	)
	private DecimalDt myLongitude;
	
	@Child(name="latitude", type=DecimalDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Latitude as expressed in KML",
		formalDefinition="Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)"
	)
	private DecimalDt myLatitude;
	
	@Child(name="altitude", type=DecimalDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Altitude as expressed in KML",
		formalDefinition="Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)"
	)
	private DecimalDt myAltitude;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLongitude,  myLatitude,  myAltitude);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLongitude, myLatitude, myAltitude);
	}

	/**
	 * Gets the value(s) for <b>longitude</b> (Longitude as expressed in KML).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public DecimalDt getLongitude() {  
		if (myLongitude == null) {
			myLongitude = new DecimalDt();
		}
		return myLongitude;
	}

	/**
	 * Sets the value(s) for <b>longitude</b> (Longitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLongitude(DecimalDt theValue) {
		myLongitude = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>longitude</b> (Longitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLongitude( long theValue) {
		myLongitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>longitude</b> (Longitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLongitude( double theValue) {
		myLongitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>longitude</b> (Longitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLongitude( java.math.BigDecimal theValue) {
		myLongitude = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>latitude</b> (Latitude as expressed in KML).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public DecimalDt getLatitude() {  
		if (myLatitude == null) {
			myLatitude = new DecimalDt();
		}
		return myLatitude;
	}

	/**
	 * Sets the value(s) for <b>latitude</b> (Latitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLatitude(DecimalDt theValue) {
		myLatitude = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>latitude</b> (Latitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLatitude( long theValue) {
		myLatitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>latitude</b> (Latitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLatitude( double theValue) {
		myLatitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>latitude</b> (Latitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLatitude( java.math.BigDecimal theValue) {
		myLatitude = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>altitude</b> (Altitude as expressed in KML).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public DecimalDt getAltitude() {  
		if (myAltitude == null) {
			myAltitude = new DecimalDt();
		}
		return myAltitude;
	}

	/**
	 * Sets the value(s) for <b>altitude</b> (Altitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public Position setAltitude(DecimalDt theValue) {
		myAltitude = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>altitude</b> (Altitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public Position setAltitude( long theValue) {
		myAltitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>altitude</b> (Altitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public Position setAltitude( double theValue) {
		myAltitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>altitude</b> (Altitude as expressed in KML)
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public Position setAltitude( java.math.BigDecimal theValue) {
		myAltitude = new DecimalDt(theValue); 
		return this; 
	}

 

	}




}