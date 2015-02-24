package org.hl7.fhir.instance.model;

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

public enum ResourceType {
    Appointment,
    ReferralRequest,
    OralHealthClaim,
    DocumentManifest,
    Goal,
    EnrollmentRequest,
    FamilyHistory,
    Medication,
    Subscription,
    DocumentReference,
    SecurityEvent,
    Conformance,
    RelatedPerson,
    Practitioner,
    Slot,
    Contraindication,
    Contract,
    Person,
    RiskAssessment,
    Group,
    PaymentNotice,
    Organization,
    Supply,
    ImagingStudy,
    Profile,
    DeviceComponent,
    Encounter,
    Substance,
    SearchParameter,
    Communication,
    OrderResponse,
    StatusResponse,
    VisionClaim,
    DeviceUseStatement,
    Reversal,
    MessageHeader,
    PharmacyClaim,
    ImmunizationRecommendation,
    Other,
    BodySite,
    Provenance,
    Questionnaire,
    ProfessionalClaim,
    ExplanationOfBenefit,
    InstitutionalClaim,
    Specimen,
    AllergyIntolerance,
    CarePlan,
    StructureDefinition,
    EpisodeOfCare,
    MedicationPrescription,
    OperationOutcome,
    Procedure,
    List,
    ConceptMap,
    ValueSet,
    OperationDefinition,
    Order,
    Immunization,
    Device,
    VisionPrescription,
    Media,
    ProcedureRequest,
    EligibilityResponse,
    DeviceUseRequest,
    DeviceMetric,
    AppointmentResponse,
    Observation,
    MedicationAdministration,
    EnrollmentResponse,
    Binary,
    MedicationStatement,
    CommunicationRequest,
    PendedRequest,
    Basic,
    StatusRequest,
    ClaimResponse,
    EligibilityRequest,
    MedicationDispense,
    DiagnosticReport,
    ImagingObjectSelection,
    HealthcareService,
    CarePlan2,
    DataElement,
    ClinicalAssessment,
    QuestionnaireAnswers,
    Readjudicate,
    NutritionOrder,
    PaymentReconciliation,
    Condition,
    Composition,
    Bundle,
    DiagnosticOrder,
    Patient,
    Coverage,
    ExtensionDefinition,
    NamingSystem,
    Schedule,
    SupportingDocumentation,
    Alert,
    Location,
    Parameters;


    public String getPath() {;
      switch (this) {
    case Appointment:
      return "appointment";
    case ReferralRequest:
      return "referralrequest";
    case OralHealthClaim:
      return "oralhealthclaim";
    case DocumentManifest:
      return "documentmanifest";
    case Goal:
      return "goal";
    case EnrollmentRequest:
      return "enrollmentrequest";
    case FamilyHistory:
      return "familyhistory";
    case Medication:
      return "medication";
    case Subscription:
      return "subscription";
    case DocumentReference:
      return "documentreference";
    case SecurityEvent:
      return "securityevent";
    case Conformance:
      return "conformance";
    case RelatedPerson:
      return "relatedperson";
    case Practitioner:
      return "practitioner";
    case Slot:
      return "slot";
    case Contraindication:
      return "contraindication";
    case Contract:
      return "contract";
    case Person:
      return "person";
    case RiskAssessment:
      return "riskassessment";
    case Group:
      return "group";
    case PaymentNotice:
      return "paymentnotice";
    case Organization:
      return "organization";
    case Supply:
      return "supply";
    case ImagingStudy:
      return "imagingstudy";
    case Profile:
      return "profile";
    case DeviceComponent:
      return "devicecomponent";
    case Encounter:
      return "encounter";
    case Substance:
      return "substance";
    case SearchParameter:
      return "searchparameter";
    case Communication:
      return "communication";
    case OrderResponse:
      return "orderresponse";
    case StatusResponse:
      return "statusresponse";
    case VisionClaim:
      return "visionclaim";
    case DeviceUseStatement:
      return "deviceusestatement";
    case Reversal:
      return "reversal";
    case MessageHeader:
      return "messageheader";
    case PharmacyClaim:
      return "pharmacyclaim";
    case ImmunizationRecommendation:
      return "immunizationrecommendation";
    case Other:
      return "other";
    case BodySite:
      return "bodysite";
    case Provenance:
      return "provenance";
    case Questionnaire:
      return "questionnaire";
    case ProfessionalClaim:
      return "professionalclaim";
    case ExplanationOfBenefit:
      return "explanationofbenefit";
    case InstitutionalClaim:
      return "institutionalclaim";
    case Specimen:
      return "specimen";
    case AllergyIntolerance:
      return "allergyintolerance";
    case CarePlan:
      return "careplan";
    case StructureDefinition:
      return "structuredefinition";
    case EpisodeOfCare:
      return "episodeofcare";
    case MedicationPrescription:
      return "medicationprescription";
    case OperationOutcome:
      return "operationoutcome";
    case Procedure:
      return "procedure";
    case List:
      return "list";
    case ConceptMap:
      return "conceptmap";
    case ValueSet:
      return "valueset";
    case OperationDefinition:
      return "operationdefinition";
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
    case ProcedureRequest:
      return "procedurerequest";
    case EligibilityResponse:
      return "eligibilityresponse";
    case DeviceUseRequest:
      return "deviceuserequest";
    case DeviceMetric:
      return "devicemetric";
    case AppointmentResponse:
      return "appointmentresponse";
    case Observation:
      return "observation";
    case MedicationAdministration:
      return "medicationadministration";
    case EnrollmentResponse:
      return "enrollmentresponse";
    case Binary:
      return "binary";
    case MedicationStatement:
      return "medicationstatement";
    case CommunicationRequest:
      return "communicationrequest";
    case PendedRequest:
      return "pendedrequest";
    case Basic:
      return "basic";
    case StatusRequest:
      return "statusrequest";
    case ClaimResponse:
      return "claimresponse";
    case EligibilityRequest:
      return "eligibilityrequest";
    case MedicationDispense:
      return "medicationdispense";
    case DiagnosticReport:
      return "diagnosticreport";
    case ImagingObjectSelection:
      return "imagingobjectselection";
    case HealthcareService:
      return "healthcareservice";
    case CarePlan2:
      return "careplan2";
    case DataElement:
      return "dataelement";
    case ClinicalAssessment:
      return "clinicalassessment";
    case QuestionnaireAnswers:
      return "questionnaireanswers";
    case Readjudicate:
      return "readjudicate";
    case NutritionOrder:
      return "nutritionorder";
    case PaymentReconciliation:
      return "paymentreconciliation";
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
    case Coverage:
      return "coverage";
    case ExtensionDefinition:
      return "extensiondefinition";
    case NamingSystem:
      return "namingsystem";
    case Schedule:
      return "schedule";
    case SupportingDocumentation:
      return "supportingdocumentation";
    case Alert:
      return "alert";
    case Location:
      return "location";
    case Parameters:
      return "parameters";
    }
      return null;
  }

}
