package org.hl7.fhir.instance.model;

// Generated on Sun, May 31, 2015 15:45-0400 for FHIR v0.5.0

public enum ResourceType {
    Appointment,
    ReferralRequest,
    Provenance,
    Questionnaire,
    ExplanationOfBenefit,
    DocumentManifest,
    Specimen,
    AllergyIntolerance,
    CarePlan,
    Goal,
    StructureDefinition,
    EnrollmentRequest,
    EpisodeOfCare,
    MedicationPrescription,
    OperationOutcome,
    Medication,
    Procedure,
    List,
    ConceptMap,
    Subscription,
    ValueSet,
    OperationDefinition,
    DocumentReference,
    Order,
    Immunization,
    Device,
    VisionPrescription,
    Media,
    Conformance,
    ProcedureRequest,
    EligibilityResponse,
    DeviceUseRequest,
    DeviceMetric,
    Flag,
    RelatedPerson,
    SupplyRequest,
    Practitioner,
    AppointmentResponse,
    Observation,
    MedicationAdministration,
    Slot,
    Contraindication,
    EnrollmentResponse,
    Binary,
    MedicationStatement,
    Contract,
    Person,
    CommunicationRequest,
    RiskAssessment,
    TestScript,
    Basic,
    Group,
    PaymentNotice,
    Organization,
    ClaimResponse,
    EligibilityRequest,
    ProcessRequest,
    MedicationDispense,
    Supply,
    DiagnosticReport,
    ImagingStudy,
    ImagingObjectSelection,
    HealthcareService,
    DataElement,
    DeviceComponent,
    FamilyMemberHistory,
    QuestionnaireAnswers,
    NutritionOrder,
    Encounter,
    Substance,
    AuditEvent,
    SearchParameter,
    PaymentReconciliation,
    Communication,
    Condition,
    Composition,
    Bundle,
    DiagnosticOrder,
    Patient,
    OrderResponse,
    Coverage,
    DeviceUseStatement,
    ProcessResponse,
    NamingSystem,
    Schedule,
    SupplyDelivery,
    ClinicalImpression,
    MessageHeader,
    Claim,
    ImmunizationRecommendation,
    Location,
    BodySite,
    Parameters;


    public String getPath() {;
      switch (this) {
    case Appointment:
      return "appointment";
    case ReferralRequest:
      return "referralrequest";
    case Provenance:
      return "provenance";
    case Questionnaire:
      return "questionnaire";
    case ExplanationOfBenefit:
      return "explanationofbenefit";
    case DocumentManifest:
      return "documentmanifest";
    case Specimen:
      return "specimen";
    case AllergyIntolerance:
      return "allergyintolerance";
    case CarePlan:
      return "careplan";
    case Goal:
      return "goal";
    case StructureDefinition:
      return "structuredefinition";
    case EnrollmentRequest:
      return "enrollmentrequest";
    case EpisodeOfCare:
      return "episodeofcare";
    case MedicationPrescription:
      return "medicationprescription";
    case OperationOutcome:
      return "operationoutcome";
    case Medication:
      return "medication";
    case Procedure:
      return "procedure";
    case List:
      return "list";
    case ConceptMap:
      return "conceptmap";
    case Subscription:
      return "subscription";
    case ValueSet:
      return "valueset";
    case OperationDefinition:
      return "operationdefinition";
    case DocumentReference:
      return "documentreference";
    case Order:
      return "order";
    case Immunization:
      return "immunization";
    case Device:
      return "device";
    case VisionPrescription:
      return "visionprescription";
    case Media:
      return "media";
    case Conformance:
      return "conformance";
    case ProcedureRequest:
      return "procedurerequest";
    case EligibilityResponse:
      return "eligibilityresponse";
    case DeviceUseRequest:
      return "deviceuserequest";
    case DeviceMetric:
      return "devicemetric";
    case Flag:
      return "flag";
    case RelatedPerson:
      return "relatedperson";
    case SupplyRequest:
      return "supplyrequest";
    case Practitioner:
      return "practitioner";
    case AppointmentResponse:
      return "appointmentresponse";
    case Observation:
      return "observation";
    case MedicationAdministration:
      return "medicationadministration";
    case Slot:
      return "slot";
    case Contraindication:
      return "contraindication";
    case EnrollmentResponse:
      return "enrollmentresponse";
    case Binary:
      return "binary";
    case MedicationStatement:
      return "medicationstatement";
    case Contract:
      return "contract";
    case Person:
      return "person";
    case CommunicationRequest:
      return "communicationrequest";
    case RiskAssessment:
      return "riskassessment";
    case TestScript:
      return "testscript";
    case Basic:
      return "basic";
    case Group:
      return "group";
    case PaymentNotice:
      return "paymentnotice";
    case Organization:
      return "organization";
    case ClaimResponse:
      return "claimresponse";
    case EligibilityRequest:
      return "eligibilityrequest";
    case ProcessRequest:
      return "processrequest";
    case MedicationDispense:
      return "medicationdispense";
    case Supply:
      return "supply";
    case DiagnosticReport:
      return "diagnosticreport";
    case ImagingStudy:
      return "imagingstudy";
    case ImagingObjectSelection:
      return "imagingobjectselection";
    case HealthcareService:
      return "healthcareservice";
    case DataElement:
      return "dataelement";
    case DeviceComponent:
      return "devicecomponent";
    case FamilyMemberHistory:
      return "familymemberhistory";
    case QuestionnaireAnswers:
      return "questionnaireanswers";
    case NutritionOrder:
      return "nutritionorder";
    case Encounter:
      return "encounter";
    case Substance:
      return "substance";
    case AuditEvent:
      return "auditevent";
    case SearchParameter:
      return "searchparameter";
    case PaymentReconciliation:
      return "paymentreconciliation";
    case Communication:
      return "communication";
    case Condition:
      return "condition";
    case Composition:
      return "composition";
    case Bundle:
      return "bundle";
    case DiagnosticOrder:
      return "diagnosticorder";
    case Patient:
      return "patient";
    case OrderResponse:
      return "orderresponse";
    case Coverage:
      return "coverage";
    case DeviceUseStatement:
      return "deviceusestatement";
    case ProcessResponse:
      return "processresponse";
    case NamingSystem:
      return "namingsystem";
    case Schedule:
      return "schedule";
    case SupplyDelivery:
      return "supplydelivery";
    case ClinicalImpression:
      return "clinicalimpression";
    case MessageHeader:
      return "messageheader";
    case Claim:
      return "claim";
    case ImmunizationRecommendation:
      return "immunizationrecommendation";
    case Location:
      return "location";
    case BodySite:
      return "bodysite";
    case Parameters:
      return "parameters";
    }
      return null;
  }

}
