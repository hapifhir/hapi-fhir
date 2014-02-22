











package ca.uhn.fhir.model.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.datatype.*;

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
 */
@ResourceDef(name="Observation")
public class Observation extends BaseResource {

	@Child(name="name", order=0, min=1, max=1)	
	private CodeableConceptDt myName;
	
	@Child(name="value", order=1, min=0, max=1, choice=@Choice(types= {
		QuantityDt.class,
		CodeableConceptDt.class,
		AttachmentDt.class,
		RatioDt.class,
		PeriodDt.class,
		SampledDataDt.class,
		StringDt.class,
	}))	
	private IDatatype myValue;
	
	@Child(name="interpretation", order=2, min=0, max=1)	
	private CodeableConceptDt myInterpretation;
	
	@Child(name="comments", order=3, min=0, max=1)	
	private StringDt myComments;
	
	@Child(name="applies", order=4, min=0, max=1, choice=@Choice(types= {
		DateTimeDt.class,
		PeriodDt.class,
	}))	
	private IDatatype myApplies;
	
	@Child(name="issued", order=5, min=0, max=1)	
	private InstantDt myIssued;
	
	@Child(name="status", order=6, min=1, max=1)	
	private CodeDt myStatus;
	
	@Child(name="reliability", order=7, min=1, max=1)	
	private CodeDt myReliability;
	
	@Child(name="bodySite", order=8, min=0, max=1)	
	private CodeableConceptDt myBodySite;
	
	@Child(name="method", order=9, min=0, max=1)	
	private CodeableConceptDt myMethod;
	
	@Child(name="identifier", order=10, min=0, max=1)	
	private IdentifierDt myIdentifier;
	
	@Child(name="subject", order=11, min=0, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
		Location.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="specimen", order=12, min=0, max=1)
	@ChildResource(types= {
		Specimen.class,
	})	
	private ResourceReference mySpecimen;
	
	@Child(name="performer", order=13, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Practitioner.class,
		Device.class,
		Organization.class,
	})	
	private List<ResourceReference> myPerformer;
	
	@Child(name="referenceRange", order=14, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myReferenceRange;
	
	@Child(name="related", order=15, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myRelated;
	
	/**
	 * Gets the value(s) for name (Type of observation (code / type))
	 *
     * <p>
     * <b>Definition:</b>
     * Describes what was observed. Sometimes this is called the observation "code"
     * </p> 
	 */
	public CodeableConceptDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Type of observation (code / type))
	 *
     * <p>
     * <b>Definition:</b>
     * Describes what was observed. Sometimes this is called the observation "code"
     * </p> 
	 */
	public void setName(CodeableConceptDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Gets the value(s) for value[x] (Actual result)
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
	 * Sets the value(s) for value[x] (Actual result)
	 *
     * <p>
     * <b>Definition:</b>
     * The information determined as a result of making the observation, if the information has a simple value
     * </p> 
	 */
	public void setValue(IDatatype theValue) {
		myValue = theValue;
	}
	
	/**
	 * Gets the value(s) for interpretation (High, low, normal, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public CodeableConceptDt getInterpretation() {
		return myInterpretation;
	}

	/**
	 * Sets the value(s) for interpretation (High, low, normal, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public void setInterpretation(CodeableConceptDt theValue) {
		myInterpretation = theValue;
	}
	
	/**
	 * Gets the value(s) for comments (Comments about result)
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public StringDt getComments() {
		return myComments;
	}

	/**
	 * Sets the value(s) for comments (Comments about result)
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public void setComments(StringDt theValue) {
		myComments = theValue;
	}
	
	/**
	 * Gets the value(s) for applies[x] (Physiologically Relevant time/time-period for observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public IDatatype getApplies() {
		return myApplies;
	}

	/**
	 * Sets the value(s) for applies[x] (Physiologically Relevant time/time-period for observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public void setApplies(IDatatype theValue) {
		myApplies = theValue;
	}
	
	/**
	 * Gets the value(s) for issued (Date/Time this was made available)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public InstantDt getIssued() {
		return myIssued;
	}

	/**
	 * Sets the value(s) for issued (Date/Time this was made available)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setIssued(InstantDt theValue) {
		myIssued = theValue;
	}
	
	/**
	 * Gets the value(s) for status (registered | preliminary | final | amended +)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (registered | preliminary | final | amended +)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for reliability (ok | ongoing | early | questionable | calibrating | error + )
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public CodeDt getReliability() {
		return myReliability;
	}

	/**
	 * Sets the value(s) for reliability (ok | ongoing | early | questionable | calibrating | error + )
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public void setReliability(CodeDt theValue) {
		myReliability = theValue;
	}
	
	/**
	 * Gets the value(s) for bodySite (Observed body part)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where on the subject's body the observation was made.
     * </p> 
	 */
	public CodeableConceptDt getBodySite() {
		return myBodySite;
	}

	/**
	 * Sets the value(s) for bodySite (Observed body part)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where on the subject's body the observation was made.
     * </p> 
	 */
	public void setBodySite(CodeableConceptDt theValue) {
		myBodySite = theValue;
	}
	
	/**
	 * Gets the value(s) for method (How it was done)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the mechanism used to perform the observation
     * </p> 
	 */
	public CodeableConceptDt getMethod() {
		return myMethod;
	}

	/**
	 * Sets the value(s) for method (How it was done)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the mechanism used to perform the observation
     * </p> 
	 */
	public void setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
	}
	
	/**
	 * Gets the value(s) for identifier (Unique Id for this particular observation)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
     * </p> 
	 */
	public IdentifierDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Unique Id for this particular observation)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
     * </p> 
	 */
	public void setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for subject (Who and/or what this is about)
	 *
     * <p>
     * <b>Definition:</b>
     * The thing the observation is being made about
     * </p> 
	 */
	public ResourceReference getSubject() {
		return mySubject;
	}

	/**
	 * Sets the value(s) for subject (Who and/or what this is about)
	 *
     * <p>
     * <b>Definition:</b>
     * The thing the observation is being made about
     * </p> 
	 */
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
	}
	
	/**
	 * Gets the value(s) for specimen (Specimen used for this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen that was used when this observation was made 
     * </p> 
	 */
	public ResourceReference getSpecimen() {
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for specimen (Specimen used for this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen that was used when this observation was made 
     * </p> 
	 */
	public void setSpecimen(ResourceReference theValue) {
		mySpecimen = theValue;
	}
	
	/**
	 * Gets the value(s) for performer (Who did the observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as "true"
     * </p> 
	 */
	public List<ResourceReference> getPerformer() {
		return myPerformer;
	}

	/**
	 * Sets the value(s) for performer (Who did the observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as "true"
     * </p> 
	 */
	public void setPerformer(List<ResourceReference> theValue) {
		myPerformer = theValue;
	}
	
	/**
	 * Gets the value(s) for referenceRange (Provides guide for interpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public List<IDatatype> getReferenceRange() {
		return myReferenceRange;
	}

	/**
	 * Sets the value(s) for referenceRange (Provides guide for interpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public void setReferenceRange(List<IDatatype> theValue) {
		myReferenceRange = theValue;
	}
	
	/**
	 * Gets the value(s) for related (Observations related to this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public List<IDatatype> getRelated() {
		return myRelated;
	}

	/**
	 * Sets the value(s) for related (Observations related to this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public void setRelated(List<IDatatype> theValue) {
		myRelated = theValue;
	}
	

	/**
	 * Block class for child element: <b>Observation.referenceRange</b> (Provides guide for interpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	@Block(name="Observation.referenceRange")	
	public static class ReferenceRange implements IResourceBlock {
	@Child(name="name", order=0, min=1, max=1)	
	private CodeableConceptDt myName;
	
	@Child(name="value", order=1, min=0, max=1, choice=@Choice(types= {
		QuantityDt.class,
		CodeableConceptDt.class,
		AttachmentDt.class,
		RatioDt.class,
		PeriodDt.class,
		SampledDataDt.class,
		StringDt.class,
	}))	
	private IDatatype myValue;
	
	@Child(name="interpretation", order=2, min=0, max=1)	
	private CodeableConceptDt myInterpretation;
	
	@Child(name="comments", order=3, min=0, max=1)	
	private StringDt myComments;
	
	@Child(name="applies", order=4, min=0, max=1, choice=@Choice(types= {
		DateTimeDt.class,
		PeriodDt.class,
	}))	
	private IDatatype myApplies;
	
	@Child(name="issued", order=5, min=0, max=1)	
	private InstantDt myIssued;
	
	@Child(name="status", order=6, min=1, max=1)	
	private CodeDt myStatus;
	
	@Child(name="reliability", order=7, min=1, max=1)	
	private CodeDt myReliability;
	
	@Child(name="bodySite", order=8, min=0, max=1)	
	private CodeableConceptDt myBodySite;
	
	@Child(name="method", order=9, min=0, max=1)	
	private CodeableConceptDt myMethod;
	
	@Child(name="identifier", order=10, min=0, max=1)	
	private IdentifierDt myIdentifier;
	
	@Child(name="subject", order=11, min=0, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
		Location.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="specimen", order=12, min=0, max=1)
	@ChildResource(types= {
		Specimen.class,
	})	
	private ResourceReference mySpecimen;
	
	@Child(name="performer", order=13, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Practitioner.class,
		Device.class,
		Organization.class,
	})	
	private List<ResourceReference> myPerformer;
	
	@Child(name="referenceRange", order=14, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myReferenceRange;
	
	@Child(name="related", order=15, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myRelated;
	
	/**
	 * Gets the value(s) for name (Type of observation (code / type))
	 *
     * <p>
     * <b>Definition:</b>
     * Describes what was observed. Sometimes this is called the observation "code"
     * </p> 
	 */
	public CodeableConceptDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Type of observation (code / type))
	 *
     * <p>
     * <b>Definition:</b>
     * Describes what was observed. Sometimes this is called the observation "code"
     * </p> 
	 */
	public void setName(CodeableConceptDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Gets the value(s) for value[x] (Actual result)
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
	 * Sets the value(s) for value[x] (Actual result)
	 *
     * <p>
     * <b>Definition:</b>
     * The information determined as a result of making the observation, if the information has a simple value
     * </p> 
	 */
	public void setValue(IDatatype theValue) {
		myValue = theValue;
	}
	
	/**
	 * Gets the value(s) for interpretation (High, low, normal, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public CodeableConceptDt getInterpretation() {
		return myInterpretation;
	}

	/**
	 * Sets the value(s) for interpretation (High, low, normal, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public void setInterpretation(CodeableConceptDt theValue) {
		myInterpretation = theValue;
	}
	
	/**
	 * Gets the value(s) for comments (Comments about result)
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public StringDt getComments() {
		return myComments;
	}

	/**
	 * Sets the value(s) for comments (Comments about result)
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public void setComments(StringDt theValue) {
		myComments = theValue;
	}
	
	/**
	 * Gets the value(s) for applies[x] (Physiologically Relevant time/time-period for observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public IDatatype getApplies() {
		return myApplies;
	}

	/**
	 * Sets the value(s) for applies[x] (Physiologically Relevant time/time-period for observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public void setApplies(IDatatype theValue) {
		myApplies = theValue;
	}
	
	/**
	 * Gets the value(s) for issued (Date/Time this was made available)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public InstantDt getIssued() {
		return myIssued;
	}

	/**
	 * Sets the value(s) for issued (Date/Time this was made available)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setIssued(InstantDt theValue) {
		myIssued = theValue;
	}
	
	/**
	 * Gets the value(s) for status (registered | preliminary | final | amended +)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (registered | preliminary | final | amended +)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for reliability (ok | ongoing | early | questionable | calibrating | error + )
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public CodeDt getReliability() {
		return myReliability;
	}

	/**
	 * Sets the value(s) for reliability (ok | ongoing | early | questionable | calibrating | error + )
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public void setReliability(CodeDt theValue) {
		myReliability = theValue;
	}
	
	/**
	 * Gets the value(s) for bodySite (Observed body part)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where on the subject's body the observation was made.
     * </p> 
	 */
	public CodeableConceptDt getBodySite() {
		return myBodySite;
	}

	/**
	 * Sets the value(s) for bodySite (Observed body part)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where on the subject's body the observation was made.
     * </p> 
	 */
	public void setBodySite(CodeableConceptDt theValue) {
		myBodySite = theValue;
	}
	
	/**
	 * Gets the value(s) for method (How it was done)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the mechanism used to perform the observation
     * </p> 
	 */
	public CodeableConceptDt getMethod() {
		return myMethod;
	}

	/**
	 * Sets the value(s) for method (How it was done)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the mechanism used to perform the observation
     * </p> 
	 */
	public void setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
	}
	
	/**
	 * Gets the value(s) for identifier (Unique Id for this particular observation)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
     * </p> 
	 */
	public IdentifierDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Unique Id for this particular observation)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
     * </p> 
	 */
	public void setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for subject (Who and/or what this is about)
	 *
     * <p>
     * <b>Definition:</b>
     * The thing the observation is being made about
     * </p> 
	 */
	public ResourceReference getSubject() {
		return mySubject;
	}

	/**
	 * Sets the value(s) for subject (Who and/or what this is about)
	 *
     * <p>
     * <b>Definition:</b>
     * The thing the observation is being made about
     * </p> 
	 */
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
	}
	
	/**
	 * Gets the value(s) for specimen (Specimen used for this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen that was used when this observation was made 
     * </p> 
	 */
	public ResourceReference getSpecimen() {
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for specimen (Specimen used for this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen that was used when this observation was made 
     * </p> 
	 */
	public void setSpecimen(ResourceReference theValue) {
		mySpecimen = theValue;
	}
	
	/**
	 * Gets the value(s) for performer (Who did the observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as "true"
     * </p> 
	 */
	public List<ResourceReference> getPerformer() {
		return myPerformer;
	}

	/**
	 * Sets the value(s) for performer (Who did the observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as "true"
     * </p> 
	 */
	public void setPerformer(List<ResourceReference> theValue) {
		myPerformer = theValue;
	}
	
	/**
	 * Gets the value(s) for referenceRange (Provides guide for interpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public List<IDatatype> getReferenceRange() {
		return myReferenceRange;
	}

	/**
	 * Sets the value(s) for referenceRange (Provides guide for interpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public void setReferenceRange(List<IDatatype> theValue) {
		myReferenceRange = theValue;
	}
	
	/**
	 * Gets the value(s) for related (Observations related to this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public List<IDatatype> getRelated() {
		return myRelated;
	}

	/**
	 * Sets the value(s) for related (Observations related to this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public void setRelated(List<IDatatype> theValue) {
		myRelated = theValue;
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
	@Block(name="Observation.related")	
	public static class Related implements IResourceBlock {
	@Child(name="name", order=0, min=1, max=1)	
	private CodeableConceptDt myName;
	
	@Child(name="value", order=1, min=0, max=1, choice=@Choice(types= {
		QuantityDt.class,
		CodeableConceptDt.class,
		AttachmentDt.class,
		RatioDt.class,
		PeriodDt.class,
		SampledDataDt.class,
		StringDt.class,
	}))	
	private IDatatype myValue;
	
	@Child(name="interpretation", order=2, min=0, max=1)	
	private CodeableConceptDt myInterpretation;
	
	@Child(name="comments", order=3, min=0, max=1)	
	private StringDt myComments;
	
	@Child(name="applies", order=4, min=0, max=1, choice=@Choice(types= {
		DateTimeDt.class,
		PeriodDt.class,
	}))	
	private IDatatype myApplies;
	
	@Child(name="issued", order=5, min=0, max=1)	
	private InstantDt myIssued;
	
	@Child(name="status", order=6, min=1, max=1)	
	private CodeDt myStatus;
	
	@Child(name="reliability", order=7, min=1, max=1)	
	private CodeDt myReliability;
	
	@Child(name="bodySite", order=8, min=0, max=1)	
	private CodeableConceptDt myBodySite;
	
	@Child(name="method", order=9, min=0, max=1)	
	private CodeableConceptDt myMethod;
	
	@Child(name="identifier", order=10, min=0, max=1)	
	private IdentifierDt myIdentifier;
	
	@Child(name="subject", order=11, min=0, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
		Location.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="specimen", order=12, min=0, max=1)
	@ChildResource(types= {
		Specimen.class,
	})	
	private ResourceReference mySpecimen;
	
	@Child(name="performer", order=13, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Practitioner.class,
		Device.class,
		Organization.class,
	})	
	private List<ResourceReference> myPerformer;
	
	@Child(name="referenceRange", order=14, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myReferenceRange;
	
	@Child(name="related", order=15, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myRelated;
	
	/**
	 * Gets the value(s) for name (Type of observation (code / type))
	 *
     * <p>
     * <b>Definition:</b>
     * Describes what was observed. Sometimes this is called the observation "code"
     * </p> 
	 */
	public CodeableConceptDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Type of observation (code / type))
	 *
     * <p>
     * <b>Definition:</b>
     * Describes what was observed. Sometimes this is called the observation "code"
     * </p> 
	 */
	public void setName(CodeableConceptDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Gets the value(s) for value[x] (Actual result)
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
	 * Sets the value(s) for value[x] (Actual result)
	 *
     * <p>
     * <b>Definition:</b>
     * The information determined as a result of making the observation, if the information has a simple value
     * </p> 
	 */
	public void setValue(IDatatype theValue) {
		myValue = theValue;
	}
	
	/**
	 * Gets the value(s) for interpretation (High, low, normal, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public CodeableConceptDt getInterpretation() {
		return myInterpretation;
	}

	/**
	 * Sets the value(s) for interpretation (High, low, normal, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public void setInterpretation(CodeableConceptDt theValue) {
		myInterpretation = theValue;
	}
	
	/**
	 * Gets the value(s) for comments (Comments about result)
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public StringDt getComments() {
		return myComments;
	}

	/**
	 * Sets the value(s) for comments (Comments about result)
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public void setComments(StringDt theValue) {
		myComments = theValue;
	}
	
	/**
	 * Gets the value(s) for applies[x] (Physiologically Relevant time/time-period for observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public IDatatype getApplies() {
		return myApplies;
	}

	/**
	 * Sets the value(s) for applies[x] (Physiologically Relevant time/time-period for observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public void setApplies(IDatatype theValue) {
		myApplies = theValue;
	}
	
	/**
	 * Gets the value(s) for issued (Date/Time this was made available)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public InstantDt getIssued() {
		return myIssued;
	}

	/**
	 * Sets the value(s) for issued (Date/Time this was made available)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setIssued(InstantDt theValue) {
		myIssued = theValue;
	}
	
	/**
	 * Gets the value(s) for status (registered | preliminary | final | amended +)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (registered | preliminary | final | amended +)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for reliability (ok | ongoing | early | questionable | calibrating | error + )
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public CodeDt getReliability() {
		return myReliability;
	}

	/**
	 * Sets the value(s) for reliability (ok | ongoing | early | questionable | calibrating | error + )
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public void setReliability(CodeDt theValue) {
		myReliability = theValue;
	}
	
	/**
	 * Gets the value(s) for bodySite (Observed body part)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where on the subject's body the observation was made.
     * </p> 
	 */
	public CodeableConceptDt getBodySite() {
		return myBodySite;
	}

	/**
	 * Sets the value(s) for bodySite (Observed body part)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where on the subject's body the observation was made.
     * </p> 
	 */
	public void setBodySite(CodeableConceptDt theValue) {
		myBodySite = theValue;
	}
	
	/**
	 * Gets the value(s) for method (How it was done)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the mechanism used to perform the observation
     * </p> 
	 */
	public CodeableConceptDt getMethod() {
		return myMethod;
	}

	/**
	 * Sets the value(s) for method (How it was done)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the mechanism used to perform the observation
     * </p> 
	 */
	public void setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
	}
	
	/**
	 * Gets the value(s) for identifier (Unique Id for this particular observation)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
     * </p> 
	 */
	public IdentifierDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Unique Id for this particular observation)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
     * </p> 
	 */
	public void setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for subject (Who and/or what this is about)
	 *
     * <p>
     * <b>Definition:</b>
     * The thing the observation is being made about
     * </p> 
	 */
	public ResourceReference getSubject() {
		return mySubject;
	}

	/**
	 * Sets the value(s) for subject (Who and/or what this is about)
	 *
     * <p>
     * <b>Definition:</b>
     * The thing the observation is being made about
     * </p> 
	 */
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
	}
	
	/**
	 * Gets the value(s) for specimen (Specimen used for this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen that was used when this observation was made 
     * </p> 
	 */
	public ResourceReference getSpecimen() {
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for specimen (Specimen used for this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen that was used when this observation was made 
     * </p> 
	 */
	public void setSpecimen(ResourceReference theValue) {
		mySpecimen = theValue;
	}
	
	/**
	 * Gets the value(s) for performer (Who did the observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as "true"
     * </p> 
	 */
	public List<ResourceReference> getPerformer() {
		return myPerformer;
	}

	/**
	 * Sets the value(s) for performer (Who did the observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as "true"
     * </p> 
	 */
	public void setPerformer(List<ResourceReference> theValue) {
		myPerformer = theValue;
	}
	
	/**
	 * Gets the value(s) for referenceRange (Provides guide for interpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public List<IDatatype> getReferenceRange() {
		return myReferenceRange;
	}

	/**
	 * Sets the value(s) for referenceRange (Provides guide for interpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public void setReferenceRange(List<IDatatype> theValue) {
		myReferenceRange = theValue;
	}
	
	/**
	 * Gets the value(s) for related (Observations related to this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public List<IDatatype> getRelated() {
		return myRelated;
	}

	/**
	 * Sets the value(s) for related (Observations related to this observation)
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public void setRelated(List<IDatatype> theValue) {
		myRelated = theValue;
	}
	
	}



}