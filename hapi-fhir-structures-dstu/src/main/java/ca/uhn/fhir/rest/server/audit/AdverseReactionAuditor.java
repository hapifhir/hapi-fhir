package ca.uhn.fhir.rest.server.audit;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.dstu.resource.AdverseReaction;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class AdverseReactionAuditor implements IResourceAuditor<AdverseReaction> {
	
	private AdverseReaction myResource = null;

	@Override
	public AdverseReaction getResource() {		
		return myResource;
	}

	@Override
	public void setResource(AdverseReaction resource) {
		myResource = resource;
	}

	@Override
	public boolean isAuditable() {		
		return myResource != null;
	}

	@Override
	public String getName() {
		if(myResource == null) return null;
		return "AdverseReaction:" + myResource.getIdentifierFirstRep().getValue();
	}

	@Override
	public BaseIdentifierDt getIdentifier() {
		if(myResource == null) return null;
		return myResource.getIdentifierFirstRep();
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
		Map<String, String> details = new HashMap<String, String>();				
		details.put("subject", myResource.getSubject().getReference().getValue());
		details.put("version", myResource.getId().getVersionIdPart());
		return details;
	}

	@Override
	public SecurityEventObjectSensitivityEnum getSensitivity() {
		return null;
	}

}
