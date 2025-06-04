package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.ALL_PARTITIONS_TENANT_NAME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.DEFAULT_PARTITION_NAME;
import static org.junit.jupiter.api.Assertions.*;

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
        assertDoesNotThrow(() -> RequestPartitionHeaderUtil.validateHeader(headerValue));
    }

    @Test
    void validateHeader_invalidPartitionId_throwsInvalidRequestException() {
        String invalidPartitionId = "not-a-number";
        
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, 
            () -> RequestPartitionHeaderUtil.validateHeader(invalidPartitionId));
        
        assertTrue(exception.getMessage().contains("Invalid partition ID"));
    }

    @Test
    void validateHeader_emptyHeader_throwsInvalidRequestException() {
        String emptyHeader = ",,,";
        
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, 
            () -> RequestPartitionHeaderUtil.validateHeader(emptyHeader));
        
        assertTrue(exception.getMessage().contains("No partition IDs provided"));
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
}
