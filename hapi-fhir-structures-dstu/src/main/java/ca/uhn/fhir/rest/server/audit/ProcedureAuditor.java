package ca.uhn.fhir.rest.server.audit;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Procedure;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class ProcedureAuditor implements IResourceAuditor<Procedure> {
	
	private Procedure myResource = null;

	@Override
	public Procedure getResource() {
		return myResource;
	}

	@Override
	public void setResource(Procedure resource) {
		myResource = resource;
	}

	@Override
	public boolean isAuditable() {
		return myResource != null;
	}

	@Override
	public String getName() {
		if(myResource == null) return null;
		return "Procedure:" + myResource.getId().getIdPart();
	}

	@Override
	public BaseIdentifierDt getIdentifier() {
		if(myResource == null) return null;
		return new IdentifierDt(myResource.getId().getResourceType(), myResource.getId().getIdPart());
	}

	@Override
	public SecurityEventObjectTypeEnum getType() {
		return SecurityEventObjectTypeEnum.OTHER;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public Map<String, String> getDetail() {
		if(myResource == null) return null;
		Map<String, String> details = new HashMap<String, String>();			
		details.put("subject", myResource.getSubject().getReference().getValue());
		return details;
	}

	@Override
	public SecurityEventObjectSensitivityEnum getSensitivity() {
		return null;
	}

}
