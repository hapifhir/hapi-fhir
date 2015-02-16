package org.hl7.fhir.instance.model;

// Generated on Sat, Feb 14, 2015 16:12-0500 for FHIR v0.4.0

public enum ResourceType {
    Condition,
    Supply,
    DeviceComponent,
    Communication,
    Group,
    ValueSet,
    OralHealthClaim,
    Coverage,
    Appointment,
    Slot,
    Contraindication,
    EpisodeOfCare,
    Composition,
    Conformance,
    NamingSystem,
    Profile,
    HealthcareService,
    OrderResponse,
    StatusResponse,
    ConceptMap,
    PharmacyClaim,
    Reversal,
    Practitioner,
    CarePlan,
    ClinicalAssessment,
    Substance,
    DeviceUseRequest,
    Schedule,
    EligibilityRequest,
    QuestionnaireAnswers,
    PaymentReconciliation,
    ProfessionalClaim,
    ImagingObjectSelection,
    OperationDefinition,
    ClaimResponse,
    BodySite,
    CommunicationRequest,
    RiskAssessment,
    Observation,
    AllergyIntolerance,
    ExplanationOfBenefit,
    SupportingDocumentation,
    RelatedPerson,
    InstitutionalClaim,
    Alert,
    EligibilityResponse,
    Person,
    StatusRequest,
    ProcedureRequest,
    VisionClaim,
    DeviceMetric,
    Organization,
    Readjudicate,
    ImmunizationRecommendation,
    MedicationDispense,
    MedicationPrescription,
    PaymentNotice,
    MedicationStatement,
    AppointmentResponse,
    Questionnaire,
    OperationOutcome,
    Media,
    Binary,
    Other,
    VisionPrescription,
    DocumentReference,
    Immunization,
    ExtensionDefinition,
    Bundle,
    Subscription,
    ImagingStudy,
    Provenance,
    Device,
    StructureDefinition,
    Order,
    Procedure,
    DiagnosticReport,
    Medication,
    MessageHeader,
    DocumentManifest,
    DataElement,
    MedicationAdministration,
    Encounter,
    SecurityEvent,
    PendedRequest,
    List,
    DeviceUseStatement,
    Goal,
    NutritionOrder,
    SearchParameter,
    ReferralRequest,
    FamilyHistory,
    EnrollmentRequest,
    Location,
    Contract,
    Basic,
    Specimen,
    EnrollmentResponse,
    Patient,
    CarePlan2,
    DiagnosticOrder,
    Parameters;


    public String getPath() {;
      switch (this) {
    case Condition:
      return "condition";
    case Supply:
      return "supply";
    case DeviceComponent:
      return "devicecomponent";
    case Communication:
      return "communication";
    case Group:
      return "group";
    case ValueSet:
      return "valueset";
    case OralHealthClaim:
      return "oralhealthclaim";
    case Coverage:
      return "coverage";
    case Appointment:
      return "appointment";
    case Slot:
      return "slot";
    case Contraindication:
      return "contraindication";
    case EpisodeOfCare:
      return "episodeofcare";
    case Composition:
      return "composition";
    case Conformance:
      return "conformance";
    case NamingSystem:
      return "namingsystem";
    case Profile:
      return "profile";
    case HealthcareService:
      return "healthcareservice";
    case OrderResponse:
      return "orderresponse";
    case StatusResponse:
      return "statusresponse";
    case ConceptMap:
      return "conceptmap";
    case PharmacyClaim:
      return "pharmacyclaim";
    case Reversal:
      return "reversal";
    case Practitioner:
      return "practitioner";
    case CarePlan:
      return "careplan";
    case ClinicalAssessment:
      return "clinicalassessment";
    case Substance:
      return "substance";
    case DeviceUseRequest:
      return "deviceuserequest";
    case Schedule:
      return "schedule";
    case EligibilityRequest:
      return "eligibilityrequest";
    case QuestionnaireAnswers:
      return "questionnaireanswers";
    case PaymentReconciliation:
      return "paymentreconciliation";
    case ProfessionalClaim:
      return "professionalclaim";
    case ImagingObjectSelection:
      return "imagingobjectselection";
    case OperationDefinition:
      return "operationdefinition";
    case ClaimResponse:
      return "claimresponse";
    case BodySite:
      return "bodysite";
    case CommunicationRequest:
      return "communicationrequest";
    case RiskAssessment:
      return "riskassessment";
    case Observation:
      return "observation";
    case AllergyIntolerance:
      return "allergyintolerance";
    case ExplanationOfBenefit:
      return "explanationofbenefit";
    case SupportingDocumentation:
      return "supportingdocumentation";
    case RelatedPerson:
      return "relatedperson";
    case InstitutionalClaim:
      return "institutionalclaim";
    case Alert:
      return "alert";
    case EligibilityResponse:
      return "eligibilityresponse";
    case Person:
      return "person";
    case StatusRequest:
      return "statusrequest";
    case ProcedureRequest:
      return "procedurerequest";
    case VisionClaim:
      return "visionclaim";
    case DeviceMetric:
      return "devicemetric";
    case Organization:
      return "organization";
    case Readjudicate:
      return "readjudicate";
    case ImmunizationRecommendation:
      return "immunizationrecommendation";
    case MedicationDispense:
      return "medicationdispense";
    case MedicationPrescription:
      return "medicationprescription";
    case PaymentNotice:
      return "paymentnotice";
    case MedicationStatement:
      return "medicationstatement";
    case AppointmentResponse:
      return "appointmentresponse";
    case Questionnaire:
      return "questionnaire";
    case OperationOutcome:
      return "operationoutcome";
    case Media:
      return "media";
    case Binary:
      return "binary";
    case Other:
      return "other";
    case VisionPrescription:
      return "visionprescription";
    case DocumentReference:
      return "documentreference";
    case Immunization:
      return "immunization";
    case ExtensionDefinition:
      return "extensiondefinition";
    case Bundle:
      return "bundle";
    case Subscription:
      return "subscription";
    case ImagingStudy:
      return "imagingstudy";
    case Provenance:
      return "provenance";
    case Device:
      return "device";
    case StructureDefinition:
      return "structuredefinition";
    case Order:
      return "order";
    case Procedure:
      return "procedure";
    case DiagnosticReport:
      return "diagnosticreport";
    case Medication:
      return "medication";
    case MessageHeader:
      return "messageheader";
    case DocumentManifest:
      return "documentmanifest";
    case DataElement:
      return "dataelement";
    case MedicationAdministration:
      return "medicationadministration";
    case Encounter:
      return "encounter";
    case SecurityEvent:
      return "securityevent";
    case PendedRequest:
      return "pendedrequest";
    case List:
      return "list";
    case DeviceUseStatement:
      return "deviceusestatement";
    case Goal:
      return "goal";
    case NutritionOrder:
      return "nutritionorder";
    case SearchParameter:
      return "searchparameter";
    case ReferralRequest:
      return "referralrequest";
    case FamilyHistory:
      return "familyhistory";
    case EnrollmentRequest:
      return "enrollmentrequest";
    case Location:
      return "location";
    case Contract:
      return "contract";
    case Basic:
      return "basic";
    case Specimen:
      return "specimen";
    case EnrollmentResponse:
      return "enrollmentresponse";
    case Patient:
      return "patient";
    case CarePlan2:
      return "careplan2";
    case DiagnosticOrder:
      return "diagnosticorder";
    case Parameters:
      return "parameters";
    }
      return null;
  }

}
