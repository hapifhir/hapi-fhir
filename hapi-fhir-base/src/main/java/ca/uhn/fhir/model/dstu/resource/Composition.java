















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
 * HAPI/FHIR <b>Composition</b> Resource
 * (A set of resources composed into a single coherent clinical statement with clinical attestation)
 *
 * <p>
 * <b>Definition:</b>
 * A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * To support documents, and also to capture the EN13606 notion of an attested commit to the patient EHR, and to allow a set of disparate resources at the information/engineering level to be gathered into a clinical statement
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Composition">http://hl7.org/fhir/profiles/Composition</a> 
 * </p>
 *
 */
@ResourceDef(name="Composition", profile="http://hl7.org/fhir/profiles/Composition", id="composition")
public class Composition extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Composition.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.class</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="class", path="Composition.class", description="", type="token"  )
	public static final String SP_CLASS = "class";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.class</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CLASS = new TokenClientParam(SP_CLASS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Composition.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Composition.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Composition.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Composition.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Composition.subject");

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="Composition.author", description="", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.author</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("Composition.author");

	/**
	 * Search parameter constant for <b>attester</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.attester.party</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="attester", path="Composition.attester.party", description="", type="reference"  )
	public static final String SP_ATTESTER = "attester";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>attester</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.attester.party</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ATTESTER = new ReferenceClientParam(SP_ATTESTER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.attester.party</b>".
	 */
	public static final Include INCLUDE_ATTESTER_PARTY = new Include("Composition.attester.party");

	/**
	 * Search parameter constant for <b>context</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.event.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="context", path="Composition.event.code", description="", type="token"  )
	public static final String SP_CONTEXT = "context";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>context</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.event.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONTEXT = new TokenClientParam(SP_CONTEXT);

	/**
	 * Search parameter constant for <b>section-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.section.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="section-type", path="Composition.section.code", description="", type="token"  )
	public static final String SP_SECTION_TYPE = "section-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>section-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.section.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SECTION_TYPE = new TokenClientParam(SP_SECTION_TYPE);

	/**
	 * Search parameter constant for <b>section-content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.section.content</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="section-content", path="Composition.section.content", description="", type="reference"  )
	public static final String SP_SECTION_CONTENT = "section-content";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>section-content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.section.content</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SECTION_CONTENT = new ReferenceClientParam(SP_SECTION_CONTENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.section.content</b>".
	 */
	public static final Include INCLUDE_SECTION_CONTENT = new Include("Composition.section.content");

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Composition.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Logical identifier of composition (version-independent)",
		formalDefinition="Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="date", type=DateTimeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Composition editing time",
		formalDefinition="The composition editing time, when the composition was last logically changed by the author"
	)
	private DateTimeDt myDate;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Kind of composition (LOINC if possible)",
		formalDefinition="Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition"
	)
	private CodeableConceptDt myType;
	
	@Child(name="class", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Categorization of Composition",
		formalDefinition="A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type"
	)
	private CodeableConceptDt myClassElement;
	
	@Child(name="title", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Human Readable name/title",
		formalDefinition="Official human-readable label for the composition"
	)
	private StringDt myTitle;
	
	@Child(name="status", type=CodeDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="preliminary | final | appended | amended | entered in error",
		formalDefinition="The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document"
	)
	private BoundCodeDt<CompositionStatusEnum> myStatus;
	
	@Child(name="confidentiality", type=CodingDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="As defined by affinity domain",
		formalDefinition="The code specifying the level of confidentiality of the Composition"
	)
	private CodingDt myConfidentiality;
	
	@Child(name="subject", order=7, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Who and/or what the composition is about",
		formalDefinition="Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure)"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="author", order=8, min=1, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Who and/or what authored the composition",
		formalDefinition="Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.)"
	)
	private java.util.List<ResourceReferenceDt> myAuthor;
	
	@Child(name="attester", order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Attests to accuracy of composition",
		formalDefinition="A participant who has attested to the accuracy of the composition/document"
	)
	private java.util.List<Attester> myAttester;
	
	@Child(name="custodian", order=10, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Org which maintains the composition",
		formalDefinition="Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information"
	)
	private ResourceReferenceDt myCustodian;
	
	@Child(name="event", order=11, min=0, max=1)	
	@Description(
		shortDefinition="The clinical event/act/item being documented",
		formalDefinition="The main event/act/item, such as a colonoscopy or an appendectomy, being documented"
	)
	private Event myEvent;
	
	@Child(name="encounter", order=12, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Encounter.class	})
	@Description(
		shortDefinition="Context of the conposition",
		formalDefinition="Describes the clinical encounter or type of care this documentation is associated with."
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="section", order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Composition is broken into sections",
		formalDefinition="The root of the sections that make up the composition"
	)
	private java.util.List<Section> mySection;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myDate,  myType,  myClassElement,  myTitle,  myStatus,  myConfidentiality,  mySubject,  myAuthor,  myAttester,  myCustodian,  myEvent,  myEncounter,  mySection);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myDate, myType, myClassElement, myTitle, myStatus, myConfidentiality, mySubject, myAuthor, myAttester, myCustodian, myEvent, myEncounter, mySection);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Logical identifier of composition (version-independent)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Logical identifier of composition (version-independent))
	 *
     * <p>
     * <b>Definition:</b>
     * Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time
     * </p> 
	 */
	public Composition setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Logical identifier of composition (version-independent))
	 *
     * <p>
     * <b>Definition:</b>
     * Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time
     * </p> 
	 */
	public Composition setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Logical identifier of composition (version-independent))
	 *
     * <p>
     * <b>Definition:</b>
     * Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time
     * </p> 
	 */
	public Composition setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (Composition editing time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The composition editing time, when the composition was last logically changed by the author
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Composition editing time)
	 *
     * <p>
     * <b>Definition:</b>
     * The composition editing time, when the composition was last logically changed by the author
     * </p> 
	 */
	public Composition setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (Composition editing time)
	 *
     * <p>
     * <b>Definition:</b>
     * The composition editing time, when the composition was last logically changed by the author
     * </p> 
	 */
	public Composition setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (Composition editing time)
	 *
     * <p>
     * <b>Definition:</b>
     * The composition editing time, when the composition was last logically changed by the author
     * </p> 
	 */
	public Composition setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (Kind of composition (LOINC if possible)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Kind of composition (LOINC if possible))
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition
     * </p> 
	 */
	public Composition setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>class</b> (Categorization of Composition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type
     * </p> 
	 */
	public CodeableConceptDt getClassElement() {  
		if (myClassElement == null) {
			myClassElement = new CodeableConceptDt();
		}
		return myClassElement;
	}

	/**
	 * Sets the value(s) for <b>class</b> (Categorization of Composition)
	 *
     * <p>
     * <b>Definition:</b>
     * A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type
     * </p> 
	 */
	public Composition setClassElement(CodeableConceptDt theValue) {
		myClassElement = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>title</b> (Human Readable name/title).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Official human-readable label for the composition
     * </p> 
	 */
	public StringDt getTitle() {  
		if (myTitle == null) {
			myTitle = new StringDt();
		}
		return myTitle;
	}

	/**
	 * Sets the value(s) for <b>title</b> (Human Readable name/title)
	 *
     * <p>
     * <b>Definition:</b>
     * Official human-readable label for the composition
     * </p> 
	 */
	public Composition setTitle(StringDt theValue) {
		myTitle = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>title</b> (Human Readable name/title)
	 *
     * <p>
     * <b>Definition:</b>
     * Official human-readable label for the composition
     * </p> 
	 */
	public Composition setTitle( String theString) {
		myTitle = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (preliminary | final | appended | amended | entered in error).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document
     * </p> 
	 */
	public BoundCodeDt<CompositionStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<CompositionStatusEnum>(CompositionStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (preliminary | final | appended | amended | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document
     * </p> 
	 */
	public Composition setStatus(BoundCodeDt<CompositionStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (preliminary | final | appended | amended | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document
     * </p> 
	 */
	public Composition setStatus(CompositionStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>confidentiality</b> (As defined by affinity domain).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code specifying the level of confidentiality of the Composition
     * </p> 
	 */
	public CodingDt getConfidentiality() {  
		if (myConfidentiality == null) {
			myConfidentiality = new CodingDt();
		}
		return myConfidentiality;
	}

	/**
	 * Sets the value(s) for <b>confidentiality</b> (As defined by affinity domain)
	 *
     * <p>
     * <b>Definition:</b>
     * The code specifying the level of confidentiality of the Composition
     * </p> 
	 */
	public Composition setConfidentiality(CodingDt theValue) {
		myConfidentiality = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> (Who and/or what the composition is about).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure)
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who and/or what the composition is about)
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure)
     * </p> 
	 */
	public Composition setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>author</b> (Who and/or what authored the composition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.)
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> (Who and/or what authored the composition)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.)
     * </p> 
	 */
	public Composition setAuthor(java.util.List<ResourceReferenceDt> theValue) {
		myAuthor = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>author</b> (Who and/or what authored the composition)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.)
     * </p> 
	 */
	public ResourceReferenceDt addAuthor() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAuthor().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>attester</b> (Attests to accuracy of composition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A participant who has attested to the accuracy of the composition/document
     * </p> 
	 */
	public java.util.List<Attester> getAttester() {  
		if (myAttester == null) {
			myAttester = new java.util.ArrayList<Attester>();
		}
		return myAttester;
	}

	/**
	 * Sets the value(s) for <b>attester</b> (Attests to accuracy of composition)
	 *
     * <p>
     * <b>Definition:</b>
     * A participant who has attested to the accuracy of the composition/document
     * </p> 
	 */
	public Composition setAttester(java.util.List<Attester> theValue) {
		myAttester = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>attester</b> (Attests to accuracy of composition)
	 *
     * <p>
     * <b>Definition:</b>
     * A participant who has attested to the accuracy of the composition/document
     * </p> 
	 */
	public Attester addAttester() {
		Attester newType = new Attester();
		getAttester().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>attester</b> (Attests to accuracy of composition),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A participant who has attested to the accuracy of the composition/document
     * </p> 
	 */
	public Attester getAttesterFirstRep() {
		if (getAttester().isEmpty()) {
			return addAttester();
		}
		return getAttester().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>custodian</b> (Org which maintains the composition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information
     * </p> 
	 */
	public ResourceReferenceDt getCustodian() {  
		if (myCustodian == null) {
			myCustodian = new ResourceReferenceDt();
		}
		return myCustodian;
	}

	/**
	 * Sets the value(s) for <b>custodian</b> (Org which maintains the composition)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information
     * </p> 
	 */
	public Composition setCustodian(ResourceReferenceDt theValue) {
		myCustodian = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>event</b> (The clinical event/act/item being documented).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The main event/act/item, such as a colonoscopy or an appendectomy, being documented
     * </p> 
	 */
	public Event getEvent() {  
		if (myEvent == null) {
			myEvent = new Event();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (The clinical event/act/item being documented)
	 *
     * <p>
     * <b>Definition:</b>
     * The main event/act/item, such as a colonoscopy or an appendectomy, being documented
     * </p> 
	 */
	public Composition setEvent(Event theValue) {
		myEvent = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>encounter</b> (Context of the conposition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the clinical encounter or type of care this documentation is associated with.
     * </p> 
	 */
	public ResourceReferenceDt getEncounter() {  
		if (myEncounter == null) {
			myEncounter = new ResourceReferenceDt();
		}
		return myEncounter;
	}

	/**
	 * Sets the value(s) for <b>encounter</b> (Context of the conposition)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the clinical encounter or type of care this documentation is associated with.
     * </p> 
	 */
	public Composition setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>section</b> (Composition is broken into sections).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The root of the sections that make up the composition
     * </p> 
	 */
	public java.util.List<Section> getSection() {  
		if (mySection == null) {
			mySection = new java.util.ArrayList<Section>();
		}
		return mySection;
	}

	/**
	 * Sets the value(s) for <b>section</b> (Composition is broken into sections)
	 *
     * <p>
     * <b>Definition:</b>
     * The root of the sections that make up the composition
     * </p> 
	 */
	public Composition setSection(java.util.List<Section> theValue) {
		mySection = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>section</b> (Composition is broken into sections)
	 *
     * <p>
     * <b>Definition:</b>
     * The root of the sections that make up the composition
     * </p> 
	 */
	public Section addSection() {
		Section newType = new Section();
		getSection().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>section</b> (Composition is broken into sections),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The root of the sections that make up the composition
     * </p> 
	 */
	public Section getSectionFirstRep() {
		if (getSection().isEmpty()) {
			return addSection();
		}
		return getSection().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Composition.attester</b> (Attests to accuracy of composition)
	 *
     * <p>
     * <b>Definition:</b>
     * A participant who has attested to the accuracy of the composition/document
     * </p> 
	 */
	@Block()	
	public static class Attester extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="mode", type=CodeDt.class, order=0, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="personal | professional | legal | official",
		formalDefinition="The type of attestation the authenticator offers"
	)
	private java.util.List<BoundCodeDt<CompositionAttestationModeEnum>> myMode;
	
	@Child(name="time", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="When composition attested",
		formalDefinition="When composition was attested by the party"
	)
	private DateTimeDt myTime;
	
	@Child(name="party", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Who attested the composition",
		formalDefinition="Who attested the composition in the specified way"
	)
	private ResourceReferenceDt myParty;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMode,  myTime,  myParty);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMode, myTime, myParty);
	}

	/**
	 * Gets the value(s) for <b>mode</b> (personal | professional | legal | official).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public java.util.List<BoundCodeDt<CompositionAttestationModeEnum>> getMode() {  
		if (myMode == null) {
			myMode = new java.util.ArrayList<BoundCodeDt<CompositionAttestationModeEnum>>();
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (personal | professional | legal | official)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public Attester setMode(java.util.List<BoundCodeDt<CompositionAttestationModeEnum>> theValue) {
		myMode = theValue;
		return this;
	}

	/**
	 * Add a value for <b>mode</b> (personal | professional | legal | official) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public BoundCodeDt<CompositionAttestationModeEnum> addMode(CompositionAttestationModeEnum theValue) {
		BoundCodeDt<CompositionAttestationModeEnum> retVal = new BoundCodeDt<CompositionAttestationModeEnum>(CompositionAttestationModeEnum.VALUESET_BINDER, theValue);
		getMode().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>mode</b> (personal | professional | legal | official),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public BoundCodeDt<CompositionAttestationModeEnum> getModeFirstRep() {
		if (getMode().size() == 0) {
			addMode();
		}
		return getMode().get(0);
	}

	/**
	 * Add a value for <b>mode</b> (personal | professional | legal | official)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public BoundCodeDt<CompositionAttestationModeEnum> addMode() {
		BoundCodeDt<CompositionAttestationModeEnum> retVal = new BoundCodeDt<CompositionAttestationModeEnum>(CompositionAttestationModeEnum.VALUESET_BINDER);
		getMode().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>mode</b> (personal | professional | legal | official)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public Attester setMode(CompositionAttestationModeEnum theValue) {
		getMode().clear();
		addMode(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>time</b> (When composition attested).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When composition was attested by the party
     * </p> 
	 */
	public DateTimeDt getTime() {  
		if (myTime == null) {
			myTime = new DateTimeDt();
		}
		return myTime;
	}

	/**
	 * Sets the value(s) for <b>time</b> (When composition attested)
	 *
     * <p>
     * <b>Definition:</b>
     * When composition was attested by the party
     * </p> 
	 */
	public Attester setTime(DateTimeDt theValue) {
		myTime = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>time</b> (When composition attested)
	 *
     * <p>
     * <b>Definition:</b>
     * When composition was attested by the party
     * </p> 
	 */
	public Attester setTimeWithSecondsPrecision( Date theDate) {
		myTime = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>time</b> (When composition attested)
	 *
     * <p>
     * <b>Definition:</b>
     * When composition was attested by the party
     * </p> 
	 */
	public Attester setTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>party</b> (Who attested the composition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who attested the composition in the specified way
     * </p> 
	 */
	public ResourceReferenceDt getParty() {  
		if (myParty == null) {
			myParty = new ResourceReferenceDt();
		}
		return myParty;
	}

	/**
	 * Sets the value(s) for <b>party</b> (Who attested the composition)
	 *
     * <p>
     * <b>Definition:</b>
     * Who attested the composition in the specified way
     * </p> 
	 */
	public Attester setParty(ResourceReferenceDt theValue) {
		myParty = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>Composition.event</b> (The clinical event/act/item being documented)
	 *
     * <p>
     * <b>Definition:</b>
     * The main event/act/item, such as a colonoscopy or an appendectomy, being documented
     * </p> 
	 */
	@Block()	
	public static class Event extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Code(s) that apply to the event being documented",
		formalDefinition="This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act."
	)
	private java.util.List<CodeableConceptDt> myCode;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="The period covered by the documentation",
		formalDefinition="The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time"
	)
	private PeriodDt myPeriod;
	
	@Child(name="detail", order=2, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="Full details for the event(s) the composition consents",
		formalDefinition="Full details for the event(s) the composition/documentation consents"
	)
	private java.util.List<ResourceReferenceDt> myDetail;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myPeriod,  myDetail);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myPeriod, myDetail);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Code(s) that apply to the event being documented).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getCode() {  
		if (myCode == null) {
			myCode = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Code(s) that apply to the event being documented)
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act.
     * </p> 
	 */
	public Event setCode(java.util.List<CodeableConceptDt> theValue) {
		myCode = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>code</b> (Code(s) that apply to the event being documented)
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act.
     * </p> 
	 */
	public CodeableConceptDt addCode() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getCode().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>code</b> (Code(s) that apply to the event being documented),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act.
     * </p> 
	 */
	public CodeableConceptDt getCodeFirstRep() {
		if (getCode().isEmpty()) {
			return addCode();
		}
		return getCode().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>period</b> (The period covered by the documentation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (The period covered by the documentation)
	 *
     * <p>
     * <b>Definition:</b>
     * The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time
     * </p> 
	 */
	public Event setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>detail</b> (Full details for the event(s) the composition consents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Full details for the event(s) the composition/documentation consents
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDetail() {  
		if (myDetail == null) {
			myDetail = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> (Full details for the event(s) the composition consents)
	 *
     * <p>
     * <b>Definition:</b>
     * Full details for the event(s) the composition/documentation consents
     * </p> 
	 */
	public Event setDetail(java.util.List<ResourceReferenceDt> theValue) {
		myDetail = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>detail</b> (Full details for the event(s) the composition consents)
	 *
     * <p>
     * <b>Definition:</b>
     * Full details for the event(s) the composition/documentation consents
     * </p> 
	 */
	public ResourceReferenceDt addDetail() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getDetail().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>Composition.section</b> (Composition is broken into sections)
	 *
     * <p>
     * <b>Definition:</b>
     * The root of the sections that make up the composition
     * </p> 
	 */
	@Block()	
	public static class Section extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="title", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Label for section",
		formalDefinition="The heading for this particular section.  This will be part of the rendered content for the document."
	)
	private StringDt myTitle;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Classification of section (recommended)",
		formalDefinition="A code identifying the kind of content contained within the section"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="subject", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="If section different to composition",
		formalDefinition="Identifies the primary subject of the section."
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="content", order=3, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="The actual data for the section",
		formalDefinition="Identifies the discrete data that provides the content for the section."
	)
	private ResourceReferenceDt myContent;
	
	@Child(name="section", type=Section.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Nested Section",
		formalDefinition="A nested sub-section within this section"
	)
	private java.util.List<Section> mySection;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myTitle,  myCode,  mySubject,  myContent,  mySection);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myTitle, myCode, mySubject, myContent, mySection);
	}

	/**
	 * Gets the value(s) for <b>title</b> (Label for section).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The heading for this particular section.  This will be part of the rendered content for the document.
     * </p> 
	 */
	public StringDt getTitle() {  
		if (myTitle == null) {
			myTitle = new StringDt();
		}
		return myTitle;
	}

	/**
	 * Sets the value(s) for <b>title</b> (Label for section)
	 *
     * <p>
     * <b>Definition:</b>
     * The heading for this particular section.  This will be part of the rendered content for the document.
     * </p> 
	 */
	public Section setTitle(StringDt theValue) {
		myTitle = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>title</b> (Label for section)
	 *
     * <p>
     * <b>Definition:</b>
     * The heading for this particular section.  This will be part of the rendered content for the document.
     * </p> 
	 */
	public Section setTitle( String theString) {
		myTitle = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Classification of section (recommended)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code identifying the kind of content contained within the section
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Classification of section (recommended))
	 *
     * <p>
     * <b>Definition:</b>
     * A code identifying the kind of content contained within the section
     * </p> 
	 */
	public Section setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> (If section different to composition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the primary subject of the section.
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (If section different to composition)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the primary subject of the section.
     * </p> 
	 */
	public Section setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>content</b> (The actual data for the section).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the discrete data that provides the content for the section.
     * </p> 
	 */
	public ResourceReferenceDt getContent() {  
		if (myContent == null) {
			myContent = new ResourceReferenceDt();
		}
		return myContent;
	}

	/**
	 * Sets the value(s) for <b>content</b> (The actual data for the section)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the discrete data that provides the content for the section.
     * </p> 
	 */
	public Section setContent(ResourceReferenceDt theValue) {
		myContent = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>section</b> (Nested Section).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A nested sub-section within this section
     * </p> 
	 */
	public java.util.List<Section> getSection() {  
		if (mySection == null) {
			mySection = new java.util.ArrayList<Section>();
		}
		return mySection;
	}

	/**
	 * Sets the value(s) for <b>section</b> (Nested Section)
	 *
     * <p>
     * <b>Definition:</b>
     * A nested sub-section within this section
     * </p> 
	 */
	public Section setSection(java.util.List<Section> theValue) {
		mySection = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>section</b> (Nested Section)
	 *
     * <p>
     * <b>Definition:</b>
     * A nested sub-section within this section
     * </p> 
	 */
	public Section addSection() {
		Section newType = new Section();
		getSection().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>section</b> (Nested Section),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A nested sub-section within this section
     * </p> 
	 */
	public Section getSectionFirstRep() {
		if (getSection().isEmpty()) {
			return addSection();
		}
		return getSection().get(0); 
	}
  

	}




}