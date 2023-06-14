package ca.uhn.fhir.rest.server.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BaseServerResponseExceptionTest {

    @Test
    public void testTrusted() {
        assertTrue(
                new InternalErrorException("aaa")
                        .setErrorMessageTrusted(true)
                        .isErrorMessageTrusted());
    }
}
