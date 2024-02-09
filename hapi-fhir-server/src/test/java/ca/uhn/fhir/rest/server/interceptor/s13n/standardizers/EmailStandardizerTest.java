package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailStandardizerTest {

	@Test
	public void testStandardization() {
		IStandardizer std = new EmailStandardizer();
		assertThat(std.standardize("  ThisIs_aFancy\n @email.com   \t")).isEqualTo("thisis_afancy@email.com");
		assertThat(std.standardize("\t емайл@мAйлсервер.ком")).isEqualTo("емайл@мaйлсервер.ком");
		assertThat(std.standardize("show . m e . t he@Moneycom")).isEqualTo("show.me.the@moneycom");
	}

}
