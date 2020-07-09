package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiBatchService;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.entity.EmpiTargetType;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;

import javax.annotation.PostConstruct;
import java.util.List;

import static ca.uhn.fhir.empi.api.IEmpiSettings.EMPI_CHANNEL_NAME;

public class EmpiBatchSvcImpl implements IEmpiBatchService {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private EmpiMatchLinkSvc myEmpiMatchLinkSvc;

	@Autowired
	private IChannelNamer myChannelNamer;

	private MessageChannel myEmpiChannelProducer;

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IChannelFactory myChannelFactory;


	@Override
	public void runEmpiOnAllTargets() {
		runEmpiOnTargetType("Patient");
		runEmpiOnTargetType("Practitioner");
	}

	@Override
	public void runEmpiOnTargetType(String theTargetType) {
		getTargetTypeOrThrowException(theTargetType);
		IFhirResourceDao patientDao = myDaoRegistry.getResourceDao(theTargetType);
		IBundleProvider search = patientDao.search(new SearchParameterMap().setLoadSynchronous(true));
		List<IBaseResource> resources = search.getResources(0, search.size());

		for (IBaseResource resource : resources) {
			ResourceModifiedJsonMessage rmjm = new ResourceModifiedJsonMessage();
			ResourceModifiedMessage resourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, resource, ResourceModifiedMessage.OperationTypeEnum.MANUALLY_TRIGGERED);
			resourceModifiedMessage.setOperationType(ResourceModifiedMessage.OperationTypeEnum.MANUALLY_TRIGGERED);
			rmjm.setPayload(resourceModifiedMessage);
			myEmpiChannelProducer.send(rmjm);
		}
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
