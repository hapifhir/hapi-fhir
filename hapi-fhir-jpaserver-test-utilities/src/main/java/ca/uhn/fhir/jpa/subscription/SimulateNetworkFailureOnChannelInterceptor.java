package ca.uhn.fhir.jpa.subscription;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.support.ChannelInterceptor;

public class SimulateNetworkFailureOnChannelInterceptor implements ChannelInterceptor {
	private boolean myIsNetworkDown = false;

	private int myFailedMessageDeliveryCount = 0;

	private int mySuccessfulMessageDeliveryCount = 0;
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		if(myIsNetworkDown){
			myFailedMessageDeliveryCount++;
			throw new MessageDeliveryException("Host unreachable");
		}

		mySuccessfulMessageDeliveryCount++;
		return message;
	}

	public void simulateNetworkOutage(){
		myIsNetworkDown = true;
	}

	public void simulateNetWorkOperational(){
		myIsNetworkDown = false;
	}

	public int getFailedMessageDeliveryCount() {
		return myFailedMessageDeliveryCount;
	}

	public int getSuccessfulMessageDeliveryCount() {
		return mySuccessfulMessageDeliveryCount;
	}

	public void resetCounters(){
		myFailedMessageDeliveryCount = 0;
		mySuccessfulMessageDeliveryCount = 0;
	}
}
