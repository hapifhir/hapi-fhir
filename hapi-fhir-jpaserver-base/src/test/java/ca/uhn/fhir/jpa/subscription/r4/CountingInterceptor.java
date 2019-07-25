package ca.uhn.fhir.jpa.subscription.r4;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

public class CountingInterceptor extends ChannelInterceptorAdapter {

	private int mySentCount;

	public int getSentCount() {
		return mySentCount;
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		if (sent) {
			mySentCount++;
		}
	}
}
