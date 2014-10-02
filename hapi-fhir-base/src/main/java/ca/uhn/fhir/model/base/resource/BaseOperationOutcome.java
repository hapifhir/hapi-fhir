package ca.uhn.fhir.model.base.resource;

import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.primitive.StringDt;

public abstract class BaseOperationOutcome extends BaseResource implements IResource {

	public abstract List<? extends BaseIssue> getIssue();

	public static abstract class BaseIssue extends BaseIdentifiableElement implements IResourceBlock {
	
		public abstract BaseCodingDt getType();

		public abstract StringDt getDetails();
		
	}

	public abstract BaseIssue getIssueFirstRep();
	
}
