package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.interceptor.BaseResourceModifiedInterceptor;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.channel.ISubscribableChannelFactory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Interceptor
@Service
public class EmpiInterceptor extends BaseResourceModifiedInterceptor {
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
		// FIXME KHS
		ourLog.info(">>> Expunging all EmpiLink records");
		theCounter.addAndGet(myExpungeEverythingService.expungeEverythingByType(EmpiLink.class));
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeAllMatchedEmpiLinks(AtomicInteger theCounter, IBaseResource theResource) {
		// FIXME EMPI
	}
}
