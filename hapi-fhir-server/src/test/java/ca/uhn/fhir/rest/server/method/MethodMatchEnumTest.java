package ca.uhn.fhir.rest.server.method;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MethodMatchEnumTest {

    @Test
    public void testOrder() {
        assertEquals(0, MethodMatchEnum.NONE.ordinal());
        assertEquals(1, MethodMatchEnum.APPROXIMATE.ordinal());
        assertEquals(2, MethodMatchEnum.EXACT.ordinal());
    }
}
