















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
 * HAPI/FHIR <b>DeviceObservationReport</b> Resource
 * (Describes the data produced by a device at a point in time)
 *
 * <p>
 * <b>Definition:</b>
 * Describes the data produced by a device at a point in time
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DeviceObservationReport">http://hl7.org/fhir/profiles/DeviceObservationReport</a> 
 * </p>
 *
 */
@ResourceDef(name="DeviceObservationReport", profile="http://hl7.org/fhir/profiles/DeviceObservationReport", id="deviceobservationreport")
public class DeviceObservationReport extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.source</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source", path="DeviceObservationReport.source", description="", type="reference"  )
	public static final String SP_SOURCE = "source";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.source</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SOURCE = new ReferenceClientParam(SP_SOURCE);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DeviceObservationReport.source</b>".
	 */
	public static final Include INCLUDE_SOURCE = new Include("DeviceObservationReport.source");

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>The compatment code</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="DeviceObservationReport.virtualDevice.code", description="The compatment code", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>The compatment code</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>channel</b>
	 * <p>
	 * Description: <b>The channel code</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.channel.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="channel", path="DeviceObservationReport.virtualDevice.channel.code", description="The channel code", type="token"  )
	public static final String SP_CHANNEL = "channel";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>channel</b>
	 * <p>
	 * Description: <b>The channel code</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.channel.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CHANNEL = new TokenClientParam(SP_CHANNEL);

	/**
	 * Search parameter constant for <b>observation</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.channel.metric.observation</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="observation", path="DeviceObservationReport.virtualDevice.channel.metric.observation", description="", type="reference"  )
	public static final String SP_OBSERVATION = "observation";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>observation</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.channel.metric.observation</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam OBSERVATION = new ReferenceClientParam(SP_OBSERVATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DeviceObservationReport.virtualDevice.channel.metric.observation</b>".
	 */
	public static final Include INCLUDE_VIRTUALDEVICE_CHANNEL_METRIC_OBSERVATION = new Include("DeviceObservationReport.virtualDevice.channel.metric.observation");

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DeviceObservationReport.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DeviceObservationReport.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("DeviceObservationReport.subject");


	@Child(name="instant", type=InstantDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="When the data values are reported",
		formalDefinition="The point in time that the values are reported"
	)
	private InstantDt myInstant;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="As assigned by the source device",
		formalDefinition="An identifier assigned to this observation bu the source device that made the observation"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="source", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Identifies/describes where the data came from",
		formalDefinition="Identification information for the device that is the source of the data"
	)
	private ResourceReferenceDt mySource;
	
	@Child(name="subject", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Subject of the measurement",
		formalDefinition="The subject of the measurement"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="virtualDevice", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A medical-related subsystem of a medical device",
		formalDefinition="A medical-related subsystem of a medical device"
	)
	private java.util.List<VirtualDevice> myVirtualDevice;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myInstant,  myIdentifier,  mySource,  mySubject,  myVirtualDevice);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myInstant, myIdentifier, mySource, mySubject, myVirtualDevice);
	}

	/**
	 * Gets the value(s) for <b>instant</b> (When the data values are reported).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The point in time that the values are reported
     * </p> 
	 */
	public InstantDt getInstant() {  
		if (myInstant == null) {
			myInstant = new InstantDt();
		}
		return myInstant;
	}

	/**
	 * Sets the value(s) for <b>instant</b> (When the data values are reported)
	 *
     * <p>
     * <b>Definition:</b>
     * The point in time that the values are reported
     * </p> 
	 */
	public DeviceObservationReport setInstant(InstantDt theValue) {
		myInstant = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>instant</b> (When the data values are reported)
	 *
     * <p>
     * <b>Definition:</b>
     * The point in time that the values are reported
     * </p> 
	 */
	public DeviceObservationReport setInstant( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myInstant = new InstantDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>instant</b> (When the data values are reported)
	 *
     * <p>
     * <b>Definition:</b>
     * The point in time that the values are reported
     * </p> 
	 */
	public DeviceObservationReport setInstantWithMillisPrecision( Date theDate) {
		myInstant = new InstantDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identifier</b> (As assigned by the source device).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier assigned to this observation bu the source device that made the observation
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (As assigned by the source device)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier assigned to this observation bu the source device that made the observation
     * </p> 
	 */
	public DeviceObservationReport setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (As assigned by the source device)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier assigned to this observation bu the source device that made the observation
     * </p> 
	 */
	public DeviceObservationReport setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (As assigned by the source device)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier assigned to this observation bu the source device that made the observation
     * </p> 
	 */
	public DeviceObservationReport setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>source</b> (Identifies/describes where the data came from).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification information for the device that is the source of the data
     * </p> 
	 */
	public ResourceReferenceDt getSource() {  
		if (mySource == null) {
			mySource = new ResourceReferenceDt();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> (Identifies/describes where the data came from)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification information for the device that is the source of the data
     * </p> 
	 */
	public DeviceObservationReport setSource(ResourceReferenceDt theValue) {
		mySource = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> (Subject of the measurement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the measurement
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Subject of the measurement)
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the measurement
     * </p> 
	 */
	public DeviceObservationReport setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>virtualDevice</b> (A medical-related subsystem of a medical device).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A medical-related subsystem of a medical device
     * </p> 
	 */
	public java.util.List<VirtualDevice> getVirtualDevice() {  
		if (myVirtualDevice == null) {
			myVirtualDevice = new java.util.ArrayList<VirtualDevice>();
		}
		return myVirtualDevice;
	}

	/**
	 * Sets the value(s) for <b>virtualDevice</b> (A medical-related subsystem of a medical device)
	 *
     * <p>
     * <b>Definition:</b>
     * A medical-related subsystem of a medical device
     * </p> 
	 */
	public DeviceObservationReport setVirtualDevice(java.util.List<VirtualDevice> theValue) {
		myVirtualDevice = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>virtualDevice</b> (A medical-related subsystem of a medical device)
	 *
     * <p>
     * <b>Definition:</b>
     * A medical-related subsystem of a medical device
     * </p> 
	 */
	public VirtualDevice addVirtualDevice() {
		VirtualDevice newType = new VirtualDevice();
		getVirtualDevice().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>virtualDevice</b> (A medical-related subsystem of a medical device),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A medical-related subsystem of a medical device
     * </p> 
	 */
	public VirtualDevice getVirtualDeviceFirstRep() {
		if (getVirtualDevice().isEmpty()) {
			return addVirtualDevice();
		}
		return getVirtualDevice().get(0); 
	}
  
	/**
	 * Block class for child element: <b>DeviceObservationReport.virtualDevice</b> (A medical-related subsystem of a medical device)
	 *
     * <p>
     * <b>Definition:</b>
     * A medical-related subsystem of a medical device
     * </p> 
	 */
	@Block()	
	public static class VirtualDevice extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Describes the compartment",
		formalDefinition="Describes the compartment"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="channel", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Groups related data items",
		formalDefinition="Groups together physiological measurement data and derived data"
	)
	private java.util.List<VirtualDeviceChannel> myChannel;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myChannel);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myChannel);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Describes the compartment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the compartment
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Describes the compartment)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the compartment
     * </p> 
	 */
	public VirtualDevice setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>channel</b> (Groups related data items).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Groups together physiological measurement data and derived data
     * </p> 
	 */
	public java.util.List<VirtualDeviceChannel> getChannel() {  
		if (myChannel == null) {
			myChannel = new java.util.ArrayList<VirtualDeviceChannel>();
		}
		return myChannel;
	}

	/**
	 * Sets the value(s) for <b>channel</b> (Groups related data items)
	 *
     * <p>
     * <b>Definition:</b>
     * Groups together physiological measurement data and derived data
     * </p> 
	 */
	public VirtualDevice setChannel(java.util.List<VirtualDeviceChannel> theValue) {
		myChannel = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>channel</b> (Groups related data items)
	 *
     * <p>
     * <b>Definition:</b>
     * Groups together physiological measurement data and derived data
     * </p> 
	 */
	public VirtualDeviceChannel addChannel() {
		VirtualDeviceChannel newType = new VirtualDeviceChannel();
		getChannel().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>channel</b> (Groups related data items),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Groups together physiological measurement data and derived data
     * </p> 
	 */
	public VirtualDeviceChannel getChannelFirstRep() {
		if (getChannel().isEmpty()) {
			return addChannel();
		}
		return getChannel().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>DeviceObservationReport.virtualDevice.channel</b> (Groups related data items)
	 *
     * <p>
     * <b>Definition:</b>
     * Groups together physiological measurement data and derived data
     * </p> 
	 */
	@Block()	
	public static class VirtualDeviceChannel extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Describes the channel",
		formalDefinition="Describes the channel"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="metric", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Piece of data reported by device",
		formalDefinition="A piece of measured or derived data that is reported by the machine"
	)
	private java.util.List<VirtualDeviceChannelMetric> myMetric;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myMetric);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myMetric);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Describes the channel).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the channel
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Describes the channel)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the channel
     * </p> 
	 */
	public VirtualDeviceChannel setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>metric</b> (Piece of data reported by device).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A piece of measured or derived data that is reported by the machine
     * </p> 
	 */
	public java.util.List<VirtualDeviceChannelMetric> getMetric() {  
		if (myMetric == null) {
			myMetric = new java.util.ArrayList<VirtualDeviceChannelMetric>();
		}
		return myMetric;
	}

	/**
	 * Sets the value(s) for <b>metric</b> (Piece of data reported by device)
	 *
     * <p>
     * <b>Definition:</b>
     * A piece of measured or derived data that is reported by the machine
     * </p> 
	 */
	public VirtualDeviceChannel setMetric(java.util.List<VirtualDeviceChannelMetric> theValue) {
		myMetric = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>metric</b> (Piece of data reported by device)
	 *
     * <p>
     * <b>Definition:</b>
     * A piece of measured or derived data that is reported by the machine
     * </p> 
	 */
	public VirtualDeviceChannelMetric addMetric() {
		VirtualDeviceChannelMetric newType = new VirtualDeviceChannelMetric();
		getMetric().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>metric</b> (Piece of data reported by device),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A piece of measured or derived data that is reported by the machine
     * </p> 
	 */
	public VirtualDeviceChannelMetric getMetricFirstRep() {
		if (getMetric().isEmpty()) {
			return addMetric();
		}
		return getMetric().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>DeviceObservationReport.virtualDevice.channel.metric</b> (Piece of data reported by device)
	 *
     * <p>
     * <b>Definition:</b>
     * A piece of measured or derived data that is reported by the machine
     * </p> 
	 */
	@Block()	
	public static class VirtualDeviceChannelMetric extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="observation", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Observation.class	})
	@Description(
		shortDefinition="The data for the metric",
		formalDefinition="The data for the metric"
	)
	private ResourceReferenceDt myObservation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myObservation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myObservation);
	}

	/**
	 * Gets the value(s) for <b>observation</b> (The data for the metric).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The data for the metric
     * </p> 
	 */
	public ResourceReferenceDt getObservation() {  
		if (myObservation == null) {
			myObservation = new ResourceReferenceDt();
		}
		return myObservation;
	}

	/**
	 * Sets the value(s) for <b>observation</b> (The data for the metric)
	 *
     * <p>
     * <b>Definition:</b>
     * The data for the metric
     * </p> 
	 */
	public VirtualDeviceChannelMetric setObservation(ResourceReferenceDt theValue) {
		myObservation = theValue;
		return this;
	}

  

	}






}