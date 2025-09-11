package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.HistoryBuilder;
import ca.uhn.fhir.jpa.dao.HistoryBuilderFactory;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.HistorySearchStyleEnum;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Bundle provider that can search and paginate history entries for specific resource IDs.
 */
public class PersistedJpaIdSearchBundleProvider implements IBundleProvider {

	private static final Logger ourLog = LoggerFactory.getLogger(PersistedJpaIdSearchBundleProvider.class);

	private final String myUuid;
	private final String myResourceType;
	private final JpaPid myResourceId;
	private final Date myLowerBound;
	private final Date myUpperBound;
	private final RequestPartitionId myPartitionId;
	private final HistorySearchStyleEnum myHistorySearchStyle;
	
	@Autowired
	private HistoryBuilderFactory myHistoryBuilderFactory;
	
	@Autowired
	private IJpaStorageResourceParser myJpaStorageResourceParser;
	
	@Autowired
	private IHapiTransactionService myTransactionService;

	public PersistedJpaIdSearchBundleProvider(
			String theResourceType,
			JpaPid theResourceId,
			Date theLowerBound,
			Date theUpperBound,
			RequestPartitionId thePartitionId,
			HistorySearchStyleEnum theHistorySearchStyle) {
		myUuid = UUID.randomUUID().toString();
		myResourceType = theResourceType;
		myResourceId = theResourceId;
		myLowerBound = theLowerBound;
		myUpperBound = theUpperBound;
		myPartitionId = thePartitionId;
		myHistorySearchStyle = theHistorySearchStyle;
	}

	@Override
	public IPrimitiveType<Date> getPublished() {
		return new InstantDt(new Date());
	}

	@Nonnull
	@Override
	public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
		return getResources(theFromIndex, theToIndex, new ResponsePage.ResponsePageBuilder());
	}

	@Override
	public List<IBaseResource> getResources(
			int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
		
		ourLog.debug("PersistedJpaIdSearchBundleProvider.getResources called with fromIndex={}, toIndex={}, resourceType={}, partitionId={}",
			theFromIndex, theToIndex, myResourceType, myPartitionId);
		
		return myTransactionService.withSystemRequestOnDefaultPartition().execute(() -> {
			HistoryBuilder historyBuilder = myHistoryBuilderFactory.newHistoryBuilder(
					myResourceType, myResourceId, myLowerBound, myUpperBound);

			// Handle null partition ID by using allPartitions() which works for bulk operations
			RequestPartitionId partitionId = myPartitionId;
			if (partitionId == null) {
				partitionId = RequestPartitionId.allPartitions();
			}
			
			// Use null offset and pass indexes directly to get proper pagination
			List<ResourceHistoryTable> results = historyBuilder.fetchEntities(
					partitionId, null, theFromIndex, theToIndex, myHistorySearchStyle);

			ourLog.debug("HistoryBuilder.fetchEntities returned {} results for range {}-{}",
				results.size(), theFromIndex, theToIndex);

			List<IBaseResource> retVal = new ArrayList<>();
			for (ResourceHistoryTable next : results) {
				retVal.add(myJpaStorageResourceParser.toResource(next, true));
			}

			ourLog.debug("PersistedJpaIdSearchBundleProvider returing {} resources", retVal.size());

			theResponsePageBuilder.setTotalRequestedResourcesFetched(results.size());
			return retVal;
		});
	}

	@Override
	public String getUuid() {
		return myUuid;
	}

	@Override
	public Integer preferredPageSize() {
		return null;
	}

	@Override
	public Integer size() {
		// Return null to allow unlimited pagination
		return null;
	}
}
