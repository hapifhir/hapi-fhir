package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import org.apache.commons.lang3.Validate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.Set;

public class BroadcastingSubscribableChannelWrapper extends AbstractSubscribableChannel implements IChannelReceiver {

    private final IChannelReceiver myWrappedChannel;
    private final MessageHandler myHandler;

    public BroadcastingSubscribableChannelWrapper(IChannelReceiver theChannel) {
        myHandler = message -> send(message);
        theChannel.subscribe(myHandler);
        myWrappedChannel = theChannel;
    }

    public SubscribableChannel getWrappedChannel() {
        return myWrappedChannel;
    }

    @Override
    protected boolean sendInternal(Message<?> theMessage, long timeout) {
        Set<MessageHandler> subscribers = getSubscribers();
        Validate.isTrue(subscribers.size() > 0, "Channel has zero subscribers");
        for (MessageHandler next : subscribers) {
            next.handleMessage(theMessage);
        }
        return true;
    }

    @Override
    public void destroy() throws Exception {
        myWrappedChannel.destroy();
        myWrappedChannel.unsubscribe(myHandler);
    }

    @Override
    public void addInterceptor(ChannelInterceptor interceptor) {
        super.addInterceptor(interceptor);
        myWrappedChannel.addInterceptor(interceptor);
    }


    @Override
    public String getName() {
        return myWrappedChannel.getName();
    }
}
