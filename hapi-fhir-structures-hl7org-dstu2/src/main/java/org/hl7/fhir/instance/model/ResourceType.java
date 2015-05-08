package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR Structures - HL7.org DSTU2
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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


// Generated on Tue, May 5, 2015 16:13-0400 for FHIR v0.5.0

public enum ResourceType {
    Condition,
    Supply,
    ProcedureRequest,
    DeviceComponent,
    DeviceMetric,
    Communication,
    Organization,
    ProcessRequest,
    Group,
    ValueSet,
    Coverage,
    ImmunizationRecommendation,
    Appointment,
    MedicationDispense,
    MedicationPrescription,
    Slot,
    PaymentNotice,
    Contraindication,
    AppointmentResponse,
    MedicationStatement,
    EpisodeOfCare,
    Questionnaire,
    Composition,
    OperationOutcome,
    Conformance,
    FamilyMemberHistory,
    NamingSystem,
    Media,
    Binary,
    HealthcareService,
    VisionPrescription,
    DocumentReference,
    Immunization,
    Bundle,
    Subscription,
    OrderResponse,
    ConceptMap,
    ImagingStudy,
    Practitioner,
    CarePlan,
    Provenance,
    Device,
    StructureDefinition,
    Order,
    Procedure,
    Substance,
    DeviceUseRequest,
    DiagnosticReport,
    Medication,
    MessageHeader,
    SupplyDelivery,
    Schedule,
    DocumentManifest,
    DataElement,
    EligibilityRequest,
    QuestionnaireAnswers,
    MedicationAdministration,
    Encounter,
    PaymentReconciliation,
    List,
    DeviceUseStatement,
    OperationDefinition,
    Goal,
    ImagingObjectSelection,
    SearchParameter,
    NutritionOrder,
    ClaimResponse,
    ReferralRequest,
    ClinicalImpression,
    BodySite,
    Flag,
    CommunicationRequest,
    Claim,
    RiskAssessment,
    EnrollmentRequest,
    Location,
    ExplanationOfBenefit,
    AllergyIntolerance,
    Observation,
    Contract,
    RelatedPerson,
    Basic,
    ProcessResponse,
    Specimen,
    AuditEvent,
    EnrollmentResponse,
    SupplyRequest,
    Patient,
    EligibilityResponse,
    Person,
    DiagnosticOrder,
    Parameters;


    public String getPath() {;
      switch (this) {
    case Condition:
      return "condition";
    case Supply:
      return "supply";
    case ProcedureRequest:
      return "procedurerequest";
    case DeviceComponent:
      return "devicecomponent";
    case DeviceMetric:
      return "devicemetric";
    case Communication:
      return "communication";
    case Organization:
      return "organization";
    case ProcessRequest:
      return "processrequest";
    case Group:
      return "group";
    case ValueSet:
      return "valueset";
    case Coverage:
      return "coverage";
    case ImmunizationRecommendation:
      return "immunizationrecommendation";
    case Appointment:
      return "appointment";
    case MedicationDispense:
      return "medicationdispense";
    case MedicationPrescription:
      return "medicationprescription";
    case Slot:
      return "slot";
    case PaymentNotice:
      return "paymentnotice";
    case Contraindication:
      return "contraindication";
    case AppointmentResponse:
      return "appointmentresponse";
    case MedicationStatement:
      return "medicationstatement";
    case EpisodeOfCare:
      return "episodeofcare";
    case Questionnaire:
      return "questionnaire";
    case Composition:
      return "composition";
    case OperationOutcome:
      return "operationoutcome";
    case Conformance:
      return "conformance";
    case FamilyMemberHistory:
      return "familymemberhistory";
    case NamingSystem:
      return "namingsystem";
    case Media:
      return "media";
    case Binary:
      return "binary";
    case HealthcareService:
      return "healthcareservice";
    case VisionPrescription:
      return "visionprescription";
    case DocumentReference:
      return "documentreference";
    case Immunization:
      return "immunization";
    case Bundle:
      return "bundle";
    case Subscription:
      return "subscription";
    case OrderResponse:
      return "orderresponse";
    case ConceptMap:
      return "conceptmap";
    case ImagingStudy:
      return "imagingstudy";
    case Practitioner:
      return "practitioner";
    case CarePlan:
      return "careplan";
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
    case Substance:
      return "substance";
    case DeviceUseRequest:
      return "deviceuserequest";
    case DiagnosticReport:
      return "diagnosticreport";
    case Medication:
      return "medication";
    case MessageHeader:
      return "messageheader";
    case SupplyDelivery:
      return "supplydelivery";
    case Schedule:
      return "schedule";
    case DocumentManifest:
      return "documentmanifest";
    case DataElement:
      return "dataelement";
    case EligibilityRequest:
      return "eligibilityrequest";
    case QuestionnaireAnswers:
      return "questionnaireanswers";
    case MedicationAdministration:
      return "medicationadministration";
    case Encounter:
      return "encounter";
    case PaymentReconciliation:
      return "paymentreconciliation";
    case List:
      return "list";
    case DeviceUseStatement:
      return "deviceusestatement";
    case OperationDefinition:
      return "operationdefinition";
    case Goal:
      return "goal";
    case ImagingObjectSelection:
      return "imagingobjectselection";
    case SearchParameter:
      return "searchparameter";
    case NutritionOrder:
      return "nutritionorder";
    case ClaimResponse:
      return "claimresponse";
    case ReferralRequest:
      return "referralrequest";
    case ClinicalImpression:
      return "clinicalimpression";
    case BodySite:
      return "bodysite";
    case Flag:
      return "flag";
    case CommunicationRequest:
      return "communicationrequest";
    case Claim:
      return "claim";
    case RiskAssessment:
      return "riskassessment";
    case EnrollmentRequest:
      return "enrollmentrequest";
    case Location:
      return "location";
    case ExplanationOfBenefit:
      return "explanationofbenefit";
    case AllergyIntolerance:
      return "allergyintolerance";
    case Observation:
      return "observation";
    case Contract:
      return "contract";
    case RelatedPerson:
      return "relatedperson";
    case Basic:
      return "basic";
    case ProcessResponse:
      return "processresponse";
    case Specimen:
      return "specimen";
    case AuditEvent:
      return "auditevent";
    case EnrollmentResponse:
      return "enrollmentresponse";
    case SupplyRequest:
      return "supplyrequest";
    case Patient:
      return "patient";
    case EligibilityResponse:
      return "eligibilityresponse";
    case Person:
      return "person";
    case DiagnosticOrder:
      return "diagnosticorder";
    case Parameters:
      return "parameters";
    }
      return null;
  }

}
