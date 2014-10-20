















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
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;

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
 * HAPI/FHIR <b>CarePlan</b> Resource
 * (Healthcare plan for patient)
 *
 * <p>
 * <b>Definition:</b>
 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient for a period of time, possibly limited to care for a specific condition or set of conditions.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/CarePlan">http://hl7.org/fhir/profiles/CarePlan</a> 
 * </p>
 *
 */
@ResourceDef(name="CarePlan", profile="http://hl7.org/fhir/profiles/CarePlan", id="careplan")
public class CarePlan extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="CarePlan.patient", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("CarePlan.patient");

	/**
	 * Search parameter constant for <b>condition</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.concern</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="condition", path="CarePlan.concern", description="", type="reference"  )
	public static final String SP_CONDITION = "condition";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>condition</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.concern</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CONDITION = new ReferenceClientParam(SP_CONDITION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.concern</b>".
	 */
	public static final Include INCLUDE_CONCERN = new Include("CarePlan.concern");

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>CarePlan.period</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="CarePlan.period", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>CarePlan.period</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>participant</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.participant.member</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="participant", path="CarePlan.participant.member", description="", type="reference"  )
	public static final String SP_PARTICIPANT = "participant";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>participant</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.participant.member</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PARTICIPANT = new ReferenceClientParam(SP_PARTICIPANT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.participant.member</b>".
	 */
	public static final Include INCLUDE_PARTICIPANT_MEMBER = new Include("CarePlan.participant.member");

	/**
	 * Search parameter constant for <b>activitycode</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>CarePlan.activity.simple.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="activitycode", path="CarePlan.activity.simple.code", description="", type="token"  )
	public static final String SP_ACTIVITYCODE = "activitycode";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>activitycode</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>CarePlan.activity.simple.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACTIVITYCODE = new TokenClientParam(SP_ACTIVITYCODE);

	/**
	 * Search parameter constant for <b>activitydate</b>
	 * <p>
	 * Description: <b>Specified date occurs within period specified by CarePlan.activity.timingSchedule</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>CarePlan.activity.simple.timing[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="activitydate", path="CarePlan.activity.simple.timing[x]", description="Specified date occurs within period specified by CarePlan.activity.timingSchedule", type="date"  )
	public static final String SP_ACTIVITYDATE = "activitydate";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>activitydate</b>
	 * <p>
	 * Description: <b>Specified date occurs within period specified by CarePlan.activity.timingSchedule</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>CarePlan.activity.simple.timing[x]</b><br/>
	 * </p>
	 */
	public static final DateClientParam ACTIVITYDATE = new DateClientParam(SP_ACTIVITYDATE);

	/**
	 * Search parameter constant for <b>activitydetail</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.activity.detail</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="activitydetail", path="CarePlan.activity.detail", description="", type="reference"  )
	public static final String SP_ACTIVITYDETAIL = "activitydetail";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>activitydetail</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.activity.detail</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ACTIVITYDETAIL = new ReferenceClientParam(SP_ACTIVITYDETAIL);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.activity.detail</b>".
	 */
	public static final Include INCLUDE_ACTIVITY_DETAIL = new Include("CarePlan.activity.detail");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this plan",
		formalDefinition="This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="patient", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who care plan is for",
		formalDefinition="Identifies the patient/subject whose intended care is described by the plan."
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="status", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="planned | active | completed",
		formalDefinition="Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record."
	)
	private BoundCodeDt<CarePlanStatusEnum> myStatus;
	
	@Child(name="period", type=PeriodDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Time period plan covers",
		formalDefinition="Indicates when the plan did (or is intended to) come into effect and end."
	)
	private PeriodDt myPeriod;
	
	@Child(name="modified", type=DateTimeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="When last updated",
		formalDefinition="Identifies the most recent date on which the plan has been revised."
	)
	private DateTimeDt myModified;
	
	@Child(name="concern", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Condition.class	})
	@Description(
		shortDefinition="Health issues this plan addresses",
		formalDefinition="Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan."
	)
	private java.util.List<ResourceReferenceDt> myConcern;
	
	@Child(name="participant", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Who's involved in plan?",
		formalDefinition="Identifies all people and organizations who are expected to be involved in the care envisioned by this plan."
	)
	private java.util.List<Participant> myParticipant;
	
	@Child(name="goal", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Desired outcome of plan",
		formalDefinition="Describes the intended objective(s) of carrying out the Care Plan."
	)
	private java.util.List<Goal> myGoal;
	
	@Child(name="activity", order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Action to occur as part of plan",
		formalDefinition="Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc."
	)
	private java.util.List<Activity> myActivity;
	
	@Child(name="notes", type=StringDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Comments about the plan",
		formalDefinition="General notes about the care plan not covered elsewhere"
	)
	private StringDt myNotes;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myPatient,  myStatus,  myPeriod,  myModified,  myConcern,  myParticipant,  myGoal,  myActivity,  myNotes);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myPatient, myStatus, myPeriod, myModified, myConcern, myParticipant, myGoal, myActivity, myNotes);
	}
	

	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Ids for this plan)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public CarePlan setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this plan)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Ids for this plan),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this plan)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public CarePlan addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this plan)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public CarePlan addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>patient</b> (Who care plan is for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the patient/subject whose intended care is described by the plan.
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (Who care plan is for)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the patient/subject whose intended care is described by the plan.
     * </p> 
	 */
	public CarePlan setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>status</b> (planned | active | completed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     * </p> 
	 */
	public BoundCodeDt<CarePlanStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<CarePlanStatusEnum>(CarePlanStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (planned | active | completed)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     * </p> 
	 */
	public CarePlan setStatus(BoundCodeDt<CarePlanStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (planned | active | completed)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     * </p> 
	 */
	public CarePlan setStatus(CarePlanStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> (Time period plan covers).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates when the plan did (or is intended to) come into effect and end.
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (Time period plan covers)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates when the plan did (or is intended to) come into effect and end.
     * </p> 
	 */
	public CarePlan setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>modified</b> (When last updated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the most recent date on which the plan has been revised.
     * </p> 
	 */
	public DateTimeDt getModified() {  
		if (myModified == null) {
			myModified = new DateTimeDt();
		}
		return myModified;
	}

	/**
	 * Sets the value(s) for <b>modified</b> (When last updated)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the most recent date on which the plan has been revised.
     * </p> 
	 */
	public CarePlan setModified(DateTimeDt theValue) {
		myModified = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>modified</b> (When last updated)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the most recent date on which the plan has been revised.
     * </p> 
	 */
	public CarePlan setModifiedWithSecondsPrecision( Date theDate) {
		myModified = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>modified</b> (When last updated)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the most recent date on which the plan has been revised.
     * </p> 
	 */
	public CarePlan setModified( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myModified = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>concern</b> (Health issues this plan addresses).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getConcern() {  
		if (myConcern == null) {
			myConcern = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myConcern;
	}

	/**
	 * Sets the value(s) for <b>concern</b> (Health issues this plan addresses)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     * </p> 
	 */
	public CarePlan setConcern(java.util.List<ResourceReferenceDt> theValue) {
		myConcern = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>concern</b> (Health issues this plan addresses)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     * </p> 
	 */
	public ResourceReferenceDt addConcern() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getConcern().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>participant</b> (Who's involved in plan?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     * </p> 
	 */
	public java.util.List<Participant> getParticipant() {  
		if (myParticipant == null) {
			myParticipant = new java.util.ArrayList<Participant>();
		}
		return myParticipant;
	}

	/**
	 * Sets the value(s) for <b>participant</b> (Who's involved in plan?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     * </p> 
	 */
	public CarePlan setParticipant(java.util.List<Participant> theValue) {
		myParticipant = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>participant</b> (Who's involved in plan?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     * </p> 
	 */
	public Participant addParticipant() {
		Participant newType = new Participant();
		getParticipant().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>participant</b> (Who's involved in plan?),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     * </p> 
	 */
	public Participant getParticipantFirstRep() {
		if (getParticipant().isEmpty()) {
			return addParticipant();
		}
		return getParticipant().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>goal</b> (Desired outcome of plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended objective(s) of carrying out the Care Plan.
     * </p> 
	 */
	public java.util.List<Goal> getGoal() {  
		if (myGoal == null) {
			myGoal = new java.util.ArrayList<Goal>();
		}
		return myGoal;
	}

	/**
	 * Sets the value(s) for <b>goal</b> (Desired outcome of plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended objective(s) of carrying out the Care Plan.
     * </p> 
	 */
	public CarePlan setGoal(java.util.List<Goal> theValue) {
		myGoal = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>goal</b> (Desired outcome of plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended objective(s) of carrying out the Care Plan.
     * </p> 
	 */
	public Goal addGoal() {
		Goal newType = new Goal();
		getGoal().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>goal</b> (Desired outcome of plan),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended objective(s) of carrying out the Care Plan.
     * </p> 
	 */
	public Goal getGoalFirstRep() {
		if (getGoal().isEmpty()) {
			return addGoal();
		}
		return getGoal().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>activity</b> (Action to occur as part of plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     * </p> 
	 */
	public java.util.List<Activity> getActivity() {  
		if (myActivity == null) {
			myActivity = new java.util.ArrayList<Activity>();
		}
		return myActivity;
	}

	/**
	 * Sets the value(s) for <b>activity</b> (Action to occur as part of plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     * </p> 
	 */
	public CarePlan setActivity(java.util.List<Activity> theValue) {
		myActivity = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>activity</b> (Action to occur as part of plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     * </p> 
	 */
	public Activity addActivity() {
		Activity newType = new Activity();
		getActivity().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>activity</b> (Action to occur as part of plan),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     * </p> 
	 */
	public Activity getActivityFirstRep() {
		if (getActivity().isEmpty()) {
			return addActivity();
		}
		return getActivity().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>notes</b> (Comments about the plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * General notes about the care plan not covered elsewhere
     * </p> 
	 */
	public StringDt getNotes() {  
		if (myNotes == null) {
			myNotes = new StringDt();
		}
		return myNotes;
	}

	/**
	 * Sets the value(s) for <b>notes</b> (Comments about the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * General notes about the care plan not covered elsewhere
     * </p> 
	 */
	public CarePlan setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>notes</b> (Comments about the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * General notes about the care plan not covered elsewhere
     * </p> 
	 */
	public CarePlan setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>CarePlan.participant</b> (Who's involved in plan?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     * </p> 
	 */
	@Block()	
	public static class Participant extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="role", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Type of involvement",
		formalDefinition="Indicates specific responsibility of an individual within the care plan.  E.g. \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc."
	)
	private CodeableConceptDt myRole;
	
	@Child(name="member", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Who is involved",
		formalDefinition="The specific person or organization who is participating/expected to participate in the care plan."
	)
	private ResourceReferenceDt myMember;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRole,  myMember);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRole, myMember);
	}
	

	/**
	 * Gets the value(s) for <b>role</b> (Type of involvement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates specific responsibility of an individual within the care plan.  E.g. \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc.
     * </p> 
	 */
	public CodeableConceptDt getRole() {  
		if (myRole == null) {
			myRole = new CodeableConceptDt();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (Type of involvement)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates specific responsibility of an individual within the care plan.  E.g. \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc.
     * </p> 
	 */
	public Participant setRole(CodeableConceptDt theValue) {
		myRole = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>member</b> (Who is involved).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The specific person or organization who is participating/expected to participate in the care plan.
     * </p> 
	 */
	public ResourceReferenceDt getMember() {  
		if (myMember == null) {
			myMember = new ResourceReferenceDt();
		}
		return myMember;
	}

	/**
	 * Sets the value(s) for <b>member</b> (Who is involved)
	 *
     * <p>
     * <b>Definition:</b>
     * The specific person or organization who is participating/expected to participate in the care plan.
     * </p> 
	 */
	public Participant setMember(ResourceReferenceDt theValue) {
		myMember = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>CarePlan.goal</b> (Desired outcome of plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended objective(s) of carrying out the Care Plan.
     * </p> 
	 */
	@Block()	
	public static class Goal extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="description", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="What's the desired outcome?",
		formalDefinition="Human-readable description of a specific desired objective of the care plan."
	)
	private StringDt myDescription;
	
	@Child(name="status", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="in progress | achieved | sustaining | cancelled",
		formalDefinition="Indicates whether the goal has been reached and is still considered relevant"
	)
	private BoundCodeDt<CarePlanGoalStatusEnum> myStatus;
	
	@Child(name="notes", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Comments about the goal",
		formalDefinition="Any comments related to the goal"
	)
	private StringDt myNotes;
	
	@Child(name="concern", order=3, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Condition.class	})
	@Description(
		shortDefinition="Health issues this goal addresses",
		formalDefinition="The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address"
	)
	private java.util.List<ResourceReferenceDt> myConcern;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDescription,  myStatus,  myNotes,  myConcern);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDescription, myStatus, myNotes, myConcern);
	}
	

	/**
	 * Gets the value(s) for <b>description</b> (What's the desired outcome?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of a specific desired objective of the care plan.
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (What's the desired outcome?)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of a specific desired objective of the care plan.
     * </p> 
	 */
	public Goal setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (What's the desired outcome?)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of a specific desired objective of the care plan.
     * </p> 
	 */
	public Goal setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (in progress | achieved | sustaining | cancelled).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the goal has been reached and is still considered relevant
     * </p> 
	 */
	public BoundCodeDt<CarePlanGoalStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<CarePlanGoalStatusEnum>(CarePlanGoalStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | achieved | sustaining | cancelled)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the goal has been reached and is still considered relevant
     * </p> 
	 */
	public Goal setStatus(BoundCodeDt<CarePlanGoalStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | achieved | sustaining | cancelled)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the goal has been reached and is still considered relevant
     * </p> 
	 */
	public Goal setStatus(CarePlanGoalStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>notes</b> (Comments about the goal).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Any comments related to the goal
     * </p> 
	 */
	public StringDt getNotes() {  
		if (myNotes == null) {
			myNotes = new StringDt();
		}
		return myNotes;
	}

	/**
	 * Sets the value(s) for <b>notes</b> (Comments about the goal)
	 *
     * <p>
     * <b>Definition:</b>
     * Any comments related to the goal
     * </p> 
	 */
	public Goal setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>notes</b> (Comments about the goal)
	 *
     * <p>
     * <b>Definition:</b>
     * Any comments related to the goal
     * </p> 
	 */
	public Goal setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>concern</b> (Health issues this goal addresses).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getConcern() {  
		if (myConcern == null) {
			myConcern = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myConcern;
	}

	/**
	 * Sets the value(s) for <b>concern</b> (Health issues this goal addresses)
	 *
     * <p>
     * <b>Definition:</b>
     * The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address
     * </p> 
	 */
	public Goal setConcern(java.util.List<ResourceReferenceDt> theValue) {
		myConcern = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>concern</b> (Health issues this goal addresses)
	 *
     * <p>
     * <b>Definition:</b>
     * The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address
     * </p> 
	 */
	public ResourceReferenceDt addConcern() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getConcern().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>CarePlan.activity</b> (Action to occur as part of plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     * </p> 
	 */
	@Block()	
	public static class Activity extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="goal", type=IdrefDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Goals this activity relates to",
		formalDefinition="Internal reference that identifies the goals that this activity is intended to contribute towards meeting"
	)
	private java.util.List<IdrefDt> myGoal;
	
	@Child(name="status", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="not started | scheduled | in progress | on hold | completed | cancelled",
		formalDefinition="Identifies what progress is being made for the specific activity."
	)
	private BoundCodeDt<CarePlanActivityStatusEnum> myStatus;
	
	@Child(name="prohibited", type=BooleanDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Do NOT do",
		formalDefinition="If true, indicates that the described activity is one that must NOT be engaged in when following the plan."
	)
	private BooleanDt myProhibited;
	
	@Child(name="actionResulting", order=3, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="Appointments, orders, etc.",
		formalDefinition="Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc."
	)
	private java.util.List<ResourceReferenceDt> myActionResulting;
	
	@Child(name="notes", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Comments about the activity",
		formalDefinition="Notes about the execution of the activity"
	)
	private StringDt myNotes;
	
	@Child(name="detail", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Procedure.class, 		ca.uhn.fhir.model.dstu.resource.MedicationPrescription.class, 		ca.uhn.fhir.model.dstu.resource.DiagnosticOrder.class, 		ca.uhn.fhir.model.dstu.resource.Encounter.class	})
	@Description(
		shortDefinition="Activity details defined in specific resource",
		formalDefinition="The details of the proposed activity represented in a specific resource"
	)
	private ResourceReferenceDt myDetail;
	
	@Child(name="simple", order=6, min=0, max=1)	
	@Description(
		shortDefinition="Activity details summarised here",
		formalDefinition="A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc"
	)
	private ActivitySimple mySimple;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myGoal,  myStatus,  myProhibited,  myActionResulting,  myNotes,  myDetail,  mySimple);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myGoal, myStatus, myProhibited, myActionResulting, myNotes, myDetail, mySimple);
	}
	

	/**
	 * Gets the value(s) for <b>goal</b> (Goals this activity relates to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Internal reference that identifies the goals that this activity is intended to contribute towards meeting
     * </p> 
	 */
	public java.util.List<IdrefDt> getGoal() {  
		if (myGoal == null) {
			myGoal = new java.util.ArrayList<IdrefDt>();
		}
		return myGoal;
	}

	/**
	 * Sets the value(s) for <b>goal</b> (Goals this activity relates to)
	 *
     * <p>
     * <b>Definition:</b>
     * Internal reference that identifies the goals that this activity is intended to contribute towards meeting
     * </p> 
	 */
	public Activity setGoal(java.util.List<IdrefDt> theValue) {
		myGoal = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>goal</b> (Goals this activity relates to)
	 *
     * <p>
     * <b>Definition:</b>
     * Internal reference that identifies the goals that this activity is intended to contribute towards meeting
     * </p> 
	 */
	public IdrefDt addGoal() {
		IdrefDt newType = new IdrefDt();
		getGoal().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>goal</b> (Goals this activity relates to),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Internal reference that identifies the goals that this activity is intended to contribute towards meeting
     * </p> 
	 */
	public IdrefDt getGoalFirstRep() {
		if (getGoal().isEmpty()) {
			return addGoal();
		}
		return getGoal().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>status</b> (not started | scheduled | in progress | on hold | completed | cancelled).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies what progress is being made for the specific activity.
     * </p> 
	 */
	public BoundCodeDt<CarePlanActivityStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<CarePlanActivityStatusEnum>(CarePlanActivityStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (not started | scheduled | in progress | on hold | completed | cancelled)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies what progress is being made for the specific activity.
     * </p> 
	 */
	public Activity setStatus(BoundCodeDt<CarePlanActivityStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (not started | scheduled | in progress | on hold | completed | cancelled)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies what progress is being made for the specific activity.
     * </p> 
	 */
	public Activity setStatus(CarePlanActivityStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>prohibited</b> (Do NOT do).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
     * </p> 
	 */
	public BooleanDt getProhibited() {  
		if (myProhibited == null) {
			myProhibited = new BooleanDt();
		}
		return myProhibited;
	}

	/**
	 * Sets the value(s) for <b>prohibited</b> (Do NOT do)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
     * </p> 
	 */
	public Activity setProhibited(BooleanDt theValue) {
		myProhibited = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>prohibited</b> (Do NOT do)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
     * </p> 
	 */
	public Activity setProhibited( boolean theBoolean) {
		myProhibited = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>actionResulting</b> (Appointments, orders, etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getActionResulting() {  
		if (myActionResulting == null) {
			myActionResulting = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myActionResulting;
	}

	/**
	 * Sets the value(s) for <b>actionResulting</b> (Appointments, orders, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.
     * </p> 
	 */
	public Activity setActionResulting(java.util.List<ResourceReferenceDt> theValue) {
		myActionResulting = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>actionResulting</b> (Appointments, orders, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.
     * </p> 
	 */
	public ResourceReferenceDt addActionResulting() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getActionResulting().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>notes</b> (Comments about the activity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Notes about the execution of the activity
     * </p> 
	 */
	public StringDt getNotes() {  
		if (myNotes == null) {
			myNotes = new StringDt();
		}
		return myNotes;
	}

	/**
	 * Sets the value(s) for <b>notes</b> (Comments about the activity)
	 *
     * <p>
     * <b>Definition:</b>
     * Notes about the execution of the activity
     * </p> 
	 */
	public Activity setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>notes</b> (Comments about the activity)
	 *
     * <p>
     * <b>Definition:</b>
     * Notes about the execution of the activity
     * </p> 
	 */
	public Activity setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>detail</b> (Activity details defined in specific resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The details of the proposed activity represented in a specific resource
     * </p> 
	 */
	public ResourceReferenceDt getDetail() {  
		if (myDetail == null) {
			myDetail = new ResourceReferenceDt();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> (Activity details defined in specific resource)
	 *
     * <p>
     * <b>Definition:</b>
     * The details of the proposed activity represented in a specific resource
     * </p> 
	 */
	public Activity setDetail(ResourceReferenceDt theValue) {
		myDetail = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>simple</b> (Activity details summarised here).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc
     * </p> 
	 */
	public ActivitySimple getSimple() {  
		if (mySimple == null) {
			mySimple = new ActivitySimple();
		}
		return mySimple;
	}

	/**
	 * Sets the value(s) for <b>simple</b> (Activity details summarised here)
	 *
     * <p>
     * <b>Definition:</b>
     * A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc
     * </p> 
	 */
	public Activity setSimple(ActivitySimple theValue) {
		mySimple = theValue;
		return this;
	}

  

	}

	/**
	 * Block class for child element: <b>CarePlan.activity.simple</b> (Activity details summarised here)
	 *
     * <p>
     * <b>Definition:</b>
     * A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc
     * </p> 
	 */
	@Block()	
	public static class ActivitySimple extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="category", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="diet | drug | encounter | observation | procedure | supply | other",
		formalDefinition="High-level categorization of the type of activity in a care plan."
	)
	private BoundCodeDt<CarePlanActivityCategoryEnum> myCategory;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Detail type of activity",
		formalDefinition="Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter."
	)
	private CodeableConceptDt myCode;
	
	@Child(name="timing", order=2, min=0, max=1, type={
		ScheduleDt.class, 		PeriodDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="When activity is to occur",
		formalDefinition="The period, timing or frequency upon which the described activity is to occur."
	)
	private IDatatype myTiming;
	
	@Child(name="location", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Where it should happen",
		formalDefinition="Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc."
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="performer", order=4, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who's responsible?",
		formalDefinition="Identifies who's expected to be involved in the activity."
	)
	private java.util.List<ResourceReferenceDt> myPerformer;
	
	@Child(name="product", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Medication.class, 		ca.uhn.fhir.model.dstu.resource.Substance.class	})
	@Description(
		shortDefinition="What's administered/supplied",
		formalDefinition="Identifies the food, drug or other product being consumed or supplied in the activity."
	)
	private ResourceReferenceDt myProduct;
	
	@Child(name="dailyAmount", type=QuantityDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="How much consumed/day?",
		formalDefinition="Identifies the quantity expected to be consumed in a given day."
	)
	private QuantityDt myDailyAmount;
	
	@Child(name="quantity", type=QuantityDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="How much is administered/supplied/consumed",
		formalDefinition="Identifies the quantity expected to be supplied."
	)
	private QuantityDt myQuantity;
	
	@Child(name="details", type=StringDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Extra info on activity occurrence",
		formalDefinition="This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc."
	)
	private StringDt myDetails;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCategory,  myCode,  myTiming,  myLocation,  myPerformer,  myProduct,  myDailyAmount,  myQuantity,  myDetails);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCategory, myCode, myTiming, myLocation, myPerformer, myProduct, myDailyAmount, myQuantity, myDetails);
	}
	

	/**
	 * Gets the value(s) for <b>category</b> (diet | drug | encounter | observation | procedure | supply | other).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * High-level categorization of the type of activity in a care plan.
     * </p> 
	 */
	public BoundCodeDt<CarePlanActivityCategoryEnum> getCategory() {  
		if (myCategory == null) {
			myCategory = new BoundCodeDt<CarePlanActivityCategoryEnum>(CarePlanActivityCategoryEnum.VALUESET_BINDER);
		}
		return myCategory;
	}

	/**
	 * Sets the value(s) for <b>category</b> (diet | drug | encounter | observation | procedure | supply | other)
	 *
     * <p>
     * <b>Definition:</b>
     * High-level categorization of the type of activity in a care plan.
     * </p> 
	 */
	public ActivitySimple setCategory(BoundCodeDt<CarePlanActivityCategoryEnum> theValue) {
		myCategory = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>category</b> (diet | drug | encounter | observation | procedure | supply | other)
	 *
     * <p>
     * <b>Definition:</b>
     * High-level categorization of the type of activity in a care plan.
     * </p> 
	 */
	public ActivitySimple setCategory(CarePlanActivityCategoryEnum theValue) {
		getCategory().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>code</b> (Detail type of activity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter.
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Detail type of activity)
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter.
     * </p> 
	 */
	public ActivitySimple setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>timing[x]</b> (When activity is to occur).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period, timing or frequency upon which the described activity is to occur.
     * </p> 
	 */
	public IDatatype getTiming() {  
		return myTiming;
	}

	/**
	 * Sets the value(s) for <b>timing[x]</b> (When activity is to occur)
	 *
     * <p>
     * <b>Definition:</b>
     * The period, timing or frequency upon which the described activity is to occur.
     * </p> 
	 */
	public ActivitySimple setTiming(IDatatype theValue) {
		myTiming = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>location</b> (Where it should happen).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.
     * </p> 
	 */
	public ResourceReferenceDt getLocation() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (Where it should happen)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.
     * </p> 
	 */
	public ActivitySimple setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>performer</b> (Who's responsible?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who's expected to be involved in the activity.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getPerformer() {  
		if (myPerformer == null) {
			myPerformer = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myPerformer;
	}

	/**
	 * Sets the value(s) for <b>performer</b> (Who's responsible?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who's expected to be involved in the activity.
     * </p> 
	 */
	public ActivitySimple setPerformer(java.util.List<ResourceReferenceDt> theValue) {
		myPerformer = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>performer</b> (Who's responsible?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who's expected to be involved in the activity.
     * </p> 
	 */
	public ResourceReferenceDt addPerformer() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getPerformer().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>product</b> (What's administered/supplied).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the food, drug or other product being consumed or supplied in the activity.
     * </p> 
	 */
	public ResourceReferenceDt getProduct() {  
		if (myProduct == null) {
			myProduct = new ResourceReferenceDt();
		}
		return myProduct;
	}

	/**
	 * Sets the value(s) for <b>product</b> (What's administered/supplied)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the food, drug or other product being consumed or supplied in the activity.
     * </p> 
	 */
	public ActivitySimple setProduct(ResourceReferenceDt theValue) {
		myProduct = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dailyAmount</b> (How much consumed/day?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be consumed in a given day.
     * </p> 
	 */
	public QuantityDt getDailyAmount() {  
		if (myDailyAmount == null) {
			myDailyAmount = new QuantityDt();
		}
		return myDailyAmount;
	}

	/**
	 * Sets the value(s) for <b>dailyAmount</b> (How much consumed/day?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be consumed in a given day.
     * </p> 
	 */
	public ActivitySimple setDailyAmount(QuantityDt theValue) {
		myDailyAmount = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>dailyAmount</b> (How much consumed/day?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be consumed in a given day.
     * </p> 
	 */
	public ActivitySimple setDailyAmount( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myDailyAmount = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>dailyAmount</b> (How much consumed/day?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be consumed in a given day.
     * </p> 
	 */
	public ActivitySimple setDailyAmount( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myDailyAmount = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>dailyAmount</b> (How much consumed/day?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be consumed in a given day.
     * </p> 
	 */
	public ActivitySimple setDailyAmount( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myDailyAmount = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>dailyAmount</b> (How much consumed/day?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be consumed in a given day.
     * </p> 
	 */
	public ActivitySimple setDailyAmount( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myDailyAmount = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>dailyAmount</b> (How much consumed/day?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be consumed in a given day.
     * </p> 
	 */
	public ActivitySimple setDailyAmount( long theValue) {
		myDailyAmount = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>dailyAmount</b> (How much consumed/day?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be consumed in a given day.
     * </p> 
	 */
	public ActivitySimple setDailyAmount( double theValue) {
		myDailyAmount = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>quantity</b> (How much is administered/supplied/consumed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be supplied.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (How much is administered/supplied/consumed)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be supplied.
     * </p> 
	 */
	public ActivitySimple setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>quantity</b> (How much is administered/supplied/consumed)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be supplied.
     * </p> 
	 */
	public ActivitySimple setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (How much is administered/supplied/consumed)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be supplied.
     * </p> 
	 */
	public ActivitySimple setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (How much is administered/supplied/consumed)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be supplied.
     * </p> 
	 */
	public ActivitySimple setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (How much is administered/supplied/consumed)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be supplied.
     * </p> 
	 */
	public ActivitySimple setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (How much is administered/supplied/consumed)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be supplied.
     * </p> 
	 */
	public ActivitySimple setQuantity( long theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (How much is administered/supplied/consumed)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be supplied.
     * </p> 
	 */
	public ActivitySimple setQuantity( double theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>details</b> (Extra info on activity occurrence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
     * </p> 
	 */
	public StringDt getDetails() {  
		if (myDetails == null) {
			myDetails = new StringDt();
		}
		return myDetails;
	}

	/**
	 * Sets the value(s) for <b>details</b> (Extra info on activity occurrence)
	 *
     * <p>
     * <b>Definition:</b>
     * This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
     * </p> 
	 */
	public ActivitySimple setDetails(StringDt theValue) {
		myDetails = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>details</b> (Extra info on activity occurrence)
	 *
     * <p>
     * <b>Definition:</b>
     * This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
     * </p> 
	 */
	public ActivitySimple setDetails( String theString) {
		myDetails = new StringDt(theString); 
		return this; 
	}

 

	}





    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.CAREPLAN;
    }

}