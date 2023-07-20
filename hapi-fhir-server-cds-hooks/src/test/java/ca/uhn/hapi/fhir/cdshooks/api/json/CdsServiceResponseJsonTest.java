package ca.uhn.hapi.fhir.cdshooks.api.json;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CdsServiceResponseJsonTest {
    private final CdsServiceResponseJson fixture = new CdsServiceResponseJson();

    @Test
    void testAddCard() {
        //setup
        final CdsServiceResponseCardJson expected = new CdsServiceResponseCardJson();
        fixture.addCard(expected);
        //execute
        final List<CdsServiceResponseCardJson> actual = fixture.getCards();
        //validate
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    void testGetCardsNotNull() {
        //execute
        final List<CdsServiceResponseCardJson> actual = fixture.getCards();
        //validate
        assertNotNull(actual);
    }
}
