package org.hl7.fhir.r4.model;

import org.hl7.fhir.exceptions.FHIRException;

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

public enum ResourceType {
    Account,
    ActivityDefinition,
    AdverseEvent,
    AllergyIntolerance,
    Appointment,
    AppointmentResponse,
    AuditEvent,
    Basic,
    Binary,
    BiologicallyDerivedProduct,
    BodyStructure,
    Bundle,
    CapabilityStatement,
    CarePlan,
    CareTeam,
    CatalogEntry,
    ChargeItem,
    ChargeItemDefinition,
    Claim,
    ClaimResponse,
    ClinicalImpression,
    CodeSystem,
    Communication,
    CommunicationRequest,
    CompartmentDefinition,
    Composition,
    ConceptMap,
    Condition,
    Consent,
    Contract,
    Coverage,
    CoverageEligibilityRequest,
    CoverageEligibilityResponse,
    DetectedIssue,
    Device,
    DeviceDefinition,
    DeviceMetric,
    DeviceRequest,
    DeviceUseStatement,
    DiagnosticReport,
    DocumentManifest,
    DocumentReference,
    EffectEvidenceSynthesis,
    Encounter,
    Endpoint,
    EnrollmentRequest,
    EnrollmentResponse,
    EpisodeOfCare,
    EventDefinition,
    Evidence,
    EvidenceVariable,
    ExampleScenario,
    ExplanationOfBenefit,
    FamilyMemberHistory,
    Flag,
    Goal,
    GraphDefinition,
    Group,
    GuidanceResponse,
    HealthcareService,
    ImagingStudy,
    Immunization,
    ImmunizationEvaluation,
    ImmunizationRecommendation,
    ImplementationGuide,
    InsurancePlan,
    Invoice,
    Library,
    Linkage,
    List,
    Location,
    Measure,
    MeasureReport,
    Media,
    Medication,
    MedicationAdministration,
    MedicationDispense,
    MedicationKnowledge,
    MedicationRequest,
    MedicationStatement,
    MedicinalProduct,
    MedicinalProductAuthorization,
    MedicinalProductContraindication,
    MedicinalProductIndication,
    MedicinalProductIngredient,
    MedicinalProductInteraction,
    MedicinalProductManufactured,
    MedicinalProductPackaged,
    MedicinalProductPharmaceutical,
    MedicinalProductUndesirableEffect,
    MessageDefinition,
    MessageHeader,
    MolecularSequence,
    NamingSystem,
    NutritionOrder,
    Observation,
    ObservationDefinition,
    OperationDefinition,
    OperationOutcome,
    Organization,
    OrganizationAffiliation,
    Parameters,
    Patient,
    PaymentNotice,
    PaymentReconciliation,
    Person,
    PlanDefinition,
    Practitioner,
    PractitionerRole,
    Procedure,
    Provenance,
    Questionnaire,
    QuestionnaireResponse,
    RelatedPerson,
    RequestGroup,
    ResearchDefinition,
    ResearchElementDefinition,
    ResearchStudy,
    ResearchSubject,
    RiskAssessment,
    RiskEvidenceSynthesis,
    Schedule,
    SearchParameter,
    ServiceRequest,
    Slot,
    Specimen,
    SpecimenDefinition,
    StructureDefinition,
    StructureMap,
    Subscription,
    Substance,
    SubstanceNucleicAcid,
    SubstancePolymer,
    SubstanceProtein,
    SubstanceReferenceInformation,
    SubstanceSourceMaterial,
    SubstanceSpecification,
    SupplyDelivery,
    SupplyRequest,
    Task,
    TerminologyCapabilities,
    TestReport,
    TestScript,
    ValueSet,
    VerificationResult,
    VisionPrescription;


    public String getPath() {;
      switch (this) {
    case Account:
      return "account";
    case ActivityDefinition:
      return "activitydefinition";
    case AdverseEvent:
      return "adverseevent";
    case AllergyIntolerance:
      return "allergyintolerance";
    case Appointment:
      return "appointment";
    case AppointmentResponse:
      return "appointmentresponse";
    case AuditEvent:
      return "auditevent";
    case Basic:
      return "basic";
    case Binary:
      return "binary";
    case BiologicallyDerivedProduct:
      return "biologicallyderivedproduct";
    case BodyStructure:
      return "bodystructure";
    case Bundle:
      return "bundle";
    case CapabilityStatement:
      return "capabilitystatement";
    case CarePlan:
      return "careplan";
    case CareTeam:
      return "careteam";
    case CatalogEntry:
      return "catalogentry";
    case ChargeItem:
      return "chargeitem";
    case ChargeItemDefinition:
      return "chargeitemdefinition";
    case Claim:
      return "claim";
    case ClaimResponse:
      return "claimresponse";
    case ClinicalImpression:
      return "clinicalimpression";
    case CodeSystem:
      return "codesystem";
    case Communication:
      return "communication";
    case CommunicationRequest:
      return "communicationrequest";
    case CompartmentDefinition:
      return "compartmentdefinition";
    case Composition:
      return "composition";
    case ConceptMap:
      return "conceptmap";
    case Condition:
      return "condition";
    case Consent:
      return "consent";
    case Contract:
      return "contract";
    case Coverage:
      return "coverage";
    case CoverageEligibilityRequest:
      return "coverageeligibilityrequest";
    case CoverageEligibilityResponse:
      return "coverageeligibilityresponse";
    case DetectedIssue:
      return "detectedissue";
    case Device:
      return "device";
    case DeviceDefinition:
      return "devicedefinition";
    case DeviceMetric:
      return "devicemetric";
    case DeviceRequest:
      return "devicerequest";
    case DeviceUseStatement:
      return "deviceusestatement";
    case DiagnosticReport:
      return "diagnosticreport";
    case DocumentManifest:
      return "documentmanifest";
    case DocumentReference:
      return "documentreference";
    case EffectEvidenceSynthesis:
      return "effectevidencesynthesis";
    case Encounter:
      return "encounter";
    case Endpoint:
      return "endpoint";
    case EnrollmentRequest:
      return "enrollmentrequest";
    case EnrollmentResponse:
      return "enrollmentresponse";
    case EpisodeOfCare:
      return "episodeofcare";
    case EventDefinition:
      return "eventdefinition";
    case Evidence:
      return "evidence";
    case EvidenceVariable:
      return "evidencevariable";
    case ExampleScenario:
      return "examplescenario";
    case ExplanationOfBenefit:
      return "explanationofbenefit";
    case FamilyMemberHistory:
      return "familymemberhistory";
    case Flag:
      return "flag";
    case Goal:
      return "goal";
    case GraphDefinition:
      return "graphdefinition";
    case Group:
      return "group";
    case GuidanceResponse:
      return "guidanceresponse";
    case HealthcareService:
      return "healthcareservice";
    case ImagingStudy:
      return "imagingstudy";
    case Immunization:
      return "immunization";
    case ImmunizationEvaluation:
      return "immunizationevaluation";
    case ImmunizationRecommendation:
      return "immunizationrecommendation";
    case ImplementationGuide:
      return "implementationguide";
    case InsurancePlan:
      return "insuranceplan";
    case Invoice:
      return "invoice";
    case Library:
      return "library";
    case Linkage:
      return "linkage";
    case List:
      return "list";
    case Location:
      return "location";
    case Measure:
      return "measure";
    case MeasureReport:
      return "measurereport";
    case Media:
      return "media";
    case Medication:
      return "medication";
    case MedicationAdministration:
      return "medicationadministration";
    case MedicationDispense:
      return "medicationdispense";
    case MedicationKnowledge:
      return "medicationknowledge";
    case MedicationRequest:
      return "medicationrequest";
    case MedicationStatement:
      return "medicationstatement";
    case MedicinalProduct:
      return "medicinalproduct";
    case MedicinalProductAuthorization:
      return "medicinalproductauthorization";
    case MedicinalProductContraindication:
      return "medicinalproductcontraindication";
    case MedicinalProductIndication:
      return "medicinalproductindication";
    case MedicinalProductIngredient:
      return "medicinalproductingredient";
    case MedicinalProductInteraction:
      return "medicinalproductinteraction";
    case MedicinalProductManufactured:
      return "medicinalproductmanufactured";
    case MedicinalProductPackaged:
      return "medicinalproductpackaged";
    case MedicinalProductPharmaceutical:
      return "medicinalproductpharmaceutical";
    case MedicinalProductUndesirableEffect:
      return "medicinalproductundesirableeffect";
    case MessageDefinition:
      return "messagedefinition";
    case MessageHeader:
      return "messageheader";
    case MolecularSequence:
      return "molecularsequence";
    case NamingSystem:
      return "namingsystem";
    case NutritionOrder:
      return "nutritionorder";
    case Observation:
      return "observation";
    case ObservationDefinition:
      return "observationdefinition";
    case OperationDefinition:
      return "operationdefinition";
    case OperationOutcome:
      return "operationoutcome";
    case Organization:
      return "organization";
    case OrganizationAffiliation:
      return "organizationaffiliation";
    case Parameters:
      return "parameters";
    case Patient:
      return "patient";
    case PaymentNotice:
      return "paymentnotice";
    case PaymentReconciliation:
      return "paymentreconciliation";
    case Person:
      return "person";
    case PlanDefinition:
      return "plandefinition";
    case Practitioner:
      return "practitioner";
    case PractitionerRole:
      return "practitionerrole";
    case Procedure:
      return "procedure";
    case Provenance:
      return "provenance";
    case Questionnaire:
      return "questionnaire";
    case QuestionnaireResponse:
      return "questionnaireresponse";
    case RelatedPerson:
      return "relatedperson";
    case RequestGroup:
      return "requestgroup";
    case ResearchDefinition:
      return "researchdefinition";
    case ResearchElementDefinition:
      return "researchelementdefinition";
    case ResearchStudy:
      return "researchstudy";
    case ResearchSubject:
      return "researchsubject";
    case RiskAssessment:
      return "riskassessment";
    case RiskEvidenceSynthesis:
      return "riskevidencesynthesis";
    case Schedule:
      return "schedule";
    case SearchParameter:
      return "searchparameter";
    case ServiceRequest:
      return "servicerequest";
    case Slot:
      return "slot";
    case Specimen:
      return "specimen";
    case SpecimenDefinition:
      return "specimendefinition";
    case StructureDefinition:
      return "structuredefinition";
    case StructureMap:
      return "structuremap";
    case Subscription:
      return "subscription";
    case Substance:
      return "substance";
    case SubstanceNucleicAcid:
      return "substancenucleicacid";
    case SubstancePolymer:
      return "substancepolymer";
    case SubstanceProtein:
      return "substanceprotein";
    case SubstanceReferenceInformation:
      return "substancereferenceinformation";
    case SubstanceSourceMaterial:
      return "substancesourcematerial";
    case SubstanceSpecification:
      return "substancespecification";
    case SupplyDelivery:
      return "supplydelivery";
    case SupplyRequest:
      return "supplyrequest";
    case Task:
      return "task";
    case TerminologyCapabilities:
      return "terminologycapabilities";
    case TestReport:
      return "testreport";
    case TestScript:
      return "testscript";
    case ValueSet:
      return "valueset";
    case VerificationResult:
      return "verificationresult";
    case VisionPrescription:
      return "visionprescription";
    }
      return null;
  }


    public static ResourceType fromCode(String code) throws FHIRException {;
    if ("Account".equals(code))
      return Account;
    if ("ActivityDefinition".equals(code))
      return ActivityDefinition;
    if ("AdverseEvent".equals(code))
      return AdverseEvent;
    if ("AllergyIntolerance".equals(code))
      return AllergyIntolerance;
    if ("Appointment".equals(code))
      return Appointment;
    if ("AppointmentResponse".equals(code))
      return AppointmentResponse;
    if ("AuditEvent".equals(code))
      return AuditEvent;
    if ("Basic".equals(code))
      return Basic;
    if ("Binary".equals(code))
      return Binary;
    if ("BiologicallyDerivedProduct".equals(code))
      return BiologicallyDerivedProduct;
    if ("BodyStructure".equals(code))
      return BodyStructure;
    if ("Bundle".equals(code))
      return Bundle;
    if ("CapabilityStatement".equals(code))
      return CapabilityStatement;
    if ("CarePlan".equals(code))
      return CarePlan;
    if ("CareTeam".equals(code))
      return CareTeam;
    if ("CatalogEntry".equals(code))
      return CatalogEntry;
    if ("ChargeItem".equals(code))
      return ChargeItem;
    if ("ChargeItemDefinition".equals(code))
      return ChargeItemDefinition;
    if ("Claim".equals(code))
      return Claim;
    if ("ClaimResponse".equals(code))
      return ClaimResponse;
    if ("ClinicalImpression".equals(code))
      return ClinicalImpression;
    if ("CodeSystem".equals(code))
      return CodeSystem;
    if ("Communication".equals(code))
      return Communication;
    if ("CommunicationRequest".equals(code))
      return CommunicationRequest;
    if ("CompartmentDefinition".equals(code))
      return CompartmentDefinition;
    if ("Composition".equals(code))
      return Composition;
    if ("ConceptMap".equals(code))
      return ConceptMap;
    if ("Condition".equals(code))
      return Condition;
    if ("Consent".equals(code))
      return Consent;
    if ("Contract".equals(code))
      return Contract;
    if ("Coverage".equals(code))
      return Coverage;
    if ("CoverageEligibilityRequest".equals(code))
      return CoverageEligibilityRequest;
    if ("CoverageEligibilityResponse".equals(code))
      return CoverageEligibilityResponse;
    if ("DetectedIssue".equals(code))
      return DetectedIssue;
    if ("Device".equals(code))
      return Device;
    if ("DeviceDefinition".equals(code))
      return DeviceDefinition;
    if ("DeviceMetric".equals(code))
      return DeviceMetric;
    if ("DeviceRequest".equals(code))
      return DeviceRequest;
    if ("DeviceUseStatement".equals(code))
      return DeviceUseStatement;
    if ("DiagnosticReport".equals(code))
      return DiagnosticReport;
    if ("DocumentManifest".equals(code))
      return DocumentManifest;
    if ("DocumentReference".equals(code))
      return DocumentReference;
    if ("EffectEvidenceSynthesis".equals(code))
      return EffectEvidenceSynthesis;
    if ("Encounter".equals(code))
      return Encounter;
    if ("Endpoint".equals(code))
      return Endpoint;
    if ("EnrollmentRequest".equals(code))
      return EnrollmentRequest;
    if ("EnrollmentResponse".equals(code))
      return EnrollmentResponse;
    if ("EpisodeOfCare".equals(code))
      return EpisodeOfCare;
    if ("EventDefinition".equals(code))
      return EventDefinition;
    if ("Evidence".equals(code))
      return Evidence;
    if ("EvidenceVariable".equals(code))
      return EvidenceVariable;
    if ("ExampleScenario".equals(code))
      return ExampleScenario;
    if ("ExplanationOfBenefit".equals(code))
      return ExplanationOfBenefit;
    if ("FamilyMemberHistory".equals(code))
      return FamilyMemberHistory;
    if ("Flag".equals(code))
      return Flag;
    if ("Goal".equals(code))
      return Goal;
    if ("GraphDefinition".equals(code))
      return GraphDefinition;
    if ("Group".equals(code))
      return Group;
    if ("GuidanceResponse".equals(code))
      return GuidanceResponse;
    if ("HealthcareService".equals(code))
      return HealthcareService;
    if ("ImagingStudy".equals(code))
      return ImagingStudy;
    if ("Immunization".equals(code))
      return Immunization;
    if ("ImmunizationEvaluation".equals(code))
      return ImmunizationEvaluation;
    if ("ImmunizationRecommendation".equals(code))
      return ImmunizationRecommendation;
    if ("ImplementationGuide".equals(code))
      return ImplementationGuide;
    if ("InsurancePlan".equals(code))
      return InsurancePlan;
    if ("Invoice".equals(code))
      return Invoice;
    if ("Library".equals(code))
      return Library;
    if ("Linkage".equals(code))
      return Linkage;
    if ("List".equals(code))
      return List;
    if ("Location".equals(code))
      return Location;
    if ("Measure".equals(code))
      return Measure;
    if ("MeasureReport".equals(code))
      return MeasureReport;
    if ("Media".equals(code))
      return Media;
    if ("Medication".equals(code))
      return Medication;
    if ("MedicationAdministration".equals(code))
      return MedicationAdministration;
    if ("MedicationDispense".equals(code))
      return MedicationDispense;
    if ("MedicationKnowledge".equals(code))
      return MedicationKnowledge;
    if ("MedicationRequest".equals(code))
      return MedicationRequest;
    if ("MedicationStatement".equals(code))
      return MedicationStatement;
    if ("MedicinalProduct".equals(code))
      return MedicinalProduct;
    if ("MedicinalProductAuthorization".equals(code))
      return MedicinalProductAuthorization;
    if ("MedicinalProductContraindication".equals(code))
      return MedicinalProductContraindication;
    if ("MedicinalProductIndication".equals(code))
      return MedicinalProductIndication;
    if ("MedicinalProductIngredient".equals(code))
      return MedicinalProductIngredient;
    if ("MedicinalProductInteraction".equals(code))
      return MedicinalProductInteraction;
    if ("MedicinalProductManufactured".equals(code))
      return MedicinalProductManufactured;
    if ("MedicinalProductPackaged".equals(code))
      return MedicinalProductPackaged;
    if ("MedicinalProductPharmaceutical".equals(code))
      return MedicinalProductPharmaceutical;
    if ("MedicinalProductUndesirableEffect".equals(code))
      return MedicinalProductUndesirableEffect;
    if ("MessageDefinition".equals(code))
      return MessageDefinition;
    if ("MessageHeader".equals(code))
      return MessageHeader;
    if ("MolecularSequence".equals(code))
      return MolecularSequence;
    if ("NamingSystem".equals(code))
      return NamingSystem;
    if ("NutritionOrder".equals(code))
      return NutritionOrder;
    if ("Observation".equals(code))
      return Observation;
    if ("ObservationDefinition".equals(code))
      return ObservationDefinition;
    if ("OperationDefinition".equals(code))
      return OperationDefinition;
    if ("OperationOutcome".equals(code))
      return OperationOutcome;
    if ("Organization".equals(code))
      return Organization;
    if ("OrganizationAffiliation".equals(code))
      return OrganizationAffiliation;
    if ("Parameters".equals(code))
      return Parameters;
    if ("Patient".equals(code))
      return Patient;
    if ("PaymentNotice".equals(code))
      return PaymentNotice;
    if ("PaymentReconciliation".equals(code))
      return PaymentReconciliation;
    if ("Person".equals(code))
      return Person;
    if ("PlanDefinition".equals(code))
      return PlanDefinition;
    if ("Practitioner".equals(code))
      return Practitioner;
    if ("PractitionerRole".equals(code))
      return PractitionerRole;
    if ("Procedure".equals(code))
      return Procedure;
    if ("Provenance".equals(code))
      return Provenance;
    if ("Questionnaire".equals(code))
      return Questionnaire;
    if ("QuestionnaireResponse".equals(code))
      return QuestionnaireResponse;
    if ("RelatedPerson".equals(code))
      return RelatedPerson;
    if ("RequestGroup".equals(code))
      return RequestGroup;
    if ("ResearchDefinition".equals(code))
      return ResearchDefinition;
    if ("ResearchElementDefinition".equals(code))
      return ResearchElementDefinition;
    if ("ResearchStudy".equals(code))
      return ResearchStudy;
    if ("ResearchSubject".equals(code))
      return ResearchSubject;
    if ("RiskAssessment".equals(code))
      return RiskAssessment;
    if ("RiskEvidenceSynthesis".equals(code))
      return RiskEvidenceSynthesis;
    if ("Schedule".equals(code))
      return Schedule;
    if ("SearchParameter".equals(code))
      return SearchParameter;
    if ("ServiceRequest".equals(code))
      return ServiceRequest;
    if ("Slot".equals(code))
      return Slot;
    if ("Specimen".equals(code))
      return Specimen;
    if ("SpecimenDefinition".equals(code))
      return SpecimenDefinition;
    if ("StructureDefinition".equals(code))
      return StructureDefinition;
    if ("StructureMap".equals(code))
      return StructureMap;
    if ("Subscription".equals(code))
      return Subscription;
    if ("Substance".equals(code))
      return Substance;
    if ("SubstanceNucleicAcid".equals(code))
      return SubstanceNucleicAcid;
    if ("SubstancePolymer".equals(code))
      return SubstancePolymer;
    if ("SubstanceProtein".equals(code))
      return SubstanceProtein;
    if ("SubstanceReferenceInformation".equals(code))
      return SubstanceReferenceInformation;
    if ("SubstanceSourceMaterial".equals(code))
      return SubstanceSourceMaterial;
    if ("SubstanceSpecification".equals(code))
      return SubstanceSpecification;
    if ("SupplyDelivery".equals(code))
      return SupplyDelivery;
    if ("SupplyRequest".equals(code))
      return SupplyRequest;
    if ("Task".equals(code))
      return Task;
    if ("TerminologyCapabilities".equals(code))
      return TerminologyCapabilities;
    if ("TestReport".equals(code))
      return TestReport;
    if ("TestScript".equals(code))
      return TestScript;
    if ("ValueSet".equals(code))
      return ValueSet;
    if ("VerificationResult".equals(code))
      return VerificationResult;
    if ("VisionPrescription".equals(code))
      return VisionPrescription;

    throw new FHIRException("Unknown resource type"+code);
  }

}
