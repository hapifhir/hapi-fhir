package ca.uhn.fhir.broker.api;

/**
 * Messages are acknowledged on the message broker's polling timeout.
 * If polling happens while the job is still processing, it will trigger a rebalance and this will
 * throw an error (which typically ends up on the DLQ and is logged in our logs).
 * -
 * If this is a thing that's known to happen frequently (and you do not want it to),
 * implement this interface instead and the messaging broker will pause rebalancing to
 * give extra time to work (preventing duplicate work or DLQ errors).
 * -
 * NB:
 * Think very carefully about whether your job requires this interface or not.
 * If a job processes "forever", you will end up with a silent consumer that
 * never releases and blocks all messages on the same partition from ever being processed.
 * -
 * This should be used sparingly!
 */
public interface IAsyncMessageListener<T> extends IMessageListener<T> {}
