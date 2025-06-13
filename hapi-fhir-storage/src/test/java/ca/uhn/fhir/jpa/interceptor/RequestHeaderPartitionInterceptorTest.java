package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.RequestDetailsHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestHeaderPartitionInterceptorTest {

    private static final Integer DEFAULT_PARTITION_ID = 100;
    private static final String VALID_PARTITION_HEADER = "123,456";
    private static final String SINGLE_PARTITION_HEADER = "123";

    private final PartitionSettings myPartitionSettings = new PartitionSettings().setDefaultPartitionId(DEFAULT_PARTITION_ID);
    private final RequestDetails myRequestDetails = RequestDetailsHelper.newServletRequestDetails();
    private final SystemRequestDetails mySrd = new SystemRequestDetails();
    private RequestHeaderPartitionInterceptor myInterceptor = new RequestHeaderPartitionInterceptor(myPartitionSettings);

    @Test
    public void testIdentifyPartitionForCreate_WithValidHeader() {
        // Setup
        myRequestDetails.addHeader(Constants.HEADER_X_REQUEST_PARTITION_IDS, SINGLE_PARTITION_HEADER);

        // Execute
        RequestPartitionId result = myInterceptor.identifyPartitionForCreate(myRequestDetails);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getPartitionIds().size());
        assertEquals(123, result.getFirstPartitionIdOrNull());
    }

    @Test
    public void testIdentifyPartitionForRead_WithValidHeader() {
        // Setup
        myRequestDetails.addHeader(Constants.HEADER_X_REQUEST_PARTITION_IDS, VALID_PARTITION_HEADER);

        // Execute
        RequestPartitionId result = myInterceptor.identifyPartitionForRead(myRequestDetails);

        // Verify
        assertNotNull(result);
        assertEquals(2, result.getPartitionIds().size());
        assertTrue(result.getPartitionIds().contains(123));
        assertTrue(result.getPartitionIds().contains(456));
    }

    @Test
    public void testIdentifyPartition_WithBlankHeaderAndSystemRequestDetailsWithPartitionId() {
        // Setup
        mySrd.addHeader(Constants.HEADER_X_REQUEST_PARTITION_IDS, "");
        RequestPartitionId existingPartitionId = RequestPartitionId.fromPartitionId(789);
        mySrd.setRequestPartitionId(existingPartitionId);

        // Execute
        RequestPartitionId result = myInterceptor.identifyPartitionForRead(mySrd);

        // Verify
        assertNotNull(result);
        assertEquals(existingPartitionId, result);
    }

    @Test
    public void testIdentifyPartition_WithBlankHeaderAndSystemRequestDetailsWithoutPartitionId() {
        // Setup
        mySrd.addHeader(Constants.HEADER_X_REQUEST_PARTITION_IDS, "");
        mySrd.setRequestPartitionId(null);

        // Execute
        RequestPartitionId result = myInterceptor.identifyPartitionForRead(mySrd);

        // Verify
        assertNotNull(result);
        assertEquals(DEFAULT_PARTITION_ID, result.getFirstPartitionIdOrNull());
    }

    @Test
    public void testIdentifyPartition_WithBlankHeaderAndNonSystemRequestDetails() {
        // Setup
        myRequestDetails.addHeader(Constants.HEADER_X_REQUEST_PARTITION_IDS, "");

        // Execute and Verify
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            myInterceptor.identifyPartitionForRead(myRequestDetails);
        });

        assertEquals(Msg.code(2642) + String.format(
                "%s header is missing or blank, it is required to identify the storage partition",
                Constants.HEADER_X_REQUEST_PARTITION_IDS), exception.getMessage());
    }

    @Test
    public void testIdentifyPartition_WithNullHeaderAndNonSystemRequestDetails() {
        // Setup
        // No need to set header, it will be null by default

        // Execute and Verify
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            myInterceptor.identifyPartitionForRead(myRequestDetails);
        });

        assertEquals(Msg.code(2642) + String.format(
                "%s header is missing or blank, it is required to identify the storage partition",
                Constants.HEADER_X_REQUEST_PARTITION_IDS), exception.getMessage());
    }
}
