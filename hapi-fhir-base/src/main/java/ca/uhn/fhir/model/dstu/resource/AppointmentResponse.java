















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
 * HAPI/FHIR <b>AppointmentResponse</b> Resource
 * ((informative) A response to a scheduled appointment for a patient and/or practitioner(s))
 *
 * <p>
 * <b>Definition:</b>
 * A scheduled appointment for a patient and/or practitioner(s) where a service may take place.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/AppointmentResponse">http://hl7.org/fhir/profiles/AppointmentResponse</a> 
 * </p>
 *
 */
@ResourceDef(name="AppointmentResponse", profile="http://hl7.org/fhir/profiles/AppointmentResponse", id="appointmentresponse")
public class AppointmentResponse extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>partstatus</b>
	 * <p>
	 * Description: <b>The overall status of the appointment</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>AppointmentResponse.participantStatus</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="partstatus", path="AppointmentResponse.participantStatus", description="The overall status of the appointment", type="string"  )
	public static final String SP_PARTSTATUS = "partstatus";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>partstatus</b>
	 * <p>
	 * Description: <b>The overall status of the appointment</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>AppointmentResponse.participantStatus</b><br/>
	 * </p>
	 */
	public static final StringClientParam PARTSTATUS = new StringClientParam(SP_PARTSTATUS);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the appointment response replies for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AppointmentResponse.individual</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="AppointmentResponse.individual", description="The subject that the appointment response replies for", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the appointment response replies for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AppointmentResponse.individual</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AppointmentResponse.individual</b>".
	 */
	public static final Include INCLUDE_INDIVIDUAL = new Include("AppointmentResponse.individual");

	/**
	 * Search parameter constant for <b>appointment</b>
	 * <p>
	 * Description: <b>The appointment that the response is attached to</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AppointmentResponse.appointment</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="appointment", path="AppointmentResponse.appointment", description="The appointment that the response is attached to", type="reference"  )
	public static final String SP_APPOINTMENT = "appointment";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>appointment</b>
	 * <p>
	 * Description: <b>The appointment that the response is attached to</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AppointmentResponse.appointment</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam APPOINTMENT = new ReferenceClientParam(SP_APPOINTMENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AppointmentResponse.appointment</b>".
	 */
	public static final Include INCLUDE_APPOINTMENT = new Include("AppointmentResponse.appointment");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this item",
		formalDefinition="This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="appointment", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Appointment.class	})
	@Description(
		shortDefinition="Parent appointment that this response is replying to",
		formalDefinition=""
	)
	private ResourceReferenceDt myAppointment;
	
	@Child(name="participantType", type=CodeableConceptDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Role of participant in the appointment",
		formalDefinition=""
	)
	private java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> myParticipantType;
	
	@Child(name="individual", order=3, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="A Person of device that is participating in the appointment",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> myIndividual;
	
	@Child(name="participantStatus", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="accepted | declined | tentative | in-process | completed | needs-action",
		formalDefinition="Participation status of the Patient"
	)
	private CodeDt myParticipantStatus;
	
	@Child(name="comment", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Additional comments about the appointment",
		formalDefinition=""
	)
	private StringDt myComment;
	
	@Child(name="start", type=InstantDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Date/Time that the appointment is to take place",
		formalDefinition=""
	)
	private InstantDt myStart;
	
	@Child(name="end", type=InstantDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Date/Time that the appointment is to conclude",
		formalDefinition=""
	)
	private InstantDt myEnd;
	
	@Child(name="schedule", type=ScheduleDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="The recurrence schedule for the appointment. The end date in the schedule marks the end of the recurrence(s), not the end of an individual appointment",
		formalDefinition=""
	)
	private ScheduleDt mySchedule;
	
	@Child(name="timezone", type=StringDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="The timezone that the times are to be converted to. Required for recurring appointments to remain accurate where the schedule makes the appointment cross a daylight saving boundry",
		formalDefinition="The timezone should be a value referenced from a timezone database"
	)
	private StringDt myTimezone;
	
	@Child(name="recorder", order=10, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Who recorded the appointment response",
		formalDefinition=""
	)
	private ResourceReferenceDt myRecorder;
	
	@Child(name="recordedDate", type=DateTimeDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="Date when the response was recorded or last updated",
		formalDefinition=""
	)
	private DateTimeDt myRecordedDate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myAppointment,  myParticipantType,  myIndividual,  myParticipantStatus,  myComment,  myStart,  myEnd,  mySchedule,  myTimezone,  myRecorder,  myRecordedDate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myAppointment, myParticipantType, myIndividual, myParticipantStatus, myComment, myStart, myEnd, mySchedule, myTimezone, myRecorder, myRecordedDate);
	}
	

	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public AppointmentResponse setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public AppointmentResponse addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
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
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public AppointmentResponse addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>appointment</b> (Parent appointment that this response is replying to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getAppointment() {  
		if (myAppointment == null) {
			myAppointment = new ResourceReferenceDt();
		}
		return myAppointment;
	}

	/**
	 * Sets the value(s) for <b>appointment</b> (Parent appointment that this response is replying to)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setAppointment(ResourceReferenceDt theValue) {
		myAppointment = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>participantType</b> (Role of participant in the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> getParticipantType() {  
		if (myParticipantType == null) {
			myParticipantType = new java.util.ArrayList<BoundCodeableConceptDt<ParticipantTypeEnum>>();
		}
		return myParticipantType;
	}

	/**
	 * Sets the value(s) for <b>participantType</b> (Role of participant in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setParticipantType(java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> theValue) {
		myParticipantType = theValue;
		return this;
	}

	/**
	 * Add a value for <b>participantType</b> (Role of participant in the appointment) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> addParticipantType(ParticipantTypeEnum theValue) {
		BoundCodeableConceptDt<ParticipantTypeEnum> retVal = new BoundCodeableConceptDt<ParticipantTypeEnum>(ParticipantTypeEnum.VALUESET_BINDER, theValue);
		getParticipantType().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>participantType</b> (Role of participant in the appointment),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> getParticipantTypeFirstRep() {
		if (getParticipantType().size() == 0) {
			addParticipantType();
		}
		return getParticipantType().get(0);
	}

	/**
	 * Add a value for <b>participantType</b> (Role of participant in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> addParticipantType() {
		BoundCodeableConceptDt<ParticipantTypeEnum> retVal = new BoundCodeableConceptDt<ParticipantTypeEnum>(ParticipantTypeEnum.VALUESET_BINDER);
		getParticipantType().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>participantType</b> (Role of participant in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setParticipantType(ParticipantTypeEnum theValue) {
		getParticipantType().clear();
		addParticipantType(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>individual</b> (A Person of device that is participating in the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getIndividual() {  
		if (myIndividual == null) {
			myIndividual = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myIndividual;
	}

	/**
	 * Sets the value(s) for <b>individual</b> (A Person of device that is participating in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setIndividual(java.util.List<ResourceReferenceDt> theValue) {
		myIndividual = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>individual</b> (A Person of device that is participating in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addIndividual() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getIndividual().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>participantStatus</b> (accepted | declined | tentative | in-process | completed | needs-action).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public CodeDt getParticipantStatus() {  
		if (myParticipantStatus == null) {
			myParticipantStatus = new CodeDt();
		}
		return myParticipantStatus;
	}

	/**
	 * Sets the value(s) for <b>participantStatus</b> (accepted | declined | tentative | in-process | completed | needs-action)
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public AppointmentResponse setParticipantStatus(CodeDt theValue) {
		myParticipantStatus = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>participantStatus</b> (accepted | declined | tentative | in-process | completed | needs-action)
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public AppointmentResponse setParticipantStatus( String theCode) {
		myParticipantStatus = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>comment</b> (Additional comments about the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getComment() {  
		if (myComment == null) {
			myComment = new StringDt();
		}
		return myComment;
	}

	/**
	 * Sets the value(s) for <b>comment</b> (Additional comments about the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setComment(StringDt theValue) {
		myComment = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>comment</b> (Additional comments about the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setComment( String theString) {
		myComment = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>start</b> (Date/Time that the appointment is to take place).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getStart() {  
		if (myStart == null) {
			myStart = new InstantDt();
		}
		return myStart;
	}

	/**
	 * Sets the value(s) for <b>start</b> (Date/Time that the appointment is to take place)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setStart(InstantDt theValue) {
		myStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>start</b> (Date/Time that the appointment is to take place)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setStartWithMillisPrecision( Date theDate) {
		myStart = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>start</b> (Date/Time that the appointment is to take place)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setStart( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myStart = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (Date/Time that the appointment is to conclude).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getEnd() {  
		if (myEnd == null) {
			myEnd = new InstantDt();
		}
		return myEnd;
	}

	/**
	 * Sets the value(s) for <b>end</b> (Date/Time that the appointment is to conclude)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setEnd(InstantDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (Date/Time that the appointment is to conclude)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setEndWithMillisPrecision( Date theDate) {
		myEnd = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>end</b> (Date/Time that the appointment is to conclude)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setEnd( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myEnd = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>schedule</b> (The recurrence schedule for the appointment. The end date in the schedule marks the end of the recurrence(s), not the end of an individual appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ScheduleDt getSchedule() {  
		if (mySchedule == null) {
			mySchedule = new ScheduleDt();
		}
		return mySchedule;
	}

	/**
	 * Sets the value(s) for <b>schedule</b> (The recurrence schedule for the appointment. The end date in the schedule marks the end of the recurrence(s), not the end of an individual appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setSchedule(ScheduleDt theValue) {
		mySchedule = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>timezone</b> (The timezone that the times are to be converted to. Required for recurring appointments to remain accurate where the schedule makes the appointment cross a daylight saving boundry).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The timezone should be a value referenced from a timezone database
     * </p> 
	 */
	public StringDt getTimezone() {  
		if (myTimezone == null) {
			myTimezone = new StringDt();
		}
		return myTimezone;
	}

	/**
	 * Sets the value(s) for <b>timezone</b> (The timezone that the times are to be converted to. Required for recurring appointments to remain accurate where the schedule makes the appointment cross a daylight saving boundry)
	 *
     * <p>
     * <b>Definition:</b>
     * The timezone should be a value referenced from a timezone database
     * </p> 
	 */
	public AppointmentResponse setTimezone(StringDt theValue) {
		myTimezone = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>timezone</b> (The timezone that the times are to be converted to. Required for recurring appointments to remain accurate where the schedule makes the appointment cross a daylight saving boundry)
	 *
     * <p>
     * <b>Definition:</b>
     * The timezone should be a value referenced from a timezone database
     * </p> 
	 */
	public AppointmentResponse setTimezone( String theString) {
		myTimezone = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>recorder</b> (Who recorded the appointment response).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getRecorder() {  
		if (myRecorder == null) {
			myRecorder = new ResourceReferenceDt();
		}
		return myRecorder;
	}

	/**
	 * Sets the value(s) for <b>recorder</b> (Who recorded the appointment response)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setRecorder(ResourceReferenceDt theValue) {
		myRecorder = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>recordedDate</b> (Date when the response was recorded or last updated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DateTimeDt getRecordedDate() {  
		if (myRecordedDate == null) {
			myRecordedDate = new DateTimeDt();
		}
		return myRecordedDate;
	}

	/**
	 * Sets the value(s) for <b>recordedDate</b> (Date when the response was recorded or last updated)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setRecordedDate(DateTimeDt theValue) {
		myRecordedDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>recordedDate</b> (Date when the response was recorded or last updated)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setRecordedDateWithSecondsPrecision( Date theDate) {
		myRecordedDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>recordedDate</b> (Date when the response was recorded or last updated)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AppointmentResponse setRecordedDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myRecordedDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 


    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.APPOINTMENTRESPONSE;
    }

}