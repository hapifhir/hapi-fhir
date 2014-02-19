package ca.uhn.fhir.model.resource;

import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildResource;
import ca.uhn.fhir.model.api.annotation.Choice;
import ca.uhn.fhir.model.api.annotation.Narrative;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.datatype.AttachmentDt;
import ca.uhn.fhir.model.datatype.CodeableConceptDt;
import ca.uhn.fhir.model.datatype.DateTimeDt;
import ca.uhn.fhir.model.datatype.InstantDt;
import ca.uhn.fhir.model.datatype.NarrativeDt;
import ca.uhn.fhir.model.datatype.PeriodDt;
import ca.uhn.fhir.model.datatype.QuantityDt;
import ca.uhn.fhir.model.datatype.RatioDt;
import ca.uhn.fhir.model.datatype.SampledDataDt;
import ca.uhn.fhir.model.datatype.StringDt;
import ca.uhn.fhir.model.enm.BodySiteEnum;
import ca.uhn.fhir.model.enm.ObservationCodesEnum;
import ca.uhn.fhir.model.enm.ObservationInterpretationEnum;
import ca.uhn.fhir.model.enm.ObservationMethodEnum;
import ca.uhn.fhir.model.enm.ObservationStatusEnum;

@ResourceDef(name="Observation", identifierOrder=10)
public class Observation extends BaseResourceWithIdentifier {

	@Child(name="name", order=0, min=1, max=1)
	@CodeableConceptElement(type=ObservationCodesEnum.class)
	private CodeableConceptDt<ObservationCodesEnum> myName;
	
	@Child(name="value", order=1, min=0, max=1, choice=@Choice(types= {
			QuantityDt.class,
			CodeableConceptDt.class,
			AttachmentDt.class,			
			RatioDt.class,
			PeriodDt.class,
			SampledDataDt.class,
			StringDt.class
	}))
	private IDatatype myValue;
	
	@Child(name="interpretation", order=2)
	@CodeableConceptElement(type=ObservationInterpretationEnum.class)
	private CodeableConceptDt<ObservationInterpretationEnum> myInterpretation;
	
	@Child(name="comments", order=3)
	private StringDt myComments;

	@Child(name="applies", order=4, choice=@Choice(types={
			DateTimeDt.class,
			PeriodDt.class
	}))
	private IDatatype myApplies;
	
	@Child(name="issued", order=5)
	private InstantDt myIssued;
	
	@Child(name="status", order=6, min=1)
	@CodeableConceptElement(type=ObservationStatusEnum.class)
	private CodeableConceptDt<ObservationStatusEnum> myStatus;

	@Child(name="reliability", order=7, min=1)
	@CodeableConceptElement(type=ObservationStatusEnum.class)
	private CodeableConceptDt<ObservationStatusEnum> myReliability;

	@Child(name="bodySite", order=8)
	@CodeableConceptElement(type=BodySiteEnum.class)
	private CodeableConceptDt<BodySiteEnum> myBodySite;

	@Child(name="method", order=9)
	@CodeableConceptElement(type=ObservationMethodEnum.class)
	private CodeableConceptDt<ObservationMethodEnum> myMethod;
	
	@Child(name="subject", order=11)
	@ChildResource(types= {
			Patient.class, Group.class // TODO: add device, location
	})
	private ResourceReference mySubject;

	public CodeableConceptDt<ObservationCodesEnum> getName() {
		return myName;
	}

	public void setName(CodeableConceptDt<ObservationCodesEnum> theName) {
		myName = theName;
	}

	public IDatatype getValue() {
		return myValue;
	}

	public void setValue(IDatatype theValue) {
		myValue = theValue;
	}

	public CodeableConceptDt<ObservationInterpretationEnum> getInterpretation() {
		return myInterpretation;
	}

	public void setInterpretation(CodeableConceptDt<ObservationInterpretationEnum> theInterpretation) {
		myInterpretation = theInterpretation;
	}

	public StringDt getComments() {
		return myComments;
	}

	public void setComments(StringDt theComments) {
		myComments = theComments;
	}

	public IDatatype getApplies() {
		return myApplies;
	}

	public void setApplies(IDatatype theApplies) {
		myApplies = theApplies;
	}

	public InstantDt getIssued() {
		return myIssued;
	}

	public void setIssued(InstantDt theIssued) {
		myIssued = theIssued;
	}

	public CodeableConceptDt<ObservationStatusEnum> getStatus() {
		return myStatus;
	}

	public void setStatus(CodeableConceptDt<ObservationStatusEnum> theStatus) {
		myStatus = theStatus;
	}

	public CodeableConceptDt<ObservationStatusEnum> getReliability() {
		return myReliability;
	}

	public void setReliability(CodeableConceptDt<ObservationStatusEnum> theReliability) {
		myReliability = theReliability;
	}

	public CodeableConceptDt<BodySiteEnum> getBodySite() {
		return myBodySite;
	}

	public void setBodySite(CodeableConceptDt<BodySiteEnum> theBodySite) {
		myBodySite = theBodySite;
	}

	public CodeableConceptDt<ObservationMethodEnum> getMethod() {
		return myMethod;
	}

	public void setMethod(CodeableConceptDt<ObservationMethodEnum> theMethod) {
		myMethod = theMethod;
	}

	public ResourceReference getSubject() {
		return mySubject;
	}

	public void setSubject(ResourceReference theSubject) {
		mySubject = theSubject;
	}
	
	
	
	
}
