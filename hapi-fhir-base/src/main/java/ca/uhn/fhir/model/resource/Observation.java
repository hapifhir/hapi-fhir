package ca.uhn.fhir.model.resource;

import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.ResourceChoiceElement;
import ca.uhn.fhir.model.api.ResourceElement;
import ca.uhn.fhir.model.datatype.AttachmentDt;
import ca.uhn.fhir.model.datatype.CodeableConceptDt;
import ca.uhn.fhir.model.datatype.DateTimeDt;
import ca.uhn.fhir.model.datatype.InstantDt;
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

@ResourceDefinition(name="Observation", identifierOrder=10)
public class Observation extends BaseResourceWithIdentifier {

	@ResourceElement(name="name", order=0, min=1, max=1)
	@CodeableConceptElement(type=ObservationCodesEnum.class)
	private CodeableConceptDt<ObservationCodesEnum> myName;
	
	@ResourceElement(name="value", order=1, min=0, max=1, choice=@ResourceChoiceElement(types= {
			QuantityDt.class,
			CodeableConceptDt.class,
			AttachmentDt.class,			
			RatioDt.class,
			PeriodDt.class,
			SampledDataDt.class,
			StringDt.class
	}))
	private IDatatype myValue;
	
	@ResourceElement(name="interpretation", order=2)
	@CodeableConceptElement(type=ObservationInterpretationEnum.class)
	private CodeableConceptDt<ObservationInterpretationEnum> myInterpretation;
	
	@ResourceElement(name="comments", order=3)
	private StringDt myComments;

	@ResourceElement(name="applies", order=4, choice=@ResourceChoiceElement(types={
			DateTimeDt.class,
			PeriodDt.class
	}))
	private IDatatype myApplies;
	
	@ResourceElement(name="issued", order=5)
	private InstantDt myIssued;
	
	@ResourceElement(name="status", order=6, min=1)
	@CodeableConceptElement(type=ObservationStatusEnum.class)
	private CodeableConceptDt<ObservationStatusEnum> myStatus;

	@ResourceElement(name="reliability", order=7, min=1)
	@CodeableConceptElement(type=ObservationStatusEnum.class)
	private CodeableConceptDt<ObservationStatusEnum> myReliability;

	@ResourceElement(name="bodySite", order=8)
	@CodeableConceptElement(type=BodySiteEnum.class)
	private CodeableConceptDt<BodySiteEnum> myBodySite;

	@ResourceElement(name="method", order=9)
	@CodeableConceptElement(type=ObservationMethodEnum.class)
	private CodeableConceptDt<ObservationMethodEnum> myMethod;
	
	
}
