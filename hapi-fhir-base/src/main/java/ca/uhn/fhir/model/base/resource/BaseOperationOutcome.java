package ca.uhn.fhir.model.base.resource;

import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.StringDt;

public abstract class BaseOperationOutcome extends BaseResource implements IResource {

	public abstract BaseIssue addIssue();

	public abstract List<? extends BaseIssue> getIssue();

	public abstract BaseIssue getIssueFirstRep();

	public static abstract class BaseIssue extends BaseIdentifiableElement implements IResourceBlock {
	
		public abstract BoundCodeDt<IssueSeverityEnum> getSeverity();
		
		public abstract StringDt getDetails();

		public abstract BaseCodingDt getType();

		public abstract BaseIssue addLocation( String theString);

		public abstract BaseIssue setSeverity(IssueSeverityEnum theSeverity);

		public abstract BaseIssue setDetails(String theString);
		
	}


	
}
