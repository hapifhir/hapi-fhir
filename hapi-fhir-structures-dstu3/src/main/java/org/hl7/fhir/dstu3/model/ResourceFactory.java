package org.hl7.fhir.dstu3.model;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

import org.hl7.fhir.exceptions.FHIRException;

public class ResourceFactory extends Factory {

    public static Resource createResource(String name) throws FHIRException {
        if ("Appointment".equals(name))
            return new Appointment();
        if ("ReferralRequest".equals(name))
            return new ReferralRequest();
        if ("Account".equals(name))
            return new Account();
        if ("DocumentManifest".equals(name))
            return new DocumentManifest();
        if ("MessageDefinition".equals(name))
            return new MessageDefinition();
        if ("Goal".equals(name))
            return new Goal();
        if ("Endpoint".equals(name))
            return new Endpoint();
        if ("EnrollmentRequest".equals(name))
            return new EnrollmentRequest();
        if ("Consent".equals(name))
            return new Consent();
        if ("CapabilityStatement".equals(name))
            return new CapabilityStatement();
        if ("Medication".equals(name))
            return new Medication();
        if ("Measure".equals(name))
            return new Measure();
        if ("ResearchSubject".equals(name))
            return new ResearchSubject();
        if ("Subscription".equals(name))
            return new Subscription();
        if ("DocumentReference".equals(name))
            return new DocumentReference();
        if ("GraphDefinition".equals(name))
            return new GraphDefinition();
        if ("ImagingManifest".equals(name))
            return new ImagingManifest();
        if ("Parameters".equals(name))
            return new Parameters();
        if ("MeasureReport".equals(name))
            return new MeasureReport();
        if ("PractitionerRole".equals(name))
            return new PractitionerRole();
        if ("RelatedPerson".equals(name))
            return new RelatedPerson();
        if ("SupplyRequest".equals(name))
            return new SupplyRequest();
        if ("Practitioner".equals(name))
            return new Practitioner();
        if ("ExpansionProfile".equals(name))
            return new ExpansionProfile();
        if ("Slot".equals(name))
            return new Slot();
        if ("Person".equals(name))
            return new Person();
        if ("Contract".equals(name))
            return new Contract();
        if ("RiskAssessment".equals(name))
            return new RiskAssessment();
        if ("Group".equals(name))
            return new Group();
        if ("PaymentNotice".equals(name))
            return new PaymentNotice();
        if ("Organization".equals(name))
            return new Organization();
        if ("CareTeam".equals(name))
            return new CareTeam();
        if ("ImplementationGuide".equals(name))
            return new ImplementationGuide();
        if ("ImagingStudy".equals(name))
            return new ImagingStudy();
        if ("DeviceComponent".equals(name))
            return new DeviceComponent();
        if ("FamilyMemberHistory".equals(name))
            return new FamilyMemberHistory();
        if ("ChargeItem".equals(name))
            return new ChargeItem();
        if ("Encounter".equals(name))
            return new Encounter();
        if ("Substance".equals(name))
            return new Substance();
        if ("SearchParameter".equals(name))
            return new SearchParameter();
        if ("ServiceDefinition".equals(name))
            return new ServiceDefinition();
        if ("Communication".equals(name))
            return new Communication();
        if ("ActivityDefinition".equals(name))
            return new ActivityDefinition();
        if ("Linkage".equals(name))
            return new Linkage();
        if ("DeviceUseStatement".equals(name))
            return new DeviceUseStatement();
        if ("RequestGroup".equals(name))
            return new RequestGroup();
        if ("DeviceRequest".equals(name))
            return new DeviceRequest();
        if ("MessageHeader".equals(name))
            return new MessageHeader();
        if ("ImmunizationRecommendation".equals(name))
            return new ImmunizationRecommendation();
        if ("BodySite".equals(name))
            return new BodySite();
        if ("Provenance".equals(name))
            return new Provenance();
        if ("Task".equals(name))
            return new Task();
        if ("Questionnaire".equals(name))
            return new Questionnaire();
        if ("ExplanationOfBenefit".equals(name))
            return new ExplanationOfBenefit();
        if ("ResearchStudy".equals(name))
            return new ResearchStudy();
        if ("Specimen".equals(name))
            return new Specimen();
        if ("AllergyIntolerance".equals(name))
            return new AllergyIntolerance();
        if ("CarePlan".equals(name))
            return new CarePlan();
        if ("StructureDefinition".equals(name))
            return new StructureDefinition();
        if ("EpisodeOfCare".equals(name))
            return new EpisodeOfCare();
        if ("OperationOutcome".equals(name))
            return new OperationOutcome();
        if ("Procedure".equals(name))
            return new Procedure();
        if ("List".equals(name))
            return new ListResource();
        if ("ConceptMap".equals(name))
            return new ConceptMap();
        if ("ValueSet".equals(name))
            return new ValueSet();
        if ("OperationDefinition".equals(name))
            return new OperationDefinition();
        if ("Immunization".equals(name))
            return new Immunization();
        if ("MedicationRequest".equals(name))
            return new MedicationRequest();
        if ("Device".equals(name))
            return new Device();
        if ("VisionPrescription".equals(name))
            return new VisionPrescription();
        if ("Media".equals(name))
            return new Media();
        if ("ProcedureRequest".equals(name))
            return new ProcedureRequest();
        if ("EligibilityResponse".equals(name))
            return new EligibilityResponse();
        if ("Sequence".equals(name))
            return new Sequence();
        if ("DeviceMetric".equals(name))
            return new DeviceMetric();
        if ("Flag".equals(name))
            return new Flag();
        if ("CodeSystem".equals(name))
            return new CodeSystem();
        if ("AppointmentResponse".equals(name))
            return new AppointmentResponse();
        if ("StructureMap".equals(name))
            return new StructureMap();
        if ("AdverseEvent".equals(name))
            return new AdverseEvent();
        if ("GuidanceResponse".equals(name))
            return new GuidanceResponse();
        if ("Observation".equals(name))
            return new Observation();
        if ("MedicationAdministration".equals(name))
            return new MedicationAdministration();
        if ("EnrollmentResponse".equals(name))
            return new EnrollmentResponse();
        if ("Binary".equals(name))
            return new Binary();
        if ("Library".equals(name))
            return new Library();
        if ("MedicationStatement".equals(name))
            return new MedicationStatement();
        if ("CommunicationRequest".equals(name))
            return new CommunicationRequest();
        if ("TestScript".equals(name))
            return new TestScript();
        if ("Basic".equals(name))
            return new Basic();
        if ("TestReport".equals(name))
            return new TestReport();
        if ("ClaimResponse".equals(name))
            return new ClaimResponse();
        if ("EligibilityRequest".equals(name))
            return new EligibilityRequest();
        if ("ProcessRequest".equals(name))
            return new ProcessRequest();
        if ("MedicationDispense".equals(name))
            return new MedicationDispense();
        if ("DiagnosticReport".equals(name))
            return new DiagnosticReport();
        if ("HealthcareService".equals(name))
            return new HealthcareService();
        if ("DataElement".equals(name))
            return new DataElement();
        if ("NutritionOrder".equals(name))
            return new NutritionOrder();
        if ("AuditEvent".equals(name))
            return new AuditEvent();
        if ("PaymentReconciliation".equals(name))
            return new PaymentReconciliation();
        if ("Condition".equals(name))
            return new Condition();
        if ("Composition".equals(name))
            return new Composition();
        if ("DetectedIssue".equals(name))
            return new DetectedIssue();
        if ("Bundle".equals(name))
            return new Bundle();
        if ("CompartmentDefinition".equals(name))
            return new CompartmentDefinition();
        if ("Patient".equals(name))
            return new Patient();
        if ("Coverage".equals(name))
            return new Coverage();
        if ("QuestionnaireResponse".equals(name))
            return new QuestionnaireResponse();
        if ("ProcessResponse".equals(name))
            return new ProcessResponse();
        if ("NamingSystem".equals(name))
            return new NamingSystem();
        if ("Schedule".equals(name))
            return new Schedule();
        if ("SupplyDelivery".equals(name))
            return new SupplyDelivery();
        if ("ClinicalImpression".equals(name))
            return new ClinicalImpression();
        if ("PlanDefinition".equals(name))
            return new PlanDefinition();
        if ("Claim".equals(name))
            return new Claim();
        if ("Location".equals(name))
            return new Location();
        else
            throw new FHIRException("Unknown Resource Name '"+name+"'");
    }

    public static Element createType(String name) throws FHIRException {
        if ("date".equals(name))
            return new DateType();
        if ("Meta".equals(name))
            return new Meta();
        if ("Address".equals(name))
            return new Address();
        if ("Attachment".equals(name))
            return new Attachment();
        if ("integer".equals(name))
            return new IntegerType();
        if ("Count".equals(name))
            return new Count();
        if ("DataRequirement".equals(name))
            return new DataRequirement();
        if ("Dosage".equals(name))
            return new Dosage();
        if ("uuid".equals(name))
            return new UuidType();
        if ("Identifier".equals(name))
            return new Identifier();
        if ("Narrative".equals(name))
            return new Narrative();
        if ("Coding".equals(name))
            return new Coding();
        if ("SampledData".equals(name))
            return new SampledData();
        if ("id".equals(name))
            return new IdType();
        if ("positiveInt".equals(name))
            return new PositiveIntType();
        if ("ElementDefinition".equals(name))
            return new ElementDefinition();
        if ("Distance".equals(name))
            return new Distance();
        if ("Period".equals(name))
            return new Period();
        if ("Duration".equals(name))
            return new Duration();
        if ("Range".equals(name))
            return new Range();
        if ("RelatedArtifact".equals(name))
            return new RelatedArtifact();
        if ("base64Binary".equals(name))
            return new Base64BinaryType();
        if ("UsageContext".equals(name))
            return new UsageContext();
        if ("Timing".equals(name))
            return new Timing();
        if ("decimal".equals(name))
            return new DecimalType();
        if ("CodeableConcept".equals(name))
            return new CodeableConcept();
        if ("ParameterDefinition".equals(name))
            return new ParameterDefinition();
        if ("dateTime".equals(name))
            return new DateTimeType();
        if ("code".equals(name))
            return new CodeType();
        if ("string".equals(name))
            return new StringType();
        if ("Contributor".equals(name))
            return new Contributor();
        if ("oid".equals(name))
            return new OidType();
        if ("instant".equals(name))
            return new InstantType();
        if ("Money".equals(name))
            return new Money();
        if ("HumanName".equals(name))
            return new HumanName();
        if ("ContactPoint".equals(name))
            return new ContactPoint();
        if ("markdown".equals(name))
            return new MarkdownType();
        if ("Ratio".equals(name))
            return new Ratio();
        if ("Age".equals(name))
            return new Age();
        if ("Reference".equals(name))
            return new Reference();
        if ("TriggerDefinition".equals(name))
            return new TriggerDefinition();
        if ("SimpleQuantity".equals(name))
            return new SimpleQuantity();
        if ("Quantity".equals(name))
            return new Quantity();
        if ("uri".equals(name))
            return new UriType();
        if ("Annotation".equals(name))
            return new Annotation();
        if ("Extension".equals(name))
            return new Extension();
        if ("ContactDetail".equals(name))
            return new ContactDetail();
        if ("boolean".equals(name))
            return new BooleanType();
        if ("Signature".equals(name))
            return new Signature();
        if ("unsignedInt".equals(name))
            return new UnsignedIntType();
        if ("time".equals(name))
            return new TimeType();
        else
            throw new FHIRException("Unknown Type Name '"+name+"'");
    }

    public static Base createResourceOrType(String name) throws FHIRException {
      switch (name.hashCode()) {
        case 487334413: return new Account();
        case 851278306: return new ActivityDefinition();
        case 516961236: return new Address();
        case -329624856: return new AdverseEvent();
        case 65759: return new Age();
        case 1721380104: return new AllergyIntolerance();
        case 438421327: return new Annotation();
        case 192873343: return new Appointment();
        case 1733332192: return new AppointmentResponse();
        case 29963587: return new Attachment();
        case -632949857: return new AuditEvent();
        case 63955982: return new Basic();
        case 1989867553: return new Binary();
        case 1767264297: return new BodySite();
        case 2000952482: return new Bundle();
        case -871422185: return new CapabilityStatement();
        case 57208314: return new CarePlan();
        case 57320750: return new CareTeam();
        case -883723257: return new ChargeItem();
        case 65189916: return new Claim();
        case 1488475261: return new ClaimResponse();
        case -1268501092: return new ClinicalImpression();
        case 1076953756: return new CodeSystem();
        case -1153521791: return new CodeableConcept();
        case 2023747466: return new Coding();
        case -236322890: return new Communication();
        case -1874423303: return new CommunicationRequest();
        case 1287805733: return new CompartmentDefinition();
        case 828944778: return new Composition();
        case 57185780: return new ConceptMap();
        case 1142656251: return new Condition();
        case -1678813190: return new Consent();
        case 973193329: return new ContactDetail();
        case 1428236656: return new ContactPoint();
        case -502303438: return new Contract();
        case -227407685: return new Contributor();
        case 65298671: return new Count();
        case -287122936: return new Coverage();
        case -1476174894: return new DataElement();
        case -367870439: return new DataRequirement();
        case 850563927: return new DetectedIssue();
        case 2043677302: return new Device();
        case 745969447: return new DeviceComponent();
        case -949306426: return new DeviceMetric();
        case 776138553: return new DeviceRequest();
        case 491858238: return new DeviceUseStatement();
        case -1122842661: return new DiagnosticReport();
        case 353103893: return new Distance();
        case 1922784394: return new DocumentManifest();
        case -1202791344: return new DocumentReference();
        case 2052815575: return new Dosage();
        case -1927368268: return new Duration();
        case -1605049009: return new ElementDefinition();
        case -1197000094: return new EligibilityRequest();
        case 1600636622: return new EligibilityResponse();
        case -766867181: return new Encounter();
        case 1805746613: return new Endpoint();
        case -1377846581: return new EnrollmentRequest();
        case 289362821: return new EnrollmentResponse();
        case -1093178557: return new EpisodeOfCare();
        case -1136815094: return new ExpansionProfile();
        case -1001676601: return new ExplanationOfBenefit();
        case 1391410207: return new Extension();
        case 1260711798: return new FamilyMemberHistory();
        case 2192268: return new Flag();
        case 2224947: return new Goal();
        case -180371167: return new GraphDefinition();
        case 69076575: return new Group();
        case 997117913: return new GuidanceResponse();
        case 933423720: return new HealthcareService();
        case 1592332600: return new HumanName();
        case 375032009: return new Identifier();
        case -5812857: return new ImagingManifest();
        case -650580623: return new ImagingStudy();
        case -2004863454: return new Immunization();
        case 1728372347: return new ImmunizationRecommendation();
        case 1410262602: return new ImplementationGuide();
        case 1830861979: return new Library();
        case 1841735333: return new Linkage();
        case 2368702: return new ListResource();
        case 1965687765: return new Location();
        case -1691992770: return new Measure();
        case 1681397778: return new MeasureReport();
        case 74219460: return new Media();
        case -302536977: return new Medication();
        case -342579923: return new MedicationAdministration();
        case -408244884: return new MedicationDispense();
        case 1627523232: return new MedicationRequest();
        case -2097348800: return new MedicationStatement();
        case -2037697382: return new MessageDefinition();
        case -1087398572: return new MessageHeader();
        case 2394661: return new Meta();
        case 74526880: return new Money();
        case 369315063: return new NamingSystem();
        case -540546990: return new Narrative();
        case 1247831734: return new NutritionOrder();
        case 1790214156: return new Observation();
        case -2140710406: return new OperationDefinition();
        case -526550005: return new OperationOutcome();
        case 1343242579: return new Organization();
        case 671337916: return new ParameterDefinition();
        case -1842766326: return new Parameters();
        case 873235173: return new Patient();
        case 2082457694: return new PaymentNotice();
        case 28778089: return new PaymentReconciliation();
        case -1907858975: return new Period();
        case -1907849355: return new Person();
        case 1401244028: return new PlanDefinition();
        case 738893626: return new Practitioner();
        case -621058352: return new PractitionerRole();
        case 908763827: return new Procedure();
        case 737478748: return new ProcedureRequest();
        case 8777024: return new ProcessRequest();
        case 325021616: return new ProcessResponse();
        case 2093211201: return new Provenance();
        case -1220360021: return new Quantity();
        case -218088061: return new Questionnaire();
        case 269058788: return new QuestionnaireResponse();
        case 78727453: return new Range();
        case 78733291: return new Ratio();
        case 1078812459: return new Reference();
        case -1307317230: return new ReferralRequest();
        case -330210563: return new RelatedArtifact();
        case 846088000: return new RelatedPerson();
        case 1445374288: return new RequestGroup();
        case 1312904398: return new ResearchStudy();
        case -1008013583: return new ResearchSubject();
        case -766422255: return new RiskAssessment();
        case 1824308900: return new SampledData();
        case -633276745: return new Schedule();
        case -912457023: return new SearchParameter();
        case 1414192097: return new Sequence();
        case 194378184: return new ServiceDefinition();
        case -1217415016: return new Signature();
        case -1097468803: return new SimpleQuantity();
        case 2579998: return new Slot();
        case -2068224216: return new Specimen();
        case 1133777670: return new StructureDefinition();
        case 1958247177: return new StructureMap();
        case 505523517: return new Subscription();
        case -1760959152: return new Substance();
        case 383030819: return new SupplyDelivery();
        case 665843328: return new SupplyRequest();
        case 2599333: return new Task();
        case -616289146: return new TestReport();
        case -589453283: return new TestScript();
        case -1789797270: return new Timing();
        case 770498827: return new TriggerDefinition();
        case 1071332590: return new UsageContext();
        case -1345530543: return new ValueSet();
        case -555387838: return new VisionPrescription();
        case -1216012752: return new Base64BinaryType();
        case 64711720: return new BooleanType();
        case 3059181: return new CodeType();
        case 3076014: return new DateType();
        case 1792749467: return new DateTimeType();
        case 1542263633: return new DecimalType();
        case 3355: return new IdType();
        case 1957570017: return new InstantType();
        case 1958052158: return new IntegerType();
        case 246938863: return new MarkdownType();
        case 110026: return new OidType();
        case -131262666: return new PositiveIntType();
        case -891985903: return new StringType();
        case 3560141: return new TimeType();
        case 1145198778: return new UnsignedIntType();
        case 116076: return new UriType();
        case 3601339: return new UuidType();
      default:
        throw new FHIRException("Unknown Resource or Type Name '"+name+"'");
    }
  }


}

