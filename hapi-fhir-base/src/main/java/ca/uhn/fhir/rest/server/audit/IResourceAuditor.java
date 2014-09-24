package ca.uhn.fhir.rest.server.audit;

import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ObjectDetail;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;


public interface IResourceAuditor<T extends IResource> {
		
	public T getResource();
	public void setResource(T resource);
	
	public boolean isAuditable();
	
	public String getName();
	public IdentifierDt getIdentifier();
	public SecurityEventObjectTypeEnum getType();	
	public String getDescription();
	public List<ObjectDetail> getDetail();
	public SecurityEventObjectSensitivityEnum getSensitivity();

}
