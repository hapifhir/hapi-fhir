package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.IEmpiBatchService;
import ca.uhn.fhir.empi.api.IEmpiQueueSubmitterSvc;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EmpiBatchSvcImpl implements IEmpiBatchService {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private EmpiSearchParamSvc myEmpiSearchParamSvc;

	@Autowired
	private IEmpiQueueSubmitterSvc myEmpiQueueSubmitterSvc;

	private static final int queueAddingPageSize = 100;

	@Override
	public void runEmpiOnAllTargets(StringType theCriteria) {
		runEmpiOnTargetType("Patient", theCriteria);
		runEmpiOnTargetType("Practitioner", theCriteria);
	}

	@Override
	public void runEmpiOnTargetType(String theTargetType, StringType theCriteria) {
		resolveTargetTypeOrThrowException(theTargetType);
		SearchParameterMap spMap = getSearchParameterMapFromCriteria(theTargetType, theCriteria);
		IFhirResourceDao patientDao = myDaoRegistry.getResourceDao(theTargetType);
		IBundleProvider search = patientDao.search(spMap);

		int lowIndex = 0;
		List<IBaseResource> resources = search.getResources(lowIndex, lowIndex + queueAddingPageSize);
		while(!resources.isEmpty()) {
			for (IBaseResource resource : resources) {
				myEmpiQueueSubmitterSvc.manuallySubmitResourceToEmpi(resource);
			}
			lowIndex += queueAddingPageSize;
			resources = search.getResources(lowIndex, lowIndex + queueAddingPageSize);
		}
	}

	private SearchParameterMap getSearchParameterMapFromCriteria(String theTargetType, StringType theCriteria) {
		SearchParameterMap spMap;
		if (theCriteria != null) {
			spMap = myEmpiSearchParamSvc.mapFromCriteria(theTargetType, theCriteria.getValueNotNull());
		} else {
			spMap = new SearchParameterMap();
		}
		return spMap;
	}

	private void resolveTargetTypeOrThrowException(String theResourceType) {
		if (!theResourceType.equalsIgnoreCase("Patient") && !theResourceType.equalsIgnoreCase("Practitioner")) {
			throw new InvalidRequestException(ProviderConstants.EMPI_BATCH_RUN+ " does not support resource type: " + theResourceType);
		}
	}
}
