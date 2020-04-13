package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.jpa.model.entity.PartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRequestPartitionHelperService {
	@Nullable
	PartitionId determineReadPartitionForRequest(@Nullable RequestDetails theRequest, String theResourceType);

	@Nullable
	PartitionId determineCreatePartitionForRequest(@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource);
}
