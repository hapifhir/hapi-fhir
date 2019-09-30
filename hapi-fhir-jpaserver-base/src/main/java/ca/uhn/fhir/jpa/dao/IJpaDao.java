package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Date;

public interface IJpaDao<T extends IBaseResource> {
	@SuppressWarnings("unchecked")
	ResourceTable updateEntity(RequestDetails theRequest, IBaseResource theResource, ResourceTable
		theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
										boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry);

	ResourceTable updateInternal(RequestDetails theRequestDetails, T theResource, boolean thePerformIndexing, boolean theForceUpdateVersion,
										  ResourceTable theEntity, IIdType theResourceId, IBaseResource theOldResource);
}
