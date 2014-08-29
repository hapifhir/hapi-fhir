















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
 * HAPI/FHIR <b>SequencingAnalysis</b> Resource
 * (Sequencing Analysis)
 *
 * <p>
 * <b>Definition:</b>
 * Computational analysis on a patient's genetic raw file
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/SequencingAnalysis">http://hl7.org/fhir/profiles/SequencingAnalysis</a> 
 * </p>
 *
 */
@ResourceDef(name="SequencingAnalysis", profile="http://hl7.org/fhir/profiles/SequencingAnalysis", id="sequencinganalysis")
public class SequencingAnalysis extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SequencingAnalysis.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="SequencingAnalysis.subject", description="Subject of the analysis", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SequencingAnalysis.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingAnalysis.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("SequencingAnalysis.subject");

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is updated</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SequencingAnalysis.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="SequencingAnalysis.date", description="Date when result of the analysis is updated", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is updated</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SequencingAnalysis.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>genome</b>
	 * <p>
	 * Description: <b>Name of the reference genome used in the analysis</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingAnalysis.genome.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="genome", path="SequencingAnalysis.genome.name", description="Name of the reference genome used in the analysis", type="string"  )
	public static final String SP_GENOME = "genome";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>genome</b>
	 * <p>
	 * Description: <b>Name of the reference genome used in the analysis</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingAnalysis.genome.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam GENOME = new StringClientParam(SP_GENOME);


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Subject",
		formalDefinition="Subject of the analysis"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="date", type=DateDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Date",
		formalDefinition="Date when result of the analysis is updated"
	)
	private DateDt myDate;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Name",
		formalDefinition="Name of the analysis"
	)
	private StringDt myName;
	
	@Child(name="genome", order=3, min=1, max=1)	
	@Description(
		shortDefinition="Reference genome",
		formalDefinition="Reference genome used in the analysis"
	)
	private Genome myGenome;
	
	@Child(name="file", type=AttachmentDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="File",
		formalDefinition="Files uploaded as result of the analysis"
	)
	private java.util.List<AttachmentDt> myFile;
	
	@Child(name="inputLab", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.SequencingLab.class	})
	@Description(
		shortDefinition="Input lab",
		formalDefinition="SequencingLab taken into account of the analysis"
	)
	private java.util.List<ResourceReferenceDt> myInputLab;
	
	@Child(name="inputAnalysis", order=6, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.SequencingAnalysis.class	})
	@Description(
		shortDefinition="Input analysis",
		formalDefinition="SequencingAnalysis taken into account of the analysis"
	)
	private java.util.List<ResourceReferenceDt> myInputAnalysis;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myDate,  myName,  myGenome,  myFile,  myInputLab,  myInputAnalysis);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myDate, myName, myGenome, myFile, myInputLab, myInputAnalysis);
	}

	/**
	 * Gets the value(s) for <b>subject</b> (Subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the analysis
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Subject)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the analysis
     * </p> 
	 */
	public SequencingAnalysis setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>date</b> (Date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public DateDt getDate() {  
		if (myDate == null) {
			myDate = new DateDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public SequencingAnalysis setDate(DateDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public SequencingAnalysis setDateWithDayPrecision( Date theDate) {
		myDate = new DateDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public SequencingAnalysis setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the analysis
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the analysis
     * </p> 
	 */
	public SequencingAnalysis setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the analysis
     * </p> 
	 */
	public SequencingAnalysis setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genome</b> (Reference genome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference genome used in the analysis
     * </p> 
	 */
	public Genome getGenome() {  
		if (myGenome == null) {
			myGenome = new Genome();
		}
		return myGenome;
	}

	/**
	 * Sets the value(s) for <b>genome</b> (Reference genome)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference genome used in the analysis
     * </p> 
	 */
	public SequencingAnalysis setGenome(Genome theValue) {
		myGenome = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>file</b> (File).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as result of the analysis
     * </p> 
	 */
	public java.util.List<AttachmentDt> getFile() {  
		if (myFile == null) {
			myFile = new java.util.ArrayList<AttachmentDt>();
		}
		return myFile;
	}

	/**
	 * Sets the value(s) for <b>file</b> (File)
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as result of the analysis
     * </p> 
	 */
	public SequencingAnalysis setFile(java.util.List<AttachmentDt> theValue) {
		myFile = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>file</b> (File)
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as result of the analysis
     * </p> 
	 */
	public AttachmentDt addFile() {
		AttachmentDt newType = new AttachmentDt();
		getFile().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>file</b> (File),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as result of the analysis
     * </p> 
	 */
	public AttachmentDt getFileFirstRep() {
		if (getFile().isEmpty()) {
			return addFile();
		}
		return getFile().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>inputLab </b> (Input lab).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingLab taken into account of the analysis
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getInputLab() {  
		if (myInputLab == null) {
			myInputLab = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myInputLab;
	}

	/**
	 * Sets the value(s) for <b>inputLab </b> (Input lab)
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingLab taken into account of the analysis
     * </p> 
	 */
	public SequencingAnalysis setInputLab(java.util.List<ResourceReferenceDt> theValue) {
		myInputLab = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>inputLab </b> (Input lab)
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingLab taken into account of the analysis
     * </p> 
	 */
	public ResourceReferenceDt addInputLab() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getInputLab().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>inputAnalysis </b> (Input analysis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingAnalysis taken into account of the analysis
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getInputAnalysis() {  
		if (myInputAnalysis == null) {
			myInputAnalysis = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myInputAnalysis;
	}

	/**
	 * Sets the value(s) for <b>inputAnalysis </b> (Input analysis)
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingAnalysis taken into account of the analysis
     * </p> 
	 */
	public SequencingAnalysis setInputAnalysis(java.util.List<ResourceReferenceDt> theValue) {
		myInputAnalysis = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>inputAnalysis </b> (Input analysis)
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingAnalysis taken into account of the analysis
     * </p> 
	 */
	public ResourceReferenceDt addInputAnalysis() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getInputAnalysis().add(newType);
		return newType; 
	}
  
	/**
	 * Block class for child element: <b>SequencingAnalysis.genome</b> (Reference genome)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference genome used in the analysis
     * </p> 
	 */
	@Block()	
	public static class Genome extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Name",
		formalDefinition="Name of the reference genome"
	)
	private CodeDt myName;
	
	@Child(name="build", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Build",
		formalDefinition="Build number of the refernece genome"
	)
	private StringDt myBuild;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myBuild);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myBuild);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the reference genome
     * </p> 
	 */
	public CodeDt getName() {  
		if (myName == null) {
			myName = new CodeDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the reference genome
     * </p> 
	 */
	public Genome setName(CodeDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the reference genome
     * </p> 
	 */
	public Genome setName( String theCode) {
		myName = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>build</b> (Build).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Build number of the refernece genome
     * </p> 
	 */
	public StringDt getBuild() {  
		if (myBuild == null) {
			myBuild = new StringDt();
		}
		return myBuild;
	}

	/**
	 * Sets the value(s) for <b>build</b> (Build)
	 *
     * <p>
     * <b>Definition:</b>
     * Build number of the refernece genome
     * </p> 
	 */
	public Genome setBuild(StringDt theValue) {
		myBuild = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>build</b> (Build)
	 *
     * <p>
     * <b>Definition:</b>
     * Build number of the refernece genome
     * </p> 
	 */
	public Genome setBuild( String theString) {
		myBuild = new StringDt(theString); 
		return this; 
	}

 

	}




}