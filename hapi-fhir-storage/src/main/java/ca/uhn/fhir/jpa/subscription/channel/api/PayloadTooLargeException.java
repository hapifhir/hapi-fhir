package ca.uhn.fhir.jpa.subscription.channel.api;

/**
 * This exception represents the message payload exceeded the maximum message size of the broker. Used as a wrapper of
 * similar exceptions specific to different message brokers, e.g. kafka.common.errors.RecordTooLargeException.
 */
public class PayloadTooLargeException extends RuntimeException {

    public PayloadTooLargeException(String theMessage){
        super(theMessage);
    }

    public PayloadTooLargeException(String theMessage, Throwable theThrowable) {
        super(theMessage, theThrowable);
    }
}
