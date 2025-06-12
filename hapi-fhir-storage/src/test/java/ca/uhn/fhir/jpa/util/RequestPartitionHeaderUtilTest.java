package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.rest.server.messaging.RequestPartitionHeaderUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.ALL_PARTITIONS_TENANT_NAME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.DEFAULT_PARTITION_NAME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link RequestPartitionHeaderUtil}
 */
class RequestPartitionHeaderUtilTest {

	private static final Integer DEFAULT_PARTITION_ID = 729;
    private static final PartitionSettings ourPartitionSettings = new PartitionSettings().setDefaultPartitionId(DEFAULT_PARTITION_ID);

    @Test
    void fromHeader_nullHeader_returnsNull() {
        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(null, ourPartitionSettings);
        assertNull(result);
    }

    @Test
    void fromHeader_defaultPartition_returnsDefaultPartitionId() {
        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(DEFAULT_PARTITION_NAME, ourPartitionSettings);

        assertNotNull(result);
        assertEquals(1, result.getPartitionIds().size());
        assertEquals(DEFAULT_PARTITION_ID, result.getFirstPartitionIdOrNull());
        assertTrue(result.isDefaultPartition(DEFAULT_PARTITION_ID));
		assertTrue(ourPartitionSettings.isDefaultPartition(result));
    }

    @Test
    void fromHeader_allPartitions_returnsAllPartitionsObject() {
        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(ALL_PARTITIONS_TENANT_NAME, ourPartitionSettings);

        assertNotNull(result);
        assertTrue(result.isAllPartitions());
    }

	@Test
	void fromHeader_allPartitionsWithOthersAfter_returnsAllPartitionsObject() {
		String partitionIds = ALL_PARTITIONS_TENANT_NAME + ",789,101";
		RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(partitionIds, ourPartitionSettings);

		assertNotNull(result);
		assertTrue(result.isAllPartitions());
	}

	@Test
	void fromHeader_allPartitionsWithOthersBefore_returnsAllPartitionsObject() {
		String partitionIds =  "789,101," + ALL_PARTITIONS_TENANT_NAME;
		RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(partitionIds, ourPartitionSettings);

		assertNotNull(result);
		assertTrue(result.isAllPartitions());
	}

    @Test
    void fromHeader_singlePartition_returnsCorrectPartitionId() {
        String partitionId = "456";
        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(partitionId, ourPartitionSettings);

        assertNotNull(result);
        assertEquals(1, result.getPartitionIds().size());
        assertEquals(Integer.parseInt(partitionId), result.getFirstPartitionIdOrNull());
    }

    @Test
    void fromHeader_multiplePartitions_returnsAllPartitionIds() {
        String partitionIds = "456,789,101";
        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(partitionIds, ourPartitionSettings);

        assertNotNull(result);
        assertEquals(3, result.getPartitionIds().size());
        assertTrue(result.getPartitionIds().contains(456));
        assertTrue(result.getPartitionIds().contains(789));
        assertTrue(result.getPartitionIds().contains(101));
    }

    @Test
    void fromHeader_mixedPartitions_handlesDefaultPartition() {
        String partitionIds = "456," + DEFAULT_PARTITION_NAME + ",789";
        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(partitionIds, ourPartitionSettings);

        assertNotNull(result);
        assertEquals(3, result.getPartitionIds().size());
        assertTrue(result.getPartitionIds().contains(456));
        assertTrue(result.getPartitionIds().contains(DEFAULT_PARTITION_ID));
        assertTrue(result.getPartitionIds().contains(789));
    }

    @Test
    void fromHeaderFirstPartitionOnly_multiplePartitions_returnsOnlyFirstPartition() {
        String partitionIds = "456,789,101";
        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeaderFirstPartitionOnly(partitionIds, ourPartitionSettings);

        assertNotNull(result);
        assertEquals(1, result.getPartitionIds().size());
        assertEquals(456, result.getFirstPartitionIdOrNull());
    }

    @Test
    void fromHeaderFirstPartitionOnly_defaultPartition_returnsDefaultPartitionId() {
        String partitionIds = DEFAULT_PARTITION_NAME + ",789,101";
        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeaderFirstPartitionOnly(partitionIds, ourPartitionSettings);

        assertNotNull(result);
        assertEquals(1, result.getPartitionIds().size());
        assertEquals(DEFAULT_PARTITION_ID, result.getFirstPartitionIdOrNull());
		assertTrue(ourPartitionSettings.isDefaultPartition(result));
	}

    @Test
    void fromHeaderFirstPartitionOnly_allPartitions_returnsAllPartitionsObject() {
        String partitionIds = ALL_PARTITIONS_TENANT_NAME + ",789,101";
        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeaderFirstPartitionOnly(partitionIds, ourPartitionSettings);

        assertNotNull(result);
        assertTrue(result.isAllPartitions());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "DEFAULT", "_ALL", "123,456"})
    void validateHeader_validValues_doesNotThrowException(String headerValue) {
        RequestPartitionHeaderUtil.validateHeader(headerValue);
    }

    @Test
    void validateHeader_invalidPartitionId_throwsInvalidRequestException() {
        String invalidPartitionId = "not-a-number";

        assertThatThrownBy(() -> RequestPartitionHeaderUtil.validateHeader(invalidPartitionId))
            .isInstanceOf(InvalidRequestException.class)
            .hasMessageContaining("Invalid partition ID");
    }

    @Test
    void validateHeader_emptyHeader_throwsInvalidRequestException() {
        String emptyHeader = ",,,";

        assertThatThrownBy(() -> RequestPartitionHeaderUtil.validateHeader(emptyHeader))
            .isInstanceOf(InvalidRequestException.class)
            .hasMessageContaining("No partition IDs provided");
    }

    @Test
    void fromHeader_withNullDefaultPartitionId_handlesDefaultPartitionCorrectly() {
        // Create settings with null default partition ID
        PartitionSettings settings = new PartitionSettings();
        settings.setDefaultPartitionId(null);

        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(DEFAULT_PARTITION_NAME, settings);

        assertNotNull(result);
        assertEquals(1, result.getPartitionIds().size());
        assertNull(result.getFirstPartitionIdOrNull());
    }

    @Test
    void fromHeader_withWhitespace_trimsValues() {
        String partitionIds = " 456 , 789 , 101 ";
        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(partitionIds, ourPartitionSettings);

        assertNotNull(result);
        assertEquals(3, result.getPartitionIds().size());
        assertTrue(result.getPartitionIds().contains(456));
        assertTrue(result.getPartitionIds().contains(789));
        assertTrue(result.getPartitionIds().contains(101));
    }

    @Test
    void fromHeader_defaultPartitionWithNullSettings_throwsException() {
        // Test case where DEFAULT partition is used but no default partition settings are provided
        assertThatThrownBy(() -> RequestPartitionHeaderUtil.fromHeader(DEFAULT_PARTITION_NAME, null))
            .isInstanceOf(InvalidRequestException.class)
            .hasMessageContaining("Can only use DEFAULT partitionId in contexts where the default partition ID is defined");
    }

    @Test
    void fromHeader_withSourceName_parsesCorrectly() {
        String sourceName = "test-source";
        String partitionId = "456";

        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeader(sourceName, partitionId, ourPartitionSettings);

        assertNotNull(result);
        assertEquals(1, result.getPartitionIds().size());
        assertEquals(Integer.parseInt(partitionId), result.getFirstPartitionIdOrNull());
    }

    @Test
    void fromHeaderFirstPartitionOnly_withSourceName_parsesCorrectly() {
        String sourceName = "test-source";
        String partitionIds = "456,789,101";

        RequestPartitionId result = RequestPartitionHeaderUtil.fromHeaderFirstPartitionOnly(sourceName, partitionIds, ourPartitionSettings);

        assertNotNull(result);
        assertEquals(1, result.getPartitionIds().size());
        assertEquals(456, result.getFirstPartitionIdOrNull());
    }

    @Test
    void validateHeader_withSourceName_validatesCorrectly() {
        String sourceName = "test-source";
        String validPartitionId = "123";

        RequestPartitionHeaderUtil.validateHeader(sourceName, validPartitionId);

        String invalidPartitionId = "not-a-number";
        assertThatThrownBy(() -> RequestPartitionHeaderUtil.validateHeader(sourceName, invalidPartitionId))
            .isInstanceOf(InvalidRequestException.class)
            .hasMessageContaining("Invalid partition ID")
            .hasMessageContaining(sourceName);
    }

    private static class TestResourceMessage extends BaseResourceMessage {
        private RequestPartitionId myPartitionId;
        private IIdType myPayloadId;
        private String myPayloadIdString;

        @Override
        public RequestPartitionId getPartitionId() {
            return myPartitionId;
        }

        @Override
        public void setPartitionId(RequestPartitionId thePartitionId) {
            myPartitionId = thePartitionId;
        }

        @Override
        public void setPayloadId(IIdType theResourceId) {
            myPayloadId = theResourceId;
            myPayloadIdString = theResourceId != null ? theResourceId.getValue() : null;
        }

        @Override
        public String getPayloadId() {
            return myPayloadIdString;
        }
    }

    private static class TestMessage<T> implements IMessage<T> {
        private final T myPayload;
        private final Map<String, Object> myHeaders = new HashMap<>();

        public TestMessage(T thePayload) {
            myPayload = thePayload;
        }

        public void addHeader(String theKey, Object theValue) {
            myHeaders.put(theKey, theValue);
        }

        @Override
        @Nonnull
        public Map<String, Object> getHeaders() {
            return myHeaders;
        }

        @Override
        public T getPayload() {
            return myPayload;
        }
    }

    @Test
    void setRequestPartitionIdFromHeaderIfNotAlreadySet_partitionIdAlreadySet_doesNotChange() {
        // Setup
        TestResourceMessage resourceMessage = new TestResourceMessage();
        RequestPartitionId existingPartitionId = RequestPartitionId.fromPartitionId(123);
        resourceMessage.setPartitionId(existingPartitionId);

        TestMessage<TestResourceMessage> message = new TestMessage<>(resourceMessage);
        message.addHeader(Constants.HEADER_X_REQUEST_PARTITION_IDS, "456");

        // Execute
        RequestPartitionHeaderUtil.setRequestPartitionIdFromHeaderIfNotAlreadySet(message, ourPartitionSettings);

        // Verify
        assertEquals(existingPartitionId, resourceMessage.getPartitionId());
    }

    @Test
    void setRequestPartitionIdFromHeaderIfNotAlreadySet_partitionIdNotSetHeaderPresent_setsFromHeader() {
        // Setup
        TestResourceMessage resourceMessage = new TestResourceMessage();

        TestMessage<TestResourceMessage> message = new TestMessage<>(resourceMessage);
        String headerValue = "456";
        message.addHeader(Constants.HEADER_X_REQUEST_PARTITION_IDS, headerValue);

        // Execute
        RequestPartitionHeaderUtil.setRequestPartitionIdFromHeaderIfNotAlreadySet(message, ourPartitionSettings);

        // Verify
        assertNotNull(resourceMessage.getPartitionId());
        assertEquals(456, resourceMessage.getPartitionId().getFirstPartitionIdOrNull());
    }

    @Test
    void setRequestPartitionIdFromHeaderIfNotAlreadySet_partitionIdNotSetHeaderNotPresent_remainsNull() {
        // Setup
        TestResourceMessage resourceMessage = new TestResourceMessage();

        TestMessage<TestResourceMessage> message = new TestMessage<>(resourceMessage);
        // No header added - header will be empty

        // Execute
        RequestPartitionHeaderUtil.setRequestPartitionIdFromHeaderIfNotAlreadySet(message, ourPartitionSettings);

        // Verify
        assertNull(resourceMessage.getPartitionId());
    }

    @Test
    void setRequestPartitionIdFromHeaderIfNotAlreadySet_payloadNotBaseResourceMessage_doesNothing() {
        // Setup
        Object nonResourceMessage = new Object();

        TestMessage<Object> message = new TestMessage<>(nonResourceMessage);

        // Execute - should not throw exception
        RequestPartitionHeaderUtil.setRequestPartitionIdFromHeaderIfNotAlreadySet(message, ourPartitionSettings);
    }
}
