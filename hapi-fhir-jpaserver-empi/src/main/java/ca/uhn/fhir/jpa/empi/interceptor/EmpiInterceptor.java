package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.empi.api.IEmpiInterceptor;
import ca.uhn.fhir.empi.rules.config.IEmpiConfig;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.interceptor.BaseResourceModifiedInterceptor;
import ca.uhn.fhir.jpa.model.message.ISubscribableChannelFactory;
import ca.uhn.fhir.jpa.model.message.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Streams;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Lazy
@Service
public class EmpiInterceptor extends BaseResourceModifiedInterceptor implements IEmpiInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiInterceptor.class);
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private ISubscribableChannelFactory mySubscribableChannelFactory;
	@Autowired
	private EmpiMatchingSubscriber myEmpiMatchingSubscriber;
	@Autowired
	private IEmpiConfig myEmpiConfig;

	@Override
	protected String getMatchingChannelName() {
		return IEmpiConfig.EMPI_MATCHING_CHANNEL_NAME;
	}

	@Override
	protected MessageHandler getSubscriber() {
		return myEmpiMatchingSubscriber;
	}

	@Override
	// FIXME KHS rename this method (after James has merged)
	protected SubscribableChannel createMatchingChannel() {
		return mySubscribableChannelFactory.createSubscribableChannel(IEmpiConfig.EMPI_MATCHING_CHANNEL_NAME, ResourceModifiedMessage.class, myEmpiConfig.getConsumerCount());
	}

	@Override
	public void resourceCreated(IBaseResource theResource, RequestDetails theRequest) {
		if (EmpiUtil.supportedResourceType(extractResourceType(theResource))) {
			super.resourceCreated(theResource, theRequest);
		}
	}

	@Override
	public void resourceDeleted(IBaseResource theResource, RequestDetails theRequest) {
		if (EmpiUtil.supportedResourceType(extractResourceType(theResource))) {
			super.resourceDeleted(theResource, theRequest);
		}
	}

	@Override
	public void resourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequest) {
		if (EmpiUtil.supportedResourceType(extractResourceType(theOldResource))) {
			super.resourceUpdated(theOldResource, theNewResource, theRequest);
		}
	}

	@Nonnull
	@Override
	protected Pointcut getSubmitPointcut() {
		// FIXME EMPI add EMPI submit pointcut
		return Pointcut.SUBSCRIPTION_RESOURCE_MODIFIED;
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
		if (isInternalRequest(theRequestDetails)) {
			return;
		}
		forbidCreationOfPersonsWithLinks(theBaseResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void blockManualPersonManipulationOnUpdate(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
		if (isInternalRequest(theRequestDetails)) {
			return;
		}
		forbidModificationOnLinksPerson(theOldResource, theNewResource);
	}

	/*
	 * We assume that if we have RequestDetails, then this was an HTTP request and not an internal one.
	 */
	private boolean isInternalRequest(RequestDetails theRequestDetails) {
		return theRequestDetails == null;
	}

	/**
	 * If we find that an updated Person has some changed values in their links, we reject the incoming change.
	 * @param theOldResource
	 * @param theNewResource
	 */
	private void forbidModificationOnLinksPerson(IBaseResource theOldResource, IBaseResource theNewResource) {
		boolean linksWereModified = false;
		if (extractResourceType(theNewResource).equalsIgnoreCase("Person")) {
			Person newPerson = (Person)theNewResource;
			Person oldPerson = (Person) theOldResource;
			if (newPerson.getLink().size() != oldPerson.getLink().size()) {
				linksWereModified = true;
			}
			Stream<Boolean> linkMatches = Streams.zip(newPerson.getLink().stream(), oldPerson.getLink().stream(), Person.PersonLinkComponent::equalsDeep);

			linksWereModified |= linkMatches.anyMatch(matched -> !matched);

			if (linksWereModified) {
				throwBlockedByEmpi();
			}
		}
	}

	private void forbidCreationOfPersonsWithLinks(IBaseResource theResource) {
		if (extractResourceType(theResource).equalsIgnoreCase("Person")) {
			Person p = (Person)theResource;
			if (!p.getLink().isEmpty()) {
				throwBlockedByEmpi();
			}
		}
	}

	private void throwBlockedByEmpi(){
		throw new ForbiddenOperationException("Cannot modify Person links when EMPI is enabled.");
	}

	private String extractResourceType(IBaseResource theResource) {
		return myFhirContext.getResourceDefinition(theResource).getName();
	}

	@Override
	@VisibleForTesting
	public void stopForUnitTest() {
		this.preDestroy();
	}
}
