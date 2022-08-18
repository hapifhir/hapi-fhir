package ca.uhn.fhir.jpa.search.tasks;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.search.SearchStrategyFactory;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.function.Consumer;

public class SearchTaskParameters {
	// parameters
	public Search Search;
	public IDao CallingDao;
	public SearchParameterMap Params;
	public String ResourceType;
	public RequestDetails Request;
	public RequestPartitionId RequestPartitionId;
	public Consumer<String> OnRemove;
	public int SyncSize;

	private Integer myLoadingThrottleForUnitTests;

	public SearchTaskParameters(ca.uhn.fhir.jpa.entity.Search theSearch,
										 IDao theCallingDao,
										 SearchParameterMap theParams,
										 String theResourceType,
										 RequestDetails theRequest,
										 ca.uhn.fhir.interceptor.model.RequestPartitionId theRequestPartitionId,
										 Consumer<String> theOnRemove,
										 int theSyncSize
	) {
		Search = theSearch;
		CallingDao = theCallingDao;
		Params = theParams;
		ResourceType = theResourceType;
		Request = theRequest;
		RequestPartitionId = theRequestPartitionId;
		OnRemove = theOnRemove;
		SyncSize = theSyncSize;
	}

	public Integer getLoadingThrottleForUnitTests() {
		return myLoadingThrottleForUnitTests;
	}

	public void setLoadingThrottleForUnitTests(Integer theLoadingThrottleForUnitTests) {
		myLoadingThrottleForUnitTests = theLoadingThrottleForUnitTests;
	}
}
