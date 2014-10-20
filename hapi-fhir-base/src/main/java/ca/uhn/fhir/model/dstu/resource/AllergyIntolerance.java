















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
 * HAPI/FHIR <b>AllergyIntolerance</b> Resource
 * (Drug, food, environmental and others)
 *
 * <p>
 * <b>Definition:</b>
 * Indicates the patient has a susceptibility to an adverse reaction upon exposure to a specified substance
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/AllergyIntolerance">http://hl7.org/fhir/profiles/AllergyIntolerance</a> 
 * </p>
 *
 */
@ResourceDef(name="AllergyIntolerance", profile="http://hl7.org/fhir/profiles/AllergyIntolerance", id="allergyintolerance")
public class AllergyIntolerance extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of sensitivity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AllergyIntolerance.sensitivityType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="AllergyIntolerance.sensitivityType", description="The type of sensitivity", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of sensitivity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AllergyIntolerance.sensitivityType</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b>The name or code of the substance that produces the sensitivity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AllergyIntolerance.substance</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="substance", path="AllergyIntolerance.substance", description="The name or code of the substance that produces the sensitivity", type="reference"  )
	public static final String SP_SUBSTANCE = "substance";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b>The name or code of the substance that produces the sensitivity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AllergyIntolerance.substance</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBSTANCE = new ReferenceClientParam(SP_SUBSTANCE);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AllergyIntolerance.substance</b>".
	 */
	public static final Include INCLUDE_SUBSTANCE = new Include("AllergyIntolerance.substance");

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Recorded date/time.</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AllergyIntolerance.recordedDate</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="AllergyIntolerance.recordedDate", description="Recorded date/time.", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Recorded date/time.</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AllergyIntolerance.recordedDate</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the sensitivity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AllergyIntolerance.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="AllergyIntolerance.status", description="The status of the sensitivity", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the sensitivity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AllergyIntolerance.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the sensitivity is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AllergyIntolerance.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="AllergyIntolerance.subject", description="The subject that the sensitivity is about", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the sensitivity is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AllergyIntolerance.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AllergyIntolerance.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("AllergyIntolerance.subject");

	/**
	 * Search parameter constant for <b>recorder</b>
	 * <p>
	 * Description: <b>Who recorded the sensitivity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AllergyIntolerance.recorder</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="recorder", path="AllergyIntolerance.recorder", description="Who recorded the sensitivity", type="reference"  )
	public static final String SP_RECORDER = "recorder";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>recorder</b>
	 * <p>
	 * Description: <b>Who recorded the sensitivity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AllergyIntolerance.recorder</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RECORDER = new ReferenceClientParam(SP_RECORDER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AllergyIntolerance.recorder</b>".
	 */
	public static final Include INCLUDE_RECORDER = new Include("AllergyIntolerance.recorder");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this item",
		formalDefinition="This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="criticality", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="fatal | high | medium | low",
		formalDefinition="Criticality of the sensitivity"
	)
	private BoundCodeDt<CriticalityEnum> myCriticality;
	
	@Child(name="sensitivityType", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="allergy | intolerance | unknown",
		formalDefinition="Type of the sensitivity"
	)
	private BoundCodeDt<SensitivityTypeEnum> mySensitivityType;
	
	@Child(name="recordedDate", type=DateTimeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="When recorded",
		formalDefinition="Date when the sensitivity was recorded"
	)
	private DateTimeDt myRecordedDate;
	
	@Child(name="status", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="suspected | confirmed | refuted | resolved",
		formalDefinition="Status of the sensitivity"
	)
	private BoundCodeDt<SensitivityStatusEnum> myStatus;
	
	@Child(name="subject", order=5, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who the sensitivity is for",
		formalDefinition="The patient who has the allergy or intolerance"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="recorder", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who recorded the sensitivity",
		formalDefinition="Indicates who has responsibility for the record"
	)
	private ResourceReferenceDt myRecorder;
	
	@Child(name="substance", order=7, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Substance.class	})
	@Description(
		shortDefinition="The substance that causes the sensitivity",
		formalDefinition="The substance that causes the sensitivity"
	)
	private ResourceReferenceDt mySubstance;
	
	@Child(name="reaction", order=8, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.AdverseReaction.class	})
	@Description(
		shortDefinition="Reactions associated with the sensitivity",
		formalDefinition="Reactions associated with the sensitivity"
	)
	private java.util.List<ResourceReferenceDt> myReaction;
	
	@Child(name="sensitivityTest", order=9, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Observation.class	})
	@Description(
		shortDefinition="Observations that confirm or refute",
		formalDefinition="Observations that confirm or refute the sensitivity"
	)
	private java.util.List<ResourceReferenceDt> mySensitivityTest;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCriticality,  mySensitivityType,  myRecordedDate,  myStatus,  mySubject,  myRecorder,  mySubstance,  myReaction,  mySensitivityTest);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCriticality, mySensitivityType, myRecordedDate, myStatus, mySubject, myRecorder, mySubstance, myReaction, mySensitivityTest);
	}
	

	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public AllergyIntolerance setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Ids for this item),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public AllergyIntolerance addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public AllergyIntolerance addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>criticality</b> (fatal | high | medium | low).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Criticality of the sensitivity
     * </p> 
	 */
	public BoundCodeDt<CriticalityEnum> getCriticality() {  
		if (myCriticality == null) {
			myCriticality = new BoundCodeDt<CriticalityEnum>(CriticalityEnum.VALUESET_BINDER);
		}
		return myCriticality;
	}

	/**
	 * Sets the value(s) for <b>criticality</b> (fatal | high | medium | low)
	 *
     * <p>
     * <b>Definition:</b>
     * Criticality of the sensitivity
     * </p> 
	 */
	public AllergyIntolerance setCriticality(BoundCodeDt<CriticalityEnum> theValue) {
		myCriticality = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>criticality</b> (fatal | high | medium | low)
	 *
     * <p>
     * <b>Definition:</b>
     * Criticality of the sensitivity
     * </p> 
	 */
	public AllergyIntolerance setCriticality(CriticalityEnum theValue) {
		getCriticality().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sensitivityType</b> (allergy | intolerance | unknown).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the sensitivity
     * </p> 
	 */
	public BoundCodeDt<SensitivityTypeEnum> getSensitivityType() {  
		if (mySensitivityType == null) {
			mySensitivityType = new BoundCodeDt<SensitivityTypeEnum>(SensitivityTypeEnum.VALUESET_BINDER);
		}
		return mySensitivityType;
	}

	/**
	 * Sets the value(s) for <b>sensitivityType</b> (allergy | intolerance | unknown)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the sensitivity
     * </p> 
	 */
	public AllergyIntolerance setSensitivityType(BoundCodeDt<SensitivityTypeEnum> theValue) {
		mySensitivityType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>sensitivityType</b> (allergy | intolerance | unknown)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the sensitivity
     * </p> 
	 */
	public AllergyIntolerance setSensitivityType(SensitivityTypeEnum theValue) {
		getSensitivityType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>recordedDate</b> (When recorded).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the sensitivity was recorded
     * </p> 
	 */
	public DateTimeDt getRecordedDate() {  
		if (myRecordedDate == null) {
			myRecordedDate = new DateTimeDt();
		}
		return myRecordedDate;
	}

	/**
	 * Sets the value(s) for <b>recordedDate</b> (When recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the sensitivity was recorded
     * </p> 
	 */
	public AllergyIntolerance setRecordedDate(DateTimeDt theValue) {
		myRecordedDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>recordedDate</b> (When recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the sensitivity was recorded
     * </p> 
	 */
	public AllergyIntolerance setRecordedDateWithSecondsPrecision( Date theDate) {
		myRecordedDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>recordedDate</b> (When recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the sensitivity was recorded
     * </p> 
	 */
	public AllergyIntolerance setRecordedDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myRecordedDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (suspected | confirmed | refuted | resolved).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the sensitivity
     * </p> 
	 */
	public BoundCodeDt<SensitivityStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<SensitivityStatusEnum>(SensitivityStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (suspected | confirmed | refuted | resolved)
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the sensitivity
     * </p> 
	 */
	public AllergyIntolerance setStatus(BoundCodeDt<SensitivityStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (suspected | confirmed | refuted | resolved)
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the sensitivity
     * </p> 
	 */
	public AllergyIntolerance setStatus(SensitivityStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> (Who the sensitivity is for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient who has the allergy or intolerance
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who the sensitivity is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The patient who has the allergy or intolerance
     * </p> 
	 */
	public AllergyIntolerance setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>recorder</b> (Who recorded the sensitivity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates who has responsibility for the record
     * </p> 
	 */
	public ResourceReferenceDt getRecorder() {  
		if (myRecorder == null) {
			myRecorder = new ResourceReferenceDt();
		}
		return myRecorder;
	}

	/**
	 * Sets the value(s) for <b>recorder</b> (Who recorded the sensitivity)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates who has responsibility for the record
     * </p> 
	 */
	public AllergyIntolerance setRecorder(ResourceReferenceDt theValue) {
		myRecorder = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>substance</b> (The substance that causes the sensitivity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The substance that causes the sensitivity
     * </p> 
	 */
	public ResourceReferenceDt getSubstance() {  
		if (mySubstance == null) {
			mySubstance = new ResourceReferenceDt();
		}
		return mySubstance;
	}

	/**
	 * Sets the value(s) for <b>substance</b> (The substance that causes the sensitivity)
	 *
     * <p>
     * <b>Definition:</b>
     * The substance that causes the sensitivity
     * </p> 
	 */
	public AllergyIntolerance setSubstance(ResourceReferenceDt theValue) {
		mySubstance = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reaction</b> (Reactions associated with the sensitivity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reactions associated with the sensitivity
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getReaction() {  
		if (myReaction == null) {
			myReaction = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myReaction;
	}

	/**
	 * Sets the value(s) for <b>reaction</b> (Reactions associated with the sensitivity)
	 *
     * <p>
     * <b>Definition:</b>
     * Reactions associated with the sensitivity
     * </p> 
	 */
	public AllergyIntolerance setReaction(java.util.List<ResourceReferenceDt> theValue) {
		myReaction = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>reaction</b> (Reactions associated with the sensitivity)
	 *
     * <p>
     * <b>Definition:</b>
     * Reactions associated with the sensitivity
     * </p> 
	 */
	public ResourceReferenceDt addReaction() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getReaction().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>sensitivityTest</b> (Observations that confirm or refute).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Observations that confirm or refute the sensitivity
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSensitivityTest() {  
		if (mySensitivityTest == null) {
			mySensitivityTest = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySensitivityTest;
	}

	/**
	 * Sets the value(s) for <b>sensitivityTest</b> (Observations that confirm or refute)
	 *
     * <p>
     * <b>Definition:</b>
     * Observations that confirm or refute the sensitivity
     * </p> 
	 */
	public AllergyIntolerance setSensitivityTest(java.util.List<ResourceReferenceDt> theValue) {
		mySensitivityTest = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>sensitivityTest</b> (Observations that confirm or refute)
	 *
     * <p>
     * <b>Definition:</b>
     * Observations that confirm or refute the sensitivity
     * </p> 
	 */
	public ResourceReferenceDt addSensitivityTest() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSensitivityTest().add(newType);
		return newType; 
	}
  


    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.ALLERGYINTOLERANCE;
    }

}