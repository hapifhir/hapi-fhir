package org.hl7.fhir.dstu3.model;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

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
    CodeSystem,
    Communication,
    CommunicationRequest,
    Composition,
    ConceptMap,
    Condition,
    Conformance,
    Contract,
    Coverage,
    DataElement,
    DecisionSupportRule,
    DecisionSupportServiceModule,
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
    ExpansionProfile,
    ExplanationOfBenefit,
    FamilyMemberHistory,
    Flag,
    Goal,
    Group,
    GuidanceResponse,
    HealthcareService,
    ImagingObjectSelection,
    ImagingStudy,
    Immunization,
    ImmunizationRecommendation,
    ImplementationGuide,
    Library,
    Linkage,
    List,
    Location,
    Measure,
    Media,
    Medication,
    MedicationAdministration,
    MedicationDispense,
    MedicationOrder,
    MedicationStatement,
    MessageHeader,
    ModuleDefinition,
    ModuleMetadata,
    NamingSystem,
    NutritionOrder,
    Observation,
    OperationDefinition,
    OperationOutcome,
    Order,
    OrderResponse,
    OrderSet,
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
    Protocol,
    Provenance,
    Questionnaire,
    QuestionnaireResponse,
    ReferralRequest,
    RelatedPerson,
    RiskAssessment,
    Schedule,
    SearchParameter,
    Sequence,
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
    case CodeSystem:
      return "codesystem";
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
    case DecisionSupportRule:
      return "decisionsupportrule";
    case DecisionSupportServiceModule:
      return "decisionsupportservicemodule";
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
    case ExpansionProfile:
      return "expansionprofile";
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
    case GuidanceResponse:
      return "guidanceresponse";
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
    case ModuleDefinition:
      return "moduledefinition";
    case ModuleMetadata:
      return "modulemetadata";
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
    case OrderSet:
      return "orderset";
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
    case Protocol:
      return "protocol";
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
    case Sequence:
      return "sequence";
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


    public static ResourceType fromCode(String code) throws FHIRException {;
    if ("Account".equals(code))
      return Account;
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
    if ("BodySite".equals(code))
      return BodySite;
    if ("Bundle".equals(code))
      return Bundle;
    if ("CarePlan".equals(code))
      return CarePlan;
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
    if ("Composition".equals(code))
      return Composition;
    if ("ConceptMap".equals(code))
      return ConceptMap;
    if ("Condition".equals(code))
      return Condition;
    if ("Conformance".equals(code))
      return Conformance;
    if ("Contract".equals(code))
      return Contract;
    if ("Coverage".equals(code))
      return Coverage;
    if ("DataElement".equals(code))
      return DataElement;
    if ("DecisionSupportRule".equals(code))
      return DecisionSupportRule;
    if ("DecisionSupportServiceModule".equals(code))
      return DecisionSupportServiceModule;
    if ("DetectedIssue".equals(code))
      return DetectedIssue;
    if ("Device".equals(code))
      return Device;
    if ("DeviceComponent".equals(code))
      return DeviceComponent;
    if ("DeviceMetric".equals(code))
      return DeviceMetric;
    if ("DeviceUseRequest".equals(code))
      return DeviceUseRequest;
    if ("DeviceUseStatement".equals(code))
      return DeviceUseStatement;
    if ("DiagnosticOrder".equals(code))
      return DiagnosticOrder;
    if ("DiagnosticReport".equals(code))
      return DiagnosticReport;
    if ("DocumentManifest".equals(code))
      return DocumentManifest;
    if ("DocumentReference".equals(code))
      return DocumentReference;
    if ("EligibilityRequest".equals(code))
      return EligibilityRequest;
    if ("EligibilityResponse".equals(code))
      return EligibilityResponse;
    if ("Encounter".equals(code))
      return Encounter;
    if ("EnrollmentRequest".equals(code))
      return EnrollmentRequest;
    if ("EnrollmentResponse".equals(code))
      return EnrollmentResponse;
    if ("EpisodeOfCare".equals(code))
      return EpisodeOfCare;
    if ("ExpansionProfile".equals(code))
      return ExpansionProfile;
    if ("ExplanationOfBenefit".equals(code))
      return ExplanationOfBenefit;
    if ("FamilyMemberHistory".equals(code))
      return FamilyMemberHistory;
    if ("Flag".equals(code))
      return Flag;
    if ("Goal".equals(code))
      return Goal;
    if ("Group".equals(code))
      return Group;
    if ("GuidanceResponse".equals(code))
      return GuidanceResponse;
    if ("HealthcareService".equals(code))
      return HealthcareService;
    if ("ImagingObjectSelection".equals(code))
      return ImagingObjectSelection;
    if ("ImagingStudy".equals(code))
      return ImagingStudy;
    if ("Immunization".equals(code))
      return Immunization;
    if ("ImmunizationRecommendation".equals(code))
      return ImmunizationRecommendation;
    if ("ImplementationGuide".equals(code))
      return ImplementationGuide;
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
    if ("Media".equals(code))
      return Media;
    if ("Medication".equals(code))
      return Medication;
    if ("MedicationAdministration".equals(code))
      return MedicationAdministration;
    if ("MedicationDispense".equals(code))
      return MedicationDispense;
    if ("MedicationOrder".equals(code))
      return MedicationOrder;
    if ("MedicationStatement".equals(code))
      return MedicationStatement;
    if ("MessageHeader".equals(code))
      return MessageHeader;
    if ("ModuleDefinition".equals(code))
      return ModuleDefinition;
    if ("ModuleMetadata".equals(code))
      return ModuleMetadata;
    if ("NamingSystem".equals(code))
      return NamingSystem;
    if ("NutritionOrder".equals(code))
      return NutritionOrder;
    if ("Observation".equals(code))
      return Observation;
    if ("OperationDefinition".equals(code))
      return OperationDefinition;
    if ("OperationOutcome".equals(code))
      return OperationOutcome;
    if ("Order".equals(code))
      return Order;
    if ("OrderResponse".equals(code))
      return OrderResponse;
    if ("OrderSet".equals(code))
      return OrderSet;
    if ("Organization".equals(code))
      return Organization;
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
    if ("Practitioner".equals(code))
      return Practitioner;
    if ("Procedure".equals(code))
      return Procedure;
    if ("ProcedureRequest".equals(code))
      return ProcedureRequest;
    if ("ProcessRequest".equals(code))
      return ProcessRequest;
    if ("ProcessResponse".equals(code))
      return ProcessResponse;
    if ("Protocol".equals(code))
      return Protocol;
    if ("Provenance".equals(code))
      return Provenance;
    if ("Questionnaire".equals(code))
      return Questionnaire;
    if ("QuestionnaireResponse".equals(code))
      return QuestionnaireResponse;
    if ("ReferralRequest".equals(code))
      return ReferralRequest;
    if ("RelatedPerson".equals(code))
      return RelatedPerson;
    if ("RiskAssessment".equals(code))
      return RiskAssessment;
    if ("Schedule".equals(code))
      return Schedule;
    if ("SearchParameter".equals(code))
      return SearchParameter;
    if ("Sequence".equals(code))
      return Sequence;
    if ("Slot".equals(code))
      return Slot;
    if ("Specimen".equals(code))
      return Specimen;
    if ("StructureDefinition".equals(code))
      return StructureDefinition;
    if ("Subscription".equals(code))
      return Subscription;
    if ("Substance".equals(code))
      return Substance;
    if ("SupplyDelivery".equals(code))
      return SupplyDelivery;
    if ("SupplyRequest".equals(code))
      return SupplyRequest;
    if ("TestScript".equals(code))
      return TestScript;
    if ("ValueSet".equals(code))
      return ValueSet;
    if ("VisionPrescription".equals(code))
      return VisionPrescription;

    throw new FHIRException("Unknown resource type"+code);
  }

}
