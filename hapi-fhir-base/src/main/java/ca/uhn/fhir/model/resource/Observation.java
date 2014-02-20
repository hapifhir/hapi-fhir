package ca.uhn.fhir.model.resource;

import java.util.List;

import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildResource;
import ca.uhn.fhir.model.api.annotation.Choice;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.datatype.AttachmentDt;
import ca.uhn.fhir.model.datatype.CodeDt;
import ca.uhn.fhir.model.datatype.CodeableConceptDt;
import ca.uhn.fhir.model.datatype.DateTimeDt;
import ca.uhn.fhir.model.datatype.InstantDt;
import ca.uhn.fhir.model.datatype.PeriodDt;
import ca.uhn.fhir.model.datatype.QuantityDt;
import ca.uhn.fhir.model.datatype.RangeDt;
import ca.uhn.fhir.model.datatype.RatioDt;
import ca.uhn.fhir.model.datatype.SampledDataDt;
import ca.uhn.fhir.model.datatype.StringDt;
import ca.uhn.fhir.model.enm.BodySiteEnum;
import ca.uhn.fhir.model.enm.ObservationCodesEnum;
import ca.uhn.fhir.model.enm.ObservationInterpretationEnum;
import ca.uhn.fhir.model.enm.ObservationMethodEnum;
import ca.uhn.fhir.model.enm.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.enm.ObservationStatusEnum;
import ca.uhn.fhir.model.enm.ReferenceRangeMeaningEnum;

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
			Patient.class, Group.class, Device.class, Location.class
	})
	private ResourceReference mySubject;

	@Child(name="specimen", order=12)
	@ChildResource(types= {
			Specimen.class
	})
	private ResourceReference mySpecimen;

	@Child(name="performer", order=13, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
			Practitioner.class, Device.class, Organization.class
	})
	private List<ResourceReference> myPerformer;

	@Child(name="referenceRange", order=14, max=Child.MAX_UNLIMITED)
	private List<ReferenceRange> myReferenceRange;
	
	@Child(name="related", order=15, max=Child.MAX_UNLIMITED)
	private List<Related> myRelated;

	@Block(name="Observation.related")
	public static class Related implements IResourceBlock
	{
		@Child(name="type", order = 0)
		@CodeableConceptElement(type=ObservationRelationshipTypeEnum.class)
		private CodeDt<ObservationRelationshipTypeEnum> myType;
		
		@Child(name="target", order = 1)
		@ChildResource(types= {
				Observation.class
		})
		private ResourceReference myTarget;

		public CodeDt<ObservationRelationshipTypeEnum> getType() {
			return myType;
		}

		public void setType(CodeDt<ObservationRelationshipTypeEnum> theType) {
			myType = theType;
		}

		public ResourceReference getTarget() {
			return myTarget;
		}

		public void setTarget(ResourceReference theTarget) {
			myTarget = theTarget;
		}

	}
	
	@Block(name="Observation.referenceRange")
	public static class ReferenceRange implements IResourceBlock
	{
		
		@Child(name="low", order=0)
		private QuantityDt myLow;

		@Child(name="high", order=1)
		private QuantityDt myHigh;
		
		@Child(name="meaning", order=2)
		@CodeableConceptElement(type=ReferenceRangeMeaningEnum.class)
		private CodeableConceptDt<ReferenceRangeMeaningEnum> myMeaning;

		@Child(name="age", order=3)
		private RangeDt myAge;

		public QuantityDt getLow() {
			return myLow;
		}

		public void setLow(QuantityDt theLow) {
			myLow = theLow;
		}

		public QuantityDt getHigh() {
			return myHigh;
		}

		public void setHigh(QuantityDt theHigh) {
			myHigh = theHigh;
		}

		public CodeableConceptDt<ReferenceRangeMeaningEnum> getMeaning() {
			return myMeaning;
		}

		public void setMeaning(CodeableConceptDt<ReferenceRangeMeaningEnum> theMeaning) {
			myMeaning = theMeaning;
		}

		public RangeDt getAge() {
			return myAge;
		}

		public void setAge(RangeDt theAge) {
			myAge = theAge;
		}

	}
	
	
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

	public ResourceReference getSpecimen() {
		return mySpecimen;
	}

	public void setSpecimen(ResourceReference theSpecimen) {
		mySpecimen = theSpecimen;
	}

	public List<ResourceReference> getPerformer() {
		return myPerformer;
	}

	public void setPerformer(List<ResourceReference> thePerformer) {
		myPerformer = thePerformer;
	}

	public List<ReferenceRange> getReferenceRange() {
		return myReferenceRange;
	}

	public void setReferenceRange(List<ReferenceRange> theReferenceRange) {
		myReferenceRange = theReferenceRange;
	}

	public List<Related> getRelated() {
		return myRelated;
	}

	public void setRelated(List<Related> theRelated) {
		myRelated = theRelated;
	}
	
	
	
	
}
