package org.hl7.fhir.instance.model;

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2

public enum ResourceType {
    Account,
    AllergyIntolerance,
    Appointment,
    AppointmentResponse,
    AuditEvent,
    Basic,
    Binary,
    BodySite,
    Bundle,
    CarePlan,
    Claim,
    ClaimResponse,
    ClinicalImpression,
    Communication,
    CommunicationRequest,
    Composition,
    ConceptMap,
    Condition,
    Conformance,
    Contract,
    Coverage,
    DataElement,
    DetectedIssue,
    Device,
    DeviceComponent,
    DeviceMetric,
    DeviceUseRequest,
    DeviceUseStatement,
    DiagnosticOrder,
    DiagnosticReport,
    DocumentManifest,
    DocumentReference,
    EligibilityRequest,
    EligibilityResponse,
    Encounter,
    EnrollmentRequest,
    EnrollmentResponse,
    EpisodeOfCare,
    ExplanationOfBenefit,
    FamilyMemberHistory,
    Flag,
    Goal,
    Group,
    HealthcareService,
    ImagingObjectSelection,
    ImagingStudy,
    Immunization,
    ImmunizationRecommendation,
    ImplementationGuide,
    List,
    Location,
    Media,
    Medication,
    MedicationAdministration,
    MedicationDispense,
    MedicationOrder,
    MedicationStatement,
    MessageHeader,
    NamingSystem,
    NutritionOrder,
    Observation,
    OperationDefinition,
    OperationOutcome,
    Order,
    OrderResponse,
    Organization,
    Parameters,
    Patient,
    PaymentNotice,
    PaymentReconciliation,
    Person,
    Practitioner,
    Procedure,
    ProcedureRequest,
    ProcessRequest,
    ProcessResponse,
    Provenance,
    Questionnaire,
    QuestionnaireResponse,
    ReferralRequest,
    RelatedPerson,
    RiskAssessment,
    Schedule,
    SearchParameter,
    Slot,
    Specimen,
    StructureDefinition,
    Subscription,
    Substance,
    SupplyDelivery,
    SupplyRequest,
    TestScript,
    ValueSet,
    VisionPrescription;


    public String getPath() {;
      switch (this) {
    case Account:
      return "account";
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
    case BodySite:
      return "bodysite";
    case Bundle:
      return "bundle";
    case CarePlan:
      return "careplan";
    case Claim:
      return "claim";
    case ClaimResponse:
      return "claimresponse";
    case ClinicalImpression:
      return "clinicalimpression";
    case Communication:
      return "communication";
    case CommunicationRequest:
      return "communicationrequest";
    case Composition:
      return "composition";
    case ConceptMap:
      return "conceptmap";
    case Condition:
      return "condition";
    case Conformance:
      return "conformance";
    case Contract:
      return "contract";
    case Coverage:
      return "coverage";
    case DataElement:
      return "dataelement";
    case DetectedIssue:
      return "detectedissue";
    case Device:
      return "device";
    case DeviceComponent:
      return "devicecomponent";
    case DeviceMetric:
      return "devicemetric";
    case DeviceUseRequest:
      return "deviceuserequest";
    case DeviceUseStatement:
      return "deviceusestatement";
    case DiagnosticOrder:
      return "diagnosticorder";
    case DiagnosticReport:
      return "diagnosticreport";
    case DocumentManifest:
      return "documentmanifest";
    case DocumentReference:
      return "documentreference";
    case EligibilityRequest:
      return "eligibilityrequest";
    case EligibilityResponse:
      return "eligibilityresponse";
    case Encounter:
      return "encounter";
    case EnrollmentRequest:
      return "enrollmentrequest";
    case EnrollmentResponse:
      return "enrollmentresponse";
    case EpisodeOfCare:
      return "episodeofcare";
    case ExplanationOfBenefit:
      return "explanationofbenefit";
    case FamilyMemberHistory:
      return "familymemberhistory";
    case Flag:
      return "flag";
    case Goal:
      return "goal";
    case Group:
      return "group";
    case HealthcareService:
      return "healthcareservice";
    case ImagingObjectSelection:
      return "imagingobjectselection";
    case ImagingStudy:
      return "imagingstudy";
    case Immunization:
      return "immunization";
    case ImmunizationRecommendation:
      return "immunizationrecommendation";
    case ImplementationGuide:
      return "implementationguide";
    case List:
      return "list";
    case Location:
      return "location";
    case Media:
      return "media";
    case Medication:
      return "medication";
    case MedicationAdministration:
      return "medicationadministration";
    case MedicationDispense:
      return "medicationdispense";
    case MedicationOrder:
      return "medicationorder";
    case MedicationStatement:
      return "medicationstatement";
    case MessageHeader:
      return "messageheader";
    case NamingSystem:
      return "namingsystem";
    case NutritionOrder:
      return "nutritionorder";
    case Observation:
      return "observation";
    case OperationDefinition:
      return "operationdefinition";
    case OperationOutcome:
      return "operationoutcome";
    case Order:
      return "order";
    case OrderResponse:
      return "orderresponse";
    case Organization:
      return "organization";
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
    case Practitioner:
      return "practitioner";
    case Procedure:
      return "procedure";
    case ProcedureRequest:
      return "procedurerequest";
    case ProcessRequest:
      return "processrequest";
    case ProcessResponse:
      return "processresponse";
    case Provenance:
      return "provenance";
    case Questionnaire:
      return "questionnaire";
    case QuestionnaireResponse:
      return "questionnaireresponse";
    case ReferralRequest:
      return "referralrequest";
    case RelatedPerson:
      return "relatedperson";
    case RiskAssessment:
      return "riskassessment";
    case Schedule:
      return "schedule";
    case SearchParameter:
      return "searchparameter";
    case Slot:
      return "slot";
    case Specimen:
      return "specimen";
    case StructureDefinition:
      return "structuredefinition";
    case Subscription:
      return "subscription";
    case Substance:
      return "substance";
    case SupplyDelivery:
      return "supplydelivery";
    case SupplyRequest:
      return "supplyrequest";
    case TestScript:
      return "testscript";
    case ValueSet:
      return "valueset";
    case VisionPrescription:
      return "visionprescription";
    }
      return null;
  }

}
