















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR Library
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


import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.RangeDt;
import ca.uhn.fhir.model.dstu.composite.RatioDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.composite.SampledDataDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationInterpretationCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.CompositeParam;
import ca.uhn.fhir.rest.gclient.DateParam;
import ca.uhn.fhir.rest.gclient.Include;
import ca.uhn.fhir.rest.gclient.QuantityParam;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.StringParam;
import ca.uhn.fhir.rest.gclient.TokenParam;


/**
 * HAPI/FHIR <b>Observation</b> Resource
 * (Measurements and simple assertions)
 *
 * <p>
 * <b>Definition:</b>
 * Measurements and simple assertions made about a patient, device or other subject
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Observations are a key aspect of healthcare.  This resource is used to capture those that do not require more sophisticated mechanisms.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Observation">http://hl7.org/fhir/profiles/Observation</a> 
 * </p>
 *
 */
@ResourceDef(name="Observation", profile="http://hl7.org/fhir/profiles/Observation", id="observation")
public class Observation extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the observation type</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Observation.name", description="The name of the observation type", type="token")
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the observation type</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.name</b><br/>
	 * </p>
	 */
	public static final TokenParam NAME = new TokenParam(SP_NAME);

	/**
	 * Search parameter constant for <b>value-quantity</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)</b><br/>
	 * Type: <b>quantity</b><br/>
	 * Path: <b>Observation.value[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="value-quantity", path="Observation.value[x]", description="The value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)", type="quantity")
	public static final String SP_VALUE_QUANTITY = "value-quantity";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>value-quantity</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)</b><br/>
	 * Type: <b>quantity</b><br/>
	 * Path: <b>Observation.value[x]</b><br/>
	 * </p>
	 */
	public static final QuantityParam VALUE_QUANTITY = new QuantityParam(SP_VALUE_QUANTITY);

	/**
	 * Search parameter constant for <b>value-concept</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a CodeableConcept</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.value[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="value-concept", path="Observation.value[x]", description="The value of the observation, if the value is a CodeableConcept", type="token")
	public static final String SP_VALUE_CONCEPT = "value-concept";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>value-concept</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a CodeableConcept</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.value[x]</b><br/>
	 * </p>
	 */
	public static final TokenParam VALUE_CONCEPT = new TokenParam(SP_VALUE_CONCEPT);

	/**
	 * Search parameter constant for <b>value-date</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a Period</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Observation.value[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="value-date", path="Observation.value[x]", description="The value of the observation, if the value is a Period", type="date")
	public static final String SP_VALUE_DATE = "value-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>value-date</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a Period</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Observation.value[x]</b><br/>
	 * </p>
	 */
	public static final DateParam VALUE_DATE = new DateParam(SP_VALUE_DATE);

	/**
	 * Search parameter constant for <b>value-string</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a string, and also searches in CodeableConcept.text</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Observation.value[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="value-string", path="Observation.value[x]", description="The value of the observation, if the value is a string, and also searches in CodeableConcept.text", type="string")
	public static final String SP_VALUE_STRING = "value-string";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>value-string</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a string, and also searches in CodeableConcept.text</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Observation.value[x]</b><br/>
	 * </p>
	 */
	public static final StringParam VALUE_STRING = new StringParam(SP_VALUE_STRING);

	/**
	 * Search parameter constant for <b>name-value-[x]</b>
	 * <p>
	 * Description: <b>Both name and one of the value parameters</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>name & value-[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name-value-[x]", path="name & value-[x]", description="Both name and one of the value parameters", type="composite")
	public static final String SP_NAME_VALUE_X = "name-value-[x]";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name-value-[x]</b>
	 * <p>
	 * Description: <b>Both name and one of the value parameters</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>name & value-[x]</b><br/>
	 * </p>
	 */
	public static final CompositeParam NAME_VALUE_X = new CompositeParam(SP_NAME_VALUE_X);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Obtained date/time. If the obtained element is a period, a date that falls in the period</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Observation.applies[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Observation.applies[x]", description="Obtained date/time. If the obtained element is a period, a date that falls in the period", type="date")
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Obtained date/time. If the obtained element is a period, a date that falls in the period</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Observation.applies[x]</b><br/>
	 * </p>
	 */
	public static final DateParam DATE = new DateParam(SP_DATE);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the observation</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Observation.status", description="The status of the observation", type="token")
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the observation</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.status</b><br/>
	 * </p>
	 */
	public static final TokenParam STATUS = new TokenParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>reliability</b>
	 * <p>
	 * Description: <b>The reliability of the observation</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.reliability</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reliability", path="Observation.reliability", description="The reliability of the observation", type="token")
	public static final String SP_RELIABILITY = "reliability";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reliability</b>
	 * <p>
	 * Description: <b>The reliability of the observation</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.reliability</b><br/>
	 * </p>
	 */
	public static final TokenParam RELIABILITY = new TokenParam(SP_RELIABILITY);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the observation is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Observation.subject", description="The subject that the observation is about", type="reference")
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the observation is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceParam SUBJECT = new ReferenceParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Observation.subject");

	/**
	 * Search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>Who and/or what performed the observation</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.performer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="performer", path="Observation.performer", description="Who and/or what performed the observation", type="reference")
	public static final String SP_PERFORMER = "performer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>Who and/or what performed the observation</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.performer</b><br/>
	 * </p>
	 */
	public static final ReferenceParam PERFORMER = new ReferenceParam(SP_PERFORMER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.performer</b>".
	 */
	public static final Include INCLUDE_PERFORMER = new Include("Observation.performer");

	/**
	 * Search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.specimen</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="specimen", path="Observation.specimen", description="", type="reference")
	public static final String SP_SPECIMEN = "specimen";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.specimen</b><br/>
	 * </p>
	 */
	public static final ReferenceParam SPECIMEN = new ReferenceParam(SP_SPECIMEN);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.specimen</b>".
	 */
	public static final Include INCLUDE_SPECIMEN = new Include("Observation.specimen");

	/**
	 * Search parameter constant for <b>related-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.related.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="related-type", path="Observation.related.type", description="", type="token")
	public static final String SP_RELATED_TYPE = "related-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>related-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.related.type</b><br/>
	 * </p>
	 */
	public static final TokenParam RELATED_TYPE = new TokenParam(SP_RELATED_TYPE);

	/**
	 * Search parameter constant for <b>related-target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.related.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="related-target", path="Observation.related.target", description="", type="reference")
	public static final String SP_RELATED_TARGET = "related-target";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>related-target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.related.target</b><br/>
	 * </p>
	 */
	public static final ReferenceParam RELATED_TARGET = new ReferenceParam(SP_RELATED_TARGET);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.related.target</b>".
	 */
	public static final Include INCLUDE_RELATED_TARGET = new Include("Observation.related.target");

	/**
	 * Search parameter constant for <b>related</b>
	 * <p>
	 * Description: <b>Related Observations - search on related-type and related-target together</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>related-target & related-type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="related", path="related-target & related-type", description="Related Observations - search on related-type and related-target together", type="composite")
	public static final String SP_RELATED = "related";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>related</b>
	 * <p>
	 * Description: <b>Related Observations - search on related-type and related-target together</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>related-target & related-type</b><br/>
	 * </p>
	 */
	public static final CompositeParam RELATED = new CompositeParam(SP_RELATED);


	@Child(name="name", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Type of observation (code / type)",
		formalDefinition="Describes what was observed. Sometimes this is called the observation \"code\""
	)
	private CodeableConceptDt myName;
	
	@Child(name="value", order=1, min=0, max=1, type={
		QuantityDt.class, 		CodeableConceptDt.class, 		AttachmentDt.class, 		RatioDt.class, 		PeriodDt.class, 		SampledDataDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="Actual result",
		formalDefinition="The information determined as a result of making the observation, if the information has a simple value"
	)
	private IDatatype myValue;
	
	@Child(name="interpretation", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="High, low, normal, etc.",
		formalDefinition="The assessment made based on the result of the observation."
	)
	private BoundCodeableConceptDt<ObservationInterpretationCodesEnum> myInterpretation;
	
	@Child(name="comments", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Comments about result",
		formalDefinition="May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result."
	)
	private StringDt myComments;
	
	@Child(name="applies", order=4, min=0, max=1, type={
		DateTimeDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="Physiologically Relevant time/time-period for observation",
		formalDefinition="The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the \"physiologically relevant time\". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself"
	)
	private IDatatype myApplies;
	
	@Child(name="issued", type=InstantDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Date/Time this was made available",
		formalDefinition=""
	)
	private InstantDt myIssued;
	
	@Child(name="status", type=CodeDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="registered | preliminary | final | amended +",
		formalDefinition="The status of the result value"
	)
	private BoundCodeDt<ObservationStatusEnum> myStatus;
	
	@Child(name="reliability", type=CodeDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="ok | ongoing | early | questionable | calibrating | error +",
		formalDefinition="An estimate of the degree to which quality issues have impacted on the value reported"
	)
	private BoundCodeDt<ObservationReliabilityEnum> myReliability;
	
	@Child(name="bodySite", type=CodeableConceptDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Observed body part",
		formalDefinition="Indicates where on the subject's body the observation was made."
	)
	private CodeableConceptDt myBodySite;
	
	@Child(name="method", type=CodeableConceptDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="How it was done",
		formalDefinition="Indicates the mechanism used to perform the observation"
	)
	private CodeableConceptDt myMethod;
	
	@Child(name="identifier", type=IdentifierDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Unique Id for this particular observation",
		formalDefinition="A unique identifier for the simple observation"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="subject", order=11, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Who and/or what this is about",
		formalDefinition="The thing the observation is being made about"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="specimen", order=12, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Specimen.class	})
	@Description(
		shortDefinition="Specimen used for this observation",
		formalDefinition="The specimen that was used when this observation was made"
	)
	private ResourceReferenceDt mySpecimen;
	
	@Child(name="performer", order=13, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Who did the observation",
		formalDefinition="Who was responsible for asserting the observed value as \"true\""
	)
	private java.util.List<ResourceReferenceDt> myPerformer;
	
	@Child(name="referenceRange", order=14, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Provides guide for interpretation",
		formalDefinition="Guidance on how to interpret the value by comparison to a normal or recommended range"
	)
	private java.util.List<ReferenceRange> myReferenceRange;
	
	@Child(name="related", order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Observations related to this observation",
		formalDefinition="Related observations - either components, or previous observations, or statements of derivation"
	)
	private java.util.List<Related> myRelated;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myValue,  myInterpretation,  myComments,  myApplies,  myIssued,  myStatus,  myReliability,  myBodySite,  myMethod,  myIdentifier,  mySubject,  mySpecimen,  myPerformer,  myReferenceRange,  myRelated);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myValue, myInterpretation, myComments, myApplies, myIssued, myStatus, myReliability, myBodySite, myMethod, myIdentifier, mySubject, mySpecimen, myPerformer, myReferenceRange, myRelated);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Type of observation (code / type)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes what was observed. Sometimes this is called the observation \"code\"
     * </p> 
	 */
	public CodeableConceptDt getName() {  
		if (myName == null) {
			myName = new CodeableConceptDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Type of observation (code / type))
	 *
     * <p>
     * <b>Definition:</b>
     * Describes what was observed. Sometimes this is called the observation \"code\"
     * </p> 
	 */
	public Observation setName(CodeableConceptDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>value[x]</b> (Actual result).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The information determined as a result of making the observation, if the information has a simple value
     * </p> 
	 */
	public IDatatype getValue() {  
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value[x]</b> (Actual result)
	 *
     * <p>
     * <b>Definition:</b>
     * The information determined as a result of making the observation, if the information has a simple value
     * </p> 
	 */
	public Observation setValue(IDatatype theValue) {
		myValue = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>interpretation</b> (High, low, normal, etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public BoundCodeableConceptDt<ObservationInterpretationCodesEnum> getInterpretation() {  
		if (myInterpretation == null) {
			myInterpretation = new BoundCodeableConceptDt<ObservationInterpretationCodesEnum>(ObservationInterpretationCodesEnum.VALUESET_BINDER);
		}
		return myInterpretation;
	}

	/**
	 * Sets the value(s) for <b>interpretation</b> (High, low, normal, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public Observation setInterpretation(BoundCodeableConceptDt<ObservationInterpretationCodesEnum> theValue) {
		myInterpretation = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>interpretation</b> (High, low, normal, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public Observation setInterpretation(ObservationInterpretationCodesEnum theValue) {
		getInterpretation().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>comments</b> (Comments about result).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public StringDt getComments() {  
		if (myComments == null) {
			myComments = new StringDt();
		}
		return myComments;
	}

	/**
	 * Sets the value(s) for <b>comments</b> (Comments about result)
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public Observation setComments(StringDt theValue) {
		myComments = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>comments</b> (Comments about result)
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public Observation setComments( String theString) {
		myComments = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>applies[x]</b> (Physiologically Relevant time/time-period for observation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the \"physiologically relevant time\". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public IDatatype getApplies() {  
		return myApplies;
	}

	/**
	 * Sets the value(s) for <b>applies[x]</b> (Physiologically Relevant time/time-period for observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the \"physiologically relevant time\". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public Observation setApplies(IDatatype theValue) {
		myApplies = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>issued</b> (Date/Time this was made available).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getIssued() {  
		if (myIssued == null) {
			myIssued = new InstantDt();
		}
		return myIssued;
	}

	/**
	 * Sets the value(s) for <b>issued</b> (Date/Time this was made available)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Observation setIssued(InstantDt theValue) {
		myIssued = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>issued</b> (Date/Time this was made available)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Observation setIssuedWithMillisPrecision( Date theDate) {
		myIssued = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>issued</b> (Date/Time this was made available)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Observation setIssued( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myIssued = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (registered | preliminary | final | amended +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public BoundCodeDt<ObservationStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ObservationStatusEnum>(ObservationStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (registered | preliminary | final | amended +)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public Observation setStatus(BoundCodeDt<ObservationStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (registered | preliminary | final | amended +)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public Observation setStatus(ObservationStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reliability</b> (ok | ongoing | early | questionable | calibrating | error +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public BoundCodeDt<ObservationReliabilityEnum> getReliability() {  
		if (myReliability == null) {
			myReliability = new BoundCodeDt<ObservationReliabilityEnum>(ObservationReliabilityEnum.VALUESET_BINDER);
		}
		return myReliability;
	}

	/**
	 * Sets the value(s) for <b>reliability</b> (ok | ongoing | early | questionable | calibrating | error +)
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public Observation setReliability(BoundCodeDt<ObservationReliabilityEnum> theValue) {
		myReliability = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>reliability</b> (ok | ongoing | early | questionable | calibrating | error +)
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public Observation setReliability(ObservationReliabilityEnum theValue) {
		getReliability().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>bodySite</b> (Observed body part).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where on the subject's body the observation was made.
     * </p> 
	 */
	public CodeableConceptDt getBodySite() {  
		if (myBodySite == null) {
			myBodySite = new CodeableConceptDt();
		}
		return myBodySite;
	}

	/**
	 * Sets the value(s) for <b>bodySite</b> (Observed body part)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where on the subject's body the observation was made.
     * </p> 
	 */
	public Observation setBodySite(CodeableConceptDt theValue) {
		myBodySite = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>method</b> (How it was done).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the mechanism used to perform the observation
     * </p> 
	 */
	public CodeableConceptDt getMethod() {  
		if (myMethod == null) {
			myMethod = new CodeableConceptDt();
		}
		return myMethod;
	}

	/**
	 * Sets the value(s) for <b>method</b> (How it was done)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the mechanism used to perform the observation
     * </p> 
	 */
	public Observation setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>identifier</b> (Unique Id for this particular observation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Unique Id for this particular observation)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
     * </p> 
	 */
	public Observation setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Unique Id for this particular observation)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
     * </p> 
	 */
	public Observation setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Unique Id for this particular observation)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
     * </p> 
	 */
	public Observation setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Who and/or what this is about).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The thing the observation is being made about
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who and/or what this is about)
	 *
     * <p>
     * <b>Definition:</b>
     * The thing the observation is being made about
     * </p> 
	 */
	public Observation setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>specimen</b> (Specimen used for this observation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen that was used when this observation was made
     * </p> 
	 */
	public ResourceReferenceDt getSpecimen() {  
		if (mySpecimen == null) {
			mySpecimen = new ResourceReferenceDt();
		}
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for <b>specimen</b> (Specimen used for this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen that was used when this observation was made
     * </p> 
	 */
	public Observation setSpecimen(ResourceReferenceDt theValue) {
		mySpecimen = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>performer</b> (Who did the observation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as \"true\"
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getPerformer() {  
		if (myPerformer == null) {
			myPerformer = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myPerformer;
	}

	/**
	 * Sets the value(s) for <b>performer</b> (Who did the observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as \"true\"
     * </p> 
	 */
	public Observation setPerformer(java.util.List<ResourceReferenceDt> theValue) {
		myPerformer = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>performer</b> (Who did the observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as \"true\"
     * </p> 
	 */
	public ResourceReferenceDt addPerformer() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getPerformer().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>referenceRange</b> (Provides guide for interpretation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public java.util.List<ReferenceRange> getReferenceRange() {  
		if (myReferenceRange == null) {
			myReferenceRange = new java.util.ArrayList<ReferenceRange>();
		}
		return myReferenceRange;
	}

	/**
	 * Sets the value(s) for <b>referenceRange</b> (Provides guide for interpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public Observation setReferenceRange(java.util.List<ReferenceRange> theValue) {
		myReferenceRange = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>referenceRange</b> (Provides guide for interpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public ReferenceRange addReferenceRange() {
		ReferenceRange newType = new ReferenceRange();
		getReferenceRange().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>referenceRange</b> (Provides guide for interpretation),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public ReferenceRange getReferenceRangeFirstRep() {
		if (getReferenceRange().isEmpty()) {
			return addReferenceRange();
		}
		return getReferenceRange().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>related</b> (Observations related to this observation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public java.util.List<Related> getRelated() {  
		if (myRelated == null) {
			myRelated = new java.util.ArrayList<Related>();
		}
		return myRelated;
	}

	/**
	 * Sets the value(s) for <b>related</b> (Observations related to this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public Observation setRelated(java.util.List<Related> theValue) {
		myRelated = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>related</b> (Observations related to this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public Related addRelated() {
		Related newType = new Related();
		getRelated().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>related</b> (Observations related to this observation),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public Related getRelatedFirstRep() {
		if (getRelated().isEmpty()) {
			return addRelated();
		}
		return getRelated().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Observation.referenceRange</b> (Provides guide for interpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	@Block()	
	public static class ReferenceRange extends BaseElement implements IResourceBlock {
	
	@Child(name="low", type=QuantityDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Low Range, if relevant",
		formalDefinition="The value of the low bound of the reference range. If this is omitted, the low bound of the reference range is assumed to be meaningless. E.g. <2.3"
	)
	private QuantityDt myLow;
	
	@Child(name="high", type=QuantityDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="High Range, if relevant",
		formalDefinition="The value of the high bound of the reference range. If this is omitted, the high bound of the reference range is assumed to be meaningless. E.g. >5"
	)
	private QuantityDt myHigh;
	
	@Child(name="meaning", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Indicates the meaning/use of this range of this range",
		formalDefinition="Code for the meaning of the reference range"
	)
	private CodeableConceptDt myMeaning;
	
	@Child(name="age", type=RangeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Applicable age range, if relevant",
		formalDefinition="The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so"
	)
	private RangeDt myAge;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLow,  myHigh,  myMeaning,  myAge);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLow, myHigh, myMeaning, myAge);
	}

	/**
	 * Gets the value(s) for <b>low</b> (Low Range, if relevant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the low bound of the reference range. If this is omitted, the low bound of the reference range is assumed to be meaningless. E.g. <2.3
     * </p> 
	 */
	public QuantityDt getLow() {  
		if (myLow == null) {
			myLow = new QuantityDt();
		}
		return myLow;
	}

	/**
	 * Sets the value(s) for <b>low</b> (Low Range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the low bound of the reference range. If this is omitted, the low bound of the reference range is assumed to be meaningless. E.g. <2.3
     * </p> 
	 */
	public ReferenceRange setLow(QuantityDt theValue) {
		myLow = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>low</b> (Low Range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the low bound of the reference range. If this is omitted, the low bound of the reference range is assumed to be meaningless. E.g. <2.3
     * </p> 
	 */
	public ReferenceRange setLow( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myLow = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low Range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the low bound of the reference range. If this is omitted, the low bound of the reference range is assumed to be meaningless. E.g. <2.3
     * </p> 
	 */
	public ReferenceRange setLow( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myLow = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low Range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the low bound of the reference range. If this is omitted, the low bound of the reference range is assumed to be meaningless. E.g. <2.3
     * </p> 
	 */
	public ReferenceRange setLow( long theValue) {
		myLow = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low Range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the low bound of the reference range. If this is omitted, the low bound of the reference range is assumed to be meaningless. E.g. <2.3
     * </p> 
	 */
	public ReferenceRange setLow( double theValue) {
		myLow = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>high</b> (High Range, if relevant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the high bound of the reference range. If this is omitted, the high bound of the reference range is assumed to be meaningless. E.g. >5
     * </p> 
	 */
	public QuantityDt getHigh() {  
		if (myHigh == null) {
			myHigh = new QuantityDt();
		}
		return myHigh;
	}

	/**
	 * Sets the value(s) for <b>high</b> (High Range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the high bound of the reference range. If this is omitted, the high bound of the reference range is assumed to be meaningless. E.g. >5
     * </p> 
	 */
	public ReferenceRange setHigh(QuantityDt theValue) {
		myHigh = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>high</b> (High Range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the high bound of the reference range. If this is omitted, the high bound of the reference range is assumed to be meaningless. E.g. >5
     * </p> 
	 */
	public ReferenceRange setHigh( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myHigh = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High Range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the high bound of the reference range. If this is omitted, the high bound of the reference range is assumed to be meaningless. E.g. >5
     * </p> 
	 */
	public ReferenceRange setHigh( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myHigh = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High Range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the high bound of the reference range. If this is omitted, the high bound of the reference range is assumed to be meaningless. E.g. >5
     * </p> 
	 */
	public ReferenceRange setHigh( long theValue) {
		myHigh = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High Range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the high bound of the reference range. If this is omitted, the high bound of the reference range is assumed to be meaningless. E.g. >5
     * </p> 
	 */
	public ReferenceRange setHigh( double theValue) {
		myHigh = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>meaning</b> (Indicates the meaning/use of this range of this range).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code for the meaning of the reference range
     * </p> 
	 */
	public CodeableConceptDt getMeaning() {  
		if (myMeaning == null) {
			myMeaning = new CodeableConceptDt();
		}
		return myMeaning;
	}

	/**
	 * Sets the value(s) for <b>meaning</b> (Indicates the meaning/use of this range of this range)
	 *
     * <p>
     * <b>Definition:</b>
     * Code for the meaning of the reference range
     * </p> 
	 */
	public ReferenceRange setMeaning(CodeableConceptDt theValue) {
		myMeaning = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>age</b> (Applicable age range, if relevant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so
     * </p> 
	 */
	public RangeDt getAge() {  
		if (myAge == null) {
			myAge = new RangeDt();
		}
		return myAge;
	}

	/**
	 * Sets the value(s) for <b>age</b> (Applicable age range, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so
     * </p> 
	 */
	public ReferenceRange setAge(RangeDt theValue) {
		myAge = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>Observation.related</b> (Observations related to this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	@Block()	
	public static class Related extends BaseElement implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="has-component | has-member | derived-from | sequel-to | replaces | qualified-by | interfered-by",
		formalDefinition="A code specifying the kind of relationship that exists with the target observation"
	)
	private BoundCodeDt<ObservationRelationshipTypeEnum> myType;
	
	@Child(name="target", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Observation.class	})
	@Description(
		shortDefinition="Observation that is related to this one",
		formalDefinition="A reference to the observation that is related to this observation"
	)
	private ResourceReferenceDt myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myTarget);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myTarget);
	}

	/**
	 * Gets the value(s) for <b>type</b> (has-component | has-member | derived-from | sequel-to | replaces | qualified-by | interfered-by).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the kind of relationship that exists with the target observation
     * </p> 
	 */
	public BoundCodeDt<ObservationRelationshipTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<ObservationRelationshipTypeEnum>(ObservationRelationshipTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (has-component | has-member | derived-from | sequel-to | replaces | qualified-by | interfered-by)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the kind of relationship that exists with the target observation
     * </p> 
	 */
	public Related setType(BoundCodeDt<ObservationRelationshipTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (has-component | has-member | derived-from | sequel-to | replaces | qualified-by | interfered-by)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the kind of relationship that exists with the target observation
     * </p> 
	 */
	public Related setType(ObservationRelationshipTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>target</b> (Observation that is related to this one).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the observation that is related to this observation
     * </p> 
	 */
	public ResourceReferenceDt getTarget() {  
		if (myTarget == null) {
			myTarget = new ResourceReferenceDt();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (Observation that is related to this one)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the observation that is related to this observation
     * </p> 
	 */
	public Related setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}

  

	}




}
