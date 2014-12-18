















package ca.uhn.fhir.model.dev.resource;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.rest.gclient.*;

import ca.uhn.fhir.model.dev.composite.AddressDt;
import ca.uhn.fhir.model.dev.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dev.valueset.AdmitSourceEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskCategoryEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskCertaintyEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskCriticalityEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskSeverityEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskStatusEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskTypeEnum;
import ca.uhn.fhir.model.dev.valueset.AlertStatusEnum;
import ca.uhn.fhir.model.dev.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dev.valueset.AnimalSpeciesEnum;
import ca.uhn.fhir.model.dev.valueset.AnswerFormatEnum;
import ca.uhn.fhir.model.dev.resource.Appointment;
import ca.uhn.fhir.model.dev.valueset.AppointmentStatusEnum;
import ca.uhn.fhir.model.dev.composite.AttachmentDt;
import ca.uhn.fhir.model.dev.resource.CarePlan;
import ca.uhn.fhir.model.dev.valueset.CarePlanActivityCategoryEnum;
import ca.uhn.fhir.model.dev.valueset.CarePlanActivityStatusEnum;
import ca.uhn.fhir.model.dev.valueset.CarePlanGoalStatusEnum;
import ca.uhn.fhir.model.dev.valueset.CarePlanStatusEnum;
import ca.uhn.fhir.model.dev.valueset.CausalityExpectationEnum;
import ca.uhn.fhir.model.dev.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.valueset.CompositionAttestationModeEnum;
import ca.uhn.fhir.model.dev.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dev.valueset.ConceptMapEquivalenceEnum;
import ca.uhn.fhir.model.dev.resource.Condition;
import ca.uhn.fhir.model.dev.valueset.ConditionStatusEnum;
import ca.uhn.fhir.model.dev.valueset.ConformanceEventModeEnum;
import ca.uhn.fhir.model.dev.valueset.ConformanceStatementStatusEnum;
import ca.uhn.fhir.model.dev.composite.ContactPointDt;
import ca.uhn.fhir.model.dev.resource.Contract;
import ca.uhn.fhir.model.dev.valueset.ContractSubtypeCodesEnum;
import ca.uhn.fhir.model.dev.valueset.ContractTermTypeCodesEnum;
import ca.uhn.fhir.model.dev.valueset.ContractTypeCodesEnum;
import ca.uhn.fhir.model.dev.valueset.DataAbsentReasonEnum;
import ca.uhn.fhir.model.dev.resource.Device;
import ca.uhn.fhir.model.dev.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dev.valueset.DiagnosticOrderPriorityEnum;
import ca.uhn.fhir.model.dev.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dev.resource.DiagnosticReport;
import ca.uhn.fhir.model.dev.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dev.resource.DocumentManifest;
import ca.uhn.fhir.model.dev.valueset.DocumentModeEnum;
import ca.uhn.fhir.model.dev.resource.DocumentReference;
import ca.uhn.fhir.model.dev.valueset.DocumentReferenceStatusEnum;
import ca.uhn.fhir.model.dev.valueset.DocumentRelationshipTypeEnum;
import ca.uhn.fhir.model.dev.composite.ElementDefinitionDt;
import ca.uhn.fhir.model.dev.resource.Encounter;
import ca.uhn.fhir.model.dev.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dev.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dev.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dev.valueset.EncounterTypeEnum;
import ca.uhn.fhir.model.dev.valueset.EnteralFormulaAdditiveTypeEnum;
import ca.uhn.fhir.model.dev.valueset.EnteralFormulaTypeEnum;
import ca.uhn.fhir.model.dev.resource.EpisodeOfCare;
import ca.uhn.fhir.model.dev.valueset.ExcludeFoodModifierEnum;
import ca.uhn.fhir.model.dev.valueset.ExposureTypeEnum;
import ca.uhn.fhir.model.dev.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dev.resource.FamilyHistory;
import ca.uhn.fhir.model.dev.valueset.FilterOperatorEnum;
import ca.uhn.fhir.model.dev.valueset.FluidConsistencyTypeEnum;
import ca.uhn.fhir.model.dev.valueset.FoodTypeEnum;
import ca.uhn.fhir.model.dev.resource.Group;
import ca.uhn.fhir.model.dev.valueset.GroupTypeEnum;
import ca.uhn.fhir.model.dev.valueset.HierarchicalRelationshipTypeEnum;
import ca.uhn.fhir.model.dev.composite.HumanNameDt;
import ca.uhn.fhir.model.dev.composite.IdentifierDt;
import ca.uhn.fhir.model.dev.valueset.ImagingModalityEnum;
import ca.uhn.fhir.model.dev.resource.ImagingStudy;
import ca.uhn.fhir.model.dev.resource.Immunization;
import ca.uhn.fhir.model.dev.valueset.ImmunizationReasonCodesEnum;
import ca.uhn.fhir.model.dev.resource.ImmunizationRecommendation;
import ca.uhn.fhir.model.dev.valueset.ImmunizationRecommendationDateCriterionCodesEnum;
import ca.uhn.fhir.model.dev.valueset.ImmunizationRecommendationStatusCodesEnum;
import ca.uhn.fhir.model.dev.valueset.ImmunizationRouteCodesEnum;
import ca.uhn.fhir.model.dev.valueset.InstanceAvailabilityEnum;
import ca.uhn.fhir.model.dev.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dev.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dev.valueset.LinkTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ListModeEnum;
import ca.uhn.fhir.model.dev.resource.Location;
import ca.uhn.fhir.model.dev.valueset.LocationModeEnum;
import ca.uhn.fhir.model.dev.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.dev.valueset.LocationTypeEnum;
import ca.uhn.fhir.model.dev.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dev.resource.Media;
import ca.uhn.fhir.model.dev.valueset.MediaTypeEnum;
import ca.uhn.fhir.model.dev.resource.Medication;
import ca.uhn.fhir.model.dev.resource.MedicationAdministration;
import ca.uhn.fhir.model.dev.valueset.MedicationAdministrationStatusEnum;
import ca.uhn.fhir.model.dev.resource.MedicationDispense;
import ca.uhn.fhir.model.dev.valueset.MedicationDispenseStatusEnum;
import ca.uhn.fhir.model.dev.valueset.MedicationKindEnum;
import ca.uhn.fhir.model.dev.resource.MedicationPrescription;
import ca.uhn.fhir.model.dev.valueset.MedicationPrescriptionStatusEnum;
import ca.uhn.fhir.model.dev.resource.MedicationStatement;
import ca.uhn.fhir.model.dev.valueset.MessageEventEnum;
import ca.uhn.fhir.model.dev.valueset.MessageSignificanceCategoryEnum;
import ca.uhn.fhir.model.dev.valueset.MessageTransportEnum;
import ca.uhn.fhir.model.dev.resource.Microarray;
import ca.uhn.fhir.model.dev.valueset.ModalityEnum;
import ca.uhn.fhir.model.dev.resource.Namespace;
import ca.uhn.fhir.model.dev.valueset.NamespaceIdentifierTypeEnum;
import ca.uhn.fhir.model.dev.valueset.NamespaceStatusEnum;
import ca.uhn.fhir.model.dev.valueset.NamespaceTypeEnum;
import ca.uhn.fhir.model.dev.valueset.NutrientModifierEnum;
import ca.uhn.fhir.model.dev.valueset.NutritionOrderStatusEnum;
import ca.uhn.fhir.model.dev.resource.Observation;
import ca.uhn.fhir.model.dev.valueset.ObservationInterpretationCodesEnum;
import ca.uhn.fhir.model.dev.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dev.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dev.resource.OperationDefinition;
import ca.uhn.fhir.model.dev.valueset.OperationKindEnum;
import ca.uhn.fhir.model.dev.resource.OperationOutcome;
import ca.uhn.fhir.model.dev.valueset.OperationParameterUseEnum;
import ca.uhn.fhir.model.dev.resource.Order;
import ca.uhn.fhir.model.dev.valueset.OrderOutcomeStatusEnum;
import ca.uhn.fhir.model.dev.resource.Organization;
import ca.uhn.fhir.model.dev.valueset.OrganizationTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ParticipantRequiredEnum;
import ca.uhn.fhir.model.dev.valueset.ParticipantStatusEnum;
import ca.uhn.fhir.model.dev.valueset.ParticipantTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ParticipationStatusEnum;
import ca.uhn.fhir.model.dev.resource.Patient;
import ca.uhn.fhir.model.dev.valueset.PatientRelationshipTypeEnum;
import ca.uhn.fhir.model.dev.composite.PeriodDt;
import ca.uhn.fhir.model.dev.resource.Practitioner;
import ca.uhn.fhir.model.dev.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.dev.valueset.PractitionerSpecialtyEnum;
import ca.uhn.fhir.model.dev.valueset.PriorityCodesEnum;
import ca.uhn.fhir.model.dev.resource.Procedure;
import ca.uhn.fhir.model.dev.valueset.ProcedureRelationshipTypeEnum;
import ca.uhn.fhir.model.dev.resource.Profile;
import ca.uhn.fhir.model.dev.valueset.ProvenanceEntityRoleEnum;
import ca.uhn.fhir.model.dev.composite.QuantityDt;
import ca.uhn.fhir.model.dev.valueset.QueryOutcomeEnum;
import ca.uhn.fhir.model.dev.resource.Questionnaire;
import ca.uhn.fhir.model.dev.valueset.QuestionnaireAnswersStatusEnum;
import ca.uhn.fhir.model.dev.valueset.QuestionnaireStatusEnum;
import ca.uhn.fhir.model.dev.composite.RangeDt;
import ca.uhn.fhir.model.dev.composite.RatioDt;
import ca.uhn.fhir.model.dev.valueset.ReactionSeverityEnum;
import ca.uhn.fhir.model.dev.resource.ReferralRequest;
import ca.uhn.fhir.model.dev.valueset.ReferralStatusEnum;
import ca.uhn.fhir.model.dev.resource.RelatedPerson;
import ca.uhn.fhir.model.dev.valueset.ResourceProfileStatusEnum;
import ca.uhn.fhir.model.dev.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ResponseTypeEnum;
import ca.uhn.fhir.model.dev.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dev.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dev.composite.SampledDataDt;
import ca.uhn.fhir.model.dev.resource.Schedule;
import ca.uhn.fhir.model.dev.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventActionEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventObjectRoleEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventObjectTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventOutcomeEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventParticipantNetworkTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventSourceTypeEnum;
import ca.uhn.fhir.model.dev.resource.SecurityGroup;
import ca.uhn.fhir.model.dev.resource.SequencingAnalysis;
import ca.uhn.fhir.model.dev.resource.SequencingLab;
import ca.uhn.fhir.model.dev.resource.Slot;
import ca.uhn.fhir.model.dev.valueset.SlotStatusEnum;
import ca.uhn.fhir.model.dev.resource.Specimen;
import ca.uhn.fhir.model.dev.valueset.SpecimenCollectionMethodEnum;
import ca.uhn.fhir.model.dev.valueset.SpecimenTreatmentProcedureEnum;
import ca.uhn.fhir.model.dev.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.model.dev.resource.Substance;
import ca.uhn.fhir.model.dev.valueset.SubstanceTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SupplementTypeEnum;
import ca.uhn.fhir.model.dev.resource.Supply;
import ca.uhn.fhir.model.dev.valueset.SupplyDispenseStatusEnum;
import ca.uhn.fhir.model.dev.valueset.SupplyItemTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SupplyStatusEnum;
import ca.uhn.fhir.model.dev.valueset.SystemRestfulInteractionEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dev.valueset.TextureModifierEnum;
import ca.uhn.fhir.model.dev.composite.TimingDt;
import ca.uhn.fhir.model.dev.valueset.TypeRestfulInteractionEnum;
import ca.uhn.fhir.model.dev.resource.ValueSet;
import ca.uhn.fhir.model.dev.valueset.ValueSetStatusEnum;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dev.composite.AgeDt;
import ca.uhn.fhir.model.dev.composite.BoundCodeableConceptDt;
import ca.uhn.fhir.model.dev.composite.DurationDt;
import ca.uhn.fhir.model.dev.composite.MoneyDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.OidDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.TimeDt;
import ca.uhn.fhir.model.primitive.UriDt;


/**
 * HAPI/FHIR <b>Contract</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A formal agreement between parties regarding the conduct of business, exchange of information or other matters.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Contract">http://hl7.org/fhir/profiles/Contract</a> 
 * </p>
 *
 */
@ResourceDef(name="Contract", profile="http://hl7.org/fhir/profiles/Contract", id="contract")
public class Contract 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The identity of the target of the contract</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Contract.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Contract.subject", description="The identity of the target of the contract", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The identity of the target of the contract</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Contract.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of the target of the contract (if a patient)</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Contract.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Contract.subject", description="The identity of the target of the contract (if a patient)", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of the target of the contract (if a patient)</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Contract.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Contract.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Contract.subject");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Unique Id for this contract."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=1, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Who and/or what this is about: typically Patient, Organization, property."
	)
	private java.util.List<ResourceReferenceDt> mySubject;
	
	@Child(name="authority", order=2, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc."
	)
	private java.util.List<ResourceReferenceDt> myAuthority;
	
	@Child(name="domain", order=3, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations."
	)
	private java.util.List<ResourceReferenceDt> myDomain;
	
	@Child(name="type", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="ContractType",
		formalDefinition="Type of contract (Privacy-Security, Agreement, Insurance)"
	)
	private BoundCodeableConceptDt<ContractTypeCodesEnum> myType;
	
	@Child(name="subtype", type=CodeableConceptDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ContractSubtype",
		formalDefinition="More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat)"
	)
	private java.util.List<BoundCodeableConceptDt<ContractSubtypeCodesEnum>> mySubtype;
	
	@Child(name="issued", type=DateTimeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="When this was issued."
	)
	private DateTimeDt myIssued;
	
	@Child(name="applies", type=PeriodDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Relevant time/time-period when applicable"
	)
	private PeriodDt myApplies;
	
	@Child(name="quantity", type=QuantityDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The number of repetitions of a service or product."
	)
	private QuantityDt myQuantity;
	
	@Child(name="unitPrice", type=MoneyDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The unit price product."
	)
	private MoneyDt myUnitPrice;
	
	@Child(name="factor", type=DecimalDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount."
	)
	private DecimalDt myFactor;
	
	@Child(name="points", type=DecimalDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point."
	)
	private DecimalDt myPoints;
	
	@Child(name="net", type=MoneyDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied."
	)
	private MoneyDt myNet;
	
	@Child(name="author", order=13, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Contract author or responsible party."
	)
	private java.util.List<ResourceReferenceDt> myAuthor;
	
	@Child(name="grantor", order=14, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dev.resource.Organization.class, 		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="First Party to the contract, may be the party who confers or delegates the rights defined in the contract."
	)
	private java.util.List<ResourceReferenceDt> myGrantor;
	
	@Child(name="grantee", order=15, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dev.resource.Organization.class, 		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated."
	)
	private java.util.List<ResourceReferenceDt> myGrantee;
	
	@Child(name="witness", order=16, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Who witnesses the contract."
	)
	private java.util.List<ResourceReferenceDt> myWitness;
	
	@Child(name="executor", order=17, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dev.resource.Organization.class, 		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="First Party to the contract, may be the party who confers or delegates the rights defined in the contract."
	)
	private java.util.List<ResourceReferenceDt> myExecutor;
	
	@Child(name="notary", order=18, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dev.resource.Organization.class, 		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="First Party to the contract, may be the party who confers or delegates the rights defined in the contract."
	)
	private java.util.List<ResourceReferenceDt> myNotary;
	
	@Child(name="signer", order=19, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="List or contract signatures."
	)
	private java.util.List<Signer> mySigner;
	
	@Child(name="term", order=20, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time."
	)
	private java.util.List<Term> myTerm;
	
	@Child(name="binding", type=AttachmentDt.class, order=21, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Legally binding contract"
	)
	private AttachmentDt myBinding;
	
	@Child(name="bindingDateTime", type=DateTimeDt.class, order=22, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Relevant time/time-period when applicable"
	)
	private DateTimeDt myBindingDateTime;
	
	@Child(name="friendly", type=AttachmentDt.class, order=23, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Friendly Human readable form (might be a reference to the UI used to capture the contract)"
	)
	private java.util.List<AttachmentDt> myFriendly;
	
	@Child(name="friendlyDateTime", type=DateTimeDt.class, order=24, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Relevant time/time-period when applicable"
	)
	private DateTimeDt myFriendlyDateTime;
	
	@Child(name="legal", type=AttachmentDt.class, order=25, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Legal text in Human readable form"
	)
	private java.util.List<AttachmentDt> myLegal;
	
	@Child(name="legalDateTime", type=DateTimeDt.class, order=26, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Relevant time/time-period when applicable"
	)
	private DateTimeDt myLegalDateTime;
	
	@Child(name="rule", type=AttachmentDt.class, order=27, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Computable Policy rules (e.g. XACML, DKAL, SecPal)"
	)
	private java.util.List<AttachmentDt> myRule;
	
	@Child(name="ruleDateTime", type=DateTimeDt.class, order=28, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Relevant time/time-period when applicable"
	)
	private DateTimeDt myRuleDateTime;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  mySubject,  myAuthority,  myDomain,  myType,  mySubtype,  myIssued,  myApplies,  myQuantity,  myUnitPrice,  myFactor,  myPoints,  myNet,  myAuthor,  myGrantor,  myGrantee,  myWitness,  myExecutor,  myNotary,  mySigner,  myTerm,  myBinding,  myBindingDateTime,  myFriendly,  myFriendlyDateTime,  myLegal,  myLegalDateTime,  myRule,  myRuleDateTime);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, mySubject, myAuthority, myDomain, myType, mySubtype, myIssued, myApplies, myQuantity, myUnitPrice, myFactor, myPoints, myNet, myAuthor, myGrantor, myGrantee, myWitness, myExecutor, myNotary, mySigner, myTerm, myBinding, myBindingDateTime, myFriendly, myFriendlyDateTime, myLegal, myLegalDateTime, myRule, myRuleDateTime);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique Id for this contract.
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Unique Id for this contract.
     * </p> 
	 */
	public Contract setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Unique Id for this contract.
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique Id for this contract.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who and/or what this is about: typically Patient, Organization, property.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSubject() {  
		if (mySubject == null) {
			mySubject = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who and/or what this is about: typically Patient, Organization, property.
     * </p> 
	 */
	public Contract setSubject(java.util.List<ResourceReferenceDt> theValue) {
		mySubject = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>subject</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who and/or what this is about: typically Patient, Organization, property.
     * </p> 
	 */
	public ResourceReferenceDt addSubject() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSubject().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>authority</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAuthority() {  
		if (myAuthority == null) {
			myAuthority = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAuthority;
	}

	/**
	 * Sets the value(s) for <b>authority</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
     * </p> 
	 */
	public Contract setAuthority(java.util.List<ResourceReferenceDt> theValue) {
		myAuthority = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>authority</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
     * </p> 
	 */
	public ResourceReferenceDt addAuthority() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAuthority().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>domain</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDomain() {  
		if (myDomain == null) {
			myDomain = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDomain;
	}

	/**
	 * Sets the value(s) for <b>domain</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations.
     * </p> 
	 */
	public Contract setDomain(java.util.List<ResourceReferenceDt> theValue) {
		myDomain = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>domain</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations.
     * </p> 
	 */
	public ResourceReferenceDt addDomain() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getDomain().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>type</b> (ContractType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of contract (Privacy-Security, Agreement, Insurance)
     * </p> 
	 */
	public BoundCodeableConceptDt<ContractTypeCodesEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<ContractTypeCodesEnum>(ContractTypeCodesEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (ContractType)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of contract (Privacy-Security, Agreement, Insurance)
     * </p> 
	 */
	public Contract setType(BoundCodeableConceptDt<ContractTypeCodesEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (ContractType)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of contract (Privacy-Security, Agreement, Insurance)
     * </p> 
	 */
	public Contract setType(ContractTypeCodesEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subtype</b> (ContractSubtype).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat)
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<ContractSubtypeCodesEnum>> getSubtype() {  
		if (mySubtype == null) {
			mySubtype = new java.util.ArrayList<BoundCodeableConceptDt<ContractSubtypeCodesEnum>>();
		}
		return mySubtype;
	}

	/**
	 * Sets the value(s) for <b>subtype</b> (ContractSubtype)
	 *
     * <p>
     * <b>Definition:</b>
     * More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat)
     * </p> 
	 */
	public Contract setSubtype(java.util.List<BoundCodeableConceptDt<ContractSubtypeCodesEnum>> theValue) {
		mySubtype = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>subtype</b> (ContractSubtype) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat)
     * </p> 
	 */
	public BoundCodeableConceptDt<ContractSubtypeCodesEnum> addSubtype(ContractSubtypeCodesEnum theValue) {
		BoundCodeableConceptDt<ContractSubtypeCodesEnum> retVal = new BoundCodeableConceptDt<ContractSubtypeCodesEnum>(ContractSubtypeCodesEnum.VALUESET_BINDER, theValue);
		getSubtype().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>subtype</b> (ContractSubtype),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat)
     * </p> 
	 */
	public BoundCodeableConceptDt<ContractSubtypeCodesEnum> getSubtypeFirstRep() {
		if (getSubtype().size() == 0) {
			addSubtype();
		}
		return getSubtype().get(0);
	}

	/**
	 * Add a value for <b>subtype</b> (ContractSubtype)
	 *
     * <p>
     * <b>Definition:</b>
     * More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat)
     * </p> 
	 */
	public BoundCodeableConceptDt<ContractSubtypeCodesEnum> addSubtype() {
		BoundCodeableConceptDt<ContractSubtypeCodesEnum> retVal = new BoundCodeableConceptDt<ContractSubtypeCodesEnum>(ContractSubtypeCodesEnum.VALUESET_BINDER);
		getSubtype().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>subtype</b> (ContractSubtype)
	 *
     * <p>
     * <b>Definition:</b>
     * More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat)
     * </p> 
	 */
	public Contract setSubtype(ContractSubtypeCodesEnum theValue) {
		getSubtype().clear();
		addSubtype(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>issued</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When this was issued.
     * </p> 
	 */
	public DateTimeDt getIssuedElement() {  
		if (myIssued == null) {
			myIssued = new DateTimeDt();
		}
		return myIssued;
	}

	
	/**
	 * Gets the value(s) for <b>issued</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When this was issued.
     * </p> 
	 */
	public Date getIssued() {  
		return getIssuedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When this was issued.
     * </p> 
	 */
	public Contract setIssued(DateTimeDt theValue) {
		myIssued = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When this was issued.
     * </p> 
	 */
	public Contract setIssued( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myIssued = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When this was issued.
     * </p> 
	 */
	public Contract setIssuedWithSecondsPrecision( Date theDate) {
		myIssued = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>applies</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public PeriodDt getApplies() {  
		if (myApplies == null) {
			myApplies = new PeriodDt();
		}
		return myApplies;
	}

	/**
	 * Sets the value(s) for <b>applies</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setApplies(PeriodDt theValue) {
		myApplies = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of repetitions of a service or product.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The number of repetitions of a service or product.
     * </p> 
	 */
	public Contract setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>unitPrice</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The unit price product.
     * </p> 
	 */
	public MoneyDt getUnitPrice() {  
		if (myUnitPrice == null) {
			myUnitPrice = new MoneyDt();
		}
		return myUnitPrice;
	}

	/**
	 * Sets the value(s) for <b>unitPrice</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The unit price product.
     * </p> 
	 */
	public Contract setUnitPrice(MoneyDt theValue) {
		myUnitPrice = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>factor</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public DecimalDt getFactorElement() {  
		if (myFactor == null) {
			myFactor = new DecimalDt();
		}
		return myFactor;
	}

	
	/**
	 * Gets the value(s) for <b>factor</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public BigDecimal getFactor() {  
		return getFactorElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>factor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public Contract setFactor(DecimalDt theValue) {
		myFactor = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>factor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public Contract setFactor( long theValue) {
		myFactor = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>factor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public Contract setFactor( double theValue) {
		myFactor = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>factor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public Contract setFactor( java.math.BigDecimal theValue) {
		myFactor = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>points</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public DecimalDt getPointsElement() {  
		if (myPoints == null) {
			myPoints = new DecimalDt();
		}
		return myPoints;
	}

	
	/**
	 * Gets the value(s) for <b>points</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public BigDecimal getPoints() {  
		return getPointsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>points</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public Contract setPoints(DecimalDt theValue) {
		myPoints = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>points</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public Contract setPoints( long theValue) {
		myPoints = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>points</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public Contract setPoints( double theValue) {
		myPoints = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>points</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public Contract setPoints( java.math.BigDecimal theValue) {
		myPoints = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>net</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
     * </p> 
	 */
	public MoneyDt getNet() {  
		if (myNet == null) {
			myNet = new MoneyDt();
		}
		return myNet;
	}

	/**
	 * Sets the value(s) for <b>net</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
     * </p> 
	 */
	public Contract setNet(MoneyDt theValue) {
		myNet = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>author</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contract author or responsible party.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contract author or responsible party.
     * </p> 
	 */
	public Contract setAuthor(java.util.List<ResourceReferenceDt> theValue) {
		myAuthor = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>author</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contract author or responsible party.
     * </p> 
	 */
	public ResourceReferenceDt addAuthor() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAuthor().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>grantor</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getGrantor() {  
		if (myGrantor == null) {
			myGrantor = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myGrantor;
	}

	/**
	 * Sets the value(s) for <b>grantor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     * </p> 
	 */
	public Contract setGrantor(java.util.List<ResourceReferenceDt> theValue) {
		myGrantor = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>grantor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     * </p> 
	 */
	public ResourceReferenceDt addGrantor() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getGrantor().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>grantee</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getGrantee() {  
		if (myGrantee == null) {
			myGrantee = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myGrantee;
	}

	/**
	 * Sets the value(s) for <b>grantee</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.
     * </p> 
	 */
	public Contract setGrantee(java.util.List<ResourceReferenceDt> theValue) {
		myGrantee = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>grantee</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.
     * </p> 
	 */
	public ResourceReferenceDt addGrantee() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getGrantee().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>witness</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who witnesses the contract.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getWitness() {  
		if (myWitness == null) {
			myWitness = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myWitness;
	}

	/**
	 * Sets the value(s) for <b>witness</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who witnesses the contract.
     * </p> 
	 */
	public Contract setWitness(java.util.List<ResourceReferenceDt> theValue) {
		myWitness = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>witness</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who witnesses the contract.
     * </p> 
	 */
	public ResourceReferenceDt addWitness() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getWitness().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>executor</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getExecutor() {  
		if (myExecutor == null) {
			myExecutor = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myExecutor;
	}

	/**
	 * Sets the value(s) for <b>executor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     * </p> 
	 */
	public Contract setExecutor(java.util.List<ResourceReferenceDt> theValue) {
		myExecutor = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>executor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     * </p> 
	 */
	public ResourceReferenceDt addExecutor() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getExecutor().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>notary</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getNotary() {  
		if (myNotary == null) {
			myNotary = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myNotary;
	}

	/**
	 * Sets the value(s) for <b>notary</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     * </p> 
	 */
	public Contract setNotary(java.util.List<ResourceReferenceDt> theValue) {
		myNotary = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>notary</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     * </p> 
	 */
	public ResourceReferenceDt addNotary() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getNotary().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>signer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * List or contract signatures.
     * </p> 
	 */
	public java.util.List<Signer> getSigner() {  
		if (mySigner == null) {
			mySigner = new java.util.ArrayList<Signer>();
		}
		return mySigner;
	}

	/**
	 * Sets the value(s) for <b>signer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * List or contract signatures.
     * </p> 
	 */
	public Contract setSigner(java.util.List<Signer> theValue) {
		mySigner = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>signer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * List or contract signatures.
     * </p> 
	 */
	public Signer addSigner() {
		Signer newType = new Signer();
		getSigner().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>signer</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * List or contract signatures.
     * </p> 
	 */
	public Signer getSignerFirstRep() {
		if (getSigner().isEmpty()) {
			return addSigner();
		}
		return getSigner().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>term</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time.
     * </p> 
	 */
	public java.util.List<Term> getTerm() {  
		if (myTerm == null) {
			myTerm = new java.util.ArrayList<Term>();
		}
		return myTerm;
	}

	/**
	 * Sets the value(s) for <b>term</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time.
     * </p> 
	 */
	public Contract setTerm(java.util.List<Term> theValue) {
		myTerm = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>term</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time.
     * </p> 
	 */
	public Term addTerm() {
		Term newType = new Term();
		getTerm().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>term</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time.
     * </p> 
	 */
	public Term getTermFirstRep() {
		if (getTerm().isEmpty()) {
			return addTerm();
		}
		return getTerm().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>binding</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Legally binding contract
     * </p> 
	 */
	public AttachmentDt getBinding() {  
		if (myBinding == null) {
			myBinding = new AttachmentDt();
		}
		return myBinding;
	}

	/**
	 * Sets the value(s) for <b>binding</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Legally binding contract
     * </p> 
	 */
	public Contract setBinding(AttachmentDt theValue) {
		myBinding = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>bindingDateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public DateTimeDt getBindingDateTimeElement() {  
		if (myBindingDateTime == null) {
			myBindingDateTime = new DateTimeDt();
		}
		return myBindingDateTime;
	}

	
	/**
	 * Gets the value(s) for <b>bindingDateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Date getBindingDateTime() {  
		return getBindingDateTimeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>bindingDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setBindingDateTime(DateTimeDt theValue) {
		myBindingDateTime = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>bindingDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setBindingDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myBindingDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>bindingDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setBindingDateTimeWithSecondsPrecision( Date theDate) {
		myBindingDateTime = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>friendly</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Friendly Human readable form (might be a reference to the UI used to capture the contract)
     * </p> 
	 */
	public java.util.List<AttachmentDt> getFriendly() {  
		if (myFriendly == null) {
			myFriendly = new java.util.ArrayList<AttachmentDt>();
		}
		return myFriendly;
	}

	/**
	 * Sets the value(s) for <b>friendly</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Friendly Human readable form (might be a reference to the UI used to capture the contract)
     * </p> 
	 */
	public Contract setFriendly(java.util.List<AttachmentDt> theValue) {
		myFriendly = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>friendly</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Friendly Human readable form (might be a reference to the UI used to capture the contract)
     * </p> 
	 */
	public AttachmentDt addFriendly() {
		AttachmentDt newType = new AttachmentDt();
		getFriendly().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>friendly</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Friendly Human readable form (might be a reference to the UI used to capture the contract)
     * </p> 
	 */
	public AttachmentDt getFriendlyFirstRep() {
		if (getFriendly().isEmpty()) {
			return addFriendly();
		}
		return getFriendly().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>friendlyDateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public DateTimeDt getFriendlyDateTimeElement() {  
		if (myFriendlyDateTime == null) {
			myFriendlyDateTime = new DateTimeDt();
		}
		return myFriendlyDateTime;
	}

	
	/**
	 * Gets the value(s) for <b>friendlyDateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Date getFriendlyDateTime() {  
		return getFriendlyDateTimeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>friendlyDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setFriendlyDateTime(DateTimeDt theValue) {
		myFriendlyDateTime = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>friendlyDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setFriendlyDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myFriendlyDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>friendlyDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setFriendlyDateTimeWithSecondsPrecision( Date theDate) {
		myFriendlyDateTime = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>legal</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Legal text in Human readable form
     * </p> 
	 */
	public java.util.List<AttachmentDt> getLegal() {  
		if (myLegal == null) {
			myLegal = new java.util.ArrayList<AttachmentDt>();
		}
		return myLegal;
	}

	/**
	 * Sets the value(s) for <b>legal</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Legal text in Human readable form
     * </p> 
	 */
	public Contract setLegal(java.util.List<AttachmentDt> theValue) {
		myLegal = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>legal</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Legal text in Human readable form
     * </p> 
	 */
	public AttachmentDt addLegal() {
		AttachmentDt newType = new AttachmentDt();
		getLegal().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>legal</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Legal text in Human readable form
     * </p> 
	 */
	public AttachmentDt getLegalFirstRep() {
		if (getLegal().isEmpty()) {
			return addLegal();
		}
		return getLegal().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>legalDateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public DateTimeDt getLegalDateTimeElement() {  
		if (myLegalDateTime == null) {
			myLegalDateTime = new DateTimeDt();
		}
		return myLegalDateTime;
	}

	
	/**
	 * Gets the value(s) for <b>legalDateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Date getLegalDateTime() {  
		return getLegalDateTimeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>legalDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setLegalDateTime(DateTimeDt theValue) {
		myLegalDateTime = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>legalDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setLegalDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myLegalDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>legalDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setLegalDateTimeWithSecondsPrecision( Date theDate) {
		myLegalDateTime = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>rule</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Computable Policy rules (e.g. XACML, DKAL, SecPal)
     * </p> 
	 */
	public java.util.List<AttachmentDt> getRule() {  
		if (myRule == null) {
			myRule = new java.util.ArrayList<AttachmentDt>();
		}
		return myRule;
	}

	/**
	 * Sets the value(s) for <b>rule</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Computable Policy rules (e.g. XACML, DKAL, SecPal)
     * </p> 
	 */
	public Contract setRule(java.util.List<AttachmentDt> theValue) {
		myRule = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>rule</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Computable Policy rules (e.g. XACML, DKAL, SecPal)
     * </p> 
	 */
	public AttachmentDt addRule() {
		AttachmentDt newType = new AttachmentDt();
		getRule().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>rule</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Computable Policy rules (e.g. XACML, DKAL, SecPal)
     * </p> 
	 */
	public AttachmentDt getRuleFirstRep() {
		if (getRule().isEmpty()) {
			return addRule();
		}
		return getRule().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>ruleDateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public DateTimeDt getRuleDateTimeElement() {  
		if (myRuleDateTime == null) {
			myRuleDateTime = new DateTimeDt();
		}
		return myRuleDateTime;
	}

	
	/**
	 * Gets the value(s) for <b>ruleDateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Date getRuleDateTime() {  
		return getRuleDateTimeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>ruleDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setRuleDateTime(DateTimeDt theValue) {
		myRuleDateTime = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>ruleDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setRuleDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myRuleDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>ruleDateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when applicable
     * </p> 
	 */
	public Contract setRuleDateTimeWithSecondsPrecision( Date theDate) {
		myRuleDateTime = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Contract.signer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * List or contract signatures.
     * </p> 
	 */
	@Block()	
	public static class Signer 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodingDt.class, order=0, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ContractSignerType",
		formalDefinition="Party or role who is signing."
	)
	private java.util.List<CodingDt> myType;
	
	@Child(name="signature", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The DSIG signature contents in Base64."
	)
	private StringDt mySignature;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  mySignature);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, mySignature);
	}

	/**
	 * Gets the value(s) for <b>type</b> (ContractSignerType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Party or role who is signing.
     * </p> 
	 */
	public java.util.List<CodingDt> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<CodingDt>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (ContractSignerType)
	 *
     * <p>
     * <b>Definition:</b>
     * Party or role who is signing.
     * </p> 
	 */
	public Signer setType(java.util.List<CodingDt> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>type</b> (ContractSignerType)
	 *
     * <p>
     * <b>Definition:</b>
     * Party or role who is signing.
     * </p> 
	 */
	public CodingDt addType() {
		CodingDt newType = new CodingDt();
		getType().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>type</b> (ContractSignerType),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Party or role who is signing.
     * </p> 
	 */
	public CodingDt getTypeFirstRep() {
		if (getType().isEmpty()) {
			return addType();
		}
		return getType().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>signature</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The DSIG signature contents in Base64.
     * </p> 
	 */
	public StringDt getSignatureElement() {  
		if (mySignature == null) {
			mySignature = new StringDt();
		}
		return mySignature;
	}

	
	/**
	 * Gets the value(s) for <b>signature</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The DSIG signature contents in Base64.
     * </p> 
	 */
	public String getSignature() {  
		return getSignatureElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>signature</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The DSIG signature contents in Base64.
     * </p> 
	 */
	public Signer setSignature(StringDt theValue) {
		mySignature = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>signature</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The DSIG signature contents in Base64.
     * </p> 
	 */
	public Signer setSignature( String theString) {
		mySignature = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Contract.term</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time.
     * </p> 
	 */
	@Block()	
	public static class Term 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Unique Id for this particular term."
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ContractTermType",
		formalDefinition="The type of the term."
	)
	private BoundCodeableConceptDt<ContractTermTypeCodesEnum> myType;
	
	@Child(name="subtype", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ContractTermSubType",
		formalDefinition="The subtype of the term which is appropriate to the term type."
	)
	private CodeableConceptDt mySubtype;
	
	@Child(name="subject", order=3, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Who or what the contract term is about."
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="text", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Human readable form of the term of the contract."
	)
	private StringDt myText;
	
	@Child(name="issued", type=DateTimeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="When this term was issued."
	)
	private DateTimeDt myIssued;
	
	@Child(name="applies", type=PeriodDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Relevant time/time-period when the term is applicable"
	)
	private PeriodDt myApplies;
	
	@Child(name="quantity", type=QuantityDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The number of repetitions of a service or product."
	)
	private QuantityDt myQuantity;
	
	@Child(name="unitPrice", type=MoneyDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The unit price product."
	)
	private MoneyDt myUnitPrice;
	
	@Child(name="factor", type=DecimalDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount."
	)
	private DecimalDt myFactor;
	
	@Child(name="points", type=DecimalDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point."
	)
	private DecimalDt myPoints;
	
	@Child(name="net", type=MoneyDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied."
	)
	private MoneyDt myNet;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType,  mySubtype,  mySubject,  myText,  myIssued,  myApplies,  myQuantity,  myUnitPrice,  myFactor,  myPoints,  myNet);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType, mySubtype, mySubject, myText, myIssued, myApplies, myQuantity, myUnitPrice, myFactor, myPoints, myNet);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique Id for this particular term.
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Unique Id for this particular term.
     * </p> 
	 */
	public Term setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>type</b> (ContractTermType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the term.
     * </p> 
	 */
	public BoundCodeableConceptDt<ContractTermTypeCodesEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<ContractTermTypeCodesEnum>(ContractTermTypeCodesEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (ContractTermType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the term.
     * </p> 
	 */
	public Term setType(BoundCodeableConceptDt<ContractTermTypeCodesEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (ContractTermType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the term.
     * </p> 
	 */
	public Term setType(ContractTermTypeCodesEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subtype</b> (ContractTermSubType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The subtype of the term which is appropriate to the term type.
     * </p> 
	 */
	public CodeableConceptDt getSubtype() {  
		if (mySubtype == null) {
			mySubtype = new CodeableConceptDt();
		}
		return mySubtype;
	}

	/**
	 * Sets the value(s) for <b>subtype</b> (ContractTermSubType)
	 *
     * <p>
     * <b>Definition:</b>
     * The subtype of the term which is appropriate to the term type.
     * </p> 
	 */
	public Term setSubtype(CodeableConceptDt theValue) {
		mySubtype = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the contract term is about.
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the contract term is about.
     * </p> 
	 */
	public Term setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human readable form of the term of the contract.
     * </p> 
	 */
	public StringDt getTextElement() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	
	/**
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human readable form of the term of the contract.
     * </p> 
	 */
	public String getText() {  
		return getTextElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>text</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human readable form of the term of the contract.
     * </p> 
	 */
	public Term setText(StringDt theValue) {
		myText = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>text</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human readable form of the term of the contract.
     * </p> 
	 */
	public Term setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>issued</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When this term was issued.
     * </p> 
	 */
	public DateTimeDt getIssuedElement() {  
		if (myIssued == null) {
			myIssued = new DateTimeDt();
		}
		return myIssued;
	}

	
	/**
	 * Gets the value(s) for <b>issued</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When this term was issued.
     * </p> 
	 */
	public Date getIssued() {  
		return getIssuedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When this term was issued.
     * </p> 
	 */
	public Term setIssued(DateTimeDt theValue) {
		myIssued = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When this term was issued.
     * </p> 
	 */
	public Term setIssued( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myIssued = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When this term was issued.
     * </p> 
	 */
	public Term setIssuedWithSecondsPrecision( Date theDate) {
		myIssued = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>applies</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when the term is applicable
     * </p> 
	 */
	public PeriodDt getApplies() {  
		if (myApplies == null) {
			myApplies = new PeriodDt();
		}
		return myApplies;
	}

	/**
	 * Sets the value(s) for <b>applies</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relevant time/time-period when the term is applicable
     * </p> 
	 */
	public Term setApplies(PeriodDt theValue) {
		myApplies = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of repetitions of a service or product.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The number of repetitions of a service or product.
     * </p> 
	 */
	public Term setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>unitPrice</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The unit price product.
     * </p> 
	 */
	public MoneyDt getUnitPrice() {  
		if (myUnitPrice == null) {
			myUnitPrice = new MoneyDt();
		}
		return myUnitPrice;
	}

	/**
	 * Sets the value(s) for <b>unitPrice</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The unit price product.
     * </p> 
	 */
	public Term setUnitPrice(MoneyDt theValue) {
		myUnitPrice = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>factor</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public DecimalDt getFactorElement() {  
		if (myFactor == null) {
			myFactor = new DecimalDt();
		}
		return myFactor;
	}

	
	/**
	 * Gets the value(s) for <b>factor</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public BigDecimal getFactor() {  
		return getFactorElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>factor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public Term setFactor(DecimalDt theValue) {
		myFactor = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>factor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public Term setFactor( long theValue) {
		myFactor = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>factor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public Term setFactor( double theValue) {
		myFactor = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>factor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     * </p> 
	 */
	public Term setFactor( java.math.BigDecimal theValue) {
		myFactor = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>points</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public DecimalDt getPointsElement() {  
		if (myPoints == null) {
			myPoints = new DecimalDt();
		}
		return myPoints;
	}

	
	/**
	 * Gets the value(s) for <b>points</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public BigDecimal getPoints() {  
		return getPointsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>points</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public Term setPoints(DecimalDt theValue) {
		myPoints = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>points</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public Term setPoints( long theValue) {
		myPoints = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>points</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public Term setPoints( double theValue) {
		myPoints = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>points</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     * </p> 
	 */
	public Term setPoints( java.math.BigDecimal theValue) {
		myPoints = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>net</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
     * </p> 
	 */
	public MoneyDt getNet() {  
		if (myNet == null) {
			myNet = new MoneyDt();
		}
		return myNet;
	}

	/**
	 * Sets the value(s) for <b>net</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
     * </p> 
	 */
	public Term setNet(MoneyDt theValue) {
		myNet = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Contract";
    }

}
