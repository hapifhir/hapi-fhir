package ca.uhn.fhir.model.base.resource;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.StringDt;

//@ResourceDef(name="Conformance")
public abstract class BaseConformance extends BaseResource implements IResource {

	public abstract StringDt getDescription();

	public abstract StringDt getPublisher();

}
