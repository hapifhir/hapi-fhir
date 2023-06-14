package ca.uhn.fhir.jpa.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class TermCodeSystemTest {

    @Test
    public void testEquals() {
        TermCodeSystem cs1 = new TermCodeSystem().setCodeSystemUri("http://foo");
        TermCodeSystem cs2 = new TermCodeSystem().setCodeSystemUri("http://foo");
        TermCodeSystem cs3 = new TermCodeSystem().setCodeSystemUri("http://foo2");
        assertEquals(cs1, cs2);
        assertNotEquals(cs1, cs3);
        assertNotEquals(cs1, null);
        assertNotEquals(cs1, "");
    }

    @Test
    public void testHashCode() {
        TermCodeSystem cs = new TermCodeSystem().setCodeSystemUri("http://foo");
        assertEquals(155243497, cs.hashCode());
    }
}
