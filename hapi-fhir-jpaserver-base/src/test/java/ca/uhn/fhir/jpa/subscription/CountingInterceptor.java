package ca.uhn.fhir.jpa.subscription;

import jdk.internal.joptsimple.internal.Strings;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import java.util.ArrayList;
import java.util.List;

public class CountingInterceptor implements ChannelInterceptor {

	private List<String> mySent = new ArrayList<>();

	public int getSentCount() {
		return mySent.size();
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		if (sent) {
			mySent.add(message.toString());
		}
	}

	@Override
	public String toString() {
		return "[" + Strings.join(mySent, "\n") + "]";
	}
}
