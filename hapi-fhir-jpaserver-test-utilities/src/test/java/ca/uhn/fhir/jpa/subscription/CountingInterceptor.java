package ca.uhn.fhir.jpa.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.ArrayList;
import java.util.List;

public class CountingInterceptor implements ChannelInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(CountingInterceptor.class);
	private List<String> mySent = new ArrayList<>();

	public int getSentCount(String theContainingKeyword) {
		return (int) mySent.stream().filter(t -> t.contains(theContainingKeyword)).count();
	}

	@Override
	public void afterSendCompletion(Message<?> theMessage, MessageChannel theChannel, boolean theSent, Exception theException) {
		ourLog.info("Counting another instance: {}", theMessage);
		if (theSent) {
			mySent.add(theMessage.toString());
		}
	}

	@Override
	public String toString() {
		return "[" + String.join("\n", mySent) + "]";
	}
}
