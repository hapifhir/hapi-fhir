package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiBatchService;
import ca.uhn.fhir.empi.api.IEmpiQueueSubmitterSvc;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.entity.EmpiTargetType;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;

import javax.annotation.PostConstruct;
import java.util.List;

import static ca.uhn.fhir.empi.api.IEmpiSettings.EMPI_CHANNEL_NAME;

public class EmpiBatchSvcImpl implements IEmpiBatchService {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IChannelNamer myChannelNamer;

	private MessageChannel myEmpiChannelProducer;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private EmpiSearchParamSvc myEmpiSearchParamSvc;

	@Autowired
	private IEmpiQueueSubmitterSvc myEmpiQueueSubmitterSvc;

	@Autowired
	private IChannelFactory myChannelFactory;
	private static final int queueAddingPageSize = 100;


	@Override
	public void runEmpiOnAllTargets(StringType theCriteria) {
		runEmpiOnTargetType("Patient", theCriteria);
		runEmpiOnTargetType("Practitioner", theCriteria);
	}

	@Override
	public void runEmpiOnTargetType(String theTargetType, StringType theCriteria) {
		getTargetTypeOrThrowException(theTargetType);
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

	private EmpiTargetType getTargetTypeOrThrowException(String theResourceType) {
		if (theResourceType.equalsIgnoreCase("Patient")) {
			return EmpiTargetType.PATIENT;
		} else if(theResourceType.equalsIgnoreCase("Practitioner")) {
			return EmpiTargetType.PRACTITIONER;
		} else {
			throw new InvalidRequestException(ProviderConstants.EMPI_BATCH_RUN+ " does not support resource type: " + theResourceType);
		}
	}

	@PostConstruct
	private void init() {
		ChannelProducerSettings channelSettings = new ChannelProducerSettings();
		String channelName = myChannelNamer.getChannelName(EMPI_CHANNEL_NAME, channelSettings);
		myEmpiChannelProducer= myChannelFactory.getOrCreateProducer(channelName, ResourceModifiedJsonMessage.class, channelSettings);
	}
}
