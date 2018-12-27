package org.hl7.fhir.r4.model;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

import org.hl7.fhir.exceptions.FHIRException;

public class ResourceFactory extends Factory {

    public static Resource createResource(String name) throws FHIRException {
        if ("Appointment".equals(name))
            return new Appointment();
        if ("Account".equals(name))
            return new Account();
        if ("Invoice".equals(name))
            return new Invoice();
        if ("CatalogEntry".equals(name))
            return new CatalogEntry();
        if ("EventDefinition".equals(name))
            return new EventDefinition();
        if ("DocumentManifest".equals(name))
            return new DocumentManifest();
        if ("MessageDefinition".equals(name))
            return new MessageDefinition();
        if ("Goal".equals(name))
            return new Goal();
        if ("MedicinalProductPackaged".equals(name))
            return new MedicinalProductPackaged();
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
        if ("Parameters".equals(name))
            return new Parameters();
        if ("CoverageEligibilityResponse".equals(name))
            return new CoverageEligibilityResponse();
        if ("MeasureReport".equals(name))
            return new MeasureReport();
        if ("PractitionerRole".equals(name))
            return new PractitionerRole();
        if ("SubstanceReferenceInformation".equals(name))
            return new SubstanceReferenceInformation();
        if ("ServiceRequest".equals(name))
            return new ServiceRequest();
        if ("RelatedPerson".equals(name))
            return new RelatedPerson();
        if ("SupplyRequest".equals(name))
            return new SupplyRequest();
        if ("Practitioner".equals(name))
            return new Practitioner();
        if ("VerificationResult".equals(name))
            return new VerificationResult();
        if ("SubstanceProtein".equals(name))
            return new SubstanceProtein();
        if ("BodyStructure".equals(name))
            return new BodyStructure();
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
        if ("ResearchDefinition".equals(name))
            return new ResearchDefinition();
        if ("MedicinalProductManufactured".equals(name))
            return new MedicinalProductManufactured();
        if ("Organization".equals(name))
            return new Organization();
        if ("CareTeam".equals(name))
            return new CareTeam();
        if ("ImplementationGuide".equals(name))
            return new ImplementationGuide();
        if ("ImagingStudy".equals(name))
            return new ImagingStudy();
        if ("FamilyMemberHistory".equals(name))
            return new FamilyMemberHistory();
        if ("ChargeItem".equals(name))
            return new ChargeItem();
        if ("ResearchElementDefinition".equals(name))
            return new ResearchElementDefinition();
        if ("ObservationDefinition".equals(name))
            return new ObservationDefinition();
        if ("Encounter".equals(name))
            return new Encounter();
        if ("Substance".equals(name))
            return new Substance();
        if ("SubstanceSpecification".equals(name))
            return new SubstanceSpecification();
        if ("SearchParameter".equals(name))
            return new SearchParameter();
        if ("Communication".equals(name))
            return new Communication();
        if ("ActivityDefinition".equals(name))
            return new ActivityDefinition();
        if ("InsurancePlan".equals(name))
            return new InsurancePlan();
        if ("Linkage".equals(name))
            return new Linkage();
        if ("SubstanceSourceMaterial".equals(name))
            return new SubstanceSourceMaterial();
        if ("ImmunizationEvaluation".equals(name))
            return new ImmunizationEvaluation();
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
        if ("Provenance".equals(name))
            return new Provenance();
        if ("Task".equals(name))
            return new Task();
        if ("Questionnaire".equals(name))
            return new Questionnaire();
        if ("ExplanationOfBenefit".equals(name))
            return new ExplanationOfBenefit();
        if ("MedicinalProductPharmaceutical".equals(name))
            return new MedicinalProductPharmaceutical();
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
        if ("ChargeItemDefinition".equals(name))
            return new ChargeItemDefinition();
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
        if ("EffectEvidenceSynthesis".equals(name))
            return new EffectEvidenceSynthesis();
        if ("BiologicallyDerivedProduct".equals(name))
            return new BiologicallyDerivedProduct();
        if ("Device".equals(name))
            return new Device();
        if ("VisionPrescription".equals(name))
            return new VisionPrescription();
        if ("Media".equals(name))
            return new Media();
        if ("MedicinalProductContraindication".equals(name))
            return new MedicinalProductContraindication();
        if ("MolecularSequence".equals(name))
            return new MolecularSequence();
        if ("EvidenceVariable".equals(name))
            return new EvidenceVariable();
        if ("MedicinalProduct".equals(name))
            return new MedicinalProduct();
        if ("DeviceMetric".equals(name))
            return new DeviceMetric();
        if ("Flag".equals(name))
            return new Flag();
        if ("CodeSystem".equals(name))
            return new CodeSystem();
        if ("SubstanceNucleicAcid".equals(name))
            return new SubstanceNucleicAcid();
        if ("RiskEvidenceSynthesis".equals(name))
            return new RiskEvidenceSynthesis();
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
        if ("MedicinalProductInteraction".equals(name))
            return new MedicinalProductInteraction();
        if ("MedicationStatement".equals(name))
            return new MedicationStatement();
        if ("CommunicationRequest".equals(name))
            return new CommunicationRequest();
        if ("TestScript".equals(name))
            return new TestScript();
        if ("Basic".equals(name))
            return new Basic();
        if ("SubstancePolymer".equals(name))
            return new SubstancePolymer();
        if ("TestReport".equals(name))
            return new TestReport();
        if ("ClaimResponse".equals(name))
            return new ClaimResponse();
        if ("MedicationDispense".equals(name))
            return new MedicationDispense();
        if ("DiagnosticReport".equals(name))
            return new DiagnosticReport();
        if ("OrganizationAffiliation".equals(name))
            return new OrganizationAffiliation();
        if ("HealthcareService".equals(name))
            return new HealthcareService();
        if ("MedicinalProductIndication".equals(name))
            return new MedicinalProductIndication();
        if ("NutritionOrder".equals(name))
            return new NutritionOrder();
        if ("TerminologyCapabilities".equals(name))
            return new TerminologyCapabilities();
        if ("Evidence".equals(name))
            return new Evidence();
        if ("AuditEvent".equals(name))
            return new AuditEvent();
        if ("PaymentReconciliation".equals(name))
            return new PaymentReconciliation();
        if ("Condition".equals(name))
            return new Condition();
        if ("SpecimenDefinition".equals(name))
            return new SpecimenDefinition();
        if ("Composition".equals(name))
            return new Composition();
        if ("DetectedIssue".equals(name))
            return new DetectedIssue();
        if ("Bundle".equals(name))
            return new Bundle();
        if ("CompartmentDefinition".equals(name))
            return new CompartmentDefinition();
        if ("MedicinalProductIngredient".equals(name))
            return new MedicinalProductIngredient();
        if ("MedicationKnowledge".equals(name))
            return new MedicationKnowledge();
        if ("Patient".equals(name))
            return new Patient();
        if ("Coverage".equals(name))
            return new Coverage();
        if ("QuestionnaireResponse".equals(name))
            return new QuestionnaireResponse();
        if ("CoverageEligibilityRequest".equals(name))
            return new CoverageEligibilityRequest();
        if ("NamingSystem".equals(name))
            return new NamingSystem();
        if ("MedicinalProductUndesirableEffect".equals(name))
            return new MedicinalProductUndesirableEffect();
        if ("ExampleScenario".equals(name))
            return new ExampleScenario();
        if ("Schedule".equals(name))
            return new Schedule();
        if ("SupplyDelivery".equals(name))
            return new SupplyDelivery();
        if ("ClinicalImpression".equals(name))
            return new ClinicalImpression();
        if ("DeviceDefinition".equals(name))
            return new DeviceDefinition();
        if ("PlanDefinition".equals(name))
            return new PlanDefinition();
        if ("MedicinalProductAuthorization".equals(name))
            return new MedicinalProductAuthorization();
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
        if ("MoneyQuantity".equals(name))
            return new MoneyQuantity();
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
        if ("SubstanceAmount".equals(name))
            return new SubstanceAmount();
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
        if ("canonical".equals(name))
            return new CanonicalType();
        if ("Range".equals(name))
            return new Range();
        if ("RelatedArtifact".equals(name))
            return new RelatedArtifact();
        if ("ProductShelfLife".equals(name))
            return new ProductShelfLife();
        if ("base64Binary".equals(name))
            return new Base64BinaryType();
        if ("UsageContext".equals(name))
            return new UsageContext();
        if ("Timing".equals(name))
            return new Timing();
        if ("decimal".equals(name))
            return new DecimalType();
        if ("ProdCharacteristic".equals(name))
            return new ProdCharacteristic();
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
        if ("MarketingStatus".equals(name))
            return new MarketingStatus();
        if ("markdown".equals(name))
            return new MarkdownType();
        if ("Population".equals(name))
            return new Population();
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
        if ("url".equals(name))
            return new UrlType();
        if ("Annotation".equals(name))
            return new Annotation();
        if ("Extension".equals(name))
            return new Extension();
        if ("ContactDetail".equals(name))
            return new ContactDetail();
        if ("boolean".equals(name))
            return new BooleanType();
        if ("Expression".equals(name))
            return new Expression();
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
        case -310041824: return new BiologicallyDerivedProduct();
        case -202769967: return new BodyStructure();
        case 2000952482: return new Bundle();
        case -871422185: return new CapabilityStatement();
        case 57208314: return new CarePlan();
        case 57320750: return new CareTeam();
        case -1007602695: return new CatalogEntry();
        case -883723257: return new ChargeItem();
        case -315725638: return new ChargeItemDefinition();
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
        case -140860822: return new CoverageEligibilityRequest();
        case -18784314: return new CoverageEligibilityResponse();
        case -367870439: return new DataRequirement();
        case 850563927: return new DetectedIssue();
        case 2043677302: return new Device();
        case 1083244649: return new DeviceDefinition();
        case -949306426: return new DeviceMetric();
        case 776138553: return new DeviceRequest();
        case 491858238: return new DeviceUseStatement();
        case -1122842661: return new DiagnosticReport();
        case 353103893: return new Distance();
        case 1922784394: return new DocumentManifest();
        case -1202791344: return new DocumentReference();
        case 2052815575: return new Dosage();
        case -1927368268: return new Duration();
        case 2012162380: return new EffectEvidenceSynthesis();
        case -1605049009: return new ElementDefinition();
        case -766867181: return new Encounter();
        case 1805746613: return new Endpoint();
        case -1377846581: return new EnrollmentRequest();
        case 289362821: return new EnrollmentResponse();
        case -1093178557: return new EpisodeOfCare();
        case 1851868013: return new EventDefinition();
        case 447611511: return new Evidence();
        case -1162161645: return new EvidenceVariable();
        case 1175230202: return new ExampleScenario();
        case -1001676601: return new ExplanationOfBenefit();
        case 198012600: return new Expression();
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
        case -650580623: return new ImagingStudy();
        case -2004863454: return new Immunization();
        case -1768794370: return new ImmunizationEvaluation();
        case 1728372347: return new ImmunizationRecommendation();
        case 1410262602: return new ImplementationGuide();
        case -1503864573: return new InsurancePlan();
        case -670115059: return new Invoice();
        case 1830861979: return new Library();
        case 1841735333: return new Linkage();
        case 2368702: return new ListResource();
        case 1965687765: return new Location();
        case -926250600: return new MarketingStatus();
        case -1691992770: return new Measure();
        case 1681397778: return new MeasureReport();
        case 74219460: return new Media();
        case -302536977: return new Medication();
        case -342579923: return new MedicationAdministration();
        case -408244884: return new MedicationDispense();
        case 1537687119: return new MedicationKnowledge();
        case 1627523232: return new MedicationRequest();
        case -2097348800: return new MedicationStatement();
        case -1349298375: return new MedicinalProduct();
        case -29557312: return new MedicinalProductAuthorization();
        case -1375810986: return new MedicinalProductContraindication();
        case -961008267: return new MedicinalProductIndication();
        case -570248726: return new MedicinalProductIngredient();
        case -844984039: return new MedicinalProductInteraction();
        case 568246684: return new MedicinalProductManufactured();
        case -500906185: return new MedicinalProductPackaged();
        case -1034780964: return new MedicinalProductPharmaceutical();
        case 1717675156: return new MedicinalProductUndesirableEffect();
        case -2037697382: return new MessageDefinition();
        case -1087398572: return new MessageHeader();
        case 2394661: return new Meta();
        case -1839726095: return new MolecularSequence();
        case 74526880: return new Money();
        case 977885515: return new MoneyQuantity();
        case 369315063: return new NamingSystem();
        case -540546990: return new Narrative();
        case 1247831734: return new NutritionOrder();
        case 1790214156: return new Observation();
        case 673706623: return new ObservationDefinition();
        case -2140710406: return new OperationDefinition();
        case -526550005: return new OperationOutcome();
        case 1343242579: return new Organization();
        case 2069161885: return new OrganizationAffiliation();
        case 671337916: return new ParameterDefinition();
        case -1842766326: return new Parameters();
        case 873235173: return new Patient();
        case 2082457694: return new PaymentNotice();
        case 28778089: return new PaymentReconciliation();
        case -1907858975: return new Period();
        case -1907849355: return new Person();
        case 1401244028: return new PlanDefinition();
        case -30093459: return new Population();
        case 738893626: return new Practitioner();
        case -621058352: return new PractitionerRole();
        case 908763827: return new Procedure();
        case 458000626: return new ProdCharacteristic();
        case 1209602103: return new ProductShelfLife();
        case 2093211201: return new Provenance();
        case -1220360021: return new Quantity();
        case -218088061: return new Questionnaire();
        case 269058788: return new QuestionnaireResponse();
        case 78727453: return new Range();
        case 78733291: return new Ratio();
        case 1078812459: return new Reference();
        case -330210563: return new RelatedArtifact();
        case 846088000: return new RelatedPerson();
        case 1445374288: return new RequestGroup();
        case 237996398: return new ResearchDefinition();
        case 463703284: return new ResearchElementDefinition();
        case 1312904398: return new ResearchStudy();
        case -1008013583: return new ResearchSubject();
        case -766422255: return new RiskAssessment();
        case 1935791054: return new RiskEvidenceSynthesis();
        case 1824308900: return new SampledData();
        case -633276745: return new Schedule();
        case -912457023: return new SearchParameter();
        case -1944810950: return new ServiceRequest();
        case -1217415016: return new Signature();
        case -1097468803: return new SimpleQuantity();
        case 2579998: return new Slot();
        case -2068224216: return new Specimen();
        case 863741595: return new SpecimenDefinition();
        case 1133777670: return new StructureDefinition();
        case 1958247177: return new StructureMap();
        case 505523517: return new Subscription();
        case -1760959152: return new Substance();
        case 1549526472: return new SubstanceAmount();
        case -300807236: return new SubstanceNucleicAcid();
        case 1272939294: return new SubstancePolymer();
        case 1361440787: return new SubstanceProtein();
        case 159007313: return new SubstanceReferenceInformation();
        case -222622766: return new SubstanceSourceMaterial();
        case 1472900499: return new SubstanceSpecification();
        case 383030819: return new SupplyDelivery();
        case 665843328: return new SupplyRequest();
        case 2599333: return new Task();
        case -549565975: return new TerminologyCapabilities();
        case -616289146: return new TestReport();
        case -589453283: return new TestScript();
        case -1789797270: return new Timing();
        case 770498827: return new TriggerDefinition();
        case 1071332590: return new UsageContext();
        case -1345530543: return new ValueSet();
        case 957089336: return new VerificationResult();
        case -555387838: return new VisionPrescription();
        case -1216012752: return new Base64BinaryType();
        case 64711720: return new BooleanType();
        case 828351732: return new CanonicalType();
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
        case 116079: return new UrlType();
        case 3601339: return new UuidType();
      default:
        throw new FHIRException("Unknown Resource or Type Name '"+name+"'");
    }
  }


}

