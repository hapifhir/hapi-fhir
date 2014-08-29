















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
 * HAPI/FHIR <b>Test</b> Resource
 * (Test's Resource)
 *
 * <p>
 * <b>Definition:</b>
 * [Template] Master Definition
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Test">http://hl7.org/fhir/profiles/Test</a> 
 * </p>
 *
 */
@ResourceDef(name="Test", profile="http://hl7.org/fhir/profiles/Test", id="test")
public class Test extends BaseResource implements IResource {


	@Child(name="stringErr", type=StringDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Strings with invalid content",
		formalDefinition=""
	)
	private java.util.List<StringDt> myStringErr;
	
	@Child(name="stringCorr", type=StringDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Strings with correct content",
		formalDefinition=""
	)
	private java.util.List<StringDt> myStringCorr;
	
	@Child(name="booleanErr", type=BooleanDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Booleans with invalid content",
		formalDefinition=""
	)
	private java.util.List<BooleanDt> myBooleanErr;
	
	@Child(name="booleanCorr", type=BooleanDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Booleans with correct content",
		formalDefinition=""
	)
	private java.util.List<BooleanDt> myBooleanCorr;
	
	@Child(name="integerErr", type=IntegerDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Integers with invalid content",
		formalDefinition=""
	)
	private java.util.List<IntegerDt> myIntegerErr;
	
	@Child(name="integerCorr", type=IntegerDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Integers with correct content",
		formalDefinition=""
	)
	private java.util.List<IntegerDt> myIntegerCorr;
	
	@Child(name="decimalErr", type=DecimalDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Decimals with invalid content",
		formalDefinition=""
	)
	private java.util.List<DecimalDt> myDecimalErr;
	
	@Child(name="decimalCorr", type=DecimalDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Decimals with correct content",
		formalDefinition=""
	)
	private java.util.List<DecimalDt> myDecimalCorr;
	
	@Child(name="b64Err", type=Base64BinaryDt.class, order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Binaries with invalid content",
		formalDefinition=""
	)
	private java.util.List<Base64BinaryDt> myB64Err;
	
	@Child(name="b64Corr", type=Base64BinaryDt.class, order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Binaries with correct content",
		formalDefinition=""
	)
	private java.util.List<Base64BinaryDt> myB64Corr;
	
	@Child(name="instantErr", type=InstantDt.class, order=10, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Instants with invalid content",
		formalDefinition=""
	)
	private java.util.List<InstantDt> myInstantErr;
	
	@Child(name="instantCorr", type=InstantDt.class, order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Instants with correct content",
		formalDefinition=""
	)
	private java.util.List<InstantDt> myInstantCorr;
	
	@Child(name="uriErr", type=UriDt.class, order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Uri's with invalid content",
		formalDefinition=""
	)
	private java.util.List<UriDt> myUriErr;
	
	@Child(name="uriCorr", type=UriDt.class, order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Uri's with correct content",
		formalDefinition=""
	)
	private java.util.List<UriDt> myUriCorr;
	
	@Child(name="idrefSingle", type=IdrefDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="Test idref",
		formalDefinition=""
	)
	private IdrefDt myIdrefSingle;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStringErr,  myStringCorr,  myBooleanErr,  myBooleanCorr,  myIntegerErr,  myIntegerCorr,  myDecimalErr,  myDecimalCorr,  myB64Err,  myB64Corr,  myInstantErr,  myInstantCorr,  myUriErr,  myUriCorr,  myIdrefSingle);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStringErr, myStringCorr, myBooleanErr, myBooleanCorr, myIntegerErr, myIntegerCorr, myDecimalErr, myDecimalCorr, myB64Err, myB64Corr, myInstantErr, myInstantCorr, myUriErr, myUriCorr, myIdrefSingle);
	}

	/**
	 * Gets the value(s) for <b>stringErr</b> (Strings with invalid content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<StringDt> getStringErr() {  
		if (myStringErr == null) {
			myStringErr = new java.util.ArrayList<StringDt>();
		}
		return myStringErr;
	}

	/**
	 * Sets the value(s) for <b>stringErr</b> (Strings with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setStringErr(java.util.List<StringDt> theValue) {
		myStringErr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>stringErr</b> (Strings with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt addStringErr() {
		StringDt newType = new StringDt();
		getStringErr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>stringErr</b> (Strings with invalid content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getStringErrFirstRep() {
		if (getStringErr().isEmpty()) {
			return addStringErr();
		}
		return getStringErr().get(0); 
	}
 	/**
	 * Adds a new value for <b>stringErr</b> (Strings with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addStringErr( String theString) {
		if (myStringErr == null) {
			myStringErr = new java.util.ArrayList<StringDt>();
		}
		myStringErr.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>stringCorr</b> (Strings with correct content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<StringDt> getStringCorr() {  
		if (myStringCorr == null) {
			myStringCorr = new java.util.ArrayList<StringDt>();
		}
		return myStringCorr;
	}

	/**
	 * Sets the value(s) for <b>stringCorr</b> (Strings with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setStringCorr(java.util.List<StringDt> theValue) {
		myStringCorr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>stringCorr</b> (Strings with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt addStringCorr() {
		StringDt newType = new StringDt();
		getStringCorr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>stringCorr</b> (Strings with correct content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getStringCorrFirstRep() {
		if (getStringCorr().isEmpty()) {
			return addStringCorr();
		}
		return getStringCorr().get(0); 
	}
 	/**
	 * Adds a new value for <b>stringCorr</b> (Strings with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addStringCorr( String theString) {
		if (myStringCorr == null) {
			myStringCorr = new java.util.ArrayList<StringDt>();
		}
		myStringCorr.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>booleanErr</b> (Booleans with invalid content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<BooleanDt> getBooleanErr() {  
		if (myBooleanErr == null) {
			myBooleanErr = new java.util.ArrayList<BooleanDt>();
		}
		return myBooleanErr;
	}

	/**
	 * Sets the value(s) for <b>booleanErr</b> (Booleans with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setBooleanErr(java.util.List<BooleanDt> theValue) {
		myBooleanErr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>booleanErr</b> (Booleans with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BooleanDt addBooleanErr() {
		BooleanDt newType = new BooleanDt();
		getBooleanErr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>booleanErr</b> (Booleans with invalid content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BooleanDt getBooleanErrFirstRep() {
		if (getBooleanErr().isEmpty()) {
			return addBooleanErr();
		}
		return getBooleanErr().get(0); 
	}
 	/**
	 * Adds a new value for <b>booleanErr</b> (Booleans with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addBooleanErr( boolean theBoolean) {
		if (myBooleanErr == null) {
			myBooleanErr = new java.util.ArrayList<BooleanDt>();
		}
		myBooleanErr.add(new BooleanDt(theBoolean));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>booleanCorr</b> (Booleans with correct content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<BooleanDt> getBooleanCorr() {  
		if (myBooleanCorr == null) {
			myBooleanCorr = new java.util.ArrayList<BooleanDt>();
		}
		return myBooleanCorr;
	}

	/**
	 * Sets the value(s) for <b>booleanCorr</b> (Booleans with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setBooleanCorr(java.util.List<BooleanDt> theValue) {
		myBooleanCorr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>booleanCorr</b> (Booleans with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BooleanDt addBooleanCorr() {
		BooleanDt newType = new BooleanDt();
		getBooleanCorr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>booleanCorr</b> (Booleans with correct content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BooleanDt getBooleanCorrFirstRep() {
		if (getBooleanCorr().isEmpty()) {
			return addBooleanCorr();
		}
		return getBooleanCorr().get(0); 
	}
 	/**
	 * Adds a new value for <b>booleanCorr</b> (Booleans with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addBooleanCorr( boolean theBoolean) {
		if (myBooleanCorr == null) {
			myBooleanCorr = new java.util.ArrayList<BooleanDt>();
		}
		myBooleanCorr.add(new BooleanDt(theBoolean));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>integerErr</b> (Integers with invalid content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<IntegerDt> getIntegerErr() {  
		if (myIntegerErr == null) {
			myIntegerErr = new java.util.ArrayList<IntegerDt>();
		}
		return myIntegerErr;
	}

	/**
	 * Sets the value(s) for <b>integerErr</b> (Integers with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setIntegerErr(java.util.List<IntegerDt> theValue) {
		myIntegerErr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>integerErr</b> (Integers with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IntegerDt addIntegerErr() {
		IntegerDt newType = new IntegerDt();
		getIntegerErr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>integerErr</b> (Integers with invalid content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IntegerDt getIntegerErrFirstRep() {
		if (getIntegerErr().isEmpty()) {
			return addIntegerErr();
		}
		return getIntegerErr().get(0); 
	}
 	/**
	 * Adds a new value for <b>integerErr</b> (Integers with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addIntegerErr( int theInteger) {
		if (myIntegerErr == null) {
			myIntegerErr = new java.util.ArrayList<IntegerDt>();
		}
		myIntegerErr.add(new IntegerDt(theInteger));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>integerCorr</b> (Integers with correct content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<IntegerDt> getIntegerCorr() {  
		if (myIntegerCorr == null) {
			myIntegerCorr = new java.util.ArrayList<IntegerDt>();
		}
		return myIntegerCorr;
	}

	/**
	 * Sets the value(s) for <b>integerCorr</b> (Integers with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setIntegerCorr(java.util.List<IntegerDt> theValue) {
		myIntegerCorr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>integerCorr</b> (Integers with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IntegerDt addIntegerCorr() {
		IntegerDt newType = new IntegerDt();
		getIntegerCorr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>integerCorr</b> (Integers with correct content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IntegerDt getIntegerCorrFirstRep() {
		if (getIntegerCorr().isEmpty()) {
			return addIntegerCorr();
		}
		return getIntegerCorr().get(0); 
	}
 	/**
	 * Adds a new value for <b>integerCorr</b> (Integers with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addIntegerCorr( int theInteger) {
		if (myIntegerCorr == null) {
			myIntegerCorr = new java.util.ArrayList<IntegerDt>();
		}
		myIntegerCorr.add(new IntegerDt(theInteger));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>decimalErr</b> (Decimals with invalid content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<DecimalDt> getDecimalErr() {  
		if (myDecimalErr == null) {
			myDecimalErr = new java.util.ArrayList<DecimalDt>();
		}
		return myDecimalErr;
	}

	/**
	 * Sets the value(s) for <b>decimalErr</b> (Decimals with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setDecimalErr(java.util.List<DecimalDt> theValue) {
		myDecimalErr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>decimalErr</b> (Decimals with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DecimalDt addDecimalErr() {
		DecimalDt newType = new DecimalDt();
		getDecimalErr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>decimalErr</b> (Decimals with invalid content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DecimalDt getDecimalErrFirstRep() {
		if (getDecimalErr().isEmpty()) {
			return addDecimalErr();
		}
		return getDecimalErr().get(0); 
	}
 	/**
	 * Adds a new value for <b>decimalErr</b> (Decimals with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addDecimalErr( long theValue) {
		if (myDecimalErr == null) {
			myDecimalErr = new java.util.ArrayList<DecimalDt>();
		}
		myDecimalErr.add(new DecimalDt(theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>decimalErr</b> (Decimals with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addDecimalErr( double theValue) {
		if (myDecimalErr == null) {
			myDecimalErr = new java.util.ArrayList<DecimalDt>();
		}
		myDecimalErr.add(new DecimalDt(theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>decimalErr</b> (Decimals with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addDecimalErr( java.math.BigDecimal theValue) {
		if (myDecimalErr == null) {
			myDecimalErr = new java.util.ArrayList<DecimalDt>();
		}
		myDecimalErr.add(new DecimalDt(theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>decimalCorr</b> (Decimals with correct content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<DecimalDt> getDecimalCorr() {  
		if (myDecimalCorr == null) {
			myDecimalCorr = new java.util.ArrayList<DecimalDt>();
		}
		return myDecimalCorr;
	}

	/**
	 * Sets the value(s) for <b>decimalCorr</b> (Decimals with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setDecimalCorr(java.util.List<DecimalDt> theValue) {
		myDecimalCorr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>decimalCorr</b> (Decimals with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DecimalDt addDecimalCorr() {
		DecimalDt newType = new DecimalDt();
		getDecimalCorr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>decimalCorr</b> (Decimals with correct content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DecimalDt getDecimalCorrFirstRep() {
		if (getDecimalCorr().isEmpty()) {
			return addDecimalCorr();
		}
		return getDecimalCorr().get(0); 
	}
 	/**
	 * Adds a new value for <b>decimalCorr</b> (Decimals with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addDecimalCorr( long theValue) {
		if (myDecimalCorr == null) {
			myDecimalCorr = new java.util.ArrayList<DecimalDt>();
		}
		myDecimalCorr.add(new DecimalDt(theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>decimalCorr</b> (Decimals with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addDecimalCorr( double theValue) {
		if (myDecimalCorr == null) {
			myDecimalCorr = new java.util.ArrayList<DecimalDt>();
		}
		myDecimalCorr.add(new DecimalDt(theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>decimalCorr</b> (Decimals with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addDecimalCorr( java.math.BigDecimal theValue) {
		if (myDecimalCorr == null) {
			myDecimalCorr = new java.util.ArrayList<DecimalDt>();
		}
		myDecimalCorr.add(new DecimalDt(theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>b64Err</b> (Binaries with invalid content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<Base64BinaryDt> getB64Err() {  
		if (myB64Err == null) {
			myB64Err = new java.util.ArrayList<Base64BinaryDt>();
		}
		return myB64Err;
	}

	/**
	 * Sets the value(s) for <b>b64Err</b> (Binaries with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setB64Err(java.util.List<Base64BinaryDt> theValue) {
		myB64Err = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>b64Err</b> (Binaries with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Base64BinaryDt addB64Err() {
		Base64BinaryDt newType = new Base64BinaryDt();
		getB64Err().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>b64Err</b> (Binaries with invalid content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Base64BinaryDt getB64ErrFirstRep() {
		if (getB64Err().isEmpty()) {
			return addB64Err();
		}
		return getB64Err().get(0); 
	}
 	/**
	 * Adds a new value for <b>b64Err</b> (Binaries with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addB64Err( byte[] theBytes) {
		if (myB64Err == null) {
			myB64Err = new java.util.ArrayList<Base64BinaryDt>();
		}
		myB64Err.add(new Base64BinaryDt(theBytes));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>b64Corr</b> (Binaries with correct content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<Base64BinaryDt> getB64Corr() {  
		if (myB64Corr == null) {
			myB64Corr = new java.util.ArrayList<Base64BinaryDt>();
		}
		return myB64Corr;
	}

	/**
	 * Sets the value(s) for <b>b64Corr</b> (Binaries with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setB64Corr(java.util.List<Base64BinaryDt> theValue) {
		myB64Corr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>b64Corr</b> (Binaries with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Base64BinaryDt addB64Corr() {
		Base64BinaryDt newType = new Base64BinaryDt();
		getB64Corr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>b64Corr</b> (Binaries with correct content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Base64BinaryDt getB64CorrFirstRep() {
		if (getB64Corr().isEmpty()) {
			return addB64Corr();
		}
		return getB64Corr().get(0); 
	}
 	/**
	 * Adds a new value for <b>b64Corr</b> (Binaries with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addB64Corr( byte[] theBytes) {
		if (myB64Corr == null) {
			myB64Corr = new java.util.ArrayList<Base64BinaryDt>();
		}
		myB64Corr.add(new Base64BinaryDt(theBytes));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>instantErr</b> (Instants with invalid content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<InstantDt> getInstantErr() {  
		if (myInstantErr == null) {
			myInstantErr = new java.util.ArrayList<InstantDt>();
		}
		return myInstantErr;
	}

	/**
	 * Sets the value(s) for <b>instantErr</b> (Instants with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setInstantErr(java.util.List<InstantDt> theValue) {
		myInstantErr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>instantErr</b> (Instants with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt addInstantErr() {
		InstantDt newType = new InstantDt();
		getInstantErr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>instantErr</b> (Instants with invalid content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getInstantErrFirstRep() {
		if (getInstantErr().isEmpty()) {
			return addInstantErr();
		}
		return getInstantErr().get(0); 
	}
 	/**
	 * Adds a new value for <b>instantErr</b> (Instants with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addInstantErr( Date theDate,  TemporalPrecisionEnum thePrecision) {
		if (myInstantErr == null) {
			myInstantErr = new java.util.ArrayList<InstantDt>();
		}
		myInstantErr.add(new InstantDt(theDate, thePrecision));
		return this; 
	}

	/**
	 * Adds a new value for <b>instantErr</b> (Instants with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addInstantErr( Date theDate) {
		if (myInstantErr == null) {
			myInstantErr = new java.util.ArrayList<InstantDt>();
		}
		myInstantErr.add(new InstantDt(theDate));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>instantCorr</b> (Instants with correct content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<InstantDt> getInstantCorr() {  
		if (myInstantCorr == null) {
			myInstantCorr = new java.util.ArrayList<InstantDt>();
		}
		return myInstantCorr;
	}

	/**
	 * Sets the value(s) for <b>instantCorr</b> (Instants with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setInstantCorr(java.util.List<InstantDt> theValue) {
		myInstantCorr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>instantCorr</b> (Instants with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt addInstantCorr() {
		InstantDt newType = new InstantDt();
		getInstantCorr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>instantCorr</b> (Instants with correct content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getInstantCorrFirstRep() {
		if (getInstantCorr().isEmpty()) {
			return addInstantCorr();
		}
		return getInstantCorr().get(0); 
	}
 	/**
	 * Adds a new value for <b>instantCorr</b> (Instants with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addInstantCorr( Date theDate,  TemporalPrecisionEnum thePrecision) {
		if (myInstantCorr == null) {
			myInstantCorr = new java.util.ArrayList<InstantDt>();
		}
		myInstantCorr.add(new InstantDt(theDate, thePrecision));
		return this; 
	}

	/**
	 * Adds a new value for <b>instantCorr</b> (Instants with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addInstantCorr( Date theDate) {
		if (myInstantCorr == null) {
			myInstantCorr = new java.util.ArrayList<InstantDt>();
		}
		myInstantCorr.add(new InstantDt(theDate));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>uriErr</b> (Uri's with invalid content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<UriDt> getUriErr() {  
		if (myUriErr == null) {
			myUriErr = new java.util.ArrayList<UriDt>();
		}
		return myUriErr;
	}

	/**
	 * Sets the value(s) for <b>uriErr</b> (Uri's with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setUriErr(java.util.List<UriDt> theValue) {
		myUriErr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>uriErr</b> (Uri's with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt addUriErr() {
		UriDt newType = new UriDt();
		getUriErr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>uriErr</b> (Uri's with invalid content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getUriErrFirstRep() {
		if (getUriErr().isEmpty()) {
			return addUriErr();
		}
		return getUriErr().get(0); 
	}
 	/**
	 * Adds a new value for <b>uriErr</b> (Uri's with invalid content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addUriErr( String theUri) {
		if (myUriErr == null) {
			myUriErr = new java.util.ArrayList<UriDt>();
		}
		myUriErr.add(new UriDt(theUri));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>uriCorr</b> (Uri's with correct content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<UriDt> getUriCorr() {  
		if (myUriCorr == null) {
			myUriCorr = new java.util.ArrayList<UriDt>();
		}
		return myUriCorr;
	}

	/**
	 * Sets the value(s) for <b>uriCorr</b> (Uri's with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setUriCorr(java.util.List<UriDt> theValue) {
		myUriCorr = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>uriCorr</b> (Uri's with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt addUriCorr() {
		UriDt newType = new UriDt();
		getUriCorr().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>uriCorr</b> (Uri's with correct content),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getUriCorrFirstRep() {
		if (getUriCorr().isEmpty()) {
			return addUriCorr();
		}
		return getUriCorr().get(0); 
	}
 	/**
	 * Adds a new value for <b>uriCorr</b> (Uri's with correct content)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Test addUriCorr( String theUri) {
		if (myUriCorr == null) {
			myUriCorr = new java.util.ArrayList<UriDt>();
		}
		myUriCorr.add(new UriDt(theUri));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>idrefSingle</b> (Test idref).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IdrefDt getIdrefSingle() {  
		if (myIdrefSingle == null) {
			myIdrefSingle = new IdrefDt();
		}
		return myIdrefSingle;
	}

	/**
	 * Sets the value(s) for <b>idrefSingle</b> (Test idref)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Test setIdrefSingle(IdrefDt theValue) {
		myIdrefSingle = theValue;
		return this;
	}

  


}