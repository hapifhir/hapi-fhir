package ca.uhn.fhir.jpa.subscription.module.cache;

import org.springframework.integration.support.management.SubscribableChannelManagement;
import org.springframework.messaging.SubscribableChannel;

public interface ISubscribableChannel extends SubscribableChannel, SubscribableChannelManagement {
}
