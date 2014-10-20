















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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


import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.rest.gclient.*;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;

import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.AdmitSourceEnum;
import ca.uhn.fhir.model.dstu.resource.AdverseReaction;
import ca.uhn.fhir.model.dstu.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.AlertStatusEnum;
import ca.uhn.fhir.model.dstu.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu.valueset.AnimalSpeciesEnum;
import ca.uhn.fhir.model.dstu.resource.Appointment;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.resource.Availability;
import ca.uhn.fhir.model.dstu.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dstu.resource.CarePlan;
import ca.uhn.fhir.model.dstu.valueset.CarePlanActivityCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanActivityStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanGoalStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CausalityExpectationEnum;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.valueset.CompositionAttestationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConceptMapEquivalenceEnum;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.valueset.ConditionRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConditionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceEventModeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceStatementStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.dstu.valueset.CriticalityEnum;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderPriorityEnum;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dstu.resource.DocumentManifest;
import ca.uhn.fhir.model.dstu.valueset.DocumentModeEnum;
import ca.uhn.fhir.model.dstu.resource.DocumentReference;
import ca.uhn.fhir.model.dstu.valueset.DocumentReferenceStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.DocumentRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExposureTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExtensionContextEnum;
import ca.uhn.fhir.model.dstu.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dstu.resource.FamilyHistory;
import ca.uhn.fhir.model.dstu.valueset.FilterOperatorEnum;
import ca.uhn.fhir.model.dstu.resource.GVFMeta;
import ca.uhn.fhir.model.dstu.resource.Group;
import ca.uhn.fhir.model.dstu.valueset.GroupTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.HierarchicalRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ImagingModalityEnum;
import ca.uhn.fhir.model.dstu.resource.ImagingStudy;
import ca.uhn.fhir.model.dstu.resource.Immunization;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationReasonCodesEnum;
import ca.uhn.fhir.model.dstu.resource.ImmunizationRecommendation;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRecommendationDateCriterionCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRecommendationStatusCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRouteCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.InstanceAvailabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.LinkTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ListModeEnum;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.valueset.LocationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu.resource.Media;
import ca.uhn.fhir.model.dstu.valueset.MediaTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu.valueset.MedicationAdministrationStatusEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu.valueset.MedicationDispenseStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.MedicationKindEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.dstu.valueset.MedicationPrescriptionStatusEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu.valueset.MessageEventEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageSignificanceCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageTransportEnum;
import ca.uhn.fhir.model.dstu.resource.Microarray;
import ca.uhn.fhir.model.dstu.valueset.ModalityEnum;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.valueset.ObservationInterpretationCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Order;
import ca.uhn.fhir.model.dstu.valueset.OrderOutcomeStatusEnum;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.OrganizationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ParticipantTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.PatientRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerSpecialtyEnum;
import ca.uhn.fhir.model.dstu.resource.Procedure;
import ca.uhn.fhir.model.dstu.valueset.ProcedureRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dstu.valueset.ProvenanceEntityRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.valueset.QueryOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireGroupNameEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireNameEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireStatusEnum;
import ca.uhn.fhir.model.dstu.composite.RangeDt;
import ca.uhn.fhir.model.dstu.composite.RatioDt;
import ca.uhn.fhir.model.dstu.valueset.ReactionSeverityEnum;
import ca.uhn.fhir.model.dstu.resource.RelatedPerson;
import ca.uhn.fhir.model.dstu.valueset.ResourceProfileStatusEnum;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ResponseTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dstu.composite.SampledDataDt;
import ca.uhn.fhir.model.dstu.composite.ScheduleDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventActionEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventParticipantNetworkTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventSourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SensitivityStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SensitivityTypeEnum;
import ca.uhn.fhir.model.dstu.resource.SequencingAnalysis;
import ca.uhn.fhir.model.dstu.resource.SequencingLab;
import ca.uhn.fhir.model.dstu.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.dstu.resource.Slot;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.valueset.SpecimenCollectionMethodEnum;
import ca.uhn.fhir.model.dstu.valueset.SpecimenTreatmentProcedureEnum;
import ca.uhn.fhir.model.dstu.resource.Substance;
import ca.uhn.fhir.model.dstu.valueset.SubstanceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyDispenseStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyItemTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyTypeEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.valueset.ValueSetStatusEnum;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.AgeDt;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IdrefDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.OidDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;


/**
 * HAPI/FHIR <b>GVFVariant</b> Resource
 * (GVF Variant)
 *
 * <p>
 * <b>Definition:</b>
 * A segment of a GVF file
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/GVFVariant">http://hl7.org/fhir/profiles/GVFVariant</a> 
 * </p>
 *
 */
@ResourceDef(name="GVFVariant", profile="http://hl7.org/fhir/profiles/GVFVariant", id="gvfvariant")
public class GVFVariant extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Patient being described </b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GVFVariant.subject.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="GVFVariant.subject.patient", description="Patient being described ", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Patient being described </b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GVFVariant.subject.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GVFVariant.subject.patient</b>".
	 */
	public static final Include INCLUDE_SUBJECT_PATIENT = new Include("GVFVariant.subject.patient");

	/**
	 * Search parameter constant for <b>coordinate</b>
	 * <p>
	 * Description: <b>Coordinate of the variant being studied</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="coordinate", path="", description="Coordinate of the variant being studied", type="string"  )
	public static final String SP_COORDINATE = "coordinate";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>coordinate</b>
	 * <p>
	 * Description: <b>Coordinate of the variant being studied</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final StringClientParam COORDINATE = new StringClientParam(SP_COORDINATE);


	@Child(name="subject", order=0, min=0, max=1)	
	@Description(
		shortDefinition="Subject described by this segment of GVF file",
		formalDefinition="Subject described by this segment of GVF file"
	)
	private Subject mySubject;
	
	@Child(name="meta", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.GVFMeta.class	})
	@Description(
		shortDefinition="GVF Meta",
		formalDefinition="Meta information of a GVF file"
	)
	private ResourceReferenceDt myMeta;
	
	@Child(name="sourceFile", type=AttachmentDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Source file",
		formalDefinition="Source GVF file"
	)
	private AttachmentDt mySourceFile;
	
	@Child(name="seqid", type=StringDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Sequence Id",
		formalDefinition="Id the sequence being described"
	)
	private StringDt mySeqid;
	
	@Child(name="source", type=StringDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Data source",
		formalDefinition="Algorithm or software used to generate the data"
	)
	private StringDt mySource;
	
	@Child(name="type", type=CodeDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Feature type",
		formalDefinition="Type of the feature being described"
	)
	private CodeDt myType;
	
	@Child(name="start", type=IntegerDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="Start position",
		formalDefinition="Start position"
	)
	private IntegerDt myStart;
	
	@Child(name="end", type=IntegerDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="End position",
		formalDefinition="End position"
	)
	private IntegerDt myEnd;
	
	@Child(name="score", type=IntegerDt.class, order=8, min=1, max=1)	
	@Description(
		shortDefinition="Sequence score",
		formalDefinition="Phred scaled score of the sequence"
	)
	private IntegerDt myScore;
	
	@Child(name="strand", type=CodeDt.class, order=9, min=1, max=1)	
	@Description(
		shortDefinition="Strand",
		formalDefinition="Direction of the strand"
	)
	private CodeDt myStrand;
	
	@Child(name="featureId", type=StringDt.class, order=10, min=1, max=1)	
	@Description(
		shortDefinition="Id of the feature",
		formalDefinition="Id of the attribute, unique to other segments in the same source file"
	)
	private StringDt myFeatureId;
	
	@Child(name="alias", type=StringDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="Alias of the feature",
		formalDefinition="Alias of the feature being described"
	)
	private StringDt myAlias;
	
	@Child(name="dbxref", order=12, min=0, max=1)	
	@Description(
		shortDefinition="Reference of the feature in a database",
		formalDefinition="Reference of the feature in a database"
	)
	private Dbxref myDbxref;
	
	@Child(name="variantSeq", type=StringDt.class, order=13, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Sequence presents in the variant",
		formalDefinition="Sequence presents in the variant"
	)
	private java.util.List<StringDt> myVariantSeq;
	
	@Child(name="referenceSeq", type=StringDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="Reference sequence",
		formalDefinition="Reference sequence"
	)
	private StringDt myReferenceSeq;
	
	@Child(name="variantFreq", type=DecimalDt.class, order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Variant frequency",
		formalDefinition="Frequency of the variant"
	)
	private java.util.List<DecimalDt> myVariantFreq;
	
	@Child(name="variantEffect", order=16, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Variant effect",
		formalDefinition="Effect of the variant"
	)
	private java.util.List<VariantEffect> myVariantEffect;
	
	@Child(name="startRange", order=17, min=0, max=1)	
	@Description(
		shortDefinition="Start range",
		formalDefinition="Attribute describing ambiguity of the start position of the feature"
	)
	private StartRange myStartRange;
	
	@Child(name="endRange", order=18, min=0, max=1)	
	@Description(
		shortDefinition="End range",
		formalDefinition="Attribute describing ambiguity of the end position of the feature"
	)
	private EndRange myEndRange;
	
	@Child(name="variantCodon", type=StringDt.class, order=19, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Codons that overlap with feature being described",
		formalDefinition="Codons that overlap with the feature being described"
	)
	private java.util.List<StringDt> myVariantCodon;
	
	@Child(name="referenceCodon", type=StringDt.class, order=20, min=0, max=1)	
	@Description(
		shortDefinition="Codon that overlap with the reference sequence",
		formalDefinition="Codon that overlap with the reference sequence"
	)
	private StringDt myReferenceCodon;
	
	@Child(name="variantAA", type=StringDt.class, order=21, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Amino acids that overlap with the features being described",
		formalDefinition="Amino acids that overlap with the features being described"
	)
	private java.util.List<StringDt> myVariantAA;
	
	@Child(name="referenceAA", type=StringDt.class, order=22, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Amino acids that overlaps with the reference sequence",
		formalDefinition="Amino acids that overlaps with the reference sequence"
	)
	private java.util.List<StringDt> myReferenceAA;
	
	@Child(name="breakpointDetail", order=23, min=0, max=1)	
	@Description(
		shortDefinition="Coordinate of a variant with zero length",
		formalDefinition="Coordinate of a variant with zero length"
	)
	private BreakpointDetail myBreakpointDetail;
	
	@Child(name="sequenceContext", order=24, min=0, max=1)	
	@Description(
		shortDefinition="Context of features being described",
		formalDefinition="Sequences adjacent to the feature"
	)
	private SequenceContext mySequenceContext;
	
	@Child(name="individual", type=StringDt.class, order=25, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Individuals being described",
		formalDefinition="Individuals for whom the feature is described"
	)
	private java.util.List<StringDt> myIndividual;
	
	@Child(name="sample", order=26, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Individual genotypic information",
		formalDefinition="Individual genotypic information"
	)
	private java.util.List<Sample> mySample;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myMeta,  mySourceFile,  mySeqid,  mySource,  myType,  myStart,  myEnd,  myScore,  myStrand,  myFeatureId,  myAlias,  myDbxref,  myVariantSeq,  myReferenceSeq,  myVariantFreq,  myVariantEffect,  myStartRange,  myEndRange,  myVariantCodon,  myReferenceCodon,  myVariantAA,  myReferenceAA,  myBreakpointDetail,  mySequenceContext,  myIndividual,  mySample);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myMeta, mySourceFile, mySeqid, mySource, myType, myStart, myEnd, myScore, myStrand, myFeatureId, myAlias, myDbxref, myVariantSeq, myReferenceSeq, myVariantFreq, myVariantEffect, myStartRange, myEndRange, myVariantCodon, myReferenceCodon, myVariantAA, myReferenceAA, myBreakpointDetail, mySequenceContext, myIndividual, mySample);
	}
	

	/**
	 * Gets the value(s) for <b>subject</b> (Subject described by this segment of GVF file).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject described by this segment of GVF file
     * </p> 
	 */
	public Subject getSubject() {  
		if (mySubject == null) {
			mySubject = new Subject();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Subject described by this segment of GVF file)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject described by this segment of GVF file
     * </p> 
	 */
	public GVFVariant setSubject(Subject theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>meta</b> (GVF Meta).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Meta information of a GVF file
     * </p> 
	 */
	public ResourceReferenceDt getMeta() {  
		if (myMeta == null) {
			myMeta = new ResourceReferenceDt();
		}
		return myMeta;
	}

	/**
	 * Sets the value(s) for <b>meta</b> (GVF Meta)
	 *
     * <p>
     * <b>Definition:</b>
     * Meta information of a GVF file
     * </p> 
	 */
	public GVFVariant setMeta(ResourceReferenceDt theValue) {
		myMeta = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sourceFile</b> (Source file).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Source GVF file
     * </p> 
	 */
	public AttachmentDt getSourceFile() {  
		if (mySourceFile == null) {
			mySourceFile = new AttachmentDt();
		}
		return mySourceFile;
	}

	/**
	 * Sets the value(s) for <b>sourceFile</b> (Source file)
	 *
     * <p>
     * <b>Definition:</b>
     * Source GVF file
     * </p> 
	 */
	public GVFVariant setSourceFile(AttachmentDt theValue) {
		mySourceFile = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>seqid</b> (Sequence Id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id the sequence being described
     * </p> 
	 */
	public StringDt getSeqid() {  
		if (mySeqid == null) {
			mySeqid = new StringDt();
		}
		return mySeqid;
	}

	/**
	 * Sets the value(s) for <b>seqid</b> (Sequence Id)
	 *
     * <p>
     * <b>Definition:</b>
     * Id the sequence being described
     * </p> 
	 */
	public GVFVariant setSeqid(StringDt theValue) {
		mySeqid = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>seqid</b> (Sequence Id)
	 *
     * <p>
     * <b>Definition:</b>
     * Id the sequence being described
     * </p> 
	 */
	public GVFVariant setSeqid( String theString) {
		mySeqid = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>source</b> (Data source).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Algorithm or software used to generate the data
     * </p> 
	 */
	public StringDt getSource() {  
		if (mySource == null) {
			mySource = new StringDt();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> (Data source)
	 *
     * <p>
     * <b>Definition:</b>
     * Algorithm or software used to generate the data
     * </p> 
	 */
	public GVFVariant setSource(StringDt theValue) {
		mySource = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>source</b> (Data source)
	 *
     * <p>
     * <b>Definition:</b>
     * Algorithm or software used to generate the data
     * </p> 
	 */
	public GVFVariant setSource( String theString) {
		mySource = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (Feature type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the feature being described
     * </p> 
	 */
	public CodeDt getType() {  
		if (myType == null) {
			myType = new CodeDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Feature type)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the feature being described
     * </p> 
	 */
	public GVFVariant setType(CodeDt theValue) {
		myType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>type</b> (Feature type)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the feature being described
     * </p> 
	 */
	public GVFVariant setType( String theCode) {
		myType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>start</b> (Start position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public IntegerDt getStart() {  
		if (myStart == null) {
			myStart = new IntegerDt();
		}
		return myStart;
	}

	/**
	 * Sets the value(s) for <b>start</b> (Start position)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public GVFVariant setStart(IntegerDt theValue) {
		myStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>start</b> (Start position)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public GVFVariant setStart( int theInteger) {
		myStart = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (End position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public IntegerDt getEnd() {  
		if (myEnd == null) {
			myEnd = new IntegerDt();
		}
		return myEnd;
	}

	/**
	 * Sets the value(s) for <b>end</b> (End position)
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public GVFVariant setEnd(IntegerDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (End position)
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public GVFVariant setEnd( int theInteger) {
		myEnd = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>score</b> (Sequence score).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Phred scaled score of the sequence
     * </p> 
	 */
	public IntegerDt getScore() {  
		if (myScore == null) {
			myScore = new IntegerDt();
		}
		return myScore;
	}

	/**
	 * Sets the value(s) for <b>score</b> (Sequence score)
	 *
     * <p>
     * <b>Definition:</b>
     * Phred scaled score of the sequence
     * </p> 
	 */
	public GVFVariant setScore(IntegerDt theValue) {
		myScore = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>score</b> (Sequence score)
	 *
     * <p>
     * <b>Definition:</b>
     * Phred scaled score of the sequence
     * </p> 
	 */
	public GVFVariant setScore( int theInteger) {
		myScore = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>strand</b> (Strand).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Direction of the strand
     * </p> 
	 */
	public CodeDt getStrand() {  
		if (myStrand == null) {
			myStrand = new CodeDt();
		}
		return myStrand;
	}

	/**
	 * Sets the value(s) for <b>strand</b> (Strand)
	 *
     * <p>
     * <b>Definition:</b>
     * Direction of the strand
     * </p> 
	 */
	public GVFVariant setStrand(CodeDt theValue) {
		myStrand = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>strand</b> (Strand)
	 *
     * <p>
     * <b>Definition:</b>
     * Direction of the strand
     * </p> 
	 */
	public GVFVariant setStrand( String theCode) {
		myStrand = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>featureId</b> (Id of the feature).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the attribute, unique to other segments in the same source file
     * </p> 
	 */
	public StringDt getFeatureId() {  
		if (myFeatureId == null) {
			myFeatureId = new StringDt();
		}
		return myFeatureId;
	}

	/**
	 * Sets the value(s) for <b>featureId</b> (Id of the feature)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the attribute, unique to other segments in the same source file
     * </p> 
	 */
	public GVFVariant setFeatureId(StringDt theValue) {
		myFeatureId = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>featureId</b> (Id of the feature)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the attribute, unique to other segments in the same source file
     * </p> 
	 */
	public GVFVariant setFeatureId( String theString) {
		myFeatureId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>alias</b> (Alias of the feature).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Alias of the feature being described
     * </p> 
	 */
	public StringDt getAlias() {  
		if (myAlias == null) {
			myAlias = new StringDt();
		}
		return myAlias;
	}

	/**
	 * Sets the value(s) for <b>alias</b> (Alias of the feature)
	 *
     * <p>
     * <b>Definition:</b>
     * Alias of the feature being described
     * </p> 
	 */
	public GVFVariant setAlias(StringDt theValue) {
		myAlias = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>alias</b> (Alias of the feature)
	 *
     * <p>
     * <b>Definition:</b>
     * Alias of the feature being described
     * </p> 
	 */
	public GVFVariant setAlias( String theString) {
		myAlias = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dbxref</b> (Reference of the feature in a database).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference of the feature in a database
     * </p> 
	 */
	public Dbxref getDbxref() {  
		if (myDbxref == null) {
			myDbxref = new Dbxref();
		}
		return myDbxref;
	}

	/**
	 * Sets the value(s) for <b>dbxref</b> (Reference of the feature in a database)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference of the feature in a database
     * </p> 
	 */
	public GVFVariant setDbxref(Dbxref theValue) {
		myDbxref = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>variantSeq</b> (Sequence presents in the variant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence presents in the variant
     * </p> 
	 */
	public java.util.List<StringDt> getVariantSeq() {  
		if (myVariantSeq == null) {
			myVariantSeq = new java.util.ArrayList<StringDt>();
		}
		return myVariantSeq;
	}

	/**
	 * Sets the value(s) for <b>variantSeq</b> (Sequence presents in the variant)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence presents in the variant
     * </p> 
	 */
	public GVFVariant setVariantSeq(java.util.List<StringDt> theValue) {
		myVariantSeq = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>variantSeq</b> (Sequence presents in the variant)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence presents in the variant
     * </p> 
	 */
	public StringDt addVariantSeq() {
		StringDt newType = new StringDt();
		getVariantSeq().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>variantSeq</b> (Sequence presents in the variant),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence presents in the variant
     * </p> 
	 */
	public StringDt getVariantSeqFirstRep() {
		if (getVariantSeq().isEmpty()) {
			return addVariantSeq();
		}
		return getVariantSeq().get(0); 
	}
 	/**
	 * Adds a new value for <b>variantSeq</b> (Sequence presents in the variant)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence presents in the variant
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public GVFVariant addVariantSeq( String theString) {
		if (myVariantSeq == null) {
			myVariantSeq = new java.util.ArrayList<StringDt>();
		}
		myVariantSeq.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>referenceSeq</b> (Reference sequence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference sequence
     * </p> 
	 */
	public StringDt getReferenceSeq() {  
		if (myReferenceSeq == null) {
			myReferenceSeq = new StringDt();
		}
		return myReferenceSeq;
	}

	/**
	 * Sets the value(s) for <b>referenceSeq</b> (Reference sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference sequence
     * </p> 
	 */
	public GVFVariant setReferenceSeq(StringDt theValue) {
		myReferenceSeq = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>referenceSeq</b> (Reference sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference sequence
     * </p> 
	 */
	public GVFVariant setReferenceSeq( String theString) {
		myReferenceSeq = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>variantFreq</b> (Variant frequency).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Frequency of the variant
     * </p> 
	 */
	public java.util.List<DecimalDt> getVariantFreq() {  
		if (myVariantFreq == null) {
			myVariantFreq = new java.util.ArrayList<DecimalDt>();
		}
		return myVariantFreq;
	}

	/**
	 * Sets the value(s) for <b>variantFreq</b> (Variant frequency)
	 *
     * <p>
     * <b>Definition:</b>
     * Frequency of the variant
     * </p> 
	 */
	public GVFVariant setVariantFreq(java.util.List<DecimalDt> theValue) {
		myVariantFreq = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>variantFreq</b> (Variant frequency)
	 *
     * <p>
     * <b>Definition:</b>
     * Frequency of the variant
     * </p> 
	 */
	public DecimalDt addVariantFreq() {
		DecimalDt newType = new DecimalDt();
		getVariantFreq().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>variantFreq</b> (Variant frequency),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Frequency of the variant
     * </p> 
	 */
	public DecimalDt getVariantFreqFirstRep() {
		if (getVariantFreq().isEmpty()) {
			return addVariantFreq();
		}
		return getVariantFreq().get(0); 
	}
 	/**
	 * Adds a new value for <b>variantFreq</b> (Variant frequency)
	 *
     * <p>
     * <b>Definition:</b>
     * Frequency of the variant
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public GVFVariant addVariantFreq( java.math.BigDecimal theValue) {
		if (myVariantFreq == null) {
			myVariantFreq = new java.util.ArrayList<DecimalDt>();
		}
		myVariantFreq.add(new DecimalDt(theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>variantFreq</b> (Variant frequency)
	 *
     * <p>
     * <b>Definition:</b>
     * Frequency of the variant
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public GVFVariant addVariantFreq( double theValue) {
		if (myVariantFreq == null) {
			myVariantFreq = new java.util.ArrayList<DecimalDt>();
		}
		myVariantFreq.add(new DecimalDt(theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>variantFreq</b> (Variant frequency)
	 *
     * <p>
     * <b>Definition:</b>
     * Frequency of the variant
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public GVFVariant addVariantFreq( long theValue) {
		if (myVariantFreq == null) {
			myVariantFreq = new java.util.ArrayList<DecimalDt>();
		}
		myVariantFreq.add(new DecimalDt(theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>variantEffect</b> (Variant effect).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Effect of the variant
     * </p> 
	 */
	public java.util.List<VariantEffect> getVariantEffect() {  
		if (myVariantEffect == null) {
			myVariantEffect = new java.util.ArrayList<VariantEffect>();
		}
		return myVariantEffect;
	}

	/**
	 * Sets the value(s) for <b>variantEffect</b> (Variant effect)
	 *
     * <p>
     * <b>Definition:</b>
     * Effect of the variant
     * </p> 
	 */
	public GVFVariant setVariantEffect(java.util.List<VariantEffect> theValue) {
		myVariantEffect = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>variantEffect</b> (Variant effect)
	 *
     * <p>
     * <b>Definition:</b>
     * Effect of the variant
     * </p> 
	 */
	public VariantEffect addVariantEffect() {
		VariantEffect newType = new VariantEffect();
		getVariantEffect().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>variantEffect</b> (Variant effect),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Effect of the variant
     * </p> 
	 */
	public VariantEffect getVariantEffectFirstRep() {
		if (getVariantEffect().isEmpty()) {
			return addVariantEffect();
		}
		return getVariantEffect().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>startRange</b> (Start range).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing ambiguity of the start position of the feature
     * </p> 
	 */
	public StartRange getStartRange() {  
		if (myStartRange == null) {
			myStartRange = new StartRange();
		}
		return myStartRange;
	}

	/**
	 * Sets the value(s) for <b>startRange</b> (Start range)
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing ambiguity of the start position of the feature
     * </p> 
	 */
	public GVFVariant setStartRange(StartRange theValue) {
		myStartRange = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>endRange</b> (End range).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing ambiguity of the end position of the feature
     * </p> 
	 */
	public EndRange getEndRange() {  
		if (myEndRange == null) {
			myEndRange = new EndRange();
		}
		return myEndRange;
	}

	/**
	 * Sets the value(s) for <b>endRange</b> (End range)
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing ambiguity of the end position of the feature
     * </p> 
	 */
	public GVFVariant setEndRange(EndRange theValue) {
		myEndRange = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>variantCodon</b> (Codons that overlap with feature being described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Codons that overlap with the feature being described
     * </p> 
	 */
	public java.util.List<StringDt> getVariantCodon() {  
		if (myVariantCodon == null) {
			myVariantCodon = new java.util.ArrayList<StringDt>();
		}
		return myVariantCodon;
	}

	/**
	 * Sets the value(s) for <b>variantCodon</b> (Codons that overlap with feature being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Codons that overlap with the feature being described
     * </p> 
	 */
	public GVFVariant setVariantCodon(java.util.List<StringDt> theValue) {
		myVariantCodon = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>variantCodon</b> (Codons that overlap with feature being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Codons that overlap with the feature being described
     * </p> 
	 */
	public StringDt addVariantCodon() {
		StringDt newType = new StringDt();
		getVariantCodon().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>variantCodon</b> (Codons that overlap with feature being described),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Codons that overlap with the feature being described
     * </p> 
	 */
	public StringDt getVariantCodonFirstRep() {
		if (getVariantCodon().isEmpty()) {
			return addVariantCodon();
		}
		return getVariantCodon().get(0); 
	}
 	/**
	 * Adds a new value for <b>variantCodon</b> (Codons that overlap with feature being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Codons that overlap with the feature being described
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public GVFVariant addVariantCodon( String theString) {
		if (myVariantCodon == null) {
			myVariantCodon = new java.util.ArrayList<StringDt>();
		}
		myVariantCodon.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>referenceCodon</b> (Codon that overlap with the reference sequence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Codon that overlap with the reference sequence
     * </p> 
	 */
	public StringDt getReferenceCodon() {  
		if (myReferenceCodon == null) {
			myReferenceCodon = new StringDt();
		}
		return myReferenceCodon;
	}

	/**
	 * Sets the value(s) for <b>referenceCodon</b> (Codon that overlap with the reference sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Codon that overlap with the reference sequence
     * </p> 
	 */
	public GVFVariant setReferenceCodon(StringDt theValue) {
		myReferenceCodon = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>referenceCodon</b> (Codon that overlap with the reference sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Codon that overlap with the reference sequence
     * </p> 
	 */
	public GVFVariant setReferenceCodon( String theString) {
		myReferenceCodon = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>variantAA</b> (Amino acids that overlap with the features being described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acids that overlap with the features being described
     * </p> 
	 */
	public java.util.List<StringDt> getVariantAA() {  
		if (myVariantAA == null) {
			myVariantAA = new java.util.ArrayList<StringDt>();
		}
		return myVariantAA;
	}

	/**
	 * Sets the value(s) for <b>variantAA</b> (Amino acids that overlap with the features being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acids that overlap with the features being described
     * </p> 
	 */
	public GVFVariant setVariantAA(java.util.List<StringDt> theValue) {
		myVariantAA = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>variantAA</b> (Amino acids that overlap with the features being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acids that overlap with the features being described
     * </p> 
	 */
	public StringDt addVariantAA() {
		StringDt newType = new StringDt();
		getVariantAA().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>variantAA</b> (Amino acids that overlap with the features being described),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acids that overlap with the features being described
     * </p> 
	 */
	public StringDt getVariantAAFirstRep() {
		if (getVariantAA().isEmpty()) {
			return addVariantAA();
		}
		return getVariantAA().get(0); 
	}
 	/**
	 * Adds a new value for <b>variantAA</b> (Amino acids that overlap with the features being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acids that overlap with the features being described
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public GVFVariant addVariantAA( String theString) {
		if (myVariantAA == null) {
			myVariantAA = new java.util.ArrayList<StringDt>();
		}
		myVariantAA.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>referenceAA</b> (Amino acids that overlaps with the reference sequence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acids that overlaps with the reference sequence
     * </p> 
	 */
	public java.util.List<StringDt> getReferenceAA() {  
		if (myReferenceAA == null) {
			myReferenceAA = new java.util.ArrayList<StringDt>();
		}
		return myReferenceAA;
	}

	/**
	 * Sets the value(s) for <b>referenceAA</b> (Amino acids that overlaps with the reference sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acids that overlaps with the reference sequence
     * </p> 
	 */
	public GVFVariant setReferenceAA(java.util.List<StringDt> theValue) {
		myReferenceAA = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>referenceAA</b> (Amino acids that overlaps with the reference sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acids that overlaps with the reference sequence
     * </p> 
	 */
	public StringDt addReferenceAA() {
		StringDt newType = new StringDt();
		getReferenceAA().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>referenceAA</b> (Amino acids that overlaps with the reference sequence),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acids that overlaps with the reference sequence
     * </p> 
	 */
	public StringDt getReferenceAAFirstRep() {
		if (getReferenceAA().isEmpty()) {
			return addReferenceAA();
		}
		return getReferenceAA().get(0); 
	}
 	/**
	 * Adds a new value for <b>referenceAA</b> (Amino acids that overlaps with the reference sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acids that overlaps with the reference sequence
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public GVFVariant addReferenceAA( String theString) {
		if (myReferenceAA == null) {
			myReferenceAA = new java.util.ArrayList<StringDt>();
		}
		myReferenceAA.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>breakpointDetail</b> (Coordinate of a variant with zero length).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of a variant with zero length
     * </p> 
	 */
	public BreakpointDetail getBreakpointDetail() {  
		if (myBreakpointDetail == null) {
			myBreakpointDetail = new BreakpointDetail();
		}
		return myBreakpointDetail;
	}

	/**
	 * Sets the value(s) for <b>breakpointDetail</b> (Coordinate of a variant with zero length)
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of a variant with zero length
     * </p> 
	 */
	public GVFVariant setBreakpointDetail(BreakpointDetail theValue) {
		myBreakpointDetail = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sequenceContext</b> (Context of features being described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sequences adjacent to the feature
     * </p> 
	 */
	public SequenceContext getSequenceContext() {  
		if (mySequenceContext == null) {
			mySequenceContext = new SequenceContext();
		}
		return mySequenceContext;
	}

	/**
	 * Sets the value(s) for <b>sequenceContext</b> (Context of features being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequences adjacent to the feature
     * </p> 
	 */
	public GVFVariant setSequenceContext(SequenceContext theValue) {
		mySequenceContext = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>individual</b> (Individuals being described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Individuals for whom the feature is described
     * </p> 
	 */
	public java.util.List<StringDt> getIndividual() {  
		if (myIndividual == null) {
			myIndividual = new java.util.ArrayList<StringDt>();
		}
		return myIndividual;
	}

	/**
	 * Sets the value(s) for <b>individual</b> (Individuals being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Individuals for whom the feature is described
     * </p> 
	 */
	public GVFVariant setIndividual(java.util.List<StringDt> theValue) {
		myIndividual = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>individual</b> (Individuals being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Individuals for whom the feature is described
     * </p> 
	 */
	public StringDt addIndividual() {
		StringDt newType = new StringDt();
		getIndividual().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>individual</b> (Individuals being described),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Individuals for whom the feature is described
     * </p> 
	 */
	public StringDt getIndividualFirstRep() {
		if (getIndividual().isEmpty()) {
			return addIndividual();
		}
		return getIndividual().get(0); 
	}
 	/**
	 * Adds a new value for <b>individual</b> (Individuals being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Individuals for whom the feature is described
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public GVFVariant addIndividual( String theString) {
		if (myIndividual == null) {
			myIndividual = new java.util.ArrayList<StringDt>();
		}
		myIndividual.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>sample</b> (Individual genotypic information).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Individual genotypic information
     * </p> 
	 */
	public java.util.List<Sample> getSample() {  
		if (mySample == null) {
			mySample = new java.util.ArrayList<Sample>();
		}
		return mySample;
	}

	/**
	 * Sets the value(s) for <b>sample</b> (Individual genotypic information)
	 *
     * <p>
     * <b>Definition:</b>
     * Individual genotypic information
     * </p> 
	 */
	public GVFVariant setSample(java.util.List<Sample> theValue) {
		mySample = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>sample</b> (Individual genotypic information)
	 *
     * <p>
     * <b>Definition:</b>
     * Individual genotypic information
     * </p> 
	 */
	public Sample addSample() {
		Sample newType = new Sample();
		getSample().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>sample</b> (Individual genotypic information),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Individual genotypic information
     * </p> 
	 */
	public Sample getSampleFirstRep() {
		if (getSample().isEmpty()) {
			return addSample();
		}
		return getSample().get(0); 
	}
  
	/**
	 * Block class for child element: <b>GVFVariant.subject</b> (Subject described by this segment of GVF file)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject described by this segment of GVF file
     * </p> 
	 */
	@Block()	
	public static class Subject extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="patient", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Subject",
		formalDefinition="Patient resource that stores information of the subejct"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="fileId", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Individual Id",
		formalDefinition="Id of individual in GVF file that corresponds to the subject (only mandatory if the file is a multi-individual one"
	)
	private StringDt myFileId;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPatient,  myFileId);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPatient, myFileId);
	}
	

	/**
	 * Gets the value(s) for <b>patient</b> (Subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Patient resource that stores information of the subejct
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (Subject)
	 *
     * <p>
     * <b>Definition:</b>
     * Patient resource that stores information of the subejct
     * </p> 
	 */
	public Subject setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>fileId</b> (Individual Id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual in GVF file that corresponds to the subject (only mandatory if the file is a multi-individual one
     * </p> 
	 */
	public StringDt getFileId() {  
		if (myFileId == null) {
			myFileId = new StringDt();
		}
		return myFileId;
	}

	/**
	 * Sets the value(s) for <b>fileId</b> (Individual Id)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual in GVF file that corresponds to the subject (only mandatory if the file is a multi-individual one
     * </p> 
	 */
	public Subject setFileId(StringDt theValue) {
		myFileId = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>fileId</b> (Individual Id)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual in GVF file that corresponds to the subject (only mandatory if the file is a multi-individual one
     * </p> 
	 */
	public Subject setFileId( String theString) {
		myFileId = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>GVFVariant.dbxref</b> (Reference of the feature in a database)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference of the feature in a database
     * </p> 
	 */
	@Block()	
	public static class Dbxref extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="database", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Name of the database",
		formalDefinition="Name of the database"
	)
	private CodeDt myDatabase;
	
	@Child(name="identity", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Id of the feature within the database",
		formalDefinition="Id of the feature within the database"
	)
	private StringDt myIdentity;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDatabase,  myIdentity);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDatabase, myIdentity);
	}
	

	/**
	 * Gets the value(s) for <b>database</b> (Name of the database).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the database
     * </p> 
	 */
	public CodeDt getDatabase() {  
		if (myDatabase == null) {
			myDatabase = new CodeDt();
		}
		return myDatabase;
	}

	/**
	 * Sets the value(s) for <b>database</b> (Name of the database)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the database
     * </p> 
	 */
	public Dbxref setDatabase(CodeDt theValue) {
		myDatabase = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>database</b> (Name of the database)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the database
     * </p> 
	 */
	public Dbxref setDatabase( String theCode) {
		myDatabase = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identity</b> (Id of the feature within the database).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the feature within the database
     * </p> 
	 */
	public StringDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}

	/**
	 * Sets the value(s) for <b>identity</b> (Id of the feature within the database)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the feature within the database
     * </p> 
	 */
	public Dbxref setIdentity(StringDt theValue) {
		myIdentity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identity</b> (Id of the feature within the database)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the feature within the database
     * </p> 
	 */
	public Dbxref setIdentity( String theString) {
		myIdentity = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>GVFVariant.variantEffect</b> (Variant effect)
	 *
     * <p>
     * <b>Definition:</b>
     * Effect of the variant
     * </p> 
	 */
	@Block()	
	public static class VariantEffect extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="sequenceVariant", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Effect of the variant",
		formalDefinition="Effect of the variant"
	)
	private CodeDt mySequenceVariant;
	
	@Child(name="index", type=IntegerDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Index of variant being discussed",
		formalDefinition="Zero-based index that tells the variant being discussed"
	)
	private IntegerDt myIndex;
	
	@Child(name="featureType", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Feature type",
		formalDefinition="Type of the feature being described"
	)
	private CodeDt myFeatureType;
	
	@Child(name="featureId", type=StringDt.class, order=3, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Feature Id",
		formalDefinition="Id of features being affected by the variant"
	)
	private java.util.List<StringDt> myFeatureId;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySequenceVariant,  myIndex,  myFeatureType,  myFeatureId);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySequenceVariant, myIndex, myFeatureType, myFeatureId);
	}
	

	/**
	 * Gets the value(s) for <b>sequenceVariant</b> (Effect of the variant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Effect of the variant
     * </p> 
	 */
	public CodeDt getSequenceVariant() {  
		if (mySequenceVariant == null) {
			mySequenceVariant = new CodeDt();
		}
		return mySequenceVariant;
	}

	/**
	 * Sets the value(s) for <b>sequenceVariant</b> (Effect of the variant)
	 *
     * <p>
     * <b>Definition:</b>
     * Effect of the variant
     * </p> 
	 */
	public VariantEffect setSequenceVariant(CodeDt theValue) {
		mySequenceVariant = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>sequenceVariant</b> (Effect of the variant)
	 *
     * <p>
     * <b>Definition:</b>
     * Effect of the variant
     * </p> 
	 */
	public VariantEffect setSequenceVariant( String theCode) {
		mySequenceVariant = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>index</b> (Index of variant being discussed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Zero-based index that tells the variant being discussed
     * </p> 
	 */
	public IntegerDt getIndex() {  
		if (myIndex == null) {
			myIndex = new IntegerDt();
		}
		return myIndex;
	}

	/**
	 * Sets the value(s) for <b>index</b> (Index of variant being discussed)
	 *
     * <p>
     * <b>Definition:</b>
     * Zero-based index that tells the variant being discussed
     * </p> 
	 */
	public VariantEffect setIndex(IntegerDt theValue) {
		myIndex = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>index</b> (Index of variant being discussed)
	 *
     * <p>
     * <b>Definition:</b>
     * Zero-based index that tells the variant being discussed
     * </p> 
	 */
	public VariantEffect setIndex( int theInteger) {
		myIndex = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>featureType</b> (Feature type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the feature being described
     * </p> 
	 */
	public CodeDt getFeatureType() {  
		if (myFeatureType == null) {
			myFeatureType = new CodeDt();
		}
		return myFeatureType;
	}

	/**
	 * Sets the value(s) for <b>featureType</b> (Feature type)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the feature being described
     * </p> 
	 */
	public VariantEffect setFeatureType(CodeDt theValue) {
		myFeatureType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>featureType</b> (Feature type)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the feature being described
     * </p> 
	 */
	public VariantEffect setFeatureType( String theCode) {
		myFeatureType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>featureId</b> (Feature Id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of features being affected by the variant
     * </p> 
	 */
	public java.util.List<StringDt> getFeatureId() {  
		if (myFeatureId == null) {
			myFeatureId = new java.util.ArrayList<StringDt>();
		}
		return myFeatureId;
	}

	/**
	 * Sets the value(s) for <b>featureId</b> (Feature Id)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of features being affected by the variant
     * </p> 
	 */
	public VariantEffect setFeatureId(java.util.List<StringDt> theValue) {
		myFeatureId = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>featureId</b> (Feature Id)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of features being affected by the variant
     * </p> 
	 */
	public StringDt addFeatureId() {
		StringDt newType = new StringDt();
		getFeatureId().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>featureId</b> (Feature Id),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of features being affected by the variant
     * </p> 
	 */
	public StringDt getFeatureIdFirstRep() {
		if (getFeatureId().isEmpty()) {
			return addFeatureId();
		}
		return getFeatureId().get(0); 
	}
 	/**
	 * Adds a new value for <b>featureId</b> (Feature Id)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of features being affected by the variant
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public VariantEffect addFeatureId( String theString) {
		if (myFeatureId == null) {
			myFeatureId = new java.util.ArrayList<StringDt>();
		}
		myFeatureId.add(new StringDt(theString));
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>GVFVariant.startRange</b> (Start range)
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing ambiguity of the start position of the feature
     * </p> 
	 */
	@Block()	
	public static class StartRange extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="start", type=IntegerDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Start of the start range",
		formalDefinition="Start of the start range"
	)
	private IntegerDt myStart;
	
	@Child(name="end", type=IntegerDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="End of the start range",
		formalDefinition="End of the start range"
	)
	private IntegerDt myEnd;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStart,  myEnd);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStart, myEnd);
	}
	

	/**
	 * Gets the value(s) for <b>start</b> (Start of the start range).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start of the start range
     * </p> 
	 */
	public IntegerDt getStart() {  
		if (myStart == null) {
			myStart = new IntegerDt();
		}
		return myStart;
	}

	/**
	 * Sets the value(s) for <b>start</b> (Start of the start range)
	 *
     * <p>
     * <b>Definition:</b>
     * Start of the start range
     * </p> 
	 */
	public StartRange setStart(IntegerDt theValue) {
		myStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>start</b> (Start of the start range)
	 *
     * <p>
     * <b>Definition:</b>
     * Start of the start range
     * </p> 
	 */
	public StartRange setStart( int theInteger) {
		myStart = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (End of the start range).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End of the start range
     * </p> 
	 */
	public IntegerDt getEnd() {  
		if (myEnd == null) {
			myEnd = new IntegerDt();
		}
		return myEnd;
	}

	/**
	 * Sets the value(s) for <b>end</b> (End of the start range)
	 *
     * <p>
     * <b>Definition:</b>
     * End of the start range
     * </p> 
	 */
	public StartRange setEnd(IntegerDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (End of the start range)
	 *
     * <p>
     * <b>Definition:</b>
     * End of the start range
     * </p> 
	 */
	public StartRange setEnd( int theInteger) {
		myEnd = new IntegerDt(theInteger); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>GVFVariant.endRange</b> (End range)
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing ambiguity of the end position of the feature
     * </p> 
	 */
	@Block()	
	public static class EndRange extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="start", type=IntegerDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Start of the end range",
		formalDefinition="Start of the end range"
	)
	private IntegerDt myStart;
	
	@Child(name="end", type=IntegerDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="End of the end range",
		formalDefinition="End of the end range"
	)
	private IntegerDt myEnd;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStart,  myEnd);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStart, myEnd);
	}
	

	/**
	 * Gets the value(s) for <b>start</b> (Start of the end range).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start of the end range
     * </p> 
	 */
	public IntegerDt getStart() {  
		if (myStart == null) {
			myStart = new IntegerDt();
		}
		return myStart;
	}

	/**
	 * Sets the value(s) for <b>start</b> (Start of the end range)
	 *
     * <p>
     * <b>Definition:</b>
     * Start of the end range
     * </p> 
	 */
	public EndRange setStart(IntegerDt theValue) {
		myStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>start</b> (Start of the end range)
	 *
     * <p>
     * <b>Definition:</b>
     * Start of the end range
     * </p> 
	 */
	public EndRange setStart( int theInteger) {
		myStart = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (End of the end range).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End of the end range
     * </p> 
	 */
	public IntegerDt getEnd() {  
		if (myEnd == null) {
			myEnd = new IntegerDt();
		}
		return myEnd;
	}

	/**
	 * Sets the value(s) for <b>end</b> (End of the end range)
	 *
     * <p>
     * <b>Definition:</b>
     * End of the end range
     * </p> 
	 */
	public EndRange setEnd(IntegerDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (End of the end range)
	 *
     * <p>
     * <b>Definition:</b>
     * End of the end range
     * </p> 
	 */
	public EndRange setEnd( int theInteger) {
		myEnd = new IntegerDt(theInteger); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>GVFVariant.breakpointDetail</b> (Coordinate of a variant with zero length)
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of a variant with zero length
     * </p> 
	 */
	@Block()	
	public static class BreakpointDetail extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="seqid", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Sequence Id of the variant",
		formalDefinition="Sequence Id of the variant"
	)
	private StringDt mySeqid;
	
	@Child(name="start", type=IntegerDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Start position",
		formalDefinition="Start position"
	)
	private IntegerDt myStart;
	
	@Child(name="end", type=IntegerDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="End position",
		formalDefinition="End position"
	)
	private IntegerDt myEnd;
	
	@Child(name="strand", type=CodeDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Strand",
		formalDefinition="Direction of strand"
	)
	private CodeDt myStrand;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySeqid,  myStart,  myEnd,  myStrand);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySeqid, myStart, myEnd, myStrand);
	}
	

	/**
	 * Gets the value(s) for <b>seqid</b> (Sequence Id of the variant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence Id of the variant
     * </p> 
	 */
	public StringDt getSeqid() {  
		if (mySeqid == null) {
			mySeqid = new StringDt();
		}
		return mySeqid;
	}

	/**
	 * Sets the value(s) for <b>seqid</b> (Sequence Id of the variant)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence Id of the variant
     * </p> 
	 */
	public BreakpointDetail setSeqid(StringDt theValue) {
		mySeqid = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>seqid</b> (Sequence Id of the variant)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence Id of the variant
     * </p> 
	 */
	public BreakpointDetail setSeqid( String theString) {
		mySeqid = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>start</b> (Start position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public IntegerDt getStart() {  
		if (myStart == null) {
			myStart = new IntegerDt();
		}
		return myStart;
	}

	/**
	 * Sets the value(s) for <b>start</b> (Start position)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public BreakpointDetail setStart(IntegerDt theValue) {
		myStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>start</b> (Start position)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public BreakpointDetail setStart( int theInteger) {
		myStart = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (End position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public IntegerDt getEnd() {  
		if (myEnd == null) {
			myEnd = new IntegerDt();
		}
		return myEnd;
	}

	/**
	 * Sets the value(s) for <b>end</b> (End position)
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public BreakpointDetail setEnd(IntegerDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (End position)
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public BreakpointDetail setEnd( int theInteger) {
		myEnd = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>strand</b> (Strand).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Direction of strand
     * </p> 
	 */
	public CodeDt getStrand() {  
		if (myStrand == null) {
			myStrand = new CodeDt();
		}
		return myStrand;
	}

	/**
	 * Sets the value(s) for <b>strand</b> (Strand)
	 *
     * <p>
     * <b>Definition:</b>
     * Direction of strand
     * </p> 
	 */
	public BreakpointDetail setStrand(CodeDt theValue) {
		myStrand = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>strand</b> (Strand)
	 *
     * <p>
     * <b>Definition:</b>
     * Direction of strand
     * </p> 
	 */
	public BreakpointDetail setStrand( String theCode) {
		myStrand = new CodeDt(theCode); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>GVFVariant.sequenceContext</b> (Context of features being described)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequences adjacent to the feature
     * </p> 
	 */
	@Block()	
	public static class SequenceContext extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="fivePrime", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="5 prime of the context",
		formalDefinition="Sequence adjacent to the feature at its 5 prime"
	)
	private StringDt myFivePrime;
	
	@Child(name="threePrime", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="3 prime of the context",
		formalDefinition="Sequence adjacent to the feature at its 3 prime"
	)
	private StringDt myThreePrime;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myFivePrime,  myThreePrime);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myFivePrime, myThreePrime);
	}
	

	/**
	 * Gets the value(s) for <b>fivePrime</b> (5 prime of the context).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence adjacent to the feature at its 5 prime
     * </p> 
	 */
	public StringDt getFivePrime() {  
		if (myFivePrime == null) {
			myFivePrime = new StringDt();
		}
		return myFivePrime;
	}

	/**
	 * Sets the value(s) for <b>fivePrime</b> (5 prime of the context)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence adjacent to the feature at its 5 prime
     * </p> 
	 */
	public SequenceContext setFivePrime(StringDt theValue) {
		myFivePrime = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>fivePrime</b> (5 prime of the context)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence adjacent to the feature at its 5 prime
     * </p> 
	 */
	public SequenceContext setFivePrime( String theString) {
		myFivePrime = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>threePrime</b> (3 prime of the context).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence adjacent to the feature at its 3 prime
     * </p> 
	 */
	public StringDt getThreePrime() {  
		if (myThreePrime == null) {
			myThreePrime = new StringDt();
		}
		return myThreePrime;
	}

	/**
	 * Sets the value(s) for <b>threePrime</b> (3 prime of the context)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence adjacent to the feature at its 3 prime
     * </p> 
	 */
	public SequenceContext setThreePrime(StringDt theValue) {
		myThreePrime = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>threePrime</b> (3 prime of the context)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence adjacent to the feature at its 3 prime
     * </p> 
	 */
	public SequenceContext setThreePrime( String theString) {
		myThreePrime = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>GVFVariant.sample</b> (Individual genotypic information)
	 *
     * <p>
     * <b>Definition:</b>
     * Individual genotypic information
     * </p> 
	 */
	@Block()	
	public static class Sample extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="phased", type=StringDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Phase status of the sequence",
		formalDefinition="Attribute describing the phasing of a sequence"
	)
	private java.util.List<StringDt> myPhased;
	
	@Child(name="genotype", type=StringDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Genotype of individuals",
		formalDefinition="Genotypes of the individual"
	)
	private java.util.List<StringDt> myGenotype;
	
	@Child(name="variantReads", type=IntegerDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Read number of the sequence",
		formalDefinition="Renumber of the sequence"
	)
	private java.util.List<IntegerDt> myVariantReads;
	
	@Child(name="totalReads", type=IntegerDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Total reads",
		formalDefinition="Total reads of all sequence present in the sample"
	)
	private IntegerDt myTotalReads;
	
	@Child(name="zygosity", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Zygosity",
		formalDefinition="Zygosity of the sequences"
	)
	private CodeDt myZygosity;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPhased,  myGenotype,  myVariantReads,  myTotalReads,  myZygosity);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPhased, myGenotype, myVariantReads, myTotalReads, myZygosity);
	}
	

	/**
	 * Gets the value(s) for <b>phased</b> (Phase status of the sequence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing the phasing of a sequence
     * </p> 
	 */
	public java.util.List<StringDt> getPhased() {  
		if (myPhased == null) {
			myPhased = new java.util.ArrayList<StringDt>();
		}
		return myPhased;
	}

	/**
	 * Sets the value(s) for <b>phased</b> (Phase status of the sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing the phasing of a sequence
     * </p> 
	 */
	public Sample setPhased(java.util.List<StringDt> theValue) {
		myPhased = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>phased</b> (Phase status of the sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing the phasing of a sequence
     * </p> 
	 */
	public StringDt addPhased() {
		StringDt newType = new StringDt();
		getPhased().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>phased</b> (Phase status of the sequence),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing the phasing of a sequence
     * </p> 
	 */
	public StringDt getPhasedFirstRep() {
		if (getPhased().isEmpty()) {
			return addPhased();
		}
		return getPhased().get(0); 
	}
 	/**
	 * Adds a new value for <b>phased</b> (Phase status of the sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Attribute describing the phasing of a sequence
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Sample addPhased( String theString) {
		if (myPhased == null) {
			myPhased = new java.util.ArrayList<StringDt>();
		}
		myPhased.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genotype</b> (Genotype of individuals).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genotypes of the individual
     * </p> 
	 */
	public java.util.List<StringDt> getGenotype() {  
		if (myGenotype == null) {
			myGenotype = new java.util.ArrayList<StringDt>();
		}
		return myGenotype;
	}

	/**
	 * Sets the value(s) for <b>genotype</b> (Genotype of individuals)
	 *
     * <p>
     * <b>Definition:</b>
     * Genotypes of the individual
     * </p> 
	 */
	public Sample setGenotype(java.util.List<StringDt> theValue) {
		myGenotype = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>genotype</b> (Genotype of individuals)
	 *
     * <p>
     * <b>Definition:</b>
     * Genotypes of the individual
     * </p> 
	 */
	public StringDt addGenotype() {
		StringDt newType = new StringDt();
		getGenotype().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>genotype</b> (Genotype of individuals),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Genotypes of the individual
     * </p> 
	 */
	public StringDt getGenotypeFirstRep() {
		if (getGenotype().isEmpty()) {
			return addGenotype();
		}
		return getGenotype().get(0); 
	}
 	/**
	 * Adds a new value for <b>genotype</b> (Genotype of individuals)
	 *
     * <p>
     * <b>Definition:</b>
     * Genotypes of the individual
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Sample addGenotype( String theString) {
		if (myGenotype == null) {
			myGenotype = new java.util.ArrayList<StringDt>();
		}
		myGenotype.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>variantReads</b> (Read number of the sequence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Renumber of the sequence
     * </p> 
	 */
	public java.util.List<IntegerDt> getVariantReads() {  
		if (myVariantReads == null) {
			myVariantReads = new java.util.ArrayList<IntegerDt>();
		}
		return myVariantReads;
	}

	/**
	 * Sets the value(s) for <b>variantReads</b> (Read number of the sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Renumber of the sequence
     * </p> 
	 */
	public Sample setVariantReads(java.util.List<IntegerDt> theValue) {
		myVariantReads = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>variantReads</b> (Read number of the sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Renumber of the sequence
     * </p> 
	 */
	public IntegerDt addVariantReads() {
		IntegerDt newType = new IntegerDt();
		getVariantReads().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>variantReads</b> (Read number of the sequence),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Renumber of the sequence
     * </p> 
	 */
	public IntegerDt getVariantReadsFirstRep() {
		if (getVariantReads().isEmpty()) {
			return addVariantReads();
		}
		return getVariantReads().get(0); 
	}
 	/**
	 * Adds a new value for <b>variantReads</b> (Read number of the sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Renumber of the sequence
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Sample addVariantReads( int theInteger) {
		if (myVariantReads == null) {
			myVariantReads = new java.util.ArrayList<IntegerDt>();
		}
		myVariantReads.add(new IntegerDt(theInteger));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>totalReads</b> (Total reads).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Total reads of all sequence present in the sample
     * </p> 
	 */
	public IntegerDt getTotalReads() {  
		if (myTotalReads == null) {
			myTotalReads = new IntegerDt();
		}
		return myTotalReads;
	}

	/**
	 * Sets the value(s) for <b>totalReads</b> (Total reads)
	 *
     * <p>
     * <b>Definition:</b>
     * Total reads of all sequence present in the sample
     * </p> 
	 */
	public Sample setTotalReads(IntegerDt theValue) {
		myTotalReads = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>totalReads</b> (Total reads)
	 *
     * <p>
     * <b>Definition:</b>
     * Total reads of all sequence present in the sample
     * </p> 
	 */
	public Sample setTotalReads( int theInteger) {
		myTotalReads = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>zygosity</b> (Zygosity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Zygosity of the sequences
     * </p> 
	 */
	public CodeDt getZygosity() {  
		if (myZygosity == null) {
			myZygosity = new CodeDt();
		}
		return myZygosity;
	}

	/**
	 * Sets the value(s) for <b>zygosity</b> (Zygosity)
	 *
     * <p>
     * <b>Definition:</b>
     * Zygosity of the sequences
     * </p> 
	 */
	public Sample setZygosity(CodeDt theValue) {
		myZygosity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>zygosity</b> (Zygosity)
	 *
     * <p>
     * <b>Definition:</b>
     * Zygosity of the sequences
     * </p> 
	 */
	public Sample setZygosity( String theCode) {
		myZygosity = new CodeDt(theCode); 
		return this; 
	}

 

	}




    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.GVFVARIANT;
    }

}