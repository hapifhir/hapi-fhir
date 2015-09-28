package ca.uhn.fhir.jpa.subscription;

import org.springframework.beans.factory.FactoryBean;

public class SubscriptionWebsocketHandlerFactory implements FactoryBean<ISubscriptionWebsocketHandler> {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionWebsocketHandler.class);

	@Override
	public ISubscriptionWebsocketHandler getObject() throws Exception {
		return new SubscriptionWebsocketHandler();
	}

	@Override
	public Class<ISubscriptionWebsocketHandler> getObjectType() {
		return ISubscriptionWebsocketHandler.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
