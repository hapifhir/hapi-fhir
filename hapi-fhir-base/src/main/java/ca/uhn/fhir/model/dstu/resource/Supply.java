















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
 * HAPI/FHIR <b>Supply</b> Resource
 * (A supply -  request and provision)
 *
 * <p>
 * <b>Definition:</b>
 * A supply - a  request for something, and provision of what is supplied
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Supply">http://hl7.org/fhir/profiles/Supply</a> 
 * </p>
 *
 */
@ResourceDef(name="Supply", profile="http://hl7.org/fhir/profiles/Supply", id="supply")
public class Supply extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>kind</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.kind</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="kind", path="Supply.kind", description="", type="token"  )
	public static final String SP_KIND = "kind";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>kind</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.kind</b><br/>
	 * </p>
	 */
	public static final TokenClientParam KIND = new TokenClientParam(SP_KIND);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Supply.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Supply.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Supply.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Supply.patient", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Supply.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Supply.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("Supply.patient");

	/**
	 * Search parameter constant for <b>supplier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Supply.dispense.supplier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="supplier", path="Supply.dispense.supplier", description="", type="reference"  )
	public static final String SP_SUPPLIER = "supplier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>supplier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Supply.dispense.supplier</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUPPLIER = new ReferenceClientParam(SP_SUPPLIER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Supply.dispense.supplier</b>".
	 */
	public static final Include INCLUDE_DISPENSE_SUPPLIER = new Include("Supply.dispense.supplier");

	/**
	 * Search parameter constant for <b>dispenseid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.dispense.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dispenseid", path="Supply.dispense.identifier", description="", type="token"  )
	public static final String SP_DISPENSEID = "dispenseid";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dispenseid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.dispense.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DISPENSEID = new TokenClientParam(SP_DISPENSEID);

	/**
	 * Search parameter constant for <b>dispensestatus</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.dispense.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dispensestatus", path="Supply.dispense.status", description="", type="token"  )
	public static final String SP_DISPENSESTATUS = "dispensestatus";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dispensestatus</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.dispense.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DISPENSESTATUS = new TokenClientParam(SP_DISPENSESTATUS);


	@Child(name="kind", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="The kind of supply (central, non-stock, etc)",
		formalDefinition="Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process"
	)
	private BoundCodeableConceptDt<SupplyTypeEnum> myKind;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Unique identifier",
		formalDefinition="Unique identifier for this supply request"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="requested | dispensed | received | failed | cancelled",
		formalDefinition="Status of the supply request"
	)
	private BoundCodeDt<SupplyStatusEnum> myStatus;
	
	@Child(name="orderedItem", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Medication.class, 		ca.uhn.fhir.model.dstu.resource.Substance.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Medication, Substance, or Device requested to be supplied",
		formalDefinition="The item that is requested to be supplied"
	)
	private ResourceReferenceDt myOrderedItem;
	
	@Child(name="patient", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Patient for whom the item is supplied",
		formalDefinition="A link to a resource representing the person whom the ordered item is for"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="dispense", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Supply details",
		formalDefinition="Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed."
	)
	private java.util.List<Dispense> myDispense;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myKind,  myIdentifier,  myStatus,  myOrderedItem,  myPatient,  myDispense);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myKind, myIdentifier, myStatus, myOrderedItem, myPatient, myDispense);
	}
	

	/**
	 * Gets the value(s) for <b>kind</b> (The kind of supply (central, non-stock, etc)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process
     * </p> 
	 */
	public BoundCodeableConceptDt<SupplyTypeEnum> getKind() {  
		if (myKind == null) {
			myKind = new BoundCodeableConceptDt<SupplyTypeEnum>(SupplyTypeEnum.VALUESET_BINDER);
		}
		return myKind;
	}

	/**
	 * Sets the value(s) for <b>kind</b> (The kind of supply (central, non-stock, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process
     * </p> 
	 */
	public Supply setKind(BoundCodeableConceptDt<SupplyTypeEnum> theValue) {
		myKind = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>kind</b> (The kind of supply (central, non-stock, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process
     * </p> 
	 */
	public Supply setKind(SupplyTypeEnum theValue) {
		getKind().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>identifier</b> (Unique identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for this supply request
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Unique identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for this supply request
     * </p> 
	 */
	public Supply setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Unique identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for this supply request
     * </p> 
	 */
	public Supply setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Unique identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for this supply request
     * </p> 
	 */
	public Supply setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (requested | dispensed | received | failed | cancelled).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the supply request
     * </p> 
	 */
	public BoundCodeDt<SupplyStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<SupplyStatusEnum>(SupplyStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (requested | dispensed | received | failed | cancelled)
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the supply request
     * </p> 
	 */
	public Supply setStatus(BoundCodeDt<SupplyStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (requested | dispensed | received | failed | cancelled)
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the supply request
     * </p> 
	 */
	public Supply setStatus(SupplyStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>orderedItem</b> (Medication, Substance, or Device requested to be supplied).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The item that is requested to be supplied
     * </p> 
	 */
	public ResourceReferenceDt getOrderedItem() {  
		if (myOrderedItem == null) {
			myOrderedItem = new ResourceReferenceDt();
		}
		return myOrderedItem;
	}

	/**
	 * Sets the value(s) for <b>orderedItem</b> (Medication, Substance, or Device requested to be supplied)
	 *
     * <p>
     * <b>Definition:</b>
     * The item that is requested to be supplied
     * </p> 
	 */
	public Supply setOrderedItem(ResourceReferenceDt theValue) {
		myOrderedItem = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> (Patient for whom the item is supplied).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A link to a resource representing the person whom the ordered item is for
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (Patient for whom the item is supplied)
	 *
     * <p>
     * <b>Definition:</b>
     * A link to a resource representing the person whom the ordered item is for
     * </p> 
	 */
	public Supply setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dispense</b> (Supply details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public java.util.List<Dispense> getDispense() {  
		if (myDispense == null) {
			myDispense = new java.util.ArrayList<Dispense>();
		}
		return myDispense;
	}

	/**
	 * Sets the value(s) for <b>dispense</b> (Supply details)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public Supply setDispense(java.util.List<Dispense> theValue) {
		myDispense = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>dispense</b> (Supply details)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public Dispense addDispense() {
		Dispense newType = new Dispense();
		getDispense().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dispense</b> (Supply details),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public Dispense getDispenseFirstRep() {
		if (getDispense().isEmpty()) {
			return addDispense();
		}
		return getDispense().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Supply.dispense</b> (Supply details)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	@Block()	
	public static class Dispense extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="External identifier",
		formalDefinition="Identifier assigned by the dispensing facility when the dispense occurs"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="in progress | dispensed | abandoned",
		formalDefinition="A code specifying the state of the dispense event."
	)
	private BoundCodeDt<SupplyDispenseStatusEnum> myStatus;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Category of dispense event",
		formalDefinition="Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc."
	)
	private BoundCodeableConceptDt<SupplyItemTypeEnum> myType;
	
	@Child(name="quantity", type=QuantityDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Amount dispensed",
		formalDefinition="The amount of supply that has been dispensed. Includes unit of measure."
	)
	private QuantityDt myQuantity;
	
	@Child(name="suppliedItem", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Medication.class, 		ca.uhn.fhir.model.dstu.resource.Substance.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Medication, Substance, or Device supplied",
		formalDefinition="Identifies the medication or substance being dispensed. This is either a link to a resource representing the details of the medication or substance or a simple attribute carrying a code that identifies the medication from a known list of medications."
	)
	private ResourceReferenceDt mySuppliedItem;
	
	@Child(name="supplier", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Dispenser",
		formalDefinition="The individual responsible for dispensing the medication"
	)
	private ResourceReferenceDt mySupplier;
	
	@Child(name="whenPrepared", type=PeriodDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Dispensing time",
		formalDefinition="The time the dispense event occurred."
	)
	private PeriodDt myWhenPrepared;
	
	@Child(name="whenHandedOver", type=PeriodDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Handover time",
		formalDefinition="The time the dispensed item was sent or handed to the patient (or agent)."
	)
	private PeriodDt myWhenHandedOver;
	
	@Child(name="destination", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Where the Supply was sent",
		formalDefinition="Identification of the facility/location where the Supply was shipped to, as part of the dispense event."
	)
	private ResourceReferenceDt myDestination;
	
	@Child(name="receiver", order=9, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who collected the Supply",
		formalDefinition="Identifies the person who picked up the Supply."
	)
	private java.util.List<ResourceReferenceDt> myReceiver;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myStatus,  myType,  myQuantity,  mySuppliedItem,  mySupplier,  myWhenPrepared,  myWhenHandedOver,  myDestination,  myReceiver);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myStatus, myType, myQuantity, mySuppliedItem, mySupplier, myWhenPrepared, myWhenHandedOver, myDestination, myReceiver);
	}
	

	/**
	 * Gets the value(s) for <b>identifier</b> (External identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility when the dispense occurs
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility when the dispense occurs
     * </p> 
	 */
	public Dispense setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility when the dispense occurs
     * </p> 
	 */
	public Dispense setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility when the dispense occurs
     * </p> 
	 */
	public Dispense setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (in progress | dispensed | abandoned).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public BoundCodeDt<SupplyDispenseStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<SupplyDispenseStatusEnum>(SupplyDispenseStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | dispensed | abandoned)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public Dispense setStatus(BoundCodeDt<SupplyDispenseStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | dispensed | abandoned)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public Dispense setStatus(SupplyDispenseStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Category of dispense event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public BoundCodeableConceptDt<SupplyItemTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<SupplyItemTypeEnum>(SupplyItemTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Category of dispense event)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public Dispense setType(BoundCodeableConceptDt<SupplyItemTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Category of dispense event)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public Dispense setType(SupplyItemTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>quantity</b> (Amount dispensed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( long theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( double theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>suppliedItem</b> (Medication, Substance, or Device supplied).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication or substance being dispensed. This is either a link to a resource representing the details of the medication or substance or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public ResourceReferenceDt getSuppliedItem() {  
		if (mySuppliedItem == null) {
			mySuppliedItem = new ResourceReferenceDt();
		}
		return mySuppliedItem;
	}

	/**
	 * Sets the value(s) for <b>suppliedItem</b> (Medication, Substance, or Device supplied)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication or substance being dispensed. This is either a link to a resource representing the details of the medication or substance or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public Dispense setSuppliedItem(ResourceReferenceDt theValue) {
		mySuppliedItem = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>supplier</b> (Dispenser).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The individual responsible for dispensing the medication
     * </p> 
	 */
	public ResourceReferenceDt getSupplier() {  
		if (mySupplier == null) {
			mySupplier = new ResourceReferenceDt();
		}
		return mySupplier;
	}

	/**
	 * Sets the value(s) for <b>supplier</b> (Dispenser)
	 *
     * <p>
     * <b>Definition:</b>
     * The individual responsible for dispensing the medication
     * </p> 
	 */
	public Dispense setSupplier(ResourceReferenceDt theValue) {
		mySupplier = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>whenPrepared</b> (Dispensing time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispense event occurred.
     * </p> 
	 */
	public PeriodDt getWhenPrepared() {  
		if (myWhenPrepared == null) {
			myWhenPrepared = new PeriodDt();
		}
		return myWhenPrepared;
	}

	/**
	 * Sets the value(s) for <b>whenPrepared</b> (Dispensing time)
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispense event occurred.
     * </p> 
	 */
	public Dispense setWhenPrepared(PeriodDt theValue) {
		myWhenPrepared = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>whenHandedOver</b> (Handover time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispensed item was sent or handed to the patient (or agent).
     * </p> 
	 */
	public PeriodDt getWhenHandedOver() {  
		if (myWhenHandedOver == null) {
			myWhenHandedOver = new PeriodDt();
		}
		return myWhenHandedOver;
	}

	/**
	 * Sets the value(s) for <b>whenHandedOver</b> (Handover time)
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispensed item was sent or handed to the patient (or agent).
     * </p> 
	 */
	public Dispense setWhenHandedOver(PeriodDt theValue) {
		myWhenHandedOver = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>destination</b> (Where the Supply was sent).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
     * </p> 
	 */
	public ResourceReferenceDt getDestination() {  
		if (myDestination == null) {
			myDestination = new ResourceReferenceDt();
		}
		return myDestination;
	}

	/**
	 * Sets the value(s) for <b>destination</b> (Where the Supply was sent)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
     * </p> 
	 */
	public Dispense setDestination(ResourceReferenceDt theValue) {
		myDestination = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>receiver</b> (Who collected the Supply).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the Supply.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getReceiver() {  
		if (myReceiver == null) {
			myReceiver = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myReceiver;
	}

	/**
	 * Sets the value(s) for <b>receiver</b> (Who collected the Supply)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the Supply.
     * </p> 
	 */
	public Dispense setReceiver(java.util.List<ResourceReferenceDt> theValue) {
		myReceiver = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>receiver</b> (Who collected the Supply)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the Supply.
     * </p> 
	 */
	public ResourceReferenceDt addReceiver() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getReceiver().add(newType);
		return newType; 
	}
  

	}




    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.SUPPLY;
    }

}