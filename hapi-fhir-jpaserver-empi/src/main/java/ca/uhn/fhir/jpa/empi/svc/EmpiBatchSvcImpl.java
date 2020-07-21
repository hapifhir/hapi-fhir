package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.IEmpiBatchService;
import ca.uhn.fhir.empi.api.IEmpiQueueSubmitterSvc;
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
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class EmpiBatchSvcImpl implements IEmpiBatchService {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private EmpiSearchParamSvc myEmpiSearchParamSvc;

	@Autowired
	private IEmpiQueueSubmitterSvc myEmpiQueueSubmitterSvc;

	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;

	private static final int QUEUE_ADDING_PAGE_SIZE = 100;

	@Override
	@Transactional
	public int runEmpiOnAllTargetTypes(String theCriteria) {
		int submittedCount = 0;
		submittedCount += runEmpiOnTargetType("Patient", theCriteria);
		submittedCount += runEmpiOnTargetType("Practitioner", theCriteria);
		return submittedCount;
	}

	@Override
	@Transactional
	public int runEmpiOnTargetType(String theTargetType, String theCriteria) {
		resolveTargetTypeOrThrowException(theTargetType);
		SearchParameterMap spMap = getSearchParameterMapFromCriteria(theTargetType, theCriteria);
		spMap.setLoadSynchronousUpTo(QUEUE_ADDING_PAGE_SIZE);
		int total = 0;
		ISearchBuilder mySearchBuilder = mySearchBuilderFactory.newSearchBuilder(myDaoRegistry.getResourceDao(theTargetType), theTargetType, Patient.class);
		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(null, UUID.randomUUID().toString());
		try (IResultIterator query = mySearchBuilder.createQuery(spMap, searchRuntimeDetails, null, RequestPartitionId.defaultPartition())) {

			Collection<ResourcePersistentId> pidsToSubmit = new ArrayList<>();
			List<IBaseResource> resourceToBeSubmitted = new ArrayList<>();

			while (query.hasNext()) {
				pidsToSubmit.add(query.next());
				if (pidsToSubmit.size() == QUEUE_ADDING_PAGE_SIZE || !query.hasNext()) {
					//TODO GGG ask ken how this works.
					total = loadResourcesAndSubmitToEmpi(total, mySearchBuilder, pidsToSubmit, resourceToBeSubmitted);
					resourceToBeSubmitted.clear();
				}
			}
		} catch (IOException theE) {
			throw new InternalErrorException("Failure while attempting to query resources for " + ProviderConstants.OPERATION_EMPI_BATCH_RUN, theE);
		}
		return total;
	}

	private int loadResourcesAndSubmitToEmpi(int theTotal, ISearchBuilder theMySearchBuilder, Collection<ResourcePersistentId> thePidsToSubmit, List<IBaseResource> theResourceToBeSubmitted) {
		theMySearchBuilder.loadResourcesByPid(thePidsToSubmit, thePidsToSubmit, theResourceToBeSubmitted, false, null);
		theResourceToBeSubmitted
			.forEach(resource -> myEmpiQueueSubmitterSvc.manuallySubmitResourceToEmpi(resource));
		theTotal += theResourceToBeSubmitted.size();
		return theTotal;
	}


	@Override
	@Transactional
	public int runEmpiOnPractitionerType(String theCriteria) {
		return runEmpiOnTargetType("Practitioner", theCriteria);
	}

	@Override
	@Transactional
	public int runEmpiOnPatientType(String theCriteria) {
		return runEmpiOnTargetType("Patient", theCriteria);
	}

	@Override
	@Transactional
	public int runEmpiOnTarget(IIdType theId, String theTargetType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theTargetType);
		IBaseResource read = resourceDao.read(theId);
		myEmpiQueueSubmitterSvc.manuallySubmitResourceToEmpi(read);
		return 1;
	}

	@Override
	@Transactional
	public int runEmpiOnTargetPractitioner(IIdType theId) {
		return runEmpiOnTarget(theId, "practitioner");
	}

	@Override
	@Transactional
	public int runEmpiOnTargetPatient(IIdType theId) {
		return runEmpiOnTarget(theId, "patient");
	}

	private SearchParameterMap getSearchParameterMapFromCriteria(String theTargetType, String theCriteria) {
		SearchParameterMap spMap;
		if (!StringUtils.isBlank(theCriteria)) {
			spMap = myEmpiSearchParamSvc.mapFromCriteria(theTargetType, theCriteria);
		} else {
			spMap = new SearchParameterMap();
		}
		return spMap;
	}

	private void resolveTargetTypeOrThrowException(String theResourceType) {
		if (!theResourceType.equalsIgnoreCase("Patient") && !theResourceType.equalsIgnoreCase("Practitioner")) {
			throw new InvalidRequestException(ProviderConstants.OPERATION_EMPI_BATCH_RUN + " does not support resource type: " + theResourceType);
		}
	}
}
