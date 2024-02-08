package ca.uhn.fhir.jpa.nickname;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class NicknameSvcTest {
	@Test
	public void testReadfile() throws IOException {
		NicknameSvc nicknameSvc = new NicknameSvc();
		assertThat(nicknameSvc.size()).isEqualTo(1082);
	}
}
