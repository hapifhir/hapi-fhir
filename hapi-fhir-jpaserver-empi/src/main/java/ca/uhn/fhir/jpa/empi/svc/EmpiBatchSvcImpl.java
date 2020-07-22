package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.IEmpiBatchService;
import ca.uhn.fhir.empi.api.IEmpiChannelSubmitterSvc;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class EmpiBatchSvcImpl implements IEmpiBatchService {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private EmpiSearchParamSvc myEmpiSearchParamSvc;

	@Autowired
	private IEmpiChannelSubmitterSvc myEmpiQueueSubmitterSvc;

	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;

	private static final int BUFFER_SIZE = 100;

	@Override
	@Transactional
	public long runEmpiOnAllTargetTypes(String theCriteria) {
		long submittedCount = 0;
		submittedCount += runEmpiOnPatientType(theCriteria);
		submittedCount += runEmpiOnPractitionerType(theCriteria);
		return submittedCount;
	}

	@Override
	@Transactional
	public long runEmpiOnTargetType(String theTargetType, String theCriteria) {
		resolveTargetTypeOrThrowException(theTargetType);
		SearchParameterMap spMap = myEmpiSearchParamSvc.getSearchParameterMapFromCriteria(theTargetType, theCriteria);
		spMap.setLoadSynchronousUpTo(BUFFER_SIZE);
		ISearchBuilder searchBuilder = mySearchBuilderFactory.newSearchBuilder(myDaoRegistry.getResourceDao(theTargetType), theTargetType, Patient.class);
		return submitAllMatchingResourcesToEmpiChannel(spMap, searchBuilder);
	}

	private long submitAllMatchingResourcesToEmpiChannel(SearchParameterMap theSpMap, ISearchBuilder theSearchBuilder) {
		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(null, UUID.randomUUID().toString());
		long total = 0;
		try (IResultIterator query = theSearchBuilder.createQuery(theSpMap, searchRuntimeDetails, null, RequestPartitionId.defaultPartition())) {
			Collection<ResourcePersistentId> pidBatch;
			do {
				pidBatch = getPidBatch(query);
				total += loadPidsAndSubmitToEmpiChannel(theSearchBuilder, pidBatch);
			} while (query.hasNext());
		} catch (IOException theE) {
			throw new InternalErrorException("Failure while attempting to query resources for " + ProviderConstants.OPERATION_EMPI_BATCH_RUN, theE);
		}
		return total;
	}

	/**
	 * Given an iterator, get a batch of max size BUFFER_SIZE.
	 *
	 * @param theQuery the iterator.
	 *
	 * @return a collection of ResourcePersistentId entities.
	 */
	public Collection<ResourcePersistentId> getPidBatch(IResultIterator theQuery) {
		Collection<ResourcePersistentId> batch = new ArrayList<>();
		while (theQuery.hasNext() && batch.size() <= BUFFER_SIZE) {
			batch.add(theQuery.next());
		}
		return batch;
	}

	/**
	 * Given a collection of ResourcePersistentId objects, and a search builder, load the IBaseResources and submit them to
	 * the EMPI channel for processing.
	 *
	 * @param theSearchBuilder the related DAO search builder.
	 * @param thePidsToSubmit The collection of PIDs whos resources you want to submit for EMPI processing.
	 *
	 * @return The total count of submitted resources.
	 */
	private long loadPidsAndSubmitToEmpiChannel(ISearchBuilder theSearchBuilder, Collection<ResourcePersistentId> thePidsToSubmit) {
		List<IBaseResource> resourcesToSubmit = new ArrayList<>();
		theSearchBuilder.loadResourcesByPid(thePidsToSubmit, Collections.emptyList(), resourcesToSubmit, false, null);
		resourcesToSubmit
			.forEach(resource -> myEmpiQueueSubmitterSvc.submitResourceToEmpiChannel(resource));
		return resourcesToSubmit.size();
	}

	@Override
	@Transactional
	public long runEmpiOnPractitionerType(String theCriteria) {
		return runEmpiOnTargetType("Practitioner", theCriteria);
	}

	@Override
	@Transactional
	public long runEmpiOnPatientType(String theCriteria) {
		return runEmpiOnTargetType("Patient", theCriteria);
	}

	@Override
	@Transactional
	public long runEmpiOnTarget(IIdType theId) {
		resolveTargetTypeOrThrowException(theId.getResourceType());
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theId.getResourceType());
		IBaseResource read = resourceDao.read(theId);
		myEmpiQueueSubmitterSvc.submitResourceToEmpiChannel(read);
		return 1;
	}

	private void resolveTargetTypeOrThrowException(String theResourceType) {
		if (!EmpiUtil.supportedTargetType(theResourceType)) {
			throw new InvalidRequestException(ProviderConstants.OPERATION_EMPI_BATCH_RUN + " does not support resource type: " + theResourceType);
		}
	}
}
