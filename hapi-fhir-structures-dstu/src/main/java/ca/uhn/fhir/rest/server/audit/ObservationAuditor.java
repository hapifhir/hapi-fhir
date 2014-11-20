package ca.uhn.fhir.rest.server.audit;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class ObservationAuditor implements IResourceAuditor<Observation> {
	
	private Observation myResource = null;

	@Override
	public Observation getResource() {
		return myResource;
	}

	@Override
	public void setResource(Observation resource) {
		myResource = resource;
	}

	@Override
	public boolean isAuditable() {
		return myResource != null;
	}

	@Override
	public String getName() {
		if(myResource == null) return null;
		return "Observation:" + myResource.getName().getCodingFirstRep().getCode().getValue();
	}

	@Override
	public BaseIdentifierDt getIdentifier() {
		return myResource.getIdentifier();
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
		details.put("dateIssued", myResource.getIssued().getValueAsString());
		details.put("version", myResource.getId().getVersionIdPart());
		details.put("subject", myResource.getSubject().getReference().getValue());
		return details;
	}

	@Override
	public SecurityEventObjectSensitivityEnum getSensitivity() {
		return null;
	}

}
