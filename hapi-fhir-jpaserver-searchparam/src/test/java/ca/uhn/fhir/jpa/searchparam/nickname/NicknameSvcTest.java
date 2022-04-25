package ca.uhn.fhir.jpa.searchparam.nickname;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class NicknameSvcTest {
	@Autowired
	NicknameSvc myNicknameSvc;

	@Configuration
	public static class SpringConfig {
		@Bean
		NicknameSvc nicknameSvc () {
			return new NicknameSvc();
		}
	}

	@Test
	public void testReadfile() {
		assertEquals(10, myNicknameSvc.size());
	}
}
