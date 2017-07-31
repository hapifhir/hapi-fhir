package ca.uhn.fhir.jpa.dao;

import org.hl7.fhir.instance.model.api.IAnyResource;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum.ResourceMetadataKeySupportingAnyResource;

final class MetadataKeyResourcePid extends ResourceMetadataKeySupportingAnyResource<Long, Long> {
	private static final long serialVersionUID = 1L;

	MetadataKeyResourcePid(String theValue) {
		super(theValue);
	}

	@Override
	public Long get(IAnyResource theResource) {
		return (Long) theResource.getUserData(IDao.RESOURCE_PID.name());
	}

	@Override
	public Long get(IResource theResource) {
		return (Long) theResource.getResourceMetadata().get(IDao.RESOURCE_PID);
	}

	@Override
	public void put(IAnyResource theResource, Long theObject) {
		theResource.setUserData(IDao.RESOURCE_PID.name(), theObject);
	}

	@Override
	public void put(IResource theResource, Long theObject) {
		theResource.getResourceMetadata().put(IDao.RESOURCE_PID, theObject);
	}
}