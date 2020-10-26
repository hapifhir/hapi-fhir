package ca.uhn.fhir.jpa.interceptor.ex;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;

// This class is replicated in PartitionExamples.java -- Keep it up to date there too!!
@Interceptor
public class PartitionInterceptorReadAllPartitions {

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId readPartition() {
		return RequestPartitionId.allPartitions();
	}

}
