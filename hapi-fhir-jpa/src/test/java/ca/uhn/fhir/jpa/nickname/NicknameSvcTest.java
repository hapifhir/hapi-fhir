package ca.uhn.fhir.jpa.nickname;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NicknameSvcTest {
	@Test
	public void testReadfile() throws IOException {
		NicknameSvc nicknameSvc = new NicknameSvc();
		assertEquals(1082, nicknameSvc.size());
	}
}
