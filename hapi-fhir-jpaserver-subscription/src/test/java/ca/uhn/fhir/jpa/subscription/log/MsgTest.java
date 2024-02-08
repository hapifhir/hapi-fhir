package ca.uhn.fhir.jpa.subscription.log;

import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MsgTest {
	@Test
	public void testCode() {
		assertThat(Msg.code(73)).isEqualTo("HAPI-0073: ");
		assertThat(Msg.code(973)).isEqualTo("HAPI-0973: ");
	}
}
