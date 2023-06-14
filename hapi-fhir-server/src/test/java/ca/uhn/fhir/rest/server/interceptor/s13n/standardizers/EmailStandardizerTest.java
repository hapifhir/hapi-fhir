package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EmailStandardizerTest {

    @Test
    public void testStandardization() {
        IStandardizer std = new EmailStandardizer();
        assertEquals(
                "thisis_afancy@email.com", std.standardize("  ThisIs_aFancy\n @email.com   \t"));
        assertEquals("емайл@мaйлсервер.ком", std.standardize("\t емайл@мAйлсервер.ком"));
        assertEquals("show.me.the@moneycom", std.standardize("show . m e . t he@Moneycom"));
    }
}
