package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.util.CloseUtil;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class MultiplexingListener<T> implements IMessageListener<T>, AutoCloseable {
	private static final Logger ourLog = LoggerFactory.getLogger(MultiplexingListener.class);
	private final List<IMessageListener<T>> mySubListeners = new LinkedList<>();

	@Override
	public void handleMessage(IMessage<T> theMessage) {
		mySubListeners.forEach(listener -> listener.handleMessage(theMessage));
	}

	public boolean addListener(IMessageListener<T> theListener) {
		return mySubListeners.add(theListener);
	}

	public boolean removeListener(IMessageListener<T> theListener) {
		return mySubListeners.remove(theListener);
	}

	@Override
	public void close() {
		mySubListeners.forEach(CloseUtil::close);
		mySubListeners.clear();
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
