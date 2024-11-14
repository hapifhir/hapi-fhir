package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A hack to pre-resolve resource references within the bundle elements.
 * This should speed up bundle ingestion by avoiding the pre-mature hibernate flushing
 * caused by the resolution of resource references during resource indexing.
 */
public class FetchSuppressInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(FetchSuppressInterceptor.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	IIdHelperService<JpaPid> myIdHelperService;
	@Autowired
	HapiTransactionService myHapiTransactionService;

	/**
	 * Temporarily holds the ids to resolve.
	 * A bridge between STORAGE_TRANSACTION_PROCESSING which has access to RequestDetails,
	 * and STORAGE_TRANSACTION_WRITE_OPERATIONS_PRE which has access to TransactionDetails.
	 * */
	final ThreadLocal<List<IIdType>> myIdThreadLocalIdTunnel = new ThreadLocal<>();

	/**
	 * Extract the IIdType references from the bundle entries, and store in our thread-local.
	 */
	@Hook(Pointcut.STORAGE_TRANSACTION_PROCESSING)
	void extractResourceIdsToResolve(RequestDetails theRequestDetails, IBaseBundle theBundle) {
		FhirTerser fhirTerser = myFhirContext.newTerser();
		List<IIdType> ids =
			BundleUtil.getSearchBundleEntryParts(myFhirContext, theBundle).stream()
				.flatMap(nextEntry->fhirTerser.getAllResourceReferences(nextEntry.getResource()).stream())
				.map(info->info.getResourceReference().getReferenceElement()).filter(Objects::nonNull)
				.collect(Collectors.toList());

		ourLog.debug("Extracted resource ids from Bundle {}", ids);

		myIdThreadLocalIdTunnel.set(ids);
	}


	/**
	 * Take the IIdType references from our thread-local, resolve them,
	 * and store the resolutions in the TransactionDetails to avoid id resolution.
	 */
	@Hook(Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_PRE)
	void preResolveResourceIds(TransactionDetails theTransactionDetails) {
		var ids = myIdThreadLocalIdTunnel.get();
		myIdThreadLocalIdTunnel.remove();

		ourLog.debug("pre-resolve ids {}", ids);
		if (!ids.isEmpty()) {
			myHapiTransactionService.withSystemRequest().execute(() -> {
				List<JpaPid> resolved = myIdHelperService.resolveResourcePersistentIdsWithCache(RequestPartitionId.allPartitions(), ids);
				for (JpaPid nextPid : resolved) {
					theTransactionDetails.addResolvedResourceId(nextPid.getAssociatedResourceId(), nextPid);
				}
			});
		}
	}
}
