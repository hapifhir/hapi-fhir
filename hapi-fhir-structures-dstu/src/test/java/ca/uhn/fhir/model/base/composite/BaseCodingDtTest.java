package ca.uhn.fhir.model.base.composite;

import static org.junit.Assert.*;

import ca.uhn.fhir.model.dstu.composite.CodingDt;
import org.junit.Test;

/**
 * Created by Bill de Beaubien on 12/30/2014.
 */
public class BaseCodingDtTest {
    private final CodingDt myTokenWithSystem = new CodingDt("http://foo.org", "53");
    private final CodingDt myTokenWithEmptySystem = new CodingDt("", "53");
    private final CodingDt myTokenWithoutSystem = new CodingDt(null, "53");

    // [parameter]=[namespace]|[code] matches a code/value in the given system namespace
    @Test
    public void whenTokenIncludesSystem_CodingWithSameSystemAndCode_shouldMatch() {
        assertTrue(new CodingDt("http://foo.org", "53").matchesToken(myTokenWithSystem));
    }

    @Test
    public void whenTokenIncludesSystem_CodingWithDifferentSystem_shouldNotMatch() {
        assertFalse(new CodingDt("http://bar.org", "53").matchesToken(myTokenWithSystem));
    }

    @Test
    public void whenTokenIncludesSystem_CodingWithBlankSystem_shouldNotMatch() {
        assertFalse(new CodingDt("", "53").matchesToken(myTokenWithSystem));
    }

    @Test
    public void whenTokenIncludesSystem_CodingWithNoSystem_shouldNotMatch() {
        assertFalse(new CodingDt(null, "53").matchesToken(myTokenWithSystem));
    }

    @Test
    public void whenTokenIncludesSystem_CodingWithSameSystemAndDifferentCode_shouldNotMatch() {
        assertFalse(new CodingDt("http://foo.org", "11").matchesToken(myTokenWithSystem));
    }

    @Test
    public void whenTokenIncludesSystem_CodingWithSameSystemAndNoCode_shouldNotMatch() {
        assertFalse(new CodingDt("http://foo.org", null).matchesToken(myTokenWithSystem));
    }

    // [parameter]=[code] matches a code/value irrespective of it's system namespace
    @Test
    public void whenTokenIncludesNoSystem_CodingWithAnySystemAndCode_shouldMatch() {
        assertTrue(new CodingDt("http://foo.org", "53").matchesToken(myTokenWithoutSystem));
        assertTrue(new CodingDt("http://bar.org", "53").matchesToken(myTokenWithoutSystem));
        assertTrue(new CodingDt("", "53").matchesToken(myTokenWithoutSystem));
        assertTrue(new CodingDt(null, "53").matchesToken(myTokenWithoutSystem));
    }

    // [parameter]=|[code] matches a code/value that has no system namespace
    @Test
    public void whenTokenIncludesEmptySystem_CodeWithNoSystem_shouldMatch() {
        assertTrue(new CodingDt(null, "53").matchesToken(myTokenWithEmptySystem));
    }

    @Test
    public void whenTokenIncludesEmptySystem_CodeWithBlankSystem_shouldMatch() {
        assertTrue(new CodingDt("", "53").matchesToken(myTokenWithEmptySystem));
    }

    @Test
    public void whenTokenIncludesEmptySystem_CodeWithSystem_shouldNotMatch() {
        assertFalse(new CodingDt("http://bar.org", "53").matchesToken(myTokenWithEmptySystem));
    }
}
