package ca.uhn.hapi.fhir.cdshooks.api.json;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(actual).isNotNull();
			assertThat(actual).hasSize(1);
		assertThat(actual.get(0)).isEqualTo(expected);
    }

    @Test
    void testGetCardsNotNull() {
        //execute
        final List<CdsServiceResponseCardJson> actual = fixture.getCards();
		//validate
		assertThat(actual).isNotNull();
    }
}
