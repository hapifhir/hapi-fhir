package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.BrokerListenerClosedException;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.util.CloseUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class MultiplexingListener<T> implements IMessageListener<T>, AutoCloseable {
	private static final Logger ourLog = LoggerFactory.getLogger(MultiplexingListener.class);
	private final List<IMessageListener<T>> mySubListeners = new LinkedList<>();

	private final Class<T> myPayloadType;
	private boolean myClosed = false;

	public MultiplexingListener(Class<T> thePayloadType) {
		myPayloadType = thePayloadType;
	}

	@Override
	public void handleMessage(IMessage<T> theMessage) {
		validateNotClosed();

		Class<?> messageClass = theMessage.getPayload().getClass();
		if (!getPayloadType().isAssignableFrom(messageClass)) {
			throw new InternalErrorException("Expecting message of type " + getPayloadType()
					+ ". But received message of type: " + messageClass);
		}
		mySubListeners.forEach(listener -> listener.handleMessage(theMessage));
	}

	private void validateNotClosed() {
		if (myClosed) {
			throw new BrokerListenerClosedException("Attempted to use a closed MultiplexingListener.");
		}
	}

	@Override
	public Class<T> getPayloadType() {
		return myPayloadType;
	}

	public boolean addListener(IMessageListener<T> theListener) {
		validateNotClosed();

		if (!getPayloadType().isAssignableFrom(theListener.getPayloadType())) {
			throw new InternalErrorException("Expecting listener of type " + getPayloadType()
					+ ". But listener was for type: " + theListener.getPayloadType());
		}
		return mySubListeners.add(theListener);
	}

	public boolean removeListener(IMessageListener<T> theListener) {
		return mySubListeners.remove(theListener);
	}

	@Override
	public void close() {
		mySubListeners.forEach(CloseUtil::close);
		mySubListeners.clear();
		myClosed = true;
	}

	public <L extends IMessageListener<T>> L getListenerOfTypeOrNull(Class<L> theMessageListenerClass) {
		for (IMessageListener<T> next : mySubListeners) {
			if (theMessageListenerClass.isAssignableFrom(next.getClass())) {
				return (L) next;
			}
		}
		return null;
	}
}
