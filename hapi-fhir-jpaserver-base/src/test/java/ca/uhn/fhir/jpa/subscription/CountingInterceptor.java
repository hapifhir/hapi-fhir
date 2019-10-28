package ca.uhn.fhir.jpa.subscription;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.ArrayList;
import java.util.List;

public class CountingInterceptor implements ChannelInterceptor {

	private List<String> mySent = new ArrayList<>();

	public int getSentCount(String theContainingKeyword) {
		return (int)mySent.stream().filter(t -> t.contains(theContainingKeyword)).count();
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		if (sent) {
			mySent.add(message.toString());
		}
	}

	@Override
	public String toString() {
		return "[" + String.join("\n", mySent) + "]";
	}
}
