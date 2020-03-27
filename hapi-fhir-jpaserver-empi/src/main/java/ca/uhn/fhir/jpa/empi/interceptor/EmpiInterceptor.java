package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.IEmpiInterceptor;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.interceptor.BaseResourceModifiedInterceptor;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.channel.ISubscribableChannelFactory;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.atomic.AtomicInteger;

@Interceptor
@Service
public class EmpiInterceptor extends BaseResourceModifiedInterceptor implements IEmpiInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiInterceptor.class);

	private static final String EMPI_MATCHING_CHANNEL_NAME = "empi-matching";
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private ISubscribableChannelFactory mySubscribableChannelFactory;
	@Autowired
	private EmpiMatchingSubscriber myEmpiMatchingSubscriber;

	@Override
	protected String getMatchingChannelName() {
		return EMPI_MATCHING_CHANNEL_NAME;
	}

	@Override
	protected MessageHandler getSubscriber() {
		return myEmpiMatchingSubscriber;
	}

	@Override
	protected SubscribableChannel createMatchingChannel() {
		return mySubscribableChannelFactory.createSubscribableChannel(EMPI_MATCHING_CHANNEL_NAME, ResourceModifiedMessage.class, 1);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING)
	public void expungeAllEmpiLinks(AtomicInteger theCounter) {
		ourLog.debug("Expunging all EmpiLink records");
		theCounter.addAndGet(myExpungeEverythingService.expungeEverythingByType(EmpiLink.class));
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeAllMatchedEmpiLinks(AtomicInteger theCounter, IBaseResource theResource) {
		// FIXME EMPI
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void blockManualPersonManipulationOnCreate(IBaseResource theBaseResource, RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
		forbidModificationOnPerson(theBaseResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void blockManualPersonManipulationOnUpdate(IBaseResource theBaseResource, RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
		forbidModificationOnPerson(theBaseResource);
	}

	private void forbidModificationOnPerson(IBaseResource theBaseResource) {
		if (extractResourceType(theBaseResource).equalsIgnoreCase("Person")) {
			throw new ForbiddenOperationException("Cannot modify Person links when EMPI is enabled");
		}
	}


	private String extractResourceType(IBaseResource theResource) {
		return myFhirContext.getResourceDefinition(theResource).getName();
	}
}
