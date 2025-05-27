package ca.uhn.fhir.broker.jms;

import ca.uhn.fhir.broker.TestMessageListenerWithLatch;
import ca.uhn.fhir.broker.api.RawStringMessage;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpringMessagingMessageHandlerAdapterTest {

    @RegisterExtension
    private final LogbackTestExtension myLogCaptor = new LogbackTestExtension(SpringMessagingMessageHandlerAdapter.class);

    private final TestMessageListenerWithLatch<RawStringMessage, String> myRawMessageListener = new TestMessageListenerWithLatch<>(RawStringMessage.class, String.class);;
	private final SpringMessagingMessageHandlerAdapter<String> myRawAdapter = new SpringMessagingMessageHandlerAdapter<>(RawStringMessage.class, myRawMessageListener);

    private final TestMessageListenerWithLatch<CustomIMessageAndMessage, String> myCustomMessageListener = new TestMessageListenerWithLatch<>(CustomIMessageAndMessage.class, String.class);;
    private final SpringMessagingMessageHandlerAdapter<String> myCustomAdapter = new SpringMessagingMessageHandlerAdapter<>(CustomIMessageAndMessage.class, myCustomMessageListener);

    @AfterEach
    public void after() {
        myRawMessageListener.clear();
		myCustomMessageListener.clear();
        myLogCaptor.clearEvents();
    }

    @Test
    public void testHandleGenericMessage() {
        // Setup
        String payload = "test payload";
        Map<String, Object> headers = new HashMap<>();
        headers.put("testHeader", "testValue");
        GenericMessage<String> message = new GenericMessage<>(payload, headers);
        myRawMessageListener.setExpectedCount(1);

        // Execute
        myRawAdapter.handleMessage(message);

        // Verify
        assertEquals(1, myRawMessageListener.getReceivedMessages().size());
        RawStringMessage receivedMessage = myRawMessageListener.getReceivedMessages().get(0);
        assertEquals(payload, receivedMessage.getPayload());
        assertEquals("testValue", receivedMessage.getHeaders().get("testHeader"));

        // Verify no warning logs were generated
        LogbackTestExtensionAssert.assertThat(myLogCaptor)
            .doesNotHaveMessage("Received unexpected message type");
    }

    @Test
    public void testHandleIMessage() {
        // Setup
        String payload = "test payload";
        Map<String, Object> headers = new HashMap<>();
        headers.put("testHeader", "testValue");
        RawStringMessage message = new RawStringMessage(payload, headers);
        myRawMessageListener.setExpectedCount(1);

        // Execute
        myRawAdapter.handleMessage(message);

        // Verify
        assertEquals(1, myRawMessageListener.getReceivedMessages().size());
        RawStringMessage receivedMessage = myRawMessageListener.getReceivedMessages().get(0);
        assertEquals(payload, receivedMessage.getPayload());
        assertEquals("testValue", receivedMessage.getHeaders().get("testHeader"));

        // Verify no warning logs were generated
        LogbackTestExtensionAssert.assertThat(myLogCaptor)
            .doesNotHaveMessage("Received unexpected message type");
    }

    @Test
    public void testHandleUnexpectedMessageType() {
        // Setup
        // Create a message that is not a GenericMessage or IMessage
        Message<Object> message = new UnexpectedMessageType("test payload");

        // Execute
        myRawAdapter.handleMessage(message);

        // Verify
        assertTrue(myRawMessageListener.getReceivedMessages().isEmpty());
        LogbackTestExtensionAssert.assertThat(myLogCaptor)
            .hasWarnMessage("Received unexpected message type. Expecting message of type interface ca.uhn.fhir.rest.server.messaging.IMessage, but received message of type class ca.uhn.fhir.broker.jms.SpringMessagingMessageHandlerAdapterTest$UnexpectedMessageType. Skipping message.");
    }

    // Custom message class for testing unexpected message type
    private static class UnexpectedMessageType implements Message<Object> {
        private final Object myPayload;

        public UnexpectedMessageType(Object payload) {
            myPayload = payload;
        }

        @Override
        public Object getPayload() {
            return myPayload;
        }

        @Override
        public org.springframework.messaging.MessageHeaders getHeaders() {
            return new org.springframework.messaging.MessageHeaders(new HashMap<>());
        }
    }

    // Custom message class that implements both Message and IMessage but is not a GenericMessage
    private static class CustomIMessageAndMessage implements Message<String>, IMessage<String> {
        private final String myPayload;
        private final Map<String, Object> myHeadersMap;
        private final org.springframework.messaging.MessageHeaders myHeaders;

        public CustomIMessageAndMessage(String payload, Map<String, Object> headers) {
            myPayload = payload;
            myHeadersMap = headers;
            myHeaders = new org.springframework.messaging.MessageHeaders(headers);
        }

        @Override
        public String getPayload() {
            return myPayload;
        }

		@Override
		public MessageHeaders getHeaders() {
			return myHeaders;
		}


	}

    // Define a custom class that is both a Message and an IMessage but not a GenericMessage
    private static class CustomMessageAndIMessage extends UnexpectedMessageType implements IMessage<Object> {
        private final Map<String, Object> myHeadersMap;

        public CustomMessageAndIMessage(Object payload, Map<String, Object> headers) {
            super(payload);
            myHeadersMap = headers;
        }
    }

    // Define a custom class that implements Message and is also an IMessage
    private static class MessageThatIsAlsoIMessage implements Message<String> {
        private final String myPayload;
        private final Map<String, Object> myHeaders;

        public MessageThatIsAlsoIMessage(String payload, Map<String, Object> headers) {
            myPayload = payload;
            myHeaders = headers;
        }

        @Override
        public String getPayload() {
            return myPayload;
        }

        @Override
        public MessageHeaders getHeaders() {
            return new MessageHeaders(myHeaders);
        }
    }

    // Define a custom IMessage class for testing
    private static class DifferentMessageType implements IMessage<String> {
        @Override
        public Map<String, Object> getHeaders() {
            return new HashMap<>();
        }

        @Override
        public String getPayload() {
            return "test";
        }
    }

    // Define a custom message class that implements both Message and IMessage interfaces
    private static class CustomMessage implements Message<String>, IMessage<String> {
        private final String myPayload;
        private final Map<String, Object> myHeaders;

        public CustomMessage(String payload, Map<String, Object> headers) {
            myPayload = payload;
            myHeaders = headers;
        }

        @Override
        public String getPayload() {
            return myPayload;
        }

        @Override
        public org.springframework.messaging.MessageHeaders getHeaders() {
            return new org.springframework.messaging.MessageHeaders(myHeaders);
        }
    }

    // Define another custom IMessage implementation for testing the IMessage branch
    private static class CustomIMessage implements IMessage<String> {
        private final String myPayload;
        private final Map<String, Object> myHeaders;

        public CustomIMessage(String payload, Map<String, Object> headers) {
            myPayload = payload;
            myHeaders = headers;
        }

        @Override
        public Map<String, Object> getHeaders() {
            return myHeaders;
        }

        @Override
        public String getPayload() {
            return myPayload;
        }
    }

    @Test
    public void testHandleCustomIMessageAndMessage() {
        // Setup
        String payload = "custom payload";
        Map<String, Object> headers = new HashMap<>();
        headers.put("customHeader", "customValue");
        CustomIMessageAndMessage message = new CustomIMessageAndMessage(payload, headers);
        myCustomMessageListener.setExpectedCount(1);

        // Execute
        myCustomAdapter.handleMessage(message);

        // Verify
        assertEquals(1, myCustomMessageListener.getReceivedMessages().size());
		CustomIMessageAndMessage receivedMessage = myCustomMessageListener.getReceivedMessages().get(0);
        assertEquals(payload, receivedMessage.getPayload());
        assertEquals("customValue", receivedMessage.getHeaders().get("customHeader"));

        // Verify no warning logs were generated
        LogbackTestExtensionAssert.assertThat(myLogCaptor)
            .doesNotHaveMessage("Received unexpected message type");
    }

    @Test
    public void testHandleIncorrectMessageType() {
        // Setup
        // Create a new adapter that expects a different message type
        TestMessageListenerWithLatch<DifferentMessageType, String> differentMessageListener = 
            new TestMessageListenerWithLatch<>(DifferentMessageType.class, String.class);
        SpringMessagingMessageHandlerAdapter<String> differentAdapter = 
            new SpringMessagingMessageHandlerAdapter<>(DifferentMessageType.class, differentMessageListener);

        // Create a RawStringMessage (which is not the expected DifferentMessageType)
        RawStringMessage message = new RawStringMessage("test payload");

        // Execute
        differentAdapter.handleMessage(message);

        // Verify
        assertTrue(differentMessageListener.getReceivedMessages().isEmpty());
        LogbackTestExtensionAssert.assertThat(myLogCaptor)
            .hasWarnMessage("Received unexpected message type. Expecting message of type class ca.uhn.fhir.broker.jms.SpringMessagingMessageHandlerAdapterTest$DifferentMessageType, but received message of type class ca.uhn.fhir.broker.api.RawStringMessage. Skipping message.");
    }
}
