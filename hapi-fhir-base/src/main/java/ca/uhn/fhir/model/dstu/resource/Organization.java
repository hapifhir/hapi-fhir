















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
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Organization">http://hl7.org/fhir/profiles/Organization</a> 
 * </p>
 *
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
	@SearchParamDefinition(name="name", path="Organization.name", description="A portion of the organization's name", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of the organization's name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Organization.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of the organization's name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="phonetic", path="", description="A portion of the organization's name using some kind of phonetic matching algorithm", type="string"  )
	public static final String SP_PHONETIC = "phonetic";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of the organization's name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final StringClientParam PHONETIC = new StringClientParam(SP_PHONETIC);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>A code for the type of organization</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Organization.type", description="A code for the type of organization", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>A code for the type of organization</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Any identifier for the organization (not the accreditation issuer's identifier)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Organization.identifier", description="Any identifier for the organization (not the accreditation issuer's identifier)", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Any identifier for the organization (not the accreditation issuer's identifier)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>partof</b>
	 * <p>
	 * Description: <b>Search all organizations that are part of the given organization</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Organization.partOf</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="partof", path="Organization.partOf", description="Search all organizations that are part of the given organization", type="reference"  )
	public static final String SP_PARTOF = "partof";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>partof</b>
	 * <p>
	 * Description: <b>Search all organizations that are part of the given organization</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Organization.partOf</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PARTOF = new ReferenceClientParam(SP_PARTOF);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Organization.partOf</b>".
	 */
	public static final Include INCLUDE_PARTOF = new Include("Organization.partOf");

	/**
	 * Search parameter constant for <b>active</b>
	 * <p>
	 * Description: <b>Whether the organization's record is active</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.active</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="active", path="Organization.active", description="Whether the organization's record is active", type="token"  )
	public static final String SP_ACTIVE = "active";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>active</b>
	 * <p>
	 * Description: <b>Whether the organization's record is active</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.active</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACTIVE = new TokenClientParam(SP_ACTIVE);


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
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
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
		ca.uhn.fhir.model.dstu.resource.Location.class	})
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
	 * Adds a new value for <b>identifier</b> (Identifies this organization  across multiple systems)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Organization addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Identifies this organization  across multiple systems)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Organization addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
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
	 * Adds a new value for <b>telecom</b> (A contact detail for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Organization addTelecom( ContactUseEnum theContactUse,  String theValue) {
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		myTelecom.add(new ContactDt(theContactUse, theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>telecom</b> (A contact detail for the organization)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Organization addTelecom( String theValue) {
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		myTelecom.add(new ContactDt(theValue));
		return this; 
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
	public Organization setActive( boolean theBoolean) {
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
	@Block()	
	public static class Contact extends BaseIdentifiableElement implements IResourceBlock {
	
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
	 * Adds a new value for <b>telecom</b> (Contact details (telephone, email, etc)  for a contact)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Contact addTelecom( ContactUseEnum theContactUse,  String theValue) {
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		myTelecom.add(new ContactDt(theContactUse, theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>telecom</b> (Contact details (telephone, email, etc)  for a contact)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Contact addTelecom( String theValue) {
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		myTelecom.add(new ContactDt(theValue));
		return this; 
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